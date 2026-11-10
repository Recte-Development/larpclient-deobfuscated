package com.larpclient;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public final class s9 {
   public static final double N = 1.5;
   public static final double s = 0.03;
   public static final double l = 0.99;
   public static final double c = 0.8;
   public static final double X = 0.0172275;
   public static final double H = 0.1F;
   public static final int t = 60;
   static final int n = 8;
   static final double h = 0.15625;
   static final double W = 0.15F;
   static final double Y = 0.3125;
   static final double V = 0.3F;
   static final double J = 1.8F;
   private static final double Z = 0.0103365;
   private static final Vec3[] i = new Vec3[]{
      Vec3.ZERO,
      new Vec3(0.0103365, 0.0, 0.0),
      new Vec3(-0.0103365, 0.0, 0.0),
      new Vec3(0.0, 0.0103365, 0.0),
      new Vec3(0.0, -0.0103365, 0.0),
      new Vec3(0.0, 0.0, 0.0103365),
      new Vec3(0.0, 0.0, -0.0103365)
   };
   public static final s9.v g = new s9.l();

   public static s9.j X(s9.h var0, s9.v var1) {
      s9.j var2 = new s9.j();
      Vec3 var3 = var0.position();
      Vec3 var4 = var0.velocity();

      for (int var5 = 0; var5 < 60; var5++) {
         double var6 = var1.water(var3) ? 0.8 : 0.99;
         var4 = new Vec3(var4.x, var4.y - 0.03, var4.z).scale(var6);
         Vec3 var8 = var3.add(var4);
         double var9 = var1.clip(var3, var8);
         var2.z[var5] = var3;
         var2.s[var5] = var9 < 1.0 ? var3.add(var4.scale(var9)) : var8;
         var2.Q[var5] = A(var0.age() + var5 + 1);
         var2.Y = var5 + 1;
         if (var9 < 1.0) {
            var2.X = var2.s[var5];
            break;
         }

         var3 = var8;
      }

      return var2;
   }

   public static List<s9.j> b(s9.h var0, s9.v var1) {
      ArrayList var2 = new ArrayList(3);
      var2.add(X(var0, var1));
      var2.add(X(var0.step(var1), var1));
      if (var0.age() > 0) {
         var2.add(X(var0.back(), var1));
      }

      return var2;
   }

   public static List<s9.j> i(Vec3 var0, Vec3 var1, Vec3 var2, int var3, s9.v var4) {
      List var5 = b(s9.h.thrown(var0, var1, var2).after(var3, var4), var4);

      for (int var6 = 1; var6 < i.length; var6++) {
         var5.add(X(s9.h.thrown(var0, var1.add(i[var6]), var2).after(var3, var4), var4));
      }

      return var5;
   }

   public static List<s9.j> o(Vec3 var0, Vec3 var1, Vec3 var2, s9.v var3) {
      ArrayList var4 = new ArrayList(i.length);

      for (Vec3 var8 : i) {
         var4.add(X(s9.h.thrown(var0, var1.add(var8), var2), var3));
      }

      return var4;
   }

   public static s9.u U(Vec3 var0, float var1, float var2, Vec3 var3, s9.v var4, double var5) {
      float var7 = Math.max(-90.0F, Math.min(var2, 0.0F));
      int var8 = 0;

      while (true) {
         float var9 = Math.max(-90.0F, var7 - var8);
         Vec3 var10 = m(var1, var9);
         if (n(X(s9.h.thrown(var0, var10, var3), var4), var0, var10.scale(1.5).add(var3), var4) != null) {
            s9.q var11 = k(o(var0, var10, var3, var4), var0, var3, var10, var4);
            if (var11 != null && var11.reliability() >= var5) {
               return new s9.u(var9, var11);
            }
         }

         if (var9 <= -90.0F) {
            return null;
         }

         var8++;
      }
   }

   public static double A(int var0) {
      return Math.max(0.0F, Math.min(0.3F, (var0 - 2) / 20.0F));
   }

   static AABB F(Vec3 var0) {
      return new AABB(var0.x - 0.15625, var0.y - 0.15F, var0.z - 0.15625, var0.x + 0.15625, var0.y - 0.15F + 0.3125, var0.z + 0.15625);
   }

   static AABB X(Vec3 var0) {
      return new AABB(var0.x - 0.3F, var0.y, var0.z - 0.3F, var0.x + 0.3F, var0.y + 1.8F, var0.z + 0.3F);
   }

   public static Vec3 m(float var0, float var1) {
      float var2 = (float) (Math.PI / 180.0);
      return new Vec3(-Mth.sin(var0 * var2) * Mth.cos(var1 * var2), -Mth.sin(var1 * var2), Mth.cos(var0 * var2) * Mth.cos(var1 * var2)).normalize();
   }

   public static s9.y n(s9.j var0, Vec3 var1, Vec3 var2, s9.v var3) {
      for (int var4 = 0; var4 < var0.Y; var4++) {
         Vec3 var5 = var1.add(var2.scale(var4));
         if (!F(var5).inflate(var0.Q[var4]).clip(var0.z[var4], var0.s[var4]).isEmpty()) {
            if (var4 > 0 && var3.clip(var1, var5) < 1.0) {
               return null;
            }

            Vec3 var6 = var0.z[var4];
            boolean var7 = X(var6).inflate(A(var4 + 1)).clip(var5, var5.add(var2)).isPresent();
            return new s9.y(var4 + 1, var6, var7);
         }
      }

      return null;
   }

   public static s9.q k(List<s9.j> var0, Vec3 var1, Vec3 var2, Vec3 var3, s9.v var4) {
      Vec3 var5 = var3.normalize();
      s9.y var6 = n((s9.j)var0.get(0), var1, var5.scale(1.5).add(var2), var4);
      return var6 == null ? null : s(var0, var1, var2, var5, var6);
   }

   private static s9.q s(List<s9.j> var0, Vec3 var1, Vec3 var2, Vec3 var3, s9.y var4) {
      int var5 = 0;
      int var6 = 0;

      for (s9.j var8 : var0) {
         for (Vec3 var12 : i) {
            var6++;
            if (n(var8, var1, var3.add(var12).scale(1.5).add(var2), g) != null) {
               var5++;
            }
         }
      }

      return new s9.q(var3, var4, var5, var6);
   }

   public static s9.q V(List<s9.j> var0, Vec3 var1, Vec3 var2, s9.v var3) {
      s9.j var4 = (s9.j)var0.get(0);
      Vec3 var5 = var1.add(0.0, 0.006249994F, 0.0);
      ArrayList var6 = new ArrayList();

      for (int var7 = 1; var7 < var4.Y; var7++) {
         Vec3 var8 = var5.add(var2.scale(var7));
         Vec3 var9 = var4.z[var7];
         Vec3 var10 = var4.s[var7].subtract(var9);
         double var11 = var10.lengthSqr();
         if (!(var11 < 1.0E-9)) {
            Vec3 var13 = var9.subtract(var8);
            double var14 = 1.5 * var7;
            double var16 = 2.0 * var10.dot(var13);
            double var18 = var13.lengthSqr() - var14 * var14;
            double var20 = var16 * var16 - 4.0 * var11 * var18;
            if (!(var20 < 0.0)) {
               double var22 = (0.15625 + var4.Q[var7]) / Math.sqrt(var11);
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
            s9.y var38 = n(var4, var1, var36.scale(1.5).add(var2), var3);
            if (var38 != null) {
               var31.add(new s9.q(var36, var38, 0, 0));
            }
         }
      }

      var31.sort(Comparator.comparingInt(var0x -> var0x.nominal().tick()));
      s9.q var33 = null;

      for (int var35 = 0; var35 < Math.min(8, var31.size()); var35++) {
         s9.q var37 = s(var0, var1, var2, ((s9.q)var31.get(var35)).direction(), ((s9.q)var31.get(var35)).nominal());
         if (var33 == null || var37.score() > var33.score()) {
            var33 = var37;
         }
      }

      return var33;
   }

   private s9() {
   }

   public record h(Vec3 position, Vec3 velocity, int age) {
      public static s9.h thrown(Vec3 var0, Vec3 var1, Vec3 var2) {
         return new s9.h(var0.subtract(0.0, 0.1F, 0.0), var1.scale(1.5).add(var2), 0);
      }

      public s9.h step(s9.v var1) {
         double var2 = var1.water(this.position) ? 0.8 : 0.99;
         Vec3 var4 = new Vec3(this.velocity.x, this.velocity.y - 0.03, this.velocity.z).scale(var2);
         return new s9.h(this.position.add(var4), var4, this.age + 1);
      }

      public s9.h back() {
         return new s9.h(this.position.subtract(this.velocity), this.velocity.scale(1.0101010101010102).add(0.0, 0.03, 0.0), this.age - 1);
      }

      public s9.h after(int var1, s9.v var2) {
         s9.h var3 = this;

         for (int var4 = 0; var4 < var1; var4++) {
            var3 = var3.step(var2);
         }

         return var3;
      }
   }

   public static final class j {
      final Vec3[] z = new Vec3[60];
      final Vec3[] s = new Vec3[60];
      final double[] Q = new double[60];
      int Y;
      Vec3 X;

      public int d() {
         return this.Y;
      }

      public Vec3 L() {
         return this.X;
      }
   }

   private static final class l implements s9.v {
      @Override
      public double clip(Vec3 var1, Vec3 var2) {
         return 1.0;
      }

      @Override
      public boolean water(Vec3 var1) {
         return false;
      }
   }

   public record q(Vec3 direction, s9.y nominal, int score, int samples) {
      public double reliability() {
         return this.samples == 0 ? 0.0 : (double)this.score / this.samples;
      }
   }

   public record u(float pitch, s9.q plan) {
   }

   public interface v {
      double clip(Vec3 var1, Vec3 var2);

      boolean water(Vec3 var1);
   }

   public record y(int tick, Vec3 point, boolean burst) {
   }
}
