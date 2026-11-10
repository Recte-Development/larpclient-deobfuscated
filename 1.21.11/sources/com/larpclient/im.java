package com.larpclient;

import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.pipeline.RenderPipeline.Builder;
import com.mojang.blaze3d.platform.DepthTestFunction;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import net.minecraft.class_10799;
import net.minecraft.class_11767;
import net.minecraft.class_12075;
import net.minecraft.class_243;
import net.minecraft.class_2561;
import net.minecraft.class_2960;
import net.minecraft.class_310;
import net.minecraft.class_327;
import net.minecraft.class_4587;
import net.minecraft.class_8030;
import net.minecraft.class_11767.class_12238;
import net.minecraft.class_327.class_11464;
import net.minecraft.class_327.class_11465;
import net.minecraft.class_327.class_6415;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Quaternionfc;
import org.joml.Vector3f;

public final class im implements AutoCloseable, class_11464 {
   private static final double N = 0.55;
   private static final int C = 15728880;
   private static final int q = 256;
   private static final Comparator<im.m> K = Comparator.<im.m>comparingDouble(var0 -> var0.D).reversed();
   private final im.m[] i = new im.m[256];
   private final im.m[] d = new im.m[256];
   private int g;
   private static final double j = 0.01890625;
   private final LinkedHashMap<String, im.r> f = new LinkedHashMap<>(64, 0.75F, true);
   private final Matrix4f R = new Matrix4f();
   private final Matrix4f G = new Matrix4f();
   private im.p k;
   private int S;
   private int T;
   private float s;
   private boolean r;
   private final Quaternionf t = new Quaternionf();
   private final Vector3f H = new Vector3f();
   private final w0 m = new w0();
   private final bz[] p = new bz[96];
   private final double[] I = new double[96];
   private final RenderPipeline[][] v = new RenderPipeline[3][2];
   private cw W;
   private class_327 J;
   private int n = -1;
   private int P = -1;
   private int b;
   private boolean F;
   private int c;

   public void q(ck var1, class_12075 var2) {
      this.c = this.m(var1, var2);
   }

   public void R(List<im.u> var1, class_12075 var2) {
      this.X();
      if (!this.F || this.P != t_.g) {
         try {
            this.W();
            this.I(var1, var2);
         } catch (RuntimeException var4) {
            this.X(var4);
         }
      }
   }

   private void I(List<im.u> var1, class_12075 var2) {
      for (im.u var4 : var1) {
         if (this.g == 256) {
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
            im.r var5 = this.m(var4.text());
            im.m var6 = this.i[this.g];
            if (var6 == null) {
               this.i[this.g] = var6 = new im.m();
            }

            var6.G = var4;
            var6.v = var5;
            double var7 = var4.x() - var2.field_63078.field_1352;
            double var9 = var4.y() - var2.field_63078.field_1351;
            double var11 = var4.z() - var2.field_63078.field_1350;
            var6.D = var7 * var7 + var9 * var9 + var11 * var11;
            this.d[this.g++] = var6;
         }
      }

      Arrays.sort(this.d, 0, this.g, K);
   }

   private void X() {
      for (int var1 = 0; var1 < this.g; var1++) {
         this.d[var1].G = null;
         this.d[var1].v = null;
         this.d[var1] = null;
      }

      this.g = 0;
   }

   public void Y(class_4587 var1, class_12075 var2, boolean var3) {
      this.A(var1, var2, var3, true, 1.0);
   }

