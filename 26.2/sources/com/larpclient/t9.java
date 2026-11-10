package com.larpclient;

import net.minecraft.world.entity.ai.attributes.AttributeInstance;

public final class t9 {
   private AttributeInstance s;
   private double g;
   private double j;

   public void y(AttributeInstance var1, double var2) {
      if (var1 != this.s) {
         this.v();
         this.s = var1;
         if (var1 != null) {
            this.g = var1.getBaseValue();
         }
      }

      if (this.s != null) {
         if (this.s.getBaseValue() != this.j) {
            this.g = this.s.getBaseValue();
         }

         this.s.setBaseValue(var2);
         this.j = var2;
      }
   }

   public void v() {
      if (this.s != null && this.s.getBaseValue() == this.j) {
         this.s.setBaseValue(this.g);
      }

      this.s = null;
   }

   public void u(AttributeInstance var1) {
      if (this.s == var1) {
         this.s = null;
      }
   }
}
