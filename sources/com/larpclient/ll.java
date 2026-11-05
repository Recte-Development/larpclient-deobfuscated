package com.larpclient;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.Identifier;
import org.joml.Vector2f;

public final class ll {
   private static final Identifier a = Identifier.fromNamespaceAndPath("larp-client", "textures/gui/glass/fill.png");
   private static final Identifier q = Identifier.fromNamespaceAndPath("larp-client", "textures/gui/glass/rim.png");
   private static final int[] K = new int[]{0, 48, 80};
   private static final int[] H = new int[]{48, 32, 48};
   static final double Q = 6.0;

   private ll() {
   }

   private static void S(GuiGraphicsExtractor var0, Identifier var1, int var2, int var3, int var4, int var5, int var6, int var7) {
      var7 = r.D(var7);
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
                     var0.blit(
                        RenderPipelines.GUI_TEXTURED,
                        var1,
                        var13,
                        var10,
                        K[var12],
                        K[var9],
                        var14,
                        var11,
                        H[var12],
                        H[var9],
                        128,
                        128,
                        var1.equals(q) ? oh.S(var7, var9 == 0 ? 1.0 : (var9 == 1 ? 0.55 : 0.28)) : var7
                     );
                  }
               }
            }
         }
      }
   }

   public static void N(GuiGraphicsExtractor var0, int var1, int var2, int var3, int var4, int var5, int var6) {
      ow.P(var0, var1, var2, var3, var4, var5, var6);
   }

   static void E(GuiGraphicsExtractor var0, int var1, int var2, int var3, int var4, int var5, int var6) {
      S(var0, a, var1, var2, var3, var4, var5, var6);
   }

   static void V(GuiGraphicsExtractor var0, int var1, int var2, int var3, int var4, int var5, int var6) {
      S(var0, q, var1, var2, var3, var4, var5, var6);
   }

   public static void z(GuiGraphicsExtractor var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7, double var8) {
      N(var0, var1, var2, var3, var4, var5, oh.j(var6, var7, 0.6));
      ow.y(var0, var1, var2, var3, var4, var5, oh.S(rv.h, var8), oh.S(rv.C, var8));
   }

   public static void Y(GuiGraphicsExtractor var0, int var1, int var2, int var3, int var4, int var5, int var6, double var7) {
      z(var0, var1, var2, var3, var4, var5, var6, var6, var7);
   }

   public static void z(GuiGraphicsExtractor var0, int var1, int var2, int var3, int var4) {
      h(var0, var1, var2, var3, var4);
      v(var0, var1, var2, var3, var4);
   }

   public static void h(GuiGraphicsExtractor var0, int var1, int var2, int var3, int var4) {
      int var5 = Minecraft.getInstance().level != null ? 1 : 0;
      int var6 = var5 != 0 && !rk.X() && jk.Y() ? 1 : 0;
      if (var6 != 0) {
         Vector2f var7 = var0.pose().transformPosition(var1, var2, new Vector2f());
         float var8 = var3 * var0.pose().m00();
         float var9 = var3 * var0.pose().m01();
         float var10 = var4 * var0.pose().m10();
         float var11 = var4 * var0.pose().m11();
         jk.Z(
            Math.round(var7.x + Math.min(0.0F, var8) + Math.min(0.0F, var10)),
            Math.round(var7.y + Math.min(0.0F, var9) + Math.min(0.0F, var11)),
            Math.round(Math.abs(var8) + Math.abs(var10)),
            Math.round(Math.abs(var9) + Math.abs(var11))
         );
      }

      D(var0, var1, var2, var3, var4);
      N(var0, var1, var2, var3, var4, rv.T, var5 != 0 && var6 == 0 ? rv.B() : rv.s);
      if (rv.u()) {
         ow.s(var0, var1, var2, var3, var4 * 0.5F, rv.T, oh.i(rv.e, 0.07 * rv.S()), oh.i(rv.e, 0.0));
      }
   }

   public static void C(GuiGraphicsExtractor var0, int var1, int var2, int var3, int var4) {
      LarpClient var5 = LarpClient.y();
      int var6 = var5 != null && !var5.v().E().o ? 0 : 1;
      double var7 = var6 != 0 ? 0.5 + 0.5 * Math.sin(System.nanoTime() / 1.0E9 * 1.6) : 0.5;
      S(var0, var1, var2, var3, var4, rv.T, 14.0F, oh.i(rv.e, (0.12 + 0.05 * var7) * rv.S()));
   }

   public static void v(GuiGraphicsExtractor var0, int var1, int var2, int var3, int var4) {
      if (rv.u()) {
         ow.y(var0, var1, var2, var3, var4, rv.T, oh.j(rv.h, oh.i(rv.e, 0.5), 0.65 * rv.S()), rv.C);
      } else {
         ow.y(var0, var1, var2, var3, var4, rv.T, rv.h, rv.C);
      }
   }

   public static void d(GuiGraphicsExtractor var0, int var1, int var2, int var3, int var4) {
      N(var0, var1, var2, var3, var4, rv.i, rv.d);
      ow.y(var0, var1, var2, var3, var4, rv.i, oh.S(rv.h, 0.8), oh.S(rv.h, 0.8));
   }

   public static void w(GuiGraphicsExtractor var0, int var1, int var2, int var3, int var4, int var5, int var6, double var7) {
      if (rv.u() && var7 > 0.0) {
         ow.A(var0, var1, var2, var3, var4, var5, 10.0F + 6.0F * (float)var7, oh.S(rv.p, 0.35 * var7), oh.S(rv.p, 0.7 * var7));
      }

      N(var0, var1, var2, var3, var4, var5, var6);
      ow.y(var0, var1, var2, var3, var4, var5, rv.h, rv.h);
   }

   public static void S(GuiGraphicsExtractor var0, float var1, float var2, float var3, float var4, float var5, float var6, int var7) {
      if (rv.u() && jk.u != jk.x.i) {
         ow.A(var0, var1, var2, var3, var4, var5, var6, var7, var7);
      }
   }

   public static void f(GuiGraphicsExtractor var0, int var1, int var2, int var3, int var4, int var5, int var6) {
      if (rv.u()) {
         LarpClient var7 = LarpClient.y();
         double var8 = var7 != null && !var7.v().E().o ? 0.0 : System.nanoTime() / 1.0E9 / 6.0;
         ow.n(var0, var1, var2, var3, var4, var5, jk.u == jk.x.i ? 0.0F : 6.0F, var8, var6);
      }
   }

   public static void j(GuiGraphicsExtractor var0, int var1, int var2, int var3, int var4, double var5) {
      P(var0, var1, var2, var3, var4, rv.u() ? Math.min(8, var4 / 2) : rv.g, var5);
   }

   public static void P(GuiGraphicsExtractor var0, int var1, int var2, int var3, int var4, int var5, double var6) {
      if (!rv.u()) {
         Y(var0, var1, var2, var3, var4, var5, rv.z, 0.5 + 0.6 * var6);
         if (var6 > 0.001) {
            ow.y(var0, var1, var2, var3, var4, var5, oh.i(rv.P, 0.45 * var6), oh.i(rv.P, 0.25 * var6));
         }
      } else {
         int var8 = rv.a(var1, rv.z);
         if (var6 > 0.001) {
            ow.A(var0, var1, var2, var3, var4, var5, 7.0F, oh.i(var8, 0.24 * var6), oh.i(var8, 0.24 * var6));
         }

         N(var0, var1, var2, var3, var4, var5, oh.j(rv.z, rv.Z, 0.5 * var6));
         ow.y(var0, var1, var2, var3, var4, var5, oh.j(rv.h, oh.i(var8, 0.9), var6), oh.j(rv.C, oh.i(var8, 0.9), var6));
      }
   }

   public static void w(GuiGraphicsExtractor var0, int var1, int var2, int var3, int var4) {
      if (rv.u()) {
         w(var0, var1, var2, var3, var4, rv.S, oh.j(rv.H, rv.d | 0xFF000000, 0.35), 1.0);
      } else {
         N(var0, var1, var2, var3, var4, rv.Gj, rv.V);
         fr.W(var0, var1, var2 + 4, Math.max(0, var4 - 8), true, rv.h);
      }
   }

   public static void w(GuiGraphicsExtractor var0, int var1, int var2, int var3, int var4, int var5) {
      if (rv.u()) {
         ow.A(var0, var1 - 1, var2 - 1, var3 + 2, var4 + 2, var5 + 1, oh.i(rv.a(var1, rv.H), 0.85));
      } else {
         ow.y(var0, var1 - 1, var2 - 1, var3 + 2, var4 + 2, var5 + 1, oh.S(rv.P, 0.55), oh.S(rv.P, 0.3));
      }
   }

   public static void q(GuiGraphicsExtractor var0, int var1, int var2, int var3, int var4, double var5, boolean var7) {
      Y(var0, var1, var2, var3, var4, rv.g, oh.j(rv.H, rv.Z, var7 ? 1.0 : var5), 0.6 + var5 * 0.4);
   }

   public static void w(GuiGraphicsExtractor var0, int var1, int var2, boolean var3) {
      e(var0, var1, var2, var3 ? 1.0 : 0.0);
   }

   public static void e(GuiGraphicsExtractor var0, int var1, int var2, double var3) {
      T(var0, var1, var2, var3, rv.m(var1), jd.controlInk(rv.w) == -15132391);
   }

   public static void T(GuiGraphicsExtractor var0, int var1, int var2, double var3, int var5, boolean var6) {
      double var7 = Math.clamp(var3, 0.0, 1.0);
      int var9 = jd.readableAccent(var5, rv.H | 0xFF000000);
      int var10 = jd.controlInk(var9);
      if (rv.u()) {
         var10 = jd.contrast(-1, var9) >= 2.2 ? -1 : -15132388;
         int var17 = var6 ? -2894888 : oh.j(rv.q | 0xFF000000, rv.P, 0.12);
         int var18 = var6 ? -1 : -3552816;
         switch (rv.D().switches()) {
            case "Square":
               b(var0, var1, var2, var7, var9, var17, oh.j(var18, var10, var7), var6);
               return;
            case "Minimal":
               O(var0, var1, var2, var7, var9, var17, var6);
               return;
            default:
               if (var7 > 0.01) {
                  S(var0, var1, var2, 24.0F, 13.0F, 6.5F, 5.0F, oh.i(var9, 0.35 * var7));
               }

               ow.P(var0, var1, var2, 24.0F, 13.0F, 6.5F, oh.j(var17, var9, var7));
               if (var7 > 0.01) {
                  ow.Z(var0, var1, var2, 24.0F, 13.0F, 6.5F, oh.i(rv.GQ, 0.0), oh.i(rv.GQ, 0.55 * var7));
               }

               if (var7 < 0.999) {
                  ow.y(var0, var1, var2, 24.0F, 13.0F, 6.5F, oh.S(rv.h, 1.0 - var7), oh.S(rv.C, 1.0 - var7));
               }

               float var19 = (float)(3.15 * (1.0 - Math.abs(var7 * 2.0 - 1.0)));
               float var20 = 9.0F + var19;
               float var15 = var1 + 2 + (float)(11.0 * var7) - var19 * (float)var7;
               ow.A(var0, var15, var2 + 2, var20, 9.0F, 4.5F, 3.0F, 1207959552, 1509949440);
               ow.P(var0, var15, var2 + 2, var20, 9.0F, 4.5F, oh.j(var18, var10, var7));
               if (var6) {
                  ow.y(var0, var15, var2 + 2, var20, 9.0F, 4.5F, 469762048, 704643072);
               }
         }
      } else {
         int var11 = oh.j(var6 ? -9143678 : -12961217, var9, var7);
         Y(var0, var1 + 1, var2 + 1, 22, 12, 6, var11, 0.35);
         int var12 = var1 + 3 + (int)Math.round(var7 * 10.0);
         N(var0, var12, var2 + 3, 8, 8, 4, 939524096);
         N(var0, var12, var2 + 3, 8, 8, 4, oh.j(-1776408, var10, var7));
      }
   }

   private static void b(GuiGraphicsExtractor var0, int var1, int var2, double var3, int var5, int var6, int var7, boolean var8) {
      if (var3 > 0.01) {
         S(var0, var1, var2, 24.0F, 13.0F, 2.5F, 5.0F, oh.i(var5, 0.3 * var3));
      }

      ow.P(var0, var1, var2, 24.0F, 13.0F, 2.5F, oh.j(var6, var5, var3));
      if (var3 < 0.999) {
         ow.y(var0, var1, var2, 24.0F, 13.0F, 2.5F, oh.S(rv.h, 1.0 - var3), oh.S(rv.C, 1.0 - var3));
      }

      float var9 = var1 + 2 + (float)(11.0 * var3);
      ow.A(var0, var9, var2 + 2, 9.0F, 9.0F, 1.5F, 2.5F, 1073741824, 1342177280);
      ow.P(var0, var9, var2 + 2, 9.0F, 9.0F, 1.5F, var7);
      if (var8) {
         ow.y(var0, var9, var2 + 2, 9.0F, 9.0F, 1.5F, 469762048, 704643072);
      }
   }

   private static void O(GuiGraphicsExtractor var0, int var1, int var2, double var3, int var5, int var6, boolean var7) {
      ow.P(var0, var1 + 1, var2 + 4, 22.0F, 5.0F, 2.5F, oh.j(var6, oh.i(var5, 0.55), var3));
      float var8 = 5.5F;
      float var9 = var1 + 1 + var8 + (float)((22.0F - 2.0F * var8) * var3);
      ow.A(var0, var9 - var8, var2 + 1, 2.0F * var8, 2.0F * var8, var8, 2.5F, 1073741824, 1342177280);
      int var10 = oh.j(var7 ? -1 : -1776408, var5 | 0xFF000000, var3);
      ow.P(var0, var9 - var8, var2 + 1, 2.0F * var8, 2.0F * var8, var8, var10);
      float var11 = var8 * 0.42F;
      ow.P(var0, var9 - var11, var2 + 1 + var8 - var11, 2.0F * var11, 2.0F * var11, var11, oh.j(oh.i(rv.w, 0.6), -1, var3));
   }

   public static void g(GuiGraphicsExtractor var0, int var1, int var2, int var3, int var4, int var5) {
      if (var4 > var3) {
         int var6 = Math.min(var3, Math.max(18, var3 * var3 / var4));
         int var7 = (int)((double)(var3 - var6) * var5 / (var4 - var3));
         N(var0, var1, var2 + var7, 2, var6, 1, rv.u() ? oh.i(rv.G0, 0.4) : -2139654281);
      }
   }

   public static void m(GuiGraphicsExtractor var0, int var1, int var2, int var3, int var4, double var5) {
      Y(var0, var1, var2, var3, var4, rv.N, oh.S(rv.s, var5), var5 * 0.7);
   }

   public static void G(GuiGraphicsExtractor var0, int var1, int var2, int var3, int var4, int var5) {
      u(var0, var1, var2, var3, var4, rv.Gj, var5);
   }

   public static void u(GuiGraphicsExtractor var0, int var1, int var2, int var3, int var4, int var5, int var6) {
      ow.y(var0, var1, var2, var3, var4, var5, var6, oh.S(var6, 0.28));
   }

   public static void D(GuiGraphicsExtractor var0, int var1, int var2, int var3, int var4) {
      N(var0, var1, var2, var3, var4, rv.T);
   }

   public static void N(GuiGraphicsExtractor var0, int var1, int var2, int var3, int var4, int var5) {
      if (rv.u()) {
         ow.A(var0, var1, var2, var3, var4, var5, 30.0F, oh.S(rv.p, 0.25), rv.p);
         ow.A(var0, var1, var2, var3, var4, var5, 6.0F, oh.S(rv.p, 0.45), oh.S(rv.p, 0.7));
      } else {
         for (byte var6 = 8; var6 >= 2; var6 -= 2) {
            N(var0, var1 - var6, var2 - var6 + 5, var3 + var6 * 2, var4 + var6 * 2, rv.T + var6, 150994944);
         }
      }
   }
}
