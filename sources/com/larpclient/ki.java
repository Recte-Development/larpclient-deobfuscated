package com.larpclient;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.renderpearl.api.pipeline.BlendFunction;
import com.mojang.renderpearl.api.pipeline.ColorTargetState;
import com.mojang.renderpearl.api.pipeline.CompareOp;
import com.mojang.renderpearl.api.pipeline.DepthStencilState;
import com.mojang.renderpearl.api.pipeline.RenderPipeline;
import com.mojang.renderpearl.api.pipeline.RenderPipeline.Builder;
import com.mojang.renderpearl.api.pipeline.RenderPipeline.Snippet;
import java.util.List;
import java.util.Locale;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.rendertype.PreparedRenderType;
import net.minecraft.client.renderer.rendertype.RenderSetup;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.resources.Identifier;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;
import org.joml.Quaternionfc;
import org.joml.Vector3f;

public final class ki implements s {
   public static final RenderType n = RenderType.create("larp_effects", RenderSetup.builder(ea.U).sortOnUpload().createRenderSetup());
   private static final RenderType[][] S = new RenderType[pf.t.values().length][3];
   private static final RenderType[][] z = new RenderType[pf.t.values().length][3];
   private static final RenderType J;
   private static final double[] G = new double[60];
   private final double[] F = new double[18];
   private static final RenderType[] Q = new RenderType[3];
   private static final RenderType Y = RenderType.create(
      "larp_geometry_solid",
      RenderSetup.builder(
            RenderPipeline.builder(new Snippet[]{RenderPipelines.DEBUG_FILLED_SNIPPET})
               .withLocation(Identifier.fromNamespaceAndPath("larp-client", "geometry_solid"))
               .withDepthStencilState(new DepthStencilState(CompareOp.GREATER_THAN_OR_EQUAL, true))
               .withColorTargetState(ColorTargetState.DEFAULT)
               .withCull(false)
               .build()
         )
         .createRenderSetup()
   );
   private boolean C;
   private pf.x E = pf.x.S;
   private boolean m;
   private final xh h;
   private final PoseStack l;
   private final Vector3f R;
   private final Vector3f u;
   private final Matrix4f P;
   private final Vec3 N;
   private static final double[] a;
   private static final double[] g;

   public void O(Vec3 var1, double var2, float var4, float var5, int var6, int var7, boolean var8, float var9) {
      VertexConsumer var10 = this.h.U(t_.a(var7, var8));
      int var11 = Float.floatToRawIntBits(var4);
      int var12 = Float.floatToRawIntBits(var5);
      int var13 = Math.clamp(Math.round(var9 * 255.0F), 0, 255) << 24 | var6 & 16777215;

      for (int var14 = 0; var14 < 4; var14++) {
         float var15 = var14 != 0 && var14 != 3 ? 1.25F : -1.25F;
         float var16 = var14 < 2 ? -1.25F : 1.25F;
         var10.addVertex(this.P, (float)(var1.x + var15 * var2 - this.N.x), (float)(var1.y + 0.02 - this.N.y), (float)(var1.z + var16 * var2 - this.N.z))
            .setColor(var13)
            .setUv(var15, var16)
            .setUv1(var11 & 65535, var11 >>> 16)
            .setUv2(var12 & 65535, var12 >>> 16)
            .setNormal(0.0F, 1.0F, 0.0F);
      }
   }

   public void v(boolean var1) {
      this.C = (boolean)(var1 && _v.g() ? 1 : 0);
   }

   public void Z() {
      this.h.b();
   }

   @Override
   public void blend(pf.x var1) {
      this.E = var1;
   }

   @Override
   public void solid(boolean var1) {
      this.m = var1;
   }

   public static RenderType x(pf.x var0) {
      return Q[var0.ordinal()];
   }

   public static RenderType M() {
      return Y;
   }

   public static void R() {
      for (RenderType var3 : S[pf.t.G.ordinal()]) {
         ea.z(var3.pipeline());
      }

      for (RenderType var7 : Q) {
         ea.z(var7.pipeline());
      }

      ea.z(Y.pipeline());
      ea.z(J.pipeline());
   }

