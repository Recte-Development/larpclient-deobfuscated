package com.larpclient.render.entity;

import com.larpclient.config.HitEffectConfig;
import com.larpclient.gui.theme.Theme;
import com.larpclient.render.particle.ParticleSystem;
import com.larpclient.render.world.EffectsRenderer;
import com.larpclient.render.world.TrailRenderer;
import com.larpclient.util.data.TickTimer;
import com.larpclient.util.math.CatmullRomSpline;
import com.larpclient.util.math.Easing;
import com.larpclient.util.misc.LarpLog;
import com.larpclient.util.misc.PerfProfiler;
import com.larpclient.util.render.ColorUtil;
import java.util.LinkedHashMap;
import java.util.Map;
import net.minecraft.world.phys.Vec3;

public final class BodyTrailEngine {
   static final int mAX_POINTS = 128;
   static final int mAX_SOURCES = 16;
   static final int mAX_RIBBONS = 8;
   static final int sUBDIVISIONS = 3;
   static final int mAX_SAMPLES = 382;
   private final Map<Integer, BodyTrailEngine.TrailSource> sources = new LinkedHashMap<>();
   private long tickCount;
   private boolean hasVisibleRibbons;
   private HitEffectConfig.BlendMode blendMode = HitEffectConfig.BlendMode.ADDITIVE;
   public BodyTrailEngine.Style style = BodyTrailEngine.Style.RIBBON;
   public TrailRenderer.WidthCurve fadeMode = TrailRenderer.WidthCurve.QUADRATIC;
   public Easing.FadeCurve easing = Easing.FadeCurve.LINEAR;
   public double lifetimeSeconds = 1.2;
   public double width = 0.12;
   public boolean showThroughWalls = (boolean)1;
   public boolean smooth = (boolean)1;
   public int startColor = Theme.accent;
   public int endColor = Theme.accentEnd;

   public BodyTrailEngine() {
      LarpLog.info("Trails", "Body trail engine initialized: {} sources, {} points per source", 16, 128);
   }

   public boolean buildHelix() {
      return this.sources.isEmpty();
   }

   public int buildAurora() {
      return this.sources.size();
   }

   public int totalPoints() {
      return this.sources.values().stream().mapToInt(var0 -> var0.size).sum();
   }

   public boolean hasVisibleRibbons() {
      return this.hasVisibleRibbons;
   }

   public void clear() {
      this.sources.clear();
      this.tickCount = 0L;
      this.hasVisibleRibbons = (boolean)0;
   }

   public void tick() {
      this.tickCount++;
      this.sources.values().removeIf(var1 -> {
         while (var1.size > 0 && this.tickCount - var1.ticks[var1.head] > this.lifetimeSeconds * 20.0) {
            var1.head = (var1.head + 1) % 128;
            var1.size--;
         }

         return var1.size == 0;
      });
   }

   public void addPoint(int var1, Vec3 var2, double var3, double var5) {
      if (Double.isFinite(var2.x) && Double.isFinite(var2.y) && Double.isFinite(var2.z) && Double.isFinite(var3) && Double.isFinite(var5)) {
         var3 = Math.clamp(var3, 0.1, 6.0);
         var5 = Math.clamp(var5, 0.1, 4.0);
         BodyTrailEngine.TrailSource var7 = this.sources.get(var1);
         if (var7 == null) {
            if (this.sources.size() >= 16) {
               return;
            }

            var7 = new BodyTrailEngine.TrailSource();
            this.sources.put(var1, var7);
         }

         double var8 = var7.totalDistance;
         if (var7.size > 0) {
            int var10 = (var7.head + var7.size - 1) % 128;
            double var11 = var7.points[var10].distanceToSqr(var2);
            if (var11 > 64.0) {
               var7.head = var7.size = 0;
            } else {
               if (var11 < 4.0E-4 && Math.abs(var7.spreads[var10] - var3) < 0.02) {
                  return;
               }

               var8 += Math.sqrt(var11);
            }
         }

         if (var7.size == 128) {
            var7.head = (var7.head + 1) % 128;
            var7.size--;
         }

         int var15 = (var7.head + var7.size++) % 128;
         var7.points[var15] = var2;
         var7.spreads[var15] = var3;
         var7.heights[var15] = var5;
         var7.distances[var15] = var7.totalDistance = var8;
         var7.ticks[var15] = this.tickCount;
      }
   }

