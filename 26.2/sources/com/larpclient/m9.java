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
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.DispenserMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.saveddata.maps.MapId;

public final class m9 {
   private static final m9 w = new m9();
   private final List<Integer> Y = new ArrayList<>();
   private final List<Integer> m = new ArrayList<>();
   private final Deque<m9.x> L = new ArrayDeque<>();
   private final Map<Integer, Integer> N = new HashMap<>();
   private final Map<Integer, Integer> X = new HashMap<>();
   private Map<Integer, Integer> r = new HashMap<>();
   private int l = -1;
   private Map<Integer, Integer> u;
   private Map<String, Integer> f = new HashMap<>();
   private m9.k B = m9.k.W;
   private int s;
   private int S = -1;
   private boolean t;
   private boolean h;
   private boolean J;
   private boolean R;
   private final ArrayDeque<Integer> C = new ArrayDeque<>();

   private m9() {
   }

   public static m9 O() {
      return w;
   }

   public void b(Minecraft var1, wk var2, m9.x var3) {
      le.q(
         "DISP side="
            + var3
            + " roundActive="
            + this.t
            + " roundComplete="
            + this.h
            + " dealtCards="
            + this.s
            + " pending="
            + this.L
            + " target="
            + this.B
            + " enabled="
            + m6.W().K
      );
      if (!this.t || this.h) {
         this.b(var1);
      }

      if (var2.isSwapBusy()) {
         this.L.addLast(var3);
      } else {
         if (this.R) {
            if (!var2.hasQueuedForce() && !this.C.isEmpty()) {
               var2.queueForce(this.C.removeFirst());
            }

            if (var2.hasQueuedForce()) {
               le.q("PRESET draw side=" + var3 + " value=" + var2.getPendingValue() + " left=" + this.C);
               this.L.addLast(var3);
               return;
            }

            this.R = (boolean)0;
            te.Z("Baccarat", "Preset used up; the automatic plan resumes");
         }

         this.X(var2);
         if (m6.W().K && this.B != m9.k.W && this.s >= 3 && this.w(var3) >= 1) {
            this.S = this.S(var2, var3);
            if (this.S >= 1) {
               var2.queueForce(this.S);
               this.J = (boolean)1;
            }
         }

         this.L.addLast(var3);
      }
   }

   public void X(Minecraft var1, wk var2, boolean var3) {
      if (var3 && var1 != null && var1.player != null) {
         Map var4 = this.t(var1);
         Map var5 = this.J(var1);
         if (this.u == null) {
            this.u = new HashMap<>(var4);
            this.f = new HashMap<>(var5);
         } else if (var2.isSwapBusy()) {
            le.q("HOLD swapBusy=" + var2.isSwapActive() + " defer=" + var2.R());
         } else {
            if (this.t && !this.L.isEmpty()) {
               for (Entry var7 : var4.entrySet()) {
                  int var8 = Math.max(0, (Integer)var7.getValue() - this.u.getOrDefault(var7.getKey(), 0));

                  while (var8-- > 0 && !this.L.isEmpty()) {
                     int var9 = this.r(var1, var2, (Integer)var7.getKey(), var5);
                     int var10 = this.J && this.S >= 1 && var9 < 1 ? this.S : var9;
                     le.q(
                        "TICK mapId="
                           + var7.getKey()
                           + " physical="
                           + var9
                           + " chosen="
                           + var10
                           + " pending="
                           + this.L
                           + " prev="
                           + this.u.getOrDefault(var7.getKey(), 0)
                           + " cur="
                           + var7.getValue()
                     );
                     if (var10 >= 1 && var10 <= 9) {
                        this.L(this.L.removeFirst(), var10, (Integer)var7.getKey());
                     } else {
                        le.q("DROPPED mapId=" + var7.getKey() + " value=" + var10 + " (consumed added but pending side not removed)");
                     }
                  }
               }
            }

            this.u = new HashMap<>(var4);
            this.f = new HashMap<>(var5);
            if (this.s > 0 && this.X(var1)) {
               this.V(var2, var4);
            }
         }
      } else {
         this.U(var2);
      }
   }

