package com.larpclient;

import java.util.List;
import java.util.function.Consumer;

public final class rg {
   public final com.larpclient.module.lv R = new com.larpclient.module.lv("Color 1", "First gradient color", -16738561);
   public final com.larpclient.module.lv s = new com.larpclient.module.lv("Color 2", "Second gradient color", -16111496);
   public final com.larpclient.module.lc y = Y("Line Width", "Outline radius in screen pixels", 2.0, 1.0, 8.0, 0.5);
   public final com.larpclient.module.lc E = Y("Outline Opacity", "Silhouette border opacity", 1.0, 0.0, 1.0, 0.05);
   public final com.larpclient.module.lc o = Y("Fill Opacity", "Gradient inside the silhouette", 0.25, 0.0, 1.0, 0.05);
   public final com.larpclient.module.lc B = Y("Glow Radius", "Gaussian halo radius in pixels", 12.0, 0.0, 32.0, 1.0);
   public final com.larpclient.module.lc L = Y("Glow Intensity", "Soft halo strength", 0.75, 0.0, 1.0, 0.05);
   public final com.larpclient.module.lc n = Y("Speed", "Gradient cycles per second", 0.5, 0.0, 3.0, 0.1);
   public final com.larpclient.module.lc G = Y("Gradient Scale", "Gradient repetitions across the screen", 1.0, 0.25, 4.0, 0.25);
   private final rg.f V = new rg.f();

   public rg(Consumer<com.larpclient.module.l6<?>> var1) {
      for (com.larpclient.module.l6 var3 : List.of(this.R, this.s, this.y, this.E, this.o, this.B, this.L, this.n, this.G)) {
         var1.accept(var3);
      }

      this.B.x(() -> this.L.getValue() > 0.0);
   }

   private static com.larpclient.module.lc Y(String var0, String var1, double var2, double var4, double var6, double var8) {
      return new com.larpclient.module.lc(var0, var1, var2, var4, var6, var8);
   }

   public List<com.larpclient.module.l6<?>> r() {
      return List.of(this.R, this.s);
   }

   public List<com.larpclient.module.l6<?>> w() {
      return List.of(this.y, this.E, this.o);
   }

   public List<com.larpclient.module.l6<?>> a() {
      return List.of(this.B, this.L);
   }

   public List<com.larpclient.module.l6<?>> J() {
      return List.of(this.n, this.G);
   }

   public rg.q I(boolean var1) {
      return new rg.q(
         this.R.getValue(),
         this.s.getValue(),
         this.y.getValue().floatValue(),
         this.G.getValue().floatValue(),
         this.o.getValue().floatValue(),
         this.E.getValue().floatValue(),
         this.B.getValue().floatValue(),
         this.L.getValue().floatValue(),
         this.V.x(ea.y(), this.n.getValue()),
         var1
      );
   }

   public void C() {
      this.V.l();
   }

   public static final class f {
      private double d = Double.NaN;
      private double t;

      public float x(double var1, double var3) {
         if (Double.isFinite(var1) && Double.isFinite(var3)) {
            if (Double.isFinite(this.d) && var1 >= this.d) {
               this.t = (this.t + (var1 - this.d) * Math.max(0.0, var3)) % 1.0;
            }

            this.d = var1;
            return (float)this.t;
         } else {
            return (float)this.t;
         }
      }

      public void l() {
         this.d = Double.NaN;
         this.t = 0.0;
      }
   }

   public record q(
      int color1, int color2, float lineWidth, float scale, float fill, float outline, float radius, float intensity, float phase, boolean entityColor
   ) {
      public boolean glowing() {
         return this.radius > 0.0F && this.intensity > 0.0F;
      }
   }
}
