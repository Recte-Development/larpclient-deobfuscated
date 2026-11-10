package com.larpclient;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.Identifier;

public final class s8 extends j0 {
   public String W;
   public boolean a;
   public boolean N;
   public boolean d;
   public boolean f;
   public boolean g;
   public Boolean G;
   public Runnable o;
   public ft.e K;
   public String z;
   public String x;
   public String q;
   public boolean b;
   public boolean H;
   public boolean i;
   public boolean w;
   public boolean C;
   public boolean Z;
   public boolean D;
   public Object l;
   public double e;
   public double p;
   public boolean c;
   private final Runnable E;
   private final fq J;
   private final fq y;
   private final fq t;
   private final fq n;
   private boolean M;
   private w4 S;
   private long A;
   private long Y;
   private float u;
   private float r;
   private float h;

   public w4 k() {
      return this.S;
   }

   public float M() {
      return this.h;
   }

   public double b(int var1, int var2) {
      return this.J.n(!this.contains(var1, var2) && (!this.F || !this.X) ? 0.0 : 1.0, 160);
   }

   public s8(String var1, Runnable var2) {
      this.K = ft.e.K;
      this.z = "";
      this.x = "";
      this.q = "";
      this.e = 1.0;
      this.p = 1.0;
      this.J = new fq(0.0);
      this.y = new fq(0.0);
      this.t = new fq(0.0);
      this.n = new fq(0.0);
      this.S = w4.at(0.0, 0.0, com.larpclient.c.c(this.P));
      this.W = var1;
      this.E = var2;
   }

   public static int s(int var0) {
      return (var0 < 144 ? 18 : 34) + 14;
   }

   public int o() {
      return Math.max(1, this.L - (this.H ? 82 : 54));
   }

   public int A() {
      return this.T + (this.O - 13) / 2;
   }

