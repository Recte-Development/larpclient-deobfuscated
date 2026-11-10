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

public final class h6 {
   private static final h6 Q = new h6();
   private final List<Integer> e = new ArrayList<>();
   private final List<Integer> B = new ArrayList<>();
   private final Deque<h6.e> V = new ArrayDeque<>();
   private final Map<Integer, Integer> x = new HashMap<>();
   private final Map<Integer, Integer> z = new HashMap<>();
   private Map<Integer, Integer> F = new HashMap<>();
   private int q = -1;
   private Map<Integer, Integer> Z;
   private Map<String, Integer> W = new HashMap<>();
   private h6.g H = h6.g.C;
   private int K;
   private int J = -1;
   private boolean o;
   private boolean n;
   private boolean C;
   private boolean d;
   private final ArrayDeque<Integer> w = new ArrayDeque<>();

   private h6() {
   }

   public static h6 l() {
      return Q;
   }

   public void q(Minecraft var1, vf var2, h6.e var3) {
      h2.n(
         "DISP side="
            + var3
            + " roundActive="
            + this.o
            + " roundComplete="
            + this.n
            + " dealtCards="
            + this.K
            + " pending="
            + this.V
            + " target="
            + this.H
            + " enabled="
            + _u.b().Q
      );
      if (!this.o || this.n) {
         this.D(var1);
      }

      if (var2.isSwapBusy()) {
         this.V.addLast(var3);
      } else {
         if (this.d) {
            if (!var2.hasQueuedForce() && !this.w.isEmpty()) {
               var2.queueForce(this.w.removeFirst());
            }

            if (var2.hasQueuedForce()) {
               h2.n("PRESET draw side=" + var3 + " value=" + var2.getPendingValue() + " left=" + this.w);
               this.V.addLast(var3);
               return;
            }

            this.d = (boolean)0;
            bn.j("Baccarat", "Preset used up; the automatic plan resumes");
         }

         this.D(var2);
         if (_u.b().Q && this.H != h6.g.C && this.K >= 3 && this.u(var3) >= 1) {
            this.J = this.e(var2, var3);
            if (this.J >= 1) {
               var2.queueForce(this.J);
               this.C = (boolean)1;
            }
         }

         this.V.addLast(var3);
      }
   }

   public void J(Minecraft var1, vf var2, boolean var3) {
      if (var3 && var1 != null && var1.player != null) {
         Map var4 = this.B(var1);
         Map var5 = this.j(var1);
         if (this.Z == null) {
            this.Z = new HashMap<>(var4);
            this.W = new HashMap<>(var5);
         } else if (var2.isSwapBusy()) {
            h2.n("HOLD swapBusy=" + var2.isSwapActive() + " defer=" + var2.Y());
         } else {
            if (this.o && !this.V.isEmpty()) {
               for (Entry var7 : var4.entrySet()) {
                  int var8 = Math.max(0, (Integer)var7.getValue() - this.Z.getOrDefault(var7.getKey(), 0));

                  while (var8-- > 0 && !this.V.isEmpty()) {
                     int var9 = this.M(var1, var2, (Integer)var7.getKey(), var5);
                     int var10 = this.C && this.J >= 1 && var9 < 1 ? this.J : var9;
                     h2.n(
                        "TICK mapId="
                           + var7.getKey()
                           + " physical="
                           + var9
                           + " chosen="
                           + var10
                           + " pending="
                           + this.V
                           + " prev="
                           + this.Z.getOrDefault(var7.getKey(), 0)
                           + " cur="
                           + var7.getValue()
                     );
                     if (var10 >= 1 && var10 <= 9) {
                        this.U(this.V.removeFirst(), var10, (Integer)var7.getKey());
                     } else {
                        h2.n("DROPPED mapId=" + var7.getKey() + " value=" + var10 + " (consumed added but pending side not removed)");
                     }
                  }
               }
            }

            this.Z = new HashMap<>(var4);
            this.W = new HashMap<>(var5);
            if (this.K > 0 && this.M(var1)) {
               this.p(var2, var4);
            }
         }
      } else {
         this.b(var2);
      }
   }

   public void e(vf var1) {
      this.D(var1);

      this.H = switch (this.H) {
         case C -> h6.g.R;
         case R -> h6.g.r;
         case r -> h6.g.C;
      };
   }

   public void Y(String var1, vf var2) {
      if (var1 != null) {
         h6.g var3 = switch (var1.trim().toLowerCase(Locale.ROOT)) {
            case "player" -> h6.g.R;
            case "banker" -> h6.g.r;
            case "off" -> h6.g.C;
            default -> null;
         };
         if (var3 != null && var3 != this.H) {
            this.D(var2);
            this.H = var3;
         }
      }
   }

