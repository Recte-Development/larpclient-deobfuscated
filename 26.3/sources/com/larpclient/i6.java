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

public final class i6 {
   private static final i6 t = new i6();
   private final List<Integer> U = new ArrayList<>();
   private final List<Integer> D = new ArrayList<>();
   private final Deque<i6.f> N = new ArrayDeque<>();
   private final Map<Integer, Integer> q = new HashMap<>();
   private Map<Integer, Integer> Y;
   private Map<String, Integer> W = new HashMap<>();
   private i6.j c = i6.j.Q;
   private int T = -1;
   private boolean l;
   private boolean A;
   private int R;

   private i6() {
   }

   public static i6 k() {
      return t;
   }

   public void O(Minecraft var1, v2 var2) {
      if (!this.l) {
         this.a(var1);
      }

      int var3 = this.U() + this.N.size();
      i6.f var4 = var3 == 0 ? i6.f.T : (var3 == 1 ? i6.f.r : (this.c == i6.j.L ? i6.f.r : i6.f.T));
      this.N.addLast(var4);
      if (!var2.isSwapBusy()) {
         int var5 = this.T;
         this.l(var2);
         if (var3 >= 2 && var4 == i6.f.r && this.c == i6.j.L && ql.S().k) {
            this.T = var5 >= 1 ? var5 : this.S(var2);
            if (this.T >= 1) {
               var2.queueForce(this.T);
               this.A = (boolean)1;
            }
         }
      }
   }

   public void F(Minecraft var1, v2 var2, boolean var3) {
      if (!var3 || var1 == null || var1.player == null) {
         this.P(var2);
      } else if (!var2.isSwapBusy()) {
         Map var4 = this.v(var1);
         Map var5 = this.H(var1);
         if (this.Y == null) {
            this.Y = new HashMap<>(var4);
            this.W = new HashMap<>(var5);
         } else {
            if (this.l && !this.N.isEmpty()) {
               for (Entry var7 : var4.entrySet()) {
                  int var8 = Math.max(0, (Integer)var7.getValue() - this.Y.getOrDefault(var7.getKey(), 0));

                  while (var8-- > 0 && !this.N.isEmpty()) {
                     int var9 = this.X(var1, var2, (Integer)var7.getKey(), var5);
                     if (var9 >= 1 && var9 <= 9) {
                        this.w(this.N.removeFirst(), var9, (Integer)var7.getKey());
                     }
                  }
               }
            }

            this.Y = new HashMap<>(var4);
            this.W = new HashMap<>(var5);
            this.F(var2, var4);
            this.Z(var2);
         }
      }
   }

   private void F(v2 var1, Map<Integer, Integer> var2) {
      if (this.U() <= 0) {
         this.R = 0;
      } else {
         int var3 = 0;

         for (Entry var5 : this.q.entrySet()) {
            int var6 = var2.getOrDefault(var5.getKey(), 0);
            var3 += Math.min((Integer)var5.getValue(), var6);
         }

         this.R = var3 <= 0 ? this.R + 1 : 0;
         if (this.R >= 6) {
            this.c(var1, var2);
         }
      }
   }

   public void g(v2 var1) {
      this.l(var1);

      this.c = switch (this.c) {
         case Q -> i6.j.L;
         case L -> i6.j.Q;
      };
      this.Z(var1);
   }

   public void I(String var1, v2 var2) {
      if (var1 != null) {
         i6.j var3 = switch (var1.trim().toLowerCase(Locale.ROOT)) {
            case "player" -> i6.j.Q;
            case "dealer" -> i6.j.L;
            case "off" -> i6.j.Q;
            default -> null;
         };
         if (var3 != null && var3 != this.c) {
            this.l(var2);
            this.c = var3;
            this.Z(var2);
         }
      }
   }

   private void Z(v2 var1) {
      if (!var1.isSwapBusy() && this.N.isEmpty()) {
         if (this.c != i6.j.L || this.U() < 2 || !ql.S().k) {
            this.T = -1;
         } else if (this.T < 1) {
            this.T = this.S(var1);
         }
      }
   }

   private void a(Minecraft var1) {
      v2.L().n();
      this.U.clear();
      this.D.clear();
      this.q.clear();
      this.T = -1;
      this.N.clear();
      this.R = 0;
      this.l = (boolean)1;
      this.A = (boolean)0;
      if (var1 != null && var1.player != null) {
         this.Y = this.v(var1);
         this.W = this.H(var1);
      }
   }

