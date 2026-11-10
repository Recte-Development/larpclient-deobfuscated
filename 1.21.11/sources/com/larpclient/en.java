package com.larpclient;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.WeakHashMap;
import java.util.Map.Entry;
import java.util.concurrent.ThreadLocalRandom;
import net.minecraft.class_10192;
import net.minecraft.class_1297;
import net.minecraft.class_1304;
import net.minecraft.class_1531;
import net.minecraft.class_1542;
import net.minecraft.class_1716;
import net.minecraft.class_1792;
import net.minecraft.class_1799;
import net.minecraft.class_2246;
import net.minecraft.class_2315;
import net.minecraft.class_2338;
import net.minecraft.class_2350;
import net.minecraft.class_243;
import net.minecraft.class_2680;
import net.minecraft.class_310;
import net.minecraft.class_7923;
import net.minecraft.class_9334;

final class en {
   private final Set<class_1799> F = Collections.newSetFromMap(new WeakHashMap<>());
   private final wq A = new wq();
   private final Set<UUID> r = new HashSet<>();
   private final Set<String> v = new HashSet<>();
   private final q5 H = new q5();
   private final boolean c;
   private final boolean p;
   private boolean j;
   private boolean W;
   private boolean E;
   private final fo G = new fo();
   public int J = 1;
   private int i;
   private final class_2338[] k = new class_2338[2];
   private final class_1799[][] L = new class_1799[2][10];
   private final int[][] b = new int[2][9];
   private final int[] t = new int[]{0, 0};
   private final int[] O = new int[]{0, 0};
   private final boolean[] S = new boolean[]{false, false};
   private final int[] N = new int[]{-1, -1};
   private final int[] T = new int[]{-1, -1};
   private final Map<UUID, Integer> u = new HashMap<>();
   private final Set<UUID> Q = new HashSet<>();
   private final Set<UUID> V = new HashSet<>();
   private final yo Y = new yo();
   private final List<Map<Integer, Integer>> d = List.of(new HashMap<>(), new HashMap<>());
   private class_1716 q;
   private int m = -1;
   private final Map<Integer, Integer> w = new HashMap<>();
   private final Map<Integer, Integer> B = new HashMap<>();
   public int X;
   public boolean I = (boolean)1;

   private class_1799 v(class_1799 var1, int var2) {
      class_1799 var3 = var1.method_46651(var2);
      this.F.add(var3);
      return var3;
   }

   public boolean e() {
      boolean var1 = this.j;
      this.j = (boolean)0;
      return var1;
   }

   public static en C() {
      en var0 = new en(true);
      var0.E = (boolean)1;
      return var0;
   }

   public static en B() {
      en var0 = new en(true);
      var0.W = (boolean)1;
      return var0;
   }

   public en() {
      this(false);
   }

   public en(boolean var1) {
      this(var1, false);
   }

   public en(boolean var1, boolean var2) {
      this.c = var1;
      this.p = var2;
   }

   public class_1799 F(int var1) {
      return this.L[0][var1] == null ? class_1799.field_8037 : this.L[0][var1].method_7972();
   }

   public int[] D() {
      return (int[])this.b[0].clone();
   }

   public void Y(class_310 var1, class_2338 var2, class_1716 var3) {
      int var4 = this.j() && Objects.equals(this.k[0], var2) ? 1 : 0;
      if (var4 != 0) {
         byte var5 = 1;

         for (int var6 = 0; var6 < 9; var6++) {
            if (var3.method_7611(var6).method_7677().method_7960()) {
               var5 = 0;
            }
         }

         if (var5 == 0 || this.S(var1) || this.p && this.A.H()) {
            this.N(var3, 0, true);
            return;
         }
      }

      try {
         this.p(var1, var2, var3);
      } catch (IllegalArgumentException var7) {
         if (var4 == 0) {
            throw var7;
         }
      }

      this.N(var3, 0, true);
   }

   public void f(class_310 var1, class_2338 var2, class_1716 var3, class_1792 var4, class_1792 var5, class_1792 var6, int[] var7) {
      if (this.j() && Objects.equals(this.k[0], var2) && this.S(var1)) {
         this.N(var3, 0, true);
      } else {
         class_1799[] var8 = new class_1799[]{class_1799.field_8037, new class_1799(var4), new class_1799(var5), new class_1799(var6)};

         for (int var9 = 0; var9 < 9; var9++) {
            class_1799 var10 = var3.method_7611(var9).method_7677();
            int var11 = var7[var9];
            if (!var10.method_7960() && var11 > 0 && var11 < 4) {
               var8[var11] = var10.method_46651(1);
            }
         }

         int var12 = Objects.equals(this.k[0], var2) && Arrays.equals(this.b[0], var7) ? 0 : 1;

         for (int var13 = 1; var13 <= 3; var13++) {
            var12 |= this.L[0][var13] != null && class_1799.method_31577(this.L[0][var13], var8[var13]) ? 0 : 1;
         }

         this.k[0] = var2.method_10062();
         this.b[0] = (int[])var7.clone();

         for (int var14 = 1; var14 <= 3; var14++) {
            this.L[0][var14] = var8[var14];
         }

         if (var12 != 0) {
            this.N(var1);
         }

         this.N(var3, 0, true);
      }
   }

