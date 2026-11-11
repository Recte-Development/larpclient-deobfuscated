package com.larpclient.render.entity;

import com.larpclient.render.model.ChinaHatMesh;
import com.larpclient.render.overlay.PostProcessing;
import com.larpclient.util.misc.LarpLog;
import com.larpclient.util.misc.PerfProfiler;
import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.PoseStack.Pose;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.OrderedSubmitNodeCollector;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;

public final class KorbloxLegRenderer {
   private static final Identifier tEXTURE_ID = Identifier.fromNamespaceAndPath("larp-client", "dynamic/korblox_leg");
   private static final int fULL_BRIGHT = 15728880;
   private static final int tEXTURE_SIZE = 64;
   private static int readyGeneration = -1;
   private static int failedGeneration = -1;

   private KorbloxLegRenderer() {
   }

   public static boolean ensureTexture() {
      int var0 = PostProcessing.reloadCount;
      if (readyGeneration == var0) {
         return true;
      }

      if (failedGeneration == var0) {
         return false;
      }

      try {
         DynamicTexture var1 = new DynamicTexture(() -> "LARP Korblox leg", 64, 64, false);

         try {
            int[] var2 = ChinaHatMesh.buildTexture();
            NativeImage var3 = var1.getPixels();

            for (int var4 = 0; var4 < var2.length; var4++) {
               var3.setPixel(var4 % 64, var4 / 64, var2[var4]);
            }

            var1.upload();
            Minecraft.getInstance().getTextureManager().register(tEXTURE_ID, var1);
         } catch (RuntimeException var5) {
            var1.close();
            throw var5;
         }

         readyGeneration = var0;
         LarpLog.info("Korblox", "Leg ready: {} faces, generated {}x{} texture, resource generation {}", ChinaHatMesh.getMesh().faces(), 64, 64, var0);
         return true;
      } catch (RuntimeException var6) {
         failedGeneration = var0;
         LarpLog.error("Korblox", "Leg texture unavailable; native legs stay until resource reload", var6);
         return false;
      }
   }

   public static void renderLeg(PoseStack var0, OrderedSubmitNodeCollector var1, ModelPart var2, LegTrail var3, int var4, int var5, boolean var6) {
      var0.pushPose();

      try {
         var2.translateAndRotate(var0);
         var1.submitCustomGeometry(var0, KorbloxLegRenderer.RenderTypes.bODY, (var4x, var5x) -> submitGeometry(var4x, var5x, var3, var4, var5, var6, false));
         if (var3.glow() > 0.0F) {
            var1.submitCustomGeometry(
               var0, KorbloxLegRenderer.RenderTypes.gLOW, (var2x, var3x) -> submitGeometry(var2x, var3x, var3, 15728880, OverlayTexture.NO_OVERLAY, var6, true)
            );
         }
      } finally {
         var0.popPose();
      }
   }

   static void submitGeometry(Pose var0, VertexConsumer var1, LegTrail var2, int var3, int var4, boolean var5, boolean var6) {
      long var7 = PerfProfiler.startTimer();
      ChinaHatMesh.Mesh var9 = ChinaHatMesh.getMesh();
      float[] var10 = var9.uvs();
      float[] var11 = var9.gradient();
      float[] var12 = new float[6];
      int[] var13 = var9.quads();
      float var14 = var6 ? 0.5F : 0.0F;

      for (int var15 = 0; var15 < var9.faces(); var15++) {
         for (int var16 = 0; var16 < 4; var16++) {
            int var17 = var13[var15 * 4 + (var5 ? (4 - var16) % 4 : var16)];
            ChinaHatMesh.deformVertex(var9, var17, var2.length(), var2.thickness(), var5, var12);
            int var18 = var6
               ? ChinaHatMesh.scaleColor(var2.tip(), var2.glow(), var11[var17])
               : ChinaHatMesh.heightFraction(var2.hip(), var2.tip(), var11[var17]);
            var1.addVertex(var0, var12[0] / 16.0F, var12[1] / 16.0F, var12[2] / 16.0F)
               .setColor(var18)
               .setUv(var10[var17 * 2] + var14, var10[var17 * 2 + 1])
               .setOverlay(var4)
               .setLight(var3)
               .setNormal(var0, var12[3], var12[4], var12[5]);
         }
      }

      PerfProfiler.endTimer("Korblox", var7);
   }

   private static final class RenderTypes {
      static final RenderType bODY = net.minecraft.client.renderer.rendertype.RenderTypes.entityCutout(KorbloxLegRenderer.tEXTURE_ID);
      static final RenderType gLOW = net.minecraft.client.renderer.rendertype.RenderTypes.eyes(KorbloxLegRenderer.tEXTURE_ID);
   }
}
