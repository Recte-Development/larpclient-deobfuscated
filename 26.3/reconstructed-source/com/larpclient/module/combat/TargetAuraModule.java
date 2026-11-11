package com.larpclient.module.combat;

import com.larpclient.config.HitEffectConfig;
import com.larpclient.gui.theme.Theme;
import com.larpclient.module.misc.BooleanSetting;
import com.larpclient.module.misc.Category;
import com.larpclient.module.misc.ColorSetting;
import com.larpclient.module.misc.ModeSetting;
import com.larpclient.module.misc.Module;
import com.larpclient.module.misc.NumberSetting;
import com.larpclient.module.misc.Setting;
import com.larpclient.render.misc.VisualsRenderer;
import com.larpclient.render.particle.ParticleSystem;
import com.larpclient.render.world.EffectsRenderer;
import com.larpclient.util.data.TickTimer;
import com.larpclient.util.entity.TargetResolver;
import com.larpclient.util.math.CatmullRomSpline;
import com.larpclient.util.math.Easing;
import com.larpclient.util.math.PhaseAccumulator;
import com.larpclient.util.misc.LarpLog;
import com.larpclient.util.misc.PerfProfiler;
import com.larpclient.util.render.ColorUtil;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public final class TargetAuraModule extends Module {
   private static TargetAuraModule instance;
   private final ModeSetting style = this.addSetting(
      new ModeSetting("Style", "Composed target effect", "Orbit", "Orbit", "Helix", "Energy", "Halo", "Atom", "Vortex", "Scanner", "Star of David")
   );
   private final ModeSetting source = this.addSetting(new ModeSetting("Source", "Read-only target source", "Auto", "Auto", "Aura", "Combat", "Crosshair") {
      @Override
      public void setValue(String var1) {
         super.setValue(TargetAuraModule.normalizeSource(var1));
      }

      @Override
      public boolean acceptsValue(String var1) {
         return super.acceptsValue(TargetAuraModule.normalizeSource(var1));
      }
   });
   private final NumberSetting speed = this.addSetting(new NumberSetting("Speed", "Cycles per second", 0.65, 0.1, 3.0, 0.05));
   private final NumberSetting width = this.addSetting(new NumberSetting("Width", "Trail width", 0.045, 0.01, 0.15, 0.005));
   private final NumberSetting particlesPerSecond = this.addSetting(new NumberSetting("Particles Per Second", "Persistent aura emission", 18.0, 0.0, 60.0, 1.0));
   private final BooleanSetting bloom = this.addSetting(new BooleanSetting("Bloom", "Explicit glow pass", true));
   private final BooleanSetting themeColors = this.addSetting(new BooleanSetting("Theme Colors", "Follow current theme", true));
   private final ColorSetting color = this.addSetting(new ColorSetting("Color", "Primary color", Theme.accent));
   private final ColorSetting gradient = this.addSetting(new ColorSetting("Gradient", "Secondary color", Theme.accentEnd));
   private final HitEffectConfig hitEffect = new HitEffectConfig();
   private double particleAccumulator;
   private int particleCounter;
   private final PhaseAccumulator phase = new PhaseAccumulator();
   private final CatmullRomSpline.Ribbon[] ribbons = new CatmullRomSpline.Ribbon[18];
   private double starCenterY;
   private final ArrayList<TargetAuraModule.DiscMark> discMarks;
   private final ArrayList<TargetAuraModule.SpriteMark> spriteMarks;
   private boolean hasTarget;
   private boolean bloomActive;
   private Object lastLevel;
   private int lastTargetId;
   private Vec3 lastTargetPos;

   private static String normalizeSource(String var0) {
      return "KillAura".equalsIgnoreCase(var0) ? "Aura" : var0;
   }

   public TargetAuraModule() {
      super("TargetAura", "Show glowing effects around your target", Category.VISUALS, -1);

      for (int var1 = 0; var1 < this.ribbons.length; var1++) {
         this.ribbons[var1] = new CatmullRomSpline.Ribbon(65);
      }

      this.starCenterY = Double.NaN;
      this.discMarks = new ArrayList<>(10);
      this.spriteMarks = new ArrayList<>(8);
      this.lastTargetId = -1;
      instance = this;
      this.color.visibleWhen(() -> !this.themeColors.getValue());
      this.gradient.visibleWhen(() -> !this.themeColors.getValue());
      this.style.onChange(var0 -> LarpLog.info("TargetAura", "Style changed: {}", var0));
   }

   public static TargetAuraModule getInstance() {
      return instance;
   }

   public List<Setting<?>> getStyleSettings() {
      return List.of(this.style, this.source, this.speed, this.width, this.particlesPerSecond, this.bloom);
   }

   public List<Setting<?>> addDisc() {
      return List.of(this.themeColors, this.color, this.gradient);
   }

   public boolean isBloomActive() {
      return !this.isEnabled() ? false : this.hasTarget && this.bloomActive;
   }

   @Override
   public void onEnable() {
      LarpLog.info(
         "TargetAura",
         "Expanded body-height aura: style={}, width={}, particle rate={}/s; readable base with bloom off",
         this.style.getValue(),
         this.width.getValue(),
         this.particlesPerSecond.getValue()
      );
   }

   @Override
   public void onTick() {
      LivingEntity var1 = TargetResolver.resolveTarget(this.source.getValue());
      if (var1 == null) {
         this.particleAccumulator = 0.0;
      } else {
         this.resetOnTargetChange(var1, var1.position());
         this.particleAccumulator = this.particleAccumulator + this.particlesPerSecond.getValue() * 0.05;
         int var2 = (int)this.particleAccumulator;
         this.particleAccumulator -= var2;
         boolean var5 = false;
         int var4 = var2;
         int var10000 = Integer.signum(var4);
         if ((var10000 | -var10000) >>> 31 != 0) {
            HitEffectConfig var3 = this.configureParticles(var2, var1.getBbWidth(), var1.getBbHeight());
            VisualsRenderer.particles.spawn(var1.position().add(0.0, this.getEmitHeight(var1.getBbHeight()), 0.0), var3);
         }
      }
   }

   private double getEmitHeight(double var1) {
      if (this.style.isMode("Halo")) {
         return var1 + 0.22;
      } else {
         return this.style.isMode("Star of David") && Double.isFinite(this.starCenterY) ? this.starCenterY : var1 * 0.65;
      }
   }

   public HitEffectConfig configureParticles(int var1, double var2, double var4) {
      this.hitEffect.seed = 2;
      this.hitEffect.particleCount = var1;
      this.hitEffect.shape = HitEffectConfig.Shape.SPHERE;
      boolean var6 = this.style.isMode("Star of David");
      this.hitEffect.scale = this.hitEffect.opacity = (var2 * 0.75 + 0.35) * (var6 ? 2.6 : 2.0);
      this.hitEffect.stretch = var4 * (this.style.isMode("Halo") ? 0.08 : (var6 ? 0.1 : 0.8));
      this.hitEffect.primaryColor = this.themeColors.getValue() ? Theme.accent : this.color.getValue();
      this.hitEffect.secondaryColor = this.themeColors.getValue() ? Theme.accentEnd : this.gradient.getValue();
      this.hitEffect.size = 0.85;
      this.hitEffect.speed = 1.4;
      this.hitEffect.gravity = 0.055;
      this.hitEffect.spread = 0.13;
      this.hitEffect.maxParticles = 128;
      this.hitEffect.lifetime = -0.05;
      this.hitEffect.intensity = 0.1;
      this.hitEffect.glow = 0.12;
      this.hitEffect.drag = 0.3;
      this.hitEffect.turbulence = 1.5;
      this.hitEffect.enabled = this.bloom.getValue();
      this.hitEffect.style = ParticleSystem.Style.GLOW;
      this.hitEffect.blendMode = HitEffectConfig.BlendMode.ADDITIVE;
      this.hitEffect.brightness = 1.0;
      if (var6) {
         this.hitEffect.turbulence = 0.6;
         this.hitEffect.drag = 0.18;
      }

      if (!this.style.isMode("Halo")) {
         this.hitEffect.primaryColor = ColorUtil.multiplyAlpha(this.hitEffect.primaryColor, 0.85);
         this.hitEffect.secondaryColor = ColorUtil.multiplyAlpha(this.hitEffect.secondaryColor, 0.65);
         this.hitEffect.spread = 0.11;
         if (++this.particleCounter % 9 == 0) {
            this.hitEffect.style = ParticleSystem.Style.SPARK;
            this.hitEffect.gravity = 0.025;
            this.hitEffect.spread = 0.045;
            this.hitEffect.size = 0.18;
            this.hitEffect.speed = 0.35;
            this.hitEffect.intensity = 0.75;
            this.hitEffect.brightness = 0.35;
            this.hitEffect.drag = 0.08;
            this.hitEffect.primaryColor = ColorUtil.lerpColor(this.hitEffect.primaryColor, this.hitEffect.primaryColor & 0xFF000000 | 16777215, 0.35);
         }
      }

      return this.hitEffect;
   }

   public void updateEffects(float var1) {
      if (this.isEnabled()) {
         this.hasTarget = (boolean)0;
         this.starCenterY = Double.NaN;
         this.discMarks.clear();
         this.spriteMarks.clear();

         for (CatmullRomSpline.Ribbon var5 : this.ribbons) {
            var5.clear();
         }

         if (this.isRuntimeActive()) {
            LivingEntity var24 = TargetResolver.resolveTarget(this.source.getValue());
            double var25 = this.phase.advance(TickTimer.getTime(), this.speed.getValue());
            if (var24 == null) {
               this.lastTargetPos = null;
               this.lastTargetId = -1;
            } else {
               Vec3 var26 = var24.getPosition(var1);
               this.resetOnTargetChange(var24, var26);
               this.hasTarget = (boolean)1;
               this.bloomActive = this.bloom.getValue();
               double var6 = var24.getBbWidth() * 0.75 + 0.35;
               double var8 = var24.getBbHeight();
               int var10 = this.themeColors.getValue() ? Theme.accent : this.color.getValue();
               int var11 = this.themeColors.getValue() ? Theme.accentEnd : this.gradient.getValue();
               if (this.style.isMode("Orbit") || this.style.isMode("Helix")) {
                  boolean var27 = this.style.isMode("Helix");

                  for (int var13 = 0; var13 < 2; var13++) {
                     CatmullRomSpline.Ribbon var14 = this.ribbons[var13];

                     for (int var15 = 0; var15 <= 64; var15++) {
                        double var16 = var15 / 64.0;
                        Vec3 var18 = orbitPoint(var26, var6, var8, var25, var13, var16, var27);
                        double var19 = var27 ? Math.sin(Math.PI * var16) : isBloomActive(var16);
                        double var21 = this.width.getValue() * var19 * 3.4;
                        int var23 = ColorUtil.multiplyAlpha(
                           ColorUtil.lerpColor(var11, var10, var16), 0.92 * (var27 ? Math.sin(Math.PI * var16) : Math.pow(var16, 0.65))
                        );
                        var14.addPoint(var18, var21, var23);
                     }

                     double var31 = var27 ? 0.5 + 0.44 * Math.sin(var25 * 0.33 + var13 * Math.PI) : 1.0;
                     this.addSprite(
                        orbitPoint(var26, var6, var8, var25, var13, var31, var27),
                        this.width.getValue() * (var27 ? 1.6 : 2.1),
                        ColorUtil.lerpColor(var11, var10, var31)
                     );
                  }

                  this.addDisc(var26.add(0.0, 0.055, 0.0), var6 * 1.05, this.width.getValue() * 2.0, ColorUtil.multiplyAlpha(var11, 0.55));
               } else if (this.style.isMode("Energy")) {
                  this.addSprite(this.ribbons[0], var26, var6, var8 * 0.48, var8, var25, var10, var11);
                  this.addSprite(this.ribbons[1], var26, var6 * 0.92, var8 * 0.88, var8, -var25 * 0.83 + 2.1, var11, var10);
                  this.addSprite(energyPoint(var26, var6, var8 * 0.48, var8, var25, TickTimer.getTime(), 1.0), this.width.getValue() * 2.1, var10);
                  this.addSprite(
                     energyPoint(var26, var6 * 0.92, var8 * 0.88, var8, -var25 * 0.83 + 2.1, TickTimer.getTime(), 1.0), this.width.getValue() * 2.1, var11
                  );
                  this.addDisc(var26.add(0.0, 0.055, 0.0), var6 * 1.1, this.width.getValue() * 2.5, ColorUtil.multiplyAlpha(var10, 0.6));
               } else if (this.style.isMode("Atom")) {
                  this.buildAtom(var26, var6, var8, var25, var10, var11);
               } else if (this.style.isMode("Vortex")) {
                  this.buildVortex(var26, var6, var8, var25, var10, var11);
               } else if (this.style.isMode("Scanner")) {
                  this.buildScanner(var26, var6, var8, var25, var10, var11);
               } else if (this.style.isMode("Star of David")) {
                  this.buildStarOfDavid(var26, var6, var8, var25, var10, var11);
               } else {
                  Vec3 var12 = var26.add(0.0, var8 + 0.22 + Math.sin(var25 * 0.6) * 0.045, 0.0);
                  this.addDisc(var12, var6 * 1.05, this.width.getValue() * 3.0, ColorUtil.multiplyAlpha(var10, 0.95));
                  this.addDisc(var12, var6 * 1.05, this.width.getValue() * 6.0, ColorUtil.multiplyAlpha(var11, 0.25));
                  this.addSprite(this.ribbons[0], var26, var6 * 1.05, var12.y - var26.y, var8 * 0.08, var25 * 0.65, var10, var11);
                  this.addSprite(
                     energyPoint(var26, var6 * 1.05, var12.y - var26.y, var8 * 0.08, var25 * 0.65, TickTimer.getTime(), 1.0),
                     this.width.getValue() * 1.8,
                     var10
                  );
                  this.addDisc(var26.add(0.0, 0.055, 0.0), var6 * 1.05, this.width.getValue() * 2.0, ColorUtil.multiplyAlpha(var11, 0.55));
               }

               Vec3 var28 = Minecraft.getInstance().gameRenderer.mainCamera().position();

               for (CatmullRomSpline.Ribbon var33 : this.ribbons) {
                  var33.computeOffsets(var28);
               }
            }
         }
      }
   }

   private void resetOnTargetChange(LivingEntity var1, Vec3 var2) {
      Level var3 = var1.level();
      if (this.lastLevel != var3 || this.lastTargetId != var1.getId() || this.lastTargetPos != null && this.lastTargetPos.distanceToSqr(var2) > 64.0) {
         this.particleAccumulator = 0.0;
         VisualsRenderer.particles.removeByEmitter(2);
      }

      this.lastLevel = var3;
      this.lastTargetId = var1.getId();
      this.lastTargetPos = var2;
   }

   private void addDisc(Vec3 var1, double var2, double var4, int var6) {
      this.discMarks.add(new TargetAuraModule.DiscMark(var1, var2, var4, var6));
   }

   private void addSprite(Vec3 var1, double var2, int var4) {
      this.spriteMarks.add(new TargetAuraModule.SpriteMark(var1, var2, ColorUtil.lerpColor(var4, var4 & 0xFF000000 | 16777215, 0.55)));
   }

   public static Vec3 orbitPoint(Vec3 var0, double var1, double var3, double var5, int var7, double var8, boolean var10) {
      double var11 = var5 + var7 * Math.PI + (var10 ? var8 * Math.PI * 4.0 : -(1.0 - var8) * Math.PI * 1.85);
      double var13 = Math.sin(var11 * 2.3 + var5 * 0.23 + var7);
      double var15 = var1 * (1.0 + 0.045 * var13);
      double var17 = var10 ? var3 * (0.06 + 0.97 * var8) : 0.54 * var3 + Math.sin(var11 * 1.2) * var3 * 0.34;
      var17 += Math.sin(var11 * 1.7 - var5 * 0.31) * var3 * 0.025;
      return var0.add(Math.cos(var11) * var15, var17, Math.sin(var11) * var15);
   }

   private void buildAtom(Vec3 var1, double var2, double var4, double var6, int var8, int var9) {
      Vec3 var10 = var1.add(0.0, var4 * 0.55, 0.0);
      double var11 = var2 * 1.15;
      double var13 = this.width.getValue();

      for (int var15 = 0; var15 < 3; var15++) {
         double var16 = var6 * 0.18 + var15 * Math.PI * 2.0 / 3.0;
         double var18 = var6 * (1.0 + var15 * 0.17) + var15 * 2.1;
         int var20 = ColorUtil.lerpColor(var8, var9, var15 / 2.0);

         for (int var21 = 0; var21 <= 48; var21++) {
            this.ribbons[var15].addPoint(atomPoint(var10, var11, var4, var16, var21 / 48.0 * Math.PI * 2.0), var13 * 1.3, ColorUtil.multiplyAlpha(var20, 0.24));
         }

         for (int var24 = 0; var24 <= 32; var24++) {
            double var22 = var24 / 32.0;
            this.ribbons[var15 + 3]
               .addPoint(
                  atomPoint(var10, var11, var4, var16, var18 - (1.0 - var22) * Math.PI * 1.1),
                  var13 * isBloomActive(var22) * 3.2,
                  ColorUtil.multiplyAlpha(ColorUtil.lerpColor(var9, var8, var22), 0.95 * Math.pow(var22, 0.65))
               );
         }

         this.addSprite(atomPoint(var10, var11, var4, var16, var18), var13 * 2.0, var20);
      }

      this.addDisc(var1.add(0.0, 0.055, 0.0), var2 * 1.05, var13 * 2.0, ColorUtil.multiplyAlpha(var9, 0.5));
   }

   public static Vec3 atomPoint(Vec3 var0, double var1, double var3, double var5, double var7) {
      double var9 = Math.toRadians(56.0);
      double var11 = Math.min(var1 * Math.sin(var9), var3 * 0.42);
      double var13 = Math.cos(var7);
      double var15 = Math.sin(var7);
      double var17 = var1 * Math.cos(var9) * var15;
      return var0.add(Math.cos(var5) * var1 * var13 - Math.sin(var5) * var17, var11 * var15, Math.sin(var5) * var1 * var13 + Math.cos(var5) * var17);
   }

   private void buildVortex(Vec3 var1, double var2, double var4, double var6, int var8, int var9) {
      double var10 = this.width.getValue();

      for (int var12 = 0; var12 < 3; var12++) {
         double var13 = fract(var6 * 0.12 + var12 / 3.0);

         for (int var15 = 0; var15 <= 64; var15++) {
            double var16 = var15 / 64.0;
            double var18 = orbitPoint(var16);
            double var20 = Math.exp(-updateEffects((var16 - var13) / 0.09));
            this.ribbons[var12]
               .addPoint(
                  vortexPoint(var1, var2, var4, var6, var12, var16),
                  var10 * (1.6 + 1.6 * var16) * (1.0 + 0.7 * var20) * var18,
                  ColorUtil.multiplyAlpha(ColorUtil.lerpColor(var8, var9, var16), (0.38 + 0.57 * var20) * var18)
               );
         }

         this.addSprite(vortexPoint(var1, var2, var4, var6, var12, var13), var10 * 1.9 * orbitPoint(var13), ColorUtil.lerpColor(var8, var9, var13));
      }

      this.addDisc(var1.add(0.0, 0.055, 0.0), var2 * 0.8, var10 * 2.0, ColorUtil.multiplyAlpha(var8, 0.55));
      this.addDisc(var1.add(0.0, var4 * 1.1, 0.0), var2 * 1.36, var10 * 1.6, ColorUtil.multiplyAlpha(var9, 0.32));
   }

   public static Vec3 vortexPoint(Vec3 var0, double var1, double var3, double var5, int var7, double var8) {
      double var10 = var5 * 0.9 + var7 * Math.PI * 2.0 / 3.0 - var8 * Math.PI * 2.3;
      double var12 = var1 * (0.72 + 0.62 * Math.pow(var8, 1.3));
      return var0.add(Math.cos(var10) * var12, var3 * (0.02 + 1.08 * var8), Math.sin(var10) * var12);
   }

   private static double orbitPoint(double var0) {
      return Math.pow(Math.sin(Math.PI * Math.clamp(var0, 0.0, 1.0)), 0.6);
   }

   private void buildScanner(Vec3 var1, double var2, double var4, double var6, int var8, int var9) {
      double var10 = this.width.getValue();
      double var12 = var6 * 0.45;
      double var14 = updateEffects(var4, var12);
      double var16 = Math.sin(var12);
      double var18 = Math.abs(var16) * var4 * 0.32;
      double var20 = Math.min(1.0, var18 * 8.0);
      this.addDisc(var1.add(0.0, var14, 0.0), var2 * 1.02, var10 * 2.6, ColorUtil.multiplyAlpha(var8, 0.95));
      this.addDisc(var1.add(0.0, var14, 0.0), var2 * 1.02, var10 * 7.0, ColorUtil.multiplyAlpha(var9, 0.28));

      for (int var22 = 1; var22 <= 6 && var20 > 0.02; var22++) {
         double var23 = var22 / 6.0;
         this.addDisc(
            var1.add(0.0, var14 - Math.signum(var16) * var18 * var23, 0.0),
            var2 * (1.02 - 0.04 * var23),
            var10 * 1.6,
            ColorUtil.multiplyAlpha(ColorUtil.lerpColor(var8, var9, var23), 0.55 * Math.pow(1.0 - var23, 1.4) * var20)
         );
      }

      for (int var25 = 0; var25 < 2; var25++) {
         double var26 = var6 * 1.1 + var25 * Math.PI;
         this.addSprite(var1.add(Math.cos(var26) * var2 * 1.02, var14, Math.sin(var26) * var2 * 1.02), var10 * 1.8, var25 == 0 ? var8 : var9);
      }
   }

   public static double updateEffects(double var0, double var2) {
      return 0.04 + (0.5 - 0.5 * Math.cos(var2)) * (var0 + 0.06);
   }

   private void buildStarOfDavid(Vec3 var1, double var2, double var4, double var6, int var8, int var9) {
      double var10 = this.width.getValue();
      double var12 = var6 * 0.45;
      double var14 = Math.sin(var12);
      double var16 = var2 * 1.3 * (1.0 + 0.025 * Math.sin(var6 * 0.5));
      double var18 = var6 * 0.1;
      double var20 = updateEffects(var4, var12);
      double var22 = Math.abs(var14) * var4 * 0.32;
      double var24 = Math.min(1.0, var22 * 8.0);
      this.starCenterY = var20;
      Vec3 var26 = var1.add(0.0, var20, 0.0);
      this.getInstance(var26, var16, var18, var6, var8, var9, 1.0, 1.0, 0, true);

      for (int var27 = 1; var27 <= 2 && var24 > 0.02; var27++) {
         double var28 = var27 / 2.0;
         this.getInstance(
            var1.add(0.0, var20 - Math.signum(var14) * var22 * var28, 0.0),
            var16 * (1.0 - 0.04 * var28),
            var18,
            var6,
            var8,
            var9,
            (var27 == 1 ? 0.42 : 0.16) * var24,
            0.7,
            var27 * 6,
            false
         );
      }

      this.addDisc(var26, var16 * 1.12, var10 * 2.4, ColorUtil.multiplyAlpha(var8, 0.85));
      this.addDisc(var26, var16 * 1.24, var10 * 1.2, ColorUtil.multiplyAlpha(var9, 0.45));

      for (int var30 = 1; var30 <= 4 && var24 > 0.02; var30++) {
         double var31 = var30 / 4.0;
         this.addDisc(
            var1.add(0.0, var20 - Math.signum(var14) * var22 * var31, 0.0),
            var16 * 1.12 * (1.0 - 0.04 * var31),
            var10 * 1.4,
            ColorUtil.multiplyAlpha(ColorUtil.lerpColor(var8, var9, var31), 0.4 * Math.pow(1.0 - var31, 1.4) * var24)
         );
      }
   }

   private void getInstance(Vec3 var1, double var2, double var4, double var6, int var8, int var9, double var10, double var12, int var14, boolean var15) {
      double var16 = this.width.getValue();

      for (int var18 = 0; var18 < 2; var18++) {
         int var19 = var18 == 0 ? var8 : var9;
         double var20 = fract(var6 * 0.09 * (var18 == 0 ? 1 : -1) + var18 * 0.5);

         for (int var22 = 0; var22 < 3; var22++) {
            Vec3 var23 = hexagonPoint(var1, var2, var4, var18 + var22 * 2);
            Vec3 var24 = hexagonPoint(var1, var2, var4, var18 + var22 * 2 + 2);

            for (int var25 = 0; var25 <= 16; var25++) {
               double var26 = var25 / 16.0;
               double var28 = (var22 + var26) / 3.0 - var20;
               double var30 = Math.exp(-updateEffects(var28 - Math.rint(var28)) / 0.006);
               this.ribbons[var14 + var18 * 3 + var22]
                  .addPoint(var23.lerp(var24, var26), var16 * (2.6 + 1.6 * var30) * var12, ColorUtil.multiplyAlpha(var19, (0.72 + 0.28 * var30) * var10));
            }
         }

         if (var15) {
            int var33 = (int)(var20 * 3.0);
            this.addSprite(
               hexagonPoint(var1, var2, var4, var18 + var33 * 2).lerp(hexagonPoint(var1, var2, var4, var18 + var33 * 2 + 2), var20 * 3.0 - var33),
               var16 * 2.3,
               var19
            );
         }
      }

      if (var15) {
         for (int var32 = 0; var32 < 6; var32++) {
            this.addSprite(hexagonPoint(var1, var2, var4, var32), var16 * 1.5, var32 % 2 == 0 ? var8 : var9);
         }
      }
   }

   public static Vec3 hexagonPoint(Vec3 var0, double var1, double var3, int var5) {
      double var6 = var3 + Math.floorMod(var5, 6) * Math.PI / 3.0;
      return var0.add(Math.cos(var6) * var1, 0.0, Math.sin(var6) * var1);
   }

   private static double fract(double var0) {
      return var0 - Math.floor(var0);
   }

   private static double updateEffects(double var0) {
      return var0 * var0;
   }

   private static double isBloomActive(double var0) {
      return (0.12 + 0.88 * Math.pow(var0, 0.8)) * Easing.smoothstep(var0 / 0.08);
   }

   private static Vec3 energyPoint(Vec3 var0, double var1, double var3, double var5, double var7, double var9, double var11) {
      double var13 = var7 - (1.0 - var11) * 2.4;
      double var15 = Math.sin(var13 * 2.1 + var9 * 0.7);
      double var17 = var1 * (1.0 + var15 * 0.045);
      return var0.add(Math.cos(var13) * var17, var3 + var5 * 0.045 * Math.sin(var13 * 1.6), Math.sin(var13) * var17);
   }

   public void render(EffectsRenderer var1, float var2, boolean var3) {
      if (this.isEnabled()) {
         if (this.hasTarget && (!var3 || this.bloomActive)) {
            long var4 = PerfProfiler.startTimer();
            var1.blend(HitEffectConfig.BlendMode.ADDITIVE);

            for (CatmullRomSpline.Ribbon var9 : this.ribbons) {
               var1.drawTrail(var9, true);
            }

            for (TargetAuraModule.DiscMark var12 : this.discMarks) {
               var1.drawDisc(var12.center(), var12.radius(), var12.width(), var12.color());
            }

            for (TargetAuraModule.SpriteMark var13 : this.spriteMarks) {
               Vec3 var14 = var13.center();
               var1.drawSprite(var14.x, var14.y, var14.z, var13.size() * (var3 ? 1.8 : 2.6), 0.0, ColorUtil.multiplyAlpha(var13.color(), var3 ? 0.35 : 0.18));
               if (!var3) {
                  var1.drawSprite(var14.x, var14.y, var14.z, var13.size(), 0.0, var13.color());
               }
            }

            var1.blend(HitEffectConfig.BlendMode.NORMAL);
            PerfProfiler.endTimer("TargetAura", var4);
         }
      }
   }

   private void addSprite(CatmullRomSpline.Ribbon var1, Vec3 var2, double var3, double var5, double var7, double var9, int var11, int var12) {
      for (int var13 = 0; var13 <= 32; var13++) {
         double var14 = var13 / 32.0;
         Vec3 var16 = energyPoint(var2, var3, var5, var7, var9, TickTimer.getTime(), var14);
         double var17 = this.width.getValue() * isBloomActive(var14) * 3.4;
         int var19 = ColorUtil.multiplyAlpha(ColorUtil.lerpColor(var12, var11, var14), Math.pow(var14, 0.65));
         var1.addPoint(var16, var17, var19);
      }
   }

   @Override
   public void onDisable() {
      this.particleAccumulator = 0.0;
      this.particleCounter = 0;
      this.hasTarget = (boolean)0;
      this.starCenterY = Double.NaN;
      this.lastTargetPos = null;
      this.lastLevel = null;
      this.lastTargetId = -1;
      this.discMarks.clear();
      this.spriteMarks.clear();

      for (CatmullRomSpline.Ribbon var4 : this.ribbons) {
         var4.clear();
      }

      this.phase.reset();
      VisualsRenderer.particles.removeByEmitter(2);
   }

   private record DiscMark(Vec3 center, double radius, double width, int color) {
   }

   private record SpriteMark(Vec3 center, double size, int color) {
   }
}
