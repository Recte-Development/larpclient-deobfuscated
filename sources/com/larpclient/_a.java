package com.larpclient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.Map.Entry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.network.protocol.game.ClientboundTakeItemEntityPacket;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerInput;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

public final class _a {
   private static ClientLevel e;
   private static final Map<UUID, ItemStack> N = new HashMap<>();
   private static final List<ItemEntity> n = new ArrayList<>();
   private static final Map<Integer, ItemStack> w = new HashMap<>();
   private static ItemStack b = ItemStack.EMPTY;
   private static AbstractContainerMenu P;
   private static int A = -1900000000;

   private static void s(Minecraft var0) {
      if (e != var0.level) {
         if (var0.player != null) {
            for (Entry var2 : w.entrySet()) {
               if (var0.player.getInventory().getItem((Integer)var2.getKey()) == var2.getValue()) {
                  var0.player.getInventory().setItem((Integer)var2.getKey(), ItemStack.EMPTY);
               }
            }
         }

         if (P != null && P.getCarried() == b) {
            P.setCarried(ItemStack.EMPTY);
         }

         for (ItemEntity var4 : n) {
            var4.discard();
         }

         N.clear();
         n.clear();
         w.clear();
         b = ItemStack.EMPTY;
         P = null;
         e = var0.level;
      }
   }

   public static ItemStack q(ItemFrame var0) {
      Minecraft var1 = Minecraft.getInstance();
      s(var1);
      return var0.level() == e ? N.get(var0.getUUID()) : null;
   }

   public static void v(ItemFrame var0) {
      N.remove(var0.getUUID());
   }

   public static void O(Minecraft var0, ItemFrame var1) {
      s(var0);
      ItemStack var2 = var1.getItem().copy();
      N.put(var1.getUUID(), ItemStack.EMPTY);
      if (!var2.isEmpty()) {
         r(var0, var2.copyWithCount(1), new Vec3(var1.getX(), var1.getY(), var1.getZ()).add(0.0, 0.15, 0.0));
      }
   }

   private static void r(Minecraft var0, ItemStack var1, Vec3 var2) {
      ItemEntity var3 = new ItemEntity(var0.level, var2.x, var2.y, var2.z, var1.copy());
      var3.setId(A--);
      var3.setDefaultPickUpDelay();
      var3.setUnlimitedLifetime();
      var0.level.addEntity(var3);
      n.add(var3);
   }

   private static int F(Minecraft var0, int var1, int var2) {
      for (int var3 = var1; var3 < var2; var3++) {
         if (var0.player.getInventory().getItem(var3).isEmpty() && !w.containsKey(var3)) {
            return var3;
         }
      }

      return -1;
   }

   private static void s(Minecraft var0, int var1, ItemStack var2) {
      w.put(var1, var2);
      var0.player.getInventory().setItem(var1, var2);
   }

   private static int O(ItemStack var0, ItemStack var1, int var2) {
      if (!ItemStack.isSameItemSameComponents(var0, var1)) {
         return 0;
      }

      int var3 = Math.min(var2, Math.min(var1.getCount(), Math.max(0, var0.getMaxStackSize() - var0.getCount())));
      var0.grow(var3);
      var1.shrink(var3);
      return var3;
   }

   private static void d(Minecraft var0, ItemStack var1, int var2, int var3) {
      ItemStack var4 = w.get(40);
      if (var4 != null && var4 != var1 && var0.player.getInventory().getItem(40) == var4) {
         O(var4, var1, var1.getCount());
      }

      for (int var5 = var2; var5 < var3 && !var1.isEmpty(); var5++) {
         ItemStack var6 = w.get(var5);
         if (var6 != null && var0.player.getInventory().getItem(var5) == var6) {
            O(var6, var1, var1.getCount());
         }
      }

      while (!var1.isEmpty()) {
         int var7 = F(var0, var2, var3);
         if (var7 < 0) {
            return;
         }

         int var8 = Math.min(var1.getCount(), var1.getMaxStackSize());
         s(var0, var7, var1.copyWithCount(var8));
         var1.shrink(var8);
      }
   }

   private static void J(Minecraft var0, int var1, ItemStack var2) {
      if (var2.isEmpty()) {
         w.remove(var1);
         if (var0.player.getInventory().getItem(var1) == var2) {
            var0.player.getInventory().setItem(var1, ItemStack.EMPTY);
         }
      }
   }

