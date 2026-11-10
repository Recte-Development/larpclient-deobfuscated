package com.larpclient;

import net.minecraft.class_243;

public final class c {
   public static void J(f1 var0, u8 var1, u8.s[] var2, double var3, int var5, int var6, boolean var7, yt.l var8, jo.c var9, g4.j var10) {
      z(var0, var1, var2, var3, var5, var6, var7, 1.0, var8, var9, var10);
   }

   public static void z(f1 var0, u8 var1, u8.s[] var2, double var3, int var5, int var6, boolean var7, double var8, yt.l var10, jo.c var11, g4.j var12) {
      var0.blend(ti.d.l);
      double var13 = (var7 ? 0.55 : 1.0) * Math.clamp(var8, 0.0, 1.0);
      switch (var1.q) {
         case H:
            N(var0, var1, var2, var3, var5, var6, var13);
            break;
         case R:
            T(var0, var1, var3, var5, var6, var13);
            break;
         case I:
            e(var0, var1, var3, var5, var6, var13);
            break;
         case Y:
            J(var0, var1, var3, var5, var6, var13, var10);
            break;
         case e:
            i(var0, var1, var11, var3, var5, var6, var13);
            break;
         case K:
            i(var0, var1, var12, var3, var5, var6, var13);
            break;
         case y:
            g2.c(var0, var1, var3, var5, var6, var13, var7);
            break;
         case v:
            g0.B(var0, var1, var3, var5, var6, var13, var7);
            break;
         case G:
            e8.b(var0, var1, var3, var5, var6, var13, var7, var10);
      }
   }

   private static void i(f1 var0, u8 var1, g4.j var2, double var3, int var5, int var6, double var7) {
      if (var2 != null) {
         double var9 = u8.o(var3, 0.0, 0.78) * var7;
         u(var0, var2.feet().method_1031(0.0, 0.025, 0.0), 0.52, 0.025, var5, var9 * 0.7);
         double var11 = var3 - 1.62;
         class_243 var13 = var1.z.method_1031(-Math.sin(var1.y) * 0.84, 0.42, Math.cos(var1.y) * 0.84);
         double var14 = u8.o(var11, 0.0, 0.55) * var7;
         u(var0, var13, 0.18 + Math.max(0.0, var11) * 2.4, 0.04, var5, var14);
         u(var0, var13.method_1031(0.0, 0.015, 0.0), 0.15 + Math.max(0.0, var11) * 1.8, 0.016, var6, var14 * 0.7);
         k(var0, var1, var11, var13, var5, var6, 28, 1.1, var7 * 0.7);
         double var16 = u8.o(var3, 2.16, 6.94) * var7;
         double var18 = 0.5 + 0.5 * Math.cos((var3 - 2.16) * Math.PI * 4.0 / 1.12);
         u(var0, var13.method_1031(0.0, 0.025, 0.0), 0.38 + var18 * 0.06, 0.012, var5, var16 * 0.22);

         for (class_243 var23 : new class_243[]{var2.rightHand(), var2.leftHand()}) {
            var0.V(var23.field_1352, var23.field_1351, var23.field_1350, 0.035, 0.0, ei.R(var6, var16 * 0.65));
         }

         double var28 = u8.o(var3, 6.640000000000001, 7.5) * var7;

         for (int var29 = 0; var29 < 12; var29++) {
            double var30 = var1.P(var29 + 970) * Math.PI * 2.0;
            double var25 = var1.P(var29 + 990) * 3.6;
            class_243 var27 = var2.feet().method_1031(Math.cos(var30) * 0.45, var25 + (var3 - 6.640000000000001) * 0.3, Math.sin(var30) * 0.45);
            var0.V(var27.field_1352, var27.field_1351, var27.field_1350, 0.022, var30, ei.R(var5, var28 * 0.5));
         }
      }
   }

