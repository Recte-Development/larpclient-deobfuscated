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

public final class w5 {
   private static final zz Z = new zz();
   private static final Map<String, Map<Integer, Integer>> M = new HashMap<>();
   private static final Map<Integer, ItemStack> l = new HashMap<>();
   private static final Map<zz.j, Entity> b = new HashMap<>();
   private static final Map<zz.o, w5.k> S = new IdentityHashMap<>();
   private static ClientLevel j;
   private static long N = Long.MIN_VALUE;
   private static Map<Integer, wn.s> K = Map.of();
   private static final Map<Integer, ItemStack> m = new HashMap<>();
   private static boolean A;
   private static int O = -1;
   private static int R = -1;
   private static boolean s;

   private static Map<Integer, wn.s> Q() {
      Minecraft var0 = Minecraft.getInstance();
      if (var0.player != null && !Z.u.isEmpty()) {
         int[] var1 = new int[36];
         int[] var2 = new int[36];

         for (int var3 = 0; var3 < 36; var3++) {
            ItemStack var4 = var0.player.getInventory().getItem(var3);
            var1[var3] = j(var4);
            var2[var3] = var4.getCount();
         }

         ArrayList var6 = new ArrayList();

         for (zz.o var5 : Z.u) {
            if (var5.X != null && var5.X.kind().equals("inventory") && var5.X.slot() < 36) {
               var6.add(new wn.z(var5.X.slot(), var5.K, var5.G, 1));
            }
         }

         Map var8 = wn.r(var1, var2, var6, K);
         if (!var8.equals(K)) {
            K = var8;
            m.clear();
            var8.forEach((var1x, var2x) -> {
               if (var2x.count() == 0) {
                  m.put(var1x, ItemStack.EMPTY);
               } else {
                  ItemStack var3x = var0.player.getInventory().getItem(var2x.portions().getFirst().source()).copyWithCount(var2x.count());
                  var3x.set(DataComponents.MAP_ID, new MapId(var2x.shown()));
                  m.put(var1x, var3x);
               }
            });
         }

         return K;
      } else {
         K = Map.of();
         m.clear();
         return K;
      }
   }

   public static int h(int var0) {
      if (s) {
         return var0;
      }

      O = var0;
      R = var0;
      wn.s var1 = Q().get(var0);
      if (var1 != null && !var1.portions().isEmpty()) {
         wn.z var2 = var1.portions().getFirst();
         if (var2.source() >= 9) {
            return var0;
         }

         R = var2.source();
         Z.i(f(var2.source()), var1.shown());
         return var2.source();
      } else {
         return var0;
      }
   }

   public static int J() {
      Minecraft var0 = Minecraft.getInstance();
      if (var0.player == null) {
         return -1;
      }

      int var1 = var0.player.getInventory().getSelectedSlot();
      if (!ze.c().isSwapActive() && !pj.U().c()) {
         if (O < 0 || O >= 9 || var1 != R) {
            O = var1;
            R = var1;
         }

         wn.s var2 = Q().get(O);
         int var3 = var2 != null && !var2.portions().isEmpty() ? var2.portions().getFirst().source() : O;
         if (var3 < 9 && var3 != var1) {
            s = (boolean)1;

            try {
               var0.player.getInventory().setSelectedSlot(var3);
               R = var3;
            } finally {
               s = (boolean)0;
            }
         }

         return O;
      } else {
         return var1;
      }
   }

   public static boolean O() {
      wn.s var0 = Q().get(J());
      return var0 == null || var0.count() > 0;
   }

   public static boolean k(boolean var0) {
      if (!O()) {
         return false;
      }

      wn.s var1 = Q().get(J());
      Minecraft var2 = Minecraft.getInstance();
      return !var0 || var1 == null || var1.portions().size() == 1 && var1.count() == var2.player.getMainHandItem().getCount();
   }

   public static boolean i(int var0) {
      wn.s var1 = Q().get(var0);
      return var1 != null && var1.portions().size() > 1;
   }

