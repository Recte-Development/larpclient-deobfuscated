package com.larpclient;

import com.mojang.blaze3d.pipeline.BlendFunction;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.pipeline.RenderPipeline.Builder;
import com.mojang.blaze3d.pipeline.RenderPipeline.Snippet;
import com.mojang.blaze3d.platform.DepthTestFunction;
import java.util.List;
import java.util.Locale;
import net.minecraft.class_10017;
import net.minecraft.class_10799;
import net.minecraft.class_12075;
import net.minecraft.class_12247;
import net.minecraft.class_1921;
import net.minecraft.class_243;
import net.minecraft.class_2960;
import net.minecraft.class_310;
import net.minecraft.class_4587;
import net.minecraft.class_4588;
import org.joml.Matrix4f;
import org.joml.Quaternionfc;
import org.joml.Vector3f;

public final class f1 implements gq {
   public static final class_1921 I = class_1921.method_75940("larp_effects", class_12247.method_75927(f2.L).method_75937().method_75938());
   private static final class_1921[][] a = new class_1921[ti.i.values().length][3];
   private static final class_1921[][] q = new class_1921[ti.i.values().length][3];
   private static final class_1921 l;
   private static final double[] A = new double[60];
   private final double[] p = new double[18];
   private static final class_1921[] L = new class_1921[3];
   private static final class_1921 C = class_1921.method_75940(
      "larp_geometry_solid",
      class_12247.method_75927(
            RenderPipeline.builder(new Snippet[]{class_10799.field_56860})
               .withLocation(class_2960.method_60655("larp-client", "geometry_solid"))
               .withDepthTestFunction(DepthTestFunction.LEQUAL_DEPTH_TEST)
               .withDepthWrite(true)
               .withoutBlend()
               .withCull(false)
               .build()
         )
         .method_75938()
   );
   private boolean Q;
   private ti.d t = ti.d.G;
   private boolean W;
   private final cw X;
   private final class_4587 r;
   private final Vector3f d;
   private final Vector3f U;
   private final Matrix4f g;
   private final class_243 T;
   private static final double[] M;
   private static final double[] G;

   public void f(class_243 var1, double var2, float var4, float var5, int var6, int var7, boolean var8, float var9) {
      class_4588 var10 = this.X.N(jg.h(var7, var8));
      int var11 = Float.floatToRawIntBits(var4);
      int var12 = Float.floatToRawIntBits(var5);
      int var13 = Math.clamp(Math.round(var9 * 255.0F), 0, 255) << 24 | var6 & 16777215;

      for (int var14 = 0; var14 < 4; var14++) {
         float var15 = var14 != 0 && var14 != 3 ? 1.25F : -1.25F;
         float var16 = var14 < 2 ? -1.25F : 1.25F;
         var10.method_22918(
               this.g,
               (float)(var1.field_1352 + var15 * var2 - this.T.field_1352),
               (float)(var1.field_1351 + 0.02 - this.T.field_1351),
               (float)(var1.field_1350 + var16 * var2 - this.T.field_1350)
            )
            .method_39415(var13)
            .method_22913(var15, var16)
            .method_60796(var11 & 65535, var11 >>> 16)
            .method_22921(var12 & 65535, var12 >>> 16)
            .method_22914(0.0F, 1.0F, 0.0F);
      }
   }

   public void I(boolean var1) {
      this.Q = (boolean)(var1 && lj.x() ? 1 : 0);
   }

   public void s() {
      this.X.P();
   }

   @Override
   public void blend(ti.d var1) {
      this.t = var1;
   }

   @Override
   public void solid(boolean var1) {
      this.W = var1;
   }

   public static class_1921 y(ti.d var0) {
      return L[var0.ordinal()];
   }

   public static class_1921 D() {
      return C;
   }

   public static void L() {
      for (class_1921 var3 : a[ti.i.P.ordinal()]) {
         f2.a(var3.method_73243());
      }

      for (class_1921 var7 : L) {
         f2.a(var7.method_73243());
      }

      f2.a(C.method_73243());
      f2.a(l.method_73243());
   }

   static jv X(jv var0) {
      for (int var1 = 0; var1 < 3; var1++) {
         if (var0.pipeline() == q[0][var1].method_73243()) {
            return yf.w(var0, a[0][var1].method_73243());
         }
      }

      throw new IllegalArgumentException("Unknown soft particle pipeline");
   }

   public static void P() {
      for (class_1921 var3 : q[ti.i.P.ordinal()]) {
         f2.a(var3.method_73243());
      }
   }

