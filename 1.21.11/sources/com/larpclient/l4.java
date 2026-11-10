package com.larpclient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ThreadLocalRandom;
import net.minecraft.class_1297;
import net.minecraft.class_1533;
import net.minecraft.class_1542;
import net.minecraft.class_1799;
import net.minecraft.class_1802;
import net.minecraft.class_310;
import net.minecraft.class_9209;
import net.minecraft.class_9334;

public final class l4 {
   private static final l4 K = new l4();
   private static final wz a = wz.A("BJAssistant");
   private final List<List<Integer>> H = new ArrayList<>();
   private final List<Integer> x = new ArrayList<>();
   private int z = 1;
   private int g;
   private l4.t b;
   private int h = -1;
   private Map<Integer, Integer> e;
   private Map<Integer, Integer> w = new HashMap<>();
   private Map<Integer, Integer> i = new HashMap<>();
   private int y;
   private l4.t n = l4.t.z;
   private final long B = System.currentTimeMillis();
   private int m;
   private int u;
   private int F;
   private boolean D;
   private boolean M;
   private boolean d = (boolean)1;
   private boolean p;
   private long o;
   private int N;
   private int G = -1;

   private l4() {
   }

   public static l4 K() {
      return K;
   }

   public void P(boolean var1) {
      this.p = var1;
   }

   public void S(class_310 var1) {
      this.H(var1, false);
   }

   public void H(class_310 var1, boolean var2) {
      if (!this.D) {
         q7.u().U();
         this.H.clear();
         this.x.clear();
         this.N = 0;
         this.G = -1;
         this.h = -1;
         this.m = 0;
         this.u = 0;
         this.D = (boolean)1;
         this.M = var2;
         if (this.e == null && var1 != null && var1.field_1724 != null) {
            this.e = this.Z(var1);
            this.w = new HashMap<>(this.e);
            this.i = new HashMap<>(this.e);
            this.y = 0;
         }
      }

      this.F++;
   }

   public void Q(class_310 var1, q7 var2, boolean var3) {
      if (!var3 || var1 == null || var1.field_1724 == null) {
         this.J();
      } else if (var2 == null || !var2.isSwapBusy()) {
         Map var4 = this.Z(var1);
         if (!var4.equals(this.i)) {
            this.i = new HashMap<>(var4);
            this.y = 0;
         } else {
            this.y++;
         }

         if (this.e == null) {
            this.e = new HashMap<>(var4);
            this.w = new HashMap<>(var4);
            this.i = new HashMap<>(var4);
         } else if (!this.D) {
            this.e = new HashMap<>(var4);
            this.w = new HashMap<>(var4);
            this.i = new HashMap<>(var4);
            this.m = 0;
            this.u = 0;
         } else {
            int var5 = this.Q(var4);
            if (this.y >= 2 && var5 > this.m) {
               int var6 = var5 - this.m;
               int var7 = Math.min(var6, this.F);
               if (var6 > var7) {
                  yq.R("BJ hand ignored duplicate map-count rise=" + var6 + " activations=" + this.F);
               }

               if (var7 != 0) {
                  int var8 = 0;

                  for (Entry var10 : var4.entrySet()) {
                     int var11 = Math.max(0, (Integer)var10.getValue() - this.w.getOrDefault(var10.getKey(), 0));

                     while (var11-- > 0 && var8 < var7) {
                        int var12 = this.B(var1, var2, (Integer)var10.getKey());
                        if (var12 >= 1 && var12 <= 10) {
                           var2.S(var12, (Integer)var10.getKey());
                           this.G(var12);
                           this.F--;
                           var8++;
                           this.w.merge((Integer)var10.getKey(), 1, Integer::sum);
                        }
                     }

                     if (var8 >= var7) {
                        break;
                     }
                  }

                  if (var8 > 0) {
                     this.m += var8;
                  }
               }
            }

            this.u = var5 <= 0 && !tw.s().m() ? this.u + 1 : 0;
            if (this.N > 0 && this.u >= 1 && this.u % 5 == 1) {
               a.K("BJ {} reset-watch dealtCards={} extras={} emptyHandTicks={} busy={}", this.h(), this.N, var5, this.u, var2 != null && var2.isSwapBusy());
            }

            if (this.N > 0 && this.u >= 6) {
               a.K("BJ {} round reset (cards returned)", this.h());
               this.H.clear();
               this.x.clear();
               this.N = 0;
               this.G = -1;
               this.h = -1;
               this.e = new HashMap<>(var4);
               this.w = new HashMap<>(var4);
               this.i = new HashMap<>(var4);
               this.m = 0;
               this.u = 0;
               this.F = 0;
               this.D = (boolean)0;
               if (this.n != l4.t.z || this.g != 0) {
                  this.n = l4.t.z;
                  this.g = 0;
                  yq.E("Blackjack", "Round over; side reset to hand 1");
               }
            }

            if (tw.s().j()) {
               this.W();
            }

            int var13 = this.z * 2 + 1;
            if (this.N >= var13 && this.n == l4.t.R) {
               int[] var14 = this.k(var2);
               if (var14 == null) {
                  this.G = -1;
                  this.h = -1;
               } else {
                  this.G = var14[0];
                  this.h = var14[1];
               }
            } else {
               this.G = -1;
               this.h = -1;
            }

            this.W(var2);
         }
      }
   }

