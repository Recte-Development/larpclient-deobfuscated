package com.larpclient;

import com.larpclient.module.Module;
import java.util.Set;
import java.util.function.Predicate;
import net.minecraft.class_1268;
import net.minecraft.class_1661;
import net.minecraft.class_1792;
import net.minecraft.class_1799;
import net.minecraft.class_1802;
import net.minecraft.class_1893;
import net.minecraft.class_310;
import net.minecraft.class_4174;
import net.minecraft.class_6880;
import net.minecraft.class_746;
import net.minecraft.class_9334;

public final class tn {
   private static final Set<class_1792> O = Set.of(
      class_1802.field_8463,
      class_1802.field_8367,
      class_1802.field_8233,
      class_1802.field_8323,
      class_1802.field_8680,
      class_1802.field_8635,
      class_1802.field_8766
   );
   public static final int G = 10;
   public static final int w = 150;
   private final Module z;
   private int I = 20;
   private int j = -100;
   private b_.s g;
   private boolean l;
   private boolean W;

   public tn(Module var1) {
      this.z = var1;
   }

   public void E() {
      if (this.g != null) {
         wv.P(this.g);
      }

      this.g = null;
      this.l = this.W = (boolean)0;
      this.I = 20;
      this.j = -100;
   }

   public void V() {
      this.E();
      class_746 var1 = class_310.method_1551().field_1724;
      if (var1 != null) {
         this.I = var1.method_7344().method_7586();
      }
   }

   public static String b() {
      class_746 var0 = class_310.method_1551().field_1724;
      if (var0 == null) {
         return null;
      } else if (var0.method_5771()) {
         return "In lava";
      } else {
         return var0.method_5869() && var0.method_5669() < var0.method_5748() / 3 ? "Low on air" : null;
      }
   }

   public boolean d(int var1) {
      class_746 var2 = class_310.method_1551().field_1724;
      int var3 = var2.method_7344().method_7586();
      boolean var4 = var2.method_6115();
      if (this.g != null && this.W && !var4) {
         q8.v(this.z.getName(), "Finished eating ({}); hunger {}", this.g, var3);
         wv.P(this.g);
         this.g = null;
         this.I = var3;
      }

      this.W = (boolean)(var4 && this.g != null ? 1 : 0);
      if (var3 > this.I) {
         this.I = var3;
      }

      b_.s var5 = null;
      int var6 = -1;
      if (var3 <= 10) {
         var6 = O();
         if (var6 >= 0) {
            var5 = b_.s.z;
         }
      }

      if (var5 == null && this.I - var3 >= var1 * 2) {
         var6 = h(tn::U);
         if (var6 >= 0) {
            var5 = b_.s.t;
         }
      }

      if (var5 == null) {
         this.K();
         return false;
      }

      if (this.g != var5) {
         if (this.g != null) {
            wv.P(this.g);
         }

         q8.G(this.z.getName(), "Eating {} at hunger {}", var5 == b_.s.z ? "food" : "a golden apple", var3);
      }

      this.g = var5;
      wv.D(var5, var6, var5 == b_.s.z ? tn::L : tn::U);
      return true;
   }

   private static boolean L(class_1799 var0) {
      return var0.method_58694(class_9334.field_50075) != null && !O.contains(var0.method_7909());
   }

   private static boolean U(class_1799 var0) {
      return var0.method_31574(class_1802.field_8463) || var0.method_31574(class_1802.field_8367);
   }

   public void K() {
      if (this.g != null) {
         wv.P(this.g);
      }

      this.g = null;
      this.W = (boolean)0;
   }

   private static int O() {
      class_1661 var0 = class_310.method_1551().field_1724.method_31548();
      int var1 = -1;
      int var2 = Integer.MIN_VALUE;

      for (int var3 = 0; var3 < 9; var3++) {
         class_1799 var4 = var0.method_5438(var3);
         class_4174 var5 = var4.method_7960() ? null : (class_4174)var4.method_58694(class_9334.field_50075);
         if (var5 != null && !O.contains(var4.method_7909())) {
            int var6 = var4.method_31574(class_1802.field_8511) ? var5.comp_2491() - 100 : var5.comp_2491();
            if (var6 > var2) {
               var2 = var6;
               var1 = var3;
            }
         }
      }

      return var1;
   }