   public f1(class_4587 var1, cw var2) {
      this(var1, var2, class_310.method_1551().field_1773.method_19418().method_71156(), class_310.method_1551().field_1773.method_19418().method_23767());
   }

   public f1(class_4587 var1, cw var2, class_12075 var3) {
      this(var1, var2, var3.field_63078, var3.field_63081);
   }

   private f1(class_4587 var1, cw var2, class_243 var3, Quaternionfc var4) {
      this.X = var2;
      this.r = var1;
      this.d = new Vector3f(1.0F, 0.0F, 0.0F).rotate(var4);
      this.U = new Vector3f(0.0F, 1.0F, 0.0F).rotate(var4);
      this.g = var1.method_23760().method_23761();
      this.T = var3;
   }

   public void V(class_10017 var1, double var2, double var4, double var6, int var8, int var9, boolean var10, double var11) {
      Matrix4f var13 = gv.Q(var1, this.r);
      if (var13 != null) {
         jb.H(this, var13, var2, var4, var6, var8, var9, var10, var11);
      }
   }

   public Matrix4f f(class_10017 var1, q4.r var2, double var3, double var5, double var7, double var9) {
      Matrix4f var11 = gv.Q(var1, this.r);
      if (var11 == null) {
         return null;
      }

      List var12 = q4.Z(var2);
      int var13 = !var12.isEmpty() && ij.G("ChinaHat") ? 1 : 0;
      if (var13 != 0) {
         Matrix4f var14 = new Matrix4f();

         for (q4.y var16 : var12) {
            ij.O(this, ij.g.get(var16.coin()), q4.Y(var11, var16, var3, var14));
         }
      }

      q4.L(this, var11, var2, var3, var5, (boolean)var13, var7, var9);
      return var11;
   }

   public void v(Matrix4f var1, double var2, double var4, double var6) {
      ut.U(this, var1, var2, var4, var6);
   }

   public Matrix4f x(class_10017 var1, double var2) {
      bl.a var4 = bl.q();
      if (var4 == null) {
         return null;
      }

      Matrix4f var5 = gv.Q(var1, this.r);
      if (var5 == null) {
         return null;
      }

      bl.S(this.X.N(bl.H()), var5, var4, var2);
      return var5;
   }

   public void i(class_10017 var1, double var2, int var4) {
      bl.a var5 = tb.x();
      if (var5 != null) {
         Matrix4f var6 = gv.Q(var1, this.r);
         if (var6 != null) {
            tb.t(this.X.N(tb.f()), var6, var5, var2, var4);
         }
      }
   }

   public void B(Matrix4f var1, double var2) {
      bl.a var4 = bl.q();
      if (var4 != null) {
         bl.z(this.X.N(bl.H()), var1, var4, var2);
      }
   }

   public void X(g7 var1, Matrix4f var2, double var3, double var5, double var7, int var9, int var10, double var11) {
      bl.a var13 = bl.q();
      if (var13 != null) {
         Vector3f var14 = new Matrix4f(this.g).invert().mul(var2).transformPosition(0.0F, -0.5F, var13.centerZ() * (float)var3, new Vector3f());
         var1.g(var11, this.T.field_1352 + var14.x, this.T.field_1351 + var14.y + 0.24F * var3, this.T.field_1350 + var14.z, 0.0, 1.0, 0.0, var3 * var5, var7);
         this.j(var1, var9, var10, 1.0);
         c5.L("China Hat fire particles", var1.H());
      }
   }

   public void j(g7 var1, int var2, int var3, double var4) {
      class_1921 var6 = g7.p();
      var1.F((var2x, var4x, var6x, var8, var10, var12, var14) -> this.i(var6, var2x, var4x, var6x, var8, var10, var12, var14), var2, var3, var4);
   }

   private void i(class_1921 var1, double var2, double var4, double var6, double var8, double var10, double var12, int var14) {
      class_4588 var15 = this.X.N(var1);
      double var16 = Math.cos(var12);
      double var18 = Math.sin(var12);

      for (int var20 = 0; var20 < 4; var20++) {
         double var21 = var20 != 0 && var20 != 3 ? 1.0 : -1.0;
         double var23 = var20 < 2 ? -1.0 : 1.0;
         double var25 = (var21 * var16 - var23 * var10 * var18) * var8;
         double var27 = (var21 * var18 + var23 * var10 * var16) * var8;
         var15.method_22918(
               this.g,
               (float)(var2 - this.T.field_1352 + this.d.x * var25 + this.U.x * var27),
               (float)(var4 - this.T.field_1351 + this.d.y * var25 + this.U.y * var27),
               (float)(var6 - this.T.field_1350 + this.d.z * var25 + this.U.z * var27)
            )
            .method_22913((float)((var21 + 1.0) * 0.5), (float)((var23 + 1.0) * 0.5))
            .method_39415(var14);
      }
   }

