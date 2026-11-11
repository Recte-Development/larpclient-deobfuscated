package com.larpclient.config;

import com.larpclient.module.misc.ColorSetting;
import com.larpclient.module.misc.NumberSetting;
import com.larpclient.module.misc.Setting;
import com.larpclient.util.data.TickTimer;
import java.util.List;
import java.util.function.Consumer;

public final class GradientOutlineSettings {
   public final ColorSetting color1 = new ColorSetting("Color 1", "First gradient color", -16738561);
   public final ColorSetting color2 = new ColorSetting("Color 2", "Second gradient color", -16111496);
   public final NumberSetting lineWidth = createSlider("Line Width", "Outline radius in screen pixels", 2.0, 1.0, 8.0, 0.5);
   public final NumberSetting outlineOpacity = createSlider("Outline Opacity", "Silhouette border opacity", 1.0, 0.0, 1.0, 0.05);
   public final NumberSetting fillOpacity = createSlider("Fill Opacity", "Gradient inside the silhouette", 0.25, 0.0, 1.0, 0.05);
   public final NumberSetting glowRadius = createSlider("Glow Radius", "Gaussian halo radius in pixels", 12.0, 0.0, 32.0, 1.0);
   public final NumberSetting glowIntensity = createSlider("Glow Intensity", "Soft halo strength", 0.75, 0.0, 1.0, 0.05);
   public final NumberSetting speed = createSlider("Speed", "Gradient cycles per second", 0.5, 0.0, 3.0, 0.1);
   public final NumberSetting gradientScale = createSlider("Gradient Scale", "Gradient repetitions across the screen", 1.0, 0.25, 4.0, 0.25);
   private final GradientOutlineSettings.PhaseTracker phaseTracker = new GradientOutlineSettings.PhaseTracker();

   public GradientOutlineSettings(Consumer<Setting<?>> var1) {
      for (Setting var3 : List.of(
         this.color1, this.color2, this.lineWidth, this.outlineOpacity, this.fillOpacity, this.glowRadius, this.glowIntensity, this.speed, this.gradientScale
      )) {
         var1.accept(var3);
      }

      this.glowRadius.visibleWhen(() -> this.glowIntensity.getValue() > 0.0);
   }

   private static NumberSetting createSlider(String var0, String var1, double var2, double var4, double var6, double var8) {
      return new NumberSetting(var0, var1, var2, var4, var6, var8);
   }

   public List<Setting<?>> colorSettings() {
      return List.of(this.color1, this.color2);
   }

   public List<Setting<?>> outlineSettings() {
      return List.of(this.lineWidth, this.outlineOpacity, this.fillOpacity);
   }

   public List<Setting<?>> glowSettings() {
      return List.of(this.glowRadius, this.glowIntensity);
   }

   public List<Setting<?>> animationSettings() {
      return List.of(this.speed, this.gradientScale);
   }

   public GradientOutlineSettings.Snapshot snapshot(boolean var1) {
      return new GradientOutlineSettings.Snapshot(
         this.color1.getValue(),
         this.color2.getValue(),
         this.lineWidth.getValue().floatValue(),
         this.gradientScale.getValue().floatValue(),
         this.fillOpacity.getValue().floatValue(),
         this.outlineOpacity.getValue().floatValue(),
         this.glowRadius.getValue().floatValue(),
         this.glowIntensity.getValue().floatValue(),
         this.phaseTracker.advance(TickTimer.getTime(), this.speed.getValue()),
         var1
      );
   }

   public void resetPhase() {
      this.phaseTracker.reset();
   }

   public static final class PhaseTracker {
      private double lastTime = Double.NaN;
      private double phase;

      public float advance(double var1, double var3) {
         if (Double.isFinite(var1) && Double.isFinite(var3)) {
            if (Double.isFinite(this.lastTime) && var1 >= this.lastTime) {
               this.phase = (this.phase + (var1 - this.lastTime) * Math.max(0.0, var3)) % 1.0;
            }

            this.lastTime = var1;
            return (float)this.phase;
         } else {
            return (float)this.phase;
         }
      }

      public void reset() {
         this.lastTime = Double.NaN;
         this.phase = 0.0;
      }
   }

   public record Snapshot(
      int color1, int color2, float lineWidth, float scale, float fill, float outline, float radius, float intensity, float phase, boolean entityColor
   ) {
      public boolean glowing() {
         return this.radius > 0.0F && this.intensity > 0.0F;
      }
   }
}
