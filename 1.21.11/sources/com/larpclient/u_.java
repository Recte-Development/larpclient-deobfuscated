package com.larpclient;

import com.larpclient.module.WingsModule;
import java.util.List;
import java.util.Locale;
import net.minecraft.class_10799;
import net.minecraft.class_1109;
import net.minecraft.class_2960;
import net.minecraft.class_310;
import net.minecraft.class_332;
import net.minecraft.class_3414;
import net.minecraft.class_3417;
import net.minecraft.class_5599;
import net.minecraft.class_5602;
import net.minecraft.class_7920;
import net.minecraft.class_8685;
import net.minecraft.class_9950;
import org.lwjgl.glfw.GLFW;

public final class u_ {
   static final double I = 1.45;
   private static final double T = 4.5;
   private static final double O = 0.09;
   private final yx Z;
   private final gm j = new gm();
   private jk g;
   private jk h;
   private class_9950 Q;
   private int X = -1;
   private int G = -1;
   private yx.s l;
   private double K;
   private yx.o q;
   private yx.x Y;
   private jk w;
   private class_2960 W;
   private double c;
   private long F;

   public u_(yx var1) {
      this.Z = var1;
   }

   public static yx.x d(hc var0) {
      return yx.B(var0, gm.s(gm.O(), gm.f()));
   }

   public void W(class_332 var1, hc var2, x var3, ht var4, int var5, int var6, boolean var7, boolean var8) {
      class_310 var9 = class_310.method_1551();
      this.q = null;
      if (this.Z.c() && GLFW.glfwGetMouseButton(var9.method_22683().method_4490(), 0) != 1) {
         this.Z.k();
      }

      List var10 = gm.O();
      WingsModule.g var11 = gm.f();
      yx.x var12 = yx.B(var2, gm.s(var10, var11));
      int var13 = var12.visible() && var9.method_18506() == null && var9.method_1569() ? 1 : 0;
      long var14 = System.nanoTime();
      yx.o var16 = this.Z.K(var14, (boolean)var13, var7, var8, var3.W(), var12, var2.width(), var5, var6);
      this.K = var16.nameOpacity();
      this.B(var16);
      if (var12.visible() && var16.stage() != yx.s.g) {
         yx.g var17 = var16.toy();
         if (var17 != null) {
            this.O(var9, var17, var14);
         }

         ue.c var18 = var12.model();
         int var19 = Math.round(var12.centerX() + (float)var16.xOffset());
         double var20 = gm.D(var10, var11);
         int var22 = Math.max(var12.renderWidth(), (int)Math.ceil(2.0 * var20 * var12.scale()));
         int var23 = var19 - var22 / 2;
         int var24 = yx.o(var12, gm.s(var10, var11));
         if (var17 == null) {
            var1.method_44379(var2.navigation(0).x() + var2.menuWidth() + 8, var24, var2.width(), var18.y() + var18.height() + 8);
         }

         try {
            if (var17 == null) {
               N(var1, var19, var12.floorY(), Math.min(var18.width() * 0.3F, var12.scale() * 0.62F), 1.0);
            }

            if (!this.D(var9)) {
               if (var17 == null) {
                  int var31 = Math.min(48, var18.width() / 2);
                  var4.K(var1, var19 - var31 / 2, var18.y() + var18.height() / 3, var31);
               }

               return;
            }

            class_8685 var25 = gm.b(var9.method_53462().id(), var4.z());
            jk var26 = var25.comp_1629() == class_7920.field_41122 ? this.h : this.g;
            var26.o(var16.pose(), var17 == null ? null : var17.limbs());
            yv var27 = gm.O();
            if (var27 != null && !lm.R()) {
               var27 = null;
            }

            var26.x(this.j.T(gm.Y(var25), var10, var11, gm.i(), var27, var16.pose(), this.Q));
            if (var17 == null) {
               var26.C(0.0F, 0.0F, 0.0F);
               var1.method_70854(
                  var26,
                  var25.comp_1626().comp_3627(),
                  var12.scale(),
                  0.0F,
                  var16.pose().bodyYaw(),
                  -1.0625F,
                  var23,
                  var24,
                  var23 + var22,
                  var18.y() + var18.height()
               );
               return;
            }

            this.q = var16;
            this.Y = var12;
            this.w = var26;
            this.W = var25.comp_1626().comp_3627();
            this.c = Math.max(1.45, Math.max(0.85 + gm.s(var10, var11) + 0.1, Math.hypot(var20, 0.6) + 0.05));
         } finally {
            if (var17 == null) {
               var1.method_44380();
            }
         }
      }
   }