   private static void i(f1 var0, u8 var1, jo.c var2, double var3, int var5, int var6, double var7) {
      if (var2 != null) {
         double var9 = u8.o(var3, 0.0, 1.8) * var7;
         u(var0, var2.feet().method_1031(0.0, 0.035, 0.0), 0.72, 0.024, var5, var9 * 0.65);
         u(var0, var2.feet().method_1031(0.0, 0.04, 0.0), 0.92, 0.01, var6, var9 * 0.35);
         double var11 = u8.o(var3, 0.0, 0.85) * var7;

         for (int var13 = 0; var13 < 24; var13++) {
            double var14 = var13 / 24.0;
            double var16 = var14 * Math.PI * 5.0 + var3 * 5.0;
            class_243 var18 = var2.feet().method_1031(Math.cos(var16) * 0.52, var14 * 3.6, Math.sin(var16) * 0.52);
            var0.V(var18.field_1352, var18.field_1351, var18.field_1350, 0.025, var16, ei.R(var6, var11 * Math.sin(var14 * Math.PI)));
         }

         double var29 = u8.o(var3, 1.28, 1.86) * var7;

         for (int var15 = 1; var15 < var2.trail().length; var15++) {
            double var31 = (double)var15 / var2.trail().length;
            E(var0, var2.trail()[var15 - 1], var2.trail()[var15], 0.085 * var31, var5, var6, var29 * var31);
         }

         class_243 var30 = var2.tip();
         var0.V(var30.field_1352, var30.field_1351, var30.field_1350, 0.09, 0.0, ei.R(var6, var29 * 0.8));
         double var32 = var3 - 1.56;
         double var33 = u8.o(var32, 0.0, 0.65) * var7;
         class_243 var20 = var1.z.method_1031(0.0, 1.65, 0.0);
         var0.V(var20.field_1352, var20.field_1351, var20.field_1350, 0.18 + var33 * 0.22, var3, ei.R(var6, var33));
         u(var0, var1.z.method_1031(0.0, 0.04, 0.0), 0.15 + Math.max(0.0, var32) * 3.1, 0.06, var5, var33);
         u(var0, var1.z.method_1031(0.0, 0.045, 0.0), 0.12 + Math.max(0.0, var32) * 2.35, 0.018, var6, var33 * 0.7);
         k(var0, var1, var32, var20, var5, var6, 40, 1.7, var7);
         double var21 = u8.o(var3, 3.25, 4.6) * var7;

         for (int var23 = 0; var23 < 16; var23++) {
            double var24 = var1.P(var23 + 930);
            double var26 = var1.P(var23 + 950) * Math.PI * 2.0;
            class_243 var28 = var2.feet()
               .method_1031(Math.cos(var26) * (0.35 + var24 * 0.4), var24 * 3.6 + Math.max(0.0, var3 - 3.25) * 0.45, Math.sin(var26) * (0.35 + var24 * 0.4));
            var0.V(var28.field_1352, var28.field_1351, var28.field_1350, 0.025, var26, ei.R(var5, var21 * 0.65));
         }
      }
   }

