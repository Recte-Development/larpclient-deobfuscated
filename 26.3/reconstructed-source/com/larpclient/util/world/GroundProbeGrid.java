package com.larpclient.util.world;

import net.minecraft.world.phys.Vec3;

public final class GroundProbeGrid {
   public static final int cellRadius = 12;
   public static final int maxRange = 48;
   public static final double probeAbove = 1.25;
   public static final double probeBelow = 4.5;
   private static final int gridSize = 25;
   private final float[] heights = new float[625];
   private final byte[] cellStates = new byte[625];
   private final int[] queue = new int[625];
   private final int originX;
   private final int originZ;
   private final double originY;
   private int processedCount;
   private int queuedCount;

   public GroundProbeGrid(Vec3 var1) {
      this.originX = (int)Math.floor(var1.x);
      this.originZ = (int)Math.floor(var1.z);
      this.originY = var1.y;
   }

   private int cellIndex(double var1, double var3) {
      int var5 = (int)Math.floor(var1) - this.originX + 12;
      int var6 = (int)Math.floor(var3) - this.originZ + 12;
      return var5 >= 0 && var6 >= 0 && var5 < 25 && var6 < 25 ? var5 * 25 + var6 : -1;
   }

   public void enqueue(double var1, double var3) {
      int var5 = this.cellIndex(var1, var3);
      if (var5 >= 0 && this.cellStates[var5] == 0) {
         this.cellStates[var5] = 1;
         this.queue[this.queuedCount++] = var5;
      }
   }

   public void enqueueRadius(double var1, double var3, double var5) {
      int var7 = (int)Math.ceil(var5);

      for (int var8 = -var7; var8 <= var7; var8++) {
         for (int var9 = -var7; var9 <= var7; var9++) {
            if (var8 * var8 + var9 * var9 <= (var5 + 0.75) * (var5 + 0.75)) {
               this.enqueue(var1 + var8, var3 + var9);
            }
         }
      }
   }

   public void processQueue(BlockRaycast.RayHitTester var1, int var2) {
      while (this.processedCount < this.queuedCount && var2-- > 0) {
         int var3 = this.queue[this.processedCount++];
         int var4 = var3 / 25 - 12;
         int var5 = var3 % 25 - 12;
         double var6 = this.originX + var4 + 0.5;
         double var8 = this.originZ + var5 + 0.5;
         BlockRaycast.RayHit var10 = var1 == null ? null : var1.test(new Vec3(var6, this.originY + 1.25, var8), new Vec3(var6, this.originY - 4.5, var8));
         this.heights[var3] = var10 != null && var10.normal().y > 0.5 ? (float)var10.point().y : Float.NaN;
         this.cellStates[var3] = 2;
      }
   }

   public double getGroundHeight(double var1, double var3) {
      int var5 = this.cellIndex(var1, var3);
      return var5 >= 0 && this.cellStates[var5] == 2 ? this.heights[var5] : Double.NaN;
   }

   public double getCenterHeight() {
      double var1 = this.getGroundHeight(this.originX + 0.5, this.originZ + 0.5);
      return Double.isNaN(var1) ? this.originY : var1;
   }

   public int pendingCount() {
      return this.queuedCount - this.processedCount;
   }
}
