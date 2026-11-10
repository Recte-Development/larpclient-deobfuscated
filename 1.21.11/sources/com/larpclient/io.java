package com.larpclient;

import net.minecraft.class_10055;
import net.minecraft.class_11659;
import net.minecraft.class_3883;
import net.minecraft.class_3887;
import net.minecraft.class_4587;
import net.minecraft.class_591;
import net.minecraft.class_922;

public final class io extends class_3887<class_10055, class_591> {
   public io(class_3883<class_10055, class_591> var1) {
      super(var1);
   }

   public void w(class_4587 var1, class_11659 var2, int var3, class_10055 var4, float var5, float var6) {
      yv var7 = ((g1)var4).larp$korblox();
      if (var7 != null && !var4.field_53333 && !var4.field_53542) {
         int var8 = class_922.method_23622(var4, 0.0F);
         class_591 var9 = (class_591)this.method_17165();
         if (var7.right()) {
            lm.F(var1, var2, var9.field_3392, var7, var3, var8, false);
         }

         if (var7.left()) {
            lm.F(var1, var2, var9.field_3397, var7, var3, var8, true);
         }
      }
   }
}
