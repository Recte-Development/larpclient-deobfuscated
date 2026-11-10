package com.larpclient;

import java.util.ArrayList;
import java.util.List;
import org.joml.Matrix4f;

public final class gm {
   public static final double i = 0.995;
   public static final float S = 0.118F;
   public static final float r = 0.19666666F;
   private static final int[] I = new int[]{0, 1, 3, 2};
   private static final float P = 0.32F;
   private static final List<gm.n> a = r();

   private static List<gm.n> r() {
      ArrayList var0 = new ArrayList();

      for (int var1 = 0; var1 < I.length; var1++) {
         float var2 = (float)Math.toRadians(90 * var1);
         var0.add(new gm.n(I[var1], 0.32F * (float)Math.sin(var2), 0.0F, -0.32F * (float)Math.cos(var2), var2));
      }

      return List.copyOf(var0);
   }

   public static List<gm.n> b(gm.y var0) {
      return var0 == gm.y.k ? a : List.of();
   }

   public static Matrix4f M(Matrix4f var0, gm.n var1, double var2, Matrix4f var4) {
      float var5 = (float)var2;
      return var4.set(var0)
         .translate(var1.x() * var5, -0.5F - var1.rise() * var5, var1.z() * var5)
         .rotateY((float) Math.PI - var1.angle())
         .rotateZ((float) Math.PI)
         .scale(0.19666666F * var5);
   }

   private gm() {
   }

   public static double z(gm.y var0, double var1) {
      return (var0 == gm.y.k ? ge.H().rise() : ld.R) * var1;
   }

   public static void P(d var0, Matrix4f var1, gm.y var2, double var3, double var5, boolean var7, double var8, double var10) {
      if (var2 == gm.y.k) {
         ge.P(var0, var1, var3, var5, var7, var8);
      } else {
         ld.x(var0, var1, var3, var5, var10);
      }
   }

   public record n(int coin, float x, float rise, float z, float angle) {
   }

   public enum y {
      k("Crown"),
      d("Halo");

      public final String W;

      y(String var3) {
         this.W = var3;
      }
   }
}
