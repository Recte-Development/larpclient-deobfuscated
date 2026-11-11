package com.larpclient.mixin;

import com.larpclient.module.render.PlayerModelModule;
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
public class AvatarRendererMixin_2 {
   @Inject(
      method = "extractRenderState(Lnet/minecraft/world/entity/Avatar;Lnet/minecraft/client/renderer/entity/state/AvatarRenderState;F)V",
      at = @At("TAIL")
   )
   private void H(Avatar var1, AvatarRenderState var2, float var3, CallbackInfo var4) {
      PlayerModelModule var5 = PlayerModelModule.getInstance();
      if (var5 != null && var1 == Minecraft.getInstance().player) {
         var5.setRenderState(null);
         if (var5.isActive()) {
            var2.isInvisible = true;
            var2.isInvisibleToPlayer = true;
            var2.headEquipment = var2.legsEquipment = var2.feetEquipment = ItemStack.EMPTY;
            if (!var2.chestEquipment.has(DataComponents.GLIDER)) {
               var2.chestEquipment = ItemStack.EMPTY;
            }

            var2.parrotOnLeftShoulder = var2.parrotOnRightShoulder = null;
            var5.setRenderState(var2);
         }
      }
   }
}