   @Override
   public void render(GuiGraphicsExtractor var1, int var2, int var3) {
      if (this.R) {
         double var4 = this.b(var2, var3);
         double var6 = this.y.n(this.a && !this.g ? 1.0 : 0.0, 180);
         double var8 = this.n.n(System.nanoTime() < this.A ? 1.0 : 0.0, 100);
         if (this.Z) {
            int var35 = this.d ? (this.f ? -24915 : w4.readableAccent(com.larpclient.c.c(this.P), com.larpclient.c.D)) : com.larpclient.c.R;
            if (!this.V) {
               var4 = 0.0;
            }

            int var36 = com.larpclient.c.G;
            if (this.d && com.larpclient.c.J()) {
               wc.b(var1, this.P, this.T, this.L, this.O, var36, 5.0F, vv.N(var35, 0.18 + 0.14 * var4));
            }

            if (!this.d && com.larpclient.c.J()) {
               if (var4 > 0.001) {
                  wc.b(var1, this.P, this.T, this.L, this.O, var36, vv.N(com.larpclient.c.H, var4 * 0.045));
               }

               w1.T(var1, this.P, this.T, this.L, this.O, var36, vv.O(com.larpclient.c.Xn, 1.3 + var4 * 0.7), vv.O(com.larpclient.c.XZ, 1.6 + var4 * 0.9));
            } else {
               wc.b(var1, this.P, this.T, this.L, this.O, var36, vv.Q(var35, com.larpclient.c.g, var4 * 0.15));
            }

            int var42 = !this.V
               ? com.larpclient.c.f
               : (this.d ? w4.controlInk(var35) : (this.f ? -24915 : vv.Q(com.larpclient.c.W, com.larpclient.c.H, this.l != null ? 0.7 + 0.3 * var4 : var4)));
            if (!this.x.isEmpty()) {
               wz.D(var1, wz.M(this.x), this.P + (this.L - 10) / 2, this.T + (this.O - 10) / 2, 10, var42);
            } else {
               ic.n var44 = this.l != null ? ic.n.C : ic.n.I;
               String var46 = ic.o(this.W, this.L - 8, var44);
               int var48 = com.larpclient.c.J() ? this.P + (this.L - ic.f(var46, var44)) / 2 : this.P + 4;
               ic.N(var1, var46, var48, this.T + (this.O - var44.T()) / 2, var42, var44);
            }

            if (this.F && this.X) {
               wc.U(var1, this.P, this.T, this.L, this.O, var36);
            }
         } else if (this.C) {
            int var34 = com.larpclient.c.J() ? com.larpclient.c.k : 6;
            if (com.larpclient.c.J()) {
               wc.g(var1, this.P, this.T, this.L, this.O, var34, vv.Q(com.larpclient.c.m, com.larpclient.c.g, var4), 0.4 + 0.6 * var4);
            } else {
               wc.b(var1, this.P, this.T, this.L, this.O, var34, vv.Q(com.larpclient.c.D, com.larpclient.c.g, var4 * 0.35));
            }

            int var11 = s(this.L);
            int var41 = var11 - 14;
            wc.b(var1, this.P + 7, this.T + 7, var41, var41, 6, com.larpclient.c.r);
            var1.blit(
               RenderPipelines.GUI_TEXTURED,
               Identifier.fromNamespaceAndPath("minecraft", "textures/misc/unknown_server.png"),
               this.P + 7,
               this.T + 7,
               0.0F,
               0.0F,
               var41,
               var41,
               64,
               64,
               64,
               64,
               oa.e(-1)
            );
            ic.N(var1, ic.o(this.W, this.L - var11 - 7, ic.n.Q), this.P + var11, this.T + 6, com.larpclient.c.H, ic.n.Q);
            ic.N(var1, ic.o(this.z, this.L - var11 - 7, ic.n.I), this.P + var11, this.T + 19, com.larpclient.c.W, ic.n.I);
            if (this.F && this.X) {
               wc.U(var1, this.P, this.T, this.L, this.O, var34);
            }
         } else if (this.i) {
            this.q(var1, var4, var8);
         } else if (this.w) {
            this.H(var1, var4, var6, var8);
         } else {
            if (!this.M && this.G != null) {
               this.t.p(this.G ? 1.0 : 0.0);
               this.M = (boolean)1;
            }

            double var10 = this.t.n(Boolean.TRUE.equals(this.G) ? 1.0 : 0.0, 180);
            var1.pose().pushMatrix();

            try {
               if (this.b && com.larpclient.c.J()) {
                  this.S = w4.at(var10, var4, com.larpclient.c.c(this.P));
                  c.j var39 = com.larpclient.c.t();
                  int var43 = this.S.accent();
                  boolean var45 = var39.enabled().equals("Glow");
                  int var47 = !var45 && !var39.enabled().equals("Tint") ? 0 : 1;
                  int var16 = com.larpclient.c.k;
                  this.h = var39.hoverLift() ? (float)var4 : 0.0F;
                  var1.pose().translate(0.0F, -this.h);
                  if (this.h > 0.01F && !var39.card().equals("Flat")) {
                     w1.h(var1, this.P, this.T, this.L, this.O, var16, 5.0F, vv.O(com.larpclient.c.Z, 0.25 * this.h), vv.O(com.larpclient.c.Z, 0.6 * this.h));
                  }

                  if (var45 && var10 > 0.01) {
                     wc.b(var1, this.P, this.T, this.L, this.O, var16, 9.0F, vv.N(var43, 0.14 * var10));
                  }

                  switch (var39.card()) {
                     case "Outline":
                        if (var4 > 0.001) {
                           wc.b(var1, this.P, this.T, this.L, this.O, var16, vv.N(com.larpclient.c.H, 0.035 * var4));
                        }
                        break;
                     case "Flat":
                        wc.b(var1, this.P, this.T, this.L, this.O, var16, vv.O(this.S.background(), 0.75));
                        break;
                     default:
                        wc.b(var1, this.P, this.T, this.L, this.O, var16, this.S.background());
                  }

                  if (var47 != 0 && var10 > 0.01) {
                     w1.c(var1, this.P, this.T, this.L * 0.7F, this.O, var16, vv.N(var43, (var45 ? 0.15 : 0.22) * var10), vv.N(var43, 0.0));
                  }

                  if (!var39.card().equals("Flat")) {
                     double var49 = (var39.card().equals("Outline") ? 0.85 : 0.55) + 0.25 * var4;
                     int var19 = vv.Q(vv.N(com.larpclient.c.n, var49), vv.N(var43, 0.55), var47 != 0 ? 0.8 * var10 : 0.0);
                     w1.T(var1, this.P, this.T, this.L, this.O, var16, var19, var19);
                  }

                  double var50 = (System.nanoTime() - this.Y) / 6.5E8;
                  if (var39.ripple() && var50 < 1.0) {
                     float var51 = 10.0F + (float)fq.y.u.q(var50) * this.L * 0.9F;
                     w1.A(var1, this.P, this.T, this.L, this.O, var16, this.u, this.r, var51, vv.N(var43, 0.22 * (1.0 - var50)));
                  }

                  if (var6 > 0.001) {
                     wc.b(var1, this.P, this.T, this.L, this.O, com.larpclient.c.k, vv.N(com.larpclient.c.H, 0.045 * var6));
                     w1.T(var1, this.P, this.T, this.L, this.O, com.larpclient.c.k, vv.N(this.S.accent(), 0.28 * var6), vv.N(this.S.accent(), 0.18 * var6));
                     wc.D(var1, this.P, this.T, this.L, this.O, com.larpclient.c.k, vv.N(this.S.accent(), var6));
                  }

                  int var52 = vv.Q(vv.Q(this.S.subtitle(), this.S.title(), 0.75 + 0.25 * var4), this.S.title(), var10);
                  if (var39.enabled().equals("Text")) {
                     var52 = vv.Q(var52, var43, var10);
                  }

                  ic.N(var1, ic.o(this.W, this.o(), ic.n.Q), this.P + 12, this.T + this.O / 2 - 12, var52, ic.n.Q);
                  ic.N(var1, ic.o(this.z, this.o(), ic.n.C), this.P + 12, this.T + this.O / 2 + 2, this.S.subtitle(), ic.n.C);
                  wc.v(var1, this.P + this.L - 32, this.A(), var10, this.S.accent(), this.S.light());
               } else if (this.b) {
                  this.S = w4.at(var10, var4, com.larpclient.c.c(this.P));
                  wc.t(var1, this.P, this.T, this.L, this.O, com.larpclient.c.k, vv.Q(this.S.background(), com.larpclient.c.J, var6), 0.9 + var4 * 0.6);
                  if (var6 > 0.001) {
                     w1.T(
                        var1, this.P, this.T, this.L, this.O, com.larpclient.c.k, vv.N(com.larpclient.c.H, 0.34 * var6), vv.N(com.larpclient.c.H, 0.26 * var6)
                     );
                  }

                  ic.N(var1, ic.o(this.W, this.o(), ic.n.Q), this.P + 12, this.T + this.O / 2 - 12, this.S.title(), ic.n.Q);
                  ic.N(var1, ic.o(this.z, this.o(), ic.n.C), this.P + 12, this.T + this.O / 2 + 2, this.S.subtitle(), ic.n.C);
                  wc.v(var1, this.P + this.L - 32, this.A(), var10, this.S.accent(), this.S.light());
               } else if (this.D) {
                  if (var4 > 0.001) {
                     wc.b(var1, this.P, this.T, this.L, this.O, com.larpclient.c.G, vv.N(com.larpclient.c.H, var4 * 0.045));
                  }

                  w1.T(
                     var1,
                     this.P,
                     this.T,
                     this.L,
                     this.O,
                     com.larpclient.c.G,
                     vv.O(com.larpclient.c.Xn, 1.3 + var4 * 0.7),
                     vv.O(com.larpclient.c.XZ, 1.6 + var4 * 0.9)
                  );
                  String var38 = ic.U(this.W, Math.max(1, this.L - 12));
                  ic.N(
                     var1,
                     var38,
                     this.P + (this.L - ic.H(var38)) / 2,
                     this.T + (this.O - 9) / 2,
                     this.V ? vv.Q(com.larpclient.c.W, com.larpclient.c.H, var4) : com.larpclient.c.f
                  );
               } else {
                  if (this.N) {
                     double var12 = Math.max(var6, var4 * 0.6);
                     if (var12 > 0.001) {
                        wc.b(var1, this.P, this.T, this.L, this.O, com.larpclient.c.G, vv.N(com.larpclient.c.H, 0.065 * var12));
                     }
                  } else {
                     wc.t(
                        var1,
                        this.P,
                        this.T,
                        this.L,
                        this.O,
                        com.larpclient.c.X,
                        vv.Q(com.larpclient.c.m, com.larpclient.c.g, Math.max(var6 * 0.65, var4)),
                        0.9 + var4 * 0.6
                     );
                  }

                  int var37 = this.x.isEmpty() ? 8 : 27;
                  int var13 = Math.min(13, Math.max(1, this.O - 2));
                  if (!this.x.isEmpty()) {
                     wz.D(
                        var1,
                        wz.M(this.x),
                        this.P + (this.W.isEmpty() ? (this.L - var13) / 2 : 8),
                        this.T + (this.O - var13) / 2,
                        var13,
                        this.a ? com.larpclient.c.H : vv.Q(com.larpclient.c.W, com.larpclient.c.H, var4)
                     );
                  }

                  int var14 = this.L - var37 - (this.G == null ? 8 : 34);
                  if (var14 > 4 && this.p > 0.001) {
                     if (this.c) {
                        var1.enableScissor(this.P + var37, this.T, this.P + this.L - 8, this.T + this.O);
                     }

                     try (oa var15 = oa.j(this.p)) {
                        ic.N(
                           var1,
                           this.c ? this.W : ic.U(this.W, var14),
                           this.P + var37,
                           this.T + (this.z.isEmpty() ? (this.O - 9) / 2 : 4),
                           this.V
                              ? (
                                 this.f
                                    ? -24915
                                    : (
                                       this.d
                                          ? com.larpclient.c.c(this.P)
                                          : (this.g && !this.a ? vv.Q(com.larpclient.c.W, com.larpclient.c.H, var4) : com.larpclient.c.H)
                                    )
                              )
                              : com.larpclient.c.W
                        );
                     } finally {
                        if (this.c) {
                           var1.disableScissor();
                        }
                     }
                  }

                  if (!this.z.isEmpty()) {
                     ic.N(var1, ic.U(this.z, this.L - 42), this.P + 8, this.T + 16, com.larpclient.c.W);
                  }

                  if (this.G != null) {
                     wc.q(var1, this.P + this.L - 32, this.T + (this.O - 12) / 2, var10);
                  }
               }

               int var40 = this.b ? com.larpclient.c.k : com.larpclient.c.G;
               if (var8 > 0.001) {
                  wc.b(var1, this.P, this.T, this.L, this.O, var40, vv.N(com.larpclient.c.H, var8 * 0.07));
               }

               if (this.F && this.X) {
                  wc.U(var1, this.P, this.T, this.L, this.O, var40);
               }
            } finally {
               var1.pose().popMatrix();
            }
         }
      }
   }

