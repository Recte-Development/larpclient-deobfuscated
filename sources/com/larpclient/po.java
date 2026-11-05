package com.larpclient;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public final class po {
   public static final double s = 1.5;
   public static final double y = 0.03;
   public static final double p = 0.99;
   public static final double o = 0.8;
   public static final double M = 0.0172275;
   public static final double x = 0.1F;
   public static final int l = 60;
   static final int Q = 8;
   static final double b = 0.15625;
   static final double m = 0.15F;
   static final double J = 0.3125;
   static final double R = 0.3F;
   static final double S = 1.8F;
   private static final double L = 0.0103365;
   private static final Vec3[] B = new Vec3[]{
      Vec3.ZERO,
      new Vec3(0.0103365, 0.0, 0.0),
      new Vec3(-0.0103365, 0.0, 0.0),
      new Vec3(0.0, 0.0103365, 0.0),
      new Vec3(0.0, -0.0103365, 0.0),
      new Vec3(0.0, 0.0, 0.0103365),
      new Vec3(0.0, 0.0, -0.0103365)
   };
   public static final po.j g = new po.k();

   public static po.f v(po.a var0, po.j var1) {
      po.f var2 = new po.f();
      Vec3 var3 = var0.position();
      Vec3 var4 = var0.velocity();

      for (int var5 = 0; var5 < 60; var5++) {
         double var6 = var1.water(var3) ? 0.8 : 0.99;
         var4 = new Vec3(var4.x, var4.y - 0.03, var4.z).scale(var6);
         Vec3 var8 = var3.add(var4);
         double var9 = var1.clip(var3, var8);
         var2.W[var5] = var3;
         var2.C[var5] = var9 < 1.0 ? var3.add(var4.scale(var9)) : var8;
         var2.s[var5] = l(var0.age() + var5 + 1);
         var2.I = var5 + 1;
         if (var9 < 1.0) {
            var2.M = var2.C[var5];
            break;
         }

         var3 = var8;
      }

      return var2;
   }

   public static List<po.f> K(po.a var0, po.j var1) {
      ArrayList var2 = new ArrayList(3);
      var2.add(v(var0, var1));
      var2.add(v(var0.step(var1), var1));
      if (var0.age() > 0) {
         var2.add(v(var0.back(), var1));
      }

      return var2;
   }

   public static List<po.f> a(Vec3 var0, Vec3 var1, Vec3 var2, int var3, po.j var4) {
      List var5 = K(po.a.thrown(var0, var1, var2).after(var3, var4), var4);

      for (int var6 = 1; var6 < B.length; var6++) {
         var5.add(v(po.a.thrown(var0, var1.add(B[var6]), var2).after(var3, var4), var4));
      }

      return var5;
   }

   public static List<po.f> v(Vec3 var0, Vec3 var1, Vec3 var2, po.j var3) {
      ArrayList var4 = new ArrayList(B.length);

      for (Vec3 var8 : B) {
         var4.add(v(po.a.thrown(var0, var1.add(var8), var2), var3));
      }

      return var4;
   }

   public static po.q Y(Vec3 var0, float var1, float var2, Vec3 var3, po.j var4, double var5) {
      float var7 = Math.max(-90.0F, Math.min(var2, 0.0F));
      int var8 = 0;

      while (true) {
         float var9 = Math.max(-90.0F, var7 - var8);
         Vec3 var10 = y(var1, var9);
         if (G(v(po.a.thrown(var0, var10, var3), var4), var0, var10.scale(1.5).add(var3), var4) != null) {
            po.n var11 = J(v(var0, var10, var3, var4), var0, var3, var10, var4);
            if (var11 != null && var11.reliability() >= var5) {
               return new po.q(var9, var11);
            }
         }

         if (var9 <= -90.0F) {
            return null;
         }

         var8++;
      }
   }

   public static double l(int var0) {
      return Math.max(0.0F, Math.min(0.3F, (var0 - 2) / 20.0F));
   }

   static AABB M(Vec3 var0) {
      return new AABB(var0.x - 0.15625, var0.y - 0.15F, var0.z - 0.15625, var0.x + 0.15625, var0.y - 0.15F + 0.3125, var0.z + 0.15625);
   }

   static AABB G(Vec3 var0) {
      return new AABB(var0.x - 0.3F, var0.y, var0.z - 0.3F, var0.x + 0.3F, var0.y + 1.8F, var0.z + 0.3F);
   }

   public static Vec3 y(float var0, float var1) {
      float var2 = (float) (Math.PI / 180.0);
      return new Vec3(-Mth.sin(var0 * var2) * Mth.cos(var1 * var2), -Mth.sin(var1 * var2), Mth.cos(var0 * var2) * Mth.cos(var1 * var2)).normalize();
   }

   public static po.v G(po.f var0, Vec3 var1, Vec3 var2, po.j var3) {
      for (int var4 = 0; var4 < var0.I; var4++) {
         Vec3 var5 = var1.add(var2.scale(var4));
         if (!M(var5).inflate(var0.s[var4]).clip(var0.W[var4], var0.C[var4]).isEmpty()) {
            if (var4 > 0 && var3.clip(var1, var5) < 1.0) {
               return null;
            }

            Vec3 var6 = var0.W[var4];
            boolean var7 = G(var6).inflate(l(var4 + 1)).clip(var5, var5.add(var2)).isPresent();
            return new po.v(var4 + 1, var6, var7);
         }
      }

      return null;
   }

   public static po.n J(List<po.f> var0, Vec3 var1, Vec3 var2, Vec3 var3, po.j var4) {
      Vec3 var5 = var3.normalize();
      po.v var6 = G((po.f)var0.get(0), var1, var5.scale(1.5).add(var2), var4);
      return var6 == null ? null : i(var0, var1, var2, var5, var6);
   }

   private static po.n i(List<po.f> var0, Vec3 var1, Vec3 var2, Vec3 var3, po.v var4) {
      int var5 = 0;
      int var6 = 0;

      for (po.f var8 : var0) {
         for (Vec3 var12 : B) {
            var6++;
            if (G(var8, var1, var3.add(var12).scale(1.5).add(var2), g) != null) {
               var5++;
            }
         }
      }

      return new po.n(var3, var4, var5, var6);
   }

   public static po.n l(List<po.f> var0, Vec3 var1, Vec3 var2, po.j var3) {
      po.f var4 = (po.f)var0.get(0);
      Vec3 var5 = var1.add(0.0, 0.006249994F, 0.0);
      ArrayList var6 = new ArrayList();

      for (int var7 = 1; var7 < var4.I; var7++) {
         Vec3 var8 = var5.add(var2.scale(var7));
         Vec3 var9 = var4.W[var7];
         Vec3 var10 = var4.C[var7].subtract(var9);
         double var11 = var10.lengthSqr();
         if (!(var11 < 1.0E-9)) {
            Vec3 var13 = var9.subtract(var8);
            double var14 = 1.5 * var7;
            double var16 = 2.0 * var10.dot(var13);
            double var18 = var13.lengthSqr() - var14 * var14;
            double var20 = var16 * var16 - 4.0 * var11 * var18;
            if (!(var20 < 0.0)) {
               double var22 = (0.15625 + var4.s[var7]) / Math.sqrt(var11);
               double var24 = Math.sqrt(var20);

               for (double var29 : new double[]{(-var16 - var24) / (2.0 * var11), (-var16 + var24) / (2.0 * var11)}) {
                  if (var29 >= -var22 && var29 <= 1.0 + var22) {
                     var6.add(var9.add(var10.scale(var29)).subtract(var8));
                  }
               }
            }
         }
      }

      ArrayList var31 = new ArrayList();

      for (Vec3 var34 : var6) {
         if (!(var34.lengthSqr() < 1.0E-12)) {
            Vec3 var36 = var34.normalize();
            po.v var38 = G(var4, var1, var36.scale(1.5).add(var2), var3);
            if (var38 != null) {
               var31.add(new po.n(var36, var38, 0, 0));
            }
         }
      }

      var31.sort(Comparator.comparingInt(var0x -> var0x.nominal().tick()));
      po.n var33 = null;

      for (int var35 = 0; var35 < Math.min(8, var31.size()); var35++) {
         po.n var37 = i(var0, var1, var2, ((po.n)var31.get(var35)).direction(), ((po.n)var31.get(var35)).nominal());
         if (var33 == null || var37.score() > var33.score()) {
            var33 = var37;
         }
      }

      return var33;
   }

   private po() {
   }

   public record a(Vec3 position, Vec3 velocity, int age) {
      public static po.a thrown(Vec3 var0, Vec3 var1, Vec3 var2) {
         return new po.a(var0.subtract(0.0, 0.1F, 0.0), var1.scale(1.5).add(var2), 0);
      }

      public po.a step(po.j var1) {
         double var2 = var1.water(this.position) ? 0.8 : 0.99;
         Vec3 var4 = new Vec3(this.velocity.x, this.velocity.y - 0.03, this.velocity.z).scale(var2);
         return new po.a(this.position.add(var4), var4, this.age + 1);
      }

      public po.a back() {
         return new po.a(this.position.subtract(this.velocity), this.velocity.scale(1.0101010101010102).add(0.0, 0.03, 0.0), this.age - 1);
      }

      public po.a after(int var1, po.j var2) {
         po.a var3 = this;

         for (int var4 = 0; var4 < var1; var4++) {
            var3 = var3.step(var2);
         }

         return var3;
      }
   }

   public static final class f {
      final Vec3[] W = new Vec3[60];
      final Vec3[] C = new Vec3[60];
      final double[] s = new double[60];
      int I;
      Vec3 M;

      public int A() {
         return this.I;
      }

      public Vec3 m() {
         return this.M;
      }
   }

   public interface j {
      double clip(Vec3 var1, Vec3 var2);

      boolean water(Vec3 var1);
   }

   private static final class k implements po.j {
      @Override
      public double clip(Vec3 var1, Vec3 var2) {
         return 1.0;
      }

      @Override
      public boolean water(Vec3 var1) {
         return false;
      }
   }

   public record n(Vec3 direction, po.v nominal, int score, int samples) {
      public double reliability() {
         return this.samples == 0 ? 0.0 : (double)this.score / this.samples;
      }
   }

   public record q(float pitch, po.n plan) {
   }

   public record v(int tick, Vec3 point, boolean burst) {
   }
}
