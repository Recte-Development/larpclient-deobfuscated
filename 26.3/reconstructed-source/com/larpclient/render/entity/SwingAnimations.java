package com.larpclient.render.entity;

import com.larpclient.util.render.PoseStackUtil;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;

public final class SwingAnimations {
   public static final String[] swingModes = new String[]{
      "Mode 1",
      "Mode 2",
      "Mode 3",
      "Mode 4",
      "Mode 5",
      "Module 1",
      "Module 2",
      "Module 3",
      "Module 4",
      "Module 5",
      "Module 6",
      "Module 7",
      "Module 8",
      "360",
      "Slant",
      "Triangle",
      "Shift",
      "Break",
      "Lunge",
      "Spear",
      "Down",
      "Jelly",
      "Shake"
   };

   private SwingAnimations() {
   }

   public static float clamp01(float var0) {
      return Float.isFinite(var0) ? Math.clamp(var0, 0.0F, 1.0F) : 0.0F;
   }

   public static int scaleDuration(int var0, double var1) {
      double var3 = Double.isFinite(var1) ? Math.clamp(var1, 0.25, 3.0) : 1.0;
      return Math.max(1, (int)Math.round(var0 * var3));
   }

   public static float ease(float var0, boolean var1, float var2, float var3) {
      float var4 = clamp01(var0);
      if (Math.abs(var3 - 1.0F) > 0.001F) {
         var4 = (float)Math.pow(var4, var3);
      }

      if (!var1) {
         return var4;
      }

      float var5 = var4 < 0.5F ? 4.0F * var4 * var4 * var4 : 1.0F - (float)Math.pow(-2.0F * var4 + 2.0F, 3.0) / 2.0F;
      return var4 + (var5 - var4) * Math.clamp(var2, 0.0F, 1.0F);
   }

   private static void rotateX(PoseStack var0, float var1) {
      PoseStackUtil.rotate(var0, Axis.XP.rotationDegrees(var1));
   }

   private static void rotateY(PoseStack var0, float var1) {
      PoseStackUtil.rotate(var0, Axis.YP.rotationDegrees(var1));
   }

   private static void rotateZ(PoseStack var0, float var1) {
      PoseStackUtil.rotate(var0, Axis.ZP.rotationDegrees(var1));
   }

   private static void translateToHand(PoseStack var0, float var1) {
      var0.translate(var1 * 0.56F, -0.52F, -0.72F);
   }

   private static void rotateZ(PoseStack var0, float var1, float var2) {
      var0.translate(
         var1 * -0.4F * (float)Math.sin(Math.sqrt(var2) * Math.PI),
         0.2F * (float)Math.sin(Math.sqrt(var2) * 2.0 * Math.PI),
         -0.2F * (float)Math.sin(var2 * Math.PI)
      );
   }

   private static void rotateY(PoseStack var0, float var1, float var2) {
      float var3 = (float)Math.sin(Math.sqrt(var2) * Math.PI);
      rotateY(var0, var1 * (45.0F - 20.0F * (float)Math.sin(var2 * var2 * Math.PI)));
      rotateZ(var0, var1 * -20.0F * var3);
      rotateX(var0, -80.0F * var3);
      rotateY(var0, var1 * -45.0F);
   }

