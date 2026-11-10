package com.larpclient;

import java.util.List;
import java.util.function.Consumer;

public final class ba {
   public final com.larpclient.module.l0 P = new com.larpclient.module.l0("Color 1", "First gradient color", -16738561);
   public final com.larpclient.module.l0 f = new com.larpclient.module.l0("Color 2", "Second gradient color", -16111496);
   public final com.larpclient.module.lo k = F("Line Width", "Outline radius in screen pixels", 2.0, 1.0, 8.0, 0.5);
   public final com.larpclient.module.lo q = F("Outline Opacity", "Silhouette border opacity", 1.0, 0.0, 1.0, 0.05);
   public final com.larpclient.module.lo s = F("Fill Opacity", "Gradient inside the silhouette", 0.25, 0.0, 1.0, 0.05);
   public final com.larpclient.module.lo x = F("Glow Radius", "Gaussian halo radius in pixels", 12.0, 0.0, 32.0, 1.0);
   public final com.larpclient.module.lo n = F("Glow Intensity", "Soft halo strength", 0.75, 0.0, 1.0, 0.05);
   public final com.larpclient.module.lo U = F("Speed", "Gradient cycles per second", 0.5, 0.0, 3.0, 0.1);
   public final com.larpclient.module.lo N = F("Gradient Scale", "Gradient repetitions across the screen", 1.0, 0.25, 4.0, 0.25);
   private final ba.o u = new ba.o();

   public ba(Consumer<com.larpclient.module.lb<?>> var1) {
      for (com.larpclient.module.lb var3 : List.of(this.P, this.f, this.k, this.q, this.s, this.x, this.n, this.U, this.N)) {
         var1.accept(var3);
      }

      this.x.O(() -> this.n.getValue() > 0.0);
   }

   private static com.larpclient.module.lo F(String var0, String var1, double var2, double var4, double var6, double var8) {
      return new com.larpclient.module.lo(var0, var1, var2, var4, var6, var8);
   }

   public List<com.larpclient.module.lb<?>> g() {
      return List.of(this.P, this.f);
   }

   public List<com.larpclient.module.lb<?>> c() {
      return List.of(this.k, this.q, this.s);
   }

   public List<com.larpclient.module.lb<?>> k() {
      return List.of(this.x, this.n);
   }

   public List<com.larpclient.module.lb<?>> m() {
      return List.of(this.U, this.N);
   }

   public ba.e g(boolean var1) {
      return new ba.e(
         this.P.getValue(),
         this.f.getValue(),
         this.k.getValue().floatValue(),
         this.N.getValue().floatValue(),
         this.s.getValue().floatValue(),
         this.q.getValue().floatValue(),
         this.x.getValue().floatValue(),
         this.n.getValue().floatValue(),
         this.u.U(qo.x(), this.U.getValue()),
         var1
      );
   }

   public void B() {
      this.u.b();
   }

   public record e(
      int color1, int color2, float lineWidth, float scale, float fill, float outline, float radius, float intensity, float phase, boolean entityColor
   ) {
      public boolean glowing() {
         return this.radius > 0.0F && this.intensity > 0.0F;
      }
   }

   public static final class o {
      private double t = Double.NaN;
      private double Y;

      public float U(double var1, double var3) {
         if (Double.isFinite(var1) && Double.isFinite(var3)) {
            if (Double.isFinite(this.t) && var1 >= this.t) {
               this.Y = (this.Y + (var1 - this.t) * Math.max(0.0, var3)) % 1.0;
            }

            this.t = var1;
            return (float)this.Y;
         } else {
            return (float)this.Y;
         }
      }

      public void b() {
         this.t = Double.NaN;
         this.Y = 0.0;
      }
   }
}
