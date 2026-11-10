package com.larpclient;

import java.util.ArrayList;
import java.util.List;
import java.util.SplittableRandom;
import java.util.function.ToIntFunction;
import net.minecraft.class_1937;
import net.minecraft.class_2246;
import net.minecraft.class_2338;
import net.minecraft.class_2350;
import net.minecraft.class_243;
import net.minecraft.class_2680;
import net.minecraft.class_310;
import net.minecraft.class_638;
import net.minecraft.class_746;
import net.minecraft.class_2338.class_2339;
import net.minecraft.class_2350.class_2353;

public final class e3 {
   static final int p = 40;
   static final int n = 400;
   static final int N = 20;
   private final s4 K;
   private final u5 Q;
   private final e3.y e;
   private List<g8.p> F = List.of();
   private int L;
   private int Y;
   private int d;
   private int j;
   private int x;
   private class_2338 T;
   private class_243 q;
   private class_243 R = class_243.field_1353;
   private boolean k;
   private boolean U;
   private boolean r;
   private String c = "";
   private boolean w = (boolean)1;
   private double J;
   private double M;
   private double Z;
   private double s;
   private double b;
   private double P;
   private double o;
   private boolean V;
   private final SplittableRandom I = new SplittableRandom();
   static final double C = 0.16;
   static final double i = 0.18;
   static final int u = 4;
   static final double v = 7.0;
   private int l;
   private int O;
   static final double E = 3.0;
   static final int f = 8;

   public e3(s4 var1, u5 var2, e3.y var3) {
      this.K = var1;
      this.Q = var2;
      this.e = var3;
   }

   public void X(List<g8.p> var1) {
      this.S(var1, true);
   }

   public void S(List<g8.p> var1, boolean var2) {
      this.i();
      this.F = var1 == null ? List.of() : List.copyOf(var1);
      this.w = var2;
      this.J = 0.0;
   }

   public void p() {
      if (!this.F.isEmpty()) {
         this.J = 0.16;
         this.M = this.I.nextDouble() * Math.PI * 2.0;
         this.Z = this.I.nextDouble() * Math.PI * 2.0;
         this.s = 0.35 + this.I.nextDouble() * 0.4;
         this.b = 0.35 + this.I.nextDouble() * 0.4;
         g8.p var1 = this.F.getLast();
         boolean var2 = c(var1.x(), var1.y(), var1.z());
         this.P = var2 ? 0.0 : (this.I.nextDouble() * 2.0 - 1.0) * 0.18;
         this.o = var2 ? 0.0 : (this.I.nextDouble() * 2.0 - 1.0) * 0.18;
      }
   }

   public boolean U() {
      return this.V;
   }

   private class_243 F(int var1, double var2) {
      g8.p var4 = this.F.get(var1);
      if (this.J == 0.0) {
         return new class_243(var4.x() + 0.5, var2, var4.z() + 0.5);
      }

      int var5 = var1 == this.F.size() - 1 && this.w ? 1 : 0;
      return new class_243(
         var4.x() + 0.5 + (var5 != 0 ? this.P : Math.sin(var1 * this.s + this.M) * this.J),
         var2,
         var4.z() + 0.5 + (var5 != 0 ? this.o : Math.sin(var1 * this.b + this.Z) * this.J)
      );
   }

   public boolean W() {
      return this.G() && !this.w;
   }

   public void i() {
      if (this.T != null) {
         bv.i(this.K);
      }

      this.F = List.of();
      this.L = this.Y = this.d = this.j = 0;
      this.T = null;
      this.q = null;
      this.R = class_243.field_1353;
      this.k = this.U = this.r = (boolean)0;
      this.c = "";
   }

   public boolean G() {
      return this.L < this.F.size();
   }

   public List<g8.p> M() {
      return this.L < this.F.size() ? this.F.subList(this.L, this.F.size()) : List.of();
   }

   public class_2338 y() {
      return this.T;
   }