   private static void J(f1 var0, u8 var1, double var2, int var4, int var5, double var6, yt.l var8) {
      double var9 = yt.s(var2);
      double var11 = u8.S((4.6 - var2) / 0.75);
      double var13 = u8.o(var2, 0.0, 1.55) * var6;
      double var15 = 1.7 * u8.S(var2 / 0.85);
      u(var0, var1.z.method_1031(0.0, 0.04, 0.0), 0.65 + 0.2 * Math.sin(var2 * 3.0), 0.025, var4, var13);

      for (int var17 = 0; var17 < 2; var17++) {
         class_243 var18 = null;

         for (int var19 = 0; var19 <= 30; var19++) {
            double var20 = var19 / 30.0;
            double var22 = var20 * Math.PI * 3.0 + var2 * 4.0 + var17 * Math.PI;
            class_243 var24 = var1.z.method_1031(Math.cos(var22) * 0.52, var15 + var20 * 1.8, Math.sin(var22) * 0.52);
            if (var18 != null) {
               E(var0, var18, var24, 0.025, var4, var5, var13 * Math.sin(var20 * Math.PI));
            }

            var18 = var24;
         }
      }

      class_243 var34 = yt.h(var1, var2);
      double var35 = u8.o(var2, 1.75, 2.5);
      double var36 = var9 * var11 * var6 * (0.6 + 0.12 * Math.sin(var2 * 4.0) + var35 * 0.35);

      for (int var37 = 0; var37 < 6; var37++) {
         yt.y var23 = yt.q(var1, var2, var8, var37);
         class_243 var39 = var23.center().method_35590(var23.from(), var9);
         class_243 var25 = var23.center().method_35590(var23.to(), var9);
         E(var0, var39, var25, 0.065, var4, var5, var36);
         class_243 var26 = var23.from();
         var0.V(var26.field_1352, var26.field_1351, var26.field_1350, 0.07 + var35 * 0.055, var2 + var37, ei.R(var5, var36));

         for (int var27 = 0; var27 < 3; var27++) {
            double var28 = (var2 * 0.45 + var37 * 0.13 + var27 / 3.0) % 1.0;
            class_243 var30 = var39.method_35590(var25, var28).method_1019(var8.normal().method_1021(0.13));
            var0.V(var30.field_1352, var30.field_1351, var30.field_1350, 0.025, 0.0, ei.R(var5, var36 * 0.7));
         }
      }

      double var38 = (var9 * 0.6 + var35 * 0.4) * var11 * var6;
      if (var38 > 0.001) {
         var0.E(
            var34.field_1352,
            var34.field_1351,
            var34.field_1350,
            1.1729999999999998 + var35 * 0.18,
            0.018,
            ei.R(var4, var38 * 0.55),
            var8.normal().field_1352,
            var8.normal().field_1351,
            var8.normal().field_1350
         );
      }

      double var40 = var2 - 3.8;
      if (var40 >= 0.0) {
         double var41 = u8.o(var40, 0.0, 0.8) * var6;

         for (int var42 = 0; var42 < 24; var42++) {
            double var29 = var1.P(var42 + 700) * Math.PI * 2.0;
            double var31 = 1.02 + var40 * (0.4 + var1.P(var42 + 730));
            class_243 var33 = var34.method_1019(var8.right().method_1021(Math.cos(var29) * var31)).method_1019(var8.up().method_1021(Math.sin(var29) * var31));
            var0.V(var33.field_1352, var33.field_1351, var33.field_1350, 0.025, var29, ei.R(var5, var41));
         }
      }
   }

   private static void N(f1 var0, u8 var1, u8.s[] var2, double var3, int var5, int var6, double var7) {
      double var9 = 1.7 * u8.S(var3 / 0.85);
      double var11 = u8.o(var3, 0.0, 1.4) * var7;
      u(var0, var1.z.method_1031(0.0, 0.035, 0.0), 0.65 + 0.35 * u8.S(var3 / 0.8), 0.035, var5, var11);
      u(var0, var1.z.method_1031(0.0, 0.04, 0.0), 1.08, 0.012, var6, var11 * 0.6);

      for (int var13 = 0; var13 < 2; var13++) {
         class_243 var14 = null;

         for (int var15 = 0; var15 <= 40; var15++) {
            double var16 = var15 / 40.0;
            double var18 = var16 * Math.PI * 3.0 + var3 * 4.0 + var13 * Math.PI;
            class_243 var20 = var1.z.method_1031(Math.cos(var18) * 0.64, var9 + var16 * 1.95, Math.sin(var18) * 0.64);
            if (var14 != null) {
               E(var0, var14, var20, 0.022, var5, var6, var11 * Math.sin(var16 * Math.PI));
            }

            var14 = var20;
         }
      }

      for (int var26 = 0; var26 < 3; var26++) {
         double var28 = var3 - (0.82 + var26 * 0.14);
         double var30 = u8.o(var28, 0.0, 0.55) * var7;
         if (!(var30 <= 0.0)) {
            class_243 var32 = var1.z.method_1031(0.0, 2.25 + var26 * 0.55, 0.0);
            class_243 var19 = null;

            for (int var33 = 0; var33 <= 28; var33++) {
               double var21 = var33 / 28.0;
               double var23 = -2.7 + var21 * 4.6 + Math.min(var28 / 0.12, 1.0) * 0.5;
               class_243 var25 = var32.method_1031(Math.cos(var23) * 1.1, Math.sin(var23) * 0.3 * (var26 == 1 ? -1 : 1), Math.sin(var23) * 1.1);
               if (var19 != null) {
                  E(var0, var19, var25, 0.085 * Math.sin(var21 * Math.PI), var5, var6, var30);
               }

               var19 = var25;
            }

            u(var0, var32, 0.45 + var28 * 2.0, 0.028, var6, var30 * 0.55);
         }
      }

      k(var0, var1, var3 - 1.25, var1.z.method_1031(0.0, 2.65, 0.0), var5, var6, 48, 1.7, var7);
      double var27 = var3 - 1.25;
      u(var0, var1.z.method_1031(0.0, 0.035, 0.0), 0.2 + Math.max(0.0, var27) * 2.6, 0.07, var6, u8.o(var27, 0.0, 0.75) * var7);

      for (int var29 = 0; var29 < var2.length; var29++) {
         if (var2[var29].landed()) {
            u8.s var31 = var2[var29];
            var0.V(
               var31.position().field_1352, var31.position().field_1351 + 0.01, var31.position().field_1350, 0.11, 0.0, ei.R(var5, var31.alpha() * 0.6 * var7)
            );
         }
      }
   }

