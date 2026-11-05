package com.larp.obf.q5682c6a8aca9b6ec.q42bdff5f872a0552;

import com.larpclient.eg;
import com.larpclient.en;
import com.larpclient.l6;
import com.larpclient.oi;
import com.larpclient.uc;
import com.larpclient.ug;
import com.larpclient.yg;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.FirstPersonHandsAndItems;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.state.level.FirstPersonHandsAndItemsRenderState;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FirstPersonHandsAndItems.class)
public abstract class q4ac2232f31ca6ab2 implements en {
   @Shadow
   private ItemStack mainHandItem;
   @Unique
   private final uc<ItemStack> O = new uc<>();

   @Override
   public void larpRig$captureHeldMap(ug var1, ItemStack var2, int var3) {
      if (var1.u() && var1.isDealtHandLocked()) {
         this.O.u(var1, var1.getDealtHandSequence(), var3, var2.copy());
      }
   }

   @Override
   public void larpRig$releaseHeldMap(ug var1, ItemStack var2) {
      if (!var1.u() || !var1.isDealtHandLocked() || !var1.isDealtHandSettled()) {
         this.O.G();
      }
   }

   @Inject(method = "extractRenderState", at = @At("HEAD"))
   private void g(LocalPlayer var1, float var2, FirstPersonHandsAndItemsRenderState var3, CallbackInfo var4) {
      Minecraft var5 = Minecraft.getInstance();
      if (var5.player != null && var5.level != null) {
         ug var6 = ug.x();
         if (var6.u()) {
            var6.getDealtHandVisual(var5.player.getMainHandItem());
            this.O.O(var6, var6.getDealtHandSequence(), var5.player.getInventory().getSelectedSlot(), var6.isDealtHandLocked());
            this.O.x(this.mainHandItem);
         }
      }
   }

   @ModifyExpressionValue(
      method = "extractRenderState",
      at = @At(
         value = "FIELD",
         target = "Lnet/minecraft/client/player/FirstPersonHandsAndItems;mainHandItem:Lnet/minecraft/world/item/ItemStack;",
         ordinal = 0
      )
   )
   private ItemStack E(ItemStack var1) {
      if (var1 != null && !var1.isEmpty()) {
         Minecraft var2 = Minecraft.getInstance();
         if (var2.player != null && var2.level != null) {
            ug var3 = ug.x();
            ItemStack var4 = var2.player.getMainHandItem();
            ItemStack var5 = var1;
            if (var3.isDealtHandLocked()) {
               var5 = var3.getDealtHandVisual(var4);
            }

            int var6 = yg.m();
            var5 = var3.projectSlot(var6, var5);
            ItemStack var7 = var3.getInventorySlotProjection(var6);
            if (var7 != null) {
               var5 = var7;
            }

            l6 var8 = l6.E();
            if (var8 != null) {
               var5 = var8.I(var5);
            }

            var5 = oi.A().y(var6, var5);
            return eg.y().t(var6, var5);
         } else {
            return var1;
         }
      } else {
         return var1;
      }
   }
}
