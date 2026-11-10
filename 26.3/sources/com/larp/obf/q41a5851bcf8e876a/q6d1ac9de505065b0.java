package com.larp.obf.q41a5851bcf8e876a;

import com.larpclient.module.PlayerModelModule;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.player.AvatarRenderer;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.entity.Avatar;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AvatarRenderer.class)
public class q6d1ac9de505065b0 {
   @Inject(
      method = "extractRenderState(Lnet/minecraft/world/entity/Avatar;Lnet/minecraft/client/renderer/entity/state/AvatarRenderState;F)V",
      at = @At("TAIL")
   )
   private void H(Avatar var1, AvatarRenderState var2, float var3, CallbackInfo var4) {
      PlayerModelModule var5 = PlayerModelModule.k();
      if (var5 != null && var1 == Minecraft.getInstance().player) {
         var5.J(null);
         if (var5.G()) {
            var2.isInvisible = true;
            var2.isInvisibleToPlayer = true;
            var2.headEquipment = var2.legsEquipment = var2.feetEquipment = ItemStack.EMPTY;
            if (!var2.chestEquipment.has(DataComponents.GLIDER)) {
               var2.chestEquipment = ItemStack.EMPTY;
            }

            var2.parrotOnLeftShoulder = var2.parrotOnRightShoulder = null;
            var5.J(var2);
         }
      }
   }
}
