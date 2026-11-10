package com.larp.obf.q41a5851bcf8e876a.q2d3f5242fd3d8828;

import com.larpclient.hh;
import com.larpclient.p2;
import com.larpclient.pp;
import com.larpclient.qm;
import com.larpclient.qo;
import com.larpclient.sl;
import com.larpclient.vy;
import net.minecraft.client.renderer.entity.ItemEntityRenderer;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(ItemEntityRenderer.class)
public class qed6c33870cc737e0 {
   @ModifyArg(
      method = "extractRenderState(Lnet/minecraft/world/entity/item/ItemEntity;Lnet/minecraft/client/renderer/entity/state/ItemEntityRenderState;F)V",
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/renderer/entity/state/ItemEntityRenderState;extractItemGroupRenderState(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/client/renderer/item/ItemModelResolver;)V"
      ),
      index = 1
   )
   private ItemStack S(Entity var1, ItemStack var2, ItemModelResolver var3) {
      ItemEntity var4 = (ItemEntity)var1;
      var2 = qm.m(var4, var2);
      var2 = sl.X().w(var4, var2);
      var2 = qo.Z().I(var4, var2);
      ItemStack var5 = vy.a().getVisualOverride(var2);
      if (var5 != null) {
         var2 = var5;
      }

      var2 = p2.p().C(var4, var2);
      var2 = pp.A().Y(var4, var2);
      return hh.R().P(var4.getId(), var2);
   }
}
