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

public final class mx {
   private static xv i = new xv();
   private static Object U;
   private static int[] a;
   private static int u;
   private static int n = -1;
   private static boolean O;
   private static AbstractContainerMenu R;
   private static AbstractContainerMenu j;
   private static final Map<Slot, mx.q> T = new IdentityHashMap<>();
   private static final Map<Slot, SlotWrapper> o = new IdentityHashMap<>();
   private static final SimpleContainer J = new SimpleContainer(1);
   private static boolean F = (boolean)1;
   private static Object B;
   private static long h;
   private static int A = -1;

   public static boolean m() {
      return rm.L();
   }

   public static void u() {
      F = (boolean)1;
   }

   public static long U() {
      return h;
   }

   public static void B() {
      B = null;
      U = null;
      a = null;
      j = null;
      R = null;
      T.clear();
      o.clear();
      A = -1;
      n = -1;
      O = (boolean)0;
      i = new xv();
      F = (boolean)1;
   }

   public static boolean U() {
      Minecraft var0 = Minecraft.getInstance();
      return var0.player != null && lj.j().B();
   }

   public static void C() {
      F = (boolean)1;
      d();
   }

   private static void d() {
      Minecraft var0 = Minecraft.getInstance();
      if (!U()) {
         U = null;
         a = null;
      } else {
         if (var0.player.containerMenu.getCarried().isEmpty()) {
            A = -1;
         }

         if (U != var0.player) {
            U = var0.player;
            i = new xv();
            a = null;
            n = var0.player.getInventory().getSelectedSlot();
            u = n;
            O = (boolean)0;
            F = (boolean)1;
            r4.g("FakeSpawner", "Visual inventory initialized; ordinary slot positions retained");
         }

         if (B != var0.player.containerMenu) {
            B = var0.player.containerMenu;
            F = (boolean)1;
         }

         if (F || a == null) {
            if (a == null || !m()) {
               long var1 = 0L;
               long var3 = 0L;

               for (int var5 = 0; var5 < 36; var5++) {
                  if (!var0.player.getInventory().getItem(var5).isEmpty() || fv.l(var5) || lj.j().N(var5)) {
                     var1 |= 1L << var5;
                  }

                  if (lj.j().U(var5) && lj.j().v(var5, var0.player.getInventory().getItem(var5)) == 0) {
                     var3 |= 1L << var5;
                  }
               }

               F = (boolean)0;
               int[] var6 = i.S(var1, var3);
               if (var6 != a) {
                  a = var6;
                  h++;
                  r4.g("FakeSpawner", "Mapping snapshot {} occupancy={} reserved={}", h, Long.toHexString(var1), Long.toHexString(var3));
               }
            }
         }
      }
   }

   public static int V(int var0) {
      d();
      return a != null && var0 >= 0 && var0 < 36 ? a[var0] : var0;
   }

   public static ItemStack R(int var0) {
      int var1 = V(var0);
      return var1 < 0 ? ItemStack.EMPTY : n(var1, Minecraft.getInstance().player.getInventory().getItem(var1));
   }

   public static ItemStack n(int var0, ItemStack var1) {
      return lj.j().U(var0) ? lj.j().V(var0, var1) : lj.j().Q(var0, fv.W(var0, var1));
   }

   public static int z(int var0) {
      d();
      if (!U()) {
         return var0;
      }

      if (var0 != n) {
         u = var0;
         O = (boolean)0;
      }

      n = var0;
      if (O && a != null && u >= 0 && u < 9 && a[u] < 0) {
         return u;
      }

      O = (boolean)0;
      if (a != null) {
         for (int var1 = 0; var1 < 9; var1++) {
            if (a[var1] == var0) {
               u = var1;
               break;
            }
         }
      }

      return u;
   }

   public static int i() {
      d();

      for (int var0 = 0; var0 < 36; var0++) {
         int var1 = a == null ? var0 : a[var0];
         if (var1 < 0 || Minecraft.getInstance().player.getInventory().getItem(var1).isEmpty() && !lj.j().N(var1) && !fv.l(var1)) {
            return var0;
         }
      }

      return -1;
   }

