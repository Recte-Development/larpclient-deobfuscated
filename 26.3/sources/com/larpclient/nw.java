package com.larpclient;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.ToDoubleFunction;
import org.joml.Quaterniond;
import org.joml.Quaterniondc;
import org.joml.Vector3d;

public final class nw {
   public static final int u = 32;
   public static final double L = 0.01890625;
   private final ArrayDeque<nw.l> T = new ArrayDeque<>();
   private final Quaterniond P = new Quaterniond();
   private final Vector3d U = new Vector3d();
   private final Vector3d t = new Vector3d();
   private String n = "";
   private nw.s R;
   private int K = 16;
   private int f;
   private int x = -1;
   private boolean O;
   private double S;
   private double a;
   private double E = 1.0;
   private double q;
   private double J;
   private double m = 8.0;
   private nw.s l;
   private double M = 8.0;

   public void G() {
      this.O = (boolean)0;
      this.S = this.a = 0.0;
      this.n = "";
      this.x = -1;
      this.f = 0;
      this.R = null;
      this.l = null;
      this.M = 8.0;
      this.T.clear();
   }

   public void j(int var1) {
      this.K = Math.clamp(var1, 4, 32);
      this.Z();
   }

   public void t(double var1) {
      this.M = Math.clamp(var1, 0.6, 8.0);
   }

   private void Z() {
      int var1 = 1 + (this.K - 4) * 3 / 28;

      while (this.T.size() > var1) {
         this.T.removeLast();
      }
   }

   public void L(List<String> var1) {
      String var2 = var1.isEmpty() ? "" : (String)var1.getFirst();
      this.x(var1, this.n.equals(var2) ? this.x : this.x + 1);
   }

   public void x(List<String> var1, int var2) {
      String var3 = var1.isEmpty() ? "" : (String)var1.getFirst();
      if (!this.n.equals(var3) || this.x != var2) {
         if (this.R != null && this.R.opacity >= 0.00392156862745098) {
            this.T.addFirst(new nw.l(this.R));
         }

         this.Z();
         this.n = var3;
         this.x = var2;
         this.f++;
         this.E = this.f % 2 == 0 ? -1.0 : 1.0;
         this.q = this.E * 0.1;
         this.J = 0.17 + 0.035 * Math.sin(this.f * 2.399);
         this.m = Math.min(7.5 + 0.6 * Math.sin(this.f * 1.7), this.M);
         this.S = this.a = 0.0;
         this.O = (boolean)0;
         this.R = null;
      }
   }

   public List<nw.s> N(
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
      this.U.set(var1, var3, var5);
      ArrayList var19 = new ArrayList(5);
      double var20 = 0.65 + this.K / 32.0 * 0.65;
      Iterator var22 = this.T.iterator();

      while (var22.hasNext()) {
         nw.l var23 = (nw.l)var22.next();
         var23.H += var16;
         if (var23.H >= var20) {
            var22.remove();
         } else {
            double var24 = bs.M(var23.H / var20);
            nw.s var26 = var23.X;
            double var27 = var26.opacity * (1.0 - var24);
            if (var27 >= 0.00392156862745098) {
               var19.add(this.b(var26, var27, var26.age, var23.H / var20));
            }
         }
      }

      if (this.n.isBlank()) {
         return List.copyOf(var19);
      }

      if (!this.O) {
         double var33 = Math.clamp(Math.abs(var8), 0.1, 8.0);
         double var35 = Math.clamp(Math.abs(var10), 0.1, 8.0);
         double var36 = 0.7 * Math.clamp(var33 / var35, 0.25, 4.0);
         double var28 = 0.75 + Math.clamp(var14, 0.0, 2.2) * 0.25;
         this.t.set(this.q * var33 * this.m, this.J * var35 * this.m, -this.m).rotate(var7);
         this.P.set(var7).rotateXYZ(0.07 * Math.sin(this.f * 1.7), this.E * 0.16 * var28, this.E * 0.1 * var28);
         double var30 = 0.098 * this.m / 0.75625 * var12;
         var30 = Math.min(var30, 1.3 * var36 * this.m / (0.01890625 * Math.max(1.0, var18.applyAsDouble(this.n))));
         var30 = Math.min(var30, 0.154 * this.m / 0.75625);
         this.l = new nw.s(this.n, this.t.x, this.t.y, this.t.z, var30, 1.0, this.P.x, this.P.y, this.P.z, this.P.w, 0.0, 0.0);
         this.O = (boolean)1;
      }

      this.S = Math.min(1.0, this.S + var16 / 0.28);
      this.a += var16;
      nw.s var34 = new nw.s(this.n, this.l.x, this.l.y, this.l.z, this.l.scale, bs.M(this.S), this.l.qx, this.l.qy, this.l.qz, this.l.qw, this.a, 0.0);
      this.R = var34;
      if (var34.opacity >= 0.00392156862745098) {
         var19.add(this.b(var34, var34.opacity, this.a, 0.0));
      }

      return List.copyOf(var19);
   }

   private nw.s b(nw.s var1, double var2, double var4, double var6) {
      return new nw.s(var1.text, this.U.x + var1.x, this.U.y + var1.y, this.U.z + var1.z, var1.scale, var2, var1.qx, var1.qy, var1.qz, var1.qw, var4, var6);
   }

   private static final class l {
      final nw.s X;
      double H;

      l(nw.s var1) {
         this.X = var1;
      }
   }

   public record s(String text, double x, double y, double z, double scale, double opacity, double qx, double qy, double qz, double qw, double age, double exit) {
   }
}
