package com.larpclient.module;

import com.larpclient._q;
import com.larpclient.o8;

public final class AmbientParticlesModule extends Module {
   private static AmbientParticlesModule h;
   public final o P = this.addSetting(new o("Style", "Flying world ambience", "Both", "Both", "Sparkles", "Comets"));
   public final b w = this.u("Radius", "Spawn region in blocks", 12.0, 4.0, 32.0, 1.0);
   public final b L = this.u("Height", "Spawn region height in blocks", 10.0, 2.0, 24.0, 1.0);
   public final b D = this.u("Density", "Sparkles per second; fills the region on enable", 160.0, 0.0, 512.0, 1.0);
   public final b d = this.u("Lifetime", "Sparkle lifetime in seconds", 5.0, 1.0, 8.0, 0.25);
   public final b A = this.u("Speed", "Motion multiplier; zero freezes flight", 1.5, 0.0, 6.0, 0.1);
   public final b u = this.u("Vertical Travel", "Peak-to-peak rise and fall in blocks", 3.0, 0.0, 8.0, 0.25);
   public final b W = this.u("Sideways Travel", "Peak-to-peak sideways motion in blocks", 2.5, 0.0, 8.0, 0.25);
   public final b G = this.u("Motion Period", "Seconds per rise and fall at Speed 1", 5.0, 1.0, 12.0, 0.25);
   public final b l = this.u("Maximum Particles", "Hard ambient sprite limit, independent of combat", 1536.0, 128.0, 2048.0, 64.0);
   public final b S = this.u("Size", "Sparkle radius in blocks", 0.05, 0.015, 0.15, 0.005);
   public final b J = this.u("Comet Count", "Maximum simultaneous flying comets", 12.0, 0.0, 32.0, 1.0);
   public final b t = this.u("Trail Length", "Comet history in seconds", 0.8, 0.2, 2.0, 0.1);
   public final b b = this.u("Trail Width", "Comet ribbon width in blocks", 0.025, 0.01, 0.08, 0.005);
   public final m r = this.addSetting(new m("Theme Colors", "Use the current theme palette", true));
   public final h i = this.addSetting(new h("Color", "Sparkles and comet heads", -1517057));
   public final h s = this.addSetting(new h("Tail Color", "Custom comet tails and sparkle fade", -6794006));
   public final b T = this.u("Opacity", "Ambient effect strength", 0.7, 0.1, 1.0, 0.05);
   public final m O = this.addSetting(new m("Bloom", "Shared world glow", true));
   private final o8 Q = new o8();

   private b u(String var1, String var2, double var3, double var5, double var7, double var9) {
      return this.addSetting(new b(var1, var2, var3, var5, var7, var9));
   }

   public AmbientParticlesModule() {
      super("Ambient Particles", "Add sparkles and comets around you", Category.VISUALS, -1);
      h = this;
      this.D.a(() -> !this.P.w("Comets"));
      this.d.a(() -> !this.P.w("Comets"));

      for (r var4 : new r[]{this.J, this.t, this.b}) {
         var4.a(() -> !this.P.w("Sparkles"));
      }

      this.G.a(() -> this.u.getValue() > 0.0 || this.W.getValue() > 0.0);
      this.i.a(() -> !this.r.getValue());
      this.s.a(() -> !this.r.getValue());
      this.r.w(var0 -> _q.z("Ambient Particles", "Theme colors: {}", var0));
      this.P.w(var1 -> this.a());
   }

   public o8.f d() {
      return new o8.f(
         !this.P.w("Comets"),
         !this.P.w("Sparkles"),
         this.w.getValue(),
         this.L.getValue(),
         this.D.getValue(),
         this.d.getValue(),
         this.A.getValue(),
         this.S.getValue(),
         this.J.getValue().intValue(),
         this.t.getValue(),
         this.b.getValue(),
         this.r.getValue() ? com.larpclient.c.y : this.i.getValue(),
         this.r.getValue() ? com.larpclient.c.t : this.s.getValue(),
         this.T.getValue(),
         this.O.getValue(),
         this.u.getValue(),
         this.W.getValue(),
         this.G.getValue(),
         this.l.getValue().intValue()
      );
   }

   public static AmbientParticlesModule w() {
      return h;
   }

   public o8 L() {
      return this.Q;
   }

   public void a() {
      this.Q.V();
   }

   @Override
   public void onTick() {
      if (this.isInGame()) {
         int var10000 = mc.isPaused();
         boolean var2 = false;
         byte var1 = (byte)var10000;
         var10000 = Integer.signum(var1);
         if ((var10000 | -var10000) >>> 31 == 0) {
            this.Q.A(mc.gameRenderer.mainCamera().position(), this.d());
         }
      }
   }

   @Override
   public void onEnable() {
      this.a();
      _q.z(
         "Ambient Particles",
         "Enabled {}; density={}/s; sprite limit={}; comets={}; vertical travel={} blocks",
         this.P.getValue(),
         this.D.getValue(),
         this.l.getValue(),
         this.J.getValue(),
         this.u.getValue()
      );
   }

   @Override
   public void onDisable() {
      this.a();
   }
}
