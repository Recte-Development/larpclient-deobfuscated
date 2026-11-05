package com.larpclient;

import com.larpclient.module.Category;
import com.larpclient.module.EmotesModule;
import com.larpclient.module.Module;
import net.minecraft.client.model.player.PlayerModel;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;

public final class WalkStyleModule extends Module {
   private static WalkStyleModule a;
   private final com.larpclient.module.lp k = this.addSetting(
      new com.larpclient.module.lp(
         "Style",
         "Local walking choreography",
         "Natural",
         "Natural",
         "Swagger",
         "Robot",
         "Sneaky",
         "Hands in pockets",
         "Ninja run",
         "Zombie",
         "Bouncy",
         "Runway"
      )
   );
   private final com.larpclient.module.lc B = this.addSetting(
      new com.larpclient.module.lc("Strength", "Animation strength in percent", 100.0, 30.0, 150.0, 5.0)
   );
   private int p = Integer.MIN_VALUE;
   private boolean s;

   public WalkStyleModule() {
      super("Walk Style", "Walking animations with articulated knees and elbows", Category.RENDER, -1);
      a = this;
   }

   public static WalkStyleModule f() {
      return a;
   }

   @Override
   public void onTick() {
      int var4;
      label17: {
         this.p = this.isInGame() ? getMc().player.getId() : Integer.MIN_VALUE;
         if (this.isInGame()) {
            var4 = getMc().options.getCameraType().isFirstPerson();
            boolean var2 = false;
            byte var1 = var4;
            var4 = Integer.signum(var1);
            if ((var4 | -var4) >>> 31 == 0) {
               var4 = 1;
               break label17;
            }
         }

         var4 = 0;
      }

      this.s = (boolean)var4;
   }

   @Override
   public void onEnable() {
      this.onTick();
      r4.g("WalkStyle", "Local articulated walking activated");
   }

   @Override
   public void onDisable() {
      this.p = Integer.MIN_VALUE;
   }

   public float[] z(PlayerModel var1, AvatarRenderState var2) {
      return this.isRuntimeActive()
            && this.s
            && var2.id == this.p
            && !var2.isPassenger
            && !var2.isFallFlying
            && !var2.isVisuallySwimming
            && !EmotesModule.h(var2)
         ? f(this.k.getValue(), this.B.getValue().floatValue(), var1, var2)
         : null;
   }

