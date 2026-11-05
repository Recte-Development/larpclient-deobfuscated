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

public final class tb {
   private static final tb j = new tb();
   private final List<Integer> y = new ArrayList<>();
   private final List<Integer> t = new ArrayList<>();
   private final Deque<tb.u> p = new ArrayDeque<>();
   private final Map<Integer, Integer> U = new HashMap<>();
   private final Map<Integer, Integer> v = new HashMap<>();
   private Map<Integer, Integer> q = new HashMap<>();
   private int E = -1;
   private Map<Integer, Integer> m;
   private Map<String, Integer> Q = new HashMap<>();
   private tb.o T = tb.o.W;
   private int M;
   private int s = -1;
   private boolean f;
   private boolean g;
   private boolean l;
   private boolean r;
   private final ArrayDeque<Integer> X = new ArrayDeque<>();

   private tb() {
   }

   public static tb q() {
      return j;
   }

   public void B(Minecraft var1, z6 var2, tb.u var3) {
      u5.O(
         "DISP side="
            + var3
            + " roundActive="
            + this.f
            + " roundComplete="
            + this.g
            + " dealtCards="
            + this.M
            + " pending="
            + this.p
            + " target="
            + this.T
            + " enabled="
            + oz.R().r
      );
      if (!this.f || this.g) {
         this.j(var1);
      }

      if (var2.isSwapBusy()) {
         this.p.addLast(var3);
      } else {
         if (this.r) {
            if (!var2.hasQueuedForce() && !this.X.isEmpty()) {
               var2.queueForce(this.X.removeFirst());
            }

            if (var2.hasQueuedForce()) {
               u5.O("PRESET draw side=" + var3 + " value=" + var2.getPendingValue() + " left=" + this.X);
               this.p.addLast(var3);
               return;
            }

            this.r = (boolean)0;
            xa.q("Baccarat", "Preset used up; the automatic plan resumes");
         }

         this.W(var2);
         if (oz.R().r && this.T != tb.o.W && this.M >= 3 && this.y(var3) >= 1) {
            this.s = this.v(var2, var3);
            if (this.s >= 1) {
               var2.queueForce(this.s);
               this.l = (boolean)1;
            }
         }

         this.p.addLast(var3);
      }
   }

   public void o(Minecraft var1, z6 var2, boolean var3) {
      if (var3 && var1 != null && var1.player != null) {
         Map var4 = this.v(var1);
         Map var5 = this.C(var1);
         if (this.m == null) {
            this.m = new HashMap<>(var4);
            this.Q = new HashMap<>(var5);
         } else if (var2.isSwapBusy()) {
            u5.O("HOLD swapBusy=" + var2.isSwapActive() + " defer=" + var2.q());
         } else {
            if (this.f && !this.p.isEmpty()) {
               for (Entry var7 : var4.entrySet()) {
                  int var8 = Math.max(0, (Integer)var7.getValue() - this.m.getOrDefault(var7.getKey(), 0));

                  while (var8-- > 0 && !this.p.isEmpty()) {
                     int var9 = this.A(var1, var2, (Integer)var7.getKey(), var5);
                     int var10 = this.l && this.s >= 1 && var9 < 1 ? this.s : var9;
                     u5.O(
                        "TICK mapId="
                           + var7.getKey()
                           + " physical="
                           + var9
                           + " chosen="
                           + var10
                           + " pending="
                           + this.p
                           + " prev="
                           + this.m.getOrDefault(var7.getKey(), 0)
                           + " cur="
                           + var7.getValue()
                     );
                     if (var10 >= 1 && var10 <= 9) {
                        this.r(this.p.removeFirst(), var10, (Integer)var7.getKey());
                     } else {
                        u5.O("DROPPED mapId=" + var7.getKey() + " value=" + var10 + " (consumed added but pending side not removed)");
                     }
                  }
               }
            }

            this.m = new HashMap<>(var4);
            this.Q = new HashMap<>(var5);
            if (this.M > 0 && this.c(var1)) {
               this.u(var2, var4);
            }
         }
      } else {
         this.u(var2);
      }
   }

   public void J(z6 var1) {
      this.W(var1);

      this.T = switch (this.T) {
         case W -> tb.o.F;
         case F -> tb.o.H;
         case H -> tb.o.W;
      };
   }

   public void l(String var1, z6 var2) {
      if (var1 != null) {
         tb.o var3 = switch (var1.trim().toLowerCase(Locale.ROOT)) {
            case "player" -> tb.o.F;
            case "banker" -> tb.o.H;
            case "off" -> tb.o.W;
            default -> null;
         };
         if (var3 != null && var3 != this.T) {
            this.W(var2);
            this.T = var3;
         }
      }
   }

