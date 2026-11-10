package com.larpclient;

import com.larpclient.module.Category;
import com.larpclient.module.Module;

public final class WavingFoliageModule extends Module {
   private static WavingFoliageModule P;
   public final com.larpclient.module.m N = this.addSetting(new com.larpclient.module.m("Leaves", "Leaves and vines sway", true));
   public final com.larpclient.module.m C = this.addSetting(
      new com.larpclient.module.m("Trees", "Bend natural trees from their planted base; log buildings stay still", true)
   );
   public final com.larpclient.module.m E = this.addSetting(
      new com.larpclient.module.m("Plants", "Grass, flowers, crops and saplings bend from their base", true)
   );
   public final com.larpclient.module.b u = this.addSetting(new com.larpclient.module.b("Strength", "How far foliage sways", 1.0, 0.2, 3.0, 0.1));
   public final com.larpclient.module.b i = this.addSetting(new com.larpclient.module.b("Speed", "Wind animation speed", 1.0, 0.2, 3.0, 0.1));
   public final com.larpclient.module.b L = this.addSetting(new com.larpclient.module.b("Gusts", "Variation in wind strength, percent", 60.0, 0.0, 100.0, 5.0));

   public WavingFoliageModule() {
      super("Waving Foliage", "Trees, grass, flowers and crops sway in wind and passing gusts", Category.RENDER, -1);
      P = this;
      this.C.a(() -> this.N.getValue());
      this.N.w(var1 -> this.V());
      this.C.w(var1 -> this.V());
      this.E.w(var1 -> this.V());
   }

   @Override
   public String getStatus() {
      return this.isRuntimeActive() && at.o() ? "Material fallback" : "";
   }

   public static WavingFoliageModule C() {
      return P;
   }

   private void V() {
      at.U();
   }

   @Override
   public void onEnable() {
      this.V();
   }

   @Override
   public void onDisable() {
      this.V();
      at.r();
   }
}
