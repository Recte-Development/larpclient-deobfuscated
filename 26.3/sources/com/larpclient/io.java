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

public final class io {
   private static final io B = new io();
   private static final jq P = jq.d("BJAssistant");
   private final List<List<Integer>> C = new ArrayList<>();
   private final List<Integer> n = new ArrayList<>();
   private int T = 1;
   private int M;
   private io.q D;
   private int Z = -1;
   private Map<Integer, Integer> L;
   private Map<Integer, Integer> l = new HashMap<>();
   private Map<Integer, Integer> i = new HashMap<>();
   private int c;
   private io.q y = io.q.S;
   private final long A = System.currentTimeMillis();
   private int f;
   private int H;
   private int z;
   private boolean k;
   private boolean v;
   private boolean V = (boolean)1;
   private boolean Y;
   private long t;
   private int S;
   private int K = -1;

   private io() {
   }

   public static io z() {
      return B;
   }

   public void h(boolean var1) {
      this.Y = var1;
   }

   public void b(Minecraft var1) {
      this.f(var1, false);
   }

   public void f(Minecraft var1, boolean var2) {
      if (!this.k) {
         vy.w().n();
         this.C.clear();
         this.n.clear();
         this.S = 0;
         this.K = -1;
         this.Z = -1;
         this.f = 0;
         this.H = 0;
         this.k = (boolean)1;
         this.v = var2;
         if (this.L == null && var1 != null && var1.player != null) {
            this.L = this.W(var1);
            this.l = new HashMap<>(this.L);
            this.i = new HashMap<>(this.L);
            this.c = 0;
         }
      }

      this.z++;
   }

   public void O(Minecraft var1, vy var2, boolean var3) {
      if (!var3 || var1 == null || var1.player == null) {
         this.X();
      } else if (var2 == null || !var2.isSwapBusy()) {
         Map var4 = this.W(var1);
         if (!var4.equals(this.i)) {
            this.i = new HashMap<>(var4);
            this.c = 0;
         } else {
            this.c++;
         }

         if (this.L == null) {
            this.L = new HashMap<>(var4);
            this.l = new HashMap<>(var4);
            this.i = new HashMap<>(var4);
         } else if (!this.k) {
            this.L = new HashMap<>(var4);
            this.l = new HashMap<>(var4);
            this.i = new HashMap<>(var4);
            this.f = 0;
            this.H = 0;
         } else {
            int var5 = this.N(var4);
            if (this.c >= 2 && var5 > this.f) {
               int var6 = var5 - this.f;
               int var7 = Math.min(var6, this.z);
               if (var6 > var7) {
                  bn.b("BJ hand ignored duplicate map-count rise=" + var6 + " activations=" + this.z);
               }

               if (var7 != 0) {
                  int var8 = 0;

                  for (Entry var10 : var4.entrySet()) {
                     int var11 = Math.max(0, (Integer)var10.getValue() - this.l.getOrDefault(var10.getKey(), 0));

                     while (var11-- > 0 && var8 < var7) {
                        int var12 = this.C(var1, var2, (Integer)var10.getKey());
                        if (var12 >= 1 && var12 <= 10) {
                           var2.l(var12, (Integer)var10.getKey());
                           this.l(var12);
                           this.z--;
                           var8++;
                           this.l.merge((Integer)var10.getKey(), 1, Integer::sum);
                        }
                     }

                     if (var8 >= var7) {
                        break;
                     }
                  }

                  if (var8 > 0) {
                     this.f += var8;
                  }
               }
            }

            this.H = var5 <= 0 && !q5.a().L() ? this.H + 1 : 0;
            if (this.S > 0 && this.H >= 1 && this.H % 5 == 1) {
               P.N("BJ {} reset-watch dealtCards={} extras={} emptyHandTicks={} busy={}", this.R(), this.S, var5, this.H, var2 != null && var2.isSwapBusy());
            }

            if (this.S > 0 && this.H >= 6) {
               P.N("BJ {} round reset (cards returned)", this.R());
               this.C.clear();
               this.n.clear();
               this.S = 0;
               this.K = -1;
               this.Z = -1;
               this.L = new HashMap<>(var4);
               this.l = new HashMap<>(var4);
               this.i = new HashMap<>(var4);
               this.f = 0;
               this.H = 0;
               this.z = 0;
               this.k = (boolean)0;
               if (this.y != io.q.S || this.M != 0) {
                  this.y = io.q.S;
                  this.M = 0;
                  bn.j("Blackjack", "Round over; side reset to hand 1");
               }
            }

            if (q5.a().N()) {
               this.w();
            }

            int var13 = this.T * 2 + 1;
            if (this.S >= var13 && this.y == io.q.i) {
               int[] var14 = this.c(var2);
               if (var14 == null) {
                  this.K = -1;
                  this.Z = -1;
               } else {
                  this.K = var14[0];
                  this.Z = var14[1];
               }
            } else {
               this.K = -1;
               this.Z = -1;
            }

            this.M(var2);
         }
      }
   }

