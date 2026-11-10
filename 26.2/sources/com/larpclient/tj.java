package com.larpclient;

import java.util.function.BooleanSupplier;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

public final class tj {
   private static LocalPlayer L;
   private static BlockHitResult b;
   private static BooleanSupplier K;
   private static int x;
   private static boolean p;
   private static int w = -1;

   public static void V(Minecraft var0, BlockHitResult var1, BooleanSupplier var2) {
      if (L != var0.player || b == null || var0.player.tickCount - x > 5) {
         L = var0.player;
         b = var1;
         K = var2;
         x = L.tickCount;
         p = (boolean)0;
         w = -1;
      }
   }

   private static boolean c(LocalPlayer var0) {
      return var0 == L && b != null && var0.tickCount - x <= 5 && Minecraft.getInstance().player == var0 && !var0.isPassenger() && !j6.j();
   }

   public static float x(LocalPlayer var0) {
      if (!c(var0)) {
         return var0.getYRot();
      }

      p = (boolean)1;
      Vec3 var1 = b.getLocation().subtract(var0.getEyePosition());
      return (float)Math.toDegrees(Math.atan2(-var1.x, var1.z));
   }

   public static float H(LocalPlayer var0) {
      if (!c(var0)) {
         return var0.getXRot();
      }

      Vec3 var1 = b.getLocation().subtract(var0.getEyePosition());
      return (float)(-Math.toDegrees(Math.atan2(var1.y, Math.hypot(var1.x, var1.z))));
   }

   public static void q(LocalPlayer var0) {
      if (c(var0) && p) {
         if (w < 0) {
            w = var0.tickCount;
            return;
         }

         if (var0.tickCount <= w) {
            return;
         }

         if (K != null) {
            if (!K.getAsBoolean()) {
               return;
            }

            K = null;
            w = var0.tickCount - 1;
         }

         if (var0.tickCount <= w + 2) {
            return;
         }
      }

      L = null;
      b = null;
      K = null;
      p = (boolean)0;
      w = -1;
   }
}
