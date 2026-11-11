package com.larpclient.util.data;

import com.larpclient.util.math.Easing;
import com.larpclient.util.world.SurfaceEdgeFinder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import net.minecraft.world.phys.Vec3;

public final class PulseWaveTracker {
   public static final int mAX_WAVES = 6;
   private final List<PulseWaveTracker.Wave> waves = new ArrayList<>();

   public void addWave(PulseWaveTracker.Wave var1) {
      if (this.waves.size() == 6) {
         this.waves.removeFirst();
      }

      this.waves.add(var1);
   }

   public void removeExpired(double var1) {
      this.waves.removeIf(var2 -> var1 - var2.born >= var2.duration);
   }

   public void clear() {
      this.waves.clear();
   }

   public List<PulseWaveTracker.Wave> getWaves() {
      return Collections.unmodifiableList(this.waves);
   }

   public static double currentRadius(PulseWaveTracker.Wave var0, double var1) {
      double var3 = Math.clamp((var1 - var0.born) / var0.duration, 0.0, 1.0);
      return var0.radius * (1.0 - Math.pow(1.0 - var3, 3.0));
   }

   public static double fade(PulseWaveTracker.Wave var0, double var1) {
      double var3 = (var1 - var0.born) / var0.duration;
      return !(var3 < 0.0) && !(var3 >= 1.0) ? Easing.FadeCurve.SHARP_ATTACK_SMOOTH_RELEASE.alpha(var3) : 0.0;
   }

   public static double intensityAt(PulseWaveTracker.Wave var0, double var1, double var3) {
      double var5 = (var3 - var0.born) / var0.duration;
      if (!(var5 < 0.0) && !(var5 >= 1.0) && !(var1 > var0.radius)) {
         double var7 = var0.radius * (1.0 - Math.pow(1.0 - var5, 3.0));
         double var9 = Easing.smoothstep(1.0 - Math.abs(var1 - var7) / var0.waveWidth);
         return var9 * Easing.FadeCurve.SHARP_ATTACK_SMOOTH_RELEASE.alpha(var5);
      } else {
         return 0.0;
      }
   }

   public enum Style {
      RIPPLE,
      STAR;
   }

   public record Wave(
      Vec3 source,
      double born,
      double radius,
      double duration,
      double waveWidth,
      double thickness,
      int from,
      int to,
      double opacity,
      double glow,
      List<SurfaceEdgeFinder.Edge> edges,
      PulseWaveTracker.Style style
   ) {
      private final int endColor;

      public Wave(
         Vec3 var1,
         double var2,
         double var4,
         double var6,
         double var8,
         double var10,
         int var12,
         int var13,
         double var14,
         double var16,
         List<SurfaceEdgeFinder.Edge> var18
      ) {
         this(var1, var2, var4, var6, var8, var10, var12, var13, var14, var16, var18, PulseWaveTracker.Style.RIPPLE);
      }

      public Wave(
         Vec3 source,
         double born,
         double radius,
         double duration,
         double waveWidth,
         double thickness,
         int from,
         int to,
         double opacity,
         double glow,
         List<SurfaceEdgeFinder.Edge> edges,
         PulseWaveTracker.Style style
      ) {
         this.source = source;
         this.born = born;
         this.radius = radius;
         this.duration = duration;
         this.waveWidth = waveWidth;
         this.thickness = thickness;
         this.from = from;
         this.endColor = to;
         this.opacity = opacity;
         this.glow = glow;
         this.edges = edges;
         this.style = style;
      }
   }
}
