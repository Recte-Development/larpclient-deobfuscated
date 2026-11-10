package com.larp.obf.qa300483f54d804a3;

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
public abstract class q71464821adce2513 {
   @Redirect(
      method = "handleBundlePacket",
      at = @At(value = "INVOKE", target = "Lnet/minecraft/network/protocol/Packet;handle(Lnet/minecraft/network/PacketListener;)V")
   )
   private <T extends PacketListener> void V(Packet<T> var1, T var2) {
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
      if (BacktrackModule.X() || !BacktrackModule.d(var1, var2, var3)) {
         var1.handle(var2);
      }
   }
}