   public void S(class_332 var1) {
      if (this.q != null) {
         yx.g var2 = this.q.toy();
         yx.x var3 = this.Y;
         float var4 = var3.scale();
         var1.method_71048();
         double var5 = Math.abs(Math.sin(var2.roll()));
         double var7 = 1.0 / (1.0 + var2.lift() * 0.9);
         N(
            var1,
            (float)var2.x(),
            var3.floorY(),
            (float)(Math.min(var3.model().width() * 0.3F, var4 * 0.62F) * (1.0 + 0.9 * var5) / (1.0 + var2.lift() * 0.5)),
            var7
         );
         double var9 = this.q.pose().ageInTicks() / 20.0;
         l(var1, var2, var4, false, var9);
         yx.e var11 = yx.e.of(var2, var4, this.c);
         this.w.C(var2.roll(), var11.shiftX(), var11.shiftY());
         var1.method_70854(
            this.w, this.W, var4, 0.0F, this.q.pose().bodyYaw(), -1.0625F, var11.left(), var11.top(), var11.left() + var11.side(), var11.top() + var11.side()
         );
         var1.method_71048();
         l(var1, var2, var4, true, var9);
      }
   }

   public double l() {
      return this.K;
   }

   public boolean h(double var1, double var3) {
      return this.Z.I(var1, var3);
   }

   public boolean y() {
      return this.Z.c();
   }

   public void P(double var1, double var3) {
      this.Z.f(var1, var3);
   }

   public boolean P(double var1, double var3) {
      if (!this.Z.r(var1, var3)) {
         return false;
      }

      this.Z.f(var1, var3);
      return true;
   }

   public boolean w() {
      return this.Z.k();
   }

   private void B(yx.o var1) {
      yx.s var2 = var1.stage();
      if (var2 != this.l) {
         yx.g var3 = var1.toy();
         switch (var2) {
            case j:
               if (this.l == yx.s.g) {
                  q8.G("Menu", "Account walk-on started after menu reveal; native left-facing stride");
               }
               break;
            case o:
               if (this.l == yx.s.w) {
                  q8.G("Menu", "Account walk-on settled; native idle and pointer tracking active");
               } else if (this.l == yx.s.b) {
                  q8.G("Menu", "Account model back home; account switcher restored");
               } else if (this.l != null && this.l.ordinal() > yx.s.o.ordinal()) {
                  q8.G("Menu", "Account model reset to its resting composition from {}", this.l);
               }
               break;
            case X:
               q8.G("Menu", "Account model picked up from {}; account switcher hidden", this.l);
               break;
            case e:
               q8.G(
                  "Menu",
                  "Account model thrown at {} blocks/s, spin {} rad/s",
                  String.format(Locale.ROOT, "%.1f", Math.hypot(var3.vx(), var3.vy())),
                  String.format(Locale.ROOT, "%.1f", var3.spin())
               );
               break;
            case E:
               q8.G("Menu", "Account model knocked down at roll {} deg{}", Math.round(Math.toDegrees(var3.roll())), var3.dizzy() > 0.0F ? ", dazed" : "");
               break;
            case N:
               if (this.l == yx.s.e) {
                  q8.G("Menu", "Account model landed on its feet");
               }
               break;
            case h:
               q8.G("Menu", "Account model running home from {} px away", Math.round(Math.abs(var1.xOffset())));
         }

         this.l = var2;
         q8.v("Menu", "Account preview: {}", this.l);
      }
   }

