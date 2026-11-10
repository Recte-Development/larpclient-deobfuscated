package com.larpclient;

import com.larpclient.module.HUDModule;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import net.minecraft.class_2960;
import net.minecraft.class_310;
import net.minecraft.class_332;
import net.minecraft.class_7532;

public final class ur {
   public static final int F = 17;
   public static final int z = 172;
   public static final int u = 72;
   public static final int b = 42;
   public static final int n = 6;
   public static final int E = 30;
   private static final int t = 9;
   private static final int D = 12;
   private static final int Q = 14;
   private static final int R = 16;
   private static final int d = 11;
   public static final int w = -603321844;
   public static final int T = 22;
   private static final String P = "XYZ";
   private static final double a = 22.0;
   private static final double G = 4.0;
   private static final double s = 6.0;
   public static final double q = 0.09;
   public static final double A = 0.12;
   public static final int X = 30;
   public static final int e = 12;
   public static final int o = 160;
   public static final int V = 38;
   public static final int i = 63;
   public static final double U = 6.0;
   private static double B = 6.0;
   private static long J;

   private ur() {
   }

   public static int G(int var0) {
      return var0 + 12;
   }

   public static int i() {
      return -267645934;
   }

   public static int z() {
      return ez.a;
   }

   public static void p(class_332 var0, String var1, int var2, int var3, int var4, boolean var5) {
      int var6 = G(var2);
      int var7 = var3 - var6;
      gu.Z(var0, var7, var4, var6, 17, i());
      t(var0, var7, var4, var6, 17, var5);
      x(var0, var7, var4, var6, 17, 0);
      gu.R(var0, var5 ? var3 - 2 : var7, var4, 2, 17, ez.a, ei.W(ez.a, -1, 0.45));
      w5.Y(var0, var1, var7 + (var5 ? 5 : 7), q2.e(var4, 17.0, 13.0), ez.a);
   }

   private static List<ur.z> z(String var0, String var1, String var2, String var3) {
      HUDModule var4 = HUDModule.y();
      ArrayList var5 = new ArrayList();
      if (var4 == null || var4.r.getValue()) {
         var5.add(new ur.z(var0, ur.n.g));
      }

      if (var4 == null || var4.U.getValue()) {
         var5.add(new ur.z(var1, ur.n.G));
      }

      if (var4 == null || var4.D.getValue()) {
         var5.add(new ur.z(var2, ur.n.o));
      }

      if (!var3.isEmpty() && (var4 == null || var4.f.getValue())) {
         var5.add(new ur.z(var3, ur.n.r));
      }

      return var5;
   }

   public static int J(int... var0) {
      return var0.length == 0 ? 42 : 72 + Arrays.stream(var0).sum() + (var0.length - 1) * 14;
   }

   public static int f(String var0, String var1, String var2, String var3) {
      return J(z(var0, var1, var2, var3).stream().mapToInt(ur.z::width).toArray());
   }

   public static int r(double var0) {
      return 42 + (int)Math.round(22.0 * Math.clamp(var0, 0.0, 1.0));
   }

   public static int e(String var0) {
      return 24 + w5.A("XYZ " + var0, 13.0);
   }

   public static int U(int var0, String var1, double var2) {
      int var4 = 48 + e(var1);
      return var0 + (int)Math.round(Math.max(0, var4 - var0) * Math.clamp(var2, 0.0, 1.0));
   }

   public static cv.z Y(int var0, int var1, int var2, boolean var3, String var4) {
      return T(var0, var1, var2, var3, e(var4));
   }

   public static cv.z T(int var0, int var1, int var2, boolean var3, int var4) {
      int var5 = var3 ? var0 : var0 + 42 + 6;
      int var6 = var2 - 42 - 6;
      int var7 = Math.min(var6, var4);
      return new cv.z(var3 ? var5 + var6 - var7 : var5, var1 + 6 + 30, var7, 22);
   }

   public static boolean[] m(int var0, int var1, boolean var2) {
      int var3 = var0 > var1 ? 1 : 0;
      int var4 = var1 > var0 ? 1 : 0;
      int var5 = var0 <= 0 ? 1 : 0;
      return new boolean[]{
         false, false, var2 ? var3 == 0 : true, var2 ? true : var3 == 0, var5 == 0 && (!var2 || var4 == 0), var5 == 0 && (var2 || var4 == 0), false, false
      };
   }

