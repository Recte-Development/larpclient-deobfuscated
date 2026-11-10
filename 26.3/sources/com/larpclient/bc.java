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

public final class bc {
   static final float W = (float)(Math.tan(Math.toRadians(35.0)) / (0.75 * Math.tan(Math.toRadians(34.0))));
   static final float[][] v = new float[][]{
      {0.36F, 0.16F, 0.78F, 1.0F, 0.25F, 0.78F, 0.2F, 0.82F, 1.0F},
      {0.42F, 0.14F, 0.8F, 0.85F, 0.2F, 0.95F, 0.55F, 0.4F, 1.0F},
      {0.62F, 0.07F, 0.25F, 1.0F, 0.42F, 0.55F, 1.0F, 0.7F, 0.3F},
      {0.12F, 0.25F, 0.7F, 0.15F, 0.85F, 1.0F, 0.7F, 0.95F, 1.0F},
      {0.28F, 0.31F, 0.36F, 0.62F, 0.68F, 0.78F, 0.92F, 0.95F, 1.0F}
   };
   private static final float[] k = new float[]{1.3F, 1.0F, 1.9F};
   private static final float[] A = new float[]{0.011F, -0.017F, 0.007F};
   private static final float[] Q = new float[]{-0.031F, -0.052F, -0.095F};
   private static final boolean s = false;

