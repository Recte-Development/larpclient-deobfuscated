package com.larpclient;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ThreadLocalRandom;
import net.minecraft.client.Minecraft;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.saveddata.maps.MapId;

public final class g {
   private static final g y = new g();
   private final List<Integer> Z = new ArrayList<>();
   private final List<Integer> F = new ArrayList<>();
   private final Deque<g.t> n = new ArrayDeque<>();
   private final Map<Integer, Integer> c = new HashMap<>();
   private Map<Integer, Integer> x;
   private Map<String, Integer> b = new HashMap<>();
   private g.r q = g.r.Q;
   private int d = -1;
   private boolean T;
   private boolean P;
   private int R;

   private g() {
   }

   public static g s() {
      return y;
   }

   public void M(Minecraft var1, zw var2) {
      if (!this.T) {
         this.Q(var1);
      }

      int var3 = this.L() + this.n.size();
      g.t var4 = var3 == 0 ? g.t.A : (var3 == 1 ? g.t.v : (this.q == g.r.X ? g.t.v : g.t.A));
      this.n.addLast(var4);
      if (!var2.isSwapBusy()) {
         int var5 = this.d;
         this.o(var2);
         if (var3 >= 2 && var4 == g.t.v && this.q == g.r.X && ku.y().f) {
            this.d = var5 >= 1 ? var5 : this.G(var2);
            if (this.d >= 1) {
               var2.queueForce(this.d);
               this.P = (boolean)1;
            }
         }
      }
   }

   public void M(Minecraft var1, zw var2, boolean var3) {
      if (!var3 || var1 == null || var1.player == null) {
         this.m(var2);
      } else if (!var2.isSwapBusy()) {
         Map var4 = this.X(var1);
         Map var5 = this.l(var1);
         if (this.x == null) {
            this.x = new HashMap<>(var4);
            this.b = new HashMap<>(var5);
         } else {
            if (this.T && !this.n.isEmpty()) {
               for (Entry var7 : var4.entrySet()) {
                  int var8 = Math.max(0, (Integer)var7.getValue() - this.x.getOrDefault(var7.getKey(), 0));

                  while (var8-- > 0 && !this.n.isEmpty()) {
                     int var9 = this.N(var1, var2, (Integer)var7.getKey(), var5);
                     if (var9 >= 1 && var9 <= 9) {
                        this.z(this.n.removeFirst(), var9, (Integer)var7.getKey());
                     }
                  }
               }
            }

            this.x = new HashMap<>(var4);
            this.b = new HashMap<>(var5);
            this.V(var2, var4);
            this.O(var2);
         }
      }
   }

   private void V(zw var1, Map<Integer, Integer> var2) {
      if (this.L() <= 0) {
         this.R = 0;
      } else {
         int var3 = 0;

         for (Entry var5 : this.c.entrySet()) {
            int var6 = var2.getOrDefault(var5.getKey(), 0);
            var3 += Math.min((Integer)var5.getValue(), var6);
         }

         this.R = var3 <= 0 ? this.R + 1 : 0;
         if (this.R >= 6) {
            this.E(var1, var2);
         }
      }
   }

   public void G(zw var1) {
      this.o(var1);

      this.q = switch (this.q) {
         case Q -> g.r.X;
         case X -> g.r.Q;
      };
      this.O(var1);
   }

   public void K(String var1, zw var2) {
      if (var1 != null) {
         g.r var3 = switch (var1.trim().toLowerCase(Locale.ROOT)) {
            case "player" -> g.r.Q;
            case "dealer" -> g.r.X;
            case "off" -> g.r.Q;
            default -> null;
         };
         if (var3 != null && var3 != this.q) {
            this.o(var2);
            this.q = var3;
            this.O(var2);
         }
      }
   }

   private void O(zw var1) {
      if (!var1.isSwapBusy() && this.n.isEmpty()) {
         if (this.q != g.r.X || this.L() < 2 || !ku.y().f) {
            this.d = -1;
         } else if (this.d < 1) {
            this.d = this.G(var1);
         }
      }
   }

   private void Q(Minecraft var1) {
      zw.K().d();
      this.Z.clear();
      this.F.clear();
      this.c.clear();
      this.d = -1;
      this.n.clear();
      this.R = 0;
      this.T = (boolean)1;
      this.P = (boolean)0;
      if (var1 != null && var1.player != null) {
         this.x = this.X(var1);
         this.b = this.l(var1);
      }
   }

   private void z(g.t var1, int var2, int var3) {
      zw.K().S(var2, var3);
      if (var1 == g.t.A) {
         this.Z.add(var2);
      } else {
         this.F.add(var2);
      }

      this.c.merge(var3, 1, Integer::sum);
      this.P = (boolean)0;
      this.d = -1;
   }

