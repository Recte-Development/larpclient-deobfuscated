package com.larpclient;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.gui.GuiGraphicsExtractor;

public final class lq {
   public static final float m = 0.86F;
   private final List<oq> p = new ArrayList<>();
   private final Runnable b;
   private final Runnable d;
   private _h.p F;
   private o8.a i;
   private oq B;
   private boolean z;
   private double K;
   private double Q;
   private int k;
   private final u7 U = new u7(0.0);

   public lq(Runnable var1, Runnable var2) {
      this.b = var1;
      this.d = var2;
   }

   public boolean g() {
      return this.F != null;
   }

   public _h.p Y() {
      return this.F == null ? null : new _h.p(this.F.x(), this.F.y(), (int)Math.ceil(this.F.width() * 0.86F), (int)Math.ceil(this.F.height() * 0.86F));
   }

   private double R(double var1) {
      return this.F.x() + (var1 - this.F.x()) / 0.86F;
   }

   private double w(double var1) {
      return this.F.y() + (var1 - this.F.y()) / 0.86F;
   }

   public void G() {
      this.p.clear();
      this.F = null;
      this.B = null;
      this.z = (boolean)0;
      this.k = 0;
   }

   public void s() {
      if (this.g()) {
         for (oq var2 : this.p) {
            var2.blur();
         }

         this.p.clear();
         this.F = null;
         this.B = null;
         this.z = (boolean)0;
         this.k = 0;
         this.d.run();
      }
   }

   public void p(int var1, int var2, int var3, int var4) {
      this.s();
      this.i = null;

      for (o8.a var6 : o8.d) {
         p7 var7 = new p7(o8.F(var6.M), () -> {
            int var2x = !o8.r(var6) ? 1 : 0;
            o8.a(var6, (boolean)var2x);
            this.b.run();
            this.d.run();
            hx.w(var2x != 0 ? hx.t.N : hx.t.F);
         });
         var7.s = null;
         var7.O = 22;
         this.p.add(var7);
      }

      this.G(var1, var2, 220, var3, var4);
   }

   public void X(o8.a var1, int var2, int var3, int var4, int var5) {
      this.s();
      this.i = var1;
      com.larpclient.module.lc var6 = new com.larpclient.module.lc("Widget Size", "Scale this HUD widget", var1.H(), 0.5, 2.5, 0.02);
      var6.W(var2x -> {
         var1.m(var2x);
         this.b.run();
      });
      this.p.add(new p9(var6, true));

      for (com.larpclient.module.l6 var8 : o8.z(var1)) {
         this.p.add(new p9(var8, true));
      }

      p7 var9 = new p7("Hide widget", () -> {
         var1.W(true);
         this.b.run();
         this.s();
      });
      p7 var10 = new p7("Reset placement", () -> {
         var1.j();
         this.b.run();
         this.d.run();
      });
      this.p.add(var9);
      this.p.add(var10);
      this.G(var2, var3, 252, var4, var5);
      hx.w(hx.t.h);
   }

   private void G(int var1, int var2, int var3, int var4, int var5) {
      var3 = Math.max(1, Math.min(var3, (int)((var4 - 10) / 0.86F)));
      int var6 = Math.max(1, Math.min((int)((var5 - 10) / 0.86F), 46 + this.p.stream().mapToInt(var0 -> var0.O + 2).sum()));
      this.F = new _h.p(_h.n(var1, (int)Math.ceil(var3 * 0.86F), var4), _h.n(var2, (int)Math.ceil(var6 * 0.86F), var5), var3, var6);
      this.k = 0;
      this.U.c(0.0);
      this.U.y(1.0, 300);
      this.B();
   }

   private void B() {
      int var1 = this.F.y() + 39 - this.k;

      for (oq var3 : this.p) {
         var3.bounds(this.F.x() + 10, var1, Math.max(1, this.F.width() - 20));
         if (var3 instanceof p7 && this.i == null) {
            var3.O = 22;
         }

         if (var3 instanceof p9 var4) {
            var4.Y(this.F.y() + 39, this.F.y() + this.F.height() - 7);
         }

         var1 += var3.O + 2;
      }
   }

   private boolean L(double var1, double var3) {
      return this.F.contains(var1, var3, 0) && var3 >= this.F.y() + 39 && var3 < this.F.y() + this.F.height() - 7;
   }

