package com.larpclient;

import net.minecraft.world.phys.Vec3;

public final class sh {
   public static void C(kj var0, pj var1, pj.n[] var2, double var3, int var5, int var6, boolean var7, uy.b var8, ln.v var9, ja.w var10) {
      K(var0, var1, var2, var3, var5, var6, var7, 1.0, var8, var9, var10);
   }

   public static void K(kj var0, pj var1, pj.n[] var2, double var3, int var5, int var6, boolean var7, double var8, uy.b var10, ln.v var11, ja.w var12) {
      var0.blend(gl.c.E);
      double var13 = (var7 ? 0.55 : 1.0) * Math.clamp(var8, 0.0, 1.0);
      switch (var1.h) {
         case e:
            C(var0, var1, var2, var3, var5, var6, var13);
            break;
         case X:
            A(var0, var1, var3, var5, var6, var13);
            break;
         case h:
            K(var0, var1, var3, var5, var6, var13);
            break;
         case P:
            Z(var0, var1, var3, var5, var6, var13, var10);
            break;
         case Q:
            V(var0, var1, var11, var3, var5, var6, var13);
            break;
         case E:
            M(var0, var1, var12, var3, var5, var6, var13);
            break;
         case m:
            uu.h(var0, var1, var3, var5, var6, var13, var7);
            break;
         case C:
            se.K(var0, var1, var3, var5, var6, var13, var7);
            break;
         case L:
            b3.d(var0, var1, var3, var5, var6, var13, var7, var10);
      }
   }

   private static void M(kj var0, pj var1, ja.w var2, double var3, int var5, int var6, double var7) {
      if (var2 != null) {
         double var9 = pj.T(var3, 0.0, 0.78) * var7;
         q(var0, var2.feet().add(0.0, 0.025, 0.0), 0.52, 0.025, var5, var9 * 0.7);
         double var11 = var3 - 1.62;
         Vec3 var13 = var1.v.add(-Math.sin(var1.z) * 0.84, 0.42, Math.cos(var1.z) * 0.84);
         double var14 = pj.T(var11, 0.0, 0.55) * var7;
         q(var0, var13, 0.18 + Math.max(0.0, var11) * 2.4, 0.04, var5, var14);
         q(var0, var13.add(0.0, 0.015, 0.0), 0.15 + Math.max(0.0, var11) * 1.8, 0.016, var6, var14 * 0.7);
         C(var0, var1, var11, var13, var5, var6, 28, 1.1, var7 * 0.7);
         double var16 = pj.T(var3, 2.16, 6.94) * var7;
         double var18 = 0.5 + 0.5 * Math.cos((var3 - 2.16) * Math.PI * 4.0 / 1.12);
         q(var0, var13.add(0.0, 0.025, 0.0), 0.38 + var18 * 0.06, 0.012, var5, var16 * 0.22);

         for (Vec3 var23 : new Vec3[]{var2.rightHand(), var2.leftHand()}) {
            var0.P(var23.x, var23.y, var23.z, 0.035, 0.0, uh.F(var6, var16 * 0.65));
         }

         double var28 = pj.T(var3, 6.640000000000001, 7.5) * var7;

         for (int var29 = 0; var29 < 12; var29++) {
            double var30 = var1.F(var29 + 970) * Math.PI * 2.0;
            double var25 = var1.F(var29 + 990) * 3.6;
            Vec3 var27 = var2.feet().add(Math.cos(var30) * 0.45, var25 + (var3 - 6.640000000000001) * 0.3, Math.sin(var30) * 0.45);
            var0.P(var27.x, var27.y, var27.z, 0.022, var30, uh.F(var5, var28 * 0.5));
         }
      }
   }

