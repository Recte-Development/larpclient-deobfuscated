package com.larpclient;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.renderpearl.api.pipeline.CompareOp;
import com.mojang.renderpearl.api.pipeline.DepthStencilState;
import com.mojang.renderpearl.api.pipeline.RenderPipeline;
import com.mojang.renderpearl.api.pipeline.RenderPipeline.Builder;
import com.mojang.renderpearl.api.pipeline.RenderPipeline.Snippet;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.Font.DisplayMode;
import net.minecraft.client.gui.Font.GlyphVisitor;
import net.minecraft.client.gui.Font.PreparedText;
import net.minecraft.client.gui.font.TextRenderable;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.rendertype.PreparedRenderType;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Quaternionfc;
import org.joml.Vector3f;

public final class od implements AutoCloseable, GlyphVisitor {
   private static final double B = 0.55;
   private static final int r = 15728880;
   private static final int R = 256;
   private static final Comparator<od.y> c = Comparator.<od.y>comparingDouble(var0 -> var0.X).reversed();
   private final od.y[] l = new od.y[256];
   private final od.y[] Q = new od.y[256];
   private int e;
   private static final double t = 0.01890625;
   private final LinkedHashMap<String, od.v> M = new LinkedHashMap<>(64, 0.75F, true);
   private final Matrix4f L = new Matrix4f();
   private final Matrix4f H = new Matrix4f();
   private od.w y;
   private int K;
   private int A;
   private float b;
   private boolean q;
   private final Quaternionf x = new Quaternionf();
   private final Vector3f Z = new Vector3f();
   private final cv a = new cv();
   private final t6[] I = new t6[96];
   private final double[] v = new double[96];
   private final RenderPipeline[][] m = new RenderPipeline[3][2];
   private xh P;
   private Font F;
   private int u = -1;
   private int d = -1;
   private int f;
   private boolean o;
   private int g;

   public void W(_9 var1, CameraRenderState var2) {
      this.g = this.U(var1, var2);
   }

   public void H(List<od.l> var1, CameraRenderState var2) {
      this.Y();
      if (!this.o || this.d != m5.k) {
         try {
            this.F();
            this.y(var1, var2);
         } catch (RuntimeException var4) {
            this.g(var4);
         }
      }
   }

   private void y(List<od.l> var1, CameraRenderState var2) {
      for (od.l var4 : var1) {
         if (this.e == 256) {
            break;
         }

         if (var4 != null
            && var4.text() != null
            && !var4.text().isBlank()
            && Double.isFinite(var4.x())
            && Double.isFinite(var4.y())
            && Double.isFinite(var4.z())
            && Double.isFinite(var4.scale())
            && !(var4.scale() <= 0.0)
            && var4.color() >>> 24 != 0
            && (
               !var4.worldAligned()
                  || Float.isFinite(var4.qx())
                     && Float.isFinite(var4.qy())
                     && Float.isFinite(var4.qz())
                     && Float.isFinite(var4.qw())
                     && !(var4.qx() * var4.qx() + var4.qy() * var4.qy() + var4.qz() * var4.qz() + var4.qw() * var4.qw() < 1.0E-4F)
            )) {
            od.v var5 = this.X(var4.text());
            od.y var6 = this.l[this.e];
            if (var6 == null) {
               this.l[this.e] = var6 = new od.y();
            }

            var6.E = var4;
            var6.Z = var5;
            double var7 = var4.x() - var2.pos.x;
            double var9 = var4.y() - var2.pos.y;
            double var11 = var4.z() - var2.pos.z;
            var6.X = var7 * var7 + var9 * var9 + var11 * var11;
            this.Q[this.e++] = var6;
         }
      }

      Arrays.sort(this.Q, 0, this.e, c);
   }

   private void Y() {
      for (int var1 = 0; var1 < this.e; var1++) {
         this.Q[var1].E = null;
         this.Q[var1].Z = null;
         this.Q[var1] = null;
      }

      this.e = 0;
   }

   public void j(PoseStack var1, CameraRenderState var2, boolean var3) {
      this.i(var1, var2, var3, true, 1.0);
   }

