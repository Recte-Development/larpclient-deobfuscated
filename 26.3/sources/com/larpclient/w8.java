package com.larpclient;

import java.util.function.BooleanSupplier;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

public final class w8 {
   private static LocalPlayer b;
   private static BlockHitResult a;
   private static BooleanSupplier e;
   private static int H;
   private static boolean F;
   private static int W = -1;

   public static void P(Minecraft var0, BlockHitResult var1, BooleanSupplier var2) {
      if (b != var0.player || a == null || var0.player.tickCount - H > 5) {
         b = var0.player;
         a = var1;
         e = var2;
         H = b.tickCount;
         F = (boolean)0;
         W = -1;
      }
   }

   private static boolean b(LocalPlayer var0) {
      return var0 == b && a != null && var0.tickCount - H <= 5 && Minecraft.getInstance().player == var0 && !var0.isPassenger() && !b6.x();
   }

   public static float d(LocalPlayer var0) {
      if (!b(var0)) {
         return var0.getYRot();
      }

      F = (boolean)1;
      Vec3 var1 = a.getLocation().subtract(var0.getEyePosition());
      return (float)Math.toDegrees(Math.atan2(-var1.x, var1.z));
   }

   public static float t(LocalPlayer var0) {
      if (!b(var0)) {
         return var0.getXRot();
      }

      Vec3 var1 = a.getLocation().subtract(var0.getEyePosition());
      return (float)(-Math.toDegrees(Math.atan2(var1.y, Math.hypot(var1.x, var1.z))));
   }

   public static void o(LocalPlayer var0) {
      if (b(var0) && F) {
         if (W < 0) {
            W = var0.tickCount;
            return;
         }

         if (var0.tickCount <= W) {
            return;
         }

         if (e != null) {
            if (!e.getAsBoolean()) {
               return;
            }

            e = null;
            W = var0.tickCount - 1;
         }

         if (var0.tickCount <= W + 2) {
            return;
         }
      }

      b = null;
      a = null;
      e = null;
      F = (boolean)0;
      W = -1;
   }
}
