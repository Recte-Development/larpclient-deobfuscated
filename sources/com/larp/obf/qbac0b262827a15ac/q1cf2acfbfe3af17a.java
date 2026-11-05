package com.larp.obf.qbac0b262827a15ac;

import com.larpclient.ej;
import com.larpclient.module.MotionScannerModule;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.protocol.game.ClientboundLevelChunkWithLightPacket;
import net.minecraft.network.protocol.game.ClientboundLightUpdatePacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPacketListener.class)
public abstract class q1cf2acfbfe3af17a {
   @Shadow
   private ClientLevel level;

   @Inject(method = "handleLevelChunkWithLight", at = @At("TAIL"))
   private void R(ClientboundLevelChunkWithLightPacket var1, CallbackInfo var2) {
      ej.u(this.level, var1.x(), var1.z(), var1.lightData());
      MotionScannerModule.Q(this.level, var1.x(), var1.z(), var1.lightData());
   }

   @Inject(method = "handleLightUpdatePacket", at = @At("TAIL"))
   private void o(ClientboundLightUpdatePacket var1, CallbackInfo var2) {
      ej.u(this.level, var1.x(), var1.z(), var1.lightData());
      MotionScannerModule.Q(this.level, var1.x(), var1.z(), var1.lightData());
   }
}