   public void F(class_1921 var1, Matrix4f var2, float[] var3, int[] var4) {
      h(this.X, var1, var2, var3, var4);
   }

   public void k(class_10017 var1, gv.y var2, int var3, double var4) {
      gv.x(var1, this.r, this.X, var2, var3, var4, false);
   }

   @Override
   public Vector3f viewer(Matrix4f var1, Vector3f var2) {
      return new Matrix4f(var1).invert().transformPosition(var2.set(0.0F, 0.0F, 0.0F));
   }

   @Override
   public void modelVertex(Matrix4f var1, float var2, float var3, float var4, int var5) {
      this.X.N(this.W ? C : L[this.t.ordinal()]).method_22918(var1, var2, var3, var4).method_39415(var5);
   }

   public void p(class_1921 var1, class_243 var2, Quaternionfc var3, float var4, float[] var5, int[] var6) {
      Matrix4f var7 = new Matrix4f(this.g)
         .translate((float)(var2.field_1352 - this.T.field_1352), (float)(var2.field_1351 - this.T.field_1351), (float)(var2.field_1350 - this.T.field_1350))
         .rotate(var3)
         .scale(var4);
      h(this.X, var1, var7, var5, var6);
   }

   public static void h(cw var0, class_1921 var1, Matrix4f var2, float[] var3, int[] var4) {
      class_4588 var5 = var0.N(var1);

      for (int var6 = 0; var6 < var4.length; var6++) {
         int var7 = var6 * 5;
         var5.method_22918(var2, var3[var7], var3[var7 + 1], var3[var7 + 2]).method_22913(var3[var7 + 3], var3[var7 + 4]).method_39415(var4[var6]);
      }
   }

   @Override
   public void vertex(double var1, double var3, double var5, int var7) {
      this.X
         .N(L[this.t.ordinal()])
         .method_22918(this.g, (float)(var1 - this.T.field_1352), (float)(var3 - this.T.field_1351), (float)(var5 - this.T.field_1350))
         .method_39415(this.t == ti.d.j ? R(var7) : var7);
   }

   private void M(class_4588 var1, double var2, double var4, double var6, int var8) {
      var1.method_22918(this.g, (float)(var2 - this.T.field_1352), (float)(var4 - this.T.field_1351), (float)(var6 - this.T.field_1350)).method_39415(var8);
   }

   @Override
   public void ribbon(class_243 var1, class_243 var2, double var3, int var5, int var6) {
      this.g(var1.field_1352, var1.field_1351, var1.field_1350, var2.field_1352, var2.field_1351, var2.field_1350, var3, var3, var5, var6);
   }

   public void g(double var1, double var3, double var5, double var7, double var9, double var11, double var13, double var15, int var17, int var18) {
      double var19 = var7 - var1;
      double var21 = var9 - var3;
      double var23 = var11 - var5;
      if (!(var19 * var19 + var21 * var21 + var23 * var23 < 1.0E-10)) {
         double var25 = this.T.field_1352 - var1;
         double var27 = this.T.field_1351 - var3;
         double var29 = this.T.field_1350 - var5;
         double var31 = var21 * var29 - var23 * var27;
         double var33 = var23 * var25 - var19 * var29;
         double var35 = var19 * var27 - var21 * var25;
         double var37 = Math.sqrt(var31 * var31 + var33 * var33 + var35 * var35);
         if (var37 < 1.0E-8) {
            var31 = this.d.x;
            var33 = this.d.y;
            var35 = this.d.z;
            var37 = 1.0;
         }

         var31 /= var37 * 2.0;
         var33 /= var37 * 2.0;
         var35 /= var37 * 2.0;
         this.vertex(var1 + var31 * var13, var3 + var33 * var13, var5 + var35 * var13, var17);
         this.vertex(var7 + var31 * var15, var9 + var33 * var15, var11 + var35 * var15, var18);
         this.vertex(var7 - var31 * var15, var9 - var33 * var15, var11 - var35 * var15, var18);
         this.vertex(var1 - var31 * var13, var3 - var33 * var13, var5 - var35 * var13, var17);
      }
   }

