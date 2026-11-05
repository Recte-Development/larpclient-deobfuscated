package com.larpclient.module;

import java.util.SplittableRandom;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public final class lt implements lg {
   private final SplittableRandom l = new SplittableRandom();
   private final lk K = new lk(lk.x);
   private final lj i = new lj();
   private boolean a;
   private float r;
   private float y;
   private float S;
   private float x;
   private float z;
   private float e;
   private float s;
   private float I;
   private int F;

   @Override
   public lx rotate(lx var1, lx var2, LivingEntity var3, boolean var4, boolean var5) {
      LocalPlayer var6 = Minecraft.getInstance().player;
      if (var1 != null && var2 != null && var3 != null && var6 != null) {
         int var7 = !var4 && var5 ? 0 : 1;
         this.K.Q();
         double var8 = var3.getX() - var6.getX();
         double var10 = var3.getZ() - var6.getZ();
         double var12 = var8 * var8 + var10 * var10;
         float var14 = le.l(var1.z, this.c(var1, var2, var12, (boolean)var7));
         float var15 = Mth.clamp(var2.I, -90.0F, 90.0F);
         float var16 = var14 - var1.z;
         float var17 = var15 - var1.I;
         float var18 = (float)Math.hypot(var16, var17);
         if (var7 != 0) {
            this.I = 0.0F;
            this.F = 0;
         } else if (var18 > 25.0F && this.F <= 0 && this.l.nextDouble() < 0.18) {
            this.I = (float)(this.l.nextDouble() * 2.5 - 1.25);
            this.F = this.l.nextInt(4, 10);
         } else if (this.F > 0) {
            this.F--;
            this.I *= 0.65F;
         } else {
            this.I = 0.0F;
         }

         float var19 = var7 != 0 ? 0.0F : this.N(var18, var12, var4, var5);
         this.s(var19);
         this.g(var3, var14 + this.I, var15, var19);
         float var20 = this.K.I(var18, (boolean)var7);
         float var21 = this.K.w(lk.o.W) * 0.07F;
         float var22 = this.K.w(lk.o.Z) * 0.055F;
         float var23 = this.K.S(lk.o.W, (boolean)var7);
         float var24 = this.K.S(lk.o.Z, (boolean)var7);
         float var25 = this.B(this.e - var1.z, 20.0F, 54.0F, 42.0F, var20, var21, var23, (boolean)var7, var5);
         float var26 = this.B(this.s - var1.I, 14.0F, 42.0F, 28.0F, var20, var22, var24, (boolean)var7, var5);
         if (var7 == 0) {
            var26 += this.K.D(var25);
         }

         return new lx(var1.z + var25, Mth.clamp(var1.I + var26, -90.0F, 90.0F));
      } else {
         return var1 != null ? var1 : var2;
      }
   }

   public lx C(lx var1, lx var2) {
      return this.i.Q(var1, var2);
   }

   private float c(lx var1, lx var2, double var3, boolean var5) {
      if (!var5 && !(var3 >= 0.16F)) {
         return this.a ? this.r : var1.z;
      }

      this.r = var2.z;
      this.a = (boolean)1;
      return this.r;
   }

   private float N(float var1, double var2, boolean var4, boolean var5) {
      if (!(var2 < 0.16F) && (var4 || var5)) {
         float var6 = 1.0F - Mth.clamp(var1 / 14.0F, 0.0F, 1.0F);
         return var6 * var6 * (var4 ? 0.35F : 0.65F);
      } else {
         return 0.0F;
      }
   }

   private void s(float var1) {
      if (var1 <= 1.0E-5F) {
         this.y *= 0.5F;
         this.S *= 0.5F;
         this.x = 0.0F;
         this.z = 0.0F;
      } else {
         float var2 = this.S() * (0.22F + var1 * 0.22F);
         float var3 = this.S() * (0.15F + var1 * 0.15F);
         this.x = Mth.clamp(this.x * 0.68F + var2, -0.65F, 0.65F);
         this.z = Mth.clamp(this.z * 0.68F + var3, -0.45F, 0.45F);
         this.y = Mth.clamp(this.y + this.x, -4.1F, 4.1F);
         this.S = Mth.clamp(this.S + this.z, -2.85F, 2.85F);
      }
   }

   private void g(LivingEntity var1, float var2, float var3, float var4) {
      if (var4 <= 1.0E-5F) {
         this.e = var2;
         this.s = var3;
      } else {
         float var5 = var2 + this.y * var4;
         float var6 = Mth.clamp(var3 + this.S * var4, -90.0F, 90.0F);
         if (L(var1, var5, var6)) {
            this.e = var5;
            this.s = var6;
         } else {
            this.y *= 0.5F;
            this.S *= 0.5F;
            var5 = var2 + this.y * var4;
            var6 = Mth.clamp(var3 + this.S * var4, -90.0F, 90.0F);
            if (L(var1, var5, var6)) {
               this.e = var5;
               this.s = var6;
            } else {
               this.y = this.S = this.x = this.z = 0.0F;
               this.e = var2;
               this.s = var3;
            }
         }
      }
   }

   private float B(float var1, float var2, float var3, float var4, float var5, float var6, float var7, boolean var8, boolean var9) {
      float var10 = Math.abs(var1);
      if (var10 <= 1.0E-5F) {
         return 0.0F;
      }

      float var11 = Mth.clamp(var10 / var4, 0.0F, 1.0F);
      float var12 = var11 * var11 * (3.0F - 2.0F * var11);
      float var13 = Math.min(var10, var2 + (var3 - var2) * var12);
      float var14;
      if (!var8 && var9) {
         float var15 = 0.78F + (float)this.l.nextDouble() * 0.22000003F;
         var14 = Mth.clamp(var15 * var5 * var7 + var6, 0.65F, 1.08F);
      } else {
         var14 = Mth.clamp(var7 + var6 * 0.18F, 0.92F, 1.15F);
      }

      float var16;
      if (!var8 && var9) {
         var16 = Math.min(var10, Math.min(var3, var13 * var14));
      } else {
         var16 = Math.min(var10, Math.min(var3, Math.max(var13 * var14, var10 * 0.93F)));
      }

      return Math.copySign(var16, var1);
   }

   private float S() {
      return (float)(this.l.nextDouble() * 2.0 - 1.0);
   }

   private static boolean L(LivingEntity var0, float var1, float var2) {
      LocalPlayer var3 = Minecraft.getInstance().player;
      AABB var4 = var0.getBoundingBox();
      double var5 = Math.min(0.0125, Math.max(0.0, var4.getXsize() * 0.25));
      double var7 = Math.min(0.0125, Math.max(0.0, var4.getYsize() * 0.25));
      double var9 = Math.min(0.0125, Math.max(0.0, var4.getZsize() * 0.25));
      AABB var11 = new AABB(var4.minX + var5, var4.minY + var7, var4.minZ + var9, var4.maxX - var5, var4.maxY - var7, var4.maxZ - var9);
      Vec3 var12 = var3.getEyePosition();
      if (var11.contains(var12)) {
         return true;
      }

      Vec3 var13 = le.W(var1, var2);
      double var14 = var12.distanceTo(var11.getCenter()) + Math.max(var11.getXsize(), Math.max(var11.getYsize(), var11.getZsize())) + 1.0;
      return var11.clip(var12, var12.add(var13.scale(var14))).isPresent();
   }

   @Override
   public float yawSpeed() {
      return 22.0F;
   }

   @Override
   public float pitchSpeed() {
      return 14.0F;
   }

   @Override
   public void reset() {
      this.a = (boolean)0;
      this.r = 0.0F;
      this.y = this.S = this.x = this.z = 0.0F;
      this.e = this.s = 0.0F;
      this.I = 0.0F;
      this.F = 0;
      this.K.c();
      this.i.y();
   }

   @Override
   public void targetChanged(LivingEntity var1) {
      this.reset();
   }
}
