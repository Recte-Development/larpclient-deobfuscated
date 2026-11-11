package com.larpclient.render.model;

import com.larpclient.render.entity.SwingTrail;
import com.larpclient.util.math.CatmullRomSpline;
import com.larpclient.util.render.ColorUtil;
import java.util.List;
import net.minecraft.world.phys.Vec3;

public final class TrailMeshBuilder {
   public static TrailMeshBuilder.TrailMesh buildMesh(List<SwingTrail.Sample> var0, double var1, TrailMeshBuilder.TrailStyle var3) {
      if (var0.size() >= 2 && !(var1 - ((SwingTrail.Sample)var0.getLast()).time() >= var3.duration)) {
         int var4 = var3.smoothing ? Math.clamp(var3.subdivisions, 1, 4) : 1;
         TrailMeshBuilder.VertexBuffer var5 = new TrailMeshBuilder.VertexBuffer(var0.size() * var4 * 12);
         TrailMeshBuilder.VertexBuffer var6 = new TrailMeshBuilder.VertexBuffer(var0.size() * var4 * 20 + var0.size() * 4);

         for (int var7 = 1; var7 < var0.size(); var7++) {
            SwingTrail.Sample var8 = (SwingTrail.Sample)var0.get(var7 - 1);
            SwingTrail.Sample var9 = (SwingTrail.Sample)var0.get(var7);
            if (var8.stroke() == var9.stroke() && !(var8.tip().distanceToSqr(var9.tip()) < 1.0E-9)) {
               SwingTrail.Sample var10 = var7 > 1 && ((SwingTrail.Sample)var0.get(var7 - 2)).stroke() == var8.stroke()
                  ? (SwingTrail.Sample)var0.get(var7 - 2)
                  : var8;
               SwingTrail.Sample var11 = var7 + 1 < var0.size() && ((SwingTrail.Sample)var0.get(var7 + 1)).stroke() == var8.stroke()
                  ? (SwingTrail.Sample)var0.get(var7 + 1)
                  : var9;
               int var12 = var7 - 1;

               while (var12 > 0 && ((SwingTrail.Sample)var0.get(var12 - 1)).stroke() == var8.stroke()) {
                  var12--;
               }

               int var13 = var7;

               while (var13 + 1 < var0.size() && ((SwingTrail.Sample)var0.get(var13 + 1)).stroke() == var8.stroke()) {
                  var13++;
               }

               TrailMeshBuilder.TrailPoint var14 = samplePoint(var10, var8, var9, var11, 0.0, var7 - 1 - var12, var13 - var12, var1, var3);

               for (int var15 = 1; var15 <= var4; var15++) {
                  double var16 = (double)var15 / var4;
                  TrailMeshBuilder.TrailPoint var18 = samplePoint(var10, var8, var9, var11, var16, var7 - 1 - var12 + var16, var13 - var12, var1, var3);
                  addSegment(var5, var6, var14, var18, var3, var8.serial() + (var9.serial() - var8.serial()) * var16);
                  var14 = var18;
               }

               if (var3.sparks && var9.serial() % 7L == 0L && var7 > 1) {
                  addSpark(var6, var8, var9, var1, var3);
               }
            }
         }

         return new TrailMeshBuilder.TrailMesh(var5, var6);
      } else {
         return TrailMeshBuilder.TrailMesh.EMPTY;
      }
   }

   private static TrailMeshBuilder.TrailPoint samplePoint(
      SwingTrail.Sample var0,
      SwingTrail.Sample var1,
      SwingTrail.Sample var2,
      SwingTrail.Sample var3,
      double var4,
      double var6,
      int var8,
      double var9,
      TrailMeshBuilder.TrailStyle var11
   ) {
      Vec3 var12 = var11.smoothing ? CatmullRomSpline.interpolate(var0.tip(), var1.tip(), var2.tip(), var3.tip(), var4) : var1.tip().lerp(var2.tip(), var4);
      Vec3 var13 = var11.smoothing
         ? CatmullRomSpline.interpolate(var0.root(), var1.root(), var2.root(), var3.root(), var4)
         : var1.root().lerp(var2.root(), var4);
      Vec3 var14 = var13.subtract(var12);
      double var15 = var14.length();
      if (var15 < 1.0E-8) {
         var14 = new Vec3(0.0, -1.0, 0.0);
      } else {
         var14 = var14.scale(1.0 / var15);
      }

      double var17 = Math.clamp((var9 - (var1.time() + (var2.time() - var1.time()) * var4)) / var11.duration, 0.0, 1.0);
      double var19 = Math.clamp(var6 / Math.max(1.0, var8 * 0.2), 0.0, 1.0);
      var19 = var19 * var19 * (3.0 - 2.0 * var19);
      double var21 = Math.pow(1.0 - var17, 1.65) * var19;
      double var23 = var15 * var11.width * (0.78 + 0.22 * Math.cos(var17 * 3.0)) * Math.sqrt(var21);
      if (var11.style.equals("Void")) {
         var23 *= 0.7 + 0.3 * (1.0 - var17);
      }

      int var25 = ColorUtil.lerpColor(var11.secondary, var11.primary, 1.0 - var17 * 0.8);
      if (var11.style.equals("Prismatic")) {
         int var26 = ColorUtil.hsbToRgb((float)((0.53 + var17 * 0.35 + var9 * 0.08) % 1.0), 0.5F, 1.0F);
         var25 = ColorUtil.lerpColor(var25, var26, 0.45);
      }

      return new TrailMeshBuilder.TrailPoint(var12, var14, var23, var25, var21 * var11.intensity);
   }

