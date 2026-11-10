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

public final class s3 {
   private static final s3 a = new s3();
   private static final zw Y = zw.k("BJAssistant");
   private final List<List<Integer>> B = new ArrayList<>();
   private final List<Integer> R = new ArrayList<>();
   private int V = 1;
   private int t;
   private s3.d x;
   private int v = -1;
   private Map<Integer, Integer> k;
   private Map<Integer, Integer> P = new HashMap<>();
   private Map<Integer, Integer> X = new HashMap<>();
   private int r;
   private s3.d j = s3.d.n;
   private final long q = System.currentTimeMillis();
   private int M;
   private int i;
   private int f;
   private boolean c;
   private boolean y;
   private boolean I = (boolean)1;
   private boolean S;
   private long b;
   private int J;
   private int U = -1;

   private s3() {
   }

   public static s3 S() {
      return a;
   }

   public void s(boolean var1) {
      this.S = var1;
   }

   public void U(Minecraft var1) {
      this.p(var1, false);
   }

   public void p(Minecraft var1, boolean var2) {
      if (!this.c) {
         we.u().h();
         this.B.clear();
         this.R.clear();
         this.J = 0;
         this.U = -1;
         this.v = -1;
         this.M = 0;
         this.i = 0;
         this.c = (boolean)1;
         this.y = var2;
         if (this.k == null && var1 != null && var1.player != null) {
            this.k = this.W(var1);
            this.P = new HashMap<>(this.k);
            this.X = new HashMap<>(this.k);
            this.r = 0;
         }
      }

      this.f++;
   }

   public void P(Minecraft var1, we var2, boolean var3) {
      if (!var3 || var1 == null || var1.player == null) {
         this.v();
      } else if (var2 == null || !var2.isSwapBusy()) {
         Map var4 = this.W(var1);
         if (!var4.equals(this.X)) {
            this.X = new HashMap<>(var4);
            this.r = 0;
         } else {
            this.r++;
         }

         if (this.k == null) {
            this.k = new HashMap<>(var4);
            this.P = new HashMap<>(var4);
            this.X = new HashMap<>(var4);
         } else if (!this.c) {
            this.k = new HashMap<>(var4);
            this.P = new HashMap<>(var4);
            this.X = new HashMap<>(var4);
            this.M = 0;
            this.i = 0;
         } else {
            int var5 = this.k(var4);
            if (this.r >= 2 && var5 > this.M) {
               int var6 = var5 - this.M;
               int var7 = Math.min(var6, this.f);
               if (var6 > var7) {
                  te.R("BJ hand ignored duplicate map-count rise=" + var6 + " activations=" + this.f);
               }

               if (var7 != 0) {
                  int var8 = 0;

                  for (Entry var10 : var4.entrySet()) {
                     int var11 = Math.max(0, (Integer)var10.getValue() - this.P.getOrDefault(var10.getKey(), 0));

                     while (var11-- > 0 && var8 < var7) {
                        int var12 = this.X(var1, var2, (Integer)var10.getKey());
                        if (var12 >= 1 && var12 <= 10) {
                           var2.D(var12, (Integer)var10.getKey());
                           this.p(var12);
                           this.f--;
                           var8++;
                           this.P.merge((Integer)var10.getKey(), 1, Integer::sum);
                        }
                     }

                     if (var8 >= var7) {
                        break;
                     }
                  }

                  if (var8 > 0) {
                     this.M += var8;
                  }
               }
            }

            this.i = var5 <= 0 && !lh.I().k() ? this.i + 1 : 0;
            if (this.J > 0 && this.i >= 1 && this.i % 5 == 1) {
               Y.t("BJ {} reset-watch dealtCards={} extras={} emptyHandTicks={} busy={}", this.N(), this.J, var5, this.i, var2 != null && var2.isSwapBusy());
            }

            if (this.J > 0 && this.i >= 6) {
               Y.t("BJ {} round reset (cards returned)", this.N());
               this.B.clear();
               this.R.clear();
               this.J = 0;
               this.U = -1;
               this.v = -1;
               this.k = new HashMap<>(var4);
               this.P = new HashMap<>(var4);
               this.X = new HashMap<>(var4);
               this.M = 0;
               this.i = 0;
               this.f = 0;
               this.c = (boolean)0;
               if (this.j != s3.d.n || this.t != 0) {
                  this.j = s3.d.n;
                  this.t = 0;
                  te.Z("Blackjack", "Round over; side reset to hand 1");
               }
            }

            if (lh.I().S()) {
               this.O();
            }

            int var13 = this.V * 2 + 1;
            if (this.J >= var13 && this.j == s3.d.l) {
               int[] var14 = this.m(var2);
               if (var14 == null) {
                  this.U = -1;
                  this.v = -1;
               } else {
                  this.U = var14[0];
                  this.v = var14[1];
               }
            } else {
               this.U = -1;
               this.v = -1;
            }

            this.t(var2);
         }
      }
   }

