package com.larpclient;

import java.util.function.BooleanSupplier;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

public final class pb {
   private static LocalPlayer m;
   private static BlockHitResult P;
   private static BooleanSupplier c;
   private static int G;
   private static boolean I;
   private static int r = -1;

   public static void N(Minecraft var0, BlockHitResult var1, BooleanSupplier var2) {
      if (m != var0.player || P == null || var0.player.tickCount - G > 5) {
         m = var0.player;
         P = var1;
         c = var2;
         G = m.tickCount;
         I = (boolean)0;
         r = -1;
      }
   }

   private static boolean Q(LocalPlayer var0) {
      return var0 == m && P != null && var0.tickCount - G <= 5 && Minecraft.getInstance().player == var0 && !var0.isPassenger() && !m9.R();
   }

   public static float T(LocalPlayer var0) {
      if (!Q(var0)) {
         return var0.getYRot();
      }

      I = (boolean)1;
      Vec3 var1 = P.getLocation().subtract(var0.getEyePosition());
      return (float)Math.toDegrees(Math.atan2(-var1.x, var1.z));
   }

   public static float U(LocalPlayer var0) {
      if (!Q(var0)) {
         return var0.getXRot();
      }

      Vec3 var1 = P.getLocation().subtract(var0.getEyePosition());
      return (float)(-Math.toDegrees(Math.atan2(var1.y, Math.hypot(var1.x, var1.z))));
   }

   public static void W(LocalPlayer var0) {
      if (Q(var0) && I) {
         if (r < 0) {
            r = var0.tickCount;
            return;
         }

         if (var0.tickCount <= r) {
            return;
         }

         if (c != null) {
            if (!c.getAsBoolean()) {
               return;
            }

            c = null;
            r = var0.tickCount - 1;
         }

         if (var0.tickCount <= r + 2) {
            return;
         }
      }

      m = null;
      P = null;
      c = null;
      I = (boolean)0;
      r = -1;
   }
}