   public void A(class_4587 var1, class_12075 var2, boolean var3, boolean var4, double var5) {
      if (this.g != 0 && !this.F && this.W != null) {
         try {
            this.b = var3 ? 1 : 0;
            this.H.set(0.0F, 0.0F, -1.0F).rotate(var2.field_63081);

            for (int var7 = 0; var7 < this.g; var7++) {
               im.m var8 = this.d[var7];
               im.u var9 = var8.G;
               im.r var10 = var8.v;
               double var11 = var9.x() - var2.field_63078.field_1352;
               double var13 = var9.y() - var2.field_63078.field_1351;
               double var15 = var9.z() - var2.field_63078.field_1350;
               double var17 = Math.hypot(var10.width + 12, var10.height + 8) * 0.01890625 * var9.scale();
               if (!(var11 * this.H.x + var13 * this.H.y + var15 * this.H.z < -var17)) {
                  this.R.set(var1.method_23760().method_23761()).translate((float)var11, (float)var13, (float)var15);
                  this.O(var9, var2.field_63081);
                  float var19 = (float)(0.01890625 * Math.min(32.0, var9.scale()));
                  this.R.scale(var19, -var19, var19).translate(0.0F, -var10.centerY, 0.0F);
                  this.k = var9.motion();
                  this.T = var10.width;
                  this.s = var10.height / 9.0F;
                  if (var4) {
                     this.R.translate(1.0F, 1.0F, -0.03F);
                     this.m.g = this.S = ei.D(-15724520, (var9.color() >>> 24) / 255.0 * 0.65 * var5);
                     this.r = (boolean)1;
                     var10.text.method_71801(this);
                     this.R.translate(-1.0F, -1.0F, 0.03F);
                  }

                  this.m.g = this.S = ei.D(var9.color(), (var9.color() >>> 24) / 255.0 * var5);
                  this.r = (boolean)0;
                  var10.text.method_71801(this);
               }
            }

            this.W.y(this::j);
         } catch (RuntimeException var23) {
            this.X(var23);
         } finally {
            this.m.D = null;
            this.k = null;
         }
      }
   }