   public boolean P(int var1, int var2) {
      if (this.y != io.q.i) {
         return true;
      } else if (this.C.isEmpty()) {
         return true;
      } else {
         int var3 = this.a();
         if (var3 < 0) {
            return false;
         } else if (var3 >= 2 && var1 >= 1 && var2 >= 1) {
            int var4 = n(this.n, var1).total;
            return var4 < 17 || var4 > 21 || var4 <= var3;
         } else {
            return true;
         }
      }
   }

   private void M(vy var1) {
      if (var1 != null && !var1.isSwapBusy() && this.Y) {
         if (this.K >= 1) {
            int var2 = var1.getPendingValue();
            if (var2 != this.K || var1.getQueuedValueAt(1) != this.Z) {
               var1.queueForceSequence(this.K, this.Z);
               bn.b("SRIG " + this.R() + " armed plan rec=" + (this.Z >= 1 ? this.K + "->" + this.Z : this.K));
            }
         } else if (var1.hasQueuedForce()) {
            var1.clearQueue();
            bn.b("SRIG " + this.R() + " cleared plan (no recommendation)");
         }
      }
   }

   private void l(int var1) {
      int var2 = this.T;
      if (this.S < var2) {
         this.S(this.S, var1);
      } else if (this.S == var2) {
         this.n.add(var1);
      } else if (this.S <= var2 * 2) {
         this.S(this.S - var2 - 1, var1);
      } else if (this.y == io.q.S) {
         this.S(this.M, var1);
      } else {
         this.n.add(var1);
      }

      this.S++;
   }

   private void S(int var1, int var2) {
      while (this.C.size() <= var1) {
         this.C.add(new ArrayList<>());
      }

      this.C.get(var1).add(var2);
   }

