package com.larpclient;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.zip.DataFormatException;
import java.util.zip.Inflater;

public final class yg {
   public static final int d = 1280331057;
   public static final int g = 262144;
   public static final int X = 16384;
   public static final int D = 16384;
   private static final int T = 4;
   private static final int r = 8;
   private static final int V = 32;
   private static final float R = 8.0F;
   private static final float k = (float)Math.cos(Math.toRadians(40.0));
   private static final float[] C = f(0.35F, 0.85F, -0.4F);

   public static yg.e k(InputStream var0) throws IOException {
      if (var0 == null) {
         throw new IOException("Prop mesh missing");
      }

      byte[] var1 = var0.readNBytes(262145);
      if (var1.length <= 262144 && var1.length >= 8) {
         DataInputStream var2 = new DataInputStream(new ByteArrayInputStream(var1, 0, 8));
         if (var2.readInt() != 1280331057) {
            throw new IOException("Invalid prop mesh header");
         }

         int var3 = var2.readInt();
         if (var3 >= 1 && var3 <= 1048576) {
            byte[] var4 = new byte[var3];
            Inflater var5 = new Inflater();

            try {
               var5.setInput(var1, 8, var1.length - 8);
               int var6 = 0;

               while (var6 < var3 && !var5.finished()) {
                  int var7 = var5.inflate(var4, var6, var3 - var6);
                  if (var7 == 0 && (var5.needsInput() || var5.needsDictionary())) {
                     break;
                  }

                  var6 += var7;
               }

               if (var6 != var3 || !var5.finished() || var5.getRemaining() != 0) {
                  throw new IOException("Prop mesh payload is truncated or has trailing data");
               }
            } catch (DataFormatException var16) {
               throw new IOException("Prop mesh payload is corrupt", var16);
            } finally {
               var5.end();
            }

            DataInputStream var18 = new DataInputStream(new ByteArrayInputStream(var4));
            int var19 = var18.readUnsignedByte();
            if (var19 >= 1 && var19 <= 32) {
               yg.d[] var8 = new yg.d[var19];

               for (int var9 = 0; var9 < var19; var9++) {
                  int var10 = var18.readUnsignedByte();
                  if (var10 > 3 || var18.readUnsignedByte() != 0) {
                     throw new IOException("Invalid prop material");
                  }

                  var8[var9] = new yg.d(var10, T(var18.readFloat()), T(var18.readFloat()), T(var18.readFloat()), T(var18.readFloat()));
               }

               int var20 = var18.readUnsignedByte();
               if (var20 >= 1 && var20 <= 4) {
                  ArrayList var21 = new ArrayList();
                  int var11 = 0;

                  for (int var12 = 0; var12 < var20; var12++) {
                     yg.j var13 = G(var18, var8);
                     var11 += var13.triangles();
                     if (var11 > 16384) {
                        throw new IOException("Prop mesh exceeds its triangle budget");
                     }

                     var21.add(var13);
                  }

                  if (var18.read() != -1) {
                     throw new IOException("Unexpected prop mesh trailer");
                  } else {
                     return new yg.e(List.copyOf(var21));
                  }
               } else {
                  throw new IOException("Invalid prop part count");
               }
            } else {
               throw new IOException("Invalid prop material count");
            }
         } else {
            throw new IOException("Invalid prop mesh payload size");
         }
      } else {
         throw new IOException("Invalid prop mesh size");
      }
   }

