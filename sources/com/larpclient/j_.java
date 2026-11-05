package com.larpclient;

import java.util.Arrays;
import java.util.Locale;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.FontDescription.Resource;
import net.minecraft.resources.Identifier;

public final class j_ {
   private static int F;
   private static final ot T = new ot();
   private static final Style[] s = Arrays.stream(j_.w.values())
      .map(var0 -> Style.EMPTY.withFont(new Resource(Identifier.fromNamespaceAndPath("larp-client", var0.name().toLowerCase(Locale.ROOT)))))
      .toArray(Style[]::new);
   private static final Style G = Style.EMPTY.withFont(new Resource(Identifier.fromNamespaceAndPath("larp-client", "world_semibold")));
   private static int Z;

   public static int g() {
      return F;
   }

   public static void H() {
      T.e(null, -1);
      F++;
   }

   public static int o(String var0, j_.m var1) {
      return S() ? E(var0, var1.p) : (int)Math.ceil(D(var0, var1.D) * var1.h);
   }

   public static String d(String var0, int var1, j_.m var2) {
      return S() ? Y(var0, var1, var2.p) : V(var0, Math.max(0, (int)Math.floor(var1 / var2.h)), var2.D);
   }

   public static void L(GuiGraphicsExtractor var0, String var1, int var2, int var3, int var4, j_.m var5) {
      if (S()) {
         y(var0, var1, var2, var3, var4, var5.p);
      } else if (var5.h == 1.0F) {
         x(var0, var1, var2, var3, var4, var5.D);
      } else {
         var0.pose().pushMatrix();

         try {
            var0.pose().translate(var2, var3);
            var0.pose().scale(var5.h, var5.h);
            x(var0, var1, 0, 0, var4, var5.D);
         } finally {
            var0.pose().popMatrix();
         }
      }
   }

   public static Component P(String var0) {
      return Component.literal(var0).setStyle(G);
   }

   public static j_.j v() {
      Z++;
      return () -> Z--;
   }

   private static boolean S() {
      if (Z > 0) {
         return true;
      }

      Minecraft var0 = Minecraft.getInstance();
      return var0 != null && var0.gui != null && var0.gui.screen() instanceof p0;
   }

   private static j_.w v(j_.w var0) {
      if (!S()) {
         return var0;
      }

      return switch (var0) {
         case H -> j_.w.t;
         case Q -> j_.w.k;
         case P -> j_.w.k;
         default -> var0;
      };
   }

   public static int a(String var0) {
      return D(var0, j_.w.H);
   }

   public static int D(String var0, j_.w var1) {
      return E(var0, v(var1));
   }

   public static void E(GuiGraphicsExtractor var0, String var1, int var2, int var3, int var4) {
      x(var0, var1, var2, var3, var4, j_.w.H);
   }

   public static void x(GuiGraphicsExtractor var0, String var1, int var2, int var3, int var4, j_.w var5) {
      y(var0, var1, var2, var3, var4, v(var5));
   }

   public static String S(String var0, int var1) {
      return V(var0, var1, j_.w.H);
   }

   public static String V(String var0, int var1, j_.w var2) {
      return Y(var0, var1, v(var2));
   }

   public static Component v(String var0, j_.w var1) {
      return Component.literal(var0).setStyle(s[var1.ordinal()]);
   }

   public static int L(String var0) {
      return E(var0, j_.w.H);
   }

   public static int E(String var0, j_.w var1) {
      Font var2 = Minecraft.getInstance().font;
      T.e(var2, jk.v);
      return T.b(var0, var1.ordinal(), () -> jn.x(var1, var0) ? jn.d(var0, var1) : var2.width(v(var0, var1)));
   }

   public static void c(GuiGraphicsExtractor var0, String var1, int var2, int var3, int var4) {
      y(var0, var1, var2, var3, var4, j_.w.H);
   }

