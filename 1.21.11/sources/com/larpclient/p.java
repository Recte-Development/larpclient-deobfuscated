package com.larpclient;

import net.minecraft.class_10799;
import net.minecraft.class_2960;
import net.minecraft.class_310;
import net.minecraft.class_332;
import org.joml.Vector2f;

public final class p {
   private static final class_2960 z = class_2960.method_60655("larp-client", "textures/gui/glass/fill.png");
   private static final class_2960 Y = class_2960.method_60655("larp-client", "textures/gui/glass/rim.png");
   private static final int[] N = new int[]{0, 48, 80};
   private static final int[] U = new int[]{48, 32, 48};
   static final double i = 6.0;

   private p() {
   }

   private static void a(class_332 var0, class_2960 var1, int var2, int var3, int var4, int var5, int var6, int var7) {
      var7 = fp.R(var7);
      if (var4 > 0 && var5 > 0 && var7 >>> 24 != 0) {
         int var8 = Math.min(var6, Math.min(var4, var5) / 2);

         for (int var9 = 0; var9 < 3; var9++) {
            int var10 = var9 == 0 ? var3 : (var9 == 1 ? var3 + var8 : var3 + var5 - var8);
            int var11 = var9 == 1 ? var5 - 2 * var8 : var8;
            if (var11 > 0) {
               for (int var12 = 0; var12 < 3; var12++) {
                  int var13 = var12 == 0 ? var2 : (var12 == 1 ? var2 + var8 : var2 + var4 - var8);
                  int var14 = var12 == 1 ? var4 - 2 * var8 : var8;
                  if (var14 > 0) {
                     var0.method_25293(
                        class_10799.field_56883,
                        var1,
                        var13,
                        var10,
                        N[var12],
                        N[var9],
                        var14,
                        var11,
                        U[var12],
                        U[var9],
                        128,
                        128,
                        var1.equals(Y) ? ei.R(var7, var9 == 0 ? 1.0 : (var9 == 1 ? 0.55 : 0.28)) : var7
                     );
                  }
               }
            }
         }
      }
   }

   public static void B(class_332 var0, int var1, int var2, int var3, int var4, int var5, int var6) {
      ws.r(var0, var1, var2, var3, var4, var5, var6);
   }

   static void S(class_332 var0, int var1, int var2, int var3, int var4, int var5, int var6) {
      a(var0, z, var1, var2, var3, var4, var5, var6);
   }

   static void t(class_332 var0, int var1, int var2, int var3, int var4, int var5, int var6) {
      a(var0, Y, var1, var2, var3, var4, var5, var6);
   }

   public static void i(class_332 var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7, double var8) {
      B(var0, var1, var2, var3, var4, var5, ei.W(var6, var7, 0.6));
      ws.A(var0, var1, var2, var3, var4, var5, ei.R(ez.o, var8), ei.R(ez.J, var8));
   }

   public static void W(class_332 var0, int var1, int var2, int var3, int var4, int var5, int var6, double var7) {
      i(var0, var1, var2, var3, var4, var5, var6, var6, var7);
   }

   public static void W(class_332 var0, int var1, int var2, int var3, int var4) {
      T(var0, var1, var2, var3, var4);
      Y(var0, var1, var2, var3, var4);
   }

   public static void T(class_332 var0, int var1, int var2, int var3, int var4) {
      int var5 = class_310.method_1551().field_1687 != null ? 1 : 0;
      int var6 = var5 != 0 && !lb.T() && t_.P() ? 1 : 0;
      if (var6 != 0) {
         Vector2f var7 = var0.method_51448().transformPosition(var1, var2, new Vector2f());
         float var8 = var3 * var0.method_51448().m00();
         float var9 = var3 * var0.method_51448().m01();
         float var10 = var4 * var0.method_51448().m10();
         float var11 = var4 * var0.method_51448().m11();
         t_.A(
            Math.round(var7.x + Math.min(0.0F, var8) + Math.min(0.0F, var10)),
            Math.round(var7.y + Math.min(0.0F, var9) + Math.min(0.0F, var11)),
            Math.round(Math.abs(var8) + Math.abs(var10)),
            Math.round(Math.abs(var9) + Math.abs(var11))
         );
      }

      o(var0, var1, var2, var3, var4);
      B(var0, var1, var2, var3, var4, ez.j, var5 != 0 && var6 == 0 ? ez.X() : ez.Z);
      if (ez.d()) {
         ws.F(var0, var1, var2, var3, var4 * 0.5F, ez.j, ei.D(ez.a, 0.07 * ez.v()), ei.D(ez.a, 0.0));
      }
   }

