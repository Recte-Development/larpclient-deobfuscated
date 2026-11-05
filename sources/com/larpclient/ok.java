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

public final class ok implements AutoCloseable, GlyphVisitor {
   private static final double W = 0.55;
   private static final int Y = 15728880;
   private static final int C = 256;
   private static final Comparator<ok.t> E = Comparator.<ok.t>comparingDouble(var0 -> var0.j).reversed();
   private final ok.t[] J = new ok.t[256];
   private final ok.t[] z = new ok.t[256];
   private int q;
   private static final double Z = 0.01890625;
   private final LinkedHashMap<String, ok.r> g = new LinkedHashMap<>(64, 0.75F, true);
   private final Matrix4f L = new Matrix4f();
   private final Matrix4f I = new Matrix4f();
   private ok.f c;
   private int D;
   private int A;
   private float G;
   private boolean l;
   private final Quaternionf k = new Quaternionf();
   private final Vector3f R = new Vector3f();
   private final ho m = new ho();
   private final lg[] K = new lg[96];
   private final double[] r = new double[96];
   private final RenderPipeline[][] e = new RenderPipeline[3][2];
   private y2 S;
   private Font X;
   private int v = -1;
   private int d = -1;
   private int a;
   private boolean T;
   private int b;

   public void q(o3 var1, CameraRenderState var2) {
      this.b = this.i(var1, var2);
   }

   public void I(List<ok.p> var1, CameraRenderState var2) {
      this.u();
      if (!this.T || this.d != jk.v) {
         try {
            this.X();
            this.z(var1, var2);
         } catch (RuntimeException var4) {
            this.T(var4);
         }
      }
   }

   private void z(List<ok.p> var1, CameraRenderState var2) {
      for (ok.p var4 : var1) {
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
            ok.r var5 = this.Z(var4.text());
            ok.t var6 = this.J[this.q];
            if (var6 == null) {
               this.J[this.q] = var6 = new ok.t();
            }

            var6.P = var4;
            var6.k = var5;
            double var7 = var4.x() - var2.pos.x;
            double var9 = var4.y() - var2.pos.y;
            double var11 = var4.z() - var2.pos.z;
            var6.j = var7 * var7 + var9 * var9 + var11 * var11;
            this.z[this.q++] = var6;
         }
      }

