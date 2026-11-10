package com.larp.obf.qa300483f54d804a3.q44c0827ad90befbb;

import com.larpclient.cc;
import com.larpclient.cy;
import com.larpclient.g5;
import com.larpclient.mg;
import com.larpclient.pq;
import com.larpclient.t5;
import com.larpclient.we;
import net.minecraft.client.renderer.entity.ItemEntityRenderer;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(ItemEntityRenderer.class)
public class q7843a4f653ada993 {
   @ModifyArg(
      method = "extractRenderState(Lnet/minecraft/world/entity/item/ItemEntity;Lnet/minecraft/client/renderer/entity/state/ItemEntityRenderState;F)V",
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/renderer/entity/state/ItemEntityRenderState;extractItemGroupRenderState(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/client/renderer/item/ItemModelResolver;)V"
      ),
      index = 1
   )
   private ItemStack R(Entity var1, ItemStack var2, ItemModelResolver var3) {
      ItemEntity var4 = (ItemEntity)var1;
      var2 = mg.c(var4, var2);
      var2 = cy.Q().q(var4, var2);
      var2 = g5.a().p(var4, var2);
      ItemStack var5 = we.Y().getVisualOverride(var2);
      if (var5 != null) {
         var2 = var5;
      }

      var2 = t5.O().z(var4, var2);
      var2 = pq.Q().f(var4, var2);
      return cc.z().s(var4.getId(), var2);
   }
}
