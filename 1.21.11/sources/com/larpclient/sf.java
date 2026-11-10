package com.larpclient;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import net.minecraft.class_238;
import net.minecraft.class_243;
import net.minecraft.class_3532;

public final class sf {
   public static final double Z = 1.5;
   public static final double A = 0.03;
   public static final double F = 0.99;
   public static final double U = 0.8;
   public static final double W = 0.0172275;
   public static final double s = 0.1F;
   public static final int Q = 60;
   static final int i = 8;
   static final double r = 0.15625;
   static final double S = 0.15F;
   static final double G = 0.3125;
   static final double m = 0.3F;
   static final double g = 1.8F;
   private static final double E = 0.0103365;
   private static final class_243[] z = new class_243[]{
      class_243.field_1353,
      new class_243(0.0103365, 0.0, 0.0),
      new class_243(-0.0103365, 0.0, 0.0),
      new class_243(0.0, 0.0103365, 0.0),
      new class_243(0.0, -0.0103365, 0.0),
      new class_243(0.0, 0.0, 0.0103365),
      new class_243(0.0, 0.0, -0.0103365)
   };
   public static final sf.k N = new sf.d();

   public static sf.c H(sf.y var0, sf.k var1) {
      sf.c var2 = new sf.c();
      class_243 var3 = var0.position();
      class_243 var4 = var0.velocity();

      for (int var5 = 0; var5 < 60; var5++) {
         double var6 = var1.water(var3) ? 0.8 : 0.99;
         var4 = new class_243(var4.field_1352, var4.field_1351 - 0.03, var4.field_1350).method_1021(var6);
         class_243 var8 = var3.method_1019(var4);
         double var9 = var1.clip(var3, var8);
         var2.c[var5] = var3;
         var2.x[var5] = var9 < 1.0 ? var3.method_1019(var4.method_1021(var9)) : var8;
         var2.M[var5] = v(var0.age() + var5 + 1);
         var2.v = var5 + 1;
         if (var9 < 1.0) {
            var2.F = var2.x[var5];
            break;
         }

         var3 = var8;
      }

      return var2;
   }

   public static List<sf.c> X(sf.y var0, sf.k var1) {
      ArrayList var2 = new ArrayList(3);
      var2.add(H(var0, var1));
      var2.add(H(var0.step(var1), var1));
      if (var0.age() > 0) {
         var2.add(H(var0.back(), var1));
      }

      return var2;
   }

   public static List<sf.c> T(class_243 var0, class_243 var1, class_243 var2, int var3, sf.k var4) {
      List var5 = X(sf.y.thrown(var0, var1, var2).after(var3, var4), var4);

      for (int var6 = 1; var6 < z.length; var6++) {
         var5.add(H(sf.y.thrown(var0, var1.method_1019(z[var6]), var2).after(var3, var4), var4));
      }

      return var5;
   }

   public static List<sf.c> I(class_243 var0, class_243 var1, class_243 var2, sf.k var3) {
      ArrayList var4 = new ArrayList(z.length);

      for (class_243 var8 : z) {
         var4.add(H(sf.y.thrown(var0, var1.method_1019(var8), var2), var3));
      }

      return var4;
   }

   public static sf.j S(class_243 var0, float var1, float var2, class_243 var3, sf.k var4, double var5) {
      float var7 = Math.max(-90.0F, Math.min(var2, 0.0F));
      int var8 = 0;

      while (true) {
         float var9 = Math.max(-90.0F, var7 - var8);
         class_243 var10 = x(var1, var9);
         if (M(H(sf.y.thrown(var0, var10, var3), var4), var0, var10.method_1021(1.5).method_1019(var3), var4) != null) {
            sf.p var11 = n(I(var0, var10, var3, var4), var0, var3, var10, var4);
            if (var11 != null && var11.reliability() >= var5) {
               return new sf.j(var9, var11);
            }
         }

         if (var9 <= -90.0F) {
            return null;
         }

         var8++;
      }
   }

   public static double v(int var0) {
      return Math.max(0.0F, Math.min(0.3F, (var0 - 2) / 20.0F));
   }

   static class_238 m(class_243 var0) {
      return new class_238(
         var0.field_1352 - 0.15625,
         var0.field_1351 - 0.15F,
         var0.field_1350 - 0.15625,
         var0.field_1352 + 0.15625,
         var0.field_1351 - 0.15F + 0.3125,
         var0.field_1350 + 0.15625
      );
   }

   static class_238 l(class_243 var0) {
      return new class_238(
         var0.field_1352 - 0.3F, var0.field_1351, var0.field_1350 - 0.3F, var0.field_1352 + 0.3F, var0.field_1351 + 1.8F, var0.field_1350 + 0.3F
      );
   }

   public static class_243 x(float var0, float var1) {
      float var2 = (float) (Math.PI / 180.0);
      return new class_243(
            -class_3532.method_15374(var0 * var2) * class_3532.method_15362(var1 * var2),
            -class_3532.method_15374(var1 * var2),
            class_3532.method_15362(var0 * var2) * class_3532.method_15362(var1 * var2)
         )
         .method_1029();
   }

   public static sf.f M(sf.c var0, class_243 var1, class_243 var2, sf.k var3) {
      for (int var4 = 0; var4 < var0.v; var4++) {
         class_243 var5 = var1.method_1019(var2.method_1021(var4));
         if (!m(var5).method_1014(var0.M[var4]).method_992(var0.c[var4], var0.x[var4]).isEmpty()) {
            if (var4 > 0 && var3.clip(var1, var5) < 1.0) {
               return null;
            }

            class_243 var6 = var0.c[var4];
            boolean var7 = l(var6).method_1014(v(var4 + 1)).method_992(var5, var5.method_1019(var2)).isPresent();
            return new sf.f(var4 + 1, var6, var7);
         }
      }

      return null;
   }

