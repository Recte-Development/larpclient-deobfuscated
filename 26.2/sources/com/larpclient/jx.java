package com.larpclient;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.Identifier;

public final class jx extends jd {
   public String K;
   public boolean z;
   public boolean g;
   public boolean x;
   public boolean a;
   public boolean R;
   public Boolean M;
   public Runnable Q;
   public sr.e q;
   public String X;
   public String l;
   public String b;
   public boolean I;
   public boolean h;
   public boolean j;
   public boolean f;
   public boolean s;
   public boolean G;
   public boolean y;
   public Object r;
   public double d;
   public double C;
   public boolean n;
   private final Runnable e;
   private final jp t;
   private final jp v;
   private final jp N;
   private final jp F;
   private boolean O;
   private lv V;
   private long B;
   private long c;
   private float k;
   private float D;
   private float P;

   public lv B() {
      return this.V;
   }

   public float g() {
      return this.P;
   }

   public double S(int var1, int var2) {
      return this.t.k(!this.contains(var1, var2) && (!this.T || !this.W) ? 0.0 : 1.0, 160);
   }

   public jx(String var1, Runnable var2) {
      this.q = sr.e.q;
      this.X = "";
      this.l = "";
      this.b = "";
      this.d = 1.0;
      this.C = 1.0;
      this.t = new jp(0.0);
      this.v = new jp(0.0);
      this.N = new jp(0.0);
      this.F = new jp(0.0);
      this.V = lv.at(0.0, 0.0, ho.Q(this.S));
      this.K = var1;
      this.e = var2;
   }

   public static int l(int var0) {
      return (var0 < 144 ? 18 : 34) + 14;
   }

   public int y() {
      return Math.max(1, this.A - (this.h ? 82 : 54));
   }

   public int U() {
      return this.u + (this.J - 13) / 2;
   }

