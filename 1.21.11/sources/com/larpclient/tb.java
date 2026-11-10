package com.larpclient;

import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;
import net.minecraft.class_12247;
import net.minecraft.class_1921;
import net.minecraft.class_2960;
import net.minecraft.class_4588;
import org.joml.Matrix4f;

public final class tb {
   private static bl.a O;
   private static boolean i;

   private tb() {
   }

   public static bl.a x() {
      if (O == null && !i) {
         try (InputStream var0 = tb.class.getResourceAsStream("/assets/larp-client/models/hats/dominus.bin")) {
            if (var0 == null) {
               throw new IOException("missing resource");
            }

            O = N(var0.readAllBytes());
            q8.G(
               "ChinaHat",
               "Dominus mesh loaded; {} triangles, {} vertices, {} blocks above the head",
               O.triangles(),
               O.vertices(),
               String.format(Locale.ROOT, "%.3f", O.rise())
            );
         } catch (IOException | RuntimeException var5) {
            i = (boolean)1;
            q8.u("ChinaHat", "Dominus mesh unavailable; Dominus hidden until restart", var5);
         }

         return O;
      } else {
         return O;
      }
   }

   public static bl.a N(byte[] var0) {
      return bl.c(var0, false);
   }

   public static double f(double var0) {
      bl.a var2 = x();
      return var2 == null ? 0.0 : var2.rise() * var0;
   }

   public static class_1921 f() {
      if (tb.p.Z != t_.g) {
         f2.a(bl.t());
         tb.p.Z = t_.g;
      }

      return tb.p.b;
   }

   static int c(int var0, int var1) {
      int var2 = -16777216;
      int var3 = var0 & 0xFF;

      for (byte var4 = 16; var4 >= 0; var4 -= 8) {
         var2 |= ((var1 >> var4 & 0xFF) * var3 + 127) / 255 << var4;
      }

      return var2;
   }

   public static void t(class_4588 var0, Matrix4f var1, bl.a var2, double var3, int var5) {
      float var6 = (float)var3;
      float[] var7 = var2.points();
      float[] var8 = var2.uvs();
      int[] var9 = var2.colors();

      for (int var13 : var2.indices()) {
         var0.method_22918(var1, var7[var13 * 3] * var6, -0.5F - var7[var13 * 3 + 1] * var6, var7[var13 * 3 + 2] * var6)
            .method_22913(var8[var13 * 2], var8[var13 * 2 + 1])
            .method_39415(c(var9[var13], var5));
      }

      c5.V("China Hat Dominus triangles", var2.triangles());
   }

   private static final class p {
      static final class_1921 b = class_1921.method_75940(
         "larp_china_hat_dominus",
         class_12247.method_75927(bl.t()).method_75934("Sampler0", class_2960.method_60655("larp-client", "models/hats/dominus.png")).method_75938()
      );
      static long Z = -1L;
   }
}
