package com.larpclient.module;

import java.util.Arrays;
import java.util.Random;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;

public final class lb implements lg {
   private final Random a = new Random();
   private final float[] O = new float[30];
   private int k;
   private int q;
   private int T;

   public lb() {
      this.reset();
   }

   @Override
   public lx rotate(lx var1, lx var2, LivingEntity var3, boolean var4, boolean var5) {
      if (var1 != null && var2 != null) {
         Minecraft var6 = Minecraft.getInstance();
         System.arraycopy(this.O, 0, this.O, 1, 29);
         this.O[0] = var2.I;
         this.k++;
         if (var4) {
            this.T = 2;
         } else if (this.T > 0) {
            this.T--;
         }

         float var7 = (var6.player != null ? var6.player.tickCount : 0) + var6.getDeltaTracker().getGameTimeDeltaPartialTick(false);
         float var8 = (float)(Math.sin(var7 * 0.4000000008323731) * 3.0 + Math.sin(var7 * 0.9500002390239708 + 1.4000004888461306) * 2.0);
         float var9 = (float)(Math.cos(var7 * 0.5F + 0.7000001555309916) * 0.5 + Math.cos(var7 * 0.7800000620494261 + 3.10000031689524) * 1.5);
         int var10 = Mth.clamp(10 - this.k, 0, 29);
         float var11 = this.O[var10] + var9 * 1.5F;
         float var12 = this.U(var1.I, var11, this.s(0.1F, 0.5F));
         float var13 = this.U(var1.z, var2.z + var8, this.s(0.1F, 0.4F));
         if (var4 || this.T > 0) {
            if (!var5) {
               var13 = var2.z;
               var12 = var2.I;
            }

            var8 = Mth.clamp(var8, -0.05F, 0.05F);
            var9 = Mth.clamp(var9, -0.05F, 0.05F);
         }

         if (this.k <= 4 && this.q % 2 == 0 && var6.player != null && !var4) {
            var13 = AuraModule.s();
         }

         if (var4) {
            this.q++;
            this.k = 0;
         }

         return new lx(var13 + var8, Mth.clamp(var12 + var9, -90.0F, 90.0F));
      } else {
         return var1 != null ? var1 : var2;
      }
   }

   private float U(float var1, float var2, float var3) {
      float var4 = Mth.clamp(var3, 0.0F, 1.0F);
      float var5 = le.P(var1, var2);
      if (Math.abs(var5) < 0.5F) {
         return var2;
      }

      float var6 = le.p(var1, var1 + var5 * var4);
      return Math.abs(le.P(var6, var2)) < 0.5F ? var2 : var6;
   }

   private float s(float var1, float var2) {
      return var1 + this.a.nextFloat() * (var2 - var1);
   }

   @Override
   public int ticksToReach(lx var1, lx var2) {
      if (var1 != null && var2 != null) {
         float var3 = Math.abs(le.P(var1.z, var2.z));
         float var4 = Math.abs(var2.I - var1.I);
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
      Arrays.fill(this.O, A() ? AuraModule.K() : 0.0F);
      this.k = 0;
      this.q = 0;
      this.T = 0;
   }

   private static boolean A() {
      Minecraft var0 = Minecraft.getInstance();
      return var0 != null && var0.player != null;
   }

   @Override
   public void targetChanged(LivingEntity var1) {
      Arrays.fill(this.O, A() ? AuraModule.K() : 0.0F);
      this.k = 0;
   }
}
