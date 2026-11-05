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

public final class xs {
   private static ClientLevel m;
   private static final Map<UUID, ItemStack> O = new HashMap<>();
   private static final List<ItemEntity> F = new ArrayList<>();
   private static final Map<Integer, ItemStack> W = new HashMap<>();
   private static ItemStack B = ItemStack.EMPTY;
   private static AbstractContainerMenu o;
   private static int w = -1900000000;

   private static void h(Minecraft var0) {
      if (m != var0.level) {
         if (var0.player != null) {
            for (Entry var2 : W.entrySet()) {
               if (var0.player.getInventory().getItem((Integer)var2.getKey()) == var2.getValue()) {
                  var0.player.getInventory().setItem((Integer)var2.getKey(), ItemStack.EMPTY);
               }
            }
         }

         if (o != null && o.getCarried() == B) {
            o.setCarried(ItemStack.EMPTY);
         }

         for (ItemEntity var4 : F) {
            var4.discard();
         }

         O.clear();
         F.clear();
         W.clear();
         B = ItemStack.EMPTY;
         o = null;
         m = var0.level;
      }
   }

   public static ItemStack E(ItemFrame var0) {
      Minecraft var1 = Minecraft.getInstance();
      h(var1);
      return var0.level() == m ? O.get(var0.getUUID()) : null;
   }

   public static void D(ItemFrame var0) {
      O.remove(var0.getUUID());
   }

   public static void L(Minecraft var0, ItemFrame var1) {
      h(var0);
      ItemStack var2 = var1.getItem().copy();
      O.put(var1.getUUID(), ItemStack.EMPTY);
      if (!var2.isEmpty()) {
         W(var0, var2.copyWithCount(1), new Vec3(var1.getX(), var1.getY(), var1.getZ()).add(0.0, 0.15, 0.0));
      }
   }

   private static void W(Minecraft var0, ItemStack var1, Vec3 var2) {
      ItemEntity var3 = new ItemEntity(var0.level, var2.x, var2.y, var2.z, var1.copy());
      var3.setId(w--);
      var3.setDefaultPickUpDelay();
      var3.setUnlimitedLifetime();
      var0.level.addEntity(var3);
      F.add(var3);
   }

   private static int h(Minecraft var0, int var1, int var2) {
      for (int var3 = var1; var3 < var2; var3++) {
         if (var0.player.getInventory().getItem(var3).isEmpty() && !W.containsKey(var3)) {
            return var3;
         }
      }

      return -1;
   }

   private static void w(Minecraft var0, int var1, ItemStack var2) {
      W.put(var1, var2);
      var0.player.getInventory().setItem(var1, var2);
   }

   private static int S(ItemStack var0, ItemStack var1, int var2) {
      if (!ItemStack.isSameItemSameComponents(var0, var1)) {
         return 0;
      }

      int var3 = Math.min(var2, Math.min(var1.getCount(), Math.max(0, var0.getMaxStackSize() - var0.getCount())));
      var0.grow(var3);
      var1.shrink(var3);
      return var3;
   }

   private static void X(Minecraft var0, ItemStack var1, int var2, int var3) {
      ItemStack var4 = W.get(40);
      if (var4 != null && var4 != var1 && var0.player.getInventory().getItem(40) == var4) {
         S(var4, var1, var1.getCount());
      }

      for (int var5 = var2; var5 < var3 && !var1.isEmpty(); var5++) {
         ItemStack var6 = W.get(var5);
         if (var6 != null && var0.player.getInventory().getItem(var5) == var6) {
            S(var6, var1, var1.getCount());
         }
      }

      while (!var1.isEmpty()) {
         int var7 = h(var0, var2, var3);
         if (var7 < 0) {
            return;
         }

         int var8 = Math.min(var1.getCount(), var1.getMaxStackSize());
         w(var0, var7, var1.copyWithCount(var8));
         var1.shrink(var8);
      }
   }

   private static void Q(Minecraft var0, int var1, ItemStack var2) {
      if (var2.isEmpty()) {
         W.remove(var1);
         if (var0.player.getInventory().getItem(var1) == var2) {
            var0.player.getInventory().setItem(var1, ItemStack.EMPTY);
         }
      }
   }