      Arrays.sort(this.z, 0, this.q, E);
   }

   private void u() {
      for (int var1 = 0; var1 < this.q; var1++) {
         this.z[var1].P = null;
         this.z[var1].k = null;
         this.z[var1] = null;
      }

      this.q = 0;
   }

   public void d(PoseStack var1, CameraRenderState var2, boolean var3) {
      this.a(var1, var2, var3, true, 1.0);
   }

   public void a(PoseStack var1, CameraRenderState var2, boolean var3, boolean var4, double var5) {
      if (this.q != 0 && !this.T && this.S != null) {
         try {
            this.a = var3 ? 1 : 0;
            this.R.set(0.0F, 0.0F, -1.0F).rotate(var2.orientation);

            for (int var7 = 0; var7 < this.q; var7++) {
               ok.t var8 = this.z[var7];
               ok.p var9 = var8.P;
               ok.r var10 = var8.k;
               double var11 = var9.x() - var2.pos.x;
               double var13 = var9.y() - var2.pos.y;
               double var15 = var9.z() - var2.pos.z;
               double var17 = Math.hypot(var10.width + 12, var10.height + 8) * 0.01890625 * var9.scale();
               if (!(var11 * this.R.x + var13 * this.R.y + var15 * this.R.z < -var17)) {
                  this.L.set(var1.last().pose()).translate((float)var11, (float)var13, (float)var15);
                  this.Q(var9, var2.orientation);
                  float var19 = (float)(0.01890625 * Math.min(32.0, var9.scale()));
                  this.L.scale(var19, -var19, var19).translate(0.0F, -var10.centerY, 0.0F);
                  this.c = var9.motion();
                  this.A = var10.width;
                  this.G = var10.height / 9.0F;
                  if (var4) {
                     this.L.translate(1.0F, 1.0F, -0.03F);
                     this.m.x = this.D = oh.i(-15724520, (var9.color() >>> 24) / 255.0 * 0.65 * var5);
                     this.l = (boolean)1;
                     var10.text.visit(this);
                     this.L.translate(-1.0F, -1.0F, 0.03F);
                  }

                  this.m.x = this.D = oh.i(var9.color(), (var9.color() >>> 24) / 255.0 * var5);
                  this.l = (boolean)0;
                  var10.text.visit(this);
               }
            }

            this.S.P(this::c);
         } catch (RuntimeException var23) {
            this.T(var23);
         } finally {
            this.m.e = null;
            this.c = null;
         }
      }
   }

   private boolean v(TextRenderable var1) {
      ok.f var2 = this.c;
      float var3 = (var1.left() + var1.right()) * 0.5F;
      float var4 = (var1.top() + var1.bottom()) * 0.5F;
      double var5 = this.A <= 0 ? 0.0 : Math.clamp((var3 + this.A * 0.5) / this.A, 0.0, 1.0);
      double var7 = 1.0;
      double var9 = 0.0;
      double var11 = 0.0;
      double var13 = 0.0;
      double var15 = 0.0;
      double var17 = 1.0;
      double var19 = this.G;
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

      double var26 = e6.q(var7) * (1.0 - e6.q(var9));
      if (var26 < 0.00392156862745098) {
         return false;
      }

      int var27 = this.D;
      if (!this.l && var2.accent() >>> 24 != 0) {
         double var24 = 0.5 - 0.5 * Math.cos(var5 * 2.4 - var2.time() * 1.2);
         var27 = oh.j(var27, var2.accent() & 16777215 | var27 & 0xFF000000, var24 * 0.85);
      }

      this.m.x = oh.S(var27, var26);
      this.I.set(this.L).translate(var3 + (float)var11, var4 + (float)var13, 0.0F).rotateZ((float)var15).scale((float)var17).translate(-var3, -var4, 0.0F);
      return true;
   }

   private void Q(ok.p var1, Quaternionfc var2) {
      this.L.rotate((Quaternionfc)(var1.worldAligned() ? this.k.set(var1.qx(), var1.qy(), var1.qz(), var1.qw()).normalize() : var2));
   }

   private void T(RuntimeException var1) {
      this.T = (boolean)1;
      this.u();
      this.g.clear();
      if (this.S != null) {
         this.S.close();
         this.S = null;
      }

      r4.r("WorldText", "World label stage failed; isolated until resource reload or re-enable", var1);
   }

   private void X() {
      Font var1 = Minecraft.getInstance().font;
      if (this.X != var1 || this.v != j_.g()) {
         this.g.clear();
         this.X = var1;
         this.v = j_.g();
      }

      if (this.d != jk.v) {
         this.g.clear();
         this.T = (boolean)0;
         this.d = jk.v;

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

               this.e[var2][var3] = var4.build();
               e4.U(this.e[var2][var3]);
            }
         }

         r4.V("WorldText", "World glyph pipelines ready: normal, through walls, soft occlusion");
      }

      if (this.S == null) {
         this.S = new y2();
      }
   }

   private ok.r Z(String var1) {
      ok.r var2 = this.g.get(var1);
      if (var2 != null) {
         return var2;
      }

      Component var3 = j_.P(var1);
      int var4 = this.X.width(var3);
      PreparedText var5 = this.X.prepareText(var3.getVisualOrderText(), -var4 / 2.0F, 0.0F, -1, false, false, 0);
      ScreenRectangle var6 = var5.bounds();
      ok.r var7 = new ok.r(var5, var4, var6 == null ? 32 : var6.height(), var6 == null ? 16.0F : var6.top() + var6.height() / 2.0F);
      if (this.g.size() >= 256) {
         this.g.remove(this.g.keySet().iterator().next());
      }

      this.g.put(var1, var7);
      return var7;
   }

   public boolean S() {
      return !this.T;
   }

   public int B(String var1) {
      if (var1.isEmpty()) {
         return 0;
      }

      this.X();
      return this.Z(var1).width;
   }

   public void i(PoseStack var1, CameraRenderState var2, o3 var3, e0 var4, boolean var5) {
      if (!this.T || this.d != jk.v) {
         if (!var5 || var4.z && !(var4.l <= 0.0)) {
            long var6 = h_.U();

            try {
               this.X();
               int var8 = this.b;
               if (var4.p == e0.m.O) {
                  this.a = 2;
                  this.H(var1, var2, var3, var4, var5, var8, 0.18);
               }

               this.a = var4.p == e0.m.x ? 1 : 0;
               this.H(var1, var2, var3, var4, var5, var8, 1.0);
            } catch (RuntimeException var12) {
               this.T = (boolean)1;
               if (this.S != null) {
                  this.S.close();
                  this.S = null;
               }

               this.g.clear();
               r4.r("WorldText", "World text render failed; isolated until resource reload or re-enable", var12);
            } finally {
               this.m.e = null;
               h_.n("WorldText", var6);
            }
         }
      }
   }

   private int i(o3 var1, CameraRenderState var2) {
      int var3 = 0;
      double var4 = Math.sin(var1.p);
      double var6 = Math.cos(var1.p);
      Vec3 var8 = var2.pos;

      for (lg var12 : var1.n()) {
         if (var12.h && !(var12.v < 0.00392156862745098)) {
            double var13 = var12.m + var12.a;
            double var15 = var12.k + var12.q;
            double var17 = var1.D + var13 * var6 + var15 * var4 - var8.x;
            double var19 = var1.w + var12.n + var12.E - var8.y;
            double var21 = var1.s + var15 * var6 - var13 * var4 - var8.z;
            double var23 = var17 * var17 + var19 * var19 + var21 * var21;

            int var25;
            for (var25 = var3++; var25 > 0 && this.r[var25 - 1] < var23; var25--) {
               this.r[var25] = this.r[var25 - 1];
               this.K[var25] = this.K[var25 - 1];
            }

            this.r[var25] = var23;
            this.K[var25] = var12;
         }
      }

      return var3;
   }

   private void H(PoseStack var1, CameraRenderState var2, o3 var3, e0 var4, boolean var5, int var6, double var7) {
      double var9 = Math.sin(var3.p);
      double var11 = Math.cos(var3.p);
      Vec3 var13 = var2.pos;
      this.R.set(0.0F, 0.0F, -1.0F).rotate(var2.orientation);

      for (int var14 = 0; var14 < var6; var14++) {
         lg var15 = this.K[var14];
         double var16 = Math.sqrt(this.r[var14]);
         if (!(var16 >= var4.D)) {
            double var18 = var15.v * var4.t * var7;
            if (var4.O) {
               double var20 = Math.min(var4.J, var4.D - 0.1);
               var18 *= 1.0 - e6.q((var16 - var20) / (var4.D - var20));
            }

            var18 *= e6.q(var16 / 0.8);
            if (var5) {
               var18 *= var4.l;
            }
            int var38 = switch (var4.d) {
               case n -> var4.s;
               case D -> oh.j(var4.s, var4.N, (Math.sin(var15.p) + 1.0) * 0.5);
               case N -> oh.O((float)((var3.e() * 0.035 + var15.p / (Math.PI * 2)) % 1.0), 0.45F, 1.0F);
            };
            var38 = oh.S(var38, var18);
            if (var38 >>> 24 >= 1) {
               ok.r var21 = this.Z(var15.W);
               double var22 = var15.m + var15.a;
               double var24 = var15.k + var15.q;
               double var26 = var3.D + var22 * var11 + var24 * var9 - var13.x;
               double var28 = var3.w + var15.n + var15.E - var13.y;
               double var30 = var3.s + var24 * var11 - var22 * var9 - var13.z;
               double var32 = Math.hypot(var21.width + 12, var21.height + 8) * 0.01890625 * var4.i * (1.0 + var15.f * var4.W) * var15.J * 0.5;
               if (!(var26 * this.R.x + var28 * this.R.y + var30 * this.R.z < -var32)) {
                  this.L.set(var1.last().pose()).translate((float)var26, (float)var28, (float)var30);
                  switch (var4.j) {
                     case l:
                        this.L.rotate(var2.orientation);
                        break;
                     case j:
                        this.L.rotateY((float)Math.atan2(-var26, -var30));
                        break;
                     case Y:
                        this.L.rotateY((float)var15.u).rotateX((float)var15.D);
                  }

                  double var34 = var4.R == e0.z.O ? 0.0 : var15.K;
                  this.L.rotateZ((float)(var15.i + var34));
                  float var36 = (float)(0.01890625 * var4.i * (1.0 + var15.f * var4.W) * var15.J);
                  this.L.scale(var36, -var36, var36).translate(0.0F, -var21.centerY, 0.0F);
                  if (var4.u && !var5) {
                     this.L.translate(2.0F, 2.0F, -0.03F);
                     this.m.x = oh.i(-15724520, (var38 >>> 24) / 255.0 * 0.5);
                     var21.text.visit(this);
                     this.L.translate(-2.0F, -2.0F, 0.03F);
                  }

                  this.m.x = var38;
                  var21.text.visit(this);
               }
            }
         }
      }

      this.S.P(this::c);
   }

   private PreparedRenderType c(PreparedRenderType var1) {
      int var2 = var1.pipeline() == RenderPipelines.TEXT_GRAYSCALE ? 1 : 0;
      return of.R(var1, this.e[this.a][var2]);
   }

   public void acceptRenderable(TextRenderable var1) {
      this.m.e = this.S.a(var1.renderType(DisplayMode.NORMAL));
      if (this.c == null) {
         var1.render(this.L, this.m, 15728880, false);
      } else if (this.v(var1)) {
         var1.render(this.I, this.m, 15728880, false);
      }
   }

   public void E() {
      if (this.S != null) {
         this.S.R();
      }
   }

   @Override
   public void close() {
      this.g.clear();
      this.u();
      Arrays.fill(this.K, null);
      if (this.S != null) {
         this.S.close();
         this.S = null;
      }

      this.T = (boolean)0;
      this.d = -1;
   }

   public record f(int kind, double reveal, double exit, double time, int accent) {
      public static final int FADE = 0;
      public static final int POP = 1;
      public static final int WAVE = 2;
   }

   public record p(
      String text, double x, double y, double z, double scale, int color, float qx, float qy, float qz, float qw, boolean worldAligned, ok.f motion
   ) {
      public p(String var1, double var2, double var4, double var6, double var8, int var10) {
         this(var1, var2, var4, var6, var8, var10, 0.0F, 0.0F, 0.0F, 1.0F, false);
      }

      public p(String var1, double var2, double var4, double var6, double var8, int var10, float var11, float var12, float var13, float var14, boolean var15) {
         this(var1, var2, var4, var6, var8, var10, var11, var12, var13, var14, var15, null);
      }
   }

   private record r(PreparedText text, int width, int height, float centerY) {
   }

   private static final class t {
      ok.p P;
      ok.r k;
      double j;
   }
}
