package com.larp.obf.q41a5851bcf8e876a;

import com.larpclient.module.NoFallModule;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.protocol.Packet;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LocalPlayer.class)
public class q3dcc03449f1cb242 {
   @ModifyArg(
      method = "sendPosition",
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/network/protocol/game/ServerboundMovePlayerPacket$PosRot;<init>(Lnet/minecraft/world/phys/Vec3;FFZZ)V"
      ),
      index = 3,
      require = 1
   )
   private boolean o(boolean var1) {
      return NoFallModule.O(var1);
   }

   @ModifyArg(
      method = "sendPosition",
      at = @At(value = "INVOKE", target = "Lnet/minecraft/network/protocol/game/ServerboundMovePlayerPacket$Pos;<init>(Lnet/minecraft/world/phys/Vec3;ZZ)V"),
      index = 1,
      require = 1
   )
   private boolean N(boolean var1) {
      return NoFallModule.O(var1);
   }

   @ModifyArg(
      method = "sendPosition",
      at = @At(value = "INVOKE", target = "Lnet/minecraft/network/protocol/game/ServerboundMovePlayerPacket$Rot;<init>(FFZZ)V"),
      index = 2,
      require = 1
   )
   private boolean L(boolean var1) {
      return NoFallModule.O(var1);
   }

   @ModifyArg(
      method = "sendPosition",
      at = @At(value = "INVOKE", target = "Lnet/minecraft/network/protocol/game/ServerboundMovePlayerPacket$StatusOnly;<init>(ZZ)V"),
      index = 0,
      require = 1
   )
   private boolean e(boolean var1) {
      return NoFallModule.O(var1);
   }

   @WrapOperation(
      method = "aiStep",
      at = @At(value = "INVOKE", target = "Lnet/minecraft/client/multiplayer/ClientPacketListener;send(Lnet/minecraft/network/protocol/Packet;)V")
   )
   private void s(ClientPacketListener var1, Packet<?> var2, Operation<Void> var3) {
      if (!NoFallModule.z(var2)) {
         var3.call(new Object[]{var1, var2});
      }
   }

   @Inject(method = "sendPosition", at = @At("TAIL"))
   private void h(CallbackInfo var1) {
      NoFallModule.B((LocalPlayer)this);
   }
}
