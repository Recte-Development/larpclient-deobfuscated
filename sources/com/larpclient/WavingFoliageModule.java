package com.larpclient;

import com.larpclient.module.Category;
import com.larpclient.module.Module;
import com.larpclient.module.dh;

public final class WavingFoliageModule extends Module {
   private static WavingFoliageModule b;
   public final dh x = this.addSetting(new dh("Leaves", "Leaves and vines sway", true));
   public final dh H = this.addSetting(new dh("Trees", "Bend natural trees from their planted base; log buildings stay still", true));
   public final dh F = this.addSetting(new dh("Plants", "Grass, flowers, crops and saplings bend from their base", true));
   public final com.larpclient.module.d9 O = this.addSetting(new com.larpclient.module.d9("Strength", "How far foliage sways", 1.0, 0.2, 3.0, 0.1));
   public final com.larpclient.module.d9 G = this.addSetting(new com.larpclient.module.d9("Speed", "Wind animation speed", 1.0, 0.2, 3.0, 0.1));
   public final com.larpclient.module.d9 t = this.addSetting(
      new com.larpclient.module.d9("Gusts", "Variation in wind strength, percent", 60.0, 0.0, 100.0, 5.0)
   );

   public WavingFoliageModule() {
      super("Waving Foliage", "Trees, grass, flowers and crops sway in wind and passing gusts", Category.RENDER, -1);
      b = this;
      this.H.H(() -> this.x.P());
      this.x.h(var1 -> this.U());
      this.H.h(var1 -> this.U());
      this.F.h(var1 -> this.U());
   }

   @Override
   public String getStatus() {
      return this.isRuntimeActive() && k.K() ? "Material fallback" : "";
   }

   public static WavingFoliageModule O() {
      return b;
   }

   private void U() {
      k.k();
   }

   @Override
   public void onEnable() {
      this.U();
   }

   @Override
   public void onDisable() {
      this.U();
      k.w();
   }
}
