package com.larpclient.module;

import java.util.Arrays;
import java.util.Random;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;

public final class x implements t {
   private final Random q = new Random();
   private final float[] H = new float[30];
   private int a;
   private int C;
   private int u;

   public x() {
      this.reset();
   }

   @Override
   public d5 rotate(d5 var1, d5 var2, LivingEntity var3, boolean var4, boolean var5) {
      if (var1 != null && var2 != null) {
         Minecraft var6 = Minecraft.getInstance();
         System.arraycopy(this.H, 0, this.H, 1, 29);
         this.H[0] = var2.G;
         this.a++;
         if (var4) {
            this.u = 2;
         } else if (this.u > 0) {
            this.u--;
         }

         float var7 = (var6.player != null ? var6.player.tickCount : 0) + var6.getDeltaTracker().getGameTimeDeltaPartialTick(false);
         float var8 = (float)(Math.sin(var7 * 0.4000000008323731) * 3.0 + Math.sin(var7 * 0.9500002390239708 + 1.4000004888461306) * 2.0);
         float var9 = (float)(Math.cos(var7 * 0.5F + 0.7000001555309916) * 0.5 + Math.cos(var7 * 0.7800000620494261 + 3.10000031689524) * 1.5);
         int var10 = Mth.clamp(10 - this.a, 0, 29);
         float var11 = this.H[var10] + var9 * 1.5F;
         float var12 = this.y(var1.G, var11, this.x(0.1F, 0.5F));
         float var13 = this.y(var1.M, var2.M + var8, this.x(0.1F, 0.4F));
         if (var4 || this.u > 0) {
            if (!var5) {
               var13 = var2.M;
               var12 = var2.G;
            }

            var8 = Mth.clamp(var8, -0.05F, 0.05F);
            var9 = Mth.clamp(var9, -0.05F, 0.05F);
         }

         if (this.a <= 4 && this.C % 2 == 0 && var6.player != null && !var4) {
            var13 = AuraModule.J();
         }

         if (var4) {
            this.C++;
            this.a = 0;
         }

         return new d5(var13 + var8, Mth.clamp(var12 + var9, -90.0F, 90.0F));
      } else {
         return var1 != null ? var1 : var2;
      }
   }

   private float y(float var1, float var2, float var3) {
      float var4 = Mth.clamp(var3, 0.0F, 1.0F);
      float var5 = dj.h(var1, var2);
      if (Math.abs(var5) < 0.5F) {
         return var2;
      }

      float var6 = dj.N(var1, var1 + var5 * var4);
      return Math.abs(dj.h(var6, var2)) < 0.5F ? var2 : var6;
   }

   private float x(float var1, float var2) {
      return var1 + this.q.nextFloat() * (var2 - var1);
   }

   @Override
   public int ticksToReach(d5 var1, d5 var2) {
      if (var1 != null && var2 != null) {
         float var3 = Math.abs(dj.h(var1.M, var2.M));
         float var4 = Math.abs(var2.G - var1.G);
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
      Arrays.fill(this.H, H() ? AuraModule.R() : 0.0F);
      this.a = 0;
      this.C = 0;
      this.u = 0;
   }

   private static boolean H() {
      Minecraft var0 = Minecraft.getInstance();
      return var0 != null && var0.player != null;
   }

   @Override
   public void targetChanged(LivingEntity var1) {
      Arrays.fill(this.H, H() ? AuraModule.R() : 0.0F);
      this.a = 0;
   }
}
