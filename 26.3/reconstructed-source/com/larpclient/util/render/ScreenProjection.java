package com.larpclient.util.render;

import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;

public final class ScreenProjection {
   public static ScreenProjection.ProjectedPoint projectPoint(Vec3 var0, Vec3 var1, Matrix4f var2, Vector3f var3, int var4, int var5, boolean var6) {
      Vector4f var7 = var2.transform(new Vector4f((float)(var0.x - var1.x), (float)(var0.y - var1.y), (float)(var0.z - var1.z), 1.0F));
      if (!(var7.w <= 1.0E-4F) && !(var7.z > var7.w) && !(var7.z < (var6 ? 0.0F : -var7.w)) && !(Math.abs(var7.x) > var7.w) && !(Math.abs(var7.y) > var7.w)) {
         Vector4f var8 = var2.transform(new Vector4f(var3.x, var3.y, var3.z, 0.0F));
         float var9 = (var7.x / var7.w + 1.0F) * var4 * 0.5F;
         float var10 = (1.0F - var7.y / var7.w) * var5 * 0.5F;
         float var11 = var7.w + var8.w;
         if (var11 <= 1.0E-4F) {
            return null;
         }

         float var12 = ((var7.x + var8.x) / var11 - var7.x / var7.w) * var4 * 0.5F;
         float var13 = ((var7.y + var8.y) / var11 - var7.y / var7.w) * var5 * 0.5F;
         float var14 = (float)Math.hypot(var12, var13);
         return Float.isFinite(var9) && Float.isFinite(var10) && Float.isFinite(var14) && var14 > 0.0F
            ? new ScreenProjection.ProjectedPoint(var9, var10, var14)
            : null;
      } else {
         return null;
      }
   }

   public static ScreenProjection.IntBounds projectBoxBounds(AABB var0, Vec3 var1, Matrix4f var2, int var3, int var4, boolean var5) {
      ScreenProjection.FloatBounds var6 = projectBox(var0, var1, var2, var3, var4, var5);
      if (var6 == null) {
         return null;
      }

      int var7 = (int)Math.floor(var6.left());
      int var8 = (int)Math.ceil(var6.right());
      int var9 = (int)Math.floor(var6.top());
      int var10 = (int)Math.ceil(var6.bottom());
      return var8 - var7 >= 2 && var10 - var9 >= 3 ? new ScreenProjection.IntBounds(var7, var9, var8, var10) : null;
   }

   public static ScreenProjection.FloatBounds projectBox(AABB var0, Vec3 var1, Matrix4f var2, int var3, int var4, boolean var5) {
      if (var0.contains(var1)) {
         return null;
      }

      Vector4f[] var6 = new Vector4f[8];

      for (int var7 = 0; var7 < 8; var7++) {
         var6[var7] = var2.transform(
            new Vector4f(
               (float)(((var7 & 1) == 0 ? var0.minX : var0.maxX) - var1.x),
               (float)(((var7 & 2) == 0 ? var0.minY : var0.maxY) - var1.y),
               (float)(((var7 & 4) == 0 ? var0.minZ : var0.maxZ) - var1.z),
               1.0F
            )
         );
      }

      float var21 = Float.POSITIVE_INFINITY;
      float var8 = var21;
      float var9 = Float.NEGATIVE_INFINITY;
      float var10 = var9;

      for (int var11 = 0; var11 < 8; var11++) {
         for (byte var12 = 1; var12 <= 4; var12 <<= 1) {
            if ((var11 & var12) == 0) {
               Vector4f var13 = new Vector4f(var6[var11]);
               Vector4f var14 = new Vector4f(var6[var11 | var12]);
               if (clipSegment(var13, var14, var5)) {
                  for (Vector4f var18 : new Vector4f[]{var13, var14}) {
                     float var19 = (var18.x / var18.w + 1.0F) * var3 * 0.5F;
                     float var20 = (1.0F - var18.y / var18.w) * var4 * 0.5F;
                     var21 = Math.min(var21, var19);
                     var9 = Math.max(var9, var19);
                     var8 = Math.min(var8, var20);
                     var10 = Math.max(var10, var20);
                  }
               }
            }
         }
      }

      if (Float.isFinite(var21)
         && Float.isFinite(var9)
         && Float.isFinite(var8)
         && Float.isFinite(var10)
         && !(var9 < 0.0F)
         && !(var10 < 0.0F)
         && !(var21 > var3)
         && !(var8 > var4)) {
         float var22 = Math.clamp(var21, 0.0F, var3);
         float var23 = Math.clamp(var9, 0.0F, var3);
         float var24 = Math.clamp(var8, 0.0F, var4);
         float var25 = Math.clamp(var10, 0.0F, var4);
         return new ScreenProjection.FloatBounds(var22, var24, var23, var25);
      } else {
         return null;
      }
   }

