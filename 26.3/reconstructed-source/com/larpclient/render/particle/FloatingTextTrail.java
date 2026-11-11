package com.larpclient.render.particle;

import com.larpclient.render.entity.SwayAnimator;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.ToDoubleFunction;
import org.joml.Quaterniond;
import org.joml.Quaterniondc;
import org.joml.Vector3d;

public final class FloatingTextTrail {
   public static final int maxTrailLength = 32;
   public static final double glyphScale = 0.01890625;
   private final ArrayDeque<FloatingTextTrail.FadingText> fadingTexts = new ArrayDeque<>();
   private final Quaterniond rotation = new Quaterniond();
   private final Vector3d cameraPos = new Vector3d();
   private final Vector3d offset = new Vector3d();
   private String currentText = "";
   private FloatingTextTrail.TextFrame current;
   private int trailLength = 16;
   private int spawnCount;
   private int revision = -1;
   private boolean placed;
   private double fadeProgress;
   private double age;
   private double side = 1.0;
   private double lateralBias;
   private double verticalBias;
   private double distance = 8.0;
   private FloatingTextTrail.TextFrame anchor;
   private double maxDistance = 8.0;

   public void reset() {
      this.placed = (boolean)0;
      this.fadeProgress = this.age = 0.0;
      this.currentText = "";
      this.revision = -1;
      this.spawnCount = 0;
      this.current = null;
      this.anchor = null;
      this.maxDistance = 8.0;
      this.fadingTexts.clear();
   }

   public void setTrailLength(int var1) {
      this.trailLength = Math.clamp(var1, 4, 32);
      this.trimTrail();
   }

   public void setMaxDistance(double var1) {
      this.maxDistance = Math.clamp(var1, 0.6, 8.0);
   }

   private void trimTrail() {
      int var1 = 1 + (this.trailLength - 4) * 3 / 28;

      while (this.fadingTexts.size() > var1) {
         this.fadingTexts.removeLast();
      }
   }

   public void update(List<String> var1) {
      String var2 = var1.isEmpty() ? "" : (String)var1.getFirst();
      this.updateWithRevision(var1, this.currentText.equals(var2) ? this.revision : this.revision + 1);
   }

   public void updateWithRevision(List<String> var1, int var2) {
      String var3 = var1.isEmpty() ? "" : (String)var1.getFirst();
      if (!this.currentText.equals(var3) || this.revision != var2) {
         if (this.current != null && this.current.opacity >= 0.00392156862745098) {
            this.fadingTexts.addFirst(new FloatingTextTrail.FadingText(this.current));
         }

         this.trimTrail();
         this.currentText = var3;
         this.revision = var2;
         this.spawnCount++;
         this.side = this.spawnCount % 2 == 0 ? -1.0 : 1.0;
         this.lateralBias = this.side * 0.1;
         this.verticalBias = 0.17 + 0.035 * Math.sin(this.spawnCount * 2.399);
         this.distance = Math.min(7.5 + 0.6 * Math.sin(this.spawnCount * 1.7), this.maxDistance);
         this.fadeProgress = this.age = 0.0;
         this.placed = (boolean)0;
         this.current = null;
      }
   }

