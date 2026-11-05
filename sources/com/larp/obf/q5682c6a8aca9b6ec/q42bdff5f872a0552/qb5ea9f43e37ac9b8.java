package com.larp.obf.q5682c6a8aca9b6ec.q42bdff5f872a0552;

import com.larpclient.hk;
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
public class qb5ea9f43e37ac9b8 {
   @Inject(
      method = "extractRenderState(Lnet/minecraft/world/entity/Avatar;Lnet/minecraft/client/renderer/entity/state/AvatarRenderState;F)V",
      at = @At("TAIL")
   )
   private void V(Avatar var1, AvatarRenderState var2, float var3, CallbackInfo var4) {
      Minecraft var5 = Minecraft.getInstance();
      if (var1 == var5.player) {
         ItemStack[] var6 = hk.T.U(var5);
         if (var6 != null) {
            var2.headEquipment = var6[1];
            var2.chestEquipment = var6[2];
            var2.legsEquipment = var6[3];
            var2.feetEquipment = var6[4];
         }
      }
   }
}