   public static boolean c(int var0, int var1, int var2) {
      class_638 var3 = class_310.method_1551().field_1687;
      if (var3 == null) {
         return true;
      }

      for (class_2350 var5 : class_2353.field_11062) {
         class_2338 var6 = new class_2338(var0 + var5.method_10148(), var1, var2 + var5.method_10165());
         if (l(var3, var6) && l(var3, var6.method_10084()) && l(var3, var6.method_10074()) && l(var3, var6.method_10087(2))) {
            return true;
         }
      }

      return false;
   }

   private static boolean l(class_1937 var0, class_2338 var1) {
      return var0.method_22340(var1) && var0.method_8320(var1).method_26220(var0, var1).method_1110();
   }

   public int R() {
      return this.x;
   }

   public String D() {
      return this.c;
   }

   public class_243 g() {
      return this.R;
   }

   public boolean t() {
      return this.k;
   }

   public boolean V() {
      return this.U;
   }

   public boolean C() {
      return this.r;
   }

   public e3.l d(double var1, ToIntFunction<class_2680> var3, boolean var4) {
      return this.h(var1, var3, var4, 0);
   }

   private e3.l h(double var1, ToIntFunction<class_2680> var3, boolean var4, int var5) {
      this.R = class_243.field_1353;
      this.k = this.U = this.r = this.V = (boolean)0;
      class_310 var6 = class_310.method_1551();
      class_746 var7 = var6.field_1724;
      if (this.L >= this.F.size()) {
         this.X();
         return e3.l.L;
      }

      g8.p var8 = this.F.get(this.L);

      for (g8.v var10 : var8.digs()) {
         class_2338 var11 = new class_2338(var10.x(), var10.y(), var10.z());
         if (bv.P(var11)) {
            if (!var11.equals(this.T)) {
               if (this.T != null) {
                  this.x++;
               }

               this.T = var11;
               this.d = this.j = 0;
            }

            if (++this.d > 400) {
               return this.R("could not break " + var11.method_23854());
            }

            boolean var12 = bv.q(this.K, var11, var1, true, 12.0F, var3, () -> true);
            if (!var12 && ++this.j > 20) {
               return this.R("no face of " + var11.method_23854() + " in reach");
            }

            return e3.l.U;
         }
      }

      if (this.T != null) {
         this.x++;
         this.X();
      }

      class_243 var35 = var7.method_73189();
      if (!var7.method_24828() && !var7.method_5799()) {
         class_243 var37 = new class_243(var8.x() + 0.5, var35.field_1351, var8.z() + 0.5);
         if (this.J > 0.0) {
            while (
               this.L + 1 < this.F.size()
                  && this.F.get(this.L + 1).y() == this.F.get(this.L).y()
                  && !this.F.get(this.L + 1).digging()
                  && this.F(this.L + 1, var35.field_1351).method_1025(var35) <= this.F(this.L, var35.field_1351).method_1025(var35)
            ) {
               this.L++;
            }

            var37 = this.F(Math.min(this.L + 2, this.F.size() - 1), var35.field_1351);
         }

         if (var37.method_1020(var35).method_37268() > 0.04) {
            this.R = var37.method_1020(var35).method_18805(1.0, 0.0, 1.0);
         }

         this.q = var35;
         return e3.l.v;
      } else {
         int var36;
         for (var36 = this.C(var35); this.L < var36 && this.n(var35, this.L, var36); this.Y = 0) {
            this.L++;
         }

         var8 = this.F.get(this.L);
         int var38 = this.L == this.F.size() - 1 ? 1 : 0;
         int var39 = var38 != 0 && this.w ? 1 : 0;
         class_243 var13 = this.F(this.L, var35.field_1351);
         double var14 = Math.sqrt(var13.method_1020(var35).method_37268());
         int var16 = Math.abs(var35.field_1351 - var8.y()) < 0.6 ? 1 : 0;
         int var17 = var36 > this.L ? 1 : 0;
         int var18 = var7.method_18798().method_37268() < 1.0E-4 ? 1 : 0;
         if (var16 == 0 || !(var14 < (var39 != 0 ? 0.22 : (var17 != 0 ? 0.7 : 0.38))) && (var39 == 0 || var18 == 0 || !(var14 < 0.45))) {
            if (var35.field_1351 < var8.y() - 0.4 && var8.move() != g8.c.b && var14 < 0.5) {
               return this.R("fell below the route");
            }

            double var19 = var14;

            for (int var21 = this.L + 1; var21 < this.F.size(); var21++) {
               g8.p var22 = this.F.get(var21 - 1);
               g8.p var23 = this.F.get(var21);
               if (var23.digging()) {
                  break;
               }

               var19 += Math.hypot(var23.x() - var22.x(), var23.z() - var22.z());
            }

            class_243 var40 = var17 != 0 ? this.F(var36, var35.field_1351) : var13;
            class_243 var41 = var40.method_1020(var35).method_18805(1.0, 0.0, 1.0);
            double var42 = var41.method_1027() < 1.0E-6 ? 0.0 : var7.method_18798().method_18805(1.0, 0.0, 1.0).method_1026(var41.method_1029());
            int var25 = var8.move() == g8.c.b && var35.field_1351 < var8.y() - 0.4 ? 1 : 0;
            this.U = (boolean)(var39 != 0 && var19 < 0.9 && var8.move() != g8.c.C && var25 == 0 && c(var8.x(), var8.y(), var8.z()) ? 1 : 0);
            double var26 = !this.w ? var19 + 3.0 : (this.U ? var19 : var19 - 0.1);
            byte var28 = this.Q.V(var26, var42, cr.a(this.K));
            if (var25 != 0) {
               var28 = 1;
               this.k = (boolean)(var14 < 1.4 ? 1 : 0);
            }

            if (var7.method_5799() && var8.y() >= Math.floor(var35.field_1351)) {
               this.k = (boolean)1;
               if (var8.move() == g8.c.J || var14 > 0.2) {
                  var28 = (byte)(var28 == 0 && !(var14 > 0.2) ? 0 : 1);
               }
            }

            this.R = var28 != 0 ? var41 : class_243.field_1353;
            byte var29 = 0;

            for (int var30 = this.L; var30 < Math.min(this.F.size(), this.L + 3); var30++) {
               if (this.F.get(var30).digging()) {
                  var29 = 1;
               }
            }

            this.r = (boolean)(var4
                  && var28 != 0
                  && !this.U
                  && var29 == 0
                  && (var19 > 2.5 || !this.w)
                  && !var7.method_5799()
                  && var7.method_7344().method_7586() > 6
               ? 1
               : 0);
            this.V = (boolean)(var28 != 0 && var25 == 0 && var16 != 0 && var36 - this.L >= 4 && var19 > 7.0 && var29 == 0 ? 1 : 0);
            if (var28 != 0 && var5 == 0) {
               this.l++;
               if (var7.method_5624()) {
                  this.O++;
               }
            }

            double var43 = var7.method_5751() - 0.6;
            ArrayList var32 = new ArrayList(this.F.size() - this.L);

            for (int var33 = this.L; var33 < this.F.size(); var33++) {
               var32.add(new class_243(this.F.get(var33).x() + 0.5, this.F.get(var33).y() + var43, this.F.get(var33).z() + 0.5));
            }

            this.e.m(l(new class_243(var35.field_1352, var8.y() + var43, var35.field_1350), var32, 3.0));
            if (var28 != 0 && this.q != null && var35.method_1025(this.q) < 1.0E-4) {
               this.Y++;
            } else if (var28 != 0) {
               this.Y = 0;
            }

            this.q = var35;
            return this.Y > 40 ? this.R("movement blocked near " + class_2338.method_49638(var35).method_23854()) : e3.l.t;
         } else {
            this.L++;
            this.Y = 0;
            if (this.L >= this.F.size()) {
               return e3.l.L;
            } else {
               return var5 < 4 ? this.h(var1, var3, var4, var5 + 1) : e3.l.t;
            }
         }
      }
   }

