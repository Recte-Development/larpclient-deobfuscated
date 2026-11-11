package com.larpclient.mixin;

import com.larpclient.util.world.FakeSpawnerController;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.protocol.game.ClientboundLevelChunkWithLightPacket;
import net.minecraft.world.level.chunk.LevelChunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPacketListener.class)
public abstract class ClientPacketListenerMixin_22 {
   @Shadow
   private ClientLevel level;

   @Inject(
      method = "handleLevelChunkWithLight",
      at = @At(
         value = "INVOKE",
         shift = Shift.AFTER,
         target = "Lnet/minecraft/client/multiplayer/ClientChunkCache;replaceWithPacketData(IILnet/minecraft/network/protocol/game/ClientboundLevelChunkPacketData;)Lnet/minecraft/world/level/chunk/LevelChunk;"
      )
   )
   private void k(ClientboundLevelChunkWithLightPacket var1, CallbackInfo var2) {
      FakeSpawnerController var3 = FakeSpawnerController.getInstance();
      if (this.level != null && var3.isActive()) {
         LevelChunk var4 = this.level.getChunk(var1.x(), var1.z());
         var3.isFakeSpawnerStack(var4, var4.getBlockEntities().keySet());
      }
   }
}
