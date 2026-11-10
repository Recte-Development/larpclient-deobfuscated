package com.larpclient;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.gui.GuiGraphicsExtractor;

public final class m2 {
   public static final float h = 0.86F;
   private final List<jd> S = new ArrayList<>();
   private final Runnable k;
   private final Runnable b;
   private hw.l m;
   private m4.p g;
   private jd I;
   private boolean c;
   private double t;
   private double a;
   private int Q;
   private final jp X = new jp(0.0);

   public m2(Runnable var1, Runnable var2) {
      this.k = var1;
      this.b = var2;
   }

   public boolean H() {
      return this.m != null;
   }

   public hw.l A() {
      return this.m == null ? null : new hw.l(this.m.x(), this.m.y(), (int)Math.ceil(this.m.width() * 0.86F), (int)Math.ceil(this.m.height() * 0.86F));
   }

   private double b(double var1) {
      return this.m.x() + (var1 - this.m.x()) / 0.86F;
   }

   private double n(double var1) {
      return this.m.y() + (var1 - this.m.y()) / 0.86F;
   }

   public void g() {
      this.S.clear();
      this.m = null;
      this.I = null;
      this.c = (boolean)0;
      this.Q = 0;
   }

   public void H() {
      if (this.H()) {
         for (jd var2 : this.S) {
            var2.blur();
         }

         this.S.clear();
         this.m = null;
         this.I = null;
         this.c = (boolean)0;
         this.Q = 0;
         this.b.run();
      }
   }

   public void H(int var1, int var2, int var3, int var4) {
      this.H();
      this.g = null;

      for (m4.p var6 : m4.P()) {
         jx var7 = new jx(m4.W(var6.r), () -> {
            int var2x = !m4.B(var6) ? 1 : 0;
            m4.Z(var6, (boolean)var2x);
            this.k.run();
            this.b.run();
            sr.v(var2x != 0 ? sr.e.J : sr.e.v);
         });
         var7.q = null;
         var7.J = 22;
         this.S.add(var7);
      }

      this.y(var1, var2, 220, var3, var4);
   }

   public void z(m4.p var1, int var2, int var3, int var4, int var5) {
      this.H();
      this.g = var1;
      com.larpclient.module.e var6 = new com.larpclient.module.e("Widget Size", "Scale this HUD widget", var1.k(), 0.2, 4.0, 0.02);
      var6.o(var2x -> {
         var1.f(var2x);
         this.k.run();
      });
      this.S.add(new ju(var6, true));

      for (com.larpclient.module.q var8 : m4.y(var1)) {
         this.S.add(new ju(var8, true));
      }

      jx var9 = new jx("Hide widget", () -> {
         var1.z(true);
         this.k.run();
         this.H();
      });
      jx var10 = new jx("Reset placement", () -> {
         var1.g();
         this.k.run();
         this.b.run();
      });
      this.S.add(var9);
      this.S.add(var10);
      this.y(var2, var3, 252, var4, var5);
      sr.v(sr.e.S);
   }

   private void y(int var1, int var2, int var3, int var4, int var5) {
      var3 = Math.max(1, Math.min(var3, (int)((var4 - 10) / 0.86F)));
      int var6 = Math.max(1, Math.min((int)((var5 - 10) / 0.86F), 46 + this.S.stream().mapToInt(var0 -> var0.J + 2).sum()));
      this.m = new hw.l(hw.m(var1, (int)Math.ceil(var3 * 0.86F), var4), hw.m(var2, (int)Math.ceil(var6 * 0.86F), var5), var3, var6);
      this.Q = 0;
      this.X.F(0.0);
      this.X.k(1.0, 300);
      this.W();
   }

   private void W() {
      int var1 = this.m.y() + 39 - this.Q;

      for (jd var3 : this.S) {
         var3.bounds(this.m.x() + 10, var1, Math.max(1, this.m.width() - 20));
         if (var3 instanceof jx && this.g == null) {
            var3.J = 22;
         }

         if (var3 instanceof ju var4) {
            var4.q(this.m.y() + 39, this.m.y() + this.m.height() - 7);
         }

         var1 += var3.J + 2;
      }
   }

   private boolean T(double var1, double var3) {
      return this.m.contains(var1, var3, 0) && var3 >= this.m.y() + 39 && var3 < this.m.y() + this.m.height() - 7;
   }

