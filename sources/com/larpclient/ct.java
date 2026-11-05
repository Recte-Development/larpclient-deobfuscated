package com.larpclient;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.ToDoubleFunction;
import org.joml.Quaterniond;
import org.joml.Quaterniondc;
import org.joml.Vector3d;

public final class ct {
   public static final int U = 32;
   public static final double C = 0.01890625;
   private final ArrayDeque<ct.u> V = new ArrayDeque<>();
   private final Quaterniond H = new Quaterniond();
   private final Vector3d W = new Vector3d();
   private final Vector3d k = new Vector3d();
   private String I = "";
   private ct.z L;
   private int E = 16;
   private int b;
   private int c = -1;
   private boolean p;
   private double J;
   private double B;
   private double Z = 1.0;
   private double A;
   private double q;
   private double v = 8.0;
   private ct.z d;
   private double F = 8.0;

   public void c() {
      this.p = (boolean)0;
      this.J = this.B = 0.0;
      this.I = "";
      this.c = -1;
      this.b = 0;
      this.L = null;
      this.d = null;
      this.F = 8.0;
      this.V.clear();
   }

   public void g(int var1) {
      this.E = Math.clamp(var1, 4, 32);
      this.O();
   }

   public void s(double var1) {
      this.F = Math.clamp(var1, 0.6, 8.0);
   }

   private void O() {
      int var1 = 1 + (this.E - 4) * 3 / 28;

      while (this.V.size() > var1) {
         this.V.removeLast();
      }
   }

   public void k(List<String> var1) {
      String var2 = var1.isEmpty() ? "" : (String)var1.getFirst();
      this.j(var1, this.I.equals(var2) ? this.c : this.c + 1);
   }

   public void j(List<String> var1, int var2) {
      String var3 = var1.isEmpty() ? "" : (String)var1.getFirst();
      if (!this.I.equals(var3) || this.c != var2) {
         if (this.L != null && this.L.opacity >= 0.00392156862745098) {
            this.V.addFirst(new ct.u(this.L));
         }

         this.O();
         this.I = var3;
         this.c = var2;
         this.b++;
         this.Z = this.b % 2 == 0 ? -1.0 : 1.0;
         this.A = this.Z * 0.1;
         this.q = 0.17 + 0.035 * Math.sin(this.b * 2.399);
         this.v = Math.min(7.5 + 0.6 * Math.sin(this.b * 1.7), this.F);
         this.J = this.B = 0.0;
         this.p = (boolean)0;
         this.L = null;
      }
   }

   public List<ct.z> m(
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
      this.W.set(var1, var3, var5);
      ArrayList var19 = new ArrayList(5);
      double var20 = 0.65 + this.E / 32.0 * 0.65;
      Iterator var22 = this.V.iterator();

      while (var22.hasNext()) {
         ct.u var23 = (ct.u)var22.next();
         var23.f += var16;
         if (var23.f >= var20) {
            var22.remove();
         } else {
            double var24 = o5.v(var23.f / var20);
            ct.z var26 = var23.a;
            double var27 = var26.opacity * (1.0 - var24);
            if (var27 >= 0.00392156862745098) {
               var19.add(this.R(var26, var27, var26.age, var23.f / var20));
            }
         }
      }

      if (this.I.isBlank()) {
         return List.copyOf(var19);
      }

      if (!this.p) {
         double var33 = Math.clamp(Math.abs(var8), 0.1, 8.0);
         double var35 = Math.clamp(Math.abs(var10), 0.1, 8.0);
         double var36 = 0.7 * Math.clamp(var33 / var35, 0.25, 4.0);
         double var28 = 0.75 + Math.clamp(var14, 0.0, 2.2) * 0.25;
         this.k.set(this.A * var33 * this.v, this.q * var35 * this.v, -this.v).rotate(var7);
         this.H.set(var7).rotateXYZ(0.07 * Math.sin(this.b * 1.7), this.Z * 0.16 * var28, this.Z * 0.1 * var28);
         double var30 = 0.098 * this.v / 0.75625 * var12;
         var30 = Math.min(var30, 1.3 * var36 * this.v / (0.01890625 * Math.max(1.0, var18.applyAsDouble(this.I))));
         var30 = Math.min(var30, 0.154 * this.v / 0.75625);
         this.d = new ct.z(this.I, this.k.x, this.k.y, this.k.z, var30, 1.0, this.H.x, this.H.y, this.H.z, this.H.w, 0.0, 0.0);
         this.p = (boolean)1;
      }

      this.J = Math.min(1.0, this.J + var16 / 0.28);
      this.B += var16;
      ct.z var34 = new ct.z(this.I, this.d.x, this.d.y, this.d.z, this.d.scale, o5.v(this.J), this.d.qx, this.d.qy, this.d.qz, this.d.qw, this.B, 0.0);
      this.L = var34;
      if (var34.opacity >= 0.00392156862745098) {
         var19.add(this.R(var34, var34.opacity, this.B, 0.0));
      }

      return List.copyOf(var19);
   }

   private ct.z R(ct.z var1, double var2, double var4, double var6) {
      return new ct.z(var1.text, this.W.x + var1.x, this.W.y + var1.y, this.W.z + var1.z, var1.scale, var2, var1.qx, var1.qy, var1.qz, var1.qw, var4, var6);
   }

   private static final class u {
      final ct.z a;
      double f;

      u(ct.z var1) {
         this.a = var1;
      }
   }

   public record z(String text, double x, double y, double z, double scale, double opacity, double qx, double qy, double qz, double qw, double age, double exit) {
   }
}
