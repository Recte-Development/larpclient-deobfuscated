package com.larp.obf.qa300483f54d804a3.qcee37ce2501e1d45;

import com.larpclient.b8;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemInHandRenderer.class)
public abstract class q393ad18a4315cd56 {
   @Shadow
   private ItemStack mainHandItem;
   @Shadow
   private ItemStack offHandItem;

   @Inject(method = "itemUsed", at = @At("HEAD"), cancellable = true)
   private void V(InteractionHand var1, CallbackInfo var2) {
      if (b8.k().k(var1)) {
         LocalPlayer var3 = Minecraft.getInstance().player;
         if (var1 == InteractionHand.MAIN_HAND) {
            this.mainHandItem = var3.getMainHandItem();
         } else {
            this.offHandItem = var3.getOffhandItem();
         }

         var2.cancel();
      }
   }

   @Inject(method = "tick", at = @At("HEAD"))
   private void U(CallbackInfo var1) {
      b8 var2 = b8.k();
      LocalPlayer var3 = Minecraft.getInstance().player;
      if (var2.k(InteractionHand.MAIN_HAND)) {
         this.mainHandItem = var3.getMainHandItem();
      }

      if (var2.k(InteractionHand.OFF_HAND)) {
         this.offHandItem = var3.getOffhandItem();
      }
   }
}
