package com.larpclient;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen.ItemPickerMenu;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen.SlotWrapper;
import net.minecraft.resources.Identifier;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerInput;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public final class u0 {
   private static mg F = new mg();
   private static Object B;
   private static int[] w;
   private static int M;
   private static int b = -1;
   private static boolean C;
   private static AbstractContainerMenu h;
   private static AbstractContainerMenu Y;
   private static final Map<Slot, u0.m> L = new IdentityHashMap<>();
   private static final Map<Slot, SlotWrapper> Q = new IdentityHashMap<>();
   private static final SimpleContainer x = new SimpleContainer(1);
   private static boolean q = (boolean)1;
   private static Object a;
   private static long K;
   private static int S = -1;

   public static boolean f() {
      return wt.v();
   }

   public static void V() {
      q = (boolean)1;
   }

   public static long F() {
      return K;
   }

   public static void u() {
      a = null;
      B = null;
      w = null;
      Y = null;
      h = null;
      L.clear();
      Q.clear();
      S = -1;
      b = -1;
      C = (boolean)0;
      F = new mg();
      q = (boolean)1;
   }

   public static boolean w() {
      Minecraft var0 = Minecraft.getInstance();
      return var0.player != null && dr.z().H();
   }

   public static void I() {
      q = (boolean)1;
      E();
   }

   private static void E() {
      Minecraft var0 = Minecraft.getInstance();
      if (!w()) {
         B = null;
         w = null;
      } else {
         if (var0.player.containerMenu.getCarried().isEmpty()) {
            S = -1;
         }

         if (B != var0.player) {
            B = var0.player;
            F = new mg();
            w = null;
            b = var0.player.getInventory().getSelectedSlot();
            M = b;
            C = (boolean)0;
            q = (boolean)1;
            mt.U("FakeSpawner", "Visual inventory initialized; ordinary slot positions retained");
         }

         if (a != var0.player.containerMenu) {
            a = var0.player.containerMenu;
            q = (boolean)1;
         }

         if (q || w == null) {
            if (w == null || !f()) {
               long var1 = 0L;
               long var3 = 0L;

               for (int var5 = 0; var5 < 36; var5++) {
                  if (!var0.player.getInventory().getItem(var5).isEmpty() || z_.w(var5) || dr.z().M(var5)) {
                     var1 |= 1L << var5;
                  }

                  if (dr.z().t(var5) && dr.z().p(var5, var0.player.getInventory().getItem(var5)) == 0) {
                     var3 |= 1L << var5;
                  }
               }

               q = (boolean)0;
               int[] var6 = F.W(var1, var3);
               if (var6 != w) {
                  w = var6;
                  K++;
                  mt.U("FakeSpawner", "Mapping snapshot {} occupancy={} reserved={}", K, Long.toHexString(var1), Long.toHexString(var3));
               }
            }
         }
      }
   }

   public static int f(int var0) {
      E();
      return w != null && var0 >= 0 && var0 < 36 ? w[var0] : var0;
   }

   public static ItemStack h(int var0) {
      int var1 = f(var0);
      return var1 < 0 ? ItemStack.EMPTY : N(var1, Minecraft.getInstance().player.getInventory().getItem(var1));
   }

   public static ItemStack N(int var0, ItemStack var1) {
      return dr.z().t(var0) ? dr.z().U(var0, var1) : dr.z().j(var0, z_.X(var0, var1));
   }

   public static int Q(int var0) {
      E();
      if (!w()) {
         return var0;
      }

      if (var0 != b) {
         M = var0;
         C = (boolean)0;
      }

      b = var0;
      if (C && w != null && M >= 0 && M < 9 && w[M] < 0) {
         return M;
      }

      C = (boolean)0;
      if (w != null) {
         for (int var1 = 0; var1 < 9; var1++) {
            if (w[var1] == var0) {
               M = var1;
               break;
            }
         }
      }

      return M;
   }

   public static int D() {
      E();

      for (int var0 = 0; var0 < 36; var0++) {
         int var1 = w == null ? var0 : w[var0];
         if (var1 < 0 || Minecraft.getInstance().player.getInventory().getItem(var1).isEmpty() && !dr.z().M(var1) && !z_.w(var1)) {
            return var0;
         }
      }

      return -1;
   }

   public static void l(int var0, ItemStack var1, int var2) {
      if (var2 >= 0) {
         F.F(var2, var0);
      }

      I();
   }

   public static void B(int var0, ItemStack var1) {
      l(var0, var1, D());
   }

   public static Slot W(AbstractContainerMenu var0, Slot var1) {
      Minecraft var2 = Minecraft.getInstance();
      if (var0 instanceof ItemPickerMenu) {
         if (w() && var1 instanceof SlotWrapper var7 && !(var7.target instanceof u0.m)) {
            V(var0);
            return A(var1, W(var2.player.inventoryMenu, var7.target));
         } else {
            return var1;
         }
      } else if (w() && !(var1 instanceof u0.m) && var1.container == var2.player.getInventory() && var1.getContainerSlot() >= 0 && var1.getContainerSlot() < 36
         )
       {
         int var3 = f(var1.getContainerSlot());
         Slot var4 = null;
         if (var3 >= 0) {
            for (Slot var6 : var0.slots) {
               if (var6.container == var1.container && var6.getContainerSlot() == var3) {
                  var4 = var6;
                  break;
               }
            }
         }

         if (h != var0) {
            L.clear();
            Q.clear();
            h = var0;
         }

         u0.m var8 = L.get(var1);
         if (var8 == null || var8.p != var4) {
            var8 = new u0.m(var1, var4);
            L.put(var1, var8);
         }

         return var8;
      } else {
         return var1;
      }
   }

   private static Slot A(Slot var0, Slot var1) {
      SlotWrapper var2 = Q.get(var0);
      if (var2 == null || var2.target != var1) {
         var2 = new SlotWrapper(var1, var0.getContainerSlot(), var0.x, var0.y);
         var2.index = var0.index;
         Q.put(var0, var2);
      }

      return var2;
   }

   private static void V(AbstractContainerMenu var0) {
      if (Y != var0) {
         Q.clear();
         Y = var0;
      }
   }

   public static Slot n(Slot var0) {
      return var0 instanceof SlotWrapper var1 ? var1.target : var0;
   }

   public static Slot g(AbstractContainerMenu var0, Slot var1, Set<Slot> var2) {
      if (w() && var0 instanceof ItemPickerMenu && var1 instanceof SlotWrapper var16) {
         V(var0);
         InventoryMenu var17 = Minecraft.getInstance().player.inventoryMenu;
         HashSet var18 = new HashSet();

         for (Slot var21 : var2) {
            var18.add(n(var21));
         }

         Slot var20 = g(var17, var16.target, var18);
         if (!var2.isEmpty()) {
            var2.clear();

            for (Slot var23 : var18) {
               for (Slot var25 : var0.slots) {
                  Slot var26 = W(var0, var25);
                  if (n(var26) == var23) {
                     var2.add(var26);
                     break;
                  }
               }
            }
         }

         return A(var1, var20);
      } else {
         Slot var3 = W(var0, var1);
         if (var3 instanceof u0.m var4 && var4.p == null && !f() && var0.containerId >= 0 && !var0.getCarried().isEmpty() && !h2.I(var0.getCarried())) {
            Inventory var5 = Minecraft.getInstance().player.getInventory();

            for (int var6 = 0; var6 < 2; var6++) {
               for (Slot var8 : var0.slots) {
                  int var9 = var8.getContainerSlot();
                  if (var8.container == var5
                     && var9 >= 0
                     && var9 < 36
                     && !var8.hasItem()
                     && (var6 != 0 || var9 != S)
                     && (var6 != 1 || var9 == S)
                     && !k(var9, var8.getItem())
                     && var8.mayPlace(var0.getCarried())
                     && var8.isActive()
                     && var8.getMaxStackSize(var0.getCarried()) >= 1
                     && !var2.stream().anyMatch(var2x -> var2x.container == var5 && var2x.getContainerSlot() == var9)) {
                     F.F(var4.S, var9);
                     V();
                     E();
                     if (!var2.isEmpty()) {
                        ArrayList var10 = new ArrayList(var2.size());

                        for (Slot var12 : var2) {
                           Slot var13 = var12;
                           if (var12.container == var5) {
                              for (Slot var15 : var0.slots) {
                                 if (var15.container == var5
                                    && var15.getContainerSlot() >= 0
                                    && var15.getContainerSlot() < 36
                                    && f(var15.getContainerSlot()) == var12.getContainerSlot()) {
                                    var13 = W(var0, var15);
                                    break;
                                 }
                              }
                           }

                           var10.add(var13);
                        }

                        var2.clear();
                        var2.addAll(var10);
                     }

                     mt.U("FakeSpawner", "Visual gap bound logical={} physical={} menu={}", var4.S, var9, var0.containerId);
                     return W(var0, var1);
                  }
               }
            }

            return var3;
         } else {
            return var3;
         }
      }
   }

   public static Slot u(AbstractContainerMenu var0, Slot var1) {
      if (var1 instanceof u0.m var2 && var2.p != null) {
         for (Slot var4 : var0.slots) {
            if (var4.container == var1.container && var4.getContainerSlot() == var1.getContainerSlot()) {
               return new u0.m(new Slot(var1.container, var2.S, var1.x, var1.y), var4);
            }
         }

         return var1;
      } else {
         return var1;
      }
   }

   public static void I(Slot var0) {
      if (!f()) {
         if (var0 instanceof u0.m var1 && var1.p != null) {
            if (Minecraft.getInstance().player.containerMenu.getCarried().isEmpty() && var1.hasItem()) {
               S = var1.p.getContainerSlot();
            }

            F.F(var1.S, var1.p.getContainerSlot());
            V();
         }
      }
   }

   private static boolean k(int var0, ItemStack var1) {
      return var0 < 0 || dr.z().t(var0) || z_.w(var0) || dr.z().M(var0);
   }

   public static boolean w(Slot var0) {
      return var0 instanceof u0.m var1 && (var1.p == null || k(var1.p.getContainerSlot(), var1.p.getItem()));
   }

   public static void w(int var0, int var1) {
      F.F(var0, var1);
      I();
   }

   public static void i(AbstractContainerMenu var0, Slot var1, int var2) {
      if (!f()) {
         Minecraft var3 = Minecraft.getInstance();
         if (var1 != null && !w(var1) && var3.gameMode != null && var0.getCarried().isEmpty()) {
            int var4 = f(var2);
            Slot var5 = null;

            for (Slot var7 : var0.slots) {
               if (var7.container == var3.player.getInventory() && var7.getContainerSlot() == var4) {
                  var5 = var7;
                  break;
               }
            }

            if (var5 != null && var5.index != var1.index && !k(var4, var5.getItem())) {
               if (var1.mayPickup(var3.player)
                  && var5.mayPickup(var3.player)
                  && (var5.getItem().isEmpty() || var1.mayPlace(var5.getItem()) && var5.getItem().getCount() <= var1.getMaxStackSize(var5.getItem()))
                  && (var1.getItem().isEmpty() || var5.mayPlace(var1.getItem()))) {
                  if (var4 < 9) {
                     if (!dr.z().w(var0, List.of(new wt.a(var1.index, var4, ContainerInput.SWAP)))) {
                        var3.gameMode.handleContainerInput(var0.containerId, var1.index, var4, ContainerInput.SWAP, var3.player);
                     }
                  } else {
                     int var10 = 0;

                     while (var1.container == var3.player.getInventory() && var1.getContainerSlot() == var10) {
                        var10++;
                     }

                     int var11 = var10;
                     Slot var8 = var5;
                     if (dr.z().t(var11)) {
                        return;
                     }

                     List var9 = List.of(
                        new wt.a(var1.index, var11, ContainerInput.SWAP),
                        new wt.a(var8.index, var11, ContainerInput.SWAP),
                        new wt.a(var1.index, var11, ContainerInput.SWAP)
                     );
                     if (!dr.z().w(var0, var9)) {
                        wt.b(var0, "visual number-key swap", var9, u0::V, u0::V);
                     }
                  }

                  F.F(var2, var4);
                  if (var1 instanceof u0.m) {
                     I(var1);
                  } else if (var1.container == var3.player.getInventory()) {
                     F.F(var1.getContainerSlot(), var1.getContainerSlot());
                  }

                  I();
               }
            }
         }
      }
   }

   public static void K(int var0) {
      if (!f()) {
         Minecraft var1 = Minecraft.getInstance();
         if (!w()) {
            var1.player.getInventory().setSelectedSlot(var0);
         } else {
            int var2 = f(var0);
            if (var2 >= 0) {
               C = (boolean)0;
               if (var2 < 9) {
                  var1.player.getInventory().setSelectedSlot(var2);
                  b = var2;
                  M = var0;
               } else if (var1.gameMode != null && var1.player.containerMenu == var1.player.inventoryMenu && var1.player.inventoryMenu.getCarried().isEmpty()) {
                  for (int var6 = 0; var6 < 9; var6++) {
                     if (!dr.z().t(var6)) {
                        dr var8 = dr.z();
                        if (var1.player.getInventory().getItem(var2).is(Items.BONE)) {
                           var8.Q(var6);
                        }

                        if (var1.player.getInventory().getItem(var6).is(Items.BONE)) {
                           var8.Q(var2);
                        }

                        var1.gameMode.handleContainerInput(0, var2, var6, ContainerInput.SWAP, var1.player);
                        F.B(var2, var6);
                        V();
                        var1.player.getInventory().setSelectedSlot(var6);
                        b = var6;
                        M = var0;
                        return;
                     }
                  }

                  if (dr.z().a(var2, var0)) {
                     F.B(var2, var0);
                     F.F(var0, var0);
                     var1.player.getInventory().setSelectedSlot(var0);
                     b = var0;
                     M = var0;
                     I();
                  }
               }
            } else {
               int var3 = 0;

               int var4;
               while (true) {
                  if (var3 >= 36) {
                     var3 = var0;

                     for (int var7 = 0; var7 < 9; var7++) {
                        if (dr.z().t(var7)) {
                           var3 = var7;
                           break;
                        }
                     }

                     var1.player.getInventory().setSelectedSlot(var3);
                     b = var3;
                     M = var0;
                     C = (boolean)1;
                     return;
                  }

                  if (!k(var3, var1.player.getInventory().getItem(var3)) && var1.player.getInventory().getItem(var3).isEmpty()) {
                     var4 = var3;
                     if (var3 < 9) {
                        break;
                     }

                     var4 = var0;
                     if (dr.z().a(var3, var4)) {
                        F.B(var3, var4);
                        F.F(var0, var4);
                        V();
                        break;
                     }
                  }

                  var3++;
               }

               var1.player.getInventory().setSelectedSlot(var4);
               b = var4;
               M = var0;
               C = (boolean)1;
            }
         }
      }
   }

   public static final class m extends Slot {
      private final Slot p;
      private final int S;

      m(Slot var1, Slot var2) {
         super((Container)(var2 == null ? u0.x : var2.container), var2 == null ? 0 : var2.getContainerSlot(), var1.x, var1.y);
         this.p = var2;
         this.S = var1.getContainerSlot();
         this.index = var2 == null ? -1 : var2.index;
      }

      public ItemStack getItem() {
         return this.p == null ? ItemStack.EMPTY : u0.N(this.p.getContainerSlot(), this.p.getItem());
      }

      public boolean mayPlace(ItemStack var1) {
         return this.p != null && !u0.w(this) && this.p.mayPlace(var1);
      }

      public boolean mayPickup(Player var1) {
         return this.p != null && (!u0.w(this) || dr.z().p(this.p.getContainerSlot(), this.p.getItem()) > 0) && this.p.mayPickup(var1);
      }

      public int getMaxStackSize() {
         return this.p == null ? 0 : this.p.getMaxStackSize();
      }

      public int getMaxStackSize(ItemStack var1) {
         return this.p == null ? 0 : this.p.getMaxStackSize(var1);
      }

      public boolean isActive() {
         return this.p != null && this.p.isActive();
      }

      public Identifier getNoItemIcon() {
         return this.p == null ? null : this.p.getNoItemIcon();
      }

      public boolean isFake() {
         return this.p != null && this.p.isFake();
      }
   }
}
