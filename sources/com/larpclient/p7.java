package com.larpclient;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.Identifier;

public final class p7 extends oq {
   public String J;
   public boolean R;
   public boolean c;
   public boolean d;
   public boolean z;
   public boolean U;
   public Boolean I;
   public Runnable H;
   public hx.t s = hx.t.X;
   public String y = "";
   public String e = "";
   public String Y = "";
   public boolean n;
   public boolean X;
   public boolean w;
   public boolean W;
   public boolean P;
   public boolean q;
   public boolean Q;
   public double Z = 1.0;
   public double t = 1.0;
   public boolean p;
   private final Runnable a;
   private final u7 C = new u7(0.0);
   private final u7 S = new u7(0.0);
   private final u7 f = new u7(0.0);
   private final u7 l = new u7(0.0);
   private boolean g;
   private jd E = jd.at(0.0, 0.0, rv.m(this.B));
   private long i;
   private long K;
   private float V;
   private float r;
   private float j;

   public jd Y() {
      return this.E;
   }

   public float D() {
      return this.j;
   }

   public double a(int var1, int var2) {
      return this.C.y(!this.contains(var1, var2) && (!this.A || !this.h) ? 0.0 : 1.0, 160);
   }

   public p7(String var1, Runnable var2) {
      this.J = var1;
      this.a = var2;
   }

   public static int N(int var0) {
      return (var0 < 144 ? 18 : 34) + 14;
   }

   public int h() {
      return Math.max(1, this.v - (this.X ? 82 : 54));
   }

   public int d() {
      return this.L + (this.O - 13) / 2;
   }

