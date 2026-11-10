package com.larpclient;

import net.minecraft.class_1309;
import net.minecraft.class_1542;
import net.minecraft.class_1713;
import net.minecraft.class_1716;
import net.minecraft.class_1735;
import net.minecraft.class_1792;
import net.minecraft.class_1799;
import net.minecraft.class_1802;
import net.minecraft.class_2338;
import net.minecraft.class_310;

public class yb {
   private static final yb X = new yb();
   public class_1792 P = class_1802.field_8626;
   public class_1792 Y = class_1802.field_8407;
   public boolean j;
   public boolean N;
   private final en c = new en(true, true);
   private boolean G;
   private boolean K;

   public static yb b() {
      return X;
   }

   public void Z() {
      this.N = (boolean)1;
      this.c.J = 2;
   }

   public boolean W(class_310 var1, class_2338 var2, class_1716 var3) {
      this.c.J = this.N ? 2 : 1;
      if (var2 != null && var1 != null) {
         try {
            this.c.Y(var1, var2, var3);
         } catch (IllegalArgumentException var5) {
            return false;
         }

         this.Y = this.c.F(1).method_7909();
         this.P = this.c.F(2).method_7909();
         return true;
      } else {
         return false;
      }
   }

   private boolean U(class_310 var1, class_1716 var2) {
      hu var3 = hu.f();
      class_2338 var4 = this.c.P(0);
      return this.j
         && var4 != null
         && var1 != null
         && var1.field_1724 != null
         && var2 == var1.field_1724.field_7512
         && var2.field_7763 == var3.z()
         && var3.N(var1, var4).equals(var3.W());
   }

   public void e(class_310 var1) {
      if (!this.G) {
         if (var1 != null && var1.field_1724 != null && var1.field_1687 != null) {
            if (!this.j) {
               if (this.K) {
                  this.c.N(var1);
               }

               this.K = (boolean)0;
            } else {
               this.K = (boolean)1;
               if (this.c.e()) {
                  this.N = (boolean)0;
               }

               this.c.J = this.N ? 2 : 1;
               this.c.I(var1);
               if (this.c.e()) {
                  this.N = (boolean)0;
               }
            }
         } else {
            if (this.K && var1 != null) {
               this.c.N(var1);
            }

            this.K = (boolean)0;
         }
      }
   }

   public class_1799 U(class_1799 var1) {
      return this.j && var1 != null && !var1.method_7960() ? this.c.l(var1) : var1;
   }

   public class_1799 p(class_1799 var1) {
      return this.U(var1);
   }

   public class_1799 C(class_1309 var1, class_1799 var2) {
      class_310 var3 = class_310.method_1551();
      return var3 != null && var1 == var3.field_1724 ? this.U(var2) : var2;
   }

   public class_1799 E(class_1542 var1, class_1799 var2) {
      class_310 var3 = class_310.method_1551();
      if (this.j && var1 != null && var3 != null && var3.field_1687 != null) {
         class_1799 var4 = this.c.Y(var3, var1);
         return var4 == var1.method_6983() ? var2 : var4;
      } else {
         return var2;
      }
   }

   public class_1799 r(class_310 var1, class_1735 var2) {
      return var1 != null
            && var1.field_1724 != null
            && var2.field_7871 != var1.field_1724.method_31548()
            && var1.field_1724.field_7512 instanceof class_1716 var3
            && var2.method_34266() >= 0
            && var2.method_34266() < 9
            && this.U(var1, var3)
         ? this.c.i(var1, var3, 0, var2.method_34266())
         : null;
   }

   public boolean e(class_310 var1, int var2, int var3, int var4, class_1713 var5) {
      if (this.G
         || var1 == null
         || var1.field_1724 == null
         || var1.field_1761 == null
         || !(
            var1.field_1724.field_7512 instanceof class_1716 var6
               && var6.field_7763 == var2
               && var3 >= 0
               && var3 < var6.field_7761.size()
               && this.U(var1, var6)
         )) {
         return false;
      }

      if (var5 == class_1713.field_7794 && var6.method_7611(var3).field_7871 == var1.field_1724.method_31548()) {
         int var18 = var6.method_7611(var3).method_34266();
         int var8 = this.c.j(var1, var18);
         if (var8 < 0 || var8 >= 9) {
            return false;
         }

         if (var6.method_34255().method_7960() && var6.method_7611(var8).method_7677().method_7960() && var6.method_7611(var3).method_7677().method_7947() == 1
            )
          {
            this.c.J(var6, var18);
            this.G = (boolean)1;

            try {
               if (var18 < 9) {
                  var1.field_1761.method_2906(var2, var8, var18, class_1713.field_7791, var1.field_1724);
               } else {
                  var1.field_1761.method_2906(var2, var3, 40, class_1713.field_7791, var1.field_1724);
                  var1.field_1761.method_2906(var2, var8, 40, class_1713.field_7791, var1.field_1724);
                  var1.field_1761.method_2906(var2, var3, 40, class_1713.field_7791, var1.field_1724);
               }
            } finally {
               this.G = (boolean)0;
            }

            this.c.I(var1);
            return true;
         } else {
            return true;
         }
      } else {
         if (var3 >= 9) {
            return false;
         }

         int var17 = this.c.U(var1, var6, 0, var3);
         if (var17 == var3) {
            return false;
         }

         this.G = (boolean)1;

         try {
            var1.field_1761.method_2906(var2, var17, var4, var5, var1.field_1724);
         } finally {
            this.G = (boolean)0;
         }

         return true;
      }
   }
}
