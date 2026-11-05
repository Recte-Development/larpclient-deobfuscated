package com.larp.obf.q5682c6a8aca9b6ec.q42bdff5f872a0552;

import com.larpclient.eg;
import com.larpclient.hk;
import com.larpclient.js;
import com.larpclient.oi;
import com.larpclient.ug;
import com.larpclient.xa;
import com.larpclient.yg;
import net.minecraft.client.renderer.entity.ItemEntityRenderer;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(ItemEntityRenderer.class)
public class qe4b986ba77dc2bfc {
   @ModifyArg(
      method = "extractRenderState(Lnet/minecraft/world/entity/item/ItemEntity;Lnet/minecraft/client/renderer/entity/state/ItemEntityRenderState;F)V",
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/renderer/entity/state/ItemEntityRenderState;extractItemGroupRenderState(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/client/renderer/item/ItemModelResolver;)V"
      ),
      index = 1
   )
   private ItemStack u(Entity var1, ItemStack var2, ItemModelResolver var3) {
      ItemEntity var4 = (ItemEntity)var1;
      var2 = yg.b(var4, var2);
      var2 = hk.G().e(var4, var2);
      var2 = oi.A().J(var4, var2);
      ItemStack var5 = ug.x().getVisualOverride(var2);
      if (var5 != null) {
         var2 = var5;
      }

      var2 = xa.r().R(var4, var2);
      var2 = js.A().Y(var4, var2);
      return eg.y().C(var4.getId(), var2);
   }
}
