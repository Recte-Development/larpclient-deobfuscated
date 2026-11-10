package com.larp.obf.q41a5851bcf8e876a.q1342f79d4278119d;

import com.larpclient._v;
import com.larpclient.f9;
import com.larpclient.h0;
import com.larpclient.h4;
import com.larpclient.jn;
import com.larpclient.nn;
import com.larpclient.ox;
import com.larpclient.q7;
import com.larpclient.qb;
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
public abstract class q265a308be6889715 {
   @Unique
   private final Map<Integer, ItemStack> o = new HashMap<>();

   @WrapOperation(
      method = "handleTakeItemEntity",
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/multiplayer/ClientLevel;playLocalSound(DDDLnet/minecraft/sounds/SoundEvent;Lnet/minecraft/sounds/SoundSource;FFZ)V"
      )
   )
   private void v(
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
      boolean var16 = var15 != null && var14.getPlayerId() == var15.getId() && var1.getEntity(var14.getItemId()) instanceof ItemEntity var17 && jn.g().c(var17);
      if (!var16) {
         var13.call(new Object[]{var1, var2, var4, var6, var8, var9, var10, var11, var12});
      }
   }

   @Inject(method = "handleTakeItemEntity", at = @At("HEAD"))
   private void Q(ClientboundTakeItemEntityPacket var1, CallbackInfo var2) {
      Minecraft var3 = Minecraft.getInstance();
      if (var3.isSameThread() && var3.player != null && var3.level != null && var1.getPlayerId() == var3.player.getId()) {
         if (var3.level.getEntity(var1.getItemId()) instanceof ItemEntity var4) {
            qb.a(var4);
            if (nn.i(var4.getItem())) {
               f9.Y(var4.getItem(), var1.getAmount());
            }
         }
      }
   }

   @Inject(method = {"handleContainerContent", "handleContainerSetSlot", "handleSetPlayerInventory", "handleSetCursorItem"}, at = @At("HEAD"))
   private void s(CallbackInfo var1) {
      Minecraft var2 = Minecraft.getInstance();
      if (var2.isSameThread()) {
         this.o.clear();
         if (var2.player != null) {
            for (int var3 = 0; var3 < var2.player.getInventory().getContainerSize(); var3++) {
               ItemStack var4 = var2.player.getInventory().getItem(var3);
               if (ox.F(var4)) {
                  this.o.put(var3, var4.copy());
               }
            }

            ItemStack var5 = var2.player.containerMenu.getCarried();
            if (ox.F(var5)) {
               this.o.put(-1, var5.copy());
            }
         }
      }
   }

   @Unique
   private void S() {
      Minecraft var1 = Minecraft.getInstance();
      if (var1.isSameThread() && var1.player != null) {
         for (Entry var3 : this.o.entrySet()) {
            ItemStack var4 = var3.getKey() == -1 ? var1.player.containerMenu.getCarried() : var1.player.getInventory().getItem((Integer)var3.getKey());
            if (ItemStack.isSameItemSameComponents(var4, (ItemStack)var3.getValue()) && var4.getCount() <= ((ItemStack)var3.getValue()).getCount()) {
               ox.x(var4, true);
            }
         }

         this.o.clear();
      }
   }

   @Inject(method = {"handleContainerContent", "handleContainerSetSlot", "handleSetPlayerInventory", "handleSetCursorItem"}, at = @At("RETURN"))
   private void Y(CallbackInfo var1) {
      this.S();
      jn.g().R();
      q7.d();
      jn.g().f();
      _v.g();
      f9.Z();
   }

   @Inject(method = "handleSystemChat", at = @At("HEAD"), cancellable = true)
   private void T(ClientboundSystemChatPacket var1, CallbackInfo var2) {
      if (jn.g().a()) {
         h0.u().X(var1.content());
      }

      if (_v.i(var1.content(), var1.overlay()) || h4.M(var1.content(), var1.overlay())) {
         var2.cancel();
      }
   }

   @Inject(method = "handleDisguisedChat", at = @At("HEAD"), cancellable = true)
   private void x(ClientboundDisguisedChatPacket var1, CallbackInfo var2) {
      if (jn.g().a()) {
         h0.u().X(var1.message());
      }

      if (_v.i(var1.message(), false) || h4.M(var1.message(), false)) {
         var2.cancel();
      }
   }

   @Inject(method = "setActionBarText", at = @At("HEAD"), cancellable = true)
   private void h(ClientboundSetActionBarTextPacket var1, CallbackInfo var2) {
      if (_v.i(var1.text(), true) || h4.M(var1.text(), true)) {
         var2.cancel();
      }
   }

   @Inject(method = "handleOpenScreen", at = @At("HEAD"), cancellable = true)
   private void a(ClientboundOpenScreenPacket var1, CallbackInfo var2) {
      if (_v.P(var1)) {
         var2.cancel();
      }
   }

   @Inject(method = "handleSoundEvent", at = @At("HEAD"), cancellable = true)
   private void z(ClientboundSoundPacket var1, CallbackInfo var2) {
      LocalPlayer var3 = Minecraft.getInstance().player;
      if (_v.k() && var3 != null && var3.distanceToSqr(var1.getX(), var1.getY(), var1.getZ()) <= 16.0) {
         var2.cancel();
      }
   }

   @Inject(method = "handleSoundEntityEvent", at = @At("HEAD"), cancellable = true)
   private void C(ClientboundSoundEntityPacket var1, CallbackInfo var2) {
      LocalPlayer var3 = Minecraft.getInstance().player;
      if (_v.k() && var3 != null && var1.getId() == var3.getId()) {
         var2.cancel();
      }
   }
}