   public boolean K(int var1, int var2) {
      if (this.n != l4.t.R) {
         return true;
      } else if (this.H.isEmpty()) {
         return true;
      } else {
         int var3 = this.E();
         if (var3 < 0) {
            return false;
         } else if (var3 >= 2 && var1 >= 1 && var2 >= 1) {
            int var4 = l(this.x, var1).total;
            return var4 < 17 || var4 > 21 || var4 <= var3;
         } else {
            return true;
         }
      }
   }

   private void W(q7 var1) {
      if (var1 != null && !var1.isSwapBusy() && this.p) {
         if (this.G >= 1) {
            int var2 = var1.getPendingValue();
            if (var2 != this.G || var1.getQueuedValueAt(1) != this.h) {
               var1.queueForceSequence(this.G, this.h);
               yq.R("SRIG " + this.h() + " armed plan rec=" + (this.h >= 1 ? this.G + "->" + this.h : this.G));
            }
         } else if (var1.hasQueuedForce()) {
            var1.clearQueue();
            yq.R("SRIG " + this.h() + " cleared plan (no recommendation)");
         }
      }
   }

   private void G(int var1) {
      int var2 = this.z;
      if (this.N < var2) {
         this.f(this.N, var1);
      } else if (this.N == var2) {
         this.x.add(var1);
      } else if (this.N <= var2 * 2) {
         this.f(this.N - var2 - 1, var1);
      } else if (this.n == l4.t.z) {
         this.f(this.g, var1);
      } else {
         this.x.add(var1);
      }

      this.N++;
   }

   private void f(int var1, int var2) {
      while (this.H.size() <= var1) {
         this.H.add(new ArrayList<>());
      }

      this.H.get(var1).add(var2);
   }

   private int[] k(q7 var1) {
      int var2 = this.n == l4.t.R ? 1 : 0;
      List var3 = var2 != 0 ? this.x : this.v();
      if (var3.isEmpty()) {
         return null;
      }

      int var4 = var2 != 0 ? this.E() : M(this.x).total;
      if (var2 != 0 && var4 < 0) {
         return null;
      }

      if (var2 != 0 && M(var3).total >= 17) {
         return null;
      }

      int var5 = -1;
      int var6 = -1;

      for (int var7 = 1; var7 <= 10; var7++) {
         if (var1.getTemplateForValue(var7) != null) {
            int var8 = l(var3, var7).total;
            if (var8 <= 21 && var8 >= 17) {
               if (var8 > var4 && var5 < 0) {
                  var5 = var7;
               } else if (var8 == var4 && var6 < 0) {
                  var6 = var7;
               }
            }
         }
      }

      if (var5 >= 1) {
         return new int[]{var5, -1};
      }

      for (int var12 = 1; var12 <= 10; var12++) {
         if (var1.getTemplateForValue(var12) != null) {
            int var15 = l(var3, var12).total;
            if (var15 <= 21 && (var2 == 0 || var15 < 17)) {
               ArrayList var9 = new ArrayList(var3);
               var9.add(var12);

               for (int var10 = 1; var10 <= 10; var10++) {
                  if (var1.getTemplateForValue(var10) != null) {
                     int var11 = l(var9, var10).total;
                     if (var11 <= 21 && var11 >= 17 && var11 > var4) {
                        return new int[]{var12, var10};
                     }
                  }
               }
            }
         }
      }

      if (var6 >= 1) {
         return new int[]{var6, -1};
      }

      for (int var13 = 1; var13 <= 10; var13++) {
         if (var1.getTemplateForValue(var13) != null) {
            int var16 = l(var3, var13).total;
            if (var16 <= 21 && (var2 == 0 || var16 < 17)) {
               ArrayList var17 = new ArrayList(var3);
               var17.add(var13);

               for (int var18 = 1; var18 <= 10; var18++) {
                  if (var1.getTemplateForValue(var18) != null) {
                     int var19 = l(var17, var18).total;
                     if (var19 <= 21 && var19 >= 17 && var19 == var4) {
                        return new int[]{var13, var18};
                     }
                  }
               }
            }
         }
      }

      int var14 = var2 != 0 ? this.Y(var1, var3, var4) : this.B(var1, var3, var4);
      return var14 >= 1 ? new int[]{var14, -1} : null;
   }

