package com.larpclient;

import net.minecraft.class_4587;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public final class qv {
   public static Matrix4f m(Matrix4f var0, ef.b var1, float var2, float var3, float var4, float var5, float var6, float[] var7, float var8) {
      var0.identity().translate(var2, var3, var4).rotateY(-var5);
      if (var8 > 0.0F) {
         ef.U(var0, var7, var8);
      }

      return var0.scale(var6 * 1.8F / var1.height());
   }

   public static void e(class_4587 var0, ef.b var1, Vector3f var2, boolean var3, float var4, boolean var5, float var6, float[] var7, float var8) {
      float var9 = 1.8F / var1.height() * var4;
      float var10 = var6 * 0.9375F;
      Vector3f var11 = new Vector3f(0.0F, var5 ? var6 * 0.125F : 0.0F, 0.0F);
      if (var5 && var8 > 0.0F) {
         ef.U(new Matrix4f(), var7, var8).invert().transformDirection(var11);
      }

      float var12 = var2.x * var9 + var11.x;
      float var13 = var2.y * var9 + var11.y;
      float var14 = var2.z * var9 + var11.z;
      var0.method_22904(var12 / var10, 1.501 - var13 / var10, -var14 / var10);
      var0.method_46416((var3 ? 1 : -1) / 16.0F, -0.625F, 0.125F);
   }

   private qv() {
   }
}
