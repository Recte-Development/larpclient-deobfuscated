package com.larpclient.render.particle;

import com.larpclient.render.world.EffectsRenderer;
import com.larpclient.util.misc.PerfProfiler;
import java.util.HashMap;
import java.util.Random;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.world.phys.Vec3;

public final class SnowSimulation {
   public static final int mAX_FLAKES = 3200;
   public static final int mAX_COLUMNS = 8192;
   public static final int mAX_COLUMN_SAMPLES_PER_TICK = 128;
   private final double[] x = new double[3200];
   private final double[] y = new double[3200];
   private final double[] z = new double[3200];
   private final double[] prevX = new double[3200];
   private final double[] prevY = new double[3200];
   private final double[] prevZ = new double[3200];
   private final double[] phase = new double[3200];
   private final double[] fallSpeed = new double[3200];
   private final double[] size = new double[3200];
   private final double[] renderX = new double[3200];
   private final double[] renderY = new double[3200];
   private final double[] renderZ = new double[3200];
   private final double[] rotation = new double[3200];
   private final int[] color = new int[3200];
   private final HashMap<Long, SnowSimulation.RoofCacheEntry> roofCache = new HashMap<>();
   private final Random random = new Random(1397641047L);
   private Vec3 center;
   private SnowSimulation.Options options;
   private int flakeCount;
   private int visibleCount;
   private int sampleCursor;
   private long tickCount;
   private final double[] renderSize = new double[3200];

   public void reset() {
      this.center = null;
      this.options = null;
      this.flakeCount = this.visibleCount = this.sampleCursor = 0;
      this.tickCount = 0L;
      this.roofCache.clear();
      this.random.setSeed(1397641047L);
   }

   public int reset() {
      return this.flakeCount;
   }

   public int getVisibleCount() {
      return this.visibleCount;
   }

   public int getCachedColumnCount() {
      return this.roofCache.size();
   }

   public static long columnKey(int var0, int var1) {
      return (long)var0 << 32 ^ var1 & 4294967295L;
   }

   private void respawn(int var1, Vec3 var2, boolean var3) {
      double var4 = this.random.nextDouble() * Math.PI * 2.0;
      double var6 = Math.sqrt(this.random.nextDouble()) * this.options.radius();
      this.x[var1] = var2.x + Math.cos(var4) * var6;
      this.z[var1] = var2.z + Math.sin(var4) * var6;
      this.y[var1] = var2.y + (var3 ? this.random.nextDouble() * 24.0 - 6.0 : 16.0 + this.random.nextDouble() * 2.0);
      this.phase[var1] = this.random.nextDouble() * Math.PI * 2.0;
      this.fallSpeed[var1] = 0.8 + this.random.nextDouble() * 1.6;
      this.size[var1] = 0.018 + Math.pow(this.random.nextDouble(), 3.0) * 0.065;
      this.prevX[var1] = this.x[var1];
      this.prevY[var1] = this.y[var1];
      this.prevZ[var1] = this.z[var1];
   }

   public void tick(Vec3 var1, SnowSimulation.Options var2, SnowSimulation.RoofProbe var3) {
      if (this.center == null
         || var1.distanceToSqr(this.center) > var2.radius() * var2.radius()
         || this.options == null
         || this.options.radius() != var2.radius()) {
         this.reset();
      }

      this.options = var2;
      this.center = var1;
      this.tickCount++;
      this.visibleCount = 0;
      int var4 = this.options.count();
      int var5 = this.flakeCount;
      this.flakeCount = var4;

      for (int var6 = var5; var6 < this.flakeCount; var6++) {
         this.respawn(var6, var1, true);
      }

      double var16 = this.tickCount * 0.05;
      double var8 = 0.65 + 0.35 * Math.sin(var16 * 0.43);

      for (int var10 = 0; var10 < this.flakeCount; var10++) {
         this.prevX[var10] = this.x[var10];
         this.prevY[var10] = this.y[var10];
         this.prevZ[var10] = this.z[var10];
         this.x[var10] = this.x[var10] + 0.05 * (this.options.wind() * 2.8 * var8 + Math.sin(var16 * 1.4 + this.phase[var10]) * 0.22);
         this.z[var10] = this.z[var10] + 0.05 * (this.options.wind() * 0.9 * var8 + Math.cos(var16 * 0.9 + this.phase[var10]) * 0.16);
         this.y[var10] = this.y[var10] - this.fallSpeed[var10] * 0.05;
         SnowSimulation.RoofCacheEntry var11 = this.roofCache.get(columnKey((int)Math.floor(this.x[var10]), (int)Math.floor(this.z[var10])));
         double var12 = this.x[var10] - var1.x;
         double var14 = this.z[var10] - var1.z;
         if (var12 * var12 + var14 * var14 > this.options.radius() * this.options.radius()
            || this.y[var10] < var1.y - 6.0
            || this.y[var10] > var1.y + 20.0
            || var11 != null && this.y[var10] <= var11.roof.height() + 0.16) {
            this.respawn(var10, var1, false);
         }
      }

      if (this.roofCache.size() >= 8192) {
         this.roofCache.entrySet().removeIf(var1x -> this.tickCount - var1x.getValue().tick() > 20L);
      }

      int var17 = 0;
      int var18 = 0;

      while (this.flakeCount > 0 && var18++ < this.flakeCount && var17 < 128) {
         int var19 = this.sampleCursor++ % this.flakeCount;
         long var13 = columnKey((int)Math.floor(this.x[var19]), (int)Math.floor(this.z[var19]));
         SnowSimulation.RoofCacheEntry var15 = this.roofCache.get(var13);
         if ((var15 == null || this.tickCount - var15.tick() > 20L) && this.roofCache.size() < 8192) {
            this.roofCache
               .put(var13, new SnowSimulation.RoofCacheEntry(var3.sample((int)Math.floor(this.x[var19]), (int)Math.floor(this.z[var19])), this.tickCount));
            var17++;
         }
      }

      if (this.tickCount % 20L == 0L) {
         this.roofCache.entrySet().removeIf(var1x -> this.tickCount - var1x.getValue().tick() > 40L);
      }

      if (this.sampleCursor > 320000) {
         this.sampleCursor = this.sampleCursor % Math.max(1, this.flakeCount);
      }
   }

