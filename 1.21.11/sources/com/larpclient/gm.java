package com.larpclient;

import com.larpclient.module.CapesModule;
import com.larpclient.module.ChinaHatModule;
import com.larpclient.module.HeadlessKorbloxModule;
import com.larpclient.module.SkinChangerModule;
import com.larpclient.module.WingsModule;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import net.minecraft.class_10055;
import net.minecraft.class_11255;
import net.minecraft.class_12249;
import net.minecraft.class_1664;
import net.minecraft.class_243;
import net.minecraft.class_2960;
import net.minecraft.class_310;
import net.minecraft.class_4587;
import net.minecraft.class_4588;
import net.minecraft.class_4608;
import net.minecraft.class_8685;
import net.minecraft.class_9950;
import net.minecraft.class_4587.class_4665;
import net.minecraft.class_4597.class_4598;
import org.joml.Matrix4f;
import org.joml.Matrix4fc;
import org.joml.Vector3f;

public final class gm {
   private static final int H = 15728880;
   static final float w = 1.85F;
   static final float y = 0.15F;
   static final float R = 1.4F;
   static final float J = 0.15F;
   private final ho.b l = new ho.b();
   private i9.g S;
   private String e = "";
   private static int G = -1;
   private static final g7 g = new g7(0L);

   static List<ChinaHatModule.a> O() {
      ChinaHatModule var0 = ChinaHatModule.h();
      return var0 == null ? List.of() : var0.Y();
   }

   static WingsModule.g f() {
      WingsModule var0 = WingsModule.B();
      return var0 == null ? null : var0.X();
   }

   static boolean i() {
      HeadlessKorbloxModule var0 = HeadlessKorbloxModule.e();
      return var0 != null && var0.s() || O().stream().anyMatch(ChinaHatModule.a::dominus);
   }

   static yv O() {
      HeadlessKorbloxModule var0 = HeadlessKorbloxModule.e();
      return var0 == null ? null : var0.w();
   }

   static double s(List<ChinaHatModule.a> var0, WingsModule.g var1) {
      double var2 = 0.0;

      for (ChinaHatModule.a var5 : var0) {
         var2 = Math.max(var2, var5.rise() + 0.05);
      }

      if (var1 != null) {
         var2 = Math.max(var2, (var1.dragon() ? 0.15F : 0.15F) * var1.size());
      }

      return var2;
   }

   static double D(List<ChinaHatModule.a> var0, WingsModule.g var1) {
      double var2 = 0.0;

      for (ChinaHatModule.a var5 : var0) {
         var2 = Math.max(var2, var5.radius() + 0.05);
      }

      if (var1 != null) {
         var2 = Math.max(var2, (var1.dragon() ? 1.4F : 1.85F) * var1.size());
      }

      return var2;
   }

   static class_8685 b(UUID var0, class_8685 var1) {
      class_8685 var2 = he.a(var0, var1);
      SkinChangerModule var3 = SkinChangerModule.m();
      if (var3 != null) {
         var2 = var3.q(var2);
      }

      CapesModule var4 = CapesModule.i();
      return var4 == null ? var2 : var4.E(var2);
   }

   static void o(class_10055 var0, yx.k var1) {
      float var2 = var1.walkSpeed() / 4.0F;
      var0.field_53537 = Math.clamp(var2 * 300.0F, 0.0F, 150.0F);
      var0.field_53538 = 0.0F;
      float var3 = var1.walkPosition() * 0.15F;
      float var4 = Math.min(0.1F, var2);
      var0.field_53536 = (float)Math.sin(var3 * 6.0F) * 32.0F * var4;
   }

   static class_2960 Y(class_8685 var0) {
      return var0.comp_1627() != null && class_310.method_1551().field_1690.method_32594(class_1664.field_7559) ? var0.comp_1627().comp_3627() : null;
   }

   gm.o w(class_2960 var1, List<ChinaHatModule.a> var2, WingsModule.g var3, yx.k var4, class_9950 var5) {
      return this.T(var1, var2, var3, false, null, var4, var5);
   }

