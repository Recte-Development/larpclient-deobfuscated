package com.larpclient.module;

import java.util.SplittableRandom;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public final class b implements x {
   private final SplittableRandom R = new SplittableRandom();
   private final ti w = new ti(ti.J);
   private final tl H = new tl();
   private boolean n;
   private float z;
   private float N;
   private float m;
   private float q;
   private float v;
   private float u;
   private float B;
   private float K;
   private int F;

   @Override
   public u rotate(u var1, u var2, LivingEntity var3, boolean var4, boolean var5) {
      LocalPlayer var6 = Minecraft.getInstance().player;
      if (var1 != null && var2 != null && var3 != null && var6 != null) {
         int var7 = !var4 && var5 ? 0 : 1;
         this.w.x();
         double var8 = var3.getX() - var6.getX();
         double var10 = var3.getZ() - var6.getZ();
         double var12 = var8 * var8 + var10 * var10;
         float var14 = tv.X(var1.R, this.C(var1, var2, var12, (boolean)var7));
         float var15 = Mth.clamp(var2.x, -90.0F, 90.0F);
         float var16 = var14 - var1.R;
         float var17 = var15 - var1.x;
         float var18 = (float)Math.hypot(var16, var17);
         if (var7 != 0) {
            this.K = 0.0F;
            this.F = 0;
         } else if (var18 > 25.0F && this.F <= 0 && this.R.nextDouble() < 0.18) {
            this.K = (float)(this.R.nextDouble() * 2.5 - 1.25);
            this.F = this.R.nextInt(4, 10);
         } else if (this.F > 0) {
            this.F--;
            this.K *= 0.65F;
         } else {
            this.K = 0.0F;
         }

         float var19 = var7 != 0 ? 0.0F : this.S(var18, var12, var4, var5);
         this.l(var19);
         this.x(var3, var14 + this.K, var15, var19);
         float var20 = this.w.v(var18, (boolean)var7);
         float var21 = this.w.b(ti.y.X) * 0.07F;
         float var22 = this.w.b(ti.y.G) * 0.055F;
         float var23 = this.w.O(ti.y.X, (boolean)var7);
         float var24 = this.w.O(ti.y.G, (boolean)var7);
         float var25 = this.g(this.u - var1.R, 20.0F, 54.0F, 42.0F, var20, var21, var23, (boolean)var7, var5);
         float var26 = this.g(this.B - var1.x, 14.0F, 42.0F, 28.0F, var20, var22, var24, (boolean)var7, var5);
         if (var7 == 0) {
            var26 += this.w.d(var25);
         }

         return new u(var1.R + var25, Mth.clamp(var1.x + var26, -90.0F, 90.0F));
      } else {
         return var1 != null ? var1 : var2;
      }
   }

   public u u(u var1, u var2) {
      return this.H.M(var1, var2);
   }

   private float C(u var1, u var2, double var3, boolean var5) {
      if (!var5 && !(var3 >= 0.16F)) {
         return this.n ? this.z : var1.R;
      }

      this.z = var2.R;
      this.n = (boolean)1;
      return this.z;
   }

   private float S(float var1, double var2, boolean var4, boolean var5) {
      if (!(var2 < 0.16F) && (var4 || var5)) {
         float var6 = 1.0F - Mth.clamp(var1 / 14.0F, 0.0F, 1.0F);
         return var6 * var6 * (var4 ? 0.35F : 0.65F);
      } else {
         return 0.0F;
      }
   }

   private void l(float var1) {
      if (var1 <= 1.0E-5F) {
         this.N *= 0.5F;
         this.m *= 0.5F;
         this.q = 0.0F;
         this.v = 0.0F;
      } else {
         float var2 = this.m() * (0.22F + var1 * 0.22F);
         float var3 = this.m() * (0.15F + var1 * 0.15F);
         this.q = Mth.clamp(this.q * 0.68F + var2, -0.65F, 0.65F);
         this.v = Mth.clamp(this.v * 0.68F + var3, -0.45F, 0.45F);
         this.N = Mth.clamp(this.N + this.q, -4.1F, 4.1F);
         this.m = Mth.clamp(this.m + this.v, -2.85F, 2.85F);
      }
   }

   private void x(LivingEntity var1, float var2, float var3, float var4) {
      if (var4 <= 1.0E-5F) {
         this.u = var2;
         this.B = var3;
      } else {
         float var5 = var2 + this.N * var4;
         float var6 = Mth.clamp(var3 + this.m * var4, -90.0F, 90.0F);
         if (S(var1, var5, var6)) {
            this.u = var5;
            this.B = var6;
         } else {
            this.N *= 0.5F;
            this.m *= 0.5F;
            var5 = var2 + this.N * var4;
            var6 = Mth.clamp(var3 + this.m * var4, -90.0F, 90.0F);
            if (S(var1, var5, var6)) {
               this.u = var5;
               this.B = var6;
            } else {
               this.N = this.m = this.q = this.v = 0.0F;
               this.u = var2;
               this.B = var3;
            }
         }
      }
   }

   private float g(float var1, float var2, float var3, float var4, float var5, float var6, float var7, boolean var8, boolean var9) {
      float var10 = Math.abs(var1);
      if (var10 <= 1.0E-5F) {
         return 0.0F;
      }

      float var11 = Mth.clamp(var10 / var4, 0.0F, 1.0F);
      float var12 = var11 * var11 * (3.0F - 2.0F * var11);
      float var13 = Math.min(var10, var2 + (var3 - var2) * var12);
      float var14;
      if (!var8 && var9) {
         float var15 = 0.78F + (float)this.R.nextDouble() * 0.22000003F;
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

   private float m() {
      return (float)(this.R.nextDouble() * 2.0 - 1.0);
   }

   private static boolean S(LivingEntity var0, float var1, float var2) {
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

      Vec3 var13 = tv.Z(var1, var2);
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
      this.n = (boolean)0;
      this.z = 0.0F;
      this.N = this.m = this.q = this.v = 0.0F;
      this.u = this.B = 0.0F;
      this.K = 0.0F;
      this.F = 0;
      this.w.l();
      this.H.g();
   }

   @Override
   public void targetChanged(LivingEntity var1) {
      this.reset();
   }
}