   public static void applySwing(PoseStack var0, String var1, boolean var2, float var3, SwingAnimations.SwingParams var4, SwingAnimations.TriangleSpin var5) {
      float var6 = ease(var3, var4.smooth, var4.smoothAmount, var4.curve);
      float var7 = (float)Math.sin(Math.sqrt(var6) * Math.PI);
      float var8 = (float)Math.sin(var6 * Math.PI);
      float var9 = var2 ? -1.0F : 1.0F;
      float var10 = var4.strength * 10.0F;
      if (var1.equals("Module 8")) {
         rotateZ(var0, var9, var6);
      }

      translateToHand(var0, var9);
      if (!var1.equals("Mode 1") && !var1.equals("Module 8")) {
         switch (var1) {
            case "Mode 2":
               rotateX(var0, 50.0F);
               rotateY(var0, var9 * -60.0F);
               rotateZ(var0, var9 * (110.0F + var4.legacyStrength * var7));
               return;
            case "Mode 3":
               rotateX(var0, 50.0F);
               rotateY(var0, var9 * (-30.0F * (1.0F - var7) - 30.0F + (var4.legacyStrength - 20.0F) * var7));
               rotateZ(var0, var9 * 110.0F);
               return;
            case "Mode 4":
               rotateY(var0, var9 * 90.0F);
               rotateZ(var0, var9 * -30.0F);
               rotateX(var0, -80.0F - var4.legacyStrength * var8);
               return;
            case "Mode 5":
               rotateX(var0, -360.0F * var6);
               return;
            default:
               if (var4.down > 0.0F) {
                  var0.translate(0.0F, -var4.down * 0.18F * var8, 0.0F);
               }

               switch (var1) {
                  case "Module 1":
                     rotateY(var0, var9 * (-45.0F - 20.0F * var7));
                     rotateZ(var0, var9 * -20.0F * var7);
                     rotateX(var0, -80.0F * var7);
                     rotateY(var0, var9 * 45.0F);
                     rotateX(var0, -var10 * var8);
                     break;
                  case "Module 2":
                     var0.translate(0.0F, 0.15F, -0.3F - var4.distance);
                     rotateY(var0, var9 * 90.0F);
                     rotateZ(var0, var9 * -60.0F);
                     rotateX(var0, -var4.angle - var10 * var8);
                     break;
                  case "Module 3":
                     rotateX(var0, var6 * (float) Math.PI - var10 * var8);
                     break;
                  case "Module 4":
                     var0.translate(0.0F, 0.15F, -0.3F - var4.distance);
                     rotateY(var0, var9 * 70.0F);
                     rotateZ(var0, var9 * -30.0F);
                     var0.scale(0.9F, 0.9F, 0.9F);
                     rotateX(var0, -var4.angle - var10 * var8);
                     break;
                  case "Module 5":
                     rotateY(var0, var9 * 45.0F);
                     rotateX(var0, -20.0F * var8);
                     rotateZ(var0, var9 * -20.0F * var8);
                     rotateX(var0, -80.0F * var8);
                     rotateY(var0, var9 * -45.0F);
                     break;
                  case "Module 6":
                     rotateY(var0, var9 * (45.0F - 20.0F * var8));
                     rotateZ(var0, var9 * -20.0F * var8);
                     rotateX(var0, -80.0F * var8);
                     rotateY(var0, var9 * -45.0F);
                     rotateX(var0, -var10 * var8);
                     break;
                  case "Module 7":
                     var0.translate(0.0F, 0.0F, -0.1F * var4.strength * var8);
                     rotateY(var0, var9 * 75.0F);
                     rotateZ(var0, var9 * (-37.5F * var8 - 60.0F));
                     rotateX(var0, -75.0F);
                     break;
                  case "360":
                     rotateX(var0, -360.0F * var6);
                     break;
                  case "Slant":
                     var0.translate(0.0F, 0.0F, -(0.3F + var4.distance) * var8);
                     rotateX(var0, -35.0F * var8);
                     rotateZ(var0, var9 * 35.0F * var8);
                     break;
                  case "Triangle":
                     float var16 = var5.angleAt(clamp01(var3), var6);
                     var0.translate(0.0F, 0.02F * var8, -var4.distance * 0.35F * var8);
                     rotateY(var0, var9 * 45.0F);
                     rotateX(var0, -var16);
                     rotateZ(var0, var9 * 10.0F * var8);
                     rotateY(var0, var9 * -45.0F);
                     break;
                  case "Shift":
                  case "Break":
                     var0.translate(var9 * 0.18F, 0.17F, -0.35F - var4.distance);
                     rotateY(var0, var9 * 90.0F);
                     rotateZ(var0, var9 * (var1.equals("Shift") ? -60 : -30));
                     rotateX(var0, -90.0F - var10 * var8 + var4.turn);
                     break;
                  case "Lunge":
                     var0.translate(var9 * (0.22F + 0.2F * var8), -0.04F + 0.1F * var8, -0.35F - var4.distance - 0.28F * var8);
                     rotateY(var0, var9 * (45.0F - var8 * var10 * 0.35F));
                     rotateZ(var0, var9 * var8 * -var10 * 0.3F);
                     rotateX(var0, -var8 * var10);
                     rotateY(var0, var9 * 30.0F);
                     break;
                  case "Spear":
                     var0.translate(0.0F, 0.07F, -0.18F - var4.distance - 0.22F * var8);
                     var0.scale(1.0F, 1.0F, 1.0F + var8 * var4.strength * 0.03F);
                     rotateX(var0, -90.0F);
                     rotateY(var0, var9 * 20.0F);
                     break;
                  case "Down":
                     var0.translate(var9 * -0.18F * var8, 0.05F + 0.18F * var8, -0.22F - var4.distance);
                     rotateY(var0, var9 * 65.0F);
                     rotateZ(var0, var9 * 5.0F);
                     rotateX(var0, -110.0F - 80.0F * var8);
                     break;
                  case "Jelly":
                     var0.translate(0.0F, 0.04F, -0.2F - var4.distance - 0.2F * var8);
                     rotateY(var0, var9 * 35.0F);
                     var0.scale(1.0F, 1.0F, 1.0F + var8 * var6 * var4.strength * 0.06F);
                     rotateX(var0, -90.0F);
                     break;
                  case "Shake":
                     float var13 = (float)Math.sin(var6 * Math.PI * 8.0) * var4.strength;
                     var0.translate(0.0F, 0.04F * var8, -0.2F - var4.distance * 0.5F);
                     rotateY(var0, var9 * (110.0F + var13));
                     rotateX(var0, -110.0F);
                     rotateY(var0, var9 * 75.0F);
                     rotateX(var0, 35.0F + var13);
                     break;
                  default:
                     throw new IllegalArgumentException("Unknown swing mode: " + var1);
               }
         }
      } else {
         rotateY(var0, var9, var6);
      }
   }

   public record SwingParams(
      float legacyStrength, float strength, float angle, float turn, float down, float distance, boolean smooth, float smoothAmount, float curve
   ) {
   }

   public static final class TriangleSpin {
      private boolean armed = (boolean)1;
      private int nextStep;
      private int currentStep;

      public float angleAt(float var1, float var2) {
         if (var1 < 0.03F) {
            this.armed = (boolean)1;
            return this.nextStep * 120.0F;
         }

         if (this.armed && var1 > 0.08F) {
            this.currentStep = this.nextStep;
            this.nextStep = (this.nextStep + 1) % 3;
            this.armed = (boolean)0;
         }

         return this.currentStep * 120.0F + var2 * 120.0F;
      }

      public void reset() {
         this.armed = (boolean)1;
         this.nextStep = this.currentStep = 0;
      }
   }
}
