package com.larpclient.module;

import java.util.Arrays;
import java.util.Random;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;

public final class tf implements x {
   private final Random p = new Random();
   private final float[] C = new float[30];
   private int l;
   private int M;
   private int d;

   public tf() {
      this.reset();
   }

   @Override
   public u rotate(u var1, u var2, LivingEntity var3, boolean var4, boolean var5) {
      if (var1 != null && var2 != null) {
         Minecraft var6 = Minecraft.getInstance();
         System.arraycopy(this.C, 0, this.C, 1, 29);
         this.C[0] = var2.x;
         this.l++;
         if (var4) {
            this.d = 2;
         } else if (this.d > 0) {
            this.d--;
         }

         float var7 = (var6.player != null ? var6.player.tickCount : 0) + var6.getDeltaTracker().getGameTimeDeltaPartialTick(false);
         float var8 = (float)(Math.sin(var7 * 0.4000000008323731) * 3.0 + Math.sin(var7 * 0.9500002390239708 + 1.4000004888461306) * 2.0);
         float var9 = (float)(Math.cos(var7 * 0.5F + 0.7000001555309916) * 0.5 + Math.cos(var7 * 0.7800000620494261 + 3.10000031689524) * 1.5);
         int var10 = Mth.clamp(10 - this.l, 0, 29);
         float var11 = this.C[var10] + var9 * 1.5F;
         float var12 = this.m(var1.x, var11, this.e(0.1F, 0.5F));
         float var13 = this.m(var1.R, var2.R + var8, this.e(0.1F, 0.4F));
         if (var4 || this.d > 0) {
            if (!var5) {
               var13 = var2.R;
               var12 = var2.x;
            }

            var8 = Mth.clamp(var8, -0.05F, 0.05F);
            var9 = Mth.clamp(var9, -0.05F, 0.05F);
         }

         if (this.l <= 4 && this.M % 2 == 0 && var6.player != null && !var4) {
            var13 = AuraModule.z();
         }

         if (var4) {
            this.M++;
            this.l = 0;
         }

         return new u(var13 + var8, Mth.clamp(var12 + var9, -90.0F, 90.0F));
      } else {
         return var1 != null ? var1 : var2;
      }
   }

   private float m(float var1, float var2, float var3) {
      float var4 = Mth.clamp(var3, 0.0F, 1.0F);
      float var5 = tv.p(var1, var2);
      if (Math.abs(var5) < 0.5F) {
         return var2;
      }

      float var6 = tv.e(var1, var1 + var5 * var4);
      return Math.abs(tv.p(var6, var2)) < 0.5F ? var2 : var6;
   }

   private float e(float var1, float var2) {
      return var1 + this.p.nextFloat() * (var2 - var1);
   }

   @Override
   public int ticksToReach(u var1, u var2) {
      if (var1 != null && var2 != null) {
         float var3 = Math.abs(tv.p(var1.R, var2.R));
         float var4 = Math.abs(var2.x - var1.x);
         return Mth.clamp((int)Math.ceil(Math.max(var3 / 45.0F, var4 / 25.0F)), 0, 8);
      } else {
         return 0;
      }
   }

   @Override
   public float yawSpeed() {
      return 45.0F;
   }

   @Override
   public float pitchSpeed() {
      return 25.0F;
   }

   @Override
   public void reset() {
      Arrays.fill(this.C, k() ? AuraModule.p() : 0.0F);
      this.l = 0;
      this.M = 0;
      this.d = 0;
   }

   private static boolean k() {
      Minecraft var0 = Minecraft.getInstance();
      return var0 != null && var0.player != null;
   }

   @Override
   public void targetChanged(LivingEntity var1) {
      Arrays.fill(this.C, k() ? AuraModule.p() : 0.0F);
      this.l = 0;
   }
}
