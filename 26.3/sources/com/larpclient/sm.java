package com.larpclient;

import net.minecraft.world.entity.ai.attributes.AttributeInstance;

public final class sm {
   private AttributeInstance u;
   private double p;
   private double B;

   public void T(AttributeInstance var1, double var2) {
      if (var1 != this.u) {
         this.p();
         this.u = var1;
         if (var1 != null) {
            this.p = var1.getBaseValue();
         }
      }

      if (this.u != null) {
         if (this.u.getBaseValue() != this.B) {
            this.p = this.u.getBaseValue();
         }

         this.u.setBaseValue(var2);
         this.B = var2;
      }
   }

   public void p() {
      if (this.u != null && this.u.getBaseValue() == this.B) {
         this.u.setBaseValue(this.p);
      }

      this.u = null;
   }

   public void M(AttributeInstance var1) {
      if (this.u == var1) {
         this.u = null;
      }
   }
}