   private void O(class_310 var1, yx.g var2, long var3) {
      if (!(var2.impact() < 4.5) && !(var3 - this.F < 9.0E7)) {
         this.F = var3;
         class_3414 var5 = var2.impact() >= 12.0F ? class_3417.field_14794 : class_3417.field_14778;
         var1.method_1483().method_4873(class_1109.method_4757(var5, 1.0F, (float)Math.clamp(var2.impact() / 16.0F, 0.18, 0.7)));
      }
   }

   private static void l(class_332 var0, yx.g var1, float var2, boolean var3, double var4) {
      if (!(var1.dizzy() <= 0.0F)) {
         double var6 = Math.cos(var1.roll());
         double var8 = Math.sin(var1.roll());

         for (int var10 = 0; var10 < 3; var10++) {
            double var11 = var4 * 4.2 + var10 * Math.PI * 2.0 / 3.0;
            double var13 = Math.sin(var11);
            if ((var13 > 0.0 ? 1 : 0) == var3) {
               double var15 = Math.cos(var11) * 0.42;
               double var17 = -0.99 + var13 * 0.08;
               float var19 = (float)(var1.x() + (var15 * var6 - var17 * var8) * var2);
               float var20 = (float)(var1.y() + (var15 * var8 + var17 * var6) * var2);
               float var21 = (float)(var2 * (0.13 + 0.03 * var13) / 64.0);
               var0.method_51448().pushMatrix();

               try {
                  var0.method_51448().translate(var19, var20);
                  var0.method_51448().rotate((float)(var4 * 3.0 + var10));
                  var0.method_51448().scale(var21, var21);
                  var0.method_25293(
                     class_10799.field_56883, gc.z(), -32, -32, 0.0F, 0.0F, 64, 64, 64, 64, 64, 64, ei.D(-855310, var1.dizzy() * (0.75 + 0.25 * var13))
                  );
               } finally {
                  var0.method_51448().popMatrix();
               }
            }
         }
      }
   }

   private boolean D(class_310 var1) {
      int var2 = t_.g;
      if (this.G == var2) {
         return false;
      }

      if (this.X == var2 && this.g != null) {
         return true;
      }

      try {
         class_5599 var3 = var1.method_31974();
         this.g = new jk(var3.method_32072(class_5602.field_27577));
         this.h = new jk(var3.method_32072(class_5602.field_27581), true);
         this.Q = new class_9950(var3.method_32072(class_5602.field_52980));
         this.X = var2;
         q8.G("Menu", "Account skin preview ready; native wide/slim models and cape, resource generation {}", var2);
         return true;
      } catch (RuntimeException var4) {
         this.g = this.h = null;
         this.Q = null;
         this.G = var2;
         q8.u("Menu", "Account model bake failed; showing the account portrait until reopen or resource reload", var4);
         return false;
      }
   }

   private static void N(class_332 var0, float var1, int var2, float var3, double var4) {
      var0.method_51448().pushMatrix();

      try {
         var0.method_51448().translate(var1, var2);
         var0.method_51448().scale(1.0F, 0.13F);

         for (int var6 = 8; var6 >= 1; var6--) {
            gu.Q(var0, 0, 0, Math.max(1, Math.round(var3 * var6 / 8.0F)), ei.R(110663832, var4));
         }
      } finally {
         var0.method_51448().popMatrix();
      }
   }

   public void k() {
      this.Z.U();
   }

   public void k(boolean var1) {
      this.g = this.h = null;
      this.Q = null;
      this.q = null;
      this.w = null;
      this.X = this.G = -1;
      this.Z.e(var1);
   }
}
