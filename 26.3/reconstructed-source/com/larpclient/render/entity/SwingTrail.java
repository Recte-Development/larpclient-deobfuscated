package com.larpclient.render.entity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import net.minecraft.world.phys.Vec3;

public final class SwingTrail {
   public static final int mAX_POINTS = 64;
   private final SwingTrail.Sample[] points = new SwingTrail.Sample[64];
   private int head;
   private int size;
   private int strokeId;
   private long serial;
   private boolean swinging;
   private double lastTime = Double.NaN;
   private float lastProgress;
   private Object owner;

   public int size() {
      return this.size;
   }

   public void clear() {
      Arrays.fill(this.points, null);
      this.head = this.size = this.strokeId = 0;
      this.swinging = (boolean)0;
      this.lastTime = Double.NaN;
      this.owner = null;
   }

   public void prune(double var1, double var3) {
      while (this.size > 0 && var1 - this.points[this.head].time() > var3) {
         this.points[this.head] = null;
         this.head = (this.head + 1) % 64;
         this.size--;
      }
   }

   public boolean addSample(Object var1, Vec3 var2, Vec3 var3, double var4, float var6, boolean var7, double var8, int var10) {
      if (isFinite(var2) && isFinite(var3) && Double.isFinite(var4)) {
         if (this.owner != var1 || Double.isFinite(this.lastTime) && (var4 < this.lastTime || var4 - this.lastTime > 0.25)) {
            this.clear();
            this.owner = var1;
         }

         this.lastTime = var4;
         this.prune(var4, var8);

         while (this.size > Math.clamp(var10, 4, 64)) {
            this.points[this.head] = null;
            this.head = (this.head + 1) % 64;
            this.size--;
         }

         if (var7 && !(var6 <= 0.0F) && !(var6 >= 1.0F)) {
            int var11 = this.swinging && !(var6 + 0.05F < this.lastProgress) ? 0 : 1;
            if (var11 != 0) {
               this.strokeId++;
            }

            this.swinging = (boolean)1;
            this.lastProgress = var6;
            SwingTrail.Sample var12 = this.size > 0 ? this.get(this.size - 1) : null;
            if (var12 != null && var12.stroke() == this.strokeId) {
               double var13 = Math.max(var2.distanceTo(var12.root()), var3.distanceTo(var12.tip()));
               if (!(var13 > 2.5)) {
                  double var15 = var4 - var12.time();
                  if (!(var15 <= 1.0E-7) && (!(var13 < 0.012) || !(var15 < 0.045))) {
                     if (var13 < 0.001) {
                        return false;
                     }

                     int var17 = Math.clamp((int)Math.ceil(var13 / 0.09), 1, 6);

                     for (int var18 = 1; var18 <= var17; var18++) {
                        double var19 = (double)var18 / var17;
                        this.push(
                           new SwingTrail.Sample(
                              var12.root().lerp(var2, var19), var12.tip().lerp(var3, var19), var12.time() + var15 * var19, this.strokeId, ++this.serial
                           ),
                           var10
                        );
                     }

                     return true;
                  }

                  return false;
               }

               this.clear();
               this.owner = var1;
               this.strokeId++;
               this.swinging = (boolean)1;
            }

            this.push(new SwingTrail.Sample(var2, var3, var4, this.strokeId, ++this.serial), var10);
            return true;
         } else {
            this.swinging = (boolean)0;
            return false;
         }
      } else {
         this.clear();
         return false;
      }
   }

   private void push(SwingTrail.Sample var1, int var2) {
      int var3 = Math.clamp(var2, 4, 64);

      while (this.size >= var3) {
         this.points[this.head] = null;
         this.head = (this.head + 1) % 64;
         this.size--;
      }

      this.points[(this.head + this.size++) % 64] = var1;
   }

   public SwingTrail.Sample get(int var1) {
      return this.points[(this.head + var1) % 64];
   }

   public List<SwingTrail.Sample> snapshot() {
      ArrayList var1 = new ArrayList(this.size);

      for (int var2 = 0; var2 < this.size; var2++) {
         var1.add(this.get(var2));
      }

      return List.copyOf(var1);
   }

   private static boolean isFinite(Vec3 var0) {
      return var0 != null && Double.isFinite(var0.x + var0.y + var0.z);
   }

   public record Sample(Vec3 root, Vec3 tip, double time, int stroke, long serial) {
   }
}