   private List<Integer> v() {
      return this.g >= 0 && this.g < this.H.size() ? this.H.get(this.g) : new ArrayList<>();
   }

   private int B(q7 var1, List<Integer> var2, int var3) {
      ArrayList var4 = new ArrayList();
      ArrayList var5 = new ArrayList();

      for (int var6 = 1; var6 <= 10; var6++) {
         if (var1.getTemplateForValue(var6) != null) {
            int var7 = l(var2, var6).total;
            if (var7 <= 21) {
               if (var7 > var3) {
                  var4.add(var6);
               } else if (var7 == var3) {
                  var5.add(var6);
               }
            }
         }
      }

      if (!var4.isEmpty()) {
         return (Integer)var4.get(ThreadLocalRandom.current().nextInt(var4.size()));
      } else {
         return !var5.isEmpty() ? (Integer)var5.get(ThreadLocalRandom.current().nextInt(var5.size())) : this.V(var1, var2);
      }
   }

   private int T() {
      int var1 = 0;

      for (List var3 : this.H) {
         var1 = Math.max(var1, M(var3).total);
      }

      return var1;
   }

   private void W() {
      int var1 = -1;

      for (int var2 = 0; var2 < this.z && var1 < 0; var2++) {
         if (var2 >= this.H.size() || M(this.H.get(var2)).total < 17) {
            var1 = var2;
         }
      }

      if (var1 >= 0) {
         this.g = var1;
      }

      this.n = this.b != null ? this.b : (var1 < 0 ? l4.t.R : l4.t.z);
   }

   private int E() {
      int var1 = -1;

      for (List var3 : this.H) {
         int var4 = M(var3).total;
         if (var4 <= 21) {
            var1 = Math.max(var1, var4);
         }
      }

      return var1;
   }

   private int Y(q7 var1, List<Integer> var2, int var3) {
      int var4 = M(var2).total;
      if (var4 >= 17) {
         return -1;
      }

      ArrayList var5 = new ArrayList();
      ArrayList var6 = new ArrayList();

      for (int var7 = 1; var7 <= 10; var7++) {
         if (var1.getTemplateForValue(var7) != null) {
            int var8 = l(var2, var7).total;
            if (var8 <= 21 && var8 >= 17) {
               if (var8 > var3) {
                  for (int var9 = 0; var9 < 6; var9++) {
                     var5.add(var7);
                  }
               } else if (var8 == var3) {
                  var6.add(var7);
               }
            }
         }
      }

      if (!var5.isEmpty()) {
         for (int var15 : var6) {
            var5.add(var15);
         }

         return (Integer)var5.get(ThreadLocalRandom.current().nextInt(var5.size()));
      } else {
         if (var4 < 17) {
            ArrayList var12 = new ArrayList();

            for (int var14 = 1; var14 <= 10; var14++) {
               if (var1.getTemplateForValue(var14) != null) {
                  int var16 = l(var2, var14).total;
                  int var10 = var16 == 16 ? 6 : (var16 == 15 ? 4 : (var16 == 14 ? 2 : 0));

                  for (int var11 = 0; var11 < var10; var11++) {
                     var12.add(var14);
                  }
               }
            }

            if (!var12.isEmpty()) {
               return (Integer)var12.get(ThreadLocalRandom.current().nextInt(var12.size()));
            }
         }

         return !var6.isEmpty() ? (Integer)var6.get(ThreadLocalRandom.current().nextInt(var6.size())) : this.V(var1, var2);
      }
   }

   private int V(q7 var1, List<Integer> var2) {
      int var3 = -1;
      int var4 = -1;

      for (int var5 = 1; var5 <= 10; var5++) {
         if (var1.getTemplateForValue(var5) != null) {
            int var6 = l(var2, var5).total;
            if (var6 <= 21 && var6 > var4) {
               var4 = var6;
               var3 = var5;
            }
         }
      }

      return var3;
   }

