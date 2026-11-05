package com.larp.obf.qbac0b262827a15ac.qb9781bf98a2db63b;

import com.larpclient.cl;
import com.larpclient.cy;
import com.larpclient.d1;
import com.larpclient.dr;
import com.larpclient.h2;
import com.larpclient.pc;
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
public abstract class qe2cc2037532a60af implements h2.p, cy.h {
   @Unique
   private boolean D;
   @Unique
   private boolean F;
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
      return this.D;
   }

   @Override
   public void fakeElytra$setRealPurchase(boolean var1) {
      this.D = var1;
   }

   @Override
   public boolean fakeSpawner$isWithdrawnBone() {
      return this.F;
   }

   @Override
   public void fakeSpawner$setWithdrawnBone(boolean var1) {
      this.F = var1;
   }

   @Inject(method = {"copy", "copyWithCount", "split"}, at = @At("RETURN"))
   private void h(CallbackInfoReturnable<ItemStack> var1) {
      h2.f((ItemStack)var1.getReturnValue(), this.D);
      cy.i((ItemStack)var1.getReturnValue(), this.F);
      if (this.E) {
         h2.H((ItemStack)var1.getReturnValue());
      }
   }

   @Inject(method = "getHoverName", at = @At("HEAD"), cancellable = true)
   private void L(CallbackInfoReturnable<Component> var1) {
      dr var2 = dr.z();
      ItemStack var3 = (ItemStack)this;
      if (var2.H(var3)) {
         var1.setReturnValue(var2.s(var3));
      }
   }

   @Inject(method = "hasFoil", at = @At("HEAD"), cancellable = true)
   private void O(CallbackInfoReturnable<Boolean> var1) {
      dr var2 = dr.z();
      if (var2.H((ItemStack)this)) {
         var1.setReturnValue(var2.l());
      }
   }

   @Inject(method = "getTooltipLines", at = @At("HEAD"), cancellable = true)
   private void A(TooltipContext var1, Player var2, TooltipFlag var3, CallbackInfoReturnable<List<Component>> var4) {
      ItemStack var5 = (ItemStack)this;
      dr var6 = dr.z();
      if (var6.H(var5)) {
         ArrayList var7 = new ArrayList();
         var7.add(var6.s(var5));
         var7.addAll(var6.T(var5, !pc.q(), var3 != null && var3.isAdvanced()));
         var4.setReturnValue(var7);
      } else if (var6.m(var5)) {
         var4.setReturnValue(var6.w(var5, false));
      }
   }

   @Inject(method = "getTooltipLines", at = @At("RETURN"), cancellable = true)
   private void P(TooltipContext var1, Player var2, TooltipFlag var3, CallbackInfoReturnable<List<Component>> var4) {
      ItemStack var5 = (ItemStack)this;
      dr var6 = dr.z();
      if (var6.H() && !d1.d(var5) && !pc.q()) {
         long var7 = d1.I(var5) ? d1.Q() : (!cl.b(var5.getItem()) && !var5.has(DataComponents.CONTAINER) ? 0L : var6.e(var5));
         if (var7 > 0L) {
            ArrayList var9 = new ArrayList((Collection)var4.getReturnValue());
            var9.removeIf(var0 -> {
               String var1x = var0.getString().trim().toLowerCase(Locale.ROOT);
               return var1x.startsWith("worth:") || var1x.startsWith("~$") || var1x.contains("$");
            });
            var9.add(Math.min(1, var9.size()), var6.X(var7));
            var4.setReturnValue(var9);
         }
      }
   }
}
