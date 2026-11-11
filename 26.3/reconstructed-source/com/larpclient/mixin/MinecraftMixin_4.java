package com.larpclient.mixin;

import com.larpclient.LarpClient;
import com.larpclient.core.ViewController;
import com.larpclient.module.combat.MaceModule;
import com.larpclient.module.misc.Module;
import com.larpclient.module.player.RodSwapModule;
import com.larpclient.util.misc.InteractionArbiter;
import com.larpclient.util.misc.LarpLog;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public abstract class MinecraftMixin_4 {
   @Inject(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/Gui;tick()V"))
   private void F(CallbackInfo var1) {
      LarpClient var2 = LarpClient.getInstance();
      InteractionArbiter.findAttackBlocker();
      if (var2 != null && var2.getModuleManager() != null) {
         var2.getModuleManager().suspendOnStateChange();
         ViewController.checkClickInterrupt();
         RodSwapModule var3 = var2.getModuleManager().getModule(RodSwapModule.class);
         if (var3 != null && var3.isRuntimeActive()) {
            U(var3, var3::onClientTick);
         }

         MaceModule var4 = MaceModule.getInstance();
         if (var4 != null && var4.isRuntimeActive()) {
            U(var4, MaceModule::updateSmashConfirmation);
         }
      }
   }

   @Unique
   private static void U(Module var0, Runnable var1) {
      try {
         var1.run();
      } catch (RuntimeException var3) {
         LarpLog.error("Combat", "Pre-input tick failed; releasing " + var0.getName(), var3);
         var0.disable();
      }
   }
}
