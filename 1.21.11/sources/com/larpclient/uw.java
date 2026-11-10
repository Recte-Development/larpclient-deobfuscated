package com.larpclient;

import java.util.Arrays;
import java.util.Locale;
import net.minecraft.class_2561;
import net.minecraft.class_2583;
import net.minecraft.class_2960;
import net.minecraft.class_310;
import net.minecraft.class_327;
import net.minecraft.class_332;
import net.minecraft.class_11719.class_11721;

public final class uw {
   private static int b;
   private static final ir H = new ir();
   private static final class_2583[] l = Arrays.stream(uw.r.values())
      .map(
         var0 -> var0 == uw.r.r
            ? class_2583.field_24360
            : class_2583.field_24360.method_27704(new class_11721(class_2960.method_60655("larp-client", var0.name().toLowerCase(Locale.ROOT))))
      )
      .toArray(class_2583[]::new);
   private static final class_2583 o = class_2583.field_24360.method_27704(new class_11721(class_2960.method_60655("larp-client", "world_semibold")));
   private static int r;

   public static int E() {
      return b;
   }

   public static void W() {
      H.t(null, -1);
      b++;
   }

   public static int C(String var0, uw.c var1) {
      return l() ? z(var0, var1.T) : (int)Math.ceil(O(var0, var1.B) * var1.g);
   }

   public static String X(String var0, int var1, uw.c var2) {
      return l() ? n(var0, var1, var2.T) : H(var0, Math.max(0, (int)Math.floor(var1 / var2.g)), var2.B);
   }

   public static void X(class_332 var0, String var1, int var2, int var3, int var4, uw.c var5) {
      if (l()) {
         D(var0, var1, var2, var3, var4, var5.T);
      } else if (var5.g == 1.0F) {
         N(var0, var1, var2, var3, var4, var5.B);
      } else {
         var0.method_51448().pushMatrix();

         try {
            var0.method_51448().translate(var2, var3);
            var0.method_51448().scale(var5.g, var5.g);
            N(var0, var1, 0, 0, var4, var5.B);
         } finally {
            var0.method_51448().popMatrix();
         }
      }
   }

   public static class_2561 a(String var0) {
      return class_2561.method_43470(var0).method_10862(o);
   }

   public static uw.h S() {
      r++;
      return () -> r--;
   }

   private static boolean l() {
      if (r > 0) {
         return true;
      }

      class_310 var0 = class_310.method_1551();
      return var0 != null && var0.field_1705 != null && var0.field_1755 instanceof tk;
   }

   private static uw.r U(uw.r var0) {
      if (!l()) {
         return var0;
      }

      return switch (var0) {
         case n -> uw.r.x;
         case V -> uw.r.m;
         case M -> uw.r.m;
         default -> var0;
      };
   }

   public static int r(String var0) {
      return O(var0, uw.r.n);
   }

   public static int O(String var0, uw.r var1) {
      return z(var0, U(var1));
   }

   public static void G(class_332 var0, String var1, int var2, int var3, int var4) {
      N(var0, var1, var2, var3, var4, uw.r.n);
   }

   public static void N(class_332 var0, String var1, int var2, int var3, int var4, uw.r var5) {
      D(var0, var1, var2, var3, var4, U(var5));
   }

   public static String J(String var0, int var1) {
      return H(var0, var1, uw.r.n);
   }

   public static String H(String var0, int var1, uw.r var2) {
      return n(var0, var1, U(var2));
   }

   public static class_2561 v(String var0, uw.r var1) {
      return class_2561.method_43470(var0).method_10862(l[var1.ordinal()]);
   }

   public static int i(String var0) {
      return z(var0, uw.r.n);
   }

   public static int z(String var0, uw.r var1) {
      class_327 var2 = class_310.method_1551().field_1772;
      H.t(var2, t_.g);
      return H.C(var0, var1.ordinal(), () -> te.e(var1, var0) ? te.N(var0, var1) : var2.method_27525(v(var0, var1)));
   }

