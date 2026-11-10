package com.larpclient.module;

import com.larpclient.ho;

public final class AmbientParticlesModule extends Module {
   private static AmbientParticlesModule e;
   public final l T = this.addSetting(new l("Style", "Flying world ambience", "Both", "Both", "Sparkles", "Comets"));
   public final e l = this.z("Radius", "Spawn region in blocks", 12.0, 4.0, 32.0, 1.0);
   public final e n = this.z("Height", "Spawn region height in blocks", 10.0, 2.0, 24.0, 1.0);
   public final e G = this.z("Density", "Sparkles per second; fills the region on enable", 160.0, 0.0, 512.0, 1.0);
   public final e y = this.z("Lifetime", "Sparkle lifetime in seconds", 5.0, 1.0, 8.0, 0.25);
   public final e I = this.z("Speed", "Motion multiplier; zero freezes flight", 1.5, 0.0, 6.0, 0.1);
   public final e S = this.z("Vertical Travel", "Peak-to-peak rise and fall in blocks", 3.0, 0.0, 8.0, 0.25);
   public final e A = this.z("Sideways Travel", "Peak-to-peak sideways motion in blocks", 2.5, 0.0, 8.0, 0.25);
   public final e z = this.z("Motion Period", "Seconds per rise and fall at Speed 1", 5.0, 1.0, 12.0, 0.25);
   public final e u = this.z("Maximum Particles", "Hard ambient sprite limit, independent of combat", 1536.0, 128.0, 2048.0, 64.0);
   public final e m = this.z("Size", "Sparkle radius in blocks", 0.05, 0.015, 0.15, 0.005);
   public final e b = this.z("Comet Count", "Maximum simultaneous flying comets", 12.0, 0.0, 32.0, 1.0);
   public final e N = this.z("Trail Length", "Comet history in seconds", 0.8, 0.2, 2.0, 0.1);
   public final e Y = this.z("Trail Width", "Comet ribbon width in blocks", 0.025, 0.01, 0.08, 0.005);
   public final s D = this.addSetting(new s("Theme Colors", "Use the current theme palette", true));
   public final i E = this.addSetting(new i("Color", "Sparkles and comet heads", -1517057));
   public final i r = this.addSetting(new i("Tail Color", "Custom comet tails and sparkle fade", -6794006));
   public final e h = this.z("Opacity", "Ambient effect strength", 0.7, 0.1, 1.0, 0.05);
   public final s w = this.addSetting(new s("Bloom", "Shared world glow", true));
   private final com.larpclient.th C = new com.larpclient.th();

   private e z(String var1, String var2, double var3, double var5, double var7, double var9) {
      return this.addSetting(new e(var1, var2, var3, var5, var7, var9));
   }

   public AmbientParticlesModule() {
      super("Ambient Particles", "Add sparkles and comets around you", Category.VISUALS, -1);
      e = this;
      this.G.P(() -> !this.T.O("Comets"));
      this.y.P(() -> !this.T.O("Comets"));

      for (q var4 : new q[]{this.b, this.N, this.Y}) {
         var4.P(() -> !this.T.O("Sparkles"));
      }

      this.z.P(() -> this.S.getValue() > 0.0 || this.A.getValue() > 0.0);
      this.E.P(() -> !this.D.getValue());
      this.r.P(() -> !this.D.getValue());
      this.D.o(var0 -> com.larpclient.h.A("Ambient Particles", "Theme colors: {}", var0));
      this.T.o(var1 -> this.a());
   }

   public com.larpclient.th.n C() {
      return new com.larpclient.th.n(
         !this.T.O("Comets"),
         !this.T.O("Sparkles"),
         this.l.getValue(),
         this.n.getValue(),
         this.G.getValue(),
         this.y.getValue(),
         this.I.getValue(),
         this.m.getValue(),
         this.b.getValue().intValue(),
         this.N.getValue(),
         this.Y.getValue(),
         this.D.getValue() ? ho.J : this.E.getValue(),
         this.D.getValue() ? ho.ds : this.r.getValue(),
         this.h.getValue(),
         this.w.getValue(),
         this.S.getValue(),
         this.A.getValue(),
         this.z.getValue(),
         this.u.getValue().intValue()
      );
   }

   public static AmbientParticlesModule f() {
      return e;
   }

   public com.larpclient.th B() {
      return this.C;
   }

   public void a() {
      this.C.b();
   }

   @Override
   public void onTick() {
      if (this.isInGame()) {
         int var10000 = mc.isPaused();
         boolean var2 = false;
         byte var1 = (byte)var10000;
         var10000 = Integer.signum(var1);
         if ((var10000 | -var10000) >>> 31 == 0) {
            this.C.d(mc.gameRenderer.mainCamera().position(), this.C());
         }
      }
   }

   @Override
   public void onEnable() {
      this.a();
      com.larpclient.h.A(
         "Ambient Particles",
         "Enabled {}; density={}/s; sprite limit={}; comets={}; vertical travel={} blocks",
         this.T.getValue(),
         this.G.getValue(),
         this.u.getValue(),
         this.b.getValue(),
         this.S.getValue()
      );
   }

   @Override
   public void onDisable() {
      this.a();
   }
}