   public static float[] f(String var0, float var1, PlayerModel var2, AvatarRenderState var3) {
      float var4 = Math.min(1.0F, var3.walkAnimationSpeed) * var1 / 100.0F;
      if (var4 < 0.02F) {
         return null;
      }

      float var5 = var3.walkAnimationPos * 0.6662F;
      float var6 = (float)Math.sin(var5);
      float var7 = (float)Math.cos(var5);
      float var8 = Math.max(0.0F, var6);
      float var9 = Math.max(0.0F, -var6);
      float var10 = 0.3F + Math.max(0.0F, -var7) * 0.4F;
      float var11 = 0.3F + Math.max(0.0F, var7) * 0.4F;
      float var12 = var8 * 1.1F;
      float var13 = var9 * 1.1F;
      switch (var0) {
         case "Swagger":
            var2.body.yRot += var6 * 0.28F * var4;
            var2.rightArm.xRot = var7 * 1.25F * var4;
            var2.leftArm.xRot = -var7 * 1.25F * var4;
            var2.rightArm.zRot += 0.18F * var4;
            var2.leftArm.zRot -= 0.18F * var4;
            var2.head.zRot += var6 * 0.08F * var4;
            var10 = 0.45F + Math.max(0.0F, -var7) * 0.4F;
            var11 = 0.45F + Math.max(0.0F, var7) * 0.4F;
            var12 = var8 * 0.9F;
            var13 = var9 * 0.9F;
            break;
         case "Robot":
            float var16 = Math.round(var7 * 2.0F) / 2.0F;
            var2.rightArm.xRot = var16 * 0.8F * var4;
            var2.leftArm.xRot = -var16 * 0.8F * var4;
            var2.rightArm.zRot = var2.leftArm.zRot = 0.0F;
            var2.rightLeg.xRot = -var16 * 0.7F * var4;
            var2.leftLeg.xRot = var16 * 0.7F * var4;
            var2.head.yRot = var2.head.xRot = 0.0F;
            var11 = 1.57F;
            var10 = 1.57F;
            var12 = Math.max(0.0F, var16) * 0.6F;
            var13 = Math.max(0.0F, -var16) * 0.6F;
            break;
         case "Sneaky":
            var2.rightArm.xRot = (-0.7F + var7 * 0.25F) * var4;
            var2.leftArm.xRot = (-0.7F - var7 * 0.25F) * var4;
            var2.rightArm.zRot -= 0.1F * var4;
            var2.leftArm.zRot += 0.1F * var4;
            var2.body.xRot += 0.2F * var4;
            var2.head.xRot += 0.15F * var4;
            var2.rightLeg.xRot = var7 * 0.7F * var4;
            var2.leftLeg.xRot = -var7 * 0.7F * var4;
            var11 = 1.5F;
            var10 = 1.5F;
            var12 = 0.7F + var8 * 0.5F;
            var13 = 0.7F + var9 * 0.5F;
            break;
         case "Hands in pockets":
            var2.rightArm.xRot = var2.leftArm.xRot = 0.2F * var4;
            var2.rightArm.zRot = 0.05F;
            var2.leftArm.zRot = -0.05F;
            var2.rightLeg.xRot *= 0.75F;
            var2.leftLeg.xRot *= 0.75F;
            var2.head.zRot += var6 * 0.05F * var4;
            var11 = 0.9F;
            var10 = 0.9F;
            var12 = var8 * 0.6F;
            var13 = var9 * 0.6F;
            break;
         case "Ninja run":
            var2.rightArm.xRot = var2.leftArm.xRot = 1.25F * var4;
            var2.rightArm.zRot = 0.2F * var4;
            var2.leftArm.zRot = -0.2F * var4;
            var2.body.xRot += 0.35F * var4;
            var2.head.xRot -= 0.2F * var4;
            var2.rightLeg.xRot = var7 * 1.6F * var4;
            var2.leftLeg.xRot = -var7 * 1.6F * var4;
            var11 = 0.1F;
            var10 = 0.1F;
            var12 = var8 * 1.5F;
            var13 = var9 * 1.5F;
            break;
         case "Zombie":
            var2.rightArm.xRot = -1.45F + var6 * 0.06F;
            var2.leftArm.xRot = -1.45F - var6 * 0.06F;
            var2.rightLeg.xRot *= 0.45F;
            var2.leftLeg.xRot *= 0.45F;
            var2.head.zRot += var6 * 0.12F * var4;
            var2.body.zRot += var6 * 0.05F * var4;
            var11 = 0.15F;
            var10 = 0.15F;
            var12 = var8 * 0.3F;
            var13 = var9 * 0.3F;
            break;
         case "Bouncy":
            var2.rightArm.xRot = var7 * 1.3F * var4;
            var2.leftArm.xRot = -var7 * 1.3F * var4;
            var2.rightLeg.xRot = var7 * 1.5F * var4;
            var2.leftLeg.xRot = -var7 * 1.5F * var4;
            var2.head.xRot = var2.head.xRot + Math.abs(var6) * 0.12F * var4;
            var11 = 0.8F;
            var10 = 0.8F;
            var12 = var8 * 1.6F;
            var13 = var9 * 1.6F;
            break;
         case "Runway":
            var2.body.zRot += var6 * 0.1F * var4;
            var2.body.yRot += var6 * 0.12F * var4;
            var2.rightArm.xRot = var7 * 0.45F * var4;
            var2.leftArm.xRot = -var7 * 0.45F * var4;
            var2.rightLeg.yRot = -0.18F * var4;
            var2.leftLeg.yRot = 0.18F * var4;
            var2.head.zRot -= var6 * 0.04F * var4;
            var11 = 0.25F;
            var10 = 0.25F;
            var12 = var8 * 0.7F;
            var13 = var9 * 0.7F;
      }

      return new float[]{-var10 * var4, -var11 * var4, var12 * var4, var13 * var4};
   }
}
