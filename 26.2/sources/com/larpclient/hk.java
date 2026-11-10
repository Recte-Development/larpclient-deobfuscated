package com.larpclient;

import com.larpclient.module.Module;
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

public final class hk {
   private static final Set<Item> a = Set.of(
      Items.GOLDEN_APPLE, Items.ENCHANTED_GOLDEN_APPLE, Items.CHORUS_FRUIT, Items.PUFFERFISH, Items.SPIDER_EYE, Items.POISONOUS_POTATO, Items.SUSPICIOUS_STEW
   );
   public static final int t = 10;
   public static final int r = 150;
   private final Module M;
   private int Z = 20;
   private int B = -100;
   private jf.k j;
   private boolean C;
   private boolean v;

   public hk(Module var1) {
      this.M = var1;
   }

   public void F() {
      if (this.j != null) {
         g7.R(this.j);
      }

      this.j = null;
      this.C = this.v = (boolean)0;
      this.Z = 20;
      this.B = -100;
   }

   public void k() {
      this.F();
      LocalPlayer var1 = Minecraft.getInstance().player;
      if (var1 != null) {
         this.Z = var1.getFoodData().getFoodLevel();
      }
   }

   public static String j() {
      LocalPlayer var0 = Minecraft.getInstance().player;
      if (var0 == null) {
         return null;
      } else if (var0.isInLava()) {
         return "In lava";
      } else {
         return var0.isUnderWater() && var0.getAirSupply() < var0.getMaxAirSupply() / 3 ? "Low on air" : null;
      }
   }

   public boolean r(int var1) {
      LocalPlayer var2 = Minecraft.getInstance().player;
      int var3 = var2.getFoodData().getFoodLevel();
      boolean var4 = var2.isUsingItem();
      if (this.j != null && this.v && !var4) {
         com.larpclient.h.U(this.M.getName(), "Finished eating ({}); hunger {}", this.j, var3);
         g7.R(this.j);
         this.j = null;
         this.Z = var3;
      }

      this.v = (boolean)(var4 && this.j != null ? 1 : 0);
      if (var3 > this.Z) {
         this.Z = var3;
      }

      jf.k var5 = null;
      int var6 = -1;
      if (var3 <= 10) {
         var6 = H();
         if (var6 >= 0) {
            var5 = jf.k.W;
         }
      }

      if (var5 == null && this.Z - var3 >= var1 * 2) {
         var6 = J(hk::F);
         if (var6 >= 0) {
            var5 = jf.k.q;
         }
      }

      if (var5 == null) {
         this.B();
         return false;
      }

      if (this.j != var5) {
         if (this.j != null) {
            g7.R(this.j);
         }

         com.larpclient.h.A(this.M.getName(), "Eating {} at hunger {}", var5 == jf.k.W ? "food" : "a golden apple", var3);
      }

      this.j = var5;
      g7.O(var5, var6, var5 == jf.k.W ? hk::S : hk::F);
      return true;
   }

   private static boolean S(ItemStack var0) {
      return var0.get(DataComponents.FOOD) != null && !a.contains(var0.getItem());
   }

   private static boolean F(ItemStack var0) {
      return var0.is(Items.GOLDEN_APPLE) || var0.is(Items.ENCHANTED_GOLDEN_APPLE);
   }

   public void B() {
      if (this.j != null) {
         g7.R(this.j);
      }

      this.j = null;
      this.v = (boolean)0;
   }

   private static int H() {
      Inventory var0 = Minecraft.getInstance().player.getInventory();
      int var1 = -1;
      int var2 = Integer.MIN_VALUE;

      for (int var3 = 0; var3 < 9; var3++) {
         ItemStack var4 = var0.getItem(var3);
         FoodProperties var5 = var4.isEmpty() ? null : (FoodProperties)var4.get(DataComponents.FOOD);
         if (var5 != null && !a.contains(var4.getItem())) {
            int var6 = var4.is(Items.ROTTEN_FLESH) ? var5.nutrition() - 100 : var5.nutrition();
            if (var6 > var2) {
               var2 = var6;
               var1 = var3;
            }
         }
      }

      return var1;
   }

   public static int J(Predicate<ItemStack> var0) {
      Inventory var1 = Minecraft.getInstance().player.getInventory();

      for (int var2 = 0; var2 < 9; var2++) {
         if (var0.test(var1.getItem(var2))) {
            return var2;
         }
      }

      return -1;
   }