   private static void T(f1 var0, u8 var1, double var2, int var4, int var5, double var6) {
      double var8 = u8.o(var2, 0.0, 2.05) * var6;
      class_243 var10 = var1.z.method_1031(0.0, 1.15, 0.0);
      double var11 = u8.S((var2 - 1.45) / 0.45);

      for (int var13 = 0; var13 < 3; var13++) {
         class_243 var14 = null;

         for (int var15 = 0; var15 <= 56; var15++) {
            double var16 = var15 / 56.0;
            double var18 = var16 * Math.PI * 5.0 - var2 * 5.0 + var13 * Math.PI * 2.0 / 3.0;
            double var20 = (1.45 * (1.0 - var16) + 0.08) * (1.0 - var11);
            class_243 var22 = var10.method_1031(Math.cos(var18) * var20, (var16 - 0.5) * 1.4 * (1.0 - var11), Math.sin(var18) * var20);
            if (var14 != null) {
               E(var0, var14, var22, 0.032, var4, var5, var8 * Math.sin(var16 * Math.PI));
            }

            var14 = var22;
         }
      }

      u(var0, var1.z.method_1031(0.0, 0.04, 0.0), 1.3 * (1.0 - var11), 0.05, var4, var8);
      double var23 = u8.o(var2, 1.83, 2.3) * var6;
      var0.V(var10.field_1352, var10.field_1351, var10.field_1350, 0.2 + var23 * 0.5, var2, ei.R(var5, var23));
      u(var0, var10, Math.max(0.0, var2 - 1.85) * 4.0, 0.065, var5, var23);
      k(var0, var1, var2 - 1.9, var10, var4, var5, 56, 2.4, var6);

      for (int var24 = 0; var24 < 48; var24++) {
         double var25 = (var2 * 0.75 + var1.P(var24)) % 1.0;
         double var26 = var1.P(var24 + 70) * Math.PI * 2.0 + var2 * 3.0;
         double var27 = (1.0 - var25) * 2.2;
         class_243 var28 = var10.method_1031(Math.cos(var26) * var27, (var1.P(var24 + 140) - 0.5) * var27, Math.sin(var26) * var27);
         var0.V(
            var28.field_1352, var28.field_1351, var28.field_1350, 0.025 + 0.035 * var25, var26, ei.R(ei.W(var4, var5, var25), var8 * Math.sin(var25 * Math.PI))
         );
      }
   }