   public void o() {
      this.J = this.J % this.Y() + 1;
   }

   public String S(int var1) {
      return this.L[0][var1] == null ? "" : this.L[0][var1].method_7964().getString().replace('\n', ' ').replace('\r', ' ');
   }

   private int Y() {
      return this.E ? 4 : (this.p ? 2 : (this.c ? 3 : 9));
   }

   public boolean S(class_310 var1) {
      if (var1 == null || !this.j() || var1.field_1724 == null) {
         return false;
      }

      if (this.p || this.W || this.E) {
         this.n(var1);
         return this.A.j();
      }

      if (this.c(var1)) {
         return true;
      }

      Map var2 = this.H(var1);

      for (int var3 = 0; var3 < 2; var3++) {
         if (this.t[var3] > 0 && this.O[var3] > 0 && this.t[var3] != this.O[var3]) {
            int var4 = var3 * 10 + this.t[var3];
            int var5 = var3 * 10 + this.O[var3];
            if (var2.getOrDefault(var4, 0) > this.B.getOrDefault(var4, 0) || this.S[var3] && var2.getOrDefault(var5, 0) > this.B.getOrDefault(var5, 0)) {
               return true;
            }
         }
      }

      return false;
   }

   public boolean J(int var1) {
      return this.k[var1] != null;
   }

   public boolean j() {
      return this.J(0) && (this.c || this.J(1));
   }

   public class_2338 P(int var1) {
      return this.k[var1];
   }

   public String k(int var1) {
      return this.t[var1] == 0 ? "" : Integer.toString(this.O[var1]);
   }

   public void p(class_310 var1, class_2338 var2, class_1716 var3) {
      if (var2.equals(this.k[1 - this.X])) {
         throw new IllegalArgumentException("Host and Viewer need different droppers.");
      }

      class_1799[] var4 = new class_1799[10];
      int[] var5 = new int[9];
      if (this.E) {
         this.X = 0;
         int var6 = 0;

         for (int var7 = 0; var7 < 9; var7++) {
            class_1799 var8 = var3.method_7611(var7).method_7677();
            if (!var8.method_7960()) {
               int var9 = Y(var8);
               if (var9 < 1 || var8.method_7947() != 1 || var4[var9] != null) {
                  throw new IllegalArgumentException("Use one helmet, chestplate, leggings and boots, with the other slots empty.");
               }

               var4[var9] = var8.method_46651(1);
               var5[var7] = var9;
               var6++;
            }
         }

         if (var6 != 4) {
            throw new IllegalArgumentException("Armor Game requires four armor pieces, one for each equipment slot.");
         }
      } else if (this.c) {
         this.X = 0;
         class_1799[] var12 = new class_1799[9];
         int[] var14 = new int[9];
         int var16 = 0;

         for (int var18 = 0; var18 < 9; var18++) {
            class_1799 var10 = var3.method_7611(var18).method_7677();
            if (this.W && (var10.method_7947() != 1 || !class_7923.field_41178.method_10221(var10.method_7909()).method_12832().endsWith("_horse_armor"))) {
               throw new IllegalArgumentException("Use one horse armor per slot, three of each type.");
            }

            if (var10.method_7960()) {
               throw new IllegalArgumentException("This game requires nine occupied slots.");
            }

            int var11 = 0;

            while (var11 < var16 && !this.j(var12[var11], var10)) {
               var11++;
            }

            if (var11 == var16) {
               var12[var16++] = var10.method_46651(1);
            }

            var14[var18] = var11;
         }

         var5 = this.W ? fo.s(var14) : (this.p ? lp.C(var14) : lp.Q(var14));
         if (this.p) {
            for (int var19 = 0; var19 < 9; var19++) {
               if (var5[var19] == 1 && var3.method_7611(var19).method_7677().method_7914() != 1) {
                  throw new IllegalArgumentException("Russian Roulette safe items must be unstackable.");
               }
            }
         }

         for (int var20 = 0; var20 < 9; var20++) {
            var4[var5[var20]] = var3.method_7611(var20).method_7677().method_46651(1);
         }
      } else {
         for (int var13 = 0; var13 < 9; var13++) {
            class_1799 var15 = var3.method_7611(var13).method_7677();
            int var17 = u4.r(var15.method_7964().getString());
            String var21 = var15.method_7964().getString();
            if (var15.method_7960()) {
               throw new IllegalArgumentException("Paper Game slot " + (var13 + 1) + " is empty.");
            }

            if (var17 < 1) {
               throw new IllegalArgumentException("Paper Game slot " + (var13 + 1) + ": cannot read rank from [" + var21 + "].");
            }

            if (u4.r(var21) != (this.X == 0 ? 1 : 0)) {
               throw new IllegalArgumentException(
                  "Paper Game slot " + (var13 + 1) + ": [" + var21 + "] belongs to the " + (this.X == 0 ? "Viewer" : "Host") + " side; change the setup side."
               );
            }

            if (var4[var17] != null) {
               throw new IllegalArgumentException("Paper Game slot " + (var13 + 1) + ": duplicate rank " + var17 + " in [" + var21 + "].");
            }

            var4[var17] = var15.method_46651(1);
            var5[var13] = var17;
         }
      }

      this.L[this.X] = var4;
      this.k[this.X] = var2.method_10062();
      this.b[this.X] = var5;
      this.N(var1);
   }

