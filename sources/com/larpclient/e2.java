package com.larpclient;

import com.mojang.blaze3d.vertex.PoseStack;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public record e2(Matrix4f weapon, Matrix4f arm) {
   public static e2 sample(_r var0, yi var1, int var2, float var3, int var4, float var5) {
      PoseStack var6 = new PoseStack();
      var0.root().transform(var6, var2, var3, var4);
      float var7 = var0.scale() * var5;
      var6.translate(var4 * var1.r * var0.scale(), var1.K * var0.scale(), var1.i * var0.scale());
      var6.scale(var7, var7, var7);
      var6.translate(-var4 * var1.r, -var1.K, -var1.i);
      PoseStack var8 = new PoseStack();
      var0.arm().transform(var8, var2, var3, var4);
      if (var0.armFit() != null) {
         var0.armFit().translate(var8, var2, var3, var4);
      }

      Quaternionf var9 = eq.l(var0);
      Vector3f var10 = eq.K(var0, var1, var9, var5);
      fx.b(var8, new Quaternionf(var9.x, var4 * var9.y, var4 * var9.z, var9.w));
      var8.translate(var4 * var10.x, var10.y, var10.z);
      return new e2(new Matrix4f(var6.last().pose()), new Matrix4f(var8.last().pose()));
   }
}