   public static void y(GuiGraphicsExtractor var0, String var1, int var2, int var3, int var4, j_.w var5) {
      var4 = r.D(var4);
      if (var4 >>> 24 >= 4) {
         if (!jn.A(var0, var1, var2, var3, var4, var5)) {
            var0.text(Minecraft.getInstance().font, v(var1, var5), var2, var3, var4, false);
         }
      }
   }

   public static void Q(GuiGraphicsExtractor var0, String var1, int var2, int var3, int var4, int var5, float var6, j_.w var7) {
      var4 = r.D(var4);
      var5 = r.D(var5);
      if (var4 >>> 24 >= 4) {
         if (!jn.M(var0, var1, var2, var3, var4, var7, var5, var6)) {
            if (var5 >>> 24 >= 4 && var6 > 0.0F) {
               for (int var8 = 0; var8 < 8; var8++) {
                  double var9 = var8 * Math.PI / 4.0;
                  var0.pose().pushMatrix();

                  try {
                     var0.pose().translate((float)(Math.cos(var9) * var6), (float)(Math.sin(var9) * var6));
                     var0.text(Minecraft.getInstance().font, v(var1, var7), var2, var3, var5, false);
                  } finally {
                     var0.pose().popMatrix();
                  }
               }
            }

            var0.text(Minecraft.getInstance().font, v(var1, var7), var2, var3, var4, false);
         }
      }
   }

   public static void d(GuiGraphicsExtractor var0, String var1, int var2, int var3, int var4, float var5, j_.w var6) {
      var4 = r.D(var4);
      if (var4 >>> 24 >= 2) {
         jn.Z(var0, var1, var2, var3, var4, var6, var5);
      }
   }

   public static float U(String var0, j_.w var1) {
      return jn.x(var1, var0) ? jn.l(var0, var1) : Minecraft.getInstance().font.getSplitter().stringWidth(v(var0, var1));
   }

   public static void n(GuiGraphicsExtractor var0, String var1, int var2, int var3, int var4) {
      c(var0, var1, var2 - L(var1) / 2, var3, var4);
   }

   public static String x(String var0, int var1) {
      return Y(var0, var1, j_.w.H);
   }

   public static String Y(String var0, int var1, j_.w var2) {
      T.e(Minecraft.getInstance().font, jk.v);
      return T.w(var0, var2.ordinal(), var1, () -> {
         if (E(var0, var2) <= var1) {
            return var0;
         }

         int var3 = var0.length();

         while (var3 > 0 && E(var0.substring(0, var3) + "…", var2) > var1) {
            var3 = var0.offsetByCodePoints(var3, -1);
         }

         return var3 == 0 ? "" : var0.substring(0, var3) + "…";
      });
   }

   static {
      r4.V(
         "Font",
         "Inter UI/HUD and ClickUI faces registered at native pixel sizes per role; ClickUI text is rasterized at the on-screen pixel size, other text by Minecraft"
      );
   }

   public interface j extends AutoCloseable {
      @Override
      void close();
   }

   public enum m {
      g(j_.w.P, 1.2F, j_.w.b),
      a(j_.w.P, 1.15F, j_.w.T),
      d(j_.w.Q, 1.0F, j_.w.F),
      r(j_.w.H, 1.0F, j_.w.r),
      Q(j_.w.Q, 0.9F, j_.w.f),
      n(j_.w.Q, 0.9F, j_.w.j),
      U(j_.w.H, 0.85F, j_.w.K),
      u(j_.w.P, 0.8F, j_.w.D);

      public final j_.w D;
      public final float h;
      final j_.w p;

      m(j_.w var3, float var4, j_.w var5) {
         this.D = var3;
         this.h = var4;
         this.p = var5;
      }

      public int C() {
         return (int)Math.ceil(9.0F * this.h);
      }
   }

   public enum w {
      H,
      Q,
      P,
      G,
      Y,
      E,
      t,
      k,
      b,
      T,
      F,
      r,
      f,
      j,
      K,
      D;
   }
}