   private void j(Minecraft var1) {
      z6.u().d();
      this.y.clear();
      this.t.clear();
      this.U.clear();
      this.v.clear();
      this.q.clear();
      this.E = -1;
      this.M = 0;
      this.s = -1;
      this.p.clear();
      this.f = (boolean)1;
      this.g = (boolean)0;
      this.l = (boolean)0;
      if (var1 != null && var1.player != null) {
         this.m = this.v(var1);
         this.Q = this.C(var1);
      }
   }

   private void r(tb.u var1, int var2, int var3) {
      z6.u().S(var2, var3);
      u5.O("RECORD side=" + var1 + " value=" + var2 + " mapId=" + var3 + " player=" + this.y + " banker=" + this.t);
      if (var1 == tb.u.n) {
         this.y.add(var2);
      } else {
         this.t.add(var2);
      }

      this.U.merge(var3, 1, Integer::sum);
      this.M++;
      this.g = this.t();
      this.l = (boolean)0;
      this.s = -1;
   }

   private boolean t() {
      if (this.y.size() >= 2 && this.t.size() >= 2) {
         int var1 = m(this.y);
         int var2 = m(this.t);
         if (this.y.size() != 2 || this.t.size() != 2) {
            int var3 = this.y.size() < 3 && var1 <= 5 ? 0 : 1;
            int var4 = this.t.size() < 3 && var2 <= 5 ? 0 : 1;
            return var3 != 0 && var4 != 0;
         } else {
            return var1 < 8 && var2 < 8 ? var1 > 5 && var2 > 5 : true;
         }
      } else {
         return false;
      }
   }

   private int y(tb.u var1) {
      return var1 == tb.u.n ? this.y.size() : this.t.size();
   }

