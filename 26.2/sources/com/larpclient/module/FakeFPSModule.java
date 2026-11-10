package com.larpclient.module;

import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

public final class FakeFPSModule extends Module {
   private static final long S = 1000L;
   private static final int a = 99999;
   private static FakeFPSModule c;
   private final l P = this.addSetting(new l("Mode", "How the shown number is made", "Multiply", "Multiply", "Add", "Fixed"));
   private final e w = this.addSetting(new e("Multiplier", "Real FPS is multiplied by this", 2.0, 1.0, 20.0, 0.1));
   private final e H = this.addSetting(new e("Extra FPS", "Added to the real FPS", 200.0, 0.0, 5000.0, 10.0));
   private final e m = this.addSetting(new e("Shown FPS", "Shown instead of the real FPS", 1000.0, 10.0, 10000.0, 10.0));
   private final e B = this.addSetting(new e("Variation", "How far the number drifts each second so it looks measured", 3.0, 0.0, 25.0, 0.5).V("%"));
   private int N = -1;
   private long u;
   private double J;

   public FakeFPSModule() {
      super("Fake FPS", "Shows a different FPS number on the HUD and F3 screen.", Category.PLAYER, -1);
      c = this;
      this.w.P(() -> this.P.O("Multiply"));
      this.H.P(() -> this.P.O("Add"));
      this.m.P(() -> this.P.O("Fixed"));
   }

   @Override
   public Set<m> getNetworkImpacts() {
      return Set.of(com.larpclient.module.m.k);
   }

   @Override
   public void onEnable() {
      this.N = -1;
      com.larpclient.h.A("FakeFps", "Showing {} FPS on the HUD and F3; the real value is unchanged", this.P.getValue());
   }

   public static int p(int var0) {
      if (c != null && c.isEnabled()) {
         FakeFPSModule var1 = c;
         return var1 != null && var1.isRuntimeActive() ? var1.Q(var0) : var0;
      } else {
         return var0;
      }
   }

   private int Q(int var1) {
      long var2 = System.currentTimeMillis();
      if (var1 != this.N || var2 - this.u >= 1000L) {
         this.N = var1;
         this.u = var2;
         this.J = ThreadLocalRandom.current().nextDouble(-1.0, 1.0);
      }

      return w(this.P.getValue(), var1, this.w.getValue(), this.H.getValue(), this.m.getValue(), this.B.getValue() / 100.0 * this.J);
   }

   public static int w(String var0, int var1, double var2, double var4, double var6, double var8) {
      double var10 = switch (var0) {
         case "Add" -> var1 + var4;
         case "Fixed" -> var6;
         default -> var1 * var2;
      };
      return Math.clamp(Math.round(var10 * (1.0 + var8)), 1, 99999);
   }
}
