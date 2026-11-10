package com.larpclient.module;

import com.larpclient.ez;
import com.larpclient.q8;
import com.larpclient.wx;

public final class AmbientParticlesModule extends Module {
   private static AmbientParticlesModule F;
   public final li t = this.addSetting(new li("Style", "Flying world ambience", "Both", "Both", "Sparkles", "Comets"));
   public final lo m = this.c("Radius", "Spawn region in blocks", 12.0, 4.0, 32.0, 1.0);
   public final lo o = this.c("Height", "Spawn region height in blocks", 10.0, 2.0, 24.0, 1.0);
   public final lo R = this.c("Density", "Sparkles per second; fills the region on enable", 160.0, 0.0, 512.0, 1.0);
   public final lo e = this.c("Lifetime", "Sparkle lifetime in seconds", 5.0, 1.0, 8.0, 0.25);
   public final lo A = this.c("Speed", "Motion multiplier; zero freezes flight", 1.5, 0.0, 6.0, 0.1);
   public final lo k = this.c("Vertical Travel", "Peak-to-peak rise and fall in blocks", 3.0, 0.0, 8.0, 0.25);
   public final lo c = this.c("Sideways Travel", "Peak-to-peak sideways motion in blocks", 2.5, 0.0, 8.0, 0.25);
   public final lo l = this.c("Motion Period", "Seconds per rise and fall at Speed 1", 5.0, 1.0, 12.0, 0.25);
   public final lo b = this.c("Maximum Particles", "Hard ambient sprite limit, independent of combat", 1536.0, 128.0, 2048.0, 64.0);
   public final lo W = this.c("Size", "Sparkle radius in blocks", 0.05, 0.015, 0.15, 0.005);
   public final lo h = this.c("Comet Count", "Maximum simultaneous flying comets", 12.0, 0.0, 32.0, 1.0);
   public final lo v = this.c("Trail Length", "Comet history in seconds", 0.8, 0.2, 2.0, 0.1);
   public final lo f = this.c("Trail Width", "Comet ribbon width in blocks", 0.025, 0.01, 0.08, 0.005);
   public final l3 p = this.addSetting(new l3("Theme Colors", "Use the current theme palette", true));
   public final l0 V = this.addSetting(new l0("Color", "Sparkles and comet heads", -1517057));
   public final l0 I = this.addSetting(new l0("Tail Color", "Custom comet tails and sparkle fade", -6794006));
   public final lo g = this.c("Opacity", "Ambient effect strength", 0.7, 0.1, 1.0, 0.05);
   public final l3 z = this.addSetting(new l3("Bloom", "Shared world glow", true));
   private final wx D = new wx();

   private lo c(String var1, String var2, double var3, double var5, double var7, double var9) {
      return this.addSetting(new lo(var1, var2, var3, var5, var7, var9));
   }

   public AmbientParticlesModule() {
      super("Ambient Particles", "Add sparkles and comets around you", Category.VISUALS, -1);
      F = this;
      this.R.O(() -> !this.t.G("Comets"));
      this.e.O(() -> !this.t.G("Comets"));

      for (lb var4 : new lb[]{this.h, this.v, this.f}) {
         var4.O(() -> !this.t.G("Sparkles"));
      }

      this.l.O(() -> this.k.getValue() > 0.0 || this.c.getValue() > 0.0);
      this.V.O(() -> !this.p.getValue());
      this.I.O(() -> !this.p.getValue());
      this.p.a(var0 -> q8.G("Ambient Particles", "Theme colors: {}", var0));
      this.t.a(var1 -> this.a());
   }

   public wx.v y() {
      return new wx.v(
         !this.t.G("Comets"),
         !this.t.G("Sparkles"),
         this.m.getValue(),
         this.o.getValue(),
         this.R.getValue(),
         this.e.getValue(),
         this.A.getValue(),
         this.W.getValue(),
         this.h.getValue().intValue(),
         this.v.getValue(),
         this.f.getValue(),
         this.p.getValue() ? ez.a : this.V.getValue(),
         this.p.getValue() ? ez.f : this.I.getValue(),
         this.g.getValue(),
         this.z.getValue(),
         this.k.getValue(),
         this.c.getValue(),
         this.l.getValue(),
         this.b.getValue().intValue()
      );
   }

   public static AmbientParticlesModule q() {
      return F;
   }

   public wx r() {
      return this.D;
   }

   public void a() {
      this.D.V();
   }

   @Override
   public void onTick() {
      if (this.isInGame()) {
         int var10000 = mc.method_1493();
         boolean var2 = false;
         byte var1 = (byte)var10000;
         var10000 = Integer.signum(var1);
         if ((var10000 | -var10000) >>> 31 == 0) {
            this.D.w(mc.field_1773.method_19418().method_71156(), this.y());
         }
      }
   }

   @Override
   public void onEnable() {
      this.a();
      q8.G(
         "Ambient Particles",
         "Enabled {}; density={}/s; sprite limit={}; comets={}; vertical travel={} blocks",
         this.t.getValue(),
         this.R.getValue(),
         this.b.getValue(),
         this.h.getValue(),
         this.k.getValue()
      );
   }

   @Override
   public void onDisable() {
      this.a();
   }
}