   public boolean C(double var1, double var3, int var5, int var6, int var7) {
      if (!this.g()) {
         return false;
      }

      this.B();
      double var8 = this.R(var1);
      double var10 = this.w(var3);
      if (!this.F.contains(var8, var10, 0)) {
         this.s();
         return false;
      }

      if (var10 < this.F.y() + 34) {
         if (var8 >= this.F.x() + this.F.width() - 24) {
            hx.w(hx.t.S);
            this.s();
         } else if (var5 == 0) {
            this.z = (boolean)1;
            this.K = var1 - this.F.x();
            this.Q = var3 - this.F.y();
         }

         return true;
      } else {
         for (oq var13 : this.p) {
            if (var13 instanceof p9 var14 && var14.y()) {
               if (var14.R(var8, var10)) {
                  var14.N(var8, var10, var5);
                  this.B = var13;
                  this.b.run();
                  return true;
               }

               var14.n();
            }
         }

         if (!this.L(var8, var10)) {
            return true;
         }

         for (int var16 = 0; var16 < this.p.size(); var16++) {
            oq var17 = this.p.get(var16);
            if (var17.L >= this.F.y() + 39 && var17.L + var17.O <= this.F.y() + this.F.height() - 7 && var17.contains(var8, var10)) {
               if (this.i == null && var5 == 1) {
                  this.X(o8.d.get(var16), (int)var1, (int)var3, var6, var7);
                  return true;
               }

               for (oq var15 : this.p) {
                  if (var15 != var17) {
                     var15.blur();
                  }
               }

               if (var17.click(var8, var10, var5)) {
                  this.B = var17;
                  this.b.run();
               }

               return true;
            }
         }

         return true;
      }
   }

   public boolean C(double var1, double var3, int var5, int var6) {
      if (!this.g()) {
         return false;
      } else if (this.z) {
         _h.p var7 = this.Y();
         this.F = new _h.p(
            _h.n((int)Math.round(var1 - this.K), var7.width(), var5),
            _h.n((int)Math.round(var3 - this.Q), var7.height(), var6),
            this.F.width(),
            this.F.height()
         );
         this.B();
         return true;
      } else if (this.B != null) {
         this.B.drag(this.R(var1), this.w(var3));
         this.b.run();
         return true;
      } else {
         return false;
      }
   }

   public boolean M() {
      int var1 = !this.z && this.B == null ? 0 : 1;
      this.z = (boolean)0;
      if (this.B != null) {
         this.B.release();
         this.B = null;
         this.d.run();
      }

      return (boolean)var1;
   }

   public boolean Z(int var1) {
      for (oq var3 : this.p) {
         if (var3 instanceof p9 var4 && var4.C()) {
            boolean var5 = var4.key(var1);
            this.b.run();
            return var5;
         }
      }

      return false;
   }

   public boolean q(int var1) {
      for (oq var3 : this.p) {
         if (var3 instanceof p9 var4 && var4.C()) {
            var4.character(var1);
            return true;
         }
      }

      return false;
   }

   public boolean v(double var1, double var3, double var5) {
      if (!this.g()) {
         return false;
      }

      double var7 = this.R(var1);
      double var9 = this.w(var3);
      if (!this.F.contains(var7, var9, 0)) {
         return false;
      }

      for (oq var12 : this.p) {
         if (var12 instanceof p9 var13 && var13.R(var7, var9)) {
            var13.J(var5);
            return true;
         }
      }

      int var14 = this.p.stream().mapToInt(var0 -> var0.O + 2).sum();
      this.k = Math.clamp(this.k - (int)Math.round(var5 * 22.0), 0, Math.max(0, var14 - this.F.height() + 46));
      this.B();
      return true;
   }

   public void c(GuiGraphicsExtractor var1, int var2, int var3) {
      if (this.g()) {
         this.B();
         int var4 = (int)Math.floor(this.R(var2));
         int var5 = (int)Math.floor(this.w(var3));
         var1.pose().pushMatrix();
         var1.pose().translate(this.F.x(), this.F.y()).scale(0.86F).translate(-this.F.x(), -this.F.y());

         try (r var6 = r.p(this.U.V())) {
            fr.S(var1, this.F.x() - 3, this.F.y() - 3, this.F.width() + 6, this.F.height() + 6, 13.0F, 1593835520);
            fr.S(var1, this.F.x(), this.F.y(), this.F.width(), this.F.height(), 10.0F, -183957239);
            rd.v(var1, this.i == null ? "HUD Elements" : o8.F(this.i.M), this.F.x() + 12, rz.W(this.F.y(), 32.0, 13.0), 13.0, -1250068);
            ou.u(var1, ou.e("x"), this.F.x() + this.F.width() - 20, this.F.y() + 12, 12, -5592406);
            fr.k(var1, this.F.x() + 2, this.F.y() + 32, this.F.width() - 4, 1, 675036230);
            var1.enableScissor(this.F.x(), this.F.y() + 39, this.F.x() + this.F.width(), this.F.y() + this.F.height() - 7);

            try {
               for (int var7 = 0; var7 < this.p.size(); var7++) {
                  oq var8 = this.p.get(var7);
                  if (var8.L + var8.O >= this.F.y() + 39 && var8.L < this.F.y() + this.F.height() - 7) {
                     if (this.i == null && var8 instanceof p7 var9) {
                        var9.c = (boolean)1;
                        var9.R = o8.r(o8.d.get(var7));
                        var9.I = var9.R;
                     }

                     var8.render(var1, this.L(var4, var5) ? var4 : -1, var5);
                  }
               }
            } finally {
               var1.disableScissor();
            }

            for (oq var25 : this.p) {
               if (var25 instanceof p9 var26 && var26.y()) {
                  var26.x(var1, var4, var5);
               }
            }
         } finally {
            var1.pose().popMatrix();
         }
      }
   }
}