   private static yg.j G(DataInputStream var0, yg.d[] var1) throws IOException {
      int var2 = var0.readUnsignedByte();
      if (var2 > 1) {
         throw new IOException("Invalid prop part flags");
      }

      int var3 = var2 == 1 ? 1 : 0;
      float[] var4 = new float[3];
      float[] var5 = new float[3];

      for (int var6 = 0; var6 < 3; var6++) {
         var4[var6] = u(var0.readFloat());
      }

      for (int var20 = 0; var20 < 3; var20++) {
         var5[var20] = u(var0.readFloat()) - var4[var20];
         if (var5[var20] < 0.0F) {
            throw new IOException("Invalid prop part bounds");
         }
      }

      if (var3 != 0 && var4[0] != 0.0F) {
         throw new IOException("A mirrored prop part must start at x = 0");
      }

      int var21 = var0.readUnsignedShort();
      if (var21 >= 3 && var21 <= 16384) {
         int[][] var7 = new int[3][var21];

         for (int var8 = 0; var8 < 3; var8++) {
            int var9 = 0;

            for (int var10 = 0; var10 < var21; var10++) {
               var7[var8][var10] = var9 = var9 + var0.readShort() & 65535;
            }
         }

         float[] var22 = new float[var21 * 3];

         for (int var23 = 0; var23 < var21; var23++) {
            for (int var25 = 0; var25 < 3; var25++) {
               var22[var23 * 3 + var25] = var4[var25] + var7[var25][var23] / 65535.0F * var5[var25];
            }
         }

         int var24 = var0.readUnsignedShort();
         if (var24 >= 1 && var24 <= 16384) {
            int[] var26 = new int[var24 * 3];
            int var11 = 0;

            for (int var12 = 0; var12 < var24; var12++) {
               int var13 = var11 + var0.readShort();
               int var14 = var13 + var0.readShort();
               int var15 = var13 + var0.readShort();

               for (int var19 : new int[]{var13, var14, var15}) {
                  if (var19 < 0 || var19 >= var21) {
                     throw new IOException("Prop triangle index out of range");
                  }
               }

               var26[var12 * 3] = var13;
               var26[var12 * 3 + 1] = var14;
               var26[var12 * 3 + 2] = var15;
               var11 = var13;
            }

            int[] var27 = new int[var24];

            for (int var28 = 0; var28 < var24; var28++) {
               var27[var28] = var0.readUnsignedByte();
               if (var27[var28] >= var1.length) {
                  throw new IOException("Prop triangle material out of range");
               }
            }

            int var29 = var0.readUnsignedByte();
            if (var29 >= 1 && var29 <= 8) {
               float[][] var30 = new float[var29][4];

               for (int var31 = 0; var31 < var29; var31++) {
                  for (int var32 = 0; var32 < 3; var32++) {
                     var30[var31][var32] = u(var0.readFloat());
                  }

                  int var33 = var0.readUnsignedByte();
                  if (var33 > 1) {
                     throw new IOException("Invalid prop instance flags");
                  }

                  var30[var31][3] = var33;
               }

               return q(var22, var26, var27, var1, (boolean)var3, var30);
            } else {
               throw new IOException("Invalid prop instance count");
            }
         } else {
            throw new IOException("Invalid prop triangle count");
         }
      } else {
         throw new IOException("Invalid prop vertex count");
      }
   }

   private static yg.j q(float[] var0, int[] var1, int[] var2, yg.d[] var3, boolean var4, float[][] var5) throws IOException {
      int var6 = var0.length / 3;
      int var7 = var2.length;
      int var8 = var4 ? 2 : 1;
      int var9 = var5.length * var8 * var7 * 3;
      float[] var10 = new float[var9 * 5];
      int[] var11 = new int[var9];
      int var12 = 0;

      for (int var13 = 0; var13 < var5.length; var13++) {
         for (int var14 = 0; var14 < var8; var14++) {
            int var15 = (var14 == 1 ? 1 : 0) != (var5[var13][3] == 1.0F ? 1 : 0) ? 1 : 0;
            float var16 = var15 != 0 ? -1.0F : 1.0F;

            for (int var17 = 0; var17 < var7; var17++) {
               yg.d var18 = var3[var2[var17]];

               for (int var19 = 0; var19 < 3; var19++) {
                  int var20 = var1[var17 * 3 + (var15 != 0 ? (3 - var19) % 3 : var19)];
                  float var21 = var0[var20 * 3];
                  float var22 = var0[var20 * 3 + 1];
                  float var23 = var0[var20 * 3 + 2];
                  int var24 = var12 * 5;
                  var10[var24] = var21 * var16 + var5[var13][0];
                  var10[var24 + 1] = var22 + var5[var13][1];
                  var10[var24 + 2] = var23 + var5[var13][2];

                  float var25 = switch (var18.kind) {
                     case 1 -> var23;
                     case 2 -> var23;
                     case 3 -> var21;
                     default -> 0.0F;
                  };

                  float var26 = switch (var18.kind) {
                     case 1 -> var22;
                     case 2 -> var21;
                     case 3 -> var22;
                     default -> 0.0F;
                  };
                  float var27 = var18.a * var25 + var18.b;
                  float var28 = var18.c * var26 + var18.d;
                  if (!(var27 >= -1.0E-4F) || !(var27 <= 1.0001F) || !(var28 >= -1.0E-4F) || !(var28 <= 1.0001F)) {
                     throw new IOException("Prop texture coordinate outside the atlas");
                  }

                  var10[var24 + 3] = Math.clamp(var27, 0.0F, 1.0F);
                  var10[var24 + 4] = Math.clamp(var28, 0.0F, 1.0F);
                  int var29 = var14 == 1 && var21 == 0.0F ? 1 : 0;
                  var11[var12] = (var13 * var8 + (var29 != 0 ? 0 : var14)) * var6 + var20;
                  var12++;
               }
            }
         }
      }

      return new yg.j(var10, b(var10, var11));
   }

