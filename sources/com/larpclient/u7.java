package com.larpclient;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.Identifier;

public final class u7 extends b {
   public String S;
   public boolean U;
   public boolean r;
   public boolean w;
   public boolean k;
   public boolean O;
   public Boolean v;
   public Runnable E;
   public kb.n s = kb.n.w;
   public String u = "";
   public String j = "";
   public String i = "";
   public boolean Q;
   public boolean Y;
   public boolean e;
   public boolean z;
   public boolean y;
   public boolean I;
   public boolean g;
   public double a = 1.0;
   public double T = 1.0;
   public boolean m;
   private final Runnable x;
   private final ef p = new ef(0.0);
   private final ef G = new ef(0.0);
   private final ef b = new ef(0.0);
   private final ef B = new ef(0.0);
   private boolean P;
   private n N = n.at(0.0, 0.0, f.z(this.q));
   private long F;
   private long t;
   private float Z;
   private float L;
   private float c;

   public n g() {
      return this.N;
   }

   public float i() {
      return this.c;
   }

   public double k(int var1, int var2) {
      return this.p.m(!this.contains(var1, var2) && (!this.l || !this.R) ? 0.0 : 1.0, 160);
   }

   public u7(String var1, Runnable var2) {
      this.S = var1;
      this.x = var2;
   }

   public static int w(int var0) {
      return (var0 < 144 ? 18 : 34) + 14;
   }

   public int B() {
      return Math.max(1, this.A - (this.Y ? 82 : 54));
   }

   public int K() {
      return this.J + (this.W - 13) / 2;
   }