   static PreparedRenderType c(PreparedRenderType var0) {
      for (int var1 = 0; var1 < 3; var1++) {
         if (var0.pipeline() == z[0][var1].pipeline()) {
            return hj.I(var0, S[0][var1].pipeline());
         }
      }

      throw new IllegalArgumentException("Unknown soft particle pipeline");
   }

   public static void w() {
      for (RenderType var3 : z[pf.t.G.ordinal()]) {
         ea.z(var3.pipeline());
      }
   }

   public ki(PoseStack var1, xh var2) {
      this(var1, var2, Minecraft.getInstance().gameRenderer.mainCamera().position(), Minecraft.getInstance().gameRenderer.mainCamera().rotation());
   }

   public ki(PoseStack var1, xh var2, CameraRenderState var3) {
      this(var1, var2, var3.pos, var3.orientation);
   }

   private ki(PoseStack var1, xh var2, Vec3 var3, Quaternionfc var4) {
      this.h = var2;
      this.l = var1;
      this.R = new Vector3f(1.0F, 0.0F, 0.0F).rotate(var4);
      this.u = new Vector3f(0.0F, 1.0F, 0.0F).rotate(var4);
      this.P = var1.last().pose();
      this.N = var3;
   }

   public void Q(EntityRenderState var1, double var2, double var4, double var6, int var8, int var9, boolean var10) {
      Matrix4f var11 = tj.Z(var1, this.l);
      if (var11 != null) {
         xc.g(this, var11, var2, var4, var6, var8, var9, var10);
      }
   }

   public void d(EntityRenderState var1, _6.l var2, double var3, double var5) {
      Matrix4f var7 = tj.Z(var1, this.l);
      if (var7 != null) {
         List var8 = _6.V(var2);
         int var9 = !var8.isEmpty() && pr.I("ChinaHat") ? 1 : 0;
         _6.R(this, var7, var2, var3, var5, var9 == 0);
         if (var9 != 0) {
            Matrix4f var10 = new Matrix4f();

            for (_6.y var12 : var8) {
               pr.D(this, pr.x.get(var12.coin()), _6.E(var7, var12, var3, var10));
            }
         }
      }
   }

   public Matrix4f s(EntityRenderState var1, double var2) {
      ml.w var4 = ml.u();
      if (var4 == null) {
         return null;
      }

      Matrix4f var5 = tj.Z(var1, this.l);
      if (var5 == null) {
         return null;
      }

      ml.T(this.h.U(ml.s()), var5, var4, var2);
      return var5;
   }

   public void t(Matrix4f var1, double var2) {
      ml.w var4 = ml.u();
      if (var4 != null) {
         ml.A(this.h.U(ml.s()), var1, var4, var2);
      }
   }

   public void q(db var1, Matrix4f var2, double var3, double var5, double var7, int var9, int var10, double var11) {
      ml.w var13 = ml.u();
      if (var13 != null) {
         Vector3f var14 = new Matrix4f(this.P).invert().mul(var2).transformPosition(0.0F, -0.5F, var13.centerZ() * (float)var3, new Vector3f());
         var1.S(var11, this.N.x + var14.x, this.N.y + var14.y + 0.24F * var3, this.N.z + var14.z, 0.0, 1.0, 0.0, var3 * var5, var7);
         this.Z(var1, var9, var10, 1.0);
         kx.s("China Hat fire particles", var1.J());
      }
   }

   public void Z(db var1, int var2, int var3, double var4) {
      RenderType var6 = db.Q();
      var1.z((var2x, var4x, var6x, var8, var10, var12, var14) -> this.n(var6, var2x, var4x, var6x, var8, var10, var12, var14), var2, var3, var4);
   }

   private void n(RenderType var1, double var2, double var4, double var6, double var8, double var10, double var12, int var14) {
      VertexConsumer var15 = this.h.U(var1);
      double var16 = Math.cos(var12);
      double var18 = Math.sin(var12);

      for (int var20 = 0; var20 < 4; var20++) {
         double var21 = var20 != 0 && var20 != 3 ? 1.0 : -1.0;
         double var23 = var20 < 2 ? -1.0 : 1.0;
         double var25 = (var21 * var16 - var23 * var10 * var18) * var8;
         double var27 = (var21 * var18 + var23 * var10 * var16) * var8;
         var15.addVertex(
               this.P,
               (float)(var2 - this.N.x + this.R.x * var25 + this.u.x * var27),
               (float)(var4 - this.N.y + this.R.y * var25 + this.u.y * var27),
               (float)(var6 - this.N.z + this.R.z * var25 + this.u.z * var27)
            )
            .setUv((float)((var21 + 1.0) * 0.5), (float)((var23 + 1.0) * 0.5))
            .setColor(var14);
      }
   }