   public static void z(class_332 var0, int var1, int var2, int var3, int var4) {
      LarpClient var5 = LarpClient.b();
      int var6 = var5 != null && !var5.e().F().k ? 0 : 1;
      double var7 = var6 != 0 ? 0.5 + 0.5 * Math.sin(System.nanoTime() / 1.0E9 * 1.6) : 0.5;
      a(var0, var1, var2, var3, var4, ez.j, 14.0F, ei.D(ez.a, (0.12 + 0.05 * var7) * ez.v()));
   }

   public static void Y(class_332 var0, int var1, int var2, int var3, int var4) {
      if (ez.d()) {
         ws.A(var0, var1, var2, var3, var4, ez.j, ei.W(ez.o, ei.D(ez.a, 0.5), 0.65 * ez.v()), ez.J);
      } else {
         ws.A(var0, var1, var2, var3, var4, ez.j, ez.o, ez.J);
      }
   }

   public static void N(class_332 var0, int var1, int var2, int var3, int var4, int var5, int var6, double var7) {
      if (ez.d() && var7 > 0.0) {
         ws.K(var0, var1, var2, var3, var4, var5, 10.0F + 6.0F * (float)var7, ei.R(ez.PX, 0.35 * var7), ei.R(ez.PX, 0.7 * var7));
      }

      B(var0, var1, var2, var3, var4, var5, var6);
      ws.A(var0, var1, var2, var3, var4, var5, ez.o, ez.o);
   }

   public static void a(class_332 var0, float var1, float var2, float var3, float var4, float var5, float var6, int var7) {
      if (ez.d() && t_.O != t_.t.A) {
         ws.K(var0, var1, var2, var3, var4, var5, var6, var7, var7);
      }
   }

   public static void T(class_332 var0, int var1, int var2, int var3, int var4, int var5, int var6) {
      if (ez.d()) {
         LarpClient var7 = LarpClient.b();
         double var8 = var7 != null && !var7.e().F().k ? 0.0 : System.nanoTime() / 1.0E9 / 6.0;
         ws.X(var0, var1, var2, var3, var4, var5, t_.O == t_.t.A ? 0.0F : 6.0F, var8, var6);
      }
   }

   public static void x(class_332 var0, int var1, int var2, int var3, int var4, double var5) {
      j(var0, var1, var2, var3, var4, ez.d() ? Math.min(8, var4 / 2) : ez.m, var5);
   }

   public static void j(class_332 var0, int var1, int var2, int var3, int var4, int var5, double var6) {
      if (!ez.d()) {
         W(var0, var1, var2, var3, var4, var5, ez.B, 0.5 + 0.6 * var6);
         if (var6 > 0.001) {
            ws.A(var0, var1, var2, var3, var4, var5, ei.D(ez.H, 0.45 * var6), ei.D(ez.H, 0.25 * var6));
         }
      } else {
         int var8 = ez.X(var1, ez.B);
         if (var6 > 0.001) {
            ws.K(var0, var1, var2, var3, var4, var5, 7.0F, ei.D(var8, 0.24 * var6), ei.D(var8, 0.24 * var6));
         }

         B(var0, var1, var2, var3, var4, var5, ei.W(ez.B, ez.K, 0.5 * var6));
         ws.A(var0, var1, var2, var3, var4, var5, ei.W(ez.o, ei.D(var8, 0.9), var6), ei.W(ez.J, ei.D(var8, 0.9), var6));
      }
   }