   private static void V(kj var0, pj var1, ln.v var2, double var3, int var5, int var6, double var7) {
      if (var2 != null) {
         double var9 = pj.T(var3, 0.0, 1.8) * var7;
         q(var0, var2.feet().add(0.0, 0.035, 0.0), 0.72, 0.024, var5, var9 * 0.65);
         q(var0, var2.feet().add(0.0, 0.04, 0.0), 0.92, 0.01, var6, var9 * 0.35);
         double var11 = pj.T(var3, 0.0, 0.85) * var7;

         for (int var13 = 0; var13 < 24; var13++) {
            double var14 = var13 / 24.0;
            double var16 = var14 * Math.PI * 5.0 + var3 * 5.0;
            Vec3 var18 = var2.feet().add(Math.cos(var16) * 0.52, var14 * 3.6, Math.sin(var16) * 0.52);
            var0.P(var18.x, var18.y, var18.z, 0.025, var16, uh.F(var6, var11 * Math.sin(var14 * Math.PI)));
         }

         double var29 = pj.T(var3, 1.28, 1.86) * var7;

         for (int var15 = 1; var15 < var2.trail().length; var15++) {
            double var31 = (double)var15 / var2.trail().length;
            r(var0, var2.trail()[var15 - 1], var2.trail()[var15], 0.085 * var31, var5, var6, var29 * var31);
         }

         Vec3 var30 = var2.tip();
         var0.P(var30.x, var30.y, var30.z, 0.09, 0.0, uh.F(var6, var29 * 0.8));
         double var32 = var3 - 1.56;
         double var33 = pj.T(var32, 0.0, 0.65) * var7;
         Vec3 var20 = var1.v.add(0.0, 1.65, 0.0);
         var0.P(var20.x, var20.y, var20.z, 0.18 + var33 * 0.22, var3, uh.F(var6, var33));
         q(var0, var1.v.add(0.0, 0.04, 0.0), 0.15 + Math.max(0.0, var32) * 3.1, 0.06, var5, var33);
         q(var0, var1.v.add(0.0, 0.045, 0.0), 0.12 + Math.max(0.0, var32) * 2.35, 0.018, var6, var33 * 0.7);
         C(var0, var1, var32, var20, var5, var6, 40, 1.7, var7);
         double var21 = pj.T(var3, 3.25, 4.6) * var7;

         for (int var23 = 0; var23 < 16; var23++) {
            double var24 = var1.F(var23 + 930);
            double var26 = var1.F(var23 + 950) * Math.PI * 2.0;
            Vec3 var28 = var2.feet()
               .add(Math.cos(var26) * (0.35 + var24 * 0.4), var24 * 3.6 + Math.max(0.0, var3 - 3.25) * 0.45, Math.sin(var26) * (0.35 + var24 * 0.4));
            var0.P(var28.x, var28.y, var28.z, 0.025, var26, uh.F(var5, var21 * 0.65));
         }
      }
   }

