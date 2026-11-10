package com.larpclient;

import net.minecraft.class_4588;
import org.joml.Matrix4fc;

final class w0 implements class_4588 {
   class_4588 D;
   int g;

   public class_4588 method_22912(float var1, float var2, float var3) {
      this.D.method_22912(var1, var2, var3);
      return this;
   }

   public class_4588 method_22918(Matrix4fc var1, float var2, float var3, float var4) {
      return this.method_22912(
         var1.m00() * var2 + var1.m10() * var3 + var1.m20() * var4 + var1.m30(),
         var1.m01() * var2 + var1.m11() * var3 + var1.m21() * var4 + var1.m31(),
         var1.m02() * var2 + var1.m12() * var3 + var1.m22() * var4 + var1.m32()
      );
   }

   public class_4588 method_1336(int var1, int var2, int var3, int var4) {
      this.D.method_1336(var1 * (this.g >>> 16 & 0xFF) / 255, var2 * (this.g >>> 8 & 0xFF) / 255, var3 * (this.g & 0xFF) / 255, var4 * (this.g >>> 24) / 255);
      return this;
   }

   public class_4588 method_39415(int var1) {
      return this.method_1336(var1 >>> 16 & 0xFF, var1 >>> 8 & 0xFF, var1 & 0xFF, var1 >>> 24);
   }

   public class_4588 method_22913(float var1, float var2) {
      this.D.method_22913(var1, var2);
      return this;
   }

   public class_4588 method_60796(int var1, int var2) {
      this.D.method_60796(var1, var2);
      return this;
   }

   public class_4588 method_22921(int var1, int var2) {
      this.D.method_22921(var1, var2);
      return this;
   }

   public class_4588 method_22914(float var1, float var2, float var3) {
      this.D.method_22914(var1, var2, var3);
      return this;
   }

   public class_4588 method_75298(float var1) {
      this.D.method_75298(var1);
      return this;
   }
}
