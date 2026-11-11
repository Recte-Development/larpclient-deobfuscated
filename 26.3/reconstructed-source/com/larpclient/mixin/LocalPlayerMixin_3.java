package com.larpclient.mixin;

import com.larpclient.module.combat.AntiTrapModule;
import com.larpclient.module.misc.IgnoreEntityModule;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import java.util.function.Predicate;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(LocalPlayer.class)
public class LocalPlayerMixin_3 {
   @ModifyExpressionValue(
      method = "pick",
      at = @At(value = "FIELD", target = "Lnet/minecraft/world/entity/EntitySelector;CAN_BE_PICKED:Ljava/util/function/Predicate;")
   )
   private static Predicate<Entity> T(Predicate<Entity> var0) {
      return !Minecraft.getInstance().options.keyUse.isDown()
         ? var0
         : var0.and(var0x -> !AntiTrapModule.shouldIgnoreEntity(var0x) && !IgnoreEntityModule.shouldIgnoreEntity(var0x));
   }

   @ModifyExpressionValue(
      method = "raycastHitResult",
      at = @At(value = "FIELD", target = "Lnet/minecraft/world/entity/EntitySelector;CAN_BE_PICKED:Ljava/util/function/Predicate;")
   )
   private Predicate<Entity> z(Predicate<Entity> var1) {
      return T(var1);
   }
}
