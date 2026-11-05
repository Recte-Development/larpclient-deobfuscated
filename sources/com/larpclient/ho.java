package com.larpclient;

import com.mojang.blaze3d.vertex.VertexConsumer;
import org.joml.Matrix4fc;

final class ho implements VertexConsumer {
   VertexConsumer e;
   int x;

   public VertexConsumer addVertex(float var1, float var2, float var3) {
      this.e.addVertex(var1, var2, var3);
      return this;
   }

   public VertexConsumer addVertex(Matrix4fc var1, float var2, float var3, float var4) {
      return this.addVertex(
         var1.m00() * var2 + var1.m10() * var3 + var1.m20() * var4 + var1.m30(),
         var1.m01() * var2 + var1.m11() * var3 + var1.m21() * var4 + var1.m31(),
         var1.m02() * var2 + var1.m12() * var3 + var1.m22() * var4 + var1.m32()
      );
   }

   public VertexConsumer setColor(int var1, int var2, int var3, int var4) {
      this.e.setColor(var1 * (this.x >>> 16 & 0xFF) / 255, var2 * (this.x >>> 8 & 0xFF) / 255, var3 * (this.x & 0xFF) / 255, var4 * (this.x >>> 24) / 255);
      return this;
   }

   public VertexConsumer setColor(int var1) {
      return this.setColor(var1 >>> 16 & 0xFF, var1 >>> 8 & 0xFF, var1 & 0xFF, var1 >>> 24);
   }

   public VertexConsumer setUv(float var1, float var2) {
      this.e.setUv(var1, var2);
      return this;
   }

   public VertexConsumer setUv1(int var1, int var2) {
      this.e.setUv1(var1, var2);
      return this;
   }

   public VertexConsumer setUv3(float var1, float var2) {
      this.e.setUv3(var1, var2);
      return this;
   }

   public VertexConsumer setUv2(int var1, int var2) {
      this.e.setUv2(var1, var2);
      return this;
   }

   public VertexConsumer setNormal(float var1, float var2, float var3) {
      this.e.setNormal(var1, var2, var3);
      return this;
   }

   public VertexConsumer setLineWidth(float var1) {
      this.e.setLineWidth(var1);
      return this;
   }
}