   public void I(double var1, double var3, double var5, double var7, double var9, double var11, double var13, double var15, int var17, int var18) {
      this.g(var1, var3, var5, var7, var9, var11, var13 * 2.5, var15 * 2.5, ei.R(var17, 0.07), ei.R(var18, 0.07));
      this.g(var1, var3, var5, var7, var9, var11, var13 * 1.25, var15 * 1.25, ei.R(var17, 0.3), ei.R(var18, 0.3));
      this.g(
         var1,
         var3,
         var5,
         var7,
         var9,
         var11,
         var13 * 0.23,
         var15 * 0.23,
         ei.W(var17, var17 & 0xFF000000 | 16777215, 0.48),
         ei.W(var18, var18 & 0xFF000000 | 16777215, 0.48)
      );
   }

   public void G(qd.x var1, boolean var2) {
      for (int var3 = 0; var3 < (var2 ? 3 : 1); var3++) {
         double var4 = !var2 ? 1.0 : (var3 == 0 ? 2.5 : (var3 == 1 ? 1.25 : 0.23));

         for (int var6 = 1; var6 < var1.w; var6++) {
            class_243 var7 = var1.Q[var6 - 1];
            class_243 var8 = var1.Q[var6];
            if (!(var7.method_1025(this.T) > var1.C) && !(var7.method_1025(var8) < 1.0E-10)) {
               class_243 var9 = var1.S[var6 - 1];
               class_243 var10 = var1.S[var6];
               double var11 = var9.field_1352 * var4;
               double var13 = var9.field_1351 * var4;
               double var15 = var9.field_1350 * var4;
               double var17 = var10.field_1352 * var4;
               double var19 = var10.field_1351 * var4;
               double var21 = var10.field_1350 * var4;
               int var23 = h(var1.M[var6 - 1], var3, var2);
               int var24 = h(var1.M[var6], var3, var2);
               this.vertex(var7.field_1352 + var11, var7.field_1351 + var13, var7.field_1350 + var15, var23);
               this.vertex(var8.field_1352 + var17, var8.field_1351 + var19, var8.field_1350 + var21, var24);
               this.vertex(var8.field_1352 - var17, var8.field_1351 - var19, var8.field_1350 - var21, var24);
               this.vertex(var7.field_1352 - var11, var7.field_1351 - var13, var7.field_1350 - var15, var23);
            }
         }
      }
   }

   public void d(qd.x var1, double var2) {
      var2 = Math.clamp(var2, 0.0, 0.95);

      for (int var4 = 1; var4 < var1.w; var4++) {
         class_243 var5 = var1.Q[var4 - 1];
         class_243 var6 = var1.Q[var4];
         if (!(var5.method_1025(this.T) > var1.C) && !(var5.method_1025(var6) < 1.0E-10)) {
            class_243 var7 = var1.S[var4 - 1];
            class_243 var8 = var1.S[var4];
            int var9 = var1.M[var4 - 1];
            int var10 = var1.M[var4];
            if (var9 >>> 24 != 0 || var10 >>> 24 != 0) {
               int var11 = var9 & 16777215;
               int var12 = var10 & 16777215;
               if (var2 > 0.0) {
                  this.vertex(
                     var5.field_1352 + var7.field_1352 * var2, var5.field_1351 + var7.field_1351 * var2, var5.field_1350 + var7.field_1350 * var2, var9
                  );
                  this.vertex(
                     var6.field_1352 + var8.field_1352 * var2, var6.field_1351 + var8.field_1351 * var2, var6.field_1350 + var8.field_1350 * var2, var10
                  );
                  this.vertex(
                     var6.field_1352 - var8.field_1352 * var2, var6.field_1351 - var8.field_1351 * var2, var6.field_1350 - var8.field_1350 * var2, var10
                  );
                  this.vertex(
                     var5.field_1352 - var7.field_1352 * var2, var5.field_1351 - var7.field_1351 * var2, var5.field_1350 - var7.field_1350 * var2, var9
                  );
               }

               for (byte var13 = -1; var13 <= 1; var13 += 2) {
                  double var14 = var2 * var13;
                  this.vertex(
                     var5.field_1352 + var7.field_1352 * var14, var5.field_1351 + var7.field_1351 * var14, var5.field_1350 + var7.field_1350 * var14, var9
                  );
                  this.vertex(
                     var6.field_1352 + var8.field_1352 * var14, var6.field_1351 + var8.field_1351 * var14, var6.field_1350 + var8.field_1350 * var14, var10
                  );
                  this.vertex(
                     var6.field_1352 + var8.field_1352 * var13, var6.field_1351 + var8.field_1351 * var13, var6.field_1350 + var8.field_1350 * var13, var12
                  );
                  this.vertex(
                     var5.field_1352 + var7.field_1352 * var13, var5.field_1351 + var7.field_1351 * var13, var5.field_1350 + var7.field_1350 * var13, var11
                  );
               }
            }
         }
      }
   }