   private void H(GuiGraphicsExtractor var1, double var2, double var4, double var6) {
      double var8 = Math.max(var2, var6) * (1.0 - var4);
      if (var8 > 0.001) {
         h5.q(var1, this.P, this.T, this.L, this.O, com.larpclient.c.i, vv.N(com.larpclient.c.H, 0.05 * var8));
      }

      int var10 = vv.Q(com.larpclient.c.W, com.larpclient.c.H, 0.45);
      int var11 = vv.Q(vv.Q(var10, com.larpclient.c.H, var2 * 0.85), com.larpclient.c.H, var4);
      int var12 = Math.min(12, this.O - 2);
      int var13 = com.larpclient.c.e(this.P, com.larpclient.c.D);
      if (w4.controlInk(com.larpclient.c.D) == -1) {
         var13 = vv.Q(var13, -1, 0.12);
      }

      if (!this.x.isEmpty()) {
         wz.D(var1, wz.M(this.x), this.P + 8, this.T + (this.O - var12) / 2, var12, vv.Q(vv.Q(com.larpclient.c.W, com.larpclient.c.H, var2 * 0.7), var13, var4));
      }

      if (this.p > 0.001) {
         byte var14 = 27;
         var1.enableScissor(this.P + var14, this.T, this.P + this.L - 4, this.T + this.O);

         try (oa var15 = oa.j(this.p)) {
            ic.n var16 = ic.n.E;
            ic.N(var1, ic.o(this.W, Math.max(1, this.L - var14 - 4), var16), this.P + var14, this.T + (this.O - var16.T()) / 2, var11, var16);
         } finally {
            var1.disableScissor();
         }
      }

      if (this.F && this.X) {
         wc.U(var1, this.P, this.T, this.L, this.O, com.larpclient.c.i);
      }
   }

