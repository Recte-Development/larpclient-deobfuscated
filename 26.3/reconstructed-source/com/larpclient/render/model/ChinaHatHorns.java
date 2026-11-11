package com.larpclient.render.model;

import com.larpclient.render.misc.ClientPipelines;
import com.larpclient.render.overlay.PostProcessing;
import com.larpclient.util.misc.LarpLog;
import com.larpclient.util.misc.PerfProfiler;
import com.larpclient.util.render.ColorUtil;
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

public final class ChinaHatHorns {
   static final int mESH_MAGIC = 827216456;
   static final int mAX_VERTICES = 65535;
   static final int mAX_TRIANGLES = 8192;
   public static final int dEFAULT_HORN_COLOR = -61184;
   public static final int dEFAULT_FIRE_COLOR = -42752;
   public static final int dEFAULT_CORE_COLOR = -2304;
   private static ChinaHatHorns.HornMesh mesh;
   private static boolean meshLoadFailed;
   private static final Identifier tEXTURE_ID = Identifier.fromNamespaceAndPath("larp-client", "dynamic/china_hat_horns");
   private static DynamicTexture texture;
   private static int[] basePixels;
   private static int[] tintedPixels;
   private static long textureReloadCount = -1L;
   private static long failedReloadCount = -1L;
   private static int lastHornColor;
   private static int lastFireColor;
   private static int lastCoreColor;

   private ChinaHatHorns() {
   }

   public static ChinaHatHorns.HornMesh loadMesh() {
      if (mesh == null && !meshLoadFailed) {
         try (InputStream var0 = ChinaHatHorns.class.getResourceAsStream("/assets/larp-client/models/hats/horns.bin")) {
            if (var0 == null) {
               throw new IOException("missing resource");
            }

            mesh = parseMesh(var0.readAllBytes());
            LarpLog.info(
               "ChinaHat",
               "Horns mesh loaded; {} triangles ({} glowing), {} vertices, {} blocks tall",
               mesh.triangles(),
               mesh.glowTriangles().length,
               mesh.vertices(),
               String.format(Locale.ROOT, "%.3f", mesh.rise())
            );
         } catch (IOException | RuntimeException var5) {
            meshLoadFailed = (boolean)1;
            LarpLog.error("ChinaHat", "Horns mesh unavailable; horns hidden until restart", var5);
         }

         return mesh;
      } else {
         return mesh;
      }
   }

   public static ChinaHatHorns.HornMesh parseMesh(byte[] var0) {
      return parseMeshChecked(var0, true);
   }

