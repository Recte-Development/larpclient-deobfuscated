package com.larpclient;

import com.larpclient.module.HUDModule;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.PlayerFaceExtractor;
import net.minecraft.resources.Identifier;

public final class mn {
   public static final int n = 17;
   public static final int r = 172;
   public static final int t = 72;
   public static final int B = 42;
   public static final int Q = 6;
   public static final int U = 30;
   private static final int H = 9;
   private static final int a = 12;
   private static final int k = 14;
   private static final int c = 16;
   private static final int F = 11;
   private static final double w = 22.0;
   private static final double V = 4.0;
   private static final double m = 6.0;
   public static final double b = 0.09;
   public static final double K = 0.12;
   public static final int M = 30;
   public static final int v = 12;
   public static final int l = 160;
   public static final int f = 38;
   public static final int o = 63;
   public static final double h = 6.0;
   private static double x = 6.0;
   private static long X;

   private mn() {
   }

   public static int T(int var0) {
      return var0 + 12;
   }

   public static int W() {
      return -267645934;
   }

   public static int i() {
      return com.larpclient.f.C;
   }

   public static void C(GuiGraphicsExtractor var0, String var1, int var2, int var3, int var4, boolean var5) {
      int var6 = T(var2);
      int var7 = var3 - var6;
      c7.s(var0, var7, var4, var6, 17, W());
      Z(var0, var7, var4, var6, 17, var5);
      X(var0, var7, var4, var6, 17, 0);
      c7.l(var0, var5 ? var3 - 2 : var7, var4, 2, 17, com.larpclient.f.C, _3.D(com.larpclient.f.C, -1, 0.45));
      we.W(var0, var1, var7 + (var5 ? 5 : 7), ew.N(var4, 17.0, 13.0), com.larpclient.f.C);
   }

   private static List<mn.j> E(String var0, String var1, String var2, String var3) {
      HUDModule var4 = HUDModule.U();
      ArrayList var5 = new ArrayList();
      if (var4 == null || var4.U.P()) {
         var5.add(new mn.j(var0, mn.z.D));
      }

      if (var4 == null || var4.y.P()) {
         var5.add(new mn.j(var1, mn.z.X));
      }

      if (var4 == null || var4.n.P()) {
         var5.add(new mn.j(var2, mn.z.C));
      }

      if (!var3.isEmpty() && (var4 == null || var4.t.P())) {
         var5.add(new mn.j(var3, mn.z.e));
      }

      return var5;
   }

   public static int v(int... var0) {
      return var0.length == 0 ? 42 : 72 + Arrays.stream(var0).sum() + (var0.length - 1) * 14;
   }

   public static int X(String var0, String var1, String var2, String var3) {
      return v(E(var0, var1, var2, var3).stream().mapToInt(mn.j::width).toArray());
   }

   public static void E(GuiGraphicsExtractor var0, int var1, int var2, int var3, String var4, String var5, String var6, String var7) {
      HUDModule var8 = HUDModule.U();
      int var9 = var8 != null && var8.I.S("Right") ? 1 : 0;
      double var10 = System.nanoTime() / 1.0E9;
      int var12 = var9 != 0 ? var1 + var3 - 42 : var1;
      c7.n(var0, var12, var2, 42.0F, 42.0F, 9.0F, W());
      b(var0, var12, var2, 42.0F, 42.0F, 9.0F);
      X(var0, var12, var2, 42, 42, 9);
      if (!ew.r(var0, "logo", var12 + 5, var2 + 5, 32.0, -1)) {
         c7.o(var0, Identifier.fromNamespaceAndPath("larp-client", "textures/gui/logo.png"), var12 + 5, var2 + 5, 32, 32, 64, 64);
      }

      List var13 = E(var4, var5, var6, var7);
      if (!var13.isEmpty()) {
         int var14 = var9 != 0 ? var1 : var1 + 42 + 6;
         int var15 = var3 - 42 - 6;
         int var16 = var2 + 6;
         c7.n(var0, var14, var16, var15, 30.0F, 6.0F, W());
         b(var0, var14, var16, var15, 30.0F, 6.0F);
         X(var0, var14, var16, var15, 30, 6);
         int var17 = var14 + 12;
         double var18 = ew.M(var16, 30.0, 13.0);

         for (mn.j var21 : var13) {
            int var22 = var17;
            switch (var21.kind()) {
               case D:
                  if (Minecraft.getInstance().player != null) {
                     PlayerFaceExtractor.extractRenderState(var0, Minecraft.getInstance().player.getSkin(), var17, var16 + 7, 16);
                  }

                  var17 += 22;
                  break;
               case C:
                  f(var0, var17, var16 + 8, var10);
                  var17 += 17;
            }

            if (var21.kind() == mn.z.e) {
               S(var0, var21.label(), var17, var18, var10);
            } else {
               we.F(var0, var21.label(), var17, var18, 13.0, -1);
            }

            var17 = var22 + var21.width() + 14;
         }
      }
   }

