package com.larpclient;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.ToDoubleFunction;
import org.joml.Quaterniond;
import org.joml.Quaterniondc;
import org.joml.Vector3d;

public final class o1 {
   public static final int M = 32;
   public static final double Q = 0.01890625;
   private final ArrayDeque<o1.m> I = new ArrayDeque<>();
   private final Quaterniond k = new Quaterniond();
   private final Vector3d N = new Vector3d();
   private final Vector3d v = new Vector3d();
   private String h = "";
   private o1.w S;
   private int q = 16;
   private int F;
   private int L = -1;
   private boolean C;
   private double o;
   private double s;
   private double e = 1.0;
   private double Y;
   private double U;
   private double a = 8.0;
   private o1.w Z;
   private double J = 8.0;

   public void p() {
      this.C = (boolean)0;
      this.o = this.s = 0.0;
      this.h = "";
      this.L = -1;
      this.F = 0;
      this.S = null;
      this.Z = null;
      this.J = 8.0;
      this.I.clear();
   }

   public void g(int var1) {
      this.q = Math.clamp(var1, 4, 32);
      this.x();
   }

   public void y(double var1) {
      this.J = Math.clamp(var1, 0.6, 8.0);
   }

   private void x() {
      int var1 = 1 + (this.q - 4) * 3 / 28;

      while (this.I.size() > var1) {
         this.I.removeLast();
      }
   }

   public void u(List<String> var1) {
      String var2 = var1.isEmpty() ? "" : (String)var1.getFirst();
      this.q(var1, this.h.equals(var2) ? this.L : this.L + 1);
   }

   public void q(List<String> var1, int var2) {
      String var3 = var1.isEmpty() ? "" : (String)var1.getFirst();
      if (!this.h.equals(var3) || this.L != var2) {
         if (this.S != null && this.S.opacity >= 0.00392156862745098) {
            this.I.addFirst(new o1.m(this.S));
         }

         this.x();
         this.h = var3;
         this.L = var2;
         this.F++;
         this.e = this.F % 2 == 0 ? -1.0 : 1.0;
         this.Y = this.e * 0.1;
         this.U = 0.17 + 0.035 * Math.sin(this.F * 2.399);
         this.a = Math.min(7.5 + 0.6 * Math.sin(this.F * 1.7), this.J);
         this.o = this.s = 0.0;
         this.C = (boolean)0;
         this.S = null;
      }
   }

   public List<o1.w> p(
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
      this.N.set(var1, var3, var5);
      ArrayList var19 = new ArrayList(5);
      double var20 = 0.65 + this.q / 32.0 * 0.65;
      Iterator var22 = this.I.iterator();

      while (var22.hasNext()) {
         o1.m var23 = (o1.m)var22.next();
         var23.z += var16;
         if (var23.z >= var20) {
            var22.remove();
         } else {
            double var24 = e6.q(var23.z / var20);
            o1.w var26 = var23.Z;
            double var27 = var26.opacity * (1.0 - var24);
            if (var27 >= 0.00392156862745098) {
               var19.add(this.i(var26, var27, var26.age, var23.z / var20));
            }
         }
      }

      if (this.h.isBlank()) {
         return List.copyOf(var19);
      }

      if (!this.C) {
         double var33 = Math.clamp(Math.abs(var8), 0.1, 8.0);
         double var35 = Math.clamp(Math.abs(var10), 0.1, 8.0);
         double var36 = 0.7 * Math.clamp(var33 / var35, 0.25, 4.0);
         double var28 = 0.75 + Math.clamp(var14, 0.0, 2.2) * 0.25;
         this.v.set(this.Y * var33 * this.a, this.U * var35 * this.a, -this.a).rotate(var7);
         this.k.set(var7).rotateXYZ(0.07 * Math.sin(this.F * 1.7), this.e * 0.16 * var28, this.e * 0.1 * var28);
         double var30 = 0.098 * this.a / 0.75625 * var12;
         var30 = Math.min(var30, 1.3 * var36 * this.a / (0.01890625 * Math.max(1.0, var18.applyAsDouble(this.h))));
         var30 = Math.min(var30, 0.154 * this.a / 0.75625);
         this.Z = new o1.w(this.h, this.v.x, this.v.y, this.v.z, var30, 1.0, this.k.x, this.k.y, this.k.z, this.k.w, 0.0, 0.0);
         this.C = (boolean)1;
      }

      this.o = Math.min(1.0, this.o + var16 / 0.28);
      this.s += var16;
      o1.w var34 = new o1.w(this.h, this.Z.x, this.Z.y, this.Z.z, this.Z.scale, e6.q(this.o), this.Z.qx, this.Z.qy, this.Z.qz, this.Z.qw, this.s, 0.0);
      this.S = var34;
      if (var34.opacity >= 0.00392156862745098) {
         var19.add(this.i(var34, var34.opacity, this.s, 0.0));
      }

      return List.copyOf(var19);
   }

   private o1.w i(o1.w var1, double var2, double var4, double var6) {
      return new o1.w(var1.text, this.N.x + var1.x, this.N.y + var1.y, this.N.z + var1.z, var1.scale, var2, var1.qx, var1.qy, var1.qz, var1.qw, var4, var6);
   }

   private static final class m {
      final o1.w Z;
      double z;

      m(o1.w var1) {
         this.Z = var1;
      }
   }

   public record w(String text, double x, double y, double z, double scale, double opacity, double qx, double qy, double qz, double qw, double age, double exit) {
   }
}
