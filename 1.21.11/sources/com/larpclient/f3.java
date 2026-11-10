package com.larpclient;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.WeakHashMap;
import java.util.Map.Entry;
import java.util.concurrent.ThreadLocalRandom;
import net.minecraft.class_1297;
import net.minecraft.class_1542;
import net.minecraft.class_1716;
import net.minecraft.class_1799;
import net.minecraft.class_2246;
import net.minecraft.class_2315;
import net.minecraft.class_2338;
import net.minecraft.class_2350;
import net.minecraft.class_243;
import net.minecraft.class_2680;
import net.minecraft.class_310;
import net.minecraft.class_638;

final class f3 {
   final class_2338[] b = new class_2338[2];
   private final class_1799[][] E = new class_1799[2][10];
   private final int[][] D = new int[2][9];
   private final int[] U = new int[]{0, 0};
   private final int[] o = new int[]{0, 0};
   private final boolean[] W = new boolean[]{false, false};
   private final boolean[] N = new boolean[]{false, false};
   private final int[] B = new int[]{-1, -1};
   private final int[] p = new int[]{-1, -1};
   private final Set<UUID> j = new HashSet<>();
   private final Set<UUID> P = new HashSet<>();
   private final Set<Integer> O = new HashSet<>();
   private final yo u = new yo();
   private final List<Map<Integer, Integer>> v = List.of(new HashMap<>(), new HashMap<>());
   private class_1716 R;
   private int d = -1;
   private final Map<Integer, Integer> r = new HashMap<>();
   private final Map<Integer, Integer> q = new HashMap<>();
   boolean T = (boolean)1;
   private boolean V = (boolean)1;
   private boolean s;
   private final Set<class_1799> y = Collections.newSetFromMap(new WeakHashMap<>());
   private class_638 c;

   f3() {
      for (class_1799[] var4 : this.E) {
         Arrays.fill(var4, class_1799.field_8037);
      }
   }

   void t(class_310 var1, t3 var2, class_2338 var3, class_2338 var4) {
      int var5 = this.c == var1.field_1687 && this.V == var2.C && Objects.equals(this.b[0], var3) && Objects.equals(this.b[1], var4) ? 0 : 1;
      this.V = var2.C;
      this.b[0] = var3;
      this.b[1] = var4;
      this.c = var1.field_1687;
      this.T = (boolean)(var2.Y() == t3.v.d ? 1 : 0);

      for (int var6 = 0; var6 < 2; var6++) {
         for (int var7 = 1; var7 <= 9; var7++) {
            this.E[var6][var7] = var2.s(var6 == 0, var7 - 1);
            if (this.D[var6][var7 - 1] == 0) {
               this.D[var6][var7 - 1] = var7;
            }
         }
      }

      if (var5 != 0 || !this.s) {
         this.t(var1);
         this.s = (boolean)1;
      }
   }

   boolean d() {
      return this.U[0] > 0 || this.U[1] > 0;
   }

   void R(int var1, int var2, int var3) {
      if (this.U[var1] == 0 && var2 >= 0 && var2 < 9 && var3 >= 1 && var3 <= 9) {
         this.D[var1][var2] = var3;
      }
   }

   boolean w(int var1) {
      return this.b[var1] != null;
   }

   boolean D() {
      return this.w(0) && this.w(1);
   }

   private int T(class_1799 var1) {
      if (var1.method_7960()) {
         return -1;
      }

      String var2 = var1.method_7964().getString();
      int var3 = u4.r(var2);
      if (var3 < 1) {
         int var8 = -1;

         for (int var9 = 0; var9 < 2; var9++) {
            for (int var6 = 1; var6 <= 9; var6++) {
               class_1799 var7 = this.E[var9][var6];
               if (this.w(var9) && var7 != null && !var7.method_7960() && var1.method_31574(var7.method_7909()) && var2.equals(var7.method_7964().getString())) {
                  if (var8 >= 0) {
                     return -1;
                  }

                  var8 = var9 * 10 + var6;
               }
            }
         }

         return var8;
      } else {
         int var4 = u4.r(var2) ? 0 : 1;
         class_1799 var5 = this.E[var4][var3];
         return this.w(var4) && var5 != null && !var5.method_7960() && var1.method_31574(var5.method_7909()) ? var4 * 10 + var3 : -1;
      }
   }

