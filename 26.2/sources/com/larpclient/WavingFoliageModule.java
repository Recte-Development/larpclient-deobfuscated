package com.larpclient;

import com.larpclient.module.Category;
import com.larpclient.module.Module;

public final class WavingFoliageModule extends Module {
   private static WavingFoliageModule K;
   public final com.larpclient.module.s F = this.addSetting(new com.larpclient.module.s("Leaves", "Leaves and vines sway", true));
   public final com.larpclient.module.s z = this.addSetting(
      new com.larpclient.module.s("Trees", "Bend natural trees from their planted base; log buildings stay still", true)
   );
   public final com.larpclient.module.s N = this.addSetting(
      new com.larpclient.module.s("Plants", "Grass, flowers, crops and saplings bend from their base", true)
   );
   public final com.larpclient.module.e v = this.addSetting(new com.larpclient.module.e("Strength", "How far foliage sways", 1.0, 0.2, 3.0, 0.1));
   public final com.larpclient.module.e G = this.addSetting(new com.larpclient.module.e("Speed", "Wind animation speed", 1.0, 0.2, 3.0, 0.1));
   public final com.larpclient.module.e t = this.addSetting(new com.larpclient.module.e("Gusts", "Variation in wind strength, percent", 60.0, 0.0, 100.0, 5.0));

   public WavingFoliageModule() {
      super("Waving Foliage", "Trees, grass, flowers and crops sway in wind and passing gusts", Category.RENDER, -1);
      K = this;
      this.z.P(() -> this.F.getValue());
      this.F.o(var1 -> this.j());
      this.z.o(var1 -> this.j());
      this.N.o(var1 -> this.j());
   }

   @Override
   public String getStatus() {
      return this.isRuntimeActive() && w6.C() ? "Material fallback" : "";
   }

   public static WavingFoliageModule h() {
      return K;
   }

   private void j() {
      w6.N();
   }

   @Override
   public void onEnable() {
      this.j();
   }

   @Override
   public void onDisable() {
      this.j();
      w6.P();
   }
}
