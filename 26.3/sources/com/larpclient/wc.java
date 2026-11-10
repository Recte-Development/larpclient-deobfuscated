package com.larpclient;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.Identifier;
import org.joml.Vector2f;

public final class wc {
   private static final Identifier I = Identifier.fromNamespaceAndPath("larp-client", "textures/gui/glass/fill.png");
   private static final Identifier a = Identifier.fromNamespaceAndPath("larp-client", "textures/gui/glass/rim.png");
   private static final int[] g = new int[]{0, 48, 80};
   private static final int[] W = new int[]{48, 32, 48};
   static final double i = 6.0;

   private wc() {
   }

   private static void o(GuiGraphicsExtractor var0, Identifier var1, int var2, int var3, int var4, int var5, int var6, int var7) {
      var7 = oa.e(var7);
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
                        g[var12],
                        g[var9],
                        var14,
                        var11,
                        W[var12],
                        W[var9],
                        128,
                        128,
                        var1.equals(a) ? vv.O(var7, var9 == 0 ? 1.0 : (var9 == 1 ? 0.55 : 0.28)) : var7
                     );
                  }
               }
            }
         }
      }
   }

   public static void b(GuiGraphicsExtractor var0, int var1, int var2, int var3, int var4, int var5, int var6) {
      w1.a(var0, var1, var2, var3, var4, var5, var6);
   }

   static void y(GuiGraphicsExtractor var0, int var1, int var2, int var3, int var4, int var5, int var6) {
      o(var0, I, var1, var2, var3, var4, var5, var6);
   }

   static void a(GuiGraphicsExtractor var0, int var1, int var2, int var3, int var4, int var5, int var6) {
      o(var0, a, var1, var2, var3, var4, var5, var6);
   }

   public static void y(GuiGraphicsExtractor var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7, double var8) {
      b(var0, var1, var2, var3, var4, var5, vv.Q(var6, var7, 0.6));
      w1.T(var0, var1, var2, var3, var4, var5, vv.O(c.Xn, var8), vv.O(c.XZ, var8));
   }

   public static void t(GuiGraphicsExtractor var0, int var1, int var2, int var3, int var4, int var5, int var6, double var7) {
      y(var0, var1, var2, var3, var4, var5, var6, var6, var7);
   }

   public static void Q(GuiGraphicsExtractor var0, int var1, int var2, int var3, int var4) {
      A(var0, var1, var2, var3, var4);
      M(var0, var1, var2, var3, var4);
   }

   public static void A(GuiGraphicsExtractor var0, int var1, int var2, int var3, int var4) {
      int var5 = Minecraft.getInstance().level != null ? 1 : 0;
      int var6 = var5 != 0 && !dx.X() && nl.z() ? 1 : 0;
      if (var6 != 0) {
         Vector2f var7 = var0.pose().transformPosition(var1, var2, new Vector2f());
         float var8 = var3 * var0.pose().m00();
         float var9 = var3 * var0.pose().m01();
         float var10 = var4 * var0.pose().m10();
         float var11 = var4 * var0.pose().m11();
         nl.N(
            Math.round(var7.x + Math.min(0.0F, var8) + Math.min(0.0F, var10)),
            Math.round(var7.y + Math.min(0.0F, var9) + Math.min(0.0F, var11)),
            Math.round(Math.abs(var8) + Math.abs(var10)),
            Math.round(Math.abs(var9) + Math.abs(var11))
         );
      }

      F(var0, var1, var2, var3, var4);
      b(var0, var1, var2, var3, var4, c.O, var5 != 0 && var6 == 0 ? c.k() : c.x);
      if (c.J()) {
         w1.p(var0, var1, var2, var3, var4 * 0.5F, c.O, vv.N(c.y, 0.07 * c.F()), vv.N(c.y, 0.0));
      }
   }

   public static void q(GuiGraphicsExtractor var0, int var1, int var2, int var3, int var4) {
      LarpClient var5 = LarpClient.N();
      int var6 = var5 != null && !var5.h().k().w ? 0 : 1;
      double var7 = var6 != 0 ? 0.5 + 0.5 * Math.sin(System.nanoTime() / 1.0E9 * 1.6) : 0.5;
      b(var0, var1, var2, var3, var4, c.O, 14.0F, vv.N(c.y, (0.12 + 0.05 * var7) * c.F()));
   }

   public static void M(GuiGraphicsExtractor var0, int var1, int var2, int var3, int var4) {
      if (c.J()) {
         w1.T(var0, var1, var2, var3, var4, c.O, vv.Q(c.Xn, vv.N(c.y, 0.5), 0.65 * c.F()), c.XZ);
      } else {
         w1.T(var0, var1, var2, var3, var4, c.O, c.Xn, c.XZ);
      }
   }

   public static void g(GuiGraphicsExtractor var0, int var1, int var2, int var3, int var4, int var5, int var6, double var7) {
      if (c.J() && var7 > 0.0) {
         w1.h(var0, var1, var2, var3, var4, var5, 10.0F + 6.0F * (float)var7, vv.O(c.Z, 0.35 * var7), vv.O(c.Z, 0.7 * var7));
      }

      b(var0, var1, var2, var3, var4, var5, var6);
      w1.T(var0, var1, var2, var3, var4, var5, c.Xn, c.Xn);
   }

   public static void b(GuiGraphicsExtractor var0, float var1, float var2, float var3, float var4, float var5, float var6, int var7) {
      if (c.J() && nl.x != nl.q.q) {
         w1.h(var0, var1, var2, var3, var4, var5, var6, var7, var7);
      }
   }

   public static void D(GuiGraphicsExtractor var0, int var1, int var2, int var3, int var4, int var5, int var6) {
      if (c.J()) {
         LarpClient var7 = LarpClient.N();
         double var8 = var7 != null && !var7.h().k().w ? 0.0 : System.nanoTime() / 1.0E9 / 6.0;
         w1.W(var0, var1, var2, var3, var4, var5, nl.x == nl.q.q ? 0.0F : 6.0F, var8, var6);
      }
   }

   public static void x(GuiGraphicsExtractor var0, int var1, int var2, int var3, int var4, double var5) {
      A(var0, var1, var2, var3, var4, c.J() ? Math.min(8, var4 / 2) : c.G, var5);
   }

   public static void A(GuiGraphicsExtractor var0, int var1, int var2, int var3, int var4, int var5, double var6) {
      if (!c.J()) {
         t(var0, var1, var2, var3, var4, var5, c.R, 0.5 + 0.6 * var6);
         if (var6 > 0.001) {
            w1.T(var0, var1, var2, var3, var4, var5, vv.N(c.H, 0.45 * var6), vv.N(c.H, 0.25 * var6));
         }
      } else {
         int var8 = c.e(var1, c.R);
         if (var6 > 0.001) {
            w1.h(var0, var1, var2, var3, var4, var5, 7.0F, vv.N(var8, 0.24 * var6), vv.N(var8, 0.24 * var6));
         }

         b(var0, var1, var2, var3, var4, var5, vv.Q(c.R, c.g, 0.5 * var6));
         w1.T(var0, var1, var2, var3, var4, var5, vv.Q(c.Xn, vv.N(var8, 0.9), var6), vv.Q(c.XZ, vv.N(var8, 0.9), var6));
      }
   }

   public static void L(GuiGraphicsExtractor var0, int var1, int var2, int var3, int var4) {
      if (c.J()) {
         g(var0, var1, var2, var3, var4, c.k, vv.Q(c.m, c.u | 0xFF000000, 0.35), 1.0);
      } else {
         b(var0, var1, var2, var3, var4, c.i, c.h);
         h5.T(var0, var1, var2 + 4, Math.max(0, var4 - 8), true, c.Xn);
      }
   }

   public static void U(GuiGraphicsExtractor var0, int var1, int var2, int var3, int var4, int var5) {
      if (c.J()) {
         w1.X(var0, var1 - 1, var2 - 1, var3 + 2, var4 + 2, var5 + 1, vv.N(c.e(var1, c.m), 0.85));
      } else {
         w1.T(var0, var1 - 1, var2 - 1, var3 + 2, var4 + 2, var5 + 1, vv.O(c.H, 0.55), vv.O(c.H, 0.3));
      }
   }

   public static void r(GuiGraphicsExtractor var0, int var1, int var2, int var3, int var4, double var5, boolean var7) {
      t(var0, var1, var2, var3, var4, c.G, vv.Q(c.m, c.g, var7 ? 1.0 : var5), 0.6 + var5 * 0.4);
   }

   public static void n(GuiGraphicsExtractor var0, int var1, int var2, boolean var3) {
      q(var0, var1, var2, var3 ? 1.0 : 0.0);
   }

   public static void q(GuiGraphicsExtractor var0, int var1, int var2, double var3) {
      v(var0, var1, var2, var3, c.c(var1), w4.controlInk(c.D) == -15132391);
   }

   public static void v(GuiGraphicsExtractor var0, int var1, int var2, double var3, int var5, boolean var6) {
      double var7 = Math.clamp(var3, 0.0, 1.0);
      int var9 = w4.readableAccent(var5, c.m | 0xFF000000);
      int var10 = w4.controlInk(var9);
      if (c.J()) {
         var10 = w4.contrast(-1, var9) >= 2.2 ? -1 : -15132388;
         int var17 = var6 ? -2894888 : vv.Q(c.I | 0xFF000000, c.H, 0.12);
         int var18 = var6 ? -1 : -3552816;
         switch (c.t().switches()) {
            case "Square":
               S(var0, var1, var2, var7, var9, var17, vv.Q(var18, var10, var7), var6);
               return;
            case "Minimal":
               c(var0, var1, var2, var7, var9, var17, var6);
               return;
            default:
               if (var7 > 0.01) {
                  b(var0, var1, var2, 24.0F, 13.0F, 6.5F, 5.0F, vv.N(var9, 0.35 * var7));
               }

               w1.a(var0, var1, var2, 24.0F, 13.0F, 6.5F, vv.Q(var17, var9, var7));
               if (var7 > 0.01) {
                  w1.c(var0, var1, var2, 24.0F, 13.0F, 6.5F, vv.N(c.t, 0.0), vv.N(c.t, 0.55 * var7));
               }

               if (var7 < 0.999) {
                  w1.T(var0, var1, var2, 24.0F, 13.0F, 6.5F, vv.O(c.Xn, 1.0 - var7), vv.O(c.XZ, 1.0 - var7));
               }

               float var19 = (float)(3.15 * (1.0 - Math.abs(var7 * 2.0 - 1.0)));
               float var20 = 9.0F + var19;
               float var15 = var1 + 2 + (float)(11.0 * var7) - var19 * (float)var7;
               w1.h(var0, var15, var2 + 2, var20, 9.0F, 4.5F, 3.0F, 1207959552, 1509949440);
               w1.a(var0, var15, var2 + 2, var20, 9.0F, 4.5F, vv.Q(var18, var10, var7));
               if (var6) {
                  w1.T(var0, var15, var2 + 2, var20, 9.0F, 4.5F, 469762048, 704643072);
               }
         }
      } else {
         int var11 = vv.Q(var6 ? -9143678 : -12961217, var9, var7);
         t(var0, var1 + 1, var2 + 1, 22, 12, 6, var11, 0.35);
         int var12 = var1 + 3 + (int)Math.round(var7 * 10.0);
         b(var0, var12, var2 + 3, 8, 8, 4, 939524096);
         b(var0, var12, var2 + 3, 8, 8, 4, vv.Q(-1776408, var10, var7));
      }
   }

   private static void S(GuiGraphicsExtractor var0, int var1, int var2, double var3, int var5, int var6, int var7, boolean var8) {
      if (var3 > 0.01) {
         b(var0, var1, var2, 24.0F, 13.0F, 2.5F, 5.0F, vv.N(var5, 0.3 * var3));
      }

      w1.a(var0, var1, var2, 24.0F, 13.0F, 2.5F, vv.Q(var6, var5, var3));
      if (var3 < 0.999) {
         w1.T(var0, var1, var2, 24.0F, 13.0F, 2.5F, vv.O(c.Xn, 1.0 - var3), vv.O(c.XZ, 1.0 - var3));
      }

      float var9 = var1 + 2 + (float)(11.0 * var3);
      w1.h(var0, var9, var2 + 2, 9.0F, 9.0F, 1.5F, 2.5F, 1073741824, 1342177280);
      w1.a(var0, var9, var2 + 2, 9.0F, 9.0F, 1.5F, var7);
      if (var8) {
         w1.T(var0, var9, var2 + 2, 9.0F, 9.0F, 1.5F, 469762048, 704643072);
      }
   }

   private static void c(GuiGraphicsExtractor var0, int var1, int var2, double var3, int var5, int var6, boolean var7) {
      w1.a(var0, var1 + 1, var2 + 4, 22.0F, 5.0F, 2.5F, vv.Q(var6, vv.N(var5, 0.55), var3));
      float var8 = 5.5F;
      float var9 = var1 + 1 + var8 + (float)((22.0F - 2.0F * var8) * var3);
      w1.h(var0, var9 - var8, var2 + 1, 2.0F * var8, 2.0F * var8, var8, 2.5F, 1073741824, 1342177280);
      int var10 = vv.Q(var7 ? -1 : -1776408, var5 | 0xFF000000, var3);
      w1.a(var0, var9 - var8, var2 + 1, 2.0F * var8, 2.0F * var8, var8, var10);
      float var11 = var8 * 0.42F;
      w1.a(var0, var9 - var11, var2 + 1 + var8 - var11, 2.0F * var11, 2.0F * var11, var11, vv.Q(vv.N(c.D, 0.6), -1, var3));
   }

   public static void V(GuiGraphicsExtractor var0, int var1, int var2, int var3, int var4, int var5) {
      if (var4 > var3) {
         int var6 = Math.min(var3, Math.max(18, var3 * var3 / var4));
         int var7 = (int)((double)(var3 - var6) * var5 / (var4 - var3));
         b(var0, var1, var2 + var7, 2, var6, 1, c.J() ? vv.N(c.W, 0.4) : -2139654281);
      }
   }

   public static void M(GuiGraphicsExtractor var0, int var1, int var2, int var3, int var4, double var5) {
      t(var0, var1, var2, var3, var4, c.X, vv.O(c.x, var5), var5 * 0.7);
   }

   public static void j(GuiGraphicsExtractor var0, int var1, int var2, int var3, int var4, int var5) {
      h(var0, var1, var2, var3, var4, c.i, var5);
   }

   public static void h(GuiGraphicsExtractor var0, int var1, int var2, int var3, int var4, int var5, int var6) {
      w1.T(var0, var1, var2, var3, var4, var5, var6, vv.O(var6, 0.28));
   }

   public static void F(GuiGraphicsExtractor var0, int var1, int var2, int var3, int var4) {
      T(var0, var1, var2, var3, var4, c.O);
   }

   public static void T(GuiGraphicsExtractor var0, int var1, int var2, int var3, int var4, int var5) {
      if (c.J()) {
         w1.h(var0, var1, var2, var3, var4, var5, 30.0F, vv.O(c.Z, 0.25), c.Z);
         w1.h(var0, var1, var2, var3, var4, var5, 6.0F, vv.O(c.Z, 0.45), vv.O(c.Z, 0.7));
      } else {
         for (byte var6 = 8; var6 >= 2; var6 -= 2) {
            b(var0, var1 - var6, var2 - var6 + 5, var3 + var6 * 2, var4 + var6 * 2, c.O + var6, 150994944);
         }
      }
   }
}
