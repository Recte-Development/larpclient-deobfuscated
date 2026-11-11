package com.larpclient.mixin;

import com.larpclient.config.VisualWinnerConfig;
import com.larpclient.module.exploit.BlackjackRigger;
import com.larpclient.module.exploit.CardForce;
import com.larpclient.module.exploit.HigherLowerSwapper;
import com.larpclient.module.misc.ArrowGame;
import com.larpclient.util.inventory.MapSwapInventory;
import com.larpclient.util.misc.FiftyFiftyDispenser;
import com.larpclient.util.world.DispenserRegistry;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.protocol.game.ClientboundBlockUpdatePacket;
import net.minecraft.network.protocol.game.ClientboundContainerClosePacket;
import net.minecraft.network.protocol.game.ClientboundContainerSetContentPacket;
import net.minecraft.network.protocol.game.ClientboundContainerSetSlotPacket;
import net.minecraft.network.protocol.game.ClientboundLevelEventPacket;
import net.minecraft.network.protocol.game.ClientboundSectionBlocksUpdatePacket;
import net.minecraft.network.protocol.game.ClientboundSetCursorItemPacket;
import net.minecraft.network.protocol.game.ClientboundSetPlayerInventoryPacket;
import net.minecraft.network.protocol.game.ClientboundSoundPacket;
import net.minecraft.network.protocol.game.ClientboundTakeItemEntityPacket;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.saveddata.maps.MapId;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPacketListener.class)
public abstract class ClientPacketListenerMixin_23 {
   private static final double E = 256.0;

   @Inject(method = "handleLevelEvent", at = @At("TAIL"))
   private void C(ClientboundLevelEventPacket var1, CallbackInfo var2) {
      if (var1.getType() == 1000) {
         BlackjackRigger.getInstance().getForcedRank(var1.getPos());
      }
   }

   @Inject(method = "handleBlockUpdate", at = @At("TAIL"))
   private void E(ClientboundBlockUpdatePacket var1, CallbackInfo var2) {
      FiftyFiftyDispenser.getInstance().isActive(var1.getPos(), var1.getBlockState());
      BlackjackRigger.getInstance().isDispenserScreenOpen(var1.getPos(), var1.getBlockState());
      DispenserRegistry.getInstance().onDispenserUpdate(Minecraft.getInstance(), var1.getPos(), var1.getBlockState());
   }

   @Inject(method = "handleChunkBlocksUpdate", at = @At("TAIL"))
   private void p(ClientboundSectionBlocksUpdatePacket var1, CallbackInfo var2) {
      Minecraft var3 = Minecraft.getInstance();
      DispenserRegistry var4 = DispenserRegistry.getInstance();
      var1.runUpdates((var2x, var3x) -> {
         FiftyFiftyDispenser.getInstance().isActive(var2x, var3x);
         BlackjackRigger.getInstance().isDispenserScreenOpen(var2x, var3x);
         var4.onDispenserUpdate(var3, var2x, var3x);
      });
   }

