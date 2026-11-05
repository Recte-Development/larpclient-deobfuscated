package com.larpclient.module;

import java.util.concurrent.ThreadLocalRandom;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.ClipContext.Block;
import net.minecraft.world.level.ClipContext.Fluid;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.HitResult.Type;

public final class ll {
   private static int n = Integer.MIN_VALUE;
   private static Vec3 Y = Vec3.ZERO;
   private static Vec3 c = Vec3.ZERO;

   private ll() {
   }

   public static void q() {
      n = Integer.MIN_VALUE;
      Y = Vec3.ZERO;
      c = Vec3.ZERO;
   }

   public static Vec3 x(AABB var0) {
      LocalPlayer var1 = Minecraft.getInstance().player;
      if (var1 == null) {
         return var0.getCenter();
      }

      Vec3 var2 = var1.getEyePosition();
      Vec3 var3 = null;
      double var4 = Double.MAX_VALUE;

      for (double var6 = var0.minX; var6 <= var0.maxX; var6 += 0.1F) {
         for (double var8 = var0.minY; var8 <= var0.maxY; var8 += 0.1F) {
            for (double var10 = var0.minZ; var10 <= var0.maxZ; var10 += 0.1F) {
               Vec3 var12 = new Vec3(var6, var8, var10);
               double var13 = var2.distanceTo(var12);
               if (var13 < var4) {
                  var4 = var13;
                  var3 = var12;
               }
            }
         }
      }

      return var3 != null ? var3 : var0.getCenter();
   }

   public static Vec3 W(Entity var0, AABB var1, double var2) {
      if (Minecraft.getInstance().player == null) {
         return var1.getCenter();
      }

      float var4 = 0.005F;
      float var5 = 0.015F;
      float var6 = 0.0015F;
      float var7 = 0.015F;
      double var8 = var1.getXsize();
      double var10 = var1.getYsize();
      double var12 = var1.getZsize();
      if (n != var0.getId()) {
         n = var0.getId();
         Y = new Vec3(0.0, var10 * 0.55F, 0.0);
         c = new Vec3(I(-0.02F, 0.02F), I(-0.02F, 0.02F), I(-0.02F, 0.02F));
      }

      Y = Y.add(c);
      double var14 = (var8 - 0.1F) / 2.0;
      double var16 = (var12 - 0.1F) / 2.0;
      if (Y.x >= var14) {
         c = new Vec3(-I(var4, var5), c.y, c.z);
      } else if (Y.x <= -var14) {
         c = new Vec3(I(var4, var5), c.y, c.z);
      }

      if (Y.y >= var10 * 0.75) {
         c = new Vec3(c.x, -I(var6, var7), c.z);
      } else if (Y.y <= var10 * 0.3F) {
         c = new Vec3(c.x, I(var6, var7), c.z);
      }

      if (Y.z >= var16) {
         c = new Vec3(c.x, c.y, -I(var4, var5));
      } else if (Y.z <= -var16) {
         c = new Vec3(c.x, c.y, I(var4, var5));
      }

      Y = Y.add(I(-0.05F, 0.05F), 0.0, I(-0.05F, 0.05F));
      Y = new Vec3(Mth.clamp(Y.x, -var14, var14), Mth.clamp(Y.y, var10 * 0.3F, var10 * 0.75), Mth.clamp(Y.z, -var16, var16));
      Vec3 var18 = new Vec3(var1.getCenter().x, var1.minY, var1.getCenter().z);
      Vec3 var19 = var18.add(Y);
      if (!lf.t(le.p(var19).t(), var2, var1)) {
         float var20 = (float)(var8 / 2.0) * 0.8F;

         for (float var21 = -var20; var21 <= var20; var21 += 0.1F) {
            for (float var22 = -var20; var22 <= var20; var22 += 0.1F) {
               for (float var23 = (float)(var10 * 0.9F); var23 >= var10 * 0.3F; var23 -= 0.1F) {
                  Vec3 var24 = var18.add(var21, var23, var22);
                  if (lf.t(le.p(var24).t(), var2, var1)) {
                     Y = new Vec3(var21, var23, var22);
                     return var18.add(Y);
                  }
               }
            }
         }
      }

      return var19;
   }

   public static Vec3 U(AABB var0, Vec3 var1, double var2) {
      Minecraft var4 = Minecraft.getInstance();
      if (var1 != null && var4.player != null && var4.level != null) {
         if (X(var0, var1, var2)) {
            return var1;
         }

         Vec3 var5 = null;
         double var6 = Double.MAX_VALUE;

         for (double var8 = var0.minX; var8 <= var0.maxX; var8 += 0.12) {
            for (double var10 = var0.minY; var10 <= var0.maxY; var10 += 0.12) {
               for (double var12 = var0.minZ; var12 <= var0.maxZ; var12 += 0.12) {
                  Vec3 var14 = new Vec3(var8, var10, var12);
                  if (X(var0, var14, var2)) {
                     double var15 = var14.distanceToSqr(var1);
                     if (var15 < var6) {
                        var6 = var15;
                        var5 = var14;
                     }
                  }
               }
            }
         }

         return var5 != null ? var5 : var1;
      } else {
         return var1;
      }
   }

   private static boolean X(AABB var0, Vec3 var1, double var2) {
      Minecraft var4 = Minecraft.getInstance();
      if (var4.player == null) {
         return false;
      }

      Vec3 var5 = var4.player.getEyePosition();
      double var6 = var5.distanceTo(var1);
      if (var6 > var2) {
         return false;
      }

      Vec3 var8 = var1.subtract(var5).normalize();
      if (!lf.t(var8, var6 + 0.2, var0)) {
         return false;
      }

      BlockHitResult var9 = var4.level.clip(new ClipContext(var5, var1, Block.COLLIDER, Fluid.NONE, var4.player));
      return var9 == null || var9.getType() == Type.MISS || var5.distanceToSqr(var9.getLocation()) >= var5.distanceToSqr(var1) - 1.0E-4;
   }

   private static float I(float var0, float var1) {
      return (float)(ThreadLocalRandom.current().nextDouble() * (var1 - var0) + var0);
   }
}