   @Override
   public void render(GuiGraphicsExtractor var1, int var2, int var3) {
      if (this.D) {
         double var4 = this.k(var2, var3);
         double var6 = this.G.m(this.U && !this.O ? 1.0 : 0.0, 180);
         double var8 = this.B.m(System.nanoTime() < this.F ? 1.0 : 0.0, 100);
         if (this.I) {
            int var35 = this.w ? (this.k ? -24915 : n.readableAccent(f.z(this.q), f.u)) : f.S;
            if (!this.X) {
               var4 = 0.0;
            }

            int var36 = f.aj;
            if (this.w && f.Y()) {
               pn.Z(var1, this.q, this.J, this.A, this.W, var36, 5.0F, _3.A(var35, 0.18 + 0.14 * var4));
            }

            if (!this.w && f.Y()) {
               if (var4 > 0.001) {
                  pn.b(var1, this.q, this.J, this.A, this.W, var36, _3.A(f.J, var4 * 0.045));
               }

               tl.V(var1, this.q, this.J, this.A, this.W, var36, _3.U(f.H, 1.3 + var4 * 0.7), _3.U(f.g, 1.6 + var4 * 0.9));
            } else {
               pn.b(var1, this.q, this.J, this.A, this.W, var36, _3.D(var35, f.x, var4 * 0.15));
            }

            int var42 = !this.X ? f.G : (this.w ? n.controlInk(var35) : (this.k ? -24915 : _3.D(f.s, f.J, var4)));
            if (!this.j.isEmpty()) {
               tp.N(var1, tp.j(this.j), this.q + (this.A - 10) / 2, this.J + (this.W - 10) / 2, 10, var42);
            } else {
               String var44 = k6.o(this.S, this.A - 8, k6.d.K);
               int var46 = f.Y() ? this.q + (this.A - k6.a(var44, k6.d.K)) / 2 : this.q + 4;
               k6.T(var1, var44, var46, this.J + (this.W - k6.d.K.q()) / 2, var42, k6.d.K);
            }

            if (this.l && this.R) {
               pn.y(var1, this.q, this.J, this.A, this.W, var36);
            }
         } else if (this.y) {
            int var34 = f.Y() ? f.D : 6;
            if (f.Y()) {
               pn.d(var1, this.q, this.J, this.A, this.W, var34, _3.D(f.i, f.x, var4), 0.4 + 0.6 * var4);
            } else {
               pn.b(var1, this.q, this.J, this.A, this.W, var34, _3.D(f.u, f.x, var4 * 0.35));
            }

            int var11 = w(this.A);
            int var41 = var11 - 14;
            pn.b(var1, this.q + 7, this.J + 7, var41, var41, 6, f.c);
            var1.blit(
               RenderPipelines.GUI_TEXTURED,
               Identifier.fromNamespaceAndPath("minecraft", "textures/misc/unknown_server.png"),
               this.q + 7,
               this.J + 7,
               0.0F,
               0.0F,
               var41,
               var41,
               64,
               64,
               64,
               64,
               de.h(-1)
            );
            k6.T(var1, k6.o(this.S, this.A - var11 - 7, k6.d.a), this.q + var11, this.J + 6, f.J, k6.d.a);
            k6.T(var1, k6.o(this.u, this.A - var11 - 7, k6.d.K), this.q + var11, this.J + 19, f.s, k6.d.K);
            if (this.l && this.R) {
               pn.y(var1, this.q, this.J, this.A, this.W, var34);
            }
         } else if (this.e) {
            this.J(var1, var4, var8);
         } else if (this.z) {
            this.B(var1, var4, var6, var8);
         } else {
            if (!this.P && this.v != null) {
               this.b.Q(this.v ? 1.0 : 0.0);
               this.P = (boolean)1;
            }

            double var10 = this.b.m(Boolean.TRUE.equals(this.v) ? 1.0 : 0.0, 180);
            var1.pose().pushMatrix();

            try {
               if (this.Q && f.Y()) {
                  this.N = n.at(var10, var4, f.z(this.q));
                  f.a var39 = f.O();
                  int var43 = this.N.accent();
                  boolean var45 = var39.enabled().equals("Glow");
                  int var47 = !var45 && !var39.enabled().equals("Tint") ? 0 : 1;
                  int var16 = f.D;
                  this.c = var39.hoverLift() ? (float)var4 : 0.0F;
                  if (this.c > 0.01F && !var39.card().equals("Flat")) {
                     tl.j(var1, this.q, this.J + 1.5F, this.A, this.W, var16, 8.0F, _3.U(f.b, 0.4 * this.c), _3.U(f.b, 0.6 * this.c));
                  }

                  var1.pose().translate(0.0F, -this.c);
                  if (var45 && var10 > 0.01) {
                     pn.Z(var1, this.q, this.J, this.A, this.W, var16, 9.0F, _3.A(var43, 0.14 * var10));
                  }

                  switch (var39.card()) {
                     case "Outline":
                        if (var4 > 0.001) {
                           pn.b(var1, this.q, this.J, this.A, this.W, var16, _3.A(f.J, 0.035 * var4));
                        }
                        break;
                     case "Flat":
                        pn.b(var1, this.q, this.J, this.A, this.W, var16, _3.U(this.N.background(), 0.75));
                        break;
                     default:
                        pn.b(var1, this.q, this.J, this.A, this.W, var16, this.N.background());
                  }

                  if (var47 != 0 && var10 > 0.01) {
                     tl.A(var1, this.q, this.J, this.A * 0.7F, this.W, var16, _3.A(var43, (var45 ? 0.15 : 0.22) * var10), _3.A(var43, 0.0));
                  }

                  if (!var39.card().equals("Flat")) {
                     double var48 = (var39.card().equals("Outline") ? 0.85 : 0.55) + 0.25 * var4;
                     int var19 = _3.D(_3.A(f.I, var48), _3.A(var43, 0.55), var47 != 0 ? 0.8 * var10 : 0.0);
                     tl.V(var1, this.q, this.J, this.A, this.W, var16, var19, var19);
                  }

                  double var49 = (System.nanoTime() - this.t) / 6.5E8;
                  if (var39.ripple() && var49 < 1.0) {
                     float var50 = 10.0F + (float)ef.q.m.k(var49) * this.A * 0.9F;
                     tl.q(var1, this.q, this.J, this.A, this.W, var16, this.Z, this.L, var50, _3.A(var43, 0.22 * (1.0 - var49)));
                  }

                  if (var6 > 0.001) {
                     pn.b(var1, this.q, this.J, this.A, this.W, f.D, _3.A(f.J, 0.045 * var6));
                     tl.V(var1, this.q, this.J, this.A, this.W, f.D, _3.A(this.N.accent(), 0.28 * var6), _3.A(this.N.accent(), 0.18 * var6));
                     pn.a(var1, this.q, this.J, this.A, this.W, f.D, _3.A(this.N.accent(), var6));
                  }

                  int var51 = _3.D(_3.D(this.N.subtitle(), this.N.title(), 0.75 + 0.25 * var4), this.N.title(), var10);
                  if (var39.enabled().equals("Text")) {
                     var51 = _3.D(var51, var43, var10);
                  }

                  k6.T(var1, k6.o(this.S, this.B(), k6.d.a), this.q + 12, this.J + this.W / 2 - 12, var51, k6.d.a);
                  k6.T(var1, k6.o(this.u, this.B(), k6.d.C), this.q + 12, this.J + this.W / 2 + 2, this.N.subtitle(), k6.d.C);
                  pn.R(var1, this.q + this.A - 32, this.K(), var10, this.N.accent(), this.N.light());
               } else if (this.Q) {
                  this.N = n.at(var10, var4, f.z(this.q));
                  pn.g(var1, this.q, this.J, this.A, this.W, f.D, _3.D(this.N.background(), f.V, var6), 0.9 + var4 * 0.6);
                  if (var6 > 0.001) {
                     tl.V(var1, this.q, this.J, this.A, this.W, f.D, _3.A(f.J, 0.34 * var6), _3.A(f.J, 0.26 * var6));
                  }

                  k6.T(var1, k6.o(this.S, this.B(), k6.d.a), this.q + 12, this.J + this.W / 2 - 12, this.N.title(), k6.d.a);
                  k6.T(var1, k6.o(this.u, this.B(), k6.d.C), this.q + 12, this.J + this.W / 2 + 2, this.N.subtitle(), k6.d.C);
                  pn.R(var1, this.q + this.A - 32, this.K(), var10, this.N.accent(), this.N.light());
               } else if (this.g) {
                  if (var4 > 0.001) {
                     pn.b(var1, this.q, this.J, this.A, this.W, f.aj, _3.A(f.J, var4 * 0.045));
                  }

                  tl.V(var1, this.q, this.J, this.A, this.W, f.aj, _3.U(f.H, 1.3 + var4 * 0.7), _3.U(f.g, 1.6 + var4 * 0.9));
                  String var38 = k6.j(this.S, Math.max(1, this.A - 12));
                  k6.Z(var1, var38, this.q + (this.A - k6.P(var38)) / 2, this.J + (this.W - 9) / 2, this.X ? _3.D(f.s, f.J, var4) : f.G);
               } else {
                  if (this.r) {
                     double var12 = Math.max(var6, var4 * 0.6);
                     if (var12 > 0.001) {
                        pn.b(var1, this.q, this.J, this.A, this.W, f.aj, _3.A(f.J, 0.065 * var12));
                     }
                  } else {
                     pn.g(var1, this.q, this.J, this.A, this.W, f.B, _3.D(f.i, f.x, Math.max(var6 * 0.65, var4)), 0.9 + var4 * 0.6);
                  }

                  int var37 = this.j.isEmpty() ? 8 : 27;
                  int var13 = Math.min(13, Math.max(1, this.W - 2));
                  if (!this.j.isEmpty()) {
                     tp.N(
                        var1,
                        tp.j(this.j),
                        this.q + (this.S.isEmpty() ? (this.A - var13) / 2 : 8),
                        this.J + (this.W - var13) / 2,
                        var13,
                        this.U ? f.J : _3.D(f.s, f.J, var4)
                     );
                  }

                  int var14 = this.A - var37 - (this.v == null ? 8 : 34);
                  if (var14 > 4 && this.T > 0.001) {
                     if (this.m) {
                        var1.enableScissor(this.q + var37, this.J, this.q + this.A - 8, this.J + this.W);
                     }

                     try (de var15 = de.l(this.T)) {
                        k6.Z(
                           var1,
                           this.m ? this.S : k6.j(this.S, var14),
                           this.q + var37,
                           this.J + (this.u.isEmpty() ? (this.W - 9) / 2 : 4),
                           this.X ? (this.k ? -24915 : (this.w ? f.z(this.q) : (this.O && !this.U ? _3.D(f.s, f.J, var4) : f.J))) : f.s
                        );
                     } finally {
                        if (this.m) {
                           var1.disableScissor();
                        }
                     }
                  }

                  if (!this.u.isEmpty()) {
                     k6.Z(var1, k6.j(this.u, this.A - 42), this.q + 8, this.J + 16, f.s);
                  }

                  if (this.v != null) {
                     pn.Y(var1, this.q + this.A - 32, this.J + (this.W - 12) / 2, var10);
                  }
               }

               int var40 = this.Q ? f.D : f.aj;
               if (var8 > 0.001) {
                  pn.b(var1, this.q, this.J, this.A, this.W, var40, _3.A(f.J, var8 * 0.07));
               }

               if (this.l && this.R) {
                  pn.y(var1, this.q, this.J, this.A, this.W, var40);
               }
            } finally {
               var1.pose().popMatrix();
            }
         }
      }
   }