   public boolean T(int var1, int var2) {
      if (this.j != s3.d.l) {
         return true;
      } else if (this.B.isEmpty()) {
         return true;
      } else {
         int var3 = this.m();
         if (var3 < 0) {
            return false;
         } else if (var3 >= 2 && var1 >= 1 && var2 >= 1) {
            int var4 = s(this.R, var1).total;
            return var4 < 17 || var4 > 21 || var4 <= var3;
         } else {
            return true;
         }
      }
   }

   private void t(we var1) {
      if (var1 != null && !var1.isSwapBusy() && this.S) {
         if (this.U >= 1) {
            int var2 = var1.getPendingValue();
            if (var2 != this.U || var1.getQueuedValueAt(1) != this.v) {
               var1.queueForceSequence(this.U, this.v);
               te.R("SRIG " + this.N() + " armed plan rec=" + (this.v >= 1 ? this.U + "->" + this.v : this.U));
            }
         } else if (var1.hasQueuedForce()) {
            var1.clearQueue();
            te.R("SRIG " + this.N() + " cleared plan (no recommendation)");
         }
      }
   }

   private void p(int var1) {
      int var2 = this.V;
      if (this.J < var2) {
         this.Y(this.J, var1);
      } else if (this.J == var2) {
         this.R.add(var1);
      } else if (this.J <= var2 * 2) {
         this.Y(this.J - var2 - 1, var1);
      } else if (this.j == s3.d.n) {
         this.Y(this.t, var1);
      } else {
         this.R.add(var1);
      }

      this.J++;
   }

   private void Y(int var1, int var2) {
      while (this.B.size() <= var1) {
         this.B.add(new ArrayList<>());
      }

      this.B.get(var1).add(var2);
   }