   public void buildGeometry(Vec3 var1, float var2) {
      this.hasVisibleRibbons = (boolean)0;
      this.blendMode = this.style == BodyTrailEngine.Style.AURORA ? HitEffectConfig.BlendMode.NORMAL : HitEffectConfig.BlendMode.ADDITIVE;
      double var3 = TickTimer.getTime();

      for (BodyTrailEngine.TrailSource var6 : this.sources.values()) {
         var6.visible = (boolean)0;
         var6.ribbonCount = var6.glowCount = 0;
         this.buildPath(var6, var2);
         if (var6.sampleCount >= 2) {
            switch (this.style) {
               case AURORA:
                  this.buildAurora(var6, var1, var3);
                  break;
               case RIBBON:
                  this.buildRibbon(var6, var1, var3);
                  break;
               case WINGS:
                  this.buildWings(var6, var1, var3);
                  break;
               case HELIX:
                  this.buildHelix(var6, var1, var3);
                  break;
               case SIMPLE:
                  this.buildSimple(var6, var1, var3);
            }

            for (int var7 = 0; var7 < var6.ribbonCount; var7++) {
               CatmullRomSpline.Ribbon var8 = var6.ribbons[var7];
               if (this.fadeMode != TrailRenderer.WidthCurve.CONSTANT) {
                  var8.fadeInTail();
               }

               var8.computeOffsets(var1);
               var6.ribbonVisible[var7] = newRibbon(var8, var1);
               var6.visible = var6.visible | var6.ribbonVisible[var7];
            }

            if (this.showThroughWalls && var6.visible) {
               this.hasVisibleRibbons = (boolean)1;
            }
         }
      }
   }

   public void buildRibbon(EffectsRenderer var1, boolean var2) {
      if (!var2 || this.showThroughWalls) {
         long var3 = PerfProfiler.startTimer();
         var1.blend(this.blendMode);

         for (BodyTrailEngine.TrailSource var6 : this.sources.values()) {
            if (var6.visible) {
               for (int var7 = 0; var7 < var6.ribbonCount; var7++) {
                  if (var6.ribbonVisible[var7]) {
                     if (var6.ribbonFades[var7] >= 0.0) {
                        var1.drawFadedTrail(var6.ribbons[var7], var6.ribbonFades[var7]);
                     } else {
                        var1.drawTrail(var6.ribbons[var7], var6.ribbonThin[var7]);
                     }
                  }
               }
            }
         }

         var1.blend(HitEffectConfig.BlendMode.ADDITIVE);

         for (BodyTrailEngine.TrailSource var13 : this.sources.values()) {
            if (var13.visible) {
               for (int var14 = 0; var14 < var13.glowCount; var14++) {
                  Vec3 var8 = var13.glowPositions[var14];
                  int var9 = var13.glowColors[var14];
                  double var10 = var13.glowSizes[var14];
                  var1.drawSpriteWithType(var8.x, var8.y, var8.z, var10, 0.0, ColorUtil.multiplyAlpha(var9, 0.32), ParticleSystem.Style.GLOW, 0.0, 0.0, 0.0);
                  var1.drawSpriteWithType(
                     var8.x,
                     var8.y,
                     var8.z,
                     var10 * 0.32,
                     0.0,
                     ColorUtil.lerpColor(var9, var9 & 0xFF000000 | 16777215, 0.7),
                     ParticleSystem.Style.GLOW,
                     0.0,
                     0.0,
                     0.0
                  );
               }
            }
         }

         var1.blend(HitEffectConfig.BlendMode.NORMAL);
         PerfProfiler.endTimer("Body Trails", var3);
      }
   }

