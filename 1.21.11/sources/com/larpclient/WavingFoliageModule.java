package com.larpclient;

import com.larpclient.module.Category;
import com.larpclient.module.Module;

public final class WavingFoliageModule extends Module {
   private static WavingFoliageModule j;
   public final com.larpclient.module.l3 S = this.addSetting(new com.larpclient.module.l3("Leaves", "Leaves and vines sway", true));
   public final com.larpclient.module.l3 K = this.addSetting(
      new com.larpclient.module.l3("Trees", "Bend natural trees from their planted base; log buildings stay still", true)
   );
   public final com.larpclient.module.l3 v = this.addSetting(
      new com.larpclient.module.l3("Plants", "Grass, flowers, crops and saplings bend from their base", true)
   );
   public final com.larpclient.module.lo z = this.addSetting(new com.larpclient.module.lo("Strength", "How far foliage sways", 1.0, 0.2, 3.0, 0.1));
   public final com.larpclient.module.lo Q = this.addSetting(new com.larpclient.module.lo("Speed", "Wind animation speed", 1.0, 0.2, 3.0, 0.1));
   public final com.larpclient.module.lo G = this.addSetting(
      new com.larpclient.module.lo("Gusts", "Variation in wind strength, percent", 60.0, 0.0, 100.0, 5.0)
   );

   public WavingFoliageModule() {
      super("Waving Foliage", "Trees, grass, flowers and crops sway in wind and passing gusts", Category.RENDER, -1);
      j = this;
      this.K.O(() -> this.S.getValue());
      this.S.a(var1 -> this.L());
      this.K.a(var1 -> this.L());
      this.v.a(var1 -> this.L());
   }

   @Override
   public String getStatus() {
      return this.isRuntimeActive() && qs.k() ? "Material fallback" : "";
   }

   public static WavingFoliageModule y() {
      return j;
   }

   private void L() {
      qs.O();
   }

   @Override
   public void onEnable() {
      this.L();
   }

   @Override
   public void onDisable() {
      this.L();
      qs.B();
   }
}
