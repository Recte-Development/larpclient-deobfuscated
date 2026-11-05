package com.larpclient;

import com.larpclient.module.Category;
import com.larpclient.module.Module;

public final class WavingFoliageModule extends Module {
   private static WavingFoliageModule r;
   public final com.larpclient.module.ln t = this.addSetting(new com.larpclient.module.ln("Leaves", "Leaves and vines sway", true));
   public final com.larpclient.module.ln M = this.addSetting(
      new com.larpclient.module.ln("Trees", "Bend natural trees from their planted base; log buildings stay still", true)
   );
   public final com.larpclient.module.ln X = this.addSetting(
      new com.larpclient.module.ln("Plants", "Grass, flowers, crops and saplings bend from their base", true)
   );
   public final com.larpclient.module.lc Q = this.addSetting(new com.larpclient.module.lc("Strength", "How far foliage sways", 1.0, 0.2, 3.0, 0.1));
   public final com.larpclient.module.lc m = this.addSetting(new com.larpclient.module.lc("Speed", "Wind animation speed", 1.0, 0.2, 3.0, 0.1));
   public final com.larpclient.module.lc g = this.addSetting(
      new com.larpclient.module.lc("Gusts", "Variation in wind strength, percent", 60.0, 0.0, 100.0, 5.0)
   );

   public WavingFoliageModule() {
      super("Waving Foliage", "Trees, grass, flowers and crops sway in wind and passing gusts", Category.RENDER, -1);
      r = this;
      this.M.x(() -> this.t.getValue());
      this.t.W(var1 -> this.e());
      this.M.W(var1 -> this.e());
      this.X.W(var1 -> this.e());
   }

   @Override
   public String getStatus() {
      return this.isRuntimeActive() && y3.S() ? "Material fallback" : "";
   }

   public static WavingFoliageModule P() {
      return r;
   }

   private void e() {
      y3.z();
   }

   @Override
   public void onEnable() {
      this.e();
   }

   @Override
   public void onDisable() {
      this.e();
      y3.J();
   }
}
