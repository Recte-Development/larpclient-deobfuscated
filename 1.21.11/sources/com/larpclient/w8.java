package com.larpclient;

import net.minecraft.class_4587;
import net.minecraft.class_7833;

public final class w8 {
   public static final String[] K = new String[]{
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

   private w8() {
   }

   public static float X(float var0) {
      return Float.isFinite(var0) ? Math.clamp(var0, 0.0F, 1.0F) : 0.0F;
   }

   public static int e(int var0, double var1) {
      double var3 = Double.isFinite(var1) ? Math.clamp(var1, 0.25, 3.0) : 1.0;
      return Math.max(1, (int)Math.round(var0 * var3));
   }

   public static float X(float var0, boolean var1, float var2, float var3) {
      float var4 = X(var0);
      if (Math.abs(var3 - 1.0F) > 0.001F) {
         var4 = (float)Math.pow(var4, var3);
      }

      if (!var1) {
         return var4;
      }

      float var5 = var4 < 0.5F ? 4.0F * var4 * var4 * var4 : 1.0F - (float)Math.pow(-2.0F * var4 + 2.0F, 3.0) / 2.0F;
      return var4 + (var5 - var4) * Math.clamp(var2, 0.0F, 1.0F);
   }

   private static void l(class_4587 var0, float var1) {
      em.W(var0, class_7833.field_40714.rotationDegrees(var1));
   }

   private static void v(class_4587 var0, float var1) {
      em.W(var0, class_7833.field_40716.rotationDegrees(var1));
   }

   private static void E(class_4587 var0, float var1) {
      em.W(var0, class_7833.field_40718.rotationDegrees(var1));
   }

   private static void P(class_4587 var0, float var1) {
      var0.method_46416(var1 * 0.56F, -0.52F, -0.72F);
   }

   private static void i(class_4587 var0, float var1, float var2) {
      var0.method_46416(
         var1 * -0.4F * (float)Math.sin(Math.sqrt(var2) * Math.PI),
         0.2F * (float)Math.sin(Math.sqrt(var2) * 2.0 * Math.PI),
         -0.2F * (float)Math.sin(var2 * Math.PI)
      );
   }

   private static void p(class_4587 var0, float var1, float var2) {
      float var3 = (float)Math.sin(Math.sqrt(var2) * Math.PI);
      v(var0, var1 * (45.0F - 20.0F * (float)Math.sin(var2 * var2 * Math.PI)));
      E(var0, var1 * -20.0F * var3);
      l(var0, -80.0F * var3);
      v(var0, var1 * -45.0F);
   }

   public static void m(class_4587 var0, String var1, boolean var2, float var3, w8.v var4, w8.s var5) {
      float var6 = X(var3, var4.smooth, var4.smoothAmount, var4.curve);
      float var7 = (float)Math.sin(Math.sqrt(var6) * Math.PI);
      float var8 = (float)Math.sin(var6 * Math.PI);
      float var9 = var2 ? -1.0F : 1.0F;
      float var10 = var4.strength * 10.0F;
      if (var1.equals("Module 8")) {
         i(var0, var9, var6);
      }

      P(var0, var9);
      if (!var1.equals("Mode 1") && !var1.equals("Module 8")) {
         switch (var1) {
            case "Mode 2":
               l(var0, 50.0F);
               v(var0, var9 * -60.0F);
               E(var0, var9 * (110.0F + var4.legacyStrength * var7));
               return;
            case "Mode 3":
               l(var0, 50.0F);
               v(var0, var9 * (-30.0F * (1.0F - var7) - 30.0F + (var4.legacyStrength - 20.0F) * var7));
               E(var0, var9 * 110.0F);
               return;
            case "Mode 4":
               v(var0, var9 * 90.0F);
               E(var0, var9 * -30.0F);
               l(var0, -80.0F - var4.legacyStrength * var8);
               return;
            case "Mode 5":
               l(var0, -360.0F * var6);
               return;
            default:
               if (var4.down > 0.0F) {
                  var0.method_46416(0.0F, -var4.down * 0.18F * var8, 0.0F);
               }

               switch (var1) {
                  case "Module 1":
                     v(var0, var9 * (-45.0F - 20.0F * var7));
                     E(var0, var9 * -20.0F * var7);
                     l(var0, -80.0F * var7);
                     v(var0, var9 * 45.0F);
                     l(var0, -var10 * var8);
                     break;
                  case "Module 2":
                     var0.method_46416(0.0F, 0.15F, -0.3F - var4.distance);
                     v(var0, var9 * 90.0F);
                     E(var0, var9 * -60.0F);
                     l(var0, -var4.angle - var10 * var8);
                     break;
                  case "Module 3":
                     l(var0, var6 * (float) Math.PI - var10 * var8);
                     break;
                  case "Module 4":
                     var0.method_46416(0.0F, 0.15F, -0.3F - var4.distance);
                     v(var0, var9 * 70.0F);
                     E(var0, var9 * -30.0F);
                     var0.method_22905(0.9F, 0.9F, 0.9F);
                     l(var0, -var4.angle - var10 * var8);
                     break;
                  case "Module 5":
                     v(var0, var9 * 45.0F);
                     l(var0, -20.0F * var8);
                     E(var0, var9 * -20.0F * var8);
                     l(var0, -80.0F * var8);
                     v(var0, var9 * -45.0F);
                     break;
                  case "Module 6":
                     v(var0, var9 * (45.0F - 20.0F * var8));
                     E(var0, var9 * -20.0F * var8);
                     l(var0, -80.0F * var8);
                     v(var0, var9 * -45.0F);
                     l(var0, -var10 * var8);
                     break;
                  case "Module 7":
                     var0.method_46416(0.0F, 0.0F, -0.1F * var4.strength * var8);
                     v(var0, var9 * 75.0F);
                     E(var0, var9 * (-37.5F * var8 - 60.0F));
                     l(var0, -75.0F);
                     break;
                  case "360":
                     l(var0, -360.0F * var6);
                     break;
                  case "Slant":
                     var0.method_46416(0.0F, 0.0F, -(0.3F + var4.distance) * var8);
                     l(var0, -35.0F * var8);
                     E(var0, var9 * 35.0F * var8);
                     break;
                  case "Triangle":
                     float var16 = var5.W(X(var3), var6);
                     var0.method_46416(0.0F, 0.02F * var8, -var4.distance * 0.35F * var8);
                     v(var0, var9 * 45.0F);
                     l(var0, -var16);
                     E(var0, var9 * 10.0F * var8);
                     v(var0, var9 * -45.0F);
                     break;
                  case "Shift":
                  case "Break":
                     var0.method_46416(var9 * 0.18F, 0.17F, -0.35F - var4.distance);
                     v(var0, var9 * 90.0F);
                     E(var0, var9 * (var1.equals("Shift") ? -60 : -30));
                     l(var0, -90.0F - var10 * var8 + var4.turn);
                     break;
                  case "Lunge":
                     var0.method_46416(var9 * (0.22F + 0.2F * var8), -0.04F + 0.1F * var8, -0.35F - var4.distance - 0.28F * var8);
                     v(var0, var9 * (45.0F - var8 * var10 * 0.35F));
                     E(var0, var9 * var8 * -var10 * 0.3F);
                     l(var0, -var8 * var10);
                     v(var0, var9 * 30.0F);
                     break;
                  case "Spear":
                     var0.method_46416(0.0F, 0.07F, -0.18F - var4.distance - 0.22F * var8);
                     var0.method_22905(1.0F, 1.0F, 1.0F + var8 * var4.strength * 0.03F);
                     l(var0, -90.0F);
                     v(var0, var9 * 20.0F);
                     break;
                  case "Down":
                     var0.method_46416(var9 * -0.18F * var8, 0.05F + 0.18F * var8, -0.22F - var4.distance);
                     v(var0, var9 * 65.0F);
                     E(var0, var9 * 5.0F);
                     l(var0, -110.0F - 80.0F * var8);
                     break;
                  case "Jelly":
                     var0.method_46416(0.0F, 0.04F, -0.2F - var4.distance - 0.2F * var8);
                     v(var0, var9 * 35.0F);
                     var0.method_22905(1.0F, 1.0F, 1.0F + var8 * var6 * var4.strength * 0.06F);
                     l(var0, -90.0F);
                     break;
                  case "Shake":
                     float var13 = (float)Math.sin(var6 * Math.PI * 8.0) * var4.strength;
                     var0.method_46416(0.0F, 0.04F * var8, -0.2F - var4.distance * 0.5F);
                     v(var0, var9 * (110.0F + var13));
                     l(var0, -110.0F);
                     v(var0, var9 * 75.0F);
                     l(var0, 35.0F + var13);
                     break;
                  default:
                     throw new IllegalArgumentException("Unknown swing mode: " + var1);
               }
         }
      } else {
         p(var0, var9, var6);
      }
   }

   public static final class s {
      private boolean x = (boolean)1;
      private int D;
      private int C;

      public float W(float var1, float var2) {
         if (var1 < 0.03F) {
            this.x = (boolean)1;
            return this.D * 120.0F;
         }

         if (this.x && var1 > 0.08F) {
            this.C = this.D;
            this.D = (this.D + 1) % 3;
            this.x = (boolean)0;
         }

         return this.C * 120.0F + var2 * 120.0F;
      }

      public void T() {
         this.x = (boolean)1;
         this.D = this.C = 0;
      }
   }

   public record v(float legacyStrength, float strength, float angle, float turn, float down, float distance, boolean smooth, float smoothAmount, float curve) {
   }
}
