package com.larpclient;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import net.minecraft.class_1268;
import net.minecraft.class_1703;
import net.minecraft.class_1713;
import net.minecraft.class_1716;
import net.minecraft.class_1735;
import net.minecraft.class_1799;
import net.minecraft.class_1802;
import net.minecraft.class_2315;
import net.minecraft.class_2338;
import net.minecraft.class_2350;
import net.minecraft.class_2401;
import net.minecraft.class_243;
import net.minecraft.class_2561;
import net.minecraft.class_2680;
import net.minecraft.class_310;
import net.minecraft.class_3917;
import net.minecraft.class_3965;
import net.minecraft.class_480;
import net.minecraft.class_9209;
import net.minecraft.class_9334;

public final class tw {
   private static final int[] m = new int[]{1, 3, 4, 5, 6, 7, 8, 9, 10};
   private static final long Z = 15000000000L;
   private static final double C = 64.0;
   private static final tw t = new tw();
   private tw.w e = tw.w.s;
   private boolean J;
   private boolean L;
   private boolean d;
   private boolean x;
   private boolean S;
   private boolean G;
   private boolean a;
   private boolean M;
   private boolean b;
   private final int[] s = new int[9];
   private final boolean[] f = new boolean[9];
   private final class_1799[] o = new class_1799[9];
   private final List<class_1799> I = new ArrayList<>();
   private class_1716 z;
   private class_2338 v;
   private class_2338 T;
   private long u;
   private long X;
   private int K;
   private int E;
   private int F = -1;
   private int i = -1;
   private tw.k g;
   private boolean p;
   private int Y = -1;
   private String P;
   private long l;
   private final int[] h = new int[11];

   public static tw s() {
      return t;
   }

   private tw() {
      Arrays.fill(this.s, -1);
      Arrays.fill(this.h, -1);
   }

   public String u(class_310 var1) {
      if (var1 != null && var1.field_1724 != null) {
         int[] var2 = new int[11];
         Arrays.fill(var2, -1);
         HashSet var3 = new HashSet();

         for (int var4 = 0; var4 < m.length; var4++) {
            int var5 = m.length - 1 - var4;
            class_1799 var6 = var1.field_1724.method_31548().method_5438(var5);
            class_9209 var7 = var6 != null && var6.method_31574(class_1802.field_8204) ? (class_9209)var6.method_58694(class_9334.field_49646) : null;
            if (var7 == null) {
               return "Visible hotbar slot " + (var4 + 1) + " must hold a filled card map.";
            }

            if (!var3.add(var7.comp_2315())) {
               return "Each hotbar slot needs a different card map.";
            }

            var2[m[var4]] = var7.comp_2315();
         }

         System.arraycopy(var2, 0, this.h, 0, var2.length);
         StringBuilder var9 = new StringBuilder("Imported hotbar map mapping:");

         for (int var8 : m) {
            var9.append(' ').append(var8 == 1 ? "A" : var8).append("=map#").append(var2[var8]);
         }

         yq.E("Blackjack FO", var9.toString());
         return null;
      } else {
         return "Join a world before importing the hotbar.";
      }
   }

   public int[] p() {
      return (int[])this.h.clone();
   }

   public void I(int[] var1) {
      Arrays.fill(this.h, -1);
      if (var1 != null) {
         System.arraycopy(var1, 0, this.h, 0, Math.min(var1.length, this.h.length));
      }
   }

   public boolean K() {
      for (int var4 : m) {
         if (this.h[var4] < 0) {
            return false;
         }
      }

      return true;
   }

   public int P(int var1) {
      return var1 >= 1 && var1 <= 10 ? this.h[var1] : -1;
   }

   public int U(int var1) {
      return E(this.h, var1);
   }

   static int E(int[] var0, int var1) {
      if (var1 >= 0 && var0 != null) {
         for (int var5 : m) {
            if (var5 < var0.length && var0[var5] == var1) {
               return var5;
            }
         }

         return -1;
      } else {
         return -1;
      }
   }