   private void buildPath(BodyTrailEngine.TrailSource var1, float var2) {
      var1.sampleCount = 0;
      if (var1.size != 0) {
         int var3 = this.smooth ? 3 : 1;

         for (int var4 = 1; var4 < var1.size; var4++) {
            int var5 = (var1.head + var4 - 1) % 128;
            int var6 = (var1.head + var4) % 128;
            Vec3 var7 = var1.points[var5];
            Vec3 var8 = var1.points[var6];
            double var9 = var1.spreads[var6];
            double var11 = var1.heights[var6];
            double var13 = var1.distances[var6];
            if (var4 == var1.size - 1 && var1.ticks[var6] == this.tickCount) {
               var8 = var7.lerp(var8, var2);
               var9 = var1.spreads[var5] + (var9 - var1.spreads[var5]) * var2;
               var11 = var1.heights[var5] + (var11 - var1.heights[var5]) * var2;
               var13 = var1.distances[var5] + (var13 - var1.distances[var5]) * var2;
            }

            double var15 = this.ageFraction(var1.ticks[var5], var2);
            double var17 = this.ageFraction(var1.ticks[var6], var2);
            if (var4 == 1) {
               this.appendSample(var1, var7, var1.spreads[var5], var1.heights[var5], var15, var1.distances[var5]);
            }

            if (var3 > 1) {
               Vec3 var19 = var4 > 1 ? var1.points[(var1.head + var4 - 2) % 128] : var7.add(var7.subtract(var8));
               Vec3 var20 = var4 < var1.size - 1 ? var1.points[(var1.head + var4 + 1) % 128] : var8.add(var8.subtract(var7));
               var1.spline.setControlPoints(var19, var7, var8, var20);
            }

            for (int var27 = 1; var27 <= var3; var27++) {
               double var28 = (double)var27 / var3;
               this.appendSample(
                  var1,
                  var3 == 1 ? var8 : var1.spline.sample(var28),
                  var1.spreads[var5] + (var9 - var1.spreads[var5]) * var28,
                  var1.heights[var5] + (var11 - var1.heights[var5]) * var28,
                  var15 + (var17 - var15) * var28,
                  var1.distances[var5] + (var13 - var1.distances[var5]) * var28
               );
            }
         }

         Vec3 var22 = null;

         for (int var23 = 0; var23 < var1.sampleCount; var23++) {
            Vec3 var24 = var1.samplePositions[Math.min(var23 + 1, var1.sampleCount - 1)].subtract(var1.samplePositions[Math.max(var23 - 1, 0)]);
            double var25 = Math.sqrt(var24.x * var24.x + var24.z * var24.z);
            Vec3 var26 = var25 < 1.0E-6 ? (var22 != null ? var22 : new Vec3(1.0, 0.0, 0.0)) : new Vec3(-var24.z / var25, 0.0, var24.x / var25);
            if (var22 != null && var26.dot(var22) < 0.0) {
               var26 = var26.scale(-1.0);
            }

            var22 = var26;
            var1.sampleNormals[var23] = var26;
         }
      }
   }

   private void appendSample(BodyTrailEngine.TrailSource var1, Vec3 var2, double var3, double var5, double var7, double var9) {
      int var11 = var1.sampleCount++;
      var1.samplePositions[var11] = var2;
      var1.sampleSpreads[var11] = var3;
      var1.sampleHeights[var11] = var5;
      var1.sampleLife[var11] = var7;
      var1.sampleDistances[var11] = var9;
   }

   private double ageFraction(long var1, float var3) {
      return Math.clamp(1.0 - ((float)this.tickCount + var3 - (float)var1) / (this.lifetimeSeconds * 20.0), 0.0, 1.0);
   }

   private void buildAurora(BodyTrailEngine.TrailSource var1, Vec3 var2, double var3) {
      this.buildBands(var1, var2, 4, 0.1, 0.0, 0.0, var3, false);
      double[] var5 = new double[]{0.16, 0.52, 0.88};

      for (int var6 = 0; var6 < var5.length; var6++) {
         CatmullRomSpline.Ribbon var7 = this.newRibbon(var1, 0.55, false);

         for (int var8 = 0; var8 < var1.sampleCount; var8++) {
            double var9 = var1.sampleLife[var8];
            double var11 = var1.sampleSpreads[var8];
            double var13 = var1.sampleDistances[var8];
            double var15 = 1.0 - var9;
            double var17 = var11 * var5[var6] + Math.sin(var13 * 2.4 + var6 * 2.1 - var3 * 1.6) * 0.09 * var11 * var15;
            double var19 = Math.cos(var13 * 1.8 + var6 * 1.7 - var3 * 1.2) * 0.16 * var15;
            int var21 = ColorUtil.lerpColor(this.buildBands(var9), ColorUtil.lerpColor(this.startColor, this.endColor, 0.5), var6 * 0.18);
            this.addRibbonPoint(var7, var1, var8, var17, var19, this.width * 1.15 * this.fadeProfile(var9), var21, 0.9 * this.easing.alpha(1.0 - var9), var2);
         }
      }
   }

