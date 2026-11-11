package com.larpclient.util.data;

import com.larpclient.render.sky.SkyboxTheme;

public final class EnumCycler {
   private final SkyboxTheme defaultValue;
   private int index;

   public EnumCycler(SkyboxTheme var1) {
      this.defaultValue = var1;
      this.index = var1.ordinal();
   }

   public SkyboxTheme current() {
      return SkyboxTheme.values()[this.index];
   }

   public SkyboxTheme getDefault() {
      return this.defaultValue;
   }

   public void setIndex(int var1) {
      this.index = Math.clamp(var1, 0, SkyboxTheme.values().length - 1);
   }

   public void getDefault(int var1) {
      this.setIndex(this.index + Integer.signum(var1));
   }

   public SkyboxTheme defaultValue() {
      return this.defaultValue;
   }

   public SkyboxTheme currentValue() {
      return this.current();
   }
}