   @Override
   public void render(GuiGraphicsExtractor var1, int var2, int var3) {
      if (this.F) {
         double var4 = this.a(var2, var3);
         double var6 = this.S.y(this.R && !this.U ? 1.0 : 0.0, 180);
         double var8 = this.l.y(System.nanoTime() < this.i ? 1.0 : 0.0, 100);
         if (this.q) {
            int var35 = this.d ? (this.z ? -24915 : jd.readableAccent(rv.m(this.B), rv.w)) : rv.z;
            if (!this.x) {
               var4 = 0.0;
            }

            int var36 = rv.g;
            if (this.d && rv.u()) {
               ll.S(var1, this.B, this.L, this.v, this.O, var36, 5.0F, oh.i(var35, 0.18 + 0.14 * var4));
            }

            if (!this.d && rv.u()) {
               if (var4 > 0.001) {
                  ll.N(var1, this.B, this.L, this.v, this.O, var36, oh.i(rv.P, var4 * 0.045));
               }

               ow.y(var1, this.B, this.L, this.v, this.O, var36, oh.S(rv.h, 1.3 + var4 * 0.7), oh.S(rv.C, 1.6 + var4 * 0.9));
            } else {
               ll.N(var1, this.B, this.L, this.v, this.O, var36, oh.j(var35, rv.Z, var4 * 0.15));
            }

            int var42 = !this.x ? rv.r : (this.d ? jd.controlInk(var35) : (this.z ? -24915 : oh.j(rv.G0, rv.P, var4)));
            if (!this.e.isEmpty()) {
               ou.u(var1, ou.e(this.e), this.B + (this.v - 10) / 2, this.L + (this.O - 10) / 2, 10, var42);
            } else {
               String var44 = j_.d(this.J, this.v - 8, j_.m.U);
               int var46 = rv.u() ? this.B + (this.v - j_.o(var44, j_.m.U)) / 2 : this.B + 4;
               j_.L(var1, var44, var46, this.L + (this.O - j_.m.U.C()) / 2, var42, j_.m.U);
            }

            if (this.A && this.h) {
               ll.w(var1, this.B, this.L, this.v, this.O, var36);
            }
         } else if (this.P) {
            int var34 = rv.u() ? rv.S : 6;
            if (rv.u()) {
               ll.w(var1, this.B, this.L, this.v, this.O, var34, oh.j(rv.H, rv.Z, var4), 0.4 + 0.6 * var4);
            } else {
               ll.N(var1, this.B, this.L, this.v, this.O, var34, oh.j(rv.w, rv.Z, var4 * 0.35));
            }

            int var11 = N(this.v);
            int var41 = var11 - 14;
            ll.N(var1, this.B + 7, this.L + 7, var41, var41, 6, rv.Y);
            var1.blit(
               RenderPipelines.GUI_TEXTURED,
               Identifier.fromNamespaceAndPath("minecraft", "textures/misc/unknown_server.png"),
               this.B + 7,
               this.L + 7,
               0.0F,
               0.0F,
               var41,
               var41,
               64,
               64,
               64,
               64,
               com.larpclient.r.D(-1)
            );
            j_.L(var1, j_.d(this.J, this.v - var11 - 7, j_.m.d), this.B + var11, this.L + 6, rv.P, j_.m.d);
            j_.L(var1, j_.d(this.y, this.v - var11 - 7, j_.m.U), this.B + var11, this.L + 19, rv.G0, j_.m.U);
            if (this.A && this.h) {
               ll.w(var1, this.B, this.L, this.v, this.O, var34);
            }
         } else if (this.w) {
            this.W(var1, var4, var8);
         } else if (this.W) {
            this.r(var1, var4, var6, var8);
         } else {
            if (!this.g && this.I != null) {
               this.f.c(this.I ? 1.0 : 0.0);
               this.g = (boolean)1;
            }

            double var10 = this.f.y(Boolean.TRUE.equals(this.I) ? 1.0 : 0.0, 180);
            var1.pose().pushMatrix();

            try {
               if (this.n && rv.u()) {
                  this.E = jd.at(var10, var4, rv.m(this.B));
                  rv.t var39 = rv.D();
                  int var43 = this.E.accent();
                  boolean var45 = var39.enabled().equals("Glow");
                  int var47 = !var45 && !var39.enabled().equals("Tint") ? 0 : 1;
                  int var16 = rv.S;
                  this.j = var39.hoverLift() ? (float)var4 : 0.0F;
                  if (this.j > 0.01F && !var39.card().equals("Flat")) {
                     ow.A(var1, this.B, this.L + 1.5F, this.v, this.O, var16, 8.0F, oh.S(rv.p, 0.4 * this.j), oh.S(rv.p, 0.6 * this.j));
                  }

                  var1.pose().translate(0.0F, -this.j);
                  if (var45 && var10 > 0.01) {
                     ll.S(var1, this.B, this.L, this.v, this.O, var16, 9.0F, oh.i(var43, 0.14 * var10));
                  }

                  switch (var39.card()) {
                     case "Outline":
                        if (var4 > 0.001) {
                           ll.N(var1, this.B, this.L, this.v, this.O, var16, oh.i(rv.P, 0.035 * var4));
                        }
                        break;
                     case "Flat":
                        ll.N(var1, this.B, this.L, this.v, this.O, var16, oh.S(this.E.background(), 0.75));
                        break;
                     default:
                        ll.N(var1, this.B, this.L, this.v, this.O, var16, this.E.background());
                  }

                  if (var47 != 0 && var10 > 0.01) {
                     ow.Z(var1, this.B, this.L, this.v * 0.7F, this.O, var16, oh.i(var43, (var45 ? 0.15 : 0.22) * var10), oh.i(var43, 0.0));
                  }

                  if (!var39.card().equals("Flat")) {
                     double var48 = (var39.card().equals("Outline") ? 0.85 : 0.55) + 0.25 * var4;
                     int var19 = oh.j(oh.i(rv.f, var48), oh.i(var43, 0.55), var47 != 0 ? 0.8 * var10 : 0.0);
                     ow.y(var1, this.B, this.L, this.v, this.O, var16, var19, var19);
                  }

                  double var49 = (System.nanoTime() - this.K) / 6.5E8;
                  if (var39.ripple() && var49 < 1.0) {
                     float var50 = 10.0F + (float)u7.p.y.m(var49) * this.v * 0.9F;
                     ow.j(var1, this.B, this.L, this.v, this.O, var16, this.V, this.r, var50, oh.i(var43, 0.22 * (1.0 - var49)));
                  }

                  if (var6 > 0.001) {
                     ll.N(var1, this.B, this.L, this.v, this.O, rv.S, oh.i(rv.P, 0.045 * var6));
                     ow.y(var1, this.B, this.L, this.v, this.O, rv.S, oh.i(this.E.accent(), 0.28 * var6), oh.i(this.E.accent(), 0.18 * var6));
                     ll.f(var1, this.B, this.L, this.v, this.O, rv.S, oh.i(this.E.accent(), var6));
                  }

                  int var51 = oh.j(oh.j(this.E.subtitle(), this.E.title(), 0.75 + 0.25 * var4), this.E.title(), var10);
                  if (var39.enabled().equals("Text")) {
                     var51 = oh.j(var51, var43, var10);
                  }

                  j_.L(var1, j_.d(this.J, this.h(), j_.m.d), this.B + 12, this.L + this.O / 2 - 12, var51, j_.m.d);
                  j_.L(var1, j_.d(this.y, this.h(), j_.m.r), this.B + 12, this.L + this.O / 2 + 2, this.E.subtitle(), j_.m.r);
                  ll.T(var1, this.B + this.v - 32, this.d(), var10, this.E.accent(), this.E.light());
               } else if (this.n) {
                  this.E = jd.at(var10, var4, rv.m(this.B));
                  ll.Y(var1, this.B, this.L, this.v, this.O, rv.S, oh.j(this.E.background(), rv.K, var6), 0.9 + var4 * 0.6);
                  if (var6 > 0.001) {
                     ow.y(var1, this.B, this.L, this.v, this.O, rv.S, oh.i(rv.P, 0.34 * var6), oh.i(rv.P, 0.26 * var6));
                  }

                  j_.L(var1, j_.d(this.J, this.h(), j_.m.d), this.B + 12, this.L + this.O / 2 - 12, this.E.title(), j_.m.d);
                  j_.L(var1, j_.d(this.y, this.h(), j_.m.r), this.B + 12, this.L + this.O / 2 + 2, this.E.subtitle(), j_.m.r);
                  ll.T(var1, this.B + this.v - 32, this.d(), var10, this.E.accent(), this.E.light());
               } else if (this.Q) {
                  if (var4 > 0.001) {
                     ll.N(var1, this.B, this.L, this.v, this.O, rv.g, oh.i(rv.P, var4 * 0.045));
                  }

                  ow.y(var1, this.B, this.L, this.v, this.O, rv.g, oh.S(rv.h, 1.3 + var4 * 0.7), oh.S(rv.C, 1.6 + var4 * 0.9));
                  String var38 = j_.S(this.J, Math.max(1, this.v - 12));
                  j_.E(var1, var38, this.B + (this.v - j_.a(var38)) / 2, this.L + (this.O - 9) / 2, this.x ? oh.j(rv.G0, rv.P, var4) : rv.r);
               } else {
                  if (this.c) {
                     double var12 = Math.max(var6, var4 * 0.6);
                     if (var12 > 0.001) {
                        ll.N(var1, this.B, this.L, this.v, this.O, rv.g, oh.i(rv.P, 0.065 * var12));
                     }
                  } else {
                     ll.Y(var1, this.B, this.L, this.v, this.O, rv.N, oh.j(rv.H, rv.Z, Math.max(var6 * 0.65, var4)), 0.9 + var4 * 0.6);
                  }

                  int var37 = this.e.isEmpty() ? 8 : 27;
                  int var13 = Math.min(13, Math.max(1, this.O - 2));
                  if (!this.e.isEmpty()) {
                     ou.u(
                        var1,
                        ou.e(this.e),
                        this.B + (this.J.isEmpty() ? (this.v - var13) / 2 : 8),
                        this.L + (this.O - var13) / 2,
                        var13,
                        this.R ? rv.P : oh.j(rv.G0, rv.P, var4)
                     );
                  }

                  int var14 = this.v - var37 - (this.I == null ? 8 : 34);
                  if (var14 > 4 && this.t > 0.001) {
                     if (this.p) {
                        var1.enableScissor(this.B + var37, this.L, this.B + this.v - 8, this.L + this.O);
                     }

                     try (r var15 = com.larpclient.r.p(this.t)) {
                        j_.E(
                           var1,
                           this.p ? this.J : j_.S(this.J, var14),
                           this.B + var37,
                           this.L + (this.y.isEmpty() ? (this.O - 9) / 2 : 4),
                           this.x ? (this.z ? -24915 : (this.d ? rv.m(this.B) : (this.U && !this.R ? oh.j(rv.G0, rv.P, var4) : rv.P))) : rv.G0
                        );
                     } finally {
                        if (this.p) {
                           var1.disableScissor();
                        }
                     }
                  }

                  if (!this.y.isEmpty()) {
                     j_.E(var1, j_.S(this.y, this.v - 42), this.B + 8, this.L + 16, rv.G0);
                  }

                  if (this.I != null) {
                     ll.e(var1, this.B + this.v - 32, this.L + (this.O - 12) / 2, var10);
                  }
               }

               int var40 = this.n ? rv.S : rv.g;
               if (var8 > 0.001) {
                  ll.N(var1, this.B, this.L, this.v, this.O, var40, oh.i(rv.P, var8 * 0.07));
               }

               if (this.A && this.h) {
                  ll.w(var1, this.B, this.L, this.v, this.O, var40);
               }
            } finally {
               var1.pose().popMatrix();
            }
         }
      }
   }

