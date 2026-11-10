package com.larpclient.module;

import net.minecraft.class_243;
import net.minecraft.class_3532;

public final class l4 {
   public float j;
   public float l;

   public l4(float var1, float var2) {
      this.j = var1;
      this.l = var2;
   }

   public l4 g() {
      return new l4(this.j, this.l);
   }

   public float o(l4 var1) {
      return Math.abs(class_3532.method_15393(this.j - var1.j)) + Math.abs(class_3532.method_15393(this.l - var1.l));
   }

   public class_243 q() {
      return a_.w(this.j, this.l);
   }

   @Override
   public String toString() {
      return "Rotation(yaw=" + this.j + ", pitch=" + this.l + ")";
   }
}
