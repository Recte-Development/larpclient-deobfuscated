package com.larp.obf.qbac0b262827a15ac;

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
public class q6bbd07a56bc08b1f {
   @ModifyExpressionValue(
      method = "pick",
      at = @At(value = "FIELD", target = "Lnet/minecraft/world/entity/EntitySelector;CAN_BE_PICKED:Ljava/util/function/Predicate;")
   )
   private static Predicate<Entity> r(Predicate<Entity> var0) {
      return !Minecraft.getInstance().options.keyUse.isDown() ? var0 : var0.and(var0x -> !AntiTrapModule.l(var0x) && !IgnoreEntityModule.P(var0x));
   }

   @ModifyExpressionValue(
      method = "raycastHitResult",
      at = @At(value = "FIELD", target = "Lnet/minecraft/world/entity/EntitySelector;CAN_BE_PICKED:Ljava/util/function/Predicate;")
   )
   private Predicate<Entity> d(Predicate<Entity> var1) {
      return r(var1);
   }
}
