package com.larp.obf.q41a5851bcf8e876a.q2d3f5242fd3d8828;

import com.larpclient.hk;
import com.larpclient.ig;
import com.larpclient.q5;
import com.larpclient.qm;
import com.larpclient.qo;
import com.larpclient.vh;
import com.larpclient.vy;
import com.larpclient.wj;
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
public abstract class q96a305afe86d517f {
   private static final double E = 256.0;

   @Inject(method = "handleLevelEvent", at = @At("TAIL"))
   private void C(ClientboundLevelEventPacket var1, CallbackInfo var2) {
      if (var1.getType() == 1000) {
         q5.a().M(var1.getPos());
      }
   }

   @Inject(method = "handleBlockUpdate", at = @At("TAIL"))
   private void E(ClientboundBlockUpdatePacket var1, CallbackInfo var2) {
      wj.m().r(var1.getPos(), var1.getBlockState());
      q5.a().f(var1.getPos(), var1.getBlockState());
      hk.e().f(Minecraft.getInstance(), var1.getPos(), var1.getBlockState());
   }

   @Inject(method = "handleChunkBlocksUpdate", at = @At("TAIL"))
   private void p(ClientboundSectionBlocksUpdatePacket var1, CallbackInfo var2) {
      Minecraft var3 = Minecraft.getInstance();
      hk var4 = hk.e();
      var1.runUpdates((var2x, var3x) -> {
         wj.m().r(var2x, var3x);
         q5.a().f(var2x, var3x);
         var4.f(var3, var2x, var3x);
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
      vy.x().U(var1.getContainerId());
   }

   @Inject(method = "handleContainerContent", at = @At("TAIL"))
   private void Y(ClientboundContainerSetContentPacket var1, CallbackInfo var2) {
      if (var1 != null) {
         vy.x().onInventorySynchronized(var1.containerId(), var1.items(), var1.carriedItem());
         hk.e().h(var1.containerId());
         qm.M(var1.containerId());
         wj.m().n(var1.containerId(), var1.items(), var1.carriedItem());
         q5.a().g(var1.containerId(), var1.items(), var1.carriedItem());
      }
   }

   @Inject(method = "handleSetCursorItem", at = @At("TAIL"))
   private void S(ClientboundSetCursorItemPacket var1, CallbackInfo var2) {
      vy.x().onServerSlot(-1, -1, var1.contents());
   }

   @Inject(method = "handleSetPlayerInventory", at = @At("TAIL"))
   private void N(ClientboundSetPlayerInventoryPacket var1, CallbackInfo var2) {
      wj.m().x(var1.slot(), var1.contents());
      q5.a().d(var1.slot(), var1.contents());
      boolean var3 = !(vy.x() instanceof vh) && vy.x().isSwapJuggling();
      vy.x().onPlayerInventorySlot(var1.slot(), var1.contents());
      vy var4 = vy.a();
      Minecraft var5 = Minecraft.getInstance();
      if (!var3 && !(var4 instanceof vh) && !var4.isSwapActive() && var5.player != null) {
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
      wj.m().D(var1.getContainerId(), var1.getSlot(), var1.getItem());
      q5.a().u(var1.getContainerId(), var1.getSlot(), var1.getItem());
      if (var1 != null) {
         boolean var3 = !(vy.x() instanceof vh) && vy.x().isSwapJuggling();
         vy.x().onServerSlot(var1.getContainerId(), var1.getSlot(), var1.getItem());
         qm.J(var1.getContainerId(), var1.getSlot());
         hk var4 = hk.e();
         var4.o(var1.getContainerId(), var1.getSlot());
         ig var5 = ig.r();
         if (var4.l() == hk.k.N && var4.v() == var1.getContainerId()) {
            int var6 = var1.getSlot();
            if (var6 >= 0 && var6 <= 44) {
               var5.D(var4.v(), var6, var1.getItem());
            }
         } else if (var1.getContainerId() == 0) {
            var5.l(var1.getSlot(), var1.getItem());
         }

         int var12 = var1.getContainerId();
         int var7 = var1.getSlot();
         vy var8 = vy.a();
         if (!var3 && (var8 instanceof vh || !var8.isSwapJuggling())) {
            boolean var9 = var12 == 0 || var12 == var8.getSwapSyncId() && var7 >= (var8 instanceof vh ? 36 : 9);
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
      if (vy.a().isDealBobAllowed(var1)) {
         var3.call(new Object[]{var1, var2});
      }
   }

   @Inject(method = "handleTakeItemEntity", at = @At("TAIL"))
   private void l(ClientboundTakeItemEntityPacket var1, CallbackInfo var2) {
      Minecraft var3 = Minecraft.getInstance();
      if (var1 != null && var3 != null && var3.player != null) {
         qo.Z().H(var3, var1.getItemId(), var1.getPlayerId(), var1.getAmount());
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
                  ig var6 = ig.r();
                  if (var6.I && !"off".equals(var6.Q())) {
                     BlockPos var7 = hk.e().f(hk.k.N);
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
