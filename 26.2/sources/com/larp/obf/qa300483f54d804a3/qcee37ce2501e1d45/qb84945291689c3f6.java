package com.larp.obf.qa300483f54d804a3.qcee37ce2501e1d45;

import com.larpclient.b8;
import com.larpclient.gz;
import com.larpclient.ji;
import com.larpclient.ll;
import com.larpclient.px;
import com.larpclient.zs;
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
public abstract class qb84945291689c3f6 implements gz.b, zs.y {
   @Unique
   private boolean x;
   @Unique
   private boolean N;
   @Unique
   private boolean R;

   @Override
   public boolean fakeSpawner$isPreview() {
      return this.R;
   }

   @Override
   public void fakeSpawner$setPreview(boolean var1) {
      this.R = var1;
   }

   @Override
   public boolean fakeElytra$isRealPurchase() {
      return this.x;
   }

   @Override
   public void fakeElytra$setRealPurchase(boolean var1) {
      this.x = var1;
   }

   @Override
   public boolean fakeSpawner$isWithdrawnBone() {
      return this.N;
   }

   @Override
   public void fakeSpawner$setWithdrawnBone(boolean var1) {
      this.N = var1;
   }

   @Inject(method = {"copy", "copyWithCount", "split"}, at = @At("RETURN"))
   private void v(CallbackInfoReturnable<ItemStack> var1) {
      gz.k((ItemStack)var1.getReturnValue(), this.x);
      zs.z((ItemStack)var1.getReturnValue(), this.N);
      if (this.R) {
         gz.r((ItemStack)var1.getReturnValue());
      }
   }

   @Inject(method = "getHoverName", at = @At("HEAD"), cancellable = true)
   private void Q(CallbackInfoReturnable<Component> var1) {
      b8 var2 = b8.k();
      ItemStack var3 = (ItemStack)this;
      if (var2.r(var3)) {
         var1.setReturnValue(var2.f(var3));
      }
   }

   @Inject(method = "hasFoil", at = @At("HEAD"), cancellable = true)
   private void m(CallbackInfoReturnable<Boolean> var1) {
      b8 var2 = b8.k();
      if (var2.r((ItemStack)this)) {
         var1.setReturnValue(var2.I());
      }
   }

   @Inject(method = "getTooltipLines", at = @At("HEAD"), cancellable = true)
   private void H(TooltipContext var1, Player var2, TooltipFlag var3, CallbackInfoReturnable<List<Component>> var4) {
      ItemStack var5 = (ItemStack)this;
      b8 var6 = b8.k();
      if (var6.r(var5)) {
         ArrayList var7 = new ArrayList();
         var7.add(var6.f(var5));
         var7.addAll(var6.I(var5, !ji.v(), var3 != null && var3.isAdvanced()));
         var4.setReturnValue(var7);
      } else if (var6.u(var5)) {
         var4.setReturnValue(var6.G(var5, false));
      }
   }

   @Inject(method = "getTooltipLines", at = @At("RETURN"), cancellable = true)
   private void z(TooltipContext var1, Player var2, TooltipFlag var3, CallbackInfoReturnable<List<Component>> var4) {
      ItemStack var5 = (ItemStack)this;
      b8 var6 = b8.k();
      if (var6.J() && !px.T(var5) && !ji.v()) {
         long var7 = px.C(var5) ? px.I() : (!ll.r(var5.getItem()) && !var5.has(DataComponents.CONTAINER) ? 0L : var6.x(var5));
         if (var7 > 0L) {
            ArrayList var9 = new ArrayList((Collection)var4.getReturnValue());
            var9.removeIf(var0 -> {
               String var1x = var0.getString().trim().toLowerCase(Locale.ROOT);
               return var1x.startsWith("worth:") || var1x.startsWith("~$") || var1x.contains("$");
            });
            var9.add(Math.min(1, var9.size()), var6.O(var7));
            var4.setReturnValue(var9);
         }
      }
   }
}
