package com.larpclient.mixin;

import com.larpclient.LarpClient;
import com.larpclient.core.ViewController;
import com.larpclient.module.combat.AuraModule;
import com.larpclient.module.combat.MaceModule;
import com.larpclient.module.combat.WTapModule;
import com.larpclient.module.movement.AutoSprintModule;
import com.larpclient.module.movement.GrimSpeedModule;
import com.larpclient.module.movement.TargetStrafeModule;
import com.larpclient.module.player.ScaffoldModule;
import com.larpclient.util.entity.CombatUtil;
import com.larpclient.util.entity.MovementCorrection;
import com.larpclient.util.misc.InteractionArbiter;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.phys.Vec2;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LocalPlayer.class)
public class LocalPlayerMixin_5 {
   @Inject(method = "aiStep()V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/ClientInput;tick()V", shift = Shift.AFTER))
   private void S(CallbackInfo var1) {
      LocalPlayer var2 = (LocalPlayer)this;
      var2.input.keyPresses = AutoSprintModule.applyInput(var2, var2.input.keyPresses);
      var2.input.keyPresses = AuraModule.getAimSettings(var2, var2.input.keyPresses);
      var2.input.keyPresses = TargetStrafeModule.modifyInput(var2, var2.input.keyPresses);
      var2.input.keyPresses = GrimSpeedModule.modifyInput(var2, var2.input.keyPresses);
      var2.input.keyPresses = WTapModule.modifyInput(var2, var2.input.keyPresses);
      var2.input.keyPresses = MaceModule.lockSprintInput(var2, var2.input.keyPresses);
      var2.input.keyPresses = MovementCorrection.correctInput(var2, var2.input.keyPresses);
      var2.input.keyPresses = ScaffoldModule.applySafeWalk(var2, var2.input.keyPresses);
      if (MaceModule.isViewOwned() || AuraModule.isSprintResetActive()) {
         var2.input.keyPresses = CombatUtil.hasTickedThisTick(var2.input.keyPresses);
      }

      AuraModule.getCombatSettings(var2, "input-final");
   }

   @WrapOperation(method = "applyInput", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;getYRot()F"), require = 1, allow = 1)
   private float U(LocalPlayer var1, Operation<Float> var2) {
      float var3 = ViewController.getYaw(var1.yBob);
      return Float.isNaN(var3) ? (Float)var2.call(new Object[]{var1}) : var3;
   }

   @WrapOperation(method = "applyInput", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;getXRot()F"), require = 1, allow = 1)
   private float C(LocalPlayer var1, Operation<Float> var2) {
      float var3 = ViewController.getPitch();
      return Float.isNaN(var3) ? (Float)var2.call(new Object[]{var1}) : var3;
   }

   @Inject(method = "canStartSprinting", at = @At("HEAD"), cancellable = true)
   private void u(CallbackInfoReturnable<Boolean> var1) {
      if (WTapModule.isReleasingForward() || MaceModule.isViewOwned() || AuraModule.isSprintResetActive()) {
         var1.setReturnValue(false);
      }
   }

   @Inject(method = "tick", at = @At("HEAD"))
   private void t(CallbackInfo var1) {
      if (LarpClient.getInstance() != null && LarpClient.getInstance().getModuleManager() != null) {
         LarpClient.getInstance().getModuleManager().tick();
      }
   }

   @Inject(method = "tick", at = @At("RETURN"))
   private void F(CallbackInfo var1) {
      AuraModule.getCombatSettings((LocalPlayer)this, "tick-end");
   }

   @Inject(method = "modifyInput", at = @At("RETURN"), cancellable = true)
   private void x(Vec2 var1, CallbackInfoReturnable<Vec2> var2) {
      var2.setReturnValue(MovementCorrection.correctMoveVector((Vec2)var2.getReturnValue()));
   }

   @Inject(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/AbstractClientPlayer;tick()V"))
   private void s(CallbackInfo var1) {
      InteractionArbiter.allowsScreenInteraction();
      InteractionArbiter.processQueue();
   }
}
