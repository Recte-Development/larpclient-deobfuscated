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

public final class hp {
   public static void o(LivingEntity var0, InteractionHand var1) {
      var0.swing(var1, var0.getItemInHand(var1).getAttackAnimation(), false);
   }

   public static void Q(LivingEntity var0, InteractionHand var1) {
      var0.swing(var1, var0.getItemInHand(var1).getInteractAnimation(), false);
   }

   public static boolean o(LivingEntity var0) {
      return var0.isSwinging();
   }

   public static @Nullable InteractionHand k(LivingEntity var0) {
      SwingDescription var1 = var0.getCurrentSwing();
      return var1 == null ? null : var1.hand();
   }

   public static boolean a(LivingEntity var0) {
      return var0.isSwinging() && var0.getSwingAnimation(0.0F) == 0.0F;
   }

   public static SwingAnimation m(ItemStack var0) {
      return var0.getAttackAnimation();
   }

   public static void Z(MultiPlayerGameMode var0, ItemStack var1) {
      var0.piercingAttack(var1.getAttackAnimation(), (PiercingWeapon)var1.get(DataComponents.PIERCING_WEAPON));
   }

   private hp() {
   }
}
