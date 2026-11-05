package com.larp.obf.q5682c6a8aca9b6ec.q42bdff5f872a0552;

import com.larpclient.mm;
import com.larpclient.ro;
import com.larpclient.ug;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockChangedAckPacket;
import net.minecraft.network.protocol.game.ClientboundOpenScreenPacket;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPacketListener.class)
public abstract class qe3c71246c81ff684 {
   @Inject(method = "handleOpenScreen", at = @At("TAIL"))
   private void w(ClientboundOpenScreenPacket var1, CallbackInfo var2) {
      Minecraft var3 = Minecraft.getInstance();
      if (var3.player != null) {
         mm.W().w(var3.player.containerMenu);
      }
   }

   @Inject(method = "handleBlockChangedAck", at = @At("TAIL"))
   private void f(ClientboundBlockChangedAckPacket var1, CallbackInfo var2) {
      ug.h().onBlockInteractionAcknowledged(var1.sequence());
   }

   @Inject(
      method = "handleOpenScreen",
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/network/protocol/PacketUtils;ensureRunningOnSameThread(Lnet/minecraft/network/protocol/Packet;Lnet/minecraft/network/PacketListener;Lnet/minecraft/network/PacketProcessor;)V",
         shift = Shift.AFTER
      ),
      cancellable = true
   )
   private void z(ClientboundOpenScreenPacket var1, CallbackInfo var2) {
      if (var1 != null) {
         MenuType var3 = var1.getType();
         if (ro.H().S(var3, var1.getContainerId())) {
            var2.cancel();
         } else if (mm.W().y(var3, var1.getContainerId())) {
            var2.cancel();
         } else {
            ug var4 = ug.x();
            if (var4.shouldSuppressOpen(var3)) {
               Minecraft var5 = Minecraft.getInstance();
               if (var5 != null && var5.player != null) {
                  AbstractContainerMenu var6 = var3.create(var1.getContainerId(), var5.player.getInventory());
                  if (var4.isSwapActive()) {
                     var6.setCarried(var5.player.containerMenu.getCarried().copy());
                  }

                  var5.player.containerMenu = var6;
                  var4.onSwapContainerOpened(var3, var1.getContainerId());
                  var2.cancel();
               }
            }
         }
      }
   }
}