   public static boolean x(ItemStack var0) {
      for (Holder var2 : var0.getEnchantments().keySet()) {
         if (var2.is(Enchantments.MENDING)) {
            return true;
         }
      }

      return false;
   }

   public static int T(ItemStack var0) {
      return var0.isDamageableItem() ? var0.getMaxDamage() - var0.getDamageValue() : Integer.MAX_VALUE;
   }

   public hk.h w(int var1, int var2) {
      Minecraft var3 = Minecraft.getInstance();
      LocalPlayer var4 = var3.player;
      if (var1 >= 0 && var1 <= 8) {
         ItemStack var5 = var4.getInventory().getItem(var1);
         int var6 = T(var5);
         if (var6 == Integer.MAX_VALUE) {
            this.C = (boolean)0;
            return hk.h.K;
         }

         int var7 = this.C ? (var6 < Math.min(var5.getMaxDamage(), var2 + 150) ? 1 : 0) : (var6 <= var2 ? 1 : 0);
         if (var7 == 0) {
            if (this.C) {
               com.larpclient.h.A(this.M.getName(), "Tool repaired to {} uses", var6);
            }

            this.C = (boolean)0;
            return hk.h.K;
         } else {
            int var8 = J(var0 -> var0.is(Items.EXPERIENCE_BOTTLE));
            if (x(var5) && var8 >= 0) {
               if (!this.C) {
                  com.larpclient.h.A(this.M.getName(), "Repairing tool at {} uses with XP bottles", var6);
               }

               this.C = (boolean)1;
               if (var4.getInventory().getSelectedSlot() != var1) {
                  var4.getInventory().setSelectedSlot(var1);
               }

               if (ta.E(this.M)) {
                  j6.U(this.M, j6.l.o, var4.getYRot(), 88.0F, 0.35);
               }

               if (var4.getXRot() >= 70.0F && var4.tickCount - this.B >= 4) {
                  ta.F(this.M, () -> {
                     int var4x = J(var0 -> var0.is(Items.EXPERIENCE_BOTTLE));
                     if (var4x >= 0) {
                        Inventory var5x = var4.getInventory();
                        var5x.setSelectedSlot(var4x);

                        try {
                           if (var3.gameMode.useItem(var4, InteractionHand.MAIN_HAND).consumesAction()) {
                              ks.p(var4, InteractionHand.MAIN_HAND);
                           }
                        } finally {
                           var5x.setSelectedSlot(var1);
                        }

                        this.B = var4.tickCount;
                     }
                  }, () -> var3.player == var4 && this.M.isRuntimeActive() && var4.getXRot() >= 70.0F);
               }

               return hk.h.E;
            } else {
               if (this.C) {
                  com.larpclient.h.A(this.M.getName(), "Stopped repairing at {} uses: {}", var6, x(var5) ? "no XP bottles left in the hotbar" : "no Mending");
               }

               this.C = (boolean)0;
               return var6 <= var2 ? hk.h.f : hk.h.K;
            }
         }
      } else {
         this.C = (boolean)0;
         return hk.h.K;
      }
   }

   public boolean P() {
      return this.C;
   }

   public boolean C(Runnable var1) {
      Minecraft var2 = Minecraft.getInstance();
      LocalPlayer var3 = var2.player;
      int var4 = J(var0 -> var0.is(Items.EXPERIENCE_BOTTLE));
      if (var4 < 0) {
         return false;
      }

      if (var3.getInventory().getSelectedSlot() != var4) {
         var3.getInventory().setSelectedSlot(var4);
      }

      if (ta.E(this.M)) {
         j6.U(this.M, j6.l.o, var3.getYRot(), 88.0F, 0.35);
      }

      if (var3.getXRot() >= 70.0F && var3.tickCount - this.B >= 4 && !var3.isUsingItem()) {
         ta.F(this.M, () -> {
            int var4x = J(var0 -> var0.is(Items.EXPERIENCE_BOTTLE));
            if (var4x >= 0) {
               var3.getInventory().setSelectedSlot(var4x);
               if (var2.gameMode.useItem(var3, InteractionHand.MAIN_HAND).consumesAction()) {
                  ks.p(var3, InteractionHand.MAIN_HAND);
                  var1.run();
               }

               this.B = var3.tickCount;
            }
         }, () -> var2.player == var3 && this.M.isRuntimeActive() && var3.getXRot() >= 70.0F);
      }

      return true;
   }

   public enum h {
      K,
      E,
      f;
   }
}
