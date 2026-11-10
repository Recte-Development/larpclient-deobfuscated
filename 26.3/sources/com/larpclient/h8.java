package com.larpclient;

import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.LivingEntity.SwingDescription;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.PiercingWeapon;
import net.minecraft.world.item.component.SwingAnimation;
import org.jspecify.annotations.Nullable;

public final class h8 {
   public static void Q(LivingEntity var0, InteractionHand var1) {
      var0.swing(var1, var0.getItemInHand(var1).getAttackAnimation(), false);
   }

   public static void i(LivingEntity var0, InteractionHand var1) {
      var0.swing(var1, var0.getItemInHand(var1).getInteractAnimation(), false);
   }

   public static boolean S(LivingEntity var0) {
      return var0.isSwinging();
   }

   public static @Nullable InteractionHand J(LivingEntity var0) {
      SwingDescription var1 = var0.getCurrentSwing();
      return var1 == null ? null : var1.hand();
   }

   public static int x(LivingEntity var0) {
      return Math.round(var0.getSwingAnimation(0.0F) * 1000.0F);
   }

   public static boolean C(LivingEntity var0) {
      return var0.isSwinging() && var0.getSwingAnimation(0.0F) == 0.0F;
   }

   public static SwingAnimation d(ItemStack var0) {
      return var0.getAttackAnimation();
   }

   public static void X(MultiPlayerGameMode var0, ItemStack var1) {
      var0.piercingAttack(var1.getAttackAnimation(), (PiercingWeapon)var1.get(DataComponents.PIERCING_WEAPON));
   }

   private h8() {
   }
}