   public void u(boolean var1) {
      if (var1 && !this.J) {
         this.X = 0L;
         this.p = (boolean)0;
      }

      this.J = var1;
   }

   public boolean j() {
      return this.J;
   }

   public boolean m() {
      return this.e != tw.w.s;
   }

   public int D() {
      if (this.J && this.e != tw.w.J) {
         return this.e == tw.w.s ? this.H() : this.F;
      } else {
         return -1;
      }
   }

   public int b(int var1, class_1799 var2) {
      if (this.e != tw.w.s && this.e != tw.w.J && var2 != null && !var2.method_7960()) {
         for (int var3 = 0; var3 < this.s.length; var3++) {
            if (this.s[var3] == var1 && this.o[var3] != null && class_1799.method_31577(var2, this.o[var3])) {
               return Math.min(var2.method_7947(), this.o[var3].method_7947());
            }
         }

         return 0;
      } else {
         return 0;
      }
   }

   public boolean x() {
      return this.e != tw.w.s && this.e != tw.w.J && !this.x;
   }

   public boolean g() {
      return this.x;
   }

   public boolean E() {
      return this.x();
   }

   public boolean N(class_310 var1) {
      return var1.field_1755 instanceof class_480 var2 && var2.method_17577() == this.z;
   }

   public boolean W(class_310 var1) {
      if (this.J
         && this.z != null
         && var1.field_1724 != null
         && var1.field_1724.field_7512 == this.z
         && this.z.method_34255().method_7960()
         && var1.field_1755 == null
         && var1.field_1765 instanceof class_3965 var2
         && var2.method_17777().equals(this.L())) {
         var1.method_1507(new class_480(this.z, var1.field_1724.method_31548(), class_2561.method_43471("container.dropper")));
         return true;
      } else {
         return false;
      }
   }

   public int w(class_1703 var1, int var2) {
      return this.x && var1 == this.z ? 32768 : var2;
   }

   public class_1799 k(class_1799 var1) {
      class_310 var2 = class_310.method_1551();
      if (this.e != tw.w.s && this.e != tw.w.J && var2.field_1724 != null) {
         for (int var6 : this.s) {
            if (var6 >= 0 && var1 == var2.field_1724.method_31548().method_5438(var6)) {
               return class_1799.field_8037;
            }
         }

         return var1;
      } else {
         return var1;
      }
   }

   public class_1799 I(class_310 var1, class_1703 var2, class_1735 var3) {
      if (this.e != tw.w.s && this.e != tw.w.J && var2 == this.z && var1.field_1724 != null) {
         if (var3.field_7871 == var1.field_1724.method_31548()) {
            for (int var7 : this.s) {
               if (var7 == var3.method_34266()) {
                  return class_1799.field_8037;
               }
            }

            return null;
         } else {
            return var3.field_7874 >= 0 && var3.field_7874 < 9 && this.o[var3.field_7874] != null ? this.o[var3.field_7874] : null;
         }
      } else {
         return null;
      }
   }

   private class_2338 L() {
      return hu.f().F(hu.q.k);
   }

   public void D(class_2338 var1) {
      if (!this.x && this.J && this.e == tw.w.s && var1.equals(this.L())) {
         this.v = var1;
         this.d = (boolean)1;
         this.L = (boolean)0;
         this.u = System.nanoTime() + 3000000000L;
      }
   }

   public void c(class_1703 var1) {
      if (var1 instanceof class_1716 var2) {
         hu var3 = hu.f();
         if (this.d || var3.f() == hu.q.k && var3.z() == var2.field_7763) {
            this.z = var2;
            this.I.clear();
            this.d = this.L = this.S = (boolean)0;
         }
      }
   }

   public boolean E(class_1703 var1) {
      return this.J && this.e != tw.w.J && var1 == this.z && this.z.method_34255().method_7960();
   }