   public void j(RenderType var1, Matrix4f var2, float[] var3, int[] var4) {
      n(this.h, var1, var2, var3, var4);
   }

   public void Z(EntityRenderState var1, tj.b var2, int var3, double var4) {
      tj.u(var1, this.l, this.h, var2, var3, var4, false);
   }

   @Override
   public void modelVertex(Matrix4f var1, float var2, float var3, float var4, int var5) {
      this.h.U(this.m ? Y : Q[this.E.ordinal()]).addVertex(var1, var2, var3, var4).setColor(var5);
   }

   public void m(RenderType var1, Vec3 var2, Quaternionfc var3, float var4, float[] var5, int[] var6) {
      Matrix4f var7 = new Matrix4f(this.P)
         .translate((float)(var2.x - this.N.x), (float)(var2.y - this.N.y), (float)(var2.z - this.N.z))
         .rotate(var3)
         .scale(var4);
      n(this.h, var1, var7, var5, var6);
   }

   public static void n(xh var0, RenderType var1, Matrix4f var2, float[] var3, int[] var4) {
      VertexConsumer var5 = var0.U(var1);

      for (int var6 = 0; var6 < var4.length; var6++) {
         int var7 = var6 * 5;
         var5.addVertex(var2, var3[var7], var3[var7 + 1], var3[var7 + 2]).setUv(var3[var7 + 3], var3[var7 + 4]).setColor(var4[var6]);
      }
   }

   @Override
   public void vertex(double var1, double var3, double var5, int var7) {
      this.h
         .U(Q[this.E.ordinal()])
         .addVertex(this.P, (float)(var1 - this.N.x), (float)(var3 - this.N.y), (float)(var5 - this.N.z))
         .setColor(this.E == pf.x.F ? n(var7) : var7);
   }

   private void m(VertexConsumer var1, double var2, double var4, double var6, int var8) {
      var1.addVertex(this.P, (float)(var2 - this.N.x), (float)(var4 - this.N.y), (float)(var6 - this.N.z)).setColor(var8);
   }

   @Override
   public void ribbon(Vec3 var1, Vec3 var2, double var3, int var5, int var6) {
      this.b(var1.x, var1.y, var1.z, var2.x, var2.y, var2.z, var3, var3, var5, var6);
   }

