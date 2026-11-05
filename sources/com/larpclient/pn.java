package com.larpclient;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.Identifier;
import org.joml.Vector2f;

public final class pn {
   private static final Identifier y = Identifier.fromNamespaceAndPath("larp-client", "textures/gui/glass/fill.png");
   private static final Identifier A = Identifier.fromNamespaceAndPath("larp-client", "textures/gui/glass/rim.png");
   private static final int[] j = new int[]{0, 48, 80};
   private static final int[] h = new int[]{48, 32, 48};
   static final double f = 6.0;

   private pn() {
   }

   private static void B(GuiGraphicsExtractor var0, Identifier var1, int var2, int var3, int var4, int var5, int var6, int var7) {
      var7 = de.h(var7);
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
                        j[var12],
                        j[var9],
                        var14,
                        var11,
                        h[var12],
                        h[var9],
                        128,
                        128,
                        var1.equals(A) ? _3.U(var7, var9 == 0 ? 1.0 : (var9 == 1 ? 0.55 : 0.28)) : var7
                     );
                  }
               }
            }
         }
      }
   }

   public static void b(GuiGraphicsExtractor var0, int var1, int var2, int var3, int var4, int var5, int var6) {
      tl.g(var0, var1, var2, var3, var4, var5, var6);
   }

   static void q(GuiGraphicsExtractor var0, int var1, int var2, int var3, int var4, int var5, int var6) {
      B(var0, y, var1, var2, var3, var4, var5, var6);
   }

   static void E(GuiGraphicsExtractor var0, int var1, int var2, int var3, int var4, int var5, int var6) {
      B(var0, A, var1, var2, var3, var4, var5, var6);
   }

   public static void p(GuiGraphicsExtractor var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7, double var8) {
      b(var0, var1, var2, var3, var4, var5, _3.D(var6, var7, 0.6));
      tl.V(var0, var1, var2, var3, var4, var5, _3.U(com.larpclient.f.H, var8), _3.U(com.larpclient.f.g, var8));
   }

   public static void g(GuiGraphicsExtractor var0, int var1, int var2, int var3, int var4, int var5, int var6, double var7) {
      p(var0, var1, var2, var3, var4, var5, var6, var6, var7);
   }

   public static void a(GuiGraphicsExtractor var0, int var1, int var2, int var3, int var4) {
      C(var0, var1, var2, var3, var4);
      b(var0, var1, var2, var3, var4);
   }

   public static void C(GuiGraphicsExtractor var0, int var1, int var2, int var3, int var4) {
      int var5 = Minecraft.getInstance().level != null ? 1 : 0;
      int var6 = var5 != 0 && !cp.P() && m5.w() ? 1 : 0;
      if (var6 != 0) {
         Vector2f var7 = var0.pose().transformPosition(var1, var2, new Vector2f());
         float var8 = var3 * var0.pose().m00();
         float var9 = var3 * var0.pose().m01();
         float var10 = var4 * var0.pose().m10();
         float var11 = var4 * var0.pose().m11();
         m5.y(
            Math.round(var7.x + Math.min(0.0F, var8) + Math.min(0.0F, var10)),
            Math.round(var7.y + Math.min(0.0F, var9) + Math.min(0.0F, var11)),
            Math.round(Math.abs(var8) + Math.abs(var10)),
            Math.round(Math.abs(var9) + Math.abs(var11))
         );
      }

      c(var0, var1, var2, var3, var4);
      b(var0, var1, var2, var3, var4, com.larpclient.f.r, var5 != 0 && var6 == 0 ? com.larpclient.f.h() : com.larpclient.f.K);
      if (com.larpclient.f.Y()) {
         tl.G(var0, var1, var2, var3, var4 * 0.5F, com.larpclient.f.r, _3.A(com.larpclient.f.C, 0.07 * com.larpclient.f.U()), _3.A(com.larpclient.f.C, 0.0));
      }
   }

   public static void F(GuiGraphicsExtractor var0, int var1, int var2, int var3, int var4) {
      LarpClient var5 = LarpClient.F();
      int var6 = var5 != null && !var5.f().t().V ? 0 : 1;
      double var7 = var6 != 0 ? 0.5 + 0.5 * Math.sin(System.nanoTime() / 1.0E9 * 1.6) : 0.5;
      Z(var0, var1, var2, var3, var4, com.larpclient.f.r, 14.0F, _3.A(com.larpclient.f.C, (0.12 + 0.05 * var7) * com.larpclient.f.U()));
   }

   public static void b(GuiGraphicsExtractor var0, int var1, int var2, int var3, int var4) {
      if (com.larpclient.f.Y()) {
         tl.V(
            var0,
            var1,
            var2,
            var3,
            var4,
            com.larpclient.f.r,
            _3.D(com.larpclient.f.H, _3.A(com.larpclient.f.C, 0.5), 0.65 * com.larpclient.f.U()),
            com.larpclient.f.g
         );
      } else {
         tl.V(var0, var1, var2, var3, var4, com.larpclient.f.r, com.larpclient.f.H, com.larpclient.f.g);
      }
   }

   public static void B(GuiGraphicsExtractor var0, int var1, int var2, int var3, int var4) {
      b(var0, var1, var2, var3, var4, com.larpclient.f.l, com.larpclient.f.v);
      tl.V(var0, var1, var2, var3, var4, com.larpclient.f.l, _3.U(com.larpclient.f.H, 0.8), _3.U(com.larpclient.f.H, 0.8));
   }

   public static void d(GuiGraphicsExtractor var0, int var1, int var2, int var3, int var4, int var5, int var6, double var7) {
      if (com.larpclient.f.Y() && var7 > 0.0) {
         tl.j(var0, var1, var2, var3, var4, var5, 10.0F + 6.0F * (float)var7, _3.U(com.larpclient.f.b, 0.35 * var7), _3.U(com.larpclient.f.b, 0.7 * var7));
      }

      b(var0, var1, var2, var3, var4, var5, var6);
      tl.V(var0, var1, var2, var3, var4, var5, com.larpclient.f.H, com.larpclient.f.H);
   }

   public static void Z(GuiGraphicsExtractor var0, float var1, float var2, float var3, float var4, float var5, float var6, int var7) {
      if (com.larpclient.f.Y() && m5.y != m5.n.N) {
         tl.j(var0, var1, var2, var3, var4, var5, var6, var7, var7);
      }
   }

   public static void a(GuiGraphicsExtractor var0, int var1, int var2, int var3, int var4, int var5, int var6) {
      if (com.larpclient.f.Y()) {
         LarpClient var7 = LarpClient.F();
         double var8 = var7 != null && !var7.f().t().V ? 0.0 : System.nanoTime() / 1.0E9 / 6.0;
         tl.Q(var0, var1, var2, var3, var4, var5, m5.y == m5.n.N ? 0.0F : 6.0F, var8, var6);
      }
   }

   public static void S(GuiGraphicsExtractor var0, int var1, int var2, int var3, int var4, double var5) {
      p(var0, var1, var2, var3, var4, com.larpclient.f.Y() ? Math.min(8, var4 / 2) : com.larpclient.f.aj, var5);
   }

   public static void p(GuiGraphicsExtractor var0, int var1, int var2, int var3, int var4, int var5, double var6) {
      if (!com.larpclient.f.Y()) {
         g(var0, var1, var2, var3, var4, var5, com.larpclient.f.S, 0.5 + 0.6 * var6);
         if (var6 > 0.001) {
            tl.V(var0, var1, var2, var3, var4, var5, _3.A(com.larpclient.f.J, 0.45 * var6), _3.A(com.larpclient.f.J, 0.25 * var6));
         }
      } else {
         int var8 = com.larpclient.f.Q(var1, com.larpclient.f.S);
         if (var6 > 0.001) {
            tl.j(var0, var1, var2, var3, var4, var5, 7.0F, _3.A(var8, 0.24 * var6), _3.A(var8, 0.24 * var6));
         }

         b(var0, var1, var2, var3, var4, var5, _3.D(com.larpclient.f.S, com.larpclient.f.x, 0.5 * var6));
         tl.V(var0, var1, var2, var3, var4, var5, _3.D(com.larpclient.f.H, _3.A(var8, 0.9), var6), _3.D(com.larpclient.f.g, _3.A(var8, 0.9), var6));
      }
   }

   public static void t(GuiGraphicsExtractor var0, int var1, int var2, int var3, int var4) {
      if (com.larpclient.f.Y()) {
         d(var0, var1, var2, var3, var4, com.larpclient.f.D, _3.D(com.larpclient.f.i, com.larpclient.f.v | 0xFF000000, 0.35), 1.0);
      } else {
         b(var0, var1, var2, var3, var4, com.larpclient.f.y, com.larpclient.f.h);
         c7.Q(var0, var1, var2 + 4, Math.max(0, var4 - 8), true, com.larpclient.f.H);
      }
   }

   public static void y(GuiGraphicsExtractor var0, int var1, int var2, int var3, int var4, int var5) {
      if (com.larpclient.f.Y()) {
         tl.M(var0, var1 - 1, var2 - 1, var3 + 2, var4 + 2, var5 + 1, _3.A(com.larpclient.f.Q(var1, com.larpclient.f.i), 0.85));
      } else {
         tl.V(var0, var1 - 1, var2 - 1, var3 + 2, var4 + 2, var5 + 1, _3.U(com.larpclient.f.J, 0.55), _3.U(com.larpclient.f.J, 0.3));
      }
   }

   public static void N(GuiGraphicsExtractor var0, int var1, int var2, int var3, int var4, double var5, boolean var7) {
      g(var0, var1, var2, var3, var4, com.larpclient.f.aj, _3.D(com.larpclient.f.i, com.larpclient.f.x, var7 ? 1.0 : var5), 0.6 + var5 * 0.4);
   }

   public static void r(GuiGraphicsExtractor var0, int var1, int var2, boolean var3) {
      Y(var0, var1, var2, var3 ? 1.0 : 0.0);
   }

   public static void Y(GuiGraphicsExtractor var0, int var1, int var2, double var3) {
      R(var0, var1, var2, var3, com.larpclient.f.z(var1), n.controlInk(com.larpclient.f.u) == -15132391);
   }

   public static void R(GuiGraphicsExtractor var0, int var1, int var2, double var3, int var5, boolean var6) {
      double var7 = Math.clamp(var3, 0.0, 1.0);
      int var9 = n.readableAccent(var5, com.larpclient.f.i | 0xFF000000);
      int var10 = n.controlInk(var9);
      if (com.larpclient.f.Y()) {
         var10 = n.contrast(-1, var9) >= 2.2 ? -1 : -15132388;
         int var17 = var6 ? -2894888 : _3.D(com.larpclient.f.m | 0xFF000000, com.larpclient.f.J, 0.12);
         int var18 = var6 ? -1 : -3552816;
         switch (com.larpclient.f.O().switches()) {
            case "Square":
               K(var0, var1, var2, var7, var9, var17, _3.D(var18, var10, var7), var6);
               return;
            case "Minimal":
               I(var0, var1, var2, var7, var9, var17, var6);
               return;
            default:
               if (var7 > 0.01) {
                  Z(var0, var1, var2, 24.0F, 13.0F, 6.5F, 5.0F, _3.A(var9, 0.35 * var7));
               }

               tl.g(var0, var1, var2, 24.0F, 13.0F, 6.5F, _3.D(var17, var9, var7));
               if (var7 > 0.01) {
                  tl.A(var0, var1, var2, 24.0F, 13.0F, 6.5F, _3.A(com.larpclient.f.O, 0.0), _3.A(com.larpclient.f.O, 0.55 * var7));
               }

               if (var7 < 0.999) {
                  tl.V(var0, var1, var2, 24.0F, 13.0F, 6.5F, _3.U(com.larpclient.f.H, 1.0 - var7), _3.U(com.larpclient.f.g, 1.0 - var7));
               }

               float var19 = (float)(3.15 * (1.0 - Math.abs(var7 * 2.0 - 1.0)));
               float var20 = 9.0F + var19;
               float var15 = var1 + 2 + (float)(11.0 * var7) - var19 * (float)var7;
               tl.j(var0, var15, var2 + 2, var20, 9.0F, 4.5F, 3.0F, 1207959552, 1509949440);
               tl.g(var0, var15, var2 + 2, var20, 9.0F, 4.5F, _3.D(var18, var10, var7));
               if (var6) {
                  tl.V(var0, var15, var2 + 2, var20, 9.0F, 4.5F, 469762048, 704643072);
               }
         }
      } else {
         int var11 = _3.D(var6 ? -9143678 : -12961217, var9, var7);
         g(var0, var1 + 1, var2 + 1, 22, 12, 6, var11, 0.35);
         int var12 = var1 + 3 + (int)Math.round(var7 * 10.0);
         b(var0, var12, var2 + 3, 8, 8, 4, 939524096);
         b(var0, var12, var2 + 3, 8, 8, 4, _3.D(-1776408, var10, var7));
      }
   }

   private static void K(GuiGraphicsExtractor var0, int var1, int var2, double var3, int var5, int var6, int var7, boolean var8) {
      if (var3 > 0.01) {
         Z(var0, var1, var2, 24.0F, 13.0F, 2.5F, 5.0F, _3.A(var5, 0.3 * var3));
      }

      tl.g(var0, var1, var2, 24.0F, 13.0F, 2.5F, _3.D(var6, var5, var3));
      if (var3 < 0.999) {
         tl.V(var0, var1, var2, 24.0F, 13.0F, 2.5F, _3.U(com.larpclient.f.H, 1.0 - var3), _3.U(com.larpclient.f.g, 1.0 - var3));
      }

      float var9 = var1 + 2 + (float)(11.0 * var3);
      tl.j(var0, var9, var2 + 2, 9.0F, 9.0F, 1.5F, 2.5F, 1073741824, 1342177280);
      tl.g(var0, var9, var2 + 2, 9.0F, 9.0F, 1.5F, var7);
      if (var8) {
         tl.V(var0, var9, var2 + 2, 9.0F, 9.0F, 1.5F, 469762048, 704643072);
      }
   }

   private static void I(GuiGraphicsExtractor var0, int var1, int var2, double var3, int var5, int var6, boolean var7) {
      tl.g(var0, var1 + 1, var2 + 4, 22.0F, 5.0F, 2.5F, _3.D(var6, _3.A(var5, 0.55), var3));
      float var8 = 5.5F;
      float var9 = var1 + 1 + var8 + (float)((22.0F - 2.0F * var8) * var3);
      tl.j(var0, var9 - var8, var2 + 1, 2.0F * var8, 2.0F * var8, var8, 2.5F, 1073741824, 1342177280);
      int var10 = _3.D(var7 ? -1 : -1776408, var5 | 0xFF000000, var3);
      tl.g(var0, var9 - var8, var2 + 1, 2.0F * var8, 2.0F * var8, var8, var10);
      float var11 = var8 * 0.42F;
      tl.g(var0, var9 - var11, var2 + 1 + var8 - var11, 2.0F * var11, 2.0F * var11, var11, _3.D(_3.A(com.larpclient.f.u, 0.6), -1, var3));
   }

   public static void u(GuiGraphicsExtractor var0, int var1, int var2, int var3, int var4, int var5) {
      if (var4 > var3) {
         int var6 = Math.min(var3, Math.max(18, var3 * var3 / var4));
         int var7 = (int)((double)(var3 - var6) * var5 / (var4 - var3));
         b(var0, var1, var2 + var7, 2, var6, 1, com.larpclient.f.Y() ? _3.A(com.larpclient.f.s, 0.4) : -2139654281);
      }
   }

   public static void h(GuiGraphicsExtractor var0, int var1, int var2, int var3, int var4, double var5) {
      g(var0, var1, var2, var3, var4, com.larpclient.f.B, _3.U(com.larpclient.f.K, var5), var5 * 0.7);
   }

   public static void Y(GuiGraphicsExtractor var0, int var1, int var2, int var3, int var4, int var5) {
      D(var0, var1, var2, var3, var4, com.larpclient.f.y, var5);
   }

   public static void D(GuiGraphicsExtractor var0, int var1, int var2, int var3, int var4, int var5, int var6) {
      tl.V(var0, var1, var2, var3, var4, var5, var6, _3.U(var6, 0.28));
   }

   public static void c(GuiGraphicsExtractor var0, int var1, int var2, int var3, int var4) {
      i(var0, var1, var2, var3, var4, com.larpclient.f.r);
   }

   public static void i(GuiGraphicsExtractor var0, int var1, int var2, int var3, int var4, int var5) {
      if (com.larpclient.f.Y()) {
         tl.j(var0, var1, var2, var3, var4, var5, 30.0F, _3.U(com.larpclient.f.b, 0.25), com.larpclient.f.b);
         tl.j(var0, var1, var2, var3, var4, var5, 6.0F, _3.U(com.larpclient.f.b, 0.45), _3.U(com.larpclient.f.b, 0.7));
      } else {
         for (byte var6 = 8; var6 >= 2; var6 -= 2) {
            b(var0, var1 - var6, var2 - var6 + 5, var3 + var6 * 2, var4 + var6 * 2, com.larpclient.f.r + var6, 150994944);
         }
      }
   }
}
