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

public final class ki {
   private static lz h = new lz();
   private static Object A;
   private static int[] H;
   private static int U;
   private static int V = -1;
   private static boolean s;
   private static AbstractContainerMenu b;
   private static AbstractContainerMenu P;
   private static final Map<Slot, ki.d> B = new IdentityHashMap<>();
   private static final Map<Slot, SlotWrapper> Y = new IdentityHashMap<>();
   private static final SimpleContainer n = new SimpleContainer(1);
   private static boolean g = (boolean)1;
   private static Object w;
   private static long L;
   private static int G = -1;

   public static boolean y() {
      return m3.u();
   }

   public static void n() {
      g = (boolean)1;
   }

   public static long x() {
      return L;
   }

   public static void X() {
      w = null;
      A = null;
      H = null;
      P = null;
      b = null;
      B.clear();
      Y.clear();
      G = -1;
      V = -1;
      s = (boolean)0;
      h = new lz();
      g = (boolean)1;
   }

   public static boolean k() {
      Minecraft var0 = Minecraft.getInstance();
      return var0.player != null && b8.k().J();
   }

   public static void I() {
      g = (boolean)1;
      m();
   }

   private static void m() {
      Minecraft var0 = Minecraft.getInstance();
      if (!k()) {
         A = null;
         H = null;
      } else {
         if (var0.player.containerMenu.getCarried().isEmpty()) {
            G = -1;
         }

         if (A != var0.player) {
            A = var0.player;
            h = new lz();
            H = null;
            V = var0.player.getInventory().getSelectedSlot();
            U = V;
            s = (boolean)0;
            g = (boolean)1;
            com.larpclient.h.U("FakeSpawner", "Visual inventory initialized; ordinary slot positions retained");
         }

         if (w != var0.player.containerMenu) {
            w = var0.player.containerMenu;
            g = (boolean)1;
         }

         if (g || H == null) {
            if (H == null || !y()) {
               long var1 = 0L;
               long var3 = 0L;

               for (int var5 = 0; var5 < 36; var5++) {
                  if (!var0.player.getInventory().getItem(var5).isEmpty() || mm.o(var5) || b8.k().C(var5)) {
                     var1 |= 1L << var5;
                  }

                  if (b8.k().t(var5) && b8.k().l(var5, var0.player.getInventory().getItem(var5)) == 0) {
                     var3 |= 1L << var5;
                  }
               }

               g = (boolean)0;
               int[] var6 = h.Y(var1, var3);
               if (var6 != H) {
                  H = var6;
                  L++;
                  com.larpclient.h.U("FakeSpawner", "Mapping snapshot {} occupancy={} reserved={}", L, Long.toHexString(var1), Long.toHexString(var3));
               }
            }
         }
      }
   }

   public static int t(int var0) {
      m();
      return H != null && var0 >= 0 && var0 < 36 ? H[var0] : var0;
   }

   public static ItemStack e(int var0) {
      int var1 = t(var0);
      return var1 < 0 ? ItemStack.EMPTY : T(var1, Minecraft.getInstance().player.getInventory().getItem(var1));
   }

   public static ItemStack T(int var0, ItemStack var1) {
      return b8.k().t(var0) ? b8.k().N(var0, var1) : b8.k().Q(var0, mm.l(var0, var1));
   }

   public static int k(int var0) {
      m();
      if (!k()) {
         return var0;
      }

      if (var0 != V) {
         U = var0;
         s = (boolean)0;
      }

      V = var0;
      if (s && H != null && U >= 0 && U < 9 && H[U] < 0) {
         return U;
      }

      s = (boolean)0;
      if (H != null) {
         for (int var1 = 0; var1 < 9; var1++) {
            if (H[var1] == var0) {
               U = var1;
               break;
            }
         }
      }

      return U;
   }

   public static int n() {
      m();

      for (int var0 = 0; var0 < 36; var0++) {
         int var1 = H == null ? var0 : H[var0];
         if (var1 < 0 || Minecraft.getInstance().player.getInventory().getItem(var1).isEmpty() && !b8.k().C(var1) && !mm.o(var1)) {
            return var0;
         }
      }

      return -1;
   }

