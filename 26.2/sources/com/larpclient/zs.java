package com.larpclient;

import net.minecraft.world.item.ItemStack;

public final class zs {
   private zs() {
   }

   public static boolean t(ItemStack var0) {
      return var0 != null && var0 instanceof zs.y var1 && var1.fakeSpawner$isWithdrawnBone();
   }

   public static void z(ItemStack var0, boolean var1) {
      if (var0 != null && !var0.isEmpty() && var0 instanceof zs.y var2) {
         var2.fakeSpawner$setWithdrawnBone(var1);
      }
   }

   public interface y {
      boolean fakeSpawner$isWithdrawnBone();

      void fakeSpawner$setWithdrawnBone(boolean var1);
   }
}
