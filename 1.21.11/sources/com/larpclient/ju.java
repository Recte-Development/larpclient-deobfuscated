package com.larpclient;

import java.util.List;
import net.minecraft.class_1268;
import net.minecraft.class_1703;
import net.minecraft.class_1713;
import net.minecraft.class_1716;
import net.minecraft.class_1735;
import net.minecraft.class_1792;
import net.minecraft.class_1799;
import net.minecraft.class_1802;
import net.minecraft.class_2246;
import net.minecraft.class_2315;
import net.minecraft.class_2338;
import net.minecraft.class_2350;
import net.minecraft.class_2371;
import net.minecraft.class_2401;
import net.minecraft.class_243;
import net.minecraft.class_2561;
import net.minecraft.class_2680;
import net.minecraft.class_310;
import net.minecraft.class_3917;
import net.minecraft.class_3965;
import net.minecraft.class_480;

public final class ju {
   private static final ju L = new ju();
   private boolean U;
   private boolean k;
   private boolean D;
   private boolean j;
   private boolean Y;
   private boolean y;
   private boolean u;
   public class_1792 H = class_1802.field_8279;
   public class_1792 W = class_1802.field_8477;
   public boolean X = (boolean)1;
   private int b = -1;
   private int r = -1;
   private int O = -1;
   private class_1716 v;
   private class_2338 K;
   private final class_1799[] P = new class_1799[]{class_1799.field_8037, class_1799.field_8037};
   private final int[] l = new int[]{-1, -1};
   private class_1799 d = class_1799.field_8037;
   private class_1799 g = class_1799.field_8037;
   private int s;
   private ju.a w = ju.a.p;
   private long V;
   private long R;

   public static ju b() {
      return L;
   }

   public boolean U() {
      return this.U;
   }

   public boolean g() {
      return this.w != ju.a.p;
   }

   public int K() {
      return this.X ? 1 : 2;
   }

   public class_1799 q(class_1799 var1) {
      class_310 var2 = class_310.method_1551();
      return this.b >= 0 && this.g() && var2.field_1724 != null && var1 == var2.field_1724.method_31548().method_5438(this.b) ? class_1799.field_8037 : var1;
   }

   public class_1799 Y(class_310 var1, class_1703 var2, class_1735 var3) {
      if (this.w != ju.a.H && this.g() && var2 == this.v && this.a()) {
         if (var3.field_7871 == var1.field_1724.method_31548()) {
            return var3.method_34266() == this.b ? class_1799.field_8037 : null;
         } else {
            return var3.field_7874 == this.r && this.v.method_7611(this.r) == var3 ? this.d : null;
         }
      } else {
         return null;
      }
   }

   public String h(int var1) {
      return this.P[var1].method_7960() ? "ITEM " + (var1 + 1) : this.P[var1].method_7964().getString();
   }

   public boolean a() {
      class_310 var1 = class_310.method_1551();
      return this.j && this.v != null && var1.field_1724 != null && var1.field_1724.field_7512 == this.v;
   }

   public void k(int var1) {
      if ((!this.g() || this.w == ju.a.g) && var1 >= 1 && var1 <= 2) {
         this.X = (boolean)(var1 == 1 ? 1 : 0);
      }
   }

   public void C(boolean var1) {
      this.U = var1;
   }

   private class_2338 r() {
      return hu.f().F(hu.q.A);
   }

   public void o(class_310 var1, class_1716 var2, class_2338 var3) {
      if (!this.g() && var3 != null) {
         this.v = var2;
         this.K = var3.method_10062();
         this.j = (boolean)0;
      }
   }

   public boolean w(class_3917<?> var1, int var2) {
      class_310 var3 = class_310.method_1551();
      if (this.k && var1 == class_3917.field_17328 && var3.field_1724 != null) {
         this.v = new class_1716(var2, var3.field_1724.method_31548());
         var3.field_1724.field_7512 = this.v;
         this.k = (boolean)0;
         this.j = (boolean)0;
         return true;
      } else {
         return false;
      }
   }

   static int[] q(List<class_1799> var0) {
      int var1 = -1;
      int var2 = -1;

      for (int var3 = 0; var3 < 9; var3++) {
         if (!((class_1799)var0.get(var3)).method_7960()) {
            if (((class_1799)var0.get(var3)).method_7947() != 1) {
               return null;
            }

            if (var1 < 0) {
               var1 = var3;
            } else {
               if (var2 >= 0) {
                  return null;
               }

               var2 = var3;
            }
         }
      }

      return var2 >= 0 && !((class_1799)var0.get(var1)).method_31574(((class_1799)var0.get(var2)).method_7909()) ? new int[]{var1, var2} : null;
   }

