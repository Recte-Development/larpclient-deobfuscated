package com.larpclient;

import com.larpclient.module.MenuBackgroundModule;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import java.util.HashSet;
import java.util.Set;
import net.minecraft.class_10799;
import net.minecraft.class_11231;
import net.minecraft.class_11244;
import net.minecraft.class_2960;
import net.minecraft.class_310;
import net.minecraft.class_3300;
import net.minecraft.class_332;
import net.minecraft.class_4588;
import net.minecraft.class_8030;
import org.joml.Matrix3x2f;
import org.joml.Matrix3x2fc;

public final class gc {
   private static final class_2960 j = class_2960.method_60655("larp-client", "textures/gui/logo.png");
   private static final class_2960 U = class_2960.method_60655("larp-client", "textures/gui/logo-hd.png");
   private static final cl d = new cl();
   private static int k = -1;
   private static boolean r;
   private static boolean g;
   private static boolean h;
   private static final Set<class_2960> E = new HashSet<>();
   private static class_2960 D;

   private gc() {
   }

   public static class_2960 z() {
      if (k != t_.g) {
         k = t_.g;
         class_3300 var0 = class_310.method_1551().method_1478();
         r = var0.method_14486(j).isPresent();
         g = var0.method_14486(U).isPresent();
         if (!r) {
            q8.L("Menu", "Logo texture missing; monochrome waves remain available, retry after resource reload");
         }

         if (!g) {
            q8.L("Menu", "High-resolution logo missing; using the 64px logo, retry after resource reload");
         }

         if (g) {
            K(U);
         }

         D = null;
         h = (boolean)0;
      }

      class_2960 var1 = g ? U : j;
      if (!var1.equals(D)) {
         D = var1;
         q8.G("Menu", "Menu logo: {}; standard backdrop and navigation retained", var1);
      }

      return var1;
   }

   private static void K(class_2960 var0) {
      if (E.add(var0)) {
         long var1 = System.nanoTime();

         try {
            class_310.method_1551().method_1531().method_65876(var0, new ca(var0));
            q8.G("Menu", "Mipmapped {} in {} ms", var0, (System.nanoTime() - var1) / 1000000L);
         } catch (RuntimeException var4) {
            q8.L("Menu", "Mipmapped {} unavailable; Minecraft loads it with single-level sampling", var0, var4);
         }
      }
   }

   public static void C(class_332 var0, int var1, int var2) {
      if (var1 > 0 && var2 > 0) {
         class_310 var3 = class_310.method_1551();
         y0 var4 = LarpClient.b().e().F();
         int var5 = var4.k && var4.U != td.r.y ? 1 : 0;
         class_2960 var6 = z();
         double var7 = var3.field_1755 instanceof hh var9 ? var9.b().Y() : 1.0;
         d.Q(System.nanoTime(), (boolean)var5, (Double)var3.field_1690.method_45581().method_41753());
         var0.method_25294(0, 0, var1, var2, ei.W(-16777216, -16185079, var7));
         boolean var20 = MenuBackgroundModule.i(var0, var1, var2, var7);
         var0.method_71048();
         float var21 = 1 / Math.max(1, var3.method_22683().method_4495());

         for (int var11 = 0; var11 < 2 && (!var20 || MenuBackgroundModule.Q()); var11++) {
            int var12 = var11 == 1 ? 1 : 0;
            float var13 = (float)((1.0 - var7) * cl.A(var1, var2) * (var12 != 0 ? 1 : -1));
            Matrix3x2f var14 = new Matrix3x2f(var0.method_51448()).translate(var13, 0.0F);
            int var15 = (int)Math.ceil(cl.A(var1, var2) + var21);
            class_8030 var16 = new class_8030(var12 != 0 ? var1 - var15 : 0, 0, var15, var2).method_71523(var14);
            if (var0.field_44659.method_70863() != null) {
               var16 = var0.field_44659.method_70863().method_49701(var16);
            }

            var0.field_59826.method_70919(new gc.y(var14, var1, var2, (boolean)var12, d.T(), var21, var0.field_44659.method_70863(), var16));
         }

         if ((!var6.equals(j) || r) && var4.l) {
            var0.method_71048();

            for (int var22 = 0; var22 < 18; var22++) {
               cl.m var23 = d.G(var22, var1, var2);
               if (!(var23.alpha() < 0.001)) {
                  int var24 = (int)Math.round(var23.size());
                  var0.method_51448().pushMatrix();

                  try {
                     double var25 = (1.0 - var7) * cl.A(var1, var2) * (var22 % 2 == 0 ? -1 : 1);
                     var0.method_51448().translate((float)(var23.x() + var25), (float)var23.y());
                     var0.method_51448().rotate((float)var23.rotation());
                     var0.method_25293(
                        class_10799.field_56883, var6, -var24 / 2, -var24 / 2, 0.0F, 0.0F, var24, var24, 64, 64, 64, 64, ei.D(-1, var23.alpha() * var7)
                     );
                  } finally {
                     var0.method_51448().popMatrix();
                  }
               }
            }
         }

         if (!h) {
            h = (boolean)1;
            q8.G("Menu", "Lit monochrome side waves ready; {} logo slots, shaded folds, Minecraft panorama speed controls motion", 18);
         }
      }
   }

