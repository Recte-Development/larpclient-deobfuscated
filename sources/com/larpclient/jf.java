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

public final class jf {
   private static final jf P = new jf();
   private final List<Integer> R = new ArrayList<>();
   private final List<Integer> a = new ArrayList<>();
   private final Deque<jf.k> v = new ArrayDeque<>();
   private final Map<Integer, Integer> Z = new HashMap<>();
   private final Map<Integer, Integer> E = new HashMap<>();
   private Map<Integer, Integer> T = new HashMap<>();
   private int t = -1;
   private Map<Integer, Integer> G;
   private Map<String, Integer> m = new HashMap<>();
   private jf.n M = jf.n.d;
   private int w;
   private int H = -1;
   private boolean k;
   private boolean J;
   private boolean g;
   private boolean F;
   private final ArrayDeque<Integer> r = new ArrayDeque<>();

   private jf() {
   }

   public static jf u() {
      return P;
   }

   public void f(Minecraft var1, u3 var2, jf.k var3) {
      u.O(
         "DISP side="
            + var3
            + " roundActive="
            + this.k
            + " roundComplete="
            + this.J
            + " dealtCards="
            + this.w
            + " pending="
            + this.v
            + " target="
            + this.M
            + " enabled="
            + hm.O().T
      );
      if (!this.k || this.J) {
         this.a(var1);
      }

      if (var2.isSwapBusy()) {
         this.v.addLast(var3);
      } else {
         if (this.F) {
            if (!var2.hasQueuedForce() && !this.r.isEmpty()) {
               var2.queueForce(this.r.removeFirst());
            }

            if (var2.hasQueuedForce()) {
               u.O("PRESET draw side=" + var3 + " value=" + var2.getPendingValue() + " left=" + this.r);
               this.v.addLast(var3);
               return;
            }

            this.F = (boolean)0;
            hh.K("Baccarat", "Preset used up; the automatic plan resumes");
         }

         this.e(var2);
         if (hm.O().T && this.M != jf.n.d && this.w >= 3 && this.c(var3) >= 1) {
            this.H = this.U(var2, var3);
            if (this.H >= 1) {
               var2.queueForce(this.H);
               this.g = (boolean)1;
            }
         }

         this.v.addLast(var3);
      }
   }

   public void E(Minecraft var1, u3 var2, boolean var3) {
      if (var3 && var1 != null && var1.player != null) {
         Map var4 = this.r(var1);
         Map var5 = this.T(var1);
         if (this.G == null) {
            this.G = new HashMap<>(var4);
            this.m = new HashMap<>(var5);
         } else if (var2.isSwapBusy()) {
            u.O("HOLD swapBusy=" + var2.isSwapActive() + " defer=" + var2.T());
         } else {
            if (this.k && !this.v.isEmpty()) {
               for (Entry var7 : var4.entrySet()) {
                  int var8 = Math.max(0, (Integer)var7.getValue() - this.G.getOrDefault(var7.getKey(), 0));

                  while (var8-- > 0 && !this.v.isEmpty()) {
                     int var9 = this.q(var1, var2, (Integer)var7.getKey(), var5);
                     int var10 = this.g && this.H >= 1 && var9 < 1 ? this.H : var9;
                     u.O(
                        "TICK mapId="
                           + var7.getKey()
                           + " physical="
                           + var9
                           + " chosen="
                           + var10
                           + " pending="
                           + this.v
                           + " prev="
                           + this.G.getOrDefault(var7.getKey(), 0)
                           + " cur="
                           + var7.getValue()
                     );
                     if (var10 >= 1 && var10 <= 9) {
                        this.C(this.v.removeFirst(), var10, (Integer)var7.getKey());
                     } else {
                        u.O("DROPPED mapId=" + var7.getKey() + " value=" + var10 + " (consumed added but pending side not removed)");
                     }
                  }
               }
            }

            this.G = new HashMap<>(var4);
            this.m = new HashMap<>(var5);
            if (this.w > 0 && this.K(var1)) {
               this.w(var2, var4);
            }
         }
      } else {
         this.V(var2);
      }
   }

   public void h(u3 var1) {
      this.e(var1);

      this.M = switch (this.M) {
         case d -> jf.n.M;
         case M -> jf.n.U;
         case U -> jf.n.d;
      };
   }

   public void Y(String var1, u3 var2) {
      if (var1 != null) {
         jf.n var3 = switch (var1.trim().toLowerCase(Locale.ROOT)) {
            case "player" -> jf.n.M;
            case "banker" -> jf.n.U;
            case "off" -> jf.n.d;
            default -> null;
         };
         if (var3 != null && var3 != this.M) {
            this.e(var2);
            this.M = var3;
         }
      }
   }

   private void a(Minecraft var1) {
      u3.H().T();
      this.R.clear();
      this.a.clear();
      this.Z.clear();
      this.E.clear();
      this.T.clear();
      this.t = -1;
      this.w = 0;
      this.H = -1;
      this.v.clear();
      this.k = (boolean)1;
      this.J = (boolean)0;
      this.g = (boolean)0;
      if (var1 != null && var1.player != null) {
         this.G = this.r(var1);
         this.m = this.T(var1);
      }
   }