   public static boolean M(Minecraft var0, InteractionHand var1) {
      return var0.player != null && w.containsKey(var1 == InteractionHand.OFF_HAND ? 40 : var0.player.getInventory().getSelectedSlot());
   }

   public static void z(Minecraft var0) {
      s(var0);
      if (var0.level != null && var0.player != null) {
         if (!b.isEmpty() && P != var0.player.containerMenu) {
            X(var0);
         }

         for (Entry var2 : new ArrayList<>(w.entrySet())) {
            int var3 = (Integer)var2.getKey();
            ItemStack var4 = (ItemStack)var2.getValue();
            ItemStack var5 = var0.player.getInventory().getItem(var3);
            if (var5 != var4) {
               if (var5.isEmpty()) {
                  var0.player.getInventory().setItem(var3, var4);
               } else {
                  w.remove(var3);
                  d(var0, var4, 0, 36);
                  if (!var4.isEmpty()) {
                     r(var0, var4, new Vec3(var0.player.getX(), var0.player.getY(), var0.player.getZ()));
                  }
               }
            }
         }

         if (!b.isEmpty() && P == var0.player.containerMenu) {
            if (P.getCarried().isEmpty()) {
               P.setCarried(b);
            } else if (P.getCarried() != b) {
               X(var0);
            }
         }

         Iterator var6 = n.iterator();

         while (var6.hasNext()) {
            ItemEntity var7 = (ItemEntity)var6.next();
            if (var7.isRemoved()) {
               var6.remove();
            } else if (!var7.hasPickUpDelay() && var7.getBoundingBox().intersects(var0.player.getBoundingBox().inflate(1.0, 0.5, 1.0))) {
               ItemStack var8 = var7.getItem().copy();
               int var9 = var8.getCount();
               d(var0, var8, 0, 36);
               int var10 = var9 - var8.getCount();
               if (var10 != 0) {
                  var0.getConnection().handleTakeItemEntity(new ClientboundTakeItemEntityPacket(var7.getId(), var0.player.getId(), var10));
                  if (var7.isRemoved() || var7.getItem().isEmpty()) {
                     var7.discard();
                     var6.remove();
                  }
               }
            }
         }
      }
   }

   public static boolean D(Minecraft var0, ItemFrame var1, InteractionHand var2) {
      s(var0);
      if (!var1.getItem().isEmpty()) {
         if (!N.containsKey(var1.getUUID()) && !M(var0, var2)) {
            return false;
         }

         var1.setRotation((var1.getRotation() + 1) % 8);
         var1.playSound(var1.getRotateItemSound(), 1.0F, 1.0F);
         return true;
      } else {
         if (!M(var0, var2)) {
            return N.containsKey(var1.getUUID());
         }

         int var3 = var2 == InteractionHand.OFF_HAND ? 40 : var0.player.getInventory().getSelectedSlot();
         ItemStack var4 = w.get(var3);
         N.put(var1.getUUID(), var4.copyWithCount(1));
         if (var4.getCount() == 1) {
            w.remove(var3);
            if (var0.player.getInventory().getItem(var3) == var4) {
               var0.player.getInventory().setItem(var3, ItemStack.EMPTY);
            }
         } else {
            var4.shrink(1);
         }

         var1.playSound(SoundEvents.ITEM_FRAME_ADD_ITEM, 1.0F, 1.0F);
         return true;
      }
   }

   public static boolean Y(Minecraft var0, ItemFrame var1) {
      s(var0);
      N.remove(var1.getUUID());
      return false;
   }

   public static boolean y(Minecraft var0) {
      if (var0.player == null) {
         return false;
      }

      int var1 = var0.player.getInventory().getSelectedSlot();
      ItemStack var2 = w.get(var1);
      ItemStack var3 = w.get(40);
      if (var2 == null && var3 == null) {
         return false;
      }

      if ((var2 != null || var0.player.getMainHandItem().isEmpty()) && (var3 != null || var0.player.getOffhandItem().isEmpty())) {
         w.remove(var1);
         w.remove(40);
         var0.player.getInventory().setItem(var1, ItemStack.EMPTY);
         var0.player.getInventory().setItem(40, ItemStack.EMPTY);
         if (var2 != null) {
            s(var0, 40, var2);
         }

         if (var3 != null) {
            s(var0, var1, var3);
         }

         return true;
      } else {
         return true;
      }
   }

