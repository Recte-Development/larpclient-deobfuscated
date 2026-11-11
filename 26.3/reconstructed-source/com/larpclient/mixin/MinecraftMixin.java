package com.larpclient.mixin;

import com.larpclient.module.combat.AutoClickerModule;
import com.larpclient.util.misc.InteractionArbiter;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Minecraft.class)
public abstract class MinecraftMixin {
   @Inject(method = "startAttack", at = @At("HEAD"), cancellable = true)
   private void b(CallbackInfoReturnable<Boolean> var1) {
      AutoClickerModule var2 = AutoClickerModule.getInstance();
      if (var2 != null && var2.isLeftClickActive() && !var2.isOverridingHit() && !InteractionArbiter.isModuleInteracting()) {
         var1.setReturnValue(false);
      }
   }

   @Inject(method = "startUseItem", at = @At("HEAD"), cancellable = true)
   private void u(CallbackInfo var1) {
      AutoClickerModule var2 = AutoClickerModule.getInstance();
      if (var2 != null && var2.isRightClickActive() && !var2.isOverridingHit() && !InteractionArbiter.isModuleInteracting()) {
         var1.cancel();
      }
   }

   @WrapMethod(method = "continueAttack")
   private void z(boolean var1, Operation<Void> var2) {
      AutoClickerModule var3 = AutoClickerModule.getInstance();
      if (var3 == null) {
         var2.call(new Object[]{var1});
      } else {
         var3.mineWithAimedHit(var1x -> var2.call(new Object[]{var1x}), var1);
      }
   }

   @WrapOperation(method = "handleKeybinds", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/KeyMapping;isDown()Z"))
   private boolean V(KeyMapping var1, Operation<Boolean> var2) {
      AutoClickerModule var3 = AutoClickerModule.getInstance();
      return (Boolean)var2.call(new Object[]{var1}) || var1 == Minecraft.getInstance().options.keyUse && var3 != null && var3.isAutoUsingItem();
   }
}