   public static void Z() {
      Minecraft var0 = Minecraft.getInstance();
      if (var0.player != null) {
         wn.s var1 = Q().get(J());
         if (var1 != null) {
            Z.i(f(var0.player.getInventory().getSelectedSlot()), var1.shown());
         }
      }
   }

   private static int d(int var0) {
      Minecraft var1 = Minecraft.getInstance();

      for (Slot var3 : var1.player.containerMenu.slots) {
         if (var3.container == var1.player.getInventory() && var3.getContainerSlot() == var0) {
            return var3.index;
         }
      }

      return -1;
   }

   private static void R(int var0, int var1, ContainerInput var2) {
      Minecraft var3 = Minecraft.getInstance();
      var3.gameMode.handleContainerInput(var3.player.containerMenu.containerId, var0, var1, var2, var3.player);
      P();
   }

   private static void x(wn.z var0, int var1) {
      Minecraft var2 = Minecraft.getInstance();
      int var3 = d(var0.source());
      int var4 = var2.player.getInventory().getItem(var0.source()).getCount();
      Z.i(f(var0.source()), var0.shown());
      R(var3, 0, ContainerInput.PICKUP);

      for (int var5 = var1; var5 < var4; var5++) {
         int var6 = j(var2.player.containerMenu.getCarried());
         long var7 = Z.u.stream().filter(var0x -> S().equals(var0x.X)).count();
         int var9 = var2.player.containerMenu.getCarried().getCount() > var7 ? var6 : var0.shown();
         if (var9 == var0.shown()) {
            for (zz.o var11 : Z.u) {
               if (S().equals(var11.X) && var11.G != var0.shown()) {
                  var9 = var11.G;
                  break;
               }
            }
         }

         Z.i(S(), var9);
         R(var3, 1, ContainerInput.PICKUP);
      }
   }