   private boolean j(class_1799 var1, class_1799 var2) {
      return var1.method_31574(var2.method_7909())
         && (!this.p && !this.W && !this.E || Objects.equals(var1.method_58694(class_9334.field_49631), var2.method_58694(class_9334.field_49631)));
   }

   private int R(class_1799 var1) {
      if (var1.method_7960()) {
         return -1;
      }

      if (this.c) {
         for (int var6 = 1; var6 <= this.Y(); var6++) {
            if (this.J(0) && this.L[0][var6] != null && this.j(var1, this.L[0][var6])) {
               return var6;
            }
         }

         return -1;
      } else {
         String var2 = var1.method_7964().getString();
         int var3 = u4.r(var2);
         if (var3 < 1) {
            return -1;
         }

         int var4 = u4.r(var2) ? 0 : 1;
         class_1799 var5 = this.L[var4][var3];
         return this.J(var4) && var5 != null && var1.method_31574(var5.method_7909()) ? var4 * 10 + var3 : -1;
      }
   }

   private Map<Integer, Integer> H(class_310 var1) {
      if (var1.field_1724.field_7512 instanceof class_1716 var2 && var2 == this.q) {
         this.g(var2, this.m);
      }

      HashMap var11 = new HashMap();

      for (Map var4 : this.d) {
         var4.forEach((var1x, var2x) -> var11.merge(var1x, var2x, Integer::sum));
      }

      for (int var13 = 0; var13 < 36; var13++) {
         class_1799 var15 = var1.field_1724.method_31548().method_5438(var13);
         int var5 = this.R(var15);
         if (var5 >= 0) {
            var11.merge(var5, var15.method_7947(), Integer::sum);
         }
      }

      class_1799 var14 = var1.field_1724.field_7512.method_34255();
      int var16 = this.R(var14);
      if (var16 >= 0) {
         var11.merge(var16, var14.method_7947(), Integer::sum);
      }

      class_1799 var17 = var1.field_1724.method_6079();
      int var6 = this.R(var17);
      if (var6 >= 0) {
         var11.merge(var6, var17.method_7947(), Integer::sum);
      }

      if (var1.field_1687 != null) {
         for (class_1297 var9 : var1.field_1687.method_18112()) {
            if (var9 instanceof class_1542 var7 && !var7.method_31481() && this.V.contains(var7.method_5667())) {
               int var10 = this.R(var7.method_6983());
               if (var10 >= 0) {
                  var11.merge(var10, var7.method_6983().method_7947(), Integer::sum);
               }
            }
         }
      }

      return var11;
   }

   private void g(class_1716 var1, int var2) {
      Map var3 = this.d.get(var2);
      var3.clear();

      for (int var4 = 0; var4 < 9; var4++) {
         class_1799 var5 = var1.method_7611(var4).method_7677();
         int var6 = this.R(var5);
         if (y6.J(var2, var6 < 0 ? -1 : var6 / 10, false)) {
            var3.merge(var6, var5.method_7947(), Integer::sum);
         }
      }
   }

   public void N(class_1716 var1, int var2, boolean var3) {
      if (var3 || this.q == var1) {
         this.q = var1;
         this.m = var2;
         this.g(var1, var2);
         if (this.p || this.W || this.E) {
            this.n(class_310.method_1551());
         }
      }
   }