   private void r(GuiGraphicsExtractor var1, double var2, double var4, double var6) {
      double var8 = Math.max(var2, var6) * (1.0 - var4);
      if (var8 > 0.001) {
         fr.S(var1, this.B, this.L, this.v, this.O, rv.Gj, oh.i(rv.P, 0.05 * var8));
      }

      int var10 = oh.j(rv.G0, rv.P, 0.45);
      int var11 = oh.j(oh.j(var10, rv.P, var2 * 0.85), rv.P, var4);
      int var12 = Math.min(12, this.O - 2);
      int var13 = rv.a(this.B, rv.w);
      if (jd.controlInk(rv.w) == -1) {
         var13 = oh.j(var13, -1, 0.12);
      }

      if (!this.e.isEmpty()) {
         ou.u(var1, ou.e(this.e), this.B + 8, this.L + (this.O - var12) / 2, var12, oh.j(oh.j(rv.G0, rv.P, var2 * 0.7), var13, var4));
      }

      if (this.t > 0.001) {
         byte var14 = 27;
         var1.enableScissor(this.B + var14, this.L, this.B + this.v - 4, this.L + this.O);

         try (r var15 = com.larpclient.r.p(this.t)) {
            j_.m var16 = j_.m.Q;
            j_.L(var1, j_.d(this.J, Math.max(1, this.v - var14 - 4), var16), this.B + var14, this.L + (this.O - var16.C()) / 2, var11, var16);
         } finally {
            var1.disableScissor();
         }
      }

      if (this.A && this.h) {
         ll.w(var1, this.B, this.L, this.v, this.O, rv.Gj);
      }
   }