   public static boolean f(Minecraft var0, InteractionHand var1) {
      return var0.player != null && W.containsKey(var1 == InteractionHand.OFF_HAND ? 40 : var0.player.getInventory().getSelectedSlot());
   }

   public static void r(Minecraft var0) {
      h(var0);
      if (var0.level != null && var0.player != null) {
         if (!B.isEmpty() && o != var0.player.containerMenu) {
            Z(var0);
         }

         for (Entry var2 : new ArrayList<>(W.entrySet())) {
            int var3 = (Integer)var2.getKey();
            ItemStack var4 = (ItemStack)var2.getValue();
            ItemStack var5 = var0.player.getInventory().getItem(var3);
            if (var5 != var4) {
               if (var5.isEmpty()) {
                  var0.player.getInventory().setItem(var3, var4);
               } else {
                  W.remove(var3);
                  X(var0, var4, 0, 36);
                  if (!var4.isEmpty()) {
                     W(var0, var4, new Vec3(var0.player.getX(), var0.player.getY(), var0.player.getZ()));
                  }
               }
            }
         }

         if (!B.isEmpty() && o == var0.player.containerMenu) {
            if (o.getCarried().isEmpty()) {
               o.setCarried(B);
            } else if (o.getCarried() != B) {
               Z(var0);
            }
         }

         Iterator var6 = F.iterator();

         while (var6.hasNext()) {
            ItemEntity var7 = (ItemEntity)var6.next();
            if (var7.isRemoved()) {
               var6.remove();
            } else if (!var7.hasPickUpDelay() && var7.getBoundingBox().intersects(var0.player.getBoundingBox().inflate(1.0, 0.5, 1.0))) {
               ItemStack var8 = var7.getItem().copy();
               int var9 = var8.getCount();
               X(var0, var8, 0, 36);
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

   public static boolean v(Minecraft var0, ItemFrame var1, InteractionHand var2) {
      h(var0);
      if (!var1.getItem().isEmpty()) {
         if (!O.containsKey(var1.getUUID()) && !f(var0, var2)) {
            return false;
         }

         var1.setRotation((var1.getRotation() + 1) % 8);
         var1.playSound(var1.getRotateItemSound(), 1.0F, 1.0F);
         return true;
      } else {
         if (!f(var0, var2)) {
            return O.containsKey(var1.getUUID());
         }

         int var3 = var2 == InteractionHand.OFF_HAND ? 40 : var0.player.getInventory().getSelectedSlot();
         ItemStack var4 = W.get(var3);
         O.put(var1.getUUID(), var4.copyWithCount(1));
         if (var4.getCount() == 1) {
            W.remove(var3);
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

   public static boolean W(Minecraft var0, ItemFrame var1) {
      h(var0);
      O.remove(var1.getUUID());
      return false;
   }

   public static boolean x(Minecraft var0) {
      if (var0.player == null) {
         return false;
      }

      int var1 = var0.player.getInventory().getSelectedSlot();
      ItemStack var2 = W.get(var1);
      ItemStack var3 = W.get(40);
      if (var2 == null && var3 == null) {
         return false;
      }

      if ((var2 != null || var0.player.getMainHandItem().isEmpty()) && (var3 != null || var0.player.getOffhandItem().isEmpty())) {
         W.remove(var1);
         W.remove(40);
         var0.player.getInventory().setItem(var1, ItemStack.EMPTY);
         var0.player.getInventory().setItem(40, ItemStack.EMPTY);
         if (var2 != null) {
            w(var0, 40, var2);
         }

         if (var3 != null) {
            w(var0, var1, var3);
         }

         return true;
      } else {
         return true;
      }
   }

   public static boolean j(Minecraft var0, boolean var1) {
      if (!f(var0, InteractionHand.MAIN_HAND)) {
         return false;
      }

      int var2 = var0.player.getInventory().getSelectedSlot();
      ItemStack var3 = W.get(var2);
      ItemStack var4 = var3.split(var1 ? var3.getCount() : 1);
      Q(var0, var2, var3);
      W(var0, var4, var0.player.getEyePosition().add(var0.player.getViewVector(1.0F).scale(0.3)));
      return true;
   }

   public static void Z(Minecraft var0) {
      if (!B.isEmpty() && var0.player != null) {
         if (o != null && o.getCarried() == B) {
            o.setCarried(ItemStack.EMPTY);
         }

         X(var0, B, 0, 36);
         if (!B.isEmpty() && var0.level != null) {
            W(var0, B, new Vec3(var0.player.getX(), var0.player.getY(), var0.player.getZ()));
         }

         B = ItemStack.EMPTY;
         o = null;
      }
   }

   public static boolean j(Minecraft var0, int var1, int var2, ContainerInput var3) {
      if (var0.player == null) {
         return false;
      }

      AbstractContainerMenu var4 = var0.player.containerMenu;
      Slot var5 = var1 >= 0 && var1 < var4.slots.size() ? var4.getSlot(var1) : null;
      int var6 = var5 != null && var5.container == var0.player.getInventory() ? var5.getContainerSlot() : -1;
      ItemStack var7 = W.get(var6);
      if (var7 != null && var5.getItem() != var7) {
         r(var0);
         return true;
      }

      int var8 = var3 == ContainerInput.SWAP && W.containsKey(var2) ? 1 : 0;
      if (var7 == null && B.isEmpty() && var8 == 0) {
         ItemStack var14 = var3 == ContainerInput.PICKUP_ALL ? var4.getCarried() : (var5 == null ? ItemStack.EMPTY : var5.getItem());
         if (var3 == ContainerInput.PICKUP_ALL || var3 == ContainerInput.QUICK_MOVE) {
            for (ItemStack var11 : W.values()) {
               if (ItemStack.isSameItemSameComponents(var14, var11)) {
                  return true;
               }
            }
         }

         return false;
      } else {
         if (B.isEmpty() && !var4.getCarried().isEmpty()) {
            return true;
         }

         if (var3 == ContainerInput.PICKUP) {
            if (B.isEmpty()) {
               if (var7 != null && var4.getCarried().isEmpty()) {
                  B = var7.split(var2 == 1 ? (var7.getCount() + 1) / 2 : var7.getCount());
                  Q(var0, var6, var7);
               }
            } else if (var6 >= 0 && (var6 < 36 || var6 == 40) && (var5.getItem().isEmpty() || var7 != null)) {
               if (var7 == null) {
                  w(var0, var6, B.split(var2 == 1 ? 1 : B.getCount()));
               } else if (ItemStack.isSameItemSameComponents(var7, B)) {
                  S(var7, B, var2 == 1 ? 1 : B.getCount());
               } else {
                  ItemStack var9 = var7;
                  w(var0, var6, B);
                  B = var9;
               }
            } else if (var1 == -999) {
               W(var0, B.split(var2 == 1 ? 1 : B.getCount()), var0.player.getEyePosition());
            }

            if (B.isEmpty()) {
               B = ItemStack.EMPTY;
            }

            var4.setCarried(B);
            o = B.isEmpty() ? null : var4;
         } else if (var3 == ContainerInput.QUICK_MOVE && var7 != null) {
            X(var0, var7, var6 < 9 ? 9 : 0, var6 >= 9 && var6 != 40 ? 9 : 36);
            Q(var0, var6, var7);
         } else if (var3 == ContainerInput.SWAP && var6 >= 0 && (var6 < 36 || var6 == 40) && var2 >= 0 && (var2 < 9 || var2 == 40)) {
            ItemStack var13 = W.get(var2);
            if ((var7 != null || var5.getItem().isEmpty()) && (var13 != null || var0.player.getInventory().getItem(var2).isEmpty())) {
               W.remove(var6);
               W.remove(var2);
               var0.player.getInventory().setItem(var6, ItemStack.EMPTY);
               var0.player.getInventory().setItem(var2, ItemStack.EMPTY);
               if (var7 != null) {
                  w(var0, var2, var7);
               }

               if (var13 != null) {
                  w(var0, var6, var13);
               }
            }
         } else if (var3 == ContainerInput.THROW && var7 != null) {
            ItemStack var12 = var7.split(var2 == 0 ? 1 : var7.getCount());
            Q(var0, var6, var7);
            W(var0, var12, var0.player.getEyePosition());
         }

         return true;
      }
   }
}
