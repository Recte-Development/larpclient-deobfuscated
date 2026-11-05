package com.larp.obf.qbac0b262827a15ac.q03e2e72dbe55651a;

import com.larpclient._a;
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
public class q9942f4918e58dbd5 {
   @WrapOperation(
      method = "handleKeybinds",
      at = @At(value = "INVOKE", target = "Lnet/minecraft/client/multiplayer/ClientPacketListener;send(Lnet/minecraft/network/protocol/Packet;)V")
   )
   private void F(ClientPacketListener var1, Packet<?> var2, Operation<Void> var3) {
      if (!(var2 instanceof ServerboundPlayerActionPacket var4 && var4.getAction() == Action.SWAP_ITEM_WITH_OFFHAND && _a.y(Minecraft.getInstance()))) {
         var3.call(new Object[]{var1, var2});
      }
   }
}
