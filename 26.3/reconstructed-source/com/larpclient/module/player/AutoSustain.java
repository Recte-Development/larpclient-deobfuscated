package com.larpclient.module.player;

import com.larpclient.core.RotationManager;
import com.larpclient.module.misc.Module;
import com.larpclient.util.entity.SwingHelper;
import com.larpclient.util.inventory.AutoEatHelper;
import com.larpclient.util.inventory.ItemUseController;
import com.larpclient.util.misc.InteractionArbiter;
import com.larpclient.util.misc.LarpLog;
import java.util.Set;
import java.util.function.Predicate;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantments;

public final class AutoSustain {
   private static final Set<Item> excludedFoods = Set.of(
      Items.GOLDEN_APPLE, Items.ENCHANTED_GOLDEN_APPLE, Items.CHORUS_FRUIT, Items.PUFFERFISH, Items.SPIDER_EYE, Items.POISONOUS_POTATO, Items.SUSPICIOUS_STEW
   );
   public static final int hungerThreshold = 10;
   public static final int repairMargin = 150;
   private final Module module;
   private int lastFoodLevel = 20;
   private int lastBottleTick = -100;
   private ItemUseController.Owner eatTask;
   private boolean repairing;
   private boolean wasUsingItem;

   public AutoSustain(Module var1) {
      this.module = var1;
   }

   public void reset() {
      if (this.eatTask != null) {
         AutoEatHelper.cancel(this.eatTask);
      }

      this.eatTask = null;
      this.repairing = this.wasUsingItem = (boolean)0;
      this.lastFoodLevel = 20;
      this.lastBottleTick = -100;
   }

   public void start() {
      this.reset();
      LocalPlayer var1 = Minecraft.getInstance().player;
      if (var1 != null) {
         this.lastFoodLevel = var1.getFoodData().getFoodLevel();
      }
   }

   public static String getDangerReason() {
      LocalPlayer var0 = Minecraft.getInstance().player;
      if (var0 == null) {
         return null;
      } else if (var0.isInLava()) {
         return "In lava";
      } else {
         return var0.isUnderWater() && var0.getAirSupply() < var0.getMaxAirSupply() / 3 ? "Low on air" : null;
      }
   }

   public boolean tryEat(int var1) {
      LocalPlayer var2 = Minecraft.getInstance().player;
      int var3 = var2.getFoodData().getFoodLevel();
      boolean var4 = var2.isUsingItem();
      if (this.eatTask != null && this.wasUsingItem && !var4) {
         LarpLog.debug(this.module.getName(), "Finished eating ({}); hunger {}", this.eatTask, var3);
         AutoEatHelper.cancel(this.eatTask);
         this.eatTask = null;
         this.lastFoodLevel = var3;
      }

      this.wasUsingItem = (boolean)(var4 && this.eatTask != null ? 1 : 0);
      if (var3 > this.lastFoodLevel) {
         this.lastFoodLevel = var3;
      }

      ItemUseController.Owner var5 = null;
      int var6 = -1;
      if (var3 <= 10) {
         var6 = findBestFoodSlot();
         if (var6 >= 0) {
            var5 = ItemUseController.Owner.NORMAL;
         }
      }

      if (var5 == null && this.lastFoodLevel - var3 >= var1 * 2) {
         var6 = findHotbarSlot(AutoSustain::isGoldenApple);
         if (var6 >= 0) {
            var5 = ItemUseController.Owner.OVERRIDE;
         }
      }

      if (var5 == null) {
         this.stopEating();
         return false;
      }

      if (this.eatTask != var5) {
         if (this.eatTask != null) {
            AutoEatHelper.cancel(this.eatTask);
         }

         LarpLog.info(this.module.getName(), "Eating {} at hunger {}", var5 == ItemUseController.Owner.NORMAL ? "food" : "a golden apple", var3);
      }

      this.eatTask = var5;
      AutoEatHelper.start(var5, var6, var5 == ItemUseController.Owner.NORMAL ? AutoSustain::tryEat : AutoSustain::isGoldenApple);
      return true;
   }

   private static boolean tryEat(ItemStack var0) {
      return var0.get(DataComponents.FOOD) != null && !excludedFoods.contains(var0.getItem());
   }

   private static boolean isGoldenApple(ItemStack var0) {
      return var0.is(Items.GOLDEN_APPLE) || var0.is(Items.ENCHANTED_GOLDEN_APPLE);
   }

   public void stopEating() {
      if (this.eatTask != null) {
         AutoEatHelper.cancel(this.eatTask);
      }

      this.eatTask = null;
      this.wasUsingItem = (boolean)0;
   }

   private static int findBestFoodSlot() {
      Inventory var0 = Minecraft.getInstance().player.getInventory();
      int var1 = -1;
      int var2 = Integer.MIN_VALUE;

      for (int var3 = 0; var3 < 9; var3++) {
         ItemStack var4 = var0.getItem(var3);
         FoodProperties var5 = var4.isEmpty() ? null : (FoodProperties)var4.get(DataComponents.FOOD);
         if (var5 != null && !excludedFoods.contains(var4.getItem())) {
            int var6 = var4.is(Items.ROTTEN_FLESH) ? var5.nutrition() - 100 : var5.nutrition();
            if (var6 > var2) {
               var2 = var6;
               var1 = var3;
            }
         }
      }

      return var1;
   }

