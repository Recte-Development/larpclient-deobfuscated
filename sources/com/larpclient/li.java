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

public final class li {
   private static final li d = new li();
   private final List<Integer> F = new ArrayList<>();
   private final List<Integer> K = new ArrayList<>();
   private final Deque<li.p> M = new ArrayDeque<>();
   private final Map<Integer, Integer> O = new HashMap<>();
   private Map<Integer, Integer> g;
   private Map<String, Integer> z = new HashMap<>();
   private li.m B = li.m.F;
   private int D = -1;
   private boolean j;
   private boolean C;
   private int v;

   private li() {
   }

   public static li D() {
      return d;
   }

   public void X(Minecraft var1, uu var2) {
      if (!this.j) {
         this.j(var1);
      }

      int var3 = this.z() + this.M.size();
      li.p var4 = var3 == 0 ? li.p.e : (var3 == 1 ? li.p.W : (this.B == li.m.X ? li.p.W : li.p.e));
      this.M.addLast(var4);
      if (!var2.isSwapBusy()) {
         int var5 = this.D;
         this.a(var2);
         if (var3 >= 2 && var4 == li.p.W && this.B == li.m.X && l_.F().j) {
            this.D = var5 >= 1 ? var5 : this.m(var2);
            if (this.D >= 1) {
               var2.queueForce(this.D);
               this.C = (boolean)1;
            }
         }
      }
   }

   public void U(Minecraft var1, uu var2, boolean var3) {
      if (!var3 || var1 == null || var1.player == null) {
         this.l(var2);
      } else if (!var2.isSwapBusy()) {
         Map var4 = this.g(var1);
         Map var5 = this.C(var1);
         if (this.g == null) {
            this.g = new HashMap<>(var4);
            this.z = new HashMap<>(var5);
         } else {
            if (this.j && !this.M.isEmpty()) {
               for (Entry var7 : var4.entrySet()) {
                  int var8 = Math.max(0, (Integer)var7.getValue() - this.g.getOrDefault(var7.getKey(), 0));

                  while (var8-- > 0 && !this.M.isEmpty()) {
                     int var9 = this.Y(var1, var2, (Integer)var7.getKey(), var5);
                     if (var9 >= 1 && var9 <= 9) {
                        this.j(this.M.removeFirst(), var9, (Integer)var7.getKey());
                     }
                  }
               }
            }

            this.g = new HashMap<>(var4);
            this.z = new HashMap<>(var5);
            this.t(var2, var4);
            this.t(var2);
         }
      }
   }

   private void t(uu var1, Map<Integer, Integer> var2) {
      if (this.z() <= 0) {
         this.v = 0;
      } else {
         int var3 = 0;

         for (Entry var5 : this.O.entrySet()) {
            int var6 = var2.getOrDefault(var5.getKey(), 0);
            var3 += Math.min((Integer)var5.getValue(), var6);
         }

         this.v = var3 <= 0 ? this.v + 1 : 0;
         if (this.v >= 6) {
            this.c(var1, var2);
         }
      }
   }

   public void Q(uu var1) {
      this.a(var1);

      this.B = switch (this.B) {
         case F -> li.m.X;
         case X -> li.m.F;
      };
      this.t(var1);
   }

   public void p(String var1, uu var2) {
      if (var1 != null) {
         li.m var3 = switch (var1.trim().toLowerCase(Locale.ROOT)) {
            case "player" -> li.m.F;
            case "dealer" -> li.m.X;
            case "off" -> li.m.F;
            default -> null;
         };
         if (var3 != null && var3 != this.B) {
            this.a(var2);
            this.B = var3;
            this.t(var2);
         }
      }
   }

   private void t(uu var1) {
      if (!var1.isSwapBusy() && this.M.isEmpty()) {
         if (this.B != li.m.X || this.z() < 2 || !l_.F().j) {
            this.D = -1;
         } else if (this.D < 1) {
            this.D = this.m(var1);
         }
      }
   }

   private void j(Minecraft var1) {
      uu.M().T();
      this.F.clear();
      this.K.clear();
      this.O.clear();
      this.D = -1;
      this.M.clear();
      this.v = 0;
      this.j = (boolean)1;
      this.C = (boolean)0;
      if (var1 != null && var1.player != null) {
         this.g = this.g(var1);
         this.z = this.C(var1);
      }
   }

