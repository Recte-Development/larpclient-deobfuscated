package com.larpclient.module;

import com.larpclient.mt;
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
   private static CrystalOptimizerModule v;
   private String y = "Ready";
   private boolean W;

   public CrystalOptimizerModule() {
      super("Crystal Optimizer", "Crystals you break disappear at once client-side for faster placement", Category.COMBAT, -1);
      v = this;
   }

   public static boolean e(double var0, int var2) {
      return var0 > 0.0 || var2 > 0;
   }

   public static void A(Player var0, Entity var1) {
      if (v != null && v.isEnabled()) {
         CrystalOptimizerModule var2 = v;
         if (var2 != null && var2.isRuntimeActive() && var1 instanceof EndCrystal var3) {
            Minecraft var4 = getMc();
            if (var0 == var4.player
               && var4.level != null
               && var4.gameMode != null
               && !var4.gameMode.isSpectator()
               && var3.level() == var4.level
               && !var3.isRemoved()
               && !var3.isInvulnerable()) {
               ItemStack var5 = var0.getWeaponItem();
               int var6 = var4.level
                  .registryAccess()
                  .lookupOrThrow(Registries.ENCHANTMENT)
                  .get(Enchantments.SHARPNESS)
                  .map(var1x -> EnchantmentHelper.getItemEnchantmentLevel(var1x, var5))
                  .orElse(0);
               if (!e(var0.getAttributeValue(Attributes.ATTACK_DAMAGE), var6)) {
                  var2.y = "Your hit deals no damage";
               } else {
                  var4.level.removeEntity(var3.getId(), RemovalReason.KILLED);
                  var2.y = "Ready";
                  if (!var2.W) {
                     mt.U("CrystalOptimizer", "First crystal {} removed locally after the attack packet", var3.getId());
                     var2.W = (boolean)1;
                  }
               }
            }
         }
      }
   }

   @Override
   public String getStatus() {
      return this.isEnabled() ? this.y : "";
   }

   @Override
   public void onEnable() {
      this.y = "Ready";
      this.W = (boolean)0;
      mt.K("CrystalOptimizer", "Enabled; crystals you break are removed client-side after the native attack");
   }
}