   static class_243 l(class_243 var0, List<class_243> var1, double var2) {
      int var4 = 0;

      while (
         var4 + 1 < var1.size()
            && ((class_243)var1.get(var4)).method_1020(var0).method_1026(((class_243)var1.get(var4 + 1)).method_1020((class_243)var1.get(var4))) < 0.0
      ) {
         var4++;
      }

      class_243 var5 = var0;
      class_243 var6 = null;
      double var7 = var2;

      for (class_243 var10 : var1.subList(var4, var1.size())) {
         class_243 var11 = var10.method_1020(var5);
         double var12 = var11.method_1033();
         if (!(var12 < 1.0E-6)) {
            if (var12 >= var7) {
               return var5.method_1019(var11.method_1021(var7 / var12));
            }

            var7 -= var12;
            var6 = var11.method_1021(1.0 / var12);
            var5 = var10;
         }
      }

      return var6 == null ? var5 : var5.method_1019(var6.method_1021(var7));
   }

   private int C(class_243 var1) {
      class_638 var2 = class_310.method_1551().field_1687;
      g8.p var3 = this.F.get(this.L);
      if (!var3.digging() && (var3.move() == g8.c.S || var3.move() == g8.c.Q) && !(Math.abs(var1.field_1351 - var3.y()) > 0.6)) {
         int var4 = this.L;

         for (int var5 = this.L + 1; var5 < Math.min(this.F.size(), this.L + 1 + 8); var4 = var5++) {
            g8.p var6 = this.F.get(var5);
            if (var6.digging() || var6.y() != var3.y() || var6.move() != g8.c.S && var6.move() != g8.c.Q) {
               break;
            }

            class_243 var7 = this.F(var5, 0.0);
            if (!g(var2, var1, var7.field_1352, var7.field_1350, var3.y())) {
               break;
            }
         }

         return var4;
      } else {
         return this.L;
      }
   }