   private void buildRibbon(BodyTrailEngine.TrailSource var1, Vec3 var2, double var3) {
      this.buildBands(var1, var2, 6, 0.24, 3.2, 5.0, var3, true);

      for (int var5 = 0; var5 < 2; var5++) {
         CatmullRomSpline.Ribbon var6 = this.newRibbon(var1, -1.0, true);

         for (int var7 = 0; var7 < var1.sampleCount; var7++) {
            double var8 = var1.sampleLife[var7];
            double var10 = var1.sampleSpreads[var7];
            double var12 = Math.min(1.0, this.fadeProfile(var8));
            double var14 = var5 == 0 ? var10 * 0.97 : var10 * 0.04;
            double var16 = var10 * 0.5;
            double var18 = var16 + (var14 - var16) * (0.25 + 0.75 * var12);
            this.addRibbonPoint(
               var6, var1, var7, var18, 0.0, this.width * 0.55 * this.fadeProfile(var8), this.buildBands(var8), this.easing.alpha(1.0 - var8), var2
            );
         }
      }
   }

   private void buildWings(BodyTrailEngine.TrailSource var1, Vec3 var2, double var3) {
      double[] var5 = new double[]{-1.0, 0.0, 1.0};

      for (double var9 : var5) {
         CatmullRomSpline.Ribbon var11 = this.newRibbon(var1, 0.0, false);

         for (int var12 = 0; var12 < var1.sampleCount; var12++) {
            double var13 = var1.sampleLife[var12];
            double var15 = var1.sampleSpreads[var12];
            double var17 = var13 * var13;
            this.addRibbonPoint(
               var11,
               var1,
               var12,
               var15 * (0.55 + var9 * 0.32 * var17),
               0.0,
               var15 * 0.42 * var17,
               this.buildBands(var13),
               0.13 * this.easing.alpha(1.0 - var13),
               var2
            );
         }
      }

      for (int var19 = 0; var19 < 2; var19++) {
         CatmullRomSpline.Ribbon var21 = this.newRibbon(var1, -1.0, true);

         for (int var24 = 0; var24 < var1.sampleCount; var24++) {
            double var26 = var1.sampleLife[var24];
            double var27 = var1.sampleSpreads[var24];
            double var28 = var1.sampleDistances[var24];
            double var29 = 1.0 - var26;
            double var30 = var28 * 3.0 + var19 * Math.PI - var3 * 3.0;
            this.addRibbonPoint(
               var21,
               var1,
               var24,
               var27 * 0.55 + Math.cos(var30) * 0.22 * var27 * var29,
               Math.sin(var30) * 0.3 * var29,
               this.width * 0.4 * this.fadeProfile(var26),
               ColorUtil.lerpColor(this.buildBands(var26), this.endColor, 0.3),
               0.8 * this.easing.alpha(1.0 - var26),
               var2
            );
         }
      }

      CatmullRomSpline.Ribbon var20 = this.newRibbon(var1, -1.0, true);

      for (int var22 = 0; var22 < var1.sampleCount; var22++) {
         double var25 = var1.sampleLife[var22];
         this.addRibbonPoint(
            var20,
            var1,
            var22,
            var1.sampleSpreads[var22] * 0.55,
            0.0,
            this.width * 2.2 * this.fadeProfile(var25),
            this.buildBands(var25),
            (0.4 + 0.6 * var25) * this.easing.alpha(1.0 - var25),
            var2
         );
      }

      int var23 = var1.sampleCount - 1;
      this.buildHelix(var1, var1.samplePositions[var23].add(0.0, var1.sampleSpreads[var23] * 0.55, 0.0), this.startColor, var1.sampleSpreads[var23] * 0.3, var2);
   }

