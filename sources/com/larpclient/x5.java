package com.larpclient;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;

public final class x5 {
   public static final String[] B = new String[]{
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

   private x5() {
   }

   public static float o(float var0) {
      return Float.isFinite(var0) ? Math.clamp(var0, 0.0F, 1.0F) : 0.0F;
   }

   public static int n(int var0, double var1) {
      double var3 = Double.isFinite(var1) ? Math.clamp(var1, 0.25, 3.0) : 1.0;
      return Math.max(1, (int)Math.round(var0 * var3));
   }

   public static float h(float var0, boolean var1, float var2, float var3) {
      float var4 = o(var0);
      if (Math.abs(var3 - 1.0F) > 0.001F) {
         var4 = (float)Math.pow(var4, var3);
      }

      if (!var1) {
         return var4;
      }

      float var5 = var4 < 0.5F ? 4.0F * var4 * var4 * var4 : 1.0F - (float)Math.pow(-2.0F * var4 + 2.0F, 3.0) / 2.0F;
      return var4 + (var5 - var4) * Math.clamp(var2, 0.0F, 1.0F);
   }

   private static void D(PoseStack var0, float var1) {
      fx.b(var0, Axis.XP.rotationDegrees(var1));
   }

   private static void Z(PoseStack var0, float var1) {
      fx.b(var0, Axis.YP.rotationDegrees(var1));
   }

   private static void Q(PoseStack var0, float var1) {
      fx.b(var0, Axis.ZP.rotationDegrees(var1));
   }

   private static void J(PoseStack var0, float var1) {
      var0.translate(var1 * 0.56F, -0.52F, -0.72F);
   }

   private static void X(PoseStack var0, float var1, float var2) {
      var0.translate(
         var1 * -0.4F * (float)Math.sin(Math.sqrt(var2) * Math.PI),
         0.2F * (float)Math.sin(Math.sqrt(var2) * 2.0 * Math.PI),
         -0.2F * (float)Math.sin(var2 * Math.PI)
      );
   }

   private static void U(PoseStack var0, float var1, float var2) {
      float var3 = (float)Math.sin(Math.sqrt(var2) * Math.PI);
      Z(var0, var1 * (45.0F - 20.0F * (float)Math.sin(var2 * var2 * Math.PI)));
      Q(var0, var1 * -20.0F * var3);
      D(var0, -80.0F * var3);
      Z(var0, var1 * -45.0F);
   }

   public static void Z(PoseStack var0, String var1, boolean var2, float var3, x5.m var4, x5.z var5) {
      float var6 = h(var3, var4.smooth, var4.smoothAmount, var4.curve);
      float var7 = (float)Math.sin(Math.sqrt(var6) * Math.PI);
      float var8 = (float)Math.sin(var6 * Math.PI);
      float var9 = var2 ? -1.0F : 1.0F;
      float var10 = var4.strength * 10.0F;
      if (var1.equals("Module 8")) {
         X(var0, var9, var6);
      }

      J(var0, var9);
      if (!var1.equals("Mode 1") && !var1.equals("Module 8")) {
         switch (var1) {
            case "Mode 2":
               D(var0, 50.0F);
               Z(var0, var9 * -60.0F);
               Q(var0, var9 * (110.0F + var4.legacyStrength * var7));
               return;
            case "Mode 3":
               D(var0, 50.0F);
               Z(var0, var9 * (-30.0F * (1.0F - var7) - 30.0F + (var4.legacyStrength - 20.0F) * var7));
               Q(var0, var9 * 110.0F);
               return;
            case "Mode 4":
               Z(var0, var9 * 90.0F);
               Q(var0, var9 * -30.0F);
               D(var0, -80.0F - var4.legacyStrength * var8);
               return;
            case "Mode 5":
               D(var0, -360.0F * var6);
               return;
            default:
               if (var4.down > 0.0F) {
                  var0.translate(0.0F, -var4.down * 0.18F * var8, 0.0F);
               }

               switch (var1) {
                  case "Module 1":
                     Z(var0, var9 * (-45.0F - 20.0F * var7));
                     Q(var0, var9 * -20.0F * var7);
                     D(var0, -80.0F * var7);
                     Z(var0, var9 * 45.0F);
                     D(var0, -var10 * var8);
                     break;
                  case "Module 2":
                     var0.translate(0.0F, 0.15F, -0.3F - var4.distance);
                     Z(var0, var9 * 90.0F);
                     Q(var0, var9 * -60.0F);
                     D(var0, -var4.angle - var10 * var8);
                     break;
                  case "Module 3":
                     D(var0, var6 * (float) Math.PI - var10 * var8);
                     break;
                  case "Module 4":
                     var0.translate(0.0F, 0.15F, -0.3F - var4.distance);
                     Z(var0, var9 * 70.0F);
                     Q(var0, var9 * -30.0F);
                     var0.scale(0.9F, 0.9F, 0.9F);
                     D(var0, -var4.angle - var10 * var8);
                     break;
                  case "Module 5":
                     Z(var0, var9 * 45.0F);
                     D(var0, -20.0F * var8);
                     Q(var0, var9 * -20.0F * var8);
                     D(var0, -80.0F * var8);
                     Z(var0, var9 * -45.0F);
                     break;
                  case "Module 6":
                     Z(var0, var9 * (45.0F - 20.0F * var8));
                     Q(var0, var9 * -20.0F * var8);
                     D(var0, -80.0F * var8);
                     Z(var0, var9 * -45.0F);
                     D(var0, -var10 * var8);
                     break;
                  case "Module 7":
                     var0.translate(0.0F, 0.0F, -0.1F * var4.strength * var8);
                     Z(var0, var9 * 75.0F);
                     Q(var0, var9 * (-37.5F * var8 - 60.0F));
                     D(var0, -75.0F);
                     break;
                  case "360":
                     D(var0, -360.0F * var6);
                     break;
                  case "Slant":
                     var0.translate(0.0F, 0.0F, -(0.3F + var4.distance) * var8);
                     D(var0, -35.0F * var8);
                     Q(var0, var9 * 35.0F * var8);
                     break;
                  case "Triangle":
                     float var16 = var5.A(o(var3), var6);
                     var0.translate(0.0F, 0.02F * var8, -var4.distance * 0.35F * var8);
                     Z(var0, var9 * 45.0F);
                     D(var0, -var16);
                     Q(var0, var9 * 10.0F * var8);
                     Z(var0, var9 * -45.0F);
                     break;
                  case "Shift":
                  case "Break":
                     var0.translate(var9 * 0.18F, 0.17F, -0.35F - var4.distance);
                     Z(var0, var9 * 90.0F);
                     Q(var0, var9 * (var1.equals("Shift") ? -60 : -30));
                     D(var0, -90.0F - var10 * var8 + var4.turn);
                     break;
                  case "Lunge":
                     var0.translate(var9 * (0.22F + 0.2F * var8), -0.04F + 0.1F * var8, -0.35F - var4.distance - 0.28F * var8);
                     Z(var0, var9 * (45.0F - var8 * var10 * 0.35F));
                     Q(var0, var9 * var8 * -var10 * 0.3F);
                     D(var0, -var8 * var10);
                     Z(var0, var9 * 30.0F);
                     break;
                  case "Spear":
                     var0.translate(0.0F, 0.07F, -0.18F - var4.distance - 0.22F * var8);
                     var0.scale(1.0F, 1.0F, 1.0F + var8 * var4.strength * 0.03F);
                     D(var0, -90.0F);
                     Z(var0, var9 * 20.0F);
                     break;
                  case "Down":
                     var0.translate(var9 * -0.18F * var8, 0.05F + 0.18F * var8, -0.22F - var4.distance);
                     Z(var0, var9 * 65.0F);
                     Q(var0, var9 * 5.0F);
                     D(var0, -110.0F - 80.0F * var8);
                     break;
                  case "Jelly":
                     var0.translate(0.0F, 0.04F, -0.2F - var4.distance - 0.2F * var8);
                     Z(var0, var9 * 35.0F);
                     var0.scale(1.0F, 1.0F, 1.0F + var8 * var6 * var4.strength * 0.06F);
                     D(var0, -90.0F);
                     break;
                  case "Shake":
                     float var13 = (float)Math.sin(var6 * Math.PI * 8.0) * var4.strength;
                     var0.translate(0.0F, 0.04F * var8, -0.2F - var4.distance * 0.5F);
                     Z(var0, var9 * (110.0F + var13));
                     D(var0, -110.0F);
                     Z(var0, var9 * 75.0F);
                     D(var0, 35.0F + var13);
                     break;
                  default:
                     throw new IllegalArgumentException("Unknown swing mode: " + var1);
               }
         }
      } else {
         U(var0, var9, var6);
      }
   }

   public record m(float legacyStrength, float strength, float angle, float turn, float down, float distance, boolean smooth, float smoothAmount, float curve) {
   }

   public static final class z {
      private boolean t = (boolean)1;
      private int I;
      private int D;

      public float A(float var1, float var2) {
         if (var1 < 0.03F) {
            this.t = (boolean)1;
            return this.I * 120.0F;
         }

         if (this.t && var1 > 0.08F) {
            this.D = this.I;
            this.I = (this.I + 1) % 3;
            this.t = (boolean)0;
         }

         return this.D * 120.0F + var2 * 120.0F;
      }

      public void N() {
         this.t = (boolean)1;
         this.I = this.D = 0;
      }
   }
}
