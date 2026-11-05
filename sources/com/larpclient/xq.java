package com.larpclient;

import net.minecraft.world.entity.ai.attributes.AttributeInstance;

public final class xq {
   private AttributeInstance R;
   private double E;
   private double k;

   public void E(AttributeInstance var1, double var2) {
      if (var1 != this.R) {
         this.e();
         this.R = var1;
         if (var1 != null) {
            this.E = var1.getBaseValue();
         }
      }

      if (this.R != null) {
         if (this.R.getBaseValue() != this.k) {
            this.E = this.R.getBaseValue();
         }

         this.R.setBaseValue(var2);
         this.k = var2;
      }
   }

   public void e() {
      if (this.R != null && this.R.getBaseValue() == this.k) {
         this.R.setBaseValue(this.E);
      }

      this.R = null;
   }

   public void Q(AttributeInstance var1) {
      if (this.R == var1) {
         this.R = null;
      }
   }
}
