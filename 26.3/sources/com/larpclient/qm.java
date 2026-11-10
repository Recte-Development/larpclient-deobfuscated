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

public final class qm {
   private static final fh L = new fh();
   private static final Map<String, Map<Integer, Integer>> r = new HashMap<>();
   private static final Map<Integer, ItemStack> o = new HashMap<>();
   private static final Map<fh.d, Entity> t = new HashMap<>();
   private static final Map<fh.j, qm.z> N = new IdentityHashMap<>();
   private static ClientLevel c;
   private static long F = Long.MIN_VALUE;
   private static Map<Integer, iq.c> j = Map.of();
   private static final Map<Integer, ItemStack> Q = new HashMap<>();
   private static boolean d;
   private static int h = -1;
   private static int q = -1;
   private static boolean U;

   private static Map<Integer, iq.c> N() {
      Minecraft var0 = Minecraft.getInstance();
      if (var0.player != null && !L.P.isEmpty()) {
         int[] var1 = new int[36];
         int[] var2 = new int[36];

         for (int var3 = 0; var3 < 36; var3++) {
            ItemStack var4 = var0.player.getInventory().getItem(var3);
            var1[var3] = N(var4);
            var2[var3] = var4.getCount();
         }

         ArrayList var6 = new ArrayList();

         for (fh.j var5 : L.P) {
            if (var5.P != null && var5.P.kind().equals("inventory") && var5.P.slot() < 36) {
               var6.add(new iq.x(var5.P.slot(), var5.r, var5.h, 1));
            }
         }

         Map var8 = iq.e(var1, var2, var6, j);
         if (!var8.equals(j)) {
            j = var8;
            Q.clear();
            var8.forEach((var1x, var2x) -> {
               if (var2x.count() == 0) {
                  Q.put(var1x, ItemStack.EMPTY);
               } else {
                  ItemStack var3x = var0.player.getInventory().getItem(var2x.portions().getFirst().source()).copyWithCount(var2x.count());
                  var3x.set(DataComponents.MAP_ID, new MapId(var2x.shown()));
                  Q.put(var1x, var3x);
               }
            });
         }

         return j;
      } else {
         j = Map.of();
         Q.clear();
         return j;
      }
   }

   public static int c(int var0) {
      if (U) {
         return var0;
      }

      h = var0;
      q = var0;
      iq.c var1 = N().get(var0);
      if (var1 != null && !var1.portions().isEmpty()) {
         iq.x var2 = var1.portions().getFirst();
         if (var2.source() >= 9) {
            return var0;
         }

         q = var2.source();
         L.c(D(var2.source()), var1.shown());
         return var2.source();
      } else {
         return var0;
      }
   }

   public static int z() {
      Minecraft var0 = Minecraft.getInstance();
      if (var0.player == null) {
         return -1;
      }

      int var1 = var0.player.getInventory().getSelectedSlot();
      if (!vy.a().isSwapActive() && !hh.R().U()) {
         if (h < 0 || h >= 9 || var1 != q) {
            h = var1;
            q = var1;
         }

         iq.c var2 = N().get(h);
         int var3 = var2 != null && !var2.portions().isEmpty() ? var2.portions().getFirst().source() : h;
         if (var3 < 9 && var3 != var1) {
            U = (boolean)1;

            try {
               var0.player.getInventory().setSelectedSlot(var3);
               q = var3;
            } finally {
               U = (boolean)0;
            }
         }

         return h;
      } else {
         return var1;
      }
   }

   public static boolean o() {
      iq.c var0 = N().get(z());
      return var0 == null || var0.count() > 0;
   }

   public static boolean Q(boolean var0) {
      if (!o()) {
         return false;
      }

      iq.c var1 = N().get(z());
      Minecraft var2 = Minecraft.getInstance();
      return !var0 || var1 == null || var1.portions().size() == 1 && var1.count() == var2.player.getMainHandItem().getCount();
   }