   private static void addSegment(
      TrailMeshBuilder.VertexBuffer var0,
      TrailMeshBuilder.VertexBuffer var1,
      TrailMeshBuilder.TrailPoint var2,
      TrailMeshBuilder.TrailPoint var3,
      TrailMeshBuilder.TrailStyle var4,
      double var5
   ) {
      if (!(var2.width < 1.0E-7) || !(var3.width < 1.0E-7)) {
         boolean var7 = var4.style.equals("Void");
         int var8 = var7 ? ColorUtil.lerpColor(var2.color, -16317420, 0.95) : var2.color;
         int var9 = var7 ? ColorUtil.lerpColor(var3.color, -16317420, 0.95) : var3.color;
         addGradientQuad(var0, var2, var3, 1.0, 0.8, var8, var9, 0.0, var7 ? 0.68 : 0.09);
         addGradientQuad(var0, var2, var3, 0.8, 0.12, var8, var9, var7 ? 0.68 : 0.09, var7 ? 0.85 : 0.32);
         addGradientQuad(var0, var2, var3, 0.12, 0.0, var8, var9, var7 ? 0.85 : 0.32, 0.05);
         double var10 = var4.style.equals("Prismatic") ? 0.009 : 0.014;
         addLightStrip(var1, var2, var3, 0.0, var10, ColorUtil.lerpColor(var2.color, -1, 0.72), ColorUtil.lerpColor(var3.color, -1, 0.72), 0.92);
         addLightStrip(var1, var2, var3, 0.56, var10 * 0.46, var2.color, var3.color, 0.34);
         if (var7) {
            addLightStrip(var1, var2, var3, 0.95, var10 * 0.65, var2.color, var3.color, 0.7);
         }

         if (var4.style.equals("Prismatic")) {
            double var12 = Math.sin(var5 * 1.3);
            double var14 = Math.max(0.0, var12) * Math.max(0.0, var12);
            addLightStrip(
               var1,
               var2,
               var3,
               0.84,
               var10 * 0.5,
               ColorUtil.lerpColor(var2.color, var4.secondary, 0.7),
               ColorUtil.lerpColor(var3.color, var4.secondary, 0.7),
               0.48 * var14
            );
         }

         if (var4.halo) {
            addLightStrip(var1, var2, var3, 0.0, var10 * 4.5, var2.color, var3.color, 0.085);
            addLightStrip(var1, var2, var3, 0.0, var10 * 2.5, var2.color, var3.color, 0.13);
         }
      }
   }

   private static void addGradientQuad(
      TrailMeshBuilder.VertexBuffer var0,
      TrailMeshBuilder.TrailPoint var1,
      TrailMeshBuilder.TrailPoint var2,
      double var3,
      double var5,
      int var7,
      int var8,
      double var9,
      double var11
   ) {
      addQuad(
         var0,
         var1.offsetPoint(var3),
         var2.offsetPoint(var3),
         var2.offsetPoint(var5),
         var1.offsetPoint(var5),
         ColorUtil.multiplyAlpha(var7, var1.opacity * var9),
         ColorUtil.multiplyAlpha(var8, var2.opacity * var9),
         ColorUtil.multiplyAlpha(var8, var2.opacity * var11),
         ColorUtil.multiplyAlpha(var7, var1.opacity * var11)
      );
   }

