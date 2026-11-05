package com.larpclient;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.util.Mth;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public final class rc {
   private static final Vector3f y = new Vector3f(0.0F, -0.375F, 0.21F);

   public static void m(PoseStack var0, int var1, float var2, float var3) {
      float var4 = Float.isFinite(var2) ? Math.clamp(var2, 0.0F, 1.0F) : 0.0F;
      float var5 = Float.isFinite(var3) ? Math.clamp(var3, 0.0F, 1.0F) : 0.0F;
      var0.translate(var1 * 0.56F, -0.52F - var5 * 0.6F, -0.72F);
      float var6 = Mth.sqrt(var4);
      var0.translate(var1 * -0.4F * Mth.sin(var6 * (float) Math.PI), 0.2F * Mth.sin(var6 * (float) (Math.PI * 2)), -0.2F * Mth.sin(var4 * (float) Math.PI));
      float var7 = Mth.sin(var4 * var4 * (float) Math.PI);
      float var8 = Mth.sin(var6 * (float) Math.PI);
      fx.b(var0, Axis.YP.rotationDegrees(var1 * (45.0F + var7 * -20.0F)));
      fx.b(var0, Axis.ZP.rotationDegrees(var1 * var8 * -20.0F));
      fx.b(var0, Axis.XP.rotationDegrees(var8 * -80.0F));
      fx.b(var0, Axis.YP.rotationDegrees(var1 * -45));
   }

   public static e2 p(int var0, float var1, float var2) {
      PoseStack var3 = new PoseStack();
      m(var3, var0, var1, var2);
      var3.translate(var0 * -0.25F, 0.35F, -0.18F);
      Quaternionf var4 = new Quaternionf().rotationXYZ((float)Math.toRadians(-50.0), 0.0F, (float)Math.toRadians(25.0));
      fx.b(var3, new Quaternionf(var4.x, var0 * var4.y, var0 * var4.z, var4.w));
      PoseStack var5 = new PoseStack();
      fx.Z(var5, var3.last().pose());
      var5.scale(0.72F, 0.72F, 0.72F);
      fx.b(var5, Axis.ZP.rotation(var0 * 0.1F));
      return new e2(new Matrix4f(var5.last().pose()), new Matrix4f(var3.last().pose()));
   }

   public static e2 A(int var0, float var1, float var2, float var3) {
      Matrix4f var4 = p(var0, var1, var2).arm();
      PoseStack var5 = new PoseStack();
      m(var5, var0, var1, var2);
      var5.translate(var0 * -0.25F, 0.35F, -0.18F);
      Quaternionf var6 = new Quaternionf().rotationXYZ((float)Math.toRadians(15.0), (float)Math.toRadians(-20.0), (float)Math.toRadians(15.0));
      fx.b(var5, new Quaternionf(var6.x, var0 * var6.y, var0 * var6.z, var6.w));
      var5.scale(0.72F * var3, 0.72F * var3, 0.72F * var3);
      return new e2(new Matrix4f(var5.last().pose()), new Matrix4f(var4));
   }

   public static float X(float var0) {
      float var1 = Float.isFinite(var0) ? Math.clamp(var0, 0.0F, 1.0F) : 0.0F;
      float var2 = e(Math.clamp(var1 / 0.22F, 0.0F, 1.0F));
      float var3 = e(Math.clamp((1.0F - var1) / 0.22F, 0.0F, 1.0F));
      return var2 * var3;
   }

   public static e2 a(e2 var0, int var1, float var2, float var3) {
      float var4 = Float.isFinite(var3) ? Math.clamp(var3, 0.0F, 1.0F) : 0.0F;
      if (var4 <= 0.0F) {
         return var0;
      }

      float var5 = Float.isFinite(var2) ? Math.clamp(var2, 0.0F, 1.0F) : 0.0F;
      Matrix4f var6 = var0.weapon();
      Vector3f var7 = var6.transformPosition(new Vector3f(y));
      Matrix3f var8 = R(var6.transformDirection(new Vector3f(0.0F, 0.0F, 1.0F)), var6.transformDirection(new Vector3f(0.0F, -1.0F, 0.0F)));
      Vector3f var9 = new Vector3f(var1 * 0.09F, -0.18F, -0.5F);
      Vector3f var10 = new Vector3f(var1 * 0.72F, -0.5F, 0.48F);
      Matrix3f var11 = R(new Vector3f(var9).negate().add(0.0F, 0.1F, 0.0F), var10);
      Quaternionf var12 = new Quaternionf().setFromNormalized(var11.mul(var8.transpose(new Matrix3f()), new Matrix3f()));
      Quaternionf var13 = new Quaternionf().slerp(var12, var4);
      float var14 = Math.clamp((var5 - 0.22F) / 0.56F, 0.0F, 1.0F);
      float var15 = (float)Math.toRadians(11.0) * (float)Math.sin(var14 * Math.PI * 2.0);
      Vector3f var16 = var13.transform(var6.transformDirection(new Vector3f(0.0F, -1.0F, 0.0F)).normalize());
      var13.premul(new Quaternionf().rotationAxis(var1 * var15 * var4, var16));
      float var17 = 0.012F * (float)Math.sin(var14 * Math.PI) * var4;
      Vector3f var18 = new Vector3f(var7).lerp(var9, var4).add(0.0F, var17, 0.0F);
      Matrix4f var19 = new Matrix4f().translation(var18).rotate(var13).translate(-var7.x, -var7.y, -var7.z);
      return new e2(new Matrix4f(var19).mul(var0.weapon()), new Matrix4f(var19).mul(var0.arm()));
   }

   private static Matrix3f R(Vector3f var0, Vector3f var1) {
      Vector3f var2 = new Vector3f(var0).normalize();
      Vector3f var3 = new Vector3f(var1).sub(new Vector3f(var2).mul(var2.dot(var1))).normalize();
      return new Matrix3f(var2, var3, new Vector3f(var2).cross(var3));
   }

   private static float e(float var0) {
      return var0 * var0 * var0 * (var0 * (var0 * 6.0F - 15.0F) + 10.0F);
   }

   public static void p(PoseStack var0, float var1) {
      var0.translate(0.0F, -0.375F, 0.165F);
      var0.scale(var1, var1, var1);
      var0.translate(0.0F, 0.375F, -0.165F);
   }

   private rc() {
   }
}