   private void buildHelix(BodyTrailEngine.TrailSource var1, Vec3 var2, double var3) {
      CatmullRomSpline.Ribbon var5 = this.newRibbon(var1, 0.0, false);

      for (int var6 = 0; var6 < var1.sampleCount; var6++) {
         double var7 = var1.sampleLife[var6];
         this.addRibbonPoint(
            var5,
            var1,
            var6,
            var1.sampleSpreads[var6] * 0.5,
            0.0,
            var1.sampleSpreads[var6] * 0.3 * Math.min(1.0, this.fadeProfile(var7) + 0.2),
            this.buildBands(var7),
            0.12 * this.easing.alpha(1.0 - var7),
            var2
         );
      }

      for (int var25 = 0; var25 < 2; var25++) {
         CatmullRomSpline.Ribbon var26 = this.newRibbon(var1, -1.0, true);
         Vec3 var8 = null;

         for (int var9 = 0; var9 < var1.sampleCount; var9++) {
            double var10 = var1.sampleLife[var9];
            double var12 = var1.sampleSpreads[var9];
            double var14 = var1.sampleDistances[var9];
            double var16 = 0.6 + 0.4 * var10;
            double var18 = var14 * 2.4 + var25 * Math.PI - var3 * 2.4;
            double var20 = 0.46 * var12 * var16;
            double var22 = Math.max(var1.sampleHeights[var9] * 0.8, 0.45) * var16;
            int var24 = var25 == 0 ? this.buildBands(var10) : ColorUtil.lerpColor(this.startColor, this.endColor, var10);
            this.addRibbonPoint(
               var26,
               var1,
               var9,
               var12 * 0.5 + Math.cos(var18) * var20,
               Math.sin(var18) * var22,
               this.width * 0.75 * this.fadeProfile(var10),
               var24,
               this.easing.alpha(1.0 - var10),
               var2
            );
            var8 = var26.points[var26.size - 1];
         }

         if (var8 != null) {
            this.buildHelix(var1, var8, var25 == 0 ? this.startColor : this.endColor, 0.16, var2);
         }
      }
   }

   private void buildSimple(BodyTrailEngine.TrailSource var1, Vec3 var2, double var3) {
      this.buildBands(var1, var2, 4, 0.07, 7.0, 3.0, var3, true);
   }

   private void buildBands(BodyTrailEngine.TrailSource var1, Vec3 var2, int var3, double var4, double var6, double var8, double var10, boolean var12) {
      for (int var13 = 0; var13 < var3; var13++) {
         CatmullRomSpline.Ribbon var14 = this.newRibbon(var1, 0.0, false);
         double var15 = (var13 + 0.5) / var3;
         double var17 = Math.abs(var15 * 2.0 - 1.0);
         double var19 = var12 ? 0.55 + 0.45 * var17 * var17 : 1.0;

         for (int var21 = 0; var21 < var1.sampleCount; var21++) {
            double var22 = var1.sampleLife[var21];
            double var24 = var1.sampleSpreads[var21];
            double var26 = var1.sampleDistances[var21];
            double var28 = 0.3 + 0.7 * Math.min(1.0, this.fadeProfile(var22));
            double var30 = var24 * 0.5 + (var15 - 0.5) * var24 * var28;
            double var32 = var6 == 0.0 ? 1.0 : 0.7 + 0.3 * Math.sin(var26 * var6 - var10 * var8 + var13 * 0.9);
            this.addRibbonPoint(
               var14, var1, var21, var30, 0.0, 2.0 * var24 / var3 * var28, this.buildBands(var22), var4 * var19 * var32 * this.easing.alpha(1.0 - var22), var2
            );
         }
      }
   }

   private CatmullRomSpline.Ribbon newRibbon(BodyTrailEngine.TrailSource var1, double var2, boolean var4) {
      int var5 = var1.ribbonCount++;
      if (var1.ribbons[var5] == null) {
         var1.ribbons[var5] = new CatmullRomSpline.Ribbon(382);
      }

      CatmullRomSpline.Ribbon var6 = var1.ribbons[var5];
      var6.clear();
      var6.maxDistanceSq = 4096.0;
      var1.ribbonFades[var5] = var2;
      var1.ribbonThin[var5] = var4;
      return var6;
   }

