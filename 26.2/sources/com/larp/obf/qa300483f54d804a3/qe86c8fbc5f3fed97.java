package com.larp.obf.qa300483f54d804a3;

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
public class qe86c8fbc5f3fed97 {
   @ModifyArg(
      method = "sendPosition",
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/network/protocol/game/ServerboundMovePlayerPacket$PosRot;<init>(Lnet/minecraft/world/phys/Vec3;FFZZ)V"
      ),
      index = 3,
      require = 1
   )
   private boolean k(boolean var1) {
      return NoFallModule.f(var1);
   }

   @ModifyArg(
      method = "sendPosition",
      at = @At(value = "INVOKE", target = "Lnet/minecraft/network/protocol/game/ServerboundMovePlayerPacket$Pos;<init>(Lnet/minecraft/world/phys/Vec3;ZZ)V"),
      index = 1,
      require = 1
   )
   private boolean T(boolean var1) {
      return NoFallModule.f(var1);
   }

   @ModifyArg(
      method = "sendPosition",
      at = @At(value = "INVOKE", target = "Lnet/minecraft/network/protocol/game/ServerboundMovePlayerPacket$Rot;<init>(FFZZ)V"),
      index = 2,
      require = 1
   )
   private boolean y(boolean var1) {
      return NoFallModule.f(var1);
   }

   @ModifyArg(
      method = "sendPosition",
      at = @At(value = "INVOKE", target = "Lnet/minecraft/network/protocol/game/ServerboundMovePlayerPacket$StatusOnly;<init>(ZZ)V"),
      index = 0,
      require = 1
   )
   private boolean b(boolean var1) {
      return NoFallModule.f(var1);
   }

   @WrapOperation(
      method = "aiStep",
      at = @At(value = "INVOKE", target = "Lnet/minecraft/client/multiplayer/ClientPacketListener;send(Lnet/minecraft/network/protocol/Packet;)V")
   )
   private void A(ClientPacketListener var1, Packet<?> var2, Operation<Void> var3) {
      if (!NoFallModule.F(var2)) {
         var3.call(new Object[]{var1, var2});
      }
   }

   @Inject(method = "sendPosition", at = @At("TAIL"))
   private void K(CallbackInfo var1) {
      NoFallModule.R((LocalPlayer)this);
   }
}