   static ChinaHatHorns.HornMesh parseMeshChecked(byte[] var0, boolean var1) {
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
               var7[var12] = addTriangles(var19 + (1.0F - var19) * var21);
               var8[var12] = addTriangles((float)Math.pow(var21, 1.2) * 0.9F);
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

            return new ChinaHatHorns.HornMesh(var5, var6, var7, var8, var18, var23, var10, var11 / var3);
         } else {
            throw new IllegalArgumentException("Invalid HRN1 counts " + var3 + "/" + var4);
         }
      } else {
         throw new IllegalArgumentException("Not an HRN1 mesh");
      }
   }

   public static double hornHeight(double var0) {
      ChinaHatHorns.HornMesh var2 = loadMesh();
      return var2 == null ? 0.0 : var2.rise() * var0;
   }

   static float parseMesh(float var0, float var1, float var2) {
      float var3 = Math.clamp((var2 - var0) / (var1 - var0), 0.0F, 1.0F);
      return var3 * var3 * (3.0F - 2.0F * var3);
   }

   public static int lighten(int var0) {
      return ColorUtil.lerpColor(var0, -1, 0.45);
   }

   public static int recolorPixel(int var0, int var1, int var2, int var3) {
      float var4 = (var0 >> 16 & 0xFF) / 255.0F;
      float var5 = (var0 >> 8 & 0xFF) / 255.0F;
      float var6 = (var0 & 0xFF) / 255.0F;
      float var7 = Math.max(var4, Math.max(var5, var6));
      float var8 = Math.min(var4, Math.min(var5, var6));
      if (var7 <= 0.0F) {
         return var0;
      }

      float var9 = (var7 - var8) / var7;
      float var10 = parseMesh(0.34F, 0.55F, var9);
      if (var10 <= 0.0F) {
         return var0;
      }

      float var11 = Math.max(var7 - var8, 1.0E-6F);
      float var12 = var7 == var4 ? ((var5 - var6) / var11 % 6.0F + 6.0F) % 6.0F : (var7 == var5 ? (var6 - var4) / var11 + 2.0F : (var4 - var5) / var11 + 4.0F);
      var12 *= 60.0F;
      if (var12 > 300.0F) {
         var12 -= 360.0F;
      }

      float var13 = parseMesh(9.0F, 20.0F, var12);
      float var14 = parseMesh(18.0F, 60.0F, var12);
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

   public static void recolorPixels(int[] var0, int[] var1, int var2, int var3, int var4) {
      for (int var5 = 0; var5 < var0.length; var5++) {
         var1[var5] = recolorPixel(var0[var5], var2, var3, var4);
      }
   }

   public static boolean updateTexture(int var0, int var1, int var2) {
      if (failedReloadCount == PostProcessing.reloadCount) {
         return false;
      }

      try {
         if (loadMesh() == null) {
            return false;
         }

         if (texture == null || textureReloadCount != PostProcessing.reloadCount) {
            loadTexture();
            lastHornColor = ~var0;
         }

         if (var0 != lastHornColor || var1 != lastFireColor || var2 != lastCoreColor) {
            recolorPixels(basePixels, tintedPixels, var0, var1, var2);
            NativeImage var3 = texture.getPixels();
            int var4 = var3.getWidth();

            for (int var5 = 0; var5 < tintedPixels.length; var5++) {
               var3.setPixel(var5 % var4, var5 / var4, tintedPixels[var5]);
            }

            texture.upload();
            lastHornColor = var0;
            lastFireColor = var1;
            lastCoreColor = var2;
            LarpLog.debug(
               "ChinaHat", "Horns recolored: horn #{}, fire #{}, core #{}", Integer.toHexString(var0), Integer.toHexString(var1), Integer.toHexString(var2)
            );
         }

         return true;
      } catch (IOException | RuntimeException var6) {
         failedReloadCount = PostProcessing.reloadCount;
         texture = null;
         LarpLog.error("ChinaHat", "Horns texture unavailable; horns hidden until resource reload", var6);
         return false;
      }
   }

   private static void loadTexture() throws IOException {
      try (InputStream var0 = ChinaHatHorns.class.getResourceAsStream("/assets/larp-client/models/hats/horns.png")) {
         if (var0 == null) {
            throw new IOException("missing horns.png");
         }

         NativeImage var1 = NativeImage.read(var0);

         DynamicTexture var2;
         try {
            basePixels = var1.getPixels();
            tintedPixels = new int[basePixels.length];
            var2 = new DynamicTexture(() -> "LARP China Hat horns", var1);
         } catch (RuntimeException var6) {
            var1.close();
            throw var6;
         }

         try {
            Minecraft.getInstance().getTextureManager().register(tEXTURE_ID, var2);
         } catch (RuntimeException var5) {
            var2.close();
            throw var5;
         }

         ClientPipelines.validatePipeline(ChinaHatHorns.HornRender.pipeline);
         texture = var2;
         textureReloadCount = PostProcessing.reloadCount;
      }
   }

   public static RenderType getRenderType() {
      return ChinaHatHorns.HornRender.renderType;
   }

   static RenderPipeline getRenderType() {
      return ChinaHatHorns.HornRender.pipeline;
   }

   public static void addTriangles(VertexConsumer var0, Matrix4f var1, ChinaHatHorns.HornMesh var2, double var3) {
      float var5 = (float)var3;
      float[] var6 = var2.points;
      float[] var7 = var2.uvs;

      for (int var11 : var2.indices) {
         var0.addVertex(var1, var6[var11 * 3] * var5, -0.5F - var6[var11 * 3 + 1] * var5, var6[var11 * 3 + 2] * var5)
            .setUv(var7[var11 * 2], var7[var11 * 2 + 1])
            .setColor(var2.colors[var11]);
      }

      PerfProfiler.addCounter("China Hat horns triangles", var2.triangles());
   }

   public static void addGlowTriangles(VertexConsumer var0, Matrix4f var1, ChinaHatHorns.HornMesh var2, double var3) {
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

   static int addTriangles(float var0) {
      int var1 = Math.round(Math.clamp(var0, 0.0F, 1.0F) * 255.0F);
      return 0xFF000000 | var1 << 16 | var1 << 8 | var1;
   }

   public record HornMesh(float[] points, float[] uvs, int[] colors, int[] glow, int[] indices, int[] glowTriangles, float rise, float centerZ) {
      public int vertices() {
         return this.colors.length;
      }

      public int triangles() {
         return this.indices.length / 3;
      }
   }

   private static final class HornRender {
      static final RenderPipeline pipeline = RenderPipeline.builder(new Snippet[]{RenderPipelines.GUI_TEXTURED_SNIPPET})
         .withLocation(Identifier.fromNamespaceAndPath("larp-client", "china_hat_horns"))
         .withFragmentShader(Identifier.fromNamespaceAndPath("larp-client", "particle/sprite"))
         .withPrimitiveTopology(PrimitiveTopology.TRIANGLES)
         .withDepthStencilState(new DepthStencilState(CompareOp.GREATER_THAN_OR_EQUAL, true))
         .withColorTargetState(ColorTargetState.DEFAULT)
         .withCull(true)
         .build();
      static final RenderType renderType = RenderType.create(
         "larp_china_hat_horns", RenderSetup.builder(pipeline).withTexture("Sampler0", ChinaHatHorns.tEXTURE_ID).createRenderSetup()
      );
   }
}
