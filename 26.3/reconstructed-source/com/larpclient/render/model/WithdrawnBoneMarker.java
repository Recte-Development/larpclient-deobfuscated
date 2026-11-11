package com.larpclient.render.model;

import net.minecraft.world.item.ItemStack;

public final class WithdrawnBoneMarker {
   private WithdrawnBoneMarker() {
   }

   public static boolean isWithdrawnBone(ItemStack var0) {
      return var0 != null && var0 instanceof WithdrawnBoneMarker.WithdrawnBoneAccess var1 && var1.fakeSpawner$isWithdrawnBone();
   }

   public static void setWithdrawnBone(ItemStack var0, boolean var1) {
      if (var0 != null && !var0.isEmpty() && var0 instanceof WithdrawnBoneMarker.WithdrawnBoneAccess var2) {
         var2.fakeSpawner$setWithdrawnBone(var1);
      }
   }

   public interface WithdrawnBoneAccess {
      boolean fakeSpawner$isWithdrawnBone();

      void fakeSpawner$setWithdrawnBone(boolean var1);
   }
}
