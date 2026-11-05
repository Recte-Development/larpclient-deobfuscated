package com.larpclient;

import java.util.List;
import org.joml.Matrix3x2f;

public final class fm {
   public static final List<String> N = List.of("Fade", "Tilt", "Slide", "Pop", "Zoom", "CRT");

   private fm() {
   }

   public static Matrix3x2f q(String var0, double var1, double var3, double var5) {
      float var7 = (float)Math.clamp(var1, 0.0, 1.0);
      Matrix3x2f var8 = new Matrix3x2f();
      if (var0.equals("Slide")) {
         return var8;
      }

      var8.translate((float)var3, (float)var5);
      switch (var0) {
         case "Pop":
            var8.translate(0.0F, (1.0F - var7) * 13.0F).scale(0.9F + 0.1F * (float)u7.p.Y.m(var7));
            break;
         case "Zoom":
            var8.scale(0.55F + 0.45F * var7);
            break;
         case "CRT":
            var8.scale(Math.max(0.003F, r(var7)), 0.01F + 0.99F * t(var7));
            break;
         case "Tilt":
            var8.rotate((float)Math.toRadians((1.0F - var7) * -22.0F));
            var8.scale(1.0F, (float)Math.cos(Math.toRadians((1.0F - var7) * 12.0F)));
            var8.scale(0.5F + var7 * 0.5F);
            break;
         default:
            var8.scale(0.5F + var7 * 0.5F);
      }

      return var8.translate((float)(-var3), (float)(-var5));
   }

   public static double j(String var0, double var1) {
      double var3 = Math.clamp(var1, 0.0, 1.0);
      return var0.equals("CRT") ? Math.min(1.0, var3 * 4.0) : var3;
   }

   public static double E(String var0, double var1) {
      return var0.equals("CRT") ? (1.0F - t((float)Math.clamp(var1, 0.0, 1.0))) * 0.85 : 0.0;
   }

   private static float r(float var0) {
      return (float)u7.p.y.m(Math.min(1.0F, var0 / 0.4F));
   }

   private static float t(float var0) {
      return (float)u7.p.y.m(Math.max(0.0F, (var0 - 0.35F) / 0.65F));
   }
}
