package com.larpclient;

import java.util.function.DoubleSupplier;
import net.minecraft.class_1101;
import net.minecraft.class_1113;
import net.minecraft.class_3414;
import net.minecraft.class_3419;
import net.minecraft.class_1113.class_1114;

public final class yd extends class_1101 {
   private final DoubleSupplier M;
   private final int T;
   private int R = -1;

   public yd(fj.g var1, DoubleSupplier var2, int var3) {
      super(class_3414.method_47908(var1.music()), class_3419.field_15248, class_1113.method_43221());
      this.M = var2;
      this.T = Math.max(1, var3);
      this.field_5446 = (boolean)1;
      this.field_18936 = (boolean)1;
      this.field_5440 = class_1114.field_5478;
      this.field_5442 = (float)var2.getAsDouble();
   }

   public void B() {
      if (this.R < 0) {
         this.R = this.T;
      }
   }

   public void M() {
      this.method_24876();
   }

   public void method_16896() {
      float var1 = (float)Math.clamp(this.M.getAsDouble(), 0.0, 1.0);
      if (this.R >= 0) {
         if (this.R-- <= 0) {
            this.method_24876();
            return;
         }

         var1 *= (float)(this.R + 1) / (this.T + 1);
      }

      this.field_5442 = var1;
   }

   public boolean method_4785() {
      return true;
   }
}
