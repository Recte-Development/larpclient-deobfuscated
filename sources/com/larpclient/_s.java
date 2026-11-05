package com.larpclient;

import java.util.function.BooleanSupplier;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

public final class _s {
   private static LocalPlayer j;
   private static BlockHitResult X;
   private static BooleanSupplier i;
   private static int K;
   private static boolean P;
   private static int v = -1;

   public static void q(Minecraft var0, BlockHitResult var1, BooleanSupplier var2) {
      if (j != var0.player || X == null || var0.player.tickCount - K > 5) {
         j = var0.player;
         X = var1;
         i = var2;
         K = j.tickCount;
         P = (boolean)0;
         v = -1;
      }
   }

   private static boolean r(LocalPlayer var0) {
      return var0 == j && X != null && var0.tickCount - K <= 5 && Minecraft.getInstance().player == var0 && !var0.isPassenger() && !ep.f();
   }

   public static float Q(LocalPlayer var0) {
      if (!r(var0)) {
         return var0.getYRot();
      }

      P = (boolean)1;
      Vec3 var1 = X.getLocation().subtract(var0.getEyePosition());
      return (float)Math.toDegrees(Math.atan2(-var1.x, var1.z));
   }

   public static float R(LocalPlayer var0) {
      if (!r(var0)) {
         return var0.getXRot();
      }

      Vec3 var1 = X.getLocation().subtract(var0.getEyePosition());
      return (float)(-Math.toDegrees(Math.atan2(var1.y, Math.hypot(var1.x, var1.z))));
   }

   public static void E(LocalPlayer var0) {
      if (r(var0) && P) {
         if (v < 0) {
            v = var0.tickCount;
            return;
         }

         if (var0.tickCount <= v) {
            return;
         }

         if (i != null) {
            if (!i.getAsBoolean()) {
               return;
            }

            i = null;
            v = var0.tickCount - 1;
         }

         if (var0.tickCount <= v + 2) {
            return;
         }
      }

      j = null;
      X = null;
      i = null;
      P = (boolean)0;
      v = -1;
   }
}
