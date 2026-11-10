package com.larpclient;

import net.minecraft.world.item.ItemStack;

public final class ox {
   private ox() {
   }

   public static boolean F(ItemStack var0) {
      return var0 != null && var0 instanceof ox.x var1 && var1.fakeSpawner$isWithdrawnBone();
   }

   public static void x(ItemStack var0, boolean var1) {
      if (var0 != null && !var0.isEmpty() && var0 instanceof ox.x var2) {
         var2.fakeSpawner$setWithdrawnBone(var1);
      }
   }

   public interface x {
      boolean fakeSpawner$isWithdrawnBone();

      void fakeSpawner$setWithdrawnBone(boolean var1);
   }
}
