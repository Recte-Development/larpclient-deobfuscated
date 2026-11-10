package com.larpclient;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.class_332;

public final class ua {
   public static final float I = 0.86F;
   private final List<lc> s = new ArrayList<>();
   private final Runnable e;
   private final Runnable D;
   private cv.z w;
   private k.l G;
   private lc U;
   private boolean H;
   private double h;
   private double Z;
   private int R;
   private final qb F = new qb(0.0);

   public ua(Runnable var1, Runnable var2) {
      this.e = var1;
      this.D = var2;
   }

   public boolean q() {
      return this.w != null;
   }

   public cv.z j() {
      return this.w == null ? null : new cv.z(this.w.x(), this.w.y(), (int)Math.ceil(this.w.width() * 0.86F), (int)Math.ceil(this.w.height() * 0.86F));
   }

   private double I(double var1) {
      return this.w.x() + (var1 - this.w.x()) / 0.86F;
   }

   private double d(double var1) {
      return this.w.y() + (var1 - this.w.y()) / 0.86F;
   }

   public void g() {
      this.s.clear();
      this.w = null;
      this.U = null;
      this.H = (boolean)0;
      this.R = 0;
   }

   public void z() {
      if (this.q()) {
         for (lc var2 : this.s) {
            var2.blur();
         }

         this.s.clear();
         this.w = null;
         this.U = null;
         this.H = (boolean)0;
         this.R = 0;
         this.D.run();
      }
   }

   public void v(int var1, int var2, int var3, int var4) {
      this.z();
      this.G = null;

      for (k.l var6 : k.A()) {
         ls var7 = new ls(k.L(var6.s), () -> {
            int var2x = !k.B(var6) ? 1 : 0;
            k.F(var6, (boolean)var2x);
            this.e.run();
            this.D.run();
            c_.b(var2x != 0 ? c_.d.d : c_.d.n);
         });
         var7.X = null;
         var7.w = 22;
         this.s.add(var7);
      }

      this.A(var1, var2, 220, var3, var4);
   }

   public void R(k.l var1, int var2, int var3, int var4, int var5) {
      this.z();
      this.G = var1;
      com.larpclient.module.lo var6 = new com.larpclient.module.lo("Widget Size", "Scale this HUD widget", var1.S(), 0.2, 4.0, 0.02);
      var6.a(var2x -> {
         var1.V(var2x);
         this.e.run();
      });
      this.s.add(new l9(var6, true));

      for (com.larpclient.module.lb var8 : k.J(var1)) {
         this.s.add(new l9(var8, true));
      }

      ls var9 = new ls("Hide widget", () -> {
         var1.a(true);
         this.e.run();
         this.z();
      });
      ls var10 = new ls("Reset placement", () -> {
         var1.x();
         this.e.run();
         this.D.run();
      });
      this.s.add(var9);
      this.s.add(var10);
      this.A(var2, var3, 252, var4, var5);
      c_.b(c_.d.D);
   }

   private void A(int var1, int var2, int var3, int var4, int var5) {
      var3 = Math.max(1, Math.min(var3, (int)((var4 - 10) / 0.86F)));
      int var6 = Math.max(1, Math.min((int)((var5 - 10) / 0.86F), 46 + this.s.stream().mapToInt(var0 -> var0.w + 2).sum()));
      this.w = new cv.z(cv.o(var1, (int)Math.ceil(var3 * 0.86F), var4), cv.o(var2, (int)Math.ceil(var6 * 0.86F), var5), var3, var6);
      this.R = 0;
      this.F.w(0.0);
      this.F.a(1.0, 300);
      this.D();
   }

   private void D() {
      int var1 = this.w.y() + 39 - this.R;

      for (lc var3 : this.s) {
         var3.bounds(this.w.x() + 10, var1, Math.max(1, this.w.width() - 20));
         if (var3 instanceof ls && this.G == null) {
            var3.w = 22;
         }

         if (var3 instanceof l9 var4) {
            var4.Z(this.w.y() + 39, this.w.y() + this.w.height() - 7);
         }

         var1 += var3.w + 2;
      }
   }

   private boolean U(double var1, double var3) {
      return this.w.contains(var1, var3, 0) && var3 >= this.w.y() + 39 && var3 < this.w.y() + this.w.height() - 7;
   }

