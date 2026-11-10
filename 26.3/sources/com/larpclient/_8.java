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

public final class _8 {
   private static a8 s = new a8();
   private static Object j;
   private static int[] a;
   private static int Q;
   private static int R = -1;
   private static boolean M;
   private static AbstractContainerMenu L;
   private static AbstractContainerMenu y;
   private static final Map<Slot, _8.k> h = new IdentityHashMap<>();
   private static final Map<Slot, SlotWrapper> q = new IdentityHashMap<>();
   private static final SimpleContainer C = new SimpleContainer(1);
   private static boolean O = (boolean)1;
   private static Object f;
   private static long B;
   private static int i = -1;

   public static boolean e() {
      return q7.M();
   }

   public static void S() {
      O = (boolean)1;
   }

   public static long D() {
      return B;
   }

   public static void P() {
      f = null;
      j = null;
      a = null;
      y = null;
      L = null;
      h.clear();
      q.clear();
      i = -1;
      R = -1;
      M = (boolean)0;
      s = new a8();
      O = (boolean)1;
   }

   public static boolean S() {
      Minecraft var0 = Minecraft.getInstance();
      return var0.player != null && jn.g().a();
   }

   public static void j() {
      O = (boolean)1;
      H();
   }

   private static void H() {
      Minecraft var0 = Minecraft.getInstance();
      if (!S()) {
         j = null;
         a = null;
      } else {
         if (var0.player.containerMenu.getCarried().isEmpty()) {
            i = -1;
         }

         if (j != var0.player) {
            j = var0.player;
            s = new a8();
            a = null;
            R = var0.player.getInventory().getSelectedSlot();
            Q = R;
            M = (boolean)0;
            O = (boolean)1;
            _q.c("FakeSpawner", "Visual inventory initialized; ordinary slot positions retained");
         }

         if (f != var0.player.containerMenu) {
            f = var0.player.containerMenu;
            O = (boolean)1;
         }

         if (O || a == null) {
            if (a == null || !e()) {
               long var1 = 0L;
               long var3 = 0L;

               for (int var5 = 0; var5 < 36; var5++) {
                  if (!var0.player.getInventory().getItem(var5).isEmpty() || _v.O(var5) || jn.g().s(var5)) {
                     var1 |= 1L << var5;
                  }

                  if (jn.g().G(var5) && jn.g().P(var5, var0.player.getInventory().getItem(var5)) == 0) {
                     var3 |= 1L << var5;
                  }
               }

               O = (boolean)0;
               int[] var6 = s.h(var1, var3);
               if (var6 != a) {
                  a = var6;
                  B++;
                  _q.c("FakeSpawner", "Mapping snapshot {} occupancy={} reserved={}", B, Long.toHexString(var1), Long.toHexString(var3));
               }
            }
         }
      }
   }

   public static int t(int var0) {
      H();
      return a != null && var0 >= 0 && var0 < 36 ? a[var0] : var0;
   }

   public static ItemStack t(int var0) {
      int var1 = t(var0);
      return var1 < 0 ? ItemStack.EMPTY : R(var1, Minecraft.getInstance().player.getInventory().getItem(var1));
   }

   public static ItemStack R(int var0, ItemStack var1) {
      return jn.g().G(var0) ? jn.g().u(var0, var1) : jn.g().Z(var0, _v.C(var0, var1));
   }

   public static int a(int var0) {
      H();
      if (!S()) {
         return var0;
      }

      if (var0 != R) {
         Q = var0;
         M = (boolean)0;
      }

      R = var0;
      if (M && a != null && Q >= 0 && Q < 9 && a[Q] < 0) {
         return Q;
      }

      M = (boolean)0;
      if (a != null) {
         for (int var1 = 0; var1 < 9; var1++) {
            if (a[var1] == var0) {
               Q = var1;
               break;
            }
         }
      }

      return Q;
   }

   public static int y() {
      H();

      for (int var0 = 0; var0 < 36; var0++) {
         int var1 = a == null ? var0 : a[var0];
         if (var1 < 0 || Minecraft.getInstance().player.getInventory().getItem(var1).isEmpty() && !jn.g().s(var1) && !_v.O(var1)) {
            return var0;
         }
      }

      return -1;
   }

   public static void B(int var0, ItemStack var1, int var2) {
      if (var2 >= 0) {
         s.T(var2, var0);
      }

      j();
   }

   public static void b(int var0, ItemStack var1) {
      B(var0, var1, y());
   }

   public static Slot W(AbstractContainerMenu var0, Slot var1) {
      Minecraft var2 = Minecraft.getInstance();
      if (var0 instanceof ItemPickerMenu) {
         if (S() && var1 instanceof SlotWrapper var7 && !(var7.target instanceof _8.k)) {
            H(var0);
            return p(var1, W(var2.player.inventoryMenu, var7.target));
         } else {
            return var1;
         }
      } else if (S() && !(var1 instanceof _8.k) && var1.container == var2.player.getInventory() && var1.getContainerSlot() >= 0 && var1.getContainerSlot() < 36
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

         if (L != var0) {
            h.clear();
            q.clear();
            L = var0;
         }

         _8.k var8 = h.get(var1);
         if (var8 == null || var8.Y != var4) {
            var8 = new _8.k(var1, var4);
            h.put(var1, var8);
         }

         return var8;
      } else {
         return var1;
      }
   }