   public static void F(class_332 var0, int var1, int var2, int var3, int var4) {
      if (ez.d()) {
         N(var0, var1, var2, var3, var4, ez.U, ei.W(ez.M, ez.g | 0xFF000000, 0.35), 1.0);
      } else {
         B(var0, var1, var2, var3, var4, ez.V, ez.I);
         gu.q(var0, var1, var2 + 4, Math.max(0, var4 - 8), true, ez.o);
      }
   }

   public static void C(class_332 var0, int var1, int var2, int var3, int var4, int var5) {
      if (ez.d()) {
         ws.X(var0, var1 - 1, var2 - 1, var3 + 2, var4 + 2, var5 + 1, ei.D(ez.X(var1, ez.M), 0.85));
      } else {
         ws.A(var0, var1 - 1, var2 - 1, var3 + 2, var4 + 2, var5 + 1, ei.R(ez.H, 0.55), ei.R(ez.H, 0.3));
      }
   }

   public static void F(class_332 var0, int var1, int var2, int var3, int var4, double var5, boolean var7) {
      W(var0, var1, var2, var3, var4, ez.m, ei.W(ez.M, ez.K, var7 ? 1.0 : var5), 0.6 + var5 * 0.4);
   }

   public static void E(class_332 var0, int var1, int var2, boolean var3) {
      s(var0, var1, var2, var3 ? 1.0 : 0.0);
   }

   public static void s(class_332 var0, int var1, int var2, double var3) {
      c(var0, var1, var2, var3, ez.P(var1), jx.controlInk(ez.d) == -15132391);
   }

   public static void c(class_332 var0, int var1, int var2, double var3, int var5, boolean var6) {
      double var7 = Math.clamp(var3, 0.0, 1.0);
      int var9 = jx.readableAccent(var5, ez.M | 0xFF000000);
      int var10 = jx.controlInk(var9);
      if (ez.d()) {
         var10 = jx.contrast(-1, var9) >= 2.2 ? -1 : -15132388;
         int var17 = var6 ? -2894888 : ei.W(ez.C | 0xFF000000, ez.H, 0.12);
         int var18 = var6 ? -1 : -3552816;
         switch (ez.x().switches()) {
            case "Square":
               v(var0, var1, var2, var7, var9, var17, ei.W(var18, var10, var7), var6);
               return;
            case "Minimal":
               v(var0, var1, var2, var7, var9, var17, var6);
               return;
            default:
               if (var7 > 0.01) {
                  a(var0, var1, var2, 24.0F, 13.0F, 6.5F, 5.0F, ei.D(var9, 0.35 * var7));
               }

               ws.r(var0, var1, var2, 24.0F, 13.0F, 6.5F, ei.W(var17, var9, var7));
               if (var7 > 0.01) {
                  ws.u(var0, var1, var2, 24.0F, 13.0F, 6.5F, ei.D(ez.f, 0.0), ei.D(ez.f, 0.55 * var7));
               }

               if (var7 < 0.999) {
                  ws.A(var0, var1, var2, 24.0F, 13.0F, 6.5F, ei.R(ez.o, 1.0 - var7), ei.R(ez.J, 1.0 - var7));
               }

               float var19 = (float)(3.15 * (1.0 - Math.abs(var7 * 2.0 - 1.0)));
               float var20 = 9.0F + var19;
               float var15 = var1 + 2 + (float)(11.0 * var7) - var19 * (float)var7;
               ws.K(var0, var15, var2 + 2, var20, 9.0F, 4.5F, 3.0F, 1207959552, 1509949440);
               ws.r(var0, var15, var2 + 2, var20, 9.0F, 4.5F, ei.W(var18, var10, var7));
               if (var6) {
                  ws.A(var0, var15, var2 + 2, var20, 9.0F, 4.5F, 469762048, 704643072);
               }
         }
      } else {
         int var11 = ei.W(var6 ? -9143678 : -12961217, var9, var7);
         W(var0, var1 + 1, var2 + 1, 22, 12, 6, var11, 0.35);
         int var12 = var1 + 3 + (int)Math.round(var7 * 10.0);
         B(var0, var12, var2 + 3, 8, 8, 4, 939524096);
         B(var0, var12, var2 + 3, 8, 8, 4, ei.W(-1776408, var10, var7));
      }
   }