   private static void f(GuiGraphicsExtractor var0, int var1, int var2, double var3) {
      double[] var5 = new double[]{5.2, 7.1, 4.3};
      double[] var6 = new double[]{0.0, 1.7, 3.1};

      for (int var7 = 0; var7 < 3; var7++) {
         int var8 = 4 + (int)Math.round(10.0 * (0.5 + 0.5 * Math.sin(var3 * var5[var7] + var6[var7])));
         tl.g(var0, var1 + var7 * 4, var2 + 14 - var8, 3.0F, var8, 1.0F, com.larpclient.f.C);
      }
   }

   private static void S(GuiGraphicsExtractor var0, String var1, int var2, double var3, double var5) {
      int var7 = var1.indexOf(58);
      if (var7 < 0) {
         we.F(var0, var1, var2, var3, 13.0, -1);
      } else {
         String var8 = var1.substring(0, var7 + 1);
         double var9 = 0.55 + 0.45 * Math.cos(var5 * Math.PI * 2.0);
         we.F(var0, var1.substring(0, var7), var2, var3, 13.0, -1);
         we.F(var0, ":", var2 + we.V(var1.substring(0, var7), 13.0), var3, 13.0, _3.U(-1, var9));
         we.F(var0, var1.substring(var7 + 1), var2 + we.V(var8, 13.0), var3, 13.0, -1);
      }
   }

   public static void X(GuiGraphicsExtractor var0, int var1, int var2, int var3, int var4, int var5) {
      r(var0, var1, var2, var3, var4, var5, 1.0);
   }

   public static void r(GuiGraphicsExtractor var0, int var1, int var2, int var3, int var4, int var5, double var6) {
      HUDModule var8 = HUDModule.U();
      if ((var8 == null || var8.L()) && !(var6 <= 0.01)) {
         double var9 = System.nanoTime() / 1.0E9 % 6.0 / 4.0;
         float var11 = var0.pose().m00();
         if (!(var9 >= 1.0) && !(var11 <= 0.0F)) {
            double var12 = -22.0 + var9 * (var0.guiWidth() + 44.0);
            double var14 = (var12 - var0.pose().m20()) / var11;
            double var16 = 11.0 / var11;
            int var18 = var1 + var5;
            int var19 = var1 + var3 - var5;
            if (!(var14 + var16 <= var18) && !(var14 - var16 >= var19)) {
               int var20 = (int)Math.round(var14 - var16);
               int var21 = Math.max(1, (int)Math.round(var16));
               int var22 = _3.U(251658239, var6);
               var0.enableScissor(var18, var2, var19, var2 + var4);

               try {
                  tl.A(var0, var20, var2, var21, var4, 0.0F, var22 & 16777215, var22);
                  tl.A(var0, var20 + var21, var2, var21, var4, 0.0F, var22, var22 & 16777215);
               } finally {
                  var0.disableScissor();
               }
            }
         }
      }
   }