   private Map<Integer, Integer> Z(class_310 var1) {
      HashMap var2 = new HashMap();
      tw var3 = tw.s();

      for (int var4 = 0; var4 < var1.field_1724.method_31548().method_5439(); var4++) {
         class_1799 var5 = var1.field_1724.method_31548().method_5438(var4);
         int var6 = var3.b(var4, var5);
         if (var6 < var5.method_7947()) {
            this.G(var2, var6 == 0 ? var5 : var5.method_46651(var5.method_7947() - var6));
         }
      }

      if (var1.field_1687 != null) {
         for (class_1297 var9 : var1.field_1687.method_18112()) {
            if (var9 instanceof class_1533 var10) {
               this.G(var2, var10.method_6940());
            } else if (var9 instanceof class_1542 var7) {
               this.G(var2, var7.method_6983());
            }
         }
      }

      return var2;
   }

   private void G(Map<Integer, Integer> var1, class_1799 var2) {
      if (var2 != null && !var2.method_7960() && var2.method_31574(class_1802.field_8204)) {
         class_9209 var3 = (class_9209)var2.method_57353().method_58694(class_9334.field_49646);
         if (var3 != null && this.k(var3) >= 1) {
            var1.merge(var3.comp_2315(), var2.method_7947(), Integer::sum);
         }
      }
   }

   private int k(class_9209 var1) {
      tw var2 = tw.s();
      return this.M && var2.K() ? var2.U(var1.comp_2315()) : q7.u().c(var1);
   }

   private int B(class_310 var1, q7 var2, int var3) {
      for (int var4 = 0; var4 < var1.field_1724.method_31548().method_5439(); var4++) {
         class_1799 var5 = var1.field_1724.method_31548().method_5438(var4);
         if (var5 != null && !var5.method_7960() && var5.method_31574(class_1802.field_8204)) {
            class_9209 var6 = (class_9209)var5.method_57353().method_58694(class_9334.field_49646);
            if (var6 != null && var6.comp_2315() == var3) {
               if (hu.f().E(hu.q.k)) {
                  return this.k(var6);
               }

               if (tw.s().j()) {
                  return var2.c(var6);
               }

               return var2.M("inventory[" + var4 + "]", var5);
            }
         }
      }

      if (var1.field_1687 != null) {
         for (class_1297 var10 : var1.field_1687.method_18112()) {
            if (var10 instanceof class_1533 var11) {
               class_1799 var7 = var11.method_6940();
               if (var7 != null && !var7.method_7960() && var7.method_31574(class_1802.field_8204)) {
                  class_9209 var8 = (class_9209)var7.method_57353().method_58694(class_9334.field_49646);
                  if (var8 != null && var8.comp_2315() == var3) {
                     if (hu.f().E(hu.q.k)) {
                        return this.k(var8);
                     }

                     if (tw.s().j()) {
                        return var2.c(var8);
                     }

                     return var2.M("item_frame[" + var11.method_5628() + "]", var7);
                  }
               }
            }
         }
      }

      return -1;
   }

   private int Q(Map<Integer, Integer> var1) {
      int var2 = 0;

      for (Entry var4 : var1.entrySet()) {
         var2 += Math.max(0, (Integer)var4.getValue() - this.e.getOrDefault(var4.getKey(), 0));
      }

      return var2;
   }

   private void J() {
      q7.u().U();
      this.H.clear();
      this.x.clear();
      this.e = null;
      this.w.clear();
      this.i.clear();
      this.y = 0;
      this.m = 0;
      this.u = 0;
      this.F = 0;
      this.D = (boolean)0;
      this.M = (boolean)0;
      this.N = 0;
      this.G = -1;
      this.h = -1;
   }

   public int R() {
      return this.T();
   }

   public int Y() {
      return M(this.x).total;
   }

   public boolean u() {
      return this.n == l4.t.R;
   }

   public int j(boolean[] var1) {
      if (!this.u() && this.N >= this.z * 2 + 1 && this.F <= 0) {
         List var2 = this.v();
         int var3 = M(var2).total;
         return var3 >= 12 && var3 < 17 ? W(var2, var1) : -1;
      } else {
         return -1;
      }
   }

   public boolean l(int var1, int var2) {
      return R(this.v(), var1, var2);
   }