   private static void N(
      class_332 var0, int var1, float var2, int var3, float var4, int var5, int var6, int var7, boolean var8, boolean var9, boolean var10, boolean var11
   ) {
      if (var3 > 0 && !(var4 <= 0.0F)) {
         float var12 = var0.method_51448().m00();
         float var13 = var0.method_51448().m20();
         int var14 = (int)Math.floor(var13 + var12 * var1);
         int var15 = (int)Math.ceil(var13 + var12 * (var1 + var3));
         int var16 = Math.round(var13 + var12 * (var1 + var3 / 2.0F));
         W(var0, var14, var16, var5, var6, var1, var2, var3, var4, var7, var8, var10);
         W(var0, var16, var15, var5, var6, var1, var2, var3, var4, var7, var9, var11);
      }
   }

   public static float J(float var0, float var1, float var2) {
      return var1 <= 0.0F ? var0 : (Math.round(var2 + var1 * var0) - var2) / var1;
   }

   private static void W(
      class_332 var0, int var1, int var2, int var3, int var4, int var5, float var6, int var7, float var8, int var9, boolean var10, boolean var11
   ) {
      if (var2 > var1 && var4 > var3) {
         int var12 = var10 ? var9 : 0;
         int var13 = var11 ? var9 : 0;
         var0.method_51448().pushMatrix();

         try {
            var0.method_51448().identity();
            var0.method_44379(var1, var3, var2, var4);
         } finally {
            var0.method_51448().popMatrix();
         }

         try {
            gu.N(var0, var5, var6 - var12, var7, var8 + var12 + var13, var9, i());
            HUDModule var14 = HUDModule.y();
            if (var14 == null || var14.h()) {
               int var15 = ei.W(ez.a, ez.f, 0.5);
               ws.u(var0, var5, var6 - var12, var7, var8 + var12 + var13, var9, ei.D(ez.a, 0.09 * ez.v()), ei.D(var15, 0.0));
            }
         } finally {
            var0.method_44380();
         }
      }
   }

   public static void B(class_332 var0, int var1, int var2, int var3, String var4, String var5, String var6, String var7) {
      B(var0, var1, var2, var3, var4, var5, var6, var7, "", 0.0);
   }

   public static void B(class_332 var0, int var1, int var2, int var3, String var4, String var5, String var6, String var7, String var8, double var9) {
      HUDModule var11 = HUDModule.y();
      int var12 = var11 != null && var11.I.G("Right") ? 1 : 0;
      var9 = Math.clamp(var9, 0.0, 1.0);
      double var13 = System.nanoTime() / 1.0E9;
      int var15 = r(var9);
      int var16 = var12 != 0 ? var1 + var3 - 42 : var1;
      gu.N(var0, var16, var2, 42.0F, var15, 9.0F, i());
      D(var0, var16, var2, 42.0F, var15, 9.0F);
      x(var0, var16, var2, 42, var15, 9);
      int var17 = var2 + (var15 - 32) / 2;
      if (!q2.w(var0, "logo", var16 + 5, var17, 32.0, -1)) {
         gu.W(var0, class_2960.method_60655("larp-client", "textures/gui/logo.png"), var16 + 5, var17, 32, 32, 64, 64);
      }

      List var18 = z(var4, var5, var6, var7);
      int var19 = var9 > 0.01 && !var8.isEmpty() ? 1 : 0;
      if (!var18.isEmpty() || var19 != 0) {
         int var20 = var12 != 0 ? var1 : var1 + 42 + 6;
         int var21 = var3 - 42 - 6;
         int var22 = var2 + 6;
         int var23 = var15 - 12;
         int var24 = var20 + var21;
         int var25 = var20;
         if (var19 == 0) {
            gu.N(var0, var20, var22, var21, var23, 6.0F, i());
            D(var0, var20, var22, var21, var23, 6.0F);
            x(var0, var20, var22, var21, var23, 6);
         } else {
            int var26 = var18.isEmpty() ? 0 : Math.min(var21, f(var4, var5, var6, var7) - 42 - 6);
            int var27 = Math.min(var21, e(var8));
            int var28 = var22 + 30;
            int var29 = var23 - 30;
            var25 = var12 != 0 ? var24 - var26 : var20;
            int var30 = var12 != 0 ? var24 - var27 : var20;
            boolean[] var31 = m(var26, var27, (boolean)var12);
            float var32 = var0.method_51448().m11();
            float var33 = var0.method_51448().m21();
            float var34 = J(var28, var32, var33);
            int var35 = Math.round(var33 + var32 * var34);
            int var36 = var22 + var23;
            N(var0, var25, var22, var26, var34 - var22, (int)Math.floor(var33 + var32 * var22), var35, 6, var31[0], var31[1], var31[2], var31[3]);
            N(var0, var30, var34, var27, var36 - var34, var35, (int)Math.ceil(var33 + var32 * var36), 6, var31[4], var31[5], var31[6], var31[7]);
            if (var26 > 0) {
               x(var0, var25, var22, var26, 30, 6);
            }

            x(var0, var30, var28, var27, var29, 6);
            int var37 = Math.min(var26, var27);
            if (var37 > 24) {
               ws.r(var0, (var12 != 0 ? var24 - var37 : var20) + 12, var34, var37 - 24, 1.0F, 0.5F, ei.R(675036230, var9));
            }

            var0.method_44379(var30, var28, var30 + var27, var28 + var29);

            try {
               double var38 = q2.e(var28, 22.0, 13.0);
               w5.y(var0, "XYZ", var30 + 12, var38, 13.0, ei.R(ez.a, var9));
               w5.y(var0, " " + var8, var30 + 12 + w5.A("XYZ", 13.0), var38, 13.0, ei.R(-1, var9));
            } finally {
               var0.method_44380();
            }
         }

         int var44 = var25 + 12;
         double var45 = q2.g(var22, 30.0, 13.0);

         for (ur.z var47 : var18) {
            int var48 = var44;
            switch (var47.kind()) {
               case g:
                  if (class_310.method_1551().field_1724 != null) {
                     class_7532.method_52722(var0, class_310.method_1551().field_1724.method_52814(), var44, var22 + 7, 16);
                  }

                  var44 += 22;
                  break;
               case o:
                  L(var0, var44, var22 + 8, var13);
                  var44 += 17;
            }

            if (var47.kind() == ur.n.r) {
               X(var0, var47.label(), var44, var45, var13);
            } else {
               w5.y(var0, var47.label(), var44, var45, 13.0, -1);
            }

            var44 = var48 + var47.width() + 14;
         }
      }
   }