   @Inject(
      method = "handleContainerClose",
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/network/protocol/PacketUtils;ensureRunningOnSameThread(Lnet/minecraft/network/protocol/Packet;Lnet/minecraft/network/PacketListener;Lnet/minecraft/network/PacketProcessor;)V",
         shift = Shift.AFTER
      )
   )
   private void h(ClientboundContainerClosePacket var1, CallbackInfo var2) {
      CardForce.findInventorySlot().logServerClose(var1.getContainerId());
   }

   @Inject(method = "handleContainerContent", at = @At("TAIL"))
   private void Y(ClientboundContainerSetContentPacket var1, CallbackInfo var2) {
      if (var1 != null) {
         CardForce.findInventorySlot().onInventorySynchronized(var1.containerId(), var1.items(), var1.carriedItem());
         DispenserRegistry.getInstance().selectNearestKey(var1.containerId());
         MapSwapInventory.isPlayerMoving(var1.containerId());
         FiftyFiftyDispenser.getInstance().onContainerContent(var1.containerId(), var1.items(), var1.carriedItem());
         BlackjackRigger.getInstance().onContainerContent(var1.containerId(), var1.items(), var1.carriedItem());
      }
   }

   @Inject(method = "handleSetCursorItem", at = @At("TAIL"))
   private void S(ClientboundSetCursorItemPacket var1, CallbackInfo var2) {
      CardForce.findInventorySlot().onServerSlot(-1, -1, var1.contents());
   }

   @Inject(method = "handleSetPlayerInventory", at = @At("TAIL"))
   private void N(ClientboundSetPlayerInventoryPacket var1, CallbackInfo var2) {
      FiftyFiftyDispenser.getInstance().findItemSlots(var1.slot(), var1.contents());
      BlackjackRigger.getInstance().onInventorySlot(var1.slot(), var1.contents());
      boolean var3 = !(CardForce.findInventorySlot() instanceof HigherLowerSwapper) && CardForce.findInventorySlot().isSwapJuggling();
      CardForce.findInventorySlot().onPlayerInventorySlot(var1.slot(), var1.contents());
      CardForce var4 = CardForce.getActive();
      Minecraft var5 = Minecraft.getInstance();
      if (!var3 && !(var4 instanceof HigherLowerSwapper) && !var4.isSwapActive() && var5.player != null) {
         MapId var6 = (MapId)var1.contents().get(DataComponents.MAP_ID);
         if (var6 != null && var1.contents().is(Items.FILLED_MAP)) {
            for (Slot var8 : var5.player.inventoryMenu.slots) {
               if (var8.container == var5.player.getInventory() && var8.getContainerSlot() == var1.slot()) {
                  var4.onDealtCardSlotUpdate(0, var8.index, var6.id());
                  break;
               }
            }
         }
      }
   }

   @Inject(method = "handleContainerSetSlot", at = @At("TAIL"))
   private void R(ClientboundContainerSetSlotPacket var1, CallbackInfo var2) {
      FiftyFiftyDispenser.getInstance().onSlotUpdate(var1.getContainerId(), var1.getSlot(), var1.getItem());
      BlackjackRigger.getInstance().onSetSlot(var1.getContainerId(), var1.getSlot(), var1.getItem());
      if (var1 != null) {
         boolean var3 = !(CardForce.findInventorySlot() instanceof HigherLowerSwapper) && CardForce.findInventorySlot().isSwapJuggling();
         CardForce.findInventorySlot().onServerSlot(var1.getContainerId(), var1.getSlot(), var1.getItem());
         MapSwapInventory.onStockSlotUpdated(var1.getContainerId(), var1.getSlot());
         DispenserRegistry var4 = DispenserRegistry.getInstance();
         var4.onTemplateSlotClick(var1.getContainerId(), var1.getSlot());
         ArrowGame var5 = ArrowGame.getInstance();
         if (var4.loadAssignments() == DispenserRegistry.GameType.ARROW_GAME && var4.getActiveKey() == var1.getContainerId()) {
            int var6 = var1.getSlot();
            if (var6 >= 0 && var6 <= 44) {
               var5.cancelMapSelection(var4.getActiveKey(), var6, var1.getItem());
            }
         } else if (var1.getContainerId() == 0) {
            var5.isHoldingFilledMap(var1.getSlot(), var1.getItem());
         }

         int var12 = var1.getContainerId();
         int var7 = var1.getSlot();
         CardForce var8 = CardForce.getActive();
         if (!var3 && (var8 instanceof HigherLowerSwapper || !var8.isSwapJuggling())) {
            boolean var9 = var12 == 0 || var12 == var8.getSwapSyncId() && var7 >= (var8 instanceof HigherLowerSwapper ? 36 : 9);
            if (var9) {
               ItemStack var10 = var1.getItem();
               if (var10 != null && !var10.isEmpty() && var10.is(Items.FILLED_MAP)) {
                  MapId var11 = (MapId)var10.getComponents().get(DataComponents.MAP_ID);
                  if (var11 != null) {
                     var8.onDealtCardSlotUpdate(var12, var7, var11.id());
                  }
               }
            }
         }
      }
   }

   @WrapOperation(method = "handleContainerSetSlot", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;setPopTime(I)V"), require = 0)
   private void v(ItemStack var1, int var2, Operation<Void> var3) {
      if (CardForce.getActive().isDealBobAllowed(var1)) {
         var3.call(new Object[]{var1, var2});
      }
   }

   @Inject(method = "handleTakeItemEntity", at = @At("TAIL"))
   private void l(ClientboundTakeItemEntityPacket var1, CallbackInfo var2) {
      Minecraft var3 = Minecraft.getInstance();
      if (var1 != null && var3 != null && var3.player != null) {
         VisualWinnerConfig.getInstance().onContainerClick(var3, var1.getItemId(), var1.getPlayerId(), var1.getAmount());
      }
   }

   @Inject(method = "handleSoundEvent", at = @At("HEAD"), cancellable = true)
   private void S(ClientboundSoundPacket var1, CallbackInfo var2) {
      if (var1 != null) {
         Holder var3 = var1.getSound();
         if (var3 != null) {
            SoundEvent var4 = (SoundEvent)var3.value();
            if (var4 == SoundEvents.ITEM_FRAME_BREAK || var4 == SoundEvents.ITEM_FRAME_REMOVE_ITEM) {
               Minecraft var5 = Minecraft.getInstance();
               if (var5 != null && var5.level != null) {
                  ArrowGame var6 = ArrowGame.getInstance();
                  if (var6.enabled && !"off".equals(var6.getTargetName())) {
                     BlockPos var7 = DispenserRegistry.getInstance().onDispenserUpdate(DispenserRegistry.GameType.ARROW_GAME);
                     if (var7 != null) {
                        double var8 = var1.getX() - (var7.getX() + 0.5);
                        double var10 = var1.getY() - (var7.getY() + 0.5);
                        double var12 = var1.getZ() - (var7.getZ() + 0.5);
                        if (var8 * var8 + var10 * var10 + var12 * var12 <= 256.0) {
                           var2.cancel();
                        }
                     }
                  }
               }
            }
         }
      }
   }
}
