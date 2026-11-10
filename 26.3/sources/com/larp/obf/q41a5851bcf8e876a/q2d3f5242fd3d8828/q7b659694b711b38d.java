package com.larp.obf.q41a5851bcf8e876a.q2d3f5242fd3d8828;

import com.larpclient.p2;
import com.larpclient.pp;
import com.larpclient.q5;
import com.larpclient.qo;
import com.larpclient.sl;
import com.larpclient.vy;
import com.larpclient.wj;
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
public abstract class q7b659694b711b38d {
   @ModifyVariable(
      method = "updateForLiving(Lnet/minecraft/client/renderer/item/ItemStackRenderState;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/ItemDisplayContext;Lnet/minecraft/world/entity/LivingEntity;)V",
      at = @At("HEAD"),
      argsOnly = true
   )
   private ItemStack x(ItemStack var1, ItemStackRenderState var2, ItemStack var3, ItemDisplayContext var4, LivingEntity var5) {
      if (var5 == Minecraft.getInstance().player && !var1.isEmpty() && wj.m().F(var1).isEmpty()) {
         return ItemStack.EMPTY;
      }

      if (var5 == Minecraft.getInstance().player && !var1.isEmpty() && q5.a().v(var1).isEmpty()) {
         return ItemStack.EMPTY;
      }

      ItemStack var6 = qo.Z().p(var5, var1);
      if (var5 == Minecraft.getInstance().player) {
         var6 = sl.X().V(var6);
      }

      var6 = p2.p().P(var5, var6);
      var6 = pp.A().C(var5, var6);
      ItemStack var7 = vy.a().getVisualOverride(var6);
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
   private ItemStack r(ItemStack var1, ItemStackRenderState var2, ItemStack var3, ItemDisplayContext var4, Entity var5) {
      if (var5 instanceof ItemEntity var6) {
         var1 = sl.X().w(var6, var1);
         var1 = qo.Z().I(var6, var1);
         var1 = p2.p().C(var6, var1);
         var1 = pp.A().Y(var6, var1);
      }

      ItemStack var10 = vy.a().getVisualOverride(var1);
      if (var10 != null) {
         var1 = var10;
      }

      return var1;
   }
}
