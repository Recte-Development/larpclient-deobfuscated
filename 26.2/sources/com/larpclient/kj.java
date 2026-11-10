package com.larpclient;

import com.mojang.blaze3d.pipeline.BlendFunction;
import com.mojang.blaze3d.pipeline.ColorTargetState;
import com.mojang.blaze3d.pipeline.DepthStencilState;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.pipeline.RenderPipeline.Builder;
import com.mojang.blaze3d.pipeline.RenderPipeline.Snippet;
import com.mojang.blaze3d.platform.CompareOp;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
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

public final class kj implements d {
   public static final RenderType I = RenderType.create("larp_effects", RenderSetup.builder(ai.N).sortOnUpload().createRenderSetup());
   private static final RenderType[][] e = new RenderType[gl.k.values().length][3];
   private static final RenderType[][] V = new RenderType[gl.k.values().length][3];
   private static final RenderType i;
   private static final double[] Z = new double[60];
   private final double[] T = new double[18];
   private static final RenderType[] z = new RenderType[3];
   private static final RenderType t = RenderType.create(
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
   private boolean N;
   private gl.c j = gl.c.K;
   private boolean R;
   private final hh u;
   private final PoseStack w;
   private final Vector3f E;
   private final Vector3f L;
   private final Matrix4f W;
   private final Vec3 q;
   private static final double[] Y;
   private static final double[] c;

   public void R(Vec3 var1, double var2, float var4, float var5, int var6, int var7, boolean var8, float var9) {
      VertexConsumer var10 = this.u.g(jv.A(var7, var8));
      int var11 = Float.floatToRawIntBits(var4);
      int var12 = Float.floatToRawIntBits(var5);
      int var13 = Math.clamp(Math.round(var9 * 255.0F), 0, 255) << 24 | var6 & 16777215;

      for (int var14 = 0; var14 < 4; var14++) {
         float var15 = var14 != 0 && var14 != 3 ? 1.25F : -1.25F;
         float var16 = var14 < 2 ? -1.25F : 1.25F;
         var10.addVertex(this.W, (float)(var1.x + var15 * var2 - this.q.x), (float)(var1.y + 0.02 - this.q.y), (float)(var1.z + var16 * var2 - this.q.z))
            .setColor(var13)
            .setUv(var15, var16)
            .setUv1(var11 & 65535, var11 >>> 16)
            .setUv2(var12 & 65535, var12 >>> 16)
            .setNormal(0.0F, 1.0F, 0.0F);
      }
   }

   public void j(boolean var1) {
      this.N = (boolean)(var1 && he.K() ? 1 : 0);
   }

   public void r() {
      this.u.K();
   }

   @Override
   public void blend(gl.c var1) {
      this.j = var1;
   }

   @Override
   public void solid(boolean var1) {
      this.R = var1;
   }

   public static RenderType Y(gl.c var0) {
      return z[var0.ordinal()];
   }

   public static RenderType R() {
      return t;
   }

   public static void e() {
      for (RenderType var3 : e[gl.k.s.ordinal()]) {
         ai.U(var3.pipeline());
      }

      for (RenderType var7 : z) {
         ai.U(var7.pipeline());
      }

      ai.U(t.pipeline());
      ai.U(i.pipeline());
   }

   static PreparedRenderType K(PreparedRenderType var0) {
      for (int var1 = 0; var1 < 3; var1++) {
         if (var0.pipeline() == V[0][var1].pipeline()) {
            return mx.z(var0, e[0][var1].pipeline());
         }
      }

      throw new IllegalArgumentException("Unknown soft particle pipeline");
   }

   public static void h() {
      for (RenderType var3 : V[gl.k.s.ordinal()]) {
         ai.U(var3.pipeline());
      }
   }

   public kj(PoseStack var1, hh var2) {
      this(var1, var2, Minecraft.getInstance().gameRenderer.mainCamera().position(), Minecraft.getInstance().gameRenderer.mainCamera().rotation());
   }

   public kj(PoseStack var1, hh var2, CameraRenderState var3) {
      this(var1, var2, var3.pos, var3.orientation);
   }

   private kj(PoseStack var1, hh var2, Vec3 var3, Quaternionfc var4) {
      this.u = var2;
      this.w = var1;
      this.E = new Vector3f(1.0F, 0.0F, 0.0F).rotate(var4);
      this.L = new Vector3f(0.0F, 1.0F, 0.0F).rotate(var4);
      this.W = var1.last().pose();
      this.q = var3;
   }

   public void w(EntityRenderState var1, double var2, double var4, double var6, int var8, int var9, boolean var10, double var11) {
      Matrix4f var13 = j4.i(var1, this.w);
      if (var13 != null) {
         cs.n(this, var13, var2, var4, var6, var8, var9, var10, var11);
      }
   }

   public Matrix4f w(EntityRenderState var1, gm.y var2, double var3, double var5, double var7, double var9) {
      Matrix4f var11 = j4.i(var1, this.w);
      if (var11 == null) {
         return null;
      }

      List var12 = gm.b(var2);
      int var13 = !var12.isEmpty() && pv.T("ChinaHat") ? 1 : 0;
      if (var13 != 0) {
         Matrix4f var14 = new Matrix4f();

         for (gm.n var16 : var12) {
            pv.N(this, pv.o.get(var16.coin()), gm.M(var11, var16, var3, var14));
         }
      }

      gm.P(this, var11, var2, var3, var5, (boolean)var13, var7, var9);
      return var11;
   }

   public void k(Matrix4f var1, double var2, double var4, double var6) {
      ld.N(this, var1, var2, var4, var6);
   }

   public Matrix4f f(EntityRenderState var1, double var2) {
      sf.a var4 = sf.m();
      if (var4 == null) {
         return null;
      }

      Matrix4f var5 = j4.i(var1, this.w);
      if (var5 == null) {
         return null;
      }

      sf.Z(this.u.g(sf.K()), var5, var4, var2);
      return var5;
   }

   public void n(EntityRenderState var1, double var2, int var4) {
      sf.a var5 = me.v();
      if (var5 != null) {
         Matrix4f var6 = j4.i(var1, this.w);
         if (var6 != null) {
            me.a(this.u.g(me.R()), var6, var5, var2, var4);
         }
      }
   }

   public void m(Matrix4f var1, double var2) {
      sf.a var4 = sf.m();
      if (var4 != null) {
         sf.F(this.u.g(sf.K()), var1, var4, var2);
      }
   }

   public void L(ky var1, Matrix4f var2, double var3, double var5, double var7, int var9, int var10, double var11) {
      sf.a var13 = sf.m();
      if (var13 != null) {
         Vector3f var14 = new Matrix4f(this.W).invert().mul(var2).transformPosition(0.0F, -0.5F, var13.centerZ() * (float)var3, new Vector3f());
         var1.H(var11, this.q.x + var14.x, this.q.y + var14.y + 0.24F * var3, this.q.z + var14.z, 0.0, 1.0, 0.0, var3 * var5, var7);
         this.I(var1, var9, var10, 1.0);
         zj.s("China Hat fire particles", var1.s());
      }
   }

   public void I(ky var1, int var2, int var3, double var4) {
      RenderType var6 = ky.H();
      var1.i((var2x, var4x, var6x, var8, var10, var12, var14) -> this.l(var6, var2x, var4x, var6x, var8, var10, var12, var14), var2, var3, var4);
   }

   private void l(RenderType var1, double var2, double var4, double var6, double var8, double var10, double var12, int var14) {
      VertexConsumer var15 = this.u.g(var1);
      double var16 = Math.cos(var12);
      double var18 = Math.sin(var12);

      for (int var20 = 0; var20 < 4; var20++) {
         double var21 = var20 != 0 && var20 != 3 ? 1.0 : -1.0;
         double var23 = var20 < 2 ? -1.0 : 1.0;
         double var25 = (var21 * var16 - var23 * var10 * var18) * var8;
         double var27 = (var21 * var18 + var23 * var10 * var16) * var8;
         var15.addVertex(
               this.W,
               (float)(var2 - this.q.x + this.E.x * var25 + this.L.x * var27),
               (float)(var4 - this.q.y + this.E.y * var25 + this.L.y * var27),
               (float)(var6 - this.q.z + this.E.z * var25 + this.L.z * var27)
            )
            .setUv((float)((var21 + 1.0) * 0.5), (float)((var23 + 1.0) * 0.5))
            .setColor(var14);
      }
   }

   public void M(RenderType var1, Matrix4f var2, float[] var3, int[] var4) {
      E(this.u, var1, var2, var3, var4);
   }

   public void u(EntityRenderState var1, j4.j var2, int var3, double var4) {
      j4.I(var1, this.w, this.u, var2, var3, var4, false);
   }

   @Override
   public Vector3f viewer(Matrix4f var1, Vector3f var2) {
      return new Matrix4f(var1).invert().transformPosition(var2.set(0.0F, 0.0F, 0.0F));
   }

   @Override
   public void modelVertex(Matrix4f var1, float var2, float var3, float var4, int var5) {
      this.u.g(this.R ? t : z[this.j.ordinal()]).addVertex(var1, var2, var3, var4).setColor(var5);
   }

   public void M(RenderType var1, Vec3 var2, Quaternionfc var3, float var4, float[] var5, int[] var6) {
      Matrix4f var7 = new Matrix4f(this.W)
         .translate((float)(var2.x - this.q.x), (float)(var2.y - this.q.y), (float)(var2.z - this.q.z))
         .rotate(var3)
         .scale(var4);
      E(this.u, var1, var7, var5, var6);
   }

   public static void E(hh var0, RenderType var1, Matrix4f var2, float[] var3, int[] var4) {
      VertexConsumer var5 = var0.g(var1);

      for (int var6 = 0; var6 < var4.length; var6++) {
         int var7 = var6 * 5;
         var5.addVertex(var2, var3[var7], var3[var7 + 1], var3[var7 + 2]).setUv(var3[var7 + 3], var3[var7 + 4]).setColor(var4[var6]);
      }
   }

   @Override
   public void vertex(double var1, double var3, double var5, int var7) {
      this.u
         .g(z[this.j.ordinal()])
         .addVertex(this.W, (float)(var1 - this.q.x), (float)(var3 - this.q.y), (float)(var5 - this.q.z))
         .setColor(this.j == gl.c.V ? B(var7) : var7);
   }

   private void k(VertexConsumer var1, double var2, double var4, double var6, int var8) {
      var1.addVertex(this.W, (float)(var2 - this.q.x), (float)(var4 - this.q.y), (float)(var6 - this.q.z)).setColor(var8);
   }

   @Override
   public void ribbon(Vec3 var1, Vec3 var2, double var3, int var5, int var6) {
      this.k(var1.x, var1.y, var1.z, var2.x, var2.y, var2.z, var3, var3, var5, var6);
   }

   public void k(double var1, double var3, double var5, double var7, double var9, double var11, double var13, double var15, int var17, int var18) {
      double var19 = var7 - var1;
      double var21 = var9 - var3;
      double var23 = var11 - var5;
      if (!(var19 * var19 + var21 * var21 + var23 * var23 < 1.0E-10)) {
         double var25 = this.q.x - var1;
         double var27 = this.q.y - var3;
         double var29 = this.q.z - var5;
         double var31 = var21 * var29 - var23 * var27;
         double var33 = var23 * var25 - var19 * var29;
         double var35 = var19 * var27 - var21 * var25;
         double var37 = Math.sqrt(var31 * var31 + var33 * var33 + var35 * var35);
         if (var37 < 1.0E-8) {
            var31 = this.E.x;
            var33 = this.E.y;
            var35 = this.E.z;
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

   public void W(double var1, double var3, double var5, double var7, double var9, double var11, double var13, double var15, int var17, int var18) {
      this.k(var1, var3, var5, var7, var9, var11, var13 * 2.5, var15 * 2.5, uh.F(var17, 0.07), uh.F(var18, 0.07));
      this.k(var1, var3, var5, var7, var9, var11, var13 * 1.25, var15 * 1.25, uh.F(var17, 0.3), uh.F(var18, 0.3));
      this.k(
         var1,
         var3,
         var5,
         var7,
         var9,
         var11,
         var13 * 0.23,
         var15 * 0.23,
         uh.q(var17, var17 & 0xFF000000 | 16777215, 0.48),
         uh.q(var18, var18 & 0xFF000000 | 16777215, 0.48)
      );
   }

   public void b(u_.p var1, boolean var2) {
      for (int var3 = 0; var3 < (var2 ? 3 : 1); var3++) {
         double var4 = !var2 ? 1.0 : (var3 == 0 ? 2.5 : (var3 == 1 ? 1.25 : 0.23));

         for (int var6 = 1; var6 < var1.p; var6++) {
            Vec3 var7 = var1.M[var6 - 1];
            Vec3 var8 = var1.M[var6];
            if (!(var7.distanceToSqr(this.q) > var1.C) && !(var7.distanceToSqr(var8) < 1.0E-10)) {
               Vec3 var9 = var1.D[var6 - 1];
               Vec3 var10 = var1.D[var6];
               double var11 = var9.x * var4;
               double var13 = var9.y * var4;
               double var15 = var9.z * var4;
               double var17 = var10.x * var4;
               double var19 = var10.y * var4;
               double var21 = var10.z * var4;
               int var23 = E(var1.I[var6 - 1], var3, var2);
               int var24 = E(var1.I[var6], var3, var2);
               this.vertex(var7.x + var11, var7.y + var13, var7.z + var15, var23);
               this.vertex(var8.x + var17, var8.y + var19, var8.z + var21, var24);
               this.vertex(var8.x - var17, var8.y - var19, var8.z - var21, var24);
               this.vertex(var7.x - var11, var7.y - var13, var7.z - var15, var23);
            }
         }
      }
   }

   public void s(u_.p var1, double var2) {
      var2 = Math.clamp(var2, 0.0, 0.95);

      for (int var4 = 1; var4 < var1.p; var4++) {
         Vec3 var5 = var1.M[var4 - 1];
         Vec3 var6 = var1.M[var4];
         if (!(var5.distanceToSqr(this.q) > var1.C) && !(var5.distanceToSqr(var6) < 1.0E-10)) {
            Vec3 var7 = var1.D[var4 - 1];
            Vec3 var8 = var1.D[var4];
            int var9 = var1.I[var4 - 1];
            int var10 = var1.I[var4];
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

   private static int E(int var0, int var1, boolean var2) {
      if (!var2) {
         return var0;
      } else {
         return var1 < 2 ? uh.F(var0, var1 == 0 ? 0.07 : 0.3) : uh.q(var0, var0 & 0xFF000000 | 16777215, 0.48);
      }
   }

   public void c(EntityRenderState var1, Vec3 var2, double var3, double var5, double var7, int var9, int var10, boolean var11) {
      Matrix4f var12 = j4.i(var1, this.w);
      if (var12 != null) {
         tf.T(this, var12, var3, var5, var7, var9, var10, var11);
      } else {
         this.W(var2, var3, var5, var9, var10, var11);
      }
   }

   public void W(Vec3 var1, double var2, double var4, int var6, int var7, boolean var8) {
      for (int var9 = 0; var9 < 64; var9++) {
         this.vertex(var1.x, var1.y + var4, var1.z, var6);
         this.vertex(var1.x + Y[var9 + 1] * var2, var1.y, var1.z + c[var9 + 1] * var2, var7);
         this.vertex(var1.x + Y[var9] * var2, var1.y, var1.z + c[var9] * var2, var7);
         this.vertex(var1.x, var1.y + var4, var1.z, var6);
      }

      if (var8) {
         this.I(var1, var2, 0.009, uh.q(var7, var7 & 0xFF000000 | 16777215, 0.4));
      }
   }

   public void I(Vec3 var1, double var2, double var4, int var6) {
      this.F(var1.x, var1.y, var1.z, var2, var4, var6);
   }

   public void F(double var1, double var3, double var5, double var7, double var9, int var11) {
      this.Q(var1, var3, var5, var7, var9, var11, 0.0, 1.0, 0.0);
   }

   public void Q(double var1, double var3, double var5, double var7, double var9, int var11, double var12, double var14, double var16) {
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
      VertexConsumer var36 = this.u.g(z[this.j.ordinal()]);
      int var37 = this.j == gl.c.V ? 1 : 0;

      for (int var38 = 0; var38 < 4; var38++) {
         double var39 = Math.max(0.0, var7 + (var38 / 2.0 - 1.0) * var9);
         double var41 = Math.max(0.0, var7 + ((var38 + 1) / 2.0 - 1.0) * var9);
         double var43 = var38 == 0 ? 0.0 : (var38 == 2 ? 1.0 : 0.22);
         double var45 = var38 == 3 ? 0.0 : (var38 == 1 ? 1.0 : 0.22);
         int var47 = uh.F(var11, var43);
         int var48 = uh.F(var11, var45);
         if (var37 != 0) {
            var47 = B(var47);
            var48 = B(var48);
         }

         for (int var49 = 0; var49 < 64; var49++) {
            double var50 = var20 * Y[var49] + var30 * c[var49];
            double var52 = var22 * Y[var49] + var32 * c[var49];
            double var54 = var24 * Y[var49] + var34 * c[var49];
            double var56 = var20 * Y[var49 + 1] + var30 * c[var49 + 1];
            double var58 = var22 * Y[var49 + 1] + var32 * c[var49 + 1];
            double var60 = var24 * Y[var49 + 1] + var34 * c[var49 + 1];
            this.k(var36, var1 + var50 * var39, var3 + var52 * var39, var5 + var54 * var39, var47);
            this.k(var36, var1 + var56 * var39, var3 + var58 * var39, var5 + var60 * var39, var47);
            this.k(var36, var1 + var56 * var41, var3 + var58 * var41, var5 + var60 * var41, var48);
            this.k(var36, var1 + var50 * var41, var3 + var52 * var41, var5 + var54 * var41, var48);
         }
      }
   }

   public void b(double var1, double var3, double var5, double var7, double var9, double var11, double var13, int var15) {
      ke.L(this.T, var7, var9, var11, var13);

      for (int var16 = 0; var16 < ke.y(); var16++) {
         int var17 = ke.H(this.T, var16, var15);

         for (int var18 = 0; var18 < 4; var18++) {
            int var19 = ke.t(var16, var18) * 3;
            this.vertex(var1 + this.T[var19], var3 + this.T[var19 + 1], var5 + this.T[var19 + 2], var17);
         }
      }
   }

   public void T(double var1, double var3, double var5, double var7, double var9, int var11, int var12, boolean var13) {
      int var14 = var13 ? var12 * 2 : var12;

      for (int var15 = 0; var15 < var14; var15++) {
         double var16 = var9 + var15 * Math.PI * 2.0 / var14;
         double var18 = var9 + (var15 + 1) * Math.PI * 2.0 / var14;
         double var20 = var7 * (var13 && var15 % 2 == 1 ? 0.4 : 1.0);
         double var22 = var7 * (var13 && (var15 + 1) % 2 == 1 ? 0.4 : 1.0);
         this.vertex(var1, var3, var5, var11);
         this.vertex(
            var1 + this.E.x * Math.cos(var16) * var20 + this.L.x * Math.sin(var16) * var20,
            var3 + this.E.y * Math.cos(var16) * var20 + this.L.y * Math.sin(var16) * var20,
            var5 + this.E.z * Math.cos(var16) * var20 + this.L.z * Math.sin(var16) * var20,
            var11
         );
         this.vertex(
            var1 + this.E.x * Math.cos(var18) * var22 + this.L.x * Math.sin(var18) * var22,
            var3 + this.E.y * Math.cos(var18) * var22 + this.L.y * Math.sin(var18) * var22,
            var5 + this.E.z * Math.cos(var18) * var22 + this.L.z * Math.sin(var18) * var22,
            var11
         );
         this.vertex(var1, var3, var5, var11);
      }
   }

   private static int B(int var0) {
      double var1 = (var0 >>> 24) / 255.0;
      return var0 & 0xFF000000 | (int)((var0 >> 16 & 0xFF) * var1) << 16 | (int)((var0 >> 8 & 0xFF) * var1) << 8 | (int)((var0 & 0xFF) * var1);
   }

   public void M(RenderType var1, double var2, double var4, double var6, double var8, double var10, int var12) {
      VertexConsumer var13 = this.u.g(var1);
      double var14 = Math.cos(var10);
      double var16 = Math.sin(var10);

      for (int var18 = 0; var18 < 4; var18++) {
         double var19 = var18 != 0 && var18 != 3 ? 1.0 : -1.0;
         double var21 = var18 < 2 ? -1.0 : 1.0;
         double var23 = (var19 * var14 - var21 * var16) * var8;
         double var25 = (var19 * var16 + var21 * var14) * var8;
         var13.addVertex(
               this.W,
               (float)(var2 - this.q.x + this.E.x * var23 + this.L.x * var25),
               (float)(var4 - this.q.y + this.E.y * var23 + this.L.y * var25),
               (float)(var6 - this.q.z + this.E.z * var23 + this.L.z * var25)
            )
            .setUv((float)((var19 + 1.0) * 0.5), (float)((var21 + 1.0) * 0.5))
            .setColor(var12);
      }
   }

   public void P(double var1, double var3, double var5, double var7, double var9, int var11) {
      this.I(var1, var3, var5, var7, var9, var11, c3.a.e, 0.0, 0.0, 0.0);
   }

   public void I(double var1, double var3, double var5, double var7, double var9, int var11, c3.a var12, double var13, double var15, double var17) {
      this.O(var1, var3, var5, var7, var9, var11, var12, var13, var15, var17, gl.k.s);
   }

   public void m(double var1, double var3, double var5, double var7, double var9, double var11, double var13, double var15, double var17, int var19, c3.a var20) {
      int var21 = s(var20);
      VertexConsumer var22 = this.u.g(e[gl.k.s.ordinal()][this.j.ordinal()]);

      for (int var23 = 0; var23 < 4; var23++) {
         double var24 = var23 != 0 && var23 != 3 ? 1.0 : -1.0;
         double var26 = var23 < 2 ? -1.0 : 1.0;
         var22.addVertex(
               this.W,
               (float)(var1 - this.q.x + var7 * var24 + var13 * var26),
               (float)(var3 - this.q.y + var9 * var24 + var15 * var26),
               (float)(var5 - this.q.z + var11 * var24 + var17 * var26)
            )
            .setUv((float)((var21 + (var24 + 1.0) * 0.5) / 9.0), (float)((var26 + 1.0) * 0.5))
            .setColor(var19);
      }
   }

   private static int s(c3.a var0) {
      return switch (var0) {
         case Z -> 0;
         case l -> 1;
         case P -> 2;
         case e, v -> 3;
         case n -> 4;
         case T, y -> 5;
         case q -> 6;
         case C -> 7;
         default -> 8;
      };
   }

   public void O(double var1, double var3, double var5, double var7, double var9, int var11, c3.a var12, double var13, double var15, double var17, gl.k var19) {
      int var20 = s(var12);
      double var21 = 1.0;
      if (var12 == c3.a.Q || var12 == c3.a.P) {
         var9 = Math.atan2(var13 * this.L.x + var15 * this.L.y + var17 * this.L.z, var13 * this.E.x + var15 * this.E.y + var17 * this.E.z);
         var21 = var12 == c3.a.Q ? 3.0 : 1.6;
      }

      VertexConsumer var23 = this.u.g((this.N ? V : e)[var19.ordinal()][this.j.ordinal()]);
      double var24 = Math.cos(var9);
      double var26 = Math.sin(var9);

      for (int var28 = 0; var28 < 4; var28++) {
         double var29 = var28 != 0 && var28 != 3 ? 1.0 : -1.0;
         double var31 = var28 < 2 ? -1.0 : 1.0;
         double var33 = (var29 * var21 * var24 - var31 * var26) * var7;
         double var35 = (var29 * var21 * var26 + var31 * var24) * var7;
         var23.addVertex(
               this.W,
               (float)(var1 - this.q.x + this.E.x * var33 + this.L.x * var35),
               (float)(var3 - this.q.y + this.E.y * var33 + this.L.y * var35),
               (float)(var5 - this.q.z + this.E.z * var33 + this.L.z * var35)
            )
            .setUv((float)((var20 + (var29 + 1.0) * 0.5) / 9.0), (float)((var31 + 1.0) * 0.5))
            .setColor(var11);
      }
   }

   public void r(double var1, double var3, double var5, double var7, double var9, double var11, double var13, double var15, int var17) {
      g.L(Z, var7, var9, var11, var13, var15);
      VertexConsumer var18 = this.u.g(i);

      for (int var19 = 0; var19 < 18; var19++) {
         int var20 = g.r(Z, var19, var17);

         for (int var21 = 0; var21 < 4; var21++) {
            int var22 = g.r(var19, var21) * 3;
            var18.addVertex(this.W, (float)(var1 - this.q.x + Z[var22]), (float)(var3 - this.q.y + Z[var22 + 1]), (float)(var5 - this.q.z + Z[var22 + 2]))
               .setUv((float)g.S(var19, var21), (float)g.w(var19, var21))
               .setColor(var20);
         }
      }
   }

   public void k(Vec3 var1, double var2, double var4, double var6, double var8, int var10, int var11, double var12) {
      int var14 = Math.clamp((int)Math.ceil(Math.abs(var8 - var6) * 12.0), 4, 96);

      for (int var15 = 0; var15 < var14; var15++) {
         double var16 = (double)var15 / var14;
         double var18 = (double)(var15 + 1) / var14;
         double var20 = var6 + (var8 - var6) * var16;
         double var22 = var6 + (var8 - var6) * var18;
         int var24 = uh.q(var10, var11, var16);
         int var25 = uh.q(var10, var11, var18);
         double var26 = var1.y + Math.sin(var20 * 2.0) * var12;
         double var28 = var1.y + Math.sin(var22 * 2.0) * var12;
         this.vertex(var1.x + Math.cos(var20) * var2, var26, var1.z + Math.sin(var20) * var2, var24);
         this.vertex(var1.x + Math.cos(var22) * var2, var28, var1.z + Math.sin(var22) * var2, var25);
         this.vertex(var1.x + Math.cos(var22) * (var2 + var4), var28, var1.z + Math.sin(var22) * (var2 + var4), uh.F(var25, 0.15));
         this.vertex(var1.x + Math.cos(var20) * (var2 + var4), var26, var1.z + Math.sin(var20) * (var2 + var4), uh.F(var24, 0.15));
      }
   }

   public void y(Vec3 var1, double var2, double var4, int var6) {
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
      i = RenderType.create(
         "larp_cash",
         RenderSetup.builder(var0)
            .withTexture("Sampler0", Identifier.fromNamespaceAndPath("larp-client", "textures/particles/cash.png"))
            .sortOnUpload()
            .createRenderSetup()
      );

      for (gl.c var4 : gl.c.values()) {
         BlendFunction var5 = switch (var4) {
            case K -> BlendFunction.TRANSLUCENT;
            case E -> BlendFunction.LIGHTNING;
            case V -> BlendFunction.TRANSLUCENT_PREMULTIPLIED_ALPHA;
         };
         Builder var6 = RenderPipeline.builder(new Snippet[]{RenderPipelines.GUI_TEXTURED_SNIPPET})
            .withLocation(Identifier.fromNamespaceAndPath("larp-client", "sprites_" + var4.name().toLowerCase(Locale.ROOT)))
            .withFragmentShader(Identifier.fromNamespaceAndPath("larp-client", "particle/sprite"))
            .withDepthStencilState(new DepthStencilState(CompareOp.GREATER_THAN_OR_EQUAL, false))
            .withColorTargetState(new ColorTargetState(var5))
            .withCull(false);
         if (var4 == gl.c.V) {
            var6.withShaderDefine("PREMULTIPLIED");
         }

         RenderPipeline var7 = var6.build();
         he.J(var6);
         RenderPipeline var8 = var6.withLocation(Identifier.fromNamespaceAndPath("larp-client", "soft_sprites_" + var4.name().toLowerCase(Locale.ROOT)))
            .build();

         for (gl.k var12 : gl.k.values()) {
            e[var12.ordinal()][var4.ordinal()] = RenderType.create(
               "larp_sprites_" + var12 + "_" + var4,
               RenderSetup.builder(var7)
                  .withTexture("Sampler0", Identifier.fromNamespaceAndPath("larp-client", "textures/particles/" + var12.w))
                  .sortOnUpload()
                  .createRenderSetup()
            );
         }

         for (gl.k var17 : gl.k.values()) {
            V[var17.ordinal()][var4.ordinal()] = RenderType.create(
               "larp_soft_sprites_" + var17 + "_" + var4,
               RenderSetup.builder(var8)
                  .withTexture("Sampler0", Identifier.fromNamespaceAndPath("larp-client", "textures/particles/" + var17.w))
                  .sortOnUpload()
                  .createRenderSetup()
            );
         }

         z[var4.ordinal()] = RenderType.create(
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

      Y = new double[65];
      c = new double[65];

      for (int var13 = 0; var13 <= 64; var13++) {
         Y[var13] = Math.cos(var13 * Math.PI / 32.0);
         c[var13] = Math.sin(var13 * Math.PI / 32.0);
      }
   }
}
