package com.larpclient;

import java.util.List;
import java.util.function.Consumer;

public final class bl {
   public final com.larpclient.module.i x = new com.larpclient.module.i("Color 1", "First gradient color", -16738561);
   public final com.larpclient.module.i P = new com.larpclient.module.i("Color 2", "Second gradient color", -16111496);
   public final com.larpclient.module.e k = R("Line Width", "Outline radius in screen pixels", 2.0, 1.0, 8.0, 0.5);
   public final com.larpclient.module.e m = R("Outline Opacity", "Silhouette border opacity", 1.0, 0.0, 1.0, 0.05);
   public final com.larpclient.module.e i = R("Fill Opacity", "Gradient inside the silhouette", 0.25, 0.0, 1.0, 0.05);
   public final com.larpclient.module.e V = R("Glow Radius", "Gaussian halo radius in pixels", 12.0, 0.0, 32.0, 1.0);
   public final com.larpclient.module.e T = R("Glow Intensity", "Soft halo strength", 0.75, 0.0, 1.0, 0.05);
   public final com.larpclient.module.e W = R("Speed", "Gradient cycles per second", 0.5, 0.0, 3.0, 0.1);
   public final com.larpclient.module.e K = R("Gradient Scale", "Gradient repetitions across the screen", 1.0, 0.25, 4.0, 0.25);
   private final bl.q X = new bl.q();

   public bl(Consumer<com.larpclient.module.q<?>> var1) {
      for (com.larpclient.module.q var3 : List.of(this.x, this.P, this.k, this.m, this.i, this.V, this.T, this.W, this.K)) {
         var1.accept(var3);
      }

      this.V.P(() -> this.T.getValue() > 0.0);
   }

   private static com.larpclient.module.e R(String var0, String var1, double var2, double var4, double var6, double var8) {
      return new com.larpclient.module.e(var0, var1, var2, var4, var6, var8);
   }

   public List<com.larpclient.module.q<?>> f() {
      return List.of(this.x, this.P);
   }

   public List<com.larpclient.module.q<?>> y() {
      return List.of(this.k, this.m, this.i);
   }

   public List<com.larpclient.module.q<?>> q() {
      return List.of(this.V, this.T);
   }

   public List<com.larpclient.module.q<?>> m() {
      return List.of(this.W, this.K);
   }

   public bl.d H(boolean var1) {
      return new bl.d(
         this.x.getValue(),
         this.P.getValue(),
         this.k.getValue().floatValue(),
         this.K.getValue().floatValue(),
         this.i.getValue().floatValue(),
         this.m.getValue().floatValue(),
         this.V.getValue().floatValue(),
         this.T.getValue().floatValue(),
         this.X.d(cf.F(), this.W.getValue()),
         var1
      );
   }

   public void v() {
      this.X.J();
   }

   public record d(
      int color1, int color2, float lineWidth, float scale, float fill, float outline, float radius, float intensity, float phase, boolean entityColor
   ) {
      public boolean glowing() {
         return this.radius > 0.0F && this.intensity > 0.0F;
      }
   }

   public static final class q {
      private double T = Double.NaN;
      private double r;

      public float d(double var1, double var3) {
         if (Double.isFinite(var1) && Double.isFinite(var3)) {
            if (Double.isFinite(this.T) && var1 >= this.T) {
               this.r = (this.r + (var1 - this.T) * Math.max(0.0, var3)) % 1.0;
            }

            this.T = var1;
            return (float)this.r;
         } else {
            return (float)this.r;
         }
      }

      public void J() {
         this.T = Double.NaN;
         this.r = 0.0;
      }
   }
}
