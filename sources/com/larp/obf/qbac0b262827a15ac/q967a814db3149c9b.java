package com.larp.obf.qbac0b262827a15ac;

import com.larpclient.tq;
import com.larpclient.module.Feature3DUIModule;
import com.larpclient.module.FreelookModule;
import com.larpclient.module.GUIAnimationModule;
import com.larpclient.module.RemovalsModule;
import com.larpclient.module.SmoothCameraModule;
import com.larpclient.module.ZoomModule;
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
public abstract class q967a814db3149c9b {
   @Unique
   private float Q = 4.0F;
   @Shadow
   private float eyeHeight;
   @Shadow
   private float eyeHeightOld;

   @Inject(method = "extractRenderState", at = @At("TAIL"))
   private void J(CameraRenderState var1, DeltaTracker var2, CallbackInfo var3) {
      if (tq.f()) {
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
   private void o(float var1, CallbackInfo var2) {
      if (!tq.q()) {
         SmoothCameraModule var3 = SmoothCameraModule.d();
         if (var3 != null) {
            Camera var4 = (Camera)this;
            Vec3 var5 = var3.M(var4, var4.position());
            this.setPosition(var5.x, var5.y, var5.z);
         }
      }
   }

   @ModifyArg(method = "alignWithEntity", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Camera;getMaxZoom(F)F"), index = 0)
   private float k(float var1) {
      this.Q = var1;
      GUIAnimationModule var2 = GUIAnimationModule.F();
      return var2 != null && var2.i() ? var1 * var2.b(true) : var1;
   }

   @Inject(method = "alignWithEntity", at = @At("TAIL"))
   private void r(float var1, CallbackInfo var2) {
      Minecraft var3 = Minecraft.getInstance();
      GUIAnimationModule var4 = GUIAnimationModule.F();
      if (var4 != null && var4.i() && var3.options.getCameraType().isFirstPerson() && var3.player != null && !var3.player.isSleeping()) {
         float var5 = var4.b(false);
         if (var5 > 0.001F) {
            this.move(-this.invokeGetMaxZoom(this.Q * var5), 0.0F, 0.0F);
         }
      }
   }

   @Inject(method = "calculateFov", at = @At("RETURN"), cancellable = true)
   private void q(float var1, CallbackInfoReturnable<Float> var2) {
      float var3 = (Float)var2.getReturnValue();
      if (RemovalsModule.r("fov")) {
         if (!((Camera)this).isPanoramicMode()) {
            var3 = ((Integer)Minecraft.getInstance().options.fov().get()).floatValue();
         }
      } else {
         GUIAnimationModule var4 = GUIAnimationModule.F();
         if (var4 != null) {
            var3 = var4.J(var3);
         }
      }

      var2.setReturnValue(ZoomModule.w(var3, ((Camera)this).isPanoramicMode()));
   }

   @Inject(method = "update", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Camera;alignWithEntity(F)V", shift = Shift.AFTER))
   private void m(DeltaTracker var1, CallbackInfo var2) {
      Entity var3 = ((Camera)this).entity();
      if (tq.f()) {
         Vec3 var14 = tq.N(((Camera)this).getCameraEntityPartialTicks(var1));
         this.setRotation(tq.J(), tq.F());
         this.setPosition(var14.x, var14.y, var14.z);
      } else if (tq.q() && var3 != null) {
         Minecraft var13 = Minecraft.getInstance();
         float var15 = tq.J();
         float var16 = tq.F();
         this.setRotation(var15, var16);
         float var17 = ((Camera)this).getCameraEntityPartialTicks(var1);
         Vec3 var18 = var3.getPosition(var17).add(0.0, var3.getEyeHeight(), 0.0);
         SmoothCameraModule var19 = SmoothCameraModule.d();
         if (var19 != null) {
            var18 = var19.M((Camera)this, var18);
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
      } else if (FreelookModule.D() && var3 != null) {
         float var4 = FreelookModule.C();
         float var5 = FreelookModule.X();
         float var6 = FreelookModule.V();
         float var7 = ((Camera)this).getCameraEntityPartialTicks(var1);
         Vec3 var8 = var3.getPosition(var7);
         double var9 = var8.y + var3.getEyeHeight();
         this.setRotation(var4, var5);
         this.setPosition(var8.x, var9, var8.z);
         this.move(-var6, 0.0F, 0.0F);
      }
   }

   @Inject(method = "update", at = @At("HEAD"))
   private void l(DeltaTracker var1, CallbackInfo var2) {
      Feature3DUIModule.Y();
   }

   @Inject(method = "update", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Camera;calculateFov(F)F"))
   private void i(DeltaTracker var1, CallbackInfo var2) {
      Feature3DUIModule var3 = Feature3DUIModule.c();
      if (var3 != null) {
         Camera var4 = (Camera)this;
         float var5 = var4.getCameraEntityPartialTicks(var1);
         Feature3DUIModule.v var6 = var3.a(var4.position(), var4.yRot(), var4.xRot(), var4.entity(), var5, Mth.lerp(var5, this.eyeHeightOld, this.eyeHeight));
         if (var6 != null) {
            this.setRotation(var6.yaw(), var6.pitch());
            this.setPosition(var6.x(), var6.y(), var6.z());
         }
      }
   }

   @Inject(method = "getMaxZoom", at = @At("HEAD"), cancellable = true)
   private void P(float var1, CallbackInfoReturnable<Float> var2) {
      if (RemovalsModule.r("clip")) {
         var2.setReturnValue(var1);
      }
   }

   @Inject(method = "modifyFovBasedOnDeathOrFluid", at = @At("HEAD"), cancellable = true)
   private void r(float var1, float var2, CallbackInfoReturnable<Float> var3) {
      if (RemovalsModule.r("fov")) {
         var3.setReturnValue(var2);
      }
   }
}
