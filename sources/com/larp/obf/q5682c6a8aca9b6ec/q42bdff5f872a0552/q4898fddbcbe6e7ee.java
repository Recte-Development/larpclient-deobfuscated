package com.larp.obf.q5682c6a8aca9b6ec.q42bdff5f872a0552;

import com.larpclient.hk;
import com.larpclient.js;
import com.larpclient.mm;
import com.larpclient.oi;
import com.larpclient.ro;
import com.larpclient.ug;
import com.larpclient.xa;
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
public abstract class q4898fddbcbe6e7ee {
   @ModifyVariable(
      method = "updateForLiving(Lnet/minecraft/client/renderer/item/ItemStackRenderState;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/ItemDisplayContext;Lnet/minecraft/world/entity/LivingEntity;)V",
      at = @At("HEAD"),
      argsOnly = true
   )
   private ItemStack A(ItemStack var1, ItemStackRenderState var2, ItemStack var3, ItemDisplayContext var4, LivingEntity var5) {
      if (var5 == Minecraft.getInstance().player && !var1.isEmpty() && ro.H().n(var1).isEmpty()) {
         return ItemStack.EMPTY;
      }

      if (var5 == Minecraft.getInstance().player && !var1.isEmpty() && mm.W().u(var1).isEmpty()) {
         return ItemStack.EMPTY;
      }

      ItemStack var6 = oi.A().w(var5, var1);
      if (var5 == Minecraft.getInstance().player) {
         var6 = hk.G().B(var6);
      }

      var6 = xa.r().s(var5, var6);
      var6 = js.A().w(var5, var6);
      ItemStack var7 = ug.x().getVisualOverride(var6);
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
   private ItemStack z(ItemStack var1, ItemStackRenderState var2, ItemStack var3, ItemDisplayContext var4, Entity var5) {
      if (var5 instanceof ItemEntity var6) {
         var1 = hk.G().e(var6, var1);
         var1 = oi.A().J(var6, var1);
         var1 = xa.r().R(var6, var1);
         var1 = js.A().Y(var6, var1);
      }

      ItemStack var10 = ug.x().getVisualOverride(var1);
      if (var10 != null) {
         var1 = var10;
      }

      return var1;
   }
}