   private static void Z(kj var0, pj var1, double var2, int var4, int var5, double var6, uy.b var8) {
      double var9 = uy.O(var2);
      double var11 = pj.Z((4.6 - var2) / 0.75);
      double var13 = pj.T(var2, 0.0, 1.55) * var6;
      double var15 = 1.7 * pj.Z(var2 / 0.85);
      q(var0, var1.v.add(0.0, 0.04, 0.0), 0.65 + 0.2 * Math.sin(var2 * 3.0), 0.025, var4, var13);

      for (int var17 = 0; var17 < 2; var17++) {
         Vec3 var18 = null;

         for (int var19 = 0; var19 <= 30; var19++) {
            double var20 = var19 / 30.0;
            double var22 = var20 * Math.PI * 3.0 + var2 * 4.0 + var17 * Math.PI;
            Vec3 var24 = var1.v.add(Math.cos(var22) * 0.52, var15 + var20 * 1.8, Math.sin(var22) * 0.52);
            if (var18 != null) {
               r(var0, var18, var24, 0.025, var4, var5, var13 * Math.sin(var20 * Math.PI));
            }

            var18 = var24;
         }
      }

      Vec3 var34 = uy.B(var1, var2);
      double var35 = pj.T(var2, 1.75, 2.5);
      double var36 = var9 * var11 * var6 * (0.6 + 0.12 * Math.sin(var2 * 4.0) + var35 * 0.35);

      for (int var37 = 0; var37 < 6; var37++) {
         uy.u var23 = uy.J(var1, var2, var8, var37);
         Vec3 var39 = var23.center().lerp(var23.from(), var9);
         Vec3 var25 = var23.center().lerp(var23.to(), var9);
         r(var0, var39, var25, 0.065, var4, var5, var36);
         Vec3 var26 = var23.from();
         var0.P(var26.x, var26.y, var26.z, 0.07 + var35 * 0.055, var2 + var37, uh.F(var5, var36));

         for (int var27 = 0; var27 < 3; var27++) {
            double var28 = (var2 * 0.45 + var37 * 0.13 + var27 / 3.0) % 1.0;
            Vec3 var30 = var39.lerp(var25, var28).add(var8.normal().scale(0.13));
            var0.P(var30.x, var30.y, var30.z, 0.025, 0.0, uh.F(var5, var36 * 0.7));
         }
      }

      double var38 = (var9 * 0.6 + var35 * 0.4) * var11 * var6;
      if (var38 > 0.001) {
         var0.Q(
            var34.x, var34.y, var34.z, 1.1729999999999998 + var35 * 0.18, 0.018, uh.F(var4, var38 * 0.55), var8.normal().x, var8.normal().y, var8.normal().z
         );
      }

      double var40 = var2 - 3.8;
      if (var40 >= 0.0) {
         double var41 = pj.T(var40, 0.0, 0.8) * var6;

         for (int var42 = 0; var42 < 24; var42++) {
            double var29 = var1.F(var42 + 700) * Math.PI * 2.0;
            double var31 = 1.02 + var40 * (0.4 + var1.F(var42 + 730));
            Vec3 var33 = var34.add(var8.right().scale(Math.cos(var29) * var31)).add(var8.up().scale(Math.sin(var29) * var31));
            var0.P(var33.x, var33.y, var33.z, 0.025, var29, uh.F(var5, var41));
         }
      }
   }

   private static void C(kj var0, pj var1, pj.n[] var2, double var3, int var5, int var6, double var7) {
      double var9 = 1.7 * pj.Z(var3 / 0.85);
      double var11 = pj.T(var3, 0.0, 1.4) * var7;
      q(var0, var1.v.add(0.0, 0.035, 0.0), 0.65 + 0.35 * pj.Z(var3 / 0.8), 0.035, var5, var11);
      q(var0, var1.v.add(0.0, 0.04, 0.0), 1.08, 0.012, var6, var11 * 0.6);

      for (int var13 = 0; var13 < 2; var13++) {
         Vec3 var14 = null;

         for (int var15 = 0; var15 <= 40; var15++) {
            double var16 = var15 / 40.0;
            double var18 = var16 * Math.PI * 3.0 + var3 * 4.0 + var13 * Math.PI;
            Vec3 var20 = var1.v.add(Math.cos(var18) * 0.64, var9 + var16 * 1.95, Math.sin(var18) * 0.64);
            if (var14 != null) {
               r(var0, var14, var20, 0.022, var5, var6, var11 * Math.sin(var16 * Math.PI));
            }

            var14 = var20;
         }
      }

      for (int var26 = 0; var26 < 3; var26++) {
         double var28 = var3 - (0.82 + var26 * 0.14);
         double var30 = pj.T(var28, 0.0, 0.55) * var7;
         if (!(var30 <= 0.0)) {
            Vec3 var32 = var1.v.add(0.0, 2.25 + var26 * 0.55, 0.0);
            Vec3 var19 = null;

            for (int var33 = 0; var33 <= 28; var33++) {
               double var21 = var33 / 28.0;
               double var23 = -2.7 + var21 * 4.6 + Math.min(var28 / 0.12, 1.0) * 0.5;
               Vec3 var25 = var32.add(Math.cos(var23) * 1.1, Math.sin(var23) * 0.3 * (var26 == 1 ? -1 : 1), Math.sin(var23) * 1.1);
               if (var19 != null) {
                  r(var0, var19, var25, 0.085 * Math.sin(var21 * Math.PI), var5, var6, var30);
               }

               var19 = var25;
            }

            q(var0, var32, 0.45 + var28 * 2.0, 0.028, var6, var30 * 0.55);
         }
      }

      C(var0, var1, var3 - 1.25, var1.v.add(0.0, 2.65, 0.0), var5, var6, 48, 1.7, var7);
      double var27 = var3 - 1.25;
      q(var0, var1.v.add(0.0, 0.035, 0.0), 0.2 + Math.max(0.0, var27) * 2.6, 0.07, var6, pj.T(var27, 0.0, 0.75) * var7);

      for (int var29 = 0; var29 < var2.length; var29++) {
         if (var2[var29].landed()) {
            pj.n var31 = var2[var29];
            var0.P(var31.position().x, var31.position().y + 0.01, var31.position().z, 0.11, 0.0, uh.F(var5, var31.alpha() * 0.6 * var7));
         }
      }
   }