   private void C(jf.k var1, int var2, int var3) {
      u3.H().f(var2, var3);
      u.O("RECORD side=" + var1 + " value=" + var2 + " mapId=" + var3 + " player=" + this.R + " banker=" + this.a);
      if (var1 == jf.k.Z) {
         this.R.add(var2);
      } else {
         this.a.add(var2);
      }

      this.Z.merge(var3, 1, Integer::sum);
      this.w++;
      this.J = this.q();
      this.g = (boolean)0;
      this.H = -1;
   }

   private boolean q() {
      if (this.R.size() >= 2 && this.a.size() >= 2) {
         int var1 = M(this.R);
         int var2 = M(this.a);
         if (this.R.size() != 2 || this.a.size() != 2) {
            int var3 = this.R.size() < 3 && var1 <= 5 ? 0 : 1;
            int var4 = this.a.size() < 3 && var2 <= 5 ? 0 : 1;
            return var3 != 0 && var4 != 0;
         } else {
            return var1 < 8 && var2 < 8 ? var1 > 5 && var2 > 5 : true;
         }
      } else {
         return false;
      }
   }

   private int c(jf.k var1) {
      return var1 == jf.k.Z ? this.R.size() : this.a.size();
   }

   private int U(u3 var1, jf.k var2) {
      int var3 = Integer.MIN_VALUE;
      ArrayList var4 = new ArrayList();
      ArrayList var5 = new ArrayList();
      ArrayList var6 = new ArrayList();

      for (int var7 = 1; var7 <= 9; var7++) {
         if (var1.getTemplateForValue(var7) != null) {
            int var8 = this.N(var1, var2, var7);
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

   private int N(u3 var1, jf.k var2, int var3) {
      ArrayList var4 = new ArrayList<>(this.R);
      ArrayList var5 = new ArrayList<>(this.a);
      if (var2 == jf.k.Z) {
         var4.add(var3);
      } else {
         var5.add(var3);
      }

      int var6 = this.H(var1, var4, var5);
      if (var4.size() == 2 && var5.size() == 2) {
         int var7 = this.M == jf.n.M ? M(var4) - M(var5) : M(var5) - M(var4);
         if ((var2 == jf.k.Z && this.M == jf.n.M || var2 == jf.k.x && this.M == jf.n.U) && var7 > 0) {
            var6 += 1000 + var7;
         }
      }

      return var6;
   }

   private int H(u3 var1, List<Integer> var2, List<Integer> var3) {
      if (var2.size() >= 2 && var3.size() >= 2) {
         int var4 = M(var2);
         int var5 = M(var3);
         if (var2.size() == 2 && var3.size() == 2) {
            if (var4 >= 8 || var5 >= 8) {
               return this.v(var4, var5);
            } else if (var4 <= 5) {
               return this.t(var1, var2, var3, jf.k.Z);
            } else {
               return var5 <= 5 ? this.t(var1, var2, var3, jf.k.x) : this.v(var4, var5);
            }
         } else {
            return var2.size() >= 3 && var3.size() == 2 && var5 <= 5 ? this.t(var1, var2, var3, jf.k.x) : this.v(var4, var5);
         }
      } else {
         return this.v(M(var2), M(var3));
      }
   }

   private int t(u3 var1, List<Integer> var2, List<Integer> var3, jf.k var4) {
      int var5 = Integer.MIN_VALUE;

      for (int var6 = 1; var6 <= 9; var6++) {
         if (var1.getTemplateForValue(var6) != null) {
            ArrayList var7 = new ArrayList(var2);
            ArrayList var8 = new ArrayList(var3);
            if (var4 == jf.k.Z) {
               var7.add(var6);
            } else {
               var8.add(var6);
            }

            var5 = Math.max(var5, this.H(var1, var7, var8));
         }
      }

      return var5 == Integer.MIN_VALUE ? this.v(M(var2), M(var3)) : var5;
   }

   private int v(int var1, int var2) {
      int var3 = this.M == jf.n.M ? var1 - var2 : var2 - var1;
      if (var3 > 0) {
         return 100 + var3;
      } else {
         return var3 == 0 ? 50 : var3;
      }
   }

   public boolean F(u3 var1, int var2, int var3) {
      if (this.M != jf.n.d && var2 >= 1 && var3 >= 1) {
         jf.k var4 = this.v.peekFirst();
         if (var4 == null) {
            return true;
         }

         int var5 = this.N(var1, var4, var2);
         int var6 = this.N(var1, var4, var3);
         int var7 = var5 >= 100 || var6 < 100 && (var5 >= 50 || var6 != 50) ? 0 : 1;
         u.O(
            "SWAP_DECISION side="
               + var4
               + " target="
               + this.M
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

   public boolean N(u3 var1, int[] var2) {
      if (var1 != null && var2 != null && var2.length != 0) {
         if (var1.isSwapBusy()) {
            return false;
         }

         this.e(var1);

         for (int var6 : var2) {
            if (var1.getTemplateForValue(var6) != null) {
               this.r.addLast(var6);
            }
         }

         if (this.r.isEmpty()) {
            return false;
         }

         var1.queueForce(this.r.removeFirst());
         this.F = var1.hasQueuedForce();
         if (!this.F) {
            this.r.clear();
         }

         return this.F;
      } else {
         return false;
      }
   }

   public String q() {
      if (!this.F) {
         return "";
      }

      StringBuilder var1 = new StringBuilder();
      int var2 = u3.H().getPendingValue();
      if (var2 >= 1) {
         var1.append(var2);
      }

      for (int var4 : this.r) {
         var1.append(var1.isEmpty() ? "" : " ").append(var4);
      }

      return var1.toString();
   }

   private void e(u3 var1) {
      this.F = (boolean)0;
      this.r.clear();
      if (var1 != null) {
         var1.clearQueue();
      }

      this.g = (boolean)0;
      this.H = -1;
   }

   private void V(u3 var1) {
      var1.T();
      this.e(var1);
      this.R.clear();
      this.a.clear();
      this.Z.clear();
      this.E.clear();
      this.T.clear();
      this.t = -1;
      this.G = null;
      this.m.clear();
      this.v.clear();
      this.w = 0;
      this.k = (boolean)0;
      this.J = (boolean)0;
   }

   private Map<Integer, Integer> r(Minecraft var1) {
      HashMap var2 = new HashMap();

      for (int var3 = 0; var3 < var1.player.getInventory().getContainerSize(); var3++) {
         r(var2, var1.player.getInventory().getItem(var3));
      }

      if (var1.player.containerMenu != null) {
         r(var2, var1.player.containerMenu.getCarried());
      }

      return var2;
   }

   private static void r(Map<Integer, Integer> var0, ItemStack var1) {
      if (var1 != null && !var1.isEmpty() && var1.is(Items.FILLED_MAP)) {
         MapId var2 = (MapId)var1.getComponents().get(DataComponents.MAP_ID);
         if (var2 != null) {
            var0.merge(var2.id(), var1.getCount(), Integer::sum);
         }
      }
   }

   private Map<String, Integer> T(Minecraft var1) {
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

   private int q(Minecraft var1, u3 var2, int var3, Map<String, Integer> var4) {
      for (int var5 = 0; var5 < var1.player.getInventory().getContainerSize(); var5++) {
         ItemStack var6 = var1.player.getInventory().getItem(var5);
         if (var6 != null && !var6.isEmpty() && var6.is(Items.FILLED_MAP)) {
            MapId var7 = (MapId)var6.getComponents().get(DataComponents.MAP_ID);
            String var8 = "inventory[" + var5 + "]";
            String var9 = var8 + "#" + var3;
            if (var7 != null && var7.id() == var3 && var4.getOrDefault(var9, 0) > this.m.getOrDefault(var9, 0)) {
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

   private boolean K(Minecraft var1) {
      if (var1.player.containerMenu instanceof DispenserMenu var2) {
         HashMap var7 = new HashMap();

         for (Slot var5 : var2.slots) {
            if (var5 != null && !(var5.container instanceof Inventory)) {
               r(var7, var5.getItem());
            }
         }

         if (var2.containerId != this.t) {
            this.t = var2.containerId;
            this.T = var7;
            return false;
         }

         for (Entry var10 : var7.entrySet()) {
            if (this.Z.containsKey(var10.getKey())) {
               int var6 = Math.max(0, (Integer)var10.getValue() - this.T.getOrDefault(var10.getKey(), 0));
               if (var6 > 0) {
                  this.E.merge((Integer)var10.getKey(), var6, Integer::sum);
               }
            }
         }

         this.T = var7;

         for (Entry var11 : this.Z.entrySet()) {
            if (this.E.getOrDefault(var11.getKey(), 0) < (Integer)var11.getValue()) {
               return false;
            }
         }

         return true;
      } else {
         this.t = -1;
         this.T.clear();
         return false;
      }
   }

   private void w(u3 var1, Map<Integer, Integer> var2) {
      this.e(var1);
      this.R.clear();
      this.a.clear();
      this.Z.clear();
      this.E.clear();
      this.T.clear();
      this.t = -1;
      this.v.clear();
      this.w = 0;
      this.k = (boolean)0;
      this.J = (boolean)0;
      this.G = new HashMap<>(var2);
      this.m = this.T(Minecraft.getInstance());
   }

   private static int M(List<Integer> var0) {
      int var1 = 0;

      for (int var3 : var0) {
         var1 += var3;
      }

      return Math.floorMod(var1, 10);
   }

   public int f() {
      return M(this.R);
   }

   public int Z() {
      return M(this.a);
   }

   public int k() {
      return this.H;
   }

   public int H() {
      return this.w;
   }

   public String a() {
      return this.M.name().toLowerCase(Locale.ROOT);
   }

   public enum k {
      Z,
      x;
   }

   private enum n {
      d,
      M,
      U;
   }
}
