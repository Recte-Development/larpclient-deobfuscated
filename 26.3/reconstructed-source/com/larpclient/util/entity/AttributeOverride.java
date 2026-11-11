package com.larpclient.util.entity;

import net.minecraft.world.entity.ai.attributes.AttributeInstance;

public final class AttributeOverride {
   private AttributeInstance attribute;
   private double originalValue;
   private double appliedValue;

   public void apply(AttributeInstance var1, double var2) {
      if (var1 != this.attribute) {
         this.restore();
         this.attribute = var1;
         if (var1 != null) {
            this.originalValue = var1.getBaseValue();
         }
      }

      if (this.attribute != null) {
         if (this.attribute.getBaseValue() != this.appliedValue) {
            this.originalValue = this.attribute.getBaseValue();
         }

         this.attribute.setBaseValue(var2);
         this.appliedValue = var2;
      }
   }

   public void restore() {
      if (this.attribute != null && this.attribute.getBaseValue() == this.appliedValue) {
         this.attribute.setBaseValue(this.originalValue);
      }

      this.attribute = null;
   }

   public void detach(AttributeInstance var1) {
      if (this.attribute == var1) {
         this.attribute = null;
      }
   }
}
