package com.larpclient;

import net.minecraft.class_238;

public final class qw {
   private class_238 A;
   private class_238 O;
   private double Z;
   private double o;
   private double W;
   private double b;
   private double q;

   public void B() {
      this.A = this.O = null;
      this.W = this.b = 0.0;
   }

   public qw.c i(double var1) {
      if (this.O == null) {
         return new qw.c(null, 0.0);
      }

      double var3 = R(this.o <= 0.0 ? 1.0 : (var1 - this.Z) / this.o);
      double var5 = R((var1 - this.q) / 0.1);
      return new qw.c(
         new class_238(
            Y(this.A.field_1323, this.O.field_1323, var3),
            Y(this.A.field_1322, this.O.field_1322, var3),
            Y(this.A.field_1321, this.O.field_1321, var3),
            Y(this.A.field_1320, this.O.field_1320, var3),
            Y(this.A.field_1325, this.O.field_1325, var3),
            Y(this.A.field_1324, this.O.field_1324, var3)
         ),
         Y(this.W, this.b, var5)
      );
   }

   public qw.c U(class_238 var1, double var2, double var4) {
      qw.c var6 = this.i(var2);
      if (var1 != null && !var1.equals(this.O)) {
         int var7 = var6.bounds != null && !(var6.alpha < 0.001) && !(var1.method_1005().method_1022(var6.bounds.method_1005()) > 8.0) ? 0 : 1;
         this.A = var7 != 0 ? var1 : var6.bounds;
         this.O = var1;
         this.Z = var2;
         this.o = Math.max(0.0, var4);
         if (var7 != 0) {
            this.W = 0.0;
            this.b = 1.0;
            this.q = var2;
         }
      }

      double var9 = var1 == null ? 0.0 : 1.0;
      if (var9 != this.b) {
         this.W = var6.alpha;
         this.b = var9;
         this.q = var2;
      }

      return this.i(var2);
   }

   private static double R(double var0) {
      var0 = Math.clamp(var0, 0.0, 1.0);
      return 1.0 - Math.pow(1.0 - var0, 3.0);
   }

   private static double Y(double var0, double var2, double var4) {
      return var0 + (var2 - var0) * var4;
   }

   public record c(class_238 bounds, double alpha) {
   }
}