   public List<FloatingTextTrail.TextFrame> computeFrames(
      double var1,
      double var3,
      double var5,
      Quaterniondc var7,
      double var8,
      double var10,
      double var12,
      double var14,
      double var16,
      ToDoubleFunction<String> var18
   ) {
      var16 = Math.clamp(var16, 0.0, 0.1);
      this.cameraPos.set(var1, var3, var5);
      ArrayList var19 = new ArrayList(5);
      double var20 = 0.65 + this.trailLength / 32.0 * 0.65;
      Iterator var22 = this.fadingTexts.iterator();

      while (var22.hasNext()) {
         FloatingTextTrail.FadingText var23 = (FloatingTextTrail.FadingText)var22.next();
         var23.elapsed += var16;
         if (var23.elapsed >= var20) {
            var22.remove();
         } else {
            double var24 = SwayAnimator.smoothstep(var23.elapsed / var20);
            FloatingTextTrail.TextFrame var26 = var23.frame;
            double var27 = var26.opacity * (1.0 - var24);
            if (var27 >= 0.00392156862745098) {
               var19.add(this.toWorldSpace(var26, var27, var26.age, var23.elapsed / var20));
            }
         }
      }

      if (this.currentText.isBlank()) {
         return List.copyOf(var19);
      }

      if (!this.placed) {
         double var33 = Math.clamp(Math.abs(var8), 0.1, 8.0);
         double var35 = Math.clamp(Math.abs(var10), 0.1, 8.0);
         double var36 = 0.7 * Math.clamp(var33 / var35, 0.25, 4.0);
         double var28 = 0.75 + Math.clamp(var14, 0.0, 2.2) * 0.25;
         this.offset.set(this.lateralBias * var33 * this.distance, this.verticalBias * var35 * this.distance, -this.distance).rotate(var7);
         this.rotation.set(var7).rotateXYZ(0.07 * Math.sin(this.spawnCount * 1.7), this.side * 0.16 * var28, this.side * 0.1 * var28);
         double var30 = 0.098 * this.distance / 0.75625 * var12;
         var30 = Math.min(var30, 1.3 * var36 * this.distance / (0.01890625 * Math.max(1.0, var18.applyAsDouble(this.currentText))));
         var30 = Math.min(var30, 0.154 * this.distance / 0.75625);
         this.anchor = new FloatingTextTrail.TextFrame(
            this.currentText,
            this.offset.x,
            this.offset.y,
            this.offset.z,
            var30,
            1.0,
            this.rotation.x,
            this.rotation.y,
            this.rotation.z,
            this.rotation.w,
            0.0,
            0.0
         );
         this.placed = (boolean)1;
      }

      this.fadeProgress = Math.min(1.0, this.fadeProgress + var16 / 0.28);
      this.age += var16;
      FloatingTextTrail.TextFrame var34 = new FloatingTextTrail.TextFrame(
         this.currentText,
         this.anchor.posX,
         this.anchor.posY,
         this.anchor.posZ,
         this.anchor.scale,
         SwayAnimator.smoothstep(this.fadeProgress),
         this.anchor.rotX,
         this.anchor.rotY,
         this.anchor.rotZ,
         this.anchor.rotW,
         this.age,
         0.0
      );
      this.current = var34;
      if (var34.opacity >= 0.00392156862745098) {
         var19.add(this.toWorldSpace(var34, var34.opacity, this.age, 0.0));
      }

      return List.copyOf(var19);
   }

   private FloatingTextTrail.TextFrame toWorldSpace(FloatingTextTrail.TextFrame var1, double var2, double var4, double var6) {
      return new FloatingTextTrail.TextFrame(
         var1.text,
         this.cameraPos.x + var1.posX,
         this.cameraPos.y + var1.posY,
         this.cameraPos.z + var1.posZ,
         var1.scale,
         var2,
         var1.rotX,
         var1.rotY,
         var1.rotZ,
         var1.rotW,
         var4,
         var6
      );
   }

   private static final class FadingText {
      final FloatingTextTrail.TextFrame frame;
      double elapsed;

      FadingText(FloatingTextTrail.TextFrame var1) {
         this.frame = var1;
      }
   }

   public record TextFrame(
      String text, double x, double y, double z, double scale, double opacity, double qx, double qy, double qz, double qw, double age, double exit
   ) {
      private final double posX;
      private final double posY;
      private final double posZ;
      private final double rotX;
      private final double rotY;
      private final double rotZ;
      private final double rotW;

      public TextFrame(
         String text, double x, double y, double z, double scale, double opacity, double qx, double qy, double qz, double qw, double age, double exit
      ) {
         this.text = text;
         this.posX = x;
         this.posY = y;
         this.posZ = z;
         this.scale = scale;
         this.opacity = opacity;
         this.rotX = qx;
         this.rotY = qy;
         this.rotZ = qz;
         this.rotW = qw;
         this.age = age;
         this.exit = exit;
      }
   }
}