   public static void D(int var0, ItemStack var1, int var2) {
      if (var2 >= 0) {
         h.H(var2, var0);
      }

      I();
   }

   public static void n(int var0, ItemStack var1) {
      D(var0, var1, n());
   }

   public static Slot x(AbstractContainerMenu var0, Slot var1) {
      Minecraft var2 = Minecraft.getInstance();
      if (var0 instanceof ItemPickerMenu) {
         if (k() && var1 instanceof SlotWrapper var7 && !(var7.target instanceof ki.d)) {
            b(var0);
            return W(var1, x(var2.player.inventoryMenu, var7.target));
         } else {
            return var1;
         }
      } else if (k() && !(var1 instanceof ki.d) && var1.container == var2.player.getInventory() && var1.getContainerSlot() >= 0 && var1.getContainerSlot() < 36
         )
       {
         int var3 = t(var1.getContainerSlot());
         Slot var4 = null;
         if (var3 >= 0) {
            for (Slot var6 : var0.slots) {
               if (var6.container == var1.container && var6.getContainerSlot() == var3) {
                  var4 = var6;
                  break;
               }
            }
         }

         if (b != var0) {
            B.clear();
            Y.clear();
            b = var0;
         }

         ki.d var8 = B.get(var1);
         if (var8 == null || var8.r != var4) {
            var8 = new ki.d(var1, var4);
            B.put(var1, var8);
         }

         return var8;
      } else {
         return var1;
      }
   }

   private static Slot W(Slot var0, Slot var1) {
      SlotWrapper var2 = Y.get(var0);
      if (var2 == null || var2.target != var1) {
         var2 = new SlotWrapper(var1, var0.getContainerSlot(), var0.x, var0.y);
         var2.index = var0.index;
         Y.put(var0, var2);
      }

      return var2;
   }

   private static void b(AbstractContainerMenu var0) {
      if (P != var0) {
         Y.clear();
         P = var0;
      }
   }

   public static Slot f(Slot var0) {
      return var0 instanceof SlotWrapper var1 ? var1.target : var0;
   }

   public static Slot e(AbstractContainerMenu var0, Slot var1, Set<Slot> var2) {
      if (k() && var0 instanceof ItemPickerMenu && var1 instanceof SlotWrapper var16) {
         b(var0);
         InventoryMenu var17 = Minecraft.getInstance().player.inventoryMenu;
         HashSet var18 = new HashSet();

         for (Slot var21 : var2) {
            var18.add(f(var21));
         }

         Slot var20 = e(var17, var16.target, var18);
         if (!var2.isEmpty()) {
            var2.clear();

            for (Slot var23 : var18) {
               for (Slot var25 : var0.slots) {
                  Slot var26 = x(var0, var25);
                  if (f(var26) == var23) {
                     var2.add(var26);
                     break;
                  }
               }
            }
         }

         return W(var1, var20);
      } else {
         Slot var3 = x(var0, var1);
         if (var3 instanceof ki.d var4 && var4.r == null && !y() && var0.containerId >= 0 && !var0.getCarried().isEmpty() && !gz.w(var0.getCarried())) {
            Inventory var5 = Minecraft.getInstance().player.getInventory();

            for (int var6 = 0; var6 < 2; var6++) {
               for (Slot var8 : var0.slots) {
                  int var9 = var8.getContainerSlot();
                  if (var8.container == var5
                     && var9 >= 0
                     && var9 < 36
                     && !var8.hasItem()
                     && (var6 != 0 || var9 != G)
                     && (var6 != 1 || var9 == G)
                     && !D(var9, var8.getItem())
                     && var8.mayPlace(var0.getCarried())
                     && var8.isActive()
                     && var8.getMaxStackSize(var0.getCarried()) >= 1
                     && !var2.stream().anyMatch(var2x -> var2x.container == var5 && var2x.getContainerSlot() == var9)) {
                     h.H(var4.d, var9);
                     n();
                     m();
                     if (!var2.isEmpty()) {
                        ArrayList var10 = new ArrayList(var2.size());

                        for (Slot var12 : var2) {
                           Slot var13 = var12;
                           if (var12.container == var5) {
                              for (Slot var15 : var0.slots) {
                                 if (var15.container == var5
                                    && var15.getContainerSlot() >= 0
                                    && var15.getContainerSlot() < 36
                                    && t(var15.getContainerSlot()) == var12.getContainerSlot()) {
                                    var13 = x(var0, var15);
                                    break;
                                 }
                              }
                           }

                           var10.add(var13);
                        }

                        var2.clear();
                        var2.addAll(var10);
                     }

                     com.larpclient.h.U("FakeSpawner", "Visual gap bound logical={} physical={} menu={}", var4.d, var9, var0.containerId);
                     return x(var0, var1);
                  }
               }
            }

            return var3;
         } else {
            return var3;
         }
      }
   }

