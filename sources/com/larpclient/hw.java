package com.larpclient;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Locale;
import org.joml.Matrix4f;

public final class hw {
   public static final double D = 0.995;
   public static final float b = 0.13F;
   public static final float T = 0.21666665F;
   private static final int[] H = new int[]{0, 1, 3, 2, 0, 1, 3, 2};
   private static final List<hw.b> M = d(0.37F, 0.072F);
   static final int h = 827149390;
   static final int p = 8192;
   static final float N = 0.065F;
   static final float f = 4.0F;
   private static final EnumMap<hw.j, hw.s> F = new EnumMap<>(hw.j.class);
   private static final EnumMap<hw.j, Boolean> i = new EnumMap<>(hw.j.class);

   private static List<hw.b> d(float var0, float var1) {
      ArrayList var2 = new ArrayList();

      for (int var3 = 0; var3 < H.length; var3++) {
         float var4 = (float)Math.toRadians(22.5 + 45 * var3);
         var2.add(new hw.b(H[var3], var0 * (float)Math.sin(var4), var1, -var0 * (float)Math.cos(var4), var4));
      }

      return List.copyOf(var2);
   }

   public static List<hw.b> S(hw.j var0) {
      return var0 == hw.j.Y ? M : List.of();
   }

   public static Matrix4f t(Matrix4f var0, hw.b var1, double var2, Matrix4f var4) {
      float var5 = (float)var2;
      return var4.set(var0)
         .translate(var1.x() * var5, -0.5F - var1.rise() * var5, var1.z() * var5)
         .rotateY((float) Math.PI - var1.angle())
         .rotateZ((float) Math.PI)
         .scale(0.21666665F * var5);
   }

   private hw() {
   }

   public static hw.s N(hw.j var0) {
      hw.s var1 = F.get(var0);
      if (var1 == null && !i.containsKey(var0)) {
         try (InputStream var2 = hw.class.getResourceAsStream("/assets/larp-client/models/hats/" + var0.K + ".bin")) {
            if (var2 == null) {
               throw new IOException("missing resource");
            }

            var1 = O(var2.readAllBytes(), var0);
            F.put(var0, var1);
            r4.V("ChinaHat", "{} mesh loaded; {} triangles, {} blocks tall", var0.j, var1.triangles(), String.format(Locale.ROOT, "%.3f", var1.rise()));
         } catch (IOException | RuntimeException var7) {
            i.put(var0, true);
            r4.r("ChinaHat", var0.j + " mesh unavailable; accessory hidden until restart", var7);
         }

         return var1;
      } else {
         return var1;
      }
   }

   public static hw.s O(byte[] var0, hw.j var1) {
      hw.s var2 = Z(var0);
      if (var1.R == 1.0F) {
         return var2;
      }

      float[] var3 = (float[])var2.points.clone();
      float var4 = Float.MAX_VALUE;
      float var5 = -Float.MAX_VALUE;

      for (byte var6 = 1; var6 < var3.length; var6 += 3) {
         var4 = Math.min(var4, var3[var6]);
         var5 = Math.max(var5, var3[var6]);
      }

      float var9 = (var4 + var5) / 2.0F;
      float var7 = 0.0F;

      for (byte var8 = 0; var8 < var3.length; var8 += 3) {
         var3[var8] *= var1.R;
         var3[var8 + 2] = var3[var8 + 2] * var1.R;
         var3[var8 + 1] = var1.h + (var3[var8 + 1] - var9) * var1.R;
         var7 = Math.max(var7, var3[var8 + 1]);
      }

      return new hw.s(var3, var2.colors, var2.emissive, var7);
   }