   private void addRibbonPoint(
      CatmullRomSpline.Ribbon var1, BodyTrailEngine.TrailSource var2, int var3, double var4, double var6, double var8, int var10, double var11, Vec3 var13
   ) {
      Vec3 var14 = var2.sampleNormals[var3];
      Vec3 var15 = var2.samplePositions[var3];
      Vec3 var16 = new Vec3(var15.x + var14.x * var6, var15.y + var4, var15.z + var14.z * var6);
      var1.addPoint(var16, Math.max(0.0, var8), ColorUtil.multiplyAlpha(var10, Math.clamp(var11, 0.0, 1.0) * distanceFade(var16, var13)));
   }

   private void buildHelix(BodyTrailEngine.TrailSource var1, Vec3 var2, int var3, double var4, Vec3 var6) {
      double var7 = distanceFade(var2, var6);
      if (!(var7 <= 0.0) && var1.glowCount < var1.glowPositions.length) {
         int var9 = var1.glowCount++;
         var1.glowPositions[var9] = var2;
         var1.glowColors[var9] = ColorUtil.multiplyAlpha(var3, var7);
         var1.glowSizes[var9] = var4;
      }
   }

   static double distanceFade(Vec3 var0, Vec3 var1) {
      return Easing.smoothstep((var0.distanceTo(var1) - 0.6) / 1.4);
   }

   private int buildBands(double var1) {
      return ColorUtil.lerpColor(this.endColor, this.startColor, var1);
   }

   private double fadeProfile(double var1) {
      return switch (this.fadeMode) {
         case CONSTANT -> 1.0;
         case QUADRATIC -> var1 * var1;
         case SINE -> Math.sin(Math.PI * var1);
         case CUBIC -> Math.pow(var1, 3.0) * 1.4;
      };
   }

   private static boolean newRibbon(CatmullRomSpline.Ribbon var0, Vec3 var1) {
      for (int var2 = 1; var2 < var0.size; var2++) {
         if (var0.points[var2 - 1].distanceToSqr(var1) <= var0.maxDistanceSq
            && var0.points[var2 - 1].distanceToSqr(var0.points[var2]) >= 1.0E-10
            && (var0.widths[var2 - 1] > 0.0 || var0.widths[var2] > 0.0)
            && (var0.colors[var2 - 1] >>> 24 > 0 || var0.colors[var2] >>> 24 > 0)) {
            return true;
         }
      }

      return false;
   }

   CatmullRomSpline.Ribbon getRibbon(int var1, int var2) {
      BodyTrailEngine.TrailSource var3 = this.sources.get(var1);
      return var3 != null && var2 < var3.ribbonCount ? var3.ribbons[var2] : null;
   }

   int ribbonCount(int var1) {
      BodyTrailEngine.TrailSource var2 = this.sources.get(var1);
      return var2 == null ? 0 : var2.ribbonCount;
   }

   public enum Style {
      AURORA,
      RIBBON,
      WINGS,
      HELIX,
      SIMPLE;
   }

   private static final class TrailSource {
      final Vec3[] points = new Vec3[128];
      final double[] spreads = new double[128];
      final double[] heights = new double[128];
      final double[] distances = new double[128];
      final long[] ticks = new long[128];
      int head;
      int size;
      double totalDistance;
      final Vec3[] samplePositions = new Vec3[382];
      final Vec3[] sampleNormals = new Vec3[382];
      final double[] sampleSpreads = new double[382];
      final double[] sampleHeights = new double[382];
      final double[] sampleLife = new double[382];
      final double[] sampleDistances = new double[382];
      int sampleCount;
      final CatmullRomSpline.SplineSegment spline = new CatmullRomSpline.SplineSegment();
      final CatmullRomSpline.Ribbon[] ribbons = new CatmullRomSpline.Ribbon[8];
      final double[] ribbonFades = new double[8];
      final boolean[] ribbonThin = new boolean[8];
      final boolean[] ribbonVisible = new boolean[8];
      int ribbonCount;
      final Vec3[] glowPositions = new Vec3[2];
      final int[] glowColors = new int[2];
      final double[] glowSizes = new double[2];
      int glowCount;
      boolean visible;
   }
}