   private static void L(class_332 var0, int var1, int var2, double var3) {
      double[] var5 = new double[]{5.2, 7.1, 4.3};
      double[] var6 = new double[]{0.0, 1.7, 3.1};

      for (int var7 = 0; var7 < 3; var7++) {
         int var8 = 4 + (int)Math.round(10.0 * (0.5 + 0.5 * Math.sin(var3 * var5[var7] + var6[var7])));
         ws.r(var0, var1 + var7 * 4, var2 + 14 - var8, 3.0F, var8, 1.0F, ez.a);
      }
   }

   private static void X(class_332 var0, String var1, int var2, double var3, double var5) {
      int var7 = var1.indexOf(58);
      if (var7 < 0) {
         w5.y(var0, var1, var2, var3, 13.0, -1);
      } else {
         String var8 = var1.substring(0, var7 + 1);
         double var9 = 0.55 + 0.45 * Math.cos(var5 * Math.PI * 2.0);
         w5.y(var0, var1.substring(0, var7), var2, var3, 13.0, -1);
         w5.y(var0, ":", var2 + w5.A(var1.substring(0, var7), 13.0), var3, 13.0, ei.R(-1, var9));
         w5.y(var0, var1.substring(var7 + 1), var2 + w5.A(var8, 13.0), var3, 13.0, -1);
      }
   }

   public static void x(class_332 var0, int var1, int var2, int var3, int var4, int var5) {
      M(var0, var1, var2, var3, var4, var5, 1.0);
   }

   public static void M(class_332 var0, int var1, int var2, int var3, int var4, int var5, double var6) {
      HUDModule var8 = HUDModule.y();
      if ((var8 == null || var8.G()) && !(var6 <= 0.01)) {
         double var9 = System.nanoTime() / 1.0E9 % 6.0 / 4.0;
         float var11 = var0.method_51448().m00();
         if (!(var9 >= 1.0) && !(var11 <= 0.0F)) {
            double var12 = -22.0 + var9 * (var0.method_51421() + 44.0);
            double var14 = (var12 - var0.method_51448().m20()) / var11;
            double var16 = 11.0 / var11;
            int var18 = var1 + var5;
            int var19 = var1 + var3 - var5;
            if (!(var14 + var16 <= var18) && !(var14 - var16 >= var19)) {
               int var20 = (int)Math.round(var14 - var16);
               int var21 = Math.max(1, (int)Math.round(var16));
               int var22 = ei.R(251658239, var6);
               var0.method_44379(var18, var2, var19, var2 + var4);

               try {
                  ws.u(var0, var20, var2, var21, var4, 0.0F, var22 & 16777215, var22);
                  ws.u(var0, var20 + var21, var2, var21, var4, 0.0F, var22, var22 & 16777215);
               } finally {
                  var0.method_44380();
               }
            }
         }
      }
   }