   public boolean E(double var1, double var3, int var5, int var6, int var7) {
      if (!this.H()) {
         return false;
      }

      this.W();
      double var8 = this.b(var1);
      double var10 = this.n(var3);
      if (!this.m.contains(var8, var10, 0)) {
         this.H();
         return false;
      }

      if (var10 < this.m.y() + 34) {
         if (var8 >= this.m.x() + this.m.width() - 24) {
            sr.v(sr.e.d);
            this.H();
         } else if (var5 == 0) {
            this.c = (boolean)1;
            this.t = var1 - this.m.x();
            this.a = var3 - this.m.y();
         }

         return true;
      } else {
         for (jd var13 : this.S) {
            if (var13 instanceof ju var14 && var14.m()) {
               if (var14.y(var8, var10)) {
                  var14.X(var8, var10, var5);
                  this.I = var13;
                  this.k.run();
                  return true;
               }

               var14.d();
            }
         }

         if (!this.T(var8, var10)) {
            return true;
         }

         for (int var16 = 0; var16 < this.S.size(); var16++) {
            jd var17 = this.S.get(var16);
            if (var17.u >= this.m.y() + 39 && var17.u + var17.J <= this.m.y() + this.m.height() - 7 && var17.contains(var8, var10)) {
               if (this.g == null && var5 == 1) {
                  this.z(m4.P().get(var16), (int)var1, (int)var3, var6, var7);
                  return true;
               }

               for (jd var15 : this.S) {
                  if (var15 != var17) {
                     var15.blur();
                  }
               }

               if (var17.click(var8, var10, var5)) {
                  this.I = var17;
                  this.k.run();
               }

               return true;
            }
         }

         return true;
      }
   }

   public boolean R(double var1, double var3, int var5, int var6) {
      if (!this.H()) {
         return false;
      } else if (this.c) {
         hw.l var7 = this.A();
         this.m = new hw.l(
            hw.m((int)Math.round(var1 - this.t), var7.width(), var5),
            hw.m((int)Math.round(var3 - this.a), var7.height(), var6),
            this.m.width(),
            this.m.height()
         );
         this.W();
         return true;
      } else if (this.I != null) {
         this.I.drag(this.b(var1), this.n(var3));
         this.k.run();
         return true;
      } else {
         return false;
      }
   }

   public boolean a() {
      int var1 = !this.c && this.I == null ? 0 : 1;
      this.c = (boolean)0;
      if (this.I != null) {
         this.I.release();
         this.I = null;
         this.b.run();
      }

      return (boolean)var1;
   }

   public boolean U(int var1) {
      for (jd var3 : this.S) {
         if (var3 instanceof ju var4 && var4.k()) {
            boolean var5 = var4.key(var1);
            this.k.run();
            return var5;
         }
      }

      return false;
   }

   public boolean h(int var1) {
      for (jd var3 : this.S) {
         if (var3 instanceof ju var4 && var4.k()) {
            var4.character(var1);
            return true;
         }
      }

      return false;
   }

   public boolean m(double var1, double var3, double var5) {
      if (!this.H()) {
         return false;
      }

      double var7 = this.b(var1);
      double var9 = this.n(var3);
      if (!this.m.contains(var7, var9, 0)) {
         return false;
      }

      for (jd var12 : this.S) {
         if (var12 instanceof ju var13 && var13.y(var7, var9)) {
            var13.N(var5);
            return true;
         }
      }

      int var14 = this.S.stream().mapToInt(var0 -> var0.J + 2).sum();
      this.Q = Math.clamp(this.Q - (int)Math.round(var5 * 22.0), 0, Math.max(0, var14 - this.m.height() + 46));
      this.W();
      return true;
   }

   public void J(GuiGraphicsExtractor var1, int var2, int var3) {
      if (this.H()) {
         this.W();
         int var4 = (int)Math.floor(this.b(var2));
         int var5 = (int)Math.floor(this.n(var3));
         var1.pose().pushMatrix();
         var1.pose().translate(this.m.x(), this.m.y()).scale(0.86F).translate(-this.m.x(), -this.m.y());

         try (ua var6 = ua.K(this.X.B())) {
            kv.f(var1, this.m.x() - 3, this.m.y() - 3, this.m.width() + 6, this.m.height() + 6, 13.0F, 1593835520);
            kv.f(var1, this.m.x(), this.m.y(), this.m.width(), this.m.height(), 10.0F, -183957239);
            gw.S(var1, this.g == null ? "HUD Elements" : m4.W(this.g.r), this.m.x() + 12, c2.X(this.m.y(), 32.0, 13.0), 13.0, -1250068);
            ur.O(var1, ur.V("x"), this.m.x() + this.m.width() - 20, this.m.y() + 12, 12, -5592406);
            kv.d(var1, this.m.x() + 2, this.m.y() + 32, this.m.width() - 4, 1, 675036230);
            var1.enableScissor(this.m.x(), this.m.y() + 39, this.m.x() + this.m.width(), this.m.y() + this.m.height() - 7);

            try {
               for (int var7 = 0; var7 < this.S.size(); var7++) {
                  jd var8 = this.S.get(var7);
                  if (var8.u + var8.J >= this.m.y() + 39 && var8.u < this.m.y() + this.m.height() - 7) {
                     if (this.g == null && var8 instanceof jx var9) {
                        var9.g = (boolean)1;
                        var9.z = m4.B(m4.P().get(var7));
                        var9.M = var9.z;
                     }

                     var8.render(var1, this.T(var4, var5) ? var4 : -1, var5);
                  }
               }
            } finally {
               var1.disableScissor();
            }

            for (jd var25 : this.S) {
               if (var25 instanceof ju var26 && var26.m()) {
                  var26.J(var1, var4, var5);
               }
            }
         } finally {
            var1.pose().popMatrix();
         }
      }
   }
}
