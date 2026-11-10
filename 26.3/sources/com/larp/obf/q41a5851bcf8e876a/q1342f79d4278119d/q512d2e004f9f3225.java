package com.larp.obf.q41a5851bcf8e876a.q1342f79d4278119d;

import com.larpclient.hc;
import com.larpclient.jn;
import com.larpclient.nb;
import com.larpclient.ni;
import com.larpclient.nn;
import com.larpclient.ox;
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
public abstract class q512d2e004f9f3225 implements nn.i, ox.x {
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
      nn.U((ItemStack)var1.getReturnValue(), this.k);
      ox.x((ItemStack)var1.getReturnValue(), this.V);
      if (this.E) {
         nn.F((ItemStack)var1.getReturnValue());
      }
   }

   @Inject(method = "getHoverName", at = @At("HEAD"), cancellable = true)
   private void r(CallbackInfoReturnable<Component> var1) {
      jn var2 = jn.g();
      ItemStack var3 = (ItemStack)this;
      if (var2.l(var3)) {
         var1.setReturnValue(var2.h(var3));
      }
   }

   @Inject(method = "hasFoil", at = @At("HEAD"), cancellable = true)
   private void H(CallbackInfoReturnable<Boolean> var1) {
      jn var2 = jn.g();
      if (var2.l((ItemStack)this)) {
         var1.setReturnValue(var2.N());
      }
   }

   @Inject(method = "getTooltipLines", at = @At("HEAD"), cancellable = true)
   private void Z(TooltipContext var1, Player var2, TooltipFlag var3, CallbackInfoReturnable<List<Component>> var4) {
      ItemStack var5 = (ItemStack)this;
      jn var6 = jn.g();
      if (var6.l(var5)) {
         ArrayList var7 = new ArrayList();
         var7.add(var6.h(var5));
         var7.addAll(var6.G(var5, !hc.m(), var3 != null && var3.isAdvanced()));
         var4.setReturnValue(var7);
      } else if (var6.k(var5)) {
         var4.setReturnValue(var6.I(var5, false));
      }
   }

   @Inject(method = "getTooltipLines", at = @At("RETURN"), cancellable = true)
   private void s(TooltipContext var1, Player var2, TooltipFlag var3, CallbackInfoReturnable<List<Component>> var4) {
      ItemStack var5 = (ItemStack)this;
      jn var6 = jn.g();
      if (var6.a() && !nb.u(var5) && !hc.m()) {
         long var7 = nb.n(var5) ? nb.E() : (!ni.u(var5.getItem()) && !var5.has(DataComponents.CONTAINER) ? 0L : var6.A(var5));
         if (var7 > 0L) {
            ArrayList var9 = new ArrayList((Collection)var4.getReturnValue());
            var9.removeIf(var0 -> {
               String var1x = var0.getString().trim().toLowerCase(Locale.ROOT);
               return var1x.startsWith("worth:") || var1x.startsWith("~$") || var1x.contains("$");
            });
            var9.add(Math.min(1, var9.size()), var6.y(var7));
            var4.setReturnValue(var9);
         }
      }
   }
}