   private static int h(int var0, int var1, boolean var2) {
      if (!var2) {
         return var0;
      } else {
         return var1 < 2 ? ei.R(var0, var1 == 0 ? 0.07 : 0.3) : ei.W(var0, var0 & 0xFF000000 | 16777215, 0.48);
      }
   }

   public void J(class_10017 var1, class_243 var2, double var3, double var5, double var7, int var9, int var10, boolean var11) {
      Matrix4f var12 = gv.Q(var1, this.r);
      if (var12 != null) {
         ge.l(this, var12, var3, var5, var7, var9, var10, var11);
      } else {
         this.y(var2, var3, var5, var9, var10, var11);
      }
   }

   public void y(class_243 var1, double var2, double var4, int var6, int var7, boolean var8) {
      for (int var9 = 0; var9 < 64; var9++) {
         this.vertex(var1.field_1352, var1.field_1351 + var4, var1.field_1350, var6);
         this.vertex(var1.field_1352 + M[var9 + 1] * var2, var1.field_1351, var1.field_1350 + G[var9 + 1] * var2, var7);
         this.vertex(var1.field_1352 + M[var9] * var2, var1.field_1351, var1.field_1350 + G[var9] * var2, var7);
         this.vertex(var1.field_1352, var1.field_1351 + var4, var1.field_1350, var6);
      }

      if (var8) {
         this.r(var1, var2, 0.009, ei.W(var7, var7 & 0xFF000000 | 16777215, 0.4));
      }
   }

   public void r(class_243 var1, double var2, double var4, int var6) {
      this.f(var1.field_1352, var1.field_1351, var1.field_1350, var2, var4, var6);
   }

   public void f(double var1, double var3, double var5, double var7, double var9, int var11) {
      this.E(var1, var3, var5, var7, var9, var11, 0.0, 1.0, 0.0);
   }

   public void E(double var1, double var3, double var5, double var7, double var9, int var11, double var12, double var14, double var16) {
      double var18 = Math.sqrt(var12 * var12 + var14 * var14 + var16 * var16);
      if (var18 < 1.0E-8) {
         var12 = 0.0;
         var14 = 1.0;
         var16 = 0.0;
      } else {
         var12 /= var18;
         var14 /= var18;
         var16 /= var18;
      }

      double var20 = Math.abs(var14) > 0.95 ? 1.0 : -var16;
      double var22 = 0.0;
      double var24 = Math.abs(var14) > 0.95 ? 0.0 : var12;
      double var26 = var20 * var12 + var22 * var14 + var24 * var16;
      var20 -= var12 * var26;
      var22 -= var14 * var26;
      var24 -= var16 * var26;
      double var28 = Math.sqrt(var20 * var20 + var22 * var22 + var24 * var24);
      var20 /= var28;
      var22 /= var28;
      var24 /= var28;
      double var30 = var22 * var16 - var24 * var14;
      double var32 = var24 * var12 - var20 * var16;
      double var34 = var20 * var14 - var22 * var12;
      class_4588 var36 = this.X.N(L[this.t.ordinal()]);
      int var37 = this.t == ti.d.j ? 1 : 0;

      for (int var38 = 0; var38 < 4; var38++) {
         double var39 = Math.max(0.0, var7 + (var38 / 2.0 - 1.0) * var9);
         double var41 = Math.max(0.0, var7 + ((var38 + 1) / 2.0 - 1.0) * var9);
         double var43 = var38 == 0 ? 0.0 : (var38 == 2 ? 1.0 : 0.22);
         double var45 = var38 == 3 ? 0.0 : (var38 == 1 ? 1.0 : 0.22);
         int var47 = ei.R(var11, var43);
         int var48 = ei.R(var11, var45);
         if (var37 != 0) {
            var47 = R(var47);
            var48 = R(var48);
         }

         for (int var49 = 0; var49 < 64; var49++) {
            double var50 = var20 * M[var49] + var30 * G[var49];
            double var52 = var22 * M[var49] + var32 * G[var49];
            double var54 = var24 * M[var49] + var34 * G[var49];
            double var56 = var20 * M[var49 + 1] + var30 * G[var49 + 1];
            double var58 = var22 * M[var49 + 1] + var32 * G[var49 + 1];
            double var60 = var24 * M[var49 + 1] + var34 * G[var49 + 1];
            this.M(var36, var1 + var50 * var39, var3 + var52 * var39, var5 + var54 * var39, var47);
            this.M(var36, var1 + var56 * var39, var3 + var58 * var39, var5 + var60 * var39, var47);
            this.M(var36, var1 + var56 * var41, var3 + var58 * var41, var5 + var60 * var41, var48);
            this.M(var36, var1 + var50 * var41, var3 + var52 * var41, var5 + var54 * var41, var48);
         }
      }
   }