   public void b(double var1, double var3, double var5, double var7, double var9, double var11, double var13, double var15, int var17, int var18) {
      double var19 = var7 - var1;
      double var21 = var9 - var3;
      double var23 = var11 - var5;
      if (!(var19 * var19 + var21 * var21 + var23 * var23 < 1.0E-10)) {
         double var25 = this.N.x - var1;
         double var27 = this.N.y - var3;
         double var29 = this.N.z - var5;
         double var31 = var21 * var29 - var23 * var27;
         double var33 = var23 * var25 - var19 * var29;
         double var35 = var19 * var27 - var21 * var25;
         double var37 = Math.sqrt(var31 * var31 + var33 * var33 + var35 * var35);
         if (var37 < 1.0E-8) {
            var31 = this.R.x;
            var33 = this.R.y;
            var35 = this.R.z;
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

   public void R(double var1, double var3, double var5, double var7, double var9, double var11, double var13, double var15, int var17, int var18) {
      this.b(var1, var3, var5, var7, var9, var11, var13 * 2.5, var15 * 2.5, _3.U(var17, 0.07), _3.U(var18, 0.07));
      this.b(var1, var3, var5, var7, var9, var11, var13 * 1.25, var15 * 1.25, _3.U(var17, 0.3), _3.U(var18, 0.3));
      this.b(
         var1,
         var3,
         var5,
         var7,
         var9,
         var11,
         var13 * 0.23,
         var15 * 0.23,
         _3.D(var17, var17 & 0xFF000000 | 16777215, 0.48),
         _3.D(var18, var18 & 0xFF000000 | 16777215, 0.48)
      );
   }

   public void M(__.l var1, boolean var2) {
      for (int var3 = 0; var3 < (var2 ? 3 : 1); var3++) {
         double var4 = !var2 ? 1.0 : (var3 == 0 ? 2.5 : (var3 == 1 ? 1.25 : 0.23));

         for (int var6 = 1; var6 < var1.k; var6++) {
            Vec3 var7 = var1.v[var6 - 1];
            Vec3 var8 = var1.v[var6];
            if (!(var7.distanceToSqr(this.N) > var1.t) && !(var7.distanceToSqr(var8) < 1.0E-10)) {
               Vec3 var9 = var1.i[var6 - 1];
               Vec3 var10 = var1.i[var6];
               double var11 = var9.x * var4;
               double var13 = var9.y * var4;
               double var15 = var9.z * var4;
               double var17 = var10.x * var4;
               double var19 = var10.y * var4;
               double var21 = var10.z * var4;
               int var23 = e(var1.w[var6 - 1], var3, var2);
               int var24 = e(var1.w[var6], var3, var2);
               this.vertex(var7.x + var11, var7.y + var13, var7.z + var15, var23);
               this.vertex(var8.x + var17, var8.y + var19, var8.z + var21, var24);
               this.vertex(var8.x - var17, var8.y - var19, var8.z - var21, var24);
               this.vertex(var7.x - var11, var7.y - var13, var7.z - var15, var23);
            }
         }
      }
   }

   public void f(__.l var1, double var2) {
      var2 = Math.clamp(var2, 0.0, 0.95);

      for (int var4 = 1; var4 < var1.k; var4++) {
         Vec3 var5 = var1.v[var4 - 1];
         Vec3 var6 = var1.v[var4];
         if (!(var5.distanceToSqr(this.N) > var1.t) && !(var5.distanceToSqr(var6) < 1.0E-10)) {
            Vec3 var7 = var1.i[var4 - 1];
            Vec3 var8 = var1.i[var4];
            int var9 = var1.w[var4 - 1];
            int var10 = var1.w[var4];
            if (var9 >>> 24 != 0 || var10 >>> 24 != 0) {
               int var11 = var9 & 16777215;
               int var12 = var10 & 16777215;
               if (var2 > 0.0) {
                  this.vertex(var5.x + var7.x * var2, var5.y + var7.y * var2, var5.z + var7.z * var2, var9);
                  this.vertex(var6.x + var8.x * var2, var6.y + var8.y * var2, var6.z + var8.z * var2, var10);
                  this.vertex(var6.x - var8.x * var2, var6.y - var8.y * var2, var6.z - var8.z * var2, var10);
                  this.vertex(var5.x - var7.x * var2, var5.y - var7.y * var2, var5.z - var7.z * var2, var9);
               }

               for (byte var13 = -1; var13 <= 1; var13 += 2) {
                  double var14 = var2 * var13;
                  this.vertex(var5.x + var7.x * var14, var5.y + var7.y * var14, var5.z + var7.z * var14, var9);
                  this.vertex(var6.x + var8.x * var14, var6.y + var8.y * var14, var6.z + var8.z * var14, var10);
                  this.vertex(var6.x + var8.x * var13, var6.y + var8.y * var13, var6.z + var8.z * var13, var12);
                  this.vertex(var5.x + var7.x * var13, var5.y + var7.y * var13, var5.z + var7.z * var13, var11);
               }
            }
         }
      }
   }

   private static int e(int var0, int var1, boolean var2) {
      if (!var2) {
         return var0;
      } else {
         return var1 < 2 ? _3.U(var0, var1 == 0 ? 0.07 : 0.3) : _3.D(var0, var0 & 0xFF000000 | 16777215, 0.48);
      }
   }

   public void p(EntityRenderState var1, Vec3 var2, double var3, double var5, double var7, int var9, int var10, boolean var11) {
      Matrix4f var12 = tj.Z(var1, this.l);
      if (var12 != null) {
         ub.F(this, var12, var3, var5, var7, var9, var10, var11);
      } else {
         this.d(var2, var3, var5, var9, var10, var11);
      }
   }

   public void d(Vec3 var1, double var2, double var4, int var6, int var7, boolean var8) {
      for (int var9 = 0; var9 < 64; var9++) {
         this.vertex(var1.x, var1.y + var4, var1.z, var6);
         this.vertex(var1.x + a[var9 + 1] * var2, var1.y, var1.z + g[var9 + 1] * var2, var7);
         this.vertex(var1.x + a[var9] * var2, var1.y, var1.z + g[var9] * var2, var7);
         this.vertex(var1.x, var1.y + var4, var1.z, var6);
      }

      if (var8) {
         this.r(var1, var2, 0.009, _3.D(var7, var7 & 0xFF000000 | 16777215, 0.4));
      }
   }

   public void r(Vec3 var1, double var2, double var4, int var6) {
      this.H(var1.x, var1.y, var1.z, var2, var4, var6);
   }

   public void H(double var1, double var3, double var5, double var7, double var9, int var11) {
      this.H(var1, var3, var5, var7, var9, var11, 0.0, 1.0, 0.0);
   }

   public void H(double var1, double var3, double var5, double var7, double var9, int var11, double var12, double var14, double var16) {
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
      VertexConsumer var36 = this.h.U(Q[this.E.ordinal()]);
      int var37 = this.E == pf.x.F ? 1 : 0;

      for (int var38 = 0; var38 < 4; var38++) {
         double var39 = Math.max(0.0, var7 + (var38 / 2.0 - 1.0) * var9);
         double var41 = Math.max(0.0, var7 + ((var38 + 1) / 2.0 - 1.0) * var9);
         double var43 = var38 == 0 ? 0.0 : (var38 == 2 ? 1.0 : 0.22);
         double var45 = var38 == 3 ? 0.0 : (var38 == 1 ? 1.0 : 0.22);
         int var47 = _3.U(var11, var43);
         int var48 = _3.U(var11, var45);
         if (var37 != 0) {
            var47 = n(var47);
            var48 = n(var48);
         }

         for (int var49 = 0; var49 < 64; var49++) {
            double var50 = var20 * a[var49] + var30 * g[var49];
            double var52 = var22 * a[var49] + var32 * g[var49];
            double var54 = var24 * a[var49] + var34 * g[var49];
            double var56 = var20 * a[var49 + 1] + var30 * g[var49 + 1];
            double var58 = var22 * a[var49 + 1] + var32 * g[var49 + 1];
            double var60 = var24 * a[var49 + 1] + var34 * g[var49 + 1];
            this.m(var36, var1 + var50 * var39, var3 + var52 * var39, var5 + var54 * var39, var47);
            this.m(var36, var1 + var56 * var39, var3 + var58 * var39, var5 + var60 * var39, var47);
            this.m(var36, var1 + var56 * var41, var3 + var58 * var41, var5 + var60 * var41, var48);
            this.m(var36, var1 + var50 * var41, var3 + var52 * var41, var5 + var54 * var41, var48);
         }
      }
   }

   public void k(double var1, double var3, double var5, double var7, double var9, double var11, double var13, int var15) {
      co.D(this.F, var7, var9, var11, var13);

      for (int var16 = 0; var16 < co.m(); var16++) {
         int var17 = co.G(this.F, var16, var15);

         for (int var18 = 0; var18 < 4; var18++) {
            int var19 = co.O(var16, var18) * 3;
            this.vertex(var1 + this.F[var19], var3 + this.F[var19 + 1], var5 + this.F[var19 + 2], var17);
         }
      }
   }

   public void s(double var1, double var3, double var5, double var7, double var9, int var11, int var12, boolean var13) {
      int var14 = var13 ? var12 * 2 : var12;

      for (int var15 = 0; var15 < var14; var15++) {
         double var16 = var9 + var15 * Math.PI * 2.0 / var14;
         double var18 = var9 + (var15 + 1) * Math.PI * 2.0 / var14;
         double var20 = var7 * (var13 && var15 % 2 == 1 ? 0.4 : 1.0);
         double var22 = var7 * (var13 && (var15 + 1) % 2 == 1 ? 0.4 : 1.0);
         this.vertex(var1, var3, var5, var11);
         this.vertex(
            var1 + this.R.x * Math.cos(var16) * var20 + this.u.x * Math.sin(var16) * var20,
            var3 + this.R.y * Math.cos(var16) * var20 + this.u.y * Math.sin(var16) * var20,
            var5 + this.R.z * Math.cos(var16) * var20 + this.u.z * Math.sin(var16) * var20,
            var11
         );
         this.vertex(
            var1 + this.R.x * Math.cos(var18) * var22 + this.u.x * Math.sin(var18) * var22,
            var3 + this.R.y * Math.cos(var18) * var22 + this.u.y * Math.sin(var18) * var22,
            var5 + this.R.z * Math.cos(var18) * var22 + this.u.z * Math.sin(var18) * var22,
            var11
         );
         this.vertex(var1, var3, var5, var11);
      }
   }

   private static int n(int var0) {
      double var1 = (var0 >>> 24) / 255.0;
      return var0 & 0xFF000000 | (int)((var0 >> 16 & 0xFF) * var1) << 16 | (int)((var0 >> 8 & 0xFF) * var1) << 8 | (int)((var0 & 0xFF) * var1);
   }

   public void j(RenderType var1, double var2, double var4, double var6, double var8, double var10, int var12) {
      VertexConsumer var13 = this.h.U(var1);
      double var14 = Math.cos(var10);
      double var16 = Math.sin(var10);

      for (int var18 = 0; var18 < 4; var18++) {
         double var19 = var18 != 0 && var18 != 3 ? 1.0 : -1.0;
         double var21 = var18 < 2 ? -1.0 : 1.0;
         double var23 = (var19 * var14 - var21 * var16) * var8;
         double var25 = (var19 * var16 + var21 * var14) * var8;
         var13.addVertex(
               this.P,
               (float)(var2 - this.N.x + this.R.x * var23 + this.u.x * var25),
               (float)(var4 - this.N.y + this.R.y * var23 + this.u.y * var25),
               (float)(var6 - this.N.z + this.R.z * var23 + this.u.z * var25)
            )
            .setUv((float)((var19 + 1.0) * 0.5), (float)((var21 + 1.0) * 0.5))
            .setColor(var12);
      }
   }

   public void t(double var1, double var3, double var5, double var7, double var9, int var11) {
      this.W(var1, var3, var5, var7, var9, var11, cg.u.h, 0.0, 0.0, 0.0);
   }

   public void W(double var1, double var3, double var5, double var7, double var9, int var11, cg.u var12, double var13, double var15, double var17) {
      this.L(var1, var3, var5, var7, var9, var11, var12, var13, var15, var17, pf.t.G);
   }

   public void G(double var1, double var3, double var5, double var7, double var9, double var11, double var13, double var15, double var17, int var19, cg.u var20) {
      int var21 = I(var20);
      VertexConsumer var22 = this.h.U(S[pf.t.G.ordinal()][this.E.ordinal()]);

      for (int var23 = 0; var23 < 4; var23++) {
         double var24 = var23 != 0 && var23 != 3 ? 1.0 : -1.0;
         double var26 = var23 < 2 ? -1.0 : 1.0;
         var22.addVertex(
               this.P,
               (float)(var1 - this.N.x + var7 * var24 + var13 * var26),
               (float)(var3 - this.N.y + var9 * var24 + var15 * var26),
               (float)(var5 - this.N.z + var11 * var24 + var17 * var26)
            )
            .setUv((float)((var21 + (var24 + 1.0) * 0.5) / 9.0), (float)((var26 + 1.0) * 0.5))
            .setColor(var19);
      }
   }

   private static int I(cg.u var0) {
      return switch (var0) {
         case a -> 0;
         case n -> 1;
         case M -> 2;
         case h, Z -> 3;
         case u -> 4;
         case z, w -> 5;
         case x -> 6;
         case T -> 7;
         default -> 8;
      };
   }

   public void L(double var1, double var3, double var5, double var7, double var9, int var11, cg.u var12, double var13, double var15, double var17, pf.t var19) {
      int var20 = I(var12);
      double var21 = 1.0;
      if (var12 == cg.u.B || var12 == cg.u.M) {
         var9 = Math.atan2(var13 * this.u.x + var15 * this.u.y + var17 * this.u.z, var13 * this.R.x + var15 * this.R.y + var17 * this.R.z);
         var21 = var12 == cg.u.B ? 3.0 : 1.6;
      }

      VertexConsumer var23 = this.h.U((this.C ? z : S)[var19.ordinal()][this.E.ordinal()]);
      double var24 = Math.cos(var9);
      double var26 = Math.sin(var9);

      for (int var28 = 0; var28 < 4; var28++) {
         double var29 = var28 != 0 && var28 != 3 ? 1.0 : -1.0;
         double var31 = var28 < 2 ? -1.0 : 1.0;
         double var33 = (var29 * var21 * var24 - var31 * var26) * var7;
         double var35 = (var29 * var21 * var26 + var31 * var24) * var7;
         var23.addVertex(
               this.P,
               (float)(var1 - this.N.x + this.R.x * var33 + this.u.x * var35),
               (float)(var3 - this.N.y + this.R.y * var33 + this.u.y * var35),
               (float)(var5 - this.N.z + this.R.z * var33 + this.u.z * var35)
            )
            .setUv((float)((var20 + (var29 + 1.0) * 0.5) / 9.0), (float)((var31 + 1.0) * 0.5))
            .setColor(var11);
      }
   }

   public void g(double var1, double var3, double var5, double var7, double var9, double var11, double var13, double var15, int var17) {
      h5.W(G, var7, var9, var11, var13, var15);
      VertexConsumer var18 = this.h.U(J);

      for (int var19 = 0; var19 < 18; var19++) {
         int var20 = h5.n(G, var19, var17);

         for (int var21 = 0; var21 < 4; var21++) {
            int var22 = h5.J(var19, var21) * 3;
            var18.addVertex(this.P, (float)(var1 - this.N.x + G[var22]), (float)(var3 - this.N.y + G[var22 + 1]), (float)(var5 - this.N.z + G[var22 + 2]))
               .setUv((float)h5.t(var19, var21), (float)h5.n(var19, var21))
               .setColor(var20);
         }
      }
   }

   public void Z(Vec3 var1, double var2, double var4, double var6, double var8, int var10, int var11, double var12) {
      int var14 = Math.clamp((int)Math.ceil(Math.abs(var8 - var6) * 12.0), 4, 96);

      for (int var15 = 0; var15 < var14; var15++) {
         double var16 = (double)var15 / var14;
         double var18 = (double)(var15 + 1) / var14;
         double var20 = var6 + (var8 - var6) * var16;
         double var22 = var6 + (var8 - var6) * var18;
         int var24 = _3.D(var10, var11, var16);
         int var25 = _3.D(var10, var11, var18);
         double var26 = var1.y + Math.sin(var20 * 2.0) * var12;
         double var28 = var1.y + Math.sin(var22 * 2.0) * var12;
         this.vertex(var1.x + Math.cos(var20) * var2, var26, var1.z + Math.sin(var20) * var2, var24);
         this.vertex(var1.x + Math.cos(var22) * var2, var28, var1.z + Math.sin(var22) * var2, var25);
         this.vertex(var1.x + Math.cos(var22) * (var2 + var4), var28, var1.z + Math.sin(var22) * (var2 + var4), _3.U(var25, 0.15));
         this.vertex(var1.x + Math.cos(var20) * (var2 + var4), var26, var1.z + Math.sin(var20) * (var2 + var4), _3.U(var24, 0.15));
      }
   }

   public void E(Vec3 var1, double var2, double var4, int var6) {
      for (int var7 = 0; var7 < 40; var7++) {
         double var8 = var7 * Math.PI / 20.0;
         double var10 = (var7 + 1) * Math.PI / 20.0;
         this.vertex(var1.x + Math.cos(var8) * var2, var1.y, var1.z + Math.sin(var8) * var2, var6);
         this.vertex(var1.x + Math.cos(var10) * var2, var1.y, var1.z + Math.sin(var10) * var2, var6);
         this.vertex(var1.x + Math.cos(var10) * (var2 + var4), var1.y, var1.z + Math.sin(var10) * (var2 + var4), var6);
         this.vertex(var1.x + Math.cos(var8) * (var2 + var4), var1.y, var1.z + Math.sin(var8) * (var2 + var4), var6);
      }
   }

   static {
      RenderPipeline var0 = RenderPipeline.builder(new Snippet[]{RenderPipelines.GUI_TEXTURED_SNIPPET})
         .withLocation(Identifier.fromNamespaceAndPath("larp-client", "cash"))
         .withFragmentShader(Identifier.fromNamespaceAndPath("larp-client", "particle/sprite"))
         .withDepthStencilState(new DepthStencilState(CompareOp.GREATER_THAN_OR_EQUAL, false))
         .withColorTargetState(new ColorTargetState(BlendFunction.TRANSLUCENT))
         .withCull(true)
         .build();
      J = RenderType.create(
         "larp_cash",
         RenderSetup.builder(var0)
            .withTexture("Sampler0", Identifier.fromNamespaceAndPath("larp-client", "textures/particles/cash.png"))
            .sortOnUpload()
            .createRenderSetup()
      );

      for (pf.x var4 : pf.x.values()) {
         BlendFunction var5 = switch (var4) {
            case S -> BlendFunction.TRANSLUCENT;
            case E -> BlendFunction.LIGHTNING;
            case F -> BlendFunction.TRANSLUCENT_PREMULTIPLIED_ALPHA;
         };
         Builder var6 = RenderPipeline.builder(new Snippet[]{RenderPipelines.GUI_TEXTURED_SNIPPET})
            .withLocation(Identifier.fromNamespaceAndPath("larp-client", "sprites_" + var4.name().toLowerCase(Locale.ROOT)))
            .withFragmentShader(Identifier.fromNamespaceAndPath("larp-client", "particle/sprite"))
            .withDepthStencilState(new DepthStencilState(CompareOp.GREATER_THAN_OR_EQUAL, false))
            .withColorTargetState(new ColorTargetState(var5))
            .withCull(false);
         if (var4 == pf.x.F) {
            var6.withShaderDefine("PREMULTIPLIED");
         }

         RenderPipeline var7 = var6.build();
         _v.Z(var6);
         RenderPipeline var8 = var6.withLocation(Identifier.fromNamespaceAndPath("larp-client", "soft_sprites_" + var4.name().toLowerCase(Locale.ROOT)))
            .build();

         for (pf.t var12 : pf.t.values()) {
            S[var12.ordinal()][var4.ordinal()] = RenderType.create(
               "larp_sprites_" + var12 + "_" + var4,
               RenderSetup.builder(var7)
                  .withTexture("Sampler0", Identifier.fromNamespaceAndPath("larp-client", "textures/particles/" + var12.o))
                  .sortOnUpload()
                  .createRenderSetup()
            );
         }

         for (pf.t var17 : pf.t.values()) {
            z[var17.ordinal()][var4.ordinal()] = RenderType.create(
               "larp_soft_sprites_" + var17 + "_" + var4,
               RenderSetup.builder(var8)
                  .withTexture("Sampler0", Identifier.fromNamespaceAndPath("larp-client", "textures/particles/" + var17.o))
                  .sortOnUpload()
                  .createRenderSetup()
            );
         }

         Q[var4.ordinal()] = RenderType.create(
            "larp_geometry_" + var4,
            RenderSetup.builder(
                  RenderPipeline.builder(new Snippet[]{RenderPipelines.DEBUG_FILLED_SNIPPET})
                     .withLocation(Identifier.fromNamespaceAndPath("larp-client", "geometry_" + var4.name().toLowerCase(Locale.ROOT)))
                     .withColorTargetState(new ColorTargetState(var5))
                     .build()
               )
               .sortOnUpload()
               .createRenderSetup()
         );
      }

      a = new double[65];
      g = new double[65];

      for (int var13 = 0; var13 <= 64; var13++) {
         a[var13] = Math.cos(var13 * Math.PI / 32.0);
         g[var13] = Math.sin(var13 * Math.PI / 32.0);
      }
   }
}
