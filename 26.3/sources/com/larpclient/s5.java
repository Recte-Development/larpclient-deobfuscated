package com.larpclient;

import java.util.List;
import net.minecraft.core.Holder;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.state.BlockState;

public final class s5 {
   public static s5.k d(ItemStack var0, BlockState var1, boolean var2) {
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
      return new s5.k(var3, !var1.requiresCorrectToolForDrops() || var0.isCorrectToolForDrops(var1), (boolean)var8);
   }

   public static int M(List<s5.k> var0, int var1) {
      int var2 = var1;

      for (int var3 = 0; var3 < var0.size(); var3++) {
         s5.k var4 = (s5.k)var0.get(var3);
         s5.k var5 = (s5.k)var0.get(var2);
         if (var4.usable() && (!var5.usable() || var4.harvest() && !var5.harvest() || var4.harvest() == var5.harvest() && var4.speed() > var5.speed())) {
            var2 = var3;
         }
      }

      return var2;
   }

   private s5() {
   }

   public record k(double speed, boolean harvest, boolean usable) {
   }

   public static final class w {
      private int E = -1;
      private int S = -1;
      private boolean I;

      public void R() {
         this.E = this.S = -1;
         this.I = (boolean)1;
      }

      public int z(int var1, int var2) {
         if (this.S >= 0 && var1 != this.S) {
            this.R();
         }

         if (!this.I && var1 != var2) {
            if (this.E < 0) {
               this.E = var1;
            }

            this.S = var2;
            return var2;
         } else {
            return var1;
         }
      }

      public int b(int var1, boolean var2) {
         int var3 = var2 && var1 == this.S && this.E >= 0 ? this.E : var1;
         this.E = this.S = -1;
         this.I = (boolean)0;
         return var3;
      }
   }
}