   gm.o T(class_2960 var1, List<ChinaHatModule.a> var2, WingsModule.g var3, boolean var4, yv var5, yx.k var6, class_9950 var7) {
      if (var7 == null) {
         var1 = null;
      }

      float var8 = var6.ageInTicks();
      this.l.R(var8, false, var6.walkSpeed(), false, false, class_243.field_1353);
      i9.m var9 = null;
      if (var3 != null && var3.dragon()) {
         if (this.S == null) {
            this.S = i9.g.U();
         }

         this.l.i(i9.v(this.l.N(), 0.0F, 0.0F, 0.0F));
         var9 = i9.y(this.l.V(), this.l.N(), 0.0F, 0.0F, 0.0F);
      }

      String var10 = var2.isEmpty() ? "none" : String.join("+", var2.stream().map(ChinaHatModule.a::label).toList());
      String var11 = (var1 == null ? "-" : var1)
         + "|"
         + var10
         + "|"
         + (var3 == null ? "-" : (var3.dragon() ? "dragon" : "luminous"))
         + "|"
         + var4
         + "|"
         + (var5 == null ? "-" : var5.legs());
      if (!var11.equals(this.e)) {
         this.e = var11;
         q8.G(
            "Menu",
            "Account model cosmetics: cape={}, hats={}, wings={}, headless={}, korblox={}",
            var1 == null ? "none" : var1,
            var10,
            var3 == null ? "none" : (var3.dragon() ? "Ender Dragon" : "Luminous"),
            var4,
            var5 == null ? "none" : (var5.right() && var5.left() ? "both legs" : (var5.right() ? "right leg" : "left leg"))
         );
      }

      if (var1 == null && var2.isEmpty() && var3 == null && !var4 && var5 == null) {
         return null;
      }

      int var12 = var3 == null ? 0 : WingsModule.D(var3.first(), var3.second(), var8 / 20.0);
      int var13 = var3 == null ? 0 : WingsModule.Z(var3.first(), var3.second(), var8 / 20.0);
      return new gm.o(var7, (class_2960)var1, var2, var3, var12, var13, this.S, var9, var8, this.l.N(), var4, var5);
   }

   public static void i(class_11255 var0, Matrix4fc var1, class_4587 var2, class_4598 var3) {
      if (var0.comp_4141() instanceof jk var4 && var4.l() != null) {
         int var13 = t_.g;
         if (G != var13) {
            gm.o var6 = var4.l();

            try {
               if (var6.cape() != null) {
                  var6.capeModel().method_62110(var4.P());
                  var6.capeModel().method_60879(var2, var3.method_73477(class_12249.method_75982(var6.cape())), 15728880, class_4608.field_21444);
                  var3.method_22993();
               }

               yv var7 = var6.korblox();
               if (var7 != null) {
                  if (var7.right()) {
                     lm.w(var2, var3, var4.t(), var7, 15728880, class_4608.field_21444, false);
                  }

                  if (var7.left()) {
                     lm.w(var2, var3, var4.b(), var7, 15728880, class_4608.field_21444, true);
                  }
               }

               WingsModule.g var8 = var6.wings();
               if (var8 != null && var8.dragon()) {
                  class_4587 var9 = new class_4587();
                  var9.method_23760().method_66521(var2.method_23760());
                  var4.v().method_22703(var9);
                  i9.g var10 = var6.dragon();
                  i9.m var11 = var6.angles();
                  var10.d(var11);
                  var10.t(var9, var3.method_73477(i9.z(var8.dragonColor())), var8.size(), false, 15728880, var8.dragonColor());
                  var3.method_22993();
               }

               gm.k var14 = new gm.k();
               j(var14, var4, new Matrix4f(var1).invert().mul(var2.method_23760().method_23761()), var6);
               class_4587 var15 = new class_4587();
               var15.method_23760().method_23761().set(var1);
               var14.j(var3, var15);
            } catch (RuntimeException var12) {
               G = var13;
               q8.u("Menu", "Account model cosmetics submission failed; body only until resource reload", var12);
            }
         }
      }
   }

