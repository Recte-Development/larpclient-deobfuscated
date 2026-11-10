package com.larpclient.module;

import java.util.SplittableRandom;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public final class q implements k {
   private final SplittableRandom i = new SplittableRandom();
   private final f0 a = new f0(f0.Y);
   private final kx m = new kx();
   private boolean B;
   private float M;
   private float d;
   private float o;
   private float c;
   private float r;
   private float A;
   private float S;
   private float Z;
   private int V;

   @Override
   public c rotate(c var1, c var2, LivingEntity var3, boolean var4, boolean var5) {
      LocalPlayer var6 = Minecraft.getInstance().player;
      if (var1 != null && var2 != null && var3 != null && var6 != null) {
         int var7 = !var4 && var5 ? 0 : 1;
         this.a.U();
         double var8 = var3.getX() - var6.getX();
         double var10 = var3.getZ() - var6.getZ();
         double var12 = var8 * var8 + var10 * var10;
         float var14 = w.P(var1.V, this.B(var1, var2, var12, (boolean)var7));
         float var15 = Mth.clamp(var2.T, -90.0F, 90.0F);
         float var16 = var14 - var1.V;
         float var17 = var15 - var1.T;
         float var18 = (float)Math.hypot(var16, var17);
         if (var7 != 0) {
            this.Z = 0.0F;
            this.V = 0;
         } else if (var18 > 25.0F && this.V <= 0 && this.i.nextDouble() < 0.18) {
            this.Z = (float)(this.i.nextDouble() * 2.5 - 1.25);
            this.V = this.i.nextInt(4, 10);
         } else if (this.V > 0) {
            this.V--;
            this.Z *= 0.65F;
         } else {
            this.Z = 0.0F;
         }

         float var19 = var7 != 0 ? 0.0F : this.d(var18, var12, var4, var5);
         this.t(var19);
         this.n(var3, var14 + this.Z, var15, var19);
         float var20 = this.a.c(var18, (boolean)var7);
         float var21 = this.a.h(f0.p.g) * 0.07F;
         float var22 = this.a.h(f0.p.K) * 0.055F;
         float var23 = this.a.E(f0.p.g, (boolean)var7);
         float var24 = this.a.E(f0.p.K, (boolean)var7);
         float var25 = this.W(this.A - var1.V, 20.0F, 54.0F, 42.0F, var20, var21, var23, (boolean)var7, var5);
         float var26 = this.W(this.S - var1.T, 14.0F, 42.0F, 28.0F, var20, var22, var24, (boolean)var7, var5);
         if (var7 == 0) {
            var26 += this.a.a(var25);
         }

         return new c(var1.V + var25, Mth.clamp(var1.T + var26, -90.0F, 90.0F));
      } else {
         return var1 != null ? var1 : var2;
      }
   }

   public c x(c var1, c var2) {
      return this.m.X(var1, var2);
   }

   private float B(c var1, c var2, double var3, boolean var5) {
      if (!var5 && !(var3 >= 0.16F)) {
         return this.B ? this.M : var1.V;
      }

      this.M = var2.V;
      this.B = (boolean)1;
      return this.M;
   }

   private float d(float var1, double var2, boolean var4, boolean var5) {
      if (!(var2 < 0.16F) && (var4 || var5)) {
         float var6 = 1.0F - Mth.clamp(var1 / 14.0F, 0.0F, 1.0F);
         return var6 * var6 * (var4 ? 0.35F : 0.65F);
      } else {
         return 0.0F;
      }
   }

   private void t(float var1) {
      if (var1 <= 1.0E-5F) {
         this.d *= 0.5F;
         this.o *= 0.5F;
         this.c = 0.0F;
         this.r = 0.0F;
      } else {
         float var2 = this.b() * (0.22F + var1 * 0.22F);
         float var3 = this.b() * (0.15F + var1 * 0.15F);
         this.c = Mth.clamp(this.c * 0.68F + var2, -0.65F, 0.65F);
         this.r = Mth.clamp(this.r * 0.68F + var3, -0.45F, 0.45F);
         this.d = Mth.clamp(this.d + this.c, -4.1F, 4.1F);
         this.o = Mth.clamp(this.o + this.r, -2.85F, 2.85F);
      }
   }

   private void n(LivingEntity var1, float var2, float var3, float var4) {
      if (var4 <= 1.0E-5F) {
         this.A = var2;
         this.S = var3;
      } else {
         float var5 = var2 + this.d * var4;
         float var6 = Mth.clamp(var3 + this.o * var4, -90.0F, 90.0F);
         if (d(var1, var5, var6)) {
            this.A = var5;
            this.S = var6;
         } else {
            this.d *= 0.5F;
            this.o *= 0.5F;
            var5 = var2 + this.d * var4;
            var6 = Mth.clamp(var3 + this.o * var4, -90.0F, 90.0F);
            if (d(var1, var5, var6)) {
               this.A = var5;
               this.S = var6;
            } else {
               this.d = this.o = this.c = this.r = 0.0F;
               this.A = var2;
               this.S = var3;
            }
         }
      }
   }

   private float W(float var1, float var2, float var3, float var4, float var5, float var6, float var7, boolean var8, boolean var9) {
      float var10 = Math.abs(var1);
      if (var10 <= 1.0E-5F) {
         return 0.0F;
      }

      float var11 = Mth.clamp(var10 / var4, 0.0F, 1.0F);
      float var12 = var11 * var11 * (3.0F - 2.0F * var11);
      float var13 = Math.min(var10, var2 + (var3 - var2) * var12);
      float var14;
      if (!var8 && var9) {
         float var15 = 0.78F + (float)this.i.nextDouble() * 0.22000003F;
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

   private float b() {
      return (float)(this.i.nextDouble() * 2.0 - 1.0);
   }

   private static boolean d(LivingEntity var0, float var1, float var2) {
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

      Vec3 var13 = w.Y(var1, var2);
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
      this.B = (boolean)0;
      this.M = 0.0F;
      this.d = this.o = this.c = this.r = 0.0F;
      this.A = this.S = 0.0F;
      this.Z = 0.0F;
      this.V = 0;
      this.a.G();
      this.m.L();
   }

   @Override
   public void targetChanged(LivingEntity var1) {
      this.reset();
   }
}
