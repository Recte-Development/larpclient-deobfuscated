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

public final class yg {
   private static final lm d = new lm();
   private static final Map<String, Map<Integer, Integer>> l = new HashMap<>();
   private static final Map<Integer, ItemStack> S = new HashMap<>();
   private static final Map<lm.y, Entity> I = new HashMap<>();
   private static final Map<lm.n, yg.d> a = new IdentityHashMap<>();
   private static ClientLevel W;
   private static long p = Long.MIN_VALUE;
   private static Map<Integer, mg.x> G = Map.of();
   private static final Map<Integer, ItemStack> o = new HashMap<>();
   private static boolean z;
   private static int C = -1;
   private static int X = -1;
   private static boolean u;

   private static Map<Integer, mg.x> M() {
      Minecraft var0 = Minecraft.getInstance();
      if (var0.player != null && !d.p.isEmpty()) {
         int[] var1 = new int[36];
         int[] var2 = new int[36];

         for (int var3 = 0; var3 < 36; var3++) {
            ItemStack var4 = var0.player.getInventory().getItem(var3);
            var1[var3] = G(var4);
            var2[var3] = var4.getCount();
         }

         ArrayList var6 = new ArrayList();

         for (lm.n var5 : d.p) {
            if (var5.T != null && var5.T.kind().equals("inventory") && var5.T.slot() < 36) {
               var6.add(new mg.g(var5.T.slot(), var5.w, var5.a, 1));
            }
         }

         Map var8 = mg.d(var1, var2, var6, G);
         if (!var8.equals(G)) {
            G = var8;
            o.clear();
            var8.forEach((var1x, var2x) -> {
               if (var2x.count() == 0) {
                  o.put(var1x, ItemStack.EMPTY);
               } else {
                  ItemStack var3x = var0.player.getInventory().getItem(var2x.portions().getFirst().source()).copyWithCount(var2x.count());
                  var3x.set(DataComponents.MAP_ID, new MapId(var2x.shown()));
                  o.put(var1x, var3x);
               }
            });
         }

         return G;
      } else {
         G = Map.of();
         o.clear();
         return G;
      }
   }

   public static int K(int var0) {
      if (u) {
         return var0;
      }

      C = var0;
      X = var0;
      mg.x var1 = M().get(var0);
      if (var1 != null && !var1.portions().isEmpty()) {
         mg.g var2 = var1.portions().getFirst();
         if (var2.source() >= 9) {
            return var0;
         }

         X = var2.source();
         d.A(y(var2.source()), var1.shown());
         return var2.source();
      } else {
         return var0;
      }
   }

   public static int m() {
      Minecraft var0 = Minecraft.getInstance();
      if (var0.player == null) {
         return -1;
      }

      int var1 = var0.player.getInventory().getSelectedSlot();
      if (!ug.x().isSwapActive() && !eg.y().G()) {
         if (C < 0 || C >= 9 || var1 != X) {
            C = var1;
            X = var1;
         }

         mg.x var2 = M().get(C);
         int var3 = var2 != null && !var2.portions().isEmpty() ? var2.portions().getFirst().source() : C;
         if (var3 < 9 && var3 != var1) {
            u = (boolean)1;

            try {
               var0.player.getInventory().setSelectedSlot(var3);
               X = var3;
            } finally {
               u = (boolean)0;
            }
         }

         return C;
      } else {
         return var1;
      }
   }

   public static boolean q() {
      mg.x var0 = M().get(m());
      return var0 == null || var0.count() > 0;
   }

   public static boolean t(boolean var0) {
      if (!q()) {
         return false;
      }

      mg.x var1 = M().get(m());
      Minecraft var2 = Minecraft.getInstance();
      return !var0 || var1 == null || var1.portions().size() == 1 && var1.count() == var2.player.getMainHandItem().getCount();
   }

   public static boolean A(int var0) {
      mg.x var1 = M().get(var0);
      return var1 != null && var1.portions().size() > 1;
   }