   private void W(GuiGraphicsExtractor var1, double var2, double var4) {
      var1.pose().pushMatrix();

      try {
         var1.pose().translate(this.B + this.v / 2.0F, this.L + this.O / 2.0F);
         float var6 = (float)(1.0 + 0.018 * var2 - 0.025 * var4);
         var1.pose().scale(var6, var6);
         var1.pose().translate(-this.B - this.v / 2.0F, -this.L - this.O / 2.0F);
         ll.N(var1, this.B, this.L, this.v, this.O, 2, oh.S(oh.j(-2144326101, -1337177788, var2), this.Z));
         int var7 = oh.S(this.x ? -1711656 : -6711922, this.Z);
         int var8 = this.e.isEmpty() ? 7 : 22;
         if (!this.e.isEmpty()) {
            ou.u(var1, ou.e(this.e), this.B + 6, this.L + (this.O - 10) / 2, 10, var7);
         }

         j_.E(var1, j_.S(this.J, this.v - var8 - 6), this.B + var8 + (int)Math.round(var2 * 2.0), this.L + (this.O - 9) / 2, var7);
         if (this.A && this.h) {
            ll.G(var1, this.B, this.L, this.v, this.O, oh.S(-1711656, this.Z));
         }
      } finally {
         var1.pose().popMatrix();
      }
   }

   @Override
   public boolean click(double var1, double var3, int var5) {
      if (this.x && var5 == 0 && this.contains(var1, var3)) {
         this.i = System.nanoTime() + 110000000L;
         if (this.n) {
            this.K = System.nanoTime();
            this.V = (float)var1;
            this.r = (float)var3;
         }

         hx.w(this.s);
         if (this.H != null && var1 >= this.B + this.v - 38) {
            this.H.run();
         } else {
            this.a.run();
         }

         return true;
      } else {
         return false;
      }
   }

   @Override
   public boolean key(int var1) {
      if (this.F && this.A && this.x && (var1 == 257 || var1 == 32)) {
         this.i = System.nanoTime() + 110000000L;
         hx.w(this.s);
         if (this.n && var1 == 32 && this.H != null) {
            this.H.run();
         } else {
            this.a.run();
         }

         return true;
      } else {
         return false;
      }
   }
}