   public void X(double var1, double var3, double var5, double var7, double var9, double var11, double var13, int var15) {
      e.V(this.p, var7, var9, var11, var13);

      for (int var16 = 0; var16 < e.W(); var16++) {
         int var17 = e.a(this.p, var16, var15);

         for (int var18 = 0; var18 < 4; var18++) {
            int var19 = e.d(var16, var18) * 3;
            this.vertex(var1 + this.p[var19], var3 + this.p[var19 + 1], var5 + this.p[var19 + 2], var17);
         }
      }
   }

   public void U(double var1, double var3, double var5, double var7, double var9, int var11, int var12, boolean var13) {
      int var14 = var13 ? var12 * 2 : var12;

      for (int var15 = 0; var15 < var14; var15++) {
         double var16 = var9 + var15 * Math.PI * 2.0 / var14;
         double var18 = var9 + (var15 + 1) * Math.PI * 2.0 / var14;
         double var20 = var7 * (var13 && var15 % 2 == 1 ? 0.4 : 1.0);
         double var22 = var7 * (var13 && (var15 + 1) % 2 == 1 ? 0.4 : 1.0);
         this.vertex(var1, var3, var5, var11);
         this.vertex(
            var1 + this.d.x * Math.cos(var16) * var20 + this.U.x * Math.sin(var16) * var20,
            var3 + this.d.y * Math.cos(var16) * var20 + this.U.y * Math.sin(var16) * var20,
            var5 + this.d.z * Math.cos(var16) * var20 + this.U.z * Math.sin(var16) * var20,
            var11
         );
         this.vertex(
            var1 + this.d.x * Math.cos(var18) * var22 + this.U.x * Math.sin(var18) * var22,
            var3 + this.d.y * Math.cos(var18) * var22 + this.U.y * Math.sin(var18) * var22,
            var5 + this.d.z * Math.cos(var18) * var22 + this.U.z * Math.sin(var18) * var22,
            var11
         );
         this.vertex(var1, var3, var5, var11);
      }
   }

   private static int R(int var0) {
      double var1 = (var0 >>> 24) / 255.0;
      return var0 & 0xFF000000 | (int)((var0 >> 16 & 0xFF) * var1) << 16 | (int)((var0 >> 8 & 0xFF) * var1) << 8 | (int)((var0 & 0xFF) * var1);
   }

   public void Q(class_1921 var1, double var2, double var4, double var6, double var8, double var10, int var12) {
      class_4588 var13 = this.X.N(var1);
      double var14 = Math.cos(var10);
      double var16 = Math.sin(var10);

      for (int var18 = 0; var18 < 4; var18++) {
         double var19 = var18 != 0 && var18 != 3 ? 1.0 : -1.0;
         double var21 = var18 < 2 ? -1.0 : 1.0;
         double var23 = (var19 * var14 - var21 * var16) * var8;
         double var25 = (var19 * var16 + var21 * var14) * var8;
         var13.method_22918(
               this.g,
               (float)(var2 - this.T.field_1352 + this.d.x * var23 + this.U.x * var25),
               (float)(var4 - this.T.field_1351 + this.d.y * var23 + this.U.y * var25),
               (float)(var6 - this.T.field_1350 + this.d.z * var23 + this.U.z * var25)
            )
            .method_22913((float)((var19 + 1.0) * 0.5), (float)((var21 + 1.0) * 0.5))
            .method_39415(var12);
      }
   }

   public void V(double var1, double var3, double var5, double var7, double var9, int var11) {
      this.U(var1, var3, var5, var7, var9, var11, gd.h.L, 0.0, 0.0, 0.0);
   }

   public void U(double var1, double var3, double var5, double var7, double var9, int var11, gd.h var12, double var13, double var15, double var17) {
      this.v(var1, var3, var5, var7, var9, var11, var12, var13, var15, var17, ti.i.P);
   }

