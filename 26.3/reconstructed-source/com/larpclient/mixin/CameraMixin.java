package com.larpclient.mixin;

import com.larpclient.core.ViewController;
import com.larpclient.module.render.CustomFOVModule;
import com.larpclient.module.render.Feature3DUIModule;
import com.larpclient.module.render.FreelookModule;
import com.larpclient.module.render.GUIAnimationModule;
import com.larpclient.module.render.RemovalsModule;
import com.larpclient.module.render.SmoothCameraModule;
import com.larpclient.module.render.ZoomModule;
import net.minecraft.client.Camera;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Camera.class)
public abstract class CameraMixin {
   @Unique
   private float A = 4.0F;
   @Shadow
   private float eyeHeight;
   @Shadow
   private float eyeHeightOld;

   @Inject(method = "extractRenderState", at = @At("TAIL"))
   private void B(CameraRenderState var1, DeltaTracker var2, CallbackInfo var3) {
      if (ViewController.isFreecam()) {
         var1.smartCull = false;
      }
   }

   @Shadow
   protected abstract void setRotation(float var1, float var2);

   @Shadow
   protected abstract void move(float var1, float var2, float var3);

   @Invoker("getMaxZoom")
   protected abstract float invokeGetMaxZoom(float var1);

   @Shadow
   protected abstract void setPosition(double var1, double var3, double var5);

   @Inject(
      method = "alignWithEntity",
      at = {
            @At(value = "INVOKE", target = "Lnet/minecraft/client/Camera;setPosition(DDD)V", shift = Shift.AFTER),
            @At(value = "INVOKE", target = "Lnet/minecraft/client/Camera;setPosition(Lnet/minecraft/world/phys/Vec3;)V", shift = Shift.AFTER)
      }
   )
   private void e(float var1, CallbackInfo var2) {
      if (!ViewController.isActive()) {
         SmoothCameraModule var3 = SmoothCameraModule.getInstance();
         if (var3 != null) {
            Camera var4 = (Camera)this;
            Vec3 var5 = var3.smoothPosition(var4, var4.position());
            this.setPosition(var5.x, var5.y, var5.z);
         }
      }
   }