   private void j(li.p var1, int var2, int var3) {
      uu.M().f(var2, var3);
      if (var1 == li.p.e) {
         this.F.add(var2);
      } else {
         this.K.add(var2);
      }

      this.O.merge(var3, 1, Integer::sum);
      this.C = (boolean)0;
      this.D = -1;
   }

   private int m(uu var1) {
      int var2 = w(this.F);
      int var3 = w(this.K);
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

   private void a(uu var1) {
      if (var1 != null) {
         var1.clearQueue();
      }

      this.C = (boolean)0;
      this.D = -1;
   }

   private void l(uu var1) {
      var1.T();
      this.a(var1);
      this.F.clear();
      this.K.clear();
      this.O.clear();
      this.g = null;
      this.z.clear();
      this.M.clear();
      this.v = 0;
      this.j = (boolean)0;
   }

   private Map<Integer, Integer> g(Minecraft var1) {
      HashMap var2 = new HashMap();

      for (int var3 = 0; var3 < var1.player.getInventory().getContainerSize(); var3++) {
         i(var2, var1.player.getInventory().getItem(var3));
      }

      if (var1.player.containerMenu != null) {
         i(var2, var1.player.containerMenu.getCarried());
      }

      return var2;
   }

   private static void i(Map<Integer, Integer> var0, ItemStack var1) {
      if (var1 != null && !var1.isEmpty() && var1.is(Items.FILLED_MAP)) {
         MapId var2 = (MapId)var1.getComponents().get(DataComponents.MAP_ID);
         if (var2 != null) {
            var0.merge(var2.id(), var1.getCount(), Integer::sum);
         }
      }
   }

   private Map<String, Integer> C(Minecraft var1) {
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

   private int Y(Minecraft var1, uu var2, int var3, Map<String, Integer> var4) {
      for (int var5 = 0; var5 < var1.player.getInventory().getContainerSize(); var5++) {
         ItemStack var6 = var1.player.getInventory().getItem(var5);
         if (var6 != null && !var6.isEmpty() && var6.is(Items.FILLED_MAP)) {
            MapId var7 = (MapId)var6.getComponents().get(DataComponents.MAP_ID);
            String var8 = "inventory[" + var5 + "]";
            String var9 = var8 + "#" + var3;
            if (var7 != null && var7.id() == var3 && var4.getOrDefault(var9, 0) > this.z.getOrDefault(var9, 0)) {
               return var2.n(var8, var6);
            }
         }
      }

      for (int var10 = 0; var10 < var1.player.getInventory().getContainerSize(); var10++) {
         ItemStack var11 = var1.player.getInventory().getItem(var10);
         if (var11 != null && !var11.isEmpty() && var11.is(Items.FILLED_MAP)) {
            MapId var12 = (MapId)var11.getComponents().get(DataComponents.MAP_ID);
            if (var12 != null && var12.id() == var3) {
               return var2.n("inventory[" + var10 + "]", var11);
            }
         }
      }

      return -1;
   }

   private void c(uu var1, Map<Integer, Integer> var2) {
      this.a(var1);
      this.F.clear();
      this.K.clear();
      this.O.clear();
      this.M.clear();
      this.v = 0;
      this.j = (boolean)0;
      this.g = new HashMap<>(var2);
      this.z = this.C(Minecraft.getInstance());
   }

   private String y(List<Integer> var1) {
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

      var2.append(" = ").append(w(var1));
      if (w(var1) > 9) {
         var2.append(" BUST");
      }

      return var2.toString();
   }

   private int z() {
      return this.O.values().stream().mapToInt(Integer::intValue).sum();
   }

   private static int w(List<Integer> var0) {
      int var1 = 0;

      for (int var3 : var0) {
         var1 += var3;
      }

      return var1;
   }

   public int X() {
      return w(this.F);
   }

   public int v() {
      return w(this.K);
   }

   public String v() {
      return this.y(this.F);
   }

   public String G() {
      return this.y(this.K);
   }

   public int S() {
      return this.D;
   }

   public int D() {
      int var1 = uu.M().getPendingValue();
      return var1 >= 1 ? var1 : this.D;
   }

   public String q() {
      return this.B.name().toLowerCase(Locale.ROOT);
   }

   private enum m {
      F,
      X;
   }

   public enum p {
      e,
      W;
   }
}
