package com.larp.obf.qbac0b262827a15ac.q03e2e72dbe55651a;

import com.larpclient._g;
import com.larpclient._u;
import com.larpclient.mi;
import com.larpclient.pj;
import com.larpclient.w5;
import com.larpclient.ze;
import com.larpclient.zv;
import net.minecraft.client.renderer.entity.ItemEntityRenderer;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(ItemEntityRenderer.class)
public class q2577ccb5b9bb4f84 {
   @ModifyArg(
      method = "extractRenderState(Lnet/minecraft/world/entity/item/ItemEntity;Lnet/minecraft/client/renderer/entity/state/ItemEntityRenderState;F)V",
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/renderer/entity/state/ItemEntityRenderState;extractItemGroupRenderState(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/client/renderer/item/ItemModelResolver;)V"
      ),
      index = 1
   )
   private ItemStack T(Entity var1, ItemStack var2, ItemModelResolver var3) {
      ItemEntity var4 = (ItemEntity)var1;
      var2 = w5.q(var4, var2);
      var2 = _g.E().S(var4, var2);
      var2 = mi.L().Z(var4, var2);
      ItemStack var5 = ze.c().getVisualOverride(var2);
      if (var5 != null) {
         var2 = var5;
      }

      var2 = zv.x().x(var4, var2);
      var2 = _u.O().v(var4, var2);
      return pj.U().F(var4.getId(), var2);
   }
}