   public static hw.s Z(byte[] var0) {
      ByteBuffer var1 = ByteBuffer.wrap(var0).order(ByteOrder.LITTLE_ENDIAN);
      if (var0.length >= 8 && var1.getInt() == 827149390) {
         int var2 = var1.getInt();
         if (var2 > 0 && var2 <= 8192 && var0.length == 8L + var2 * 56L) {
            float[] var3 = new float[var2 * 9];
            int[] var4 = new int[var2];
            boolean[] var5 = new boolean[var2];
            float var6 = 0.0F;

            for (int var7 = 0; var7 < var2; var7++) {
               for (int var8 = 0; var8 < 3; var8++) {
                  float var9 = var1.getFloat();
                  float var10 = var1.getFloat();
                  float var11 = var1.getFloat();
                  if (!Float.isFinite(var9)
                     || !Float.isFinite(var10)
                     || !Float.isFinite(var11)
                     || Math.abs(var9) > 64.0F
                     || Math.abs(var10) > 64.0F
                     || Math.abs(var11) > 64.0F) {
                     throw new IllegalArgumentException("Invalid NLM1 vertex");
                  }

                  float var12 = (var10 + 4.0F) * 0.065F - 0.5F;
                  var3[var7 * 9 + var8 * 3] = var9 * 0.065F;
                  var3[var7 * 9 + var8 * 3 + 1] = var12;
                  var3[var7 * 9 + var8 * 3 + 2] = -var11 * 0.065F;
                  var6 = Math.max(var6, var12);
               }

               float var13 = var1.getFloat();
               float var14 = var1.getFloat();
               float var15 = var1.getFloat();
               int var16 = var1.getInt() & 16777215;
               var5[var7] = (boolean)(var1.getInt() != 0 ? 1 : 0);
               var4[var7] = var5[var7] ? var16 : dg.z(0xFF000000 | var16, s(var13, var14, var15)) & 16777215;
            }

            return new hw.s(var3, var4, var5, var6);
         } else {
            throw new IllegalArgumentException("Invalid NLM1 triangle count " + var2);
         }
      } else {
         throw new IllegalArgumentException("Not an NLM1 mesh");
      }
   }

   static double s(float var0, float var1, float var2) {
      double var3 = Math.sqrt(var0 * var0 + var1 * var1 + var2 * var2);
      if (!(var3 > 1.0E-6)) {
         return 0.8;
      }

      double var5 = var0 / var3;
      double var7 = var1 / var3;
      double var9 = var2 / var3;
      return Math.clamp(var5 * var5 * 0.6 + var9 * var9 * 0.8 + var7 * var7 * (var7 > 0.0 ? 1.0 : 0.5), 0.5, 1.0);
   }

   public static double e(hw.j var0, double var1) {
      hw.s var3 = N(var0);
      return var3 == null ? 0.0 : var3.rise() * var1;
   }

   public static void V(_c var0, Matrix4f var1, hw.j var2, double var3, double var5, boolean var7) {
      hw.s var8 = N(var2);
      if (var8 != null) {
         float var9 = (float)var3;
         int var10 = (int)(Math.clamp(var5, 0.0, 1.0) * 255.0) << 24;
         float[] var11 = var8.points;
         var0.blend(fk.h.z);
         var0.solid(var5 >= 0.995);

         try {
            int var12 = !var7 && !S(var2).isEmpty() ? 1 : 0;
            int var13 = 0;

            for (int var14 = var8.colors.length; var13 < var14; var13++) {
               if (var12 == 0 || !var8.emissive[var13]) {
                  int var15 = var10 | var8.colors[var13];
                  int var16 = var13 * 9;

                  for (int var17 = 0; var17 < 4; var17++) {
                     int var18 = var16 + Math.min(var17, 2) * 3;
                     var0.modelVertex(var1, var11[var18] * var9, -0.5F - var11[var18 + 1] * var9, var11[var18 + 2] * var9, var15);
                  }
               }
            }
         } finally {
            var0.solid(false);
         }
      }
   }

   public record b(int coin, float x, float rise, float z, float angle) {
   }

   public enum j {
      Y("Crown", "crown", 1.0F, Float.NaN),
      f("Halo", "halo", 1.6F, 0.3F);

      public final String j;
      final String K;
      final float R;
      final float h;

      j(String var3, String var4, float var5, float var6) {
         this.j = var3;
         this.K = var4;
         this.R = var5;
         this.h = var6;
      }
   }

   public record s(float[] points, int[] colors, boolean[] emissive, float rise) {
      public int triangles() {
         return this.colors.length;
      }
   }
}