   private void B(GuiGraphicsExtractor var1, double var2, double var4, double var6) {
      double var8 = Math.max(var2, var6) * (1.0 - var4);
      if (var8 > 0.001) {
         c7.n(var1, this.q, this.J, this.A, this.W, f.y, _3.A(f.J, 0.05 * var8));
      }

      int var10 = _3.D(f.s, f.J, 0.45);
      int var11 = _3.D(_3.D(var10, f.J, var2 * 0.85), f.J, var4);
      int var12 = Math.min(12, this.W - 2);
      int var13 = f.Q(this.q, f.u);
      if (n.controlInk(f.u) == -1) {
         var13 = _3.D(var13, -1, 0.12);
      }

      if (!this.j.isEmpty()) {
         tp.N(var1, tp.j(this.j), this.q + 8, this.J + (this.W - var12) / 2, var12, _3.D(_3.D(f.s, f.J, var2 * 0.7), var13, var4));
      }

      if (this.T > 0.001) {
         byte var14 = 27;
         var1.enableScissor(this.q + var14, this.J, this.q + this.A - 4, this.J + this.W);

         try (de var15 = de.l(this.T)) {
            k6.d var16 = k6.d.F;
            k6.T(var1, k6.o(this.S, Math.max(1, this.A - var14 - 4), var16), this.q + var14, this.J + (this.W - var16.q()) / 2, var11, var16);
         } finally {
            var1.disableScissor();
         }
      }

      if (this.l && this.R) {
         pn.y(var1, this.q, this.J, this.A, this.W, f.y);
      }
   }

