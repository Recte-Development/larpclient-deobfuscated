package com.larp.obf.qa300483f54d804a3;

import com.larpclient.ls;
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
public abstract class qe333f830fec4403e {
   @Shadow
   private ClientLevel level;

   @Inject(method = "handleLevelChunkWithLight", at = @At("TAIL"))
   private void B(ClientboundLevelChunkWithLightPacket var1, CallbackInfo var2) {
      ls.z(this.level, var1.getX(), var1.getZ(), var1.getLightData());
      MotionScannerModule.t(this.level, var1.getX(), var1.getZ(), var1.getLightData());
   }

   @Inject(method = "handleLightUpdatePacket", at = @At("TAIL"))
   private void a(ClientboundLightUpdatePacket var1, CallbackInfo var2) {
      ls.z(this.level, var1.getX(), var1.getZ(), var1.getLightData());
      MotionScannerModule.t(this.level, var1.getX(), var1.getZ(), var1.getLightData());
   }
}