   public static ScreenProjection.ProjectedLine projectLine(Vec3 var0, Vec3 var1, Vec3 var2, Matrix4f var3, int var4, int var5, boolean var6) {
      Vector4f var7 = var3.transform(new Vector4f((float)(var0.x - var2.x), (float)(var0.y - var2.y), (float)(var0.z - var2.z), 1.0F));
      Vector4f var8 = var3.transform(new Vector4f((float)(var1.x - var2.x), (float)(var1.y - var2.y), (float)(var1.z - var2.z), 1.0F));
      if (!clipSegment(var7, var8, var6)) {
         return null;
      }

      float var9 = (var7.x / var7.w + 1.0F) * var4 * 0.5F;
      float var10 = (1.0F - var7.y / var7.w) * var5 * 0.5F;
      float var11 = (var8.x / var8.w + 1.0F) * var4 * 0.5F;
      float var12 = (1.0F - var8.y / var8.w) * var5 * 0.5F;
      return Float.isFinite(var9)
            && Float.isFinite(var10)
            && Float.isFinite(var11)
            && Float.isFinite(var12)
            && !(Math.max(var9, var11) < 0.0F)
            && !(Math.max(var10, var12) < 0.0F)
            && !(Math.min(var9, var11) > var4)
            && !(Math.min(var10, var12) > var5)
         ? new ScreenProjection.ProjectedLine(var9, var10, var11, var12)
         : null;
   }

   private static boolean clipSegment(Vector4f var0, Vector4f var1, boolean var2) {
      for (int var3 = 0; var3 < 3; var3++) {
         float var4 = var3 == 0 ? var0.w - 1.0E-4F : (var3 == 1 ? (var2 ? var0.z : var0.w + var0.z) : var0.w - var0.z);
         float var5 = var3 == 0 ? var1.w - 1.0E-4F : (var3 == 1 ? (var2 ? var1.z : var1.w + var1.z) : var1.w - var1.z);
         if (var4 < 0.0F && var5 < 0.0F) {
            return false;
         }

         if (var4 < 0.0F) {
            var0.lerp(var1, var4 / (var4 - var5));
         } else if (var5 < 0.0F) {
            var1.lerp(var0, var5 / (var5 - var4));
         }
      }

      return true;
   }

   private ScreenProjection() {
   }

   public record FloatBounds(float left, float top, float right, float bottom) {
   }

   public record IntBounds(int left, int top, int right, int bottom) {
   }

   public record ProjectedLine(float x1, float y1, float x2, float y2) {
      private final float startX;
      private final float startY;
      private final float endX;
      private final float endY;

      public ProjectedLine(float x1, float y1, float x2, float y2) {
         this.startX = x1;
         this.startY = y1;
         this.endX = x2;
         this.endY = y2;
      }

      public float getStartX() {
         return this.startX;
      }

      public float getStartY() {
         return this.startY;
      }

      public float getEndX() {
         return this.endX;
      }

      public float getEndY() {
         return this.endY;
      }
   }

   public record ProjectedPoint(float x, float y, float scale) {
      private final float x2;
      private final float y2;

      public ProjectedPoint(float x, float y, float scale) {
         this.x2 = x;
         this.y2 = y;
         this.scale = scale;
      }

      public float getX() {
         return this.x2;
      }

      public float getY() {
         return this.y2;
      }
   }
}