   private static void A(kj var0, pj var1, double var2, int var4, int var5, double var6) {
      double var8 = pj.T(var2, 0.0, 2.05) * var6;
      Vec3 var10 = var1.v.add(0.0, 1.15, 0.0);
      double var11 = pj.Z((var2 - 1.45) / 0.45);

      for (int var13 = 0; var13 < 3; var13++) {
         Vec3 var14 = null;

         for (int var15 = 0; var15 <= 56; var15++) {
            double var16 = var15 / 56.0;
            double var18 = var16 * Math.PI * 5.0 - var2 * 5.0 + var13 * Math.PI * 2.0 / 3.0;
            double var20 = (1.45 * (1.0 - var16) + 0.08) * (1.0 - var11);
            Vec3 var22 = var10.add(Math.cos(var18) * var20, (var16 - 0.5) * 1.4 * (1.0 - var11), Math.sin(var18) * var20);
            if (var14 != null) {
               r(var0, var14, var22, 0.032, var4, var5, var8 * Math.sin(var16 * Math.PI));
            }

            var14 = var22;
         }
      }

      q(var0, var1.v.add(0.0, 0.04, 0.0), 1.3 * (1.0 - var11), 0.05, var4, var8);
      double var23 = pj.T(var2, 1.83, 2.3) * var6;
      var0.P(var10.x, var10.y, var10.z, 0.2 + var23 * 0.5, var2, uh.F(var5, var23));
      q(var0, var10, Math.max(0.0, var2 - 1.85) * 4.0, 0.065, var5, var23);
      C(var0, var1, var2 - 1.9, var10, var4, var5, 56, 2.4, var6);

      for (int var24 = 0; var24 < 48; var24++) {
         double var25 = (var2 * 0.75 + var1.F(var24)) % 1.0;
         double var26 = var1.F(var24 + 70) * Math.PI * 2.0 + var2 * 3.0;
         double var27 = (1.0 - var25) * 2.2;
         Vec3 var28 = var10.add(Math.cos(var26) * var27, (var1.F(var24 + 140) - 0.5) * var27, Math.sin(var26) * var27);
         var0.P(var28.x, var28.y, var28.z, 0.025 + 0.035 * var25, var26, uh.F(uh.q(var4, var5, var25), var8 * Math.sin(var25 * Math.PI)));
      }
   }