   public static boolean R(List<Integer> var0, int var1, int var2) {
      if (!var0.isEmpty() && var1 >= 1 && var1 <= 10 && var2 >= 1 && var2 <= 10) {
         int var3 = l(var0, var1).total;
         int var4 = l(var0, var2).total;
         return var4 <= 21 && (var3 < 17 || var4 >= 17);
      } else {
         return false;
      }
   }

   public static int W(List<Integer> var0, boolean[] var1) {
      if (!var0.isEmpty() && var1.length >= 11) {
         ArrayList var2 = new ArrayList();
         ArrayList var3 = new ArrayList();

         for (int var4 = 1; var4 <= 10; var4++) {
            if (var1[var4]) {
               int var5 = l(var0, var4).total;
               if (var5 >= 17 && var5 <= 21) {
                  var2.add(var4);
               } else if (var5 < 17) {
                  var3.add(var4);
               }
            }
         }

         ArrayList var6 = var2.isEmpty() ? var3 : var2;
         return var6.isEmpty() ? -1 : (Integer)var6.get(ThreadLocalRandom.current().nextInt(var6.size()));
      } else {
         return -1;
      }
   }

   public int V(boolean[] var1) {
      if (this.u() && this.N >= this.z * 2 + 1 && this.F <= 0) {
         int var2 = 0;

         for (List var4 : this.H) {
            int var5 = M(var4).total;
            if (var5 <= 21) {
               var2 = Math.max(var2, var5);
            }
         }

         return q(var2, this.x, var1);
      } else {
         return -1;
      }
   }

   public static int q(int var0, List<Integer> var1, boolean[] var2) {
      int var3 = M(var1).total;
      if (var0 >= 2 && var0 <= 21 && !var1.isEmpty() && var2.length >= 11 && var3 < 17) {
         if (var0 >= 18) {
            for (int var4 = 1; var4 <= 10; var4++) {
               if (var2[var4] && l(var1, var4).total == 17) {
                  return var4;
               }
            }
         }

         ArrayList var16 = new ArrayList();
         int var5 = -1;
         int var6 = 21;

         for (int var7 = 1; var7 <= 10; var7++) {
            if (var2[var7]) {
               int var8 = l(var1, var7).total;
               if (var8 > var6) {
                  var5 = var7;
                  var6 = var8;
               }

               if (var8 >= 24) {
                  var16.add(var7);
               }
            }
         }

         int var17 = var16.isEmpty() ? var5 : (Integer)var16.get(ThreadLocalRandom.current().nextInt(var16.size()));
         if (var17 <= 0 || var3 >= 12 && var3 <= 15) {
            int var18 = -1;
            int var9 = -1;
            ArrayList var10 = new ArrayList();
            ArrayList var11 = new ArrayList();
            boolean[] var12 = (boolean[])var2.clone();

            for (int var13 = 1; var13 <= 10; var13++) {
               if (var12[var13]) {
                  int var14 = l(var1, var13).total;
                  if (var14 < 17) {
                     var12[var13] = (boolean)0;
                     ArrayList var15 = new ArrayList(var1);
                     var15.add(var13);
                     if (R(var15, var12)) {
                        if (var14 >= 12) {
                           var10.add(var13);
                        }

                        if (var14 > var3) {
                           var11.add(var13);
                        }

                        if (var14 > var9) {
                           var18 = var13;
                           var9 = var14;
                        }
                     }

                     var12[var13] = (boolean)1;
                  }
               }
            }

            if (var17 <= 0) {
               return var10.isEmpty() ? var18 : (Integer)var10.get(ThreadLocalRandom.current().nextInt(var10.size()));
            } else {
               return !var11.isEmpty() && ThreadLocalRandom.current().nextBoolean()
                  ? (Integer)var11.get(ThreadLocalRandom.current().nextInt(var11.size()))
                  : var17;
            }
         } else {
            return var17;
         }
      } else {
         return -1;
      }
   }

   private static boolean R(List<Integer> var0, boolean[] var1) {
      for (int var2 = 1; var2 <= 10; var2++) {
         if (var1[var2]) {
            int var3 = l(var0, var2).total;
            if (var3 > 21) {
               return true;
            }

            if (var3 < 17) {
               var1[var2] = (boolean)0;
               ArrayList var4 = new ArrayList(var0);
               var4.add(var2);
               boolean var5 = R(var4, var1);
               var1[var2] = (boolean)1;
               if (var5) {
                  return true;
               }
            }
         }
      }

      return false;
   }

