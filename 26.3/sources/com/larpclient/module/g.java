package com.larpclient.module;

import java.util.Arrays;
import java.util.Random;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;

public final class g implements k {
   private final Random U = new Random();
   private final float[] s = new float[30];
   private int v;
   private int y;
   private int m;

   public g() {
      this.reset();
   }

   @Override
   public c rotate(c var1, c var2, LivingEntity var3, boolean var4, boolean var5) {
      if (var1 != null && var2 != null) {
         Minecraft var6 = Minecraft.getInstance();
         System.arraycopy(this.s, 0, this.s, 1, 29);
         this.s[0] = var2.T;
         this.v++;
         if (var4) {
            this.m = 2;
         } else if (this.m > 0) {
            this.m--;
         }

         float var7 = (var6.player != null ? var6.player.tickCount : 0) + var6.getDeltaTracker().getGameTimeDeltaPartialTick(false);
         float var8 = (float)(Math.sin(var7 * 0.4000000008323731) * 3.0 + Math.sin(var7 * 0.9500002390239708 + 1.4000004888461306) * 2.0);
         float var9 = (float)(Math.cos(var7 * 0.5F + 0.7000001555309916) * 0.5 + Math.cos(var7 * 0.7800000620494261 + 3.10000031689524) * 1.5);
         int var10 = Mth.clamp(10 - this.v, 0, 29);
         float var11 = this.s[var10] + var9 * 1.5F;
         float var12 = this.p(var1.T, var11, this.e(0.1F, 0.5F));
         float var13 = this.p(var1.V, var2.V + var8, this.e(0.1F, 0.4F));
         if (var4 || this.m > 0) {
            if (!var5) {
               var13 = var2.V;
               var12 = var2.T;
            }

            var8 = Mth.clamp(var8, -0.05F, 0.05F);
            var9 = Mth.clamp(var9, -0.05F, 0.05F);
         }

         if (this.v <= 4 && this.y % 2 == 0 && var6.player != null && !var4) {
            var13 = AuraModule.t();
         }

         if (var4) {
            this.y++;
            this.v = 0;
         }

         return new c(var13 + var8, Mth.clamp(var12 + var9, -90.0F, 90.0F));
      } else {
         return var1 != null ? var1 : var2;
      }
   }

   private float p(float var1, float var2, float var3) {
      float var4 = Mth.clamp(var3, 0.0F, 1.0F);
      float var5 = w.J(var1, var2);
      if (Math.abs(var5) < 0.5F) {
         return var2;
      }

      float var6 = w.c(var1, var1 + var5 * var4);
      return Math.abs(w.J(var6, var2)) < 0.5F ? var2 : var6;
   }

   private float e(float var1, float var2) {
      return var1 + this.U.nextFloat() * (var2 - var1);
   }

   @Override
   public int ticksToReach(c var1, c var2) {
      if (var1 != null && var2 != null) {
         float var3 = Math.abs(w.J(var1.V, var2.V));
         float var4 = Math.abs(var2.T - var1.T);
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
      Arrays.fill(this.s, C() ? AuraModule.N() : 0.0F);
      this.v = 0;
      this.y = 0;
      this.m = 0;
   }

   private static boolean C() {
      Minecraft var0 = Minecraft.getInstance();
      return var0 != null && var0.player != null;
   }

   @Override
   public void targetChanged(LivingEntity var1) {
      Arrays.fill(this.s, C() ? AuraModule.N() : 0.0F);
      this.v = 0;
   }
}
