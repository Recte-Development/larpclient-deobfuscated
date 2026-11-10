package com.larpclient;

import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.PoseStack.Pose;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.OrderedSubmitNodeCollector;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;

public final class f3 {
   private static final Identifier P = Identifier.fromNamespaceAndPath("larp-client", "dynamic/korblox_leg");
   private static final int G = 15728880;
   private static final int d = 64;
   private static int c = -1;
   private static int V = -1;

   private f3() {
   }

   public static boolean M() {
      int var0 = nl.q;
      if (c == var0) {
         return true;
      }

      if (V == var0) {
         return false;
      }

      try {
         DynamicTexture var1 = new DynamicTexture(() -> "LARP Korblox leg", 64, 64, false);

         try {
            int[] var2 = w5.M();
            NativeImage var3 = var1.getPixels();

            for (int var4 = 0; var4 < var2.length; var4++) {
               var3.setPixel(var4 % 64, var4 / 64, var2[var4]);
            }

            var1.upload();
            Minecraft.getInstance().getTextureManager().register(P, var1);
         } catch (RuntimeException var5) {
            var1.close();
            throw var5;
         }

         c = var0;
         _q.z("Korblox", "Leg ready: {} faces, generated {}x{} texture, resource generation {}", w5.b().faces(), 64, 64, var0);
         return true;
      } catch (RuntimeException var6) {
         V = var0;
         _q.o("Korblox", "Leg texture unavailable; native legs stay until resource reload", var6);
         return false;
      }
   }

   public static void t(PoseStack var0, OrderedSubmitNodeCollector var1, ModelPart var2, a1 var3, int var4, int var5, boolean var6) {
      var0.pushPose();

      try {
         var2.translateAndRotate(var0);
         var1.submitCustomGeometry(var0, f3.d.K, (var4x, var5x) -> J(var4x, var5x, var3, var4, var5, var6, false));
         if (var3.glow() > 0.0F) {
            var1.submitCustomGeometry(var0, f3.d.r, (var2x, var3x) -> J(var2x, var3x, var3, 15728880, OverlayTexture.NO_OVERLAY, var6, true));
         }
      } finally {
         var0.popPose();
      }
   }

   static void J(Pose var0, VertexConsumer var1, a1 var2, int var3, int var4, boolean var5, boolean var6) {
      long var7 = he.s();
      w5.k var9 = w5.b();
      float[] var10 = var9.uvs();
      float[] var11 = var9.gradient();
      float[] var12 = new float[6];
      int[] var13 = var9.quads();
      float var14 = var6 ? 0.5F : 0.0F;

      for (int var15 = 0; var15 < var9.faces(); var15++) {
         for (int var16 = 0; var16 < 4; var16++) {
            int var17 = var13[var15 * 4 + (var5 ? (4 - var16) % 4 : var16)];
            w5.Y(var9, var17, var2.length(), var2.thickness(), var5, var12);
            int var18 = var6 ? w5.n(var2.tip(), var2.glow(), var11[var17]) : w5.q(var2.hip(), var2.tip(), var11[var17]);
            var1.addVertex(var0, var12[0] / 16.0F, var12[1] / 16.0F, var12[2] / 16.0F)
               .setColor(var18)
               .setUv(var10[var17 * 2] + var14, var10[var17 * 2 + 1])
               .setOverlay(var4)
               .setLight(var3)
               .setNormal(var0, var12[3], var12[4], var12[5]);
         }
      }

      he.T("Korblox", var7);
   }

   private static final class d {
      static final RenderType K = RenderTypes.entityCutout(f3.P);
      static final RenderType r = RenderTypes.eyes(f3.P);
   }
}
