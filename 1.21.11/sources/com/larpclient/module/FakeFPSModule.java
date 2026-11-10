package com.larpclient.module;

import com.larpclient.q8;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

public final class FakeFPSModule extends Module {
   private static final long X = 1000L;
   private static final int B = 99999;
   private static FakeFPSModule k;
   private final li h = this.addSetting(new li("Mode", "How the shown number is made", "Multiply", "Multiply", "Add", "Fixed"));
   private final lo E = this.addSetting(new lo("Multiplier", "Real FPS is multiplied by this", 2.0, 1.0, 20.0, 0.1));
   private final lo N = this.addSetting(new lo("Extra FPS", "Added to the real FPS", 200.0, 0.0, 5000.0, 10.0));
   private final lo v = this.addSetting(new lo("Shown FPS", "Shown instead of the real FPS", 1000.0, 10.0, 10000.0, 10.0));
   private final lo U = this.addSetting(new lo("Variation", "How far the number drifts each second so it looks measured", 3.0, 0.0, 25.0, 0.5).m("%"));
   private int u = -1;
   private long p;
   private double g;

   public FakeFPSModule() {
      super("Fake FPS", "Shows a different FPS number on the HUD and F3 screen.", Category.PLAYER, -1);
      k = this;
      this.E.O(() -> this.h.G("Multiply"));
      this.N.O(() -> this.h.G("Add"));
      this.v.O(() -> this.h.G("Fixed"));
   }

   @Override
   public Set<l5> getNetworkImpacts() {
      return Set.of(l5.E);
   }

   @Override
   public void onEnable() {
      this.u = -1;
      q8.G("FakeFps", "Showing {} FPS on the HUD and F3; the real value is unchanged", this.h.getValue());
   }

   public static int l(int var0) {
      if (k != null && k.isEnabled()) {
         FakeFPSModule var1 = k;
         return var1 != null && var1.isRuntimeActive() ? var1.P(var0) : var0;
      } else {
         return var0;
      }
   }

   private int P(int var1) {
      long var2 = System.currentTimeMillis();
      if (var1 != this.u || var2 - this.p >= 1000L) {
         this.u = var1;
         this.p = var2;
         this.g = ThreadLocalRandom.current().nextDouble(-1.0, 1.0);
      }

      return U(this.h.getValue(), var1, this.E.getValue(), this.N.getValue(), this.v.getValue(), this.U.getValue() / 100.0 * this.g);
   }

   public static int U(String var0, int var1, double var2, double var4, double var6, double var8) {
      double var10 = switch (var0) {
         case "Add" -> var1 + var4;
         case "Fixed" -> var6;
         default -> var1 * var2;
      };
      return Math.clamp(Math.round(var10 * (1.0 + var8)), 1, 99999);
   }
}