   private int[] c(vy var1) {
      int var2 = this.y == io.q.i ? 1 : 0;
      List var3 = var2 != 0 ? this.n : this.H();
      if (var3.isEmpty()) {
         return null;
      }

      int var4 = var2 != 0 ? this.a() : Y(this.n).total;
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
            int var8 = n(var3, var7).total;
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
            int var15 = n(var3, var12).total;
            if (var15 <= 21 && (var2 == 0 || var15 < 17)) {
               ArrayList var9 = new ArrayList(var3);
               var9.add(var12);

               for (int var10 = 1; var10 <= 10; var10++) {
                  if (var1.getTemplateForValue(var10) != null) {
                     int var11 = n(var9, var10).total;
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
            int var16 = n(var3, var13).total;
            if (var16 <= 21 && (var2 == 0 || var16 < 17)) {
               ArrayList var17 = new ArrayList(var3);
               var17.add(var13);

               for (int var18 = 1; var18 <= 10; var18++) {
                  if (var1.getTemplateForValue(var18) != null) {
                     int var19 = n(var17, var18).total;
                     if (var19 <= 21 && var19 >= 17 && var19 == var4) {
                        return new int[]{var13, var18};
                     }
                  }
               }
            }
         }
      }

      int var14 = var2 != 0 ? this.A(var1, var3, var4) : this.c(var1, var3, var4);
      return var14 >= 1 ? new int[]{var14, -1} : null;
   }

   private List<Integer> H() {
      return this.M >= 0 && this.M < this.C.size() ? this.C.get(this.M) : new ArrayList<>();
   }

   private int c(vy var1, List<Integer> var2, int var3) {
      ArrayList var4 = new ArrayList();
      ArrayList var5 = new ArrayList();

      for (int var6 = 1; var6 <= 10; var6++) {
         if (var1.getTemplateForValue(var6) != null) {
            int var7 = n(var2, var6).total;
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

   private int r() {
      int var1 = 0;

      for (List var3 : this.C) {
         var1 = Math.max(var1, Y(var3).total);
      }

      return var1;
   }

   private void w() {
      int var1 = -1;

      for (int var2 = 0; var2 < this.T && var1 < 0; var2++) {
         if (var2 >= this.C.size() || Y(this.C.get(var2)).total < 17) {
            var1 = var2;
         }
      }

      if (var1 >= 0) {
         this.M = var1;
      }

      this.y = this.D != null ? this.D : (var1 < 0 ? io.q.i : io.q.S);
   }

   private int a() {
      int var1 = -1;

      for (List var3 : this.C) {
         int var4 = Y(var3).total;
         if (var4 <= 21) {
            var1 = Math.max(var1, var4);
         }
      }

      return var1;
   }

   private int A(vy var1, List<Integer> var2, int var3) {
      int var4 = Y(var2).total;
      if (var4 >= 17) {
         return -1;
      }

      ArrayList var5 = new ArrayList();
      ArrayList var6 = new ArrayList();

      for (int var7 = 1; var7 <= 10; var7++) {
         if (var1.getTemplateForValue(var7) != null) {
            int var8 = n(var2, var7).total;
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
                  int var16 = n(var2, var14).total;
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

   private int W(vy var1, List<Integer> var2) {
      int var3 = -1;
      int var4 = -1;

      for (int var5 = 1; var5 <= 10; var5++) {
         if (var1.getTemplateForValue(var5) != null) {
            int var6 = n(var2, var5).total;
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
      q5 var3 = q5.a();

      for (int var4 = 0; var4 < var1.player.getInventory().getContainerSize(); var4++) {
         ItemStack var5 = var1.player.getInventory().getItem(var4);
         int var6 = var3.r(var4, var5);
         if (var6 < var5.getCount()) {
            this.g(var2, var6 == 0 ? var5 : var5.copyWithCount(var5.getCount() - var6));
         }
      }

      if (var1.level != null) {
         for (Entity var9 : var1.level.entitiesForRendering()) {
            if (var9 instanceof ItemFrame var10) {
               this.g(var2, var10.getItem());
            } else if (var9 instanceof ItemEntity var7) {
               this.g(var2, var7.getItem());
            }
         }
      }

      return var2;
   }

   private void g(Map<Integer, Integer> var1, ItemStack var2) {
      if (var2 != null && !var2.isEmpty() && var2.is(Items.FILLED_MAP)) {
         MapId var3 = (MapId)var2.getComponents().get(DataComponents.MAP_ID);
         if (var3 != null && this.q(var3) >= 1) {
            var1.merge(var3.id(), var2.getCount(), Integer::sum);
         }
      }
   }

   private int q(MapId var1) {
      q5 var2 = q5.a();
      return this.v && var2.G() ? var2.n(var1.id()) : vy.w().V(var1);
   }

   private int C(Minecraft var1, vy var2, int var3) {
      for (int var4 = 0; var4 < var1.player.getInventory().getContainerSize(); var4++) {
         ItemStack var5 = var1.player.getInventory().getItem(var4);
         if (var5 != null && !var5.isEmpty() && var5.is(Items.FILLED_MAP)) {
            MapId var6 = (MapId)var5.getComponents().get(DataComponents.MAP_ID);
            if (var6 != null && var6.id() == var3) {
               if (hk.e().L(hk.k.q)) {
                  return this.q(var6);
               }

               if (q5.a().N()) {
                  return var2.V(var6);
               }

               return var2.B("inventory[" + var4 + "]", var5);
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
                     if (hk.e().L(hk.k.q)) {
                        return this.q(var8);
                     }

                     if (q5.a().N()) {
                        return var2.V(var8);
                     }

                     return var2.B("item_frame[" + var11.getId() + "]", var7);
                  }
               }
            }
         }
      }

      return -1;
   }

   private int N(Map<Integer, Integer> var1) {
      int var2 = 0;

      for (Entry var4 : var1.entrySet()) {
         var2 += Math.max(0, (Integer)var4.getValue() - this.L.getOrDefault(var4.getKey(), 0));
      }

      return var2;
   }

   private void X() {
      vy.w().n();
      this.C.clear();
      this.n.clear();
      this.L = null;
      this.l.clear();
      this.i.clear();
      this.c = 0;
      this.f = 0;
      this.H = 0;
      this.z = 0;
      this.k = (boolean)0;
      this.v = (boolean)0;
      this.S = 0;
      this.K = -1;
      this.Z = -1;
   }

   public int g() {
      return this.r();
   }

   public int b() {
      return Y(this.n).total;
   }

   public boolean e() {
      return this.y == io.q.i;
   }

   public int m(boolean[] var1) {
      if (!this.e() && this.S >= this.T * 2 + 1 && this.z <= 0) {
         List var2 = this.H();
         int var3 = Y(var2).total;
         return var3 >= 12 && var3 < 17 ? v(var2, var1) : -1;
      } else {
         return -1;
      }
   }

   public boolean S(int var1, int var2) {
      return s(this.H(), var1, var2);
   }

   public static boolean s(List<Integer> var0, int var1, int var2) {
      if (!var0.isEmpty() && var1 >= 1 && var1 <= 10 && var2 >= 1 && var2 <= 10) {
         int var3 = n(var0, var1).total;
         int var4 = n(var0, var2).total;
         return var4 <= 21 && (var3 < 17 || var4 >= 17);
      } else {
         return false;
      }
   }

   public static int v(List<Integer> var0, boolean[] var1) {
      if (!var0.isEmpty() && var1.length >= 11) {
         ArrayList var2 = new ArrayList();
         ArrayList var3 = new ArrayList();

         for (int var4 = 1; var4 <= 10; var4++) {
            if (var1[var4]) {
               int var5 = n(var0, var4).total;
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

   public int W(boolean[] var1) {
      if (this.e() && this.S >= this.T * 2 + 1 && this.z <= 0) {
         int var2 = 0;

         for (List var4 : this.C) {
            int var5 = Y(var4).total;
            if (var5 <= 21) {
               var2 = Math.max(var2, var5);
            }
         }

         return y(var2, this.n, var1);
      } else {
         return -1;
      }
   }

   public static int y(int var0, List<Integer> var1, boolean[] var2) {
      int var3 = Y(var1).total;
      if (var0 >= 2 && var0 <= 21 && !var1.isEmpty() && var2.length >= 11 && var3 < 17) {
         if (var0 >= 18) {
            for (int var4 = 1; var4 <= 10; var4++) {
               if (var2[var4] && n(var1, var4).total == 17) {
                  return var4;
               }
            }
         }

         ArrayList var16 = new ArrayList();
         int var5 = -1;
         int var6 = 21;

         for (int var7 = 1; var7 <= 10; var7++) {
            if (var2[var7]) {
               int var8 = n(var1, var7).total;
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
                  int var14 = n(var1, var13).total;
                  if (var14 < 17) {
                     var12[var13] = (boolean)0;
                     ArrayList var15 = new ArrayList(var1);
                     var15.add(var13);
                     if (y(var15, var12)) {
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

   private static boolean y(List<Integer> var0, boolean[] var1) {
      for (int var2 = 1; var2 <= 10; var2++) {
         if (var1[var2]) {
            int var3 = n(var0, var2).total;
            if (var3 > 21) {
               return true;
            }

            if (var3 < 17) {
               var1[var2] = (boolean)0;
               ArrayList var4 = new ArrayList(var0);
               var4.add(var2);
               boolean var5 = y(var4, var1);
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
      for (List var2 : this.C) {
         if (Y(var2).soft) {
            return true;
         }
      }

      return false;
   }

   public boolean F() {
      return Y(this.n).soft;
   }

   public int Q() {
      return this.T;
   }

   public int s(int var1) {
      return var1 >= 0 && var1 < this.C.size() ? Y(this.C.get(var1)).total : 0;
   }

   public int c(int var1) {
      if (var1 >= 0 && var1 < this.C.size()) {
         return Y(this.C.get(var1)).soft ? 1 : 0;
      } else {
         return 0;
      }
   }

   public void t(int var1) {
      int var2 = Math.max(1, Math.min(3, var1));
      if (this.T != var2) {
         this.T = var2;
         this.X();
         this.M = 0;
      }
   }

   public void e() {
      this.K = -1;
      this.Z = -1;
      if (this.y == io.q.i) {
         this.y = io.q.S;
         this.M = 0;
      } else {
         int var1 = this.M + 1;
         if (var1 >= this.T) {
            this.y = io.q.i;
         } else {
            this.M = var1;
         }
      }
   }

   public void I(String var1) {
      if ("dealer".equalsIgnoreCase(var1)) {
         this.y = io.q.i;
      } else if ("player".equalsIgnoreCase(var1)) {
         this.y = io.q.S;
         this.M = 0;
      }
   }

   public void M(int var1) {
      io.q var2 = var1 < 0 ? io.q.i : io.q.S;
      int var3 = var1 < 0 ? this.M : Math.min(var1, this.T - 1);
      if (var2 != this.y || var3 != this.M) {
         this.y = var2;
         this.M = var3;
         this.K = -1;
         this.Z = -1;
      }
   }

   public int P() {
      return this.y == io.q.i ? -1 : this.M;
   }

   public void w(String var1) {
      this.D = "player".equalsIgnoreCase(var1) ? io.q.S : ("dealer".equalsIgnoreCase(var1) ? io.q.i : null);
   }

   public boolean U() {
      return this.D == null;
   }

   public String m() {
      if (this.y == io.q.i) {
         return "Dealer";
      } else {
         return this.T == 1 ? "Player" : "Hand " + (this.M + 1);
      }
   }

   public String k(int var1) {
      if (var1 < 0) {
         return this.n.isEmpty() ? "-" : d(this.n);
      } else {
         return var1 < this.C.size() && !this.C.get(var1).isEmpty() ? d(this.C.get(var1)) : "-";
      }
   }

   private static String d(List<Integer> var0) {
      io.r var1 = Y(var0);
      return var1.total + (var1.total > 21 ? " bust" : (var1.soft ? " soft" : ""));
   }

   public String q() {
      return switch (this.y) {
         case S -> "HAND " + (this.M + 1);
         case i -> "DEALER";
      };
   }

   public String R() {
      return switch (this.y) {
         case S -> "hand-" + this.M;
         case i -> "dealer";
      };
   }

   public int c() {
      return this.K;
   }

   public int V() {
      return this.Z;
   }

   public String S() {
      return this.K < 1 ? "-1" : this.l();
   }

   public String A() {
      return this.K < 1 ? "none" : this.l();
   }

   private String l() {
      StringBuilder var1 = new StringBuilder(Y(this.K));
      if (this.Z >= 1) {
         var1.append(" -> ").append(Y(this.Z));
      }

      return var1.toString();
   }

   private static String Y(int var0) {
      return switch (var0) {
         case 1 -> "A";
         case 10 -> "10";
         default -> String.valueOf(var0);
      };
   }

   public int S() {
      return this.S;
   }

   public int A() {
      return this.z;
   }

   public boolean g() {
      return this.V;
   }

   private static io.r n(List<Integer> var0, int var1) {
      ArrayList var2 = new ArrayList(var0);
      var2.add(var1);
      return Y(var2);
   }

   private static io.r Y(List<Integer> var0) {
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

      return new io.r(var1, var2 > 0);
   }

   private enum q {
      S,
      i;
   }

   private record r(int total, boolean soft) {
   }
}