   @Override
   public void render(GuiGraphicsExtractor var1, int var2, int var3) {
      if (this.i) {
         double var4 = this.S(var2, var3);
         double var6 = this.v.k(this.z && !this.R ? 1.0 : 0.0, 180);
         double var8 = this.F.k(System.nanoTime() < this.B ? 1.0 : 0.0, 100);
         if (this.G) {
            int var35 = this.x ? (this.a ? -24915 : lv.readableAccent(ho.Q(this.S), ho.A)) : ho.B;
            if (!this.p) {
               var4 = 0.0;
            }

            int var36 = ho.Q;
            if (this.x && ho.S()) {
               wp.e(var1, this.S, this.u, this.A, this.J, var36, 5.0F, uh.H(var35, 0.18 + 0.14 * var4));
            }

            if (!this.x && ho.S()) {
               if (var4 > 0.001) {
                  wp.I(var1, this.S, this.u, this.A, this.J, var36, uh.H(ho.u, var4 * 0.045));
               }

               tb.s(var1, this.S, this.u, this.A, this.J, var36, uh.F(ho.Y, 1.3 + var4 * 0.7), uh.F(ho.S, 1.6 + var4 * 0.9));
            } else {
               wp.I(var1, this.S, this.u, this.A, this.J, var36, uh.q(var35, ho.e, var4 * 0.15));
            }

            int var42 = !this.p ? ho.j : (this.x ? lv.controlInk(var35) : (this.a ? -24915 : uh.q(ho.Z, ho.u, this.r != null ? 0.7 + 0.3 * var4 : var4)));
            if (!this.l.isEmpty()) {
               ur.O(var1, ur.V(this.l), this.S + (this.A - 10) / 2, this.u + (this.J - 10) / 2, 10, var42);
            } else {
               bj.u var44 = this.r != null ? bj.u.X : bj.u.M;
               String var46 = bj.i(this.K, this.A - 8, var44);
               int var48 = ho.S() ? this.S + (this.A - bj.e(var46, var44)) / 2 : this.S + 4;
               bj.k(var1, var46, var48, this.u + (this.J - var44.n()) / 2, var42, var44);
            }

            if (this.T && this.W) {
               wp.t(var1, this.S, this.u, this.A, this.J, var36);
            }
         } else if (this.s) {
            int var34 = ho.S() ? ho.dr : 6;
            if (ho.S()) {
               wp.S(var1, this.S, this.u, this.A, this.J, var34, uh.q(ho.f, ho.e, var4), 0.4 + 0.6 * var4);
            } else {
               wp.I(var1, this.S, this.u, this.A, this.J, var34, uh.q(ho.A, ho.e, var4 * 0.35));
            }

            int var11 = l(this.A);
            int var41 = var11 - 14;
            wp.I(var1, this.S + 7, this.u + 7, var41, var41, 6, ho.dw);
            var1.blit(
               RenderPipelines.GUI_TEXTURED,
               Identifier.fromNamespaceAndPath("minecraft", "textures/misc/unknown_server.png"),
               this.S + 7,
               this.u + 7,
               0.0F,
               0.0F,
               var41,
               var41,
               64,
               64,
               64,
               64,
               ua.I(-1)
            );
            bj.k(var1, bj.i(this.K, this.A - var11 - 7, bj.u.i), this.S + var11, this.u + 6, ho.u, bj.u.i);
            bj.k(var1, bj.i(this.X, this.A - var11 - 7, bj.u.M), this.S + var11, this.u + 19, ho.Z, bj.u.M);
            if (this.T && this.W) {
               wp.t(var1, this.S, this.u, this.A, this.J, var34);
            }
         } else if (this.j) {
            this.e(var1, var4, var8);
         } else if (this.f) {
            this.P(var1, var4, var6, var8);
         } else {
            if (!this.O && this.M != null) {
               this.N.F(this.M ? 1.0 : 0.0);
               this.O = (boolean)1;
            }

            double var10 = this.N.k(Boolean.TRUE.equals(this.M) ? 1.0 : 0.0, 180);
            var1.pose().pushMatrix();

            try {
               if (this.I && ho.S()) {
                  this.V = lv.at(var10, var4, ho.Q(this.S));
                  ho.p var39 = ho.m();
                  int var43 = this.V.accent();
                  boolean var45 = var39.enabled().equals("Glow");
                  int var47 = !var45 && !var39.enabled().equals("Tint") ? 0 : 1;
                  int var16 = ho.dr;
                  this.P = var39.hoverLift() ? (float)var4 : 0.0F;
                  var1.pose().translate(0.0F, -this.P);
                  if (this.P > 0.01F && !var39.card().equals("Flat")) {
                     tb.Q(var1, this.S, this.u, this.A, this.J, var16, 5.0F, uh.F(ho.v, 0.25 * this.P), uh.F(ho.v, 0.6 * this.P));
                  }

                  if (var45 && var10 > 0.01) {
                     wp.e(var1, this.S, this.u, this.A, this.J, var16, 9.0F, uh.H(var43, 0.14 * var10));
                  }

                  switch (var39.card()) {
                     case "Outline":
                        if (var4 > 0.001) {
                           wp.I(var1, this.S, this.u, this.A, this.J, var16, uh.H(ho.u, 0.035 * var4));
                        }
                        break;
                     case "Flat":
                        wp.I(var1, this.S, this.u, this.A, this.J, var16, uh.F(this.V.background(), 0.75));
                        break;
                     default:
                        wp.I(var1, this.S, this.u, this.A, this.J, var16, this.V.background());
                  }

                  if (var47 != 0 && var10 > 0.01) {
                     tb.d(var1, this.S, this.u, this.A * 0.7F, this.J, var16, uh.H(var43, (var45 ? 0.15 : 0.22) * var10), uh.H(var43, 0.0));
                  }

                  if (!var39.card().equals("Flat")) {
                     double var49 = (var39.card().equals("Outline") ? 0.85 : 0.55) + 0.25 * var4;
                     int var19 = uh.q(uh.H(ho.c, var49), uh.H(var43, 0.55), var47 != 0 ? 0.8 * var10 : 0.0);
                     tb.s(var1, this.S, this.u, this.A, this.J, var16, var19, var19);
                  }

                  double var50 = (System.nanoTime() - this.c) / 6.5E8;
                  if (var39.ripple() && var50 < 1.0) {
                     float var51 = 10.0F + (float)jp.m.S.s(var50) * this.A * 0.9F;
                     tb.d(var1, this.S, this.u, this.A, this.J, var16, this.k, this.D, var51, uh.H(var43, 0.22 * (1.0 - var50)));
                  }

                  if (var6 > 0.001) {
                     wp.I(var1, this.S, this.u, this.A, this.J, ho.dr, uh.H(ho.u, 0.045 * var6));
                     tb.s(var1, this.S, this.u, this.A, this.J, ho.dr, uh.H(this.V.accent(), 0.28 * var6), uh.H(this.V.accent(), 0.18 * var6));
                     wp.m(var1, this.S, this.u, this.A, this.J, ho.dr, uh.H(this.V.accent(), var6));
                  }

                  int var52 = uh.q(uh.q(this.V.subtitle(), this.V.title(), 0.75 + 0.25 * var4), this.V.title(), var10);
                  if (var39.enabled().equals("Text")) {
                     var52 = uh.q(var52, var43, var10);
                  }

                  bj.k(var1, bj.i(this.K, this.y(), bj.u.i), this.S + 12, this.u + this.J / 2 - 12, var52, bj.u.i);
                  bj.k(var1, bj.i(this.X, this.y(), bj.u.X), this.S + 12, this.u + this.J / 2 + 2, this.V.subtitle(), bj.u.X);
                  wp.Q(var1, this.S + this.A - 32, this.U(), var10, this.V.accent(), this.V.light());
               } else if (this.I) {
                  this.V = lv.at(var10, var4, ho.Q(this.S));
                  wp.Z(var1, this.S, this.u, this.A, this.J, ho.dr, uh.q(this.V.background(), ho.o, var6), 0.9 + var4 * 0.6);
                  if (var6 > 0.001) {
                     tb.s(var1, this.S, this.u, this.A, this.J, ho.dr, uh.H(ho.u, 0.34 * var6), uh.H(ho.u, 0.26 * var6));
                  }

                  bj.k(var1, bj.i(this.K, this.y(), bj.u.i), this.S + 12, this.u + this.J / 2 - 12, this.V.title(), bj.u.i);
                  bj.k(var1, bj.i(this.X, this.y(), bj.u.X), this.S + 12, this.u + this.J / 2 + 2, this.V.subtitle(), bj.u.X);
                  wp.Q(var1, this.S + this.A - 32, this.U(), var10, this.V.accent(), this.V.light());
               } else if (this.y) {
                  if (var4 > 0.001) {
                     wp.I(var1, this.S, this.u, this.A, this.J, ho.Q, uh.H(ho.u, var4 * 0.045));
                  }

                  tb.s(var1, this.S, this.u, this.A, this.J, ho.Q, uh.F(ho.Y, 1.3 + var4 * 0.7), uh.F(ho.S, 1.6 + var4 * 0.9));
                  String var38 = bj.T(this.K, Math.max(1, this.A - 12));
                  bj.E(var1, var38, this.S + (this.A - bj.u(var38)) / 2, this.u + (this.J - 9) / 2, this.p ? uh.q(ho.Z, ho.u, var4) : ho.j);
               } else {
                  if (this.g) {
                     double var12 = Math.max(var6, var4 * 0.6);
                     if (var12 > 0.001) {
                        wp.I(var1, this.S, this.u, this.A, this.J, ho.Q, uh.H(ho.u, 0.065 * var12));
                     }
                  } else {
                     wp.Z(var1, this.S, this.u, this.A, this.J, ho.D, uh.q(ho.f, ho.e, Math.max(var6 * 0.65, var4)), 0.9 + var4 * 0.6);
                  }

                  int var37 = this.l.isEmpty() ? 8 : 27;
                  int var13 = Math.min(13, Math.max(1, this.J - 2));
                  if (!this.l.isEmpty()) {
                     ur.O(
                        var1,
                        ur.V(this.l),
                        this.S + (this.K.isEmpty() ? (this.A - var13) / 2 : 8),
                        this.u + (this.J - var13) / 2,
                        var13,
                        this.z ? ho.u : uh.q(ho.Z, ho.u, var4)
                     );
                  }

                  int var14 = this.A - var37 - (this.M == null ? 8 : 34);
                  if (var14 > 4 && this.C > 0.001) {
                     if (this.n) {
                        var1.enableScissor(this.S + var37, this.u, this.S + this.A - 8, this.u + this.J);
                     }

                     try (ua var15 = ua.K(this.C)) {
                        bj.E(
                           var1,
                           this.n ? this.K : bj.T(this.K, var14),
                           this.S + var37,
                           this.u + (this.X.isEmpty() ? (this.J - 9) / 2 : 4),
                           this.p ? (this.a ? -24915 : (this.x ? ho.Q(this.S) : (this.R && !this.z ? uh.q(ho.Z, ho.u, var4) : ho.u))) : ho.Z
                        );
                     } finally {
                        if (this.n) {
                           var1.disableScissor();
                        }
                     }
                  }

                  if (!this.X.isEmpty()) {
                     bj.E(var1, bj.T(this.X, this.A - 42), this.S + 8, this.u + 16, ho.Z);
                  }

                  if (this.M != null) {
                     wp.E(var1, this.S + this.A - 32, this.u + (this.J - 12) / 2, var10);
                  }
               }

               int var40 = this.I ? ho.dr : ho.Q;
               if (var8 > 0.001) {
                  wp.I(var1, this.S, this.u, this.A, this.J, var40, uh.H(ho.u, var8 * 0.07));
               }

               if (this.T && this.W) {
                  wp.t(var1, this.S, this.u, this.A, this.J, var40);
               }
            } finally {
               var1.pose().popMatrix();
            }
         }
      }
   }

