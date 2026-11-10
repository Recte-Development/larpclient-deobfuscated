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

public final class __ implements AutoCloseable, GlyphVisitor {
   private static final double C = 0.55;
   private static final int c = 15728880;
   private static final int A = 256;
   private static final Comparator<__.u> N = Comparator.<__.u>comparingDouble(var0 -> var0.E).reversed();
   private final __.u[] Z = new __.u[256];
   private final __.u[] H = new __.u[256];
   private int x;
   private static final double b = 0.01890625;
   private final LinkedHashMap<String, __.r> B = new LinkedHashMap<>(64, 0.75F, true);
   private final Matrix4f e = new Matrix4f();
   private final Matrix4f d = new Matrix4f();
   private __.l j;
   private int k;
   private int U;
   private float P;
   private boolean J;
   private final Quaternionf h = new Quaternionf();
   private final Vector3f K = new Vector3f();
   private final bi I = new bi();
   private final fe[] M = new fe[96];
   private final double[] T = new double[96];
   private final RenderPipeline[][] q = new RenderPipeline[3][2];
   private wh m;
   private Font g;
   private int w = -1;
   private int l = -1;
   private int E;
   private boolean L;
   private int n;

   public void d(hx var1, CameraRenderState var2) {
      this.n = this.K(var1, var2);
   }

   public void a(List<__.n> var1, CameraRenderState var2) {
      this.X();
      if (!this.L || this.l != nl.q) {
         try {
            this.U();
            this.L(var1, var2);
         } catch (RuntimeException var4) {
            this.f(var4);
         }
      }
   }

   private void L(List<__.n> var1, CameraRenderState var2) {
      for (__.n var4 : var1) {
         if (this.x == 256) {
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
            __.r var5 = this.G(var4.text());
            __.u var6 = this.Z[this.x];
            if (var6 == null) {
               this.Z[this.x] = var6 = new __.u();
            }

            var6.R = var4;
            var6.w = var5;
            double var7 = var4.x() - var2.pos.x;
            double var9 = var4.y() - var2.pos.y;
            double var11 = var4.z() - var2.pos.z;
            var6.E = var7 * var7 + var9 * var9 + var11 * var11;
            this.H[this.x++] = var6;
         }
      }

      Arrays.sort(this.H, 0, this.x, N);
   }

   private void X() {
      for (int var1 = 0; var1 < this.x; var1++) {
         this.H[var1].R = null;
         this.H[var1].w = null;
         this.H[var1] = null;
      }

      this.x = 0;
   }

   public void s(PoseStack var1, CameraRenderState var2, boolean var3) {
      this.r(var1, var2, var3, true, 1.0);
   }

   public void r(PoseStack var1, CameraRenderState var2, boolean var3, boolean var4, double var5) {
      if (this.x != 0 && !this.L && this.m != null) {
         try {
            this.E = var3 ? 1 : 0;
            this.K.set(0.0F, 0.0F, -1.0F).rotate(var2.orientation);

            for (int var7 = 0; var7 < this.x; var7++) {
               __.u var8 = this.H[var7];
               __.n var9 = var8.R;
               __.r var10 = var8.w;
               double var11 = var9.x() - var2.pos.x;
               double var13 = var9.y() - var2.pos.y;
               double var15 = var9.z() - var2.pos.z;
               double var17 = Math.hypot(var10.width + 12, var10.height + 8) * 0.01890625 * var9.scale();
               if (!(var11 * this.K.x + var13 * this.K.y + var15 * this.K.z < -var17)) {
                  this.e.set(var1.last().pose()).translate((float)var11, (float)var13, (float)var15);
                  this.I(var9, var2.orientation);
                  float var19 = (float)(0.01890625 * Math.min(32.0, var9.scale()));
                  this.e.scale(var19, -var19, var19).translate(0.0F, -var10.centerY, 0.0F);
                  this.j = var9.motion();
                  this.U = var10.width;
                  this.P = var10.height / 9.0F;
                  if (var4) {
                     this.e.translate(1.0F, 1.0F, -0.03F);
                     this.I.U = this.k = vv.N(-15724520, (var9.color() >>> 24) / 255.0 * 0.65 * var5);
                     this.J = (boolean)1;
                     var10.text.visit(this);
                     this.e.translate(-1.0F, -1.0F, 0.03F);
                  }

                  this.I.U = this.k = vv.N(var9.color(), (var9.color() >>> 24) / 255.0 * var5);
                  this.J = (boolean)0;
                  var10.text.visit(this);
               }
            }

            this.m.R(this::a);
         } catch (RuntimeException var23) {
            this.f(var23);
         } finally {
            this.I.I = null;
            this.j = null;
         }
      }
   }

