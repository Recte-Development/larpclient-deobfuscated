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

public final class kr {
   private static final kr m = new kr();
   private final List<Integer> T = new ArrayList<>();
   private final List<Integer> F = new ArrayList<>();
   private final Deque<kr.n> e = new ArrayDeque<>();
   private final Map<Integer, Integer> z = new HashMap<>();
   private Map<Integer, Integer> G;
   private Map<String, Integer> O = new HashMap<>();
   private kr.g L = kr.g.c;
   private int Q = -1;
   private boolean f;
   private boolean o;
   private int d;

   private kr() {
   }

   public static kr N() {
      return m;
   }

   public void a(Minecraft var1, w0 var2) {
      if (!this.f) {
         this.p(var1);
      }

      int var3 = this.w() + this.e.size();
      kr.n var4 = var3 == 0 ? kr.n.M : (var3 == 1 ? kr.n.c : (this.L == kr.g.a ? kr.n.c : kr.n.M));
      this.e.addLast(var4);
      if (!var2.isSwapBusy()) {
         int var5 = this.Q;
         this.m(var2);
         if (var3 >= 2 && var4 == kr.n.c && this.L == kr.g.a && c9.G().j) {
            this.Q = var5 >= 1 ? var5 : this.D(var2);
            if (this.Q >= 1) {
               var2.queueForce(this.Q);
               this.o = (boolean)1;
            }
         }
      }
   }

   public void R(Minecraft var1, w0 var2, boolean var3) {
      if (!var3 || var1 == null || var1.player == null) {
         this.P(var2);
      } else if (!var2.isSwapBusy()) {
         Map var4 = this.r(var1);
         Map var5 = this.R(var1);
         if (this.G == null) {
            this.G = new HashMap<>(var4);
            this.O = new HashMap<>(var5);
         } else {
            if (this.f && !this.e.isEmpty()) {
               for (Entry var7 : var4.entrySet()) {
                  int var8 = Math.max(0, (Integer)var7.getValue() - this.G.getOrDefault(var7.getKey(), 0));

                  while (var8-- > 0 && !this.e.isEmpty()) {
                     int var9 = this.h(var1, var2, (Integer)var7.getKey(), var5);
                     if (var9 >= 1 && var9 <= 9) {
                        this.a(this.e.removeFirst(), var9, (Integer)var7.getKey());
                     }
                  }
               }
            }

            this.G = new HashMap<>(var4);
            this.O = new HashMap<>(var5);
            this.q(var2, var4);
            this.A(var2);
         }
      }
   }

   private void q(w0 var1, Map<Integer, Integer> var2) {
      if (this.w() <= 0) {
         this.d = 0;
      } else {
         int var3 = 0;

         for (Entry var5 : this.z.entrySet()) {
            int var6 = var2.getOrDefault(var5.getKey(), 0);
            var3 += Math.min((Integer)var5.getValue(), var6);
         }

         this.d = var3 <= 0 ? this.d + 1 : 0;
         if (this.d >= 6) {
            this.w(var1, var2);
         }
      }
   }

   public void Q(w0 var1) {
      this.m(var1);

      this.L = switch (this.L) {
         case c -> kr.g.a;
         case a -> kr.g.c;
      };
      this.A(var1);
   }

   public void S(String var1, w0 var2) {
      if (var1 != null) {
         kr.g var3 = switch (var1.trim().toLowerCase(Locale.ROOT)) {
            case "player" -> kr.g.c;
            case "dealer" -> kr.g.a;
            case "off" -> kr.g.c;
            default -> null;
         };
         if (var3 != null && var3 != this.L) {
            this.m(var2);
            this.L = var3;
            this.A(var2);
         }
      }
   }

   private void A(w0 var1) {
      if (!var1.isSwapBusy() && this.e.isEmpty()) {
         if (this.L != kr.g.a || this.w() < 2 || !c9.G().j) {
            this.Q = -1;
         } else if (this.Q < 1) {
            this.Q = this.D(var1);
         }
      }
   }

   private void p(Minecraft var1) {
      w0.l().h();
      this.T.clear();
      this.F.clear();
      this.z.clear();
      this.Q = -1;
      this.e.clear();
      this.d = 0;
      this.f = (boolean)1;
      this.o = (boolean)0;
      if (var1 != null && var1.player != null) {
         this.G = this.r(var1);
         this.O = this.R(var1);
      }
   }

