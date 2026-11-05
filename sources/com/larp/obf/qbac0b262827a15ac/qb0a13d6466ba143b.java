package com.larp.obf.qbac0b262827a15ac;

import com.larpclient.module.NoFallModule;
import net.minecraft.client.player.LocalPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(LocalPlayer.class)
public class qb0a13d6466ba143b {
   @ModifyArg(
      method = "sendPosition",
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/network/protocol/game/ServerboundMovePlayerPacket$PosRot;<init>(Lnet/minecraft/world/phys/Vec3;FFZZ)V"
      ),
      index = 3,
      require = 1
   )
   private boolean S(boolean var1) {
      return NoFallModule.M(var1);
   }

   @ModifyArg(
      method = "sendPosition",
      at = @At(value = "INVOKE", target = "Lnet/minecraft/network/protocol/game/ServerboundMovePlayerPacket$Pos;<init>(Lnet/minecraft/world/phys/Vec3;ZZ)V"),
      index = 1,
      require = 1
   )
   private boolean j(boolean var1) {
      return NoFallModule.M(var1);
   }

   @ModifyArg(
      method = "sendPosition",
      at = @At(value = "INVOKE", target = "Lnet/minecraft/network/protocol/game/ServerboundMovePlayerPacket$Rot;<init>(FFZZ)V"),
      index = 2,
      require = 1
   )
   private boolean a(boolean var1) {
      return NoFallModule.M(var1);
   }

   @ModifyArg(
      method = "sendPosition",
      at = @At(value = "INVOKE", target = "Lnet/minecraft/network/protocol/game/ServerboundMovePlayerPacket$StatusOnly;<init>(ZZ)V"),
      index = 0,
      require = 1
   )
   private boolean h(boolean var1) {
      return NoFallModule.M(var1);
   }
}
