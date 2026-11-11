package com.larpclient.mixin;

import com.larpclient.hud.VisualInventory;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.MouseHandler;
import net.minecraft.world.entity.player.Inventory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(MouseHandler.class)
public abstract class MouseHandlerMixin2 {
   @WrapOperation(method = "onScroll", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Inventory;setSelectedSlot(I)V"))
   private void g(Inventory var1, int var2, Operation<Void> var3) {
      if (VisualInventory.isActive()) {
         VisualInventory.selectHotbarSlot(var2);
      } else {
         var3.call(new Object[]{var1, var2});
      }
   }

   @WrapOperation(method = "onScroll", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Inventory;getSelectedSlot()I"))
   private int w(Inventory var1, Operation<Integer> var2) {
      return VisualInventory.getSelectedSlot((Integer)var2.call(new Object[]{var1}));
   }
}
