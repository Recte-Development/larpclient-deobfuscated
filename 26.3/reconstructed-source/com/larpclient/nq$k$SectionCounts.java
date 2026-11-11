package com.larpclient;

import com.larpclient.util.world.BuildScanResult;
import net.minecraft.world.phys.AABB;

final class nq$k$SectionCounts {
   int storage;
   int utilities;
   int crafted;
   int altered;
   int dedicated;
   int sampleY;
   int enderChests;
   String clue = "";
   int minX = 16;
   int minY = Integer.MAX_VALUE;
   int minZ = 16;
   int maxX;
   int maxY = Integer.MIN_VALUE;
   int maxZ;

   nq$k$SectionCounts(int var1) {
      this.sampleY = var1;
   }

   void expandBounds(int var1, int var2, int var3) {
      this.minX = Math.min(this.minX, var1);
      this.minY = Math.min(this.minY, var2);
      this.minZ = Math.min(this.minZ, var3);
      this.maxX = Math.max(this.maxX, var1 + 1);
      this.maxY = Math.max(this.maxY, var2 + 1);
      this.maxZ = Math.max(this.maxZ, var3 + 1);
   }

   BuildScanResult toResult() {
      AABB var1 = this.minY == Integer.MAX_VALUE ? null : new AABB(this.minX, this.minY, this.minZ, this.maxX, this.maxY, this.maxZ);
      return new BuildScanResult(this.storage, this.utilities, this.crafted, this.altered, this.dedicated, this.sampleY, this.clue, var1, this.enderChests);
   }
}
