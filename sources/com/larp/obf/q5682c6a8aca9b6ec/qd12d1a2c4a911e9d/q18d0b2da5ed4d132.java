package com.larp.obf.q5682c6a8aca9b6ec.qd12d1a2c4a911e9d;

import com.larpclient.e3;
import com.larpclient.hr;
import com.larpclient.ja;
import com.larpclient.lj;
import com.larpclient.mb;
import com.larpclient.yl;
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
public abstract class q18d0b2da5ed4d132 implements ja.b, yl.w {
   @Unique
   private boolean l;
   @Unique
   private boolean P;
   @Unique
   private boolean H;

   @Override
   public boolean fakeSpawner$isPreview() {
      return this.H;
   }

   @Override
   public void fakeSpawner$setPreview(boolean var1) {
      this.H = var1;
   }

   @Override
   public boolean fakeElytra$isRealPurchase() {
      return this.l;
   }

   @Override
   public void fakeElytra$setRealPurchase(boolean var1) {
      this.l = var1;
   }

   @Override
   public boolean fakeSpawner$isWithdrawnBone() {
      return this.P;
   }

   @Override
   public void fakeSpawner$setWithdrawnBone(boolean var1) {
      this.P = var1;
   }

   @Inject(method = {"copy", "copyWithCount", "split"}, at = @At("RETURN"))
   private void H(CallbackInfoReturnable<ItemStack> var1) {
      ja.x((ItemStack)var1.getReturnValue(), this.l);
      yl.T((ItemStack)var1.getReturnValue(), this.P);
      if (this.H) {
         ja.T((ItemStack)var1.getReturnValue());
      }
   }

   @Inject(method = "getHoverName", at = @At("HEAD"), cancellable = true)
   private void g(CallbackInfoReturnable<Component> var1) {
      lj var2 = lj.j();
      ItemStack var3 = (ItemStack)this;
      if (var2.u(var3)) {
         var1.setReturnValue(var2.O(var3));
      }
   }

   @Inject(method = "hasFoil", at = @At("HEAD"), cancellable = true)
   private void U(CallbackInfoReturnable<Boolean> var1) {
      lj var2 = lj.j();
      if (var2.u((ItemStack)this)) {
         var1.setReturnValue(var2.O());
      }
   }

   @Inject(method = "getTooltipLines", at = @At("HEAD"), cancellable = true)
   private void n(TooltipContext var1, Player var2, TooltipFlag var3, CallbackInfoReturnable<List<Component>> var4) {
      ItemStack var5 = (ItemStack)this;
      lj var6 = lj.j();
      if (var6.u(var5)) {
         ArrayList var7 = new ArrayList();
         var7.add(var6.O(var5));
         var7.addAll(var6.w(var5, !e3.o(), var3 != null && var3.isAdvanced()));
         var4.setReturnValue(var7);
      } else if (var6.p(var5)) {
         var4.setReturnValue(var6.z(var5, false));
      }
   }

   @Inject(method = "getTooltipLines", at = @At("RETURN"), cancellable = true)
   private void j(TooltipContext var1, Player var2, TooltipFlag var3, CallbackInfoReturnable<List<Component>> var4) {
      ItemStack var5 = (ItemStack)this;
      lj var6 = lj.j();
      if (var6.B() && !hr.d(var5) && !e3.o()) {
         long var7 = hr.C(var5) ? hr.h() : (!mb.j(var5.getItem()) && !var5.has(DataComponents.CONTAINER) ? 0L : var6.n(var5));
         if (var7 > 0L) {
            ArrayList var9 = new ArrayList((Collection)var4.getReturnValue());
            var9.removeIf(var0 -> {
               String var1x = var0.getString().trim().toLowerCase(Locale.ROOT);
               return var1x.startsWith("worth:") || var1x.startsWith("~$") || var1x.contains("$");
            });
            var9.add(Math.min(1, var9.size()), var6.w(var7));
            var4.setReturnValue(var9);
         }
      }
   }
}
