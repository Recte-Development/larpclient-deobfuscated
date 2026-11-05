package com.larp.obf.q5682c6a8aca9b6ec;

import com.larpclient.module.BacktrackModule;
import net.minecraft.ReportedException;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.PacketListener;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.PacketUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ClientPacketListener.class)
public abstract class q27799d38983c5bcd {
   @Redirect(
      method = "handleBundlePacket",
      at = @At(value = "INVOKE", target = "Lnet/minecraft/network/protocol/Packet;handle(Lnet/minecraft/network/PacketListener;)V")
   )
   private <T extends PacketListener> void i(Packet<T> var1, T var2) {
      Runnable var3 = () -> {
         if (var2.shouldHandleMessage(var1)) {
            try {
               var1.handle(var2);
            } catch (Exception var4) {
               if (var4 instanceof ReportedException var3x && var3x.getCause() instanceof OutOfMemoryError) {
                  throw PacketUtils.makeReportedException(var4, var1, var2);
               }

               var2.onPacketError(var1, var4);
            }
         }
      };
      if (BacktrackModule.G() || !BacktrackModule.a(var1, var2, var3)) {
         var1.handle(var2);
      }
   }
}