   private Map<Integer, Integer> h(class_310 var1) {
      if (var1.field_1724.field_7512 instanceof class_1716 var2 && var2 == this.R) {
         this.y(var2, this.d);
      }

      HashMap var11 = new HashMap();

      for (Map var4 : this.v) {
         var4.forEach((var1x, var2x) -> var11.merge(var1x, var2x, Integer::sum));
      }

      for (int var13 = 0; var13 < 36; var13++) {
         class_1799 var15 = var1.field_1724.method_31548().method_5438(var13);
         int var5 = this.T(var15);
         if (var5 >= 0) {
            var11.merge(var5, var15.method_7947(), Integer::sum);
         }
      }

      class_1799 var14 = var1.field_1724.field_7512 == null ? class_1799.field_8037 : var1.field_1724.field_7512.method_34255();
      int var16 = this.T(var14);
      if (var16 >= 0) {
         var11.merge(var16, var14.method_7947(), Integer::sum);
      }

      class_1799 var17 = var1.field_1724.method_6079();
      int var6 = this.T(var17);
      if (var6 >= 0) {
         var11.merge(var6, var17.method_7947(), Integer::sum);
      }

      if (var1 != null && var1.field_1687 != null) {
         for (class_1297 var9 : var1.field_1687.method_18112()) {
            if (var9 instanceof class_1542 var7 && !var7.method_31481() && this.P.contains(var7.method_5667())) {
               int var10 = this.T(var7.method_6983());
               if (var10 >= 0) {
                  var11.merge(var10, var7.method_6983().method_7947(), Integer::sum);
               }
            }
         }
      }

      return var11;
   }

   private void y(class_1716 var1, int var2) {
      Map var3 = this.v.get(var2);
      var3.clear();

      for (int var4 = 0; var4 < 9; var4++) {
         class_1799 var5 = var1.method_7611(var4).method_7677();
         int var6 = this.T(var5);
         if (y6.J(var2, var6 < 0 ? -1 : var6 / 10, this.A(var2, var4, var5))) {
            var3.merge(var6, var5.method_7947(), Integer::sum);
         }
      }
   }

   public void K(class_1716 var1, int var2, boolean var3) {
      if (var3 || this.R == var1) {
         this.R = var1;
         this.d = var2;
         this.y(var1, var2);
      }
   }

   private void x(class_310 var1) {
      if (var1.field_1687 != null) {
         for (class_1297 var3 : var1.field_1687.method_18112()) {
            if (var3 instanceof class_1542 var4 && !var4.method_31481() && !this.j.contains(var4.method_5667())) {
               int var5 = this.T(var4.method_6983());
               if (var5 >= 0) {
                  this.j.add(var4.method_5667());
                  class_2338 var6 = this.b[var5 / 10];
                  if (var6 != null) {
                     class_2680 var7 = var1.field_1687.method_8320(var6);
                     if (var7.method_27852(class_2246.field_10228) || var7.method_27852(class_2246.field_10200)) {
                        class_2350 var8 = (class_2350)var7.method_11654(class_2315.field_10918);
                        class_243 var9 = class_243.method_24953(var6)
                           .method_1031(var8.method_10148() * 0.7, var8.method_10164() * 0.7, var8.method_10165() * 0.7);
                        if (var4.method_5707(var9) <= 2.25) {
                           this.P.add(var4.method_5667());
                           this.O.add(var5);
                        }
                     }
                  }
               }
            }
         }
      }
   }

   public class_1799 e(class_310 var1, class_1542 var2) {
      this.B(var1);
      return this.P.contains(var2.method_5667()) ? this.G(var2.method_6983()) : var2.method_6983();
   }

   public void t(class_310 var1) {
      Arrays.fill(this.U, 0);
      Arrays.fill(this.o, 0);
      Arrays.fill(this.N, false);
      this.u.o();

      for (Map var3 : this.v) {
         var3.clear();
      }

      this.R = null;
      this.d = -1;
      Arrays.fill(this.W, false);
      Arrays.fill(this.B, -1);
      Arrays.fill(this.p, -1);
      this.P.clear();
      this.j.clear();
      this.O.clear();
      if (var1 != null && var1.field_1687 != null) {
         for (class_1297 var5 : var1.field_1687.method_18112()) {
            this.j.add(var5.method_5667());
         }
      }

      this.r.clear();
      if (var1 != null && var1.field_1724 != null) {
         this.r.putAll(this.h(var1));
      }

      this.q.clear();
      this.q.putAll(this.r);
   }

   public void B(class_310 var1) {
      if (this.D() && var1 != null && var1.field_1724 != null && var1.field_1687 != null) {
         this.x(var1);
         this.k(var1, this.h(var1), var1.field_1687.method_75260());
      }
   }

