package com.larpclient;

import java.util.List;
import java.util.function.Consumer;

public final class q6 {
   public final com.larpclient.module.h o = new com.larpclient.module.h("Color 1", "First gradient color", -16738561);
   public final com.larpclient.module.h v = new com.larpclient.module.h("Color 2", "Second gradient color", -16111496);
   public final com.larpclient.module.b G = V("Line Width", "Outline radius in screen pixels", 2.0, 1.0, 8.0, 0.5);
   public final com.larpclient.module.b r = V("Outline Opacity", "Silhouette border opacity", 1.0, 0.0, 1.0, 0.05);
   public final com.larpclient.module.b z = V("Fill Opacity", "Gradient inside the silhouette", 0.25, 0.0, 1.0, 0.05);
   public final com.larpclient.module.b L = V("Glow Radius", "Gaussian halo radius in pixels", 12.0, 0.0, 32.0, 1.0);
   public final com.larpclient.module.b s = V("Glow Intensity", "Soft halo strength", 0.75, 0.0, 1.0, 0.05);
   public final com.larpclient.module.b t = V("Speed", "Gradient cycles per second", 0.5, 0.0, 3.0, 0.1);
   public final com.larpclient.module.b S = V("Gradient Scale", "Gradient repetitions across the screen", 1.0, 0.25, 4.0, 0.25);
   private final q6.q U = new q6.q();

   public q6(Consumer<com.larpclient.module.r<?>> var1) {
      for (com.larpclient.module.r var3 : List.of(this.o, this.v, this.G, this.r, this.z, this.L, this.s, this.t, this.S)) {
         var1.accept(var3);
      }

      this.L.a(() -> this.s.getValue() > 0.0);
   }

   private static com.larpclient.module.b V(String var0, String var1, double var2, double var4, double var6, double var8) {
      return new com.larpclient.module.b(var0, var1, var2, var4, var6, var8);
   }

   public List<com.larpclient.module.r<?>> t() {
      return List.of(this.o, this.v);
   }

   public List<com.larpclient.module.r<?>> m() {
      return List.of(this.G, this.r, this.z);
   }

   public List<com.larpclient.module.r<?>> P() {
      return List.of(this.L, this.s);
   }

   public List<com.larpclient.module.r<?>> y() {
      return List.of(this.t, this.S);
   }

   public q6.u g(boolean var1) {
      return new q6.u(
         this.o.getValue(),
         this.v.getValue(),
         this.G.getValue().floatValue(),
         this.S.getValue().floatValue(),
         this.z.getValue().floatValue(),
         this.r.getValue().floatValue(),
         this.L.getValue().floatValue(),
         this.s.getValue().floatValue(),
         this.U.i(hz.Q(), this.t.getValue()),
         var1
      );
   }

   public void A() {
      this.U.z();
   }

   public static final class q {
      private double w = Double.NaN;
      private double A;

      public float i(double var1, double var3) {
         if (Double.isFinite(var1) && Double.isFinite(var3)) {
            if (Double.isFinite(this.w) && var1 >= this.w) {
               this.A = (this.A + (var1 - this.w) * Math.max(0.0, var3)) % 1.0;
            }

            this.w = var1;
            return (float)this.A;
         } else {
            return (float)this.A;
         }
      }

      public void z() {
         this.w = Double.NaN;
         this.A = 0.0;
      }
   }

   public record u(
      int color1, int color2, float lineWidth, float scale, float fill, float outline, float radius, float intensity, float phase, boolean entityColor
   ) {
      public boolean glowing() {
         return this.radius > 0.0F && this.intensity > 0.0F;
      }
   }
}