   static void j(gm.k var0, jk var1, Matrix4f var2, gm.o var3) {
      if (!var3.hats().isEmpty()) {
         class_4587 var4 = new class_4587();
         var4.method_23760().method_23761().set(var2);
         var1.l().method_22703(var4);
         var0.blend(ti.d.G);

         for (ChinaHatModule.a var6 : var3.hats()) {
            if (var6.dominus()) {
               var0.l(var4.method_23760().method_23761(), var6.radius(), var6.top());
            } else if (var6.horns() != null) {
               bl.a var11 = bl.q();
               if (var11 != null) {
                  float var12 = (float)var6.radius();
                  Vector3f var13 = var2.transformDirection(0.0F, -1.0F, 0.0F, new Vector3f()).normalize();
                  Vector3f var14 = var4.method_23760()
                     .method_23761()
                     .transformPosition(0.0F, -0.5F, var11.centerZ() * var12, new Vector3f())
                     .add(new Vector3f(var13).mul(0.24F * var12));
                  g.g(var3.age() / 20.0, var14.x, var14.y, var14.z, var13.x, var13.y, var13.z, var12 * var6.horns().size(), var6.horns().heat());
                  var0.U(var4.method_23760().method_23761(), var6.radius(), var6.horns(), g);
               }
            } else if (var6.accessory() == null) {
               if (var6.kippah()) {
                  jb.H(var0, var4.method_23760().method_23761(), var6.radius(), var6.height(), var6.offset(), var6.top(), var6.rim(), var6.outline(), 1.0);
               } else {
                  ge.l(var0, var4.method_23760().method_23761(), var6.radius(), var6.height(), var6.offset(), var6.top(), var6.rim(), var6.outline());
               }
            } else {
               List var7 = q4.Z(var6.accessory());
               int var8 = !var7.isEmpty() && ij.G("Menu") ? 1 : 0;
               q4.L(
                  var0, var4.method_23760().method_23761(), var6.accessory(), var6.radius(), (var6.top() >>> 24) / 255.0, (boolean)var8, 1.0, var3.age() / 20.0
               );
               if (var8 != 0) {
                  for (q4.y var10 : var7) {
                     var0.v(var10.coin(), q4.Y(var4.method_23760().method_23761(), var10, var6.radius(), new Matrix4f()));
                  }
               }
            }
         }
      }

      if (var3.wings() != null && !var3.wings().dragon()) {
         x(var0, var1, var2, var3, var3.wings());
      }
   }

   private static void x(gm.k var0, jk var1, Matrix4f var2, gm.o var3, WingsModule.g var4) {
      class_4587 var5 = new class_4587();
      var5.method_23760().method_23761().set(var2);
      var1.v().method_22703(var5);
      ho.z var6 = ho.O(0.0F, 0.0F, 0.0F, 0.0F);
      ho.x var7 = ho.M(var3.age(), var3.walking(), 0.0F, 0.0F);
      ho.o[] var8 = new ho.o[2];
      byte var9 = -1;

      for (int var10 = 0; var9 <= 1; var10++) {
         Matrix4f var11 = new Matrix4f(var5.method_23760().method_23761()).mul(ho.d(var6, var3.age(), var3.walking(), var4.size(), var9, false));
         var8[var10] = ho.e(class_243.field_1353, var11, var9, var7, var3.primary(), var3.secondary(), var4.size());
         var9 += 2;
      }

      for (ho.t var12 : ho.t.values()) {
         for (ho.o var16 : var8) {
            ho.B(var0, var16, var12);
         }
      }
   }

   static final class k implements gq {
      private final List<gm.k.u> f = new ArrayList<>();
      private final List<gm.k.n> e = new ArrayList<>();
      private gm.k.c Y;
      private gm.k.x k;
      private final Vector3f C = new Vector3f();
      private ti.d q = ti.d.G;
      private ti.d I;
      private boolean B;
      private boolean U;
      private float[] S = new float[768];
      private int[] l = new int[256];
      private int O;