   public static boolean F(Minecraft var0, boolean var1) {
      if (!M(var0, InteractionHand.MAIN_HAND)) {
         return false;
      }

      int var2 = var0.player.getInventory().getSelectedSlot();
      ItemStack var3 = w.get(var2);
      ItemStack var4 = var3.split(var1 ? var3.getCount() : 1);
      J(var0, var2, var3);
      r(var0, var4, var0.player.getEyePosition().add(var0.player.getViewVector(1.0F).scale(0.3)));
      return true;
   }

   public static void X(Minecraft var0) {
      if (!b.isEmpty() && var0.player != null) {
         if (P != null && P.getCarried() == b) {
            P.setCarried(ItemStack.EMPTY);
         }

         d(var0, b, 0, 36);
         if (!b.isEmpty() && var0.level != null) {
            r(var0, b, new Vec3(var0.player.getX(), var0.player.getY(), var0.player.getZ()));
         }

         b = ItemStack.EMPTY;
         P = null;
      }
   }

   public static boolean E(Minecraft var0, int var1, int var2, ContainerInput var3) {
      if (var0.player == null) {
         return false;
      }

      AbstractContainerMenu var4 = var0.player.containerMenu;
      Slot var5 = var1 >= 0 && var1 < var4.slots.size() ? var4.getSlot(var1) : null;
      int var6 = var5 != null && var5.container == var0.player.getInventory() ? var5.getContainerSlot() : -1;
      ItemStack var7 = w.get(var6);
      if (var7 != null && var5.getItem() != var7) {
         z(var0);
         return true;
      }

      int var8 = var3 == ContainerInput.SWAP && w.containsKey(var2) ? 1 : 0;
      if (var7 == null && b.isEmpty() && var8 == 0) {
         ItemStack var14 = var3 == ContainerInput.PICKUP_ALL ? var4.getCarried() : (var5 == null ? ItemStack.EMPTY : var5.getItem());
         if (var3 == ContainerInput.PICKUP_ALL || var3 == ContainerInput.QUICK_MOVE) {
            for (ItemStack var11 : w.values()) {
               if (ItemStack.isSameItemSameComponents(var14, var11)) {
                  return true;
               }
            }
         }

         return false;
      } else {
         if (b.isEmpty() && !var4.getCarried().isEmpty()) {
            return true;
         }

         if (var3 == ContainerInput.PICKUP) {
            if (b.isEmpty()) {
               if (var7 != null && var4.getCarried().isEmpty()) {
                  b = var7.split(var2 == 1 ? (var7.getCount() + 1) / 2 : var7.getCount());
                  J(var0, var6, var7);
               }
            } else if (var6 >= 0 && (var6 < 36 || var6 == 40) && (var5.getItem().isEmpty() || var7 != null)) {
               if (var7 == null) {
                  s(var0, var6, b.split(var2 == 1 ? 1 : b.getCount()));
               } else if (ItemStack.isSameItemSameComponents(var7, b)) {
                  O(var7, b, var2 == 1 ? 1 : b.getCount());
               } else {
                  ItemStack var9 = var7;
                  s(var0, var6, b);
                  b = var9;
               }
            } else if (var1 == -999) {
               r(var0, b.split(var2 == 1 ? 1 : b.getCount()), var0.player.getEyePosition());
            }

            if (b.isEmpty()) {
               b = ItemStack.EMPTY;
            }

            var4.setCarried(b);
            P = b.isEmpty() ? null : var4;
         } else if (var3 == ContainerInput.QUICK_MOVE && var7 != null) {
            d(var0, var7, var6 < 9 ? 9 : 0, var6 >= 9 && var6 != 40 ? 9 : 36);
            J(var0, var6, var7);
         } else if (var3 == ContainerInput.SWAP && var6 >= 0 && (var6 < 36 || var6 == 40) && var2 >= 0 && (var2 < 9 || var2 == 40)) {
            ItemStack var13 = w.get(var2);
            if ((var7 != null || var5.getItem().isEmpty()) && (var13 != null || var0.player.getInventory().getItem(var2).isEmpty())) {
               w.remove(var6);
               w.remove(var2);
               var0.player.getInventory().setItem(var6, ItemStack.EMPTY);
               var0.player.getInventory().setItem(var2, ItemStack.EMPTY);
               if (var7 != null) {
                  s(var0, var2, var7);
               }

               if (var13 != null) {
                  s(var0, var6, var13);
               }
            }
         } else if (var3 == ContainerInput.THROW && var7 != null) {
            ItemStack var12 = var7.split(var2 == 0 ? 1 : var7.getCount());
            J(var0, var6, var7);
            r(var0, var12, var0.player.getEyePosition());
         }

         return true;
      }
   }
}
