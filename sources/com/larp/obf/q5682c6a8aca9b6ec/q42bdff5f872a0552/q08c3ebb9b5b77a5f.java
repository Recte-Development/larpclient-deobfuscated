package com.larp.obf.q5682c6a8aca9b6ec.q42bdff5f872a0552;

import com.larpclient.xs;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ServerboundPlayerActionPacket;
import net.minecraft.network.protocol.game.ServerboundPlayerActionPacket.Action;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Minecraft.class)
public class q08c3ebb9b5b77a5f {
   @WrapOperation(
      method = "handleKeybinds",
      at = @At(value = "INVOKE", target = "Lnet/minecraft/client/multiplayer/ClientPacketListener;send(Lnet/minecraft/network/protocol/Packet;)V")
   )
   private void D(ClientPacketListener var1, Packet<?> var2, Operation<Void> var3) {
      if (!(var2 instanceof ServerboundPlayerActionPacket var4 && var4.getAction() == Action.SWAP_ITEM_WITH_OFFHAND && xs.x(Minecraft.getInstance()))) {
         var3.call(new Object[]{var1, var2});
      }
   }
}