   private void a(class_310 var1) {
      if (var1.field_1687 != null) {
         if (this.c && this.H.E()) {
            this.R(var1, this.b(var1));
         }

         for (class_1297 var3 : var1.field_1687.method_18112()) {
            if (var3 instanceof class_1542 var4 && !var4.method_31481() && !this.Q.contains(var4.method_5667())) {
               int var5 = this.R(var4.method_6983());
               if (var5 >= 0) {
                  if (this.c && this.O[0] > 0 && var1.field_1724 != null && var4.method_5858(var1.field_1724) <= 9.0) {
                     int var6 = this.H.h(var5, -1);
                     if (var6 > 0 || var5 == this.t[0]) {
                        this.u.put(var4.method_5667(), var6 > 0 ? var6 : this.O[0]);
                        this.V.add(var4.method_5667());
                        this.Q.add(var4.method_5667());
                        continue;
                     }
                  }

                  class_2338 var10 = this.k[var5 / 10];
                  if (var10 != null) {
                     class_2680 var7 = var1.field_1687.method_8320(var10);
                     if (var7.method_27852(class_2246.field_10228) || var7.method_27852(class_2246.field_10200)) {
                        class_2350 var8 = (class_2350)var7.method_11654(class_2315.field_10918);
                        class_243 var9 = class_243.method_24953(var10)
                           .method_1031(var8.method_10148() * 0.7, var8.method_10164() * 0.7, var8.method_10165() * 0.7);
                        if (var4.method_5707(var9) <= 2.25) {
                           this.V.add(var4.method_5667());
                           this.Q.add(var4.method_5667());
                        }
                     }
                  }
               }
            }
         }
      }
   }

   public class_1799 Y(class_310 var1, class_1542 var2) {
      this.I(var1);
      if (!this.p && !this.W && !this.E) {
         if (this.c && this.u.containsKey(var2.method_5667())) {
            return this.u(var2.method_6983(), this.u.get(var2.method_5667()));
         } else {
            return this.V.contains(var2.method_5667()) ? this.l(var2.method_6983()) : var2.method_6983();
         }
      } else {
         return this.r.contains(var2.method_5667()) ? this.u(var2.method_6983(), this.A.b("world:" + var2.method_5667(), 1)) : var2.method_6983();
      }
   }

   public void N(class_310 var1) {
      this.G.H();
      this.j = (boolean)0;
      this.A.F(this.b[0]);
      this.r.clear();
      this.v.clear();
      if ((this.p || this.W || this.E) && var1.field_1724 != null) {
         for (int var2 = 0; var2 < 38; var2++) {
            class_1799 var3 = var2 < 36
               ? var1.field_1724.method_31548().method_5438(var2)
               : (var2 == 36 ? var1.field_1724.field_7512.method_34255() : var1.field_1724.method_6079());
            if (this.R(var3) > 0) {
               this.v.add("player:" + var2);
            }
         }
      }

      if (this.E && var1.field_1687 != null) {
         for (class_1297 var4 : var1.field_1687.method_18112()) {
            if (var4 instanceof class_1531 var6) {
               for (int var5 = 1; var5 <= 4; var5++) {
                  if (!var6.method_6118(i(var5)).method_7960()) {
                     this.v.add("stand:" + var6.method_5667() + ":" + var5);
                  }
               }
            }
         }
      }

      this.H.b();
      this.u.clear();
      this.i = ThreadLocalRandom.current().nextInt(2520);
      Arrays.fill(this.t, 0);
      Arrays.fill(this.O, 0);
      this.Y.o();

      for (Map var10 : this.d) {
         var10.clear();
      }

      this.q = null;
      this.m = -1;
      Arrays.fill(this.S, false);
      Arrays.fill(this.N, -1);
      Arrays.fill(this.T, -1);
      this.V.clear();
      this.Q.clear();
      if (var1.field_1687 != null) {
         for (class_1297 var11 : var1.field_1687.method_18112()) {
            this.Q.add(var11.method_5667());
         }
      }

      this.w.clear();
      if (var1.field_1724 != null) {
         this.w.putAll(this.H(var1));
      }

      this.B.clear();
      this.B.putAll(this.w);
   }

