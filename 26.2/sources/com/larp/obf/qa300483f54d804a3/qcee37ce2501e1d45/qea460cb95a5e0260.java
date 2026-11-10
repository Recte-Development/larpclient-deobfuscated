package com.larp.obf.qa300483f54d804a3.qcee37ce2501e1d45;

import com.larpclient.b8;
import com.larpclient.bo;
import com.larpclient.gz;
import com.larpclient.kb;
import com.larpclient.m3;
import com.larpclient.mm;
import com.larpclient.sc;
import com.larpclient.za;
import com.larpclient.zs;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.protocol.game.ClientboundDisguisedChatPacket;
import net.minecraft.network.protocol.game.ClientboundOpenScreenPacket;
import net.minecraft.network.protocol.game.ClientboundSetActionBarTextPacket;
import net.minecraft.network.protocol.game.ClientboundSoundEntityPacket;
import net.minecraft.network.protocol.game.ClientboundSoundPacket;
import net.minecraft.network.protocol.game.ClientboundSystemChatPacket;
import net.minecraft.network.protocol.game.ClientboundTakeItemEntityPacket;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPacketListener.class)
public abstract class qea460cb95a5e0260 {
   @Unique
   private final Map<Integer, ItemStack> R = new HashMap<>();

   @WrapOperation(
      method = "handleTakeItemEntity",
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/multiplayer/ClientLevel;playLocalSound(DDDLnet/minecraft/sounds/SoundEvent;Lnet/minecraft/sounds/SoundSource;FFZ)V"
      )
   )
   private void a(
      ClientLevel var1,
      double var2,
      double var4,
      double var6,
      SoundEvent var8,
      SoundSource var9,
      float var10,
      float var11,
      boolean var12,
      Operation<Void> var13,
      ClientboundTakeItemEntityPacket var14
   ) {
      LocalPlayer var15 = Minecraft.getInstance().player;
      boolean var16 = var15 != null && var14.getPlayerId() == var15.getId() && var1.getEntity(var14.getItemId()) instanceof ItemEntity var17 && b8.k().o(var17);
      if (!var16) {
         var13.call(new Object[]{var1, var2, var4, var6, var8, var9, var10, var11, var12});
      }
   }

   @Inject(method = "handleTakeItemEntity", at = @At("HEAD"))
   private void n(ClientboundTakeItemEntityPacket var1, CallbackInfo var2) {
      Minecraft var3 = Minecraft.getInstance();
      if (var3.isSameThread() && var3.player != null && var3.level != null && var1.getPlayerId() == var3.player.getId()) {
         if (var3.level.getEntity(var1.getItemId()) instanceof ItemEntity var4) {
            bo.b(var4);
            if (gz.W(var4.getItem())) {
               sc.H(var4.getItem(), var1.getAmount());
            }
         }
      }
   }

   @Inject(method = {"handleContainerContent", "handleContainerSetSlot", "handleSetPlayerInventory", "handleSetCursorItem"}, at = @At("HEAD"))
   private void X(CallbackInfo var1) {
      Minecraft var2 = Minecraft.getInstance();
      if (var2.isSameThread()) {
         this.R.clear();
         if (var2.player != null) {
            for (int var3 = 0; var3 < var2.player.getInventory().getContainerSize(); var3++) {
               ItemStack var4 = var2.player.getInventory().getItem(var3);
               if (zs.t(var4)) {
                  this.R.put(var3, var4.copy());
               }
            }

            ItemStack var5 = var2.player.containerMenu.getCarried();
            if (zs.t(var5)) {
               this.R.put(-1, var5.copy());
            }
         }
      }
   }

   @Unique
   private void n() {
      Minecraft var1 = Minecraft.getInstance();
      if (var1.isSameThread() && var1.player != null) {
         for (Entry var3 : this.R.entrySet()) {
            ItemStack var4 = var3.getKey() == -1 ? var1.player.containerMenu.getCarried() : var1.player.getInventory().getItem((Integer)var3.getKey());
            if (ItemStack.isSameItemSameComponents(var4, (ItemStack)var3.getValue()) && var4.getCount() <= ((ItemStack)var3.getValue()).getCount()) {
               zs.z(var4, true);
            }
         }

         this.R.clear();
      }
   }

   @Inject(method = {"handleContainerContent", "handleContainerSetSlot", "handleSetPlayerInventory", "handleSetCursorItem"}, at = @At("RETURN"))
   private void Z(CallbackInfo var1) {
      this.n();
      b8.k().J();
      m3.m();
      b8.k().P();
      mm.o();
      sc.r();
   }

   @Inject(method = "handleSystemChat", at = @At("HEAD"), cancellable = true)
   private void y(ClientboundSystemChatPacket var1, CallbackInfo var2) {
      if (b8.k().J()) {
         kb.h().r(var1.content());
      }

      if (mm.A(var1.content(), var1.overlay()) || za.O(var1.content(), var1.overlay())) {
         var2.cancel();
      }
   }

   @Inject(method = "handleDisguisedChat", at = @At("HEAD"), cancellable = true)
   private void v(ClientboundDisguisedChatPacket var1, CallbackInfo var2) {
      if (b8.k().J()) {
         kb.h().r(var1.message());
      }

      if (mm.A(var1.message(), false) || za.O(var1.message(), false)) {
         var2.cancel();
      }
   }

   @Inject(method = "setActionBarText", at = @At("HEAD"), cancellable = true)
   private void p(ClientboundSetActionBarTextPacket var1, CallbackInfo var2) {
      if (mm.A(var1.text(), true) || za.O(var1.text(), true)) {
         var2.cancel();
      }
   }

   @Inject(method = "handleOpenScreen", at = @At("HEAD"), cancellable = true)
   private void Y(ClientboundOpenScreenPacket var1, CallbackInfo var2) {
      if (mm.f(var1)) {
         var2.cancel();
      }
   }

   @Inject(method = "handleSoundEvent", at = @At("HEAD"), cancellable = true)
   private void G(ClientboundSoundPacket var1, CallbackInfo var2) {
      LocalPlayer var3 = Minecraft.getInstance().player;
      if (mm.G() && var3 != null && var3.distanceToSqr(var1.getX(), var1.getY(), var1.getZ()) <= 16.0) {
         var2.cancel();
      }
   }

   @Inject(method = "handleSoundEntityEvent", at = @At("HEAD"), cancellable = true)
   private void E(ClientboundSoundEntityPacket var1, CallbackInfo var2) {
      LocalPlayer var3 = Minecraft.getInstance().player;
      if (mm.G() && var3 != null && var1.getId() == var3.getId()) {
         var2.cancel();
      }
   }
}
