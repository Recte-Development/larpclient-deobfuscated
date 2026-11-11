package com.larpclient.render.entity;

import com.larpclient.util.math.Easing;
import java.util.Arrays;
import net.minecraft.world.phys.Vec3;

public final class OrbitTrail {
   public static final int oRBIT_COUNT = 3;
   public static final int cAPACITY = 256;
   private static final double sTEP = 0.008333333333333333;
   private final Vec3[][] history = new Vec3[3][256];
   private final double[] times = new double[256];
   private final Vec3[] heads = new Vec3[3];
   private int start;
   private int size;
   private double lastTime = Double.NaN;
   private double sampleTime;
   private double angle;
   private double lastRadius;
   private double startTime;

   public void reset() {
      this.start = this.size = 0;
      this.lastTime = Double.NaN;
      this.angle = 0.0;
      Arrays.fill(this.heads, null);

      for (Vec3[] var4 : this.history) {
         Arrays.fill(var4, null);
      }
   }

   public void update(double var1, double var3, double var5, double var7) {
      if (!Double.isNaN(this.lastTime) && (var1 < this.lastTime || var1 - this.lastTime > 1.0)) {
         this.reset();
      }

      if (Double.isNaN(this.lastTime)) {
         this.lastTime = this.sampleTime = var1;
         this.startTime = var1;
         this.lastRadius = var5;
         this.addSample(var1, this.angle, var5);
      }

      double var9 = var1 - this.lastTime;
      double var11 = this.angle + var9 * var3 * Math.PI * 2.0;

      while (this.sampleTime + 0.008333333333333333 <= var1) {
         this.sampleTime += 0.008333333333333333;
         double var13 = var9 > 0.0 ? Math.clamp((this.sampleTime - this.lastTime) / var9, 0.0, 1.0) : 1.0;
         this.addSample(this.sampleTime, this.angle + (var11 - this.angle) * var13, this.lastRadius + (var5 - this.lastRadius) * var13);
      }

      this.angle = var11;

      for (int var15 = 0; var15 < 3; var15++) {
         this.heads[var15] = orbitPosition(this.angle, var5, var15);
      }

      this.lastTime = var1;

      for (this.lastRadius = var5; this.size > 0 && var1 - this.times[this.start] > var7; this.size--) {
         this.start = (this.start + 1) % 256;
      }
   }

   private void addSample(double var1, double var3, double var5) {
      if (this.size == 256) {
         this.start = (this.start + 1) % 256;
         this.size--;
      }

      int var7 = (this.start + this.size++) % 256;
      this.times[var7] = var1;

      for (int var8 = 0; var8 < 3; var8++) {
         this.history[var8][var7] = orbitPosition(var3, var5, var8);
      }
   }

   private static Vec3 orbitPosition(double var0, double var2, int var4) {
      double var5 = var4 * Math.PI * 2.0 / 3.0;
      double var7 = var0 + var5;
      return new Vec3(Math.cos(var7) * var2, 0.5 + 0.5 * Math.sin(var0 * 0.65 + var5), Math.sin(var7) * var2);
   }

   public int getSize() {
      return this.size;
   }

   public double fadeIn(double var1) {
      return Double.isNaN(this.lastTime) ? 0.0 : Easing.smoothstep((var1 - this.startTime) / 0.15);
   }

   public Vec3 getHead(int var1) {
      return this.heads[var1];
   }

   public Vec3 getTrailPoint(int var1, int var2) {
      return this.history[var1][(this.start + var2) % 256];
   }

   public double getTrailTime(int var1) {
      return this.times[(this.start + var1) % 256];
   }

   public static double getTrailTime(double var0, double var2) {
      return Math.min(var0, var2 / 8.0);
   }

   public static Vec3 anchorOffset(Vec3 var0, Vec3 var1, double var2, double var4) {
      double var6 = var4 * 3.2;
      return var1.add(var0.x, var6 + var0.y * Math.max(0.0, var2 - var6 * 2.0), var0.z);
   }
}