   private int G(zw var1) {
      int var2 = t(this.Z);
      int var3 = t(this.F);
      if (var2 <= 9 && var3 < 9) {
         ArrayList var4 = new ArrayList();
         ArrayList var5 = new ArrayList();
         int var6 = -1;

         for (int var7 = 1; var7 <= 9; var7++) {
            if (var1.getTemplateForValue(var7) != null) {
               int var8 = var3 + var7;
               if (var8 <= 9) {
                  if (var2 >= 1 && var8 >= var2) {
                     var4.add(var7);
                  }

                  if (var8 > var6) {
                     var6 = var8;
                     var5.clear();
                     var5.add(var7);
                  } else if (var8 == var6) {
                     var5.add(var7);
                  }
               }
            }
         }

         if (!var4.isEmpty()) {
            return (Integer)var4.get(ThreadLocalRandom.current().nextInt(var4.size()));
         } else {
            return var5.isEmpty() ? -1 : (Integer)var5.get(ThreadLocalRandom.current().nextInt(var5.size()));
         }
      } else {
         return -1;
      }
   }

   private void o(zw var1) {
      if (var1 != null) {
         var1.clearQueue();
      }

      this.P = (boolean)0;
      this.d = -1;
   }

   private void m(zw var1) {
      var1.d();
      this.o(var1);
      this.Z.clear();
      this.F.clear();
      this.c.clear();
      this.x = null;
      this.b.clear();
      this.n.clear();
      this.R = 0;
      this.T = (boolean)0;
   }

   private Map<Integer, Integer> X(Minecraft var1) {
      HashMap var2 = new HashMap();

      for (int var3 = 0; var3 < var1.player.getInventory().getContainerSize(); var3++) {
         Y(var2, var1.player.getInventory().getItem(var3));
      }

      if (var1.player.containerMenu != null) {
         Y(var2, var1.player.containerMenu.getCarried());
      }

      return var2;
   }

   private static void Y(Map<Integer, Integer> var0, ItemStack var1) {
      if (var1 != null && !var1.isEmpty() && var1.is(Items.FILLED_MAP)) {
         MapId var2 = (MapId)var1.getComponents().get(DataComponents.MAP_ID);
         if (var2 != null) {
            var0.merge(var2.id(), var1.getCount(), Integer::sum);
         }
      }
   }

   private Map<String, Integer> l(Minecraft var1) {
      HashMap var2 = new HashMap();

      for (int var3 = 0; var3 < var1.player.getInventory().getContainerSize(); var3++) {
         ItemStack var4 = var1.player.getInventory().getItem(var3);
         if (var4 != null && !var4.isEmpty() && var4.is(Items.FILLED_MAP)) {
            MapId var5 = (MapId)var4.getComponents().get(DataComponents.MAP_ID);
            if (var5 != null) {
               var2.put("inventory[" + var3 + "]#" + var5.id(), var4.getCount());
            }
         }
      }

      return var2;
   }

   private int N(Minecraft var1, zw var2, int var3, Map<String, Integer> var4) {
      for (int var5 = 0; var5 < var1.player.getInventory().getContainerSize(); var5++) {
         ItemStack var6 = var1.player.getInventory().getItem(var5);
         if (var6 != null && !var6.isEmpty() && var6.is(Items.FILLED_MAP)) {
            MapId var7 = (MapId)var6.getComponents().get(DataComponents.MAP_ID);
            String var8 = "inventory[" + var5 + "]";
            String var9 = var8 + "#" + var3;
            if (var7 != null && var7.id() == var3 && var4.getOrDefault(var9, 0) > this.b.getOrDefault(var9, 0)) {
               return var2.J(var8, var6);
            }
         }
      }

      for (int var10 = 0; var10 < var1.player.getInventory().getContainerSize(); var10++) {
         ItemStack var11 = var1.player.getInventory().getItem(var10);
         if (var11 != null && !var11.isEmpty() && var11.is(Items.FILLED_MAP)) {
            MapId var12 = (MapId)var11.getComponents().get(DataComponents.MAP_ID);
            if (var12 != null && var12.id() == var3) {
               return var2.J("inventory[" + var10 + "]", var11);
            }
         }
      }

      return -1;
   }

   private void E(zw var1, Map<Integer, Integer> var2) {
      this.o(var1);
      this.Z.clear();
      this.F.clear();
      this.c.clear();
      this.n.clear();
      this.R = 0;
      this.T = (boolean)0;
      this.x = new HashMap<>(var2);
      this.b = this.l(Minecraft.getInstance());
   }

   private String g(List<Integer> var1) {
      if (var1.isEmpty()) {
         return "-";
      }

      StringBuilder var2 = new StringBuilder();

      for (int var4 : var1) {
         if (!var2.isEmpty()) {
            var2.append(", ");
         }

         var2.append(var4);
      }

      var2.append(" = ").append(t(var1));
      if (t(var1) > 9) {
         var2.append(" BUST");
      }

      return var2.toString();
   }

   private int L() {
      return this.c.values().stream().mapToInt(Integer::intValue).sum();
   }

   private static int t(List<Integer> var0) {
      int var1 = 0;

      for (int var3 : var0) {
         var1 += var3;
      }

      return var1;
   }

   public int c() {
      return t(this.Z);
   }

   public int b() {
      return t(this.F);
   }

   public String f() {
      return this.g(this.Z);
   }

   public String l() {
      return this.g(this.F);
   }

   public int D() {
      return this.d;
   }

   public int F() {
      int var1 = zw.K().getPendingValue();
      return var1 >= 1 ? var1 : this.d;
   }

   public String Q() {
      return this.q.name().toLowerCase(Locale.ROOT);
   }

   private enum r {
      Q,
      X;
   }

   public enum t {
      A,
      v;
   }
}
