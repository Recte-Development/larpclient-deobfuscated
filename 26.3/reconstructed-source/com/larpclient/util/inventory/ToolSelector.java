package com.larpclient.util.inventory;

import java.util.List;
import net.minecraft.core.Holder;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.state.BlockState;

public final class ToolSelector {
   public static ToolSelector.ToolScore evaluateTool(ItemStack var0, BlockState var1, boolean var2) {
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
      return new ToolSelector.ToolScore(var3, !var1.requiresCorrectToolForDrops() || var0.isCorrectToolForDrops(var1), (boolean)var8);
   }

   public static int pickBestTool(List<ToolSelector.ToolScore> var0, int var1) {
      int var2 = var1;

      for (int var3 = 0; var3 < var0.size(); var3++) {
         ToolSelector.ToolScore var4 = (ToolSelector.ToolScore)var0.get(var3);
         ToolSelector.ToolScore var5 = (ToolSelector.ToolScore)var0.get(var2);
         if (var4.usable() && (!var5.usable() || var4.harvest() && !var5.harvest() || var4.harvest() == var5.harvest() && var4.speed() > var5.speed())) {
            var2 = var3;
         }
      }

      return var2;
   }

   private ToolSelector() {
   }

   public static final class SlotSwitcher {
      private int originalSlot = -1;
      private int targetSlot = -1;
      private boolean suppressed;

      public void suppress() {
         this.originalSlot = this.targetSlot = -1;
         this.suppressed = (boolean)1;
      }

      public int select(int var1, int var2) {
         if (this.targetSlot >= 0 && var1 != this.targetSlot) {
            this.suppress();
         }

         if (!this.suppressed && var1 != var2) {
            if (this.originalSlot < 0) {
               this.originalSlot = var1;
            }

            this.targetSlot = var2;
            return var2;
         } else {
            return var1;
         }
      }

      public int restore(int var1, boolean var2) {
         int var3 = var2 && var1 == this.targetSlot && this.originalSlot >= 0 ? this.originalSlot : var1;
         this.originalSlot = this.targetSlot = -1;
         this.suppressed = (boolean)0;
         return var3;
      }
   }

   public record ToolScore(double speed, boolean harvest, boolean usable) {
   }
}