      @Override
      public void blend(ti.d var1) {
         this.q = var1;
      }

      @Override
      public void solid(boolean var1) {
         this.B = var1;
      }

      @Override
      public Vector3f viewer(Matrix4f var1, Vector3f var2) {
         new Matrix4f(var1).invert().transformPosition(var2.set(0.0F, 0.0F, 1000.0F));
         if (var2.z > 0.0F) {
            new Matrix4f(var1).invert().transformPosition(var2.set(0.0F, 0.0F, -1000.0F));
         }

         return var2;
      }

      void v(int var1, Matrix4f var2) {
         this.e.add(new gm.k.n(var1, var2));
      }

      void U(Matrix4f var1, double var2, ChinaHatModule.y var4, g7 var5) {
         this.Y = new gm.k.c(new Matrix4f(var1), var2, var4, var5);
      }

      void l(Matrix4f var1, double var2, int var4) {
         this.k = new gm.k.x(new Matrix4f(var1), var2, var4);
      }

      @Override
      public void modelVertex(Matrix4f var1, float var2, float var3, float var4, int var5) {
         var1.transformPosition(var2, var3, var4, this.C);
         this.vertex(this.C.x, this.C.y, this.C.z, var5);
      }

      @Override
      public void vertex(double var1, double var3, double var5, int var7) {
         if (this.I != this.q || this.U != this.B) {
            this.r();
            this.I = this.q;
            this.U = this.B;
         }

         if (this.O == this.l.length) {
            this.l = Arrays.copyOf(this.l, this.O * 2);
            this.S = Arrays.copyOf(this.S, this.O * 6);
         }

         this.S[this.O * 3] = (float)var1;
         this.S[this.O * 3 + 1] = (float)var3;
         this.S[this.O * 3 + 2] = (float)var5;
         this.l[this.O++] = var7;
      }

      @Override
      public void ribbon(class_243 var1, class_243 var2, double var3, int var5, int var6) {
         double var7 = var2.field_1352 - var1.field_1352;
         double var9 = var2.field_1351 - var1.field_1351;
         double var11 = var2.field_1350 - var1.field_1350;
         if (!(var7 * var7 + var9 * var9 + var11 * var11 < 1.0E-10)) {
            double var13 = Math.hypot(var7, var9);
            double var15 = var13 < 1.0E-8 ? 0.5 : var9 / var13 / 2.0;
            double var17 = var13 < 1.0E-8 ? 0.0 : -var7 / var13 / 2.0;
            this.vertex(var1.field_1352 + var15 * var3, var1.field_1351 + var17 * var3, var1.field_1350, var5);
            this.vertex(var2.field_1352 + var15 * var3, var2.field_1351 + var17 * var3, var2.field_1350, var6);
            this.vertex(var2.field_1352 - var15 * var3, var2.field_1351 - var17 * var3, var2.field_1350, var6);
            this.vertex(var1.field_1352 - var15 * var3, var1.field_1351 - var17 * var3, var1.field_1350, var5);
         }
      }

      List<Vector3f> V() {
         this.r();
         ArrayList var1 = new ArrayList();

         for (gm.k.u var3 : this.f) {
            for (int var4 = 0; var4 < var3.colors.length; var4++) {
               var1.add(new Vector3f(var3.points[var4 * 3], var3.points[var4 * 3 + 1], var3.points[var4 * 3 + 2]));
            }
         }

         return var1;
      }

      private void r() {
         if (this.I != null && this.O > 0) {
            this.f.add(new gm.k.u(this.I, this.U, Arrays.copyOf(this.S, this.O * 3), Arrays.copyOf(this.l, this.O)));
         }

         this.O = 0;
      }

