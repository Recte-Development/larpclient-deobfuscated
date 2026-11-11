package com.larpclient.mixin;

import com.larpclient.module.combat.AnchorHunter;
import com.larpclient.module.movement.AutoElytraPilot;
import com.larpclient.module.world.AutoAreaMiner;
import com.larpclient.module.world.AutoBuilderModule;
import com.larpclient.util.math.RotationSmoother2;
import net.minecraft.client.player.ClientInput;
import net.minecraft.client.player.KeyboardInput;
import net.minecraft.world.entity.player.Input;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(KeyboardInput.class)
public abstract class KeyboardInputMixin3 extends ClientInput {
   @Inject(method = "tick", at = @At("TAIL"))
   private void X(CallbackInfo var1) {
      Input var2 = AutoElytraPilot.setDestinationFromView();
      if (var2 == null) {
         var2 = AnchorHunter.collectAnchorDrop();
      }

      if (var2 == null) {
         var2 = AutoAreaMiner.findBlockSlot();
      }

      if (var2 == null) {
         var2 = AutoBuilderModule.getMovementInput();
      }

      if (var2 != null) {
         this.keyPresses = var2;
         this.moveVector = RotationSmoother2.inputToVector(var2);
      }
   }
}