   public static void B(int var0, ItemStack var1, int var2) {
      if (var2 >= 0) {
         i.F(var2, var0);
      }

      C();
   }

   public static void S(int var0, ItemStack var1) {
      B(var0, var1, i());
   }

   public static Slot v(AbstractContainerMenu var0, Slot var1) {
      Minecraft var2 = Minecraft.getInstance();
      if (var0 instanceof ItemPickerMenu) {
         if (U() && var1 instanceof SlotWrapper var7 && !(var7.target instanceof mx.q)) {
            i(var0);
            return y(var1, v(var2.player.inventoryMenu, var7.target));
         } else {
            return var1;
         }
      } else if (U() && !(var1 instanceof mx.q) && var1.container == var2.player.getInventory() && var1.getContainerSlot() >= 0 && var1.getContainerSlot() < 36
         )
       {
         int var3 = V(var1.getContainerSlot());
         Slot var4 = null;
         if (var3 >= 0) {
            for (Slot var6 : var0.slots) {
               if (var6.container == var1.container && var6.getContainerSlot() == var3) {
                  var4 = var6;
                  break;
               }
            }
         }

         if (R != var0) {
            T.clear();
            o.clear();
            R = var0;
         }

         mx.q var8 = T.get(var1);
         if (var8 == null || var8.T != var4) {
            var8 = new mx.q(var1, var4);
            T.put(var1, var8);
         }

         return var8;
      } else {
         return var1;
      }
   }

   private static Slot y(Slot var0, Slot var1) {
      SlotWrapper var2 = o.get(var0);
      if (var2 == null || var2.target != var1) {
         var2 = new SlotWrapper(var1, var0.getContainerSlot(), var0.x, var0.y);
         var2.index = var0.index;
         o.put(var0, var2);
      }

      return var2;
   }

   private static void i(AbstractContainerMenu var0) {
      if (j != var0) {
         o.clear();
         j = var0;
      }
   }

   public static Slot C(Slot var0) {
      return var0 instanceof SlotWrapper var1 ? var1.target : var0;
   }

   public static Slot B(AbstractContainerMenu var0, Slot var1, Set<Slot> var2) {
      if (U() && var0 instanceof ItemPickerMenu && var1 instanceof SlotWrapper var16) {
         i(var0);
         InventoryMenu var17 = Minecraft.getInstance().player.inventoryMenu;
         HashSet var18 = new HashSet();

         for (Slot var21 : var2) {
            var18.add(C(var21));
         }

         Slot var20 = B(var17, var16.target, var18);
         if (!var2.isEmpty()) {
            var2.clear();

            for (Slot var23 : var18) {
               for (Slot var25 : var0.slots) {
                  Slot var26 = v(var0, var25);
                  if (C(var26) == var23) {
                     var2.add(var26);
                     break;
                  }
               }
            }
         }

         return y(var1, var20);
      } else {
         Slot var3 = v(var0, var1);
         if (var3 instanceof mx.q var4 && var4.T == null && !m() && var0.containerId >= 0 && !var0.getCarried().isEmpty() && !ja.Q(var0.getCarried())) {
            Inventory var5 = Minecraft.getInstance().player.getInventory();

            for (int var6 = 0; var6 < 2; var6++) {
               for (Slot var8 : var0.slots) {
                  int var9 = var8.getContainerSlot();
                  if (var8.container == var5
                     && var9 >= 0
                     && var9 < 36
                     && !var8.hasItem()
                     && (var6 != 0 || var9 != A)
                     && (var6 != 1 || var9 == A)
                     && !R(var9, var8.getItem())
                     && var8.mayPlace(var0.getCarried())
                     && var8.isActive()
                     && var8.getMaxStackSize(var0.getCarried()) >= 1
                     && !var2.stream().anyMatch(var2x -> var2x.container == var5 && var2x.getContainerSlot() == var9)) {
                     i.F(var4.O, var9);
                     u();
                     d();
                     if (!var2.isEmpty()) {
                        ArrayList var10 = new ArrayList(var2.size());

                        for (Slot var12 : var2) {
                           Slot var13 = var12;
                           if (var12.container == var5) {
                              for (Slot var15 : var0.slots) {
                                 if (var15.container == var5
                                    && var15.getContainerSlot() >= 0
                                    && var15.getContainerSlot() < 36
                                    && V(var15.getContainerSlot()) == var12.getContainerSlot()) {
                                    var13 = v(var0, var15);
                                    break;
                                 }
                              }
                           }

                           var10.add(var13);
                        }

                        var2.clear();
                        var2.addAll(var10);
                     }

                     r4.g("FakeSpawner", "Visual gap bound logical={} physical={} menu={}", var4.O, var9, var0.containerId);
                     return v(var0, var1);
                  }
               }
            }

            return var3;
         } else {
            return var3;
         }
      }
   }

