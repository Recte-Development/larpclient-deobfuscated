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

public final class wg {
   private static final Identifier T = Identifier.fromNamespaceAndPath("larp-client", "dynamic/korblox_leg");
   private static final int u = 15728880;
   private static final int v = 64;
   private static int U = -1;
   private static int x = -1;

   private wg() {
   }

   public static boolean H() {
      int var0 = wa.s;
      if (U == var0) {
         return true;
      }

      if (x == var0) {
         return false;
      }

      try {
         DynamicTexture var1 = new DynamicTexture(() -> "LARP Korblox leg", 64, 64, false);

         try {
            int[] var2 = com.larpclient.u.x();
            NativeImage var3 = var1.getPixels();

            for (int var4 = 0; var4 < var2.length; var4++) {
               var3.setPixel(var4 % 64, var4 / 64, var2[var4]);
            }

            var1.upload();
            Minecraft.getInstance().getTextureManager().register(T, var1);
         } catch (RuntimeException var5) {
            var1.close();
            throw var5;
         }

         U = var0;
         h.A("Korblox", "Leg ready: {} faces, generated {}x{} texture, resource generation {}", com.larpclient.u.z().faces(), 64, 64, var0);
         return true;
      } catch (RuntimeException var6) {
         x = var0;
         h.m("Korblox", "Leg texture unavailable; native legs stay until resource reload", var6);
         return false;
      }
   }

   public static void j(PoseStack var0, OrderedSubmitNodeCollector var1, ModelPart var2, l8 var3, int var4, int var5, boolean var6) {
      var0.pushPose();

      try {
         var2.translateAndRotate(var0);
         var1.submitCustomGeometry(var0, wg.o.m, (var4x, var5x) -> O(var4x, var5x, var3, var4, var5, var6, false));
         if (var3.glow() > 0.0F) {
            var1.submitCustomGeometry(var0, wg.o.P, (var2x, var3x) -> O(var2x, var3x, var3, 15728880, OverlayTexture.NO_OVERLAY, var6, true));
         }
      } finally {
         var0.popPose();
      }
   }

   static void O(Pose var0, VertexConsumer var1, l8 var2, int var3, int var4, boolean var5, boolean var6) {
      long var7 = zj.m();
      u.f var9 = com.larpclient.u.z();
      float[] var10 = var9.uvs();
      float[] var11 = var9.gradient();
      float[] var12 = new float[6];
      int[] var13 = var9.quads();
      float var14 = var6 ? 0.5F : 0.0F;

      for (int var15 = 0; var15 < var9.faces(); var15++) {
         for (int var16 = 0; var16 < 4; var16++) {
            int var17 = var13[var15 * 4 + (var5 ? (4 - var16) % 4 : var16)];
            com.larpclient.u.m(var9, var17, var2.length(), var2.thickness(), var5, var12);
            int var18 = var6 ? com.larpclient.u.z(var2.tip(), var2.glow(), var11[var17]) : com.larpclient.u.W(var2.hip(), var2.tip(), var11[var17]);
            var1.addVertex(var0, var12[0] / 16.0F, var12[1] / 16.0F, var12[2] / 16.0F)
               .setColor(var18)
               .setUv(var10[var17 * 2] + var14, var10[var17 * 2 + 1])
               .setOverlay(var4)
               .setLight(var3)
               .setNormal(var0, var12[3], var12[4], var12[5]);
         }
      }

      zj.l("Korblox", var7);
   }

   private static final class o {
      static final RenderType m = RenderTypes.entityCutout(wg.T);
      static final RenderType P = RenderTypes.eyes(wg.T);
   }
}