   private int[] m(we var1) {
      int var2 = this.j == s3.d.l ? 1 : 0;
      List var3 = var2 != 0 ? this.R : this.F();
      if (var3.isEmpty()) {
         return null;
      }

      int var4 = var2 != 0 ? this.m() : b(this.R).total;
      if (var2 != 0 && var4 < 0) {
         return null;
      }

      if (var2 != 0 && b(var3).total >= 17) {
         return null;
      }

      int var5 = -1;
      int var6 = -1;

      for (int var7 = 1; var7 <= 10; var7++) {
         if (var1.getTemplateForValue(var7) != null) {
            int var8 = s(var3, var7).total;
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
            int var15 = s(var3, var12).total;
            if (var15 <= 21 && (var2 == 0 || var15 < 17)) {
               ArrayList var9 = new ArrayList(var3);
               var9.add(var12);

               for (int var10 = 1; var10 <= 10; var10++) {
                  if (var1.getTemplateForValue(var10) != null) {
                     int var11 = s(var9, var10).total;
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
            int var16 = s(var3, var13).total;
            if (var16 <= 21 && (var2 == 0 || var16 < 17)) {
               ArrayList var17 = new ArrayList(var3);
               var17.add(var13);

               for (int var18 = 1; var18 <= 10; var18++) {
                  if (var1.getTemplateForValue(var18) != null) {
                     int var19 = s(var17, var18).total;
                     if (var19 <= 21 && var19 >= 17 && var19 == var4) {
                        return new int[]{var13, var18};
                     }
                  }
               }
            }
         }
      }

      int var14 = var2 != 0 ? this.Y(var1, var3, var4) : this.f(var1, var3, var4);
      return var14 >= 1 ? new int[]{var14, -1} : null;
   }

   private List<Integer> F() {
      return this.t >= 0 && this.t < this.B.size() ? this.B.get(this.t) : new ArrayList<>();
   }

   private int f(we var1, List<Integer> var2, int var3) {
      ArrayList var4 = new ArrayList();
      ArrayList var5 = new ArrayList();

      for (int var6 = 1; var6 <= 10; var6++) {
         if (var1.getTemplateForValue(var6) != null) {
            int var7 = s(var2, var6).total;
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
         return !var5.isEmpty() ? (Integer)var5.get(ThreadLocalRandom.current().nextInt(var5.size())) : this.m(var1, var2);
      }
   }

   private int P() {
      int var1 = 0;

      for (List var3 : this.B) {
         var1 = Math.max(var1, b(var3).total);
      }

      return var1;
   }

   private void O() {
      int var1 = -1;

      for (int var2 = 0; var2 < this.V && var1 < 0; var2++) {
         if (var2 >= this.B.size() || b(this.B.get(var2)).total < 17) {
            var1 = var2;
         }
      }

      if (var1 >= 0) {
         this.t = var1;
      }

      this.j = this.x != null ? this.x : (var1 < 0 ? s3.d.l : s3.d.n);
   }

   private int m() {
      int var1 = -1;

      for (List var3 : this.B) {
         int var4 = b(var3).total;
         if (var4 <= 21) {
            var1 = Math.max(var1, var4);
         }
      }

      return var1;
   }

   private int Y(we var1, List<Integer> var2, int var3) {
      int var4 = b(var2).total;
      if (var4 >= 17) {
         return -1;
      }

      ArrayList var5 = new ArrayList();
      ArrayList var6 = new ArrayList();

      for (int var7 = 1; var7 <= 10; var7++) {
         if (var1.getTemplateForValue(var7) != null) {
            int var8 = s(var2, var7).total;
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
                  int var16 = s(var2, var14).total;
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

         return !var6.isEmpty() ? (Integer)var6.get(ThreadLocalRandom.current().nextInt(var6.size())) : this.m(var1, var2);
      }
   }

   private int m(we var1, List<Integer> var2) {
      int var3 = -1;
      int var4 = -1;

      for (int var5 = 1; var5 <= 10; var5++) {
         if (var1.getTemplateForValue(var5) != null) {
            int var6 = s(var2, var5).total;
            if (var6 <= 21 && var6 > var4) {
               var4 = var6;
               var3 = var5;
            }
         }
      }

      return var3;
   }

   private Map<Integer, Integer> W(Minecraft var1) {
      HashMap var2 = new HashMap();
      lh var3 = lh.I();

      for (int var4 = 0; var4 < var1.player.getInventory().getContainerSize(); var4++) {
         ItemStack var5 = var1.player.getInventory().getItem(var4);
         int var6 = var3.p(var4, var5);
         if (var6 < var5.getCount()) {
            this.i(var2, var6 == 0 ? var5 : var5.copyWithCount(var5.getCount() - var6));
         }
      }

      if (var1.level != null) {
         for (Entity var9 : var1.level.entitiesForRendering()) {
            if (var9 instanceof ItemFrame var10) {
               this.i(var2, var10.getItem());
            } else if (var9 instanceof ItemEntity var7) {
               this.i(var2, var7.getItem());
            }
         }
      }

      return var2;
   }

   private void i(Map<Integer, Integer> var1, ItemStack var2) {
      if (var2 != null && !var2.isEmpty() && var2.is(Items.FILLED_MAP)) {
         MapId var3 = (MapId)var2.getComponents().get(DataComponents.MAP_ID);
         if (var3 != null && this.C(var3) >= 1) {
            var1.merge(var3.id(), var2.getCount(), Integer::sum);
         }
      }
   }

   private int C(MapId var1) {
      lh var2 = lh.I();
      return this.y && var2.V() ? var2.d(var1.id()) : we.u().q(var1);
   }

   private int X(Minecraft var1, we var2, int var3) {
      for (int var4 = 0; var4 < var1.player.getInventory().getContainerSize(); var4++) {
         ItemStack var5 = var1.player.getInventory().getItem(var4);
         if (var5 != null && !var5.isEmpty() && var5.is(Items.FILLED_MAP)) {
            MapId var6 = (MapId)var5.getComponents().get(DataComponents.MAP_ID);
            if (var6 != null && var6.id() == var3) {
               if (gj.c().H(gj.d.a)) {
                  return this.C(var6);
               }

               if (lh.I().S()) {
                  return var2.q(var6);
               }

               return var2.i("inventory[" + var4 + "]", var5);
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
                     if (gj.c().H(gj.d.a)) {
                        return this.C(var8);
                     }

                     if (lh.I().S()) {
                        return var2.q(var8);
                     }

                     return var2.i("item_frame[" + var11.getId() + "]", var7);
                  }
               }
            }
         }
      }

      return -1;
   }

   private int k(Map<Integer, Integer> var1) {
      int var2 = 0;

      for (Entry var4 : var1.entrySet()) {
         var2 += Math.max(0, (Integer)var4.getValue() - this.k.getOrDefault(var4.getKey(), 0));
      }

      return var2;
   }

   private void v() {
      we.u().h();
      this.B.clear();
      this.R.clear();
      this.k = null;
      this.P.clear();
      this.X.clear();
      this.r = 0;
      this.M = 0;
      this.i = 0;
      this.f = 0;
      this.c = (boolean)0;
      this.y = (boolean)0;
      this.J = 0;
      this.U = -1;
      this.v = -1;
   }

   public int A() {
      return this.P();
   }

   public int K() {
      return b(this.R).total;
   }

   public boolean U() {
      return this.j == s3.d.l;
   }

   public int l(boolean[] var1) {
      if (!this.U() && this.J >= this.V * 2 + 1 && this.f <= 0) {
         List var2 = this.F();
         int var3 = b(var2).total;
         return var3 >= 12 && var3 < 17 ? p(var2, var1) : -1;
      } else {
         return -1;
      }
   }

   public boolean r(int var1, int var2) {
      return L(this.F(), var1, var2);
   }

   public static boolean L(List<Integer> var0, int var1, int var2) {
      if (!var0.isEmpty() && var1 >= 1 && var1 <= 10 && var2 >= 1 && var2 <= 10) {
         int var3 = s(var0, var1).total;
         int var4 = s(var0, var2).total;
         return var4 <= 21 && (var3 < 17 || var4 >= 17);
      } else {
         return false;
      }
   }

   public static int p(List<Integer> var0, boolean[] var1) {
      if (!var0.isEmpty() && var1.length >= 11) {
         ArrayList var2 = new ArrayList();
         ArrayList var3 = new ArrayList();

         for (int var4 = 1; var4 <= 10; var4++) {
            if (var1[var4]) {
               int var5 = s(var0, var4).total;
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

   public int f(boolean[] var1) {
      if (this.U() && this.J >= this.V * 2 + 1 && this.f <= 0) {
         int var2 = 0;

         for (List var4 : this.B) {
            int var5 = b(var4).total;
            if (var5 <= 21) {
               var2 = Math.max(var2, var5);
            }
         }

         return y(var2, this.R, var1);
      } else {
         return -1;
      }
   }

   public static int y(int var0, List<Integer> var1, boolean[] var2) {
      int var3 = b(var1).total;
      if (var0 >= 2 && var0 <= 21 && !var1.isEmpty() && var2.length >= 11 && var3 < 17) {
         if (var0 >= 18) {
            for (int var4 = 1; var4 <= 10; var4++) {
               if (var2[var4] && s(var1, var4).total == 17) {
                  return var4;
               }
            }
         }

         ArrayList var16 = new ArrayList();
         int var5 = -1;
         int var6 = 21;

         for (int var7 = 1; var7 <= 10; var7++) {
            if (var2[var7]) {
               int var8 = s(var1, var7).total;
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
                  int var14 = s(var1, var13).total;
                  if (var14 < 17) {
                     var12[var13] = (boolean)0;
                     ArrayList var15 = new ArrayList(var1);
                     var15.add(var13);
                     if (t(var15, var12)) {
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

   private static boolean t(List<Integer> var0, boolean[] var1) {
      for (int var2 = 1; var2 <= 10; var2++) {
         if (var1[var2]) {
            int var3 = s(var0, var2).total;
            if (var3 > 21) {
               return true;
            }

            if (var3 < 17) {
               var1[var2] = (boolean)0;
               ArrayList var4 = new ArrayList(var0);
               var4.add(var2);
               boolean var5 = t(var4, var1);
               var1[var2] = (boolean)1;
               if (var5) {
                  return true;
               }
            }
         }
      }

      return false;
   }

   public boolean u() {
      for (List var2 : this.B) {
         if (b(var2).soft) {
            return true;
         }
      }

      return false;
   }

   public boolean K() {
      return b(this.R).soft;
   }

   public int h() {
      return this.V;
   }

   public int s(int var1) {
      return var1 >= 0 && var1 < this.B.size() ? b(this.B.get(var1)).total : 0;
   }

   public int t(int var1) {
      if (var1 >= 0 && var1 < this.B.size()) {
         return b(this.B.get(var1)).soft ? 1 : 0;
      } else {
         return 0;
      }
   }

   public void S(int var1) {
      int var2 = Math.max(1, Math.min(3, var1));
      if (this.V != var2) {
         this.V = var2;
         this.v();
         this.t = 0;
      }
   }

   public void S() {
      this.U = -1;
      this.v = -1;
      if (this.j == s3.d.l) {
         this.j = s3.d.n;
         this.t = 0;
      } else {
         int var1 = this.t + 1;
         if (var1 >= this.V) {
            this.j = s3.d.l;
         } else {
            this.t = var1;
         }
      }
   }

   public void p(String var1) {
      if ("dealer".equalsIgnoreCase(var1)) {
         this.j = s3.d.l;
      } else if ("player".equalsIgnoreCase(var1)) {
         this.j = s3.d.n;
         this.t = 0;
      }
   }

   public void G(int var1) {
      s3.d var2 = var1 < 0 ? s3.d.l : s3.d.n;
      int var3 = var1 < 0 ? this.t : Math.min(var1, this.V - 1);
      if (var2 != this.j || var3 != this.t) {
         this.j = var2;
         this.t = var3;
         this.U = -1;
         this.v = -1;
      }
   }

   public int L() {
      return this.j == s3.d.l ? -1 : this.t;
   }

   public void L(String var1) {
      this.x = "player".equalsIgnoreCase(var1) ? s3.d.n : ("dealer".equalsIgnoreCase(var1) ? s3.d.l : null);
   }

   public boolean F() {
      return this.x == null;
   }

   public String y() {
      if (this.j == s3.d.l) {
         return "Dealer";
      } else {
         return this.V == 1 ? "Player" : "Hand " + (this.t + 1);
      }
   }

   public String a(int var1) {
      if (var1 < 0) {
         return this.R.isEmpty() ? "-" : i(this.R);
      } else {
         return var1 < this.B.size() && !this.B.get(var1).isEmpty() ? i(this.B.get(var1)) : "-";
      }
   }

   private static String i(List<Integer> var0) {
      s3.a var1 = b(var0);
      return var1.total + (var1.total > 21 ? " bust" : (var1.soft ? " soft" : ""));
   }

   public String t() {
      return switch (this.j) {
         case n -> "HAND " + (this.t + 1);
         case l -> "DEALER";
      };
   }

   public String N() {
      return switch (this.j) {
         case n -> "hand-" + this.t;
         case l -> "dealer";
      };
   }

   public int X() {
      return this.U;
   }

   public int Z() {
      return this.v;
   }

   public String B() {
      return this.U < 1 ? "-1" : this.R();
   }

   public String U() {
      return this.U < 1 ? "none" : this.R();
   }

   private String R() {
      StringBuilder var1 = new StringBuilder(i(this.U));
      if (this.v >= 1) {
         var1.append(" -> ").append(i(this.v));
      }

      return var1.toString();
   }

   private static String i(int var0) {
      return switch (var0) {
         case 1 -> "A";
         case 10 -> "10";
         default -> String.valueOf(var0);
      };
   }

   public int D() {
      return this.J;
   }

   public int t() {
      return this.f;
   }

   public boolean e() {
      return this.I;
   }

   private static s3.a s(List<Integer> var0, int var1) {
      ArrayList var2 = new ArrayList(var0);
      var2.add(var1);
      return b(var2);
   }

   private static s3.a b(List<Integer> var0) {
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

      return new s3.a(var1, var2 > 0);
   }

   private record a(int total, boolean soft) {
   }

   private enum d {
      n,
      l;
   }
}