   public void k(int var1, List<class_1799> var2, class_1799 var3) {
      if (this.v != null && var1 == this.v.field_7763 && var2.size() == this.v.field_7761.size()) {
         this.j = (boolean)1;
         if (this.w == ju.a.p) {
            int[] var4 = q(var2);
            if (var4 != null) {
               for (int var5 = 0; var5 < 2; var5++) {
                  this.l[var5] = var4[var5];
                  this.P[var5] = ((class_1799)var2.get(var4[var5])).method_7972();
               }

               this.H = this.P[0].method_7909();
               this.W = this.P[1].method_7909();
            }
         }

         this.u(var2, var3);
      }
   }

   public void d(int var1, int var2, class_1799 var3) {
      if (this.v != null && this.g() && this.w != ju.a.H) {
         if (var1 == this.v.field_7763 || var1 == 0 && var2 == 36 + this.b) {
            if (this.w == ju.a.N || this.w == ju.a.J) {
               if (var2 == this.r) {
                  this.y = this.w == ju.a.N ? var3.method_7960() : class_1799.method_7973(var3, this.d);
               }

               if (var2 == 36 + this.b) {
                  this.u = this.w == ju.a.N ? class_1799.method_7973(var3, this.d) : var3.method_7960();
               }

               if (this.y && this.u && this.v.method_34255().method_7960()) {
                  if (this.w == ju.a.N) {
                     this.O(!class_1799.method_7973(this.v.method_7611(this.O).method_7677(), this.g));
                  } else {
                     this.e();
                  }
               }
            }

            if (this.w == ju.a.g && var2 == this.O && !class_1799.method_7973(var3, this.g)) {
               this.Z();
            }
         }
      }
   }

   public void b(int var1, class_1799 var2) {
      if (this.v != null && var1 == this.b) {
         this.d(this.v.field_7763, 36 + var1, var2);
      }
   }

   private void O(boolean var1) {
      this.w = ju.a.g;
      this.V = System.nanoTime() + 15000000000L;
      yq.E("50/50", "Trigger swap confirmed: " + this.g.method_7964().getString());
      if (!this.U || this.K() != this.s || var1) {
         this.Z();
      }
   }

   private void e() {
      this.w = ju.a.p;
      this.d = class_1799.field_8037;
      this.b = -1;
      this.g = class_1799.field_8037;
      yq.E("50/50", "Returned held item; dispenser ready");
   }

   private void u(List<class_1799> var1, class_1799 var2) {
      if (this.g() && this.w != ju.a.H && this.b >= 0 && var2.method_7960()) {
         int var3 = 36 + this.b;
         if (this.w == ju.a.N && ((class_1799)var1.get(this.r)).method_7960() && class_1799.method_7973((class_1799)var1.get(var3), this.d)) {
            this.O(!class_1799.method_7973((class_1799)var1.get(this.O), this.g));
         } else if (this.w == ju.a.N && ((class_1799)var1.get(var3)).method_7960()) {
            this.w = ju.a.p;
            this.b = -1;
            this.d = class_1799.field_8037;
            this.g = class_1799.field_8037;
            yq.E("50/50", "Missed trigger: removal not accepted; no replay");
         } else if (this.w == ju.a.g && !class_1799.method_7973((class_1799)var1.get(this.O), this.g)) {
            this.Z();
         } else if (this.w == ju.a.J && class_1799.method_7973((class_1799)var1.get(this.r), this.d) && ((class_1799)var1.get(var3)).method_7960()) {
            this.e();
         }
      }
   }

   private void Z() {
      class_310 var1 = class_310.method_1551();
      if (this.a()
         && this.v.method_7611(this.r).method_7677().method_7960()
         && class_1799.method_7973(var1.field_1724.method_31548().method_5438(this.b), this.d)) {
         this.w = ju.a.J;
         this.V = System.nanoTime() + 15000000000L;
         this.o(this.r, this.b);
      } else {
         this.z("return state changed");
      }
   }

   private void o(int var1, int var2) {
      this.y = this.u = (boolean)0;
      class_310 var3 = class_310.method_1551();
      this.D = (boolean)1;

      try {
         var3.field_1761.method_2906(this.v.field_7763, var1, var2, class_1713.field_7791, var3.field_1724);
      } finally {
         this.D = (boolean)0;
      }
   }

   public int q(class_1703 var1, int var2) {
      return this.D && var1 == this.v ? 32768 : var2;
   }

   public boolean S() {
      return this.g() && !this.D && this.w != ju.a.H;
   }

   public boolean r() {
      return this.w == ju.a.g;
   }

   public boolean B() {
      if (!this.S()) {
         return false;
      }

      class_310 var1 = class_310.method_1551();
      return !this.r()
         || !(
            var1.field_1765 instanceof class_3965 var2
               && var1.field_1687.method_8320(var2.method_17777()).method_27852(class_2246.field_10363)
               && this.K != null
               && !(var2.method_17777().method_10262(this.K) > 9.0)
         );
   }