   public static void b(GuiGraphicsExtractor var0, float var1, float var2, float var3, float var4, float var5) {
      HUDModule var6 = HUDModule.U();
      if ((var6 == null || var6.F()) && !(var3 <= 0.0F) && !(var4 <= 0.0F)) {
         double var7 = com.larpclient.f.U();
         int var9 = com.larpclient.f.C;
         int var10 = _3.D(com.larpclient.f.C, com.larpclient.f.O, 0.5);
         tl.A(var0, var1, var2, var3, var4, var5, _3.A(var9, 0.09 * var7), _3.A(var10, 0.0));
         int var11 = _3.A(var9, 0.12 * var7);
         tl.V(var0, var1, var2, var3, var4, var5, var11, var11);
      }
   }

   public static void Z(GuiGraphicsExtractor var0, int var1, int var2, int var3, int var4, boolean var5) {
      HUDModule var6 = HUDModule.U();
      if ((var6 == null || var6.F()) && var3 > 0 && var4 > 0) {
         int var7 = _3.A(com.larpclient.f.C, 0.09 * com.larpclient.f.U());
         int var8 = var7 & 16777215;
         tl.A(var0, var1, var2, var3, var4, 0.0F, var5 ? var8 : var7, var5 ? var7 : var8);
      }
   }

   public static int R(String var0) {
      return 12 + we.V("XYZ " + var0, 15.0);
   }

   public static void u(GuiGraphicsExtractor var0, int var1, int var2, int var3, String var4) {
      c7.n(var0, var1, var2, var3, 27.0F, 6.0F, W());
      b(var0, var1, var2, var3, 27.0F, 6.0F);
      X(var0, var1, var2, var3, 27, 6);
      double var5 = ew.N(var2, 27.0, 15.0);
      we.F(var0, "XYZ", var1 + 6, var5, 15.0, com.larpclient.f.C);
      we.F(var0, " " + var4, var1 + 6 + we.V("XYZ", 15.0), var5, 15.0, -1);
   }

   public static String i(double var0) {
      return String.format(Locale.ROOT, "%.2f bps", var0);
   }

   public static void W(GuiGraphicsExtractor var0, int var1, int var2, zk var3, boolean var4) {
      HUDModule var5 = HUDModule.U();
      int var6 = var5 == null ? W() : var5.T.P();
      int var7 = var5 == null ? -1374744817 : var5.r.P();
      int var8 = var5 == null ? com.larpclient.f.C : var5.j.P();
      var0.enableScissor(var1, var2 + 30, var1 + 172, var2 + 72);
      c7.n(var0, var1, var2, 172.0F, 72.0F, 12.0F, var7);
      var0.disableScissor();
      var0.enableScissor(var1, var2, var1 + 172, var2 + 30);
      c7.n(var0, var1, var2, 172.0F, 72.0F, 12.0F, var6);
      var0.disableScissor();
      b(var0, var1, var2, 172.0F, 72.0F, 12.0F);
      X(var0, var1, var2, 172, 72, 12);
      we.F(var0, "Speed", var1 + 12, ew.N(var2, 30.0, 13.0), 13.0, u(var6));
      double var9 = var4 ? 4.3 : var3.U();
      String var11 = String.format(Locale.ROOT, "%.2f", var9);
      String var12 = " bps";
      double var13 = ew.N(var2, 30.0, 13.0);
      double var15 = var13 + 1.6545454263687132;
      int var17 = we.V(var12, 10.4);
      int var18 = var1 + 172 - 12;
      we.F(var0, var11, var18 - var17 - we.V(var11, 13.0), var13, 13.0, u(var6));
      we.F(var0, var12, var18 - var17, var15, 10.4, _3.U(u(var6), 0.55));
      int var19 = var4 ? 40 : Math.min(40, var3.y());
      double var20 = 0.0;

      for (int var22 = 0; var22 < var19; var22++) {
         var20 = Math.max(var20, g(var3, var4, var22, var19));
      }

      double var44 = i(var20, var4);
      int var24 = var1 + 12;
      short var25 = 148;
      int var26 = var2 + 63;
      int var27 = Math.round(f(var2, var44 / 2.0, var44));

      for (byte var28 = 0; var28 < var25; var28 += 6) {
         c7.s(var0, var24 + var28, var27, Math.min(3, var25 - var28), 1, _3.U(var8, 0.1));
      }

      c7.s(var0, var24, var26, var25, 1, _3.U(var8, 0.16));
      if (var19 >= 2) {
         for (int var45 = 0; var45 <= var25; var45++) {
            float var29 = f(var2, V(var3, var4, var19, (double)var45 / var25), var44);
            int var30 = Math.round(var29);
            if (var26 - var30 > 0) {
               c7.l(var0, var24 + var45, var30, 1, var26 - var30, _3.U(var8, 0.26), _3.U(var8, 0.0));
            }
         }

         float var46 = var24;
         float var47 = f(var2, g(var3, var4, 0, var19), var44);

         for (int var48 = 0; var48 + 1 < var19; var48++) {
            double var31 = g(var3, var4, Math.max(0, var48 - 1), var19);
            double var33 = g(var3, var4, var48, var19);
            double var35 = g(var3, var4, var48 + 1, var19);
            double var37 = g(var3, var4, Math.min(var19 - 1, var48 + 2), var19);

            for (int var39 = 1; var39 <= 4; var39++) {
               double var40 = var39 / 4.0;
               float var42 = var24 + (float)((var48 + var40) * var25 / (var19 - 1));
               float var43 = f(var2, _h.Q(var31, var33, var35, var37, var40), var44);
               var0.pose().pushMatrix();
               var0.pose().translate(var46, var47).rotate((float)Math.atan2(var43 - var47, var42 - var46));
               tl.g(var0, 0.0F, -0.25F, (float)Math.hypot(var42 - var46, var43 - var47) + 0.5F, 1.5F, 0.75F, var8);
               var0.pose().popMatrix();
               var46 = var42;
               var47 = var43;
            }
         }

         tl.g(var0, var46 - 3.5F, var47 - 3.5F, 7.0F, 7.0F, 3.5F, _3.U(var8, 0.25));
         tl.g(var0, var46 - 2.0F, var47 - 2.0F, 4.0F, 4.0F, 2.0F, var8);
      }
   }

