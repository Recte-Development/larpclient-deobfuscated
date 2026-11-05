package com.larp.obf.qbac0b262827a15ac.q03e2e72dbe55651a;

import com.larpclient.c6;
import com.larpclient.ep;
import com.larpclient.kq;
import com.larpclient.mi;
import com.larpclient.w5;
import com.larpclient.z;
import com.larpclient.z8;
import com.larpclient.ze;
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
public abstract class q55d770e129a4bb21 {
   private static final double j = 256.0;

   @Inject(method = "handleLevelEvent", at = @At("TAIL"))
   private void X(ClientboundLevelEventPacket var1, CallbackInfo var2) {
      if (var1.getType() == 1000) {
         kq.I().r(var1.getPos());
      }
   }

   @Inject(method = "handleBlockUpdate", at = @At("TAIL"))
   private void Y(ClientboundBlockUpdatePacket var1, CallbackInfo var2) {
      c6.K().i(var1.getPos(), var1.getBlockState());
      kq.I().I(var1.getPos(), var1.getBlockState());
      ep.H().u(Minecraft.getInstance(), var1.getPos(), var1.getBlockState());
   }

   @Inject(method = "handleChunkBlocksUpdate", at = @At("TAIL"))
   private void C(ClientboundSectionBlocksUpdatePacket var1, CallbackInfo var2) {
      Minecraft var3 = Minecraft.getInstance();
      ep var4 = ep.H();
      var1.runUpdates((var2x, var3x) -> {
         c6.K().i(var2x, var3x);
         kq.I().I(var2x, var3x);
         var4.u(var3, var2x, var3x);
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
   private void s(ClientboundContainerClosePacket var1, CallbackInfo var2) {
      ze.n().X(var1.getContainerId());
   }

   @Inject(method = "handleContainerContent", at = @At("TAIL"))
   private void D(ClientboundContainerSetContentPacket var1, CallbackInfo var2) {
      if (var1 != null) {
         ze.n().onInventorySynchronized(var1.containerId(), var1.items(), var1.carriedItem());
         ep.H().U(var1.containerId());
         w5.j(var1.containerId());
         c6.K().Y(var1.containerId(), var1.items(), var1.carriedItem());
         kq.I().m(var1.containerId(), var1.items(), var1.carriedItem());
      }
   }

   @Inject(method = "handleSetCursorItem", at = @At("TAIL"))
   private void O(ClientboundSetCursorItemPacket var1, CallbackInfo var2) {
      ze.n().onServerSlot(-1, -1, var1.contents());
   }

   @Inject(method = "handleSetPlayerInventory", at = @At("TAIL"))
   private void b(ClientboundSetPlayerInventoryPacket var1, CallbackInfo var2) {
      c6.K().N(var1.slot(), var1.contents());
      kq.I().H(var1.slot(), var1.contents());
      boolean var3 = !(ze.n() instanceof z8) && ze.n().isSwapJuggling();
      ze.n().onPlayerInventorySlot(var1.slot(), var1.contents());
      ze var4 = ze.c();
      Minecraft var5 = Minecraft.getInstance();
      if (!var3 && !(var4 instanceof z8) && !var4.isSwapActive() && var5.player != null) {
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
   private void u(ClientboundContainerSetSlotPacket var1, CallbackInfo var2) {
      c6.K().R(var1.getContainerId(), var1.getSlot(), var1.getItem());
      kq.I().t(var1.getContainerId(), var1.getSlot(), var1.getItem());
      if (var1 != null) {
         boolean var3 = !(ze.n() instanceof z8) && ze.n().isSwapJuggling();
         ze.n().onServerSlot(var1.getContainerId(), var1.getSlot(), var1.getItem());
         w5.i(var1.getContainerId(), var1.getSlot());
         ep var4 = ep.H();
         var4.k(var1.getContainerId(), var1.getSlot());
         z var5 = z.v();
         if (var4.U() == ep.o.x && var4.V() == var1.getContainerId()) {
            int var6 = var1.getSlot();
            if (var6 >= 0 && var6 <= 44) {
               var5.n(var4.b(), var6, var1.getItem());
            }
         } else if (var1.getContainerId() == 0) {
            var5.S(var1.getSlot(), var1.getItem());
         }

         int var12 = var1.getContainerId();
         int var7 = var1.getSlot();
         ze var8 = ze.c();
         if (!var3 && (var8 instanceof z8 || !var8.isSwapJuggling())) {
            boolean var9 = var12 == 0 || var12 == var8.getSwapSyncId() && var7 >= (var8 instanceof z8 ? 36 : 9);
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
   private void Q(ItemStack var1, int var2, Operation<Void> var3) {
      if (ze.c().isDealBobAllowed(var1)) {
         var3.call(new Object[]{var1, var2});
      }
   }

   @Inject(method = "handleTakeItemEntity", at = @At("TAIL"))
   private void W(ClientboundTakeItemEntityPacket var1, CallbackInfo var2) {
      Minecraft var3 = Minecraft.getInstance();
      if (var1 != null && var3 != null && var3.player != null) {
         mi.L().I(var3, var1.getItemId(), var1.getPlayerId(), var1.getAmount());
      }
   }

   @Inject(method = "handleSoundEvent", at = @At("HEAD"), cancellable = true)
   private void X(ClientboundSoundPacket var1, CallbackInfo var2) {
      if (var1 != null) {
         Holder var3 = var1.getSound();
         if (var3 != null) {
            SoundEvent var4 = (SoundEvent)var3.value();
            if (var4 == SoundEvents.ITEM_FRAME_BREAK || var4 == SoundEvents.ITEM_FRAME_REMOVE_ITEM) {
               Minecraft var5 = Minecraft.getInstance();
               if (var5 != null && var5.level != null) {
                  z var6 = z.v();
                  if (var6.d && !"off".equals(var6.B())) {
                     BlockPos var7 = ep.H().n(ep.o.x);
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
