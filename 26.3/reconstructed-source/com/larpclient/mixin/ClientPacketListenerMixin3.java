package com.larpclient.mixin;

import com.larpclient.module.exploit.BlackjackRigger;
import com.larpclient.module.exploit.CardForce;
import com.larpclient.util.misc.FiftyFiftyDispenser;
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
public abstract class ClientPacketListenerMixin3 {
   @Inject(method = "handleOpenScreen", at = @At("TAIL"))
   private void F(ClientboundOpenScreenPacket var1, CallbackInfo var2) {
      Minecraft var3 = Minecraft.getInstance();
      if (var3.player != null) {
         BlackjackRigger.getInstance().getInstance(var3.player.containerMenu);
      }
   }

   @Inject(method = "handleBlockChangedAck", at = @At("TAIL"))
   private void B(ClientboundBlockChangedAckPacket var1, CallbackInfo var2) {
      CardForce.findInventorySlot().onBlockInteractionAcknowledged(var1.sequence());
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
   private void v(ClientboundOpenScreenPacket var1, CallbackInfo var2) {
      if (var1 != null) {
         MenuType var3 = var1.getType();
         if (FiftyFiftyDispenser.getInstance().onMenuOpen(var3, var1.getContainerId())) {
            var2.cancel();
         } else if (BlackjackRigger.getInstance().onSetSlot(var3, var1.getContainerId())) {
            var2.cancel();
         } else {
            CardForce var4 = CardForce.getActive();
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