   private void P(GuiGraphicsExtractor var1, double var2, double var4, double var6) {
      double var8 = Math.max(var2, var6) * (1.0 - var4);
      if (var8 > 0.001) {
         kv.f(var1, this.S, this.u, this.A, this.J, ho.k, uh.H(ho.u, 0.05 * var8));
      }

      int var10 = uh.q(ho.Z, ho.u, 0.45);
      int var11 = uh.q(uh.q(var10, ho.u, var2 * 0.85), ho.u, var4);
      int var12 = Math.min(12, this.J - 2);
      int var13 = ho.S(this.S, ho.A);
      if (lv.controlInk(ho.A) == -1) {
         var13 = uh.q(var13, -1, 0.12);
      }

      if (!this.l.isEmpty()) {
         ur.O(var1, ur.V(this.l), this.S + 8, this.u + (this.J - var12) / 2, var12, uh.q(uh.q(ho.Z, ho.u, var2 * 0.7), var13, var4));
      }

      if (this.C > 0.001) {
         byte var14 = 27;
         var1.enableScissor(this.S + var14, this.u, this.S + this.A - 4, this.u + this.J);

         try (ua var15 = ua.K(this.C)) {
            bj.u var16 = bj.u.I;
            bj.k(var1, bj.i(this.K, Math.max(1, this.A - var14 - 4), var16), this.S + var14, this.u + (this.J - var16.n()) / 2, var11, var16);
         } finally {
            var1.disableScissor();
         }
      }

      if (this.T && this.W) {
         wp.t(var1, this.S, this.u, this.A, this.J, ho.k);
      }
   }