   private static void K(kj var0, pj var1, double var2, int var4, int var5, double var6) {
      double var8 = pj.T(var2, 0.0, 2.9) * var6;
      q(var0, var1.v.add(0.0, 0.04, 0.0), 0.65 + Math.sin(var2 * 3.0) * 0.06, 0.045, var4, var8);

      for (int var10 = 0; var10 < 3; var10++) {
         Vec3 var11 = null;

         for (int var12 = 0; var12 <= 36; var12++) {
            double var13 = var12 / 36.0;
            double var15 = var13 * 5.0 + var2 * 2.0 + var10 * Math.PI * 2.0 / 3.0;
            double var17 = 0.45 * (1.0 - var13) + 0.05;
            Vec3 var19 = var1.v.add(Math.cos(var15) * var17, 0.2 + var13 * 3.4, Math.sin(var15) * var17);
            if (var11 != null) {
               r(var0, var11, var19, 0.04 * (1.0 - var13), var4, var5, var8 * Math.sin(var13 * Math.PI));
            }

            var11 = var19;
         }
      }

      for (int var23 = 0; var23 < 88; var23++) {
         double var24 = var1.F(var23) * 1.5 + 0.3;
         double var25 = var2 - var24;
         if (!(var25 < 0.0) && !(var25 > 2.0)) {
            double var26 = var1.F(var23 + 100) * Math.PI * 2.0 + var25 * 1.4;
            double var27 = (0.12 + var1.F(var23 + 200) * 0.5) * (1.0 - var25 / 2.0);
            Vec3 var28 = var1.v.add(Math.cos(var26) * var27, var1.F(var23 + 300) * 1.8 + var25 * 1.65, Math.sin(var26) * var27);
            Vec3 var20 = var28.add(-Math.cos(var26) * 0.02, -0.1 - var25 * 0.03, -Math.sin(var26) * 0.02);
            double var21 = pj.T(var25, 0.0, 2.0) * var6;
            r(var0, var20, var28, 0.025, var4, var5, var21);
            var0.P(var28.x, var28.y, var28.z, 0.035, var26, uh.F(uh.q(var4, var5, var25 / 2.0), var21));
         }
      }

      q(var0, var1.v.add(0.0, 0.05, 0.0), Math.max(0.0, var2 - 1.55) * 1.9, 0.045, var5, pj.T(var2, 1.55, 2.5) * var6);
   }

   static void C(kj var0, pj var1, double var2, Vec3 var4, int var5, int var6, int var7, double var8, double var10) {
      if (!(var2 < 0.0) && !(var2 > 1.2)) {
         for (int var12 = 0; var12 < var7; var12++) {
            double var13 = var1.F(var12 + 400) * Math.PI * 2.0;
            Vec3 var15 = new Vec3(Math.cos(var13) * var8, (var1.F(var12 + 500) - 0.3) * var8, Math.sin(var13) * var8);
            Vec3 var16 = var4.add(var15.scale(var2)).add(0.0, -var2 * var2 * 0.9, 0.0);
            Vec3 var17 = var16.subtract(var15.scale(0.06));
            double var18 = pj.T(var2, 0.0, 1.2) * var10;
            r(var0, var17, var16, 0.022, var5, var6, var18);
            var0.P(var16.x, var16.y, var16.z, 0.027, var13, uh.F(var6, var18));
         }
      }
   }

   static void q(kj var0, Vec3 var1, double var2, double var4, int var6, double var7) {
      if (var7 > 0.001 && var2 > 0.001) {
         var0.I(var1, var2, var4, uh.F(var6, var7));
      }
   }

   static void r(kj var0, Vec3 var1, Vec3 var2, double var3, int var5, int var6, double var7) {
      if (!(var7 <= 0.001) && !(var3 <= 0.001)) {
         var0.ribbon(var1, var2, var3, uh.F(var5, var7 * 0.5), uh.F(var6, var7 * 0.5));
         var0.ribbon(var1, var2, var3 * 0.3, uh.F(uh.q(var5, -1, 0.75), var7), uh.F(-1, var7));
      }
   }

   private sh() {
   }
}
