package com.larp.obf.qa300483f54d804a3.q44c0827ad90befbb;

import com.larpclient.bs;
import com.larpclient.g5;
import com.larpclient.gj;
import com.larpclient.lh;
import com.larpclient.mg;
import com.larpclient.pw;
import com.larpclient.wc;
import com.larpclient.we;
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
public abstract class q31da586bc3efc9ff {
   private static final double y = 256.0;

   @Inject(method = "handleLevelEvent", at = @At("TAIL"))
   private void g(ClientboundLevelEventPacket var1, CallbackInfo var2) {
      if (var1.getType() == 1000) {
         lh.I().V(var1.getPos());
      }
   }

   @Inject(method = "handleBlockUpdate", at = @At("TAIL"))
   private void f(ClientboundBlockUpdatePacket var1, CallbackInfo var2) {
      pw.S().B(var1.getPos(), var1.getBlockState());
      lh.I().H(var1.getPos(), var1.getBlockState());
      gj.c().h(Minecraft.getInstance(), var1.getPos(), var1.getBlockState());
   }

   @Inject(method = "handleChunkBlocksUpdate", at = @At("TAIL"))
   private void W(ClientboundSectionBlocksUpdatePacket var1, CallbackInfo var2) {
      Minecraft var3 = Minecraft.getInstance();
      gj var4 = gj.c();
      var1.runUpdates((var2x, var3x) -> {
         pw.S().B(var2x, var3x);
         lh.I().H(var2x, var3x);
         var4.h(var3, var2x, var3x);
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
   private void W(ClientboundContainerClosePacket var1, CallbackInfo var2) {
      we.l().y(var1.getContainerId());
   }

   @Inject(method = "handleContainerContent", at = @At("TAIL"))
   private void y(ClientboundContainerSetContentPacket var1, CallbackInfo var2) {
      if (var1 != null) {
         we.l().onInventorySynchronized(var1.containerId(), var1.items(), var1.carriedItem());
         gj.c().e(var1.containerId());
         mg.m(var1.containerId());
         pw.S().J(var1.containerId(), var1.items(), var1.carriedItem());
         lh.I().V(var1.containerId(), var1.items(), var1.carriedItem());
      }
   }

   @Inject(method = "handleSetCursorItem", at = @At("TAIL"))
   private void E(ClientboundSetCursorItemPacket var1, CallbackInfo var2) {
      we.l().onServerSlot(-1, -1, var1.contents());
   }

   @Inject(method = "handleSetPlayerInventory", at = @At("TAIL"))
   private void f(ClientboundSetPlayerInventoryPacket var1, CallbackInfo var2) {
      pw.S().O(var1.slot(), var1.contents());
      lh.I().h(var1.slot(), var1.contents());
      boolean var3 = !(we.l() instanceof wc) && we.l().isSwapJuggling();
      we.l().onPlayerInventorySlot(var1.slot(), var1.contents());
      we var4 = we.Y();
      Minecraft var5 = Minecraft.getInstance();
      if (!var3 && !(var4 instanceof wc) && !var4.isSwapActive() && var5.player != null) {
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
   private void m(ClientboundContainerSetSlotPacket var1, CallbackInfo var2) {
      pw.S().z(var1.getContainerId(), var1.getSlot(), var1.getItem());
      lh.I().i(var1.getContainerId(), var1.getSlot(), var1.getItem());
      if (var1 != null) {
         boolean var3 = !(we.l() instanceof wc) && we.l().isSwapJuggling();
         we.l().onServerSlot(var1.getContainerId(), var1.getSlot(), var1.getItem());
         mg.M(var1.getContainerId(), var1.getSlot());
         gj var4 = gj.c();
         var4.j(var1.getContainerId(), var1.getSlot());
         bs var5 = bs.R();
         if (var4.E() == gj.d.g && var4.r() == var1.getContainerId()) {
            int var6 = var1.getSlot();
            if (var6 >= 0 && var6 <= 44) {
               var5.E(var4.C(), var6, var1.getItem());
            }
         } else if (var1.getContainerId() == 0) {
            var5.p(var1.getSlot(), var1.getItem());
         }

         int var12 = var1.getContainerId();
         int var7 = var1.getSlot();
         we var8 = we.Y();
         if (!var3 && (var8 instanceof wc || !var8.isSwapJuggling())) {
            boolean var9 = var12 == 0 || var12 == var8.getSwapSyncId() && var7 >= (var8 instanceof wc ? 36 : 9);
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
   private void D(ItemStack var1, int var2, Operation<Void> var3) {
      if (we.Y().isDealBobAllowed(var1)) {
         var3.call(new Object[]{var1, var2});
      }
   }

   @Inject(method = "handleTakeItemEntity", at = @At("TAIL"))
   private void i(ClientboundTakeItemEntityPacket var1, CallbackInfo var2) {
      Minecraft var3 = Minecraft.getInstance();
      if (var1 != null && var3 != null && var3.player != null) {
         g5.a().D(var3, var1.getItemId(), var1.getPlayerId(), var1.getAmount());
      }
   }

   @Inject(method = "handleSoundEvent", at = @At("HEAD"), cancellable = true)
   private void x(ClientboundSoundPacket var1, CallbackInfo var2) {
      if (var1 != null) {
         Holder var3 = var1.getSound();
         if (var3 != null) {
            SoundEvent var4 = (SoundEvent)var3.value();
            if (var4 == SoundEvents.ITEM_FRAME_BREAK || var4 == SoundEvents.ITEM_FRAME_REMOVE_ITEM) {
               Minecraft var5 = Minecraft.getInstance();
               if (var5 != null && var5.level != null) {
                  bs var6 = bs.R();
                  if (var6.l && !"off".equals(var6.V())) {
                     BlockPos var7 = gj.c().q(gj.d.g);
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
