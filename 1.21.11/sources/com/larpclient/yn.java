package com.larpclient;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.ToDoubleFunction;
import org.joml.Quaterniond;
import org.joml.Quaterniondc;
import org.joml.Vector3d;

public final class yn {
   public static final int m = 32;
   public static final double z = 0.01890625;
   private final ArrayDeque<yn.q> K = new ArrayDeque<>();
   private final Quaterniond N = new Quaterniond();
   private final Vector3d l = new Vector3d();
   private final Vector3d R = new Vector3d();
   private String C = "";
   private yn.u H;
   private int P = 16;
   private int V;
   private int A = -1;
   private boolean u;
   private double s;
   private double h;
   private double d = 1.0;
   private double E;
   private double X;
   private double O = 8.0;
   private yn.u r;
   private double y = 8.0;

   public void I() {
      this.u = (boolean)0;
      this.s = this.h = 0.0;
      this.C = "";
      this.A = -1;
      this.V = 0;
      this.H = null;
      this.r = null;
      this.y = 8.0;
      this.K.clear();
   }

   public void A(int var1) {
      this.P = Math.clamp(var1, 4, 32);
      this.d();
   }

   public void t(double var1) {
      this.y = Math.clamp(var1, 0.6, 8.0);
   }

   private void d() {
      int var1 = 1 + (this.P - 4) * 3 / 28;

      while (this.K.size() > var1) {
         this.K.removeLast();
      }
   }

   public void K(List<String> var1) {
      String var2 = var1.isEmpty() ? "" : (String)var1.getFirst();
      this.b(var1, this.C.equals(var2) ? this.A : this.A + 1);
   }

   public void b(List<String> var1, int var2) {
      String var3 = var1.isEmpty() ? "" : (String)var1.getFirst();
      if (!this.C.equals(var3) || this.A != var2) {
         if (this.H != null && this.H.opacity >= 0.00392156862745098) {
            this.K.addFirst(new yn.q(this.H));
         }

         this.d();
         this.C = var3;
         this.A = var2;
         this.V++;
         this.d = this.V % 2 == 0 ? -1.0 : 1.0;
         this.E = this.d * 0.1;
         this.X = 0.17 + 0.035 * Math.sin(this.V * 2.399);
         this.O = Math.min(7.5 + 0.6 * Math.sin(this.V * 1.7), this.y);
         this.s = this.h = 0.0;
         this.u = (boolean)0;
         this.H = null;
      }
   }

   public List<yn.u> w(
      double var1,
      double var3,
      double var5,
      Quaterniondc var7,
      double var8,
      double var10,
      double var12,
      double var14,
      double var16,
      ToDoubleFunction<String> var18
   ) {
      var16 = Math.clamp(var16, 0.0, 0.1);
      this.l.set(var1, var3, var5);
      ArrayList var19 = new ArrayList(5);
      double var20 = 0.65 + this.P / 32.0 * 0.65;
      Iterator var22 = this.K.iterator();

      while (var22.hasNext()) {
         yn.q var23 = (yn.q)var22.next();
         var23.r += var16;
         if (var23.r >= var20) {
            var22.remove();
         } else {
            double var24 = u7.T(var23.r / var20);
            yn.u var26 = var23.p;
            double var27 = var26.opacity * (1.0 - var24);
            if (var27 >= 0.00392156862745098) {
               var19.add(this.N(var26, var27, var26.age, var23.r / var20));
            }
         }
      }

      if (this.C.isBlank()) {
         return List.copyOf(var19);
      }

      if (!this.u) {
         double var33 = Math.clamp(Math.abs(var8), 0.1, 8.0);
         double var35 = Math.clamp(Math.abs(var10), 0.1, 8.0);
         double var36 = 0.7 * Math.clamp(var33 / var35, 0.25, 4.0);
         double var28 = 0.75 + Math.clamp(var14, 0.0, 2.2) * 0.25;
         this.R.set(this.E * var33 * this.O, this.X * var35 * this.O, -this.O).rotate(var7);
         this.N.set(var7).rotateXYZ(0.07 * Math.sin(this.V * 1.7), this.d * 0.16 * var28, this.d * 0.1 * var28);
         double var30 = 0.098 * this.O / 0.75625 * var12;
         var30 = Math.min(var30, 1.3 * var36 * this.O / (0.01890625 * Math.max(1.0, var18.applyAsDouble(this.C))));
         var30 = Math.min(var30, 0.154 * this.O / 0.75625);
         this.r = new yn.u(this.C, this.R.x, this.R.y, this.R.z, var30, 1.0, this.N.x, this.N.y, this.N.z, this.N.w, 0.0, 0.0);
         this.u = (boolean)1;
      }

      this.s = Math.min(1.0, this.s + var16 / 0.28);
      this.h += var16;
      yn.u var34 = new yn.u(this.C, this.r.x, this.r.y, this.r.z, this.r.scale, u7.T(this.s), this.r.qx, this.r.qy, this.r.qz, this.r.qw, this.h, 0.0);
      this.H = var34;
      if (var34.opacity >= 0.00392156862745098) {
         var19.add(this.N(var34, var34.opacity, this.h, 0.0));
      }

      return List.copyOf(var19);
   }

   private yn.u N(yn.u var1, double var2, double var4, double var6) {
      return new yn.u(var1.text, this.l.x + var1.x, this.l.y + var1.y, this.l.z + var1.z, var1.scale, var2, var1.qx, var1.qy, var1.qz, var1.qw, var4, var6);
   }

   private static final class q {
      final yn.u p;
      double r;

      q(yn.u var1) {
         this.p = var1;
      }
   }

   public record u(String text, double x, double y, double z, double scale, double opacity, double qx, double qy, double qz, double qw, double age, double exit) {
   }
}
