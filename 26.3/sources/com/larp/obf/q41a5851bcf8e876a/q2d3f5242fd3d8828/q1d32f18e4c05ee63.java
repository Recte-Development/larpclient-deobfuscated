package com.larp.obf.q41a5851bcf8e876a.q2d3f5242fd3d8828;

import com.larpclient.vb;
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
public class q1d32f18e4c05ee63 {
   @WrapOperation(
      method = "handleKeybinds",
      at = @At(value = "INVOKE", target = "Lnet/minecraft/client/multiplayer/ClientPacketListener;send(Lnet/minecraft/network/protocol/Packet;)V")
   )
   private void m(ClientPacketListener var1, Packet<?> var2, Operation<Void> var3) {
      if (!(var2 instanceof ServerboundPlayerActionPacket var4 && var4.getAction() == Action.SWAP_ITEM_WITH_OFFHAND && vb.r(Minecraft.getInstance()))) {
         var3.call(new Object[]{var1, var2});
      }
   }
}