   private static void e(f1 var0, u8 var1, double var2, int var4, int var5, double var6) {
      double var8 = u8.o(var2, 0.0, 2.9) * var6;
      u(var0, var1.z.method_1031(0.0, 0.04, 0.0), 0.65 + Math.sin(var2 * 3.0) * 0.06, 0.045, var4, var8);

      for (int var10 = 0; var10 < 3; var10++) {
         class_243 var11 = null;

         for (int var12 = 0; var12 <= 36; var12++) {
            double var13 = var12 / 36.0;
            double var15 = var13 * 5.0 + var2 * 2.0 + var10 * Math.PI * 2.0 / 3.0;
            double var17 = 0.45 * (1.0 - var13) + 0.05;
            class_243 var19 = var1.z.method_1031(Math.cos(var15) * var17, 0.2 + var13 * 3.4, Math.sin(var15) * var17);
            if (var11 != null) {
               E(var0, var11, var19, 0.04 * (1.0 - var13), var4, var5, var8 * Math.sin(var13 * Math.PI));
            }

            var11 = var19;
         }
      }

      for (int var23 = 0; var23 < 88; var23++) {
         double var24 = var1.P(var23) * 1.5 + 0.3;
         double var25 = var2 - var24;
         if (!(var25 < 0.0) && !(var25 > 2.0)) {
            double var26 = var1.P(var23 + 100) * Math.PI * 2.0 + var25 * 1.4;
            double var27 = (0.12 + var1.P(var23 + 200) * 0.5) * (1.0 - var25 / 2.0);
            class_243 var28 = var1.z.method_1031(Math.cos(var26) * var27, var1.P(var23 + 300) * 1.8 + var25 * 1.65, Math.sin(var26) * var27);
            class_243 var20 = var28.method_1031(-Math.cos(var26) * 0.02, -0.1 - var25 * 0.03, -Math.sin(var26) * 0.02);
            double var21 = u8.o(var25, 0.0, 2.0) * var6;
            E(var0, var20, var28, 0.025, var4, var5, var21);
            var0.V(var28.field_1352, var28.field_1351, var28.field_1350, 0.035, var26, ei.R(ei.W(var4, var5, var25 / 2.0), var21));
         }
      }

      u(var0, var1.z.method_1031(0.0, 0.05, 0.0), Math.max(0.0, var2 - 1.55) * 1.9, 0.045, var5, u8.o(var2, 1.55, 2.5) * var6);
   }

   static void k(f1 var0, u8 var1, double var2, class_243 var4, int var5, int var6, int var7, double var8, double var10) {
      if (!(var2 < 0.0) && !(var2 > 1.2)) {
         for (int var12 = 0; var12 < var7; var12++) {
            double var13 = var1.P(var12 + 400) * Math.PI * 2.0;
            class_243 var15 = new class_243(Math.cos(var13) * var8, (var1.P(var12 + 500) - 0.3) * var8, Math.sin(var13) * var8);
            class_243 var16 = var4.method_1019(var15.method_1021(var2)).method_1031(0.0, -var2 * var2 * 0.9, 0.0);
            class_243 var17 = var16.method_1020(var15.method_1021(0.06));
            double var18 = u8.o(var2, 0.0, 1.2) * var10;
            E(var0, var17, var16, 0.022, var5, var6, var18);
            var0.V(var16.field_1352, var16.field_1351, var16.field_1350, 0.027, var13, ei.R(var6, var18));
         }
      }
   }

   static void u(f1 var0, class_243 var1, double var2, double var4, int var6, double var7) {
      if (var7 > 0.001 && var2 > 0.001) {
         var0.r(var1, var2, var4, ei.R(var6, var7));
      }
   }

   static void E(f1 var0, class_243 var1, class_243 var2, double var3, int var5, int var6, double var7) {
      if (!(var7 <= 0.001) && !(var3 <= 0.001)) {
         var0.ribbon(var1, var2, var3, ei.R(var5, var7 * 0.5), ei.R(var6, var7 * 0.5));
         var0.ribbon(var1, var2, var3 * 0.3, ei.R(ei.W(var5, -1, 0.75), var7), ei.R(-1, var7));
      }
   }

   private c() {
   }
}
