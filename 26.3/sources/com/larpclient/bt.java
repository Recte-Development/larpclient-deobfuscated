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

public final class bt {
   private static final Set<Item> H = Set.of(
      Items.GOLDEN_APPLE, Items.ENCHANTED_GOLDEN_APPLE, Items.CHORUS_FRUIT, Items.PUFFERFISH, Items.SPIDER_EYE, Items.POISONOUS_POTATO, Items.SUSPICIOUS_STEW
   );
   public static final int r = 10;
   public static final int U = 150;
   private final Module J;
   private int V = 20;
   private int t = -100;
   private pt.d b;
   private boolean o;
   private boolean Y;

   public bt(Module var1) {
      this.J = var1;
   }

   public void b() {
      if (this.b != null) {
         f7.h(this.b);
      }

      this.b = null;
      this.o = this.Y = (boolean)0;
      this.V = 20;
      this.t = -100;
   }

   public void v() {
      this.b();
      LocalPlayer var1 = Minecraft.getInstance().player;
      if (var1 != null) {
         this.V = var1.getFoodData().getFoodLevel();
      }
   }

   public static String L() {
      LocalPlayer var0 = Minecraft.getInstance().player;
      if (var0 == null) {
         return null;
      } else if (var0.isInLava()) {
         return "In lava";
      } else {
         return var0.isUnderWater() && var0.getAirSupply() < var0.getMaxAirSupply() / 3 ? "Low on air" : null;
      }
   }

   public boolean T(int var1) {
      LocalPlayer var2 = Minecraft.getInstance().player;
      int var3 = var2.getFoodData().getFoodLevel();
      boolean var4 = var2.isUsingItem();
      if (this.b != null && this.Y && !var4) {
         _q.c(this.J.getName(), "Finished eating ({}); hunger {}", this.b, var3);
         f7.h(this.b);
         this.b = null;
         this.V = var3;
      }

      this.Y = (boolean)(var4 && this.b != null ? 1 : 0);
      if (var3 > this.V) {
         this.V = var3;
      }

      pt.d var5 = null;
      int var6 = -1;
      if (var3 <= 10) {
         var6 = i();
         if (var6 >= 0) {
            var5 = pt.d.p;
         }
      }

      if (var5 == null && this.V - var3 >= var1 * 2) {
         var6 = a(bt::N);
         if (var6 >= 0) {
            var5 = pt.d.i;
         }
      }

      if (var5 == null) {
         this.s();
         return false;
      }

      if (this.b != var5) {
         if (this.b != null) {
            f7.h(this.b);
         }

         _q.z(this.J.getName(), "Eating {} at hunger {}", var5 == pt.d.p ? "food" : "a golden apple", var3);
      }

      this.b = var5;
      f7.Q(var5, var6, var5 == pt.d.p ? bt::T : bt::N);
      return true;
   }

   private static boolean T(ItemStack var0) {
      return var0.get(DataComponents.FOOD) != null && !H.contains(var0.getItem());
   }

   private static boolean N(ItemStack var0) {
      return var0.is(Items.GOLDEN_APPLE) || var0.is(Items.ENCHANTED_GOLDEN_APPLE);
   }

   public void s() {
      if (this.b != null) {
         f7.h(this.b);
      }

      this.b = null;
      this.Y = (boolean)0;
   }

   private static int i() {
      Inventory var0 = Minecraft.getInstance().player.getInventory();
      int var1 = -1;
      int var2 = Integer.MIN_VALUE;

      for (int var3 = 0; var3 < 9; var3++) {
         ItemStack var4 = var0.getItem(var3);
         FoodProperties var5 = var4.isEmpty() ? null : (FoodProperties)var4.get(DataComponents.FOOD);
         if (var5 != null && !H.contains(var4.getItem())) {
            int var6 = var4.is(Items.ROTTEN_FLESH) ? var5.nutrition() - 100 : var5.nutrition();
            if (var6 > var2) {
               var2 = var6;
               var1 = var3;
            }
         }
      }

      return var1;
   }

   public static int a(Predicate<ItemStack> var0) {
      Inventory var1 = Minecraft.getInstance().player.getInventory();

      for (int var2 = 0; var2 < 9; var2++) {
         if (var0.test(var1.getItem(var2))) {
            return var2;
         }
      }

      return -1;
   }