   public void I(class_310 var1) {
      if (var1 != null && this.j() && var1.field_1724 != null && var1.field_1687 != null) {
         if (!this.p && !this.W && !this.E) {
            this.a(var1);
            int[] var2 = (int[])this.t.clone();
            Map var3 = this.H(var1);
            if (this.Y
               .n(
                  this.t[0] > 0 && (this.c || this.t[1] > 0),
                  !this.c(var1)
                     && var3.getOrDefault(this.t[0], 0) <= this.B.getOrDefault(this.t[0], 0)
                     && var3.getOrDefault(10 + this.t[1], 0) <= this.B.getOrDefault(10 + this.t[1], 0)
                     && (!this.S[0] || var3.getOrDefault(this.O[0], 0) <= this.B.getOrDefault(this.O[0], 0))
                     && (!this.S[1] || var3.getOrDefault(10 + this.O[1], 0) <= this.B.getOrDefault(10 + this.O[1], 0)),
                  var1.field_1687.method_75260()
               )) {
               this.N(var1);
            } else {
               for (Entry var5 : var3.entrySet()) {
                  if ((Integer)var5.getValue() > this.w.getOrDefault(var5.getKey(), 0)) {
                     int var6 = (Integer)var5.getKey() / 10;
                     int var7 = (Integer)var5.getKey() % 10;
                     if (this.p || this.W || this.E) {
                        int var8 = lp.S(this.t[var6], this.O[var6] == 0 ? this.J : this.O[var6], var7);
                        if (var8 != this.t[var6]) {
                           this.H.b();
                           this.t[var6] = var8;
                        }
                     } else if (this.t[var6] == 0) {
                        this.t[var6] = var7;
                     }
                  }
               }

               if (this.O[0] == 0 && (this.t[0] > 0 || this.t[1] > 0)) {
                  if (this.c) {
                     this.O[0] = this.J;
                  } else {
                     ThreadLocalRandom var9 = ThreadLocalRandom.current();
                     u4.d var11 = u4.I(this.t[0] > 0 ? this.t[0] : var9.nextInt(1, 10), this.t[1] > 0 ? this.t[1] : var9.nextInt(1, 10), this.I, var9.nextInt());
                     this.O[0] = var11.host();
                     this.O[1] = var11.viewer();
                  }
               }

               for (int var10 = 0; var10 < 2; var10++) {
                  if (this.t[var10] > 0 && var2[var10] == 0) {
                     this.S(var10, this.t[var10], this.O[var10]);
                  }
               }

               this.w.clear();
               this.w.putAll(var3);
            }
         } else {
            this.n(var1);
         }
      }
   }

   private void S(int var1, int var2, int var3) {
      if (var2 >= 1 && var3 >= 1) {
         String var4 = this.L[var1][var2].method_7964().getString();
         String var5 = this.L[var1][var3].method_7964().getString();
         yq.E(
            this.p ? "Russian Roulette" : "45/45/10",
            (this.c ? "" : (var1 == 0 ? "Host: " : "Viewer: ")) + "Drew " + var4 + (var2 == var3 ? "; unchanged." : "; spoofed as " + var5 + " (visual only).")
         );
      }
   }

   private void n(class_310 var1) {
      if (this.j() && var1.field_1724 != null && var1.field_1687 != null) {
         LinkedHashMap var2 = new LinkedHashMap();
         HashSet var3 = new HashSet();
         class_1716 var4 = this.b(var1);
         if (var4 != null) {
            for (int var5 = 0; var5 < 9; var5++) {
               var2.put("stock:" + var5, this.R(var4.method_7611(var5).method_7677()));
            }
         }

         for (int var14 = 0; var14 < 38; var14++) {
            class_1799 var6 = var14 < 36
               ? var1.field_1724.method_31548().method_5438(var14)
               : (var14 == 36 ? var1.field_1724.field_7512.method_34255() : var1.field_1724.method_6079());
            String var7 = "player:" + var14;
            int var8 = this.R(var6);
            if (var8 < 0) {
               this.v.remove(var7);
            }

            if (!this.v.contains(var7)) {
               var2.put(var7, var8);
               if (var4 == null) {
                  var3.add(var7);
               }
            }
         }

         class_2338 var15 = this.k[0];
         class_2680 var16 = var1.field_1687.method_8320(var15);
         class_243 var17 = class_243.method_24953(var15);
         if (var16.method_27852(class_2246.field_10228) || var16.method_27852(class_2246.field_10200)) {
            class_2350 var18 = (class_2350)var16.method_11654(class_2315.field_10918);
            var17 = var17.method_1031(var18.method_10148() * 0.7, var18.method_10164() * 0.7, var18.method_10165() * 0.7);
         }

         for (class_1297 var9 : var1.field_1687.method_18112()) {
            if (var9 instanceof class_1542 var10 && !var10.method_31481() && this.R(var10.method_6983()) > 0) {
               int var11 = var10.method_5707(var17) <= 2.25 ? 1 : 0;
               if (!this.Q.contains(var10.method_5667()) && (var11 != 0 || var10.method_5858(var1.field_1724) <= 9.0)) {
                  this.r.add(var10.method_5667());
               }

               if (this.r.contains(var10.method_5667())) {
                  String var12 = "world:" + var10.method_5667();
                  var2.put(var12, this.R(var10.method_6983()));
                  if (var11 != 0) {
                     var3.add(var12);
                  }
               }
            }
         }

         if (this.E) {
            for (class_1297 var25 : var1.field_1687.method_18112()) {
               if (var25 instanceof class_1531 var20 && var20.method_5707(var17) < 9.0) {
                  for (int var26 = 1; var26 <= 4; var26++) {
                     String var27 = "stand:" + var20.method_5667() + ":" + var26;
                     int var13 = this.R(var20.method_6118(i(var26)));
                     if (var13 < 1) {
                        this.v.remove(var27);
                     }

                     if (!this.v.contains(var27)) {
                        var2.put(var27, var13);
                        var3.add(var27);
                     }
                  }
               }
            }
         }

         if (this.p && var4 != null) {
            this.A.P(var2);
         }

         if (this.W) {
            this.A.C(var2, var3, () -> this.G.F(this.J), this.i, var4 != null);
         } else {
            this.A.u(var2, var3, this.J, this.i, var4 != null);
         }

         if (this.p) {
            for (int var21 = 0; var21 < 9; var21++) {
               if (this.b[0][var21] == 2) {
                  this.A.L(2, var21);
               }
            }
         }

         if (this.W && var4 != null && this.A.m()) {
            this.G.H();
         }

         for (wq.l var24 : this.A.u()) {
            if (this.p && var24.display() == 2) {
               this.j = (boolean)1;
               this.J = 1;
            }

            this.S(0, var24.actual(), var24.display());
         }
      }
   }