   public static Slot v(AbstractContainerMenu var0, Slot var1) {
      if (var1 instanceof ki.d var2 && var2.r != null) {
         for (Slot var4 : var0.slots) {
            if (var4.container == var1.container && var4.getContainerSlot() == var1.getContainerSlot()) {
               return new ki.d(new Slot(var1.container, var2.d, var1.x, var1.y), var4);
            }
         }

         return var1;
      } else {
         return var1;
      }
   }

   public static void K(Slot var0) {
      if (!y()) {
         if (var0 instanceof ki.d var1 && var1.r != null) {
            if (Minecraft.getInstance().player.containerMenu.getCarried().isEmpty() && var1.hasItem()) {
               G = var1.r.getContainerSlot();
            }

            h.H(var1.d, var1.r.getContainerSlot());
            n();
         }
      }
   }

   private static boolean D(int var0, ItemStack var1) {
      return var0 < 0 || b8.k().t(var0) || mm.o(var0) || b8.k().C(var0);
   }

   public static boolean z(Slot var0) {
      return var0 instanceof ki.d var1 && (var1.r == null || D(var1.r.getContainerSlot(), var1.r.getItem()));
   }

   public static void g(int var0, int var1) {
      h.H(var0, var1);
      I();
   }

   public static void z(AbstractContainerMenu var0, Slot var1, int var2) {
      if (!y()) {
         Minecraft var3 = Minecraft.getInstance();
         if (var1 != null && !z(var1) && var3.gameMode != null && var0.getCarried().isEmpty()) {
            int var4 = t(var2);
            Slot var5 = null;

            for (Slot var7 : var0.slots) {
               if (var7.container == var3.player.getInventory() && var7.getContainerSlot() == var4) {
                  var5 = var7;
                  break;
               }
            }

            if (var5 != null && var5.index != var1.index && !D(var4, var5.getItem())) {
               if (var1.mayPickup(var3.player)
                  && var5.mayPickup(var3.player)
                  && (var5.getItem().isEmpty() || var1.mayPlace(var5.getItem()) && var5.getItem().getCount() <= var1.getMaxStackSize(var5.getItem()))
                  && (var1.getItem().isEmpty() || var5.mayPlace(var1.getItem()))) {
                  if (var4 < 9) {
                     if (!b8.k().n(var0, List.of(new m3.g(var1.index, var4, ContainerInput.SWAP)))) {
                        var3.gameMode.handleContainerInput(var0.containerId, var1.index, var4, ContainerInput.SWAP, var3.player);
                     }
                  } else {
                     int var10 = 0;

                     while (var1.container == var3.player.getInventory() && var1.getContainerSlot() == var10) {
                        var10++;
                     }

                     int var11 = var10;
                     Slot var8 = var5;
                     if (b8.k().t(var11)) {
                        return;
                     }

                     List var9 = List.of(
                        new m3.g(var1.index, var11, ContainerInput.SWAP),
                        new m3.g(var8.index, var11, ContainerInput.SWAP),
                        new m3.g(var1.index, var11, ContainerInput.SWAP)
                     );
                     if (!b8.k().n(var0, var9)) {
                        m3.E(var0, "visual number-key swap", var9, ki::n, ki::n);
                     }
                  }

                  h.H(var2, var4);
                  if (var1 instanceof ki.d) {
                     K(var1);
                  } else if (var1.container == var3.player.getInventory()) {
                     h.H(var1.getContainerSlot(), var1.getContainerSlot());
                  }

                  I();
               }
            }
         }
      }
   }

