package com.larpclient;

import net.minecraft.class_10799;
import net.minecraft.class_2960;
import net.minecraft.class_332;

public final class ls extends lc {
   public String O;
   public boolean o;
   public boolean p;
   public boolean S;
   public boolean v;
   public boolean L;
   public boolean V;
   public Boolean W;
   public Runnable Y;
   public c_.d X = c_.d.q;
   public String T = "";
   public String n = "";
   public String z = "";
   public boolean j;
   public boolean i;
   public boolean A;
   public boolean t;
   public boolean E;
   public boolean x;
   public boolean r;
   public Object U;
   public double Q = 1.0;
   public double B = 1.0;
   public boolean h;
   private final Runnable f;
   private final qb P = new qb(0.0);
   private final qb u = new qb(0.0);
   private final qb c = new qb(0.0);
   private final qb k = new qb(0.0);
   private boolean F;
   private jx e = jx.at(0.0, 0.0, ez.P(this.D));
   private long K;
   private long G;
   private float H;
   private float C;
   private float s;

   public jx C() {
      return this.e;
   }

   public float P() {
      return this.s;
   }

   public double Q(int var1, int var2) {
      return this.P.a(!this.contains(var1, var2) && (!this.M || !this.q) ? 0.0 : 1.0, 160);
   }

   public ls(String var1, Runnable var2) {
      this.O = var1;
      this.f = var2;
   }

   public static int p(int var0) {
      return (var0 < 144 ? 18 : 34) + 14;
   }

   public int I() {
      return Math.max(1, this.d - (this.i ? 82 : 54));
   }

   public int b() {
      return this.g + (this.w - 13) / 2;
   }

