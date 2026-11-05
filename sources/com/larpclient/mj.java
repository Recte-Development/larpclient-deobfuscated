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

public final class mj {
   private static final mj H = new mj();
   private static final t0 a = t0.R("BJAssistant");
   private final List<List<Integer>> G = new ArrayList<>();
   private final List<Integer> e = new ArrayList<>();
   private int n = 1;
   private int S;
   private mj.k P;
   private int c = -1;
   private Map<Integer, Integer> g;
   private Map<Integer, Integer> Z = new HashMap<>();
   private Map<Integer, Integer> J = new HashMap<>();
   private int u;
   private mj.k d = mj.k.Q;
   private final long I = System.currentTimeMillis();
   private int j;
   private int O;
   private int w;
   private boolean p;
   private boolean W;
   private boolean q = (boolean)1;
   private boolean T;
   private long l;
   private int A;
   private int s = -1;

   private mj() {
   }

   public static mj D() {
      return H;
   }

   public void x(boolean var1) {
      this.T = var1;
   }

   public void v(Minecraft var1) {
      this.I(var1, false);
   }

   public void I(Minecraft var1, boolean var2) {
      if (!this.p) {
         ze.V().d();
         this.G.clear();
         this.e.clear();
         this.A = 0;
         this.s = -1;
         this.c = -1;
         this.j = 0;
         this.O = 0;
         this.p = (boolean)1;
         this.W = var2;
         if (this.g == null && var1 != null && var1.player != null) {
            this.g = this.M(var1);
            this.Z = new HashMap<>(this.g);
            this.J = new HashMap<>(this.g);
            this.u = 0;
         }
      }

      this.w++;
   }