   public boolean X(class_310 var1, class_1268 var2, class_3965 var3) {
      if (this.D) {
         return false;
      }

      if (this.g()) {
         return this.w != ju.a.H;
      }

      if (this.U
         && this.a()
         && var2 == class_1268.field_5808
         && var1.field_1687 != null
         && var1.field_1687.method_8320(var3.method_17777()).method_27852(class_2246.field_10363)
         && !(Boolean)var1.field_1687.method_8320(var3.method_17777()).method_11654(class_2401.field_11265)
         && this.K != null
         && var3.method_17777().method_10262(this.K) <= 9.0) {
         this.J(var1);
      }

      return false;
   }

   public void H(class_2338 var1, class_2680 var2) {
      if (this.K != null && this.K.equals(var1)) {
         int var3 = var2.method_28498(class_2315.field_10920) && var2.method_11654(class_2315.field_10920) ? 1 : 0;
         int var4 = var3 != 0 && !this.Y ? 1 : 0;
         this.Y = (boolean)var3;
         if (var4 != 0 && this.U && this.a() && !this.g()) {
            this.J(class_310.method_1551());
         }
      }
   }

   private void J(class_310 var1) {
      class_2371 var2 = this.v.method_7602();
      if (q(var2) != null
         && this.l[0] >= 0
         && class_1799.method_7973((class_1799)var2.get(this.l[0]), this.P[0])
         && class_1799.method_7973((class_1799)var2.get(this.l[1]), this.P[1])
         && this.v.method_34255().method_7960()) {
         this.b = -1;

         for (int var3 = 0; var3 < 9; var3++) {
            if (var1.field_1724.method_31548().method_5438(var3).method_7960() && var3 != var1.field_1724.method_31548().method_67532()) {
               this.b = var3;
               break;
            }
         }

         if (this.b >= 0) {
            this.s = this.K();
            this.O = this.l[this.s - 1];
            this.r = this.l[2 - this.s];
            this.g = ((class_1799)var2.get(this.O)).method_7972();
            this.d = ((class_1799)var2.get(this.r)).method_7972();
            this.w = ju.a.N;
            this.V = System.nanoTime() + 15000000000L;
            this.o(this.r, this.b);
         }
      }
   }

   private void z(String var1) {
      this.w = ju.a.H;
      this.U = (boolean)0;
      this.b = -1;
      yq.E("50/50", "Stopped: " + var1 + "; held item remains real, no replay");
      class_310 var2 = class_310.method_1551();
      if (var2.field_1724 != null && var2.field_1724.field_7512 == this.v) {
         var2.method_1507(new class_480(this.v, var2.field_1724.method_31548(), class_2561.method_43470("50/50 recovery")));
      }
   }

   public void q(class_310 var1) {
      if (var1.field_1724 == null || var1.field_1687 == null) {
         this.v = null;
         this.w = ju.a.p;
         this.k = this.j = this.Y = (boolean)0;
         this.b = -1;
      } else if (this.w == ju.a.H) {
         if (var1.field_1724.field_7512 != this.v) {
            this.w = ju.a.p;
            this.v = null;
            this.j = (boolean)0;
         }
      } else if (this.g()) {
         if (var1.field_1724.field_7512 != this.v) {
            this.z("container closed during transfer");
         } else {
            if (this.r()) {
               if (!this.U || this.K() != this.s || System.nanoTime() > this.V) {
                  this.Z();
               }
            } else if (System.nanoTime() > this.V) {
               this.z("server confirmation timed out");
            }
         }
      } else {
         if (this.v != null && var1.field_1724.field_7512 != this.v) {
            this.v = null;
            this.j = (boolean)0;
         }

         if (!this.U) {
            if (this.a() && var1.field_1755 == null) {
               var1.field_1724.method_7346();
            }

            this.k = (boolean)0;
         } else if (!this.a()) {
            if ((!this.k || System.nanoTime() >= this.V) && var1.field_1755 == null && var1.field_1724.field_7512 == var1.field_1724.field_7498) {
               if (System.nanoTime() >= this.R) {
                  this.K = this.r();
                  if (this.K != null && !(var1.field_1724.method_5707(class_243.method_24953(this.K)) > 16.0)) {
                     this.k = (boolean)1;
                     this.j = (boolean)0;
                     this.V = System.nanoTime() + 5000000000L;
                     this.R = System.nanoTime() + 5000000000L;
                     this.D = (boolean)1;

                     try {
                        var1.field_1761
                           .method_2896(
                              var1.field_1724, class_1268.field_5808, new class_3965(class_243.method_24953(this.K), class_2350.field_11036, this.K, false)
                           );
                     } finally {
                        this.D = (boolean)0;
                     }
                  }
               }
            }
         }
      }
   }

   private enum a {
      p,
      N,
      g,
      J,
      H;
   }
}
