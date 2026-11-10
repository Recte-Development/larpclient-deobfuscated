package com.larpclient;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.gui.GuiGraphicsExtractor;

public final class hs {
   public static final float I = 0.86F;
   private final List<j0> X = new ArrayList<>();
   private final Runnable h;
   private final Runnable L;
   private qy.i G;
   private dk.q V;
   private j0 O;
   private boolean J;
   private double N;
   private double e;
   private int p;
   private final fq C = new fq(0.0);

   public hs(Runnable var1, Runnable var2) {
      this.h = var1;
      this.L = var2;
   }

   public boolean h() {
      return this.G != null;
   }

   public qy.i O() {
      return this.G == null ? null : new qy.i(this.G.x(), this.G.y(), (int)Math.ceil(this.G.width() * 0.86F), (int)Math.ceil(this.G.height() * 0.86F));
   }

   private double j(double var1) {
      return this.G.x() + (var1 - this.G.x()) / 0.86F;
   }

   private double y(double var1) {
      return this.G.y() + (var1 - this.G.y()) / 0.86F;
   }

   public void l() {
      this.X.clear();
      this.G = null;
      this.O = null;
      this.J = (boolean)0;
      this.p = 0;
   }

   public void e() {
      if (this.h()) {
         for (j0 var2 : this.X) {
            var2.blur();
         }

         this.X.clear();
         this.G = null;
         this.O = null;
         this.J = (boolean)0;
         this.p = 0;
         this.L.run();
      }
   }

   public void w(int var1, int var2, int var3, int var4) {
      this.e();
      this.V = null;

      for (dk.q var6 : dk.X()) {
         s8 var7 = new s8(dk.m(var6.d), () -> {
            int var2x = !dk.k(var6) ? 1 : 0;
            dk.I(var6, (boolean)var2x);
            this.h.run();
            this.L.run();
            ft.h(var2x != 0 ? ft.e.y : ft.e.J);
         });
         var7.K = null;
         var7.O = 22;
         this.X.add(var7);
      }

      this.U(var1, var2, 220, var3, var4);
   }

   public void o(dk.q var1, int var2, int var3, int var4, int var5) {
      this.e();
      this.V = var1;
      com.larpclient.module.b var6 = new com.larpclient.module.b("Widget Size", "Scale this HUD widget", var1.Z(), 0.2, 4.0, 0.02);
      var6.w(var2x -> {
         var1.m(var2x);
         this.h.run();
      });
      this.X.add(new su(var6, true));

      for (com.larpclient.module.r var8 : dk.K(var1)) {
         this.X.add(new su(var8, true));
      }

      s8 var9 = new s8("Hide widget", () -> {
         var1.a(true);
         this.h.run();
         this.e();
      });
      s8 var10 = new s8("Reset placement", () -> {
         var1.O();
         this.h.run();
         this.L.run();
      });
      this.X.add(var9);
      this.X.add(var10);
      this.U(var2, var3, 252, var4, var5);
      ft.h(ft.e.Q);
   }

   private void U(int var1, int var2, int var3, int var4, int var5) {
      var3 = Math.max(1, Math.min(var3, (int)((var4 - 10) / 0.86F)));
      int var6 = Math.max(1, Math.min((int)((var5 - 10) / 0.86F), 46 + this.X.stream().mapToInt(var0 -> var0.O + 2).sum()));
      this.G = new qy.i(qy.k(var1, (int)Math.ceil(var3 * 0.86F), var4), qy.k(var2, (int)Math.ceil(var6 * 0.86F), var5), var3, var6);
      this.p = 0;
      this.C.p(0.0);
      this.C.n(1.0, 300);
      this.w();
   }

   private void w() {
      int var1 = this.G.y() + 39 - this.p;

      for (j0 var3 : this.X) {
         var3.bounds(this.G.x() + 10, var1, Math.max(1, this.G.width() - 20));
         if (var3 instanceof s8 && this.V == null) {
            var3.O = 22;
         }

         if (var3 instanceof su var4) {
            var4.B(this.G.y() + 39, this.G.y() + this.G.height() - 7);
         }

         var1 += var3.O + 2;
      }
   }

   private boolean e(double var1, double var3) {
      return this.G.contains(var1, var3, 0) && var3 >= this.G.y() + 39 && var3 < this.G.y() + this.G.height() - 7;
   }

