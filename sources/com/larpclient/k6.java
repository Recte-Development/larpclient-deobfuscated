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

public final class k6 {
   private static int z;
   private static final hv Z = new hv();
   private static final Style[] E = Arrays.stream(k6.v.values())
      .map(var0 -> Style.EMPTY.withFont(new Resource(Identifier.fromNamespaceAndPath("larp-client", var0.name().toLowerCase(Locale.ROOT)))))
      .toArray(Style[]::new);
   private static final Style Q = Style.EMPTY.withFont(new Resource(Identifier.fromNamespaceAndPath("larp-client", "world_semibold")));
   private static int M;

   public static int O() {
      return z;
   }

   public static void V() {
      Z.k(null, -1);
      z++;
   }

   public static int a(String var0, k6.d var1) {
      return Q() ? w(var0, var1.M) : (int)Math.ceil(z(var0, var1.P) * var1.H);
   }

   public static String o(String var0, int var1, k6.d var2) {
      return Q() ? Q(var0, var1, var2.M) : q(var0, Math.max(0, (int)Math.floor(var1 / var2.H)), var2.P);
   }

   public static void T(GuiGraphicsExtractor var0, String var1, int var2, int var3, int var4, k6.d var5) {
      if (Q()) {
         r(var0, var1, var2, var3, var4, var5.M);
      } else if (var5.H == 1.0F) {
         M(var0, var1, var2, var3, var4, var5.P);
      } else {
         var0.pose().pushMatrix();

         try {
            var0.pose().translate(var2, var3);
            var0.pose().scale(var5.H, var5.H);
            M(var0, var1, 0, 0, var4, var5.P);
         } finally {
            var0.pose().popMatrix();
         }
      }
   }

   public static Component I(String var0) {
      return Component.literal(var0).setStyle(Q);
   }

   public static k6.l F() {
      M++;
      return () -> M--;
   }

   private static boolean Q() {
      if (M > 0) {
         return true;
      }

      Minecraft var0 = Minecraft.getInstance();
      return var0 != null && var0.gui != null && var0.gui.screen() instanceof hw;
   }

   private static k6.v X(k6.v var0) {
      if (!Q()) {
         return var0;
      }

      return switch (var0) {
         case K -> k6.v.M;
         case B -> k6.v.v;
         case L -> k6.v.v;
         default -> var0;
      };
   }

   public static int P(String var0) {
      return z(var0, k6.v.K);
   }

   public static int z(String var0, k6.v var1) {
      return w(var0, X(var1));
   }

   public static void Z(GuiGraphicsExtractor var0, String var1, int var2, int var3, int var4) {
      M(var0, var1, var2, var3, var4, k6.v.K);
   }

   public static void M(GuiGraphicsExtractor var0, String var1, int var2, int var3, int var4, k6.v var5) {
      r(var0, var1, var2, var3, var4, X(var5));
   }

   public static String j(String var0, int var1) {
      return q(var0, var1, k6.v.K);
   }

   public static String q(String var0, int var1, k6.v var2) {
      return Q(var0, var1, X(var2));
   }

   public static Component X(String var0, k6.v var1) {
      return Component.literal(var0).setStyle(E[var1.ordinal()]);
   }

   public static int x(String var0) {
      return w(var0, k6.v.K);
   }

   public static int w(String var0, k6.v var1) {
      Font var2 = Minecraft.getInstance().font;
      Z.k(var2, m5.k);
      return Z.i(var0, var1.ordinal(), () -> zh.y(var1, var0) ? zh.Y(var0, var1) : var2.width(X(var0, var1)));
   }

   public static void G(GuiGraphicsExtractor var0, String var1, int var2, int var3, int var4) {
      r(var0, var1, var2, var3, var4, k6.v.K);
   }