   public void I(wk var1) {
      this.X(var1);

      this.B = switch (this.B) {
         case W -> m9.k.a;
         case a -> m9.k.v;
         case v -> m9.k.W;
      };
   }

   public void q(String var1, wk var2) {
      if (var1 != null) {
         m9.k var3 = switch (var1.trim().toLowerCase(Locale.ROOT)) {
            case "player" -> m9.k.a;
            case "banker" -> m9.k.v;
            case "off" -> m9.k.W;
            default -> null;
         };
         if (var3 != null && var3 != this.B) {
            this.X(var2);
            this.B = var3;
         }
      }
   }

   private void b(Minecraft var1) {
      wk.m().h();
      this.Y.clear();
      this.m.clear();
      this.N.clear();
      this.X.clear();
      this.r.clear();
      this.l = -1;
      this.s = 0;
      this.S = -1;
      this.L.clear();
      this.t = (boolean)1;
      this.h = (boolean)0;
      this.J = (boolean)0;
      if (var1 != null && var1.player != null) {
         this.u = this.t(var1);
         this.f = this.J(var1);
      }
   }

   private void L(m9.x var1, int var2, int var3) {
      wk.m().D(var2, var3);
      le.q("RECORD side=" + var1 + " value=" + var2 + " mapId=" + var3 + " player=" + this.Y + " banker=" + this.m);
      if (var1 == m9.x.x) {
         this.Y.add(var2);
      } else {
         this.m.add(var2);
      }

      this.N.merge(var3, 1, Integer::sum);
      this.s++;
      this.h = this.T();
      this.J = (boolean)0;
      this.S = -1;
   }

   private boolean T() {
      if (this.Y.size() >= 2 && this.m.size() >= 2) {
         int var1 = M(this.Y);
         int var2 = M(this.m);
         if (this.Y.size() != 2 || this.m.size() != 2) {
            int var3 = this.Y.size() < 3 && var1 <= 5 ? 0 : 1;
            int var4 = this.m.size() < 3 && var2 <= 5 ? 0 : 1;
            return var3 != 0 && var4 != 0;
         } else {
            return var1 < 8 && var2 < 8 ? var1 > 5 && var2 > 5 : true;
         }
      } else {
         return false;
      }
   }

   private int w(m9.x var1) {
      return var1 == m9.x.x ? this.Y.size() : this.m.size();
   }

   private int S(wk var1, m9.x var2) {
      int var3 = Integer.MIN_VALUE;
      ArrayList var4 = new ArrayList();
      ArrayList var5 = new ArrayList();
      ArrayList var6 = new ArrayList();

      for (int var7 = 1; var7 <= 9; var7++) {
         if (var1.getTemplateForValue(var7) != null) {
            int var8 = this.n(var1, var2, var7);
            if (var8 >= 100) {
               var5.add(var7);
            } else if (var8 == 50) {
               var6.add(var7);
            }

            if (var8 > var3) {
               var3 = var8;
               var4.clear();
               var4.add(var7);
            } else if (var8 == var3) {
               var4.add(var7);
            }
         }
      }

      if (!var5.isEmpty()) {
         return (Integer)var5.get(ThreadLocalRandom.current().nextInt(var5.size()));
      } else if (!var6.isEmpty()) {
         return (Integer)var6.get(ThreadLocalRandom.current().nextInt(var6.size()));
      } else {
         return var4.isEmpty() ? -1 : (Integer)var4.get(ThreadLocalRandom.current().nextInt(var4.size()));
      }
   }

   private int n(wk var1, m9.x var2, int var3) {
      ArrayList var4 = new ArrayList<>(this.Y);
      ArrayList var5 = new ArrayList<>(this.m);
      if (var2 == m9.x.x) {
         var4.add(var3);
      } else {
         var5.add(var3);
      }

      int var6 = this.f(var1, var4, var5);
      if (var4.size() == 2 && var5.size() == 2) {
         int var7 = this.B == m9.k.a ? M(var4) - M(var5) : M(var5) - M(var4);
         if ((var2 == m9.x.x && this.B == m9.k.a || var2 == m9.x.t && this.B == m9.k.v) && var7 > 0) {
            var6 += 1000 + var7;
         }
      }

      return var6;
   }

