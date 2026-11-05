package com.larp.obf.q5682c6a8aca9b6ec;

import com.larpclient.module.NoFallModule;
import net.minecraft.client.player.LocalPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(LocalPlayer.class)
public class qa4602ac4ac1a6e39 {
   @ModifyArg(
      method = "sendPosition",
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/network/protocol/game/ServerboundMovePlayerPacket$PosRot;<init>(Lnet/minecraft/world/phys/Vec3;FFZZ)V"
      ),
      index = 3,
      require = 1
   )
   private boolean W(boolean var1) {
      return NoFallModule.H(var1);
   }

   @ModifyArg(
      method = "sendPosition",
      at = @At(value = "INVOKE", target = "Lnet/minecraft/network/protocol/game/ServerboundMovePlayerPacket$Pos;<init>(Lnet/minecraft/world/phys/Vec3;ZZ)V"),
      index = 1,
      require = 1
   )
   private boolean q(boolean var1) {
      return NoFallModule.H(var1);
   }

   @ModifyArg(
      method = "sendPosition",
      at = @At(value = "INVOKE", target = "Lnet/minecraft/network/protocol/game/ServerboundMovePlayerPacket$Rot;<init>(FFZZ)V"),
      index = 2,
      require = 1
   )
   private boolean N(boolean var1) {
      return NoFallModule.H(var1);
   }

   @ModifyArg(
      method = "sendPosition",
      at = @At(value = "INVOKE", target = "Lnet/minecraft/network/protocol/game/ServerboundMovePlayerPacket$StatusOnly;<init>(ZZ)V"),
      index = 0,
      require = 1
   )
   private boolean i(boolean var1) {
      return NoFallModule.H(var1);
   }
}