   public static void r(GuiGraphicsExtractor var0, String var1, int var2, int var3, int var4, k6.v var5) {
      var4 = de.h(var4);
      if (var4 >>> 24 >= 4) {
         if (!zh.Y(var0, var1, var2, var3, var4, var5)) {
            var0.text(Minecraft.getInstance().font, X(var1, var5), var2, var3, var4, false);
         }
      }
   }

   public static void s(GuiGraphicsExtractor var0, String var1, int var2, int var3, int var4, int var5, float var6, k6.v var7) {
      var4 = de.h(var4);
      var5 = de.h(var5);
      if (var4 >>> 24 >= 4) {
         if (!zh.C(var0, var1, var2, var3, var4, var7, var5, var6)) {
            if (var5 >>> 24 >= 4 && var6 > 0.0F) {
               for (int var8 = 0; var8 < 8; var8++) {
                  double var9 = var8 * Math.PI / 4.0;
                  var0.pose().pushMatrix();

                  try {
                     var0.pose().translate((float)(Math.cos(var9) * var6), (float)(Math.sin(var9) * var6));
                     var0.text(Minecraft.getInstance().font, X(var1, var7), var2, var3, var5, false);
                  } finally {
                     var0.pose().popMatrix();
                  }
               }
            }

            var0.text(Minecraft.getInstance().font, X(var1, var7), var2, var3, var4, false);
         }
      }
   }

   public static void Z(GuiGraphicsExtractor var0, String var1, int var2, int var3, int var4, float var5, k6.v var6) {
      var4 = de.h(var4);
      if (var4 >>> 24 >= 2) {
         zh.j(var0, var1, var2, var3, var4, var6, var5);
      }
   }

   public static float u(String var0, k6.v var1) {
      return zh.y(var1, var0) ? zh.x(var0, var1) : Minecraft.getInstance().font.getSplitter().stringWidth(X(var0, var1));
   }

   public static void M(GuiGraphicsExtractor var0, String var1, int var2, int var3, int var4) {
      G(var0, var1, var2 - x(var1) / 2, var3, var4);
   }

   public static String n(String var0, int var1) {
      return Q(var0, var1, k6.v.K);
   }

   public static String Q(String var0, int var1, k6.v var2) {
      Z.k(Minecraft.getInstance().font, m5.k);
      return Z.I(var0, var2.ordinal(), var1, () -> {
         if (w(var0, var2) <= var1) {
            return var0;
         }

         int var3 = var0.length();

         while (var3 > 0 && w(var0.substring(0, var3) + "…", var2) > var1) {
            var3 = var0.offsetByCodePoints(var3, -1);
         }

         return var3 == 0 ? "" : var0.substring(0, var3) + "…";
      });
   }

   static {
      mt.K(
         "Font",
         "Inter UI/HUD and ClickUI faces registered at native pixel sizes per role; ClickUI text is rasterized at the on-screen pixel size, other text by Minecraft"
      );
   }

   public enum d {
      y(k6.v.L, 1.2F, k6.v.t),
      m(k6.v.L, 1.15F, k6.v.P),
      a(k6.v.B, 1.0F, k6.v.S),
      C(k6.v.K, 1.0F, k6.v.R),
      F(k6.v.B, 0.9F, k6.v.e),
      X(k6.v.B, 0.9F, k6.v.W),
      K(k6.v.K, 0.85F, k6.v.Y),
      q(k6.v.L, 0.8F, k6.v.N);

      public final k6.v P;
      public final float H;
      final k6.v M;

      d(k6.v var3, float var4, k6.v var5) {
         this.P = var3;
         this.H = var4;
         this.M = var5;
      }

      public int q() {
         return (int)Math.ceil(9.0F * this.H);
      }
   }

   public interface l extends AutoCloseable {
      @Override
      void close();
   }

   public enum v {
      K,
      B,
      L,
      A,
      U,
      k,
      M,
      v,
      t,
      P,
      S,
      R,
      e,
      W,
      Y,
      N;
   }
}
