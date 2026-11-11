package com.larpclient.render.world;

import com.larpclient.config.HitEffectConfig;
import com.larpclient.gui.theme.Theme;
import com.larpclient.util.data.TickTimer;
import com.larpclient.util.math.CatmullRomSpline;
import com.larpclient.util.math.Easing;
import com.larpclient.util.misc.LarpLog;
import com.larpclient.util.misc.PerfProfiler;
import com.larpclient.util.render.ColorUtil;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import net.minecraft.world.phys.Vec3;

public final class TrailRenderer {
   private static final int mAX_POINTS = 128;
   private static final int mAX_SOURCES = 32;
   private final Map<Integer, TrailRenderer.Ribbon> ribbons = new LinkedHashMap<>();
   private long tickCount;
   public TrailRenderer.WidthCurve widthCurve = TrailRenderer.WidthCurve.QUADRATIC;
   public Easing.FadeCurve alphaCurve = Easing.FadeCurve.LINEAR;
   public HitEffectConfig.BlendMode blendMode = HitEffectConfig.BlendMode.NORMAL;
   public double lifetime = 1.2;
   public double width = 0.12;
   public boolean glow = (boolean)1;
   public boolean smoothing = (boolean)1;
   public boolean animated;
   private boolean glowVisible;
   public int colorFrom = Theme.accent;
   public int colorTo = Theme.accentEnd;

   public TrailRenderer() {
      LarpLog.info("Trails", "Ribbon engine initialized: 32 sources, 128 points per source");
   }

   public boolean hasGlowingRibbon() {
      for (TrailRenderer.Ribbon var2 : this.ribbons.values()) {
         if (var2.style == null ? this.glow : var2.style.glow()) {
            return true;
         }
      }

      return false;
   }

   public boolean isGlowVisible() {
      return this.glowVisible;
   }

   public boolean isEmpty() {
      return this.ribbons.isEmpty();
   }

   public int clear() {
      return this.ribbons.size();
   }

   public int totalPointCount() {
      return this.ribbons.values().stream().mapToInt(var0 -> var0.count).sum();
   }

   public void clear() {
      this.ribbons.clear();
      this.tickCount = 0L;
      this.glowVisible = (boolean)0;
   }

   public void removeSource(int var1) {
      this.ribbons.remove(var1);
   }

   public void tick() {
      this.tickCount++;
      this.ribbons.values().removeIf(var1 -> {
         while (var1.count > 0 && this.tickCount - var1.pointTicks[var1.head] > (var1.style == null ? this.lifetime : var1.style.lifetime()) * 20.0) {
            var1.head = (var1.head + 1) % 128;
            var1.count--;
         }

         return var1.count == 0;
      });
   }

   public void addPoint(int var1, Vec3 var2) {
      TrailRenderer.Ribbon var3 = this.ribbons.get(var1);
      if (var3 == null) {
         if (this.ribbons.size() >= 32) {
            return;
         }

         var3 = new TrailRenderer.Ribbon();
         this.ribbons.put(var1, var3);
      }

      if (var3.count > 0) {
         Vec3 var4 = var3.points[(var3.head + var3.count - 1) % 128];
         double var5 = var4.distanceToSqr(var2);
         if (var5 > 64.0) {
            var3.head = var3.count = 0;
         } else if (var5 < 4.0E-4) {
            return;
         }
      }

      if (var3.count == 128) {
         var3.head = (var3.head + 1) % 128;
         var3.count--;
      }

      int var7 = (var3.head + var3.count++) % 128;
      var3.points[var7] = var2;
      var3.pointTicks[var7] = this.tickCount;
   }

   public void addStyledPoint(int var1, Vec3 var2, TrailRenderer.RibbonStyle var3) {
      this.addPoint(var1, var2);
      TrailRenderer.Ribbon var4 = this.ribbons.get(var1);
      if (var4 != null) {
         var4.style = var3;
      }
   }

   public void buildRibbons(Vec3 var1, float var2) {
      this.glowVisible = (boolean)0;
      double var3 = TickTimer.getTime();

      for (TrailRenderer.Ribbon var6 : this.ribbons.values()) {
         var6.mesh.clear();
         var6.mesh.maxDistanceSq = 4096.0;
         TrailRenderer.RibbonStyle var7 = var6.style;
         double var8 = var7 == null ? this.lifetime : var7.lifetime();
         double var10 = var7 == null ? this.width : var7.width();
         int var12 = var7 == null ? this.colorFrom : var7.from();
         int var13 = var7 == null ? this.colorTo : var7.colorTo();
         boolean var14 = var7 == null ? this.smoothing : var7.smoothing();
         TrailRenderer.WidthCurve var15 = var7 == null ? this.widthCurve : var7.widthCurve();
         Easing.FadeCurve var16 = var7 == null ? this.alphaCurve : var7.alphaCurve();
         var6.blendMode = var7 == null ? this.blendMode : var7.blend();
         var6.glow = var7 == null ? this.glow : var7.glow();
         var6.animated = this.animated;

         for (int var17 = 1; var17 < var6.count; var17++) {
            int var18 = (var6.head + var17 - 1) % 128;
            int var19 = (var6.head + var17) % 128;
            Vec3 var20 = var6.points[var18];
            Vec3 var21 = var6.points[var19];
            if (var17 == var6.count - 1 && var6.pointTicks[var19] == this.tickCount) {
               var21 = var20.lerp(var21, var2);
            }

            double var22 = Math.clamp(1.0 - ((float)this.tickCount + var2 - (float)var6.pointTicks[var18]) / (var8 * 20.0), 0.0, 1.0);
            double var24 = Math.clamp(1.0 - ((float)this.tickCount + var2 - (float)var6.pointTicks[var19]) / (var8 * 20.0), 0.0, 1.0);
            if (var17 == 1) {
               this.totalPointCount(var6.mesh, var20, var22, var12, var13, var16, var15, var10, var3);
            }

            int var26 = var14 ? 4 : 1;
            if (var26 > 1) {
               Vec3 var27 = var17 > 1 ? var6.points[(var6.head + var17 - 2) % 128] : var20.add(var20.subtract(var21));
               Vec3 var28 = var17 < var6.count - 1 ? var6.points[(var6.head + var17 + 1) % 128] : var21.add(var21.subtract(var20));
               var6.spline.setControlPoints(var27, var20, var21, var28);
            }

            for (int var31 = 1; var31 <= var26; var31++) {
               double var32 = (double)var31 / var26;
               Vec3 var30 = var26 == 1 ? var21 : var6.spline.sample(var32);
               this.totalPointCount(var6.mesh, var30, var22 + (var24 - var22) * var32, var12, var13, var16, var15, var10, var3);
            }
         }

         if (var15 != TrailRenderer.WidthCurve.CONSTANT) {
            var6.mesh.fadeInTail();
         }

         var6.mesh.computeOffsets(var1);
         var6.visible = isRibbonVisible(var6.mesh, var1);
         if (var6.glow && var6.visible) {
            this.glowVisible = (boolean)1;
         }
      }
   }

