package com.larpclient.module;

import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Entity.RemovalReason;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.boss.enderdragon.EndCrystal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;

public final class CrystalOptimizerModule extends Module {
   private static CrystalOptimizerModule D;
   private String v = "Ready";
   private boolean W;

   public CrystalOptimizerModule() {
      super("Crystal Optimizer", "Crystals you break disappear at once client-side for faster placement", Category.COMBAT, -1);
      D = this;
   }

   public static boolean J(double var0, int var2) {
      return var0 > 0.0 || var2 > 0;
   }

   public static boolean W(Player var0) {
      ItemStack var1 = var0.getWeaponItem();
      int var2 = var0.level()
         .registryAccess()
         .lookupOrThrow(Registries.ENCHANTMENT)
         .get(Enchantments.SHARPNESS)
         .map(var1x -> EnchantmentHelper.getItemEnchantmentLevel(var1x, var1))
         .orElse(0);
      return J(var0.getAttributeValue(Attributes.ATTACK_DAMAGE), var2);
   }

   public static void n(Player var0, Entity var1) {
      if (D != null && D.isEnabled()) {
         CrystalOptimizerModule var2 = D;
         if (var2 != null && var2.isRuntimeActive() && var1 instanceof EndCrystal var3) {
            Minecraft var4 = getMc();
            if (var0 == var4.player
               && var4.level != null
               && var4.gameMode != null
               && !var4.gameMode.isSpectator()
               && var3.level() == var4.level
               && !var3.isRemoved()
               && !var3.isInvulnerable()) {
               if (!W(var0)) {
                  var2.v = "Your hit deals no damage";
               } else {
                  var4.level.removeEntity(var3.getId(), RemovalReason.KILLED);
                  var2.v = "Ready";
                  if (!var2.W) {
                     com.larpclient.h.U("CrystalOptimizer", "First crystal {} removed locally after the attack packet", var3.getId());
                     var2.W = (boolean)1;
                  }
               }
            }
         }
      }
   }

   @Override
   public String getStatus() {
      return this.isEnabled() ? this.v : "";
   }

   @Override
   public void onEnable() {
      this.v = "Ready";
      this.W = (boolean)0;
      com.larpclient.h.A("CrystalOptimizer", "Enabled; crystals you break are removed client-side after the native attack");
   }
}
