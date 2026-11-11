package com.larpclient.util.math;

import com.larpclient.render.model.CustomItemModel;
import com.larpclient.render.model.KnifeMesh;
import com.larpclient.render.model.WeaponAnimator;
import com.larpclient.util.data.HandPoseSample;
import com.mojang.blaze3d.vertex.PoseStack;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public final class PoseBlender {
   public static final double bLEND_SECONDS = 0.08;
   private KnifeMesh lastAnimation;
   private int lastVariant;
   private int lastTick;
   private float lastScale;
   private double lastTime = Double.NaN;
   private double blendStartTime = Double.NEGATIVE_INFINITY;
   private Matrix4f[] currentPoses;
   private Matrix4f[] previousPoses;

   public void reset() {
      this.lastAnimation = null;
      this.currentPoses = this.previousPoses = null;
      this.lastTime = Double.NaN;
      this.blendStartTime = Double.NEGATIVE_INFINITY;
   }

   public Matrix4f[] computePoses(KnifeMesh var1, CustomItemModel var2, WeaponAnimator.Frame var3, int var4, float var5, double var6) {
      int var8 = this.lastAnimation == var1
            && this.lastVariant == var4
            && this.lastScale == var5
            && Double.isFinite(var6)
            && Double.isFinite(this.lastTime)
            && !(var6 < this.lastTime)
            && !(var6 - this.lastTime > 0.5)
         ? 0
         : 1;
      if (var8 != 0) {
         this.reset();
         this.lastAnimation = var1;
         this.lastVariant = var4;
         this.lastScale = var5;
         this.currentPoses = allocateMatrices(var1.parts().size() + 2);
         this.previousPoses = allocateMatrices(this.currentPoses.length);
      } else if (var3.interruptedAttack || var6 - this.blendStartTime < 0.08 && var3.stage != this.lastTick) {
         for (int var9 = 0; var9 < this.currentPoses.length; var9++) {
            this.previousPoses[var9].set(this.currentPoses[var9]);
         }

         this.blendStartTime = var6;
      }

      HandPoseSample var15 = HandPoseSample.sample(var1, var2, var3.stage, var3.progress, var4, var5);
      this.currentPoses[0].set(var15.weapon());
      this.currentPoses[1].set(var15.arm());
      PoseStack var10 = new PoseStack();

      for (int var11 = 0; var11 < var1.parts().size(); var11++) {
         var10.pushPose();
         var1.parts().get(var11).transform(var10, var3.stage, var3.progress, var4);
         this.currentPoses[var11 + 2].set(var10.last().pose());
         var10.popPose();
      }

      float var16 = (float)Math.clamp((var6 - this.blendStartTime) / 0.08, 0.0, 1.0);
      if (var16 < 1.0F) {
         var16 = 1.0F - (1.0F - var16) * (1.0F - var16);
         Matrix4f var12 = new Matrix4f(this.currentPoses[0]).invert().mul(this.currentPoses[1]);
         Matrix4f var13 = new Matrix4f(this.previousPoses[0]).invert().mul(this.previousPoses[1]);
         blendMatrix(var13, var12, var16);
         blendMatrix(this.previousPoses[0], this.currentPoses[0], var16);
         this.currentPoses[1].set(this.currentPoses[0]).mul(var12);

         for (int var14 = 2; var14 < this.currentPoses.length; var14++) {
            blendMatrix(this.previousPoses[var14], this.currentPoses[var14], var16);
         }
      }

      this.lastTime = var6;
      this.lastTick = var3.stage;
      return this.currentPoses;
   }

   private static Matrix4f[] allocateMatrices(int var0) {
      Matrix4f[] var1 = new Matrix4f[var0];

      for (int var2 = 0; var2 < var0; var2++) {
         var1[var2] = new Matrix4f();
      }

      return var1;
   }

   private static void blendMatrix(Matrix4f var0, Matrix4f var1, float var2) {
      Vector3f var3 = var0.getTranslation(new Vector3f()).lerp(var1.getTranslation(new Vector3f()), var2);
      Quaternionf var4 = var0.getUnnormalizedRotation(new Quaternionf()).normalize().slerp(var1.getUnnormalizedRotation(new Quaternionf()).normalize(), var2);
      Vector3f var5 = var0.getScale(new Vector3f()).lerp(var1.getScale(new Vector3f()), var2);
      var1.translationRotateScale(var3, var4, var5);
   }
}
