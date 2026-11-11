package com.larpclient.util.data;

import java.nio.ByteBuffer;
import java.util.Arrays;
import net.minecraft.world.phys.Vec3;

public final class TrailBuffer {
   public static final int mAX_POINTS = 48;
   public static final int dEFAULT_LIFETIME_MS = 800;
   private final Vec3[] points = new Vec3[47];
   private final double[] timestamps = new double[47];
   private int count;
   private int head;
   private int lastIndex = -1;
   private Vec3 latestPos;
   private Vec3 lastSampled;
   private Vec3 currentPos;
   private double lastTime = Double.NaN;

   public int size() {
      return this.count;
   }

   public void clear() {
      Arrays.fill(this.points, null);
      this.count = this.head = 0;
      this.latestPos = this.lastSampled = this.currentPos = null;
      this.lastIndex = -1;
      this.lastTime = Double.NaN;
   }

   public void record_(Vec3 var1, boolean var2, double var3, double var5) {
      if (Double.isFinite(var3) && Double.isFinite(var1.x + var1.y + var1.z)) {
         if (Double.isFinite(this.lastTime) && (var3 < this.lastTime || this.latestPos != null && this.latestPos.distanceToSqr(var1) > 9.0)) {
            this.clear();
         }

         this.lastTime = var3;
         this.latestPos = var1;

         for (int var7 = 0; var7 < this.points.length; var7++) {
            if (this.points[var7] != null && var3 - this.timestamps[var7] >= var5) {
               this.points[var7] = null;
               this.count--;
            }
         }

         this.currentPos = var2 ? var1 : null;
         if (!var2) {
            this.lastSampled = null;
         } else if (this.lastSampled != null && this.lastSampled.distanceToSqr(var1) < 0.2025 && this.points[this.lastIndex] != null) {
            this.timestamps[this.lastIndex] = var3;
         } else {
            if (this.points[this.head] == null) {
               this.count++;
            }

            this.points[this.head] = var1;
            this.timestamps[this.head] = var3;
            this.lastIndex = this.head;
            this.head = (this.head + 1) % this.points.length;
            this.lastSampled = var1;
         }
      } else {
         this.clear();
      }
   }

   public void size(ByteBuffer var1, Vec3 var2, Vec3 var3, double var4, double var6, boolean var8) {
      int var9 = 0;
      float var10 = Float.POSITIVE_INFINITY;
      float var11 = var10;
      float var12 = Float.NEGATIVE_INFINITY;
      float var13 = var12;
      int var14 = var1.position();
      var1.position(var14 + 32);

      for (int var15 = -1; var15 < this.points.length; var15++) {
         Vec3 var16 = var15 < 0 ? (this.currentPos == null ? null : var3) : this.points[var15];
         float var17 = var15 < 0 ? 1.0F : (float)Math.clamp(1.0 - (var4 - this.timestamps[var15]) / Math.max(0.1, var6), 0.0, 1.0);
         var17 = var17 >= 0.35F ? 1.0F : var17 / 0.35F;
         if (var8 && var16 != null && !(var17 <= 0.0F) && !(var16.distanceToSqr(var2) > 9216.0)) {
            float var18 = (float)(var16.x - var2.x);
            float var19 = (float)(var16.y - var2.y);
            float var20 = (float)(var16.z - var2.z);
            var1.putFloat(var18).putFloat(var19).putFloat(var20).putFloat(var17);
            var10 = Math.min(var10, var18);
            var12 = Math.max(var12, var18);
            var11 = Math.min(var11, var20);
            var13 = Math.max(var13, var20);
            var9++;
         }
      }

      for (int var21 = var9; var21 < 48; var21++) {
         var1.putLong(0L).putLong(0L);
      }

      int var22 = var1.position();
      var1.position(var14);
      var1.putFloat(var9).putFloat(0.0F).putFloat(0.0F).putFloat(0.0F);
      var1.putFloat(var9 == 0 ? 0.0F : var10 - 1.8F)
         .putFloat(var9 == 0 ? 0.0F : var11 - 1.8F)
         .putFloat(var9 == 0 ? 0.0F : var12 + 1.8F)
         .putFloat(var9 == 0 ? 0.0F : var13 + 1.8F);
      var1.position(var22);
   }
}