   void k(class_310 var1, Map<Integer, Integer> var2, long var3) {
      if (this.u
         .n(
            this.U[0] > 0 && this.U[1] > 0,
            var2.getOrDefault(this.U[0], 0) <= this.q.getOrDefault(this.U[0], 0)
               && var2.getOrDefault(10 + this.U[1], 0) <= this.q.getOrDefault(10 + this.U[1], 0)
               && (!this.W[0] || var2.getOrDefault(this.o[0], 0) <= this.q.getOrDefault(this.o[0], 0))
               && (!this.W[1] || var2.getOrDefault(10 + this.o[1], 0) <= this.q.getOrDefault(10 + this.o[1], 0)),
            var3
         )) {
         this.t(var1);
      } else {
         for (Entry var6 : var2.entrySet()) {
            if (this.O.contains(var6.getKey()) && (Integer)var6.getValue() > this.r.getOrDefault(var6.getKey(), 0)) {
               int var7 = (Integer)var6.getKey() / 10;
               int var8 = (Integer)var6.getKey() % 10;
               if (this.U[var7] == 0) {
                  this.U[var7] = var8;
               }
            }
         }

         if (!this.V) {
            System.arraycopy(this.U, 0, this.o, 0, 2);
         }

         if (this.V && this.o[0] == 0 && (this.U[0] > 0 || this.U[1] > 0)) {
            ThreadLocalRandom var10 = ThreadLocalRandom.current();
            u4.d var12 = u4.I(this.U[0] > 0 ? this.U[0] : var10.nextInt(1, 10), this.U[1] > 0 ? this.U[1] : var10.nextInt(1, 10), this.T, var10.nextInt());
            ArrayList var13 = new ArrayList();

            for (int var14 = 1; var14 <= 9; var14++) {
               for (int var9 = 1; var9 <= 9; var9++) {
                  if ((this.T ? var14 > var9 : var9 > var14) && !this.E[0][var14].method_7960() && !this.E[1][var9].method_7960()) {
                     var13.add(new u4.d(var14, var9));
                  }
               }
            }

            if (this.E[0][var12.host()].method_7960() || this.E[1][var12.viewer()].method_7960()) {
               if (var13.isEmpty()) {
                  return;
               }

               List var15 = var13.stream().filter(var1x -> var1x.host() == this.U[0] || var1x.viewer() == this.U[1]).toList();
               List var16 = var15.isEmpty() ? var13 : var15;
               var12 = (u4.d)var16.get(var10.nextInt(var16.size()));
            }

            this.o[0] = var12.host();
            this.o[1] = var12.viewer();
         }

         for (int var11 = 0; var11 < 2; var11++) {
            if (!this.N[var11] && this.U[var11] > 0 && this.o[var11] > 0) {
               this.N[var11] = (boolean)1;
               yq.E("Paper", (var11 == 0 ? "Host: " : "Viewer: ") + yq.R(Integer.toString(this.U[var11]), Integer.toString(this.o[var11]), this.V, true));
            }
         }

         this.r.clear();
         this.r.putAll(var2);
      }
   }

   public class_1799 G(class_1799 var1) {
      if (this.y.contains(var1)) {
         return var1;
      } else {
         int var2 = this.T(var1);
         if (var2 < 0) {
            return var1;
         } else {
            int var3 = var2 / 10;
            int var4 = var2 % 10;
            if (this.W[var3] && var4 == this.o[var3]) {
               return this.A(this.E[var3][this.U[var3]], var1.method_7947());
            } else {
               return this.U[var3] == var4 && this.o[var3] != 0 && this.o[var3] != var4 ? this.A(this.E[var3][this.o[var3]], var1.method_7947()) : var1;
            }
         }
      }
   }

   private class_1799 A(class_1799 var1, int var2) {
      class_1799 var3 = var1.method_46651(var2);
      this.y.add(var3);
      return var3;
   }

   public class_1799 B(class_310 var1, class_1716 var2, int var3, int var4) {
      class_1799 var5 = this.o(var1, var2, var3, var4);
      return var5.method_7960() ? var5 : this.A(var5, var5.method_7947());
   }