   private static void addLightStrip(
      TrailMeshBuilder.VertexBuffer var0,
      TrailMeshBuilder.TrailPoint var1,
      TrailMeshBuilder.TrailPoint var2,
      double var3,
      double var5,
      int var7,
      int var8,
      double var9
   ) {
      Vec3 var11 = var1.offsetPoint(var3);
      Vec3 var12 = var2.offsetPoint(var3);
      Vec3 var13 = var1.axis.scale(var5 * Math.sqrt(Math.max(0.0, var1.opacity)) * 0.5);
      Vec3 var14 = var2.axis.scale(var5 * Math.sqrt(Math.max(0.0, var2.opacity)) * 0.5);
      int var15 = ColorUtil.multiplyAlpha(var7, var1.opacity * var9);
      int var16 = ColorUtil.multiplyAlpha(var8, var2.opacity * var9);
      addQuad(var0, var11.add(var13), var12.add(var14), var12.subtract(var14), var11.subtract(var13), var15, var16, var16, var15);
   }

   private static void addSpark(
      TrailMeshBuilder.VertexBuffer var0, SwingTrail.Sample var1, SwingTrail.Sample var2, double var3, TrailMeshBuilder.TrailStyle var5
   ) {
      double var6 = var3 - var2.time();
      double var8 = Math.min(0.3, var5.duration * 1.2);
      if (!(var6 < 0.0) && !(var6 >= var8)) {
         Vec3 var10 = var2.tip().subtract(var1.tip());
         if (!(var10.lengthSqr() < 1.0E-4)) {
            Vec3 var11 = var2.root().subtract(var2.tip()).normalize();
            double var12 = (var2.serial() & 1L) == 0L ? -1.0 : 1.0;
            Vec3 var14 = var10.normalize().scale(0.3).add(var11.cross(new Vec3(0.0, 0.0, 1.0)).scale(var12 * 0.25));
            Vec3 var15 = var2.tip().add(var14.scale(var6)).add(0.0, -0.18 * var6 * var6, 0.0);
            Vec3 var16 = var15.subtract(var14.scale(0.035));
            Vec3 var17 = new Vec3(-var14.y, var14.x, 0.0).normalize().scale(0.0025 * (1.0 - var6 / var8));
            int var18 = ColorUtil.multiplyAlpha(ColorUtil.lerpColor(var5.primary, -1, 0.55), (1.0 - var6 / var8) * var5.intensity * 0.8);
            addQuad(var0, var15.add(var17), var16.add(var17), var16.subtract(var17), var15.subtract(var17), var18, 0, 0, var18);
         }
      }
   }

   private static void addQuad(TrailMeshBuilder.VertexBuffer var0, Vec3 var1, Vec3 var2, Vec3 var3, Vec3 var4, int var5, int var6, int var7, int var8) {
      var0.addVertex(var1, var5);
      var0.addVertex(var2, var6);
      var0.addVertex(var3, var7);
      var0.addVertex(var4, var8);
   }

   private TrailMeshBuilder() {
   }

   public record TrailMesh(TrailMeshBuilder.VertexBuffer body, TrailMeshBuilder.VertexBuffer light) {
      public static final TrailMeshBuilder.TrailMesh EMPTY = new TrailMeshBuilder.TrailMesh(
         new TrailMeshBuilder.VertexBuffer(0), new TrailMeshBuilder.VertexBuffer(0)
      );

      public int vertices() {
         return this.body.getColor() + this.light.getColor();
      }
   }

   private record TrailPoint(Vec3 tip, Vec3 axis, double width, int color, double opacity) {
      Vec3 offsetPoint(double var1) {
         return this.tip.add(this.axis.scale(this.width * var1));
      }
   }

   public record TrailStyle(
      String style,
      double duration,
      double width,
      double intensity,
      int primary,
      int secondary,
      boolean halo,
      boolean sparks,
      boolean smoothing,
      int subdivisions
   ) {
   }

   public static final class VertexBuffer {
      private final float[] positions;
      private final int[] colors;
      private int vertexCount;

      private VertexBuffer(int var1) {
         this.positions = new float[var1 * 3];
         this.colors = new int[var1];
      }

      public int getColor() {
         return this.vertexCount;
      }

      public boolean addVertex() {
         return this.vertexCount == 0;
      }

      public float getX(int var1) {
         return this.positions[var1 * 3];
      }

      public float getY(int var1) {
         return this.positions[var1 * 3 + 1];
      }

      public float getZ(int var1) {
         return this.positions[var1 * 3 + 2];
      }

      public int getColor(int var1) {
         return this.colors[var1];
      }

      private void addVertex(Vec3 var1, int var2) {
         this.positions[this.vertexCount * 3] = (float)var1.x;
         this.positions[this.vertexCount * 3 + 1] = (float)var1.y;
         this.positions[this.vertexCount * 3 + 2] = (float)var1.z;
         this.colors[this.vertexCount++] = var2;
      }
   }
}
