package com.larpclient;

import net.minecraft.class_1799;

public final class wa {
   private wa() {
   }

   public static boolean x(class_1799 var0) {
      return var0 != null && var0 instanceof wa.k var1 && var1.fakeSpawner$isWithdrawnBone();
   }

   public static void C(class_1799 var0, boolean var1) {
      if (var0 != null && !var0.method_7960() && var0 instanceof wa.k var2) {
         var2.fakeSpawner$setWithdrawnBone(var1);
      }
   }

   public interface k {
      boolean fakeSpawner$isWithdrawnBone();

      void fakeSpawner$setWithdrawnBone(boolean var1);
   }
}
