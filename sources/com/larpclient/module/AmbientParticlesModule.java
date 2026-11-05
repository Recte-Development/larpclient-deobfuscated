package com.larpclient.module;

import com.larpclient.r4;
import com.larpclient.rv;
import com.larpclient.xl;

public final class AmbientParticlesModule extends Module {
   private static AmbientParticlesModule k;
   public final lp e = this.addSetting(new lp("Style", "Flying world ambience", "Both", "Both", "Sparkles", "Comets"));
   public final lc q = this.N("Radius", "Spawn region in blocks", 12.0, 4.0, 32.0, 1.0);
   public final lc H = this.N("Height", "Spawn region height in blocks", 10.0, 2.0, 24.0, 1.0);
   public final lc W = this.N("Density", "Sparkles per second; fills the region on enable", 160.0, 0.0, 512.0, 1.0);
   public final lc Y = this.N("Lifetime", "Sparkle lifetime in seconds", 5.0, 1.0, 8.0, 0.25);
   public final lc w = this.N("Speed", "Motion multiplier; zero freezes flight", 1.5, 0.0, 6.0, 0.1);
   public final lc J = this.N("Vertical Travel", "Peak-to-peak rise and fall in blocks", 3.0, 0.0, 8.0, 0.25);
   public final lc r = this.N("Sideways Travel", "Peak-to-peak sideways motion in blocks", 2.5, 0.0, 8.0, 0.25);
   public final lc d = this.N("Motion Period", "Seconds per rise and fall at Speed 1", 5.0, 1.0, 12.0, 0.25);
   public final lc Z = this.N("Maximum Particles", "Hard ambient sprite limit, independent of combat", 1536.0, 128.0, 2048.0, 64.0);
   public final lc u = this.N("Size", "Sparkle radius in blocks", 0.05, 0.015, 0.15, 0.005);
   public final lc D = this.N("Comet Count", "Maximum simultaneous flying comets", 12.0, 0.0, 32.0, 1.0);
   public final lc y = this.N("Trail Length", "Comet history in seconds", 0.8, 0.2, 2.0, 0.1);
   public final lc B = this.N("Trail Width", "Comet ribbon width in blocks", 0.025, 0.01, 0.08, 0.005);
   public final ln R = this.addSetting(new ln("Theme Colors", "Use the current theme palette", true));
   public final lv L = this.addSetting(new lv("Color", "Sparkles and comet heads", -1517057));
   public final lv x = this.addSetting(new lv("Tail Color", "Custom comet tails and sparkle fade", -6794006));
   public final lc E = this.N("Opacity", "Ambient effect strength", 0.7, 0.1, 1.0, 0.05);
   public final ln t = this.addSetting(new ln("Bloom", "Shared world glow", true));
   private final xl C = new xl();

   private lc N(String var1, String var2, double var3, double var5, double var7, double var9) {
      return this.addSetting(new lc(var1, var2, var3, var5, var7, var9));
   }

   public AmbientParticlesModule() {
      super("Ambient Particles", "Add sparkles and comets around you", Category.VISUALS, -1);
      k = this;
      this.W.x(() -> !this.e.i("Comets"));
      this.Y.x(() -> !this.e.i("Comets"));

      for (l6 var4 : new l6[]{this.D, this.y, this.B}) {
         var4.x(() -> !this.e.i("Sparkles"));
      }

      this.d.x(() -> this.J.getValue() > 0.0 || this.r.getValue() > 0.0);
      this.L.x(() -> !this.R.getValue());
      this.x.x(() -> !this.R.getValue());
      this.R.W(var0 -> r4.V("Ambient Particles", "Theme colors: {}", var0));
      this.e.W(var1 -> this.w());
   }

   public xl.c l() {
      return new xl.c(
         !this.e.i("Comets"),
         !this.e.i("Sparkles"),
         this.q.getValue(),
         this.H.getValue(),
         this.W.getValue(),
         this.Y.getValue(),
         this.w.getValue(),
         this.u.getValue(),
         this.D.getValue().intValue(),
         this.y.getValue(),
         this.B.getValue(),
         this.R.getValue() ? rv.e : this.L.getValue(),
         this.R.getValue() ? rv.GQ : this.x.getValue(),
         this.E.getValue(),
         this.t.getValue(),
         this.J.getValue(),
         this.r.getValue(),
         this.d.getValue(),
         this.Z.getValue().intValue()
      );
   }

   public static AmbientParticlesModule E() {
      return k;
   }

   public xl h() {
      return this.C;
   }

   public void w() {
      this.C.F();
   }

   @Override
   public void onTick() {
      if (this.isInGame()) {
         int var10000 = mc.isPaused();
         boolean var2 = false;
         byte var1 = (byte)var10000;
         var10000 = Integer.signum(var1);
         if ((var10000 | -var10000) >>> 31 == 0) {
            this.C.e(mc.gameRenderer.mainCamera().position(), this.l());
         }
      }
   }

   @Override
   public void onEnable() {
      this.w();
      r4.V(
         "Ambient Particles",
         "Enabled {}; density={}/s; sprite limit={}; comets={}; vertical travel={} blocks",
         this.e.getValue(),
         this.W.getValue(),
         this.Z.getValue(),
         this.D.getValue(),
         this.J.getValue()
      );
   }

   @Override
   public void onDisable() {
      this.w();
   }
}
