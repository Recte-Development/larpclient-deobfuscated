package com.larpclient;

import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.pipeline.RenderPipeline.Snippet;
import com.mojang.blaze3d.platform.DepthTestFunction;
import com.mojang.blaze3d.vertex.VertexFormat.class_5596;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Locale;
import net.minecraft.class_1011;
import net.minecraft.class_1043;
import net.minecraft.class_10799;
import net.minecraft.class_12247;
import net.minecraft.class_1921;
import net.minecraft.class_290;
import net.minecraft.class_2960;
import net.minecraft.class_310;
import net.minecraft.class_4588;
import org.joml.Matrix4f;

public final class bl {
   static final int w = 827216456;
   static final int p = 65535;
   static final int g = 8192;
   public static final int M = -61184;
   public static final int P = -42752;
   public static final int s = -2304;
   private static bl.a Y;
   private static boolean m;
   private static final class_2960 f = class_2960.method_60655("larp-client", "dynamic/china_hat_horns");
   private static class_1043 k;
   private static int[] j;
   private static int[] D;
   private static long o = -1L;
   private static long Q = -1L;
   private static int S;
   private static int r;
   private static int c;

   private bl() {
   }

   public static bl.a q() {
      if (Y == null && !m) {
         try (InputStream var0 = bl.class.getResourceAsStream("/assets/larp-client/models/hats/horns.bin")) {
            if (var0 == null) {
               throw new IOException("missing resource");
            }

            Y = s(var0.readAllBytes());
            q8.G(
               "ChinaHat",
               "Horns mesh loaded; {} triangles ({} glowing), {} vertices, {} blocks tall",
               Y.triangles(),
               Y.glowTriangles().length,
               Y.vertices(),
               String.format(Locale.ROOT, "%.3f", Y.rise())
            );
         } catch (IOException | RuntimeException var5) {
            m = (boolean)1;
            q8.u("ChinaHat", "Horns mesh unavailable; horns hidden until restart", var5);
         }

         return Y;
      } else {
         return Y;
      }
   }

   public static bl.a s(byte[] var0) {
      return c(var0, true);
   }

   static bl.a c(byte[] var0, boolean var1) {
      ByteBuffer var2 = ByteBuffer.wrap(var0).order(ByteOrder.LITTLE_ENDIAN);
      if (var0.length >= 12 && var2.getInt() == 827216456) {
         int var3 = var2.getInt();
         int var4 = var2.getInt();
         if (var3 > 0 && var3 <= 65535 && var4 > 0 && var4 <= 8192 && var0.length == 12L + var3 * 22L + var4 * 6L) {
            float[] var5 = new float[var3 * 3];
            float[] var6 = new float[var3 * 2];
            int[] var7 = new int[var3];
            int[] var8 = new int[var3];
            boolean[] var9 = new boolean[var3];
            float var10 = 0.0F;
            float var11 = 0.0F;

            for (int var12 = 0; var12 < var3; var12++) {
               for (int var13 = 0; var13 < 5; var13++) {
                  float var14 = var2.getFloat();
                  if (!Float.isFinite(var14) || Math.abs(var14) > 2.0F) {
                     throw new IllegalArgumentException("Invalid HRN1 vertex");
                  }

                  if (var13 < 3) {
                     var5[var12 * 3 + var13] = var14;
                  } else {
                     var6[var12 * 2 + var13 - 3] = var14;
                  }
               }

               float var19 = (var2.get() & 0xFF) / 255.0F;
               float var21 = (var2.get() & 0xFF) / 255.0F;
               var7[var12] = P(var19 + (1.0F - var19) * var21);
               var8[var12] = P((float)Math.pow(var21, 1.2) * 0.9F);
               var9[var12] = (boolean)(var21 > 0.0F ? 1 : 0);
               var10 = Math.max(var10, var5[var12 * 3 + 1]);
               var11 += var5[var12 * 3 + 2];
            }

            int[] var18 = new int[var4 * 3];
            int var20 = 0;

            for (int var22 = 0; var22 < var4; var22++) {
               byte var15 = 0;

               for (int var16 = 0; var16 < 3; var16++) {
                  int var17 = var2.getShort() & '\uffff';
                  if (var17 >= var3) {
                     throw new IllegalArgumentException("Invalid HRN1 index");
                  }

                  var18[var22 * 3 + var16] = var17;
                  var15 |= var9[var17];
               }

               if (var15 != 0) {
                  var20++;
               }
            }

            if (var1 && var20 == 0) {
               throw new IllegalArgumentException("HRN1 mesh has no glowing tips");
            }

            int[] var23 = new int[var20];
            int var24 = 0;
            int var25 = 0;

            while (var24 < var4) {
               if (var9[var18[var24 * 3]] || var9[var18[var24 * 3 + 1]] || var9[var18[var24 * 3 + 2]]) {
                  var23[var25++] = var24;
               }

               var24++;
            }

            return new bl.a(var5, var6, var7, var8, var18, var23, var10, var11 / var3);
         } else {
            throw new IllegalArgumentException("Invalid HRN1 counts " + var3 + "/" + var4);
         }
      } else {
         throw new IllegalArgumentException("Not an HRN1 mesh");
      }
   }

