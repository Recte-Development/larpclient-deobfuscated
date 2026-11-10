package com.larpclient;

import net.minecraft.class_10055;
import net.minecraft.class_243;
import net.minecraft.class_310;
import net.minecraft.class_4587;
import net.minecraft.class_5599;
import net.minecraft.class_5602;
import net.minecraft.class_591;
import net.minecraft.class_630;
import net.minecraft.class_7920;
import org.joml.Quaternionf;

public final class qm {
   private class_591 G;
   private class_591 w;
   private int r = -1;

   public void l() {
      this.G = this.w = null;
      this.r = -1;
   }

   public void t() {
      if (this.r != t_.g || this.G == null) {
         class_5599 var1 = class_310.method_1551().method_31974();
         this.G = new class_591(var1.method_32072(class_5602.field_27577), false);
         this.w = new class_591(var1.method_32072(class_5602.field_27581), true);
         this.r = t_.g;
      }
   }

   public void T(class_10055 var1, u8 var2, u8.s[] var3, class_243 var4, class_4587 var5, cw var6, int var7, boolean var8) {
      if (this.r == t_.g && this.G != null) {
         int var9 = var1.field_53520.comp_1629() == class_7920.field_41122 ? 1 : 0;
         class_591 var10 = var9 != 0 ? this.w : this.G;

         for (class_630 var12 : var10.method_63512().method_32088()) {
            var12.method_41923();
            var12.field_3665 = (boolean)1;
         }

         var10.field_3394.field_3665 = var1.field_53543;
         var10.field_3483.field_3665 = var1.field_53544;
         var10.field_3484.field_3665 = var1.field_53530;
         var10.field_3486.field_3665 = var1.field_53531;
         var10.field_3482.field_3665 = var1.field_53545;
         var10.field_3479.field_3665 = var1.field_53546;
         class_630[] var17 = new class_630[]{var10.field_3398, var10.field_3391, var10.field_3401, var10.field_27433, var10.field_3392, var10.field_3397};

         for (int var18 = 0; var18 < var3.length; var18++) {
            u8.s var13 = var3[var18];
            if (!(var13.alpha() <= 0.001) && !(var13.scale() <= 0.001) && !(var13.dissolve() >= 1.0)) {
               var5.method_22903();

               try {
                  V(var5, var13, var2.y, var18, (boolean)var9, var4);
                  gv.A(var17[var18], var5, var6, var1.field_53520.comp_1626().comp_3627(), var7, var13.alpha(), var13.dissolve(), var8);
               } finally {
                  var5.method_22909();
               }
            }
         }
      }
   }

   public static void V(class_4587 var0, u8.s var1, double var2, int var4, boolean var5, class_243 var6) {
      var0.method_22904(
         var1.position().field_1352 - var6.field_1352, var1.position().field_1351 - var6.field_1351, var1.position().field_1350 - var6.field_1350
      );
      if (var1.orientation() != null) {
         em.W(var0, var1.orientation());
      } else {
         em.W(var0, new Quaternionf().rotateY((float)(Math.PI - var2)).rotateXYZ((float)var1.rx(), (float)var1.ry(), (float)var1.rz()));
      }

      float var7 = (float)(0.9375 * var1.scale());
      if (var1.stretch() == null) {
         var0.method_22905(-var7, -var7, var7);
      } else {
         var0.method_22905((float)(-var7 * var1.stretch().field_1352), (float)(-var7 * var1.stretch().field_1351), (float)(var7 * var1.stretch().field_1350));
      }

      var0.method_22904(-u8.z(var4, var5) / 16.0, -u8.E[var4][1] / 16.0, 0.0);
   }
}