   private static class_1304 i(int var0) {
      return switch (var0) {
         case 1 -> class_1304.field_6169;
         case 2 -> class_1304.field_6174;
         case 3 -> class_1304.field_6172;
         default -> class_1304.field_6166;
      };
   }

   private static int Y(class_1799 var0) {
      class_10192 var1 = (class_10192)var0.method_58694(class_9334.field_54196);
      if (var1 == null) {
         return -1;
      }

      for (int var2 = 1; var2 <= 4; var2++) {
         if (var1.comp_3174() == i(var2)) {
            return var2;
         }
      }

      return -1;
   }

   public class_1799[] N(class_1531 var1) {
      if (this.E && this.j() && !(var1.method_5707(class_243.method_24953(this.k[0])) > 16.0)) {
         this.n(class_310.method_1551());
         class_1799[] var2 = new class_1799[5];
         Arrays.fill(var2, class_1799.field_8037);

         for (int var3 = 1; var3 <= 4; var3++) {
            class_1799 var4 = var1.method_6118(i(var3));
            int var5 = this.A.b("stand:" + var1.method_5667() + ":" + var3, var3);
            if (!var4.method_7960()) {
               var2[var5] = this.R(var4) > 0 ? this.u(var4, var5) : var4;
            }
         }

         return var2;
      } else {
         return null;
      }
   }

   private class_1799 C(class_1799 var1) {
      class_310 var2 = class_310.method_1551();
      this.n(var2);
      if (var2.field_1724 == null) {
         return var1;
      }

      for (int var3 = 0; var3 < 38; var3++) {
         class_1799 var4 = var3 < 36
            ? var2.field_1724.method_31548().method_5438(var3)
            : (var3 == 36 ? var2.field_1724.field_7512.method_34255() : var2.field_1724.method_6079());
         if (var1 == var4) {
            return this.u(var1, this.A.b("player:" + var3, this.v.contains("player:" + var3) ? this.R(var1) : (this.R(var1) > 0 ? 1 : -1)));
         }
      }

      return var1;
   }

   private void R(class_310 var1, class_1716 var2) {
      if (this.c && var1.field_1724 != null && this.O[0] != 0) {
         int[] var3 = new int[47];
         int[] var4 = new int[47];
         Arrays.fill(var3, -2);
         Arrays.fill(var4, -1);

         for (int var5 = 0; var5 < 38; var5++) {
            class_1799 var6 = var5 < 36
               ? var1.field_1724.method_31548().method_5438(var5)
               : (var5 == 36 ? var1.field_1724.field_7512.method_34255() : var1.field_1724.method_6079());
            int var7 = this.R(var6);
            var3[var5] = var7;
            var4[var5] = var7 == this.t[0] ? this.O[0] : var7;
         }

         if (var2 != null) {
            for (int var8 = 0; var8 < 9; var8++) {
               int var9 = this.R(var2.method_7611(var8).method_7677());
               var3[38 + var8] = var9;
               var4[38 + var8] = var9;
               if (this.S[0] && (var8 == this.N[0] || var8 == this.T[0])) {
                  var4[38 + var8] = var9 == this.t[0] ? this.O[0] : (var9 == this.O[0] ? this.t[0] : var9);
               }
            }
         }

         this.H.O(var3, var4);
      }
   }