   public boolean c(class_3917<?> var1, int var2) {
      class_310 var3 = class_310.method_1551();
      if (!this.d && this.L && var1 == class_3917.field_17328 && var3.field_1724 != null) {
         this.z = new class_1716(var2, var3.field_1724.method_31548());
         var3.field_1724.field_7512 = this.z;
         this.L = this.S = (boolean)0;
         return true;
      } else {
         return false;
      }
   }

   public void s(int var1, List<class_1799> var2, class_1799 var3) {
      if (this.z != null && var1 == this.z.field_7763 && var2.size() == this.z.field_7761.size()) {
         this.I.clear();

         for (class_1799 var5 : var2) {
            this.I.add(var5.method_7972());
         }

         this.S = (boolean)1;
         this.Z(var3);
      }
   }

   public void K(int var1, int var2, class_1799 var3) {
      if (this.z != null && !this.I.isEmpty() && this.e != tw.w.J) {
         if (var1 == this.z.field_7763 && var2 >= 0 && var2 < this.I.size()) {
            this.I.set(var2, var3.method_7972());
         } else {
            if (var1 != 0 || u(var2) < 0 || var2 >= this.I.size()) {
               return;
            }

            this.I.set(var2, var3.method_7972());
         }

         this.Z(this.z.method_34255());
      }
   }

   public void t(int var1, class_1799 var2) {
      if (this.z != null && !this.I.isEmpty() && this.e != tw.w.J && var1 >= 0 && var1 < 36) {
         this.I.set(b(var1), var2.method_7972());
         this.Z(this.z.method_34255());
      }
   }

   private static int b(int var0) {
      return var0 < 9 ? 36 + var0 : var0;
   }

   static int D(int var0) {
      for (int var1 = 0; var1 < m.length; var1++) {
         if (m[var1] == var0) {
            return var1;
         }
      }

      return -1;
   }

   static boolean K(int var0, boolean var1, int var2, int var3) {
      return !var1 && var0 + var2 > 21 && var0 + var3 > 21;
   }

   static int u(int var0) {
      return var0 >= 9 && var0 < 45 ? var0 : -1;
   }

   private boolean g(boolean var1) {
      if (this.I.size() < 45) {
         return false;
      }

      for (int var2 = 0; var2 < 9; var2++) {
         if (this.s[var2] >= 0) {
            if (!class_1799.method_7973(this.I.get(var2), var1 ? class_1799.field_8037 : this.o[var2])) {
               return false;
            }

            class_1799 var3 = this.I.get(b(this.s[var2]));
            if (var1 ? !class_1799.method_7973(var3, this.o[var2]) : !var3.method_7960()) {
               return false;
            }
         }
      }

      return true;
   }

   private boolean p() {
      if (this.I.size() < 9) {
         return false;
      }

      for (int var1 = 0; var1 < 9; var1++) {
         if (this.s[var1] < 0 && this.o[var1] != null && !this.o[var1].method_7960() && !class_1799.method_7973(this.I.get(var1), this.o[var1])) {
            return true;
         }
      }

      return false;
   }

   private void Z(class_1799 var1) {
      if (var1.method_7960()) {
         if (this.e == tw.w.I && this.g(true)) {
            this.e = tw.w.m;
            if (!this.p() && !this.M && this.J && l4.K().u() == this.b) {
               yq.E("Blackjack FO", (this.b ? "Dealer" : "Player") + " draw ready");
            } else {
               this.l();
            }
         } else if (this.e != tw.w.m || !this.p() && !this.M && this.g(true)) {
            if (this.e == tw.w.H) {
               this.Z(false);
               if (this.e == tw.w.H && this.g(false)) {
                  this.e = tw.w.s;
                  this.M = (boolean)0;
                  Arrays.fill(this.s, -1);
                  yq.E("Blackjack FO", "Other cards returned after dispenser draw");
               }
            }
         } else {
            this.l();
         }
      }
   }

   static int z(boolean var0, boolean var1, boolean var2, boolean var3) {
      if (var0 && var3) {
         return 0;
      } else {
         return var1 && var2 ? 1 : -1;
      }
   }

