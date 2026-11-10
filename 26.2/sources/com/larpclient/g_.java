package com.larpclient;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;

public final class g_ {
   public static final String[] J = new String[]{
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

   private g_() {
   }

   public static float G(float var0) {
      return Float.isFinite(var0) ? Math.clamp(var0, 0.0F, 1.0F) : 0.0F;
   }

   public static int o(int var0, double var1) {
      double var3 = Double.isFinite(var1) ? Math.clamp(var1, 0.25, 3.0) : 1.0;
      return Math.max(1, (int)Math.round(var0 * var3));
   }

   public static float I(float var0, boolean var1, float var2, float var3) {
      float var4 = G(var0);
      if (Math.abs(var3 - 1.0F) > 0.001F) {
         var4 = (float)Math.pow(var4, var3);
      }

      if (!var1) {
         return var4;
      }

      float var5 = var4 < 0.5F ? 4.0F * var4 * var4 * var4 : 1.0F - (float)Math.pow(-2.0F * var4 + 2.0F, 3.0) / 2.0F;
      return var4 + (var5 - var4) * Math.clamp(var2, 0.0F, 1.0F);
   }

   private static void c(PoseStack var0, float var1) {
      uf.q(var0, Axis.XP.rotationDegrees(var1));
   }

   private static void e(PoseStack var0, float var1) {
      uf.q(var0, Axis.YP.rotationDegrees(var1));
   }

   private static void o(PoseStack var0, float var1) {
      uf.q(var0, Axis.ZP.rotationDegrees(var1));
   }

   private static void t(PoseStack var0, float var1) {
      var0.translate(var1 * 0.56F, -0.52F, -0.72F);
   }

   private static void l(PoseStack var0, float var1, float var2) {
      var0.translate(
         var1 * -0.4F * (float)Math.sin(Math.sqrt(var2) * Math.PI),
         0.2F * (float)Math.sin(Math.sqrt(var2) * 2.0 * Math.PI),
         -0.2F * (float)Math.sin(var2 * Math.PI)
      );
   }

   private static void K(PoseStack var0, float var1, float var2) {
      float var3 = (float)Math.sin(Math.sqrt(var2) * Math.PI);
      e(var0, var1 * (45.0F - 20.0F * (float)Math.sin(var2 * var2 * Math.PI)));
      o(var0, var1 * -20.0F * var3);
      c(var0, -80.0F * var3);
      e(var0, var1 * -45.0F);
   }

   public static void H(PoseStack var0, String var1, boolean var2, float var3, g_.u var4, g_.t var5) {
      float var6 = I(var3, var4.smooth, var4.smoothAmount, var4.curve);
      float var7 = (float)Math.sin(Math.sqrt(var6) * Math.PI);
      float var8 = (float)Math.sin(var6 * Math.PI);
      float var9 = var2 ? -1.0F : 1.0F;
      float var10 = var4.strength * 10.0F;
      if (var1.equals("Module 8")) {
         l(var0, var9, var6);
      }

      t(var0, var9);
      if (!var1.equals("Mode 1") && !var1.equals("Module 8")) {
         switch (var1) {
            case "Mode 2":
               c(var0, 50.0F);
               e(var0, var9 * -60.0F);
               o(var0, var9 * (110.0F + var4.legacyStrength * var7));
               return;
            case "Mode 3":
               c(var0, 50.0F);
               e(var0, var9 * (-30.0F * (1.0F - var7) - 30.0F + (var4.legacyStrength - 20.0F) * var7));
               o(var0, var9 * 110.0F);
               return;
            case "Mode 4":
               e(var0, var9 * 90.0F);
               o(var0, var9 * -30.0F);
               c(var0, -80.0F - var4.legacyStrength * var8);
               return;
            case "Mode 5":
               c(var0, -360.0F * var6);
               return;
            default:
               if (var4.down > 0.0F) {
                  var0.translate(0.0F, -var4.down * 0.18F * var8, 0.0F);
               }

               switch (var1) {
                  case "Module 1":
                     e(var0, var9 * (-45.0F - 20.0F * var7));
                     o(var0, var9 * -20.0F * var7);
                     c(var0, -80.0F * var7);
                     e(var0, var9 * 45.0F);
                     c(var0, -var10 * var8);
                     break;
                  case "Module 2":
                     var0.translate(0.0F, 0.15F, -0.3F - var4.distance);
                     e(var0, var9 * 90.0F);
                     o(var0, var9 * -60.0F);
                     c(var0, -var4.angle - var10 * var8);
                     break;
                  case "Module 3":
                     c(var0, var6 * (float) Math.PI - var10 * var8);
                     break;
                  case "Module 4":
                     var0.translate(0.0F, 0.15F, -0.3F - var4.distance);
                     e(var0, var9 * 70.0F);
                     o(var0, var9 * -30.0F);
                     var0.scale(0.9F, 0.9F, 0.9F);
                     c(var0, -var4.angle - var10 * var8);
                     break;
                  case "Module 5":
                     e(var0, var9 * 45.0F);
                     c(var0, -20.0F * var8);
                     o(var0, var9 * -20.0F * var8);
                     c(var0, -80.0F * var8);
                     e(var0, var9 * -45.0F);
                     break;
                  case "Module 6":
                     e(var0, var9 * (45.0F - 20.0F * var8));
                     o(var0, var9 * -20.0F * var8);
                     c(var0, -80.0F * var8);
                     e(var0, var9 * -45.0F);
                     c(var0, -var10 * var8);
                     break;
                  case "Module 7":
                     var0.translate(0.0F, 0.0F, -0.1F * var4.strength * var8);
                     e(var0, var9 * 75.0F);
                     o(var0, var9 * (-37.5F * var8 - 60.0F));
                     c(var0, -75.0F);
                     break;
                  case "360":
                     c(var0, -360.0F * var6);
                     break;
                  case "Slant":
                     var0.translate(0.0F, 0.0F, -(0.3F + var4.distance) * var8);
                     c(var0, -35.0F * var8);
                     o(var0, var9 * 35.0F * var8);
                     break;
                  case "Triangle":
                     float var16 = var5.m(G(var3), var6);
                     var0.translate(0.0F, 0.02F * var8, -var4.distance * 0.35F * var8);
                     e(var0, var9 * 45.0F);
                     c(var0, -var16);
                     o(var0, var9 * 10.0F * var8);
                     e(var0, var9 * -45.0F);
                     break;
                  case "Shift":
                  case "Break":
                     var0.translate(var9 * 0.18F, 0.17F, -0.35F - var4.distance);
                     e(var0, var9 * 90.0F);
                     o(var0, var9 * (var1.equals("Shift") ? -60 : -30));
                     c(var0, -90.0F - var10 * var8 + var4.turn);
                     break;
                  case "Lunge":
                     var0.translate(var9 * (0.22F + 0.2F * var8), -0.04F + 0.1F * var8, -0.35F - var4.distance - 0.28F * var8);
                     e(var0, var9 * (45.0F - var8 * var10 * 0.35F));
                     o(var0, var9 * var8 * -var10 * 0.3F);
                     c(var0, -var8 * var10);
                     e(var0, var9 * 30.0F);
                     break;
                  case "Spear":
                     var0.translate(0.0F, 0.07F, -0.18F - var4.distance - 0.22F * var8);
                     var0.scale(1.0F, 1.0F, 1.0F + var8 * var4.strength * 0.03F);
                     c(var0, -90.0F);
                     e(var0, var9 * 20.0F);
                     break;
                  case "Down":
                     var0.translate(var9 * -0.18F * var8, 0.05F + 0.18F * var8, -0.22F - var4.distance);
                     e(var0, var9 * 65.0F);
                     o(var0, var9 * 5.0F);
                     c(var0, -110.0F - 80.0F * var8);
                     break;
                  case "Jelly":
                     var0.translate(0.0F, 0.04F, -0.2F - var4.distance - 0.2F * var8);
                     e(var0, var9 * 35.0F);
                     var0.scale(1.0F, 1.0F, 1.0F + var8 * var6 * var4.strength * 0.06F);
                     c(var0, -90.0F);
                     break;
                  case "Shake":
                     float var13 = (float)Math.sin(var6 * Math.PI * 8.0) * var4.strength;
                     var0.translate(0.0F, 0.04F * var8, -0.2F - var4.distance * 0.5F);
                     e(var0, var9 * (110.0F + var13));
                     c(var0, -110.0F);
                     e(var0, var9 * 75.0F);
                     c(var0, 35.0F + var13);
                     break;
                  default:
                     throw new IllegalArgumentException("Unknown swing mode: " + var1);
               }
         }
      } else {
         K(var0, var9, var6);
      }
   }

   public static final class t {
      private boolean i = (boolean)1;
      private int B;
      private int E;

      public float m(float var1, float var2) {
         if (var1 < 0.03F) {
            this.i = (boolean)1;
            return this.B * 120.0F;
         }

         if (this.i && var1 > 0.08F) {
            this.E = this.B;
            this.B = (this.B + 1) % 3;
            this.i = (boolean)0;
         }

         return this.E * 120.0F + var2 * 120.0F;
      }

      public void z() {
         this.i = (boolean)1;
         this.B = this.E = 0;
      }
   }

   public record u(float legacyStrength, float strength, float angle, float turn, float down, float distance, boolean smooth, float smoothAmount, float curve) {
   }
}
