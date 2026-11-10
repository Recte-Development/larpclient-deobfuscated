package com.larp.obf.q41a5851bcf8e876a.q2d3f5242fd3d8828;

import com.larpclient.sl;
import net.minecraft.client.renderer.entity.ArmorStandRenderer;
import net.minecraft.client.renderer.entity.state.ArmorStandRenderState;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ArmorStandRenderer.class)
public class q949c82e34681e085 {
   @Inject(
      method = "extractRenderState(Lnet/minecraft/world/entity/decoration/ArmorStand;Lnet/minecraft/client/renderer/entity/state/ArmorStandRenderState;F)V",
      at = @At("TAIL")
   )
   private void M(ArmorStand var1, ArmorStandRenderState var2, float var3, CallbackInfo var4) {
      ItemStack[] var5 = sl.s.d(var1);
      if (var5 != null) {
         var2.headEquipment = var5[1];
         var2.chestEquipment = var5[2];
         var2.legsEquipment = var5[3];
         var2.feetEquipment = var5[4];
      }
   }
}