   @ModifyArg(method = "alignWithEntity", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Camera;getMaxZoom(F)F"), index = 0)
   private float c(float var1) {
      this.A = var1;
      GUIAnimationModule var2 = GUIAnimationModule.getInstance();
      return var2 != null && var2.getPerspectiveProgress() ? var1 * var2.getPerspectiveProgress(true) : var1;
   }

   @Inject(method = "alignWithEntity", at = @At("TAIL"))
   private void w(float var1, CallbackInfo var2) {
      Minecraft var3 = Minecraft.getInstance();
      GUIAnimationModule var4 = GUIAnimationModule.getInstance();
      if (var4 != null && var4.getPerspectiveProgress() && var3.options.getCameraType().isFirstPerson() && var3.player != null && !var3.player.isSleeping()) {
         float var5 = var4.getPerspectiveProgress(false);
         if (var5 > 0.001F) {
            this.move(-this.invokeGetMaxZoom(this.A * var5), 0.0F, 0.0F);
         }
      }
   }

   @Inject(method = "calculateFov", at = @At("RETURN"), cancellable = true)
   private void G(float var1, CallbackInfoReturnable<Float> var2) {
      float var3 = (Float)var2.getReturnValue();
      if (RemovalsModule.isRemovalEnabled("fov")) {
         if (!((Camera)this).isPanoramicMode()) {
            var3 = ((Integer)Minecraft.getInstance().options.fov().get()).floatValue();
         }
      } else {
         GUIAnimationModule var4 = GUIAnimationModule.getInstance();
         if (var4 != null) {
            var3 = var4.smoothFov(var3);
         }
      }

      if (!((Camera)this).isPanoramicMode()) {
         var3 = CustomFOVModule.getFov(var3);
      }

      var2.setReturnValue(ZoomModule.applyFov(var3, ((Camera)this).isPanoramicMode()));
   }

   @Inject(method = "calculateHudFov", at = @At("RETURN"), cancellable = true)
   private void i(float var1, CallbackInfoReturnable<Float> var2) {
      var2.setReturnValue(CustomFOVModule.getFov((Float)var2.getReturnValue()));
   }

   @Inject(method = "update", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Camera;alignWithEntity(F)V", shift = Shift.AFTER))
   private void i(DeltaTracker var1, CallbackInfo var2) {
      Entity var3 = ((Camera)this).entity();
      if (ViewController.isFreecam()) {
         Vec3 var14 = ViewController.getPosition(((Camera)this).getCameraEntityPartialTicks(var1));
         this.setRotation(ViewController.getRawYaw(), ViewController.getPosition());
         this.setPosition(var14.x, var14.y, var14.z);
      } else if (ViewController.isActive() && var3 != null) {
         Minecraft var13 = Minecraft.getInstance();
         float var15 = ViewController.getRawYaw();
         float var16 = ViewController.getPosition();
         this.setRotation(var15, var16);
         float var17 = ((Camera)this).getCameraEntityPartialTicks(var1);
         Vec3 var18 = var3.getPosition(var17).add(0.0, var3.getEyeHeight(), 0.0);
         SmoothCameraModule var19 = SmoothCameraModule.getInstance();
         if (var19 != null) {
            var18 = var19.smoothPosition((Camera)this, var18);
         }

         this.setPosition(var18.x, var18.y, var18.z);
         if (!var13.options.getCameraType().isFirstPerson()) {
            float var10 = 4.0F;
            float var11 = 1.0F;
            if (var3 instanceof LivingEntity var12) {
               var10 = (float)var12.getAttributeValue(Attributes.CAMERA_DISTANCE);
               var11 = var12.getScale();
            }

            this.move(-this.invokeGetMaxZoom(var10 * var11), 0.0F, 0.0F);
         }
      } else if (FreelookModule.isFreelookActive() && var3 != null) {
         float var4 = FreelookModule.getYaw();
         float var5 = FreelookModule.getPitch();
         float var6 = FreelookModule.getDistance();
         float var7 = ((Camera)this).getCameraEntityPartialTicks(var1);
         Vec3 var8 = var3.getPosition(var7);
         double var9 = var8.y + var3.getEyeHeight();
         this.setRotation(var4, var5);
         this.setPosition(var8.x, var9, var8.z);
         this.move(-var6, 0.0F, 0.0F);
      }
   }

   @Inject(method = "update", at = @At("HEAD"))
   private void N(DeltaTracker var1, CallbackInfo var2) {
      Feature3DUIModule.tickCamera();
   }

   @Inject(method = "update", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Camera;calculateFov(F)F"))
   private void q(DeltaTracker var1, CallbackInfo var2) {
      Feature3DUIModule var3 = Feature3DUIModule.getInstance();
      if (var3 != null) {
         Camera var4 = (Camera)this;
         float var5 = var4.getCameraEntityPartialTicks(var1);
         Feature3DUIModule.MenuPose var6 = var3.computeMenuPose(
            var4.position(), var4.yRot(), var4.xRot(), var4.entity(), var5, Mth.lerp(var5, this.eyeHeightOld, this.eyeHeight)
         );
         if (var6 != null) {
            this.setRotation(var6.yaw(), var6.pitch());
            this.setPosition(var6.getX(), var6.getY(), var6.getZ());
         }
      }
   }

   @Inject(method = "getMaxZoom", at = @At("HEAD"), cancellable = true)
   private void X(float var1, CallbackInfoReturnable<Float> var2) {
      if (RemovalsModule.isRemovalEnabled("clip")) {
         var2.setReturnValue(var1);
      }
   }

   @Inject(method = "modifyFovBasedOnDeathOrFluid", at = @At("HEAD"), cancellable = true)
   private void E(float var1, float var2, CallbackInfoReturnable<Float> var3) {
      if (RemovalsModule.isRemovalEnabled("fov")) {
         var3.setReturnValue(var2);
      }
   }
}
