package com.larpclient;

import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.PiercingWeapon;
import net.minecraft.world.item.component.SwingAnimation;
import org.jspecify.annotations.Nullable;

public final class ks {
   public static void X(LivingEntity var0, InteractionHand var1) {
      var0.swing(var1);
   }

   public static void p(LivingEntity var0, InteractionHand var1) {
      var0.swing(var1);
   }

   public static boolean M(LivingEntity var0) {
      return var0.swinging;
   }

   public static @Nullable InteractionHand t(LivingEntity var0) {
      return var0.swinging ? var0.swingingArm : null;
   }

   public static int X(LivingEntity var0) {
      return var0.swingTime;
   }

   public static boolean h(LivingEntity var0) {
      return var0.swinging && var0.swingTime < 0;
   }

   public static SwingAnimation P(ItemStack var0) {
      return var0.getSwingAnimation();
   }

   public static void L(MultiPlayerGameMode var0, ItemStack var1) {
      var0.piercingAttack((PiercingWeapon)var1.get(DataComponents.PIERCING_WEAPON));
   }

   private ks() {
   }
}
