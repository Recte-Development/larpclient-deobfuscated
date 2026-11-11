package com.larpclient.module.combat;

import com.larpclient.util.entity.SwingHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;

public final class AttackWobble {
   private static final long durationMs = 350L;
   private int tickCount;
   private int baseInterval = 40;
   private int nextInterval = 35;
   private boolean active;
   private long startTime;
   private int swingCount;
   private float yawOffset;
   private float pitchOffset;

   public void incrementTicks() {
      this.tickCount++;
   }

   public void setInterval(int var1) {
      this.baseInterval = Math.max(10, var1);
      if (this.nextInterval == 35 || this.nextInterval < 10) {
         this.randomizeInterval();
      }
   }

   private void randomizeInterval() {
      int var1 = Math.max(2, this.baseInterval / 5);
      this.nextInterval = this.baseInterval + (int)((Math.random() - 0.5) * 2.0 * var1);
   }

   public void update() {
      if (!this.active && this.tickCount >= this.nextInterval) {
         this.active = (boolean)1;
         this.startTime = System.currentTimeMillis();
         this.tickCount = 0;
         this.swingCount = 0;
         this.randomizeInterval();
         float var1 = Math.random() < 0.5 ? -1.0F : 1.0F;
         float var2 = Math.random() < 0.5 ? -1.0F : 1.0F;
         this.yawOffset = var1 * (2.2F + (float)(Math.random() * 2.3));
         this.pitchOffset = var2 * (1.2F + (float)(Math.random() * 1.8));
      }

      if (this.active) {
         long var5 = System.currentTimeMillis() - this.startTime;
         Minecraft var3 = Minecraft.getInstance();
         LocalPlayer var4 = var3 == null ? null : var3.player;
         if (var4 != null && this.swingCount == 0 && var5 >= 60L) {
            SwingHelper.swingAttack(var4, InteractionHand.MAIN_HAND);
            this.swingCount++;
         }

         if (var5 >= 350L) {
            this.active = (boolean)0;
            this.yawOffset = 0.0F;
            this.pitchOffset = 0.0F;
         }
      }
   }

   public Rotation applyOffset(Rotation var1) {
      if (var1 != null && this.active) {
         long var2 = System.currentTimeMillis() - this.startTime;
         float var4 = Math.min(1.0F, (float)var2 / 350.0F);
         float var5 = (float)Math.sin(var4 * Math.PI);
         return new Rotation(var1.yaw + this.yawOffset * var5, Mth.clamp(var1.pitch + this.pitchOffset * var5, -90.0F, 90.0F));
      } else {
         return var1;
      }
   }

   public void reset() {
      this.tickCount = 0;
      this.active = (boolean)0;
      this.swingCount = 0;
      this.yawOffset = 0.0F;
      this.pitchOffset = 0.0F;
      this.randomizeInterval();
   }

   public boolean isActive() {
      return this.active;
   }
}