   public static boolean e(ItemStack var0) {
      for (Holder var2 : var0.getEnchantments().keySet()) {
         if (var2.is(Enchantments.MENDING)) {
            return true;
         }
      }

      return false;
   }

   public static int J(ItemStack var0) {
      return var0.isDamageableItem() ? var0.getMaxDamage() - var0.getDamageValue() : Integer.MAX_VALUE;
   }

   public bt.x k(int var1, int var2) {
      Minecraft var3 = Minecraft.getInstance();
      LocalPlayer var4 = var3.player;
      if (var1 >= 0 && var1 <= 8) {
         ItemStack var5 = var4.getInventory().getItem(var1);
         int var6 = J(var5);
         if (var6 == Integer.MAX_VALUE) {
            this.o = (boolean)0;
            return bt.x.o;
         }

         int var7 = this.o ? (var6 < Math.min(var5.getMaxDamage(), var2 + 150) ? 1 : 0) : (var6 <= var2 ? 1 : 0);
         if (var7 == 0) {
            if (this.o) {
               _q.z(this.J.getName(), "Tool repaired to {} uses", var6);
            }

            this.o = (boolean)0;
            return bt.x.o;
         } else {
            int var8 = a(var0 -> var0.is(Items.EXPERIENCE_BOTTLE));
            if (e(var5) && var8 >= 0) {
               if (!this.o) {
                  _q.z(this.J.getName(), "Repairing tool at {} uses with XP bottles", var6);
               }

               this.o = (boolean)1;
               if (var4.getInventory().getSelectedSlot() != var1) {
                  var4.getInventory().setSelectedSlot(var1);
               }

               if (_p.Q(this.J)) {
                  b6.P(this.J, b6.w.x, var4.getYRot(), 88.0F, 0.35);
               }

               if (var4.getXRot() >= 70.0F && var4.tickCount - this.t >= 4) {
                  _p.w(this.J, () -> {
                     int var4x = a(var0 -> var0.is(Items.EXPERIENCE_BOTTLE));
                     if (var4x >= 0) {
                        Inventory var5x = var4.getInventory();
                        var5x.setSelectedSlot(var4x);

                        try {
                           if (var3.gameMode.useItem(var4, InteractionHand.MAIN_HAND).consumesAction()) {
                              h8.i(var4, InteractionHand.MAIN_HAND);
                           }
                        } finally {
                           var5x.setSelectedSlot(var1);
                        }

                        this.t = var4.tickCount;
                     }
                  }, () -> var3.player == var4 && this.J.isRuntimeActive() && var4.getXRot() >= 70.0F);
               }

               return bt.x.a;
            } else {
               if (this.o) {
                  _q.z(this.J.getName(), "Stopped repairing at {} uses: {}", var6, e(var5) ? "no XP bottles left in the hotbar" : "no Mending");
               }

               this.o = (boolean)0;
               return var6 <= var2 ? bt.x.f : bt.x.o;
            }
         }
      } else {
         this.o = (boolean)0;
         return bt.x.o;
      }
   }

   public boolean t() {
      return this.o;
   }

   public boolean w(Runnable var1) {
      Minecraft var2 = Minecraft.getInstance();
      LocalPlayer var3 = var2.player;
      int var4 = a(var0 -> var0.is(Items.EXPERIENCE_BOTTLE));
      if (var4 < 0) {
         return false;
      }

      if (var3.getInventory().getSelectedSlot() != var4) {
         var3.getInventory().setSelectedSlot(var4);
      }

      if (_p.Q(this.J)) {
         b6.P(this.J, b6.w.x, var3.getYRot(), 88.0F, 0.35);
      }

      if (var3.getXRot() >= 70.0F && var3.tickCount - this.t >= 4 && !var3.isUsingItem()) {
         _p.w(this.J, () -> {
            int var4x = a(var0 -> var0.is(Items.EXPERIENCE_BOTTLE));
            if (var4x >= 0) {
               var3.getInventory().setSelectedSlot(var4x);
               if (var2.gameMode.useItem(var3, InteractionHand.MAIN_HAND).consumesAction()) {
                  h8.i(var3, InteractionHand.MAIN_HAND);
                  var1.run();
               }

               this.t = var3.tickCount;
            }
         }, () -> var2.player == var3 && this.J.isRuntimeActive() && var3.getXRot() >= 70.0F);
      }

      return true;
   }

   public enum x {
      o,
      a,
      f;
   }
}