   public static double A(double var0) {
      bl.a var2 = q();
      return var2 == null ? 0.0 : var2.rise() * var0;
   }

   static float y(float var0, float var1, float var2) {
      float var3 = Math.clamp((var2 - var0) / (var1 - var0), 0.0F, 1.0F);
      return var3 * var3 * (3.0F - 2.0F * var3);
   }

   public static int G(int var0) {
      return ei.W(var0, -1, 0.45);
   }

   public static int v(int var0, int var1, int var2, int var3) {
      float var4 = (var0 >> 16 & 0xFF) / 255.0F;
      float var5 = (var0 >> 8 & 0xFF) / 255.0F;
      float var6 = (var0 & 0xFF) / 255.0F;
      float var7 = Math.max(var4, Math.max(var5, var6));
      float var8 = Math.min(var4, Math.min(var5, var6));
      if (var7 <= 0.0F) {
         return var0;
      }

      float var9 = (var7 - var8) / var7;
      float var10 = y(0.34F, 0.55F, var9);
      if (var10 <= 0.0F) {
         return var0;
      }

      float var11 = Math.max(var7 - var8, 1.0E-6F);
      float var12 = var7 == var4 ? ((var5 - var6) / var11 % 6.0F + 6.0F) % 6.0F : (var7 == var5 ? (var6 - var4) / var11 + 2.0F : (var4 - var5) / var11 + 4.0F);
      var12 *= 60.0F;
      if (var12 > 300.0F) {
         var12 -= 360.0F;
      }

      float var13 = y(9.0F, 20.0F, var12);
      float var14 = y(18.0F, 60.0F, var12);
      int var15 = var0 & 0xFF000000;

      for (byte var16 = 16; var16 >= 0; var16 -= 8) {
         float var17 = (var0 >> var16 & 0xFF) / 255.0F;
         float var18 = (var1 >> var16 & 0xFF) / 255.0F;
         float var19 = ((var2 >> var16 & 0xFF) + ((var3 >> var16 & 0xFF) - (var2 >> var16 & 0xFF)) * var14) / 255.0F;
         float var20 = var18 + (var19 - var18) * var13;
         float var21 = var7 * (1.0F - var9 + var9 * var20);
         int var22 = Math.round(Math.clamp(var17 + (var21 - var17) * var10, 0.0F, 1.0F) * 255.0F);
         var15 |= var22 << var16;
      }

      return var15;
   }

   public static void n(int[] var0, int[] var1, int var2, int var3, int var4) {
      for (int var5 = 0; var5 < var0.length; var5++) {
         var1[var5] = v(var0[var5], var2, var3, var4);
      }
   }

