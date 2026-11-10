package com.larpclient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Input;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerInput;
import net.minecraft.world.inventory.DispenserMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.saveddata.maps.MapId;

public final class mg {
   private static final um L = new um();
   private static final Map<String, Map<Integer, Integer>> K = new HashMap<>();
   private static final Map<Integer, ItemStack> y = new HashMap<>();
   private static final Map<um.l, Entity> X = new HashMap<>();
   private static final Map<um.o, mg.y> P = new IdentityHashMap<>();
   private static ClientLevel m;
   private static long j = Long.MIN_VALUE;
   private static Map<Integer, ph.x> O = Map.of();
   private static final Map<Integer, ItemStack> a = new HashMap<>();
   private static boolean Z;
   private static int b = -1;
   private static int r = -1;
   private static boolean p;

   private static Map<Integer, ph.x> d() {
      Minecraft var0 = Minecraft.getInstance();
      if (var0.player != null && !L.O.isEmpty()) {
         int[] var1 = new int[36];
         int[] var2 = new int[36];

         for (int var3 = 0; var3 < 36; var3++) {
            ItemStack var4 = var0.player.getInventory().getItem(var3);
            var1[var3] = Q(var4);
            var2[var3] = var4.getCount();
         }

         ArrayList var6 = new ArrayList();

         for (um.o var5 : L.O) {
            if (var5.T != null && var5.T.kind().equals("inventory") && var5.T.slot() < 36) {
               var6.add(new ph.a(var5.T.slot(), var5.j, var5.c, 1));
            }
         }

         Map var8 = ph.O(var1, var2, var6, O);
         if (!var8.equals(O)) {
            O = var8;
            a.clear();
            var8.forEach((var1x, var2x) -> {
               if (var2x.count() == 0) {
                  a.put(var1x, ItemStack.EMPTY);
               } else {
                  ItemStack var3x = var0.player.getInventory().getItem(var2x.portions().getFirst().source()).copyWithCount(var2x.count());
                  var3x.set(DataComponents.MAP_ID, new MapId(var2x.shown()));
                  a.put(var1x, var3x);
               }
            });
         }

         return O;
      } else {
         O = Map.of();
         a.clear();
         return O;
      }
   }

   public static int t(int var0) {
      if (p) {
         return var0;
      }

      b = var0;
      r = var0;
      ph.x var1 = d().get(var0);
      if (var1 != null && !var1.portions().isEmpty()) {
         ph.a var2 = var1.portions().getFirst();
         if (var2.source() >= 9) {
            return var0;
         }

         r = var2.source();
         L.H(d(var2.source()), var1.shown());
         return var2.source();
      } else {
         return var0;
      }
   }

   public static int j() {
      Minecraft var0 = Minecraft.getInstance();
      if (var0.player == null) {
         return -1;
      }

      int var1 = var0.player.getInventory().getSelectedSlot();
      if (!we.Y().isSwapActive() && !cc.z().v()) {
         if (b < 0 || b >= 9 || var1 != r) {
            b = var1;
            r = var1;
         }

         ph.x var2 = d().get(b);
         int var3 = var2 != null && !var2.portions().isEmpty() ? var2.portions().getFirst().source() : b;
         if (var3 < 9 && var3 != var1) {
            p = (boolean)1;

            try {
               var0.player.getInventory().setSelectedSlot(var3);
               r = var3;
            } finally {
               p = (boolean)0;
            }
         }

         return b;
      } else {
         return var1;
      }
   }

   public static boolean g() {
      ph.x var0 = d().get(j());
      return var0 == null || var0.count() > 0;
   }

   public static boolean C(boolean var0) {
      if (!g()) {
         return false;
      }

      ph.x var1 = d().get(j());
      Minecraft var2 = Minecraft.getInstance();
      return !var0 || var1 == null || var1.portions().size() == 1 && var1.count() == var2.player.getMainHandItem().getCount();
   }

   public static boolean P(int var0) {
      ph.x var1 = d().get(var0);
      return var1 != null && var1.portions().size() > 1;
   }

   public static void V() {
      Minecraft var0 = Minecraft.getInstance();
      if (var0.player != null) {
         ph.x var1 = d().get(j());
         if (var1 != null) {
            L.H(d(var0.player.getInventory().getSelectedSlot()), var1.shown());
         }
      }
   }

