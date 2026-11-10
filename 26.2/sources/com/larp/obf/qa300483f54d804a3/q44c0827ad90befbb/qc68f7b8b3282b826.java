package com.larp.obf.qa300483f54d804a3.q44c0827ad90befbb;

import com.larpclient.cy;
import com.larpclient.g5;
import com.larpclient.lh;
import com.larpclient.pq;
import com.larpclient.pw;
import com.larpclient.t5;
import com.larpclient.we;
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
public abstract class qc68f7b8b3282b826 {
   @ModifyVariable(
      method = "updateForLiving(Lnet/minecraft/client/renderer/item/ItemStackRenderState;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/ItemDisplayContext;Lnet/minecraft/world/entity/LivingEntity;)V",
      at = @At("HEAD"),
      argsOnly = true
   )
   private ItemStack I(ItemStack var1, ItemStackRenderState var2, ItemStack var3, ItemDisplayContext var4, LivingEntity var5) {
      if (var5 == Minecraft.getInstance().player && !var1.isEmpty() && pw.S().C(var1).isEmpty()) {
         return ItemStack.EMPTY;
      }

      if (var5 == Minecraft.getInstance().player && !var1.isEmpty() && lh.I().z(var1).isEmpty()) {
         return ItemStack.EMPTY;
      }

      ItemStack var6 = g5.a().M(var5, var1);
      if (var5 == Minecraft.getInstance().player) {
         var6 = cy.Q().D(var6);
      }

      var6 = t5.O().O(var5, var6);
      var6 = pq.Q().I(var5, var6);
      ItemStack var7 = we.Y().getVisualOverride(var6);
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
   private ItemStack e(ItemStack var1, ItemStackRenderState var2, ItemStack var3, ItemDisplayContext var4, Entity var5) {
      if (var5 instanceof ItemEntity var6) {
         var1 = cy.Q().q(var6, var1);
         var1 = g5.a().p(var6, var1);
         var1 = t5.O().z(var6, var1);
         var1 = pq.Q().f(var6, var1);
      }

      ItemStack var10 = we.Y().getVisualOverride(var1);
      if (var10 != null) {
         var1 = var10;
      }

      return var1;
   }
}