   static boolean a(int var0, boolean var1) {
      return var0 == 1 && !var1;
   }

   static boolean i(boolean var0, boolean var1, long var2, long var4) {
      return !var0 && !var1 && var2 >= var4;
   }

   private int T(int var1) {
      if (this.I.size() < 45) {
         return -1;
      }

      class_1799 var2 = this.I.get(var1);
      class_1799 var3 = this.I.get(b(this.s[var1]));
      return z(class_1799.method_7973(var2, this.o[var1]), var2.method_7960(), class_1799.method_7973(var3, this.o[var1]), var3.method_7960());
   }

   public void d(class_2338 var1, class_2680 var2) {
      if (this.v != null) {
         if (var2.method_26204() instanceof class_2401 && var1.method_10262(this.v) <= 9.0) {
            boolean var5 = (Boolean)var2.method_11654(class_2401.field_11265);
            int var6 = !var5 || var1.equals(this.T) && this.a ? 0 : 1;
            this.T = var1.method_10062();
            this.a = var5;
            if (var6 != 0) {
               this.U("lever");
            }
         } else if (this.v.equals(var1)) {
            int var3 = var2.method_26204() instanceof class_2315 && var2.method_11654(class_2315.field_10920) ? 1 : 0;
            int var4 = var3 != 0 && !this.G ? 1 : 0;
            this.G = (boolean)var3;
            if (var4 != 0) {
               this.U("dispenser");
            }
         }
      }
   }

   private void U(String var1) {
      if (this.J && System.nanoTime() >= this.l) {
         int var2 = this.e == tw.w.s ? this.H() : -1;
         if (var2 > 0 && this.x(var2)) {
            this.p = (boolean)1;
            l4 var4 = l4.K();
            this.Y = var4.S();
            this.P = var4.h();
            yq.E(
               "Blackjack FO",
               "Started rigging " + var2 + " for " + (var4.u() ? "dealer" : "player") + " on " + var1 + ": player=" + var4.R() + " dealer=" + var4.Y()
            );
         } else if (this.e == tw.w.s && (l4.K().u() || l4.K().R() >= 12)) {
            l4 var3 = l4.K();
            yq.E(
               "Blackjack FO",
               (var3.u() ? "Dealer" : "Player")
                  + " draw unforced on "
                  + var1
                  + ": transfer="
                  + this.e
                  + " connected="
                  + this.S
                  + " slots="
                  + this.I.size()
                  + " cards="
                  + var3.S()
                  + " pending="
                  + var3.X()
                  + " player="
                  + var3.R()
                  + " dealer="
                  + var3.Y()
            );
         }
      }
   }

   private int H() {
      if (this.S && this.I.size() >= 45) {
         boolean[] var1 = new boolean[11];
         int var2 = 0;

         for (int var3 = 0; var3 < m.length; var3++) {
            class_1799 var4 = this.I.get(var3);
            class_9209 var5 = var4.method_31574(class_1802.field_8204) ? (class_9209)var4.method_58694(class_9334.field_49646) : null;
            int var6 = this.K() ? (var5 == null ? -1 : this.U(var5.comp_2315())) : m[var3];
            if (var6 >= 1 && var6 <= 10) {
               var1[var6] = (boolean)(var5 != null ? 1 : 0);
               if (var1[var6]) {
                  var2 |= 1 << var6;
               }
            }
         }

         l4 var7 = l4.K();
         if (this.p && (var7.S() != this.Y || !var7.h().equals(this.P))) {
            this.p = (boolean)0;
         }

         tw.k var8 = new tw.k(var7.h(), var7.R(), var7.Y(), var7.i(), var7.c(), var7.S(), var7.X(), var2);
         if (!var8.equals(this.g)) {
            this.g = var8;
            this.i = var7.u() ? var7.V(var1) : var7.j(var1);
         }

         return this.p ? -1 : this.i;
      } else {
         this.g = null;
         this.i = -1;
         return -1;
      }
   }