   private static int f(int var0) {
      Minecraft var1 = Minecraft.getInstance();

      for (Slot var3 : var1.player.containerMenu.slots) {
         if (var3.container == var1.player.getInventory() && var3.getContainerSlot() == var0) {
            return var3.index;
         }
      }

      return -1;
   }

   private static void P(int var0, int var1, ContainerInput var2) {
      Minecraft var3 = Minecraft.getInstance();
      var3.gameMode.handleContainerInput(var3.player.containerMenu.containerId, var0, var1, var2, var3.player);
      U();
   }

   private static void N(ph.a var0, int var1) {
      Minecraft var2 = Minecraft.getInstance();
      int var3 = f(var0.source());
      int var4 = var2.player.getInventory().getItem(var0.source()).getCount();
      L.H(d(var0.source()), var0.shown());
      P(var3, 0, ContainerInput.PICKUP);

      for (int var5 = var1; var5 < var4; var5++) {
         int var6 = Q(var2.player.containerMenu.getCarried());
         long var7 = L.O.stream().filter(var0x -> O().equals(var0x.T)).count();
         int var9 = var2.player.containerMenu.getCarried().getCount() > var7 ? var6 : var0.shown();
         if (var9 == var0.shown()) {
            for (um.o var11 : L.O) {
               if (O().equals(var11.T) && var11.c != var0.shown()) {
                  var9 = var11.c;
                  break;
               }
            }
         }

         L.H(O(), var9);
         P(var3, 1, ContainerInput.PICKUP);
      }
   }

   public static void T(Minecraft var0) {
      if (var0.player != null
         && var0.gameMode != null
         && !L.O.isEmpty()
         && !Z
         && !we.Y().isSwapActive()
         && !we.Y().blocksUserInteraction()
         && !pw.S().C()
         && !lh.I().A()
         && !we.Y().isSwapFreezingPassThrough()
         && var0.player.containerMenu.getCarried().isEmpty()) {
         U();

         for (um.o var2 : L.O) {
            if (var2.T != null && var2.T.kind().equals("inventory") && var2.T.slot() < 36) {
               int var3 = var2.T.slot();
               int var4 = var2.j;
               int var5 = var2.c;
               ItemStack var6 = var0.player.getInventory().getItem(var3);
               if (Q(var6) == var4) {
                  int var7 = (int)L.O.stream().filter(var3x -> d(var3).equals(var3x.T) && var3x.j == var4 && var3x.c == var5).count();
                  if (var7 < var6.getCount()) {
                     int var8 = -1;

                     for (int var9 = 0; var9 < 36; var9++) {
                        if (var0.player.getInventory().getItem(var9).isEmpty() && f(var9) >= 0) {
                           var8 = var9;
                           break;
                        }
                     }

                     if (var8 >= 0 && f(var3) >= 0) {
                        Z = (boolean)1;

                        try {
                           P(f(var3), 0, ContainerInput.PICKUP);

                           for (int var13 = 0; var13 < var7; var13++) {
                              L.H(O(), var5);
                              P(f(var8), 1, ContainerInput.PICKUP);
                           }

                           if (!var0.player.containerMenu.getCarried().isEmpty()) {
                              P(f(var3), 0, ContainerInput.PICKUP);
                           }
                        } finally {
                           Z = (boolean)0;
                        }

                        return;
                     }

                     return;
                  }
               }
            }
         }
      }
   }