   private void q(GuiGraphicsExtractor var1, double var2, double var4) {
      var1.pose().pushMatrix();

      try {
         var1.pose().translate(this.P + this.L / 2.0F, this.T + this.O / 2.0F);
         float var6 = (float)(1.0 + 0.018 * var2 - 0.025 * var4);
         var1.pose().scale(var6, var6);
         var1.pose().translate(-this.P - this.L / 2.0F, -this.T - this.O / 2.0F);
         wc.b(var1, this.P, this.T, this.L, this.O, 2, vv.O(vv.Q(-2144326101, -1337177788, var2), this.e));
         int var7 = vv.O(this.V ? -1711656 : -6711922, this.e);
         int var8 = this.x.isEmpty() ? 7 : 22;
         if (!this.x.isEmpty()) {
            wz.D(var1, wz.M(this.x), this.P + 6, this.T + (this.O - 10) / 2, 10, var7);
         }

         ic.N(var1, ic.U(this.W, this.L - var8 - 6), this.P + var8 + (int)Math.round(var2 * 2.0), this.T + (this.O - 9) / 2, var7);
         if (this.F && this.X) {
            wc.j(var1, this.P, this.T, this.L, this.O, vv.O(-1711656, this.e));
         }
      } finally {
         var1.pose().popMatrix();
      }
   }

   @Override
   public boolean click(double var1, double var3, int var5) {
      if (this.V && var5 == 0 && this.contains(var1, var3)) {
         this.A = System.nanoTime() + 110000000L;
         if (this.b) {
            this.Y = System.nanoTime();
            this.u = (float)var1;
            this.r = (float)var3;
         }

         ft.h(this.K);
         if (this.o != null && var1 >= this.P + this.L - 38) {
            this.o.run();
         } else {
            this.E.run();
         }

         return true;
      } else {
         return false;
      }
   }

   @Override
   public boolean key(int var1) {
      if (this.R && this.F && this.V && (var1 == 257 || var1 == 32)) {
         this.A = System.nanoTime() + 110000000L;
         ft.h(this.K);
         if (this.b && var1 == 32 && this.o != null) {
            this.o.run();
         } else {
            this.E.run();
         }

         return true;
      } else {
         return false;
      }
   }
}
