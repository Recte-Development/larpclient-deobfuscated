package com.larpclient;

import net.minecraft.class_11905;
import net.minecraft.class_11908;
import net.minecraft.class_11909;
import net.minecraft.class_332;
import net.minecraft.class_437;

public abstract class tx extends t1 implements c2 {
   protected final class_437 U;
   protected int h;
   protected int o;
   protected int W;
   protected int H;
   private final qb J = new qb(0.0);
   private boolean r;
   private boolean s;
   private long R;
   private double Z = 1.0;
   private double n;
   private double u = 1.0;

   protected tx(String var1, class_437 var2) {
      super(var1);
      this.U = var2;
   }

   @Override
   public boolean dimAtmosphere() {
      return true;
   }

   protected lv f(String var1, Runnable var2, int var3, int var4, int var5) {
      lv var6 = new lv(var1, "", var2);
      var6.bounds(var3, var4, var5);
      var6.w = 24;
      this.K.add(var6);
      return var6;
   }

   protected void Q(int var1, int var2) {
      this.W = Math.min(var1, this.field_22789 - 24);
      this.H = Math.min(var2, this.field_22790 - 24);
      this.h = (this.field_22789 - this.W) / 2;
      this.o = (this.field_22790 - this.H) / 2;
   }

   protected void Z(class_332 var1) {
      if (!this.r) {
         this.r = (boolean)1;
         this.J.w(0.0);
      }

      double var2 = LarpClient.b().e().F().k ? this.J.a(1.0, 260) : 1.0;
      if (this.s) {
         var2 = this.u * (1.0 - qb.y.R.f((System.nanoTime() - this.R) / 1.6E8));
      }

      this.Z = var2;
      this.n = LarpClient.b().e().F().k ? (1.0 - var2) * 12.0 : 0.0;
      var1.method_51448().pushMatrix();
      var1.method_51448().translate(0.0F, (float)this.n);
      p.o(var1, this.h, this.o, this.W, this.H);
      p.B(var1, this.h, this.o, this.W, this.H, 7, ei.R(-451076058, var2));
   }

   protected void drawControls(class_332 var1, int var2, int var3) {
      for (lc var5 : this.K) {
         if (var5.I) {
            if (var5 instanceof lv var6) {
               var6.S = this.Z;
            }

            if (this.s) {
               var5.N = (boolean)0;
            }

            var5.render(var1, var2, var3);
         }
      }
   }

   public void method_25419() {
      if (!this.s) {
         if (!LarpClient.b().e().F().k) {
            this.field_22787.method_1507(this.U);
         } else {
            this.s = (boolean)1;
            this.u = this.Z;
            this.R = System.nanoTime();

            for (lc var2 : this.K) {
               var2.N = (boolean)0;
            }
         }
      }
   }

   public void method_25393() {
      if (this.s && System.nanoTime() - this.R >= 160000000L) {
         this.field_22787.method_1507(this.U);
      }
   }

   @Override
   public boolean method_25404(class_11908 var1) {
      return !this.r || this.Z <= 0.15 || this.s || super.method_25404(var1);
   }

   @Override
   public boolean method_25400(class_11905 var1) {
      return !this.r || this.Z <= 0.15 || this.s || super.method_25400(var1);
   }

   private class_11909 X(class_11909 var1) {
      return new class_11909(var1.comp_4798(), var1.comp_4799() - this.n, var1.comp_4800());
   }

   @Override
   public boolean method_25402(class_11909 var1, boolean var2) {
      return !this.r || this.Z <= 0.15 || this.s || super.method_25402(this.X(var1), var2);
   }

   @Override
   public boolean method_25403(class_11909 var1, double var2, double var4) {
      return this.s || super.method_25403(this.X(var1), var2, var4);
   }

   @Override
   public boolean method_25406(class_11909 var1) {
      return super.method_25406(this.X(var1));
   }

   protected int a(int var1) {
      return (int)Math.round(var1 - this.n);
   }

   protected double R(double var1) {
      return var1 - this.n;
   }

   protected void j(class_332 var1) {
      var1.method_51448().popMatrix();
   }

   public boolean method_25401(double var1, double var3, double var5, double var7) {
      return this.s;
   }

   protected boolean q() {
      return this.s;
   }

   protected double b() {
      return this.Z;
   }
}
