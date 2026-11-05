package com.larpclient;

import com.larpclient.module.SwordModelsModule;
import com.larpclient.module.ViewModelModule;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.PoseStack.Pose;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.player.PlayerModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.player.AvatarRenderer;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;
import net.minecraft.util.LightCoordsUtil;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.player.PlayerModelPart;
import net.minecraft.world.entity.player.PlayerModelType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwingAnimationType;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public final class yb {
   static final float X = (float)(Math.tan(Math.toRadians(35.0)) / (0.75 * Math.tan(Math.toRadians(34.0))));
   static final float[][] a = new float[][]{
      {0.36F, 0.16F, 0.78F, 1.0F, 0.25F, 0.78F, 0.2F, 0.82F, 1.0F},
      {0.42F, 0.14F, 0.8F, 0.85F, 0.2F, 0.95F, 0.55F, 0.4F, 1.0F},
      {0.62F, 0.07F, 0.25F, 1.0F, 0.42F, 0.55F, 1.0F, 0.7F, 0.3F},
      {0.12F, 0.25F, 0.7F, 0.15F, 0.85F, 1.0F, 0.7F, 0.95F, 1.0F},
      {0.28F, 0.31F, 0.36F, 0.62F, 0.68F, 0.78F, 0.92F, 0.95F, 1.0F}
   };
   private static final float[] S = new float[]{1.3F, 1.0F, 1.9F};
   private static final float[] o = new float[]{0.011F, -0.017F, 0.007F};
   private static final float[] f = new float[]{-0.031F, -0.052F, -0.095F};
   private static final boolean q = false;

   public static boolean f(
      AbstractClientPlayer var0, InteractionHand var1, ItemStack var2, float var3, float var4, float var5, PoseStack var6, SubmitNodeCollector var7, int var8
   ) {
      SwordModelsModule var9 = SwordModelsModule.t();
      Minecraft var10 = Minecraft.getInstance();
      if (var9 != null && var9.p(var2) && var0 == var10.player && !var0.isScoping()) {
         yi var11 = var9.T();
         int var12 = var0.getSkin().model() == PlayerModelType.SLIM ? 1 : 0;
         _r var13 = jv.N(var11, (boolean)var12);
         if (var13 == null) {
            return false;
         }

         HumanoidArm var14 = var1 == InteractionHand.MAIN_HAND ? var0.getMainArm() : var0.getMainArm().getOpposite();
         int var15 = var14 == HumanoidArm.RIGHT ? 1 : -1;
         if (var11.E()) {
            return n(var0, var1, var14, var15, var2, var3, var4, var5, var6, var7, var8, var9, var13);
         }

         xh var16 = var9.T(var1);
         if (var0.isUsingItem() || !var10.isWindowActive()) {
            var16.P();
         }

         double var17 = ea.t() + Math.clamp(var5, 0.0F, 1.0F) * 0.05;
         xh.n var19 = var16.X(var2, var11, var17, var3, var9.x.getValue());
         Matrix4f[] var20 = var9.z(var1).X(var13, var11, var19, var15, var9.V.getValue().floatValue(), var17);
         Vector3f var21 = new Vector3f(var15 * var9.d.getValue().floatValue(), var9.A.getValue().floatValue() + var19.I, var9.q.getValue().floatValue());
         Vector3f var22 = var20[0].transformPosition(new Vector3f(var15 * var11.r, var11.K, var11.i));
         var6.pushPose();

         try {
            r(var6, var1, var15, new Vector3f(var22).add(var21).mul(X, X, 1.0F));
            var6.scale(X, X, 1.0F);
            var6.translate(var21.x, var21.y, var21.z);
            if (!var0.isInvisible()) {
               var6.pushPose();

               try {
                  fx.Z(var6, var20[1]);
                  var6.scale(0.72F, 0.72F, 0.72F);
                  F(var0, var1, var14, var15, var11, var6, var7, var8);
               } finally {
                  var6.popPose();
               }
            }

            fx.Z(var6, var20[0]);

            for (int var23 = 0; var23 < var13.parts().size(); var23++) {
               _r.y var24 = var13.parts().get(var23);
               var6.pushPose();

               try {
                  fx.Z(var6, var20[var23 + 2]);
                  fg.K(var1, var15 < 0, var6, var24.vertices(), var15);
                  if (var24.name().equals("blade")) {
                     ym.p(var1, var2, var24.vertices(), var15, var6, var3, var19.k == 3, var7);
                  }

                  if (var24.tint()) {
                     float[] var25 = a[var9.Y()];
                     int var26 = LightCoordsUtil.pack(Math.max(LightCoordsUtil.block(var8), 11), LightCoordsUtil.sky(var8));
                     var7.submitCustomGeometry(
                        var6, d(jv.n(0), var9.r.getValue() && var2.hasFoil()), (var6x, var7x) -> B(var24, var6x, var7x, var26, var15, 0, var25, var17)
                     );

                     for (int var27 = 1; var27 <= 2; var27++) {
                        int var28 = var27;
                        var7.order(1)
                           .submitCustomGeometry(
                              var6, RenderTypes.eyes(jv.n(var27)), (var7x, var8x) -> B(var24, var7x, var8x, var26, var15, var28, var25, var17)
                           );
                     }
                  } else {
                     var7.submitCustomGeometry(
                        var6, d(jv.H(var11), var9.r.getValue() && var2.hasFoil()), (var3x, var4x) -> F(var24, var3x, var4x, var8, var15, false)
                     );
                  }
               } finally {
                  var6.popPose();
               }
            }

            return true;
         } finally {
            var6.popPose();
         }
      } else {
         return false;
      }
   }

   private static boolean n(
      AbstractClientPlayer var0,
      InteractionHand var1,
      HumanoidArm var2,
      int var3,
      ItemStack var4,
      float var5,
      float var6,
      float var7,
      PoseStack var8,
      SubmitNodeCollector var9,
      int var10,
      SwordModelsModule var11,
      _r var12
   ) {
      if (!var0.isUsingItem() && !var0.isAutoSpinAttack() && hp.m(var4).type() == SwingAnimationType.WHACK) {
         var8.pushPose();

         try {
            yi var13 = var11.T();
            e2 var14 = var13 == yi.D ? rc.A(var3, var5, var6, var11.V.getValue().floatValue()) : rc.p(var3, var5, var6);
            Vector3f var15 = new Vector3f(var3 * var11.d.getValue().floatValue(), var11.A.getValue().floatValue(), var11.q.getValue().floatValue());
            r(var8, var1, var3, var14.weapon().getTranslation(new Vector3f()).add(var15));
            var8.translate(var15.x, var15.y, var15.z);
            if (var13 == yi.m) {
               xh var16 = var11.T(var1);
               if (!Minecraft.getInstance().isWindowActive()) {
                  var16.P();
               }

               double var17 = ea.t() + Math.clamp(var7, 0.0F, 1.0F) * 0.05;
               xt.i var19 = var11.K(var1).y(var16.X(var4, var13, var17, var5, false), var17);
               var14 = rc.a(var14, var3, var19.phase(), var19.weight());
            }

            if (!var0.isInvisible()) {
               var8.pushPose();

               try {
                  fx.Z(var8, var14.arm());
                  var8.scale(0.72F, 0.72F, 0.72F);
                  F(var0, var1, var2, var3, var13, var8, var9, var10);
               } finally {
                  var8.popPose();
               }
            }

            fx.Z(var8, var14.weapon());

            for (_r.y var36 : var12.parts()) {
               var8.pushPose();

               try {
                  if (var36.name().equals("case")) {
                     rc.p(var8, var11.V.getValue().floatValue());
                  }

                  int var18 = var13 != yi.D && !var36.name().equals("case") ? var3 : 1;
                  fg.K(var1, var3 < 0, var8, var36.vertices(), var18);
                  var9.submitCustomGeometry(
                     var8, d(jv.H(var13), var11.r.getValue() && var4.hasFoil()), (var3x, var4x) -> F(var36, var3x, var4x, var10, var18, false)
                  );
               } finally {
                  var8.popPose();
               }
            }

            return true;
         } finally {
            var8.popPose();
         }
      } else {
         return false;
      }
   }

   private static void r(PoseStack var0, InteractionHand var1, int var2, Vector3f var3) {
      ViewModelModule var4 = ViewModelModule.p();
      if (var4 != null && var4.isRuntimeActive()) {
         var4.X(var0, var1, var2 < 0);
         var0.translate(var3.x, var3.y, var3.z);
         var4.o(var0, var1, var2 < 0);
         var0.translate(-var3.x, -var3.y, -var3.z);
      }
   }

   private static void F(
      AbstractClientPlayer var0, InteractionHand var1, HumanoidArm var2, int var3, yi var4, PoseStack var5, SubmitNodeCollector var6, int var7
   ) {
      AvatarRenderer var8 = (AvatarRenderer)Minecraft.getInstance().getEntityRenderDispatcher().getRenderer(var0);
      var5.pushPose();

      try {
         int var9 = var0.getSkin().model() == PlayerModelType.SLIM ? 1 : 0;
         eq.T(var5, var4, var3, (boolean)var9);
         Identifier var10 = var0.getSkin().body().texturePath();
         if (var2 == HumanoidArm.RIGHT) {
            var8.renderRightHand(var5, var6, var7, var10, var0.isModelPartShown(PlayerModelPart.RIGHT_SLEEVE));
         } else {
            var8.renderLeftHand(var5, var6, var7, var10, var0.isModelPartShown(PlayerModelPart.LEFT_SLEEVE));
         }

         PlayerModel var11 = (PlayerModel)var8.getModel();
         fg.J(var1, var3 < 0, var5, var2 == HumanoidArm.RIGHT ? var11.rightArm : var11.leftArm);
         m7.d(var2, var5, var6, true);
      } finally {
         var5.popPose();
      }
   }

   private static void F(_r.y var0, Pose var1, VertexConsumer var2, int var3, int var4, boolean var5) {
      float[] var6 = var0.vertices();

      for (byte var7 = 0; var7 < var6.length; var7 += 32) {
         for (int var8 = 0; var8 < 4; var8++) {
            int var9 = var7 + _r.vertexIndex(var4, var8) * 8;
            var2.addVertex(var1, var4 * var6[var9], var6[var9 + 1], var6[var9 + 2])
               .setColor(-1)
               .setUv(var5 ? var4 * var6[var9] : var6[var9 + 3], var5 ? var6[var9 + 1] : var6[var9 + 4])
               .setOverlay(OverlayTexture.NO_OVERLAY)
               .setLight(var3)
               .setNormal(var1, var4 * var6[var9 + 5], var6[var9 + 6], var6[var9 + 7]);
         }
      }
   }

   static void B(_r.y var0, Pose var1, VertexConsumer var2, int var3, int var4, int var5, float[] var6, double var7) {
      float[] var9 = var0.vertices();
      float[] var10 = var0.finishUv();
      int[] var11 = var0.finishColor();
      float var12 = S[var5];
      float var13 = (float)(var7 * o[var5] % 1.0) + (var5 == 1 ? 0.37F : 0.0F);
      float var14 = (float)(var7 * f[var5] % 1.0);
      float var15 = (float)(var7 % 3600.0);

      for (byte var16 = 0; var16 < var9.length; var16 += 32) {
         for (int var17 = 0; var17 < 4; var17++) {
            int var18 = var16 + _r.vertexIndex(var4, var17) * 8;
            float var19 = var10[var18 / 4];
            float var20 = var10[var18 / 4 + 1];
            int var21;
            if (var5 == 0) {
               float var22 = 1.25F * (var11[var18 / 8] & 0xFF) / 255.0F;
               var21 = p(1.0F, var6[0] * var22, var6[1] * var22, var6[2] * var22);
            } else if (var5 == 1) {
               float var23 = 0.5F + 0.5F * (float)Math.sin(var20 * 7.0F - var15 * 1.3F);
               var21 = p(0.85F, var6[3] + (var6[6] - var6[3]) * var23, var6[4] + (var6[7] - var6[4]) * var23, var6[5] + (var6[8] - var6[5]) * var23);
            } else {
               var21 = p(0.75F + 0.25F * (float)Math.sin(var20 * 23.0F + var19 * 11.0F - var15 * 3.1F), 1.0F, 1.0F, 1.0F);
            }

            var2.addVertex(var1, var4 * var9[var18], var9[var18 + 1], var9[var18 + 2])
               .setColor(var21)
               .setUv(var19 * var12 + var13, var20 * var12 + var14)
               .setOverlay(OverlayTexture.NO_OVERLAY)
               .setLight(var3)
               .setNormal(var1, var4 * var9[var18 + 5], var9[var18 + 6], var9[var18 + 7]);
         }
      }
   }

   private static int p(float var0, float var1, float var2, float var3) {
      return Math.round(Math.clamp(var0, 0.0F, 1.0F) * 255.0F) << 24
         | Math.round(Math.clamp(var1, 0.0F, 1.0F) * 255.0F) << 16
         | Math.round(Math.clamp(var2, 0.0F, 1.0F) * 255.0F) << 8
         | Math.round(Math.clamp(var3, 0.0F, 1.0F) * 255.0F);
   }

   private yb() {
   }

   private static RenderType d(Identifier var0, boolean var1) {
      return var1 ? RenderTypes.entitySolidGlint(var0) : RenderTypes.entitySolid(var0);
   }

   private static RenderType U() {
      throw new IllegalStateException("26.3 glint is part of the base pass");
   }
}