   private boolean x(class_11767 var1) {
      im.p var2 = this.k;
      float var3 = (var1.method_71829() + var1.method_71831()) * 0.5F;
      float var4 = (var1.method_71830() + var1.method_71832()) * 0.5F;
      double var5 = this.T <= 0 ? 0.0 : Math.clamp((var3 + this.T * 0.5) / this.T, 0.0, 1.0);
      double var7 = 1.0;
      double var9 = 0.0;
      double var11 = 0.0;
      double var13 = 0.0;
      double var15 = 0.0;
      double var17 = 1.0;
      double var19 = this.s;
      if (var2.kind() != 0) {
         var7 = Math.clamp((var2.reveal() - var5 * 0.55) / 0.44999999999999996, 0.0, 1.0);
         var9 = Math.clamp((var2.exit() - var5 * 0.4) / 0.6, 0.0, 1.0);
         double var21 = 1.0 - Math.pow(1.0 - var7, 3.0);
         if (var2.kind() == 3) {
            double var23 = u(var7);
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

      double var27 = var2.kind() == 3 ? (var7 <= 0.0 ? 0.0 : u7.T(var7 * 5.0)) * (1.0 - u7.T(var9 * 1.15)) : u7.T(var7) * (1.0 - u7.T(var9));
      if (var27 < 0.00392156862745098) {
         return false;
      }

      int var29 = this.S;
      if (!this.r && var2.accent() >>> 24 != 0) {
         double var24 = 0.5 - 0.5 * Math.cos(var5 * 2.4 - var2.time() * 1.2);
         var29 = ei.W(var29, var2.accent() & 16777215 | var29 & 0xFF000000, var24 * 0.85);
      }

      this.m.g = ei.R(var29, var27);
      this.G.set(this.R).translate(var3 + (float)var11, var4 + (float)var13, 0.0F).rotateZ((float)var15).scale((float)var17).translate(-var3, -var4, 0.0F);
      return true;
   }

   public static double u(double var0) {
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

   private void O(im.u var1, Quaternionfc var2) {
      this.R.rotate((Quaternionfc)(var1.worldAligned() ? this.t.set(var1.qx(), var1.qy(), var1.qz(), var1.qw()).normalize() : var2));
   }

   private void X(RuntimeException var1) {
      this.F = (boolean)1;
      this.X();
      this.f.clear();
      if (this.W != null) {
         this.W.close();
         this.W = null;
      }

      q8.u("WorldText", "World label stage failed; isolated until resource reload or re-enable", var1);
   }

   private void W() {
      class_327 var1 = class_310.method_1551().field_1772;
      if (this.J != var1 || this.n != uw.E()) {
         this.f.clear();
         this.J = var1;
         this.n = uw.E();
      }

      if (this.P != t_.g) {
         this.f.clear();
         this.F = (boolean)0;
         this.P = t_.g;

         for (int var2 = 0; var2 < 3; var2++) {
            for (int var3 = 0; var3 < 2; var3++) {
               Builder var4 = f2.i(var3 == 1 ? class_10799.field_56820 : class_10799.field_56818)
                  .withLocation(class_2960.method_60655("larp-client", "world_text_" + var2 + "_" + var3))
                  .withVertexShader(yf.E(class_10799.field_56818))
                  .withFragmentShader(class_2960.method_60655("larp-client", "world_text"))
                  .withCull(false)
                  .withDepthBias(0.0F, 0.0F)
                  .withDepthTestFunction(
                     var2 == 1 ? DepthTestFunction.NO_DEPTH_TEST : (var2 == 2 ? DepthTestFunction.GREATER_DEPTH_TEST : DepthTestFunction.LEQUAL_DEPTH_TEST)
                  )
                  .withDepthWrite(false);
               if (var3 == 1) {
                  var4.withShaderDefine("IS_GRAYSCALE");
               }

               this.v[var2][var3] = var4.build();
               f2.a(this.v[var2][var3]);
            }
         }

         q8.G("WorldText", "World glyph pipelines ready: normal, through walls, soft occlusion");
      }

      if (this.W == null) {
         this.W = new cw();
      }
   }

   private im.r m(String var1) {
      im.r var2 = this.f.get(var1);
      if (var2 != null) {
         return var2;
      }

      class_2561 var3 = uw.a(var1);
      int var4 = this.J.method_27525(var3);
      class_11465 var5 = this.J.method_71795(var3.method_30937(), -var4 / 2.0F, 0.0F, -1, false, false, 0);
      class_8030 var6 = var5.method_71800();
      im.r var7 = new im.r(var5, var4, var6 == null ? 32 : var6.comp_1197(), var6 == null ? 16.0F : var6.method_49618() + var6.comp_1197() / 2.0F);
      if (this.f.size() >= 256) {
         this.f.remove(this.f.keySet().iterator().next());
      }

      this.f.put(var1, var7);
      return var7;
   }

   public boolean c() {
      return !this.F;
   }

   public int h(String var1) {
      if (var1.isEmpty()) {
         return 0;
      }

      this.W();
      return this.m(var1).width;
   }

   public void J(class_4587 var1, class_12075 var2, ck var3, ic var4, boolean var5) {
      if (!this.F || this.P != t_.g) {
         if (!var5 || var4.n && !(var4.u <= 0.0)) {
            long var6 = c5.X();

            try {
               this.W();
               int var8 = this.c;
               if (var4.F == ic.e.v) {
                  this.b = 2;
                  this.l(var1, var2, var3, var4, var5, var8, 0.18);
               }

               this.b = var4.F == ic.e.i ? 1 : 0;
               this.l(var1, var2, var3, var4, var5, var8, 1.0);
            } catch (RuntimeException var12) {
               this.F = (boolean)1;
               if (this.W != null) {
                  this.W.close();
                  this.W = null;
               }

               this.f.clear();
               q8.u("WorldText", "World text render failed; isolated until resource reload or re-enable", var12);
            } finally {
               this.m.D = null;
               c5.w("WorldText", var6);
            }
         }
      }
   }

   private int m(ck var1, class_12075 var2) {
      int var3 = 0;
      double var4 = Math.sin(var1.e);
      double var6 = Math.cos(var1.e);
      class_243 var8 = var2.field_63078;

      for (bz var12 : var1.d()) {
         if (var12.S && !(var12.M < 0.00392156862745098)) {
            double var13 = var12.p + var12.m;
            double var15 = var12.K + var12.q;
            double var17 = var1.K + var13 * var6 + var15 * var4 - var8.field_1352;
            double var19 = var1.p + var12.A + var12.l - var8.field_1351;
            double var21 = var1.u + var15 * var6 - var13 * var4 - var8.field_1350;
            double var23 = var17 * var17 + var19 * var19 + var21 * var21;

            int var25;
            for (var25 = var3++; var25 > 0 && this.I[var25 - 1] < var23; var25--) {
               this.I[var25] = this.I[var25 - 1];
               this.p[var25] = this.p[var25 - 1];
            }

            this.I[var25] = var23;
            this.p[var25] = var12;
         }
      }

      return var3;
   }

   private void l(class_4587 var1, class_12075 var2, ck var3, ic var4, boolean var5, int var6, double var7) {
      double var9 = Math.sin(var3.e);
      double var11 = Math.cos(var3.e);
      class_243 var13 = var2.field_63078;
      this.H.set(0.0F, 0.0F, -1.0F).rotate(var2.field_63081);

      for (int var14 = 0; var14 < var6; var14++) {
         bz var15 = this.p[var14];
         double var16 = Math.sqrt(this.I[var14]);
         if (!(var16 >= var4.z)) {
            double var18 = var15.M * var4.C * var7;
            if (var4.G) {
               double var20 = Math.min(var4.U, var4.z - 0.1);
               var18 *= 1.0 - u7.T((var16 - var20) / (var4.z - var20));
            }

            var18 *= u7.T(var16 / 0.8);
            if (var5) {
               var18 *= var4.u;
            }
            int var38 = switch (var4.e) {
               case u -> var4.w;
               case z -> ei.W(var4.w, var4.q, (Math.sin(var15.o) + 1.0) * 0.5);
               case J -> ei.X((float)((var3.k() * 0.035 + var15.o / (Math.PI * 2)) % 1.0), 0.45F, 1.0F);
            };
            var38 = ei.R(var38, var18);
            if (var38 >>> 24 >= 1) {
               im.r var21 = this.m(var15.k);
               double var22 = var15.p + var15.m;
               double var24 = var15.K + var15.q;
               double var26 = var3.K + var22 * var11 + var24 * var9 - var13.field_1352;
               double var28 = var3.p + var15.A + var15.l - var13.field_1351;
               double var30 = var3.u + var24 * var11 - var22 * var9 - var13.field_1350;
               double var32 = Math.hypot(var21.width + 12, var21.height + 8) * 0.01890625 * var4.s * (1.0 + var15.w * var4.k) * var15.O * 0.5;
               if (!(var26 * this.H.x + var28 * this.H.y + var30 * this.H.z < -var32)) {
                  this.R.set(var1.method_23760().method_23761()).translate((float)var26, (float)var28, (float)var30);
                  switch (var4.m) {
                     case M:
                        this.R.rotate(var2.field_63081);
                        break;
                     case s:
                        this.R.rotateY((float)Math.atan2(-var26, -var30));
                        break;
                     case w:
                        this.R.rotateY((float)var15.W).rotateX((float)var15.Y);
                  }

                  double var34 = var4.g == ic.z.i ? 0.0 : var15.c;
                  this.R.rotateZ((float)(var15.d + var34));
                  float var36 = (float)(0.01890625 * var4.s * (1.0 + var15.w * var4.k) * var15.O);
                  this.R.scale(var36, -var36, var36).translate(0.0F, -var21.centerY, 0.0F);
                  if (var4.W && !var5) {
                     this.R.translate(2.0F, 2.0F, -0.03F);
                     this.m.g = ei.D(-15724520, (var38 >>> 24) / 255.0 * 0.5);
                     var21.text.method_71801(this);
                     this.R.translate(-2.0F, -2.0F, 0.03F);
                  }

                  this.m.g = var38;
                  var21.text.method_71801(this);
               }
            }
         }
      }

      this.W.y(this::j);
   }

   private jv j(jv var1) {
      int var2 = var1.pipeline() == class_10799.field_56820 ? 1 : 0;
      return yf.w(var1, this.v[this.b][var2]);
   }

   public void method_71797(class_12238 var1) {
      this.r(var1);
   }

   public void method_71798(class_11767 var1) {
      this.r(var1);
   }

   private void r(class_11767 var1) {
      this.m.D = this.W.N(var1.method_73401(class_6415.field_33993));
      if (this.k == null) {
         var1.method_73403(this.R, this.m, 15728880, false);
      } else if (this.x(var1)) {
         var1.method_73403(this.G, this.m, 15728880, false);
      }
   }

   public void i() {
      if (this.W != null) {
         this.W.W();
      }
   }

   @Override
   public void close() {
      this.f.clear();
      this.X();
      Arrays.fill(this.p, null);
      if (this.W != null) {
         this.W.close();
         this.W = null;
      }

      this.F = (boolean)0;
      this.P = -1;
   }

   private static final class m {
      im.u G;
      im.r v;
      double D;
   }

   public record p(int kind, double reveal, double exit, double time, int accent) {
      public static final int FADE = 0;
      public static final int POP = 1;
      public static final int WAVE = 2;
      public static final int FALL = 3;
      public static final double FALL_HEIGHT = 12.0;
   }

   private record r(class_11465 text, int width, int height, float centerY) {
   }

   public record u(
      String text, double x, double y, double z, double scale, int color, float qx, float qy, float qz, float qw, boolean worldAligned, im.p motion
   ) {
      public u(String var1, double var2, double var4, double var6, double var8, int var10) {
         this(var1, var2, var4, var6, var8, var10, 0.0F, 0.0F, 0.0F, 1.0F, false);
      }

      public u(String var1, double var2, double var4, double var6, double var8, int var10, float var11, float var12, float var13, float var14, boolean var15) {
         this(var1, var2, var4, var6, var8, var10, var11, var12, var13, var14, var15, null);
      }
   }
}