   public static void q() {
      k = -1;
      D = null;
      h = (boolean)0;
   }

   record y(Matrix3x2fc pose, int width, int height, boolean right, double time, float feather, class_8030 scissorArea, class_8030 bounds)
      implements class_11244 {
      public RenderPipeline comp_4055() {
         return class_10799.field_56879;
      }

      public class_11231 comp_4056() {
         return class_11231.method_70899();
      }

      public void method_70917(class_4588 var1) {
         float var2 = (float)cl.A(this.width, this.height);
         float var3 = this.right ? this.width : 0.0F;

         for (int var4 = 0; var4 < 6; var4++) {
            for (int var5 = 0; var5 < 96; var5++) {
               double var6 = var5 / 96.0;
               double var8 = (var5 + 1.0) / 96.0;
               float var10 = (float)(var6 * this.height);
               float var11 = (float)(var8 * this.height);
               float var12 = (float)(var2 * cl.n(var6, this.time, var4, this.right));
               float var13 = (float)(var2 * cl.n(var8, this.time, var4, this.right));
               if (this.right) {
                  var12 = this.width - var12;
                  var13 = this.width - var13;
               }

               int var14 = cl.o(var6, var4, this.right);
               int var15 = cl.o(var8, var4, this.right);

               for (int var16 = 0; var16 < 4; var16++) {
                  double var17 = cl.u(var16);
                  double var19 = cl.u(var16 + 1);
                  this.quad(
                     var1,
                     (float)(var3 + (var12 - var3) * var17),
                     var10,
                     (float)(var3 + (var13 - var3) * var17),
                     var11,
                     (float)(var3 + (var13 - var3) * var19),
                     var11,
                     (float)(var3 + (var12 - var3) * var19),
                     var10,
                     cl.F(var6, var4, this.right, var17),
                     cl.F(var8, var4, this.right, var17),
                     cl.F(var8, var4, this.right, var19),
                     cl.F(var6, var4, this.right, var19)
                  );
               }

               float var21 = this.right ? -this.feather : this.feather;
               this.quad(var1, var12, var10, var13, var11, var13 + var21, var11, var12 + var21, var10, var14, var15, var15 & 16777215, var14 & 16777215);
            }
         }
      }

      private void quad(
         class_4588 var1,
         float var2,
         float var3,
         float var4,
         float var5,
         float var6,
         float var7,
         float var8,
         float var9,
         int var10,
         int var11,
         int var12,
         int var13
      ) {
         if (this.right) {
            var1.method_70815(this.pose, var8, var9).method_39415(var13);
            var1.method_70815(this.pose, var6, var7).method_39415(var12);
            var1.method_70815(this.pose, var4, var5).method_39415(var11);
            var1.method_70815(this.pose, var2, var3).method_39415(var10);
         } else {
            var1.method_70815(this.pose, var2, var3).method_39415(var10);
            var1.method_70815(this.pose, var4, var5).method_39415(var11);
            var1.method_70815(this.pose, var6, var7).method_39415(var12);
            var1.method_70815(this.pose, var8, var9).method_39415(var13);
         }
      }

      public class_8030 comp_4069() {
         return this.scissorArea;
      }

      public class_8030 comp_4274() {
         return this.bounds;
      }
   }
}
