package com.larpclient.mixin;

import com.larpclient.module.combat.BacktrackModule;
import net.minecraft.network.PacketListener;
import net.minecraft.network.protocol.Packet;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(targets = "net.minecraft.network.PacketProcessor$ListenerAndPacket")
public abstract class PacketProcessorListenerAndPacketMixin {
   @Shadow
   @Final
   private PacketListener listener;
   @Shadow
   @Final
   private Packet<?> packet;

   @Shadow
   public abstract void handle();

   @Inject(method = "handle", at = @At("HEAD"), cancellable = true)
   private void B(CallbackInfo var1) {
      if (!BacktrackModule.boxDistanceSq()) {
         if (BacktrackModule.releaseHeld(this.packet, this.listener, this::handle)) {
            var1.cancel();
         }
      }
   }
}