   public static void z(class_332 var0, String var1, int var2, int var3, int var4) {
      D(var0, var1, var2, var3, var4, uw.r.n);
   }

   public static void D(class_332 var0, String var1, int var2, int var3, int var4, uw.r var5) {
      var4 = fp.R(var4);
      if (var4 >>> 24 >= 4) {
         if (!te.C(var0, var1, var2, var3, var4, var5)) {
            var0.method_51439(class_310.method_1551().field_1772, v(var1, var5), var2, var3, var4, false);
         }
      }
   }

   public static void S(class_332 var0, String var1, int var2, int var3, int var4, int var5, float var6, uw.r var7) {
      var4 = fp.R(var4);
      var5 = fp.R(var5);
      if (var4 >>> 24 >= 4) {
         if (!te.b(var0, var1, var2, var3, var4, var7, var5, var6)) {
            if (var5 >>> 24 >= 4 && var6 > 0.0F) {
               for (int var8 = 0; var8 < 8; var8++) {
                  double var9 = var8 * Math.PI / 4.0;
                  var0.method_51448().pushMatrix();

                  try {
                     var0.method_51448().translate((float)(Math.cos(var9) * var6), (float)(Math.sin(var9) * var6));
                     var0.method_51439(class_310.method_1551().field_1772, v(var1, var7), var2, var3, var5, false);
                  } finally {
                     var0.method_51448().popMatrix();
                  }
               }
            }

            var0.method_51439(class_310.method_1551().field_1772, v(var1, var7), var2, var3, var4, false);
         }
      }
   }

   public static void A(class_332 var0, String var1, int var2, int var3, int var4, float var5, uw.r var6) {
      var4 = fp.R(var4);
      if (var4 >>> 24 >= 2) {
         te.u(var0, var1, var2, var3, var4, var6, var5);
      }
   }

   public static float t(String var0, uw.r var1) {
      return te.e(var1, var0) ? te.I(var0, var1) : class_310.method_1551().field_1772.method_27527().method_27488(v(var0, var1));
   }

   public static void N(class_332 var0, String var1, int var2, int var3, int var4) {
      z(var0, var1, var2 - i(var1) / 2, var3, var4);
   }

   public static String R(String var0, int var1) {
      return n(var0, var1, uw.r.n);
   }

   public static String n(String var0, int var1, uw.r var2) {
      H.t(class_310.method_1551().field_1772, t_.g);
      return H.x(var0, var2.ordinal(), var1, () -> {
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
      q8.G(
         "Font",
         "Inter UI/HUD and ClickUI faces registered at native pixel sizes per role; ClickUI text is rasterized at the on-screen pixel size, other text by Minecraft"
      );
   }

   public enum c {
      Y(uw.r.M, 1.2F, uw.r.K),
      v(uw.r.M, 1.15F, uw.r.b),
      e(uw.r.V, 1.0F, uw.r.q),
      o(uw.r.n, 1.0F, uw.r.A),
      t(uw.r.V, 0.9F, uw.r.Q),
      E(uw.r.V, 0.9F, uw.r.O),
      M(uw.r.n, 0.85F, uw.r.g),
      H(uw.r.M, 0.8F, uw.r.e);

      public final uw.r B;
      public final float g;
      final uw.r T;

      c(uw.r var3, float var4, uw.r var5) {
         this.B = var3;
         this.g = var4;
         this.T = var5;
      }

      public int b() {
         return (int)Math.ceil(9.0F * this.g);
      }
   }

   public interface h extends AutoCloseable {
      @Override
      void close();
   }

   public enum r {
      n,
      V,
      M,
      p,
      J,
      d,
      s,
      x,
      m,
      K,
      b,
      q,
      A,
      Q,
      O,
      g,
      e,
      r;
   }
}