   public void x(class_2338 var1) {
      if (this.v != null && this.v.equals(var1) && (this.e == tw.w.I || this.e == tw.w.m)) {
         this.M = (boolean)1;
         if (this.e == tw.w.m) {
            this.l();
         }
      }
   }

   public void l(class_2338 var1) {
      if (this.J && this.v != null && var1.method_10262(this.v) <= 9.0) {
         this.l = System.nanoTime() + 2000000000L;
      }
   }

   static int[] P(int[] var0, boolean[] var1, boolean[] var2, int var3, int var4) {
      if (var0.length == 9 && var1.length == 9 && var2.length == 36 && var3 >= 0 && var3 < 9) {
         int var5 = -1;

         for (int var6 = 0; var6 < 9; var6++) {
            if (var1[var6] && var0[var6] == var4) {
               if (var5 >= 0) {
                  return null;
               }

               var5 = var6;
            }
         }

         if (var5 < 0) {
            return null;
         }

         int[] var11 = new int[9];
         Arrays.fill(var11, -1);
         var11[var5] = -2;
         boolean[] var7 = (boolean[])var2.clone();
         var7[var3] = (boolean)1;

         for (int var8 = 0; var8 < 9; var8++) {
            if (var1[var8] && var8 != var5) {
               int var9 = -1;

               for (int var10 = 0; var10 < 9; var10++) {
                  if (!var7[var10]) {
                     var9 = var10;
                     break;
                  }
               }

               if (var9 < 0) {
                  for (int var12 = 35; var12 >= 9; var12--) {
                     if (!var7[var12]) {
                        var9 = var12;
                        break;
                     }
                  }
               }

               if (var9 < 0) {
                  return null;
               }

               var11[var8] = var9;
               var7[var9] = (boolean)1;
            }
         }

         return var11;
      } else {
         return null;
      }
   }

   private boolean x(int var1) {
      class_310 var2 = class_310.method_1551();
      if (this.S
         && this.z != null
         && var2.field_1724 != null
         && var2.field_1724.field_7512 == this.z
         && this.z.method_34255().method_7960()
         && this.I.size() >= 45) {
         int var3 = this.J(var1);
         class_9209 var4 = var3 < 0 ? null : (class_9209)this.I.get(var3).method_58694(class_9334.field_49646);
         int var5 = var4 == null ? -1 : var4.comp_2315();
         if (var5 < 0) {
            yq.E("Blackjack FO", "Skipped draw: no card template for rank " + var1);
            return false;
         }

         int[] var6 = new int[9];
         boolean[] var7 = new boolean[9];
         boolean[] var8 = new boolean[36];
         l4 var9 = l4.K();
         this.b = var9.u();
         int var10 = var9.Y();
         boolean var11 = var9.c();

         for (int var12 = 0; var12 < 9; var12++) {
            class_1799 var13 = this.I.get(var12);
            class_9209 var14 = var13.method_31574(class_1802.field_8204) ? (class_9209)var13.method_58694(class_9334.field_49646) : null;
            var6[var12] = var14 == null ? -1 : var14.comp_2315();
            int var15 = this.K() ? (var14 == null ? -1 : this.U(var14.comp_2315())) : m[var12];
            var7[var12] = (boolean)(var13.method_7960()
                  || var12 != var3 && var14 != null && var15 >= 1 && (this.b ? K(var10, var11, var1, var15) : var9.l(var1, var15))
               ? 0
               : 1);
         }

         for (int var21 = 0; var21 < 36; var21++) {
            var8[var21] = (boolean)(this.I.get(b(var21)).method_7960() && var2.field_1724.method_31548().method_5438(var21).method_7960() ? 0 : 1);
         }

         int[] var22 = P(var6, var7, var8, var2.field_1724.method_31548().method_67532(), var5);
         if (var22 == null) {
            yq.E("Blackjack FO", "Skipped draw: card missing/duplicated or not enough empty inventory slots");
            return false;
         }

         Arrays.fill(this.s, -1);
         Arrays.fill(this.f, false);

         for (int var23 = 0; var23 < 9; var23++) {
            this.o[var23] = this.I.get(var23).method_7972();
            this.s[var23] = var22[var23] >= 0 ? var22[var23] : -1;
         }

         this.e = tw.w.I;
         this.F = var1;
         this.M = (boolean)0;
         this.K = 0;
         this.E = 0;
         this.u = System.nanoTime() + 15000000000L;
         this.x = (boolean)1;

         try {
            for (int var24 = 0; var24 < 9; var24++) {
               if (this.s[var24] >= 0) {
                  this.U(var24, this.s[var24], false, var2);
               }
            }
         } catch (RuntimeException var19) {
            this.A("removal click failed");
            return false;
         } finally {
            this.x = (boolean)0;
         }

         return true;
      } else {
         yq.E("Blackjack FO", "Skipped draw: dispenser not ready");
         return false;
      }
   }

