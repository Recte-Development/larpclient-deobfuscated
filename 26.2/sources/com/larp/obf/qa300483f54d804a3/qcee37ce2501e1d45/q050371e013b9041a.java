package com.larp.obf.qa300483f54d804a3.qcee37ce2501e1d45;

import com.larpclient.b8;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.protocol.game.ClientboundLevelChunkPacketData;
import net.minecraft.world.level.chunk.LevelChunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPacketListener.class)
public abstract class q050371e013b9041a {
   @Shadow
   private ClientLevel level;

   @Inject(method = "updateLevelChunk", at = @At("TAIL"))
   private void t(int var1, int var2, ClientboundLevelChunkPacketData var3, CallbackInfo var4) {
      b8 var5 = b8.k();
      if (this.level != null && var5.J()) {
         LevelChunk var6 = this.level.getChunk(var1, var2);
         var5.t(var6, var6.getBlockEntities().keySet());
      }
   }
}
