package com.larpclient.module.combat;

import com.larpclient.module.misc.Category;
import com.larpclient.module.misc.Module;
import com.larpclient.util.misc.LarpLog;
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
   private static CrystalOptimizerModule instance;
   private String status = "Ready";
   private boolean loggedFirstRemoval;

   public CrystalOptimizerModule() {
      super("Crystal Optimizer", "Crystals you break disappear at once client-side for faster placement", Category.COMBAT, -1);
      instance = this;
   }

   public static boolean hasDamageSource(double var0, int var2) {
      return var0 > 0.0 || var2 > 0;
   }

   public static boolean canDealDamage(Player var0) {
      ItemStack var1 = var0.getWeaponItem();
      int var2 = var0.level()
         .registryAccess()
         .lookupOrThrow(Registries.ENCHANTMENT)
         .get(Enchantments.SHARPNESS)
         .map(var1x -> EnchantmentHelper.getItemEnchantmentLevel(var1x, var1))
         .orElse(0);
      return hasDamageSource(var0.getAttributeValue(Attributes.ATTACK_DAMAGE), var2);
   }

   public static void onAttackEntity(Player var0, Entity var1) {
      if (instance != null && instance.isEnabled()) {
         CrystalOptimizerModule var2 = instance;
         if (var2 != null && var2.isRuntimeActive() && var1 instanceof EndCrystal var3) {
            Minecraft var4 = getMc();
            if (var0 == var4.player
               && var4.level != null
               && var4.gameMode != null
               && !var4.gameMode.isSpectator()
               && var3.level() == var4.level
               && !var3.isRemoved()
               && !var3.isInvulnerable()) {
               if (!canDealDamage(var0)) {
                  var2.status = "Your hit deals no damage";
               } else {
                  var4.level.removeEntity(var3.getId(), RemovalReason.KILLED);
                  var2.status = "Ready";
                  if (!var2.loggedFirstRemoval) {
                     LarpLog.debug("CrystalOptimizer", "First crystal {} removed locally after the attack packet", var3.getId());
                     var2.loggedFirstRemoval = (boolean)1;
                  }
               }
            }
         }
      }
   }

   @Override
   public String getStatus() {
      return this.isEnabled() ? this.status : "";
   }

   @Override
   public void onEnable() {
      this.status = "Ready";
      this.loggedFirstRemoval = (boolean)0;
      LarpLog.info("CrystalOptimizer", "Enabled; crystals you break are removed client-side after the native attack");
   }
}
