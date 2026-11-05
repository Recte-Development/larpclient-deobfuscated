package com.larp.obf.q5682c6a8aca9b6ec;

import com.larpclient.ye;
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
public abstract class qeff7ab137576368d {
   @Shadow
   private ClientLevel level;

   @Inject(method = "handleLevelChunkWithLight", at = @At("TAIL"))
   private void p(ClientboundLevelChunkWithLightPacket var1, CallbackInfo var2) {
      ye.y(this.level, var1.x(), var1.z(), var1.lightData());
      MotionScannerModule.C(this.level, var1.x(), var1.z(), var1.lightData());
   }

   @Inject(method = "handleLightUpdatePacket", at = @At("TAIL"))
   private void f(ClientboundLightUpdatePacket var1, CallbackInfo var2) {
      ye.y(this.level, var1.x(), var1.z(), var1.lightData());
      MotionScannerModule.C(this.level, var1.x(), var1.z(), var1.lightData());
   }
}