   @Override
   public void render(class_332 var1, int var2, int var3) {
      if (this.I) {
         double var4 = this.Q(var2, var3);
         double var6 = this.u.a(this.o && !this.V ? 1.0 : 0.0, 180);
         double var8 = this.k.a(System.nanoTime() < this.K ? 1.0 : 0.0, 100);
         if (this.x) {
            int var35 = this.v ? (this.L ? -24915 : jx.readableAccent(ez.P(this.D), ez.d)) : ez.B;
            if (!this.N) {
               var4 = 0.0;
            }

            int var36 = ez.m;
            if (this.v && ez.d()) {
               com.larpclient.p.a(var1, this.D, this.g, this.d, this.w, var36, 5.0F, ei.D(var35, 0.18 + 0.14 * var4));
            }

            if (!this.v && ez.d()) {
               if (var4 > 0.001) {
                  com.larpclient.p.B(var1, this.D, this.g, this.d, this.w, var36, ei.D(ez.H, var4 * 0.045));
               }

               ws.A(var1, this.D, this.g, this.d, this.w, var36, ei.R(ez.o, 1.3 + var4 * 0.7), ei.R(ez.J, 1.6 + var4 * 0.9));
            } else {
               com.larpclient.p.B(var1, this.D, this.g, this.d, this.w, var36, ei.W(var35, ez.K, var4 * 0.15));
            }

            int var42 = !this.N ? ez.t : (this.v ? jx.controlInk(var35) : (this.L ? -24915 : ei.W(ez.PH, ez.H, this.U != null ? 0.7 + 0.3 * var4 : var4)));
            if (!this.n.isEmpty()) {
               lx.N(var1, lx.n(this.n), this.D + (this.d - 10) / 2, this.g + (this.w - 10) / 2, 10, var42);
            } else {
               uw.c var44 = this.U != null ? uw.c.o : uw.c.M;
               String var46 = uw.X(this.O, this.d - 8, var44);
               int var48 = ez.d() ? this.D + (this.d - uw.C(var46, var44)) / 2 : this.D + 4;
               uw.X(var1, var46, var48, this.g + (this.w - var44.b()) / 2, var42, var44);
            }

            if (this.M && this.q) {
               com.larpclient.p.C(var1, this.D, this.g, this.d, this.w, var36);
            }
         } else if (this.E) {
            int var34 = ez.d() ? ez.U : 6;
            if (ez.d()) {
               com.larpclient.p.N(var1, this.D, this.g, this.d, this.w, var34, ei.W(ez.M, ez.K, var4), 0.4 + 0.6 * var4);
            } else {
               com.larpclient.p.B(var1, this.D, this.g, this.d, this.w, var34, ei.W(ez.d, ez.K, var4 * 0.35));
            }

            int var11 = p(this.d);
            int var41 = var11 - 14;
            com.larpclient.p.B(var1, this.D + 7, this.g + 7, var41, var41, 6, ez.p);
            var1.method_25293(
               class_10799.field_56883,
               class_2960.method_60655("minecraft", "textures/misc/unknown_server.png"),
               this.D + 7,
               this.g + 7,
               0.0F,
               0.0F,
               var41,
               var41,
               64,
               64,
               64,
               64,
               fp.R(-1)
            );
            uw.X(var1, uw.X(this.O, this.d - var11 - 7, uw.c.e), this.D + var11, this.g + 6, ez.H, uw.c.e);
            uw.X(var1, uw.X(this.T, this.d - var11 - 7, uw.c.M), this.D + var11, this.g + 19, ez.PH, uw.c.M);
            if (this.M && this.q) {
               com.larpclient.p.C(var1, this.D, this.g, this.d, this.w, var34);
            }
         } else if (this.A) {
            this.v(var1, var4, var8);
         } else if (this.t) {
            this.J(var1, var4, var6, var8);
         } else {
            if (!this.F && this.W != null) {
               this.c.w(this.W ? 1.0 : 0.0);
               this.F = (boolean)1;
            }

            double var10 = this.c.a(Boolean.TRUE.equals(this.W) ? 1.0 : 0.0, 180);
            var1.method_51448().pushMatrix();

            try {
               if (this.j && ez.d()) {
                  this.e = jx.at(var10, var4, ez.P(this.D));
                  ez.j var39 = ez.x();
                  int var43 = this.e.accent();
                  boolean var45 = var39.enabled().equals("Glow");
                  int var47 = !var45 && !var39.enabled().equals("Tint") ? 0 : 1;
                  int var16 = ez.U;
                  this.s = var39.hoverLift() ? (float)var4 : 0.0F;
                  var1.method_51448().translate(0.0F, -this.s);
                  if (this.s > 0.01F && !var39.card().equals("Flat")) {
                     ws.K(var1, this.D, this.g, this.d, this.w, var16, 5.0F, ei.R(ez.PX, 0.25 * this.s), ei.R(ez.PX, 0.6 * this.s));
                  }

                  if (var45 && var10 > 0.01) {
                     com.larpclient.p.a(var1, this.D, this.g, this.d, this.w, var16, 9.0F, ei.D(var43, 0.14 * var10));
                  }

                  switch (var39.card()) {
                     case "Outline":
                        if (var4 > 0.001) {
                           com.larpclient.p.B(var1, this.D, this.g, this.d, this.w, var16, ei.D(ez.H, 0.035 * var4));
                        }
                        break;
                     case "Flat":
                        com.larpclient.p.B(var1, this.D, this.g, this.d, this.w, var16, ei.R(this.e.background(), 0.75));
                        break;
                     default:
                        com.larpclient.p.B(var1, this.D, this.g, this.d, this.w, var16, this.e.background());
                  }

                  if (var47 != 0 && var10 > 0.01) {
                     ws.u(var1, this.D, this.g, this.d * 0.7F, this.w, var16, ei.D(var43, (var45 ? 0.15 : 0.22) * var10), ei.D(var43, 0.0));
                  }

                  if (!var39.card().equals("Flat")) {
                     double var49 = (var39.card().equals("Outline") ? 0.85 : 0.55) + 0.25 * var4;
                     int var19 = ei.W(ei.D(ez.D, var49), ei.D(var43, 0.55), var47 != 0 ? 0.8 * var10 : 0.0);
                     ws.A(var1, this.D, this.g, this.d, this.w, var16, var19, var19);
                  }

                  double var50 = (System.nanoTime() - this.G) / 6.5E8;
                  if (var39.ripple() && var50 < 1.0) {
                     float var51 = 10.0F + (float)qb.y.E.f(var50) * this.d * 0.9F;
                     ws.u(var1, this.D, this.g, this.d, this.w, var16, this.H, this.C, var51, ei.D(var43, 0.22 * (1.0 - var50)));
                  }

                  if (var6 > 0.001) {
                     com.larpclient.p.B(var1, this.D, this.g, this.d, this.w, ez.U, ei.D(ez.H, 0.045 * var6));
                     ws.A(var1, this.D, this.g, this.d, this.w, ez.U, ei.D(this.e.accent(), 0.28 * var6), ei.D(this.e.accent(), 0.18 * var6));
                     com.larpclient.p.T(var1, this.D, this.g, this.d, this.w, ez.U, ei.D(this.e.accent(), var6));
                  }

                  int var52 = ei.W(ei.W(this.e.subtitle(), this.e.title(), 0.75 + 0.25 * var4), this.e.title(), var10);
                  if (var39.enabled().equals("Text")) {
                     var52 = ei.W(var52, var43, var10);
                  }

                  uw.X(var1, uw.X(this.O, this.I(), uw.c.e), this.D + 12, this.g + this.w / 2 - 12, var52, uw.c.e);
                  uw.X(var1, uw.X(this.T, this.I(), uw.c.o), this.D + 12, this.g + this.w / 2 + 2, this.e.subtitle(), uw.c.o);
                  com.larpclient.p.c(var1, this.D + this.d - 32, this.b(), var10, this.e.accent(), this.e.light());
               } else if (this.j) {
                  this.e = jx.at(var10, var4, ez.P(this.D));
                  com.larpclient.p.W(var1, this.D, this.g, this.d, this.w, ez.U, ei.W(this.e.background(), ez.PC, var6), 0.9 + var4 * 0.6);
                  if (var6 > 0.001) {
                     ws.A(var1, this.D, this.g, this.d, this.w, ez.U, ei.D(ez.H, 0.34 * var6), ei.D(ez.H, 0.26 * var6));
                  }

                  uw.X(var1, uw.X(this.O, this.I(), uw.c.e), this.D + 12, this.g + this.w / 2 - 12, this.e.title(), uw.c.e);
                  uw.X(var1, uw.X(this.T, this.I(), uw.c.o), this.D + 12, this.g + this.w / 2 + 2, this.e.subtitle(), uw.c.o);
                  if (this.p) {
                     lx.N(var1, lx.n("star"), this.D + this.d - 27, this.g + (this.w - 11) / 2, 11, this.e.accent());
                  } else {
                     com.larpclient.p.c(var1, this.D + this.d - 32, this.b(), var10, this.e.accent(), this.e.light());
                  }
               } else if (this.r) {
                  if (var4 > 0.001) {
                     com.larpclient.p.B(var1, this.D, this.g, this.d, this.w, ez.m, ei.D(ez.H, var4 * 0.045));
                  }

                  ws.A(var1, this.D, this.g, this.d, this.w, ez.m, ei.R(ez.o, 1.3 + var4 * 0.7), ei.R(ez.J, 1.6 + var4 * 0.9));
                  String var38 = uw.J(this.O, Math.max(1, this.d - 12));
                  uw.G(var1, var38, this.D + (this.d - uw.r(var38)) / 2, this.g + (this.w - 9) / 2, this.N ? ei.W(ez.PH, ez.H, var4) : ez.t);
               } else {
                  if (this.S) {
                     double var12 = Math.max(var6, var4 * 0.6);
                     if (var12 > 0.001) {
                        com.larpclient.p.B(var1, this.D, this.g, this.d, this.w, ez.m, ei.D(ez.H, 0.065 * var12));
                     }
                  } else {
                     com.larpclient.p.W(var1, this.D, this.g, this.d, this.w, ez.X, ei.W(ez.M, ez.K, Math.max(var6 * 0.65, var4)), 0.9 + var4 * 0.6);
                  }

                  int var37 = this.n.isEmpty() ? 8 : 27;
                  int var13 = Math.min(13, Math.max(1, this.w - 2));
                  if (!this.n.isEmpty()) {
                     lx.N(
                        var1,
                        lx.n(this.n),
                        this.D + (this.O.isEmpty() ? (this.d - var13) / 2 : 8),
                        this.g + (this.w - var13) / 2,
                        var13,
                        this.o ? ez.H : ei.W(ez.PH, ez.H, var4)
                     );
                  }

                  int var14 = this.d - var37 - (this.W == null ? 8 : 34);
                  if (var14 > 4 && this.B > 0.001) {
                     if (this.h) {
                        var1.method_44379(this.D + var37, this.g, this.D + this.d - 8, this.g + this.w);
                     }

                     try (fp var15 = fp.O(this.B)) {
                        uw.G(
                           var1,
                           this.h ? this.O : uw.J(this.O, var14),
                           this.D + var37,
                           this.g + (this.T.isEmpty() ? (this.w - 9) / 2 : 4),
                           this.N ? (this.L ? -24915 : (this.v ? ez.P(this.D) : (this.V && !this.o ? ei.W(ez.PH, ez.H, var4) : ez.H))) : ez.PH
                        );
                     } finally {
                        if (this.h) {
                           var1.method_44380();
                        }
                     }
                  }

                  if (!this.T.isEmpty()) {
                     uw.G(var1, uw.J(this.T, this.d - 42), this.D + 8, this.g + 16, ez.PH);
                  }

                  if (this.W != null) {
                     com.larpclient.p.s(var1, this.D + this.d - 32, this.g + (this.w - 12) / 2, var10);
                  }
               }

               int var40 = this.j ? ez.U : ez.m;
               if (var8 > 0.001) {
                  com.larpclient.p.B(var1, this.D, this.g, this.d, this.w, var40, ei.D(ez.H, var8 * 0.07));
               }

               if (this.M && this.q) {
                  com.larpclient.p.C(var1, this.D, this.g, this.d, this.w, var40);
               }
            } finally {
               var1.method_51448().popMatrix();
            }
         }
      }
   }

