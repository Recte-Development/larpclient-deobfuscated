package com.larpclient;

import com.mojang.blaze3d.pipeline.DepthStencilState;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.pipeline.RenderPipeline.Builder;
import com.mojang.blaze3d.pipeline.RenderPipeline.Snippet;
import com.mojang.blaze3d.platform.CompareOp;
import com.mojang.blaze3d.vertex.PoseStack;
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

public final class b0 implements AutoCloseable, GlyphVisitor {
   private static final double M = 0.55;
   private static final int D = 15728880;
   private static final int U = 256;
   private static final Comparator<b0.p> J = Comparator.<b0.p>comparingDouble(var0 -> var0.f).reversed();
   private final b0.p[] B = new b0.p[256];
   private final b0.p[] v = new b0.p[256];
   private int q;
   private static final double d = 0.01890625;
   private final LinkedHashMap<String, b0.f> Y = new LinkedHashMap<>(64, 0.75F, true);
   private final Matrix4f m = new Matrix4f();
   private final Matrix4f H = new Matrix4f();
   private b0.b h;
   private int z;
   private int F;
   private float e;
   private boolean C;
   private final Quaternionf S = new Quaternionf();
   private final Vector3f A = new Vector3f();
   private final wd g = new wd();
   private final wq[] t = new wq[96];
   private final double[] x = new double[96];
   private final RenderPipeline[][] p = new RenderPipeline[3][2];
   private hh f;
   private Font Z;
   private int X = -1;
   private int k = -1;
   private int G;
   private boolean O;
   private int j;

   public void z(z8 var1, CameraRenderState var2) {
      this.j = this.q(var1, var2);
   }

   public void Q(List<b0.k> var1, CameraRenderState var2) {
      this.k();
      if (!this.O || this.k != wa.s) {
         try {
            this.l();
            this.s(var1, var2);
         } catch (RuntimeException var4) {
            this.y(var4);
         }
      }
   }

   private void s(List<b0.k> var1, CameraRenderState var2) {
      for (b0.k var4 : var1) {
         if (this.q == 256) {
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
            b0.f var5 = this.Y(var4.text());
            b0.p var6 = this.B[this.q];
            if (var6 == null) {
               this.B[this.q] = var6 = new b0.p();
            }

            var6.c = var4;
            var6.X = var5;
            double var7 = var4.x() - var2.pos.x;
            double var9 = var4.y() - var2.pos.y;
            double var11 = var4.z() - var2.pos.z;
            var6.f = var7 * var7 + var9 * var9 + var11 * var11;
            this.v[this.q++] = var6;
         }
      }

      Arrays.sort(this.v, 0, this.q, J);
   }

   private void k() {
      for (int var1 = 0; var1 < this.q; var1++) {
         this.v[var1].c = null;
         this.v[var1].X = null;
         this.v[var1] = null;
      }

      this.q = 0;
   }

   public void P(PoseStack var1, CameraRenderState var2, boolean var3) {
      this.f(var1, var2, var3, true, 1.0);
   }

   public void f(PoseStack var1, CameraRenderState var2, boolean var3, boolean var4, double var5) {
      if (this.q != 0 && !this.O && this.f != null) {
         try {
            this.G = var3 ? 1 : 0;
            this.A.set(0.0F, 0.0F, -1.0F).rotate(var2.orientation);

            for (int var7 = 0; var7 < this.q; var7++) {
               b0.p var8 = this.v[var7];
               b0.k var9 = var8.c;
               b0.f var10 = var8.X;
               double var11 = var9.x() - var2.pos.x;
               double var13 = var9.y() - var2.pos.y;
               double var15 = var9.z() - var2.pos.z;
               double var17 = Math.hypot(var10.width + 12, var10.height + 8) * 0.01890625 * var9.scale();
               if (!(var11 * this.A.x + var13 * this.A.y + var15 * this.A.z < -var17)) {
                  this.m.set(var1.last().pose()).translate((float)var11, (float)var13, (float)var15);
                  this.J(var9, var2.orientation);
                  float var19 = (float)(0.01890625 * Math.min(32.0, var9.scale()));
                  this.m.scale(var19, -var19, var19).translate(0.0F, -var10.centerY, 0.0F);
                  this.h = var9.motion();
                  this.F = var10.width;
                  this.e = var10.height / 9.0F;
                  if (var4) {
                     this.m.translate(1.0F, 1.0F, -0.03F);
                     this.g.y = this.z = uh.H(-15724520, (var9.color() >>> 24) / 255.0 * 0.65 * var5);
                     this.C = (boolean)1;
                     var10.text.visit(this);
                     this.m.translate(-1.0F, -1.0F, 0.03F);
                  }

                  this.g.y = this.z = uh.H(var9.color(), (var9.color() >>> 24) / 255.0 * var5);
                  this.C = (boolean)0;
                  var10.text.visit(this);
               }
            }

            this.f.K(this::O);
         } catch (RuntimeException var23) {
            this.y(var23);
         } finally {
            this.g.t = null;
            this.h = null;
         }
      }
   }