   public static void D(class_332 var0, float var1, float var2, float var3, float var4, float var5) {
      HUDModule var6 = HUDModule.y();
      if ((var6 == null || var6.h()) && !(var3 <= 0.0F) && !(var4 <= 0.0F)) {
         double var7 = ez.v();
         int var9 = ez.a;
         int var10 = ei.W(ez.a, ez.f, 0.5);
         ws.u(var0, var1, var2, var3, var4, var5, ei.D(var9, 0.09 * var7), ei.D(var10, 0.0));
         int var11 = ei.D(var9, 0.12 * var7);
         ws.A(var0, var1, var2, var3, var4, var5, var11, var11);
      }
   }

   public static void t(class_332 var0, int var1, int var2, int var3, int var4, boolean var5) {
      HUDModule var6 = HUDModule.y();
      if ((var6 == null || var6.h()) && var3 > 0 && var4 > 0) {
         int var7 = ei.D(ez.a, 0.09 * ez.v());
         int var8 = var7 & 16777215;
         ws.u(var0, var1, var2, var3, var4, 0.0F, var5 ? var8 : var7, var5 ? var7 : var8);
      }
   }

   public static int m(String var0) {
      return 12 + w5.A("XYZ " + var0, 15.0);
   }

   public static void y(class_332 var0, int var1, int var2, int var3, String var4) {
      gu.N(var0, var1, var2, var3, 27.0F, 6.0F, i());
      D(var0, var1, var2, var3, 27.0F, 6.0F);
      x(var0, var1, var2, var3, 27, 6);
      double var5 = q2.e(var2, 27.0, 15.0);
      w5.y(var0, "XYZ", var1 + 6, var5, 15.0, ez.a);
      w5.y(var0, " " + var4, var1 + 6 + w5.A("XYZ", 15.0), var5, 15.0, -1);
   }

   public static String U(double var0) {
      return String.format(Locale.ROOT, "%.2f bps", var0);
   }