   private int J(int var1) {
      if (!this.K()) {
         return D(var1);
      }

      int var2 = this.P(var1);
      if (var2 >= 0 && this.I.size() >= 9) {
         int var3 = -1;

         for (int var4 = 0; var4 < 9; var4++) {
            class_9209 var5 = this.I.get(var4).method_31574(class_1802.field_8204) ? (class_9209)this.I.get(var4).method_58694(class_9334.field_49646) : null;
            if (var5 != null && var5.comp_2315() == var2) {
               if (var3 >= 0) {
                  return -1;
               }

               var3 = var4;
            }
         }

         return var3;
      } else {
         return -1;
      }
   }

   private void l() {
      class_310 var1 = class_310.method_1551();
      if (this.e != tw.w.J) {
         if (var1.field_1724 != null && var1.field_1724.field_7512 == this.z && this.z.method_34255().method_7960() && this.I.size() >= 45) {
            this.e = tw.w.H;
            this.u = System.nanoTime() + 15000000000L;
            Arrays.fill(this.f, false);
            this.Z(true);
            this.Z(this.z.method_34255());
         } else {
            this.A("return state changed");
         }
      }
   }

   private void Z(boolean var1) {
      class_310 var2 = class_310.method_1551();
      if (var2.field_1724 != null && var2.field_1724.field_7512 == this.z && this.I.size() >= 45) {
         StringBuilder var3 = new StringBuilder();
         this.x = (boolean)1;

         try {
            int var4 = 0;

            for (int var5 = 0; var5 < 9; var5++) {
               if (this.s[var5] >= 0) {
                  int var6 = this.T(var5);
                  if (var6 == 0) {
                     var4++;
                  } else {
                     if (var4 > this.E) {
                        this.E = var4;
                        this.K = 0;
                        this.u = System.nanoTime() + 15000000000L;
                     }

                     if (var6 < 0) {
                        if (var1) {
                           var3.append(' ').append(var5 + 1).append('[').append(this.I.get(var5)).append(" / ").append(this.I.get(b(this.s[var5]))).append(']');
                        }
                     } else if (a(var6, this.f[var5])) {
                        this.U(var5, this.s[var5], true, var2);
                        this.f[var5] = (boolean)1;
                     }
                  }
               }
            }
         } catch (RuntimeException var10) {
            this.A("return click failed");
         } finally {
            this.x = (boolean)0;
         }

         if (!var3.isEmpty()) {
            yq.E("Blackjack FO", "Waiting for changed slots:" + var3);
         }
      }
   }

   private void U(int var1, int var2, boolean var3, class_310 var4) {
      if (var2 < 9) {
         var4.field_1761.method_2906(this.z.field_7763, var1, var2, class_1713.field_7791, var4.field_1724);
      } else {
         int var5 = var3 ? b(var2) : var1;
         int var6 = var3 ? var1 : b(var2);
         var4.field_1761.method_2906(this.z.field_7763, var5, 0, class_1713.field_7790, var4.field_1724);
         var4.field_1761.method_2906(this.z.field_7763, var6, 0, class_1713.field_7790, var4.field_1724);
      }
   }

