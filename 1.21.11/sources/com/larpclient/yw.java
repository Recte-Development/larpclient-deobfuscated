package com.larpclient;

import com.larpclient.module.PlayerModelModule;
import java.io.IOException;
import java.io.InputStream;
import net.minecraft.class_10055;
import net.minecraft.class_11659;
import net.minecraft.class_12249;
import net.minecraft.class_1306;
import net.minecraft.class_2960;
import net.minecraft.class_310;
import net.minecraft.class_4587;
import net.minecraft.class_4588;
import net.minecraft.class_4608;
import net.minecraft.class_591;
import net.minecraft.class_4587.class_4665;
import org.joml.Matrix3f;
import org.joml.Vector3f;

public final class yw {
   private static final class_2960 J = M("models/emotes/ak47/mesh.bin");
   private static final class_2960 U = M("models/emotes/ak47/albedo.png");
   private static final int Z = 15728880;
   private static float[] h;
   private static long S = -1L;
   private static long f = -1L;

   private yw() {
   }

   private static class_2960 M(String var0) {
      return class_2960.method_60655("larp-client", var0);
   }

   public static boolean B(class_591 var0, class_10055 var1, class_4587 var2, class_11659 var3, int var4) {
      q1 var5 = (q1)var1;
      float var6 = var5.larp$emoteWeight();
      if (!(var6 <= 0.0F) && "ak47".equals(var5.larp$emoteProp())) {
         PlayerModelModule var7 = PlayerModelModule.h();
         if (var7 != null && var7.g(var1)) {
            return false;
         }

         if (!D()) {
            return false;
         }

         float[] var8 = var5.larp$emoteSample();
         int var9 = cb.x.B.ordinal() * 10;
         double var10 = V(var8[var9 + 7], 1.0) * var6;
         if (var10 < 0.001) {
            return true;
         }

         int var12 = V(var8[var9 + 9], 0.0) >= 0.5 ? 1 : 0;
         var2.method_22903();

         try {
            if (var12 != 0) {
               var0.method_72859(var1, class_1306.field_6183, var2);
            } else {
               var0.method_63512().method_22703(var2);
               var0.field_3391.method_22703(var2);
            }

            ed.r(var2, (boolean)var12, V(var8[var9 + 3], 0.0), V(var8[var9 + 4], 0.0), V(var8[var9 + 5], 0.0), var10);
            float[] var13 = h;
            var3.method_73483(var2, class_12249.method_75982(U), (var2x, var3x) -> L(var13, var2x, var3x, var4));
            c5.L("Rifle quads", var13.length / 8 / 4);
            if (var12 != 0 && var6 >= 0.999F) {
               S(var2, var3, var4, var5.larp$emoteCycle());
            }
         } finally {
            var2.method_22909();
         }

         return true;
      } else {
         return false;
      }
   }

   private static void S(class_4587 var0, class_11659 var1, int var2, float var3) {
      float[] var4 = ed.q(var3);
      if (var4.length > 0) {
         var1.method_73529(1).method_73483(var0, class_12249.method_76014(U), (var1x, var2x) -> L(var4, var1x, var2x, 15728880));
      }

      Vector3f var5 = new Matrix3f(var0.method_23760().method_23762()).invert().transform(new Vector3f(0.0F, -1.0F, 0.0F));
      if (var5.lengthSquared() > 1.0E-8F) {
         float[] var6 = ed.s(var3, var5.normalize());
         if (var6.length > 0) {
            var1.method_73483(var0, class_12249.method_75982(U), (var2x, var3x) -> L(var6, var2x, var3x, var2));
         }
      }
   }

   private static double V(float var0, double var1) {
      return Float.isNaN(var0) ? var1 : var0;
   }

   static void L(float[] var0, class_4665 var1, class_4588 var2, int var3) {
      for (byte var4 = 0; var4 < var0.length; var4 += 8) {
         var2.method_56824(var1, var0[var4], var0[var4 + 1], var0[var4 + 2])
            .method_39415(-1)
            .method_22913(var0[var4 + 3], var0[var4 + 4])
            .method_22922(class_4608.field_21444)
            .method_60803(var3)
            .method_60831(var1, var0[var4 + 5], var0[var4 + 6], var0[var4 + 7]);
      }
   }

   private static boolean D() {
      if (f == t_.g) {
         return false;
      }

      if (h != null && S == t_.g) {
         return true;
      }

      try (InputStream var0 = class_310.method_1551().method_1478().getResourceOrThrow(J).method_14482()) {
         class_310.method_1551().method_1478().getResourceOrThrow(U);
         h = ed.l(yg.k(var0));
         S = t_.g;
         q8.G("Emotes", "Rifle ready: {} quads, resource generation {}", h.length / 8 / 4, S);
         return true;
      } catch (IOException | RuntimeException var5) {
         f = t_.g;
         h = null;
         q8.u("Emotes", "Rifle model unavailable; AK Spray shows held items until resource reload", var5);
         return false;
      }
   }
}
