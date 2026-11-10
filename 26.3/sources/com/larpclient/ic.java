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

public final class ic {
   private static int N;
   private static final it Q = new it();
   private static final Style[] W = Arrays.stream(ic.b.values())
      .map(var0 -> Style.EMPTY.withFont(new Resource(Identifier.fromNamespaceAndPath("larp-client", var0.name().toLowerCase(Locale.ROOT)))))
      .toArray(Style[]::new);
   private static final Style n = Style.EMPTY.withFont(new Resource(Identifier.fromNamespaceAndPath("larp-client", "world_semibold")));
   private static int o;

   public static int l() {
      return N;
   }

   public static void C() {
      Q.x(null, -1);
      N++;
   }

   public static int f(String var0, ic.n var1) {
      return j() ? z(var0, var1.u) : (int)Math.ceil(U(var0, var1.P) * var1.z);
   }

   public static String o(String var0, int var1, ic.n var2) {
      return j() ? P(var0, var1, var2.u) : N(var0, Math.max(0, (int)Math.floor(var1 / var2.z)), var2.P);
   }

   public static void N(GuiGraphicsExtractor var0, String var1, int var2, int var3, int var4, ic.n var5) {
      if (j()) {
         Y(var0, var1, var2, var3, var4, var5.u);
      } else if (var5.z == 1.0F) {
         W(var0, var1, var2, var3, var4, var5.P);
      } else {
         var0.pose().pushMatrix();

         try {
            var0.pose().translate(var2, var3);
            var0.pose().scale(var5.z, var5.z);
            W(var0, var1, 0, 0, var4, var5.P);
         } finally {
            var0.pose().popMatrix();
         }
      }
   }

   public static Component u(String var0) {
      return Component.literal(var0).setStyle(n);
   }

   public static ic.m Z() {
      o++;
      return () -> o--;
   }

   private static boolean j() {
      if (o > 0) {
         return true;
      }

      Minecraft var0 = Minecraft.getInstance();
      return var0 != null && var0.gui != null && var0.gui.screen() instanceof f8;
   }

   private static ic.b j(ic.b var0) {
      if (!j()) {
         return var0;
      }

      return switch (var0) {
         case c -> ic.b.Y;
         case R -> ic.b.D;
         case I -> ic.b.D;
         default -> var0;
      };
   }

   public static int H(String var0) {
      return U(var0, ic.b.c);
   }

   public static int U(String var0, ic.b var1) {
      return z(var0, j(var1));
   }

   public static void N(GuiGraphicsExtractor var0, String var1, int var2, int var3, int var4) {
      W(var0, var1, var2, var3, var4, ic.b.c);
   }

   public static void W(GuiGraphicsExtractor var0, String var1, int var2, int var3, int var4, ic.b var5) {
      Y(var0, var1, var2, var3, var4, j(var5));
   }

   public static String U(String var0, int var1) {
      return N(var0, var1, ic.b.c);
   }

   public static String N(String var0, int var1, ic.b var2) {
      return P(var0, var1, j(var2));
   }

   public static Component D(String var0, ic.b var1) {
      return Component.literal(var0).setStyle(W[var1.ordinal()]);
   }

   public static int E(String var0) {
      return z(var0, ic.b.c);
   }

   public static int z(String var0, ic.b var1) {
      Font var2 = Minecraft.getInstance().font;
      Q.x(var2, nl.q);
      return Q.y(var0, var1.ordinal(), () -> wf.D(var1, var0) ? wf.v(var0, var1) : var2.width(D(var0, var1)));
   }

   public static void H(GuiGraphicsExtractor var0, String var1, int var2, int var3, int var4) {
      Y(var0, var1, var2, var3, var4, ic.b.c);
   }

   public static void Y(GuiGraphicsExtractor var0, String var1, int var2, int var3, int var4, ic.b var5) {
      var4 = oa.e(var4);
      if (var4 >>> 24 >= 4) {
         if (!wf.Y(var0, var1, var2, var3, var4, var5)) {
            var0.text(Minecraft.getInstance().font, D(var1, var5), var2, var3, var4, false);
         }
      }
   }

   public static void A(GuiGraphicsExtractor var0, String var1, int var2, int var3, int var4, int var5, float var6, ic.b var7) {
      var4 = oa.e(var4);
      var5 = oa.e(var5);
      if (var4 >>> 24 >= 4) {
         if (!wf.Y(var0, var1, var2, var3, var4, var7, var5, var6)) {
            if (var5 >>> 24 >= 4 && var6 > 0.0F) {
               for (int var8 = 0; var8 < 8; var8++) {
                  double var9 = var8 * Math.PI / 4.0;
                  var0.pose().pushMatrix();

                  try {
                     var0.pose().translate((float)(Math.cos(var9) * var6), (float)(Math.sin(var9) * var6));
                     var0.text(Minecraft.getInstance().font, D(var1, var7), var2, var3, var5, false);
                  } finally {
                     var0.pose().popMatrix();
                  }
               }
            }

            var0.text(Minecraft.getInstance().font, D(var1, var7), var2, var3, var4, false);
         }
      }
   }

   public static void L(GuiGraphicsExtractor var0, String var1, int var2, int var3, int var4, float var5, ic.b var6) {
      var4 = oa.e(var4);
      if (var4 >>> 24 >= 2) {
         wf.A(var0, var1, var2, var3, var4, var6, var5);
      }
   }

   public static float Z(String var0, ic.b var1) {
      return wf.D(var1, var0) ? wf.m(var0, var1) : Minecraft.getInstance().font.getSplitter().stringWidth(D(var0, var1));
   }

   public static void s(GuiGraphicsExtractor var0, String var1, int var2, int var3, int var4) {
      H(var0, var1, var2 - E(var1) / 2, var3, var4);
   }

   public static String W(String var0, int var1) {
      return P(var0, var1, ic.b.c);
   }

   public static String P(String var0, int var1, ic.b var2) {
      Q.x(Minecraft.getInstance().font, nl.q);
      return Q.N(var0, var2.ordinal(), var1, () -> {
         if (z(var0, var2) <= var1) {
            return var0;
         }

         int var3 = var0.length();

         while (var3 > 0 && z(var0.substring(0, var3) + "…", var2) > var1) {
            var3 = var0.offsetByCodePoints(var3, -1);
         }

         return var3 == 0 ? "" : var0.substring(0, var3) + "…";
      });
   }

   static {
      _q.z(
         "Font",
         "Inter UI/HUD and ClickUI faces registered at native pixel sizes per role; ClickUI text is rasterized at the on-screen pixel size, other text by Minecraft"
      );
   }

   public enum b {
      c,
      R,
      I,
      n,
      r,
      q,
      Y,
      D,
      v,
      K,
      S,
      H,
      J,
      A,
      d,
      m;
   }

   public interface m extends AutoCloseable {
      @Override
      void close();
   }

   public enum n {
      t(ic.b.I, 1.2F, ic.b.v),
      Z(ic.b.I, 1.15F, ic.b.K),
      Q(ic.b.R, 1.0F, ic.b.S),
      C(ic.b.c, 1.0F, ic.b.H),
      E(ic.b.R, 0.9F, ic.b.J),
      w(ic.b.R, 0.9F, ic.b.A),
      I(ic.b.c, 0.85F, ic.b.d),
      L(ic.b.I, 0.8F, ic.b.m);

      public final ic.b P;
      public final float z;
      final ic.b u;

      n(ic.b var3, float var4, ic.b var5) {
         this.P = var3;
         this.z = var4;
         this.u = var5;
      }

      public int T() {
         return (int)Math.ceil(9.0F * this.z);
      }
   }
}
