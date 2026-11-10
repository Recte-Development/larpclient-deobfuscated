package com.larpclient.module;

import com.larpclient._q;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

public final class FakeFPSModule extends Module {
   private static final long C = 1000L;
   private static final int F = 99999;
   private static FakeFPSModule K;
   private final o H = this.addSetting(new o("Mode", "How the shown number is made", "Multiply", "Multiply", "Add", "Fixed"));
   private final b V = this.addSetting(new b("Multiplier", "Real FPS is multiplied by this", 2.0, 1.0, 20.0, 0.1));
   private final b u = this.addSetting(new b("Extra FPS", "Added to the real FPS", 200.0, 0.0, 5000.0, 10.0));
   private final b S = this.addSetting(new b("Shown FPS", "Shown instead of the real FPS", 1000.0, 10.0, 10000.0, 10.0));
   private final b I = this.addSetting(new b("Variation", "How far the number drifts each second so it looks measured", 3.0, 0.0, 25.0, 0.5).A("%"));
   private int l = -1;
   private long Z;
   private double f;

   public FakeFPSModule() {
      super("Fake FPS", "Shows a different FPS number on the HUD and F3 screen.", Category.PLAYER, -1);
      K = this;
      this.V.a(() -> this.H.w("Multiply"));
      this.u.a(() -> this.H.w("Add"));
      this.S.a(() -> this.H.w("Fixed"));
   }

   @Override
   public Set<l> getNetworkImpacts() {
      return Set.of(com.larpclient.module.l.E);
   }

   @Override
   public void onEnable() {
      this.l = -1;
      _q.z("FakeFps", "Showing {} FPS on the HUD and F3; the real value is unchanged", this.H.getValue());
   }

   public static int E(int var0) {
      if (K != null && K.isEnabled()) {
         FakeFPSModule var1 = K;
         return var1 != null && var1.isRuntimeActive() ? var1.J(var0) : var0;
      } else {
         return var0;
      }
   }

   private int J(int var1) {
      long var2 = System.currentTimeMillis();
      if (var1 != this.l || var2 - this.Z >= 1000L) {
         this.l = var1;
         this.Z = var2;
         this.f = ThreadLocalRandom.current().nextDouble(-1.0, 1.0);
      }

      return J(this.H.getValue(), var1, this.V.getValue(), this.u.getValue(), this.S.getValue(), this.I.getValue() / 100.0 * this.f);
   }

   public static int J(String var0, int var1, double var2, double var4, double var6, double var8) {
      double var10 = switch (var0) {
         case "Add" -> var1 + var4;
         case "Fixed" -> var6;
         default -> var1 * var2;
      };
      return Math.clamp(Math.round(var10 * (1.0 + var8)), 1, 99999);
   }
}
