package com.larpclient.mixin;

import com.larpclient.config.VisualWinnerConfig;
import com.larpclient.module.exploit.CardForce;
import com.larpclient.module.misc.CasinoRigManager;
import com.larpclient.module.player.FishingRodSwapper;
import com.larpclient.util.data.TimedCache;
import com.larpclient.util.inventory.HeldMapCapture;
import com.larpclient.util.inventory.MapSwapInventory;
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
public abstract class FirstPersonHandsAndItemsMixin3 implements HeldMapCapture {
   @Shadow
   private ItemStack mainHandItem;
   @Unique
   private final TimedCache<ItemStack> s = new TimedCache<>();

   @Override
   public void larpRig$captureHeldMap(CardForce var1, ItemStack var2, int var3) {
      if (var1.isCardGame() && var1.isDealtHandLocked()) {
         this.s.store(var1, var1.getDealtHandSequence(), var3, var2.copy());
      }
   }

   @Override
   public void larpRig$releaseHeldMap(CardForce var1, ItemStack var2) {
      if (!var1.isCardGame() || !var1.isDealtHandLocked() || !var1.isDealtHandSettled()) {
         this.s.clear();
      }
   }

   @Inject(method = "extractRenderState", at = @At("HEAD"))
   private void J(LocalPlayer var1, float var2, FirstPersonHandsAndItemsRenderState var3, CallbackInfo var4) {
      Minecraft var5 = Minecraft.getInstance();
      if (var5.player != null && var5.level != null) {
         CardForce var6 = CardForce.getActive();
         if (var6.isCardGame()) {
            var6.getDealtHandVisual(var5.player.getMainHandItem());
            this.s.invalidateUnlessMatching(var6, var6.getDealtHandSequence(), var5.player.getInventory().getSelectedSlot(), var6.isDealtHandLocked());
            this.s.markConsumed(this.mainHandItem);
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
            CardForce var3 = CardForce.getActive();
            ItemStack var4 = var2.player.getMainHandItem();
            ItemStack var5 = var1;
            if (var3.isDealtHandLocked()) {
               var5 = var3.getDealtHandVisual(var4);
            }

            int var6 = MapSwapInventory.syncSelectedSlot();
            var5 = var3.projectSlot(var6, var5);
            ItemStack var7 = var3.getInventorySlotProjection(var6);
            if (var7 != null) {
               var5 = var7;
            }

            CasinoRigManager var8 = CasinoRigManager.getInstance();
            if (var8 != null) {
               var5 = var8.applyVisualOverrides(var5);
            }

            var5 = VisualWinnerConfig.getInstance().replaceHotbarStack(var6, var5);
            return FishingRodSwapper.getInstance().unlock(var6, var5);
         } else {
            return var1;
         }
      } else {
         return var1;
      }
   }
}
