package com.larpclient.module.misc;

import com.larpclient.module.world.ScanPhase;
import com.larpclient.util.misc.LarpLog;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

public final class FakeFPSModule extends Module {
   private static final long uPDATE_INTERVAL_MS = 1000L;
   private static final int mAX_FPS = 99999;
   private static FakeFPSModule instance;
   private final ModeSetting mode = this.addSetting(new ModeSetting("Mode", "How the shown number is made", "Multiply", "Multiply", "Add", "Fixed"));
   private final NumberSetting multiplier = this.addSetting(new NumberSetting("Multiplier", "Real FPS is multiplied by this", 2.0, 1.0, 20.0, 0.1));
   private final NumberSetting extraFps = this.addSetting(new NumberSetting("Extra FPS", "Added to the real FPS", 200.0, 0.0, 5000.0, 10.0));
   private final NumberSetting shownFps = this.addSetting(new NumberSetting("Shown FPS", "Shown instead of the real FPS", 1000.0, 10.0, 10000.0, 10.0));
   private final NumberSetting variation = this.addSetting(
      new NumberSetting("Variation", "How far the number drifts each second so it looks measured", 3.0, 0.0, 25.0, 0.5).setSuffix("%")
   );
   private int lastRealFps = -1;
   private long lastUpdateMs;
   private double jitter;

   public FakeFPSModule() {
      super("Fake FPS", "Shows a different FPS number on the HUD and F3 screen.", Category.PLAYER, -1);
      instance = this;
      this.multiplier.visibleWhen(() -> this.mode.isMode("Multiply"));
      this.extraFps.visibleWhen(() -> this.mode.isMode("Add"));
      this.shownFps.visibleWhen(() -> this.mode.isMode("Fixed"));
   }

   @Override
   public Set<ScanPhase> getNetworkImpacts() {
      return Set.of(ScanPhase.iDLE);
   }

   @Override
   public void onEnable() {
      this.lastRealFps = -1;
      LarpLog.info("FakeFps", "Showing {} FPS on the HUD and F3; the real value is unchanged", this.mode.getValue());
   }

   public static int getDisplayedFps(int var0) {
      if (instance != null && instance.isEnabled()) {
         FakeFPSModule var1 = instance;
         return var1 != null && var1.isRuntimeActive() ? var1.computeFps(var0) : var0;
      } else {
         return var0;
      }
   }

   private int computeFps(int var1) {
      long var2 = System.currentTimeMillis();
      if (var1 != this.lastRealFps || var2 - this.lastUpdateMs >= 1000L) {
         this.lastRealFps = var1;
         this.lastUpdateMs = var2;
         this.jitter = ThreadLocalRandom.current().nextDouble(-1.0, 1.0);
      }

      return computeFps(
         this.mode.getValue(),
         var1,
         this.multiplier.getValue(),
         this.extraFps.getValue(),
         this.shownFps.getValue(),
         this.variation.getValue() / 100.0 * this.jitter
      );
   }

   public static int computeFps(String var0, int var1, double var2, double var4, double var6, double var8) {
      double var10 = switch (var0) {
         case "Add" -> var1 + var4;
         case "Fixed" -> var6;
         default -> var1 * var2;
      };
      return Math.clamp(Math.round(var10 * (1.0 + var8)), 1, 99999);
   }
}
