package com.larpclient.module;

import com.larpclient._q;
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
   private static CrystalOptimizerModule y;
   private String Y = "Ready";
   private boolean x;

   public CrystalOptimizerModule() {
      super("Crystal Optimizer", "Crystals you break disappear at once client-side for faster placement", Category.COMBAT, -1);
      y = this;
   }

   public static boolean p(double var0, int var2) {
      return var0 > 0.0 || var2 > 0;
   }

   public static boolean B(Player var0) {
      ItemStack var1 = var0.getWeaponItem();
      int var2 = var0.level()
         .registryAccess()
         .lookupOrThrow(Registries.ENCHANTMENT)
         .get(Enchantments.SHARPNESS)
         .map(var1x -> EnchantmentHelper.getItemEnchantmentLevel(var1x, var1))
         .orElse(0);
      return p(var0.getAttributeValue(Attributes.ATTACK_DAMAGE), var2);
   }

   public static void E(Player var0, Entity var1) {
      if (y != null && y.isEnabled()) {
         CrystalOptimizerModule var2 = y;
         if (var2 != null && var2.isRuntimeActive() && var1 instanceof EndCrystal var3) {
            Minecraft var4 = getMc();
            if (var0 == var4.player
               && var4.level != null
               && var4.gameMode != null
               && !var4.gameMode.isSpectator()
               && var3.level() == var4.level
               && !var3.isRemoved()
               && !var3.isInvulnerable()) {
               if (!B(var0)) {
                  var2.Y = "Your hit deals no damage";
               } else {
                  var4.level.removeEntity(var3.getId(), RemovalReason.KILLED);
                  var2.Y = "Ready";
                  if (!var2.x) {
                     _q.c("CrystalOptimizer", "First crystal {} removed locally after the attack packet", var3.getId());
                     var2.x = (boolean)1;
                  }
               }
            }
         }
      }
   }

   @Override
   public String getStatus() {
      return this.isEnabled() ? this.Y : "";
   }

   @Override
   public void onEnable() {
      this.Y = "Ready";
      this.x = (boolean)0;
      _q.z("CrystalOptimizer", "Enabled; crystals you break are removed client-side after the native attack");
   }
}