   public void p(double var1, double var3, double var5, double var7, double var9, double var11, double var13, double var15, double var17, int var19, gd.h var20) {
      int var21 = W(var20);
      class_4588 var22 = this.X.N(a[ti.i.P.ordinal()][this.t.ordinal()]);

      for (int var23 = 0; var23 < 4; var23++) {
         double var24 = var23 != 0 && var23 != 3 ? 1.0 : -1.0;
         double var26 = var23 < 2 ? -1.0 : 1.0;
         var22.method_22918(
               this.g,
               (float)(var1 - this.T.field_1352 + var7 * var24 + var13 * var26),
               (float)(var3 - this.T.field_1351 + var9 * var24 + var15 * var26),
               (float)(var5 - this.T.field_1350 + var11 * var24 + var17 * var26)
            )
            .method_22913((float)((var21 + (var24 + 1.0) * 0.5) / 9.0), (float)((var26 + 1.0) * 0.5))
            .method_39415(var19);
      }
   }

   private static int W(gd.h var0) {
      return switch (var0) {
         case f -> 0;
         case x -> 1;
         case P -> 2;
         case L, Y -> 3;
         case t -> 4;
         case n, S -> 5;
         case e -> 6;
         case g -> 7;
         default -> 8;
      };
   }

   public void v(double var1, double var3, double var5, double var7, double var9, int var11, gd.h var12, double var13, double var15, double var17, ti.i var19) {
      int var20 = W(var12);
      double var21 = 1.0;
      if (var12 == gd.h.E || var12 == gd.h.P) {
         var9 = Math.atan2(var13 * this.U.x + var15 * this.U.y + var17 * this.U.z, var13 * this.d.x + var15 * this.d.y + var17 * this.d.z);
         var21 = var12 == gd.h.E ? 3.0 : 1.6;
      }

      class_4588 var23 = this.X.N((this.Q ? q : a)[var19.ordinal()][this.t.ordinal()]);
      double var24 = Math.cos(var9);
      double var26 = Math.sin(var9);

      for (int var28 = 0; var28 < 4; var28++) {
         double var29 = var28 != 0 && var28 != 3 ? 1.0 : -1.0;
         double var31 = var28 < 2 ? -1.0 : 1.0;
         double var33 = (var29 * var21 * var24 - var31 * var26) * var7;
         double var35 = (var29 * var21 * var26 + var31 * var24) * var7;
         var23.method_22918(
               this.g,
               (float)(var1 - this.T.field_1352 + this.d.x * var33 + this.U.x * var35),
               (float)(var3 - this.T.field_1351 + this.d.y * var33 + this.U.y * var35),
               (float)(var5 - this.T.field_1350 + this.d.z * var33 + this.U.z * var35)
            )
            .method_22913((float)((var20 + (var29 + 1.0) * 0.5) / 9.0), (float)((var31 + 1.0) * 0.5))
            .method_39415(var11);
      }
   }

   public void X(double var1, double var3, double var5, double var7, double var9, double var11, double var13, double var15, int var17) {
      y.P(A, var7, var9, var11, var13, var15);
      class_4588 var18 = this.X.N(l);

      for (int var19 = 0; var19 < 18; var19++) {
         int var20 = y.H(A, var19, var17);

         for (int var21 = 0; var21 < 4; var21++) {
            int var22 = y.x(var19, var21) * 3;
            var18.method_22918(
                  this.g,
                  (float)(var1 - this.T.field_1352 + A[var22]),
                  (float)(var3 - this.T.field_1351 + A[var22 + 1]),
                  (float)(var5 - this.T.field_1350 + A[var22 + 2])
               )
               .method_22913((float)y.i(var19, var21), (float)y.g(var19, var21))
               .method_39415(var20);
         }
      }
   }

   public void X(class_243 var1, double var2, double var4, double var6, double var8, int var10, int var11, double var12) {
      int var14 = Math.clamp((int)Math.ceil(Math.abs(var8 - var6) * 12.0), 4, 96);

      for (int var15 = 0; var15 < var14; var15++) {
         double var16 = (double)var15 / var14;
         double var18 = (double)(var15 + 1) / var14;
         double var20 = var6 + (var8 - var6) * var16;
         double var22 = var6 + (var8 - var6) * var18;
         int var24 = ei.W(var10, var11, var16);
         int var25 = ei.W(var10, var11, var18);
         double var26 = var1.field_1351 + Math.sin(var20 * 2.0) * var12;
         double var28 = var1.field_1351 + Math.sin(var22 * 2.0) * var12;
         this.vertex(var1.field_1352 + Math.cos(var20) * var2, var26, var1.field_1350 + Math.sin(var20) * var2, var24);
         this.vertex(var1.field_1352 + Math.cos(var22) * var2, var28, var1.field_1350 + Math.sin(var22) * var2, var25);
         this.vertex(var1.field_1352 + Math.cos(var22) * (var2 + var4), var28, var1.field_1350 + Math.sin(var22) * (var2 + var4), ei.R(var25, 0.15));
         this.vertex(var1.field_1352 + Math.cos(var20) * (var2 + var4), var26, var1.field_1350 + Math.sin(var20) * (var2 + var4), ei.R(var24, 0.15));
      }
   }