   private int f(wk var1, List<Integer> var2, List<Integer> var3) {
      if (var2.size() >= 2 && var3.size() >= 2) {
         int var4 = M(var2);
         int var5 = M(var3);
         if (var2.size() == 2 && var3.size() == 2) {
            if (var4 >= 8 || var5 >= 8) {
               return this.o(var4, var5);
            } else if (var4 <= 5) {
               return this.L(var1, var2, var3, m9.x.x);
            } else {
               return var5 <= 5 ? this.L(var1, var2, var3, m9.x.t) : this.o(var4, var5);
            }
         } else {
            return var2.size() >= 3 && var3.size() == 2 && var5 <= 5 ? this.L(var1, var2, var3, m9.x.t) : this.o(var4, var5);
         }
      } else {
         return this.o(M(var2), M(var3));
      }
   }

   private int L(wk var1, List<Integer> var2, List<Integer> var3, m9.x var4) {
      int var5 = Integer.MIN_VALUE;

      for (int var6 = 1; var6 <= 9; var6++) {
         if (var1.getTemplateForValue(var6) != null) {
            ArrayList var7 = new ArrayList(var2);
            ArrayList var8 = new ArrayList(var3);
            if (var4 == m9.x.x) {
               var7.add(var6);
            } else {
               var8.add(var6);
            }

            var5 = Math.max(var5, this.f(var1, var7, var8));
         }
      }

      return var5 == Integer.MIN_VALUE ? this.o(M(var2), M(var3)) : var5;
   }

   private int o(int var1, int var2) {
      int var3 = this.B == m9.k.a ? var1 - var2 : var2 - var1;
      if (var3 > 0) {
         return 100 + var3;
      } else {
         return var3 == 0 ? 50 : var3;
      }
   }

   public boolean y(wk var1, int var2, int var3) {
      if (this.B != m9.k.W && var2 >= 1 && var3 >= 1) {
         m9.x var4 = this.L.peekFirst();
         if (var4 == null) {
            return true;
         }

         int var5 = this.n(var1, var4, var2);
         int var6 = this.n(var1, var4, var3);
         int var7 = var5 >= 100 || var6 < 100 && (var5 >= 50 || var6 != 50) ? 0 : 1;
         le.q(
            "SWAP_DECISION side="
               + var4
               + " target="
               + this.B
               + " real="
               + var2
               + " armed="
               + var3
               + " realScore="
               + var5
               + " armedScore="
               + var6
               + " swap="
               + var7
         );
         return (boolean)var7;
      } else {
         return true;
      }
   }

   public boolean T(wk var1, int[] var2) {
      if (var1 != null && var2 != null && var2.length != 0) {
         if (var1.isSwapBusy()) {
            return false;
         }

         this.X(var1);

         for (int var6 : var2) {
            if (var1.getTemplateForValue(var6) != null) {
               this.C.addLast(var6);
            }
         }

         if (this.C.isEmpty()) {
            return false;
         }

         var1.queueForce(this.C.removeFirst());
         this.R = var1.hasQueuedForce();
         if (!this.R) {
            this.C.clear();
         }

         return this.R;
      } else {
         return false;
      }
   }

   public String n() {
      if (!this.R) {
         return "";
      }

      StringBuilder var1 = new StringBuilder();
      int var2 = wk.m().getPendingValue();
      if (var2 >= 1) {
         var1.append(var2);
      }

      for (int var4 : this.C) {
         var1.append(var1.isEmpty() ? "" : " ").append(var4);
      }

      return var1.toString();
   }

   private void X(wk var1) {
      this.R = (boolean)0;
      this.C.clear();
      if (var1 != null) {
         var1.clearQueue();
      }

      this.J = (boolean)0;
      this.S = -1;
   }

