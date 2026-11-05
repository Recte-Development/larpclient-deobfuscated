package com.larp.obf.q5682c6a8aca9b6ec.q42bdff5f872a0552;

import com.larpclient.f2;
import com.larpclient.f9;
import com.larpclient.mm;
import com.larpclient.oi;
import com.larpclient.ro;
import com.larpclient.ug;
import com.larpclient.um;
import com.larpclient.yg;
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
public abstract class qfa6eb8746071b66c {
   private static final double g = 256.0;

   @Inject(method = "handleLevelEvent", at = @At("TAIL"))
   private void y(ClientboundLevelEventPacket var1, CallbackInfo var2) {
      if (var1.getType() == 1000) {
         mm.W().x(var1.getPos());
      }
   }

   @Inject(method = "handleBlockUpdate", at = @At("TAIL"))
   private void J(ClientboundBlockUpdatePacket var1, CallbackInfo var2) {
      ro.H().j(var1.getPos(), var1.getBlockState());
      mm.W().q(var1.getPos(), var1.getBlockState());
      f9.W().b(Minecraft.getInstance(), var1.getPos(), var1.getBlockState());
   }

   @Inject(method = "handleChunkBlocksUpdate", at = @At("TAIL"))
   private void g(ClientboundSectionBlocksUpdatePacket var1, CallbackInfo var2) {
      Minecraft var3 = Minecraft.getInstance();
      f9 var4 = f9.W();
      var1.runUpdates((var2x, var3x) -> {
         ro.H().j(var2x, var3x);
         mm.W().q(var2x, var3x);
         var4.b(var3, var2x, var3x);
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
   private void Y(ClientboundContainerClosePacket var1, CallbackInfo var2) {
      ug.h().G(var1.getContainerId());
   }

   @Inject(method = "handleContainerContent", at = @At("TAIL"))
   private void Y(ClientboundContainerSetContentPacket var1, CallbackInfo var2) {
      if (var1 != null) {
         ug.h().onInventorySynchronized(var1.containerId(), var1.items(), var1.carriedItem());
         f9.W().x(var1.containerId());
         yg.n(var1.containerId());
         ro.H().T(var1.containerId(), var1.items(), var1.carriedItem());
         mm.W().h(var1.containerId(), var1.items(), var1.carriedItem());
      }
   }

   @Inject(method = "handleSetCursorItem", at = @At("TAIL"))
   private void p(ClientboundSetCursorItemPacket var1, CallbackInfo var2) {
      ug.h().onServerSlot(-1, -1, var1.contents());
   }

   @Inject(method = "handleSetPlayerInventory", at = @At("TAIL"))
   private void s(ClientboundSetPlayerInventoryPacket var1, CallbackInfo var2) {
      ro.H().e(var1.slot(), var1.contents());
      mm.W().u(var1.slot(), var1.contents());
      boolean var3 = !(ug.h() instanceof um) && ug.h().isSwapJuggling();
      ug.h().onPlayerInventorySlot(var1.slot(), var1.contents());
      ug var4 = ug.x();
      Minecraft var5 = Minecraft.getInstance();
      if (!var3 && !(var4 instanceof um) && !var4.isSwapActive() && var5.player != null) {
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
   private void C(ClientboundContainerSetSlotPacket var1, CallbackInfo var2) {
      ro.H().O(var1.getContainerId(), var1.getSlot(), var1.getItem());
      mm.W().v(var1.getContainerId(), var1.getSlot(), var1.getItem());
      if (var1 != null) {
         boolean var3 = !(ug.h() instanceof um) && ug.h().isSwapJuggling();
         ug.h().onServerSlot(var1.getContainerId(), var1.getSlot(), var1.getItem());
         yg.T(var1.getContainerId(), var1.getSlot());
         f9 var4 = f9.W();
         var4.z(var1.getContainerId(), var1.getSlot());
         f2 var5 = f2.f();
         if (var4.o() == f9.b.F && var4.i() == var1.getContainerId()) {
            int var6 = var1.getSlot();
            if (var6 >= 0 && var6 <= 44) {
               var5.C(var4.v(), var6, var1.getItem());
            }
         } else if (var1.getContainerId() == 0) {
            var5.d(var1.getSlot(), var1.getItem());
         }

         int var12 = var1.getContainerId();
         int var7 = var1.getSlot();
         ug var8 = ug.x();
         if (!var3 && (var8 instanceof um || !var8.isSwapJuggling())) {
            boolean var9 = var12 == 0 || var12 == var8.getSwapSyncId() && var7 >= (var8 instanceof um ? 36 : 9);
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
   private void V(ItemStack var1, int var2, Operation<Void> var3) {
      if (ug.x().isDealBobAllowed(var1)) {
         var3.call(new Object[]{var1, var2});
      }
   }

   @Inject(method = "handleTakeItemEntity", at = @At("TAIL"))
   private void L(ClientboundTakeItemEntityPacket var1, CallbackInfo var2) {
      Minecraft var3 = Minecraft.getInstance();
      if (var1 != null && var3 != null && var3.player != null) {
         oi.A().W(var3, var1.getItemId(), var1.getPlayerId(), var1.getAmount());
      }
   }

   @Inject(method = "handleSoundEvent", at = @At("HEAD"), cancellable = true)
   private void l(ClientboundSoundPacket var1, CallbackInfo var2) {
      if (var1 != null) {
         Holder var3 = var1.getSound();
         if (var3 != null) {
            SoundEvent var4 = (SoundEvent)var3.value();
            if (var4 == SoundEvents.ITEM_FRAME_BREAK || var4 == SoundEvents.ITEM_FRAME_REMOVE_ITEM) {
               Minecraft var5 = Minecraft.getInstance();
               if (var5 != null && var5.level != null) {
                  f2 var6 = f2.f();
                  if (var6.p && !"off".equals(var6.v())) {
                     BlockPos var7 = f9.W().R(f9.b.F);
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
