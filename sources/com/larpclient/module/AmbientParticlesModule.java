package com.larpclient.module;

import com.larpclient.mh;
import com.larpclient.mt;

public final class AmbientParticlesModule extends Module {
   private static AmbientParticlesModule W;
   public final d6 M = this.addSetting(new d6("Style", "Flying world ambience", "Both", "Both", "Sparkles", "Comets"));
   public final d9 N = this.F("Radius", "Spawn region in blocks", 12.0, 4.0, 32.0, 1.0);
   public final d9 a = this.F("Height", "Spawn region height in blocks", 10.0, 2.0, 24.0, 1.0);
   public final d9 z = this.F("Density", "Sparkles per second; fills the region on enable", 160.0, 0.0, 512.0, 1.0);
   public final d9 b = this.F("Lifetime", "Sparkle lifetime in seconds", 5.0, 1.0, 8.0, 0.25);
   public final d9 L = this.F("Speed", "Motion multiplier; zero freezes flight", 1.5, 0.0, 6.0, 0.1);
   public final d9 s = this.F("Vertical Travel", "Peak-to-peak rise and fall in blocks", 3.0, 0.0, 8.0, 0.25);
   public final d9 I = this.F("Sideways Travel", "Peak-to-peak sideways motion in blocks", 2.5, 0.0, 8.0, 0.25);
   public final d9 d = this.F("Motion Period", "Seconds per rise and fall at Speed 1", 5.0, 1.0, 12.0, 0.25);
   public final d9 g = this.F("Maximum Particles", "Hard ambient sprite limit, independent of combat", 1536.0, 128.0, 2048.0, 64.0);
   public final d9 x = this.F("Size", "Sparkle radius in blocks", 0.05, 0.015, 0.15, 0.005);
   public final d9 l = this.F("Comet Count", "Maximum simultaneous flying comets", 12.0, 0.0, 32.0, 1.0);
   public final d9 O = this.F("Trail Length", "Comet history in seconds", 0.8, 0.2, 2.0, 0.1);
   public final d9 C = this.F("Trail Width", "Comet ribbon width in blocks", 0.025, 0.01, 0.08, 0.005);
   public final dh i = this.addSetting(new dh("Theme Colors", "Use the current theme palette", true));
   public final dk o = this.addSetting(new dk("Color", "Sparkles and comet heads", -1517057));
   public final dk e = this.addSetting(new dk("Tail Color", "Custom comet tails and sparkle fade", -6794006));
   public final d9 F = this.F("Opacity", "Ambient effect strength", 0.7, 0.1, 1.0, 0.05);
   public final dh P = this.addSetting(new dh("Bloom", "Shared world glow", true));
   private final mh f = new mh();

   private d9 F(String var1, String var2, double var3, double var5, double var7, double var9) {
      return this.addSetting(new d9(var1, var2, var3, var5, var7, var9));
   }

   public AmbientParticlesModule() {
      super("Ambient Particles", "Add sparkles and comets around you", Category.VISUALS, -1);
      W = this;
      this.z.H(() -> !this.M.S("Comets"));
      this.b.H(() -> !this.M.S("Comets"));

      for (df var4 : new df[]{this.l, this.O, this.C}) {
         var4.H(() -> !this.M.S("Sparkles"));
      }

      this.d.H(() -> this.s.P() > 0.0 || this.I.P() > 0.0);
      this.o.H(() -> !this.i.P());
      this.e.H(() -> !this.i.P());
      this.i.h(var0 -> mt.K("Ambient Particles", "Theme colors: {}", var0));
      this.M.h(var1 -> this.e());
   }

   public mh.r B() {
      return new mh.r(
         !this.M.S("Comets"),
         !this.M.S("Sparkles"),
         this.N.P(),
         this.a.P(),
         this.z.P(),
         this.b.P(),
         this.L.P(),
         this.x.P(),
         this.l.P().intValue(),
         this.O.P(),
         this.C.P(),
         this.i.P() ? com.larpclient.f.C : this.o.P(),
         this.i.P() ? com.larpclient.f.O : this.e.P(),
         this.F.P(),
         this.P.P(),
         this.s.P(),
         this.I.P(),
         this.d.P(),
         this.g.P().intValue()
      );
   }

   public static AmbientParticlesModule v() {
      return W;
   }

   public mh E() {
      return this.f;
   }

   public void e() {
      this.f.w();
   }

   @Override
   public void onTick() {
      if (this.isInGame()) {
         int var10000 = mc.isPaused();
         boolean var2 = false;
         byte var1 = (byte)var10000;
         var10000 = Integer.signum(var1);
         if ((var10000 | -var10000) >>> 31 == 0) {
            this.f.U(mc.gameRenderer.mainCamera().position(), this.B());
         }
      }
   }

   @Override
   public void onEnable() {
      this.e();
      mt.K(
         "Ambient Particles",
         "Enabled {}; density={}/s; sprite limit={}; comets={}; vertical travel={} blocks",
         this.M.P(),
         this.z.P(),
         this.g.P(),
         this.l.P(),
         this.s.P()
      );
   }

   @Override
   public void onDisable() {
      this.e();
   }
}
