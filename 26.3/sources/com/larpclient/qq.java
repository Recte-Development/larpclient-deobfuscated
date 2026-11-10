package com.larpclient;

import net.minecraft.world.phys.AABB;

public record qq(int storage, int utilities, int crafted, int altered, int dedicated, int sampleY, String clue, AABB bounds, int enderChests) {
   public static final qq EMPTY = new qq(0, 0, 0, 0, 0, 0, "");

   public qq(int var1, int var2, int var3, int var4, int var5, int var6, String var7, AABB var8) {
      this(var1, var2, var3, var4, var5, var6, var7, var8, 0);
   }

   public qq(int var1, int var2, int var3, int var4, int var5, int var6, String var7) {
      this(var1, var2, var3, var4, var5, var6, var7, null);
   }

   public boolean qualifies(String var1, int var2, int var3) {
      return this.qualifies(var1, var2, var3, 1);
   }

   public boolean qualifies(String var1, int var2, int var3, int var4) {
      if (this.dedicated > this.enderChests || this.enderChests >= var4) {
         return true;
      } else if (var1.equals("Conservative")) {
         return false;
      } else {
         return this.storage >= var2 && this.utilities >= var3
            ? true
            : var1.equals("Broad") && this.storage >= Math.max(2, var2 / 2) && (this.utilities > 0 || this.crafted >= 16 || this.altered >= 4);
      }
   }

   public int weight() {
      return Math.min(this.dedicated, 8) * 32
         + Math.min(this.storage, 32) * 3
         + Math.min(this.utilities, 16) * 2
         + Math.min(this.crafted, 32) / 4
         + Math.min(this.altered, 8);
   }

   public String counts() {
      String var1 = this.storage + " storage, " + this.utilities + " utility";
      if (this.enderChests > 0) {
         var1 = var1 + ", " + this.enderChests + " ender chests";
      }

      if (this.crafted > 0) {
         var1 = var1 + ", " + this.crafted + " crafted";
      }

      if (this.altered > 0) {
         var1 = var1 + ", " + this.altered + " terrain clues";
      }

      return var1;
   }

   public String describe() {
      return (this.dedicated > 0 ? this.clue + " · " : "Possible build · ") + this.counts() + " · Y:" + this.sampleY;
   }
}