   public static int findHotbarSlot(Predicate<ItemStack> var0) {
      Inventory var1 = Minecraft.getInstance().player.getInventory();

      for (int var2 = 0; var2 < 9; var2++) {
         if (var0.test(var1.getItem(var2))) {
            return var2;
         }
      }

      return -1;
   }

   public static boolean hasMending(ItemStack var0) {
      for (Holder var2 : var0.getEnchantments().keySet()) {
         if (var2.is(Enchantments.MENDING)) {
            return true;
         }
      }

      return false;
   }

   public static int getRemainingDurability(ItemStack var0) {
      return var0.isDamageableItem() ? var0.getMaxDamage() - var0.getDamageValue() : Integer.MAX_VALUE;
   }

   public AutoSustain.RepairResult repairTool(int var1, int var2) {
      Minecraft var3 = Minecraft.getInstance();
      LocalPlayer var4 = var3.player;
      if (var1 >= 0 && var1 <= 8) {
         ItemStack var5 = var4.getInventory().getItem(var1);
         int var6 = getRemainingDurability(var5);
         if (var6 == Integer.MAX_VALUE) {
            this.repairing = (boolean)0;
            return AutoSustain.RepairResult.NONE;
         }

         int var7 = this.repairing ? (var6 < Math.min(var5.getMaxDamage(), var2 + 150) ? 1 : 0) : (var6 <= var2 ? 1 : 0);
         if (var7 == 0) {
            if (this.repairing) {
               LarpLog.info(this.module.getName(), "Tool repaired to {} uses", var6);
            }

            this.repairing = (boolean)0;
            return AutoSustain.RepairResult.NONE;
         } else {
            int var8 = findHotbarSlot(var0 -> var0.is(Items.EXPERIENCE_BOTTLE));
            if (hasMending(var5) && var8 >= 0) {
               if (!this.repairing) {
                  LarpLog.info(this.module.getName(), "Repairing tool at {} uses with XP bottles", var6);
               }

               this.repairing = (boolean)1;
               if (var4.getInventory().getSelectedSlot() != var1) {
                  var4.getInventory().setSelectedSlot(var1);
               }

               if (InteractionArbiter.ownsInteraction(this.module)) {
                  RotationManager.requestForModule(this.module, RotationManager.Priority.BOT, var4.getYRot(), 88.0F, 0.35);
               }

               if (var4.getXRot() >= 70.0F && var4.tickCount - this.lastBottleTick >= 4) {
                  InteractionArbiter.queueAction(this.module, () -> {
                     int var4x = findHotbarSlot(var0 -> var0.is(Items.EXPERIENCE_BOTTLE));
                     if (var4x >= 0) {
                        Inventory var5x = var4.getInventory();
                        var5x.setSelectedSlot(var4x);

                        try {
                           if (var3.gameMode.useItem(var4, InteractionHand.MAIN_HAND).consumesAction()) {
                              SwingHelper.swingInteract(var4, InteractionHand.MAIN_HAND);
                           }
                        } finally {
                           var5x.setSelectedSlot(var1);
                        }

                        this.lastBottleTick = var4.tickCount;
                     }
                  }, () -> var3.player == var4 && this.module.isRuntimeActive() && var4.getXRot() >= 70.0F);
               }

               return AutoSustain.RepairResult.REPAIRING;
            } else {
               if (this.repairing) {
                  LarpLog.info(
                     this.module.getName(), "Stopped repairing at {} uses: {}", var6, hasMending(var5) ? "no XP bottles left in the hotbar" : "no Mending"
                  );
               }

               this.repairing = (boolean)0;
               return var6 <= var2 ? AutoSustain.RepairResult.LOW_DURABILITY : AutoSustain.RepairResult.NONE;
            }
         }
      } else {
         this.repairing = (boolean)0;
         return AutoSustain.RepairResult.NONE;
      }
   }

   public boolean isRepairing() {
      return this.repairing;
   }

   public boolean throwXpBottle(Runnable var1) {
      Minecraft var2 = Minecraft.getInstance();
      LocalPlayer var3 = var2.player;
      int var4 = findHotbarSlot(var0 -> var0.is(Items.EXPERIENCE_BOTTLE));
      if (var4 < 0) {
         return false;
      }

      if (var3.getInventory().getSelectedSlot() != var4) {
         var3.getInventory().setSelectedSlot(var4);
      }

      if (InteractionArbiter.ownsInteraction(this.module)) {
         RotationManager.requestForModule(this.module, RotationManager.Priority.BOT, var3.getYRot(), 88.0F, 0.35);
      }

      if (var3.getXRot() >= 70.0F && var3.tickCount - this.lastBottleTick >= 4 && !var3.isUsingItem()) {
         InteractionArbiter.queueAction(this.module, () -> {
            int var4x = findHotbarSlot(var0 -> var0.is(Items.EXPERIENCE_BOTTLE));
            if (var4x >= 0) {
               var3.getInventory().setSelectedSlot(var4x);
               if (var2.gameMode.useItem(var3, InteractionHand.MAIN_HAND).consumesAction()) {
                  SwingHelper.swingInteract(var3, InteractionHand.MAIN_HAND);
                  var1.run();
               }

               this.lastBottleTick = var3.tickCount;
            }
         }, () -> var2.player == var3 && this.module.isRuntimeActive() && var3.getXRot() >= 70.0F);
      }

      return true;
   }

   public enum RepairResult {
      NONE,
      REPAIRING,
      LOW_DURABILITY;
   }
}
