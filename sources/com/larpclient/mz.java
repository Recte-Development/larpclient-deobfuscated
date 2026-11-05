package com.larpclient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ThreadLocalRandom;
import net.minecraft.client.Minecraft;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.saveddata.maps.MapId;

public final class mz {
   private static final mz r = new mz();
   private static final p6 y = p6.r("BJAssistant");
   private final List<List<Integer>> D = new ArrayList<>();
   private final List<Integer> o = new ArrayList<>();
   private int x = 1;
   private int k;
   private mz.i V;
   private int n = -1;
   private Map<Integer, Integer> p;
   private Map<Integer, Integer> z = new HashMap<>();
   private Map<Integer, Integer> Y = new HashMap<>();
   private int S;
   private mz.i U = mz.i.i;
   private final long t = System.currentTimeMillis();
   private int j;
   private int L;
   private int K;
   private boolean I;
   private boolean v;
   private boolean q = (boolean)1;
   private boolean s;
   private long T;
   private int N;
   private int H = -1;

   private mz() {
   }

   public static mz F() {
      return r;
   }

   public void L(boolean var1) {
      this.s = var1;
   }

   public void x(Minecraft var1) {
      this.s(var1, false);
   }

   public void s(Minecraft var1, boolean var2) {
      if (!this.I) {
         ug.w().T();
         this.D.clear();
         this.o.clear();
         this.N = 0;
         this.H = -1;
         this.n = -1;
         this.j = 0;
         this.L = 0;
         this.I = (boolean)1;
         this.v = var2;
         if (this.p == null && var1 != null && var1.player != null) {
            this.p = this.C(var1);
            this.z = new HashMap<>(this.p);
            this.Y = new HashMap<>(this.p);
            this.S = 0;
         }
      }

      this.K++;
   }

   public void l(Minecraft var1, ug var2, boolean var3) {
      if (!var3 || var1 == null || var1.player == null) {
         this.x();
      } else if (var2 == null || !var2.isSwapBusy()) {
         Map var4 = this.C(var1);
         if (!var4.equals(this.Y)) {
            this.Y = new HashMap<>(var4);
            this.S = 0;
         } else {
            this.S++;
         }

         if (this.p == null) {
            this.p = new HashMap<>(var4);
            this.z = new HashMap<>(var4);
            this.Y = new HashMap<>(var4);
         } else if (!this.I) {
            this.p = new HashMap<>(var4);
            this.z = new HashMap<>(var4);
            this.Y = new HashMap<>(var4);
            this.j = 0;
            this.L = 0;
         } else {
            int var5 = this.A(var4);
            if (this.S >= 2 && var5 > this.j) {
               int var6 = var5 - this.j;
               int var7 = Math.min(var6, this.K);
               if (var6 > var7) {
                  hh.L("BJ hand ignored duplicate map-count rise=" + var6 + " activations=" + this.K);
               }

               if (var7 != 0) {
                  int var8 = 0;

                  for (Entry var10 : var4.entrySet()) {
                     int var11 = Math.max(0, (Integer)var10.getValue() - this.z.getOrDefault(var10.getKey(), 0));

                     while (var11-- > 0 && var8 < var7) {
                        int var12 = this.J(var1, var2, (Integer)var10.getKey());
                        if (var12 >= 1 && var12 <= 10) {
                           var2.f(var12, (Integer)var10.getKey());
                           this.Y(var12);
                           this.K--;
                           var8++;
                           this.z.merge((Integer)var10.getKey(), 1, Integer::sum);
                        }
                     }

                     if (var8 >= var7) {
                        break;
                     }
                  }

                  if (var8 > 0) {
                     this.j += var8;
                  }
               }
            }

            this.L = var5 <= 0 && !mm.W().x() ? this.L + 1 : 0;
            if (this.N > 0 && this.L >= 1 && this.L % 5 == 1) {
               y.h("BJ {} reset-watch dealtCards={} extras={} emptyHandTicks={} busy={}", this.m(), this.N, var5, this.L, var2 != null && var2.isSwapBusy());
            }

            if (this.N > 0 && this.L >= 6) {
               y.h("BJ {} round reset (cards returned)", this.m());
               this.D.clear();
               this.o.clear();
               this.N = 0;
               this.H = -1;
               this.n = -1;
               this.p = new HashMap<>(var4);
               this.z = new HashMap<>(var4);
               this.Y = new HashMap<>(var4);
               this.j = 0;
               this.L = 0;
               this.K = 0;
               this.I = (boolean)0;
               if (this.U != mz.i.i || this.k != 0) {
                  this.U = mz.i.i;
                  this.k = 0;
                  hh.K("Blackjack", "Round over; side reset to hand 1");
               }
            }

            if (mm.W().I()) {
               this.I();
            }

            int var13 = this.x * 2 + 1;
            if (this.N >= var13 && this.U == mz.i.f) {
               int[] var14 = this.D(var2);
               if (var14 == null) {
                  this.H = -1;
                  this.n = -1;
               } else {
                  this.H = var14[0];
                  this.n = var14[1];
               }
            } else {
               this.H = -1;
               this.n = -1;
            }

            this.w(var2);
         }
      }
   }