   public static boolean g(int var0) {
      iq.c var1 = N().get(var0);
      return var1 != null && var1.portions().size() > 1;
   }

   public static void p() {
      Minecraft var0 = Minecraft.getInstance();
      if (var0.player != null) {
         iq.c var1 = N().get(z());
         if (var1 != null) {
            L.c(D(var0.player.getInventory().getSelectedSlot()), var1.shown());
         }
      }
   }

   private static int h(int var0) {
      Minecraft var1 = Minecraft.getInstance();

      for (Slot var3 : var1.player.containerMenu.slots) {
         if (var3.container == var1.player.getInventory() && var3.getContainerSlot() == var0) {
            return var3.index;
         }
      }

      return -1;
   }

   private static void u(int var0, int var1, ContainerInput var2) {
      Minecraft var3 = Minecraft.getInstance();
      var3.gameMode.handleContainerInput(var3.player.containerMenu.containerId, var0, var1, var2, var3.player);
      I();
   }

   private static void z(iq.x var0, int var1) {
      Minecraft var2 = Minecraft.getInstance();
      int var3 = h(var0.source());
      int var4 = var2.player.getInventory().getItem(var0.source()).getCount();
      L.c(D(var0.source()), var0.shown());
      u(var3, 0, ContainerInput.PICKUP);

      for (int var5 = var1; var5 < var4; var5++) {
         int var6 = N(var2.player.containerMenu.getCarried());
         long var7 = L.P.stream().filter(var0x -> c().equals(var0x.P)).count();
         int var9 = var2.player.containerMenu.getCarried().getCount() > var7 ? var6 : var0.shown();
         if (var9 == var0.shown()) {
            for (fh.j var11 : L.P) {
               if (c().equals(var11.P) && var11.h != var0.shown()) {
                  var9 = var11.h;
                  break;
               }
            }
         }

         L.c(c(), var9);
         u(var3, 1, ContainerInput.PICKUP);
      }
   }