   private boolean Z(TextRenderable var1) {
      __.l var2 = this.j;
      float var3 = (var1.left() + var1.right()) * 0.5F;
      float var4 = (var1.top() + var1.bottom()) * 0.5F;
      double var5 = this.U <= 0 ? 0.0 : Math.clamp((var3 + this.U * 0.5) / this.U, 0.0, 1.0);
      double var7 = 1.0;
      double var9 = 0.0;
      double var11 = 0.0;
      double var13 = 0.0;
      double var15 = 0.0;
      double var17 = 1.0;
      double var19 = this.P;
      if (var2.kind() != 0) {
         var7 = Math.clamp((var2.reveal() - var5 * 0.55) / 0.44999999999999996, 0.0, 1.0);
         var9 = Math.clamp((var2.exit() - var5 * 0.4) / 0.6, 0.0, 1.0);
         double var21 = 1.0 - Math.pow(1.0 - var7, 3.0);
         if (var2.kind() == 3) {
            double var23 = E(var7);
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

      double var27 = var2.kind() == 3 ? (var7 <= 0.0 ? 0.0 : bs.M(var7 * 5.0)) * (1.0 - bs.M(var9 * 1.15)) : bs.M(var7) * (1.0 - bs.M(var9));
      if (var27 < 0.00392156862745098) {
         return false;
      }

      int var29 = this.k;
      if (!this.J && var2.accent() >>> 24 != 0) {
         double var24 = 0.5 - 0.5 * Math.cos(var5 * 2.4 - var2.time() * 1.2);
         var29 = vv.Q(var29, var2.accent() & 16777215 | var29 & 0xFF000000, var24 * 0.85);
      }

      this.I.U = vv.O(var29, var27);
      this.d.set(this.e).translate(var3 + (float)var11, var4 + (float)var13, 0.0F).rotateZ((float)var15).scale((float)var17).translate(-var3, -var4, 0.0F);
      return true;
   }

   public static double E(double var0) {
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

   private void I(__.n var1, Quaternionfc var2) {
      this.e.rotate((Quaternionfc)(var1.worldAligned() ? this.h.set(var1.qx(), var1.qy(), var1.qz(), var1.qw()).normalize() : var2));
   }

   private void f(RuntimeException var1) {
      this.L = (boolean)1;
      this.X();
      this.B.clear();
      if (this.m != null) {
         this.m.close();
         this.m = null;
      }

      _q.o("WorldText", "World label stage failed; isolated until resource reload or re-enable", var1);
   }

   private void U() {
      Font var1 = Minecraft.getInstance().font;
      if (this.g != var1 || this.w != ic.l()) {
         this.B.clear();
         this.g = var1;
         this.w = ic.l();
      }

      if (this.l != nl.q) {
         this.B.clear();
         this.L = (boolean)0;
         this.l = nl.q;

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

               this.q[var2][var3] = var4.build();
               se.j(this.q[var2][var3]);
            }
         }

         _q.z("WorldText", "World glyph pipelines ready: normal, through walls, soft occlusion");
      }

      if (this.m == null) {
         this.m = new wh();
      }
   }

   private __.r G(String var1) {
      __.r var2 = this.B.get(var1);
      if (var2 != null) {
         return var2;
      }

      Component var3 = ic.u(var1);
      int var4 = this.g.width(var3);
      PreparedText var5 = this.g.prepareText(var3.getVisualOrderText(), -var4 / 2.0F, 0.0F, -1, false, false, 0);
      ScreenRectangle var6 = var5.bounds();
      __.r var7 = new __.r(var5, var4, var6 == null ? 32 : var6.height(), var6 == null ? 16.0F : var6.top() + var6.height() / 2.0F);
      if (this.B.size() >= 256) {
         this.B.remove(this.B.keySet().iterator().next());
      }

      this.B.put(var1, var7);
      return var7;
   }

   public boolean H() {
      return !this.L;
   }

   public int R(String var1) {
      if (var1.isEmpty()) {
         return 0;
      }

      this.U();
      return this.G(var1).width;
   }

   public void z(PoseStack var1, CameraRenderState var2, hx var3, jd var4, boolean var5) {
      if (!this.L || this.l != nl.q) {
         if (!var5 || var4.z && !(var4.L <= 0.0)) {
            long var6 = he.s();

            try {
               this.U();
               int var8 = this.n;
               if (var4.D == jd.h.q) {
                  this.E = 2;
                  this.X(var1, var2, var3, var4, var5, var8, 0.18);
               }

               this.E = var4.D == jd.h.j ? 1 : 0;
               this.X(var1, var2, var3, var4, var5, var8, 1.0);
            } catch (RuntimeException var12) {
               this.L = (boolean)1;
               if (this.m != null) {
                  this.m.close();
                  this.m = null;
               }

               this.B.clear();
               _q.o("WorldText", "World text render failed; isolated until resource reload or re-enable", var12);
            } finally {
               this.I.I = null;
               he.T("WorldText", var6);
            }
         }
      }
   }

   private int K(hx var1, CameraRenderState var2) {
      int var3 = 0;
      double var4 = Math.sin(var1.k);
      double var6 = Math.cos(var1.k);
      Vec3 var8 = var2.pos;

      for (fe var12 : var1.v()) {
         if (var12.Q && !(var12.g < 0.00392156862745098)) {
            double var13 = var12.Y + var12.l;
            double var15 = var12.O + var12.V;
            double var17 = var1.S + var13 * var6 + var15 * var4 - var8.x;
            double var19 = var1.Z + var12.h + var12.W - var8.y;
            double var21 = var1.X + var15 * var6 - var13 * var4 - var8.z;
            double var23 = var17 * var17 + var19 * var19 + var21 * var21;

            int var25;
            for (var25 = var3++; var25 > 0 && this.T[var25 - 1] < var23; var25--) {
               this.T[var25] = this.T[var25 - 1];
               this.M[var25] = this.M[var25 - 1];
            }

            this.T[var25] = var23;
            this.M[var25] = var12;
         }
      }

      return var3;
   }

   private void X(PoseStack var1, CameraRenderState var2, hx var3, jd var4, boolean var5, int var6, double var7) {
      double var9 = Math.sin(var3.k);
      double var11 = Math.cos(var3.k);
      Vec3 var13 = var2.pos;
      this.K.set(0.0F, 0.0F, -1.0F).rotate(var2.orientation);

      for (int var14 = 0; var14 < var6; var14++) {
         fe var15 = this.M[var14];
         double var16 = Math.sqrt(this.T[var14]);
         if (!(var16 >= var4.o)) {
            double var18 = var15.g * var4.V * var7;
            if (var4.K) {
               double var20 = Math.min(var4.N, var4.o - 0.1);
               var18 *= 1.0 - bs.M((var16 - var20) / (var4.o - var20));
            }

            var18 *= bs.M(var16 / 0.8);
            if (var5) {
               var18 *= var4.L;
            }
            int var38 = switch (var4.A) {
               case C -> var4.b;
               case d -> vv.Q(var4.b, var4.F, (Math.sin(var15.t) + 1.0) * 0.5);
               case V -> vv.b((float)((var3.b() * 0.035 + var15.t / (Math.PI * 2)) % 1.0), 0.45F, 1.0F);
            };
            var38 = vv.O(var38, var18);
            if (var38 >>> 24 >= 1) {
               __.r var21 = this.G(var15.p);
               double var22 = var15.Y + var15.l;
               double var24 = var15.O + var15.V;
               double var26 = var3.S + var22 * var11 + var24 * var9 - var13.x;
               double var28 = var3.Z + var15.h + var15.W - var13.y;
               double var30 = var3.X + var24 * var11 - var22 * var9 - var13.z;
               double var32 = Math.hypot(var21.width + 12, var21.height + 8) * 0.01890625 * var4.J * (1.0 + var15.A * var4.M) * var15.P * 0.5;
               if (!(var26 * this.K.x + var28 * this.K.y + var30 * this.K.z < -var32)) {
                  this.e.set(var1.last().pose()).translate((float)var26, (float)var28, (float)var30);
                  switch (var4.I) {
                     case Y:
                        this.e.rotate(var2.orientation);
                        break;
                     case M:
                        this.e.rotateY((float)Math.atan2(-var26, -var30));
                        break;
                     case G:
                        this.e.rotateY((float)var15.k).rotateX((float)var15.d);
                  }

                  double var34 = var4.Z == jd.i.U ? 0.0 : var15.E;
                  this.e.rotateZ((float)(var15.v + var34));
                  float var36 = (float)(0.01890625 * var4.J * (1.0 + var15.A * var4.M) * var15.P);
                  this.e.scale(var36, -var36, var36).translate(0.0F, -var21.centerY, 0.0F);
                  if (var4.k && !var5) {
                     this.e.translate(2.0F, 2.0F, -0.03F);
                     this.I.U = vv.N(-15724520, (var38 >>> 24) / 255.0 * 0.5);
                     var21.text.visit(this);
                     this.e.translate(-2.0F, -2.0F, 0.03F);
                  }

                  this.I.U = var38;
                  var21.text.visit(this);
               }
            }
         }
      }

      this.m.R(this::a);
   }

   private PreparedRenderType a(PreparedRenderType var1) {
      int var2 = var1.pipeline() == RenderPipelines.TEXT_GRAYSCALE ? 1 : 0;
      return p7.m(var1, this.q[this.E][var2]);
   }

   public void acceptRenderable(TextRenderable var1) {
      this.I.I = this.m.w(var1.renderType(DisplayMode.NORMAL));
      if (this.j == null) {
         var1.render(this.e, this.I, 15728880, false);
      } else if (this.Z(var1)) {
         var1.render(this.d, this.I, 15728880, false);
      }
   }

   public void b() {
      if (this.m != null) {
         this.m.A();
      }
   }

   @Override
   public void close() {
      this.B.clear();
      this.X();
      Arrays.fill(this.M, null);
      if (this.m != null) {
         this.m.close();
         this.m = null;
      }

      this.L = (boolean)0;
      this.l = -1;
   }

   public record l(int kind, double reveal, double exit, double time, int accent) {
      public static final int FADE = 0;
      public static final int POP = 1;
      public static final int WAVE = 2;
      public static final int FALL = 3;
      public static final double FALL_HEIGHT = 12.0;
   }

   public record n(
      String text, double x, double y, double z, double scale, int color, float qx, float qy, float qz, float qw, boolean worldAligned, __.l motion
   ) {
      public n(String var1, double var2, double var4, double var6, double var8, int var10) {
         this(var1, var2, var4, var6, var8, var10, 0.0F, 0.0F, 0.0F, 1.0F, false);
      }

      public n(String var1, double var2, double var4, double var6, double var8, int var10, float var11, float var12, float var13, float var14, boolean var15) {
         this(var1, var2, var4, var6, var8, var10, var11, var12, var13, var14, var15, null);
      }
   }

   private record r(PreparedText text, int width, int height, float centerY) {
   }

   private static final class u {
      __.n R;
      __.r w;
      double E;
   }
}