   public boolean K(int var1, int var2) {
      if (this.U != mz.i.f) {
         return true;
      } else if (this.D.isEmpty()) {
         return true;
      } else {
         int var3 = this.o();
         if (var3 < 0) {
            return false;
         } else if (var3 >= 2 && var1 >= 1 && var2 >= 1) {
            int var4 = V(this.o, var1).total;
            return var4 < 17 || var4 > 21 || var4 <= var3;
         } else {
            return true;
         }
      }
   }

   private void w(ug var1) {
      if (var1 != null && !var1.isSwapBusy() && this.s) {
         if (this.H >= 1) {
            int var2 = var1.getPendingValue();
            if (var2 != this.H || var1.getQueuedValueAt(1) != this.n) {
               var1.queueForceSequence(this.H, this.n);
               hh.L("SRIG " + this.m() + " armed plan rec=" + (this.n >= 1 ? this.H + "->" + this.n : this.H));
            }
         } else if (var1.hasQueuedForce()) {
            var1.clearQueue();
            hh.L("SRIG " + this.m() + " cleared plan (no recommendation)");
         }
      }
   }

   private void Y(int var1) {
      int var2 = this.x;
      if (this.N < var2) {
         this.t(this.N, var1);
      } else if (this.N == var2) {
         this.o.add(var1);
      } else if (this.N <= var2 * 2) {
         this.t(this.N - var2 - 1, var1);
      } else if (this.U == mz.i.i) {
         this.t(this.k, var1);
      } else {
         this.o.add(var1);
      }

      this.N++;
   }

   private void t(int var1, int var2) {
      while (this.D.size() <= var1) {
         this.D.add(new ArrayList<>());
      }

      this.D.get(var1).add(var2);
   }