   public static void T() {
      Minecraft var0 = Minecraft.getInstance();
      if (var0.player != null) {
         mg.x var1 = M().get(m());
         if (var1 != null) {
            d.A(y(var0.player.getInventory().getSelectedSlot()), var1.shown());
         }
      }
   }

   private static int i(int var0) {
      Minecraft var1 = Minecraft.getInstance();

      for (Slot var3 : var1.player.containerMenu.slots) {
         if (var3.container == var1.player.getInventory() && var3.getContainerSlot() == var0) {
            return var3.index;
         }
      }

      return -1;
   }

   private static void T(int var0, int var1, ContainerInput var2) {
      Minecraft var3 = Minecraft.getInstance();
      var3.gameMode.handleContainerInput(var3.player.containerMenu.containerId, var0, var1, var2, var3.player);
      n();
   }

   private static void Y(mg.g var0, int var1) {
      Minecraft var2 = Minecraft.getInstance();
      int var3 = i(var0.source());
      int var4 = var2.player.getInventory().getItem(var0.source()).getCount();
      d.A(y(var0.source()), var0.shown());
      T(var3, 0, ContainerInput.PICKUP);

      for (int var5 = var1; var5 < var4; var5++) {
         int var6 = G(var2.player.containerMenu.getCarried());
         long var7 = d.p.stream().filter(var0x -> Y().equals(var0x.T)).count();
         int var9 = var2.player.containerMenu.getCarried().getCount() > var7 ? var6 : var0.shown();
         if (var9 == var0.shown()) {
            for (lm.n var11 : d.p) {
               if (Y().equals(var11.T) && var11.a != var0.shown()) {
                  var9 = var11.a;
                  break;
               }
            }
         }

         d.A(Y(), var9);
         T(var3, 1, ContainerInput.PICKUP);
      }
   }