   private boolean n(TextRenderable var1) {
      b0.b var2 = this.h;
      float var3 = (var1.left() + var1.right()) * 0.5F;
      float var4 = (var1.top() + var1.bottom()) * 0.5F;
      double var5 = this.F <= 0 ? 0.0 : Math.clamp((var3 + this.F * 0.5) / this.F, 0.0, 1.0);
      double var7 = 1.0;
      double var9 = 0.0;
      double var11 = 0.0;
      double var13 = 0.0;
      double var15 = 0.0;
      double var17 = 1.0;
      double var19 = this.e;
      if (var2.kind() != 0) {
         var7 = Math.clamp((var2.reveal() - var5 * 0.55) / 0.44999999999999996, 0.0, 1.0);
         var9 = Math.clamp((var2.exit() - var5 * 0.4) / 0.6, 0.0, 1.0);
         double var21 = 1.0 - Math.pow(1.0 - var7, 3.0);
         if (var2.kind() == 3) {
            double var23 = X(var7);
            double var25 = Math.sin(var5 * 91.7 + 1.3) * 0.55;
            var13 = var19 * 9.0 * (-12.0 * (1.0 - var23) + 14.0 * var9 * var9);
            var15 = var25 * (1.0 - var23) + var25 * 1.6 * var9;
         } else if (var2.kind() == 1) {
            double var28 = 1.0 + 2.2 * Math.pow(var7 - 1.0, 3.0) + 1.2 * Math.pow(var7 - 1.0, 2.0);
            var13 = var19 * (7.0 * (1.0 - var21) + 0.7 * Math.sin(var2.time() * 2.4 - var5 * 6.0) * var21 - 10.0 * var9 * var9);
            var11 = var19 * (var5 - 0.5) * 8.0 * var9 * var9;
            var15 = (var5 - 0.5) * 0.6 * var9;
            var17 = (0.4 + 0.6 * var28) * (1.0 - 0.3 * var9);
         } else {
            var11 = var19 * -6.0 * (1.0 - var21);
            var13 = var19 * (1.4 * Math.sin(var2.time() * 3.0 - var5 * 7.0) * var21 + 7.0 * var9 * var9);
         }
      }

      double var27 = var2.kind() == 3 ? (var7 <= 0.0 ? 0.0 : l.F(var7 * 5.0)) * (1.0 - l.F(var9 * 1.15)) : l.F(var7) * (1.0 - l.F(var9));
      if (var27 < 0.00392156862745098) {
         return false;
      }

      int var29 = this.z;
      if (!this.C && var2.accent() >>> 24 != 0) {
         double var24 = 0.5 - 0.5 * Math.cos(var5 * 2.4 - var2.time() * 1.2);
         var29 = uh.q(var29, var2.accent() & 16777215 | var29 & 0xFF000000, var24 * 0.85);
      }

      this.g.y = uh.F(var29, var27);
      this.H.set(this.m).translate(var3 + (float)var11, var4 + (float)var13, 0.0F).rotateZ((float)var15).scale((float)var17).translate(-var3, -var4, 0.0F);
      return true;
   }

   public static double X(double var0) {
      var0 = Math.clamp(var0, 0.0, 1.0);
      double var2 = 7.5625;
      double var4 = 2.75;
      if (var0 < 1.0 / var4) {
         return var2 * var0 * var0;
      } else if (var0 < 2.0 / var4) {
         var0 -= 1.5 / var4;
         return var2 * var0 * var0 + 0.75;
      } else if (var0 < 2.5 / var4) {
         var0 -= 2.25 / var4;
         return var2 * var0 * var0 + 0.9375;
      } else {
         var0 -= 2.625 / var4;
         return var2 * var0 * var0 + 0.984375;
      }
   }

   private void J(b0.k var1, Quaternionfc var2) {
      this.m.rotate((Quaternionfc)(var1.worldAligned() ? this.S.set(var1.qx(), var1.qy(), var1.qz(), var1.qw()).normalize() : var2));
   }