   private void U(wk var1) {
      var1.h();
      this.X(var1);
      this.Y.clear();
      this.m.clear();
      this.N.clear();
      this.X.clear();
      this.r.clear();
      this.l = -1;
      this.u = null;
      this.f.clear();
      this.L.clear();
      this.s = 0;
      this.t = (boolean)0;
      this.h = (boolean)0;
   }

   private Map<Integer, Integer> t(Minecraft var1) {
      HashMap var2 = new HashMap();

      for (int var3 = 0; var3 < var1.player.getInventory().getContainerSize(); var3++) {
         c(var2, var1.player.getInventory().getItem(var3));
      }

      if (var1.player.containerMenu != null) {
         c(var2, var1.player.containerMenu.getCarried());
      }

      return var2;
   }

   private static void c(Map<Integer, Integer> var0, ItemStack var1) {
      if (var1 != null && !var1.isEmpty() && var1.is(Items.FILLED_MAP)) {
         MapId var2 = (MapId)var1.getComponents().get(DataComponents.MAP_ID);
         if (var2 != null) {
            var0.merge(var2.id(), var1.getCount(), Integer::sum);
         }
      }
   }

   private Map<String, Integer> J(Minecraft var1) {
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

   private int r(Minecraft var1, wk var2, int var3, Map<String, Integer> var4) {
      for (int var5 = 0; var5 < var1.player.getInventory().getContainerSize(); var5++) {
         ItemStack var6 = var1.player.getInventory().getItem(var5);
         if (var6 != null && !var6.isEmpty() && var6.is(Items.FILLED_MAP)) {
            MapId var7 = (MapId)var6.getComponents().get(DataComponents.MAP_ID);
            String var8 = "inventory[" + var5 + "]";
            String var9 = var8 + "#" + var3;
            if (var7 != null && var7.id() == var3 && var4.getOrDefault(var9, 0) > this.f.getOrDefault(var9, 0)) {
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

   private boolean X(Minecraft var1) {
      if (var1.player.containerMenu instanceof DispenserMenu var2) {
         HashMap var7 = new HashMap();

         for (Slot var5 : var2.slots) {
            if (var5 != null && !(var5.container instanceof Inventory)) {
               c(var7, var5.getItem());
            }
         }

         if (var2.containerId != this.l) {
            this.l = var2.containerId;
            this.r = var7;
            return false;
         }

         for (Entry var10 : var7.entrySet()) {
            if (this.N.containsKey(var10.getKey())) {
               int var6 = Math.max(0, (Integer)var10.getValue() - this.r.getOrDefault(var10.getKey(), 0));
               if (var6 > 0) {
                  this.X.merge((Integer)var10.getKey(), var6, Integer::sum);
               }
            }
         }

         this.r = var7;

         for (Entry var11 : this.N.entrySet()) {
            if (this.X.getOrDefault(var11.getKey(), 0) < (Integer)var11.getValue()) {
               return false;
            }
         }

         return true;
      } else {
         this.l = -1;
         this.r.clear();
         return false;
      }
   }

   private void V(wk var1, Map<Integer, Integer> var2) {
      this.X(var1);
      this.Y.clear();
      this.m.clear();
      this.N.clear();
      this.X.clear();
      this.r.clear();
      this.l = -1;
      this.L.clear();
      this.s = 0;
      this.t = (boolean)0;
      this.h = (boolean)0;
      this.u = new HashMap<>(var2);
      this.f = this.J(Minecraft.getInstance());
   }

   private static int M(List<Integer> var0) {
      int var1 = 0;

      for (int var3 : var0) {
         var1 += var3;
      }

      return Math.floorMod(var1, 10);
   }

   public int H() {
      return M(this.Y);
   }

   public int x() {
      return M(this.m);
   }

   public int v() {
      return this.S;
   }

   public int u() {
      return this.s;
   }

   public String T() {
      return this.B.name().toLowerCase(Locale.ROOT);
   }

   private enum k {
      W,
      a,
      v;
   }

   public enum x {
      x,
      t;
   }
}