   public void i(PoseStack var1, CameraRenderState var2, boolean var3, boolean var4, double var5) {
      if (this.e != 0 && !this.o && this.P != null) {
         try {
            this.f = var3 ? 1 : 0;
            this.Z.set(0.0F, 0.0F, -1.0F).rotate(var2.orientation);

            for (int var7 = 0; var7 < this.e; var7++) {
               od.y var8 = this.Q[var7];
               od.l var9 = var8.E;
               od.v var10 = var8.Z;
               double var11 = var9.x() - var2.pos.x;
               double var13 = var9.y() - var2.pos.y;
               double var15 = var9.z() - var2.pos.z;
               double var17 = Math.hypot(var10.width + 12, var10.height + 8) * 0.01890625 * var9.scale();
               if (!(var11 * this.Z.x + var13 * this.Z.y + var15 * this.Z.z < -var17)) {
                  this.L.set(var1.last().pose()).translate((float)var11, (float)var13, (float)var15);
                  this.A(var9, var2.orientation);
                  float var19 = (float)(0.01890625 * Math.min(32.0, var9.scale()));
                  this.L.scale(var19, -var19, var19).translate(0.0F, -var10.centerY, 0.0F);
                  this.y = var9.motion();
                  this.A = var10.width;
                  this.b = var10.height / 9.0F;
                  if (var4) {
                     this.L.translate(1.0F, 1.0F, -0.03F);
                     this.a.P = this.K = _3.A(-15724520, (var9.color() >>> 24) / 255.0 * 0.65 * var5);
                     this.q = (boolean)1;
                     var10.text.visit(this);
                     this.L.translate(-1.0F, -1.0F, 0.03F);
                  }

                  this.a.P = this.K = _3.A(var9.color(), (var9.color() >>> 24) / 255.0 * var5);
                  this.q = (boolean)0;
                  var10.text.visit(this);
               }
            }

            this.P.g(this::S);
         } catch (RuntimeException var23) {
            this.g(var23);
         } finally {
            this.a.S = null;
            this.y = null;
         }
      }
   }

   private boolean h(TextRenderable var1) {
      od.w var2 = this.y;
      float var3 = (var1.left() + var1.right()) * 0.5F;
      float var4 = (var1.top() + var1.bottom()) * 0.5F;
      double var5 = this.A <= 0 ? 0.0 : Math.clamp((var3 + this.A * 0.5) / this.A, 0.0, 1.0);
      double var7 = 1.0;
      double var9 = 0.0;
      double var11 = 0.0;
      double var13 = 0.0;
      double var15 = 0.0;
      double var17 = 1.0;
      double var19 = this.b;
      if (var2.kind() != 0) {
         var7 = Math.clamp((var2.reveal() - var5 * 0.55) / 0.44999999999999996, 0.0, 1.0);
         var9 = Math.clamp((var2.exit() - var5 * 0.4) / 0.6, 0.0, 1.0);
         double var21 = 1.0 - Math.pow(1.0 - var7, 3.0);
         if (var2.kind() == 1) {
            double var23 = 1.0 + 2.2 * Math.pow(var7 - 1.0, 3.0) + 1.2 * Math.pow(var7 - 1.0, 2.0);
            var13 = var19 * (7.0 * (1.0 - var21) + 0.7 * Math.sin(var2.time() * 2.4 - var5 * 6.0) * var21 - 10.0 * var9 * var9);
            var11 = var19 * (var5 - 0.5) * 8.0 * var9 * var9;
            var15 = (var5 - 0.5) * 0.6 * var9;
            var17 = (0.4 + 0.6 * var23) * (1.0 - 0.3 * var9);
         } else {
            var11 = var19 * -6.0 * (1.0 - var21);
            var13 = var19 * (1.4 * Math.sin(var2.time() * 3.0 - var5 * 7.0) * var21 + 7.0 * var9 * var9);
         }
      }

      double var26 = o5.v(var7) * (1.0 - o5.v(var9));
      if (var26 < 0.00392156862745098) {
         return false;
      }

      int var27 = this.K;
      if (!this.q && var2.accent() >>> 24 != 0) {
         double var24 = 0.5 - 0.5 * Math.cos(var5 * 2.4 - var2.time() * 1.2);
         var27 = _3.D(var27, var2.accent() & 16777215 | var27 & 0xFF000000, var24 * 0.85);
      }

      this.a.P = _3.U(var27, var26);
      this.H.set(this.L).translate(var3 + (float)var11, var4 + (float)var13, 0.0F).rotateZ((float)var15).scale((float)var17).translate(-var3, -var4, 0.0F);
      return true;
   }

   private void A(od.l var1, Quaternionfc var2) {
      this.L.rotate((Quaternionfc)(var1.worldAligned() ? this.x.set(var1.qx(), var1.qy(), var1.qz(), var1.qw()).normalize() : var2));
   }

   private void g(RuntimeException var1) {
      this.o = (boolean)1;
      this.Y();
      this.M.clear();
      if (this.P != null) {
         this.P.close();
         this.P = null;
      }

      mt.P("WorldText", "World label stage failed; isolated until resource reload or re-enable", var1);
   }

