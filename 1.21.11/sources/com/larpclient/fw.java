package com.larpclient;

import net.minecraft.class_1324;

public final class fw {
   private class_1324 Q;
   private double d;
   private double K;

   public void r(class_1324 var1, double var2) {
      if (var1 != this.Q) {
         this.A();
         this.Q = var1;
         if (var1 != null) {
            this.d = var1.method_6201();
         }
      }

      if (this.Q != null) {
         if (this.Q.method_6201() != this.K) {
            this.d = this.Q.method_6201();
         }

         this.Q.method_6192(var2);
         this.K = var2;
      }
   }

   public void A() {
      if (this.Q != null && this.Q.method_6201() == this.K) {
         this.Q.method_6192(this.d);
      }

      this.Q = null;
   }

   public void S(class_1324 var1) {
      if (this.Q == var1) {
         this.Q = null;
      }
   }
}
