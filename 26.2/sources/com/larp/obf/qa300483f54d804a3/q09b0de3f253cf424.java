package com.larp.obf.qa300483f54d804a3;

import com.larpclient.module.AntiTrapModule;
import com.larpclient.module.IgnoreEntityModule;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import java.util.function.Predicate;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(LocalPlayer.class)
public class q09b0de3f253cf424 {
   @ModifyExpressionValue(
      method = "pick",
      at = @At(value = "FIELD", target = "Lnet/minecraft/world/entity/EntitySelector;CAN_BE_PICKED:Ljava/util/function/Predicate;")
   )
   private static Predicate<Entity> n(Predicate<Entity> var0) {
      return !Minecraft.getInstance().options.keyUse.isDown() ? var0 : var0.and(var0x -> !AntiTrapModule.K(var0x) && !IgnoreEntityModule.i(var0x));
   }

   @ModifyExpressionValue(
      method = "raycastHitResult",
      at = @At(value = "FIELD", target = "Lnet/minecraft/world/entity/EntitySelector;CAN_BE_PICKED:Ljava/util/function/Predicate;")
   )
   private Predicate<Entity> y(Predicate<Entity> var1) {
      return n(var1);
   }
}