   public static sf.p n(List<sf.c> var0, class_243 var1, class_243 var2, class_243 var3, sf.k var4) {
      class_243 var5 = var3.method_1029();
      sf.f var6 = M((sf.c)var0.get(0), var1, var5.method_1021(1.5).method_1019(var2), var4);
      return var6 == null ? null : C(var0, var1, var2, var5, var6);
   }

   private static sf.p C(List<sf.c> var0, class_243 var1, class_243 var2, class_243 var3, sf.f var4) {
      int var5 = 0;
      int var6 = 0;

      for (sf.c var8 : var0) {
         for (class_243 var12 : z) {
            var6++;
            if (M(var8, var1, var3.method_1019(var12).method_1021(1.5).method_1019(var2), N) != null) {
               var5++;
            }
         }
      }

      return new sf.p(var3, var4, var5, var6);
   }

   public static sf.p p(List<sf.c> var0, class_243 var1, class_243 var2, sf.k var3) {
      sf.c var4 = (sf.c)var0.get(0);
      class_243 var5 = var1.method_1031(0.0, 0.006249994F, 0.0);
      ArrayList var6 = new ArrayList();

      for (int var7 = 1; var7 < var4.v; var7++) {
         class_243 var8 = var5.method_1019(var2.method_1021(var7));
         class_243 var9 = var4.c[var7];
         class_243 var10 = var4.x[var7].method_1020(var9);
         double var11 = var10.method_1027();
         if (!(var11 < 1.0E-9)) {
            class_243 var13 = var9.method_1020(var8);
            double var14 = 1.5 * var7;
            double var16 = 2.0 * var10.method_1026(var13);
            double var18 = var13.method_1027() - var14 * var14;
            double var20 = var16 * var16 - 4.0 * var11 * var18;
            if (!(var20 < 0.0)) {
               double var22 = (0.15625 + var4.M[var7]) / Math.sqrt(var11);
               double var24 = Math.sqrt(var20);

               for (double var29 : new double[]{(-var16 - var24) / (2.0 * var11), (-var16 + var24) / (2.0 * var11)}) {
                  if (var29 >= -var22 && var29 <= 1.0 + var22) {
                     var6.add(var9.method_1019(var10.method_1021(var29)).method_1020(var8));
                  }
               }
            }
         }
      }

      ArrayList var31 = new ArrayList();

      for (class_243 var34 : var6) {
         if (!(var34.method_1027() < 1.0E-12)) {
            class_243 var36 = var34.method_1029();
            sf.f var38 = M(var4, var1, var36.method_1021(1.5).method_1019(var2), var3);
            if (var38 != null) {
               var31.add(new sf.p(var36, var38, 0, 0));
            }
         }
      }

      var31.sort(Comparator.comparingInt(var0x -> var0x.nominal().tick()));
      sf.p var33 = null;

      for (int var35 = 0; var35 < Math.min(8, var31.size()); var35++) {
         sf.p var37 = C(var0, var1, var2, ((sf.p)var31.get(var35)).direction(), ((sf.p)var31.get(var35)).nominal());
         if (var33 == null || var37.score() > var33.score()) {
            var33 = var37;
         }
      }

      return var33;
   }

   private sf() {
   }

   public static final class c {
      final class_243[] c = new class_243[60];
      final class_243[] x = new class_243[60];
      final double[] M = new double[60];
      int v;
      class_243 F;

      public int m() {
         return this.v;
      }

      public class_243 N() {
         return this.F;
      }
   }

   private static final class d implements sf.k {
      @Override
      public double clip(class_243 var1, class_243 var2) {
         return 1.0;
      }

      @Override
      public boolean water(class_243 var1) {
         return false;
      }
   }

   public record f(int tick, class_243 point, boolean burst) {
   }

   public record j(float pitch, sf.p plan) {
   }

   public interface k {
      double clip(class_243 var1, class_243 var2);

      boolean water(class_243 var1);
   }

   public record p(class_243 direction, sf.f nominal, int score, int samples) {
      public double reliability() {
         return this.samples == 0 ? 0.0 : (double)this.score / this.samples;
      }
   }

   public record y(class_243 position, class_243 velocity, int age) {
      public static sf.y thrown(class_243 var0, class_243 var1, class_243 var2) {
         return new sf.y(var0.method_1023(0.0, 0.1F, 0.0), var1.method_1021(1.5).method_1019(var2), 0);
      }

      public sf.y step(sf.k var1) {
         double var2 = var1.water(this.position) ? 0.8 : 0.99;
         class_243 var4 = new class_243(this.velocity.field_1352, this.velocity.field_1351 - 0.03, this.velocity.field_1350).method_1021(var2);
         return new sf.y(this.position.method_1019(var4), var4, this.age + 1);
      }

      public sf.y back() {
         return new sf.y(this.position.method_1020(this.velocity), this.velocity.method_1021(1.0101010101010102).method_1031(0.0, 0.03, 0.0), this.age - 1);
      }

      public sf.y after(int var1, sf.k var2) {
         sf.y var3 = this;

         for (int var4 = 0; var4 < var1; var4++) {
            var3 = var3.step(var2);
         }

         return var3;
      }
   }
}