   public void C(Minecraft var1, ze var2, boolean var3) {
      if (!var3 || var1 == null || var1.player == null) {
         this.J();
      } else if (var2 == null || !var2.isSwapBusy()) {
         Map var4 = this.M(var1);
         if (!var4.equals(this.J)) {
            this.J = new HashMap<>(var4);
            this.u = 0;
         } else {
            this.u++;
         }

         if (this.g == null) {
            this.g = new HashMap<>(var4);
            this.Z = new HashMap<>(var4);
            this.J = new HashMap<>(var4);
         } else if (!this.p) {
            this.g = new HashMap<>(var4);
            this.Z = new HashMap<>(var4);
            this.J = new HashMap<>(var4);
            this.j = 0;
            this.O = 0;
         } else {
            int var5 = this.w(var4);
            if (this.u >= 2 && var5 > this.j) {
               int var6 = var5 - this.j;
               int var7 = Math.min(var6, this.w);
               if (var6 > var7) {
                  xa.i("BJ hand ignored duplicate map-count rise=" + var6 + " activations=" + this.w);
               }

               if (var7 != 0) {
                  int var8 = 0;

                  for (Entry var10 : var4.entrySet()) {
                     int var11 = Math.max(0, (Integer)var10.getValue() - this.Z.getOrDefault(var10.getKey(), 0));

                     while (var11-- > 0 && var8 < var7) {
                        int var12 = this.g(var1, var2, (Integer)var10.getKey());
                        if (var12 >= 1 && var12 <= 10) {
                           var2.S(var12, (Integer)var10.getKey());
                           this.C(var12);
                           this.w--;
                           var8++;
                           this.Z.merge((Integer)var10.getKey(), 1, Integer::sum);
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

            this.O = var5 <= 0 && !kq.I().k() ? this.O + 1 : 0;
            if (this.A > 0 && this.O >= 1 && this.O % 5 == 1) {
               a.a("BJ {} reset-watch dealtCards={} extras={} emptyHandTicks={} busy={}", this.v(), this.A, var5, this.O, var2 != null && var2.isSwapBusy());
            }

            if (this.A > 0 && this.O >= 6) {
               a.a("BJ {} round reset (cards returned)", this.v());
               this.G.clear();
               this.e.clear();
               this.A = 0;
               this.s = -1;
               this.c = -1;
               this.g = new HashMap<>(var4);
               this.Z = new HashMap<>(var4);
               this.J = new HashMap<>(var4);
               this.j = 0;
               this.O = 0;
               this.w = 0;
               this.p = (boolean)0;
               if (this.d != mj.k.Q || this.S != 0) {
                  this.d = mj.k.Q;
                  this.S = 0;
                  xa.q("Blackjack", "Round over; side reset to hand 1");
               }
            }

            if (kq.I().Z()) {
               this.C();
            }

            int var13 = this.n * 2 + 1;
            if (this.A >= var13 && this.d == mj.k.w) {
               int[] var14 = this.M(var2);
               if (var14 == null) {
                  this.s = -1;
                  this.c = -1;
               } else {
                  this.s = var14[0];
                  this.c = var14[1];
               }
            } else {
               this.s = -1;
               this.c = -1;
            }

            this.B(var2);
         }
      }
   }

   public boolean U(int var1, int var2) {
      if (this.d != mj.k.w) {
         return true;
      } else if (this.G.isEmpty()) {
         return true;
      } else {
         int var3 = this.j();
         if (var3 < 0) {
            return false;
         } else if (var3 >= 2 && var1 >= 1 && var2 >= 1) {
            int var4 = X(this.e, var1).total;
            return var4 < 17 || var4 > 21 || var4 <= var3;
         } else {
            return true;
         }
      }
   }

   private void B(ze var1) {
      if (var1 != null && !var1.isSwapBusy() && this.T) {
         if (this.s >= 1) {
            int var2 = var1.getPendingValue();
            if (var2 != this.s || var1.getQueuedValueAt(1) != this.c) {
               var1.queueForceSequence(this.s, this.c);
               xa.i("SRIG " + this.v() + " armed plan rec=" + (this.c >= 1 ? this.s + "->" + this.c : this.s));
            }
         } else if (var1.hasQueuedForce()) {
            var1.clearQueue();
            xa.i("SRIG " + this.v() + " cleared plan (no recommendation)");
         }
      }
   }

   private void C(int var1) {
      int var2 = this.n;
      if (this.A < var2) {
         this.r(this.A, var1);
      } else if (this.A == var2) {
         this.e.add(var1);
      } else if (this.A <= var2 * 2) {
         this.r(this.A - var2 - 1, var1);
      } else if (this.d == mj.k.Q) {
         this.r(this.S, var1);
      } else {
         this.e.add(var1);
      }

      this.A++;
   }

   private void r(int var1, int var2) {
      while (this.G.size() <= var1) {
         this.G.add(new ArrayList<>());
      }

      this.G.get(var1).add(var2);
   }

   private int[] M(ze var1) {
      int var2 = this.d == mj.k.w ? 1 : 0;
      List var3 = var2 != 0 ? this.e : this.R();
      if (var3.isEmpty()) {
         return null;
      }

      int var4 = var2 != 0 ? this.j() : Y(this.e).total;
      if (var2 != 0 && var4 < 0) {
         return null;
      }

      if (var2 != 0 && Y(var3).total >= 17) {
         return null;
      }

      int var5 = -1;
      int var6 = -1;

      for (int var7 = 1; var7 <= 10; var7++) {
         if (var1.getTemplateForValue(var7) != null) {
            int var8 = X(var3, var7).total;
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
            int var15 = X(var3, var12).total;
            if (var15 <= 21 && (var2 == 0 || var15 < 17)) {
               ArrayList var9 = new ArrayList(var3);
               var9.add(var12);

               for (int var10 = 1; var10 <= 10; var10++) {
                  if (var1.getTemplateForValue(var10) != null) {
                     int var11 = X(var9, var10).total;
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
            int var16 = X(var3, var13).total;
            if (var16 <= 21 && (var2 == 0 || var16 < 17)) {
               ArrayList var17 = new ArrayList(var3);
               var17.add(var13);

               for (int var18 = 1; var18 <= 10; var18++) {
                  if (var1.getTemplateForValue(var18) != null) {
                     int var19 = X(var17, var18).total;
                     if (var19 <= 21 && var19 >= 17 && var19 == var4) {
                        return new int[]{var13, var18};
                     }
                  }
               }
            }
         }
      }

      int var14 = var2 != 0 ? this.C(var1, var3, var4) : this.Y(var1, var3, var4);
      return var14 >= 1 ? new int[]{var14, -1} : null;
   }

   private List<Integer> R() {
      return this.S >= 0 && this.S < this.G.size() ? this.G.get(this.S) : new ArrayList<>();
   }

   private int Y(ze var1, List<Integer> var2, int var3) {
      ArrayList var4 = new ArrayList();
      ArrayList var5 = new ArrayList();

      for (int var6 = 1; var6 <= 10; var6++) {
         if (var1.getTemplateForValue(var6) != null) {
            int var7 = X(var2, var6).total;
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
         return !var5.isEmpty() ? (Integer)var5.get(ThreadLocalRandom.current().nextInt(var5.size())) : this.G(var1, var2);
      }
   }

   private int r() {
      int var1 = 0;

      for (List var3 : this.G) {
         var1 = Math.max(var1, Y(var3).total);
      }

      return var1;
   }

   private void C() {
      int var1 = -1;

      for (int var2 = 0; var2 < this.n && var1 < 0; var2++) {
         if (var2 >= this.G.size() || Y(this.G.get(var2)).total < 17) {
            var1 = var2;
         }
      }

      if (var1 >= 0) {
         this.S = var1;
      }

      this.d = this.P != null ? this.P : (var1 < 0 ? mj.k.w : mj.k.Q);
   }

   private int j() {
      int var1 = -1;

      for (List var3 : this.G) {
         int var4 = Y(var3).total;
         if (var4 <= 21) {
            var1 = Math.max(var1, var4);
         }
      }

      return var1;
   }

   private int C(ze var1, List<Integer> var2, int var3) {
      int var4 = Y(var2).total;
      if (var4 >= 17) {
         return -1;
      }

      ArrayList var5 = new ArrayList();
      ArrayList var6 = new ArrayList();

      for (int var7 = 1; var7 <= 10; var7++) {
         if (var1.getTemplateForValue(var7) != null) {
            int var8 = X(var2, var7).total;
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
                  int var16 = X(var2, var14).total;
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

         return !var6.isEmpty() ? (Integer)var6.get(ThreadLocalRandom.current().nextInt(var6.size())) : this.G(var1, var2);
      }
   }

   private int G(ze var1, List<Integer> var2) {
      int var3 = -1;
      int var4 = -1;

      for (int var5 = 1; var5 <= 10; var5++) {
         if (var1.getTemplateForValue(var5) != null) {
            int var6 = X(var2, var5).total;
            if (var6 <= 21 && var6 > var4) {
               var4 = var6;
               var3 = var5;
            }
         }
      }

      return var3;
   }

   private Map<Integer, Integer> M(Minecraft var1) {
      HashMap var2 = new HashMap();
      kq var3 = kq.I();

      for (int var4 = 0; var4 < var1.player.getInventory().getContainerSize(); var4++) {
         ItemStack var5 = var1.player.getInventory().getItem(var4);
         int var6 = var3.Y(var4, var5);
         if (var6 < var5.getCount()) {
            this.z(var2, var6 == 0 ? var5 : var5.copyWithCount(var5.getCount() - var6));
         }
      }

      if (var1.level != null) {
         for (Entity var9 : var1.level.entitiesForRendering()) {
            if (var9 instanceof ItemFrame var10) {
               this.z(var2, var10.getItem());
            } else if (var9 instanceof ItemEntity var7) {
               this.z(var2, var7.getItem());
            }
         }
      }

      return var2;
   }

   private void z(Map<Integer, Integer> var1, ItemStack var2) {
      if (var2 != null && !var2.isEmpty() && var2.is(Items.FILLED_MAP)) {
         MapId var3 = (MapId)var2.getComponents().get(DataComponents.MAP_ID);
         if (var3 != null && this.U(var3) >= 1) {
            var1.merge(var3.id(), var2.getCount(), Integer::sum);
         }
      }
   }

   private int U(MapId var1) {
      kq var2 = kq.I();
      return this.W && var2.f() ? var2.P(var1.id()) : ze.V().Y(var1);
   }

   private int g(Minecraft var1, ze var2, int var3) {
      for (int var4 = 0; var4 < var1.player.getInventory().getContainerSize(); var4++) {
         ItemStack var5 = var1.player.getInventory().getItem(var4);
         if (var5 != null && !var5.isEmpty() && var5.is(Items.FILLED_MAP)) {
            MapId var6 = (MapId)var5.getComponents().get(DataComponents.MAP_ID);
            if (var6 != null && var6.id() == var3) {
               if (ep.H().q(ep.o.v)) {
                  return this.U(var6);
               }

               if (kq.I().Z()) {
                  return var2.Y(var6);
               }

               return var2.J("inventory[" + var4 + "]", var5);
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
                     if (ep.H().q(ep.o.v)) {
                        return this.U(var8);
                     }

                     if (kq.I().Z()) {
                        return var2.Y(var8);
                     }

                     return var2.J("item_frame[" + var11.getId() + "]", var7);
                  }
               }
            }
         }
      }

      return -1;
   }

   private int w(Map<Integer, Integer> var1) {
      int var2 = 0;

      for (Entry var4 : var1.entrySet()) {
         var2 += Math.max(0, (Integer)var4.getValue() - this.g.getOrDefault(var4.getKey(), 0));
      }

      return var2;
   }

   private void J() {
      ze.V().d();
      this.G.clear();
      this.e.clear();
      this.g = null;
      this.Z.clear();
      this.J.clear();
      this.u = 0;
      this.j = 0;
      this.O = 0;
      this.w = 0;
      this.p = (boolean)0;
      this.W = (boolean)0;
      this.A = 0;
      this.s = -1;
      this.c = -1;
   }

   public int B() {
      return this.r();
   }

   public int l() {
      return Y(this.e).total;
   }

   public boolean v() {
      return this.d == mj.k.w;
   }

   public int D(boolean[] var1) {
      if (!this.v() && this.A >= this.n * 2 + 1 && this.w <= 0) {
         List var2 = this.R();
         int var3 = Y(var2).total;
         return var3 >= 12 && var3 < 17 ? O(var2, var1) : -1;
      } else {
         return -1;
      }
   }

   public boolean l(int var1, int var2) {
      return U(this.R(), var1, var2);
   }

   public static boolean U(List<Integer> var0, int var1, int var2) {
      if (!var0.isEmpty() && var1 >= 1 && var1 <= 10 && var2 >= 1 && var2 <= 10) {
         int var3 = X(var0, var1).total;
         int var4 = X(var0, var2).total;
         return var4 <= 21 && (var3 < 17 || var4 >= 17);
      } else {
         return false;
      }
   }

   public static int O(List<Integer> var0, boolean[] var1) {
      if (!var0.isEmpty() && var1.length >= 11) {
         ArrayList var2 = new ArrayList();
         ArrayList var3 = new ArrayList();

         for (int var4 = 1; var4 <= 10; var4++) {
            if (var1[var4]) {
               int var5 = X(var0, var4).total;
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

   public int T(boolean[] var1) {
      if (this.v() && this.A >= this.n * 2 + 1 && this.w <= 0) {
         int var2 = 0;

         for (List var4 : this.G) {
            int var5 = Y(var4).total;
            if (var5 <= 21) {
               var2 = Math.max(var2, var5);
            }
         }

         return x(var2, this.e, var1);
      } else {
         return -1;
      }
   }

   public static int x(int var0, List<Integer> var1, boolean[] var2) {
      int var3 = Y(var1).total;
      if (var0 >= 2 && var0 <= 21 && !var1.isEmpty() && var2.length >= 11 && var3 < 17) {
         if (var0 >= 18) {
            for (int var4 = 1; var4 <= 10; var4++) {
               if (var2[var4] && X(var1, var4).total == 17) {
                  return var4;
               }
            }
         }

         ArrayList var16 = new ArrayList();
         int var5 = -1;
         int var6 = 21;

         for (int var7 = 1; var7 <= 10; var7++) {
            if (var2[var7]) {
               int var8 = X(var1, var7).total;
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
                  int var14 = X(var1, var13).total;
                  if (var14 < 17) {
                     var12[var13] = (boolean)0;
                     ArrayList var15 = new ArrayList(var1);
                     var15.add(var13);
                     if (n(var15, var12)) {
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

   private static boolean n(List<Integer> var0, boolean[] var1) {
      for (int var2 = 1; var2 <= 10; var2++) {
         if (var1[var2]) {
            int var3 = X(var0, var2).total;
            if (var3 > 21) {
               return true;
            }

            if (var3 < 17) {
               var1[var2] = (boolean)0;
               ArrayList var4 = new ArrayList(var0);
               var4.add(var2);
               boolean var5 = n(var4, var1);
               var1[var2] = (boolean)1;
               if (var5) {
                  return true;
               }
            }
         }
      }

      return false;
   }

   public boolean K() {
      for (List var2 : this.G) {
         if (Y(var2).soft) {
            return true;
         }
      }

      return false;
   }

   public boolean A() {
      return Y(this.e).soft;
   }

   public int o() {
      return this.n;
   }

   public int T(int var1) {
      return var1 >= 0 && var1 < this.G.size() ? Y(this.G.get(var1)).total : 0;
   }

   public int m(int var1) {
      if (var1 >= 0 && var1 < this.G.size()) {
         return Y(this.G.get(var1)).soft ? 1 : 0;
      } else {
         return 0;
      }
   }

   public void o(int var1) {
      int var2 = Math.max(1, Math.min(3, var1));
      if (this.n != var2) {
         this.n = var2;
         this.J();
         this.S = 0;
      }
   }

   public void k() {
      this.s = -1;
      this.c = -1;
      if (this.d == mj.k.w) {
         this.d = mj.k.Q;
         this.S = 0;
      } else {
         int var1 = this.S + 1;
         if (var1 >= this.n) {
            this.d = mj.k.w;
         } else {
            this.S = var1;
         }
      }
   }

   public void k(String var1) {
      if ("dealer".equalsIgnoreCase(var1)) {
         this.d = mj.k.w;
      } else if ("player".equalsIgnoreCase(var1)) {
         this.d = mj.k.Q;
         this.S = 0;
      }
   }

   public void U(int var1) {
      mj.k var2 = var1 < 0 ? mj.k.w : mj.k.Q;
      int var3 = var1 < 0 ? this.S : Math.min(var1, this.n - 1);
      if (var2 != this.d || var3 != this.S) {
         this.d = var2;
         this.S = var3;
         this.s = -1;
         this.c = -1;
      }
   }

   public int A() {
      return this.d == mj.k.w ? -1 : this.S;
   }

   public void h(String var1) {
      this.P = "player".equalsIgnoreCase(var1) ? mj.k.Q : ("dealer".equalsIgnoreCase(var1) ? mj.k.w : null);
   }

   public boolean B() {
      return this.P == null;
   }

   public String O() {
      if (this.d == mj.k.w) {
         return "Dealer";
      } else {
         return this.n == 1 ? "Player" : "Hand " + (this.S + 1);
      }
   }

   public String Y(int var1) {
      if (var1 < 0) {
         return this.e.isEmpty() ? "-" : P(this.e);
      } else {
         return var1 < this.G.size() && !this.G.get(var1).isEmpty() ? P(this.G.get(var1)) : "-";
      }
   }

   private static String P(List<Integer> var0) {
      mj.s var1 = Y(var0);
      return var1.total + (var1.total > 21 ? " bust" : (var1.soft ? " soft" : ""));
   }

   public String X() {
      return switch (this.d) {
         case Q -> "HAND " + (this.S + 1);
         case w -> "DEALER";
      };
   }

   public String v() {
      return switch (this.d) {
         case Q -> "hand-" + this.S;
         case w -> "dealer";
      };
   }

   public int M() {
      return this.s;
   }

   public int Z() {
      return this.c;
   }

   public String S() {
      return this.s < 1 ? "-1" : this.c();
   }

   public String r() {
      return this.s < 1 ? "none" : this.c();
   }

   private String c() {
      StringBuilder var1 = new StringBuilder(j(this.s));
      if (this.c >= 1) {
         var1.append(" -> ").append(j(this.c));
      }

      return var1.toString();
   }

   private static String j(int var0) {
      return switch (var0) {
         case 1 -> "A";
         case 10 -> "10";
         default -> String.valueOf(var0);
      };
   }

   public int O() {
      return this.A;
   }

   public int J() {
      return this.w;
   }

   public boolean M() {
      return this.q;
   }

   private static mj.s X(List<Integer> var0, int var1) {
      ArrayList var2 = new ArrayList(var0);
      var2.add(var1);
      return Y(var2);
   }

   private static mj.s Y(List<Integer> var0) {
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

      return new mj.s(var1, var2 > 0);
   }

   private enum k {
      Q,
      w;
   }

   private record s(int total, boolean soft) {
   }
}