   private void D(Minecraft var1) {
      vf.x().n();
      this.e.clear();
      this.B.clear();
      this.x.clear();
      this.z.clear();
      this.F.clear();
      this.q = -1;
      this.K = 0;
      this.J = -1;
      this.V.clear();
      this.o = (boolean)1;
      this.n = (boolean)0;
      this.C = (boolean)0;
      if (var1 != null && var1.player != null) {
         this.Z = this.B(var1);
         this.W = this.j(var1);
      }
   }

   private void U(h6.e var1, int var2, int var3) {
      vf.x().l(var2, var3);
      h2.n("RECORD side=" + var1 + " value=" + var2 + " mapId=" + var3 + " player=" + this.e + " banker=" + this.B);
      if (var1 == h6.e.t) {
         this.e.add(var2);
      } else {
         this.B.add(var2);
      }

      this.x.merge(var3, 1, Integer::sum);
      this.K++;
      this.n = this.O();
      this.C = (boolean)0;
      this.J = -1;
   }

   private boolean O() {
      if (this.e.size() >= 2 && this.B.size() >= 2) {
         int var1 = r(this.e);
         int var2 = r(this.B);
         if (this.e.size() != 2 || this.B.size() != 2) {
            int var3 = this.e.size() < 3 && var1 <= 5 ? 0 : 1;
            int var4 = this.B.size() < 3 && var2 <= 5 ? 0 : 1;
            return var3 != 0 && var4 != 0;
         } else {
            return var1 < 8 && var2 < 8 ? var1 > 5 && var2 > 5 : true;
         }
      } else {
         return false;
      }
   }

   private int u(h6.e var1) {
      return var1 == h6.e.t ? this.e.size() : this.B.size();
   }