   private static double V(zk var0, boolean var1, int var2, double var3) {
      double var5 = Math.clamp(var3, 0.0, 1.0) * (var2 - 1);
      int var7 = Math.min(var2 - 2, (int)Math.floor(var5));
      return _h.Q(
         g(var0, var1, Math.max(0, var7 - 1), var2),
         g(var0, var1, var7, var2),
         g(var0, var1, var7 + 1, var2),
         g(var0, var1, Math.min(var2 - 1, var7 + 2), var2),
         var5 - var7
      );
   }

   private static double i(double var0, boolean var2) {
      double var3 = Math.max(6.0, var0 * 1.15);
      if (var2) {
         return var3;
      }

      long var5 = System.nanoTime();
      double var7 = X == 0L ? 1.0 : Math.clamp((var5 - X) / 1.0E9, 0.0, 0.25);
      X = var5;
      x = x + (var3 - x) * (1.0 - Math.exp(-6.0 * var7));
      return Math.max(x, var0);
   }

   private static double g(zk var0, boolean var1, int var2, int var3) {
      return var1 ? (Math.sin(var2 * 0.18) + 1.0) * 2.2 : (var3 == 0 ? 0.0 : var0.D(var0.y() - var3 + var2));
   }

   public static float k(int var0, double var1) {
      return f(var0, var1, 6.0);
   }

   public static float f(int var0, double var1, double var3) {
      return var0 + 63 - (float)(Math.clamp(var1 / Math.max(0.01, var3), 0.0, 1.0) * 25.0);
   }

   public static int u(int var0) {
      double var1 = (var0 >> 16 & 0xFF) * 0.2126 + (var0 >> 8 & 0xFF) * 0.7152 + (var0 & 0xFF) * 0.0722;
      return var1 > 150.0 ? -15197408 : -986637;
   }

   private record j(String label, mn.z kind) {
      int width() {
         int var1 = we.V(this.label, 13.0);

         return switch (this.kind) {
            case D -> 22 + var1;
            case C -> 17 + var1;
            default -> var1;
         };
      }
   }

   private enum z {
      D,
      X,
      C,
      e;
   }
}
