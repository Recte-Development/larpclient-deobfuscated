package com.larpclient;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.world.phys.Vec3;

public final class x9 {
   public static final List<zg.r> x = s();

   private static List<zg.r> s() {
      Vec3[] var0 = new Vec3[6];

      for (int var1 = 0; var1 < 6; var1++) {
         double var2 = var1 * Math.PI / 3.0 - (Math.PI / 2);
         var0[var1] = new Vec3(Math.cos(var2), 0.0, Math.sin(var2));
      }

      ArrayList var4 = new ArrayList();

      for (int var5 = 0; var5 < 2; var5++) {
         for (int var3 = 0; var3 < 3; var3++) {
            var4.add(new zg.r(var0[var5 + var3 * 2], var0[var5 + (var3 * 2 + 2) % 6], 0.5));
         }
      }

      return List.copyOf(var4);
   }

   public static double P(double var0, double var2, double var4) {
      return var0 + var2 * var4;
   }

   private x9() {
   }
}