   private void y(RuntimeException var1) {
      this.O = (boolean)1;
      this.k();
      this.Y.clear();
      if (this.f != null) {
         this.f.close();
         this.f = null;
      }

      com.larpclient.h.m("WorldText", "World label stage failed; isolated until resource reload or re-enable", var1);
   }

   private void l() {
      Font var1 = Minecraft.getInstance().font;
      if (this.Z != var1 || this.X != bj.X()) {
         this.Y.clear();
         this.Z = var1;
         this.X = bj.X();
      }

      if (this.k != wa.s) {
         this.Y.clear();
         this.O = (boolean)0;
         this.k = wa.s;

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

               this.p[var2][var3] = var4.build();
               ai.U(this.p[var2][var3]);
            }
         }

         com.larpclient.h.A("WorldText", "World glyph pipelines ready: normal, through walls, soft occlusion");
      }

      if (this.f == null) {
         this.f = new hh();
      }
   }

   private b0.f Y(String var1) {
      b0.f var2 = this.Y.get(var1);
      if (var2 != null) {
         return var2;
      }

      Component var3 = bj.h(var1);
      int var4 = this.Z.width(var3);
      PreparedText var5 = this.Z.prepareText(var3.getVisualOrderText(), -var4 / 2.0F, 0.0F, -1, false, false, 0);
      ScreenRectangle var6 = var5.bounds();
      b0.f var7 = new b0.f(var5, var4, var6 == null ? 32 : var6.height(), var6 == null ? 16.0F : var6.top() + var6.height() / 2.0F);
      if (this.Y.size() >= 256) {
         this.Y.remove(this.Y.keySet().iterator().next());
      }

      this.Y.put(var1, var7);
      return var7;
   }

   public boolean M() {
      return !this.O;
   }

   public int B(String var1) {
      if (var1.isEmpty()) {
         return 0;
      }

      this.l();
      return this.Y(var1).width;
   }

   public void z(PoseStack var1, CameraRenderState var2, z8 var3, mo var4, boolean var5) {
      if (!this.O || this.k != wa.s) {
         if (!var5 || var4.z && !(var4.t <= 0.0)) {
            long var6 = zj.m();

            try {
               this.l();
               int var8 = this.j;
               if (var4.S == mo.v.g) {
                  this.G = 2;
                  this.Z(var1, var2, var3, var4, var5, var8, 0.18);
               }

               this.G = var4.S == mo.v.o ? 1 : 0;
               this.Z(var1, var2, var3, var4, var5, var8, 1.0);
            } catch (RuntimeException var12) {
               this.O = (boolean)1;
               if (this.f != null) {
                  this.f.close();
                  this.f = null;
               }

               this.Y.clear();
               com.larpclient.h.m("WorldText", "World text render failed; isolated until resource reload or re-enable", var12);
            } finally {
               this.g.t = null;
               zj.l("WorldText", var6);
            }
         }
      }
   }

   private int q(z8 var1, CameraRenderState var2) {
      int var3 = 0;
      double var4 = Math.sin(var1.z);
      double var6 = Math.cos(var1.z);
      Vec3 var8 = var2.pos;

      for (wq var12 : var1.C()) {
         if (var12.l && !(var12.O < 0.00392156862745098)) {
            double var13 = var12.n + var12.C;
            double var15 = var12.x + var12.a;
            double var17 = var1.X + var13 * var6 + var15 * var4 - var8.x;
            double var19 = var1.H + var12.r + var12.L - var8.y;
            double var21 = var1.F + var15 * var6 - var13 * var4 - var8.z;
            double var23 = var17 * var17 + var19 * var19 + var21 * var21;

            int var25;
            for (var25 = var3++; var25 > 0 && this.x[var25 - 1] < var23; var25--) {
               this.x[var25] = this.x[var25 - 1];
               this.t[var25] = this.t[var25 - 1];
            }

            this.x[var25] = var23;
            this.t[var25] = var12;
         }
      }

      return var3;
   }

   private void Z(PoseStack var1, CameraRenderState var2, z8 var3, mo var4, boolean var5, int var6, double var7) {
      double var9 = Math.sin(var3.z);
      double var11 = Math.cos(var3.z);
      Vec3 var13 = var2.pos;
      this.A.set(0.0F, 0.0F, -1.0F).rotate(var2.orientation);

      for (int var14 = 0; var14 < var6; var14++) {
         wq var15 = this.t[var14];
         double var16 = Math.sqrt(this.x[var14]);
         if (!(var16 >= var4.Y)) {
            double var18 = var15.O * var4.E * var7;
            if (var4.P) {
               double var20 = Math.min(var4.X, var4.Y - 0.1);
               var18 *= 1.0 - l.F((var16 - var20) / (var4.Y - var20));
            }

            var18 *= l.F(var16 / 0.8);
            if (var5) {
               var18 *= var4.t;
            }
            int var38 = switch (var4.R) {
               case o -> var4.c;
               case b -> uh.q(var4.c, var4.U, (Math.sin(var15.q) + 1.0) * 0.5);
               case N -> uh.n((float)((var3.L() * 0.035 + var15.q / (Math.PI * 2)) % 1.0), 0.45F, 1.0F);
            };
            var38 = uh.F(var38, var18);
            if (var38 >>> 24 >= 1) {
               b0.f var21 = this.Y(var15.W);
               double var22 = var15.n + var15.C;
               double var24 = var15.x + var15.a;
               double var26 = var3.X + var22 * var11 + var24 * var9 - var13.x;
               double var28 = var3.H + var15.r + var15.L - var13.y;
               double var30 = var3.F + var24 * var11 - var22 * var9 - var13.z;
               double var32 = Math.hypot(var21.width + 12, var21.height + 8) * 0.01890625 * var4.b * (1.0 + var15.P * var4.o) * var15.Q * 0.5;
               if (!(var26 * this.A.x + var28 * this.A.y + var30 * this.A.z < -var32)) {
                  this.m.set(var1.last().pose()).translate((float)var26, (float)var28, (float)var30);
                  switch (var4.h) {
                     case l:
                        this.m.rotate(var2.orientation);
                        break;
                     case V:
                        this.m.rotateY((float)Math.atan2(-var26, -var30));
                        break;
                     case n:
                        this.m.rotateY((float)var15.G).rotateX((float)var15.J);
                  }

                  double var34 = var4.w == mo.d.k ? 0.0 : var15.A;
                  this.m.rotateZ((float)(var15.h + var34));
                  float var36 = (float)(0.01890625 * var4.b * (1.0 + var15.P * var4.o) * var15.Q);
                  this.m.scale(var36, -var36, var36).translate(0.0F, -var21.centerY, 0.0F);
                  if (var4.A && !var5) {
                     this.m.translate(2.0F, 2.0F, -0.03F);
                     this.g.y = uh.H(-15724520, (var38 >>> 24) / 255.0 * 0.5);
                     var21.text.visit(this);
                     this.m.translate(-2.0F, -2.0F, 0.03F);
                  }

                  this.g.y = var38;
                  var21.text.visit(this);
               }
            }
         }
      }

      this.f.K(this::O);
   }

   private PreparedRenderType O(PreparedRenderType var1) {
      int var2 = var1.pipeline() == RenderPipelines.TEXT_GRAYSCALE ? 1 : 0;
      return mx.z(var1, this.p[this.G][var2]);
   }

   public void acceptRenderable(TextRenderable var1) {
      this.g.t = this.f.g(var1.renderType(DisplayMode.NORMAL));
      if (this.h == null) {
         var1.render(this.m, this.g, 15728880, false);
      } else if (this.n(var1)) {
         var1.render(this.H, this.g, 15728880, false);
      }
   }

   public void i() {
      if (this.f != null) {
         this.f.g();
      }
   }

   @Override
   public void close() {
      this.Y.clear();
      this.k();
      Arrays.fill(this.t, null);
      if (this.f != null) {
         this.f.close();
         this.f = null;
      }

      this.O = (boolean)0;
      this.k = -1;
   }

   public record b(int kind, double reveal, double exit, double time, int accent) {
      public static final int FADE = 0;
      public static final int POP = 1;
      public static final int WAVE = 2;
      public static final int FALL = 3;
      public static final double FALL_HEIGHT = 12.0;
   }

   private record f(PreparedText text, int width, int height, float centerY) {
   }

   public record k(
      String text, double x, double y, double z, double scale, int color, float qx, float qy, float qz, float qw, boolean worldAligned, b0.b motion
   ) {
      public k(String var1, double var2, double var4, double var6, double var8, int var10) {
         this(var1, var2, var4, var6, var8, var10, 0.0F, 0.0F, 0.0F, 1.0F, false);
      }

      public k(String var1, double var2, double var4, double var6, double var8, int var10, float var11, float var12, float var13, float var14, boolean var15) {
         this(var1, var2, var4, var6, var8, var10, var11, var12, var13, var14, var15, null);
      }
   }

   private static final class p {
      b0.k c;
      b0.f X;
      double f;
   }
}