   private void F() {
      Font var1 = Minecraft.getInstance().font;
      if (this.F != var1 || this.u != k6.O()) {
         this.M.clear();
         this.F = var1;
         this.u = k6.O();
      }

      if (this.d != m5.k) {
         this.M.clear();
         this.o = (boolean)0;
         this.d = m5.k;

         for (int var2 = 0; var2 < 3; var2++) {
            for (int var3 = 0; var3 < 2; var3++) {
               Builder var4 = RenderPipeline.builder(new Snippet[]{RenderPipelines.WORLD_TEXT_SNIPPET})
                  .withLocation(Identifier.fromNamespaceAndPath("larp-client", "world_text_" + var2 + "_" + var3))
                  .withVertexShader("core/text")
                  .withFragmentShader(Identifier.fromNamespaceAndPath("larp-client", "world_text"))
                  .withCull(false)
                  .withDepthStencilState(
                     var2 == 1
                        ? Optional.empty()
                        : Optional.of(new DepthStencilState(var2 == 2 ? CompareOp.LESS_THAN : CompareOp.GREATER_THAN_OR_EQUAL, false))
                  );
               if (var3 == 1) {
                  var4.withShaderDefine("IS_GRAYSCALE");
               }

               this.m[var2][var3] = var4.build();
               ea.z(this.m[var2][var3]);
            }
         }

         mt.K("WorldText", "World glyph pipelines ready: normal, through walls, soft occlusion");
      }

      if (this.P == null) {
         this.P = new xh();
      }
   }

   private od.v X(String var1) {
      od.v var2 = this.M.get(var1);
      if (var2 != null) {
         return var2;
      }

      Component var3 = k6.I(var1);
      int var4 = this.F.width(var3);
      PreparedText var5 = this.F.prepareText(var3.getVisualOrderText(), -var4 / 2.0F, 0.0F, -1, false, false, 0);
      ScreenRectangle var6 = var5.bounds();
      od.v var7 = new od.v(var5, var4, var6 == null ? 32 : var6.height(), var6 == null ? 16.0F : var6.top() + var6.height() / 2.0F);
      if (this.M.size() >= 256) {
         this.M.remove(this.M.keySet().iterator().next());
      }

      this.M.put(var1, var7);
      return var7;
   }

   public boolean Y() {
      return !this.o;
   }

   public int k(String var1) {
      if (var1.isEmpty()) {
         return 0;
      }

      this.F();
      return this.X(var1).width;
   }

   public void M(PoseStack var1, CameraRenderState var2, _9 var3, mf var4, boolean var5) {
      if (!this.o || this.d != m5.k) {
         if (!var5 || var4.S && !(var4.V <= 0.0)) {
            long var6 = kx.G();

            try {
               this.F();
               int var8 = this.g;
               if (var4.W == mf.f.X) {
                  this.f = 2;
                  this.V(var1, var2, var3, var4, var5, var8, 0.18);
               }

               this.f = var4.W == mf.f.h ? 1 : 0;
               this.V(var1, var2, var3, var4, var5, var8, 1.0);
            } catch (RuntimeException var12) {
               this.o = (boolean)1;
               if (this.P != null) {
                  this.P.close();
                  this.P = null;
               }

               this.M.clear();
               mt.P("WorldText", "World text render failed; isolated until resource reload or re-enable", var12);
            } finally {
               this.a.S = null;
               kx.u("WorldText", var6);
            }
         }
      }
   }

   private int U(_9 var1, CameraRenderState var2) {
      int var3 = 0;
      double var4 = Math.sin(var1.n);
      double var6 = Math.cos(var1.n);
      Vec3 var8 = var2.pos;

      for (t6 var12 : var1.b()) {
         if (var12.g && !(var12.G < 0.00392156862745098)) {
            double var13 = var12.r + var12.l;
            double var15 = var12.H + var12.z;
            double var17 = var1.t + var13 * var6 + var15 * var4 - var8.x;
            double var19 = var1.z + var12.N + var12.L - var8.y;
            double var21 = var1.P + var15 * var6 - var13 * var4 - var8.z;
            double var23 = var17 * var17 + var19 * var19 + var21 * var21;

            int var25;
            for (var25 = var3++; var25 > 0 && this.v[var25 - 1] < var23; var25--) {
               this.v[var25] = this.v[var25 - 1];
               this.I[var25] = this.I[var25 - 1];
            }

            this.v[var25] = var23;
            this.I[var25] = var12;
         }
      }

      return var3;
   }