   private static Slot p(Slot var0, Slot var1) {
      SlotWrapper var2 = q.get(var0);
      if (var2 == null || var2.target != var1) {
         var2 = new SlotWrapper(var1, var0.getContainerSlot(), var0.x, var0.y);
         var2.index = var0.index;
         q.put(var0, var2);
      }

      return var2;
   }

   private static void H(AbstractContainerMenu var0) {
      if (y != var0) {
         q.clear();
         y = var0;
      }
   }

   public static Slot K(Slot var0) {
      return var0 instanceof SlotWrapper var1 ? var1.target : var0;
   }

   public static Slot y(AbstractContainerMenu var0, Slot var1, Set<Slot> var2) {
      if (S() && var0 instanceof ItemPickerMenu && var1 instanceof SlotWrapper var16) {
         H(var0);
         InventoryMenu var17 = Minecraft.getInstance().player.inventoryMenu;
         HashSet var18 = new HashSet();

         for (Slot var21 : var2) {
            var18.add(K(var21));
         }

         Slot var20 = y(var17, var16.target, var18);
         if (!var2.isEmpty()) {
            var2.clear();

            for (Slot var23 : var18) {
               for (Slot var25 : var0.slots) {
                  Slot var26 = W(var0, var25);
                  if (K(var26) == var23) {
                     var2.add(var26);
                     break;
                  }
               }
            }
         }

         return p(var1, var20);
      } else {
         Slot var3 = W(var0, var1);
         if (var3 instanceof _8.k var4 && var4.Y == null && !e() && var0.containerId >= 0 && !var0.getCarried().isEmpty() && !nn.O(var0.getCarried())) {
            Inventory var5 = Minecraft.getInstance().player.getInventory();

            for (int var6 = 0; var6 < 2; var6++) {
               for (Slot var8 : var0.slots) {
                  int var9 = var8.getContainerSlot();
                  if (var8.container == var5
                     && var9 >= 0
                     && var9 < 36
                     && !var8.hasItem()
                     && (var6 != 0 || var9 != i)
                     && (var6 != 1 || var9 == i)
                     && !f(var9, var8.getItem())
                     && var8.mayPlace(var0.getCarried())
                     && var8.isActive()
                     && var8.getMaxStackSize(var0.getCarried()) >= 1
                     && !var2.stream().anyMatch(var2x -> var2x.container == var5 && var2x.getContainerSlot() == var9)) {
                     s.T(var4.W, var9);
                     S();
                     H();
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

                     _q.c("FakeSpawner", "Visual gap bound logical={} physical={} menu={}", var4.W, var9, var0.containerId);
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

   public static Slot T(AbstractContainerMenu var0, Slot var1) {
      if (var1 instanceof _8.k var2 && var2.Y != null) {
         for (Slot var4 : var0.slots) {
            if (var4.container == var1.container && var4.getContainerSlot() == var1.getContainerSlot()) {
               return new _8.k(new Slot(var1.container, var2.W, var1.x, var1.y), var4);
            }
         }

         return var1;
      } else {
         return var1;
      }
   }

   public static void L(Slot var0) {
      if (!e()) {
         if (var0 instanceof _8.k var1 && var1.Y != null) {
            if (Minecraft.getInstance().player.containerMenu.getCarried().isEmpty() && var1.hasItem()) {
               i = var1.Y.getContainerSlot();
            }

            s.T(var1.W, var1.Y.getContainerSlot());
            S();
         }
      }
   }

   private static boolean f(int var0, ItemStack var1) {
      return var0 < 0 || jn.g().G(var0) || _v.O(var0) || jn.g().s(var0);
   }

   public static boolean z(Slot var0) {
      return var0 instanceof _8.k var1 && (var1.Y == null || f(var1.Y.getContainerSlot(), var1.Y.getItem()));
   }

   public static void k(int var0, int var1) {
      s.T(var0, var1);
      j();
   }

   public static void k(AbstractContainerMenu var0, Slot var1, int var2) {
      if (!e()) {
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

            if (var5 != null && var5.index != var1.index && !f(var4, var5.getItem())) {
               if (var1.mayPickup(var3.player)
                  && var5.mayPickup(var3.player)
                  && (var5.getItem().isEmpty() || var1.mayPlace(var5.getItem()) && var5.getItem().getCount() <= var1.getMaxStackSize(var5.getItem()))
                  && (var1.getItem().isEmpty() || var5.mayPlace(var1.getItem()))) {
                  if (var4 < 9) {
                     if (!jn.g().H(var0, List.of(new q7.l(var1.index, var4, ContainerInput.SWAP)))) {
                        var3.gameMode.handleContainerInput(var0.containerId, var1.index, var4, ContainerInput.SWAP, var3.player);
                     }
                  } else {
                     int var10 = 0;

                     while (var1.container == var3.player.getInventory() && var1.getContainerSlot() == var10) {
                        var10++;
                     }

                     int var11 = var10;
                     Slot var8 = var5;
                     if (jn.g().G(var11)) {
                        return;
                     }

                     List var9 = List.of(
                        new q7.l(var1.index, var11, ContainerInput.SWAP),
                        new q7.l(var8.index, var11, ContainerInput.SWAP),
                        new q7.l(var1.index, var11, ContainerInput.SWAP)
                     );
                     if (!jn.g().H(var0, var9)) {
                        q7.I(var0, "visual number-key swap", var9, _8::S, _8::S);
                     }
                  }

                  s.T(var2, var4);
                  if (var1 instanceof _8.k) {
                     L(var1);
                  } else if (var1.container == var3.player.getInventory()) {
                     s.T(var1.getContainerSlot(), var1.getContainerSlot());
                  }

                  j();
               }
            }
         }
      }
   }

   public static void s(int var0) {
      if (!e()) {
         Minecraft var1 = Minecraft.getInstance();
         if (!S()) {
            var1.player.getInventory().setSelectedSlot(var0);
         } else {
            int var2 = t(var0);
            if (var2 >= 0) {
               M = (boolean)0;
               if (var2 < 9) {
                  var1.player.getInventory().setSelectedSlot(var2);
                  R = var2;
                  Q = var0;
               } else if (var1.gameMode != null && var1.player.containerMenu == var1.player.inventoryMenu && var1.player.inventoryMenu.getCarried().isEmpty()) {
                  for (int var6 = 0; var6 < 9; var6++) {
                     if (!jn.g().G(var6)) {
                        jn var8 = jn.g();
                        if (var1.player.getInventory().getItem(var2).is(Items.BONE)) {
                           var8.f(var6);
                        }

                        if (var1.player.getInventory().getItem(var6).is(Items.BONE)) {
                           var8.f(var2);
                        }

                        var1.gameMode.handleContainerInput(0, var2, var6, ContainerInput.SWAP, var1.player);
                        s.D(var2, var6);
                        S();
                        var1.player.getInventory().setSelectedSlot(var6);
                        R = var6;
                        Q = var0;
                        return;
                     }
                  }

                  if (jn.g().o(var2, var0)) {
                     s.D(var2, var0);
                     s.T(var0, var0);
                     var1.player.getInventory().setSelectedSlot(var0);
                     R = var0;
                     Q = var0;
                     j();
                  }
               }
            } else {
               int var3 = 0;

               int var4;
               while (true) {
                  if (var3 >= 36) {
                     var3 = var0;

                     for (int var7 = 0; var7 < 9; var7++) {
                        if (jn.g().G(var7)) {
                           var3 = var7;
                           break;
                        }
                     }

                     var1.player.getInventory().setSelectedSlot(var3);
                     R = var3;
                     Q = var0;
                     M = (boolean)1;
                     return;
                  }

                  if (!f(var3, var1.player.getInventory().getItem(var3)) && var1.player.getInventory().getItem(var3).isEmpty()) {
                     var4 = var3;
                     if (var3 < 9) {
                        break;
                     }

                     var4 = var0;
                     if (jn.g().o(var3, var4)) {
                        s.D(var3, var4);
                        s.T(var0, var4);
                        S();
                        break;
                     }
                  }

                  var3++;
               }

               var1.player.getInventory().setSelectedSlot(var4);
               R = var4;
               Q = var0;
               M = (boolean)1;
            }
         }
      }
   }

   public static final class k extends Slot {
      private final Slot Y;
      private final int W;

      k(Slot var1, Slot var2) {
         super((Container)(var2 == null ? _8.C : var2.container), var2 == null ? 0 : var2.getContainerSlot(), var1.x, var1.y);
         this.Y = var2;
         this.W = var1.getContainerSlot();
         this.index = var2 == null ? -1 : var2.index;
      }

      public ItemStack getItem() {
         return this.Y == null ? ItemStack.EMPTY : _8.R(this.Y.getContainerSlot(), this.Y.getItem());
      }

      public boolean mayPlace(ItemStack var1) {
         return this.Y != null && !_8.z(this) && this.Y.mayPlace(var1);
      }

      public boolean mayPickup(Player var1) {
         return this.Y != null && (!_8.z(this) || jn.g().P(this.Y.getContainerSlot(), this.Y.getItem()) > 0) && this.Y.mayPickup(var1);
      }

      public int getMaxStackSize() {
         return this.Y == null ? 0 : this.Y.getMaxStackSize();
      }

      public int getMaxStackSize(ItemStack var1) {
         return this.Y == null ? 0 : this.Y.getMaxStackSize(var1);
      }

      public boolean isActive() {
         return this.Y != null && this.Y.isActive();
      }

      public Identifier getNoItemIcon() {
         return this.Y == null ? null : this.Y.getNoItemIcon();
      }

      public boolean isFake() {
         return this.Y != null && this.Y.isFake();
      }
   }
}