   public static boolean f(int var0, int var1, int var2, ContainerInput var3) {
      Minecraft var4 = Minecraft.getInstance();
      if (!Z && var4.player != null && !L.O.isEmpty() && !we.Y().isSwapActive() && !pw.S().C() && !lh.I().A()) {
         AbstractContainerMenu var5 = var4.player.containerMenu;
         if (var5.containerId == var0 && var1 >= 0 && var1 < var5.slots.size()) {
            Slot var6 = var5.getSlot(var1);
            if (var6.container == var4.player.getInventory() && var6.getContainerSlot() >= 0 && var6.getContainerSlot() < 36) {
               U();
               ph.x var7 = d().get(var6.getContainerSlot());
               if (var7 == null) {
                  return false;
               }

               if (var3 != ContainerInput.PICKUP && var3 != ContainerInput.QUICK_MOVE && var3 != ContainerInput.THROW) {
                  return true;
               }

               Z = (boolean)1;

               try {
                  if (!var5.getCarried().isEmpty()) {
                     if (var3 != ContainerInput.PICKUP) {
                        return true;
                     }

                     ItemStack var19 = J(var5.getCarried());
                     int var21 = -1;
                     if (var7.shown() == Q(var19)) {
                        for (ph.a var25 : var7.portions()) {
                           if (ItemStack.isSameItemSameComponents(var4.player.getInventory().getItem(var25.source()), var5.getCarried())
                              && var4.player.getInventory().getItem(var25.source()).getCount() < var5.getCarried().getMaxStackSize()) {
                              var21 = f(var25.source());
                              break;
                           }
                        }
                     }

                     if (var21 < 0 && (var7.count() == 0 || var7.shown() == Q(var19))) {
                        for (int var23 = 0; var23 < 36; var23++) {
                           if (var4.player.getInventory().getItem(var23).isEmpty()) {
                              var21 = f(var23);
                              break;
                           }
                        }
                     }

                     if (var21 < 0 && var7.shown() != Q(var19) && !var7.portions().isEmpty()) {
                        ph.a var24 = var7.portions().getFirst();
                        ItemStack var26 = var4.player.getInventory().getItem(var24.source());
                        if (ItemStack.isSameItemSameComponents(var26, var5.getCarried())) {
                           return true;
                        }

                        if (var24.count() == var26.getCount()) {
                           var21 = f(var24.source());
                        }
                     }

                     if (var21 >= 0) {
                        P(var21, var2, var3);
                     }

                     return true;
                  } else {
                     if (var7.portions().isEmpty()) {
                        return true;
                     }

                     if (var3 == ContainerInput.QUICK_MOVE) {
                        for (ph.a var9 : var7.portions()) {
                           int var10 = var4.player.getInventory().getItem(var9.source()).getCount();
                           if (var10 == var9.count()) {
                              L.H(d(var9.source()), var9.shown());
                              P(f(var9.source()), 0, var3);
                           } else if (var5 instanceof DispenserMenu) {
                              int var11 = -1;
                              ItemStack var12 = var4.player.getInventory().getItem(var9.source());

                              for (int var13 = 0; var13 < 9; var13++) {
                                 ItemStack var14 = var5.getSlot(var13).getItem();
                                 if (!var14.isEmpty()
                                    && ItemStack.isSameItemSameComponents(var14, var12)
                                    && var14.getCount() + var9.count() <= var14.getMaxStackSize()) {
                                    var11 = var13;
                                    break;
                                 }
                              }

                              if (var11 < 0) {
                                 for (int var27 = 0; var27 < 9; var27++) {
                                    if (var5.getSlot(var27).getItem().isEmpty()) {
                                       var11 = var27;
                                       break;
                                    }
                                 }
                              }

                              if (var11 >= 0) {
                                 N(var9, var9.count());
                                 P(var11, 0, ContainerInput.PICKUP);
                                 if (!var5.getCarried().isEmpty()) {
                                    P(f(var9.source()), 0, ContainerInput.PICKUP);
                                 }
                              }
                           }
                        }
                     } else {
                        ph.a var18 = var7.portions().getFirst();
                        int var20 = var3 == ContainerInput.THROW
                           ? (var2 == 0 ? 1 : var18.count())
                           : Math.min(var18.count(), var2 == 1 ? (var7.count() + 1) / 2 : var18.count());
                        N(var18, var20);
                        if (var3 == ContainerInput.THROW) {
                           P(-999, 0, ContainerInput.PICKUP);
                        }
                     }

                     return true;
                  }
               } finally {
                  Z = (boolean)0;
               }
            } else {
               return false;
            }
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   private static um.l d(int var0) {
      return new um.l("inventory", "", var0);
   }

   private static um.l O() {
      return new um.l("cursor", "", 0);
   }

   private static um.l P(Entity var0) {
      return new um.l("entity", var0.getStringUUID(), 0);
   }

   private static int Q(ItemStack var0) {
      MapId var1 = var0.is(Items.FILLED_MAP) ? (MapId)var0.get(DataComponents.MAP_ID) : null;
      return var1 == null ? -1 : var1.id();
   }

   private static void o(Map<um.l, um.f> var0, um.l var1, ItemStack var2) {
      var0.put(var1, new um.f(Q(var2), var2.getCount()));
      if (Q(var2) >= 0) {
         y.put(Q(var2), var2.copyWithCount(1));
      }
   }

   private static String c(Minecraft var0) {
      gj var1 = gj.c();
      return var0.player != null && var0.player.containerMenu instanceof DispenserMenu var2 && var1.r() == var2.containerId ? var1.C() : null;
   }

   private static Map<um.l, um.f> l(Minecraft var0) {
      LinkedHashMap var1 = new LinkedHashMap();
      if (var0.player != null && var0.level != null) {
         for (int var2 = 0; var2 < var0.player.getInventory().getContainerSize(); var2++) {
            o(var1, d(var2), var0.player.getInventory().getItem(var2));
         }

         o(var1, O(), var0.player.containerMenu.getCarried());
         String var7 = c(var0);
         if (var7 != null) {
            Map var3 = K.computeIfAbsent(var7, var0x -> new HashMap<>());

            for (int var4 = 0; var4 < 9; var4++) {
               ItemStack var5 = var0.player.containerMenu.getSlot(var4).getItem();
               o(var1, new um.l("stock", var7, var4), var5);
               if (Q(var5) >= 0) {
                  var3.put(Q(var5), var4);
               }
            }
         }

         for (Entity var10 : var0.level.entitiesForRendering()) {
            if (var10 instanceof ItemFrame || var10 instanceof ItemEntity) {
               um.l var12 = P(var10);
               X.put(var12, var10);
               o(var1, var12, var10 instanceof ItemFrame var6 ? var6.getItem() : ((ItemEntity)var10).getItem());
            }
         }

         for (Entry var11 : X.entrySet()) {
            if (((Entity)var11.getValue()).isRemoved()) {
               o(var1, (um.l)var11.getKey(), ItemStack.EMPTY);
            }
         }

         return var1;
      } else {
         return var1;
      }
   }

   public static void J() {
      L.j();
      K.clear();
      y.clear();
      X.clear();
      P.clear();
      j = Long.MIN_VALUE;
      O = Map.of();
      a.clear();
      b = -1;
      r = -1;
   }

   public static void N(Minecraft var0) {
      if (m != var0.level) {
         J();
         m = var0.level;
      }

      if (var0.player != null && var0.level != null && var0.level.getGameTime() != j) {
         j = var0.level.getGameTime();
         if (!L.O.isEmpty()) {
            L.y(l(var0));
         }

         P.keySet().retainAll(L.O);
         j();
      }
   }

   public static void U() {
      Minecraft var0 = Minecraft.getInstance();
      N(var0);
      if (var0.player != null && var0.level != null && !L.O.isEmpty()) {
         L.y(l(var0));
      }
   }

   public static boolean S(Minecraft var0) {
      Input var1 = var0.player.input.keyPresses;
      return !var0.player.onGround()
         || var0.player.getDeltaMovement().horizontalDistanceSqr() > 1.0E-6
         || var1.forward()
         || var1.backward()
         || var1.left()
         || var1.right()
         || var1.jump()
         || var0.options.keyUp.isDown()
         || var0.options.keyDown.isDown()
         || var0.options.keyLeft.isDown()
         || var0.options.keyRight.isDown()
         || var0.options.keyJump.isDown();
   }

   public static boolean O(we var0, int var1, int var2) {
      Minecraft var3 = Minecraft.getInstance();
      N(var3);
      String var4 = var0.z();
      if (var4 != null && var1 >= 0 && var3.player != null) {
         ItemStack var5 = var3.player.getInventory().getItem(var1);
         int var6 = Q(var5);
         int var7 = var0.getTemplateIdForValue(var2);
         if (var6 >= 0 && var7 >= 0 && var6 != var7) {
            Map var8 = K.computeIfAbsent(var4, var0x -> new HashMap<>());

            for (int var9 = 1; var9 <= 10; var9++) {
               int var10 = var0.getTemplateIdForValue(var9);
               int var11 = var0.v(var9);
               if (var11 < 0) {
                  var11 = var0.containerIndexForValue(var9);
               }

               if (var10 >= 0 && var11 >= 0 && var11 < 9) {
                  var8.putIfAbsent(var10, var11);
               }
            }

            ItemStack var12 = var5.copyWithCount(1);
            var12.set(DataComponents.MAP_ID, new MapId(var7));
            y.putIfAbsent(var7, var12);
            L.G(var4, var6, var7, d(var1), l(var3));
            return true;
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   private static ItemStack w(um.l var0, ItemStack var1) {
      um.o var2 = L.L(var0, Q(var1));
      if (var2 == null) {
         return null;
      }

      mg.y var3 = P.get(var2);
      if (var3 != null && ItemStack.matches(var3.actual(), var1)) {
         return var3.visual();
      }

      ItemStack var4 = var1.copy();
      var4.set(DataComponents.MAP_ID, new MapId(var2.c));
      P.put(var2, new mg.y(var1.copy(), var4));
      return var4;
   }

   public static ItemStack Y(int var0) {
      Minecraft var1 = Minecraft.getInstance();
      if (var1.player != null && var0 >= 0 && var0 < var1.player.getInventory().getContainerSize() && !L.O.isEmpty()) {
         N(var1);
         if (var0 < 36) {
            d();
            return a.get(var0);
         } else {
            return w(d(var0), var1.player.getInventory().getItem(var0));
         }
      } else {
         return null;
      }
   }

   public static ItemStack W(String var0, ItemStack var1) {
      N(Minecraft.getInstance());

      for (um.o var3 : L.O) {
         if (var3.T != null && !var3.p() && var3.j == Q(var1)) {
            Entity var4 = X.get(var3.T);
            if (var3.T.kind().equals("inventory") && var0.equals("inventory[" + var3.T.slot() + "]")
               || var4 instanceof ItemFrame && var0.equals("item_frame[" + var4.getId() + "]")) {
               return w(var3.T, var1);
            }
         }
      }

      return var1;
   }

   public static ItemStack J(ItemStack var0) {
      N(Minecraft.getInstance());
      ItemStack var1 = w(O(), var0);
      return var1 == null ? var0 : var1;
   }

   public static ItemStack c(Entity var0, ItemStack var1) {
      if (L.O.isEmpty()) {
         return var1;
      }

      N(Minecraft.getInstance());
      ItemStack var2 = w(P(var0), var1);
      if (var2 == null && Q(var1) >= 0) {
         U();
         var2 = w(P(var0), var1);
      }

      return var2 == null ? var1 : var2;
   }

   public static ItemStack U(Slot var0) {
      Minecraft var1 = Minecraft.getInstance();
      if (var1.player != null && var0 != null) {
         if (var0.container == var1.player.getInventory()) {
            return Y(var0.getContainerSlot());
         }

         String var2 = c(var1);
         if (var2 != null && var0.index >= 0 && var0.index < 9 && !L.O.isEmpty()) {
            N(var1);
            Map var3 = K.getOrDefault(var2, Map.of());
            ItemStack var4 = var0.getItem();
            int var5 = Q(var4);
            if (var5 < 0) {
               for (Entry var7 : var3.entrySet()) {
                  if ((Integer)var7.getValue() == var0.index && L.G(var2, (Integer)var7.getKey()) > 0) {
                     var5 = (Integer)var7.getKey();
                     break;
                  }
               }
            }

            int var9 = var3.getOrDefault(var5, -1) == var0.index ? L.G(var2, var5) : 0;
            if (var9 == 0) {
               return null;
            }

            ItemStack var10 = var4.isEmpty() ? y.get(var5) : var4;
            if (var10 == null) {
               return null;
            }

            int var8 = Math.max(0, var4.getCount() + var9);
            return var8 == 0 ? ItemStack.EMPTY : var10.copyWithCount(var8);
         } else {
            return null;
         }
      } else {
         return null;
      }
   }

   public static void M(int var0, int var1) {
      Minecraft var2 = Minecraft.getInstance();
      if (var2.player != null && var2.player.containerMenu.containerId == var0 && var1 >= 0 && var1 < 9) {
         String var3 = c(var2);
         if (var3 != null) {
            U();
            L.s(new um.l("stock", var3, var1));
         }
      }
   }

   public static void m(int var0) {
      Minecraft var1 = Minecraft.getInstance();
      if (var1.player != null && var1.player.containerMenu.containerId == var0 && c(var1) != null) {
         U();
         L.D();
      }
   }

   private record y(ItemStack actual, ItemStack visual) {
   }
}