   public static void T(Minecraft var0) {
      if (var0.player != null
         && var0.gameMode != null
         && !d.p.isEmpty()
         && !z
         && !ug.x().isSwapActive()
         && !ug.x().blocksUserInteraction()
         && !ro.H().p()
         && !mm.W().h()
         && !ug.x().isSwapFreezingPassThrough()
         && var0.player.containerMenu.getCarried().isEmpty()) {
         n();

         for (lm.n var2 : d.p) {
            if (var2.T != null && var2.T.kind().equals("inventory") && var2.T.slot() < 36) {
               int var3 = var2.T.slot();
               int var4 = var2.w;
               int var5 = var2.a;
               ItemStack var6 = var0.player.getInventory().getItem(var3);
               if (G(var6) == var4) {
                  int var7 = (int)d.p.stream().filter(var3x -> y(var3).equals(var3x.T) && var3x.w == var4 && var3x.a == var5).count();
                  if (var7 < var6.getCount()) {
                     int var8 = -1;

                     for (int var9 = 0; var9 < 36; var9++) {
                        if (var0.player.getInventory().getItem(var9).isEmpty() && i(var9) >= 0) {
                           var8 = var9;
                           break;
                        }
                     }

                     if (var8 >= 0 && i(var3) >= 0) {
                        z = (boolean)1;

                        try {
                           T(i(var3), 0, ContainerInput.PICKUP);

                           for (int var13 = 0; var13 < var7; var13++) {
                              d.A(Y(), var5);
                              T(i(var8), 1, ContainerInput.PICKUP);
                           }

                           if (!var0.player.containerMenu.getCarried().isEmpty()) {
                              T(i(var3), 0, ContainerInput.PICKUP);
                           }
                        } finally {
                           z = (boolean)0;
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

   public static boolean a(int var0, int var1, int var2, ContainerInput var3) {
      Minecraft var4 = Minecraft.getInstance();
      if (!z && var4.player != null && !d.p.isEmpty() && !ug.x().isSwapActive() && !ro.H().p() && !mm.W().h()) {
         AbstractContainerMenu var5 = var4.player.containerMenu;
         if (var5.containerId == var0 && var1 >= 0 && var1 < var5.slots.size()) {
            Slot var6 = var5.getSlot(var1);
            if (var6.container == var4.player.getInventory() && var6.getContainerSlot() >= 0 && var6.getContainerSlot() < 36) {
               n();
               mg.x var7 = M().get(var6.getContainerSlot());
               if (var7 == null) {
                  return false;
               }

               if (var3 != ContainerInput.PICKUP && var3 != ContainerInput.QUICK_MOVE && var3 != ContainerInput.THROW) {
                  return true;
               }

               z = (boolean)1;

               try {
                  if (!var5.getCarried().isEmpty()) {
                     if (var3 != ContainerInput.PICKUP) {
                        return true;
                     }

                     ItemStack var19 = Z(var5.getCarried());
                     int var21 = -1;
                     if (var7.shown() == G(var19)) {
                        for (mg.g var25 : var7.portions()) {
                           if (ItemStack.isSameItemSameComponents(var4.player.getInventory().getItem(var25.source()), var5.getCarried())
                              && var4.player.getInventory().getItem(var25.source()).getCount() < var5.getCarried().getMaxStackSize()) {
                              var21 = i(var25.source());
                              break;
                           }
                        }
                     }

                     if (var21 < 0 && (var7.count() == 0 || var7.shown() == G(var19))) {
                        for (int var23 = 0; var23 < 36; var23++) {
                           if (var4.player.getInventory().getItem(var23).isEmpty()) {
                              var21 = i(var23);
                              break;
                           }
                        }
                     }

                     if (var21 < 0 && var7.shown() != G(var19) && !var7.portions().isEmpty()) {
                        mg.g var24 = var7.portions().getFirst();
                        ItemStack var26 = var4.player.getInventory().getItem(var24.source());
                        if (ItemStack.isSameItemSameComponents(var26, var5.getCarried())) {
                           return true;
                        }

                        if (var24.count() == var26.getCount()) {
                           var21 = i(var24.source());
                        }
                     }

                     if (var21 >= 0) {
                        T(var21, var2, var3);
                     }

                     return true;
                  } else {
                     if (var7.portions().isEmpty()) {
                        return true;
                     }

                     if (var3 == ContainerInput.QUICK_MOVE) {
                        for (mg.g var9 : var7.portions()) {
                           int var10 = var4.player.getInventory().getItem(var9.source()).getCount();
                           if (var10 == var9.count()) {
                              d.A(y(var9.source()), var9.shown());
                              T(i(var9.source()), 0, var3);
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
                                 Y(var9, var9.count());
                                 T(var11, 0, ContainerInput.PICKUP);
                                 if (!var5.getCarried().isEmpty()) {
                                    T(i(var9.source()), 0, ContainerInput.PICKUP);
                                 }
                              }
                           }
                        }
                     } else {
                        mg.g var18 = var7.portions().getFirst();
                        int var20 = var3 == ContainerInput.THROW
                           ? (var2 == 0 ? 1 : var18.count())
                           : Math.min(var18.count(), var2 == 1 ? (var7.count() + 1) / 2 : var18.count());
                        Y(var18, var20);
                        if (var3 == ContainerInput.THROW) {
                           T(-999, 0, ContainerInput.PICKUP);
                        }
                     }

                     return true;
                  }
               } finally {
                  z = (boolean)0;
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

   private static lm.y y(int var0) {
      return new lm.y("inventory", "", var0);
   }

   private static lm.y Y() {
      return new lm.y("cursor", "", 0);
   }

   private static lm.y I(Entity var0) {
      return new lm.y("entity", var0.getStringUUID(), 0);
   }

   private static int G(ItemStack var0) {
      MapId var1 = var0.is(Items.FILLED_MAP) ? (MapId)var0.get(DataComponents.MAP_ID) : null;
      return var1 == null ? -1 : var1.id();
   }

   private static void Z(Map<lm.y, lm.q> var0, lm.y var1, ItemStack var2) {
      var0.put(var1, new lm.q(G(var2), var2.getCount()));
      if (G(var2) >= 0) {
         S.put(G(var2), var2.copyWithCount(1));
      }
   }

   private static String X(Minecraft var0) {
      f9 var1 = f9.W();
      return var0.player != null && var0.player.containerMenu instanceof DispenserMenu var2 && var1.i() == var2.containerId ? var1.v() : null;
   }

   private static Map<lm.y, lm.q> e(Minecraft var0) {
      LinkedHashMap var1 = new LinkedHashMap();
      if (var0.player != null && var0.level != null) {
         for (int var2 = 0; var2 < var0.player.getInventory().getContainerSize(); var2++) {
            Z(var1, y(var2), var0.player.getInventory().getItem(var2));
         }

         Z(var1, Y(), var0.player.containerMenu.getCarried());
         String var7 = X(var0);
         if (var7 != null) {
            Map var3 = l.computeIfAbsent(var7, var0x -> new HashMap<>());

            for (int var4 = 0; var4 < 9; var4++) {
               ItemStack var5 = var0.player.containerMenu.getSlot(var4).getItem();
               Z(var1, new lm.y("stock", var7, var4), var5);
               if (G(var5) >= 0) {
                  var3.put(G(var5), var4);
               }
            }
         }

         for (Entity var10 : var0.level.entitiesForRendering()) {
            if (var10 instanceof ItemFrame || var10 instanceof ItemEntity) {
               lm.y var12 = I(var10);
               I.put(var12, var10);
               Z(var1, var12, var10 instanceof ItemFrame var6 ? var6.getItem() : ((ItemEntity)var10).getItem());
            }
         }

         for (Entry var11 : I.entrySet()) {
            if (((Entity)var11.getValue()).isRemoved()) {
               Z(var1, (lm.y)var11.getKey(), ItemStack.EMPTY);
            }
         }

         return var1;
      } else {
         return var1;
      }
   }

   public static void A() {
      d.O();
      l.clear();
      S.clear();
      I.clear();
      a.clear();
      p = Long.MIN_VALUE;
      G = Map.of();
      o.clear();
      C = -1;
      X = -1;
   }

   public static void F(Minecraft var0) {
      if (W != var0.level) {
         A();
         W = var0.level;
      }

      if (var0.player != null && var0.level != null && var0.level.getGameTime() != p) {
         p = var0.level.getGameTime();
         if (!d.p.isEmpty()) {
            d.E(e(var0));
         }

         a.keySet().retainAll(d.p);
         m();
      }
   }

   public static void n() {
      Minecraft var0 = Minecraft.getInstance();
      F(var0);
      if (var0.player != null && var0.level != null && !d.p.isEmpty()) {
         d.E(e(var0));
      }
   }

   public static boolean u(Minecraft var0) {
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

   public static boolean B(ug var0, int var1, int var2) {
      Minecraft var3 = Minecraft.getInstance();
      F(var3);
      String var4 = var0.A();
      if (var4 != null && var1 >= 0 && var3.player != null) {
         ItemStack var5 = var3.player.getInventory().getItem(var1);
         int var6 = G(var5);
         int var7 = var0.getTemplateIdForValue(var2);
         if (var6 >= 0 && var7 >= 0 && var6 != var7) {
            Map var8 = l.computeIfAbsent(var4, var0x -> new HashMap<>());

            for (int var9 = 1; var9 <= 10; var9++) {
               int var10 = var0.getTemplateIdForValue(var9);
               int var11 = var0.r(var9);
               if (var11 < 0) {
                  var11 = var0.containerIndexForValue(var9);
               }

               if (var10 >= 0 && var11 >= 0 && var11 < 9) {
                  var8.putIfAbsent(var10, var11);
               }
            }

            ItemStack var12 = var5.copyWithCount(1);
            var12.set(DataComponents.MAP_ID, new MapId(var7));
            S.putIfAbsent(var7, var12);
            d.O(var4, var6, var7, y(var1), e(var3));
            return true;
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   private static ItemStack C(lm.y var0, ItemStack var1) {
      lm.n var2 = d.u(var0, G(var1));
      if (var2 == null) {
         return null;
      }

      yg.d var3 = a.get(var2);
      if (var3 != null && ItemStack.matches(var3.actual(), var1)) {
         return var3.visual();
      }

      ItemStack var4 = var1.copy();
      var4.set(DataComponents.MAP_ID, new MapId(var2.a));
      a.put(var2, new yg.d(var1.copy(), var4));
      return var4;
   }

   public static ItemStack P(int var0) {
      Minecraft var1 = Minecraft.getInstance();
      if (var1.player != null && var0 >= 0 && var0 < var1.player.getInventory().getContainerSize() && !d.p.isEmpty()) {
         F(var1);
         if (var0 < 36) {
            M();
            return o.get(var0);
         } else {
            return C(y(var0), var1.player.getInventory().getItem(var0));
         }
      } else {
         return null;
      }
   }

   public static ItemStack W(String var0, ItemStack var1) {
      F(Minecraft.getInstance());

      for (lm.n var3 : d.p) {
         if (var3.T != null && !var3.B() && var3.w == G(var1)) {
            Entity var4 = I.get(var3.T);
            if (var3.T.kind().equals("inventory") && var0.equals("inventory[" + var3.T.slot() + "]")
               || var4 instanceof ItemFrame && var0.equals("item_frame[" + var4.getId() + "]")) {
               return C(var3.T, var1);
            }
         }
      }

      return var1;
   }

   public static ItemStack Z(ItemStack var0) {
      F(Minecraft.getInstance());
      ItemStack var1 = C(Y(), var0);
      return var1 == null ? var0 : var1;
   }

   public static ItemStack b(Entity var0, ItemStack var1) {
      if (d.p.isEmpty()) {
         return var1;
      }

      F(Minecraft.getInstance());
      ItemStack var2 = C(I(var0), var1);
      if (var2 == null && G(var1) >= 0) {
         n();
         var2 = C(I(var0), var1);
      }

      return var2 == null ? var1 : var2;
   }

   public static ItemStack j(Slot var0) {
      Minecraft var1 = Minecraft.getInstance();
      if (var1.player != null && var0 != null) {
         if (var0.container == var1.player.getInventory()) {
            return P(var0.getContainerSlot());
         }

         String var2 = X(var1);
         if (var2 != null && var0.index >= 0 && var0.index < 9 && !d.p.isEmpty()) {
            F(var1);
            Map var3 = l.getOrDefault(var2, Map.of());
            ItemStack var4 = var0.getItem();
            int var5 = G(var4);
            if (var5 < 0) {
               for (Entry var7 : var3.entrySet()) {
                  if ((Integer)var7.getValue() == var0.index && d.g(var2, (Integer)var7.getKey()) > 0) {
                     var5 = (Integer)var7.getKey();
                     break;
                  }
               }
            }

            int var9 = var3.getOrDefault(var5, -1) == var0.index ? d.g(var2, var5) : 0;
            if (var9 == 0) {
               return null;
            }

            ItemStack var10 = var4.isEmpty() ? S.get(var5) : var4;
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

   public static void T(int var0, int var1) {
      Minecraft var2 = Minecraft.getInstance();
      if (var2.player != null && var2.player.containerMenu.containerId == var0 && var1 >= 0 && var1 < 9) {
         String var3 = X(var2);
         if (var3 != null) {
            n();
            d.k(new lm.y("stock", var3, var1));
         }
      }
   }

   public static void n(int var0) {
      Minecraft var1 = Minecraft.getInstance();
      if (var1.player != null && var1.player.containerMenu.containerId == var0 && X(var1) != null) {
         n();
         d.w();
      }
   }

   private record d(ItemStack actual, ItemStack visual) {
   }
}
