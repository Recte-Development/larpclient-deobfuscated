package com.larpclient;

import com.larpclient.module.PlayerModelModule;
import java.io.IOException;
import java.io.InputStream;
import net.minecraft.class_10055;
import net.minecraft.class_11659;
import net.minecraft.class_12249;
import net.minecraft.class_2960;
import net.minecraft.class_310;
import net.minecraft.class_4587;
import net.minecraft.class_591;

public final class cn {
   private static final class_2960 d = z("models/emotes/diamond_pickaxe/mesh.bin");
   private static final class_2960 T = z("models/emotes/diamond_pickaxe/albedo.png");
   private static float[] u;
   private static long W = -1L;
   private static long n = -1L;

   private cn() {
   }

   private static class_2960 z(String var0) {
      return class_2960.method_60655("larp-client", var0);
   }

   public static boolean V(class_591 var0, class_10055 var1, class_4587 var2, class_11659 var3, int var4) {
      q1 var5 = (q1)var1;
      float var6 = var5.larp$emoteWeight();
      if (!(var6 <= 0.0F) && "crypto_rock".equals(var5.larp$emoteProp())) {
         PlayerModelModule var7 = PlayerModelModule.h();
         if (var7 != null && var7.g(var1)) {
            return false;
         }

         if (!s()) {
            return false;
         }

         float[] var8 = var5.larp$emoteSample();
         int var9 = cb.x.z.ordinal() * 10;
         double var10 = J(var8[var9 + 7], 1.0) * var6;
         if (var10 < 0.001) {
            return true;
         }

         var2.method_22903();

         try {
            var0.method_63512().method_22703(var2);
            yz.i(
               var2,
               J(var8[var9], 0.0),
               J(var8[var9 + 1], 0.0),
               J(var8[var9 + 2], 0.0),
               J(var8[var9 + 3], 0.0),
               J(var8[var9 + 4], 0.0),
               J(var8[var9 + 5], 0.0),
               var10
            );
            float[] var12 = u;
            var3.method_73483(var2, class_12249.method_75982(T), (var2x, var3x) -> yw.L(var12, var2x, var3x, var4));
            c5.L("Pickaxe quads", var12.length / 8 / 4);
         } finally {
            var2.method_22909();
         }

         return true;
      } else {
         return false;
      }
   }

   private static double J(float var0, double var1) {
      return Float.isNaN(var0) ? var1 : var0;
   }

   private static boolean s() {
      if (n == t_.g) {
         return false;
      }

      if (u != null && W == t_.g) {
         return true;
      }

      try (InputStream var0 = class_310.method_1551().method_1478().getResourceOrThrow(d).method_14482()) {
         class_310.method_1551().method_1478().getResourceOrThrow(T);
         u = ed.l(yg.k(var0));
         W = t_.g;
         q8.G("Emotes", "Pickaxe ready: {} quads, resource generation {}", u.length / 8 / 4, W);
         return true;
      } catch (IOException | RuntimeException var5) {
         n = t_.g;
         u = null;
         q8.u("Emotes", "Pickaxe model unavailable; Crypto Mining shows held items until resource reload", var5);
         return false;
      }
   }
}
