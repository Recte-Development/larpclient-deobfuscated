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

public final class fz implements ia {
   public static final RenderType X = RenderType.create("larp_effects", RenderSetup.builder(se.Q).sortOnUpload().createRenderSetup());
   private static final RenderType[][] f = new RenderType[_0.j.values().length][3];
   private static final RenderType[][] W = new RenderType[_0.j.values().length][3];
   private static final RenderType Z;
   private static final double[] B = new double[60];
   private final double[] J = new double[18];
   private static final RenderType[] C = new RenderType[3];
   private static final RenderType u = RenderType.create(
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
   private boolean e;
   private _0.v F = _0.v.z;
   private boolean z;
   private final wh K;
   private final PoseStack S;
   private final Vector3f y;
   private final Vector3f x;
   private final Matrix4f b;
   private final Vec3 d;
   private static final double[] Q;
   private static final double[] R;

   public void q(Vec3 var1, double var2, float var4, float var5, int var6, int var7, boolean var8, float var9) {
      VertexConsumer var10 = this.K.w(nt.s(var7, var8));
      int var11 = Float.floatToRawIntBits(var4);
      int var12 = Float.floatToRawIntBits(var5);
      int var13 = Math.clamp(Math.round(var9 * 255.0F), 0, 255) << 24 | var6 & 16777215;

      for (int var14 = 0; var14 < 4; var14++) {
         float var15 = var14 != 0 && var14 != 3 ? 1.25F : -1.25F;
         float var16 = var14 < 2 ? -1.25F : 1.25F;
         var10.addVertex(this.b, (float)(var1.x + var15 * var2 - this.d.x), (float)(var1.y + 0.02 - this.d.y), (float)(var1.z + var16 * var2 - this.d.z))
            .setColor(var13)
            .setUv(var15, var16)
            .setUv1(var11 & 65535, var11 >>> 16)
            .setUv2(var12 & 65535, var12 >>> 16)
            .setNormal(0.0F, 1.0F, 0.0F);
      }
   }

   public void x(boolean var1) {
      this.e = (boolean)(var1 && d3.t() ? 1 : 0);
   }

   public void I() {
      this.K.S();
   }

   @Override
   public void blend(_0.v var1) {
      this.F = var1;
   }

   @Override
   public void solid(boolean var1) {
      this.z = var1;
   }

   public static RenderType j(_0.v var0) {
      return C[var0.ordinal()];
   }

   public static RenderType H() {
      return u;
   }

   public static void C() {
      for (RenderType var3 : f[_0.j.U.ordinal()]) {
         se.j(var3.pipeline());
      }

      for (RenderType var7 : C) {
         se.j(var7.pipeline());
      }

      se.j(u.pipeline());
      se.j(Z.pipeline());
   }

   static PreparedRenderType H(PreparedRenderType var0) {
      for (int var1 = 0; var1 < 3; var1++) {
         if (var0.pipeline() == W[0][var1].pipeline()) {
            return p7.m(var0, f[0][var1].pipeline());
         }
      }

      throw new IllegalArgumentException("Unknown soft particle pipeline");
   }

   public static void y() {
      for (RenderType var3 : W[_0.j.U.ordinal()]) {
         se.j(var3.pipeline());
      }
   }

   public fz(PoseStack var1, wh var2) {
      this(var1, var2, Minecraft.getInstance().gameRenderer.mainCamera().position(), Minecraft.getInstance().gameRenderer.mainCamera().rotation());
   }

   public fz(PoseStack var1, wh var2, CameraRenderState var3) {
      this(var1, var2, var3.pos, var3.orientation);
   }

   private fz(PoseStack var1, wh var2, Vec3 var3, Quaternionfc var4) {
      this.K = var2;
      this.S = var1;
      this.y = new Vector3f(1.0F, 0.0F, 0.0F).rotate(var4);
      this.x = new Vector3f(0.0F, 1.0F, 0.0F).rotate(var4);
      this.b = var1.last().pose();
      this.d = var3;
   }

   public void y(EntityRenderState var1, double var2, double var4, double var6, int var8, int var9, boolean var10, double var11) {
      Matrix4f var13 = w7.e(var1, this.S);
      if (var13 != null) {
         bg.j(this, var13, var2, var4, var6, var8, var9, var10, var11);
      }
   }

   public Matrix4f X(EntityRenderState var1, sb.q var2, double var3, double var5, double var7, double var9) {
      Matrix4f var11 = w7.e(var1, this.S);
      if (var11 == null) {
         return null;
      }

      List var12 = sb.E(var2);
      int var13 = !var12.isEmpty() && ho.F("ChinaHat") ? 1 : 0;
      if (var13 != 0) {
         Matrix4f var14 = new Matrix4f();

         for (sb.r var16 : var12) {
            ho.R(this, ho.X.get(var16.coin()), sb.v(var11, var16, var3, var14));
         }
      }

      sb.l(this, var11, var2, var3, var5, (boolean)var13, var7, var9);
      return var11;
   }

   public void u(Matrix4f var1, double var2, double var4, double var6) {
      hm.k(this, var1, var2, var4, var6);
   }

   public Matrix4f H(EntityRenderState var1, double var2) {
      np.u var4 = np.G();
      if (var4 == null) {
         return null;
      }

      Matrix4f var5 = w7.e(var1, this.S);
      if (var5 == null) {
         return null;
      }

      np.v(this.K.w(np.F()), var5, var4, var2);
      return var5;
   }

   public void t(EntityRenderState var1, double var2, int var4) {
      np.u var5 = hu.j();
      if (var5 != null) {
         Matrix4f var6 = w7.e(var1, this.S);
         if (var6 != null) {
            hu.E(this.K.w(hu.n()), var6, var5, var2, var4);
         }
      }
   }

   public void r(Matrix4f var1, double var2) {
      np.u var4 = np.G();
      if (var4 != null) {
         np.f(this.K.w(np.F()), var1, var4, var2);
      }
   }

   public void q(dj var1, Matrix4f var2, double var3, double var5, double var7, int var9, int var10, double var11) {
      np.u var13 = np.G();
      if (var13 != null) {
         Vector3f var14 = new Matrix4f(this.b).invert().mul(var2).transformPosition(0.0F, -0.5F, var13.centerZ() * (float)var3, new Vector3f());
         var1.m(var11, this.d.x + var14.x, this.d.y + var14.y + 0.24F * var3, this.d.z + var14.z, 0.0, 1.0, 0.0, var3 * var5, var7);
         this.z(var1, var9, var10, 1.0);
         he.z("China Hat fire particles", var1.g());
      }
   }

   public void z(dj var1, int var2, int var3, double var4) {
      RenderType var6 = dj.H();
      var1.m((var2x, var4x, var6x, var8, var10, var12, var14) -> this.i(var6, var2x, var4x, var6x, var8, var10, var12, var14), var2, var3, var4);
   }

   private void i(RenderType var1, double var2, double var4, double var6, double var8, double var10, double var12, int var14) {
      VertexConsumer var15 = this.K.w(var1);
      double var16 = Math.cos(var12);
      double var18 = Math.sin(var12);

      for (int var20 = 0; var20 < 4; var20++) {
         double var21 = var20 != 0 && var20 != 3 ? 1.0 : -1.0;
         double var23 = var20 < 2 ? -1.0 : 1.0;
         double var25 = (var21 * var16 - var23 * var10 * var18) * var8;
         double var27 = (var21 * var18 + var23 * var10 * var16) * var8;
         var15.addVertex(
               this.b,
               (float)(var2 - this.d.x + this.y.x * var25 + this.x.x * var27),
               (float)(var4 - this.d.y + this.y.y * var25 + this.x.y * var27),
               (float)(var6 - this.d.z + this.y.z * var25 + this.x.z * var27)
            )
            .setUv((float)((var21 + 1.0) * 0.5), (float)((var23 + 1.0) * 0.5))
            .setColor(var14);
      }
   }

   public void w(RenderType var1, Matrix4f var2, float[] var3, int[] var4) {
      E(this.K, var1, var2, var3, var4);
   }

   public void m(EntityRenderState var1, w7.b var2, int var3, double var4) {
      w7.Q(var1, this.S, this.K, var2, var3, var4, false);
   }

   @Override
   public Vector3f viewer(Matrix4f var1, Vector3f var2) {
      return new Matrix4f(var1).invert().transformPosition(var2.set(0.0F, 0.0F, 0.0F));
   }

   @Override
   public void modelVertex(Matrix4f var1, float var2, float var3, float var4, int var5) {
      this.K.w(this.z ? u : C[this.F.ordinal()]).addVertex(var1, var2, var3, var4).setColor(var5);
   }

   public void L(RenderType var1, Vec3 var2, Quaternionfc var3, float var4, float[] var5, int[] var6) {
      Matrix4f var7 = new Matrix4f(this.b)
         .translate((float)(var2.x - this.d.x), (float)(var2.y - this.d.y), (float)(var2.z - this.d.z))
         .rotate(var3)
         .scale(var4);
      E(this.K, var1, var7, var5, var6);
   }

   public static void E(wh var0, RenderType var1, Matrix4f var2, float[] var3, int[] var4) {
      VertexConsumer var5 = var0.w(var1);

      for (int var6 = 0; var6 < var4.length; var6++) {
         int var7 = var6 * 5;
         var5.addVertex(var2, var3[var7], var3[var7 + 1], var3[var7 + 2]).setUv(var3[var7 + 3], var3[var7 + 4]).setColor(var4[var6]);
      }
   }

   @Override
   public void vertex(double var1, double var3, double var5, int var7) {
      this.K
         .w(C[this.F.ordinal()])
         .addVertex(this.b, (float)(var1 - this.d.x), (float)(var3 - this.d.y), (float)(var5 - this.d.z))
         .setColor(this.F == _0.v.Y ? H(var7) : var7);
   }

   private void t(VertexConsumer var1, double var2, double var4, double var6, int var8) {
      var1.addVertex(this.b, (float)(var2 - this.d.x), (float)(var4 - this.d.y), (float)(var6 - this.d.z)).setColor(var8);
   }

   @Override
   public void ribbon(Vec3 var1, Vec3 var2, double var3, int var5, int var6) {
      this.E(var1.x, var1.y, var1.z, var2.x, var2.y, var2.z, var3, var3, var5, var6);
   }

   public void E(double var1, double var3, double var5, double var7, double var9, double var11, double var13, double var15, int var17, int var18) {
      double var19 = var7 - var1;
      double var21 = var9 - var3;
      double var23 = var11 - var5;
      if (!(var19 * var19 + var21 * var21 + var23 * var23 < 1.0E-10)) {
         double var25 = this.d.x - var1;
         double var27 = this.d.y - var3;
         double var29 = this.d.z - var5;
         double var31 = var21 * var29 - var23 * var27;
         double var33 = var23 * var25 - var19 * var29;
         double var35 = var19 * var27 - var21 * var25;
         double var37 = Math.sqrt(var31 * var31 + var33 * var33 + var35 * var35);
         if (var37 < 1.0E-8) {
            var31 = this.y.x;
            var33 = this.y.y;
            var35 = this.y.z;
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

   public void h(double var1, double var3, double var5, double var7, double var9, double var11, double var13, double var15, int var17, int var18) {
      this.E(var1, var3, var5, var7, var9, var11, var13 * 2.5, var15 * 2.5, vv.O(var17, 0.07), vv.O(var18, 0.07));
      this.E(var1, var3, var5, var7, var9, var11, var13 * 1.25, var15 * 1.25, vv.O(var17, 0.3), vv.O(var18, 0.3));
      this.E(
         var1,
         var3,
         var5,
         var7,
         var9,
         var11,
         var13 * 0.23,
         var15 * 0.23,
         vv.Q(var17, var17 & 0xFF000000 | 16777215, 0.48),
         vv.Q(var18, var18 & 0xFF000000 | 16777215, 0.48)
      );
   }

   public void n(x.c var1, boolean var2) {
      for (int var3 = 0; var3 < (var2 ? 3 : 1); var3++) {
         double var4 = !var2 ? 1.0 : (var3 == 0 ? 2.5 : (var3 == 1 ? 1.25 : 0.23));

         for (int var6 = 1; var6 < var1.l; var6++) {
            Vec3 var7 = var1.r[var6 - 1];
            Vec3 var8 = var1.r[var6];
            if (!(var7.distanceToSqr(this.d) > var1.e) && !(var7.distanceToSqr(var8) < 1.0E-10)) {
               Vec3 var9 = var1.k[var6 - 1];
               Vec3 var10 = var1.k[var6];
               double var11 = var9.x * var4;
               double var13 = var9.y * var4;
               double var15 = var9.z * var4;
               double var17 = var10.x * var4;
               double var19 = var10.y * var4;
               double var21 = var10.z * var4;
               int var23 = s(var1.W[var6 - 1], var3, var2);
               int var24 = s(var1.W[var6], var3, var2);
               this.vertex(var7.x + var11, var7.y + var13, var7.z + var15, var23);
               this.vertex(var8.x + var17, var8.y + var19, var8.z + var21, var24);
               this.vertex(var8.x - var17, var8.y - var19, var8.z - var21, var24);
               this.vertex(var7.x - var11, var7.y - var13, var7.z - var15, var23);
            }
         }
      }
   }

   public void Y(x.c var1, double var2) {
      var2 = Math.clamp(var2, 0.0, 0.95);

      for (int var4 = 1; var4 < var1.l; var4++) {
         Vec3 var5 = var1.r[var4 - 1];
         Vec3 var6 = var1.r[var4];
         if (!(var5.distanceToSqr(this.d) > var1.e) && !(var5.distanceToSqr(var6) < 1.0E-10)) {
            Vec3 var7 = var1.k[var4 - 1];
            Vec3 var8 = var1.k[var4];
            int var9 = var1.W[var4 - 1];
            int var10 = var1.W[var4];
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

   private static int s(int var0, int var1, boolean var2) {
      if (!var2) {
         return var0;
      } else {
         return var1 < 2 ? vv.O(var0, var1 == 0 ? 0.07 : 0.3) : vv.Q(var0, var0 & 0xFF000000 | 16777215, 0.48);
      }
   }

   public void b(EntityRenderState var1, Vec3 var2, double var3, double var5, double var7, int var9, int var10, boolean var11) {
      Matrix4f var12 = w7.e(var1, this.S);
      if (var12 != null) {
         q8.n(this, var12, var3, var5, var7, var9, var10, var11);
      } else {
         this.c(var2, var3, var5, var9, var10, var11);
      }
   }

   public void c(Vec3 var1, double var2, double var4, int var6, int var7, boolean var8) {
      for (int var9 = 0; var9 < 64; var9++) {
         this.vertex(var1.x, var1.y + var4, var1.z, var6);
         this.vertex(var1.x + Q[var9 + 1] * var2, var1.y, var1.z + R[var9 + 1] * var2, var7);
         this.vertex(var1.x + Q[var9] * var2, var1.y, var1.z + R[var9] * var2, var7);
         this.vertex(var1.x, var1.y + var4, var1.z, var6);
      }

      if (var8) {
         this.q(var1, var2, 0.009, vv.Q(var7, var7 & 0xFF000000 | 16777215, 0.4));
      }
   }

   public void q(Vec3 var1, double var2, double var4, int var6) {
      this.l(var1.x, var1.y, var1.z, var2, var4, var6);
   }

   public void l(double var1, double var3, double var5, double var7, double var9, int var11) {
      this.O(var1, var3, var5, var7, var9, var11, 0.0, 1.0, 0.0);
   }

   public void O(double var1, double var3, double var5, double var7, double var9, int var11, double var12, double var14, double var16) {
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
      VertexConsumer var36 = this.K.w(C[this.F.ordinal()]);
      int var37 = this.F == _0.v.Y ? 1 : 0;

      for (int var38 = 0; var38 < 4; var38++) {
         double var39 = Math.max(0.0, var7 + (var38 / 2.0 - 1.0) * var9);
         double var41 = Math.max(0.0, var7 + ((var38 + 1) / 2.0 - 1.0) * var9);
         double var43 = var38 == 0 ? 0.0 : (var38 == 2 ? 1.0 : 0.22);
         double var45 = var38 == 3 ? 0.0 : (var38 == 1 ? 1.0 : 0.22);
         int var47 = vv.O(var11, var43);
         int var48 = vv.O(var11, var45);
         if (var37 != 0) {
            var47 = H(var47);
            var48 = H(var48);
         }

         for (int var49 = 0; var49 < 64; var49++) {
            double var50 = var20 * Q[var49] + var30 * R[var49];
            double var52 = var22 * Q[var49] + var32 * R[var49];
            double var54 = var24 * Q[var49] + var34 * R[var49];
            double var56 = var20 * Q[var49 + 1] + var30 * R[var49 + 1];
            double var58 = var22 * Q[var49 + 1] + var32 * R[var49 + 1];
            double var60 = var24 * Q[var49 + 1] + var34 * R[var49 + 1];
            this.t(var36, var1 + var50 * var39, var3 + var52 * var39, var5 + var54 * var39, var47);
            this.t(var36, var1 + var56 * var39, var3 + var58 * var39, var5 + var60 * var39, var47);
            this.t(var36, var1 + var56 * var41, var3 + var58 * var41, var5 + var60 * var41, var48);
            this.t(var36, var1 + var50 * var41, var3 + var52 * var41, var5 + var54 * var41, var48);
         }
      }
   }

   public void c(double var1, double var3, double var5, double var7, double var9, double var11, double var13, int var15) {
      po.T(this.J, var7, var9, var11, var13);

      for (int var16 = 0; var16 < po.T(); var16++) {
         int var17 = po.e(this.J, var16, var15);

         for (int var18 = 0; var18 < 4; var18++) {
            int var19 = po.J(var16, var18) * 3;
            this.vertex(var1 + this.J[var19], var3 + this.J[var19 + 1], var5 + this.J[var19 + 2], var17);
         }
      }
   }

   public void b(double var1, double var3, double var5, double var7, double var9, int var11, int var12, boolean var13) {
      int var14 = var13 ? var12 * 2 : var12;

      for (int var15 = 0; var15 < var14; var15++) {
         double var16 = var9 + var15 * Math.PI * 2.0 / var14;
         double var18 = var9 + (var15 + 1) * Math.PI * 2.0 / var14;
         double var20 = var7 * (var13 && var15 % 2 == 1 ? 0.4 : 1.0);
         double var22 = var7 * (var13 && (var15 + 1) % 2 == 1 ? 0.4 : 1.0);
         this.vertex(var1, var3, var5, var11);
         this.vertex(
            var1 + this.y.x * Math.cos(var16) * var20 + this.x.x * Math.sin(var16) * var20,
            var3 + this.y.y * Math.cos(var16) * var20 + this.x.y * Math.sin(var16) * var20,
            var5 + this.y.z * Math.cos(var16) * var20 + this.x.z * Math.sin(var16) * var20,
            var11
         );
         this.vertex(
            var1 + this.y.x * Math.cos(var18) * var22 + this.x.x * Math.sin(var18) * var22,
            var3 + this.y.y * Math.cos(var18) * var22 + this.x.y * Math.sin(var18) * var22,
            var5 + this.y.z * Math.cos(var18) * var22 + this.x.z * Math.sin(var18) * var22,
            var11
         );
         this.vertex(var1, var3, var5, var11);
      }
   }

   private static int H(int var0) {
      double var1 = (var0 >>> 24) / 255.0;
      return var0 & 0xFF000000 | (int)((var0 >> 16 & 0xFF) * var1) << 16 | (int)((var0 >> 8 & 0xFF) * var1) << 8 | (int)((var0 & 0xFF) * var1);
   }

   public void r(RenderType var1, double var2, double var4, double var6, double var8, double var10, int var12) {
      VertexConsumer var13 = this.K.w(var1);
      double var14 = Math.cos(var10);
      double var16 = Math.sin(var10);

      for (int var18 = 0; var18 < 4; var18++) {
         double var19 = var18 != 0 && var18 != 3 ? 1.0 : -1.0;
         double var21 = var18 < 2 ? -1.0 : 1.0;
         double var23 = (var19 * var14 - var21 * var16) * var8;
         double var25 = (var19 * var16 + var21 * var14) * var8;
         var13.addVertex(
               this.b,
               (float)(var2 - this.d.x + this.y.x * var23 + this.x.x * var25),
               (float)(var4 - this.d.y + this.y.y * var23 + this.x.y * var25),
               (float)(var6 - this.d.z + this.y.z * var23 + this.x.z * var25)
            )
            .setUv((float)((var19 + 1.0) * 0.5), (float)((var21 + 1.0) * 0.5))
            .setColor(var12);
      }
   }

   public void K(double var1, double var3, double var5, double var7, double var9, int var11) {
      this.a(var1, var3, var5, var7, var9, var11, l.b.Y, 0.0, 0.0, 0.0);
   }

   public void a(double var1, double var3, double var5, double var7, double var9, int var11, l.b var12, double var13, double var15, double var17) {
      this.i(var1, var3, var5, var7, var9, var11, var12, var13, var15, var17, _0.j.U);
   }

   public void p(double var1, double var3, double var5, double var7, double var9, double var11, double var13, double var15, double var17, int var19, l.b var20) {
      int var21 = L(var20);
      VertexConsumer var22 = this.K.w(f[_0.j.U.ordinal()][this.F.ordinal()]);

      for (int var23 = 0; var23 < 4; var23++) {
         double var24 = var23 != 0 && var23 != 3 ? 1.0 : -1.0;
         double var26 = var23 < 2 ? -1.0 : 1.0;
         var22.addVertex(
               this.b,
               (float)(var1 - this.d.x + var7 * var24 + var13 * var26),
               (float)(var3 - this.d.y + var9 * var24 + var15 * var26),
               (float)(var5 - this.d.z + var11 * var24 + var17 * var26)
            )
            .setUv((float)((var21 + (var24 + 1.0) * 0.5) / 9.0), (float)((var26 + 1.0) * 0.5))
            .setColor(var19);
      }
   }

   private static int L(l.b var0) {
      return switch (var0) {
         case P -> 0;
         case y -> 1;
         case i -> 2;
         case Y, K -> 3;
         case x -> 4;
         case u, J -> 5;
         case r -> 6;
         case j -> 7;
         default -> 8;
      };
   }

   public void i(double var1, double var3, double var5, double var7, double var9, int var11, l.b var12, double var13, double var15, double var17, _0.j var19) {
      int var20 = L(var12);
      double var21 = 1.0;
      if (var12 == l.b.L || var12 == l.b.i) {
         var9 = Math.atan2(var13 * this.x.x + var15 * this.x.y + var17 * this.x.z, var13 * this.y.x + var15 * this.y.y + var17 * this.y.z);
         var21 = var12 == l.b.L ? 3.0 : 1.6;
      }

      VertexConsumer var23 = this.K.w((this.e ? W : f)[var19.ordinal()][this.F.ordinal()]);
      double var24 = Math.cos(var9);
      double var26 = Math.sin(var9);

      for (int var28 = 0; var28 < 4; var28++) {
         double var29 = var28 != 0 && var28 != 3 ? 1.0 : -1.0;
         double var31 = var28 < 2 ? -1.0 : 1.0;
         double var33 = (var29 * var21 * var24 - var31 * var26) * var7;
         double var35 = (var29 * var21 * var26 + var31 * var24) * var7;
         var23.addVertex(
               this.b,
               (float)(var1 - this.d.x + this.y.x * var33 + this.x.x * var35),
               (float)(var3 - this.d.y + this.y.y * var33 + this.x.y * var35),
               (float)(var5 - this.d.z + this.y.z * var33 + this.x.z * var35)
            )
            .setUv((float)((var20 + (var29 + 1.0) * 0.5) / 9.0), (float)((var31 + 1.0) * 0.5))
            .setColor(var11);
      }
   }

   public void x(double var1, double var3, double var5, double var7, double var9, double var11, double var13, double var15, int var17) {
      ii.Y(B, var7, var9, var11, var13, var15);
      VertexConsumer var18 = this.K.w(Z);

      for (int var19 = 0; var19 < 18; var19++) {
         int var20 = ii.G(B, var19, var17);

         for (int var21 = 0; var21 < 4; var21++) {
            int var22 = ii.a(var19, var21) * 3;
            var18.addVertex(this.b, (float)(var1 - this.d.x + B[var22]), (float)(var3 - this.d.y + B[var22 + 1]), (float)(var5 - this.d.z + B[var22 + 2]))
               .setUv((float)ii.t(var19, var21), (float)ii.c(var19, var21))
               .setColor(var20);
         }
      }
   }

   public void c(Vec3 var1, double var2, double var4, double var6, double var8, int var10, int var11, double var12) {
      int var14 = Math.clamp((int)Math.ceil(Math.abs(var8 - var6) * 12.0), 4, 96);

      for (int var15 = 0; var15 < var14; var15++) {
         double var16 = (double)var15 / var14;
         double var18 = (double)(var15 + 1) / var14;
         double var20 = var6 + (var8 - var6) * var16;
         double var22 = var6 + (var8 - var6) * var18;
         int var24 = vv.Q(var10, var11, var16);
         int var25 = vv.Q(var10, var11, var18);
         double var26 = var1.y + Math.sin(var20 * 2.0) * var12;
         double var28 = var1.y + Math.sin(var22 * 2.0) * var12;
         this.vertex(var1.x + Math.cos(var20) * var2, var26, var1.z + Math.sin(var20) * var2, var24);
         this.vertex(var1.x + Math.cos(var22) * var2, var28, var1.z + Math.sin(var22) * var2, var25);
         this.vertex(var1.x + Math.cos(var22) * (var2 + var4), var28, var1.z + Math.sin(var22) * (var2 + var4), vv.O(var25, 0.15));
         this.vertex(var1.x + Math.cos(var20) * (var2 + var4), var26, var1.z + Math.sin(var20) * (var2 + var4), vv.O(var24, 0.15));
      }
   }

   public void T(Vec3 var1, double var2, double var4, int var6) {
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
      Z = RenderType.create(
         "larp_cash",
         RenderSetup.builder(var0)
            .withTexture("Sampler0", Identifier.fromNamespaceAndPath("larp-client", "textures/particles/cash.png"))
            .sortOnUpload()
            .createRenderSetup()
      );

      for (_0.v var4 : _0.v.values()) {
         BlendFunction var5 = switch (var4) {
            case z -> BlendFunction.TRANSLUCENT;
            case c -> BlendFunction.LIGHTNING;
            case Y -> BlendFunction.TRANSLUCENT_PREMULTIPLIED_ALPHA;
         };
         Builder var6 = RenderPipeline.builder(new Snippet[]{RenderPipelines.GUI_TEXTURED_SNIPPET})
            .withLocation(Identifier.fromNamespaceAndPath("larp-client", "sprites_" + var4.name().toLowerCase(Locale.ROOT)))
            .withFragmentShader(Identifier.fromNamespaceAndPath("larp-client", "particle/sprite"))
            .withDepthStencilState(new DepthStencilState(CompareOp.GREATER_THAN_OR_EQUAL, false))
            .withColorTargetState(new ColorTargetState(var5))
            .withCull(false);
         if (var4 == _0.v.Y) {
            var6.withShaderDefine("PREMULTIPLIED");
         }

         RenderPipeline var7 = var6.build();
         d3.R(var6);
         RenderPipeline var8 = var6.withLocation(Identifier.fromNamespaceAndPath("larp-client", "soft_sprites_" + var4.name().toLowerCase(Locale.ROOT)))
            .build();

         for (_0.j var12 : _0.j.values()) {
            f[var12.ordinal()][var4.ordinal()] = RenderType.create(
               "larp_sprites_" + var12 + "_" + var4,
               RenderSetup.builder(var7)
                  .withTexture("Sampler0", Identifier.fromNamespaceAndPath("larp-client", "textures/particles/" + var12.s))
                  .sortOnUpload()
                  .createRenderSetup()
            );
         }

         for (_0.j var17 : _0.j.values()) {
            W[var17.ordinal()][var4.ordinal()] = RenderType.create(
               "larp_soft_sprites_" + var17 + "_" + var4,
               RenderSetup.builder(var8)
                  .withTexture("Sampler0", Identifier.fromNamespaceAndPath("larp-client", "textures/particles/" + var17.s))
                  .sortOnUpload()
                  .createRenderSetup()
            );
         }

         C[var4.ordinal()] = RenderType.create(
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

      Q = new double[65];
      R = new double[65];

      for (int var13 = 0; var13 <= 64; var13++) {
         Q[var13] = Math.cos(var13 * Math.PI / 32.0);
         R[var13] = Math.sin(var13 * Math.PI / 32.0);
      }
   }
}