   private boolean c(class_310 var1) {
      if (this.c && !this.p && !this.W && !this.E && this.S[0] && this.H.E()) {
         class_1716 var2 = this.b(var1);
         if (var2 != null) {
            this.R(var1, var2);
         }

         for (int var3 = 0; var3 < 9; var3++) {
            int var4 = y6.b(var3, this.N[0], this.T[0], true);
            if (this.H.g(38 + var4) != this.b[0][var3]) {
               return true;
            }
         }

         return false;
      } else {
         return false;
      }
   }

   private class_1716 b(class_310 var1) {
      return var1.field_1724 != null && var1.field_1724.field_7512 == this.q && this.m == 0 ? this.q : null;
   }

   private class_1799 u(class_1799 var1, int var2) {
      return var2 > 0 && var2 <= this.Y() && this.L[0][var2] != null ? this.v(this.L[0][var2], var1.method_7947()) : var1;
   }

   public class_1799 l(class_1799 var1) {
      if (this.F.contains(var1)) {
         return var1;
      }

      if (!this.p && !this.W && !this.E) {
         if (this.c && this.H.E()) {
            class_310 var2 = class_310.method_1551();
            this.R(var2, this.b(var2));
            if (var2.field_1724 != null) {
               for (int var3 = 0; var3 < 36; var3++) {
                  if (var1 == var2.field_1724.method_31548().method_5438(var3)) {
                     return this.u(var1, this.H.g(var3));
                  }
               }

               if (var1 == var2.field_1724.field_7512.method_34255()) {
                  return this.u(var1, this.H.g(36));
               }

               if (var1 == var2.field_1724.method_6079()) {
                  return this.u(var1, this.H.g(37));
               }
            }
         }

         int var5 = this.R(var1);
         if (var5 < 0) {
            return var1;
         } else {
            int var6 = var5 / 10;
            int var4 = var5 % 10;
            if (!this.c && this.S[var6] && var4 == this.O[var6]) {
               return this.v(this.L[var6][this.t[var6]], var1.method_7947());
            } else {
               return this.t[var6] == var4 && this.O[var6] != 0 && this.O[var6] != var4 ? this.v(this.L[var6][this.O[var6]], var1.method_7947()) : var1;
            }
         }
      } else {
         return this.C(var1);
      }
   }

   public class_1799 i(class_310 var1, class_1716 var2, int var3, int var4) {
      class_1799 var5 = this.U(var1, var2, var3, var4);
      return var5.method_7960() ? var5 : this.v(var5, var5.method_7947());
   }

   private class_1799 U(class_310 var1, class_1716 var2, int var3, int var4) {
      class_1799 var5 = var2.method_7611(var4).method_7677();
      if (!this.p && !this.W && !this.E) {
         class_1799 var18 = this.a(var5, var3, false);
         int var19 = this.t[var3];
         int var8 = this.O[var3];
         if (var19 != 0 && var8 != 0 && var19 != var8) {
            this.M(var1, var2, var3);
            if (this.c && this.S[var3]) {
               this.R(var1, var2);
               int var21 = y6.b(var4, this.N[var3], this.T[var3], true);
               return this.u(var2.method_7611(var21).method_7677(), this.H.g(38 + var21));
            }

            if (this.S[var3]) {
               int var20 = y6.b(var4, this.N[var3], this.T[var3], true);
               class_1799 var22 = var2.method_7611(var20).method_7677();
               return this.a(var22, var3, var20 != var4);
            }

            int var9 = this.H(var1).getOrDefault(var3 * 10 + var19, 0) > this.B.getOrDefault(var3 * 10 + var19, 0) ? 1 : 0;
            int var10 = -1;
            int var11 = -1;
            int var12 = -1;
            int var13 = 0;
            int[] var14 = new int[9];

            for (int var15 = 0; var15 < 9; var15++) {
               class_1799 var16 = var2.method_7611(var15).method_7677();
               var14[var15] = var16.method_7947();
               int var17 = this.R(var16);
               if (var17 == var3 * 10 + var19) {
                  var10 = var15;
               }

               if (var17 == var3 * 10 + var8) {
                  var11 = var15;
               }

               if (var16.method_7960()) {
                  var12 = var15;
                  var13++;
               } else if (var17 / 10 == var3 && var17 > 0 && this.b[var3][var15] == 0) {
                  this.b[var3][var15] = var17 % 10;
               }
            }

            if (var10 < 0) {
               for (int var23 = 0; var23 < 9; var23++) {
                  if (this.b[var3][var23] == var19 && var2.method_7611(var23).method_7677().method_7960()) {
                     var10 = var23;
                     break;
                  }
               }

               if (var10 < 0 && var13 == 1) {
                  var10 = var12;
               }
            }

            if (this.c) {
               var11 = this.Q(var2, var8);
            }

            if (var10 >= 0 && var14[var10] >= this.L[var3][var19].method_7914()) {
               return var18;
            } else {
               int[] var24 = es.C(var14, var10, var11, (boolean)var9);
               if (var24[var4] == var14[var4]) {
                  return var18;
               } else {
                  return var24[var4] == 0 ? class_1799.field_8037 : (var5.method_7960() ? this.L[var3][var19] : var5).method_46651(var24[var4]);
               }
            }
         } else {
            return var18;
         }
      } else {
         this.n(var1);
         int var6 = this.A.I(var4);
         class_1799 var7 = var2.method_7611(var6).method_7677();
         return this.u(var7, this.A.b("stock:" + var6, this.R(var7) > 0 ? 1 : -1));
      }
   }