   public boolean R(double var1, double var3, int var5, int var6, int var7) {
      if (!this.h()) {
         return false;
      }

      this.w();
      double var8 = this.j(var1);
      double var10 = this.y(var3);
      if (!this.G.contains(var8, var10, 0)) {
         this.e();
         return false;
      }

      if (var10 < this.G.y() + 34) {
         if (var8 >= this.G.x() + this.G.width() - 24) {
            ft.h(ft.e.X);
            this.e();
         } else if (var5 == 0) {
            this.J = (boolean)1;
            this.N = var1 - this.G.x();
            this.e = var3 - this.G.y();
         }

         return true;
      } else {
         for (j0 var13 : this.X) {
            if (var13 instanceof su var14 && var14.n()) {
               if (var14.W(var8, var10)) {
                  var14.b(var8, var10, var5);
                  this.O = var13;
                  this.h.run();
                  return true;
               }

               var14.O();
            }
         }

         if (!this.e(var8, var10)) {
            return true;
         }

         for (int var16 = 0; var16 < this.X.size(); var16++) {
            j0 var17 = this.X.get(var16);
            if (var17.T >= this.G.y() + 39 && var17.T + var17.O <= this.G.y() + this.G.height() - 7 && var17.contains(var8, var10)) {
               if (this.V == null && var5 == 1) {
                  this.o(dk.X().get(var16), (int)var1, (int)var3, var6, var7);
                  return true;
               }

               for (j0 var15 : this.X) {
                  if (var15 != var17) {
                     var15.blur();
                  }
               }

               if (var17.click(var8, var10, var5)) {
                  this.O = var17;
                  this.h.run();
               }

               return true;
            }
         }

         return true;
      }
   }

   public boolean a(double var1, double var3, int var5, int var6) {
      if (!this.h()) {
         return false;
      } else if (this.J) {
         qy.i var7 = this.O();
         this.G = new qy.i(
            qy.k((int)Math.round(var1 - this.N), var7.width(), var5),
            qy.k((int)Math.round(var3 - this.e), var7.height(), var6),
            this.G.width(),
            this.G.height()
         );
         this.w();
         return true;
      } else if (this.O != null) {
         this.O.drag(this.j(var1), this.y(var3));
         this.h.run();
         return true;
      } else {
         return false;
      }
   }

   public boolean p() {
      int var1 = !this.J && this.O == null ? 0 : 1;
      this.J = (boolean)0;
      if (this.O != null) {
         this.O.release();
         this.O = null;
         this.L.run();
      }

      return (boolean)var1;
   }

   public boolean V(int var1) {
      for (j0 var3 : this.X) {
         if (var3 instanceof su var4 && var4.T()) {
            boolean var5 = var4.key(var1);
            this.h.run();
            return var5;
         }
      }

      return false;
   }

   public boolean m(int var1) {
      for (j0 var3 : this.X) {
         if (var3 instanceof su var4 && var4.T()) {
            var4.character(var1);
            return true;
         }
      }

      return false;
   }

   public boolean c(double var1, double var3, double var5) {
      if (!this.h()) {
         return false;
      }

      double var7 = this.j(var1);
      double var9 = this.y(var3);
      if (!this.G.contains(var7, var9, 0)) {
         return false;
      }

      for (j0 var12 : this.X) {
         if (var12 instanceof su var13 && var13.W(var7, var9)) {
            var13.L(var5);
            return true;
         }
      }

      int var14 = this.X.stream().mapToInt(var0 -> var0.O + 2).sum();
      this.p = Math.clamp(this.p - (int)Math.round(var5 * 22.0), 0, Math.max(0, var14 - this.G.height() + 46));
      this.w();
      return true;
   }

   public void g(GuiGraphicsExtractor var1, int var2, int var3) {
      if (this.h()) {
         this.w();
         int var4 = (int)Math.floor(this.j(var2));
         int var5 = (int)Math.floor(this.y(var3));
         var1.pose().pushMatrix();
         var1.pose().translate(this.G.x(), this.G.y()).scale(0.86F).translate(-this.G.x(), -this.G.y());

         try (oa var6 = oa.j(this.C.K())) {
            h5.q(var1, this.G.x() - 3, this.G.y() - 3, this.G.width() + 6, this.G.height() + 6, 13.0F, 1593835520);
            h5.q(var1, this.G.x(), this.G.y(), this.G.width(), this.G.height(), 10.0F, -183957239);
            i5.i(var1, this.V == null ? "HUD Elements" : dk.m(this.V.d), this.G.x() + 12, n8.b(this.G.y(), 32.0, 13.0), 13.0, -1250068);
            wz.D(var1, wz.M("x"), this.G.x() + this.G.width() - 20, this.G.y() + 12, 12, -5592406);
            h5.N(var1, this.G.x() + 2, this.G.y() + 32, this.G.width() - 4, 1, 675036230);
            var1.enableScissor(this.G.x(), this.G.y() + 39, this.G.x() + this.G.width(), this.G.y() + this.G.height() - 7);

            try {
               for (int var7 = 0; var7 < this.X.size(); var7++) {
                  j0 var8 = this.X.get(var7);
                  if (var8.T + var8.O >= this.G.y() + 39 && var8.T < this.G.y() + this.G.height() - 7) {
                     if (this.V == null && var8 instanceof s8 var9) {
                        var9.N = (boolean)1;
                        var9.a = dk.k(dk.X().get(var7));
                        var9.G = var9.a;
                     }

                     var8.render(var1, this.e(var4, var5) ? var4 : -1, var5);
                  }
               }
            } finally {
               var1.disableScissor();
            }

            for (j0 var25 : this.X) {
               if (var25 instanceof su var26 && var26.n()) {
                  var26.l(var1, var4, var5);
               }
            }
         } finally {
            var1.pose().popMatrix();
         }
      }
   }
}
