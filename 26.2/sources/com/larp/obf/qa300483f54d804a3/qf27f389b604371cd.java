package com.larp.obf.qa300483f54d804a3;

import com.larpclient.module.BacktrackModule;
import net.minecraft.network.PacketListener;
import net.minecraft.network.protocol.Packet;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(targets = "net.minecraft.network.PacketProcessor$ListenerAndPacket")
public abstract class qf27f389b604371cd {
   @Shadow
   @Final
   private PacketListener listener;
   @Shadow
   @Final
   private Packet<?> packet;

   @Shadow
   public abstract void handle();

   @Inject(method = "handle", at = @At("HEAD"), cancellable = true)
   private void W(CallbackInfo var1) {
      if (!BacktrackModule.X()) {
         if (BacktrackModule.d(this.packet, this.listener, this::handle)) {
            var1.cancel();
         }
      }
   }
}
