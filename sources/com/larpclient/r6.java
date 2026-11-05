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

public final class r6 implements _c {
   public static final RenderType Z = RenderType.create("larp_effects", RenderSetup.builder(e4.e).sortOnUpload().createRenderSetup());
   private static final RenderType[][] o = new RenderType[fk.w.values().length][3];
   private static final RenderType[][] M = new RenderType[fk.w.values().length][3];
   private static final RenderType j;
   private static final double[] z = new double[60];
   private final double[] t = new double[18];
   private static final RenderType[] L = new RenderType[3];
   private static final RenderType R = RenderType.create(
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
   private boolean X;
   private fk.h m = fk.h.z;
   private boolean E;
   private final y2 x;
   private final PoseStack C;
   private final Vector3f p;
   private final Vector3f H;
   private final Matrix4f K;
   private final Vec3 a;
   private static final double[] h;
   private static final double[] c;

   public void a(Vec3 var1, double var2, float var4, float var5, int var6, int var7, boolean var8, float var9) {
      VertexConsumer var10 = this.x.a(m1.F(var7, var8));
      int var11 = Float.floatToRawIntBits(var4);
      int var12 = Float.floatToRawIntBits(var5);
      int var13 = Math.clamp(Math.round(var9 * 255.0F), 0, 255) << 24 | var6 & 16777215;

      for (int var14 = 0; var14 < 4; var14++) {
         float var15 = var14 != 0 && var14 != 3 ? 1.25F : -1.25F;
         float var16 = var14 < 2 ? -1.25F : 1.25F;
         var10.addVertex(this.K, (float)(var1.x + var15 * var2 - this.a.x), (float)(var1.y + 0.02 - this.a.y), (float)(var1.z + var16 * var2 - this.a.z))
            .setColor(var13)
            .setUv(var15, var16)
            .setUv1(var11 & 65535, var11 >>> 16)
            .setUv2(var12 & 65535, var12 >>> 16)
            .setNormal(0.0F, 1.0F, 0.0F);
      }
   }

   public void T(boolean var1) {
      this.X = (boolean)(var1 && jx.g() ? 1 : 0);
   }

   public void m() {
      this.x.v();
   }

   @Override
   public void blend(fk.h var1) {
      this.m = var1;
   }

   @Override
   public void solid(boolean var1) {
      this.E = var1;
   }

   public static RenderType U(fk.h var0) {
      return L[var0.ordinal()];
   }

   public static RenderType a() {
      return R;
   }

   public static void C() {
      for (RenderType var3 : o[fk.w.t.ordinal()]) {
         e4.U(var3.pipeline());
      }

      for (RenderType var7 : L) {
         e4.U(var7.pipeline());
      }

      e4.U(R.pipeline());
      e4.U(j.pipeline());
   }

   static PreparedRenderType B(PreparedRenderType var0) {
      for (int var1 = 0; var1 < 3; var1++) {
         if (var0.pipeline() == M[0][var1].pipeline()) {
            return of.R(var0, o[0][var1].pipeline());
         }
      }

      throw new IllegalArgumentException("Unknown soft particle pipeline");
   }

   public static void P() {
      for (RenderType var3 : M[fk.w.t.ordinal()]) {
         e4.U(var3.pipeline());
      }
   }

   public r6(PoseStack var1, y2 var2) {
      this(var1, var2, Minecraft.getInstance().gameRenderer.mainCamera().position(), Minecraft.getInstance().gameRenderer.mainCamera().rotation());
   }

   public r6(PoseStack var1, y2 var2, CameraRenderState var3) {
      this(var1, var2, var3.pos, var3.orientation);
   }

   private r6(PoseStack var1, y2 var2, Vec3 var3, Quaternionfc var4) {
      this.x = var2;
      this.C = var1;
      this.p = new Vector3f(1.0F, 0.0F, 0.0F).rotate(var4);
      this.H = new Vector3f(0.0F, 1.0F, 0.0F).rotate(var4);
      this.K = var1.last().pose();
      this.a = var3;
   }

   public void m(EntityRenderState var1, double var2, double var4, double var6, int var8, int var9, boolean var10) {
      Matrix4f var11 = uv.z(var1, this.C);
      if (var11 != null) {
         _e.u(this, var11, var2, var4, var6, var8, var9, var10);
      }
   }

   public void G(EntityRenderState var1, hw.j var2, double var3, double var5) {
      Matrix4f var7 = uv.z(var1, this.C);
      if (var7 != null) {
         List var8 = hw.S(var2);
         int var9 = !var8.isEmpty() && jz.u("ChinaHat") ? 1 : 0;
         hw.V(this, var7, var2, var3, var5, var9 == 0);
         if (var9 != 0) {
            Matrix4f var10 = new Matrix4f();

            for (hw.b var12 : var8) {
               jz.t(this, jz.u.get(var12.coin()), hw.t(var7, var12, var3, var10));
            }
         }
      }
   }

   public Matrix4f Z(EntityRenderState var1, double var2) {
      ju.h var4 = ju.k();
      if (var4 == null) {
         return null;
      }

      Matrix4f var5 = uv.z(var1, this.C);
      if (var5 == null) {
         return null;
      }

      ju.H(this.x.a(ju.G()), var5, var4, var2);
      return var5;
   }

   public void c(Matrix4f var1, double var2) {
      ju.h var4 = ju.k();
      if (var4 != null) {
         ju.b(this.x.a(ju.G()), var1, var4, var2);
      }
   }

   public void a(jp var1, Matrix4f var2, double var3, double var5, double var7, int var9, int var10, double var11) {
      ju.h var13 = ju.k();
      if (var13 != null) {
         Vector3f var14 = new Matrix4f(this.K).invert().mul(var2).transformPosition(0.0F, -0.5F, var13.centerZ() * (float)var3, new Vector3f());
         var1.M(var11, this.a.x + var14.x, this.a.y + var14.y + 0.24F * var3, this.a.z + var14.z, 0.0, 1.0, 0.0, var3 * var5, var7);
         this.T(var1, var9, var10, 1.0);
         h_.S("China Hat fire particles", var1.Y());
      }
   }

   public void T(jp var1, int var2, int var3, double var4) {
      RenderType var6 = jp.p();
      var1.l((var2x, var4x, var6x, var8, var10, var12, var14) -> this.I(var6, var2x, var4x, var6x, var8, var10, var12, var14), var2, var3, var4);
   }

   private void I(RenderType var1, double var2, double var4, double var6, double var8, double var10, double var12, int var14) {
      VertexConsumer var15 = this.x.a(var1);
      double var16 = Math.cos(var12);
      double var18 = Math.sin(var12);

      for (int var20 = 0; var20 < 4; var20++) {
         double var21 = var20 != 0 && var20 != 3 ? 1.0 : -1.0;
         double var23 = var20 < 2 ? -1.0 : 1.0;
         double var25 = (var21 * var16 - var23 * var10 * var18) * var8;
         double var27 = (var21 * var18 + var23 * var10 * var16) * var8;
         var15.addVertex(
               this.K,
               (float)(var2 - this.a.x + this.p.x * var25 + this.H.x * var27),
               (float)(var4 - this.a.y + this.p.y * var25 + this.H.y * var27),
               (float)(var6 - this.a.z + this.p.z * var25 + this.H.z * var27)
            )
            .setUv((float)((var21 + 1.0) * 0.5), (float)((var23 + 1.0) * 0.5))
            .setColor(var14);
      }
   }

   public void g(RenderType var1, Matrix4f var2, float[] var3, int[] var4) {
      D(this.x, var1, var2, var3, var4);
   }

   public void v(EntityRenderState var1, uv.w var2, int var3, double var4) {
      uv.k(var1, this.C, this.x, var2, var3, var4, false);
   }

   @Override
   public void modelVertex(Matrix4f var1, float var2, float var3, float var4, int var5) {
      this.x.a(this.E ? R : L[this.m.ordinal()]).addVertex(var1, var2, var3, var4).setColor(var5);
   }

   public void R(RenderType var1, Vec3 var2, Quaternionfc var3, float var4, float[] var5, int[] var6) {
      Matrix4f var7 = new Matrix4f(this.K)
         .translate((float)(var2.x - this.a.x), (float)(var2.y - this.a.y), (float)(var2.z - this.a.z))
         .rotate(var3)
         .scale(var4);
      D(this.x, var1, var7, var5, var6);
   }

   public static void D(y2 var0, RenderType var1, Matrix4f var2, float[] var3, int[] var4) {
      VertexConsumer var5 = var0.a(var1);

      for (int var6 = 0; var6 < var4.length; var6++) {
         int var7 = var6 * 5;
         var5.addVertex(var2, var3[var7], var3[var7 + 1], var3[var7 + 2]).setUv(var3[var7 + 3], var3[var7 + 4]).setColor(var4[var6]);
      }
   }

   @Override
   public void vertex(double var1, double var3, double var5, int var7) {
      this.x
         .a(L[this.m.ordinal()])
         .addVertex(this.K, (float)(var1 - this.a.x), (float)(var3 - this.a.y), (float)(var5 - this.a.z))
         .setColor(this.m == fk.h.v ? R(var7) : var7);
   }

   private void u(VertexConsumer var1, double var2, double var4, double var6, int var8) {
      var1.addVertex(this.K, (float)(var2 - this.a.x), (float)(var4 - this.a.y), (float)(var6 - this.a.z)).setColor(var8);
   }

   @Override
   public void ribbon(Vec3 var1, Vec3 var2, double var3, int var5, int var6) {
      this.p(var1.x, var1.y, var1.z, var2.x, var2.y, var2.z, var3, var3, var5, var6);
   }

   public void p(double var1, double var3, double var5, double var7, double var9, double var11, double var13, double var15, int var17, int var18) {
      double var19 = var7 - var1;
      double var21 = var9 - var3;
      double var23 = var11 - var5;
      if (!(var19 * var19 + var21 * var21 + var23 * var23 < 1.0E-10)) {
         double var25 = this.a.x - var1;
         double var27 = this.a.y - var3;
         double var29 = this.a.z - var5;
         double var31 = var21 * var29 - var23 * var27;
         double var33 = var23 * var25 - var19 * var29;
         double var35 = var19 * var27 - var21 * var25;
         double var37 = Math.sqrt(var31 * var31 + var33 * var33 + var35 * var35);
         if (var37 < 1.0E-8) {
            var31 = this.p.x;
            var33 = this.p.y;
            var35 = this.p.z;
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

   public void n(double var1, double var3, double var5, double var7, double var9, double var11, double var13, double var15, int var17, int var18) {
      this.p(var1, var3, var5, var7, var9, var11, var13 * 2.5, var15 * 2.5, oh.S(var17, 0.07), oh.S(var18, 0.07));
      this.p(var1, var3, var5, var7, var9, var11, var13 * 1.25, var15 * 1.25, oh.S(var17, 0.3), oh.S(var18, 0.3));
      this.p(
         var1,
         var3,
         var5,
         var7,
         var9,
         var11,
         var13 * 0.23,
         var15 * 0.23,
         oh.j(var17, var17 & 0xFF000000 | 16777215, 0.48),
         oh.j(var18, var18 & 0xFF000000 | 16777215, 0.48)
      );
   }

   public void y(h3.b var1, boolean var2) {
      for (int var3 = 0; var3 < (var2 ? 3 : 1); var3++) {
         double var4 = !var2 ? 1.0 : (var3 == 0 ? 2.5 : (var3 == 1 ? 1.25 : 0.23));

         for (int var6 = 1; var6 < var1.L; var6++) {
            Vec3 var7 = var1.I[var6 - 1];
            Vec3 var8 = var1.I[var6];
            if (!(var7.distanceToSqr(this.a) > var1.k) && !(var7.distanceToSqr(var8) < 1.0E-10)) {
               Vec3 var9 = var1.y[var6 - 1];
               Vec3 var10 = var1.y[var6];
               double var11 = var9.x * var4;
               double var13 = var9.y * var4;
               double var15 = var9.z * var4;
               double var17 = var10.x * var4;
               double var19 = var10.y * var4;
               double var21 = var10.z * var4;
               int var23 = w(var1.Z[var6 - 1], var3, var2);
               int var24 = w(var1.Z[var6], var3, var2);
               this.vertex(var7.x + var11, var7.y + var13, var7.z + var15, var23);
               this.vertex(var8.x + var17, var8.y + var19, var8.z + var21, var24);
               this.vertex(var8.x - var17, var8.y - var19, var8.z - var21, var24);
               this.vertex(var7.x - var11, var7.y - var13, var7.z - var15, var23);
            }
         }
      }
   }

   public void m(h3.b var1, double var2) {
      var2 = Math.clamp(var2, 0.0, 0.95);

      for (int var4 = 1; var4 < var1.L; var4++) {
         Vec3 var5 = var1.I[var4 - 1];
         Vec3 var6 = var1.I[var4];
         if (!(var5.distanceToSqr(this.a) > var1.k) && !(var5.distanceToSqr(var6) < 1.0E-10)) {
            Vec3 var7 = var1.y[var4 - 1];
            Vec3 var8 = var1.y[var4];
            int var9 = var1.Z[var4 - 1];
            int var10 = var1.Z[var4];
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

   private static int w(int var0, int var1, boolean var2) {
      if (!var2) {
         return var0;
      } else {
         return var1 < 2 ? oh.S(var0, var1 == 0 ? 0.07 : 0.3) : oh.j(var0, var0 & 0xFF000000 | 16777215, 0.48);
      }
   }

   public void T(EntityRenderState var1, Vec3 var2, double var3, double var5, double var7, int var9, int var10, boolean var11) {
      Matrix4f var12 = uv.z(var1, this.C);
      if (var12 != null) {
         dg.p(this, var12, var3, var5, var7, var9, var10, var11);
      } else {
         this.E(var2, var3, var5, var9, var10, var11);
      }
   }

   public void E(Vec3 var1, double var2, double var4, int var6, int var7, boolean var8) {
      for (int var9 = 0; var9 < 64; var9++) {
         this.vertex(var1.x, var1.y + var4, var1.z, var6);
         this.vertex(var1.x + h[var9 + 1] * var2, var1.y, var1.z + c[var9 + 1] * var2, var7);
         this.vertex(var1.x + h[var9] * var2, var1.y, var1.z + c[var9] * var2, var7);
         this.vertex(var1.x, var1.y + var4, var1.z, var6);
      }

      if (var8) {
         this.C(var1, var2, 0.009, oh.j(var7, var7 & 0xFF000000 | 16777215, 0.4));
      }
   }

   public void C(Vec3 var1, double var2, double var4, int var6) {
      this.C(var1.x, var1.y, var1.z, var2, var4, var6);
   }

   public void C(double var1, double var3, double var5, double var7, double var9, int var11) {
      this.e(var1, var3, var5, var7, var9, var11, 0.0, 1.0, 0.0);
   }

   public void e(double var1, double var3, double var5, double var7, double var9, int var11, double var12, double var14, double var16) {
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
      VertexConsumer var36 = this.x.a(L[this.m.ordinal()]);
      int var37 = this.m == fk.h.v ? 1 : 0;

      for (int var38 = 0; var38 < 4; var38++) {
         double var39 = Math.max(0.0, var7 + (var38 / 2.0 - 1.0) * var9);
         double var41 = Math.max(0.0, var7 + ((var38 + 1) / 2.0 - 1.0) * var9);
         double var43 = var38 == 0 ? 0.0 : (var38 == 2 ? 1.0 : 0.22);
         double var45 = var38 == 3 ? 0.0 : (var38 == 1 ? 1.0 : 0.22);
         int var47 = oh.S(var11, var43);
         int var48 = oh.S(var11, var45);
         if (var37 != 0) {
            var47 = R(var47);
            var48 = R(var48);
         }

         for (int var49 = 0; var49 < 64; var49++) {
            double var50 = var20 * h[var49] + var30 * c[var49];
            double var52 = var22 * h[var49] + var32 * c[var49];
            double var54 = var24 * h[var49] + var34 * c[var49];
            double var56 = var20 * h[var49 + 1] + var30 * c[var49 + 1];
            double var58 = var22 * h[var49 + 1] + var32 * c[var49 + 1];
            double var60 = var24 * h[var49 + 1] + var34 * c[var49 + 1];
            this.u(var36, var1 + var50 * var39, var3 + var52 * var39, var5 + var54 * var39, var47);
            this.u(var36, var1 + var56 * var39, var3 + var58 * var39, var5 + var60 * var39, var47);
            this.u(var36, var1 + var56 * var41, var3 + var58 * var41, var5 + var60 * var41, var48);
            this.u(var36, var1 + var50 * var41, var3 + var52 * var41, var5 + var54 * var41, var48);
         }
      }
   }

   public void r(double var1, double var3, double var5, double var7, double var9, double var11, double var13, int var15) {
      com.larpclient.x.L(this.t, var7, var9, var11, var13);

      for (int var16 = 0; var16 < com.larpclient.x.b(); var16++) {
         int var17 = com.larpclient.x.s(this.t, var16, var15);

         for (int var18 = 0; var18 < 4; var18++) {
            int var19 = com.larpclient.x.s(var16, var18) * 3;
            this.vertex(var1 + this.t[var19], var3 + this.t[var19 + 1], var5 + this.t[var19 + 2], var17);
         }
      }
   }

   public void Z(double var1, double var3, double var5, double var7, double var9, int var11, int var12, boolean var13) {
      int var14 = var13 ? var12 * 2 : var12;

      for (int var15 = 0; var15 < var14; var15++) {
         double var16 = var9 + var15 * Math.PI * 2.0 / var14;
         double var18 = var9 + (var15 + 1) * Math.PI * 2.0 / var14;
         double var20 = var7 * (var13 && var15 % 2 == 1 ? 0.4 : 1.0);
         double var22 = var7 * (var13 && (var15 + 1) % 2 == 1 ? 0.4 : 1.0);
         this.vertex(var1, var3, var5, var11);
         this.vertex(
            var1 + this.p.x * Math.cos(var16) * var20 + this.H.x * Math.sin(var16) * var20,
            var3 + this.p.y * Math.cos(var16) * var20 + this.H.y * Math.sin(var16) * var20,
            var5 + this.p.z * Math.cos(var16) * var20 + this.H.z * Math.sin(var16) * var20,
            var11
         );
         this.vertex(
            var1 + this.p.x * Math.cos(var18) * var22 + this.H.x * Math.sin(var18) * var22,
            var3 + this.p.y * Math.cos(var18) * var22 + this.H.y * Math.sin(var18) * var22,
            var5 + this.p.z * Math.cos(var18) * var22 + this.H.z * Math.sin(var18) * var22,
            var11
         );
         this.vertex(var1, var3, var5, var11);
      }
   }

   private static int R(int var0) {
      double var1 = (var0 >>> 24) / 255.0;
      return var0 & 0xFF000000 | (int)((var0 >> 16 & 0xFF) * var1) << 16 | (int)((var0 >> 8 & 0xFF) * var1) << 8 | (int)((var0 & 0xFF) * var1);
   }

   public void k(RenderType var1, double var2, double var4, double var6, double var8, double var10, int var12) {
      VertexConsumer var13 = this.x.a(var1);
      double var14 = Math.cos(var10);
      double var16 = Math.sin(var10);

      for (int var18 = 0; var18 < 4; var18++) {
         double var19 = var18 != 0 && var18 != 3 ? 1.0 : -1.0;
         double var21 = var18 < 2 ? -1.0 : 1.0;
         double var23 = (var19 * var14 - var21 * var16) * var8;
         double var25 = (var19 * var16 + var21 * var14) * var8;
         var13.addVertex(
               this.K,
               (float)(var2 - this.a.x + this.p.x * var23 + this.H.x * var25),
               (float)(var4 - this.a.y + this.p.y * var23 + this.H.y * var25),
               (float)(var6 - this.a.z + this.p.z * var23 + this.H.z * var25)
            )
            .setUv((float)((var19 + 1.0) * 0.5), (float)((var21 + 1.0) * 0.5))
            .setColor(var12);
      }
   }

   public void m(double var1, double var3, double var5, double var7, double var9, int var11) {
      this.j(var1, var3, var5, var7, var9, var11, f4.a.g, 0.0, 0.0, 0.0);
   }

   public void j(double var1, double var3, double var5, double var7, double var9, int var11, f4.a var12, double var13, double var15, double var17) {
      this.q(var1, var3, var5, var7, var9, var11, var12, var13, var15, var17, fk.w.t);
   }

   public void s(double var1, double var3, double var5, double var7, double var9, double var11, double var13, double var15, double var17, int var19, f4.a var20) {
      int var21 = f(var20);
      VertexConsumer var22 = this.x.a(o[fk.w.t.ordinal()][this.m.ordinal()]);

      for (int var23 = 0; var23 < 4; var23++) {
         double var24 = var23 != 0 && var23 != 3 ? 1.0 : -1.0;
         double var26 = var23 < 2 ? -1.0 : 1.0;
         var22.addVertex(
               this.K,
               (float)(var1 - this.a.x + var7 * var24 + var13 * var26),
               (float)(var3 - this.a.y + var9 * var24 + var15 * var26),
               (float)(var5 - this.a.z + var11 * var24 + var17 * var26)
            )
            .setUv((float)((var21 + (var24 + 1.0) * 0.5) / 9.0), (float)((var26 + 1.0) * 0.5))
            .setColor(var19);
      }
   }

   private static int f(f4.a var0) {
      return switch (var0) {
         case u -> 0;
         case D -> 1;
         case d -> 2;
         case g, o -> 3;
         case n -> 4;
         case y, S -> 5;
         case x -> 6;
         case F -> 7;
         default -> 8;
      };
   }

   public void q(double var1, double var3, double var5, double var7, double var9, int var11, f4.a var12, double var13, double var15, double var17, fk.w var19) {
      int var20 = f(var12);
      double var21 = 1.0;
      if (var12 == f4.a.b || var12 == f4.a.d) {
         var9 = Math.atan2(var13 * this.H.x + var15 * this.H.y + var17 * this.H.z, var13 * this.p.x + var15 * this.p.y + var17 * this.p.z);
         var21 = var12 == f4.a.b ? 3.0 : 1.6;
      }

      VertexConsumer var23 = this.x.a((this.X ? M : o)[var19.ordinal()][this.m.ordinal()]);
      double var24 = Math.cos(var9);
      double var26 = Math.sin(var9);

      for (int var28 = 0; var28 < 4; var28++) {
         double var29 = var28 != 0 && var28 != 3 ? 1.0 : -1.0;
         double var31 = var28 < 2 ? -1.0 : 1.0;
         double var33 = (var29 * var21 * var24 - var31 * var26) * var7;
         double var35 = (var29 * var21 * var26 + var31 * var24) * var7;
         var23.addVertex(
               this.K,
               (float)(var1 - this.a.x + this.p.x * var33 + this.H.x * var35),
               (float)(var3 - this.a.y + this.p.y * var33 + this.H.y * var35),
               (float)(var5 - this.a.z + this.p.z * var33 + this.H.z * var35)
            )
            .setUv((float)((var20 + (var29 + 1.0) * 0.5) / 9.0), (float)((var31 + 1.0) * 0.5))
            .setColor(var11);
      }
   }

   public void h(double var1, double var3, double var5, double var7, double var9, double var11, double var13, double var15, int var17) {
      ut.w(z, var7, var9, var11, var13, var15);
      VertexConsumer var18 = this.x.a(j);

      for (int var19 = 0; var19 < 18; var19++) {
         int var20 = ut.g(z, var19, var17);

         for (int var21 = 0; var21 < 4; var21++) {
            int var22 = ut.F(var19, var21) * 3;
            var18.addVertex(this.K, (float)(var1 - this.a.x + z[var22]), (float)(var3 - this.a.y + z[var22 + 1]), (float)(var5 - this.a.z + z[var22 + 2]))
               .setUv((float)ut.Y(var19, var21), (float)ut.N(var19, var21))
               .setColor(var20);
         }
      }
   }

   public void w(Vec3 var1, double var2, double var4, double var6, double var8, int var10, int var11, double var12) {
      int var14 = Math.clamp((int)Math.ceil(Math.abs(var8 - var6) * 12.0), 4, 96);

      for (int var15 = 0; var15 < var14; var15++) {
         double var16 = (double)var15 / var14;
         double var18 = (double)(var15 + 1) / var14;
         double var20 = var6 + (var8 - var6) * var16;
         double var22 = var6 + (var8 - var6) * var18;
         int var24 = oh.j(var10, var11, var16);
         int var25 = oh.j(var10, var11, var18);
         double var26 = var1.y + Math.sin(var20 * 2.0) * var12;
         double var28 = var1.y + Math.sin(var22 * 2.0) * var12;
         this.vertex(var1.x + Math.cos(var20) * var2, var26, var1.z + Math.sin(var20) * var2, var24);
         this.vertex(var1.x + Math.cos(var22) * var2, var28, var1.z + Math.sin(var22) * var2, var25);
         this.vertex(var1.x + Math.cos(var22) * (var2 + var4), var28, var1.z + Math.sin(var22) * (var2 + var4), oh.S(var25, 0.15));
         this.vertex(var1.x + Math.cos(var20) * (var2 + var4), var26, var1.z + Math.sin(var20) * (var2 + var4), oh.S(var24, 0.15));
      }
   }

   public void z(Vec3 var1, double var2, double var4, int var6) {
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
      j = RenderType.create(
         "larp_cash",
         RenderSetup.builder(var0)
            .withTexture("Sampler0", Identifier.fromNamespaceAndPath("larp-client", "textures/particles/cash.png"))
            .sortOnUpload()
            .createRenderSetup()
      );

      for (fk.h var4 : fk.h.values()) {
         BlendFunction var5 = switch (var4) {
            case z -> BlendFunction.TRANSLUCENT;
            case u -> BlendFunction.LIGHTNING;
            case v -> BlendFunction.TRANSLUCENT_PREMULTIPLIED_ALPHA;
         };
         Builder var6 = RenderPipeline.builder(new Snippet[]{RenderPipelines.GUI_TEXTURED_SNIPPET})
            .withLocation(Identifier.fromNamespaceAndPath("larp-client", "sprites_" + var4.name().toLowerCase(Locale.ROOT)))
            .withFragmentShader(Identifier.fromNamespaceAndPath("larp-client", "particle/sprite"))
            .withDepthStencilState(new DepthStencilState(CompareOp.GREATER_THAN_OR_EQUAL, false))
            .withColorTargetState(new ColorTargetState(var5))
            .withCull(false);
         if (var4 == fk.h.v) {
            var6.withShaderDefine("PREMULTIPLIED");
         }

         RenderPipeline var7 = var6.build();
         jx.f(var6);
         RenderPipeline var8 = var6.withLocation(Identifier.fromNamespaceAndPath("larp-client", "soft_sprites_" + var4.name().toLowerCase(Locale.ROOT)))
            .build();

         for (fk.w var12 : fk.w.values()) {
            o[var12.ordinal()][var4.ordinal()] = RenderType.create(
               "larp_sprites_" + var12 + "_" + var4,
               RenderSetup.builder(var7)
                  .withTexture("Sampler0", Identifier.fromNamespaceAndPath("larp-client", "textures/particles/" + var12.m))
                  .sortOnUpload()
                  .createRenderSetup()
            );
         }

         for (fk.w var17 : fk.w.values()) {
            M[var17.ordinal()][var4.ordinal()] = RenderType.create(
               "larp_soft_sprites_" + var17 + "_" + var4,
               RenderSetup.builder(var8)
                  .withTexture("Sampler0", Identifier.fromNamespaceAndPath("larp-client", "textures/particles/" + var17.m))
                  .sortOnUpload()
                  .createRenderSetup()
            );
         }

         L[var4.ordinal()] = RenderType.create(
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

      h = new double[65];
      c = new double[65];

      for (int var13 = 0; var13 <= 64; var13++) {
         h[var13] = Math.cos(var13 * Math.PI / 32.0);
         c[var13] = Math.sin(var13 * Math.PI / 32.0);
      }
   }
}
