package com.larp.obf.q41a5851bcf8e876a.q2d3f5242fd3d8828;

import com.larpclient.a6;
import com.larpclient.d5;
import com.larpclient.hh;
import com.larpclient.q2;
import com.larpclient.qm;
import com.larpclient.qo;
import com.larpclient.vy;
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
public abstract class qfdfaf7831f346d00 implements q2 {
   @Shadow
   private ItemStack mainHandItem;
   @Unique
   private final d5<ItemStack> s = new d5<>();

   @Override
   public void larpRig$captureHeldMap(vy var1, ItemStack var2, int var3) {
      if (var1.W() && var1.isDealtHandLocked()) {
         this.s.H(var1, var1.getDealtHandSequence(), var3, var2.copy());
      }
   }

   @Override
   public void larpRig$releaseHeldMap(vy var1, ItemStack var2) {
      if (!var1.W() || !var1.isDealtHandLocked() || !var1.isDealtHandSettled()) {
         this.s.P();
      }
   }

   @Inject(method = "extractRenderState", at = @At("HEAD"))
   private void J(LocalPlayer var1, float var2, FirstPersonHandsAndItemsRenderState var3, CallbackInfo var4) {
      Minecraft var5 = Minecraft.getInstance();
      if (var5.player != null && var5.level != null) {
         vy var6 = vy.a();
         if (var6.W()) {
            var6.getDealtHandVisual(var5.player.getMainHandItem());
            this.s.b(var6, var6.getDealtHandSequence(), var5.player.getInventory().getSelectedSlot(), var6.isDealtHandLocked());
            this.s.W(this.mainHandItem);
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
   private ItemStack f(ItemStack var1) {
      if (var1 != null && !var1.isEmpty()) {
         Minecraft var2 = Minecraft.getInstance();
         if (var2.player != null && var2.level != null) {
            vy var3 = vy.a();
            ItemStack var4 = var2.player.getMainHandItem();
            ItemStack var5 = var1;
            if (var3.isDealtHandLocked()) {
               var5 = var3.getDealtHandVisual(var4);
            }

            int var6 = qm.z();
            var5 = var3.projectSlot(var6, var5);
            ItemStack var7 = var3.getInventorySlotProjection(var6);
            if (var7 != null) {
               var5 = var7;
            }

            a6 var8 = a6.F();
            if (var8 != null) {
               var5 = var8.m(var5);
            }

            var5 = qo.Z().g(var6, var5);
            return hh.R().o(var6, var5);
         } else {
            return var1;
         }
      } else {
         return var1;
      }
   }
}
