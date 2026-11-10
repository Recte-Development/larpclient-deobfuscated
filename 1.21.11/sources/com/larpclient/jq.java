package com.larpclient;

import com.mojang.blaze3d.pipeline.BlendFunction;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.pipeline.RenderPipeline.Snippet;
import com.mojang.blaze3d.platform.DepthTestFunction;
import net.minecraft.class_10799;
import net.minecraft.class_12075;
import net.minecraft.class_12247;
import net.minecraft.class_1921;
import net.minecraft.class_243;
import net.minecraft.class_2960;
import net.minecraft.class_4587;
import net.minecraft.class_4588;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public final class jq {
   private static final class_1921 B = class_1921.method_75940(
      "larp_glyphs",
      class_12247.method_75927(
            RenderPipeline.builder(new Snippet[]{class_10799.field_56860})
               .withLocation(class_2960.method_60655("larp-client", "larp/glyphs"))
               .withDepthTestFunction(DepthTestFunction.NO_DEPTH_TEST)
               .withDepthWrite(false)
               .withBlend(BlendFunction.LIGHTNING)
               .withCull(false)
               .build()
         )
         .method_75938()
   );
   private static final class_1921 l = class_1921.method_75940(
      "larp_pearls",
      class_12247.method_75927(
            RenderPipeline.builder(new Snippet[]{class_10799.field_56864})
               .withLocation(class_2960.method_60655("larp-client", "larp/pearls"))
               .withFragmentShader(class_2960.method_60655("larp-client", "larp/pearl"))
               .withDepthTestFunction(DepthTestFunction.LEQUAL_DEPTH_TEST)
               .withDepthWrite(false)
               .withBlend(BlendFunction.LIGHTNING)
               .withCull(false)
               .build()
         )
         .method_75934("Sampler0", class_2960.method_60655("larp-client", "shaders/larp/pearl_bloom.png"))
         .method_75938()
   );
   private static final int[] f = new int[]{-1, 1, 1, -1};
   private static final int[] X = new int[]{1, 1, -1, -1};
   private static int E = -1;
   private final cw L;
   private final Matrix4f y;
   private final class_243 S;
   private final Vector3f p;
   private final Vector3f t;

   public jq(class_4587 var1, cw var2, class_12075 var3) {
      if (E != t_.g) {
         f2.a(B.method_73243());
         f2.a(l.method_73243());
         E = t_.g;
      }

      this.L = var2;
      this.y = var1.method_23760().method_23761();
      this.S = var3.field_63078;
      this.p = new Vector3f(1.0F, 0.0F, 0.0F).rotate(var3.field_63081);
      this.t = new Vector3f(0.0F, 1.0F, 0.0F).rotate(var3.field_63081);
   }

   private void S(class_4588 var1, double var2, double var4, double var6, int var8) {
      var1.method_22918(this.y, (float)(var2 - this.S.field_1352), (float)(var4 - this.S.field_1351), (float)(var6 - this.S.field_1350)).method_39415(var8);
   }

   public void n(class_243 var1, class_243 var2, double var3, int var5, int var6) {
      double var7 = var2.field_1352 + -var1.field_1352;
      double var9 = var2.field_1351 + -var1.field_1351;
      double var11 = var2.field_1350 + -var1.field_1350;
      if (!(var7 * var7 + var9 * var9 + var11 * var11 < 1.0E-10)) {
         double var13 = this.S.field_1352 + -var1.field_1352;
         double var15 = this.S.field_1351 + -var1.field_1351;
         double var17 = this.S.field_1350 + -var1.field_1350;
         double var19 = var9 * var17 - var11 * var15;
         double var21 = var11 * var13 - var7 * var17;
         double var23 = var7 * var15 - var9 * var13;
         if (var19 * var19 + var21 * var21 + var23 * var23 < 1.0E-10) {
            var19 = this.p.x;
            var21 = this.p.y;
            var23 = this.p.z;
         }

         double var25 = Math.sqrt(var19 * var19 + var21 * var21 + var23 * var23);
         if (var25 < 1.0E-5F) {
            var23 = 0.0;
            var21 = 0.0;
            var19 = 0.0;
         } else {
            var19 /= var25;
            var21 /= var25;
            var23 /= var25;
         }

         double var27 = var3 * 0.5;
         var19 *= var27;
         var21 *= var27;
         var23 *= var27;
         class_4588 var29 = this.L.N(B);
         this.S(var29, var1.field_1352 + var19, var1.field_1351 + var21, var1.field_1350 + var23, var5);
         this.S(var29, var2.field_1352 + var19, var2.field_1351 + var21, var2.field_1350 + var23, var6);
         this.S(var29, var2.field_1352 + -var19, var2.field_1351 + -var21, var2.field_1350 + -var23, var6);
         this.S(var29, var1.field_1352 + -var19, var1.field_1351 + -var21, var1.field_1350 + -var23, var5);
      }
   }

   public void i(class_243 var1, double var2, int var4) {
      class_4588 var5 = this.L.N(l);
      float var6 = (float)(var2 * 0.5);

      for (int var7 = 0; var7 < 4; var7++) {
         float var8 = (float)(var1.field_1352 - this.S.field_1352) + (this.p.x * f[var7] + this.t.x * X[var7]) * var6;
         float var9 = (float)(var1.field_1351 - this.S.field_1351) + (this.p.y * f[var7] + this.t.y * X[var7]) * var6;
         float var10 = (float)(var1.field_1350 - this.S.field_1350) + (this.p.z * f[var7] + this.t.z * X[var7]) * var6;
         var5.method_22918(this.y, var8, var9, var10).method_22913(f[var7] < 0 ? 0.0F : 1.0F, X[var7] < 0 ? 0.0F : 1.0F).method_39415(var4);
      }
   }
}