   private void J(class_332 var1, double var2, double var4, double var6) {
      double var8 = Math.max(var2, var6) * (1.0 - var4);
      if (var8 > 0.001) {
         gu.N(var1, this.D, this.g, this.d, this.w, ez.V, ei.D(ez.H, 0.05 * var8));
      }

      int var10 = ei.W(ez.PH, ez.H, 0.45);
      int var11 = ei.W(ei.W(var10, ez.H, var2 * 0.85), ez.H, var4);
      int var12 = Math.min(12, this.w - 2);
      int var13 = ez.X(this.D, ez.d);
      if (jx.controlInk(ez.d) == -1) {
         var13 = ei.W(var13, -1, 0.12);
      }

      if (!this.n.isEmpty()) {
         lx.N(var1, lx.n(this.n), this.D + 8, this.g + (this.w - var12) / 2, var12, ei.W(ei.W(ez.PH, ez.H, var2 * 0.7), var13, var4));
      }

      if (this.B > 0.001) {
         byte var14 = 27;
         var1.method_44379(this.D + var14, this.g, this.D + this.d - 4, this.g + this.w);

         try (fp var15 = fp.O(this.B)) {
            uw.c var16 = uw.c.t;
            uw.X(var1, uw.X(this.O, Math.max(1, this.d - var14 - 4), var16), this.D + var14, this.g + (this.w - var16.b()) / 2, var11, var16);
         } finally {
            var1.method_44380();
         }
      }

      if (this.M && this.q) {
         com.larpclient.p.C(var1, this.D, this.g, this.d, this.w, ez.V);
      }
   }