   public boolean i() {
      for (List var2 : this.H) {
         if (M(var2).soft) {
            return true;
         }
      }

      return false;
   }

   public boolean c() {
      return M(this.x).soft;
   }

   public int D() {
      return this.z;
   }

   public int F(int var1) {
      return var1 >= 0 && var1 < this.H.size() ? M(this.H.get(var1)).total : 0;
   }

   public int x(int var1) {
      if (var1 >= 0 && var1 < this.H.size()) {
         return M(this.H.get(var1)).soft ? 1 : 0;
      } else {
         return 0;
      }
   }

   public void B(int var1) {
      int var2 = Math.max(1, Math.min(3, var1));
      if (this.z != var2) {
         this.z = var2;
         this.J();
         this.g = 0;
      }
   }

   public void X() {
      this.G = -1;
      this.h = -1;
      if (this.n == l4.t.R) {
         this.n = l4.t.z;
         this.g = 0;
      } else {
         int var1 = this.g + 1;
         if (var1 >= this.z) {
            this.n = l4.t.R;
         } else {
            this.g = var1;
         }
      }
   }

   public void M(String var1) {
      if ("dealer".equalsIgnoreCase(var1)) {
         this.n = l4.t.R;
      } else if ("player".equalsIgnoreCase(var1)) {
         this.n = l4.t.z;
         this.g = 0;
      }
   }

   public void h(int var1) {
      l4.t var2 = var1 < 0 ? l4.t.R : l4.t.z;
      int var3 = var1 < 0 ? this.g : Math.min(var1, this.z - 1);
      if (var2 != this.n || var3 != this.g) {
         this.n = var2;
         this.g = var3;
         this.G = -1;
         this.h = -1;
      }
   }

   public int k() {
      return this.n == l4.t.R ? -1 : this.g;
   }

   public void s(String var1) {
      this.b = "player".equalsIgnoreCase(var1) ? l4.t.z : ("dealer".equalsIgnoreCase(var1) ? l4.t.R : null);
   }

   public boolean F() {
      return this.b == null;
   }

   public String Z() {
      if (this.n == l4.t.R) {
         return "Dealer";
      } else {
         return this.z == 1 ? "Player" : "Hand " + (this.g + 1);
      }
   }

   public String a(int var1) {
      if (var1 < 0) {
         return this.x.isEmpty() ? "-" : a(this.x);
      } else {
         return var1 < this.H.size() && !this.H.get(var1).isEmpty() ? a(this.H.get(var1)) : "-";
      }
   }

   private static String a(List<Integer> var0) {
      l4.r var1 = M(var0);
      return var1.total + (var1.total > 21 ? " bust" : (var1.soft ? " soft" : ""));
   }

   public String s() {
      return switch (this.n) {
         case z -> "HAND " + (this.g + 1);
         case R -> "DEALER";
      };
   }

   public String h() {
      return switch (this.n) {
         case z -> "hand-" + this.g;
         case R -> "dealer";
      };
   }

   public int x() {
      return this.G;
   }

   public int K() {
      return this.h;
   }

   public String T() {
      return this.G < 1 ? "-1" : this.A();
   }

   public String n() {
      return this.G < 1 ? "none" : this.A();
   }

   private String A() {
      StringBuilder var1 = new StringBuilder(w(this.G));
      if (this.h >= 1) {
         var1.append(" -> ").append(w(this.h));
      }

      return var1.toString();
   }

   private static String w(int var0) {
      return switch (var0) {
         case 1 -> "A";
         case 10 -> "10";
         default -> String.valueOf(var0);
      };
   }

   public int S() {
      return this.N;
   }

   public int X() {
      return this.F;
   }

   public boolean k() {
      return this.d;
   }

   private static l4.r l(List<Integer> var0, int var1) {
      ArrayList var2 = new ArrayList(var0);
      var2.add(var1);
      return M(var2);
   }

   private static l4.r M(List<Integer> var0) {
      int var1 = 0;
      int var2 = 0;

      for (int var4 : var0) {
         if (var4 == 1) {
            var2++;
            var1 += 11;
         } else {
            var1 += Math.min(10, Math.max(2, var4));
         }
      }

      while (var1 > 21 && var2 > 0) {
         var1 -= 10;
         var2--;
      }

      return new l4.r(var1, var2 > 0);
   }

   private record r(int total, boolean soft) {
   }

   private enum t {
      z,
      R;
   }
}
