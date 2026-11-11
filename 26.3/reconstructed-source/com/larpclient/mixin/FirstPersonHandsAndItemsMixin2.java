package com.larpclient.mixin;

import com.larpclient.util.world.FakeSpawnerController;
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
public abstract class FirstPersonHandsAndItemsMixin2 {
   @Shadow
   private ItemStack mainHandItem;
   @Shadow
   private ItemStack offHandItem;

   @Inject(method = "itemUsed", at = @At("HEAD"), cancellable = true)
   private void n(InteractionHand var1, CallbackInfo var2) {
      if (FakeSpawnerController.getInstance().wasUsedRecently(var1)) {
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
   private void V(LocalPlayer var1, CallbackInfo var2) {
      FakeSpawnerController var3 = FakeSpawnerController.getInstance();
      if (var3.wasUsedRecently(InteractionHand.MAIN_HAND)) {
         this.mainHandItem = var1.getMainHandItem();
      }

      if (var3.wasUsedRecently(InteractionHand.OFF_HAND)) {
         this.offHandItem = var1.getOffhandItem();
      }
   }
}
