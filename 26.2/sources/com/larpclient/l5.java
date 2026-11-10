package com.larpclient;

import java.util.List;
import net.minecraft.core.Holder;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.state.BlockState;

public final class l5 {
   public static l5.e F(ItemStack var0, BlockState var1, boolean var2) {
      double var3 = var0.getDestroySpeed(var1);
      if (var3 > 1.0) {
         for (Holder var6 : var0.getEnchantments().keySet()) {
            if (var6.is(Enchantments.EFFICIENCY)) {
               int var7 = var0.getEnchantments().getLevel(var6);
               var3 += (double)var7 * var7 + 1.0;
            }
         }
      }

      int var8 = var2 && var0.isDamageableItem() && var0.getMaxDamage() - var0.getDamageValue() <= 1 ? 0 : 1;
      return new l5.e(var3, !var1.requiresCorrectToolForDrops() || var0.isCorrectToolForDrops(var1), (boolean)var8);
   }

   public static int H(List<l5.e> var0, int var1) {
      int var2 = var1;

      for (int var3 = 0; var3 < var0.size(); var3++) {
         l5.e var4 = (l5.e)var0.get(var3);
         l5.e var5 = (l5.e)var0.get(var2);
         if (var4.usable() && (!var5.usable() || var4.harvest() && !var5.harvest() || var4.harvest() == var5.harvest() && var4.speed() > var5.speed())) {
            var2 = var3;
         }
      }

      return var2;
   }

   private l5() {
   }

   public record e(double speed, boolean harvest, boolean usable) {
   }

   public static final class u {
      private int T = -1;
      private int L = -1;
      private boolean Y;

      public void v() {
         this.T = this.L = -1;
         this.Y = (boolean)1;
      }

      public int T(int var1, int var2) {
         if (this.L >= 0 && var1 != this.L) {
            this.v();
         }

         if (!this.Y && var1 != var2) {
            if (this.T < 0) {
               this.T = var1;
            }

            this.L = var2;
            return var2;
         } else {
            return var1;
         }
      }

      public int G(int var1, boolean var2) {
         int var3 = var2 && var1 == this.L && this.T >= 0 ? this.T : var1;
         this.T = this.L = -1;
         this.Y = (boolean)0;
         return var3;
      }
   }
}
