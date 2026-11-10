package com.larp.obf.qa300483f54d804a3.q44c0827ad90befbb;

import com.larpclient.cc;
import com.larpclient.g5;
import com.larpclient.gf;
import com.larpclient.kl;
import com.larpclient.mg;
import com.larpclient.sg;
import com.larpclient.we;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemInHandRenderer.class)
public abstract class q7e2805b4ca7bcb2e implements kl {
   @Shadow
   private ItemStack mainHandItem;
   @Unique
   private final gf<ItemStack> n = new gf<>();

   @Override
   public void larpRig$captureHeldMap(we var1, ItemStack var2, int var3) {
      if (var1.C() && var1.isDealtHandLocked()) {
         this.n.O(var1, var1.getDealtHandSequence(), var3, var2.copy());
      }
   }

   @Override
   public void larpRig$releaseHeldMap(we var1, ItemStack var2) {
      if (!var1.C() || !var1.isDealtHandLocked() || !var1.isDealtHandSettled()) {
         this.n.j();
      }
   }

   @Inject(method = "submitHandsWithItems", at = @At("HEAD"))
   private void J(float var1, PoseStack var2, SubmitNodeCollector var3, LocalPlayer var4, int var5, CallbackInfo var6) {
      Minecraft var7 = Minecraft.getInstance();
      if (var7.player != null && var7.level != null) {
         we var8 = we.Y();
         if (var8.C()) {
            var8.getDealtHandVisual(var7.player.getMainHandItem());
            this.n.p(var8, var8.getDealtHandSequence(), var7.player.getInventory().getSelectedSlot(), var8.isDealtHandLocked());
            this.n.i(this.mainHandItem);
         }
      }
   }

   @ModifyVariable(method = "renderItem", at = @At("HEAD"), argsOnly = true)
   private ItemStack D(ItemStack var1) {
      return this.J(var1);
   }

   @ModifyVariable(method = "renderMap", at = @At("HEAD"), argsOnly = true)
   private ItemStack y(ItemStack var1) {
      return this.J(var1);
   }

   @Unique
   private ItemStack J(ItemStack var1) {
      if (var1 == this.mainHandItem && var1 != null && !var1.isEmpty()) {
         Minecraft var2 = Minecraft.getInstance();
         if (var2.player != null && var2.level != null) {
            we var3 = we.Y();
            ItemStack var4 = var2.player.getMainHandItem();
            ItemStack var5 = var1;
            if (var3.isDealtHandLocked()) {
               var5 = var3.getDealtHandVisual(var4);
            }

            int var6 = mg.j();
            var5 = var3.projectSlot(var6, var5);
            ItemStack var7 = var3.getInventorySlotProjection(var6);
            if (var7 != null) {
               var5 = var7;
            }

            sg var8 = sg.G();
            if (var8 != null) {
               var5 = var8.b(var5);
            }

            var5 = g5.a().y(var6, var5);
            return cc.z().u(var6, var5);
         } else {
            return var1;
         }
      } else {
         return var1;
      }
   }
}
