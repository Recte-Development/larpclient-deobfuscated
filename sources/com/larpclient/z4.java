package com.larpclient;

import com.larpclient.module.df;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.gui.GuiGraphicsExtractor;

public final class z4 {
   public static final float k = 0.86F;
   private final List<b> S = new ArrayList<>();
   private final Runnable x;
   private final Runnable H;
   private ez.h W;
   private mq.s X;
   private b y;
   private boolean C;
   private double a;
   private double G;
   private int R;
   private final ef Y = new ef(0.0);

   public z4(Runnable var1, Runnable var2) {
      this.x = var1;
      this.H = var2;
   }

   public boolean l() {
      return this.W != null;
   }

   public ez.h m() {
      return this.W == null ? null : new ez.h(this.W.x(), this.W.y(), (int)Math.ceil(this.W.width() * 0.86F), (int)Math.ceil(this.W.height() * 0.86F));
   }

   private double Y(double var1) {
      return this.W.x() + (var1 - this.W.x()) / 0.86F;
   }

   private double t(double var1) {
      return this.W.y() + (var1 - this.W.y()) / 0.86F;
   }

   public void y() {
      this.S.clear();
      this.W = null;
      this.y = null;
      this.C = (boolean)0;
      this.R = 0;
   }

   public void n() {
      if (this.l()) {
         for (b var2 : this.S) {
            var2.blur();
         }

         this.S.clear();
         this.W = null;
         this.y = null;
         this.C = (boolean)0;
         this.R = 0;
         this.H.run();
      }
   }

   public void m(int var1, int var2, int var3, int var4) {
      this.n();
      this.X = null;

      for (mq.s var6 : mq.Y) {
         u7 var7 = new u7(mq.w(var6.g), () -> {
            int var2x = !mq.M(var6) ? 1 : 0;
            mq.m(var6, (boolean)var2x);
            this.x.run();
            this.H.run();
            kb.d(var2x != 0 ? kb.n.l : kb.n.O);
         });
         var7.s = null;
         var7.W = 22;
         this.S.add(var7);
      }

      this.X(var1, var2, 220, var3, var4);
   }

   public void w(mq.s var1, int var2, int var3, int var4, int var5) {
      this.n();
      this.X = var1;
      com.larpclient.module.d9 var6 = new com.larpclient.module.d9("Widget Size", "Scale this HUD widget", var1.X(), 0.5, 2.5, 0.02);
      var6.h(var2x -> {
         var1.W(var2x);
         this.x.run();
      });
      this.S.add(new un(var6, true));

      for (df var8 : mq.E(var1)) {
         this.S.add(new un(var8, true));
      }

      u7 var9 = new u7("Hide widget", () -> {
         var1.r(true);
         this.x.run();
         this.n();
      });
      u7 var10 = new u7("Reset placement", () -> {
         var1.f();
         this.x.run();
         this.H.run();
      });
      this.S.add(var9);
      this.S.add(var10);
      this.X(var2, var3, 252, var4, var5);
      kb.d(kb.n.T);
   }

   private void X(int var1, int var2, int var3, int var4, int var5) {
      var3 = Math.max(1, Math.min(var3, (int)((var4 - 10) / 0.86F)));
      int var6 = Math.max(1, Math.min((int)((var5 - 10) / 0.86F), 46 + this.S.stream().mapToInt(var0 -> var0.W + 2).sum()));
      this.W = new ez.h(ez.a(var1, (int)Math.ceil(var3 * 0.86F), var4), ez.a(var2, (int)Math.ceil(var6 * 0.86F), var5), var3, var6);
      this.R = 0;
      this.Y.Q(0.0);
      this.Y.m(1.0, 300);
      this.q();
   }

   private void q() {
      int var1 = this.W.y() + 39 - this.R;

      for (b var3 : this.S) {
         var3.bounds(this.W.x() + 10, var1, Math.max(1, this.W.width() - 20));
         if (var3 instanceof u7 && this.X == null) {
            var3.W = 22;
         }

         if (var3 instanceof un var4) {
            var4.N(this.W.y() + 39, this.W.y() + this.W.height() - 7);
         }

         var1 += var3.W + 2;
      }
   }

   private boolean B(double var1, double var3) {
      return this.W.contains(var1, var3, 0) && var3 >= this.W.y() + 39 && var3 < this.W.y() + this.W.height() - 7;
   }