   private int[] D(ug var1) {
      int var2 = this.U == mz.i.f ? 1 : 0;
      List var3 = var2 != 0 ? this.o : this.f();
      if (var3.isEmpty()) {
         return null;
      }

      int var4 = var2 != 0 ? this.o() : I(this.o).total;
      if (var2 != 0 && var4 < 0) {
         return null;
      }

      if (var2 != 0 && I(var3).total >= 17) {
         return null;
      }

      int var5 = -1;
      int var6 = -1;

      for (int var7 = 1; var7 <= 10; var7++) {
         if (var1.getTemplateForValue(var7) != null) {
            int var8 = V(var3, var7).total;
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
            int var15 = V(var3, var12).total;
            if (var15 <= 21 && (var2 == 0 || var15 < 17)) {
               ArrayList var9 = new ArrayList(var3);
               var9.add(var12);

               for (int var10 = 1; var10 <= 10; var10++) {
                  if (var1.getTemplateForValue(var10) != null) {
                     int var11 = V(var9, var10).total;
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
            int var16 = V(var3, var13).total;
            if (var16 <= 21 && (var2 == 0 || var16 < 17)) {
               ArrayList var17 = new ArrayList(var3);
               var17.add(var13);

               for (int var18 = 1; var18 <= 10; var18++) {
                  if (var1.getTemplateForValue(var18) != null) {
                     int var19 = V(var17, var18).total;
                     if (var19 <= 21 && var19 >= 17 && var19 == var4) {
                        return new int[]{var13, var18};
                     }
                  }
               }
            }
         }
      }

      int var14 = var2 != 0 ? this.G(var1, var3, var4) : this.N(var1, var3, var4);
      return var14 >= 1 ? new int[]{var14, -1} : null;
   }

   private List<Integer> f() {
      return this.k >= 0 && this.k < this.D.size() ? this.D.get(this.k) : new ArrayList<>();
   }

   private int N(ug var1, List<Integer> var2, int var3) {
      ArrayList var4 = new ArrayList();
      ArrayList var5 = new ArrayList();

      for (int var6 = 1; var6 <= 10; var6++) {
         if (var1.getTemplateForValue(var6) != null) {
            int var7 = V(var2, var6).total;
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
         return !var5.isEmpty() ? (Integer)var5.get(ThreadLocalRandom.current().nextInt(var5.size())) : this.W(var1, var2);
      }
   }

   private int P() {
      int var1 = 0;

      for (List var3 : this.D) {
         var1 = Math.max(var1, I(var3).total);
      }

      return var1;
   }

   private void I() {
      int var1 = -1;

      for (int var2 = 0; var2 < this.x && var1 < 0; var2++) {
         if (var2 >= this.D.size() || I(this.D.get(var2)).total < 17) {
            var1 = var2;
         }
      }

      if (var1 >= 0) {
         this.k = var1;
      }

      this.U = this.V != null ? this.V : (var1 < 0 ? mz.i.f : mz.i.i);
   }

   private int o() {
      int var1 = -1;

      for (List var3 : this.D) {
         int var4 = I(var3).total;
         if (var4 <= 21) {
            var1 = Math.max(var1, var4);
         }
      }

      return var1;
   }

   private int G(ug var1, List<Integer> var2, int var3) {
      int var4 = I(var2).total;
      if (var4 >= 17) {
         return -1;
      }

      ArrayList var5 = new ArrayList();
      ArrayList var6 = new ArrayList();

      for (int var7 = 1; var7 <= 10; var7++) {
         if (var1.getTemplateForValue(var7) != null) {
            int var8 = V(var2, var7).total;
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
                  int var16 = V(var2, var14).total;
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

         return !var6.isEmpty() ? (Integer)var6.get(ThreadLocalRandom.current().nextInt(var6.size())) : this.W(var1, var2);
      }
   }

   private int W(ug var1, List<Integer> var2) {
      int var3 = -1;
      int var4 = -1;

      for (int var5 = 1; var5 <= 10; var5++) {
         if (var1.getTemplateForValue(var5) != null) {
            int var6 = V(var2, var5).total;
            if (var6 <= 21 && var6 > var4) {
               var4 = var6;
               var3 = var5;
            }
         }
      }

      return var3;
   }

   private Map<Integer, Integer> C(Minecraft var1) {
      HashMap var2 = new HashMap();
      mm var3 = mm.W();

      for (int var4 = 0; var4 < var1.player.getInventory().getContainerSize(); var4++) {
         ItemStack var5 = var1.player.getInventory().getItem(var4);
         int var6 = var3.v(var4, var5);
         if (var6 < var5.getCount()) {
            this.G(var2, var6 == 0 ? var5 : var5.copyWithCount(var5.getCount() - var6));
         }
      }

      if (var1.level != null) {
         for (Entity var9 : var1.level.entitiesForRendering()) {
            if (var9 instanceof ItemFrame var10) {
               this.G(var2, var10.getItem());
            } else if (var9 instanceof ItemEntity var7) {
               this.G(var2, var7.getItem());
            }
         }
      }

      return var2;
   }

   private void G(Map<Integer, Integer> var1, ItemStack var2) {
      if (var2 != null && !var2.isEmpty() && var2.is(Items.FILLED_MAP)) {
         MapId var3 = (MapId)var2.getComponents().get(DataComponents.MAP_ID);
         if (var3 != null && this.T(var3) >= 1) {
            var1.merge(var3.id(), var2.getCount(), Integer::sum);
         }
      }
   }

   private int T(MapId var1) {
      mm var2 = mm.W();
      return this.v && var2.S() ? var2.d(var1.id()) : ug.w().u(var1);
   }

   private int J(Minecraft var1, ug var2, int var3) {
      for (int var4 = 0; var4 < var1.player.getInventory().getContainerSize(); var4++) {
         ItemStack var5 = var1.player.getInventory().getItem(var4);
         if (var5 != null && !var5.isEmpty() && var5.is(Items.FILLED_MAP)) {
            MapId var6 = (MapId)var5.getComponents().get(DataComponents.MAP_ID);
            if (var6 != null && var6.id() == var3) {
               if (f9.W().f(f9.b.P)) {
                  return this.T(var6);
               }

               if (mm.W().I()) {
                  return var2.u(var6);
               }

               return var2.n("inventory[" + var4 + "]", var5);
            }
         }
      }

      if (var1.level != null) {
         for (Entity var10 : var1.level.entitiesForRendering()) {
            if (var10 instanceof ItemFrame var11) {
               ItemStack var7 = var11.getItem();
               if (var7 != null && !var7.isEmpty() && var7.is(Items.FILLED_MAP)) {
                  MapId var8 = (MapId)var7.getComponents().get(DataComponents.MAP_ID);
                  if (var8 != null && var8.id() == var3) {
                     if (f9.W().f(f9.b.P)) {
                        return this.T(var8);
                     }

                     if (mm.W().I()) {
                        return var2.u(var8);
                     }

                     return var2.n("item_frame[" + var11.getId() + "]", var7);
                  }
               }
            }
         }
      }

      return -1;
   }

   private int A(Map<Integer, Integer> var1) {
      int var2 = 0;

      for (Entry var4 : var1.entrySet()) {
         var2 += Math.max(0, (Integer)var4.getValue() - this.p.getOrDefault(var4.getKey(), 0));
      }

      return var2;
   }

   private void x() {
      ug.w().T();
      this.D.clear();
      this.o.clear();
      this.p = null;
      this.z.clear();
      this.Y.clear();
      this.S = 0;
      this.j = 0;
      this.L = 0;
      this.K = 0;
      this.I = (boolean)0;
      this.v = (boolean)0;
      this.N = 0;
      this.H = -1;
      this.n = -1;
   }

   public int Y() {
      return this.P();
   }

   public int h() {
      return I(this.o).total;
   }

   public boolean A() {
      return this.U == mz.i.f;
   }

   public int r(boolean[] var1) {
      if (!this.A() && this.N >= this.x * 2 + 1 && this.K <= 0) {
         List var2 = this.f();
         int var3 = I(var2).total;
         return var3 >= 12 && var3 < 17 ? u(var2, var1) : -1;
      } else {
         return -1;
      }
   }

   public boolean O(int var1, int var2) {
      return n(this.f(), var1, var2);
   }

   public static boolean n(List<Integer> var0, int var1, int var2) {
      if (!var0.isEmpty() && var1 >= 1 && var1 <= 10 && var2 >= 1 && var2 <= 10) {
         int var3 = V(var0, var1).total;
         int var4 = V(var0, var2).total;
         return var4 <= 21 && (var3 < 17 || var4 >= 17);
      } else {
         return false;
      }
   }

   public static int u(List<Integer> var0, boolean[] var1) {
      if (!var0.isEmpty() && var1.length >= 11) {
         ArrayList var2 = new ArrayList();
         ArrayList var3 = new ArrayList();

         for (int var4 = 1; var4 <= 10; var4++) {
            if (var1[var4]) {
               int var5 = V(var0, var4).total;
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

   public int t(boolean[] var1) {
      if (this.A() && this.N >= this.x * 2 + 1 && this.K <= 0) {
         int var2 = 0;

         for (List var4 : this.D) {
            int var5 = I(var4).total;
            if (var5 <= 21) {
               var2 = Math.max(var2, var5);
            }
         }

         return k(var2, this.o, var1);
      } else {
         return -1;
      }
   }

   public static int k(int var0, List<Integer> var1, boolean[] var2) {
      int var3 = I(var1).total;
      if (var0 >= 2 && var0 <= 21 && !var1.isEmpty() && var2.length >= 11 && var3 < 17) {
         if (var0 >= 18) {
            for (int var4 = 1; var4 <= 10; var4++) {
               if (var2[var4] && V(var1, var4).total == 17) {
                  return var4;
               }
            }
         }

         ArrayList var16 = new ArrayList();
         int var5 = -1;
         int var6 = 21;

         for (int var7 = 1; var7 <= 10; var7++) {
            if (var2[var7]) {
               int var8 = V(var1, var7).total;
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
                  int var14 = V(var1, var13).total;
                  if (var14 < 17) {
                     var12[var13] = (boolean)0;
                     ArrayList var15 = new ArrayList(var1);
                     var15.add(var13);
                     if (q(var15, var12)) {
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

   private static boolean q(List<Integer> var0, boolean[] var1) {
      for (int var2 = 1; var2 <= 10; var2++) {
         if (var1[var2]) {
            int var3 = V(var0, var2).total;
            if (var3 > 21) {
               return true;
            }

            if (var3 < 17) {
               var1[var2] = (boolean)0;
               ArrayList var4 = new ArrayList(var0);
               var4.add(var2);
               boolean var5 = q(var4, var1);
               var1[var2] = (boolean)1;
               if (var5) {
                  return true;
               }
            }
         }
      }

      return false;
   }

   public boolean x() {
      for (List var2 : this.D) {
         if (I(var2).soft) {
            return true;
         }
      }

      return false;
   }

   public boolean a() {
      return I(this.o).soft;
   }

   public int B() {
      return this.x;
   }

   public int A(int var1) {
      return var1 >= 0 && var1 < this.D.size() ? I(this.D.get(var1)).total : 0;
   }

   public int O(int var1) {
      if (var1 >= 0 && var1 < this.D.size()) {
         return I(this.D.get(var1)).soft ? 1 : 0;
      } else {
         return 0;
      }
   }

   public void t(int var1) {
      int var2 = Math.max(1, Math.min(3, var1));
      if (this.x != var2) {
         this.x = var2;
         this.x();
         this.k = 0;
      }
   }

   public void J() {
      this.H = -1;
      this.n = -1;
      if (this.U == mz.i.f) {
         this.U = mz.i.i;
         this.k = 0;
      } else {
         int var1 = this.k + 1;
         if (var1 >= this.x) {
            this.U = mz.i.f;
         } else {
            this.k = var1;
         }
      }
   }

   public void A(String var1) {
      if ("dealer".equalsIgnoreCase(var1)) {
         this.U = mz.i.f;
      } else if ("player".equalsIgnoreCase(var1)) {
         this.U = mz.i.i;
         this.k = 0;
      }
   }

   public void T(int var1) {
      mz.i var2 = var1 < 0 ? mz.i.f : mz.i.i;
      int var3 = var1 < 0 ? this.k : Math.min(var1, this.x - 1);
      if (var2 != this.U || var3 != this.k) {
         this.U = var2;
         this.k = var3;
         this.H = -1;
         this.n = -1;
      }
   }

   public int Z() {
      return this.U == mz.i.f ? -1 : this.k;
   }

   public void n(String var1) {
      this.V = "player".equalsIgnoreCase(var1) ? mz.i.i : ("dealer".equalsIgnoreCase(var1) ? mz.i.f : null);
   }

   public boolean n() {
      return this.V == null;
   }

   public String e() {
      if (this.U == mz.i.f) {
         return "Dealer";
      } else {
         return this.x == 1 ? "Player" : "Hand " + (this.k + 1);
      }
   }

   public String I(int var1) {
      if (var1 < 0) {
         return this.o.isEmpty() ? "-" : x(this.o);
      } else {
         return var1 < this.D.size() && !this.D.get(var1).isEmpty() ? x(this.D.get(var1)) : "-";
      }
   }

   private static String x(List<Integer> var0) {
      mz.y var1 = I(var0);
      return var1.total + (var1.total > 21 ? " bust" : (var1.soft ? " soft" : ""));
   }

   public String P() {
      return switch (this.U) {
         case i -> "HAND " + (this.k + 1);
         case f -> "DEALER";
      };
   }

   public String m() {
      return switch (this.U) {
         case i -> "hand-" + this.k;
         case f -> "dealer";
      };
   }

   public int N() {
      return this.H;
   }

   public int u() {
      return this.n;
   }

   public String J() {
      return this.H < 1 ? "-1" : this.l();
   }

   public String K() {
      return this.H < 1 ? "none" : this.l();
   }

   private String l() {
      StringBuilder var1 = new StringBuilder(A(this.H));
      if (this.n >= 1) {
         var1.append(" -> ").append(A(this.n));
      }

      return var1.toString();
   }

   private static String A(int var0) {
      return switch (var0) {
         case 1 -> "A";
         case 10 -> "10";
         default -> String.valueOf(var0);
      };
   }

   public int G() {
      return this.N;
   }

   public int K() {
      return this.K;
   }

   public boolean R() {
      return this.q;
   }

   private static mz.y V(List<Integer> var0, int var1) {
      ArrayList var2 = new ArrayList(var0);
      var2.add(var1);
      return I(var2);
   }

   private static mz.y I(List<Integer> var0) {
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

      return new mz.y(var1, var2 > 0);
   }

   private enum i {
      i,
      f;
   }

   private record y(int total, boolean soft) {
   }
}