   private class_1799 a(class_1799 var1, int var2, boolean var3) {
      int var4 = this.R(var1);
      if (!this.c && var4 >= 0 && this.S[var4 / 10]) {
         return this.l(var1);
      } else {
         return y6.J(var2, var4 < 0 ? -1 : var4 / 10, var3) ? this.l(var1) : var1;
      }
   }

   private int Q(class_1716 var1, int var2) {
      int[] var3 = new int[9];

      for (int var4 = 0; var4 < 9; var4++) {
         var3[var4] = this.R(var1.method_7611(var4).method_7677());
      }

      return lp.M(var3, var2, this.i);
   }

   private void M(class_310 var1, class_1716 var2, int var3) {
      if (!this.S[var3] && this.t[var3] != 0 && this.O[var3] != 0 && this.t[var3] != this.O[var3]) {
         int var4 = -1;
         int var5 = -1;
         int var6 = -1;
         int var7 = 0;

         for (int var8 = 0; var8 < 9; var8++) {
            int var9 = this.R(var2.method_7611(var8).method_7677());
            if (this.b[var3][var8] == 0 && var9 > 0 && var9 / 10 == var3) {
               this.b[var3][var8] = var9 % 10;
            }

            if (this.b[var3][var8] == this.t[var3] && (var4 < 0 || var2.method_7611(var8).method_7677().method_7960())) {
               var4 = var8;
            }

            if (var9 == var3 * 10 + this.O[var3]) {
               var5 = var8;
            }

            if (var2.method_7611(var8).method_7677().method_7960()) {
               var6 = var8;
               var7++;
            }
         }

         if (this.c) {
            var5 = this.Q(var2, this.O[var3]);
         }

         if (var4 < 0 && var7 == 1) {
            var4 = var6;
            this.b[var3][var4] = this.t[var3];
         }

         if (var4 >= 0 && var5 >= 0 && var4 != var5) {
            if (this.R(var2.method_7611(var4).method_7677()) != var3 * 10 + this.t[var3]
               && var2.method_7611(var5).method_7677().method_7947() == 1
               && this.H(var1).getOrDefault(var3 * 10 + this.t[var3], 0) > this.B.getOrDefault(var3 * 10 + this.t[var3], 0)) {
               this.N[var3] = var4;
               this.T[var3] = var5;
               this.S[var3] = (boolean)1;
            }
         }
      }
   }

   public int j(class_310 var1, int var2) {
      this.n(var1);
      return this.A.i("player:" + var2);
   }

   public void J(class_1716 var1, int var2) {
      int var3 = this.A.b("player:" + var2, -1);

      for (int var4 = 0; var4 < 9; var4++) {
         if ((!this.p || (this.b[0][var4] == 2 ? 1 : 0) == (var3 == 2 ? 1 : 0)) && var1.method_7611(this.A.I(var4)).method_7677().method_7960()) {
            this.A.w("player:" + var2, var4);
            return;
         }
      }
   }

   public boolean g(class_310 var1, class_1716 var2, int var3, int var4) {
      if (this.c && !this.p && !this.W && !this.E && this.S[0]) {
         class_1799 var5 = var4 == 36 ? var2.method_34255() : var1.field_1724.method_31548().method_5438(var4);
         class_1799 var6 = var2.method_7611(var3).method_7677();
         if (var6.method_7947() == 1 && var6.method_7914() == 1 && class_1799.method_7973(var6, var5)) {
            this.R(var1, var2);
            return this.H.d(38 + var3, var4);
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   public int U(class_310 var1, class_1716 var2, int var3, int var4) {
      if (!this.p && !this.W && !this.E) {
         this.M(var1, var2, var3);
         if (this.c && this.S[var3]) {
            this.R(var1, var2);
         }

         return y6.b(var4, this.N[var3], this.T[var3], this.S[var3]);
      } else {
         this.n(var1);
         return this.A.I(var4);
      }
   }
}