   private void A(String var1) {
      this.e = tw.w.J;
      this.J = (boolean)0;
      yq.E("Blackjack FO", "Stopped: " + var1 + "; inspect real slots, no automatic replay");
      class_310 var2 = class_310.method_1551();
      if (var2.field_1724 != null && var2.field_1724.field_7512 == this.z) {
         var2.method_1507(new class_480(this.z, var2.field_1724.method_31548(), class_2561.method_43470("Blackjack FO recovery")));
      }
   }

   public void L(class_310 var1) {
      if (var1.field_1724 == null || var1.field_1687 == null) {
         this.z = null;
         this.e = tw.w.s;
         this.L = this.d = this.S = this.G = this.M = (boolean)0;
         this.a = (boolean)0;
         this.T = null;
         Arrays.fill(this.s, -1);
      } else if (this.e == tw.w.J) {
         if (var1.field_1724.field_7512 != this.z) {
            this.e = tw.w.s;
            this.z = null;
            this.S = (boolean)0;
         }
      } else if (this.e != tw.w.s) {
         if (var1.field_1724.field_7512 != this.z) {
            this.A("container closed during transfer");
         } else {
            if (this.e != tw.w.m || this.J && l4.K().u() == this.b && (this.v == null || !(var1.field_1724.method_5707(class_243.method_24953(this.v)) > 64.0))
               )
             {
               if (this.e != tw.w.m && System.nanoTime() > this.u) {
                  if (this.e == tw.w.I || this.e == tw.w.H && this.K++ == 0) {
                     yq.E("Blackjack FO", "Returning parked cards after " + this.e + " confirmation timeout");
                     if (this.e == tw.w.I && this.M && this.I.size() >= 45) {
                        for (int var7 = 0; var7 < 9; var7++) {
                           if (this.s[var7] >= 0 && this.I.get(var7).method_7960() && this.I.get(b(this.s[var7])).method_7960()) {
                              this.s[var7] = -1;
                           }
                        }
                     }

                     this.l();
                  } else {
                     this.A("server confirmation timed out in " + this.e);
                  }
               }
            } else {
               this.l();
            }
         }
      } else {
         if (this.z != null && var1.field_1724.field_7512 != this.z) {
            this.z = null;
            this.S = (boolean)0;
            this.I.clear();
            this.X = 0L;
         }

         if (!this.J) {
            if (this.S && this.z != null && var1.field_1755 == null) {
               var1.field_1724.method_7346();
            }

            this.L = (boolean)0;
         } else {
            long var2 = System.nanoTime();
            if (!this.d || var2 >= this.u) {
               this.d = (boolean)0;
               if (this.z != null && i(this.S, var1.field_1755 != null, var2, this.u)) {
                  var1.field_1724.method_7346();
                  this.z = null;
                  this.I.clear();
                  this.L = (boolean)0;
                  this.X = 0L;
                  yq.E("Blackjack FO", "Dispenser opening timed out; reconnecting");
               }

               if (!this.S) {
                  if (var1.field_1755 == null && var1.field_1724.field_7512 == var1.field_1724.field_7498 && var2 >= this.X) {
                     this.v = this.L();
                     if (this.v != null && !(var1.field_1724.method_5707(class_243.method_24953(this.v)) > 64.0)) {
                        this.L = (boolean)1;
                        this.u = System.nanoTime() + 3000000000L;
                        this.X = System.nanoTime() + 3000000000L;
                        this.x = (boolean)1;

                        try {
                           var1.field_1761
                              .method_2896(
                                 var1.field_1724, class_1268.field_5808, new class_3965(class_243.method_24953(this.v), class_2350.field_11036, this.v, false)
                              );
                        } finally {
                           this.x = (boolean)0;
                        }
                     }
                  }
               }
            }
         }
      }
   }

   private record k(String side, int playerTotal, int dealerTotal, boolean playerSoft, boolean dealerSoft, int dealtCards, int pending, int availableMask) {
   }

   private enum w {
      s,
      I,
      m,
      H,
      J;
   }
}
