package com.larpclient;

import com.larpclient.module.df;
import com.larpclient.module.dk;
import java.util.List;
import java.util.function.Consumer;

public final class p3 {
   public final dk q = new dk("Color 1", "First gradient color", -16738561);
   public final dk x = new dk("Color 2", "Second gradient color", -16111496);
   public final com.larpclient.module.d9 K = v("Line Width", "Outline radius in screen pixels", 2.0, 1.0, 8.0, 0.5);
   public final com.larpclient.module.d9 p = v("Outline Opacity", "Silhouette border opacity", 1.0, 0.0, 1.0, 0.05);
   public final com.larpclient.module.d9 r = v("Fill Opacity", "Gradient inside the silhouette", 0.25, 0.0, 1.0, 0.05);
   public final com.larpclient.module.d9 E = v("Glow Radius", "Gaussian halo radius in pixels", 12.0, 0.0, 32.0, 1.0);
   public final com.larpclient.module.d9 C = v("Glow Intensity", "Soft halo strength", 0.75, 0.0, 1.0, 0.05);
   public final com.larpclient.module.d9 P = v("Speed", "Gradient cycles per second", 0.5, 0.0, 3.0, 0.1);
   public final com.larpclient.module.d9 a = v("Gradient Scale", "Gradient repetitions across the screen", 1.0, 0.25, 4.0, 0.25);
   private final p3.s F = new p3.s();

   public p3(Consumer<df<?>> var1) {
      for (df var3 : List.of(this.q, this.x, this.K, this.p, this.r, this.E, this.C, this.P, this.a)) {
         var1.accept(var3);
      }

      this.E.H(() -> this.C.P() > 0.0);
   }

   private static com.larpclient.module.d9 v(String var0, String var1, double var2, double var4, double var6, double var8) {
      return new com.larpclient.module.d9(var0, var1, var2, var4, var6, var8);
   }

   public List<df<?>> N() {
      return List.of(this.q, this.x);
   }

   public List<df<?>> F() {
      return List.of(this.K, this.p, this.r);
   }

   public List<df<?>> w() {
      return List.of(this.E, this.C);
   }

   public List<df<?>> f() {
      return List.of(this.P, this.a);
   }

   public p3.p a(boolean var1) {
      return new p3.p(
         this.q.P(),
         this.x.P(),
         this.K.P().floatValue(),
         this.a.P().floatValue(),
         this.r.P().floatValue(),
         this.p.P().floatValue(),
         this.E.P().floatValue(),
         this.C.P().floatValue(),
         this.F.b(ty.K(), this.P.P()),
         var1
      );
   }

   public void i() {
      this.F.Z();
   }

   public record p(
      int color1, int color2, float lineWidth, float scale, float fill, float outline, float radius, float intensity, float phase, boolean entityColor
   ) {
      public boolean glowing() {
         return this.radius > 0.0F && this.intensity > 0.0F;
      }
   }

   public static final class s {
      private double b = Double.NaN;
      private double j;

      public float b(double var1, double var3) {
         if (Double.isFinite(var1) && Double.isFinite(var3)) {
            if (Double.isFinite(this.b) && var1 >= this.b) {
               this.j = (this.j + (var1 - this.b) * Math.max(0.0, var3)) % 1.0;
            }

            this.b = var1;
            return (float)this.j;
         } else {
            return (float)this.j;
         }
      }

      public void Z() {
         this.b = Double.NaN;
         this.j = 0.0;
      }
   }
}
