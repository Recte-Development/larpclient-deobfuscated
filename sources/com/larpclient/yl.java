package com.larpclient;

import net.minecraft.world.item.ItemStack;

public final class yl {
   private yl() {
   }

   public static boolean z(ItemStack var0) {
      return var0 != null && var0 instanceof yl.w var1 && var1.fakeSpawner$isWithdrawnBone();
   }

   public static void T(ItemStack var0, boolean var1) {
      if (var0 != null && !var0.isEmpty() && var0 instanceof yl.w var2) {
         var2.fakeSpawner$setWithdrawnBone(var1);
      }
   }

   public interface w {
      boolean fakeSpawner$isWithdrawnBone();

      void fakeSpawner$setWithdrawnBone(boolean var1);
   }
}