   private void J(GuiGraphicsExtractor var1, double var2, double var4) {
      var1.pose().pushMatrix();

      try {
         var1.pose().translate(this.q + this.A / 2.0F, this.J + this.W / 2.0F);
         float var6 = (float)(1.0 + 0.018 * var2 - 0.025 * var4);
         var1.pose().scale(var6, var6);
         var1.pose().translate(-this.q - this.A / 2.0F, -this.J - this.W / 2.0F);
         pn.b(var1, this.q, this.J, this.A, this.W, 2, _3.U(_3.D(-2144326101, -1337177788, var2), this.a));
         int var7 = _3.U(this.X ? -1711656 : -6711922, this.a);
         int var8 = this.j.isEmpty() ? 7 : 22;
         if (!this.j.isEmpty()) {
            tp.N(var1, tp.j(this.j), this.q + 6, this.J + (this.W - 10) / 2, 10, var7);
         }

         k6.Z(var1, k6.j(this.S, this.A - var8 - 6), this.q + var8 + (int)Math.round(var2 * 2.0), this.J + (this.W - 9) / 2, var7);
         if (this.l && this.R) {
            pn.Y(var1, this.q, this.J, this.A, this.W, _3.U(-1711656, this.a));
         }
      } finally {
         var1.pose().popMatrix();
      }
   }

   @Override
   public boolean click(double var1, double var3, int var5) {
      if (this.X && var5 == 0 && this.contains(var1, var3)) {
         this.F = System.nanoTime() + 110000000L;
         if (this.Q) {
            this.t = System.nanoTime();
            this.Z = (float)var1;
            this.L = (float)var3;
         }

         kb.d(this.s);
         if (this.E != null && var1 >= this.q + this.A - 38) {
            this.E.run();
         } else {
            this.x.run();
         }

         return true;
      } else {
         return false;
      }
   }

   @Override
   public boolean key(int var1) {
      if (this.D && this.l && this.X && (var1 == 257 || var1 == 32)) {
         this.F = System.nanoTime() + 110000000L;
         kb.d(this.s);
         if (this.Q && var1 == 32 && this.E != null) {
            this.E.run();
         } else {
            this.x.run();
         }

         return true;
      } else {
         return false;
      }
   }
}
