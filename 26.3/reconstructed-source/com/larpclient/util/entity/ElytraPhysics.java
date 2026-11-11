package com.larpclient.util.entity;

import net.minecraft.util.Mth;

public final class ElytraPhysics {
   public static final double gRAVITY = 0.08;
   public static final double mIN_SPEED = 0.01;

   public static double lookX(float var0, float var1) {
      return Mth.sin(-var0 * (float) (Math.PI / 180.0)) * Mth.cos(var1 * (float) (Math.PI / 180.0));
   }

   public static double lookY(float var0) {
      return -Mth.sin(var0 * (float) (Math.PI / 180.0));
   }

   public static double lookZ(float var0, float var1) {
      return Mth.cos(-var0 * (float) (Math.PI / 180.0)) * Mth.cos(var1 * (float) (Math.PI / 180.0));
   }

   public static void lookY(ElytraPhysics.FlightState var0, double var1) {
      double var3 = lookX(var0.yaw, var0.pitch);
      double var5 = lookZ(var0.yaw, var0.pitch);
      float var7 = var0.pitch * (float) (Math.PI / 180.0);
      double var8 = Math.sqrt(var3 * var3 + var5 * var5);
      double var10 = Math.sqrt(var0.velX * var0.velX + var0.velZ * var0.velZ);
      double var12 = Mth.square(Math.cos(var7));
      double var14 = var0.velX;
      double var16 = var0.velY + var1 * (-1.0 + var12 * 0.75);
      double var18 = var0.velZ;
      if (var16 < 0.0 && var8 > 0.0) {
         double var20 = var16 * -0.1 * var12;
         var14 += var3 * var20 / var8;
         var16 += var20;
         var18 += var5 * var20 / var8;
      }

      if (var7 < 0.0F && var8 > 0.0) {
         double var22 = var10 * -Mth.sin(var7) * 0.04;
         var14 += -var3 * var22 / var8;
         var16 += var22 * 3.2;
         var18 += -var5 * var22 / var8;
      }

      if (var8 > 0.0) {
         var14 += (var3 / var8 * var10 - var14) * 0.1;
         var18 += (var5 / var8 * var10 - var18) * 0.1;
      }

      var0.velX = var14 * 0.99F;
      var0.velY = var16 * 0.98F;
      var0.velZ = var18 * 0.99F;
   }

   public static void applyFireworkBoost(ElytraPhysics.FlightState var0) {
      double var1 = lookX(var0.yaw, var0.pitch);
      double var3 = lookY(var0.pitch);
      double var5 = lookZ(var0.yaw, var0.pitch);
      var0.velX = var0.velX + (var1 * 0.1 + (var1 * 1.5 - var0.velX) * 0.5);
      var0.velY = var0.velY + (var3 * 0.1 + (var3 * 1.5 - var0.velY) * 0.5);
      var0.velZ = var0.velZ + (var5 * 0.1 + (var5 * 1.5 - var0.velZ) * 0.5);
   }

   public static void advancePosition(ElytraPhysics.FlightState var0) {
      var0.posX = var0.posX + var0.velX;
      var0.posY = var0.posY + var0.velY;
      var0.posZ = var0.posZ + var0.velZ;
   }

   public static int boostDurationTicks(int var0) {
      return 10 * (1 + Math.max(0, var0)) + 5;
   }

   public static float yawTo(double var0, double var2, double var4, double var6) {
      return (float)Math.toDegrees(Math.atan2(-(var4 - var0), var6 - var2));
   }

   public static float stepYaw(float var0, float var1, float var2) {
      float var3 = Mth.wrapDegrees(var1 - var0);
      float var4 = Math.min(Math.abs(var3), Math.max(0.5F, Math.min(var2, Math.abs(var3) * 0.45F)));
      return var0 + Math.copySign(var4, var3);
   }

   private ElytraPhysics() {
   }

   public static final class FlightState {
      public double posX;
      public double posY;
      public double posZ;
      public double velX;
      public double velY;
      public double velZ;
      public float yaw;
      public float pitch;

      public ElytraPhysics.FlightState reset(double var1, double var3, double var5, double var7, double var9, double var11, float var13, float var14) {
         this.posX = var1;
         this.posY = var3;
         this.posZ = var5;
         this.velX = var7;
         this.velY = var9;
         this.velZ = var11;
         this.yaw = var13;
         this.pitch = var14;
         return this;
      }

      public ElytraPhysics.FlightState copy(ElytraPhysics.FlightState var1) {
         return this.reset(var1.posX, var1.posY, var1.posZ, var1.velX, var1.velY, var1.velZ, var1.yaw, var1.pitch);
      }

      public double speed() {
         return Math.sqrt(this.velX * this.velX + this.velZ * this.velZ);
      }
   }
}