   private void v(class_332 var1, double var2, double var4) {
      var1.method_51448().pushMatrix();

      try {
         var1.method_51448().translate(this.D + this.d / 2.0F, this.g + this.w / 2.0F);
         float var6 = (float)(1.0 + 0.018 * var2 - 0.025 * var4);
         var1.method_51448().scale(var6, var6);
         var1.method_51448().translate(-this.D - this.d / 2.0F, -this.g - this.w / 2.0F);
         com.larpclient.p.B(var1, this.D, this.g, this.d, this.w, 2, ei.R(ei.W(-2144326101, -1337177788, var2), this.Q));
         int var7 = ei.R(this.N ? -1711656 : -6711922, this.Q);
         int var8 = this.n.isEmpty() ? 7 : 22;
         if (!this.n.isEmpty()) {
            lx.N(var1, lx.n(this.n), this.D + 6, this.g + (this.w - 10) / 2, 10, var7);
         }

         uw.G(var1, uw.J(this.O, this.d - var8 - 6), this.D + var8 + (int)Math.round(var2 * 2.0), this.g + (this.w - 9) / 2, var7);
         if (this.M && this.q) {
            com.larpclient.p.l(var1, this.D, this.g, this.d, this.w, ei.R(-1711656, this.Q));
         }
      } finally {
         var1.method_51448().popMatrix();
      }
   }

   @Override
   public boolean click(double var1, double var3, int var5) {
      if (this.N && var5 == 0 && this.contains(var1, var3)) {
         this.K = System.nanoTime() + 110000000L;
         if (this.j) {
            this.G = System.nanoTime();
            this.H = (float)var1;
            this.C = (float)var3;
         }

         c_.b(this.X);
         if (this.Y != null && var1 >= this.D + this.d - 38) {
            this.Y.run();
         } else {
            this.f.run();
         }

         return true;
      } else {
         return false;
      }
   }

   @Override
   public boolean key(int var1) {
      if (this.I && this.M && this.N && (var1 == 257 || var1 == 32)) {
         this.K = System.nanoTime() + 110000000L;
         c_.b(this.X);
         if (this.j && var1 == 32 && this.Y != null) {
            this.Y.run();
         } else {
            this.f.run();
         }

         return true;
      } else {
         return false;
      }
   }
}