   public void j(class_243 var1, double var2, double var4, int var6) {
      for (int var7 = 0; var7 < 40; var7++) {
         double var8 = var7 * Math.PI / 20.0;
         double var10 = (var7 + 1) * Math.PI / 20.0;
         this.vertex(var1.field_1352 + Math.cos(var8) * var2, var1.field_1351, var1.field_1350 + Math.sin(var8) * var2, var6);
         this.vertex(var1.field_1352 + Math.cos(var10) * var2, var1.field_1351, var1.field_1350 + Math.sin(var10) * var2, var6);
         this.vertex(var1.field_1352 + Math.cos(var10) * (var2 + var4), var1.field_1351, var1.field_1350 + Math.sin(var10) * (var2 + var4), var6);
         this.vertex(var1.field_1352 + Math.cos(var8) * (var2 + var4), var1.field_1351, var1.field_1350 + Math.sin(var8) * (var2 + var4), var6);
      }
   }

   static {
      RenderPipeline var0 = RenderPipeline.builder(new Snippet[]{class_10799.field_56864})
         .withLocation(class_2960.method_60655("larp-client", "cash"))
         .withFragmentShader(class_2960.method_60655("larp-client", "particle/sprite"))
         .withDepthTestFunction(DepthTestFunction.LEQUAL_DEPTH_TEST)
         .withDepthWrite(false)
         .withBlend(BlendFunction.TRANSLUCENT)
         .withCull(true)
         .build();
      l = class_1921.method_75940(
         "larp_cash",
         class_12247.method_75927(var0)
            .method_75934("Sampler0", class_2960.method_60655("larp-client", "textures/particles/cash.png"))
            .method_75937()
            .method_75938()
      );

      for (ti.d var4 : ti.d.values()) {
         BlendFunction var5 = switch (var4) {
            case G -> BlendFunction.TRANSLUCENT;
            case l -> BlendFunction.LIGHTNING;
            case j -> BlendFunction.TRANSLUCENT_PREMULTIPLIED_ALPHA;
         };
         Builder var6 = RenderPipeline.builder(new Snippet[]{class_10799.field_56864})
            .withLocation(class_2960.method_60655("larp-client", "sprites_" + var4.name().toLowerCase(Locale.ROOT)))
            .withFragmentShader(class_2960.method_60655("larp-client", "particle/sprite"))
            .withDepthTestFunction(DepthTestFunction.LEQUAL_DEPTH_TEST)
            .withDepthWrite(false)
            .withBlend(var5)
            .withCull(false);
         if (var4 == ti.d.j) {
            var6.withShaderDefine("PREMULTIPLIED");
         }

         RenderPipeline var7 = var6.build();
         lj.o(var6);
         RenderPipeline var8 = var6.withLocation(class_2960.method_60655("larp-client", "soft_sprites_" + var4.name().toLowerCase(Locale.ROOT))).build();

         for (ti.i var12 : ti.i.values()) {
            a[var12.ordinal()][var4.ordinal()] = class_1921.method_75940(
               "larp_sprites_" + var12 + "_" + var4,
               class_12247.method_75927(var7)
                  .method_75934("Sampler0", class_2960.method_60655("larp-client", "textures/particles/" + var12.E))
                  .method_75937()
                  .method_75938()
            );
         }

         for (ti.i var17 : ti.i.values()) {
            q[var17.ordinal()][var4.ordinal()] = class_1921.method_75940(
               "larp_soft_sprites_" + var17 + "_" + var4,
               class_12247.method_75927(var8)
                  .method_75934("Sampler0", class_2960.method_60655("larp-client", "textures/particles/" + var17.E))
                  .method_75937()
                  .method_75938()
            );
         }

         L[var4.ordinal()] = class_1921.method_75940(
            "larp_geometry_" + var4,
            class_12247.method_75927(
                  RenderPipeline.builder(new Snippet[]{class_10799.field_56860})
                     .withLocation(class_2960.method_60655("larp-client", "geometry_" + var4.name().toLowerCase(Locale.ROOT)))
                     .withBlend(var5)
                     .withCull(false)
                     .build()
               )
               .method_75937()
               .method_75938()
         );
      }

      M = new double[65];
      G = new double[65];

      for (int var13 = 0; var13 <= 64; var13++) {
         M[var13] = Math.cos(var13 * Math.PI / 32.0);
         G[var13] = Math.sin(var13 * Math.PI / 32.0);
      }
   }
}