   private class_1799 o(class_310 var1, class_1716 var2, int var3, int var4) {
      class_1799 var5 = var2.method_7611(var4).method_7677();
      class_1799 var6 = this.U(var5, var3, false);
      int var7 = this.U[var3];
      int var8 = this.o[var3];
      if (var7 != 0 && var8 != 0 && var7 != var8) {
         this.e(var1, var2, var3);
         if (this.W[var3]) {
            int var18 = y6.b(var4, this.B[var3], this.p[var3], true);
            class_1799 var19 = var2.method_7611(var18).method_7677();
            return this.U(var19, var3, var18 != var4 || this.A(var3, var18, var19));
         }

         int var9 = this.h(var1).getOrDefault(var3 * 10 + var7, 0) > this.q.getOrDefault(var3 * 10 + var7, 0) ? 1 : 0;
         int var10 = -1;
         int var11 = -1;
         int var12 = -1;
         int var13 = 0;
         int[] var14 = new int[9];

         for (int var15 = 0; var15 < 9; var15++) {
            class_1799 var16 = var2.method_7611(var15).method_7677();
            var14[var15] = var16.method_7947();
            int var17 = this.T(var16);
            if (var17 == var3 * 10 + var7) {
               var10 = var15;
            }

            if (var17 == var3 * 10 + var8) {
               var11 = var15;
            }

            if (var16.method_7960()) {
               var12 = var15;
               var13++;
            } else if (var17 / 10 == var3 && var17 > 0 && this.D[var3][var15] == 0) {
               this.D[var3][var15] = var17 % 10;
            }
         }

         if (var10 < 0) {
            for (int var20 = 0; var20 < 9; var20++) {
               if (this.D[var3][var20] == var7 && var2.method_7611(var20).method_7677().method_7960()) {
                  var10 = var20;
                  break;
               }
            }

            if (var10 < 0 && var13 == 1) {
               var10 = var12;
            }
         }

         if (var10 >= 0 && var14[var10] >= this.E[var3][var7].method_7914()) {
            return var6;
         } else {
            int[] var21 = es.C(var14, var10, var11, (boolean)var9);
            if (var21[var4] == var14[var4]) {
               return var6;
            } else {
               return var21[var4] == 0 ? class_1799.field_8037 : (var5.method_7960() ? this.E[var3][var7] : var5).method_46651(var21[var4]);
            }
         }
      } else {
         return var6;
      }
   }

   private boolean A(int var1, int var2, class_1799 var3) {
      if (!this.W[var1]) {
         return false;
      }

      int var4 = this.T(var3);
      return var4 == var1 * 10 + this.U[var1] && var2 != this.B[var1] || var4 == var1 * 10 + this.o[var1] && var2 != this.p[var1];
   }

   private class_1799 U(class_1799 var1, int var2, boolean var3) {
      int var4 = this.T(var1);
      if (var4 >= 0 && this.W[var4 / 10]) {
         return this.G(var1);
      } else {
         return y6.J(var2, var4 < 0 ? -1 : var4 / 10, var3) ? this.G(var1) : var1;
      }
   }

   private void e(class_310 var1, class_1716 var2, int var3) {
      if (!this.W[var3] && this.U[var3] != 0 && this.o[var3] != 0 && this.U[var3] != this.o[var3]) {
         int var4 = -1;
         int var5 = -1;
         int var6 = -1;
         int var7 = 0;

         for (int var8 = 0; var8 < 9; var8++) {
            int var9 = this.T(var2.method_7611(var8).method_7677());
            if (this.D[var3][var8] == 0 && var9 > 0 && var9 / 10 == var3) {
               this.D[var3][var8] = var9 % 10;
            }

            if (this.D[var3][var8] == this.U[var3] && var2.method_7611(var8).method_7677().method_7960()) {
               var4 = var8;
            }

            if (var9 == var3 * 10 + this.o[var3]) {
               var5 = var8;
            }

            if (var2.method_7611(var8).method_7677().method_7960()) {
               var6 = var8;
               var7++;
            }
         }

         if (var4 < 0 && var7 == 1) {
            var4 = var6;
            this.D[var3][var4] = this.U[var3];
         }

         if (var4 >= 0 && var5 >= 0 && var4 != var5) {
            if (this.T(var2.method_7611(var4).method_7677()) != var3 * 10 + this.U[var3]
               && var2.method_7611(var5).method_7677().method_7947() == 1
               && this.h(var1).getOrDefault(var3 * 10 + this.U[var3], 0) > this.q.getOrDefault(var3 * 10 + this.U[var3], 0)) {
               this.B[var3] = var4;
               this.p[var3] = var5;
               this.W[var3] = (boolean)1;
            }
         }
      }
   }

   public int F(class_310 var1, class_1716 var2, int var3, int var4) {
      this.e(var1, var2, var3);
      return y6.b(var4, this.B[var3], this.p[var3], this.W[var3]);
   }
}
