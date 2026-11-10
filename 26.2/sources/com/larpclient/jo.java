package com.larpclient;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.ToDoubleFunction;
import org.joml.Quaterniond;
import org.joml.Quaterniondc;
import org.joml.Vector3d;

public final class jo {
   public static final int B = 32;
   public static final double H = 0.01890625;
   private final ArrayDeque<jo.y> A = new ArrayDeque<>();
   private final Quaterniond S = new Quaterniond();
   private final Vector3d R = new Vector3d();
   private final Vector3d a = new Vector3d();
   private String n = "";
   private jo.j s;
   private int Z = 16;
   private int j;
   private int M = -1;
   private boolean t;
   private double c;
   private double K;
   private double z = 1.0;
   private double L;
   private double w;
   private double e = 8.0;
   private jo.j v;
   private double U = 8.0;

   public void h() {
      this.t = (boolean)0;
      this.c = this.K = 0.0;
      this.n = "";
      this.M = -1;
      this.j = 0;
      this.s = null;
      this.v = null;
      this.U = 8.0;
      this.A.clear();
   }

   public void Y(int var1) {
      this.Z = Math.clamp(var1, 4, 32);
      this.P();
   }

   public void r(double var1) {
      this.U = Math.clamp(var1, 0.6, 8.0);
   }

   private void P() {
      int var1 = 1 + (this.Z - 4) * 3 / 28;

      while (this.A.size() > var1) {
         this.A.removeLast();
      }
   }

   public void h(List<String> var1) {
      String var2 = var1.isEmpty() ? "" : (String)var1.getFirst();
      this.X(var1, this.n.equals(var2) ? this.M : this.M + 1);
   }

   public void X(List<String> var1, int var2) {
      String var3 = var1.isEmpty() ? "" : (String)var1.getFirst();
      if (!this.n.equals(var3) || this.M != var2) {
         if (this.s != null && this.s.opacity >= 0.00392156862745098) {
            this.A.addFirst(new jo.y(this.s));
         }

         this.P();
         this.n = var3;
         this.M = var2;
         this.j++;
         this.z = this.j % 2 == 0 ? -1.0 : 1.0;
         this.L = this.z * 0.1;
         this.w = 0.17 + 0.035 * Math.sin(this.j * 2.399);
         this.e = Math.min(7.5 + 0.6 * Math.sin(this.j * 1.7), this.U);
         this.c = this.K = 0.0;
         this.t = (boolean)0;
         this.s = null;
      }
   }

   public List<jo.j> L(
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
      this.R.set(var1, var3, var5);
      ArrayList var19 = new ArrayList(5);
      double var20 = 0.65 + this.Z / 32.0 * 0.65;
      Iterator var22 = this.A.iterator();

      while (var22.hasNext()) {
         jo.y var23 = (jo.y)var22.next();
         var23.S += var16;
         if (var23.S >= var20) {
            var22.remove();
         } else {
            double var24 = l.F(var23.S / var20);
            jo.j var26 = var23.J;
            double var27 = var26.opacity * (1.0 - var24);
            if (var27 >= 0.00392156862745098) {
               var19.add(this.w(var26, var27, var26.age, var23.S / var20));
            }
         }
      }

      if (this.n.isBlank()) {
         return List.copyOf(var19);
      }

      if (!this.t) {
         double var33 = Math.clamp(Math.abs(var8), 0.1, 8.0);
         double var35 = Math.clamp(Math.abs(var10), 0.1, 8.0);
         double var36 = 0.7 * Math.clamp(var33 / var35, 0.25, 4.0);
         double var28 = 0.75 + Math.clamp(var14, 0.0, 2.2) * 0.25;
         this.a.set(this.L * var33 * this.e, this.w * var35 * this.e, -this.e).rotate(var7);
         this.S.set(var7).rotateXYZ(0.07 * Math.sin(this.j * 1.7), this.z * 0.16 * var28, this.z * 0.1 * var28);
         double var30 = 0.098 * this.e / 0.75625 * var12;
         var30 = Math.min(var30, 1.3 * var36 * this.e / (0.01890625 * Math.max(1.0, var18.applyAsDouble(this.n))));
         var30 = Math.min(var30, 0.154 * this.e / 0.75625);
         this.v = new jo.j(this.n, this.a.x, this.a.y, this.a.z, var30, 1.0, this.S.x, this.S.y, this.S.z, this.S.w, 0.0, 0.0);
         this.t = (boolean)1;
      }

      this.c = Math.min(1.0, this.c + var16 / 0.28);
      this.K += var16;
      jo.j var34 = new jo.j(this.n, this.v.x, this.v.y, this.v.z, this.v.scale, l.F(this.c), this.v.qx, this.v.qy, this.v.qz, this.v.qw, this.K, 0.0);
      this.s = var34;
      if (var34.opacity >= 0.00392156862745098) {
         var19.add(this.w(var34, var34.opacity, this.K, 0.0));
      }

      return List.copyOf(var19);
   }

   private jo.j w(jo.j var1, double var2, double var4, double var6) {
      return new jo.j(var1.text, this.R.x + var1.x, this.R.y + var1.y, this.R.z + var1.z, var1.scale, var2, var1.qx, var1.qy, var1.qz, var1.qw, var4, var6);
   }

   public record j(String text, double x, double y, double z, double scale, double opacity, double qx, double qy, double qz, double qw, double age, double exit) {
   }

   private static final class y {
      final jo.j J;
      double S;

      y(jo.j var1) {
         this.J = var1;
      }
   }
}
