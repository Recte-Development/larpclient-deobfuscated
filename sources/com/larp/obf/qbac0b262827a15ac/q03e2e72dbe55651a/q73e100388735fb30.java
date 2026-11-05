package com.larp.obf.qbac0b262827a15ac.q03e2e72dbe55651a;

import com.larpclient.c3;
import com.larpclient.ed;
import com.larpclient.mi;
import com.larpclient.my;
import com.larpclient.pj;
import com.larpclient.w5;
import com.larpclient.ze;
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
public abstract class q73e100388735fb30 implements ed {
   @Shadow
   private ItemStack mainHandItem;
   @Unique
   private final my<ItemStack> f = new my<>();

   @Override
   public void larpRig$captureHeldMap(ze var1, ItemStack var2, int var3) {
      if (var1.q() && var1.isDealtHandLocked()) {
         this.f.E(var1, var1.getDealtHandSequence(), var3, var2.copy());
      }
   }

   @Override
   public void larpRig$releaseHeldMap(ze var1, ItemStack var2) {
      if (!var1.q() || !var1.isDealtHandLocked() || !var1.isDealtHandSettled()) {
         this.f.v();
      }
   }

   @Inject(method = "extractRenderState", at = @At("HEAD"))
   private void g(LocalPlayer var1, float var2, FirstPersonHandsAndItemsRenderState var3, CallbackInfo var4) {
      Minecraft var5 = Minecraft.getInstance();
      if (var5.player != null && var5.level != null) {
         ze var6 = ze.c();
         if (var6.q()) {
            var6.getDealtHandVisual(var5.player.getMainHandItem());
            this.f.L(var6, var6.getDealtHandSequence(), var5.player.getInventory().getSelectedSlot(), var6.isDealtHandLocked());
            this.f.u(this.mainHandItem);
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
   private ItemStack C(ItemStack var1) {
      if (var1 != null && !var1.isEmpty()) {
         Minecraft var2 = Minecraft.getInstance();
         if (var2.player != null && var2.level != null) {
            ze var3 = ze.c();
            ItemStack var4 = var2.player.getMainHandItem();
            ItemStack var5 = var1;
            if (var3.isDealtHandLocked()) {
               var5 = var3.getDealtHandVisual(var4);
            }

            int var6 = w5.J();
            var5 = var3.projectSlot(var6, var5);
            ItemStack var7 = var3.getInventorySlotProjection(var6);
            if (var7 != null) {
               var5 = var7;
            }

            c3 var8 = c3.z();
            if (var8 != null) {
               var5 = var8.r(var5);
            }

            var5 = mi.L().Z(var6, var5);
            return pj.U().a(var6, var5);
         } else {
            return var1;
         }
      } else {
         return var1;
      }
   }
}