   private void w(i6.f var1, int var2, int var3) {
      v2.L().l(var2, var3);
      if (var1 == i6.f.T) {
         this.U.add(var2);
      } else {
         this.D.add(var2);
      }

      this.q.merge(var3, 1, Integer::sum);
      this.A = (boolean)0;
      this.T = -1;
   }

   private int S(v2 var1) {
      int var2 = k(this.U);
      int var3 = k(this.D);
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

   private void l(v2 var1) {
      if (var1 != null) {
         var1.clearQueue();
      }

      this.A = (boolean)0;
      this.T = -1;
   }

   private void P(v2 var1) {
      var1.n();
      this.l(var1);
      this.U.clear();
      this.D.clear();
      this.q.clear();
      this.Y = null;
      this.W.clear();
      this.N.clear();
      this.R = 0;
      this.l = (boolean)0;
   }

   private Map<Integer, Integer> v(Minecraft var1) {
      HashMap var2 = new HashMap();

      for (int var3 = 0; var3 < var1.player.getInventory().getContainerSize(); var3++) {
         D(var2, var1.player.getInventory().getItem(var3));
      }

      if (var1.player.containerMenu != null) {
         D(var2, var1.player.containerMenu.getCarried());
      }

      return var2;
   }

   private static void D(Map<Integer, Integer> var0, ItemStack var1) {
      if (var1 != null && !var1.isEmpty() && var1.is(Items.FILLED_MAP)) {
         MapId var2 = (MapId)var1.getComponents().get(DataComponents.MAP_ID);
         if (var2 != null) {
            var0.merge(var2.id(), var1.getCount(), Integer::sum);
         }
      }
   }

   private Map<String, Integer> H(Minecraft var1) {
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

   private int X(Minecraft var1, v2 var2, int var3, Map<String, Integer> var4) {
      for (int var5 = 0; var5 < var1.player.getInventory().getContainerSize(); var5++) {
         ItemStack var6 = var1.player.getInventory().getItem(var5);
         if (var6 != null && !var6.isEmpty() && var6.is(Items.FILLED_MAP)) {
            MapId var7 = (MapId)var6.getComponents().get(DataComponents.MAP_ID);
            String var8 = "inventory[" + var5 + "]";
            String var9 = var8 + "#" + var3;
            if (var7 != null && var7.id() == var3 && var4.getOrDefault(var9, 0) > this.W.getOrDefault(var9, 0)) {
               return var2.B(var8, var6);
            }
         }
      }

      for (int var10 = 0; var10 < var1.player.getInventory().getContainerSize(); var10++) {
         ItemStack var11 = var1.player.getInventory().getItem(var10);
         if (var11 != null && !var11.isEmpty() && var11.is(Items.FILLED_MAP)) {
            MapId var12 = (MapId)var11.getComponents().get(DataComponents.MAP_ID);
            if (var12 != null && var12.id() == var3) {
               return var2.B("inventory[" + var10 + "]", var11);
            }
         }
      }

      return -1;
   }

   private void c(v2 var1, Map<Integer, Integer> var2) {
      this.l(var1);
      this.U.clear();
      this.D.clear();
      this.q.clear();
      this.N.clear();
      this.R = 0;
      this.l = (boolean)0;
      this.Y = new HashMap<>(var2);
      this.W = this.H(Minecraft.getInstance());
   }

   private String B(List<Integer> var1) {
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

      var2.append(" = ").append(k(var1));
      if (k(var1) > 9) {
         var2.append(" BUST");
      }

      return var2.toString();
   }

   private int U() {
      return this.q.values().stream().mapToInt(Integer::intValue).sum();
   }

   private static int k(List<Integer> var0) {
      int var1 = 0;

      for (int var3 : var0) {
         var1 += var3;
      }

      return var1;
   }

   public int Z() {
      return k(this.U);
   }

   public int K() {
      return k(this.D);
   }

   public String d() {
      return this.B(this.U);
   }

   public String f() {
      return this.B(this.D);
   }

   public int g() {
      return this.T;
   }

   public int f() {
      int var1 = v2.L().getPendingValue();
      return var1 >= 1 ? var1 : this.T;
   }

   public String y() {
      return this.c.name().toLowerCase(Locale.ROOT);
   }

   public enum f {
      T,
      r;
   }

   private enum j {
      Q,
      L;
   }
}
