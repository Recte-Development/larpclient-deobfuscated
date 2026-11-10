package com.larpclient;

import net.minecraft.class_4587;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public record tz(Matrix4f weapon, Matrix4f arm) {
   public static tz sample(g_ var0, co var1, int var2, float var3, int var4, float var5) {
      class_4587 var6 = new class_4587();
      var0.root().transform(var6, var2, var3, var4);
      float var7 = var0.scale() * var5;
      var6.method_46416(var4 * var1.r * var0.scale(), var1.y * var0.scale(), var1.S * var0.scale());
      var6.method_22905(var7, var7, var7);
      var6.method_46416(-var4 * var1.r, -var1.y, -var1.S);
      class_4587 var8 = new class_4587();
      var0.arm().transform(var8, var2, var3, var4);
      if (var0.armFit() != null) {
         var0.armFit().translate(var8, var2, var3, var4);
      }

      Quaternionf var9 = sy.l(var0);
      Vector3f var10 = sy.Q(var0, var1, var9, var5);
      em.W(var8, new Quaternionf(var9.x, var4 * var9.y, var4 * var9.z, var9.w));
      var8.method_46416(var4 * var10.x, var10.y, var10.z);
      return new tz(new Matrix4f(var6.method_23760().method_23761()), new Matrix4f(var8.method_23760().method_23761()));
   }
}