   public static void T(Minecraft var0) {
      if (var0.player != null
         && var0.gameMode != null
         && !Z.u.isEmpty()
         && !A
         && !ze.c().isSwapActive()
         && !ze.c().blocksUserInteraction()
         && !c6.K().j()
         && !kq.I().j()
         && !ze.c().isSwapFreezingPassThrough()
         && var0.player.containerMenu.getCarried().isEmpty()) {
         P();

         for (zz.o var2 : Z.u) {
            if (var2.X != null && var2.X.kind().equals("inventory") && var2.X.slot() < 36) {
               int var3 = var2.X.slot();
               int var4 = var2.K;
               int var5 = var2.G;
               ItemStack var6 = var0.player.getInventory().getItem(var3);
               if (j(var6) == var4) {
                  int var7 = (int)Z.u.stream().filter(var3x -> f(var3).equals(var3x.X) && var3x.K == var4 && var3x.G == var5).count();
                  if (var7 < var6.getCount()) {
                     int var8 = -1;

                     for (int var9 = 0; var9 < 36; var9++) {
                        if (var0.player.getInventory().getItem(var9).isEmpty() && d(var9) >= 0) {
                           var8 = var9;
                           break;
                        }
                     }

                     if (var8 >= 0 && d(var3) >= 0) {
                        A = (boolean)1;

                        try {
                           R(d(var3), 0, ContainerInput.PICKUP);

                           for (int var13 = 0; var13 < var7; var13++) {
                              Z.i(S(), var5);
                              R(d(var8), 1, ContainerInput.PICKUP);
                           }

                           if (!var0.player.containerMenu.getCarried().isEmpty()) {
                              R(d(var3), 0, ContainerInput.PICKUP);
                           }
                        } finally {
                           A = (boolean)0;
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
      if (!A && var4.player != null && !Z.u.isEmpty() && !ze.c().isSwapActive() && !c6.K().j() && !kq.I().j()) {
         AbstractContainerMenu var5 = var4.player.containerMenu;
         if (var5.containerId == var0 && var1 >= 0 && var1 < var5.slots.size()) {
            Slot var6 = var5.getSlot(var1);
            if (var6.container == var4.player.getInventory() && var6.getContainerSlot() >= 0 && var6.getContainerSlot() < 36) {
               P();
               wn.s var7 = Q().get(var6.getContainerSlot());
               if (var7 == null) {
                  return false;
               }

               if (var3 != ContainerInput.PICKUP && var3 != ContainerInput.QUICK_MOVE && var3 != ContainerInput.THROW) {
                  return true;
               }

               A = (boolean)1;

               try {
                  if (!var5.getCarried().isEmpty()) {
                     if (var3 != ContainerInput.PICKUP) {
                        return true;
                     }

                     ItemStack var19 = u(var5.getCarried());
                     int var21 = -1;
                     if (var7.shown() == j(var19)) {
                        for (wn.z var25 : var7.portions()) {
                           if (ItemStack.isSameItemSameComponents(var4.player.getInventory().getItem(var25.source()), var5.getCarried())
                              && var4.player.getInventory().getItem(var25.source()).getCount() < var5.getCarried().getMaxStackSize()) {
                              var21 = d(var25.source());
                              break;
                           }
                        }
                     }

                     if (var21 < 0 && (var7.count() == 0 || var7.shown() == j(var19))) {
                        for (int var23 = 0; var23 < 36; var23++) {
                           if (var4.player.getInventory().getItem(var23).isEmpty()) {
                              var21 = d(var23);
                              break;
                           }
                        }
                     }

                     if (var21 < 0 && var7.shown() != j(var19) && !var7.portions().isEmpty()) {
                        wn.z var24 = var7.portions().getFirst();
                        ItemStack var26 = var4.player.getInventory().getItem(var24.source());
                        if (ItemStack.isSameItemSameComponents(var26, var5.getCarried())) {
                           return true;
                        }

                        if (var24.count() == var26.getCount()) {
                           var21 = d(var24.source());
                        }
                     }

                     if (var21 >= 0) {
                        R(var21, var2, var3);
                     }

                     return true;
                  } else {
                     if (var7.portions().isEmpty()) {
                        return true;
                     }

                     if (var3 == ContainerInput.QUICK_MOVE) {
                        for (wn.z var9 : var7.portions()) {
                           int var10 = var4.player.getInventory().getItem(var9.source()).getCount();
                           if (var10 == var9.count()) {
                              Z.i(f(var9.source()), var9.shown());
                              R(d(var9.source()), 0, var3);
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
                                 x(var9, var9.count());
                                 R(var11, 0, ContainerInput.PICKUP);
                                 if (!var5.getCarried().isEmpty()) {
                                    R(d(var9.source()), 0, ContainerInput.PICKUP);
                                 }
                              }
                           }
                        }
                     } else {
                        wn.z var18 = var7.portions().getFirst();
                        int var20 = var3 == ContainerInput.THROW
                           ? (var2 == 0 ? 1 : var18.count())
                           : Math.min(var18.count(), var2 == 1 ? (var7.count() + 1) / 2 : var18.count());
                        x(var18, var20);
                        if (var3 == ContainerInput.THROW) {
                           R(-999, 0, ContainerInput.PICKUP);
                        }
                     }

                     return true;
                  }
               } finally {
                  A = (boolean)0;
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

   private static zz.j f(int var0) {
      return new zz.j("inventory", "", var0);
   }

   private static zz.j S() {
      return new zz.j("cursor", "", 0);
   }

   private static zz.j x(Entity var0) {
      return new zz.j("entity", var0.getStringUUID(), 0);
   }

   private static int j(ItemStack var0) {
      MapId var1 = var0.is(Items.FILLED_MAP) ? (MapId)var0.get(DataComponents.MAP_ID) : null;
      return var1 == null ? -1 : var1.id();
   }

   private static void o(Map<zz.j, zz.d> var0, zz.j var1, ItemStack var2) {
      var0.put(var1, new zz.d(j(var2), var2.getCount()));
      if (j(var2) >= 0) {
         l.put(j(var2), var2.copyWithCount(1));
      }
   }

   private static String D(Minecraft var0) {
      ep var1 = ep.H();
      return var0.player != null && var0.player.containerMenu instanceof DispenserMenu var2 && var1.V() == var2.containerId ? var1.b() : null;
   }

   private static Map<zz.j, zz.d> X(Minecraft var0) {
      LinkedHashMap var1 = new LinkedHashMap();
      if (var0.player != null && var0.level != null) {
         for (int var2 = 0; var2 < var0.player.getInventory().getContainerSize(); var2++) {
            o(var1, f(var2), var0.player.getInventory().getItem(var2));
         }

         o(var1, S(), var0.player.containerMenu.getCarried());
         String var7 = D(var0);
         if (var7 != null) {
            Map var3 = M.computeIfAbsent(var7, var0x -> new HashMap<>());

            for (int var4 = 0; var4 < 9; var4++) {
               ItemStack var5 = var0.player.containerMenu.getSlot(var4).getItem();
               o(var1, new zz.j("stock", var7, var4), var5);
               if (j(var5) >= 0) {
                  var3.put(j(var5), var4);
               }
            }
         }

         for (Entity var10 : var0.level.entitiesForRendering()) {
            if (var10 instanceof ItemFrame || var10 instanceof ItemEntity) {
               zz.j var12 = x(var10);
               b.put(var12, var10);
               o(var1, var12, var10 instanceof ItemFrame var6 ? var6.getItem() : ((ItemEntity)var10).getItem());
            }
         }

         for (Entry var11 : b.entrySet()) {
            if (((Entity)var11.getValue()).isRemoved()) {
               o(var1, (zz.j)var11.getKey(), ItemStack.EMPTY);
            }
         }

         return var1;
      } else {
         return var1;
      }
   }

   public static void d() {
      Z.n();
      M.clear();
      l.clear();
      b.clear();
      S.clear();
      N = Long.MIN_VALUE;
      K = Map.of();
      m.clear();
      O = -1;
      R = -1;
   }

   public static void A(Minecraft var0) {
      if (j != var0.level) {
         d();
         j = var0.level;
      }

      if (var0.player != null && var0.level != null && var0.level.getGameTime() != N) {
         N = var0.level.getGameTime();
         if (!Z.u.isEmpty()) {
            Z.g(X(var0));
         }

         S.keySet().retainAll(Z.u);
         J();
      }
   }

   public static void P() {
      Minecraft var0 = Minecraft.getInstance();
      A(var0);
      if (var0.player != null && var0.level != null && !Z.u.isEmpty()) {
         Z.g(X(var0));
      }
   }

   public static boolean h(Minecraft var0) {
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

   public static boolean J(ze var0, int var1, int var2) {
      Minecraft var3 = Minecraft.getInstance();
      A(var3);
      String var4 = var0.a();
      if (var4 != null && var1 >= 0 && var3.player != null) {
         ItemStack var5 = var3.player.getInventory().getItem(var1);
         int var6 = j(var5);
         int var7 = var0.getTemplateIdForValue(var2);
         if (var6 >= 0 && var7 >= 0 && var6 != var7) {
            Map var8 = M.computeIfAbsent(var4, var0x -> new HashMap<>());

            for (int var9 = 1; var9 <= 10; var9++) {
               int var10 = var0.getTemplateIdForValue(var9);
               int var11 = var0.K(var9);
               if (var11 < 0) {
                  var11 = var0.containerIndexForValue(var9);
               }

               if (var10 >= 0 && var11 >= 0 && var11 < 9) {
                  var8.putIfAbsent(var10, var11);
               }
            }

            ItemStack var12 = var5.copyWithCount(1);
            var12.set(DataComponents.MAP_ID, new MapId(var7));
            l.putIfAbsent(var7, var12);
            Z.j(var4, var6, var7, f(var1), X(var3));
            return true;
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   private static ItemStack n(zz.j var0, ItemStack var1) {
      zz.o var2 = Z.b(var0, j(var1));
      if (var2 == null) {
         return null;
      }

      w5.k var3 = S.get(var2);
      if (var3 != null && ItemStack.matches(var3.actual(), var1)) {
         return var3.visual();
      }

      ItemStack var4 = var1.copy();
      var4.set(DataComponents.MAP_ID, new MapId(var2.G));
      S.put(var2, new w5.k(var1.copy(), var4));
      return var4;
   }

   public static ItemStack Q(int var0) {
      Minecraft var1 = Minecraft.getInstance();
      if (var1.player != null && var0 >= 0 && var0 < var1.player.getInventory().getContainerSize() && !Z.u.isEmpty()) {
         A(var1);
         if (var0 < 36) {
            Q();
            return m.get(var0);
         } else {
            return n(f(var0), var1.player.getInventory().getItem(var0));
         }
      } else {
         return null;
      }
   }

   public static ItemStack V(String var0, ItemStack var1) {
      A(Minecraft.getInstance());

      for (zz.o var3 : Z.u) {
         if (var3.X != null && !var3.T() && var3.K == j(var1)) {
            Entity var4 = b.get(var3.X);
            if (var3.X.kind().equals("inventory") && var0.equals("inventory[" + var3.X.slot() + "]")
               || var4 instanceof ItemFrame && var0.equals("item_frame[" + var4.getId() + "]")) {
               return n(var3.X, var1);
            }
         }
      }

      return var1;
   }

   public static ItemStack u(ItemStack var0) {
      A(Minecraft.getInstance());
      ItemStack var1 = n(S(), var0);
      return var1 == null ? var0 : var1;
   }

   public static ItemStack q(Entity var0, ItemStack var1) {
      if (Z.u.isEmpty()) {
         return var1;
      }

      A(Minecraft.getInstance());
      ItemStack var2 = n(x(var0), var1);
      if (var2 == null && j(var1) >= 0) {
         P();
         var2 = n(x(var0), var1);
      }

      return var2 == null ? var1 : var2;
   }

   public static ItemStack c(Slot var0) {
      Minecraft var1 = Minecraft.getInstance();
      if (var1.player != null && var0 != null) {
         if (var0.container == var1.player.getInventory()) {
            return Q(var0.getContainerSlot());
         }

         String var2 = D(var1);
         if (var2 != null && var0.index >= 0 && var0.index < 9 && !Z.u.isEmpty()) {
            A(var1);
            Map var3 = M.getOrDefault(var2, Map.of());
            ItemStack var4 = var0.getItem();
            int var5 = j(var4);
            if (var5 < 0) {
               for (Entry var7 : var3.entrySet()) {
                  if ((Integer)var7.getValue() == var0.index && Z.a(var2, (Integer)var7.getKey()) > 0) {
                     var5 = (Integer)var7.getKey();
                     break;
                  }
               }
            }

            int var9 = var3.getOrDefault(var5, -1) == var0.index ? Z.a(var2, var5) : 0;
            if (var9 == 0) {
               return null;
            }

            ItemStack var10 = var4.isEmpty() ? l.get(var5) : var4;
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

   public static void i(int var0, int var1) {
      Minecraft var2 = Minecraft.getInstance();
      if (var2.player != null && var2.player.containerMenu.containerId == var0 && var1 >= 0 && var1 < 9) {
         String var3 = D(var2);
         if (var3 != null) {
            P();
            Z.k(new zz.j("stock", var3, var1));
         }
      }
   }

   public static void j(int var0) {
      Minecraft var1 = Minecraft.getInstance();
      if (var1.player != null && var1.player.containerMenu.containerId == var0 && D(var1) != null) {
         P();
         Z.z();
      }
   }

   private record k(ItemStack actual, ItemStack visual) {
   }
}
