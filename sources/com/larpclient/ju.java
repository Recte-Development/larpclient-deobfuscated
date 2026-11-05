package com.larpclient;

import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.renderpearl.api.pipeline.ColorTargetState;
import com.mojang.renderpearl.api.pipeline.CompareOp;
import com.mojang.renderpearl.api.pipeline.DepthStencilState;
import com.mojang.renderpearl.api.pipeline.PrimitiveTopology;
import com.mojang.renderpearl.api.pipeline.RenderPipeline;
import com.mojang.renderpearl.api.pipeline.RenderPipeline.Snippet;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Locale;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.rendertype.RenderSetup;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.resources.Identifier;
import org.joml.Matrix4f;

public final class ju {
   static final int W = 827216456;
   static final int J = 65535;
   static final int D = 8192;
   public static final int C = -61184;
   public static final int Q = -42752;
   public static final int c = -2304;
   private static ju.h k;
   private static boolean y;
   private static final Identifier G = Identifier.fromNamespaceAndPath("larp-client", "dynamic/china_hat_horns");
   private static DynamicTexture v;
   private static int[] U;
   private static int[] o;
   private static long X = -1L;
   private static long g = -1L;
   private static int b;
   private static int l;
   private static int u;

   private ju() {
   }

   public static ju.h k() {
      if (k == null && !y) {
         try (InputStream var0 = ju.class.getResourceAsStream("/assets/larp-client/models/hats/horns.bin")) {
            if (var0 == null) {
               throw new IOException("missing resource");
            }

            k = O(var0.readAllBytes());
            r4.V(
               "ChinaHat",
               "Horns mesh loaded; {} triangles ({} glowing), {} vertices, {} blocks tall",
               k.triangles(),
               k.glowTriangles().length,
               k.vertices(),
               String.format(Locale.ROOT, "%.3f", k.rise())
            );
         } catch (IOException | RuntimeException var5) {
            y = (boolean)1;
            r4.r("ChinaHat", "Horns mesh unavailable; horns hidden until restart", var5);
         }

         return k;
      } else {
         return k;
      }
   }

   public static ju.h O(byte[] var0) {
      ByteBuffer var1 = ByteBuffer.wrap(var0).order(ByteOrder.LITTLE_ENDIAN);
      if (var0.length >= 12 && var1.getInt() == 827216456) {
         int var2 = var1.getInt();
         int var3 = var1.getInt();
         if (var2 > 0 && var2 <= 65535 && var3 > 0 && var3 <= 8192 && var0.length == 12L + var2 * 22L + var3 * 6L) {
            float[] var4 = new float[var2 * 3];
            float[] var5 = new float[var2 * 2];
            int[] var6 = new int[var2];
            int[] var7 = new int[var2];
            boolean[] var8 = new boolean[var2];
            float var9 = 0.0F;
            float var10 = 0.0F;

            for (int var11 = 0; var11 < var2; var11++) {
               for (int var12 = 0; var12 < 5; var12++) {
                  float var13 = var1.getFloat();
                  if (!Float.isFinite(var13) || Math.abs(var13) > 2.0F) {
                     throw new IllegalArgumentException("Invalid HRN1 vertex");
                  }

                  if (var12 < 3) {
                     var4[var11 * 3 + var12] = var13;
                  } else {
                     var5[var11 * 2 + var12 - 3] = var13;
                  }
               }

               float var18 = (var1.get() & 0xFF) / 255.0F;
               float var20 = (var1.get() & 0xFF) / 255.0F;
               var6[var11] = x(var18 + (1.0F - var18) * var20);
               var7[var11] = x((float)Math.pow(var20, 1.2) * 0.9F);
               var8[var11] = (boolean)(var20 > 0.0F ? 1 : 0);
               var9 = Math.max(var9, var4[var11 * 3 + 1]);
               var10 += var4[var11 * 3 + 2];
            }

            int[] var17 = new int[var3 * 3];
            int var19 = 0;

            for (int var21 = 0; var21 < var3; var21++) {
               byte var14 = 0;

               for (int var15 = 0; var15 < 3; var15++) {
                  int var16 = var1.getShort() & '\uffff';
                  if (var16 >= var2) {
                     throw new IllegalArgumentException("Invalid HRN1 index");
                  }

                  var17[var21 * 3 + var15] = var16;
                  var14 |= var8[var16];
               }

               if (var14 != 0) {
                  var19++;
               }
            }

            if (var19 == 0) {
               throw new IllegalArgumentException("HRN1 mesh has no glowing tips");
            }

            int[] var22 = new int[var19];
            int var23 = 0;
            int var24 = 0;

            while (var23 < var3) {
               if (var8[var17[var23 * 3]] || var8[var17[var23 * 3 + 1]] || var8[var17[var23 * 3 + 2]]) {
                  var22[var24++] = var23;
               }

               var23++;
            }

            return new ju.h(var4, var5, var6, var7, var17, var22, var9, var10 / var2);
         } else {
            throw new IllegalArgumentException("Invalid HRN1 counts " + var2 + "/" + var3);
         }
      } else {
         throw new IllegalArgumentException("Not an HRN1 mesh");
      }
   }

   public static double V(double var0) {
      ju.h var2 = k();
      return var2 == null ? 0.0 : var2.rise() * var0;
   }

   static float s(float var0, float var1, float var2) {
      float var3 = Math.clamp((var2 - var0) / (var1 - var0), 0.0F, 1.0F);
      return var3 * var3 * (3.0F - 2.0F * var3);
   }

   public static int S(int var0) {
      return oh.j(var0, -1, 0.45);
   }

