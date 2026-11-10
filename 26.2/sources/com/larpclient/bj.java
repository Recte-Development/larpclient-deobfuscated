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

public final class bj {
   private static int w;
   private static final kk R = new kk();
   private static final Style[] z = Arrays.stream(bj.j.values())
      .map(var0 -> Style.EMPTY.withFont(new Resource(Identifier.fromNamespaceAndPath("larp-client", var0.name().toLowerCase(Locale.ROOT)))))
      .toArray(Style[]::new);
   private static final Style A = Style.EMPTY.withFont(new Resource(Identifier.fromNamespaceAndPath("larp-client", "world_semibold")));
   private static int d;

   public static int X() {
      return w;
   }

   public static void I() {
      R.j(null, -1);
      w++;
   }

   public static int e(String var0, bj.u var1) {
      return V() ? G(var0, var1.a) : (int)Math.ceil(x(var0, var1.D) * var1.x);
   }

   public static String i(String var0, int var1, bj.u var2) {
      return V() ? v(var0, var1, var2.a) : o(var0, Math.max(0, (int)Math.floor(var1 / var2.x)), var2.D);
   }

   public static void k(GuiGraphicsExtractor var0, String var1, int var2, int var3, int var4, bj.u var5) {
      if (V()) {
         z(var0, var1, var2, var3, var4, var5.a);
      } else if (var5.x == 1.0F) {
         J(var0, var1, var2, var3, var4, var5.D);
      } else {
         var0.pose().pushMatrix();

         try {
            var0.pose().translate(var2, var3);
            var0.pose().scale(var5.x, var5.x);
            J(var0, var1, 0, 0, var4, var5.D);
         } finally {
            var0.pose().popMatrix();
         }
      }
   }

   public static Component h(String var0) {
      return Component.literal(var0).setStyle(A);
   }

   public static bj.a y() {
      d++;
      return () -> d--;
   }

   private static boolean V() {
      if (d > 0) {
         return true;
      }

      Minecraft var0 = Minecraft.getInstance();
      return var0 != null && var0.gui != null && var0.gui.screen() instanceof hf;
   }

   private static bj.j l(bj.j var0) {
      if (!V()) {
         return var0;
      }

      return switch (var0) {
         case c -> bj.j.Q;
         case T -> bj.j.y;
         case w -> bj.j.y;
         default -> var0;
      };
   }

   public static int u(String var0) {
      return x(var0, bj.j.c);
   }

   public static int x(String var0, bj.j var1) {
      return G(var0, l(var1));
   }

   public static void E(GuiGraphicsExtractor var0, String var1, int var2, int var3, int var4) {
      J(var0, var1, var2, var3, var4, bj.j.c);
   }

   public static void J(GuiGraphicsExtractor var0, String var1, int var2, int var3, int var4, bj.j var5) {
      z(var0, var1, var2, var3, var4, l(var5));
   }

   public static String T(String var0, int var1) {
      return o(var0, var1, bj.j.c);
   }

   public static String o(String var0, int var1, bj.j var2) {
      return v(var0, var1, l(var2));
   }

   public static Component n(String var0, bj.j var1) {
      return Component.literal(var0).setStyle(z[var1.ordinal()]);
   }

   public static int q(String var0) {
      return G(var0, bj.j.c);
   }

   public static int G(String var0, bj.j var1) {
      Font var2 = Minecraft.getInstance().font;
      R.j(var2, wa.s);
      return R.R(var0, var1.ordinal(), () -> m.x(var1, var0) ? m.G(var0, var1) : var2.width(n(var0, var1)));
   }

   public static void h(GuiGraphicsExtractor var0, String var1, int var2, int var3, int var4) {
      z(var0, var1, var2, var3, var4, bj.j.c);
   }

   public static void z(GuiGraphicsExtractor var0, String var1, int var2, int var3, int var4, bj.j var5) {
      var4 = ua.I(var4);
      if (var4 >>> 24 >= 4) {
         if (!m.C(var0, var1, var2, var3, var4, var5)) {
            var0.text(Minecraft.getInstance().font, n(var1, var5), var2, var3, var4, false);
         }
      }
   }

   public static void Q(GuiGraphicsExtractor var0, String var1, int var2, int var3, int var4, int var5, float var6, bj.j var7) {
      var4 = ua.I(var4);
      var5 = ua.I(var5);
      if (var4 >>> 24 >= 4) {
         if (!m.C(var0, var1, var2, var3, var4, var7, var5, var6)) {
            if (var5 >>> 24 >= 4 && var6 > 0.0F) {
               for (int var8 = 0; var8 < 8; var8++) {
                  double var9 = var8 * Math.PI / 4.0;
                  var0.pose().pushMatrix();

                  try {
                     var0.pose().translate((float)(Math.cos(var9) * var6), (float)(Math.sin(var9) * var6));
                     var0.text(Minecraft.getInstance().font, n(var1, var7), var2, var3, var5, false);
                  } finally {
                     var0.pose().popMatrix();
                  }
               }
            }

            var0.text(Minecraft.getInstance().font, n(var1, var7), var2, var3, var4, false);
         }
      }
   }

   public static void T(GuiGraphicsExtractor var0, String var1, int var2, int var3, int var4, float var5, bj.j var6) {
      var4 = ua.I(var4);
      if (var4 >>> 24 >= 2) {
         m.r(var0, var1, var2, var3, var4, var6, var5);
      }
   }

   public static float j(String var0, bj.j var1) {
      return m.x(var1, var0) ? m.O(var0, var1) : Minecraft.getInstance().font.getSplitter().stringWidth(n(var0, var1));
   }

   public static void n(GuiGraphicsExtractor var0, String var1, int var2, int var3, int var4) {
      h(var0, var1, var2 - q(var1) / 2, var3, var4);
   }

   public static String G(String var0, int var1) {
      return v(var0, var1, bj.j.c);
   }

   public static String v(String var0, int var1, bj.j var2) {
      R.j(Minecraft.getInstance().font, wa.s);
      return R.l(var0, var2.ordinal(), var1, () -> {
         if (G(var0, var2) <= var1) {
            return var0;
         }

         int var3 = var0.length();

         while (var3 > 0 && G(var0.substring(0, var3) + "…", var2) > var1) {
            var3 = var0.offsetByCodePoints(var3, -1);
         }

         return var3 == 0 ? "" : var0.substring(0, var3) + "…";
      });
   }

   static {
      h.A(
         "Font",
         "Inter UI/HUD and ClickUI faces registered at native pixel sizes per role; ClickUI text is rasterized at the on-screen pixel size, other text by Minecraft"
      );
   }

   public interface a extends AutoCloseable {
      @Override
      void close();
   }

   public enum j {
      c,
      T,
      w,
      N,
      M,
      z,
      Q,
      y,
      b,
      X,
      f,
      t,
      P,
      H,
      m,
      p;
   }

   public enum u {
      S(bj.j.w, 1.2F, bj.j.b),
      N(bj.j.w, 1.15F, bj.j.X),
      i(bj.j.T, 1.0F, bj.j.f),
      X(bj.j.c, 1.0F, bj.j.t),
      I(bj.j.T, 0.9F, bj.j.P),
      y(bj.j.T, 0.9F, bj.j.H),
      M(bj.j.c, 0.85F, bj.j.m),
      m(bj.j.w, 0.8F, bj.j.p);

      public final bj.j D;
      public final float x;
      final bj.j a;

      u(bj.j var3, float var4, bj.j var5) {
         this.D = var3;
         this.x = var4;
         this.a = var5;
      }

      public int n() {
         return (int)Math.ceil(9.0F * this.x);
      }
   }
}
