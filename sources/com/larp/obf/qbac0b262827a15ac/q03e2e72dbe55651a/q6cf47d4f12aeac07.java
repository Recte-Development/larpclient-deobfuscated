package com.larp.obf.qbac0b262827a15ac.q03e2e72dbe55651a;

import com.larpclient._g;
import com.larpclient._u;
import com.larpclient.c6;
import com.larpclient.kq;
import com.larpclient.mi;
import com.larpclient.ze;
import com.larpclient.zv;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(ItemModelResolver.class)
public abstract class q6cf47d4f12aeac07 {
   @ModifyVariable(
      method = "updateForLiving(Lnet/minecraft/client/renderer/item/ItemStackRenderState;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/ItemDisplayContext;Lnet/minecraft/world/entity/LivingEntity;)V",
      at = @At("HEAD"),
      argsOnly = true
   )
   private ItemStack t(ItemStack var1, ItemStackRenderState var2, ItemStack var3, ItemDisplayContext var4, LivingEntity var5) {
      if (var5 == Minecraft.getInstance().player && !var1.isEmpty() && c6.K().f(var1).isEmpty()) {
         return ItemStack.EMPTY;
      }

      if (var5 == Minecraft.getInstance().player && !var1.isEmpty() && kq.I().R(var1).isEmpty()) {
         return ItemStack.EMPTY;
      }

      ItemStack var6 = mi.L().u(var5, var1);
      if (var5 == Minecraft.getInstance().player) {
         var6 = _g.E().i(var6);
      }

      var6 = zv.x().H(var5, var6);
      var6 = _u.O().P(var5, var6);
      ItemStack var7 = ze.c().getVisualOverride(var6);
      if (var7 != null) {
         var6 = var7;
      }

      return var6;
   }

   @ModifyVariable(
      method = "updateForNonLiving(Lnet/minecraft/client/renderer/item/ItemStackRenderState;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/ItemDisplayContext;Lnet/minecraft/world/entity/Entity;)V",
      at = @At("HEAD"),
      argsOnly = true
   )
   private ItemStack R(ItemStack var1, ItemStackRenderState var2, ItemStack var3, ItemDisplayContext var4, Entity var5) {
      if (var5 instanceof ItemEntity var6) {
         var1 = _g.E().S(var6, var1);
         var1 = mi.L().Z(var6, var1);
         var1 = zv.x().x(var6, var1);
         var1 = _u.O().v(var6, var1);
      }

      ItemStack var10 = ze.c().getVisualOverride(var1);
      if (var10 != null) {
         var1 = var10;
      }

      return var1;
   }
}
