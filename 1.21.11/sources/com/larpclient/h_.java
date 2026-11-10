package com.larpclient;

import net.minecraft.class_11909;
import net.minecraft.class_124;
import net.minecraft.class_1277;
import net.minecraft.class_1661;
import net.minecraft.class_1707;
import net.minecraft.class_1713;
import net.minecraft.class_1735;
import net.minecraft.class_1792;
import net.minecraft.class_1799;
import net.minecraft.class_2338;
import net.minecraft.class_2561;
import net.minecraft.class_332;
import net.minecraft.class_476;
import net.minecraft.class_9930;

public class h_ extends class_476 {
   private final class_2338 z;
   private class_1799 q = class_1799.field_8037;
   private int Q = -1;
   private long S;
   private long u;
   private int Y;
   private int v;
   private boolean m;
   private static final class_1277 R = new class_1277(1);

   private void w() {
      if (!this.q.method_7960()) {
         if (this.Q < 54) {
            ((class_1707)this.field_2797).method_7611(this.Q).method_7673(this.q);
         }

         this.q = class_1799.field_8037;
         this.Q = -1;
      }
   }

   private void J(int var1) {
      this.Q = var1;
      class_1735 var2 = ((class_1707)this.field_2797).method_7611(var1);
      this.q = (var1 < 54 ? var2 : iy.x(this.field_2797, var2)).method_7677().method_7972();
      if (var1 < 54) {
         var2.method_7673(class_1799.field_8037);
      }

      ((class_1707)this.field_2797).method_34254(this.q);
      this.S = System.currentTimeMillis() + 200L;
   }

   private int S(class_1792 var1) {
      int var2 = 0;

      for (int var3 = 0; var3 < 45; var3++) {
         if (((class_1707)this.field_2797).method_7611(var3).method_7677().method_31574(var1)) {
            var2 += ((class_1707)this.field_2797).method_7611(var3).method_7677().method_7947();
         }
      }

      return var2;
   }

   public h_(class_1707 var1, class_1661 var2, class_2561 var3) {
      this(var1, var2, var3, null);
   }

   public h_(class_1707 var1, class_1661 var2, class_2561 var3, class_2338 var4) {
      super(var1, var2, var3);
      this.z = var4 == null ? null : var4.method_10062();
   }

   protected void method_62000(class_9930 var1) {
   }

   public void method_25394(class_332 var1, int var2, int var3, float var4) {
      if (!this.q.method_7960() && System.currentTimeMillis() >= this.S) {
         this.w();
      }

      if (this.m && System.currentTimeMillis() >= this.u) {
         this.m = (boolean)0;
         this.field_22787.method_1507(new fn(this.z, cz.T().w(this.z), this.Y, this.v));
      } else {
         if (this.field_22787.field_1724 != null) {
            ((class_1707)this.field_2797).method_34254(this.q.method_7960() ? this.field_22787.field_1724.field_7498.method_34255() : this.q);
         }

         super.method_25394(var1, var2, var3, var4);
         this.method_2380(var1, var2, var3);
      }
   }

   protected void method_2385(class_332 var1, class_1735 var2, int var3, int var4) {
      if (this.Q >= 54 && var2.field_7874 == this.Q && !this.q.method_7960()) {
         class_1735 var5 = new class_1735(R, 0, var2.field_7873, var2.field_7872);
         var5.field_7874 = var2.field_7874;
         super.method_2385(var1, var5, var3, var4);
      } else {
         super.method_2385(var1, var2, var3, var4);
      }
   }

   public boolean method_25402(class_11909 var1, boolean var2) {
      if (this.q.method_7960() && !this.m) {
         if (s2.m(var1) != 0 && s2.m(var1) != 1) {
            return true;
         }

         for (int var3 = 0; var3 < ((class_1707)this.field_2797).field_7761.size(); var3++) {
            class_1735 var4 = (class_1735)((class_1707)this.field_2797).field_7761.get(var3);
            int var5 = this.field_2776 + var4.field_7873;
            int var6 = this.field_2800 + var4.field_7872;
            if (!(var1.comp_4798() < var5) && !(var1.comp_4798() >= var5 + 16) && !(var1.comp_4799() < var6) && !(var1.comp_4799() >= var6 + 16)) {
               cz var7 = cz.T();
               if (var3 < 45 && qh.I(var4.method_7677().method_7909())) {
                  int var11 = !this.field_22787.field_1690.field_1832.method_1434() && !this.field_22787.field_1724.method_5715() ? 0 : 1;
                  int var12 = var11 != 0
                     ? var7.m(var4.method_7677().method_7909(), var4.method_7677().method_7947())
                     : var7.T(var4.method_7677().method_7909(), s2.m(var1) == 1 ? 1 : var4.method_7677().method_7947());
                  if (var12 > 0) {
                     var4.method_7677().method_7934(var12);
                  }
               } else if ((var3 == 48 || var3 == 50) && this.field_22787.field_1724.field_7498.method_34255().method_7960()) {
                  qh var8 = var7.w(this.z);
                  int var9 = this.S(var8.E);
                  int var10 = this.S(var8.S);
                  if (var9 + var10 == 0) {
                     this.J(var3);
                     if (var3 == 48) {
                        b1.q(class_2561.method_43470("Spawner has no loot").method_27692(class_124.field_1061), true);
                     }
                  } else if (var3 == 48) {
                     this.J(var3);
                     this.Y = var9;
                     this.v = var10;
                     this.u = this.S + 50L;
                     this.m = (boolean)1;
                  }
               } else if (var3 == 49 && this.field_22787.field_1724.field_7498.method_34255().method_7960()) {
                  this.J(var3);
               } else if (var3 < 54) {
                  var7.F(var3);
               } else if (var3 >= 54
                  && !iy.x(this.field_2797, var4).method_7677().method_7960()
                  && this.field_22787.field_1724.field_7498.method_34255().method_7960()) {
                  this.J(var3);
               }

               if (this.field_22787.field_1724 != null) {
                  ((class_1707)this.field_2797).method_34254(this.q.method_7960() ? this.field_22787.field_1724.field_7498.method_34255() : this.q);
               }
               break;
            }
         }

         return true;
      } else {
         return true;
      }
   }

   protected void method_2383(class_1735 var1, int var2, int var3, class_1713 var4) {
   }

   public void method_25419() {
      this.field_22787.method_1507(null);
      iv.h(this.z);
   }

   public void method_25432() {
      this.w();
   }
}
