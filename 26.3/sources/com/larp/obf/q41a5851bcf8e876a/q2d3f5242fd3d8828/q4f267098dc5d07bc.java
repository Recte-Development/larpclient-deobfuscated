package com.larp.obf.q41a5851bcf8e876a.q2d3f5242fd3d8828;

import com.larpclient._u;
import com.larpclient.hk;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.DispenserMenu;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractContainerScreen.class)
public abstract class q4f267098dc5d07bc {
   @Shadow
   @Final
   protected AbstractContainerMenu menu;

   @Inject(method = "init", at = @At("TAIL"))
   private void E(CallbackInfo var1) {
      if (this.menu instanceof DispenserMenu var2) {
         _u.b().M();
         hk.e().g(Minecraft.getInstance(), var2);
      }
   }
}