   private void a(kr.n var1, int var2, int var3) {
      w0.l().D(var2, var3);
      if (var1 == kr.n.M) {
         this.T.add(var2);
      } else {
         this.F.add(var2);
      }

      this.z.merge(var3, 1, Integer::sum);
      this.o = (boolean)0;
      this.Q = -1;
   }

   private int D(w0 var1) {
      int var2 = Y(this.T);
      int var3 = Y(this.F);
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

   private void m(w0 var1) {
      if (var1 != null) {
         var1.clearQueue();
      }

      this.o = (boolean)0;
      this.Q = -1;
   }

   private void P(w0 var1) {
      var1.h();
      this.m(var1);
      this.T.clear();
      this.F.clear();
      this.z.clear();
      this.G = null;
      this.O.clear();
      this.e.clear();
      this.d = 0;
      this.f = (boolean)0;
   }

   private Map<Integer, Integer> r(Minecraft var1) {
      HashMap var2 = new HashMap();

      for (int var3 = 0; var3 < var1.player.getInventory().getContainerSize(); var3++) {
         J(var2, var1.player.getInventory().getItem(var3));
      }

      if (var1.player.containerMenu != null) {
         J(var2, var1.player.containerMenu.getCarried());
      }

      return var2;
   }

   private static void J(Map<Integer, Integer> var0, ItemStack var1) {
      if (var1 != null && !var1.isEmpty() && var1.is(Items.FILLED_MAP)) {
         MapId var2 = (MapId)var1.getComponents().get(DataComponents.MAP_ID);
         if (var2 != null) {
            var0.merge(var2.id(), var1.getCount(), Integer::sum);
         }
      }
   }

   private Map<String, Integer> R(Minecraft var1) {
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

   private int h(Minecraft var1, w0 var2, int var3, Map<String, Integer> var4) {
      for (int var5 = 0; var5 < var1.player.getInventory().getContainerSize(); var5++) {
         ItemStack var6 = var1.player.getInventory().getItem(var5);
         if (var6 != null && !var6.isEmpty() && var6.is(Items.FILLED_MAP)) {
            MapId var7 = (MapId)var6.getComponents().get(DataComponents.MAP_ID);
            String var8 = "inventory[" + var5 + "]";
            String var9 = var8 + "#" + var3;
            if (var7 != null && var7.id() == var3 && var4.getOrDefault(var9, 0) > this.O.getOrDefault(var9, 0)) {
               return var2.i(var8, var6);
            }
         }
      }

      for (int var10 = 0; var10 < var1.player.getInventory().getContainerSize(); var10++) {
         ItemStack var11 = var1.player.getInventory().getItem(var10);
         if (var11 != null && !var11.isEmpty() && var11.is(Items.FILLED_MAP)) {
            MapId var12 = (MapId)var11.getComponents().get(DataComponents.MAP_ID);
            if (var12 != null && var12.id() == var3) {
               return var2.i("inventory[" + var10 + "]", var11);
            }
         }
      }

      return -1;
   }

   private void w(w0 var1, Map<Integer, Integer> var2) {
      this.m(var1);
      this.T.clear();
      this.F.clear();
      this.z.clear();
      this.e.clear();
      this.d = 0;
      this.f = (boolean)0;
      this.G = new HashMap<>(var2);
      this.O = this.R(Minecraft.getInstance());
   }

   private String Y(List<Integer> var1) {
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

      var2.append(" = ").append(Y(var1));
      if (Y(var1) > 9) {
         var2.append(" BUST");
      }

      return var2.toString();
   }

   private int w() {
      return this.z.values().stream().mapToInt(Integer::intValue).sum();
   }

   private static int Y(List<Integer> var0) {
      int var1 = 0;

      for (int var3 : var0) {
         var1 += var3;
      }

      return var1;
   }

   public int K() {
      return Y(this.T);
   }

   public int O() {
      return Y(this.F);
   }

   public String k() {
      return this.Y(this.T);
   }

   public String T() {
      return this.Y(this.F);
   }

   public int e() {
      return this.Q;
   }

   public int S() {
      int var1 = w0.l().getPendingValue();
      return var1 >= 1 ? var1 : this.Q;
   }

   public String L() {
      return this.L.name().toLowerCase(Locale.ROOT);
   }

   private enum g {
      c,
      a;
   }

   public enum n {
      M,
      c;
   }
}