   public static void z(Minecraft var0) {
      if (var0.player != null
         && var0.gameMode != null
         && !L.P.isEmpty()
         && !d
         && !vy.a().isSwapActive()
         && !vy.a().blocksUserInteraction()
         && !wj.m().Q()
         && !q5.a().C()
         && !vy.a().isSwapFreezingPassThrough()
         && var0.player.containerMenu.getCarried().isEmpty()) {
         I();

         for (fh.j var2 : L.P) {
            if (var2.P != null && var2.P.kind().equals("inventory") && var2.P.slot() < 36) {
               int var3 = var2.P.slot();
               int var4 = var2.r;
               int var5 = var2.h;
               ItemStack var6 = var0.player.getInventory().getItem(var3);
               if (N(var6) == var4) {
                  int var7 = (int)L.P.stream().filter(var3x -> D(var3).equals(var3x.P) && var3x.r == var4 && var3x.h == var5).count();
                  if (var7 < var6.getCount()) {
                     int var8 = -1;

                     for (int var9 = 0; var9 < 36; var9++) {
                        if (var0.player.getInventory().getItem(var9).isEmpty() && h(var9) >= 0) {
                           var8 = var9;
                           break;
                        }
                     }

                     if (var8 >= 0 && h(var3) >= 0) {
                        d = (boolean)1;

                        try {
                           u(h(var3), 0, ContainerInput.PICKUP);

                           for (int var13 = 0; var13 < var7; var13++) {
                              L.c(c(), var5);
                              u(h(var8), 1, ContainerInput.PICKUP);
                           }

                           if (!var0.player.containerMenu.getCarried().isEmpty()) {
                              u(h(var3), 0, ContainerInput.PICKUP);
                           }
                        } finally {
                           d = (boolean)0;
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

   public static boolean j(int var0, int var1, int var2, ContainerInput var3) {
      Minecraft var4 = Minecraft.getInstance();
      if (!d && var4.player != null && !L.P.isEmpty() && !vy.a().isSwapActive() && !wj.m().Q() && !q5.a().C()) {
         AbstractContainerMenu var5 = var4.player.containerMenu;
         if (var5.containerId == var0 && var1 >= 0 && var1 < var5.slots.size()) {
            Slot var6 = var5.getSlot(var1);
            if (var6.container == var4.player.getInventory() && var6.getContainerSlot() >= 0 && var6.getContainerSlot() < 36) {
               I();
               iq.c var7 = N().get(var6.getContainerSlot());
               if (var7 == null) {
                  return false;
               }

               if (var3 != ContainerInput.PICKUP && var3 != ContainerInput.QUICK_MOVE && var3 != ContainerInput.THROW) {
                  return true;
               }

               d = (boolean)1;

               try {
                  if (!var5.getCarried().isEmpty()) {
                     if (var3 != ContainerInput.PICKUP) {
                        return true;
                     }

                     ItemStack var19 = h(var5.getCarried());
                     int var21 = -1;
                     if (var7.shown() == N(var19)) {
                        for (iq.x var25 : var7.portions()) {
                           if (ItemStack.isSameItemSameComponents(var4.player.getInventory().getItem(var25.source()), var5.getCarried())
                              && var4.player.getInventory().getItem(var25.source()).getCount() < var5.getCarried().getMaxStackSize()) {
                              var21 = h(var25.source());
                              break;
                           }
                        }
                     }

                     if (var21 < 0 && (var7.count() == 0 || var7.shown() == N(var19))) {
                        for (int var23 = 0; var23 < 36; var23++) {
                           if (var4.player.getInventory().getItem(var23).isEmpty()) {
                              var21 = h(var23);
                              break;
                           }
                        }
                     }

                     if (var21 < 0 && var7.shown() != N(var19) && !var7.portions().isEmpty()) {
                        iq.x var24 = var7.portions().getFirst();
                        ItemStack var26 = var4.player.getInventory().getItem(var24.source());
                        if (ItemStack.isSameItemSameComponents(var26, var5.getCarried())) {
                           return true;
                        }

                        if (var24.count() == var26.getCount()) {
                           var21 = h(var24.source());
                        }
                     }

                     if (var21 >= 0) {
                        u(var21, var2, var3);
                     }

                     return true;
                  } else {
                     if (var7.portions().isEmpty()) {
                        return true;
                     }

                     if (var3 == ContainerInput.QUICK_MOVE) {
                        for (iq.x var9 : var7.portions()) {
                           int var10 = var4.player.getInventory().getItem(var9.source()).getCount();
                           if (var10 == var9.count()) {
                              L.c(D(var9.source()), var9.shown());
                              u(h(var9.source()), 0, var3);
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
                                 z(var9, var9.count());
                                 u(var11, 0, ContainerInput.PICKUP);
                                 if (!var5.getCarried().isEmpty()) {
                                    u(h(var9.source()), 0, ContainerInput.PICKUP);
                                 }
                              }
                           }
                        }
                     } else {
                        iq.x var18 = var7.portions().getFirst();
                        int var20 = var3 == ContainerInput.THROW
                           ? (var2 == 0 ? 1 : var18.count())
                           : Math.min(var18.count(), var2 == 1 ? (var7.count() + 1) / 2 : var18.count());
                        z(var18, var20);
                        if (var3 == ContainerInput.THROW) {
                           u(-999, 0, ContainerInput.PICKUP);
                        }
                     }

                     return true;
                  }
               } finally {
                  d = (boolean)0;
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

   private static fh.d D(int var0) {
      return new fh.d("inventory", "", var0);
   }

   private static fh.d c() {
      return new fh.d("cursor", "", 0);
   }

   private static fh.d A(Entity var0) {
      return new fh.d("entity", var0.getStringUUID(), 0);
   }

   private static int N(ItemStack var0) {
      MapId var1 = var0.is(Items.FILLED_MAP) ? (MapId)var0.get(DataComponents.MAP_ID) : null;
      return var1 == null ? -1 : var1.id();
   }

   private static void h(Map<fh.d, fh.k> var0, fh.d var1, ItemStack var2) {
      var0.put(var1, new fh.k(N(var2), var2.getCount()));
      if (N(var2) >= 0) {
         o.put(N(var2), var2.copyWithCount(1));
      }
   }

   private static String o(Minecraft var0) {
      hk var1 = hk.e();
      return var0.player != null && var0.player.containerMenu instanceof DispenserMenu var2 && var1.v() == var2.containerId ? var1.v() : null;
   }

   private static Map<fh.d, fh.k> O(Minecraft var0) {
      LinkedHashMap var1 = new LinkedHashMap();
      if (var0.player != null && var0.level != null) {
         for (int var2 = 0; var2 < var0.player.getInventory().getContainerSize(); var2++) {
            h(var1, D(var2), var0.player.getInventory().getItem(var2));
         }

         h(var1, c(), var0.player.containerMenu.getCarried());
         String var7 = o(var0);
         if (var7 != null) {
            Map var3 = r.computeIfAbsent(var7, var0x -> new HashMap<>());

            for (int var4 = 0; var4 < 9; var4++) {
               ItemStack var5 = var0.player.containerMenu.getSlot(var4).getItem();
               h(var1, new fh.d("stock", var7, var4), var5);
               if (N(var5) >= 0) {
                  var3.put(N(var5), var4);
               }
            }
         }

         for (Entity var10 : var0.level.entitiesForRendering()) {
            if (var10 instanceof ItemFrame || var10 instanceof ItemEntity) {
               fh.d var12 = A(var10);
               t.put(var12, var10);
               h(var1, var12, var10 instanceof ItemFrame var6 ? var6.getItem() : ((ItemEntity)var10).getItem());
            }
         }

         for (Entry var11 : t.entrySet()) {
            if (((Entity)var11.getValue()).isRemoved()) {
               h(var1, (fh.d)var11.getKey(), ItemStack.EMPTY);
            }
         }

         return var1;
      } else {
         return var1;
      }
   }

   public static void X() {
      L.u();
      r.clear();
      o.clear();
      t.clear();
      N.clear();
      F = Long.MIN_VALUE;
      j = Map.of();
      Q.clear();
      h = -1;
      q = -1;
   }

   public static void p(Minecraft var0) {
      if (c != var0.level) {
         X();
         c = var0.level;
      }

      if (var0.player != null && var0.level != null && var0.level.getGameTime() != F) {
         F = var0.level.getGameTime();
         if (!L.P.isEmpty()) {
            L.m(O(var0));
         }

         N.keySet().retainAll(L.P);
         z();
      }
   }

   public static void I() {
      Minecraft var0 = Minecraft.getInstance();
      p(var0);
      if (var0.player != null && var0.level != null && !L.P.isEmpty()) {
         L.m(O(var0));
      }
   }

   public static boolean M(Minecraft var0) {
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

   public static boolean v(vy var0, int var1, int var2) {
      Minecraft var3 = Minecraft.getInstance();
      p(var3);
      String var4 = var0.w();
      if (var4 != null && var1 >= 0 && var3.player != null) {
         ItemStack var5 = var3.player.getInventory().getItem(var1);
         int var6 = N(var5);
         int var7 = var0.getTemplateIdForValue(var2);
         if (var6 >= 0 && var7 >= 0 && var6 != var7) {
            Map var8 = r.computeIfAbsent(var4, var0x -> new HashMap<>());

            for (int var9 = 1; var9 <= 10; var9++) {
               int var10 = var0.getTemplateIdForValue(var9);
               int var11 = var0.n(var9);
               if (var11 < 0) {
                  var11 = var0.containerIndexForValue(var9);
               }

               if (var10 >= 0 && var11 >= 0 && var11 < 9) {
                  var8.putIfAbsent(var10, var11);
               }
            }

            ItemStack var12 = var5.copyWithCount(1);
            var12.set(DataComponents.MAP_ID, new MapId(var7));
            o.putIfAbsent(var7, var12);
            L.D(var4, var6, var7, D(var1), O(var3));
            return true;
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   private static ItemStack u(fh.d var0, ItemStack var1) {
      fh.j var2 = L.i(var0, N(var1));
      if (var2 == null) {
         return null;
      }

      qm.z var3 = N.get(var2);
      if (var3 != null && ItemStack.matches(var3.actual(), var1)) {
         return var3.visual();
      }

      ItemStack var4 = var1.copy();
      var4.set(DataComponents.MAP_ID, new MapId(var2.h));
      N.put(var2, new qm.z(var1.copy(), var4));
      return var4;
   }

   public static ItemStack S(int var0) {
      Minecraft var1 = Minecraft.getInstance();
      if (var1.player != null && var0 >= 0 && var0 < var1.player.getInventory().getContainerSize() && !L.P.isEmpty()) {
         p(var1);
         if (var0 < 36) {
            N();
            return Q.get(var0);
         } else {
            return u(D(var0), var1.player.getInventory().getItem(var0));
         }
      } else {
         return null;
      }
   }

   public static ItemStack v(String var0, ItemStack var1) {
      p(Minecraft.getInstance());

      for (fh.j var3 : L.P) {
         if (var3.P != null && !var3.R() && var3.r == N(var1)) {
            Entity var4 = t.get(var3.P);
            if (var3.P.kind().equals("inventory") && var0.equals("inventory[" + var3.P.slot() + "]")
               || var4 instanceof ItemFrame && var0.equals("item_frame[" + var4.getId() + "]")) {
               return u(var3.P, var1);
            }
         }
      }

      return var1;
   }

   public static ItemStack h(ItemStack var0) {
      p(Minecraft.getInstance());
      ItemStack var1 = u(c(), var0);
      return var1 == null ? var0 : var1;
   }

   public static ItemStack m(Entity var0, ItemStack var1) {
      if (L.P.isEmpty()) {
         return var1;
      }

      p(Minecraft.getInstance());
      ItemStack var2 = u(A(var0), var1);
      if (var2 == null && N(var1) >= 0) {
         I();
         var2 = u(A(var0), var1);
      }

      return var2 == null ? var1 : var2;
   }

   public static ItemStack A(Slot var0) {
      Minecraft var1 = Minecraft.getInstance();
      if (var1.player != null && var0 != null) {
         if (var0.container == var1.player.getInventory()) {
            return S(var0.getContainerSlot());
         }

         String var2 = o(var1);
         if (var2 != null && var0.index >= 0 && var0.index < 9 && !L.P.isEmpty()) {
            p(var1);
            Map var3 = r.getOrDefault(var2, Map.of());
            ItemStack var4 = var0.getItem();
            int var5 = N(var4);
            if (var5 < 0) {
               for (Entry var7 : var3.entrySet()) {
                  if ((Integer)var7.getValue() == var0.index && L.v(var2, (Integer)var7.getKey()) > 0) {
                     var5 = (Integer)var7.getKey();
                     break;
                  }
               }
            }

            int var9 = var3.getOrDefault(var5, -1) == var0.index ? L.v(var2, var5) : 0;
            if (var9 == 0) {
               return null;
            }

            ItemStack var10 = var4.isEmpty() ? o.get(var5) : var4;
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

   public static void J(int var0, int var1) {
      Minecraft var2 = Minecraft.getInstance();
      if (var2.player != null && var2.player.containerMenu.containerId == var0 && var1 >= 0 && var1 < 9) {
         String var3 = o(var2);
         if (var3 != null) {
            I();
            L.j(new fh.d("stock", var3, var1));
         }
      }
   }

   public static void M(int var0) {
      Minecraft var1 = Minecraft.getInstance();
      if (var1.player != null && var1.player.containerMenu.containerId == var0 && o(var1) != null) {
         I();
         L.b();
      }
   }

   private record z(ItemStack actual, ItemStack visual) {
   }
}
