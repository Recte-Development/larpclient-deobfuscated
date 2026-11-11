package com.larpclient.util.world;

import java.util.Locale;

public final class LayerScanPlan {
   private final LayerScanPlan.Region region;
   private final int layerHeight;
   private final int laneWidth;
   private final boolean rowsAlongX;
   private final boolean startAtLowLane;
   private final boolean startAtLowRow;

   public LayerScanPlan(LayerScanPlan.Region var1, int var2, int var3, int var4, int var5) {
      this.region = var1;
      this.layerHeight = Math.max(1, var2);
      this.laneWidth = Math.max(1, var3);
      this.rowsAlongX = var1.rowsAlongX();
      int var6 = this.rowsAlongX ? var4 : var5;
      int var7 = this.rowsAlongX ? var5 : var4;
      this.startAtLowRow = (boolean)(Math.abs(var6 - this.rowStart()) <= Math.abs(var6 - this.getRegion()) ? 1 : 0);
      this.startAtLowLane = (boolean)(Math.abs(var7 - this.laneStart()) <= Math.abs(var7 - this.laneEnd()) ? 1 : 0);
   }

   public LayerScanPlan.Region getRegion() {
      return this.region;
   }

   public boolean rowDirection() {
      return this.rowsAlongX;
   }

   public int getLayerHeight() {
      return this.layerHeight;
   }

   public int getLaneWidth() {
      return this.laneWidth;
   }

   public int layerCount() {
      return (this.region.sizeY() + this.layerHeight - 1) / this.layerHeight;
   }

   public LayerScanPlan.Layer layerAt(int var1) {
      int var2 = this.region.maxY - var1 * this.layerHeight;
      return new LayerScanPlan.Layer(var1, Math.max(this.region.minY, var2 - this.layerHeight + 1), var2);
   }

   public int layerIndexOf(int var1) {
      return var1 <= this.region.maxY && var1 >= this.region.minY ? (this.region.maxY - var1) / this.layerHeight : -1;
   }

   private int rowStart() {
      return this.rowsAlongX ? this.region.minX : this.region.minZ;
   }

   private int getRegion() {
      return this.rowsAlongX ? this.region.maxX : this.region.maxZ;
   }

   private int laneStart() {
      return this.rowsAlongX ? this.region.minZ : this.region.minX;
   }

   private int laneEnd() {
      return this.rowsAlongX ? this.region.maxZ : this.region.maxX;
   }

   public int rowDirection() {
      return (this.laneEnd() - this.laneStart() + this.laneWidth) / this.laneWidth;
   }

   public int laneIndexOf(int var1, int var2, int var3) {
      int var4 = this.rowsAlongX ? var2 : var1;
      int var5 = this.startAtLowLane ? var4 - this.laneStart() : this.laneEnd() - var4;
      int var6 = var5 / this.laneWidth;
      return var3 % 2 == 0 ? var6 : this.rowDirection() - 1 - var6;
   }

   public int[] laneBounds(int var1, int var2) {
      int var3 = var2 % 2 == 0 ? var1 : this.rowDirection() - 1 - var1;
      int var4 = this.startAtLowLane ? this.laneStart() + var3 * this.laneWidth : this.laneEnd() - var3 * this.laneWidth;
      int var5 = this.startAtLowLane ? Math.min(this.laneEnd(), var4 + this.laneWidth - 1) : Math.max(this.laneStart(), var4 - this.laneWidth + 1);
      return new int[]{Math.min(var4, var5), Math.max(var4, var5)};
   }

   public int rowDirection(int var1, int var2) {
      int var3 = var1 + this.laneParity(var2);
      int var4 = this.startAtLowRow == (var3 % 2 == 0 ? 1 : 0) ? 1 : 0;
      return var4 != 0 ? 1 : -1;
   }

   private int laneParity(int var1) {
      return (int)((long)this.rowDirection() * var1 % 2L);
   }

   public long sortKey(int var1, int var2, int var3, int var4) {
      int var5 = this.laneIndexOf(var1, var3, var4);
      int var6 = this.rowsAlongX ? var1 : var3;
      int var7 = this.rowDirection(var5, var4);
      int var8 = var7 > 0 ? var6 - this.rowStart() : this.getRegion() - var6;
      int var9 = this.region.maxY - var2;
      return (long)var5 << 40 | (long)var8 << 12 | Math.min(var9, 4095);
   }

   public boolean isThinLane(int var1, int var2) {
      int[] var3 = this.laneBounds(var1, var2);
      LayerScanPlan.Layer var4 = this.layerAt(var2);
      return var3[1] - var3[0] == 2 && var4.height() == 3;
   }

   public int laneCenter(int var1, int var2) {
      int[] var3 = this.laneBounds(var1, var2);
      return (var3[0] + var3[1]) / 2;
   }

   public static int[] parseCoords(String var0) {
      if (var0 == null) {
         return null;
      }

      String[] var1 = var0.trim().replace(',', ' ').split("\\s+");
      if (var1.length != 3) {
         return null;
      }

      try {
         return new int[]{Integer.parseInt(var1[0]), Integer.parseInt(var1[1]), Integer.parseInt(var1[2])};
      } catch (NumberFormatException var3) {
         return null;
      }
   }

   public static String formatCoords(int var0, int var1, int var2) {
      return var0 + " " + var1 + " " + var2;
   }

   public static boolean isWithinReach(int var0, int var1, int var2) {
      return var0 - (var1 + 1) <= var2;
   }

   public record Layer(int index, int bottom, int top) {
      public int height() {
         return this.top - this.bottom + 1;
      }

      public boolean contains(int var1) {
         return var1 >= this.bottom && var1 <= this.top;
      }
   }

   public record Region(int minX, int minY, int minZ, int maxX, int maxY, int maxZ) {
      public static LayerScanPlan.Region fromCorners(int[] var0, int[] var1) {
         return new LayerScanPlan.Region(
            Math.min(var0[0], var1[0]),
            Math.min(var0[1], var1[1]),
            Math.min(var0[2], var1[2]),
            Math.max(var0[0], var1[0]),
            Math.max(var0[1], var1[1]),
            Math.max(var0[2], var1[2])
         );
      }

      public int sizeX() {
         return this.maxX - this.minX + 1;
      }

      public int sizeY() {
         return this.maxY - this.minY + 1;
      }

      public int sizeZ() {
         return this.maxZ - this.minZ + 1;
      }

      public long volume() {
         return (long)this.sizeX() * this.sizeY() * this.sizeZ();
      }

      public boolean contains(int var1, int var2, int var3) {
         return var1 >= this.minX && var1 <= this.maxX && var2 >= this.minY && var2 <= this.maxY && var3 >= this.minZ && var3 <= this.maxZ;
      }

      public boolean containsColumn(int var1, int var2) {
         return var1 >= this.minX && var1 <= this.maxX && var2 >= this.minZ && var2 <= this.maxZ;
      }

      public boolean rowsAlongX() {
         return this.sizeX() >= this.sizeZ();
      }

      public String describe() {
         return String.format(Locale.ROOT, "%d x %d x %d (%,d blocks)", this.sizeX(), this.sizeY(), this.sizeZ(), this.volume());
      }
   }
}