   private void e(GuiGraphicsExtractor var1, double var2, double var4) {
      var1.pose().pushMatrix();

      try {
         var1.pose().translate(this.S + this.A / 2.0F, this.u + this.J / 2.0F);
         float var6 = (float)(1.0 + 0.018 * var2 - 0.025 * var4);
         var1.pose().scale(var6, var6);
         var1.pose().translate(-this.S - this.A / 2.0F, -this.u - this.J / 2.0F);
         wp.I(var1, this.S, this.u, this.A, this.J, 2, uh.F(uh.q(-2144326101, -1337177788, var2), this.d));
         int var7 = uh.F(this.p ? -1711656 : -6711922, this.d);
         int var8 = this.l.isEmpty() ? 7 : 22;
         if (!this.l.isEmpty()) {
            ur.O(var1, ur.V(this.l), this.S + 6, this.u + (this.J - 10) / 2, 10, var7);
         }

         bj.E(var1, bj.T(this.K, this.A - var8 - 6), this.S + var8 + (int)Math.round(var2 * 2.0), this.u + (this.J - 9) / 2, var7);
         if (this.T && this.W) {
            wp.L(var1, this.S, this.u, this.A, this.J, uh.F(-1711656, this.d));
         }
      } finally {
         var1.pose().popMatrix();
      }
   }

   @Override
   public boolean click(double var1, double var3, int var5) {
      if (this.p && var5 == 0 && this.contains(var1, var3)) {
         this.B = System.nanoTime() + 110000000L;
         if (this.I) {
            this.c = System.nanoTime();
            this.k = (float)var1;
            this.D = (float)var3;
         }

         sr.v(this.q);
         if (this.Q != null && var1 >= this.S + this.A - 38) {
            this.Q.run();
         } else {
            this.e.run();
         }

         return true;
      } else {
         return false;
      }
   }

   @Override
   public boolean key(int var1) {
      if (this.i && this.T && this.p && (var1 == 257 || var1 == 32)) {
         this.B = System.nanoTime() + 110000000L;
         sr.v(this.q);
         if (this.I && var1 == 32 && this.Q != null) {
            this.Q.run();
         } else {
            this.e.run();
         }

         return true;
      } else {
         return false;
      }
   }
}