   public static void y(class_332 var0, int var1, int var2, cq var3, boolean var4) {
      HUDModule var5 = HUDModule.y();
      int var6 = var5 != null && !var5.M.getValue() ? 0 : 1;
      int var7 = var6 != 0 ? -267645934 : var5.Z.getValue();
      int var8 = var6 != 0 ? -603321844 : var5.V.getValue();
      int var9 = var5 == null ? -1841171 : var5.X.getValue();
      var0.method_44379(var1, var2 + 30, var1 + 172, var2 + 72);
      gu.N(var0, var1, var2, 172.0F, 72.0F, 12.0F, var8);
      var0.method_44380();
      var0.method_44379(var1, var2, var1 + 172, var2 + 30);
      gu.N(var0, var1, var2, 172.0F, 72.0F, 12.0F, var7);
      var0.method_44380();
      D(var0, var1, var2, 172.0F, 72.0F, 12.0F);
      x(var0, var1, var2, 172, 72, 12);
      if (var6 != 0) {
         ws.r(var0, var1 + 2, var2 + 30, 168.0F, 1.0F, 0.5F, 675036230);
      }

      w5.y(var0, "Speed", var1 + 12, q2.e(var2, 30.0, 13.0), 13.0, E(var7));
      double var10 = var4 ? 4.3 : var3.g();
      String var12 = String.format(Locale.ROOT, "%.2f", var10);
      String var13 = " bps";
      double var14 = q2.e(var2, 30.0, 13.0);
      double var16 = var14 + 1.890909147262573;
      int var18 = w5.A(var13, 10.4);
      int var19 = var1 + 172 - 12;
      w5.y(var0, var12, var19 - var18 - w5.A(var12, 13.0), var14, 13.0, E(var7));
      w5.y(var0, var13, var19 - var18, var16, 10.4, ei.R(E(var7), 0.55));
      int var20 = var4 ? 40 : Math.min(40, var3.X());
      double var21 = 0.0;

      for (int var23 = 0; var23 < var20; var23++) {
         var21 = Math.max(var21, j(var3, var4, var23, var20));
      }

      double var45 = m(var21, var4);
      int var25 = var1 + 12;
      short var26 = 148;
      int var27 = var2 + 63;
      int var28 = Math.round(b(var2, var45 / 2.0, var45));

      for (byte var29 = 0; var29 < var26; var29 += 6) {
         gu.Z(var0, var25 + var29, var28, Math.min(3, var26 - var29), 1, ei.R(var9, 0.1));
      }

      gu.Z(var0, var25, var27, var26, 1, ei.R(var9, 0.16));
      if (var20 >= 2) {
         for (int var46 = 0; var46 <= var26; var46++) {
            float var30 = b(var2, r(var3, var4, var20, (double)var46 / var26), var45);
            int var31 = Math.round(var30);
            if (var27 - var31 > 0) {
               gu.R(var0, var25 + var46, var31, 1, var27 - var31, ei.R(var9, 0.26), ei.R(var9, 0.0));
            }
         }

         float var47 = var25;
         float var48 = b(var2, j(var3, var4, 0, var20), var45);

         for (int var49 = 0; var49 + 1 < var20; var49++) {
            double var32 = j(var3, var4, Math.max(0, var49 - 1), var20);
            double var34 = j(var3, var4, var49, var20);
            double var36 = j(var3, var4, var49 + 1, var20);
            double var38 = j(var3, var4, Math.min(var20 - 1, var49 + 2), var20);

            for (int var40 = 1; var40 <= 4; var40++) {
               double var41 = var40 / 4.0;
               float var43 = var25 + (float)((var49 + var41) * var26 / (var20 - 1));
               float var44 = b(var2, i5.x(var32, var34, var36, var38, var41), var45);
               var0.method_51448().pushMatrix();
               var0.method_51448().translate(var47, var48).rotate((float)Math.atan2(var44 - var48, var43 - var47));
               ws.r(var0, 0.0F, -0.25F, (float)Math.hypot(var43 - var47, var44 - var48) + 0.5F, 1.5F, 0.75F, var9);
               var0.method_51448().popMatrix();
               var47 = var43;
               var48 = var44;
            }
         }

         ws.r(var0, var47 - 3.5F, var48 - 3.5F, 7.0F, 7.0F, 3.5F, ei.R(var9, 0.25));
         ws.r(var0, var47 - 2.0F, var48 - 2.0F, 4.0F, 4.0F, 2.0F, var9);
      }
   }

   private static double r(cq var0, boolean var1, int var2, double var3) {
      double var5 = Math.clamp(var3, 0.0, 1.0) * (var2 - 1);
      int var7 = Math.min(var2 - 2, (int)Math.floor(var5));
      return i5.x(
         j(var0, var1, Math.max(0, var7 - 1), var2),
         j(var0, var1, var7, var2),
         j(var0, var1, var7 + 1, var2),
         j(var0, var1, Math.min(var2 - 1, var7 + 2), var2),
         var5 - var7
      );
   }

   private static double m(double var0, boolean var2) {
      double var3 = Math.max(6.0, var0 * 1.15);
      if (var2) {
         return var3;
      }

      long var5 = System.nanoTime();
      double var7 = J == 0L ? 1.0 : Math.clamp((var5 - J) / 1.0E9, 0.0, 0.25);
      J = var5;
      B = B + (var3 - B) * (1.0 - Math.exp(-6.0 * var7));
      return Math.max(B, var0);
   }

   private static double j(cq var0, boolean var1, int var2, int var3) {
      return var1 ? (Math.sin(var2 * 0.18) + 1.0) * 2.2 : (var3 == 0 ? 0.0 : var0.G(var0.X() - var3 + var2));
   }

   public static float i(int var0, double var1) {
      return b(var0, var1, 6.0);
   }

   public static float b(int var0, double var1, double var3) {
      return var0 + 63 - (float)(Math.clamp(var1 / Math.max(0.01, var3), 0.0, 1.0) * 25.0);
   }

   public static int E(int var0) {
      double var1 = (var0 >> 16 & 0xFF) * 0.2126 + (var0 >> 8 & 0xFF) * 0.7152 + (var0 & 0xFF) * 0.0722;
      return var1 > 150.0 ? -15197408 : -986637;
   }

   private enum n {
      g,
      G,
      o,
      r;
   }

   private record z(String label, ur.n kind) {
      int width() {
         int var1 = w5.A(this.label, 13.0);

         return switch (this.kind) {
            case g -> 22 + var1;
            case o -> 17 + var1;
            default -> var1;
         };
      }
   }
}
