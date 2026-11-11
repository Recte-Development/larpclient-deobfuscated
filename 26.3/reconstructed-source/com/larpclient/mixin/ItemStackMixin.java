package com.larpclient.mixin;

import com.larpclient.render.model.WithdrawnBoneMarker;
import com.larpclient.render.world.ItemStackMarkers;
import com.larpclient.util.data.GlobalFlag;
import com.larpclient.util.entity.FarmMobType;
import com.larpclient.util.misc.AuctionUtil;
import com.larpclient.util.world.FakeSpawnerController;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.Item.TooltipContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin implements ItemStackMarkers.StackMarkerAccess, WithdrawnBoneMarker.WithdrawnBoneAccess {
   @Unique
   private boolean k;
   @Unique
   private boolean V;
   @Unique
   private boolean E;

   @Override
   public boolean fakeSpawner$isPreview() {
      return this.E;
   }

   @Override
   public void fakeSpawner$setPreview(boolean var1) {
      this.E = var1;
   }

   @Override
   public boolean fakeElytra$isRealPurchase() {
      return this.k;
   }

   @Override
   public void fakeElytra$setRealPurchase(boolean var1) {
      this.k = var1;
   }

   @Override
   public boolean fakeSpawner$isWithdrawnBone() {
      return this.V;
   }

   @Override
   public void fakeSpawner$setWithdrawnBone(boolean var1) {
      this.V = var1;
   }

   @Inject(method = {"copy", "copyWithCount", "split"}, at = @At("RETURN"))
   private void i(CallbackInfoReturnable<ItemStack> var1) {
      ItemStackMarkers.setRealPurchase((ItemStack)var1.getReturnValue(), this.k);
      WithdrawnBoneMarker.setWithdrawnBone((ItemStack)var1.getReturnValue(), this.V);
      if (this.E) {
         ItemStackMarkers.markPreview((ItemStack)var1.getReturnValue());
      }
   }

   @Inject(method = "getHoverName", at = @At("HEAD"), cancellable = true)
   private void r(CallbackInfoReturnable<Component> var1) {
      FakeSpawnerController var2 = FakeSpawnerController.getInstance();
      ItemStack var3 = (ItemStack)this;
      if (var2.isFakeSpawnerStack(var3)) {
         var1.setReturnValue(var2.getStackName(var3));
      }
   }

   @Inject(method = "hasFoil", at = @At("HEAD"), cancellable = true)
   private void H(CallbackInfoReturnable<Boolean> var1) {
      FakeSpawnerController var2 = FakeSpawnerController.getInstance();
      if (var2.isFakeSpawnerStack((ItemStack)this)) {
         var1.setReturnValue(var2.tick());
      }
   }

   @Inject(method = "getTooltipLines", at = @At("HEAD"), cancellable = true)
   private void Z(TooltipContext var1, Player var2, TooltipFlag var3, CallbackInfoReturnable<List<Component>> var4) {
      ItemStack var5 = (ItemStack)this;
      FakeSpawnerController var6 = FakeSpawnerController.getInstance();
      if (var6.isFakeSpawnerStack(var5)) {
         ArrayList var7 = new ArrayList();
         var7.add(var6.getStackName(var5));
         var7.addAll(var6.isReservedSlot(var5, !GlobalFlag.isActive(), var3 != null && var3.isAdvanced()));
         var4.setReturnValue(var7);
      } else if (var6.recordRenderCounts(var5)) {
         var4.setReturnValue(var6.removeFromPile(var5, false));
      }
   }

   @Inject(method = "getTooltipLines", at = @At("RETURN"), cancellable = true)
   private void s(TooltipContext var1, Player var2, TooltipFlag var3, CallbackInfoReturnable<List<Component>> var4) {
      ItemStack var5 = (ItemStack)this;
      FakeSpawnerController var6 = FakeSpawnerController.getInstance();
      if (var6.isActive() && !AuctionUtil.isAuctionItem(var5) && !GlobalFlag.isActive()) {
         long var7 = AuctionUtil.isSellPrompt(var5)
            ? AuctionUtil.getContainerWorth()
            : (!FarmMobType.isDropItem(var5.getItem()) && !var5.has(DataComponents.CONTAINER) ? 0L : var6.loadPiles(var5));
         if (var7 > 0L) {
            ArrayList var9 = new ArrayList((Collection)var4.getReturnValue());
            var9.removeIf(var0 -> {
               String var1x = var0.getString().trim().toLowerCase(Locale.ROOT);
               return var1x.startsWith("worth:") || var1x.startsWith("~$") || var1x.contains("$");
            });
            var9.add(Math.min(1, var9.size()), var6.tickBreakAttempts(var7));
            var4.setReturnValue(var9);
         }
      }
   }
}
