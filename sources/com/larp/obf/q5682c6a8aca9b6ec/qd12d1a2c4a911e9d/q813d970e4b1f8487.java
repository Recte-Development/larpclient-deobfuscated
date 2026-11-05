package com.larp.obf.q5682c6a8aca9b6ec.qd12d1a2c4a911e9d;

import com.larpclient.lj;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.FirstPersonHandsAndItems;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FirstPersonHandsAndItems.class)
public abstract class q813d970e4b1f8487 {
   @Shadow
   private ItemStack mainHandItem;
   @Shadow
   private ItemStack offHandItem;

   @Inject(method = "itemUsed", at = @At("HEAD"), cancellable = true)
   private void d(InteractionHand var1, CallbackInfo var2) {
      if (lj.j().C(var1)) {
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
   private void D(LocalPlayer var1, CallbackInfo var2) {
      lj var3 = lj.j();
      if (var3.C(InteractionHand.MAIN_HAND)) {
         this.mainHandItem = var1.getMainHandItem();
      }

      if (var3.C(InteractionHand.OFF_HAND)) {
         this.offHandItem = var1.getOffhandItem();
      }
   }
}