   private boolean n(class_243 var1, int var2, int var3) {
      g8.p var4 = this.F.get(var2);
      g8.p var5 = this.F.get(var3);
      double var6 = Math.hypot(var5.x() - var4.x(), var5.z() - var4.z());
      double var8 = Math.hypot(var5.x() + 0.5 - var1.field_1352, var5.z() + 0.5 - var1.field_1350);
      return var8 <= var6 + 0.05;
   }

   static boolean g(class_1937 var0, class_243 var1, double var2, double var4, int var6) {
      double var7 = var2 - var1.field_1352;
      double var9 = var4 - var1.field_1350;
      double var11 = Math.hypot(var7, var9);
      if (var11 < 1.0E-6) {
         return true;
      }

      double var13 = -var9 / var11 * 0.31;
      double var15 = var7 / var11 * 0.31;
      class_2339 var17 = new class_2339();

      for (double var18 = 0.0; var18 <= var11; var18 += 0.25) {
         double var20 = var1.field_1352 + var7 * var18 / var11;
         double var22 = var1.field_1350 + var9 * var18 / var11;

         for (int var24 = -1; var24 <= 1; var24++) {
            int var25 = (int)Math.floor(var20 + var13 * var24);
            int var26 = (int)Math.floor(var22 + var15 * var24);

            for (int var27 = 0; var27 <= 1; var27++) {
               class_2680 var28 = var0.method_8320(var17.method_10103(var25, var6 + var27, var26));
               if (!var28.method_26220(var0, var17).method_1110() || !var28.method_26227().method_15769()) {
                  return false;
               }
            }

            class_2680 var29 = var0.method_8320(var17.method_10103(var25, var6 - 1, var26));
            if (var29.method_26220(var0, var17).method_1110() || var29.method_27852(class_2246.field_10092)) {
               return false;
            }
         }
      }

      return true;
   }

   public double F() {
      return this.l == 0 ? 0.0 : (double)this.O / this.l;
   }

   private e3.l R(String var1) {
      this.X();
      this.c = var1;
      this.R = class_243.field_1353;
      return e3.l.s;
   }

   private void X() {
      if (this.T != null) {
         bv.i(this.K);
      }

      this.T = null;
      this.d = this.j = 0;
   }

   public enum l {
      t,
      U,
      v,
      L,
      s;
   }

   public interface y {
      void m(class_243 var1);
   }
}