   private static boolean isRibbonVisible(CatmullRomSpline.Ribbon var0, Vec3 var1) {
      for (int var2 = 1; var2 < var0.size; var2++) {
         if (var0.points[var2 - 1].distanceToSqr(var1) <= 4096.0
            && var0.points[var2 - 1].distanceToSqr(var0.points[var2]) >= 1.0E-10
            && (var0.widths[var2 - 1] > 0.0 || var0.widths[var2] > 0.0)
            && (var0.colors[var2 - 1] >>> 24 > 0 || var0.colors[var2] >>> 24 > 0)) {
            return true;
         }
      }

      return false;
   }

   private void totalPointCount(
      CatmullRomSpline.Ribbon var1, Vec3 var2, double var3, int var5, int var6, Easing.FadeCurve var7, TrailRenderer.WidthCurve var8, double var9, double var11
   ) {
      int var13 = ColorUtil.multiplyAlpha(ColorUtil.lerpColor(var6, var5, var3), var7.alpha(1.0 - var3));
      double var14 = var9 * this.isEmpty(var3, var8);
      if (this.animated) {
         var14 *= (1.0 + 0.075 * Math.sin(var2.x * 7.0 + var2.y * 3.0 + var2.z * 5.0 - var11 * 1.7)) * (1.0 + 0.3 * var3);
         var13 = ColorUtil.multiplyAlpha(var13, var3 * var3);
      }

      var1.addPoint(var2, var14, var13);
   }

   public void render(EffectsRenderer var1, float var2, boolean var3) {
      long var4 = PerfProfiler.startTimer();

      for (TrailRenderer.Ribbon var7 : this.ribbons.values()) {
         if (var7.visible && (!var3 || var7.glow)) {
            var1.blend(var7.blendMode);
            var1.drawTrail(var7.mesh, var7.animated);
         }
      }

      var1.blend(HitEffectConfig.BlendMode.NORMAL);
      PerfProfiler.endTimer("Trails", var4);
   }

   private double isEmpty(double var1, TrailRenderer.WidthCurve var3) {
      return switch (var3) {
         case CONSTANT -> 1.0;
         case QUADRATIC -> var1 * var1;
         case SINE -> Math.sin(Math.PI * var1);
         case CUBIC -> Math.pow(var1, 3.0) * 1.4;
      };
   }

   private static final class Ribbon {
      final Vec3[] points = new Vec3[128];
      final long[] pointTicks = new long[128];
      int head;
      int count;
      TrailRenderer.RibbonStyle style;
      final CatmullRomSpline.Ribbon mesh = new CatmullRomSpline.Ribbon(512);
      final CatmullRomSpline.SplineSegment spline = new CatmullRomSpline.SplineSegment();
      HitEffectConfig.BlendMode blendMode;
      boolean glow;
      boolean animated;
      boolean visible;
   }

   public record RibbonStyle(
      double lifetime,
      double width,
      int from,
      int to,
      boolean glow,
      boolean smoothing,
      HitEffectConfig.BlendMode blend,
      TrailRenderer.WidthCurve widthCurve,
      Easing.FadeCurve alphaCurve
   ) {
      private final int colorTo;

      public RibbonStyle(
         double lifetime,
         double width,
         int from,
         int to,
         boolean glow,
         boolean smoothing,
         HitEffectConfig.BlendMode blend,
         TrailRenderer.WidthCurve widthCurve,
         Easing.FadeCurve alphaCurve
      ) {
         if (Double.isFinite(lifetime) && !(lifetime <= 0.0) && Double.isFinite(width) && !(width <= 0.0)) {
            Objects.requireNonNull(blend);
            Objects.requireNonNull(widthCurve);
            Objects.requireNonNull(alphaCurve);
            this.lifetime = lifetime;
            this.width = width;
            this.from = from;
            this.colorTo = to;
            this.glow = glow;
            this.smoothing = smoothing;
            this.blend = blend;
            this.widthCurve = widthCurve;
            this.alphaCurve = alphaCurve;
         } else {
            throw new IllegalArgumentException("Trail lifetime and width must be finite and positive");
         }
      }
   }

   public enum WidthCurve {
      CONSTANT,
      QUADRATIC,
      SINE,
      CUBIC;
   }
}