   public static int h(Predicate<class_1799> var0) {
      class_1661 var1 = class_310.method_1551().field_1724.method_31548();

      for (int var2 = 0; var2 < 9; var2++) {
         if (var0.test(var1.method_5438(var2))) {
            return var2;
         }
      }

      return -1;
   }

   public static boolean S(class_1799 var0) {
      for (class_6880 var2 : var0.method_58657().method_57534()) {
         if (var2.method_40225(class_1893.field_9101)) {
            return true;
         }
      }

      return false;
   }

   public static int p(class_1799 var0) {
      return var0.method_7963() ? var0.method_7936() - var0.method_7919() : Integer.MAX_VALUE;
   }

   public tn.b k(int var1, int var2) {
      class_310 var3 = class_310.method_1551();
      class_746 var4 = var3.field_1724;
      if (var1 >= 0 && var1 <= 8) {
         class_1799 var5 = var4.method_31548().method_5438(var1);
         int var6 = p(var5);
         if (var6 == Integer.MAX_VALUE) {
            this.l = (boolean)0;
            return tn.b.Q;
         }

         int var7 = this.l ? (var6 < Math.min(var5.method_7936(), var2 + 150) ? 1 : 0) : (var6 <= var2 ? 1 : 0);
         if (var7 == 0) {
            if (this.l) {
               q8.G(this.z.getName(), "Tool repaired to {} uses", var6);
            }

            this.l = (boolean)0;
            return tn.b.Q;
         } else {
            int var8 = h(var0 -> var0.method_31574(class_1802.field_8287));
            if (S(var5) && var8 >= 0) {
               if (!this.l) {
                  q8.G(this.z.getName(), "Repairing tool at {} uses with XP bottles", var6);
               }

               this.l = (boolean)1;
               if (var4.method_31548().method_67532() != var1) {
                  var4.method_31548().method_61496(var1);
               }

               if (cr.a(this.z)) {
                  t9.O(this.z, t9.v.e, var4.method_36454(), 88.0F, 0.35);
               }

               if (var4.method_36455() >= 70.0F && var4.field_6012 - this.j >= 4) {
                  cr.y(this.z, () -> {
                     int var4x = h(var0 -> var0.method_31574(class_1802.field_8287));
                     if (var4x >= 0) {
                        class_1661 var5x = var4.method_31548();
                        var5x.method_61496(var4x);

                        try {
                           if (var3.field_1761.method_2919(var4, class_1268.field_5808).method_23665()) {
                              w6.H(var4, class_1268.field_5808);
                           }
                        } finally {
                           var5x.method_61496(var1);
                        }

                        this.j = var4.field_6012;
                     }
                  }, () -> var3.field_1724 == var4 && this.z.isRuntimeActive() && var4.method_36455() >= 70.0F);
               }

               return tn.b.c;
            } else {
               if (this.l) {
                  q8.G(this.z.getName(), "Stopped repairing at {} uses: {}", var6, S(var5) ? "no XP bottles left in the hotbar" : "no Mending");
               }

               this.l = (boolean)0;
               return var6 <= var2 ? tn.b.p : tn.b.Q;
            }
         }
      } else {
         this.l = (boolean)0;
         return tn.b.Q;
      }
   }

   public boolean s() {
      return this.l;
   }

   public boolean Q(Runnable var1) {
      class_310 var2 = class_310.method_1551();
      class_746 var3 = var2.field_1724;
      int var4 = h(var0 -> var0.method_31574(class_1802.field_8287));
      if (var4 < 0) {
         return false;
      }

      if (var3.method_31548().method_67532() != var4) {
         var3.method_31548().method_61496(var4);
      }

      if (cr.a(this.z)) {
         t9.O(this.z, t9.v.e, var3.method_36454(), 88.0F, 0.35);
      }

      if (var3.method_36455() >= 70.0F && var3.field_6012 - this.j >= 4 && !var3.method_6115()) {
         cr.y(this.z, () -> {
            int var4x = h(var0 -> var0.method_31574(class_1802.field_8287));
            if (var4x >= 0) {
               var3.method_31548().method_61496(var4x);
               if (var2.field_1761.method_2919(var3, class_1268.field_5808).method_23665()) {
                  w6.H(var3, class_1268.field_5808);
                  var1.run();
               }

               this.j = var3.field_6012;
            }
         }, () -> var2.field_1724 == var3 && this.z.isRuntimeActive() && var3.method_36455() >= 70.0F);
      }

      return true;
   }

   public enum b {
      Q,
      c,
      p;
   }
}