   public void prepareRender(Vec3 var1, float var2) {
      this.visibleCount = 0;
      if (this.center != null && this.options != null && !(var1.distanceToSqr(this.center) > this.options.radius() * this.options.radius())) {
         double var3 = Math.clamp(var2, 0.0F, 1.0F);
         double var5 = (this.tickCount - 1L + var3) * 0.05;

         for (int var7 = 0; var7 < this.flakeCount; var7++) {
            double var8 = this.prevX[var7] + (this.x[var7] - this.prevX[var7]) * var3;
            double var10 = this.prevY[var7] + (this.y[var7] - this.prevY[var7]) * var3;
            double var12 = this.prevZ[var7] + (this.z[var7] - this.prevZ[var7]) * var3;
            SnowSimulation.RoofCacheEntry var14 = this.roofCache.get(columnKey((int)Math.floor(var8), (int)Math.floor(var12)));
            if (var14 != null && this.tickCount - var14.tick() <= 20L && !(var10 <= var14.roof.height() + 0.16)) {
               double var15 = Math.sqrt((var8 - var1.x) * (var8 - var1.x) + (var10 - var1.y) * (var10 - var1.y) + (var12 - var1.z) * (var12 - var1.z));
               double var17 = Math.clamp((this.options.radius() - var15) / (this.options.radius() * 0.2), 0.0, 1.0)
                  * Math.clamp((var15 - 0.35) / 0.9, 0.0, 1.0);
               var17 *= Math.clamp((var10 - var14.roof.height() - 0.16) / 0.4, 0.0, 1.0);
               int var19 = (int)Math.round(210.0 * var17);
               if (var19 > 1) {
                  double var20 = 0.22 + 0.78 * Math.clamp(var14.roof.light(), 0.0, 1.0);
                  int var22 = (int)(232.0 * var20);
                  int var23 = (int)(241.0 * var20);
                  int var24 = (int)(255.0 * var20);
                  this.color[this.visibleCount] = var19 << 24 | var22 << 16 | var23 << 8 | var24;
                  this.renderX[this.visibleCount] = var8;
                  this.renderY[this.visibleCount] = var10;
                  this.renderZ[this.visibleCount] = var12;
                  this.rotation[this.visibleCount] = this.phase[var7] + var5 * (0.35 + 0.15 * this.fallSpeed[var7]);
                  this.renderSize[this.visibleCount] = this.size[var7];
                  this.visibleCount++;
               }
            }
         }
      }
   }

   public void render(EffectsRenderer var1) {
      if (this.visibleCount != 0) {
         RenderType var2 = SnowflakeRenderTypes.getRenderType();

         for (int var3 = 0; var3 < this.visibleCount; var3++) {
            var1.drawChinaHatAt(var2, this.renderX[var3], this.renderY[var3], this.renderZ[var3], this.renderSize[var3], this.rotation[var3], this.color[var3]);
         }

         PerfProfiler.addCounter("Snow flakes", this.visibleCount);
      }
   }

   public Vec3 tick(int var1) {
      if (var1 >= 0 && var1 < this.visibleCount) {
         return new Vec3(this.renderX[var1], this.renderY[var1], this.renderZ[var1]);
      } else {
         throw new IndexOutOfBoundsException();
      }
   }

   public record Options(double radius, double density, double wind, int limit) {
      public Options(double radius, double density, double wind, int limit) {
         if (Double.isFinite(radius) && Double.isFinite(density) && Double.isFinite(wind)) {
            radius = Math.clamp(radius, 12.0, 40.0);
            density = Math.clamp(density, 0.0, 1.0);
            wind = Math.clamp(wind, 0.0, 1.0);
            limit = Math.clamp(limit, 0, 3200);
            this.radius = radius;
            this.density = density;
            this.wind = wind;
            this.limit = limit;
         } else {
            throw new IllegalArgumentException("Non-finite snow options");
         }
      }

      public int count() {
         return (int)Math.round(this.limit * this.density);
      }
   }

   private record RoofCacheEntry(SnowSimulation.RoofSample roof, long tick) {
   }

   @FunctionalInterface
   public interface RoofProbe {
      SnowSimulation.RoofSample sample(int var1, int var2);
   }

   public record RoofSample(double height, double light) {
      public static final SnowSimulation.RoofSample UNAVAILABLE = new SnowSimulation.RoofSample(Double.POSITIVE_INFINITY, 0.0);
   }
}