   public boolean K(double var1, double var3, int var5, int var6, int var7) {
      if (!this.q()) {
         return false;
      }

      this.D();
      double var8 = this.I(var1);
      double var10 = this.d(var3);
      if (!this.w.contains(var8, var10, 0)) {
         this.z();
         return false;
      }

      if (var10 < this.w.y() + 34) {
         if (var8 >= this.w.x() + this.w.width() - 24) {
            c_.b(c_.d.g);
            this.z();
         } else if (var5 == 0) {
            this.H = (boolean)1;
            this.h = var1 - this.w.x();
            this.Z = var3 - this.w.y();
         }

         return true;
      } else {
         for (lc var13 : this.s) {
            if (var13 instanceof l9 var14 && var14.j()) {
               if (var14.p(var8, var10)) {
                  var14.b(var8, var10, var5);
                  this.U = var13;
                  this.e.run();
                  return true;
               }

               var14.f();
            }
         }

         if (!this.U(var8, var10)) {
            return true;
         }

         for (int var16 = 0; var16 < this.s.size(); var16++) {
            lc var17 = this.s.get(var16);
            if (var17.g >= this.w.y() + 39 && var17.g + var17.w <= this.w.y() + this.w.height() - 7 && var17.contains(var8, var10)) {
               if (this.G == null && var5 == 1) {
                  this.R(k.A().get(var16), (int)var1, (int)var3, var6, var7);
                  return true;
               }

               for (lc var15 : this.s) {
                  if (var15 != var17) {
                     var15.blur();
                  }
               }

               if (var17.click(var8, var10, var5)) {
                  this.U = var17;
                  this.e.run();
               }

               return true;
            }
         }

         return true;
      }
   }

   public boolean q(double var1, double var3, int var5, int var6) {
      if (!this.q()) {
         return false;
      } else if (this.H) {
         cv.z var7 = this.j();
         this.w = new cv.z(
            cv.o((int)Math.round(var1 - this.h), var7.width(), var5),
            cv.o((int)Math.round(var3 - this.Z), var7.height(), var6),
            this.w.width(),
            this.w.height()
         );
         this.D();
         return true;
      } else if (this.U != null) {
         this.U.drag(this.I(var1), this.d(var3));
         this.e.run();
         return true;
      } else {
         return false;
      }
   }

   public boolean A() {
      int var1 = !this.H && this.U == null ? 0 : 1;
      this.H = (boolean)0;
      if (this.U != null) {
         this.U.release();
         this.U = null;
         this.D.run();
      }

      return (boolean)var1;
   }

   public boolean Q(int var1) {
      for (lc var3 : this.s) {
         if (var3 instanceof l9 var4 && var4.P()) {
            boolean var5 = var4.key(var1);
            this.e.run();
            return var5;
         }
      }

      return false;
   }

   public boolean y(int var1) {
      for (lc var3 : this.s) {
         if (var3 instanceof l9 var4 && var4.P()) {
            var4.character(var1);
            return true;
         }
      }

      return false;
   }

   public boolean A(double var1, double var3, double var5) {
      if (!this.q()) {
         return false;
      }

      double var7 = this.I(var1);
      double var9 = this.d(var3);
      if (!this.w.contains(var7, var9, 0)) {
         return false;
      }

      for (lc var12 : this.s) {
         if (var12 instanceof l9 var13 && var13.p(var7, var9)) {
            var13.V(var5);
            return true;
         }
      }

      int var14 = this.s.stream().mapToInt(var0 -> var0.w + 2).sum();
      this.R = Math.clamp(this.R - (int)Math.round(var5 * 22.0), 0, Math.max(0, var14 - this.w.height() + 46));
      this.D();
      return true;
   }

   public void D(class_332 var1, int var2, int var3) {
      if (this.q()) {
         this.D();
         int var4 = (int)Math.floor(this.I(var2));
         int var5 = (int)Math.floor(this.d(var3));
         var1.method_51448().pushMatrix();
         var1.method_51448().translate(this.w.x(), this.w.y()).scale(0.86F).translate(-this.w.x(), -this.w.y());

         try (fp var6 = fp.O(this.F.u())) {
            gu.N(var1, this.w.x() - 3, this.w.y() - 3, this.w.width() + 6, this.w.height() + 6, 13.0F, 1593835520);
            gu.N(var1, this.w.x(), this.w.y(), this.w.width(), this.w.height(), 10.0F, -183957239);
            w5.y(var1, this.G == null ? "HUD Elements" : k.L(this.G.s), this.w.x() + 12, q2.e(this.w.y(), 32.0, 13.0), 13.0, -1250068);
            lx.N(var1, lx.n("x"), this.w.x() + this.w.width() - 20, this.w.y() + 12, 12, -5592406);
            gu.Z(var1, this.w.x() + 2, this.w.y() + 32, this.w.width() - 4, 1, 675036230);
            var1.method_44379(this.w.x(), this.w.y() + 39, this.w.x() + this.w.width(), this.w.y() + this.w.height() - 7);

            try {
               for (int var7 = 0; var7 < this.s.size(); var7++) {
                  lc var8 = this.s.get(var7);
                  if (var8.g + var8.w >= this.w.y() + 39 && var8.g < this.w.y() + this.w.height() - 7) {
                     if (this.G == null && var8 instanceof ls var9) {
                        var9.S = (boolean)1;
                        var9.o = k.B(k.A().get(var7));
                        var9.W = var9.o;
                     }

                     var8.render(var1, this.U(var4, var5) ? var4 : -1, var5);
                  }
               }
            } finally {
               var1.method_44380();
            }

            for (lc var25 : this.s) {
               if (var25 instanceof l9 var26 && var26.j()) {
                  var26.S(var1, var4, var5);
               }
            }
         } finally {
            var1.method_51448().popMatrix();
         }
      }
   }
}
