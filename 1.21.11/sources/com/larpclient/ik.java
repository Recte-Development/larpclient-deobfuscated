package com.larpclient;

import com.larpclient.module.SwordModelsModule;
import com.larpclient.module.ViewModelModule;
import net.minecraft.class_1007;
import net.minecraft.class_11659;
import net.minecraft.class_12122;
import net.minecraft.class_12249;
import net.minecraft.class_1268;
import net.minecraft.class_1306;
import net.minecraft.class_1664;
import net.minecraft.class_1799;
import net.minecraft.class_1921;
import net.minecraft.class_2960;
import net.minecraft.class_310;
import net.minecraft.class_4587;
import net.minecraft.class_4588;
import net.minecraft.class_4608;
import net.minecraft.class_591;
import net.minecraft.class_742;
import net.minecraft.class_765;
import net.minecraft.class_7920;
import net.minecraft.class_4587.class_4665;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public final class ik {
   static final float Y = (float)(Math.tan(Math.toRadians(35.0)) / (0.75 * Math.tan(Math.toRadians(34.0))));
   static final float[][] z = new float[][]{
      {0.36F, 0.16F, 0.78F, 1.0F, 0.25F, 0.78F, 0.2F, 0.82F, 1.0F},
      {0.42F, 0.14F, 0.8F, 0.85F, 0.2F, 0.95F, 0.55F, 0.4F, 1.0F},
      {0.62F, 0.07F, 0.25F, 1.0F, 0.42F, 0.55F, 1.0F, 0.7F, 0.3F},
      {0.12F, 0.25F, 0.7F, 0.15F, 0.85F, 1.0F, 0.7F, 0.95F, 1.0F},
      {0.28F, 0.31F, 0.36F, 0.62F, 0.68F, 0.78F, 0.92F, 0.95F, 1.0F}
   };
   private static final float[] h = new float[]{1.3F, 1.0F, 1.9F};
   private static final float[] P = new float[]{0.011F, -0.017F, 0.007F};
   private static final float[] D = new float[]{-0.031F, -0.052F, -0.095F};
   private static final boolean s = true;

   public static boolean C(class_742 var0, class_1268 var1, class_1799 var2, float var3, float var4, float var5, class_4587 var6, class_11659 var7, int var8) {
      SwordModelsModule var9 = SwordModelsModule.X();
      class_310 var10 = class_310.method_1551();
      if (var9 != null && var9.b(var2) && var0 == var10.field_1724 && !var0.method_31550()) {
         co var11 = var9.R();
         int var12 = var0.method_52814().comp_1629() == class_7920.field_41122 ? 1 : 0;
         g_ var13 = w1.n(var11, (boolean)var12);
         if (var13 == null) {
            return false;
         }

         class_1306 var14 = var1 == class_1268.field_5808 ? var0.method_6068() : var0.method_6068().method_5928();
         int var15 = var14 == class_1306.field_6183 ? 1 : -1;
         if (var11.v()) {
            return w(var0, var1, var14, var15, var2, var3, var4, var5, var6, var7, var8, var9, var13);
         }

         sl var16 = var9.o(var1);
         if (var0.method_6115() || !var10.method_1569()) {
            var16.L();
         }

         double var17 = qo.K() + Math.clamp(var5, 0.0F, 1.0F) * 0.05;
         sl.t var19 = var16.j(var9.x(var1, var2), var11, var17, var3, var9.U.getValue());
         Matrix4f[] var20 = var9.I(var1).n(var13, var11, var19, var15, var9.R.getValue().floatValue(), var17);
         Vector3f var21 = new Vector3f(var15 * var9.N.getValue().floatValue(), var9.W.getValue().floatValue() + var19.t, var9.O.getValue().floatValue());
         Vector3f var22 = var20[0].transformPosition(new Vector3f(var15 * var11.r, var11.y, var11.S));
         var6.method_22903();

         try {
            K(var6, var1, var15, new Vector3f(var22).add(var21).mul(Y, Y, 1.0F));
            var6.method_22905(Y, Y, 1.0F);
            var6.method_46416(var21.x, var21.y, var21.z);
            if (!var0.method_5767()) {
               var6.method_22903();

               try {
                  em.L(var6, var20[1]);
                  var6.method_22905(0.72F, 0.72F, 0.72F);
                  j(var0, var1, var14, var15, var11, var6, var7, var8);
               } finally {
                  var6.method_22909();
               }
            }

            em.L(var6, var20[0]);

            for (int var23 = 0; var23 < var13.parts().size(); var23++) {
               g_.c var24 = var13.parts().get(var23);
               var6.method_22903();

               try {
                  em.L(var6, var20[var23 + 2]);
                  bw.k(var1, var15 < 0, var6, var24.vertices(), var15);
                  if (var24.name().equals("blade")) {
                     we.P(var1, var2, var24.vertices(), var15, var6, var3, var19.L == 3, var7);
                  }

                  if (var24.tint()) {
                     float[] var25 = z[var9.u()];
                     int var26 = class_765.method_23687(Math.max(class_765.method_24186(var8), 11), class_765.method_24187(var8));
                     var7.method_73483(
                        var6, Z(w1.A(0), var9.Y.getValue() && var2.method_7958()), (var6x, var7x) -> R(var24, var6x, var7x, var26, var15, 0, var25, var17)
                     );

                     for (int var27 = 1; var27 <= 2; var27++) {
                        int var28 = var27;
                        var7.method_73529(1)
                           .method_73483(
                              var6, class_12249.method_76014(w1.A(var27)), (var7x, var8x) -> R(var24, var7x, var8x, var26, var15, var28, var25, var17)
                           );
                     }
                  } else {
                     var7.method_73483(
                        var6, Z(w1.T(var11), var9.Y.getValue() && var2.method_7958()), (var3x, var4x) -> T(var24, var3x, var4x, var8, var15, false)
                     );
                  }

                  if (var9.Y.getValue() && var2.method_7958()) {
                     var7.method_73529(2).method_73483(var6, d(), (var3x, var4x) -> T(var24, var3x, var4x, var8, var15, true));
                  }
               } finally {
                  var6.method_22909();
               }
            }

            return true;
         } finally {
            var6.method_22909();
         }
      } else {
         return false;
      }
   }

   private static boolean w(
      class_742 var0,
      class_1268 var1,
      class_1306 var2,
      int var3,
      class_1799 var4,
      float var5,
      float var6,
      float var7,
      class_4587 var8,
      class_11659 var9,
      int var10,
      SwordModelsModule var11,
      g_ var12
   ) {
      int var13 = var0.method_6115() && var0.method_6058() == var1 && !SwordModelsModule.C(var4) ? 1 : 0;
      if (var13 != 0) {
         var11.l(var1, var4);
      }

      if (var13 == 0 && !var0.method_6123() && w6.t(var4).comp_4975() == class_12122.field_63399) {
         var8.method_22903();

         try {
            co var14 = var11.R();
            double var15 = qo.K() + Math.clamp(var7, 0.0F, 1.0F) * 0.05;
            float var17 = 0.0F;
            tz var18;
            if (var14 == co.o) {
               gs var19 = var11.F(var1);
               var19.Y(w6.z(var0) == var1, w6.P(var0), var15);
               var17 = var3 * var19.f(var15);
               int var20 = !SwordModelsModule.Z(var4, var0.method_5998(var1)) ? 1 : 0;
               var18 = sn.a(var3, var19.m(var11.x(var1, var4), var6, (boolean)var20), var11.R.getValue().floatValue());
            } else {
               var18 = sn.n(var3, var5, var6);
            }

            Vector3f var37 = new Vector3f(var3 * var11.N.getValue().floatValue(), var11.W.getValue().floatValue(), var11.O.getValue().floatValue());
            K(var8, var1, var3, var18.weapon().getTranslation(new Vector3f()).add(var37));
            var8.method_46416(var37.x, var37.y, var37.z);
            if (var14 == co.F) {
               sl var38 = var11.o(var1);
               if (!class_310.method_1551().method_1569()) {
                  var38.L();
               }

               fv.i var21 = var11.Y(var1).z(var38.j(var11.x(var1, var4), var14, var15, var5, false), var15);
               var18 = sn.S(var18, var3, var21.phase(), var21.weight());
            }

            if (!var0.method_5767()) {
               var8.method_22903();

               try {
                  em.L(var8, var18.arm());
                  var8.method_22905(0.72F, 0.72F, 0.72F);
                  j(var0, var1, var2, var3, var14, var8, var9, var10);
               } finally {
                  var8.method_22909();
               }
            }

            em.L(var8, var18.weapon());

            for (g_.c var40 : var12.parts()) {
               var8.method_22903();

               try {
                  if (var40.name().equals("case")) {
                     sn.l(var8, var11.R.getValue().floatValue());
                  }

                  int var22 = var14 != co.o && !var40.name().equals("case") ? var3 : 1;
                  bw.k(var1, var3 < 0, var8, var40.vertices(), var22);
                  if (var17 != 0.0F && var40.name().equals("swivel")) {
                     var8.method_34425(sn.c(var40.x(), var40.y(), var40.z(), var17));
                  }

                  var9.method_73483(
                     var8, Z(w1.T(var14), var11.Y.getValue() && var4.method_7958()), (var3x, var4x) -> T(var40, var3x, var4x, var10, var22, false)
                  );
                  if (var11.Y.getValue() && var4.method_7958()) {
                     var9.method_73529(1).method_73483(var8, d(), (var3x, var4x) -> T(var40, var3x, var4x, var10, var22, true));
                  }
               } finally {
                  var8.method_22909();
               }
            }

            return true;
         } finally {
            var8.method_22909();
         }
      } else {
         return false;
      }
   }

   private static void K(class_4587 var0, class_1268 var1, int var2, Vector3f var3) {
      ViewModelModule var4 = ViewModelModule.B();
      if (var4 != null && var4.isRuntimeActive()) {
         var4.K(var0, var1, var2 < 0);
         var0.method_46416(var3.x, var3.y, var3.z);
         var4.R(var0, var1, var2 < 0);
         var0.method_46416(-var3.x, -var3.y, -var3.z);
      }
   }

   private static void j(class_742 var0, class_1268 var1, class_1306 var2, int var3, co var4, class_4587 var5, class_11659 var6, int var7) {
      class_1007 var8 = class_310.method_1551().method_1561().method_74405(var0);
      var5.method_22903();

      try {
         int var9 = var0.method_52814().comp_1629() == class_7920.field_41122 ? 1 : 0;
         sy.M(var5, var4, var3, (boolean)var9);
         class_2960 var10 = var0.method_52814().comp_1626().comp_3627();
         if (var2 == class_1306.field_6183) {
            var8.method_4220(var5, var6, var7, var10, var0.method_74091(class_1664.field_7570));
         } else {
            var8.method_4221(var5, var6, var7, var10, var0.method_74091(class_1664.field_7568));
         }

         class_591 var11 = (class_591)var8.method_4038();
         bw.f(var1, var3 < 0, var5, var2 == class_1306.field_6183 ? var11.field_3401 : var11.field_27433);
         sg.D(var2, var5, var6, true);
      } finally {
         var5.method_22909();
      }
   }

   private static void T(g_.c var0, class_4665 var1, class_4588 var2, int var3, int var4, boolean var5) {
      float[] var6 = var0.vertices();

      for (byte var7 = 0; var7 < var6.length; var7 += 32) {
         for (int var8 = 0; var8 < 4; var8++) {
            int var9 = var7 + g_.vertexIndex(var4, var8) * 8;
            var2.method_56824(var1, var4 * var6[var9], var6[var9 + 1], var6[var9 + 2])
               .method_39415(-1)
               .method_22913(var5 ? var4 * var6[var9] : var6[var9 + 3], var5 ? var6[var9 + 1] : var6[var9 + 4])
               .method_22922(class_4608.field_21444)
               .method_60803(var3)
               .method_60831(var1, var4 * var6[var9 + 5], var6[var9 + 6], var6[var9 + 7]);
         }
      }
   }

   static void R(g_.c var0, class_4665 var1, class_4588 var2, int var3, int var4, int var5, float[] var6, double var7) {
      float[] var9 = var0.vertices();
      float[] var10 = var0.finishUv();
      int[] var11 = var0.finishColor();
      float var12 = h[var5];
      float var13 = (float)(var7 * P[var5] % 1.0) + (var5 == 1 ? 0.37F : 0.0F);
      float var14 = (float)(var7 * D[var5] % 1.0);
      float var15 = (float)(var7 % 3600.0);

      for (byte var16 = 0; var16 < var9.length; var16 += 32) {
         for (int var17 = 0; var17 < 4; var17++) {
            int var18 = var16 + g_.vertexIndex(var4, var17) * 8;
            float var19 = var10[var18 / 4];
            float var20 = var10[var18 / 4 + 1];
            int var21;
            if (var5 == 0) {
               float var22 = 1.25F * (var11[var18 / 8] & 0xFF) / 255.0F;
               var21 = E(1.0F, var6[0] * var22, var6[1] * var22, var6[2] * var22);
            } else if (var5 == 1) {
               float var23 = 0.5F + 0.5F * (float)Math.sin(var20 * 7.0F - var15 * 1.3F);
               var21 = E(0.85F, var6[3] + (var6[6] - var6[3]) * var23, var6[4] + (var6[7] - var6[4]) * var23, var6[5] + (var6[8] - var6[5]) * var23);
            } else {
               var21 = E(0.75F + 0.25F * (float)Math.sin(var20 * 23.0F + var19 * 11.0F - var15 * 3.1F), 1.0F, 1.0F, 1.0F);
            }

            var2.method_56824(var1, var4 * var9[var18], var9[var18 + 1], var9[var18 + 2])
               .method_39415(var21)
               .method_22913(var19 * var12 + var13, var20 * var12 + var14)
               .method_22922(class_4608.field_21444)
               .method_60803(var3)
               .method_60831(var1, var4 * var9[var18 + 5], var9[var18 + 6], var9[var18 + 7]);
         }
      }
   }

   private static int E(float var0, float var1, float var2, float var3) {
      return Math.round(Math.clamp(var0, 0.0F, 1.0F) * 255.0F) << 24
         | Math.round(Math.clamp(var1, 0.0F, 1.0F) * 255.0F) << 16
         | Math.round(Math.clamp(var2, 0.0F, 1.0F) * 255.0F) << 8
         | Math.round(Math.clamp(var3, 0.0F, 1.0F) * 255.0F);
   }

   private ik() {
   }

   private static class_1921 Z(class_2960 var0, boolean var1) {
      return class_12249.method_75982(var0);
   }

   private static class_1921 d() {
      return class_12249.method_75995();
   }
}
