package com.larpclient;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.Identifier;
import org.joml.Vector2f;

public final class wp {
   private static final Identifier Y = Identifier.fromNamespaceAndPath("larp-client", "textures/gui/glass/fill.png");
   private static final Identifier z = Identifier.fromNamespaceAndPath("larp-client", "textures/gui/glass/rim.png");
   private static final int[] B = new int[]{0, 48, 80};
   private static final int[] g = new int[]{48, 32, 48};
   static final double X = 6.0;

   private wp() {
   }

   private static void R(GuiGraphicsExtractor var0, Identifier var1, int var2, int var3, int var4, int var5, int var6, int var7) {
      var7 = ua.I(var7);
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
                        B[var12],
                        B[var9],
                        var14,
                        var11,
                        g[var12],
                        g[var9],
                        128,
                        128,
                        var1.equals(z) ? uh.F(var7, var9 == 0 ? 1.0 : (var9 == 1 ? 0.55 : 0.28)) : var7
                     );
                  }
               }
            }
         }
      }
   }

   public static void I(GuiGraphicsExtractor var0, int var1, int var2, int var3, int var4, int var5, int var6) {
      tb.E(var0, var1, var2, var3, var4, var5, var6);
   }

   static void U(GuiGraphicsExtractor var0, int var1, int var2, int var3, int var4, int var5, int var6) {
      R(var0, Y, var1, var2, var3, var4, var5, var6);
   }

   static void p(GuiGraphicsExtractor var0, int var1, int var2, int var3, int var4, int var5, int var6) {
      R(var0, z, var1, var2, var3, var4, var5, var6);
   }

   public static void V(GuiGraphicsExtractor var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7, double var8) {
      I(var0, var1, var2, var3, var4, var5, uh.q(var6, var7, 0.6));
      tb.s(var0, var1, var2, var3, var4, var5, uh.F(ho.Y, var8), uh.F(ho.S, var8));
   }

   public static void Z(GuiGraphicsExtractor var0, int var1, int var2, int var3, int var4, int var5, int var6, double var7) {
      V(var0, var1, var2, var3, var4, var5, var6, var6, var7);
   }

   public static void g(GuiGraphicsExtractor var0, int var1, int var2, int var3, int var4) {
      Q(var0, var1, var2, var3, var4);
      s(var0, var1, var2, var3, var4);
   }

   public static void Q(GuiGraphicsExtractor var0, int var1, int var2, int var3, int var4) {
      int var5 = Minecraft.getInstance().level != null ? 1 : 0;
      int var6 = var5 != 0 && !m_.T() && wa.b() ? 1 : 0;
      if (var6 != 0) {
         Vector2f var7 = var0.pose().transformPosition(var1, var2, new Vector2f());
         float var8 = var3 * var0.pose().m00();
         float var9 = var3 * var0.pose().m01();
         float var10 = var4 * var0.pose().m10();
         float var11 = var4 * var0.pose().m11();
         wa.T(
            Math.round(var7.x + Math.min(0.0F, var8) + Math.min(0.0F, var10)),
            Math.round(var7.y + Math.min(0.0F, var9) + Math.min(0.0F, var11)),
            Math.round(Math.abs(var8) + Math.abs(var10)),
            Math.round(Math.abs(var9) + Math.abs(var11))
         );
      }

      R(var0, var1, var2, var3, var4);
      I(var0, var1, var2, var3, var4, ho.x, var5 != 0 && var6 == 0 ? ho.c() : ho.t);
      if (ho.S()) {
         tb.u(var0, var1, var2, var3, var4 * 0.5F, ho.x, uh.H(ho.J, 0.07 * ho.s()), uh.H(ho.J, 0.0));
      }
   }

   public static void v(GuiGraphicsExtractor var0, int var1, int var2, int var3, int var4) {
      LarpClient var5 = LarpClient.R();
      int var6 = var5 != null && !var5.o().g().L ? 0 : 1;
      double var7 = var6 != 0 ? 0.5 + 0.5 * Math.sin(System.nanoTime() / 1.0E9 * 1.6) : 0.5;
      e(var0, var1, var2, var3, var4, ho.x, 14.0F, uh.H(ho.J, (0.12 + 0.05 * var7) * ho.s()));
   }

   public static void s(GuiGraphicsExtractor var0, int var1, int var2, int var3, int var4) {
      if (ho.S()) {
         tb.s(var0, var1, var2, var3, var4, ho.x, uh.q(ho.Y, uh.H(ho.J, 0.5), 0.65 * ho.s()), ho.S);
      } else {
         tb.s(var0, var1, var2, var3, var4, ho.x, ho.Y, ho.S);
      }
   }

   public static void S(GuiGraphicsExtractor var0, int var1, int var2, int var3, int var4, int var5, int var6, double var7) {
      if (ho.S() && var7 > 0.0) {
         tb.Q(var0, var1, var2, var3, var4, var5, 10.0F + 6.0F * (float)var7, uh.F(ho.v, 0.35 * var7), uh.F(ho.v, 0.7 * var7));
      }

      I(var0, var1, var2, var3, var4, var5, var6);
      tb.s(var0, var1, var2, var3, var4, var5, ho.Y, ho.Y);
   }

   public static void e(GuiGraphicsExtractor var0, float var1, float var2, float var3, float var4, float var5, float var6, int var7) {
      if (ho.S() && wa.h != wa.a.Z) {
         tb.Q(var0, var1, var2, var3, var4, var5, var6, var7, var7);
      }
   }

   public static void m(GuiGraphicsExtractor var0, int var1, int var2, int var3, int var4, int var5, int var6) {
      if (ho.S()) {
         LarpClient var7 = LarpClient.R();
         double var8 = var7 != null && !var7.o().g().L ? 0.0 : System.nanoTime() / 1.0E9 / 6.0;
         tb.N(var0, var1, var2, var3, var4, var5, wa.h == wa.a.Z ? 0.0F : 6.0F, var8, var6);
      }
   }

   public static void u(GuiGraphicsExtractor var0, int var1, int var2, int var3, int var4, double var5) {
      s(var0, var1, var2, var3, var4, ho.S() ? Math.min(8, var4 / 2) : ho.Q, var5);
   }

   public static void s(GuiGraphicsExtractor var0, int var1, int var2, int var3, int var4, int var5, double var6) {
      if (!ho.S()) {
         Z(var0, var1, var2, var3, var4, var5, ho.B, 0.5 + 0.6 * var6);
         if (var6 > 0.001) {
            tb.s(var0, var1, var2, var3, var4, var5, uh.H(ho.u, 0.45 * var6), uh.H(ho.u, 0.25 * var6));
         }
      } else {
         int var8 = ho.S(var1, ho.B);
         if (var6 > 0.001) {
            tb.Q(var0, var1, var2, var3, var4, var5, 7.0F, uh.H(var8, 0.24 * var6), uh.H(var8, 0.24 * var6));
         }

         I(var0, var1, var2, var3, var4, var5, uh.q(ho.B, ho.e, 0.5 * var6));
         tb.s(var0, var1, var2, var3, var4, var5, uh.q(ho.Y, uh.H(var8, 0.9), var6), uh.q(ho.S, uh.H(var8, 0.9), var6));
      }
   }

   public static void G(GuiGraphicsExtractor var0, int var1, int var2, int var3, int var4) {
      if (ho.S()) {
         S(var0, var1, var2, var3, var4, ho.dr, uh.q(ho.f, ho.F | 0xFF000000, 0.35), 1.0);
      } else {
         I(var0, var1, var2, var3, var4, ho.k, ho.a);
         kv.Y(var0, var1, var2 + 4, Math.max(0, var4 - 8), true, ho.Y);
      }
   }

   public static void t(GuiGraphicsExtractor var0, int var1, int var2, int var3, int var4, int var5) {
      if (ho.S()) {
         tb.J(var0, var1 - 1, var2 - 1, var3 + 2, var4 + 2, var5 + 1, uh.H(ho.S(var1, ho.f), 0.85));
      } else {
         tb.s(var0, var1 - 1, var2 - 1, var3 + 2, var4 + 2, var5 + 1, uh.F(ho.u, 0.55), uh.F(ho.u, 0.3));
      }
   }

   public static void k(GuiGraphicsExtractor var0, int var1, int var2, int var3, int var4, double var5, boolean var7) {
      Z(var0, var1, var2, var3, var4, ho.Q, uh.q(ho.f, ho.e, var7 ? 1.0 : var5), 0.6 + var5 * 0.4);
   }

   public static void I(GuiGraphicsExtractor var0, int var1, int var2, boolean var3) {
      E(var0, var1, var2, var3 ? 1.0 : 0.0);
   }

   public static void E(GuiGraphicsExtractor var0, int var1, int var2, double var3) {
      Q(var0, var1, var2, var3, ho.Q(var1), lv.controlInk(ho.A) == -15132391);
   }

   public static void Q(GuiGraphicsExtractor var0, int var1, int var2, double var3, int var5, boolean var6) {
      double var7 = Math.clamp(var3, 0.0, 1.0);
      int var9 = lv.readableAccent(var5, ho.f | 0xFF000000);
      int var10 = lv.controlInk(var9);
      if (ho.S()) {
         var10 = lv.contrast(-1, var9) >= 2.2 ? -1 : -15132388;
         int var17 = var6 ? -2894888 : uh.q(ho.dy | 0xFF000000, ho.u, 0.12);
         int var18 = var6 ? -1 : -3552816;
         switch (ho.m().switches()) {
            case "Square":
               F(var0, var1, var2, var7, var9, var17, uh.q(var18, var10, var7), var6);
               return;
            case "Minimal":
               Q(var0, var1, var2, var7, var9, var17, var6);
               return;
            default:
               if (var7 > 0.01) {
                  e(var0, var1, var2, 24.0F, 13.0F, 6.5F, 5.0F, uh.H(var9, 0.35 * var7));
               }

               tb.E(var0, var1, var2, 24.0F, 13.0F, 6.5F, uh.q(var17, var9, var7));
               if (var7 > 0.01) {
                  tb.d(var0, var1, var2, 24.0F, 13.0F, 6.5F, uh.H(ho.ds, 0.0), uh.H(ho.ds, 0.55 * var7));
               }

               if (var7 < 0.999) {
                  tb.s(var0, var1, var2, 24.0F, 13.0F, 6.5F, uh.F(ho.Y, 1.0 - var7), uh.F(ho.S, 1.0 - var7));
               }

               float var19 = (float)(3.15 * (1.0 - Math.abs(var7 * 2.0 - 1.0)));
               float var20 = 9.0F + var19;
               float var15 = var1 + 2 + (float)(11.0 * var7) - var19 * (float)var7;
               tb.Q(var0, var15, var2 + 2, var20, 9.0F, 4.5F, 3.0F, 1207959552, 1509949440);
               tb.E(var0, var15, var2 + 2, var20, 9.0F, 4.5F, uh.q(var18, var10, var7));
               if (var6) {
                  tb.s(var0, var15, var2 + 2, var20, 9.0F, 4.5F, 469762048, 704643072);
               }
         }
      } else {
         int var11 = uh.q(var6 ? -9143678 : -12961217, var9, var7);
         Z(var0, var1 + 1, var2 + 1, 22, 12, 6, var11, 0.35);
         int var12 = var1 + 3 + (int)Math.round(var7 * 10.0);
         I(var0, var12, var2 + 3, 8, 8, 4, 939524096);
         I(var0, var12, var2 + 3, 8, 8, 4, uh.q(-1776408, var10, var7));
      }
   }

   private static void F(GuiGraphicsExtractor var0, int var1, int var2, double var3, int var5, int var6, int var7, boolean var8) {
      if (var3 > 0.01) {
         e(var0, var1, var2, 24.0F, 13.0F, 2.5F, 5.0F, uh.H(var5, 0.3 * var3));
      }

      tb.E(var0, var1, var2, 24.0F, 13.0F, 2.5F, uh.q(var6, var5, var3));
      if (var3 < 0.999) {
         tb.s(var0, var1, var2, 24.0F, 13.0F, 2.5F, uh.F(ho.Y, 1.0 - var3), uh.F(ho.S, 1.0 - var3));
      }

      float var9 = var1 + 2 + (float)(11.0 * var3);
      tb.Q(var0, var9, var2 + 2, 9.0F, 9.0F, 1.5F, 2.5F, 1073741824, 1342177280);
      tb.E(var0, var9, var2 + 2, 9.0F, 9.0F, 1.5F, var7);
      if (var8) {
         tb.s(var0, var9, var2 + 2, 9.0F, 9.0F, 1.5F, 469762048, 704643072);
      }
   }

   private static void Q(GuiGraphicsExtractor var0, int var1, int var2, double var3, int var5, int var6, boolean var7) {
      tb.E(var0, var1 + 1, var2 + 4, 22.0F, 5.0F, 2.5F, uh.q(var6, uh.H(var5, 0.55), var3));
      float var8 = 5.5F;
      float var9 = var1 + 1 + var8 + (float)((22.0F - 2.0F * var8) * var3);
      tb.Q(var0, var9 - var8, var2 + 1, 2.0F * var8, 2.0F * var8, var8, 2.5F, 1073741824, 1342177280);
      int var10 = uh.q(var7 ? -1 : -1776408, var5 | 0xFF000000, var3);
      tb.E(var0, var9 - var8, var2 + 1, 2.0F * var8, 2.0F * var8, var8, var10);
      float var11 = var8 * 0.42F;
      tb.E(var0, var9 - var11, var2 + 1 + var8 - var11, 2.0F * var11, 2.0F * var11, var11, uh.q(uh.H(ho.A, 0.6), -1, var3));
   }

   public static void p(GuiGraphicsExtractor var0, int var1, int var2, int var3, int var4, int var5) {
      if (var4 > var3) {
         int var6 = Math.min(var3, Math.max(18, var3 * var3 / var4));
         int var7 = (int)((double)(var3 - var6) * var5 / (var4 - var3));
         I(var0, var1, var2 + var7, 2, var6, 1, ho.S() ? uh.H(ho.Z, 0.4) : -2139654281);
      }
   }

   public static void Z(GuiGraphicsExtractor var0, int var1, int var2, int var3, int var4, double var5) {
      Z(var0, var1, var2, var3, var4, ho.D, uh.F(ho.t, var5), var5 * 0.7);
   }

   public static void L(GuiGraphicsExtractor var0, int var1, int var2, int var3, int var4, int var5) {
      q(var0, var1, var2, var3, var4, ho.k, var5);
   }

   public static void q(GuiGraphicsExtractor var0, int var1, int var2, int var3, int var4, int var5, int var6) {
      tb.s(var0, var1, var2, var3, var4, var5, var6, uh.F(var6, 0.28));
   }

   public static void R(GuiGraphicsExtractor var0, int var1, int var2, int var3, int var4) {
      T(var0, var1, var2, var3, var4, ho.x);
   }

   public static void T(GuiGraphicsExtractor var0, int var1, int var2, int var3, int var4, int var5) {
      if (ho.S()) {
         tb.Q(var0, var1, var2, var3, var4, var5, 30.0F, uh.F(ho.v, 0.25), ho.v);
         tb.Q(var0, var1, var2, var3, var4, var5, 6.0F, uh.F(ho.v, 0.45), uh.F(ho.v, 0.7));
      } else {
         for (byte var6 = 8; var6 >= 2; var6 -= 2) {
            I(var0, var1 - var6, var2 - var6 + 5, var3 + var6 * 2, var4 + var6 * 2, ho.x + var6, 150994944);
         }
      }
   }
}