   public static void x(int var0) {
      if (!y()) {
         Minecraft var1 = Minecraft.getInstance();
         if (!k()) {
            var1.player.getInventory().setSelectedSlot(var0);
         } else {
            int var2 = t(var0);
            if (var2 >= 0) {
               s = (boolean)0;
               if (var2 < 9) {
                  var1.player.getInventory().setSelectedSlot(var2);
                  V = var2;
                  U = var0;
               } else if (var1.gameMode != null && var1.player.containerMenu == var1.player.inventoryMenu && var1.player.inventoryMenu.getCarried().isEmpty()) {
                  for (int var6 = 0; var6 < 9; var6++) {
                     if (!b8.k().t(var6)) {
                        b8 var8 = b8.k();
                        if (var1.player.getInventory().getItem(var2).is(Items.BONE)) {
                           var8.Y(var6);
                        }

                        if (var1.player.getInventory().getItem(var6).is(Items.BONE)) {
                           var8.Y(var2);
                        }

                        var1.gameMode.handleContainerInput(0, var2, var6, ContainerInput.SWAP, var1.player);
                        h.d(var2, var6);
                        n();
                        var1.player.getInventory().setSelectedSlot(var6);
                        V = var6;
                        U = var0;
                        return;
                     }
                  }

                  if (b8.k().c(var2, var0)) {
                     h.d(var2, var0);
                     h.H(var0, var0);
                     var1.player.getInventory().setSelectedSlot(var0);
                     V = var0;
                     U = var0;
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
                        if (b8.k().t(var7)) {
                           var3 = var7;
                           break;
                        }
                     }

                     var1.player.getInventory().setSelectedSlot(var3);
                     V = var3;
                     U = var0;
                     s = (boolean)1;
                     return;
                  }

                  if (!D(var3, var1.player.getInventory().getItem(var3)) && var1.player.getInventory().getItem(var3).isEmpty()) {
                     var4 = var3;
                     if (var3 < 9) {
                        break;
                     }

                     var4 = var0;
                     if (b8.k().c(var3, var4)) {
                        h.d(var3, var4);
                        h.H(var0, var4);
                        n();
                        break;
                     }
                  }

                  var3++;
               }

               var1.player.getInventory().setSelectedSlot(var4);
               V = var4;
               U = var0;
               s = (boolean)1;
            }
         }
      }
   }

   public static final class d extends Slot {
      private final Slot r;
      private final int d;

      d(Slot var1, Slot var2) {
         super((Container)(var2 == null ? ki.n : var2.container), var2 == null ? 0 : var2.getContainerSlot(), var1.x, var1.y);
         this.r = var2;
         this.d = var1.getContainerSlot();
         this.index = var2 == null ? -1 : var2.index;
      }

      public ItemStack getItem() {
         return this.r == null ? ItemStack.EMPTY : ki.T(this.r.getContainerSlot(), this.r.getItem());
      }

      public boolean mayPlace(ItemStack var1) {
         return this.r != null && !ki.z(this) && this.r.mayPlace(var1);
      }

      public boolean mayPickup(Player var1) {
         return this.r != null && (!ki.z(this) || b8.k().l(this.r.getContainerSlot(), this.r.getItem()) > 0) && this.r.mayPickup(var1);
      }

      public int getMaxStackSize() {
         return this.r == null ? 0 : this.r.getMaxStackSize();
      }

      public int getMaxStackSize(ItemStack var1) {
         return this.r == null ? 0 : this.r.getMaxStackSize(var1);
      }

      public boolean isActive() {
         return this.r != null && this.r.isActive();
      }

      public Identifier getNoItemIcon() {
         return this.r == null ? null : this.r.getNoItemIcon();
      }

      public boolean isFake() {
         return this.r != null && this.r.isFake();
      }
   }
}
