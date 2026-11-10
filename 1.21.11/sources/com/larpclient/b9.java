package com.larpclient;

import net.minecraft.class_4184;
import net.minecraft.class_4604;
import net.minecraft.class_5636;
import org.joml.Matrix4f;
import org.joml.Matrix4fc;
import org.joml.Vector4f;
import org.joml.Vector4fc;

public final class b9 {
   private static final Matrix4f w = new Matrix4f();
   private static final Matrix4f f = new Matrix4f();
   private static final Vector4f V = new Vector4f();
   private static class_5636 y = class_5636.field_27888;
   private static boolean c;
   private static float F;
   private static float T;
   private static class_4604 a;

   public static void X(float var0) {
      F = var0;
   }

   public static void x(float var0) {
      T = var0;
   }

   public static float V() {
      return T;
   }

   public static void W(class_4604 var0) {
      a = var0;
   }

   public static class_4604 k() {
      return a;
   }

   public static void T(class_4184 var0, Matrix4fc var1, Matrix4fc var2, Vector4fc var3) {
      w.set(var1);
      f.set(var2);
      V.set(var3);
      y = var0.method_19334();
      c = (boolean)1;
   }

   public static boolean G() {
      return c;
   }

   public static Matrix4fc T() {
      return w;
   }

   public static Matrix4fc v() {
      return f;
   }

   public static Matrix4f n(Matrix4f var0) {
      return var0.set(f).mul(w);
   }

   public static Vector4fc l() {
      return V;
   }

   public static class_5636 B() {
      return y;
   }

   public static float U() {
      return F;
   }

   private b9() {
   }
}