   public static int N(int var0, int var1, int var2, int var3) {
      float var4 = (var0 >> 16 & 0xFF) / 255.0F;
      float var5 = (var0 >> 8 & 0xFF) / 255.0F;
      float var6 = (var0 & 0xFF) / 255.0F;
      float var7 = Math.max(var4, Math.max(var5, var6));
      float var8 = Math.min(var4, Math.min(var5, var6));
      if (var7 <= 0.0F) {
         return var0;
      }

      float var9 = (var7 - var8) / var7;
      float var10 = s(0.34F, 0.55F, var9);
      if (var10 <= 0.0F) {
         return var0;
      }

      float var11 = Math.max(var7 - var8, 1.0E-6F);
      float var12 = var7 == var4 ? ((var5 - var6) / var11 % 6.0F + 6.0F) % 6.0F : (var7 == var5 ? (var6 - var4) / var11 + 2.0F : (var4 - var5) / var11 + 4.0F);
      var12 *= 60.0F;
      if (var12 > 300.0F) {
         var12 -= 360.0F;
      }

      float var13 = s(9.0F, 20.0F, var12);
      float var14 = s(18.0F, 60.0F, var12);
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

   public static void W(int[] var0, int[] var1, int var2, int var3, int var4) {
      for (int var5 = 0; var5 < var0.length; var5++) {
         var1[var5] = N(var0[var5], var2, var3, var4);
      }
   }

   public static boolean F(int var0, int var1, int var2) {
      if (g == jk.v) {
         return false;
      }

      try {
         if (k() == null) {
            return false;
         }

         if (v == null || X != jk.v) {
            b();
            b = ~var0;
         }

         if (var0 != b || var1 != l || var2 != u) {
            W(U, o, var0, var1, var2);
            NativeImage var3 = v.getPixels();
            int var4 = var3.getWidth();

            for (int var5 = 0; var5 < o.length; var5++) {
               var3.setPixel(var5 % var4, var5 / var4, o[var5]);
            }

            v.upload();
            b = var0;
            l = var1;
            u = var2;
            r4.g("ChinaHat", "Horns recolored: horn #{}, fire #{}, core #{}", Integer.toHexString(var0), Integer.toHexString(var1), Integer.toHexString(var2));
         }

         return true;
      } catch (IOException | RuntimeException var6) {
         g = jk.v;
         v = null;
         r4.r("ChinaHat", "Horns texture unavailable; horns hidden until resource reload", var6);
         return false;
      }
   }

   private static void b() throws IOException {
      try (InputStream var0 = ju.class.getResourceAsStream("/assets/larp-client/models/hats/horns.png")) {
         if (var0 == null) {
            throw new IOException("missing horns.png");
         }

         NativeImage var1 = NativeImage.read(var0);

         DynamicTexture var2;
         try {
            U = var1.getPixels();
            o = new int[U.length];
            var2 = new DynamicTexture(() -> "LARP China Hat horns", var1);
         } catch (RuntimeException var6) {
            var1.close();
            throw var6;
         }

         try {
            Minecraft.getInstance().getTextureManager().register(G, var2);
         } catch (RuntimeException var5) {
            var2.close();
            throw var5;
         }

         e4.U(ju.s.X);
         v = var2;
         X = jk.v;
      }
   }

   public static RenderType G() {
      return ju.s.w;
   }

   public static void H(VertexConsumer var0, Matrix4f var1, ju.h var2, double var3) {
      float var5 = (float)var3;
      float[] var6 = var2.points;
      float[] var7 = var2.uvs;

      for (int var11 : var2.indices) {
         var0.addVertex(var1, var6[var11 * 3] * var5, -0.5F - var6[var11 * 3 + 1] * var5, var6[var11 * 3 + 2] * var5)
            .setUv(var7[var11 * 2], var7[var11 * 2 + 1])
            .setColor(var2.colors[var11]);
      }

      h_.A("China Hat horns triangles", var2.triangles());
   }

   public static void b(VertexConsumer var0, Matrix4f var1, ju.h var2, double var3) {
      float var5 = (float)var3;
      float[] var6 = var2.points;
      float[] var7 = var2.uvs;

      for (int var11 : var2.glowTriangles) {
         for (int var12 = 0; var12 < 3; var12++) {
            int var13 = var2.indices[var11 * 3 + var12];
            var0.addVertex(var1, var6[var13 * 3] * var5, -0.5F - var6[var13 * 3 + 1] * var5, var6[var13 * 3 + 2] * var5)
               .setUv(var7[var13 * 2], var7[var13 * 2 + 1])
               .setColor(var2.glow[var13]);
         }
      }
   }

   static int x(float var0) {
      int var1 = Math.round(Math.clamp(var0, 0.0F, 1.0F) * 255.0F);
      return 0xFF000000 | var1 << 16 | var1 << 8 | var1;
   }

   public record h(float[] points, float[] uvs, int[] colors, int[] glow, int[] indices, int[] glowTriangles, float rise, float centerZ) {
      public int vertices() {
         return this.colors.length;
      }

      public int triangles() {
         return this.indices.length / 3;
      }
   }

   private static final class s {
      static final RenderPipeline X = RenderPipeline.builder(new Snippet[]{RenderPipelines.GUI_TEXTURED_SNIPPET})
         .withLocation(Identifier.fromNamespaceAndPath("larp-client", "china_hat_horns"))
         .withFragmentShader(Identifier.fromNamespaceAndPath("larp-client", "particle/sprite"))
         .withPrimitiveTopology(PrimitiveTopology.TRIANGLES)
         .withDepthStencilState(new DepthStencilState(CompareOp.GREATER_THAN_OR_EQUAL, true))
         .withColorTargetState(ColorTargetState.DEFAULT)
         .withCull(true)
         .build();
      static final RenderType w = RenderType.create("larp_china_hat_horns", RenderSetup.builder(X).withTexture("Sampler0", ju.G).createRenderSetup());
   }
}