   public static Slot P(AbstractContainerMenu var0, Slot var1) {
      if (var1 instanceof mx.q var2 && var2.T != null) {
         for (Slot var4 : var0.slots) {
            if (var4.container == var1.container && var4.getContainerSlot() == var1.getContainerSlot()) {
               return new mx.q(new Slot(var1.container, var2.O, var1.x, var1.y), var4);
            }
         }

         return var1;
      } else {
         return var1;
      }
   }

   public static void E(Slot var0) {
      if (!m()) {
         if (var0 instanceof mx.q var1 && var1.T != null) {
            if (Minecraft.getInstance().player.containerMenu.getCarried().isEmpty() && var1.hasItem()) {
               A = var1.T.getContainerSlot();
            }

            i.F(var1.O, var1.T.getContainerSlot());
            u();
         }
      }
   }

   private static boolean R(int var0, ItemStack var1) {
      return var0 < 0 || lj.j().U(var0) || fv.l(var0) || lj.j().N(var0);
   }

   public static boolean r(Slot var0) {
      return var0 instanceof mx.q var1 && (var1.T == null || R(var1.T.getContainerSlot(), var1.T.getItem()));
   }

   public static void z(int var0, int var1) {
      i.F(var0, var1);
      C();
   }

   public static void b(AbstractContainerMenu var0, Slot var1, int var2) {
      if (!m()) {
         Minecraft var3 = Minecraft.getInstance();
         if (var1 != null && !r(var1) && var3.gameMode != null && var0.getCarried().isEmpty()) {
            int var4 = V(var2);
            Slot var5 = null;

            for (Slot var7 : var0.slots) {
               if (var7.container == var3.player.getInventory() && var7.getContainerSlot() == var4) {
                  var5 = var7;
                  break;
               }
            }

            if (var5 != null && var5.index != var1.index && !R(var4, var5.getItem())) {
               if (var1.mayPickup(var3.player)
                  && var5.mayPickup(var3.player)
                  && (var5.getItem().isEmpty() || var1.mayPlace(var5.getItem()) && var5.getItem().getCount() <= var1.getMaxStackSize(var5.getItem()))
                  && (var1.getItem().isEmpty() || var5.mayPlace(var1.getItem()))) {
                  if (var4 < 9) {
                     if (!lj.j().n(var0, List.of(new rm.k(var1.index, var4, ContainerInput.SWAP)))) {
                        var3.gameMode.handleContainerInput(var0.containerId, var1.index, var4, ContainerInput.SWAP, var3.player);
                     }
                  } else {
                     int var10 = 0;

                     while (var1.container == var3.player.getInventory() && var1.getContainerSlot() == var10) {
                        var10++;
                     }

                     int var11 = var10;
                     Slot var8 = var5;
                     if (lj.j().U(var11)) {
                        return;
                     }

                     List var9 = List.of(
                        new rm.k(var1.index, var11, ContainerInput.SWAP),
                        new rm.k(var8.index, var11, ContainerInput.SWAP),
                        new rm.k(var1.index, var11, ContainerInput.SWAP)
                     );
                     if (!lj.j().n(var0, var9)) {
                        rm.W(var0, "visual number-key swap", var9, mx::u, mx::u);
                     }
                  }

                  i.F(var2, var4);
                  if (var1 instanceof mx.q) {
                     E(var1);
                  } else if (var1.container == var3.player.getInventory()) {
                     i.F(var1.getContainerSlot(), var1.getContainerSlot());
                  }

                  C();
               }
            }
         }
      }
   }

