package com.larpclient.mixin;

import com.larpclient.module.combat.BacktrackModule;
import com.larpclient.module.exploit.FakeLagModule;
import com.larpclient.module.exploit.FakePayModule;
import com.larpclient.util.entity.CombatState;
import io.netty.channel.ChannelFutureListener;
import net.minecraft.network.Connection;
import net.minecraft.network.PacketListener;
import net.minecraft.network.protocol.Packet;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Connection.class)
public class ConnectionMixin_2 {
   @Inject(method = "send(Lnet/minecraft/network/protocol/Packet;Lio/netty/channel/ChannelFutureListener;Z)V", at = @At("HEAD"), cancellable = true)
   private void C(Packet<?> var1, ChannelFutureListener var2, boolean var3, CallbackInfo var4) {
      if (!BacktrackModule.isReleasingPongs()) {
         if (FakePayModule.blockPayPacket(var1)) {
            var4.cancel();
         } else if (BacktrackModule.hasHeldPackets() && BacktrackModule.interceptSend((Connection)this, var1, var2, var3)) {
            var4.cancel();
         } else if (FakeLagModule.isActive() && FakeLagModule.interceptSend((Connection)this, var1, var2, var3)) {
            var4.cancel();
         } else {
            CombatState.onPacket(var1);
         }
      }
   }

   @Inject(method = "genericsFtw", at = @At("HEAD"))
   private static void j(Packet<?> var0, PacketListener var1, CallbackInfo var2) {
      BacktrackModule.onReceive(var0, var1);
   }
}
