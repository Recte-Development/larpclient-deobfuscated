package com.larp.obf.qbac0b262827a15ac.qb9781bf98a2db63b;

import com.larpclient.cy;
import com.larpclient.dr;
import com.larpclient.h2;
import com.larpclient.k9;
import com.larpclient.op;
import com.larpclient.wf;
import com.larpclient.wt;
import com.larpclient.x7;
import com.larpclient.z_;
import com.larpclient.module.StaffListModule;
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
public abstract class qe1516fb8ceeb50d8 {
   @Unique
   private final Map<Integer, ItemStack> A = new HashMap<>();

   @WrapOperation(
      method = "handleTakeItemEntity",
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/multiplayer/ClientLevel;playLocalSound(DDDLnet/minecraft/sounds/SoundEvent;Lnet/minecraft/sounds/SoundSource;FFZ)V"
      )
   )
   private void P(
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
      boolean var16 = var15 != null && var14.getPlayerId() == var15.getId() && var1.getEntity(var14.getItemId()) instanceof ItemEntity var17 && dr.z().L(var17);
      if (!var16) {
         var13.call(new Object[]{var1, var2, var4, var6, var8, var9, var10, var11, var12});
      }
   }

   @Inject(method = "handleTakeItemEntity", at = @At("HEAD"))
   private void e(ClientboundTakeItemEntityPacket var1, CallbackInfo var2) {
      Minecraft var3 = Minecraft.getInstance();
      if (var3.isSameThread() && var3.player != null && var3.level != null && var1.getPlayerId() == var3.player.getId()) {
         if (var3.level.getEntity(var1.getItemId()) instanceof ItemEntity var4) {
            x7.P(var4);
            if (h2.M(var4.getItem())) {
               wf.I(var4.getItem(), var1.getAmount());
            }
         }
      }
   }

   @Inject(method = {"handleContainerContent", "handleContainerSetSlot", "handleSetPlayerInventory", "handleSetCursorItem"}, at = @At("HEAD"))
   private void v(CallbackInfo var1) {
      Minecraft var2 = Minecraft.getInstance();
      if (var2.isSameThread()) {
         this.A.clear();
         if (var2.player != null) {
            for (int var3 = 0; var3 < var2.player.getInventory().getContainerSize(); var3++) {
               ItemStack var4 = var2.player.getInventory().getItem(var3);
               if (cy.t(var4)) {
                  this.A.put(var3, var4.copy());
               }
            }

            ItemStack var5 = var2.player.containerMenu.getCarried();
            if (cy.t(var5)) {
               this.A.put(-1, var5.copy());
            }
         }
      }
   }

   @Unique
   private void I() {
      Minecraft var1 = Minecraft.getInstance();
      if (var1.isSameThread() && var1.player != null) {
         for (Entry var3 : this.A.entrySet()) {
            ItemStack var4 = var3.getKey() == -1 ? var1.player.containerMenu.getCarried() : var1.player.getInventory().getItem((Integer)var3.getKey());
            if (ItemStack.isSameItemSameComponents(var4, (ItemStack)var3.getValue()) && var4.getCount() <= ((ItemStack)var3.getValue()).getCount()) {
               cy.i(var4, true);
            }
         }

         this.A.clear();
      }
   }

   @Inject(method = {"handleContainerContent", "handleContainerSetSlot", "handleSetPlayerInventory", "handleSetCursorItem"}, at = @At("RETURN"))
   private void H(CallbackInfo var1) {
      this.I();
      dr.z().L();
      wt.g();
      dr.z().e();
      z_.a();
      wf.g();
   }

   @Inject(method = "handleSystemChat", at = @At("HEAD"), cancellable = true)
   private void n(ClientboundSystemChatPacket var1, CallbackInfo var2) {
      if (dr.z().H() || StaffListModule.e()) {
         op.a().Z(var1.content());
      }

      if (z_.F(var1.content(), var1.overlay()) || k9.m(var1.content(), var1.overlay())) {
         var2.cancel();
      }
   }

   @Inject(method = "handleDisguisedChat", at = @At("HEAD"), cancellable = true)
   private void G(ClientboundDisguisedChatPacket var1, CallbackInfo var2) {
      if (dr.z().H() || StaffListModule.e()) {
         op.a().Z(var1.message());
      }

      if (z_.F(var1.message(), false) || k9.m(var1.message(), false)) {
         var2.cancel();
      }
   }

   @Inject(method = "setActionBarText", at = @At("HEAD"), cancellable = true)
   private void C(ClientboundSetActionBarTextPacket var1, CallbackInfo var2) {
      if (z_.F(var1.text(), true) || k9.m(var1.text(), true)) {
         var2.cancel();
      }
   }

   @Inject(method = "handleOpenScreen", at = @At("HEAD"), cancellable = true)
   private void S(ClientboundOpenScreenPacket var1, CallbackInfo var2) {
      if (z_.Z(var1)) {
         var2.cancel();
      }
   }

   @Inject(method = "handleSoundEvent", at = @At("HEAD"), cancellable = true)
   private void x(ClientboundSoundPacket var1, CallbackInfo var2) {
      LocalPlayer var3 = Minecraft.getInstance().player;
      if (z_.K() && var3 != null && var3.distanceToSqr(var1.getX(), var1.getY(), var1.getZ()) <= 16.0) {
         var2.cancel();
      }
   }

   @Inject(method = "handleSoundEntityEvent", at = @At("HEAD"), cancellable = true)
   private void B(ClientboundSoundEntityPacket var1, CallbackInfo var2) {
      LocalPlayer var3 = Minecraft.getInstance().player;
      if (z_.K() && var3 != null && var1.getId() == var3.getId()) {
         var2.cancel();
      }
   }
}