   public static void P(int var0) {
      if (!m()) {
         Minecraft var1 = Minecraft.getInstance();
         if (!U()) {
            var1.player.getInventory().setSelectedSlot(var0);
         } else {
            int var2 = V(var0);
            if (var2 >= 0) {
               O = (boolean)0;
               if (var2 < 9) {
                  var1.player.getInventory().setSelectedSlot(var2);
                  n = var2;
                  u = var0;
               } else if (var1.gameMode != null && var1.player.containerMenu == var1.player.inventoryMenu && var1.player.inventoryMenu.getCarried().isEmpty()) {
                  for (int var6 = 0; var6 < 9; var6++) {
                     if (!lj.j().U(var6)) {
                        lj var8 = lj.j();
                        if (var1.player.getInventory().getItem(var2).is(Items.BONE)) {
                           var8.x(var6);
                        }

                        if (var1.player.getInventory().getItem(var6).is(Items.BONE)) {
                           var8.x(var2);
                        }

                        var1.gameMode.handleContainerInput(0, var2, var6, ContainerInput.SWAP, var1.player);
                        i.B(var2, var6);
                        u();
                        var1.player.getInventory().setSelectedSlot(var6);
                        n = var6;
                        u = var0;
                        return;
                     }
                  }

                  if (lj.j().S(var2, var0)) {
                     i.B(var2, var0);
                     i.F(var0, var0);
                     var1.player.getInventory().setSelectedSlot(var0);
                     n = var0;
                     u = var0;
                     C();
                  }
               }
            } else {
               int var3 = 0;

               int var4;
               while (true) {
                  if (var3 >= 36) {
                     var3 = var0;

                     for (int var7 = 0; var7 < 9; var7++) {
                        if (lj.j().U(var7)) {
                           var3 = var7;
                           break;
                        }
                     }

                     var1.player.getInventory().setSelectedSlot(var3);
                     n = var3;
                     u = var0;
                     O = (boolean)1;
                     return;
                  }

                  if (!R(var3, var1.player.getInventory().getItem(var3)) && var1.player.getInventory().getItem(var3).isEmpty()) {
                     var4 = var3;
                     if (var3 < 9) {
                        break;
                     }

                     var4 = var0;
                     if (lj.j().S(var3, var4)) {
                        i.B(var3, var4);
                        i.F(var0, var4);
                        u();
                        break;
                     }
                  }

                  var3++;
               }

               var1.player.getInventory().setSelectedSlot(var4);
               n = var4;
               u = var0;
               O = (boolean)1;
            }
         }
      }
   }

   public static final class q extends Slot {
      private final Slot T;
      private final int O;

      q(Slot var1, Slot var2) {
         super((Container)(var2 == null ? mx.J : var2.container), var2 == null ? 0 : var2.getContainerSlot(), var1.x, var1.y);
         this.T = var2;
         this.O = var1.getContainerSlot();
         this.index = var2 == null ? -1 : var2.index;
      }

      public ItemStack getItem() {
         return this.T == null ? ItemStack.EMPTY : mx.n(this.T.getContainerSlot(), this.T.getItem());
      }

      public boolean mayPlace(ItemStack var1) {
         return this.T != null && !mx.r(this) && this.T.mayPlace(var1);
      }

      public boolean mayPickup(Player var1) {
         return this.T != null && (!mx.r(this) || lj.j().v(this.T.getContainerSlot(), this.T.getItem()) > 0) && this.T.mayPickup(var1);
      }

      public int getMaxStackSize() {
         return this.T == null ? 0 : this.T.getMaxStackSize();
      }

      public int getMaxStackSize(ItemStack var1) {
         return this.T == null ? 0 : this.T.getMaxStackSize(var1);
      }

      public boolean isActive() {
         return this.T != null && this.T.isActive();
      }

      public Identifier getNoItemIcon() {
         return this.T == null ? null : this.T.getNoItemIcon();
      }

      public boolean isFake() {
         return this.T != null && this.T.isFake();
      }
   }
}