   private int v(z6 var1, tb.u var2) {
      int var3 = Integer.MIN_VALUE;
      ArrayList var4 = new ArrayList();
      ArrayList var5 = new ArrayList();
      ArrayList var6 = new ArrayList();

      for (int var7 = 1; var7 <= 9; var7++) {
         if (var1.getTemplateForValue(var7) != null) {
            int var8 = this.S(var1, var2, var7);
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

   private int S(z6 var1, tb.u var2, int var3) {
      ArrayList var4 = new ArrayList<>(this.y);
      ArrayList var5 = new ArrayList<>(this.t);
      if (var2 == tb.u.n) {
         var4.add(var3);
      } else {
         var5.add(var3);
      }

      int var6 = this.L(var1, var4, var5);
      if (var4.size() == 2 && var5.size() == 2) {
         int var7 = this.T == tb.o.F ? m(var4) - m(var5) : m(var5) - m(var4);
         if ((var2 == tb.u.n && this.T == tb.o.F || var2 == tb.u.P && this.T == tb.o.H) && var7 > 0) {
            var6 += 1000 + var7;
         }
      }

      return var6;
   }

   private int L(z6 var1, List<Integer> var2, List<Integer> var3) {
      if (var2.size() >= 2 && var3.size() >= 2) {
         int var4 = m(var2);
         int var5 = m(var3);
         if (var2.size() == 2 && var3.size() == 2) {
            if (var4 >= 8 || var5 >= 8) {
               return this.f(var4, var5);
            } else if (var4 <= 5) {
               return this.Y(var1, var2, var3, tb.u.n);
            } else {
               return var5 <= 5 ? this.Y(var1, var2, var3, tb.u.P) : this.f(var4, var5);
            }
         } else {
            return var2.size() >= 3 && var3.size() == 2 && var5 <= 5 ? this.Y(var1, var2, var3, tb.u.P) : this.f(var4, var5);
         }
      } else {
         return this.f(m(var2), m(var3));
      }
   }

   private int Y(z6 var1, List<Integer> var2, List<Integer> var3, tb.u var4) {
      int var5 = Integer.MIN_VALUE;

      for (int var6 = 1; var6 <= 9; var6++) {
         if (var1.getTemplateForValue(var6) != null) {
            ArrayList var7 = new ArrayList(var2);
            ArrayList var8 = new ArrayList(var3);
            if (var4 == tb.u.n) {
               var7.add(var6);
            } else {
               var8.add(var6);
            }

            var5 = Math.max(var5, this.L(var1, var7, var8));
         }
      }

      return var5 == Integer.MIN_VALUE ? this.f(m(var2), m(var3)) : var5;
   }

   private int f(int var1, int var2) {
      int var3 = this.T == tb.o.F ? var1 - var2 : var2 - var1;
      if (var3 > 0) {
         return 100 + var3;
      } else {
         return var3 == 0 ? 50 : var3;
      }
   }

   public boolean X(z6 var1, int var2, int var3) {
      if (this.T != tb.o.W && var2 >= 1 && var3 >= 1) {
         tb.u var4 = this.p.peekFirst();
         if (var4 == null) {
            return true;
         }

         int var5 = this.S(var1, var4, var2);
         int var6 = this.S(var1, var4, var3);
         int var7 = var5 >= 100 || var6 < 100 && (var5 >= 50 || var6 != 50) ? 0 : 1;
         u5.O(
            "SWAP_DECISION side="
               + var4
               + " target="
               + this.T
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

   public boolean I(z6 var1, int[] var2) {
      if (var1 != null && var2 != null && var2.length != 0) {
         if (var1.isSwapBusy()) {
            return false;
         }

         this.W(var1);

         for (int var6 : var2) {
            if (var1.getTemplateForValue(var6) != null) {
               this.X.addLast(var6);
            }
         }

         if (this.X.isEmpty()) {
            return false;
         }

         var1.queueForce(this.X.removeFirst());
         this.r = var1.hasQueuedForce();
         if (!this.r) {
            this.X.clear();
         }

         return this.r;
      } else {
         return false;
      }
   }

   public String D() {
      if (!this.r) {
         return "";
      }

      StringBuilder var1 = new StringBuilder();
      int var2 = z6.u().getPendingValue();
      if (var2 >= 1) {
         var1.append(var2);
      }

      for (int var4 : this.X) {
         var1.append(var1.isEmpty() ? "" : " ").append(var4);
      }

      return var1.toString();
   }

   private void W(z6 var1) {
      this.r = (boolean)0;
      this.X.clear();
      if (var1 != null) {
         var1.clearQueue();
      }

      this.l = (boolean)0;
      this.s = -1;
   }

   private void u(z6 var1) {
      var1.d();
      this.W(var1);
      this.y.clear();
      this.t.clear();
      this.U.clear();
      this.v.clear();
      this.q.clear();
      this.E = -1;
      this.m = null;
      this.Q.clear();
      this.p.clear();
      this.M = 0;
      this.f = (boolean)0;
      this.g = (boolean)0;
   }

   private Map<Integer, Integer> v(Minecraft var1) {
      HashMap var2 = new HashMap();

      for (int var3 = 0; var3 < var1.player.getInventory().getContainerSize(); var3++) {
         a(var2, var1.player.getInventory().getItem(var3));
      }

      if (var1.player.containerMenu != null) {
         a(var2, var1.player.containerMenu.getCarried());
      }

      return var2;
   }

   private static void a(Map<Integer, Integer> var0, ItemStack var1) {
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

   private int A(Minecraft var1, z6 var2, int var3, Map<String, Integer> var4) {
      for (int var5 = 0; var5 < var1.player.getInventory().getContainerSize(); var5++) {
         ItemStack var6 = var1.player.getInventory().getItem(var5);
         if (var6 != null && !var6.isEmpty() && var6.is(Items.FILLED_MAP)) {
            MapId var7 = (MapId)var6.getComponents().get(DataComponents.MAP_ID);
            String var8 = "inventory[" + var5 + "]";
            String var9 = var8 + "#" + var3;
            if (var7 != null && var7.id() == var3 && var4.getOrDefault(var9, 0) > this.Q.getOrDefault(var9, 0)) {
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

   private boolean c(Minecraft var1) {
      if (var1.player.containerMenu instanceof DispenserMenu var2) {
         HashMap var7 = new HashMap();

         for (Slot var5 : var2.slots) {
            if (var5 != null && !(var5.container instanceof Inventory)) {
               a(var7, var5.getItem());
            }
         }

         if (var2.containerId != this.E) {
            this.E = var2.containerId;
            this.q = var7;
            return false;
         }

         for (Entry var10 : var7.entrySet()) {
            if (this.U.containsKey(var10.getKey())) {
               int var6 = Math.max(0, (Integer)var10.getValue() - this.q.getOrDefault(var10.getKey(), 0));
               if (var6 > 0) {
                  this.v.merge((Integer)var10.getKey(), var6, Integer::sum);
               }
            }
         }

         this.q = var7;

         for (Entry var11 : this.U.entrySet()) {
            if (this.v.getOrDefault(var11.getKey(), 0) < (Integer)var11.getValue()) {
               return false;
            }
         }

         return true;
      } else {
         this.E = -1;
         this.q.clear();
         return false;
      }
   }

   private void u(z6 var1, Map<Integer, Integer> var2) {
      this.W(var1);
      this.y.clear();
      this.t.clear();
      this.U.clear();
      this.v.clear();
      this.q.clear();
      this.E = -1;
      this.p.clear();
      this.M = 0;
      this.f = (boolean)0;
      this.g = (boolean)0;
      this.m = new HashMap<>(var2);
      this.Q = this.C(Minecraft.getInstance());
   }

   private static int m(List<Integer> var0) {
      int var1 = 0;

      for (int var3 : var0) {
         var1 += var3;
      }

      return Math.floorMod(var1, 10);
   }

   public int V() {
      return m(this.y);
   }

   public int a() {
      return m(this.t);
   }

   public int c() {
      return this.s;
   }

   public int i() {
      return this.M;
   }

   public String I() {
      return this.T.name().toLowerCase(Locale.ROOT);
   }

   private enum o {
      W,
      F,
      H;
   }

   public enum u {
      n,
      P;
   }
}
