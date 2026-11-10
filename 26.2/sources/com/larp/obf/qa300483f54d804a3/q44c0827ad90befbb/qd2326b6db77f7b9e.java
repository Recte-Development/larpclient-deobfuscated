package com.larp.obf.qa300483f54d804a3.q44c0827ad90befbb;

import com.larpclient.cy;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.player.AvatarRenderer;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.world.entity.Avatar;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AvatarRenderer.class)
public class qd2326b6db77f7b9e {
   @Inject(
      method = "extractRenderState(Lnet/minecraft/world/entity/Avatar;Lnet/minecraft/client/renderer/entity/state/AvatarRenderState;F)V",
      at = @At("TAIL")
   )
   private void H(Avatar var1, AvatarRenderState var2, float var3, CallbackInfo var4) {
      Minecraft var5 = Minecraft.getInstance();
      if (var1 == var5.player) {
         ItemStack[] var6 = cy.d.v(var5);
         if (var6 != null) {
            var2.headEquipment = var6[1];
            var2.chestEquipment = var6[2];
            var2.legsEquipment = var6[3];
            var2.feetEquipment = var6[4];
         }
      }
   }
}