   private void V(PoseStack var1, CameraRenderState var2, _9 var3, mf var4, boolean var5, int var6, double var7) {
      double var9 = Math.sin(var3.n);
      double var11 = Math.cos(var3.n);
      Vec3 var13 = var2.pos;
      this.Z.set(0.0F, 0.0F, -1.0F).rotate(var2.orientation);

      for (int var14 = 0; var14 < var6; var14++) {
         t6 var15 = this.I[var14];
         double var16 = Math.sqrt(this.v[var14]);
         if (!(var16 >= var4.M)) {
            double var18 = var15.G * var4.t * var7;
            if (var4.N) {
               double var20 = Math.min(var4.v, var4.M - 0.1);
               var18 *= 1.0 - o5.v((var16 - var20) / (var4.M - var20));
            }

            var18 *= o5.v(var16 / 0.8);
            if (var5) {
               var18 *= var4.V;
            }
            int var38 = switch (var4.h) {
               case Y -> var4.G;
               case O -> _3.D(var4.G, var4.D, (Math.sin(var15.R) + 1.0) * 0.5);
               case N -> _3.Z((float)((var3.t() * 0.035 + var15.R / (Math.PI * 2)) % 1.0), 0.45F, 1.0F);
            };
            var38 = _3.U(var38, var18);
            if (var38 >>> 24 >= 1) {
               od.v var21 = this.X(var15.W);
               double var22 = var15.r + var15.l;
               double var24 = var15.H + var15.z;
               double var26 = var3.t + var22 * var11 + var24 * var9 - var13.x;
               double var28 = var3.z + var15.N + var15.L - var13.y;
               double var30 = var3.P + var24 * var11 - var22 * var9 - var13.z;
               double var32 = Math.hypot(var21.width + 12, var21.height + 8) * 0.01890625 * var4.Q * (1.0 + var15.u * var4.E) * var15.F * 0.5;
               if (!(var26 * this.Z.x + var28 * this.Z.y + var30 * this.Z.z < -var32)) {
                  this.L.set(var1.last().pose()).translate((float)var26, (float)var28, (float)var30);
                  switch (var4.K) {
                     case M:
                        this.L.rotate(var2.orientation);
                        break;
                     case i:
                        this.L.rotateY((float)Math.atan2(-var26, -var30));
                        break;
                     case j:
                        this.L.rotateY((float)var15.E).rotateX((float)var15.p);
                  }

                  double var34 = var4.z == mf.k.X ? 0.0 : var15.I;
                  this.L.rotateZ((float)(var15.J + var34));
                  float var36 = (float)(0.01890625 * var4.Q * (1.0 + var15.u * var4.E) * var15.F);
                  this.L.scale(var36, -var36, var36).translate(0.0F, -var21.centerY, 0.0F);
                  if (var4.y && !var5) {
                     this.L.translate(2.0F, 2.0F, -0.03F);
                     this.a.P = _3.A(-15724520, (var38 >>> 24) / 255.0 * 0.5);
                     var21.text.visit(this);
                     this.L.translate(-2.0F, -2.0F, 0.03F);
                  }

                  this.a.P = var38;
                  var21.text.visit(this);
               }
            }
         }
      }

      this.P.g(this::S);
   }

   private PreparedRenderType S(PreparedRenderType var1) {
      int var2 = var1.pipeline() == RenderPipelines.TEXT_GRAYSCALE ? 1 : 0;
      return hj.I(var1, this.m[this.f][var2]);
   }

   public void acceptRenderable(TextRenderable var1) {
      this.a.S = this.P.U(var1.renderType(DisplayMode.NORMAL));
      if (this.y == null) {
         var1.render(this.L, this.a, 15728880, false);
      } else if (this.h(var1)) {
         var1.render(this.H, this.a, 15728880, false);
      }
   }

   public void J() {
      if (this.P != null) {
         this.P.s();
      }
   }

   @Override
   public void close() {
      this.M.clear();
      this.Y();
      Arrays.fill(this.I, null);
      if (this.P != null) {
         this.P.close();
         this.P = null;
      }

      this.o = (boolean)0;
      this.d = -1;
   }

   public record l(
      String text, double x, double y, double z, double scale, int color, float qx, float qy, float qz, float qw, boolean worldAligned, od.w motion
   ) {
      public l(String var1, double var2, double var4, double var6, double var8, int var10) {
         this(var1, var2, var4, var6, var8, var10, 0.0F, 0.0F, 0.0F, 1.0F, false);
      }

      public l(String var1, double var2, double var4, double var6, double var8, int var10, float var11, float var12, float var13, float var14, boolean var15) {
         this(var1, var2, var4, var6, var8, var10, var11, var12, var13, var14, var15, null);
      }
   }

   private record v(PreparedText text, int width, int height, float centerY) {
   }

   public record w(int kind, double reveal, double exit, double time, int accent) {
      public static final int FADE = 0;
      public static final int POP = 1;
      public static final int WAVE = 2;
   }

   private static final class y {
      od.l E;
      od.v Z;
      double X;
   }
}