   private static int[] b(float[] var0, int[] var1) {
      int var2 = var1.length / 3;
      float[] var3 = new float[var2 * 3];
      HashMap var4 = new HashMap();

      for (int var5 = 0; var5 < var2; var5++) {
         int var6 = var5 * 15;
         int var7 = var6 + 5;
         int var8 = var6 + 10;
         float var9 = var0[var7] - var0[var6];
         float var10 = var0[var7 + 1] - var0[var6 + 1];
         float var11 = var0[var7 + 2] - var0[var6 + 2];
         float var12 = var0[var8] - var0[var6];
         float var13 = var0[var8 + 1] - var0[var6 + 1];
         float var14 = var0[var8 + 2] - var0[var6 + 2];
         var3[var5 * 3] = var10 * var14 - var11 * var13;
         var3[var5 * 3 + 1] = var11 * var12 - var9 * var14;
         var3[var5 * 3 + 2] = var9 * var13 - var10 * var12;

         for (int var15 = 0; var15 < 3; var15++) {
            var4.computeIfAbsent(var1[var5 * 3 + var15], var0x -> new ArrayList<>()).add(var5);
         }
      }

      int[] var16 = new int[var1.length];

      for (int var17 = 0; var17 < var2; var17++) {
         float[] var18 = f(var3[var17 * 3], var3[var17 * 3 + 1], var3[var17 * 3 + 2]);

         for (int var19 = 0; var19 < 3; var19++) {
            float var20 = 0.0F;
            float var21 = 0.0F;
            float var22 = 0.0F;

            for (int var25 : (List)var4.get(var1[var17 * 3 + var19])) {
               float[] var27 = f(var3[var25 * 3], var3[var25 * 3 + 1], var3[var25 * 3 + 2]);
               if (!(var27[0] * var18[0] + var27[1] * var18[1] + var27[2] * var18[2] < k)) {
                  var20 += var3[var25 * 3];
                  var21 += var3[var25 * 3 + 1];
                  var22 += var3[var25 * 3 + 2];
               }
            }

            float[] var24 = f(var20, var21, var22);
            float var26 = 0.5F + 0.38F * Math.max(0.0F, var24[0] * C[0] + var24[1] * C[1] + var24[2] * C[2]) + 0.12F * (0.5F + 0.5F * var24[1]);
            int var28 = Math.round(255.0F * Math.min(1.0F, var26));
            var16[var17 * 3 + var19] = 0xFF000000 | var28 << 16 | var28 << 8 | var28;
         }
      }

      return var16;
   }

   private static float[] f(float var0, float var1, float var2) {
      float var3 = (float)Math.sqrt(var0 * var0 + var1 * var1 + var2 * var2);
      return var3 < 1.0E-12F ? new float[]{0.0F, 1.0F, 0.0F} : new float[]{var0 / var3, var1 / var3, var2 / var3};
   }

   private static float T(float var0) throws IOException {
      if (!Float.isFinite(var0)) {
         throw new IOException("Prop mesh value is not finite");
      } else {
         return var0;
      }
   }

   private static float u(float var0) throws IOException {
      if (Float.isFinite(var0) && !(Math.abs(var0) > 8.0F)) {
         return var0;
      } else {
         throw new IOException("Prop mesh coordinate out of range");
      }
   }

   private yg() {
   }

   private record d(int kind, float a, float b, float c, float d) {
   }

   public record e(List<yg.j> parts) {
      public int triangles() {
         return this.parts.stream().mapToInt(yg.j::triangles).sum();
      }
   }

   public record j(float[] vertices, int[] colors) {
      public int triangles() {
         return this.colors.length / 3;
      }
   }
}
