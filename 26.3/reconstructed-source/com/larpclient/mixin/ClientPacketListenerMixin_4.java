package com.larpclient.mixin;

import com.larpclient.module.world.MotionScannerModule;
import com.larpclient.util.data.LightCaptureTracker;
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
public abstract class ClientPacketListenerMixin_4 {
   @Shadow
   private ClientLevel level;

   @Inject(method = "handleLevelChunkWithLight", at = @At("TAIL"))
   private void d(ClientboundLevelChunkWithLightPacket var1, CallbackInfo var2) {
      LightCaptureTracker.onLightUpdate(this.level, var1.x(), var1.z(), var1.lightData());
      MotionScannerModule.onLightUpdate(this.level, var1.x(), var1.z(), var1.lightData());
   }

   @Inject(method = "handleLightUpdatePacket", at = @At("TAIL"))
   private void f(ClientboundLightUpdatePacket var1, CallbackInfo var2) {
      LightCaptureTracker.onLightUpdate(this.level, var1.x(), var1.z(), var1.lightData());
      MotionScannerModule.onLightUpdate(this.level, var1.x(), var1.z(), var1.lightData());
   }
}