   private int e(vf var1, h6.e var2) {
      int var3 = Integer.MIN_VALUE;
      ArrayList var4 = new ArrayList();
      ArrayList var5 = new ArrayList();
      ArrayList var6 = new ArrayList();

      for (int var7 = 1; var7 <= 9; var7++) {
         if (var1.getTemplateForValue(var7) != null) {
            int var8 = this.Q(var1, var2, var7);
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

   private int Q(vf var1, h6.e var2, int var3) {
      ArrayList var4 = new ArrayList<>(this.e);
      ArrayList var5 = new ArrayList<>(this.B);
      if (var2 == h6.e.t) {
         var4.add(var3);
      } else {
         var5.add(var3);
      }

      int var6 = this.Y(var1, var4, var5);
      if (var4.size() == 2 && var5.size() == 2) {
         int var7 = this.H == h6.g.R ? r(var4) - r(var5) : r(var5) - r(var4);
         if ((var2 == h6.e.t && this.H == h6.g.R || var2 == h6.e.x && this.H == h6.g.r) && var7 > 0) {
            var6 += 1000 + var7;
         }
      }

      return var6;
   }

   private int Y(vf var1, List<Integer> var2, List<Integer> var3) {
      if (var2.size() >= 2 && var3.size() >= 2) {
         int var4 = r(var2);
         int var5 = r(var3);
         if (var2.size() == 2 && var3.size() == 2) {
            if (var4 >= 8 || var5 >= 8) {
               return this.N(var4, var5);
            } else if (var4 <= 5) {
               return this.j(var1, var2, var3, h6.e.t);
            } else {
               return var5 <= 5 ? this.j(var1, var2, var3, h6.e.x) : this.N(var4, var5);
            }
         } else {
            return var2.size() >= 3 && var3.size() == 2 && var5 <= 5 ? this.j(var1, var2, var3, h6.e.x) : this.N(var4, var5);
         }
      } else {
         return this.N(r(var2), r(var3));
      }
   }

   private int j(vf var1, List<Integer> var2, List<Integer> var3, h6.e var4) {
      int var5 = Integer.MIN_VALUE;

      for (int var6 = 1; var6 <= 9; var6++) {
         if (var1.getTemplateForValue(var6) != null) {
            ArrayList var7 = new ArrayList(var2);
            ArrayList var8 = new ArrayList(var3);
            if (var4 == h6.e.t) {
               var7.add(var6);
            } else {
               var8.add(var6);
            }

            var5 = Math.max(var5, this.Y(var1, var7, var8));
         }
      }

      return var5 == Integer.MIN_VALUE ? this.N(r(var2), r(var3)) : var5;
   }

   private int N(int var1, int var2) {
      int var3 = this.H == h6.g.R ? var1 - var2 : var2 - var1;
      if (var3 > 0) {
         return 100 + var3;
      } else {
         return var3 == 0 ? 50 : var3;
      }
   }

   public boolean z(vf var1, int var2, int var3) {
      if (this.H != h6.g.C && var2 >= 1 && var3 >= 1) {
         h6.e var4 = this.V.peekFirst();
         if (var4 == null) {
            return true;
         }

         int var5 = this.Q(var1, var4, var2);
         int var6 = this.Q(var1, var4, var3);
         int var7 = var5 >= 100 || var6 < 100 && (var5 >= 50 || var6 != 50) ? 0 : 1;
         h2.n(
            "SWAP_DECISION side="
               + var4
               + " target="
               + this.H
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

   public boolean t(vf var1, int[] var2) {
      if (var1 != null && var2 != null && var2.length != 0) {
         if (var1.isSwapBusy()) {
            return false;
         }

         this.D(var1);

         for (int var6 : var2) {
            if (var1.getTemplateForValue(var6) != null) {
               this.w.addLast(var6);
            }
         }

         if (this.w.isEmpty()) {
            return false;
         }

         var1.queueForce(this.w.removeFirst());
         this.d = var1.hasQueuedForce();
         if (!this.d) {
            this.w.clear();
         }

         return this.d;
      } else {
         return false;
      }
   }

   public String i() {
      if (!this.d) {
         return "";
      }

      StringBuilder var1 = new StringBuilder();
      int var2 = vf.x().getPendingValue();
      if (var2 >= 1) {
         var1.append(var2);
      }

      for (int var4 : this.w) {
         var1.append(var1.isEmpty() ? "" : " ").append(var4);
      }

      return var1.toString();
   }

   private void D(vf var1) {
      this.d = (boolean)0;
      this.w.clear();
      if (var1 != null) {
         var1.clearQueue();
      }

      this.C = (boolean)0;
      this.J = -1;
   }

   private void b(vf var1) {
      var1.n();
      this.D(var1);
      this.e.clear();
      this.B.clear();
      this.x.clear();
      this.z.clear();
      this.F.clear();
      this.q = -1;
      this.Z = null;
      this.W.clear();
      this.V.clear();
      this.K = 0;
      this.o = (boolean)0;
      this.n = (boolean)0;
   }

   private Map<Integer, Integer> B(Minecraft var1) {
      HashMap var2 = new HashMap();

      for (int var3 = 0; var3 < var1.player.getInventory().getContainerSize(); var3++) {
         w(var2, var1.player.getInventory().getItem(var3));
      }

      if (var1.player.containerMenu != null) {
         w(var2, var1.player.containerMenu.getCarried());
      }

      return var2;
   }

   private static void w(Map<Integer, Integer> var0, ItemStack var1) {
      if (var1 != null && !var1.isEmpty() && var1.is(Items.FILLED_MAP)) {
         MapId var2 = (MapId)var1.getComponents().get(DataComponents.MAP_ID);
         if (var2 != null) {
            var0.merge(var2.id(), var1.getCount(), Integer::sum);
         }
      }
   }

   private Map<String, Integer> j(Minecraft var1) {
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

   private int M(Minecraft var1, vf var2, int var3, Map<String, Integer> var4) {
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

   private boolean M(Minecraft var1) {
      if (var1.player.containerMenu instanceof DispenserMenu var2) {
         HashMap var7 = new HashMap();

         for (Slot var5 : var2.slots) {
            if (var5 != null && !(var5.container instanceof Inventory)) {
               w(var7, var5.getItem());
            }
         }

         if (var2.containerId != this.q) {
            this.q = var2.containerId;
            this.F = var7;
            return false;
         }

         for (Entry var10 : var7.entrySet()) {
            if (this.x.containsKey(var10.getKey())) {
               int var6 = Math.max(0, (Integer)var10.getValue() - this.F.getOrDefault(var10.getKey(), 0));
               if (var6 > 0) {
                  this.z.merge((Integer)var10.getKey(), var6, Integer::sum);
               }
            }
         }

         this.F = var7;

         for (Entry var11 : this.x.entrySet()) {
            if (this.z.getOrDefault(var11.getKey(), 0) < (Integer)var11.getValue()) {
               return false;
            }
         }

         return true;
      } else {
         this.q = -1;
         this.F.clear();
         return false;
      }
   }

   private void p(vf var1, Map<Integer, Integer> var2) {
      this.D(var1);
      this.e.clear();
      this.B.clear();
      this.x.clear();
      this.z.clear();
      this.F.clear();
      this.q = -1;
      this.V.clear();
      this.K = 0;
      this.o = (boolean)0;
      this.n = (boolean)0;
      this.Z = new HashMap<>(var2);
      this.W = this.j(Minecraft.getInstance());
   }

   private static int r(List<Integer> var0) {
      int var1 = 0;

      for (int var3 : var0) {
         var1 += var3;
      }

      return Math.floorMod(var1, 10);
   }

   public int Z() {
      return r(this.e);
   }

   public int k() {
      return r(this.B);
   }

   public int J() {
      return this.J;
   }

   public int x() {
      return this.K;
   }

   public String V() {
      return this.H.name().toLowerCase(Locale.ROOT);
   }

   public enum e {
      t,
      x;
   }

   private enum g {
      C,
      R,
      r;
   }
}
