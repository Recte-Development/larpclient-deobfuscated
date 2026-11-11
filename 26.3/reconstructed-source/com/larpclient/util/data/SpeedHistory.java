package com.larpclient.util.data;

public final class SpeedHistory {
   public static final int cAPACITY = 120;
   private final double[] samples = new double[120];
   private int head;
   private int count;
   private double sum;
   private double lastX;
   private double lastZ;
   private Object owner;
   private Object level;
   private boolean hasLastPosition;

   public void reset() {
      this.head = this.count = 0;
      this.sum = 0.0;
      this.hasLastPosition = (boolean)0;
      this.owner = this.level = null;
   }

   public void resetPosition() {
      this.hasLastPosition = (boolean)0;
   }

   public void record_(Object var1, Object var2, double var3, double var5, double var7) {
      if (var1 != null && var2 != null && Double.isFinite(var3) && Double.isFinite(var5) && Double.isFinite(var7) && !(var7 <= 0.0)) {
         if (this.owner != var1 || this.level != var2) {
            this.reset();
            this.owner = var1;
            this.level = var2;
         }

         if (!this.hasLastPosition) {
            this.lastX = var3;
            this.lastZ = var5;
            this.hasLastPosition = (boolean)1;
         } else {
            double var9 = Math.hypot(var3 - this.lastX, var5 - this.lastZ);
            this.lastX = var3;
            this.lastZ = var5;
            if (var9 > 16.0) {
               this.head = this.count = 0;
               this.sum = 0.0;
            } else {
               double var11 = var9 * var7;
               if (!Double.isFinite(var11)) {
                  this.reset();
               } else {
                  if (this.count == 120) {
                     this.sum = this.sum - this.samples[this.head];
                     this.samples[this.head] = var11;
                     this.head = (this.head + 1) % 120;
                  } else {
                     this.samples[(this.head + this.count++) % 120] = var11;
                  }

                  this.sum += var11;
               }
            }
         }
      } else {
         this.reset();
      }
   }

   public int size() {
      return this.count;
   }

   public double get(int var1) {
      if (var1 >= 0 && var1 < this.count) {
         return this.samples[(this.head + var1) % 120];
      } else {
         throw new IndexOutOfBoundsException(var1);
      }
   }

   public double latest() {
      return this.count == 0 ? 0.0 : this.get(this.count - 1);
   }

   public double average() {
      return this.count == 0 ? 0.0 : Math.max(0.0, this.sum / this.count);
   }
}
