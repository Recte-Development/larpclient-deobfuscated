package com.larpclient.module;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;

public final class tv {
   private tv() {
   }

   public static float g() {
      Minecraft var0 = Minecraft.getInstance();
      if (var0 != null && var0.options != null && var0.options.sensitivity() != null) {
         double var1 = (Double)var0.options.sensitivity().get();
         double var3 = var1 * 0.6F + 0.2F;
         return (float)(var3 * var3 * var3 * 1.2F);
      } else {
         return 0.00405F;
      }
   }

   public static float p(float var0, float var1) {
      float var2 = var1 - var0;

      while (var2 > 180.0F) {
         var2 -= 360.0F;
      }

      while (var2 < -180.0F) {
         var2 += 360.0F;
      }

      return var2;
   }

   public static float X(float var0, float var1) {
      return var0 + p(var0, var1);
   }

   public static u C(Vec3 var0) {
      LocalPlayer var1 = Minecraft.getInstance().player;
      double var2 = var0.x - var1.getX();
      double var4 = var0.y - (var1.getY() + var1.getEyeHeight(var1.getPose()));
      double var6 = var0.z - var1.getZ();
      double var8 = Math.sqrt(var2 * var2 + var6 * var6);
      float var10 = (float)Math.toDegrees(Math.atan2(var6, var2)) - 90.0F;
      float var11 = (float)(-Math.toDegrees(Math.atan2(var4, var8)));
      return new u(var10, var11);
   }

   public static u o(u var0, u var1) {
      float var2 = g();
      if (!(var2 <= 1.0E-6F) && Float.isFinite(var2)) {
         float var3 = p(var0.R, var1.R);
         float var4 = var1.x - var0.x;
         int var5 = Math.round(var3 / var2);
         int var6 = Math.round(var4 / var2);
         if (var5 == 0 && Math.abs(var3) >= var2 * 0.25F && Math.abs(var3) > 0.001F) {
            var5 = (int)Math.signum(var3);
         }

         if (var6 == 0 && Math.abs(var4) >= var2 * 0.25F && Math.abs(var4) > 0.001F) {
            var6 = (int)Math.signum(var4);
         }

         return new u(var0.R + var5 * var2, Mth.clamp(var0.x + var6 * var2, -90.0F, 90.0F));
      } else {
         return var1;
      }
   }

   public static float e(float var0, float var1) {
      float var2 = g();
      return !(var2 <= 1.0E-6F) && Float.isFinite(var2) ? var0 + Math.round((var1 - var0) / var2) * var2 : var1;
   }

   public static Vec3 Z(float var0, float var1) {
      float var2 = var1 * (float) (Math.PI / 180.0);
      float var3 = -var0 * (float) (Math.PI / 180.0);
      float var4 = Mth.cos(var3);
      float var5 = Mth.sin(var3);
      float var6 = Mth.cos(var2);
      float var7 = Mth.sin(var2);
      return new Vec3(var5 * var6, -var7, var4 * var6);
   }
}