   public static boolean r(
      AbstractClientPlayer var0, InteractionHand var1, ItemStack var2, float var3, float var4, float var5, PoseStack var6, SubmitNodeCollector var7, int var8
   ) {
      SwordModelsModule var9 = SwordModelsModule.p();
      Minecraft var10 = Minecraft.getInstance();
      if (var9 != null && var9.E(var2) && var0 == var10.player && !var0.isScoping()) {
         s_ var11 = var9.V();
         int var12 = var0.getSkin().model() == PlayerModelType.SLIM ? 1 : 0;
         w9 var13 = fb.T(var11, (boolean)var12);
         if (var13 == null) {
            return false;
         }

         HumanoidArm var14 = var1 == InteractionHand.MAIN_HAND ? var0.getMainArm() : var0.getMainArm().getOpposite();
         int var15 = var14 == HumanoidArm.RIGHT ? 1 : -1;
         if (var11.d()) {
            return Z(var0, var1, var14, var15, var2, var3, var4, var5, var6, var7, var8, var9, var13);
         }

         i7 var16 = var9.R(var1);
         if (var0.isUsingItem() || !var10.isWindowActive()) {
            var16.X();
         }

         double var17 = hz.S() + Math.clamp(var5, 0.0F, 1.0F) * 0.05;
         i7.g var19 = var16.q(var9.i(var1, var2), var11, var17, var3, var9.V.getValue());
         Matrix4f[] var20 = var9.D(var1).h(var13, var11, var19, var15, var9.t.getValue().floatValue(), var17);
         Vector3f var21 = new Vector3f(var15 * var9.r.getValue().floatValue(), var9.x.getValue().floatValue() + var19.A, var9.i.getValue().floatValue());
         Vector3f var22 = var20[0].transformPosition(new Vector3f(var15 * var11.E, var11.k, var11.Y));
         var6.pushPose();

         try {
            M(var6, var1, var15, new Vector3f(var22).add(var21).mul(W, W, 1.0F));
            var6.scale(W, W, 1.0F);
            var6.translate(var21.x, var21.y, var21.z);
            if (!var0.isInvisible()) {
               var6.pushPose();

               try {
                  qd.D(var6, var20[1]);
                  var6.scale(0.72F, 0.72F, 0.72F);
                  i(var0, var1, var14, var15, var11, var6, var7, var8);
               } finally {
                  var6.popPose();
               }
            }

            qd.D(var6, var20[0]);

            for (int var23 = 0; var23 < var13.parts().size(); var23++) {
               w9.c var24 = var13.parts().get(var23);
               var6.pushPose();

               try {
                  qd.D(var6, var20[var23 + 2]);
                  jp.n(var1, var15 < 0, var6, var24.vertices(), var15);
                  if (var24.name().equals("blade")) {
                     dh.O(var1, var2, var24.vertices(), var15, var6, var3, var19.B == 3, var7);
                  }

                  if (var24.tint()) {
                     float[] var25 = v[var9.M()];
                     int var26 = LightCoordsUtil.pack(Math.max(LightCoordsUtil.block(var8), 11), LightCoordsUtil.sky(var8));
                     var7.submitCustomGeometry(
                        var6, q(fb.B(0), var9.j.getValue() && var2.hasFoil()), (var6x, var7x) -> V(var24, var6x, var7x, var26, var15, 0, var25, var17)
                     );

                     for (int var27 = 1; var27 <= 2; var27++) {
                        int var28 = var27;
                        var7.order(1)
                           .submitCustomGeometry(
                              var6, RenderTypes.eyes(fb.B(var27)), (var7x, var8x) -> V(var24, var7x, var8x, var26, var15, var28, var25, var17)
                           );
                     }
                  } else {
                     var7.submitCustomGeometry(
                        var6, q(fb.D(var11), var9.j.getValue() && var2.hasFoil()), (var3x, var4x) -> C(var24, var3x, var4x, var8, var15, false)
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

   private static boolean Z(
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
      w9 var12
   ) {
      int var13 = var0.isUsingItem() && var0.getUsedItemHand() == var1 && !SwordModelsModule.m(var4) ? 1 : 0;
      if (var13 != 0) {
         var11.T(var1, var4);
      }

      if (var13 == 0 && !var0.isAutoSpinAttack() && h8.d(var4).type() == SwingAnimationType.WHACK) {
         var8.pushPose();

         try {
            s_ var14 = var11.V();
            double var15 = hz.S() + Math.clamp(var7, 0.0F, 1.0F) * 0.05;
            float var17 = 0.0F;
            b_ var18;
            if (var14 == s_.s) {
               wd var19 = var11.k(var1);
               var19.p(h8.J(var0) == var1, h8.x(var0), var15);
               var17 = var3 * var19.P(var15);
               int var20 = !SwordModelsModule.J(var4, var0.getItemInHand(var1)) ? 1 : 0;
               var18 = dw.f(var3, var19.M(var11.i(var1, var4), var6, (boolean)var20), var11.t.getValue().floatValue());
            } else {
               var18 = dw.F(var3, var5, var6);
            }

            Vector3f var37 = new Vector3f(var3 * var11.r.getValue().floatValue(), var11.x.getValue().floatValue(), var11.i.getValue().floatValue());
            M(var8, var1, var3, var18.weapon().getTranslation(new Vector3f()).add(var37));
            var8.translate(var37.x, var37.y, var37.z);
            if (var14 == s_.r) {
               i7 var38 = var11.R(var1);
               if (!Minecraft.getInstance().isWindowActive()) {
                  var38.X();
               }

               v6.e var21 = var11.s(var1).E(var38.q(var11.i(var1, var4), var14, var15, var5, false), var15);
               var18 = dw.d(var18, var3, var21.phase(), var21.weight());
            }

            if (!var0.isInvisible()) {
               var8.pushPose();

               try {
                  qd.D(var8, var18.arm());
                  var8.scale(0.72F, 0.72F, 0.72F);
                  i(var0, var1, var2, var3, var14, var8, var9, var10);
               } finally {
                  var8.popPose();
               }
            }

            qd.D(var8, var18.weapon());

            for (w9.c var40 : var12.parts()) {
               var8.pushPose();

               try {
                  if (var40.name().equals("case")) {
                     dw.P(var8, var11.t.getValue().floatValue());
                  }

                  int var22 = var14 != s_.s && !var40.name().equals("case") ? var3 : 1;
                  jp.n(var1, var3 < 0, var8, var40.vertices(), var22);
                  if (var17 != 0.0F && var40.name().equals("swivel")) {
                     var8.mulPose(dw.I(var40.x(), var40.y(), var40.z(), var17));
                  }

                  var9.submitCustomGeometry(
                     var8, q(fb.D(var14), var11.j.getValue() && var4.hasFoil()), (var3x, var4x) -> C(var40, var3x, var4x, var10, var22, false)
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

   private static void M(PoseStack var0, InteractionHand var1, int var2, Vector3f var3) {
      ViewModelModule var4 = ViewModelModule.t();
      if (var4 != null && var4.isRuntimeActive()) {
         var4.i(var0, var1, var2 < 0);
         var0.translate(var3.x, var3.y, var3.z);
         var4.n(var0, var1, var2 < 0);
         var0.translate(-var3.x, -var3.y, -var3.z);
      }
   }

   private static void i(
      AbstractClientPlayer var0, InteractionHand var1, HumanoidArm var2, int var3, s_ var4, PoseStack var5, SubmitNodeCollector var6, int var7
   ) {
      AvatarRenderer var8 = (AvatarRenderer)Minecraft.getInstance().getEntityRenderDispatcher().getRenderer(var0);
      var5.pushPose();

      try {
         int var9 = var0.getSkin().model() == PlayerModelType.SLIM ? 1 : 0;
         f_.k(var5, var4, var3, (boolean)var9);
         Identifier var10 = var0.getSkin().body().texturePath();
         if (var2 == HumanoidArm.RIGHT) {
            var8.renderRightHand(var5, var6, var7, var10, var0.isModelPartShown(PlayerModelPart.RIGHT_SLEEVE));
         } else {
            var8.renderLeftHand(var5, var6, var7, var10, var0.isModelPartShown(PlayerModelPart.LEFT_SLEEVE));
         }

         PlayerModel var11 = (PlayerModel)var8.getModel();
         jp.B(var1, var3 < 0, var5, var2 == HumanoidArm.RIGHT ? var11.rightArm : var11.leftArm);
         oj.L(var2, var5, var6, true);
      } finally {
         var5.popPose();
      }
   }

   private static void C(w9.c var0, Pose var1, VertexConsumer var2, int var3, int var4, boolean var5) {
      float[] var6 = var0.vertices();

      for (byte var7 = 0; var7 < var6.length; var7 += 32) {
         for (int var8 = 0; var8 < 4; var8++) {
            int var9 = var7 + w9.vertexIndex(var4, var8) * 8;
            var2.addVertex(var1, var4 * var6[var9], var6[var9 + 1], var6[var9 + 2])
               .setColor(-1)
               .setUv(var5 ? var4 * var6[var9] : var6[var9 + 3], var5 ? var6[var9 + 1] : var6[var9 + 4])
               .setOverlay(OverlayTexture.NO_OVERLAY)
               .setLight(var3)
               .setNormal(var1, var4 * var6[var9 + 5], var6[var9 + 6], var6[var9 + 7]);
         }
      }
   }

   static void V(w9.c var0, Pose var1, VertexConsumer var2, int var3, int var4, int var5, float[] var6, double var7) {
      float[] var9 = var0.vertices();
      float[] var10 = var0.finishUv();
      int[] var11 = var0.finishColor();
      float var12 = k[var5];
      float var13 = (float)(var7 * A[var5] % 1.0) + (var5 == 1 ? 0.37F : 0.0F);
      float var14 = (float)(var7 * Q[var5] % 1.0);
      float var15 = (float)(var7 % 3600.0);

      for (byte var16 = 0; var16 < var9.length; var16 += 32) {
         for (int var17 = 0; var17 < 4; var17++) {
            int var18 = var16 + w9.vertexIndex(var4, var17) * 8;
            float var19 = var10[var18 / 4];
            float var20 = var10[var18 / 4 + 1];
            int var21;
            if (var5 == 0) {
               float var22 = 1.25F * (var11[var18 / 8] & 0xFF) / 255.0F;
               var21 = b(1.0F, var6[0] * var22, var6[1] * var22, var6[2] * var22);
            } else if (var5 == 1) {
               float var23 = 0.5F + 0.5F * (float)Math.sin(var20 * 7.0F - var15 * 1.3F);
               var21 = b(0.85F, var6[3] + (var6[6] - var6[3]) * var23, var6[4] + (var6[7] - var6[4]) * var23, var6[5] + (var6[8] - var6[5]) * var23);
            } else {
               var21 = b(0.75F + 0.25F * (float)Math.sin(var20 * 23.0F + var19 * 11.0F - var15 * 3.1F), 1.0F, 1.0F, 1.0F);
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

   private static int b(float var0, float var1, float var2, float var3) {
      return Math.round(Math.clamp(var0, 0.0F, 1.0F) * 255.0F) << 24
         | Math.round(Math.clamp(var1, 0.0F, 1.0F) * 255.0F) << 16
         | Math.round(Math.clamp(var2, 0.0F, 1.0F) * 255.0F) << 8
         | Math.round(Math.clamp(var3, 0.0F, 1.0F) * 255.0F);
   }

   private bc() {
   }

   private static RenderType q(Identifier var0, boolean var1) {
      return var1 ? RenderTypes.entitySolidGlint(var0) : RenderTypes.entitySolid(var0);
   }

   private static RenderType a() {
      throw new IllegalStateException("26.3 glint is part of the base pass");
   }
}
