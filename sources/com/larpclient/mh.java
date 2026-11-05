package com.larpclient;

import net.minecraft.world.entity.ai.attributes.AttributeInstance;

public final class mh {
   private AttributeInstance g;
   private double r;
   private double I;

   public void e(AttributeInstance var1, double var2) {
      if (var1 != this.g) {
         this.e();
         this.g = var1;
         if (var1 != null) {
            this.r = var1.getBaseValue();
         }
      }

      if (this.g != null) {
         if (this.g.getBaseValue() != this.I) {
            this.r = this.g.getBaseValue();
         }

         this.g.setBaseValue(var2);
         this.I = var2;
      }
   }

   public void e() {
      if (this.g != null && this.g.getBaseValue() == this.I) {
         this.g.setBaseValue(this.r);
      }

      this.g = null;
   }

   public void v(AttributeInstance var1) {
      if (this.g == var1) {
         this.g = null;
      }
   }
}
