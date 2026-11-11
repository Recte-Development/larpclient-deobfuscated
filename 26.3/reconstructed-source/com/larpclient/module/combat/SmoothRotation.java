package com.larpclient.module.combat;

import java.util.Arrays;
import java.util.Random;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;

public final class SmoothRotation implements RotationProvider {
   private final Random random = new Random();
   private final float[] pitchHistory = new float[30];
   private int ticksSinceAttack;
   private int attackCount;
   private int attackGraceTicks;

   public SmoothRotation() {
      this.reset();
   }

   @Override
   public Rotation rotate(Rotation var1, Rotation var2, LivingEntity var3, boolean var4, boolean var5) {
      if (var1 != null && var2 != null) {
         Minecraft var6 = Minecraft.getInstance();
         System.arraycopy(this.pitchHistory, 0, this.pitchHistory, 1, 29);
         this.pitchHistory[0] = var2.pitch;
         this.ticksSinceAttack++;
         if (var4) {
            this.attackGraceTicks = 2;
         } else if (this.attackGraceTicks > 0) {
            this.attackGraceTicks--;
         }

         float var7 = (var6.player != null ? var6.player.tickCount : 0) + var6.getDeltaTracker().getGameTimeDeltaPartialTick(false);
         float var8 = (float)(Math.sin(var7 * 0.4000000008323731) * 3.0 + Math.sin(var7 * 0.9500002390239708 + 1.4000004888461306) * 2.0);
         float var9 = (float)(Math.cos(var7 * 0.5F + 0.7000001555309916) * 0.5 + Math.cos(var7 * 0.7800000620494261 + 3.10000031689524) * 1.5);
         int var10 = Mth.clamp(10 - this.ticksSinceAttack, 0, 29);
         float var11 = this.pitchHistory[var10] + var9 * 1.5F;
         float var12 = this.approachAngle(var1.pitch, var11, this.randomRange(0.1F, 0.5F));
         float var13 = this.approachAngle(var1.yaw, var2.yaw + var8, this.randomRange(0.1F, 0.4F));
         if (var4 || this.attackGraceTicks > 0) {
            if (!var5) {
               var13 = var2.yaw;
               var12 = var2.pitch;
            }

            var8 = Mth.clamp(var8, -0.05F, 0.05F);
            var9 = Mth.clamp(var9, -0.05F, 0.05F);
         }

         if (this.ticksSinceAttack <= 4 && this.attackCount % 2 == 0 && var6.player != null && !var4) {
            var13 = AuraModule.getInstance();
         }

         if (var4) {
            this.attackCount++;
            this.ticksSinceAttack = 0;
         }

         return new Rotation(var13 + var8, Mth.clamp(var12 + var9, -90.0F, 90.0F));
      } else {
         return var1 != null ? var1 : var2;
      }
   }

   private float approachAngle(float var1, float var2, float var3) {
      float var4 = Mth.clamp(var3, 0.0F, 1.0F);
      float var5 = RotationMath.angleDelta(var1, var2);
      if (Math.abs(var5) < 0.5F) {
         return var2;
      }

      float var6 = RotationMath.snapAngle(var1, var1 + var5 * var4);
      return Math.abs(RotationMath.angleDelta(var6, var2)) < 0.5F ? var2 : var6;
   }

   private float randomRange(float var1, float var2) {
      return var1 + this.random.nextFloat() * (var2 - var1);
   }

   @Override
   public int ticksToReach(Rotation var1, Rotation var2) {
      if (var1 != null && var2 != null) {
         float var3 = Math.abs(RotationMath.angleDelta(var1.yaw, var2.yaw));
         float var4 = Math.abs(var2.pitch - var1.pitch);
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
      Arrays.fill(this.pitchHistory, hasPlayer() ? AuraModule.getViewPitch() : 0.0F);
      this.ticksSinceAttack = 0;
      this.attackCount = 0;
      this.attackGraceTicks = 0;
   }

   private static boolean hasPlayer() {
      Minecraft var0 = Minecraft.getInstance();
      return var0 != null && var0.player != null;
   }

   @Override
   public void targetChanged(LivingEntity var1) {
      Arrays.fill(this.pitchHistory, hasPlayer() ? AuraModule.getViewPitch() : 0.0F);
      this.ticksSinceAttack = 0;
   }
}