   public static boolean i(int var0, int var1, int var2) {
      if (Q == t_.g) {
         return false;
      }

      try {
         if (q() == null) {
            return false;
         }

         if (k == null || o != t_.g) {
            G();
            S = ~var0;
         }

         if (var0 != S || var1 != r || var2 != c) {
            n(j, D, var0, var1, var2);
            class_1011 var3 = k.method_4525();
            int var4 = var3.method_4307();

            for (int var5 = 0; var5 < D.length; var5++) {
               var3.method_61941(var5 % var4, var5 / var4, D[var5]);
            }

            k.method_4524();
            S = var0;
            r = var1;
            c = var2;
            q8.v("ChinaHat", "Horns recolored: horn #{}, fire #{}, core #{}", Integer.toHexString(var0), Integer.toHexString(var1), Integer.toHexString(var2));
         }

         return true;
      } catch (IOException | RuntimeException var6) {
         Q = t_.g;
         k = null;
         q8.u("ChinaHat", "Horns texture unavailable; horns hidden until resource reload", var6);
         return false;
      }
   }

   private static void G() throws IOException {
      try (InputStream var0 = bl.class.getResourceAsStream("/assets/larp-client/models/hats/horns.png")) {
         if (var0 == null) {
            throw new IOException("missing horns.png");
         }

         class_1011 var1 = class_1011.method_4309(var0);

         class_1043 var2;
         try {
            j = var1.method_61942();
            D = new int[j.length];
            var2 = new class_1043(() -> "LARP China Hat horns", var1);
         } catch (RuntimeException var6) {
            var1.close();
            throw var6;
         }

         try {
            class_310.method_1551().method_1531().method_4616(f, var2);
         } catch (RuntimeException var5) {
            var2.close();
            throw var5;
         }

         f2.a(bl.i.M);
         k = var2;
         o = t_.g;
      }
   }

   public static class_1921 H() {
      return bl.i.o;
   }

   static RenderPipeline t() {
      return bl.i.M;
   }

   public static void S(class_4588 var0, Matrix4f var1, bl.a var2, double var3) {
      float var5 = (float)var3;
      float[] var6 = var2.points;
      float[] var7 = var2.uvs;

      for (int var11 : var2.indices) {
         var0.method_22918(var1, var6[var11 * 3] * var5, -0.5F - var6[var11 * 3 + 1] * var5, var6[var11 * 3 + 2] * var5)
            .method_22913(var7[var11 * 2], var7[var11 * 2 + 1])
            .method_39415(var2.colors[var11]);
      }

      c5.V("China Hat horns triangles", var2.triangles());
   }

   public static void z(class_4588 var0, Matrix4f var1, bl.a var2, double var3) {
      float var5 = (float)var3;
      float[] var6 = var2.points;
      float[] var7 = var2.uvs;

      for (int var11 : var2.glowTriangles) {
         for (int var12 = 0; var12 < 3; var12++) {
            int var13 = var2.indices[var11 * 3 + var12];
            var0.method_22918(var1, var6[var13 * 3] * var5, -0.5F - var6[var13 * 3 + 1] * var5, var6[var13 * 3 + 2] * var5)
               .method_22913(var7[var13 * 2], var7[var13 * 2 + 1])
               .method_39415(var2.glow[var13]);
         }
      }
   }

   static int P(float var0) {
      int var1 = Math.round(Math.clamp(var0, 0.0F, 1.0F) * 255.0F);
      return 0xFF000000 | var1 << 16 | var1 << 8 | var1;
   }

   public record a(float[] points, float[] uvs, int[] colors, int[] glow, int[] indices, int[] glowTriangles, float rise, float centerZ) {
      public int vertices() {
         return this.colors.length;
      }

      public int triangles() {
         return this.indices.length / 3;
      }
   }

   private static final class i {
      static final RenderPipeline M = RenderPipeline.builder(new Snippet[]{class_10799.field_56864})
         .withLocation(class_2960.method_60655("larp-client", "china_hat_horns"))
         .withFragmentShader(class_2960.method_60655("larp-client", "particle/sprite"))
         .withVertexFormat(class_290.field_1575, class_5596.field_27379)
         .withDepthTestFunction(DepthTestFunction.LEQUAL_DEPTH_TEST)
         .withDepthWrite(true)
         .withoutBlend()
         .withCull(true)
         .build();
      static final class_1921 o = class_1921.method_75940("larp_china_hat_horns", class_12247.method_75927(M).method_75934("Sampler0", bl.f).method_75938());
   }
}
