package com.larpclient.util.entity;

import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.LivingEntity.SwingDescription;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.PiercingWeapon;
import net.minecraft.world.item.component.SwingAnimation;
import org.jspecify.annotations.Nullable;

public final class SwingHelper {
   public static void swingAttack(LivingEntity var0, InteractionHand var1) {
      var0.swing(var1, var0.getItemInHand(var1).getAttackAnimation(), false);
   }

   public static void swingInteract(LivingEntity var0, InteractionHand var1) {
      var0.swing(var1, var0.getItemInHand(var1).getInteractAnimation(), false);
   }

   public static boolean isSwinging(LivingEntity var0) {
      return var0.isSwinging();
   }

   public static @Nullable InteractionHand getSwingHand(LivingEntity var0) {
      SwingDescription var1 = var0.getCurrentSwing();
      return var1 == null ? null : var1.hand();
   }

   public static int getSwingProgressMillis(LivingEntity var0) {
      return Math.round(var0.getSwingAnimation(0.0F) * 1000.0F);
   }

   public static boolean isSwingJustStarted(LivingEntity var0) {
      return var0.isSwinging() && var0.getSwingAnimation(0.0F) == 0.0F;
   }

   public static SwingAnimation getAttackAnimation(ItemStack var0) {
      return var0.getAttackAnimation();
   }

   public static void piercingAttack(MultiPlayerGameMode var0, ItemStack var1) {
      var0.piercingAttack(var1.getAttackAnimation(), (PiercingWeapon)var1.get(DataComponents.PIERCING_WEAPON));
   }

   private SwingHelper() {
   }
}