      void j(class_4598 var1, class_4587 var2) {
         this.r();
         gm.k.c var3 = this.Y;
         this.Y = null;
         bl.a var4 = bl.q();
         int var5 = var3 != null && var4 != null && bl.i(var3.look().horn(), var3.look().fire(), var3.look().core()) ? 1 : 0;
         if (var5 != 0) {
            bl.S(var1.method_73477(bl.H()), new Matrix4f(var2.method_23760().method_23761()).mul(var3.head()), var4, var3.scale());
            var1.method_22993();
         }

         gm.k.x var6 = this.k;
         this.k = null;
         bl.a var7 = tb.x();
         if (var6 != null && var7 != null) {
            tb.t(var1.method_73477(tb.f()), new Matrix4f(var2.method_23760().method_23761()).mul(var6.head()), var7, var6.scale(), var6.tint());
            var1.method_22993();
         }

         for (boolean var11 : new boolean[]{true, false}) {
            for (gm.k.u var13 : this.f) {
               if (var13.solid == var11) {
                  class_4588 var14 = var1.method_73477(var13.solid ? f1.D() : f1.y(var13.blend));

                  for (int var15 = 0; var15 < var13.colors.length; var15++) {
                     var14.method_56824(var2.method_23760(), var13.points[var15 * 3], var13.points[var15 * 3 + 1], var13.points[var15 * 3 + 2])
                        .method_39415(var13.colors[var15]);
                  }

                  var1.method_22993();
               }
            }

            if (var11) {
               b3.f var23 = ij.l();
               if (var23 != null) {
                  for (gm.k.n var25 : this.e) {
                     ij.r var26 = ij.g.get(var25.coin());
                     class_4588 var16 = var1.method_73477(var26.type());
                     Matrix4f var17 = new Matrix4f(var2.method_23760().method_23761()).mul(var25.transform());
                     float[] var18 = var23.vertices();
                     int[] var19 = var23.colors();

                     for (int var20 = 0; var20 < var19.length; var20++) {
                        var16.method_22918(var17, var18[var20 * 5], var18[var20 * 5 + 1], var18[var20 * 5 + 2])
                           .method_22913(var18[var20 * 5 + 3], var18[var20 * 5 + 4])
                           .method_39415(var19[var20]);
                     }

                     var1.method_22993();
                  }
               }
            }
         }

         this.f.clear();
         this.e.clear();
         if (var5 != 0) {
            class_4665 var21 = var2.method_23760();
            class_4588 var22 = var1.method_73477(g7.p());
            var3.fire()
               .F(
                  (var2x, var4x, var6x, var8, var10, var12, var14x) -> {
                     float var15x = (float)Math.cos(var12);
                     float var16x = (float)Math.sin(var12);
                     float var17x = (float)var8;
                     float var18x = (float)(var8 * var10);
                     float var19x = (float)var2x;
                     float var20x = (float)var4x;
                     float var21x = (float)var6x;

                     for (int var22x = 0; var22x < 4; var22x++) {
                        float var23x = var22x != 0 && var22x != 3 ? 1.0F : -1.0F;
                        float var24 = var22x < 2 ? -1.0F : 1.0F;
                        var22.method_56824(
                              var21,
                              var19x + var23x * var17x * var15x - var24 * var18x * var16x,
                              var20x + var23x * var17x * var16x + var24 * var18x * var15x,
                              var21x
                           )
                           .method_22913((var23x + 1.0F) * 0.5F, (var24 + 1.0F) * 0.5F)
                           .method_39415(var14x);
                     }
                  },
                  var3.look().fire(),
                  var3.look().core(),
                  1.0
               );
            var1.method_22993();
         }
      }

      private record c(Matrix4f head, double scale, ChinaHatModule.y look, g7 fire) {
      }

      private record n(int coin, Matrix4f transform) {
      }

      private record u(ti.d blend, boolean solid, float[] points, int[] colors) {
      }

      private record x(Matrix4f head, double scale, int tint) {
      }
   }

   record o(
      class_9950 capeModel,
      class_2960 cape,
      List<ChinaHatModule.a> hats,
      WingsModule.g wings,
      int primary,
      int secondary,
      i9.g dragon,
      i9.m angles,
      float age,
      float walking,
      boolean headless,
      yv korblox
   ) {
   }
}