   private static void v(class_332 var0, int var1, int var2, double var3, int var5, int var6, int var7, boolean var8) {
      if (var3 > 0.01) {
         a(var0, var1, var2, 24.0F, 13.0F, 2.5F, 5.0F, ei.D(var5, 0.3 * var3));
      }

      ws.r(var0, var1, var2, 24.0F, 13.0F, 2.5F, ei.W(var6, var5, var3));
      if (var3 < 0.999) {
         ws.A(var0, var1, var2, 24.0F, 13.0F, 2.5F, ei.R(ez.o, 1.0 - var3), ei.R(ez.J, 1.0 - var3));
      }

      float var9 = var1 + 2 + (float)(11.0 * var3);
      ws.K(var0, var9, var2 + 2, 9.0F, 9.0F, 1.5F, 2.5F, 1073741824, 1342177280);
      ws.r(var0, var9, var2 + 2, 9.0F, 9.0F, 1.5F, var7);
      if (var8) {
         ws.A(var0, var9, var2 + 2, 9.0F, 9.0F, 1.5F, 469762048, 704643072);
      }
   }

   private static void v(class_332 var0, int var1, int var2, double var3, int var5, int var6, boolean var7) {
      ws.r(var0, var1 + 1, var2 + 4, 22.0F, 5.0F, 2.5F, ei.W(var6, ei.D(var5, 0.55), var3));
      float var8 = 5.5F;
      float var9 = var1 + 1 + var8 + (float)((22.0F - 2.0F * var8) * var3);
      ws.K(var0, var9 - var8, var2 + 1, 2.0F * var8, 2.0F * var8, var8, 2.5F, 1073741824, 1342177280);
      int var10 = ei.W(var7 ? -1 : -1776408, var5 | 0xFF000000, var3);
      ws.r(var0, var9 - var8, var2 + 1, 2.0F * var8, 2.0F * var8, var8, var10);
      float var11 = var8 * 0.42F;
      ws.r(var0, var9 - var11, var2 + 1 + var8 - var11, 2.0F * var11, 2.0F * var11, var11, ei.W(ei.D(ez.d, 0.6), -1, var3));
   }

   public static void t(class_332 var0, int var1, int var2, int var3, int var4, int var5) {
      if (var4 > var3) {
         int var6 = Math.min(var3, Math.max(18, var3 * var3 / var4));
         int var7 = (int)((double)(var3 - var6) * var5 / (var4 - var3));
         B(var0, var1, var2 + var7, 2, var6, 1, ez.d() ? ei.D(ez.PH, 0.4) : -2139654281);
      }
   }

   public static void e(class_332 var0, int var1, int var2, int var3, int var4, double var5) {
      W(var0, var1, var2, var3, var4, ez.X, ei.R(ez.Z, var5), var5 * 0.7);
   }

   public static void l(class_332 var0, int var1, int var2, int var3, int var4, int var5) {
      a(var0, var1, var2, var3, var4, ez.V, var5);
   }

   public static void a(class_332 var0, int var1, int var2, int var3, int var4, int var5, int var6) {
      ws.A(var0, var1, var2, var3, var4, var5, var6, ei.R(var6, 0.28));
   }

   public static void o(class_332 var0, int var1, int var2, int var3, int var4) {
      f(var0, var1, var2, var3, var4, ez.j);
   }

   public static void f(class_332 var0, int var1, int var2, int var3, int var4, int var5) {
      if (ez.d()) {
         ws.K(var0, var1, var2, var3, var4, var5, 30.0F, ei.R(ez.PX, 0.25), ez.PX);
         ws.K(var0, var1, var2, var3, var4, var5, 6.0F, ei.R(ez.PX, 0.45), ei.R(ez.PX, 0.7));
      } else {
         for (byte var6 = 8; var6 >= 2; var6 -= 2) {
            B(var0, var1 - var6, var2 - var6 + 5, var3 + var6 * 2, var4 + var6 * 2, ez.j + var6, 150994944);
         }
      }
   }
}