   public boolean B(double var1, double var3, int var5, int var6, int var7) {
      if (!this.l()) {
         return false;
      }

      this.q();
      double var8 = this.Y(var1);
      double var10 = this.t(var3);
      if (!this.W.contains(var8, var10, 0)) {
         this.n();
         return false;
      }

      if (var10 < this.W.y() + 34) {
         if (var8 >= this.W.x() + this.W.width() - 24) {
            kb.d(kb.n.z);
            this.n();
         } else if (var5 == 0) {
            this.C = (boolean)1;
            this.a = var1 - this.W.x();
            this.G = var3 - this.W.y();
         }

         return true;
      } else {
         for (b var13 : this.S) {
            if (var13 instanceof un var14 && var14.g()) {
               if (var14.e(var8, var10)) {
                  var14.C(var8, var10, var5);
                  this.y = var13;
                  this.x.run();
                  return true;
               }

               var14.M();
            }
         }

         if (!this.B(var8, var10)) {
            return true;
         }

         for (int var16 = 0; var16 < this.S.size(); var16++) {
            b var17 = this.S.get(var16);
            if (var17.J >= this.W.y() + 39 && var17.J + var17.W <= this.W.y() + this.W.height() - 7 && var17.contains(var8, var10)) {
               if (this.X == null && var5 == 1) {
                  this.w(mq.Y.get(var16), (int)var1, (int)var3, var6, var7);
                  return true;
               }

               for (b var15 : this.S) {
                  if (var15 != var17) {
                     var15.blur();
                  }
               }

               if (var17.click(var8, var10, var5)) {
                  this.y = var17;
                  this.x.run();
               }

               return true;
            }
         }

         return true;
      }
   }

   public boolean R(double var1, double var3, int var5, int var6) {
      if (!this.l()) {
         return false;
      } else if (this.C) {
         ez.h var7 = this.m();
         this.W = new ez.h(
            ez.a((int)Math.round(var1 - this.a), var7.width(), var5),
            ez.a((int)Math.round(var3 - this.G), var7.height(), var6),
            this.W.width(),
            this.W.height()
         );
         this.q();
         return true;
      } else if (this.y != null) {
         this.y.drag(this.Y(var1), this.t(var3));
         this.x.run();
         return true;
      } else {
         return false;
      }
   }

   public boolean p() {
      int var1 = !this.C && this.y == null ? 0 : 1;
      this.C = (boolean)0;
      if (this.y != null) {
         this.y.release();
         this.y = null;
         this.H.run();
      }

      return (boolean)var1;
   }

   public boolean w(int var1) {
      for (b var3 : this.S) {
         if (var3 instanceof un var4 && var4.s()) {
            boolean var5 = var4.key(var1);
            this.x.run();
            return var5;
         }
      }

      return false;
   }

   public boolean z(int var1) {
      for (b var3 : this.S) {
         if (var3 instanceof un var4 && var4.s()) {
            var4.character(var1);
            return true;
         }
      }

      return false;
   }

   public boolean D(double var1, double var3, double var5) {
      if (!this.l()) {
         return false;
      }

      double var7 = this.Y(var1);
      double var9 = this.t(var3);
      if (!this.W.contains(var7, var9, 0)) {
         return false;
      }

      for (b var12 : this.S) {
         if (var12 instanceof un var13 && var13.e(var7, var9)) {
            var13.z(var5);
            return true;
         }
      }

      int var14 = this.S.stream().mapToInt(var0 -> var0.W + 2).sum();
      this.R = Math.clamp(this.R - (int)Math.round(var5 * 22.0), 0, Math.max(0, var14 - this.W.height() + 46));
      this.q();
      return true;
   }

   public void g(GuiGraphicsExtractor var1, int var2, int var3) {
      if (this.l()) {
         this.q();
         int var4 = (int)Math.floor(this.Y(var2));
         int var5 = (int)Math.floor(this.t(var3));
         var1.pose().pushMatrix();
         var1.pose().translate(this.W.x(), this.W.y()).scale(0.86F).translate(-this.W.x(), -this.W.y());

         try (de var6 = de.l(this.Y.G())) {
            c7.n(var1, this.W.x() - 3, this.W.y() - 3, this.W.width() + 6, this.W.height() + 6, 13.0F, 1593835520);
            c7.n(var1, this.W.x(), this.W.y(), this.W.width(), this.W.height(), 10.0F, -183957239);
            we.F(var1, this.X == null ? "HUD Elements" : mq.w(this.X.g), this.W.x() + 12, ew.N(this.W.y(), 32.0, 13.0), 13.0, -1250068);
            tp.N(var1, tp.j("x"), this.W.x() + this.W.width() - 20, this.W.y() + 12, 12, -5592406);
            c7.s(var1, this.W.x() + 2, this.W.y() + 32, this.W.width() - 4, 1, 675036230);
            var1.enableScissor(this.W.x(), this.W.y() + 39, this.W.x() + this.W.width(), this.W.y() + this.W.height() - 7);

            try {
               for (int var7 = 0; var7 < this.S.size(); var7++) {
                  b var8 = this.S.get(var7);
                  if (var8.J + var8.W >= this.W.y() + 39 && var8.J < this.W.y() + this.W.height() - 7) {
                     if (this.X == null && var8 instanceof u7 var9) {
                        var9.r = (boolean)1;
                        var9.U = mq.M(mq.Y.get(var7));
                        var9.v = var9.U;
                     }

                     var8.render(var1, this.B(var4, var5) ? var4 : -1, var5);
                  }
               }
            } finally {
               var1.disableScissor();
            }

            for (b var25 : this.S) {
               if (var25 instanceof un var26 && var26.g()) {
                  var26.M(var1, var4, var5);
               }
            }
         } finally {
            var1.pose().popMatrix();
         }
      }
   }
}
