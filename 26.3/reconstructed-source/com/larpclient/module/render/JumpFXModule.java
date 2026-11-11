package com.larpclient.module.render;

import com.larpclient.LarpClient;
import com.larpclient.config.HitEffectConfig;
import com.larpclient.gui.theme.Theme;
import com.larpclient.module.misc.BooleanSetting;
import com.larpclient.module.misc.Category;
import com.larpclient.module.misc.ColorSetting;
import com.larpclient.module.misc.ModeSetting;
import com.larpclient.module.misc.Module;
import com.larpclient.module.misc.NumberSetting;
import com.larpclient.module.misc.Setting;
import com.larpclient.render.particle.ParticleStyles;
import com.larpclient.render.world.EffectsRenderer;
import com.larpclient.render.world.JumpRingRenderer;
import com.larpclient.util.data.PulseWaveTracker;
import com.larpclient.util.data.TickTimer;
import com.larpclient.util.entity.JumpLandTracker;
import com.larpclient.util.math.HexagonGeometry;
import com.larpclient.util.misc.LarpLog;
import com.larpclient.util.render.ColorUtil;
import com.larpclient.util.world.SurfaceEdgeFinder;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ThreadLocalRandom;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;

public final class JumpFXModule extends Module {
   private static JumpFXModule instance;
   private final ModeSetting mode = this.addSetting(
      new ModeSetting(
         "Mode",
         "Terrain outlines, stars and Clarity jump effects",
         "Star of David",
         "Terrain",
         "Star of David",
         "Ripple",
         "Shockwave",
         "Rune",
         "Bloom",
         "Corona",
         "Vortex",
         "Spectrum",
         "Cloud",
         "End Rod",
         "Crit",
         "Flame",
         "Soul Flame",
         "Happy Villager"
      )
   );
   private final ModeSetting trigger = this.addSetting(new ModeSetting("Trigger", "Movement transition", "Land", "Jump", "Land", "Both"));
   private final NumberSetting maxRadius = this.addSetting(new NumberSetting("Maximum Radius", "Blocks", 1.0, 1.0, 8.0, 0.25));
   private final NumberSetting pulseDuration = this.addSetting(new NumberSetting("Pulse Duration", "Seconds", 1.0, 0.3, 3.0, 0.05));
   private final NumberSetting waveWidth = this.addSetting(new NumberSetting("Wave Width", "Soft wave band in blocks", 0.9, 0.5, 3.0, 0.05));
   private final NumberSetting outlineThickness = this.addSetting(
      new NumberSetting("Outline Thickness", "World-space stroke width", 0.04, 0.005, 0.08, 0.005) {
         @Override
         public String getValueAsString() {
            return String.format(Locale.ROOT, "%.3f", this.getValue());
         }
      }
   );
   private final ModeSetting colorTheme = this.addSetting(
      new ModeSetting("Color Theme", "Theme gradient, white or custom color", "Custom", "Theme", "White", "Custom")
   );
   private final ColorSetting color = this.addSetting(new ColorSetting("Color", "Custom outline color", -15204097));
   private final NumberSetting opacity = this.addSetting(new NumberSetting("Opacity", "Outline opacity", 0.35, 0.05, 1.0, 0.05));
   private final NumberSetting glowIntensity = this.addSetting(
      new NumberSetting("Glow Intensity", "Soft strokes and quality-controlled bloom", 2.0, 0.0, 2.0, 0.05)
   );
   private final ColorSetting circleColor = this.addSetting(new ColorSetting("Circle Colour", "Primary circle color", -9452545));
   private final ColorSetting circleColor2 = this.addSetting(new ColorSetting("Circle Colour 2", "Secondary circle color", -3703297));
   private final NumberSetting circleSize = this.addSetting(new NumberSetting("Circle Size", "Circle scale in blocks", 1.6, 0.5, 6.0, 0.1));
   private final NumberSetting circleDuration = this.addSetting(new NumberSetting("Circle Duration", "Seconds", 0.9, 0.2, 3.0, 0.1));
   private final BooleanSetting circleThroughWalls = this.addSetting(new BooleanSetting("Circle Through Walls", "Show circles through terrain", false));
   private final NumberSetting particleRadius = this.addSetting(new NumberSetting("Radius", "Particle ring radius in blocks", 0.6, 0.1, 3.0, 0.1));
   private final NumberSetting points = this.addSetting(new NumberSetting("Points", "Particles per jump or landing", 16.0, 4.0, 64.0, 1.0));
   private final BooleanSetting onlySprinting = this.addSetting(new BooleanSetting("Only Sprinting", "Emit Clarity effects only while sprinting", false));
   private final ParticleStyles.StyleState styleState = new ParticleStyles.StyleState();
   private final ArrayList<Particle> particles = new ArrayList<>();
   private JumpFXModule.WaveParams waveParams;
   private final JumpLandTracker jumpLandTracker = new JumpLandTracker();
   private final PulseWaveTracker waveTracker = new PulseWaveTracker();
   private Object trackedLevel;
   private Object trackedPlayer;

   public JumpFXModule() {
      super("JumpFX", "Show glowing effects on jumps and landings", Category.VISUALS, -1);
      instance = this;
      this.color.visibleWhen(() -> this.isOutlineMode() && this.colorTheme.isMode("Custom"));

      for (Setting var2 : List.of(this.maxRadius, this.pulseDuration, this.outlineThickness, this.colorTheme, this.opacity)) {
         var2.visibleWhen(this::isOutlineMode);
      }

      for (Setting var5 : List.of(this.circleColor, this.circleColor2, this.circleSize, this.circleDuration, this.circleThroughWalls)) {
         var5.visibleWhen(() -> ParticleStyles.customStyleIndex(this.mode.getValue()) >= 0);
      }

      for (NumberSetting var6 : List.of(this.particleRadius, this.points)) {
         var6.visibleWhen(() -> ParticleStyles.isVanillaStyle(this.mode.getValue()));
      }

      this.onlySprinting.visibleWhen(() -> ParticleStyles.isKnownStyle(this.mode.getValue()));
      this.glowIntensity.visibleWhen(() -> this.isOutlineMode() || ParticleStyles.customStyleIndex(this.mode.getValue()) >= 0);
      this.waveWidth.visibleWhen(() -> this.mode.isMode("Terrain"));
      this.mode.onChange(var1 -> {
         this.createColumnBoxProvider();
         LarpLog.info("JumpFX", "Mode changed: {}; active pulses cleared", var1);
      });
   }

   public Setting<?> getModeSetting() {
      return this.mode;
   }

   public List<Setting<?>> getPrimarySettings() {
      return List.of(this.mode, this.trigger, this.onlySprinting);
   }

   public List<Setting<?>> isOutlineMode() {
      return List.of(
         this.maxRadius,
         this.pulseDuration,
         this.waveWidth,
         this.outlineThickness,
         this.circleSize,
         this.circleDuration,
         this.circleThroughWalls,
         this.particleRadius,
         this.points
      );
   }

   public List<Setting<?>> getColorSettings() {
      return List.of(this.colorTheme, this.color, this.opacity, this.glowIntensity, this.circleColor, this.circleColor2);
   }

   public static JumpFXModule getInstance() {
      return instance;
   }

   @Override
   public void onEnable() {
      this.reset();
      LarpLog.info("JumpFX", "Enabled; mode={}, trigger={}, bounded world effects and native particles", this.mode.getValue(), this.trigger.getValue());
   }

   @Override
   public void onDisable() {
      this.reset();
   }

   private boolean isOutlineMode() {
      return this.mode.isMode("Terrain") || this.mode.isMode("Star of David");
   }

   private void createColumnBoxProvider() {
      this.waveTracker.clear();
      this.styleState.tick();
      this.waveParams = null;

      for (Particle var2 : this.particles) {
         var2.remove();
      }

      this.particles.clear();
      JumpRingRenderer.closeBuffer();
   }

   public void reset() {
      this.jumpLandTracker.reset();
      this.createColumnBoxProvider();
      this.trackedLevel = this.trackedPlayer = null;
   }

   public void updateWaveParams() {
      if (this.isEnabled()) {
         this.waveParams = null;
         int var1 = ParticleStyles.customStyleIndex(this.mode.getValue());
         if (this.isRuntimeActive() && var1 >= 0 && !this.styleState.spawn().isEmpty()) {
            this.waveParams = new JumpFXModule.WaveParams(
               var1,
               this.circleColor.getValue(),
               this.circleSize.getValue(),
               this.circleDuration.getValue(),
               this.circleThroughWalls.getValue(),
               this.glowIntensity.getValue().floatValue(),
               TickTimer.getTime()
            );
            JumpRingRenderer.setParameters(this.circleColor2.getValue());
         }
      }
   }

   private boolean canTrackMovement() {
      LocalPlayer var1 = this.getPlayer();
      Module var2 = LarpClient.getInstance().getModuleManager().getByName("Fly");
      Module var3 = LarpClient.getInstance().getModuleManager().getByName("Freecam");
      return var1 != null
         && var1.isAlive()
         && !var1.isSpectator()
         && !var1.isInWater()
         && !var1.isInLava()
         && !var1.onClimbable()
         && !var1.isPassenger()
         && !var1.isFallFlying()
         && !var1.getAbilities().flying
         && (var2 == null || !var2.isOperational())
         && (var3 == null || !var3.isOperational());
   }

   private JumpLandTracker.Sample buildSample() {
      LocalPlayer var1 = this.getPlayer();
      if (this.trackedLevel != getMc().level || this.trackedPlayer != var1) {
         this.reset();
         this.trackedLevel = getMc().level;
         this.trackedPlayer = var1;
      }

      return new JumpLandTracker.Sample(this.trackedLevel, var1, var1.position(), var1.onGround(), this.canTrackMovement());
   }

   public void onPreTick() {
      if (this.isEnabled()) {
         if (this.isRuntimeActive() && this.isInGame()) {
            this.handleTransition(this.jumpLandTracker.updateAndRecordGround(this.buildSample()));
         }
      }
   }

   public void onTickUpdate() {
      if (this.isEnabled()) {
         if (this.isRuntimeActive() && this.isInGame()) {
            this.waveTracker.removeExpired(TickTimer.getTickTime());
            this.styleState.isExpired(TickTimer.getTickTime(), this.circleDuration.getValue());
            this.particles.removeIf(var0 -> !var0.isAlive());
            this.handleTransition(this.jumpLandTracker.update(this.buildSample()));
         } else {
            this.reset();
         }
      }
   }

   private void handleTransition(JumpLandTracker.Result var1) {
      if (JumpLandTracker.matchesMode(this.trigger.getValue(), var1.event())) {
         if (!this.isOutlineMode()) {
            if (!this.onlySprinting.getValue() || this.getPlayer().isSprinting()) {
               int var7 = ParticleStyles.customStyleIndex(this.mode.getValue());
               if (var7 >= 0) {
                  this.styleState.tick(new ParticleStyles.StyleSpec(var1.source(), TickTimer.getTickTime(), ThreadLocalRandom.current().nextFloat()));
               } else {
                  ParticleOptions var3 = ParticleStyles.vanillaParticle(this.mode.getValue());
                  if (var3 == null) {
                     return;
                  }

                  for (Vec3 var5 : ParticleStyles.ringPoints(var1.source(), this.particleRadius.getValue(), this.points.getValue().intValue())) {
                     if (this.particles.size() == 384) {
                        this.particles.removeFirst().remove();
                     }

                     Particle var6 = getMc().particleEngine.createParticle(var3, var5.x, var5.y, var5.z, 0.0, 0.02, 0.0);
                     if (var6 != null) {
                        this.particles.add(var6);
                     }
                  }
               }
            }
         } else {
            PulseWaveTracker.Wave var2 = this.createWave(
               var1.source(), TickTimer.getTickTime(), this.mode.isMode("Terrain") ? this.createColumnBoxProvider(var1.source().y) : null
            );
            if (var2 != null) {
               this.waveTracker.addWave(var2);
            }
         }
      }
   }

   public PulseWaveTracker.Wave createWave(Vec3 var1, double var2, SurfaceEdgeFinder.ColumnBoxProvider var4) {
      if (this.isEnabled()) {
         if (!this.isOutlineMode()) {
            return null;
         }

         boolean var5 = this.mode.isMode("Star of David");
         List var6 = var5 ? HexagonGeometry.hEX_FACES : SurfaceEdgeFinder.findEdges(var1, this.maxRadius.getValue(), var4);
         if (var6.isEmpty()) {
            return null;
         }

         int var7 = this.colorTheme.isMode("Theme") ? Theme.accent : (this.colorTheme.isMode("White") ? -1 : this.color.getValue());
         int var8 = this.colorTheme.isMode("Theme") ? Theme.accentEnd : var7;
         return new PulseWaveTracker.Wave(
            var1,
            var2,
            this.maxRadius.getValue(),
            this.pulseDuration.getValue(),
            this.waveWidth.getValue(),
            this.outlineThickness.getValue(),
            var7,
            var8,
            this.opacity.getValue(),
            this.glowIntensity.getValue(),
            var6,
            var5 ? PulseWaveTracker.Style.STAR : PulseWaveTracker.Style.RIPPLE
         );
      } else {
         return null;
      }
   }

   private SurfaceEdgeFinder.ColumnBoxProvider createColumnBoxProvider(double var1) {
      ClientLevel var3 = getMc().level;
      CollisionContext var4 = CollisionContext.of(this.getPlayer());
      return (var4x, var5) -> {
         ArrayList var6 = new ArrayList();
         int var7 = (int)Math.floor(var1);

         for (int var8 = var7 - 3; var8 <= var7 + 3; var8++) {
            BlockPos var9 = new BlockPos(var4x, var8, var5);
            if (var3.hasChunkAt(var9)) {
               BlockState var10 = var3.getBlockState(var9);
               if (var10.getFluidState().isEmpty()) {
                  for (AABB var12 : var10.getCollisionShape(var3, var9, var4).toAabbs()) {
                     var6.add(var12.move(var4x, var8, var5));
                     if (var6.size() > 32) {
                        return var6;
                     }
                  }
               }
            }
         }

         return var6;
      };
   }

   public boolean hasGlow() {
      return !this.isEnabled()
         ? false
         : this.isRuntimeActive()
            && (this.waveParams != null && this.waveParams.glow() > 0.0F || this.waveTracker.getWaves().stream().anyMatch(var0 -> var0.glow() > 0.0));
   }

   public void renderWorld(EffectsRenderer var1, boolean var2) {
      if (this.isEnabled()) {
         if (this.isRuntimeActive()) {
            if (this.waveParams != null) {
               JumpFXModule.WaveParams var3 = this.waveParams;
               float var4 = var2 ? Math.clamp(var3.glow() * 0.2F, 0.0F, 1.0F) : 1.0F;
               if (var4 > 0.0F) {
                  for (ParticleStyles.StyleSpec var6 : this.styleState.spawn()) {
                     float var7 = (float)((var3.now() - var6.born()) / var3.duration());
                     if (var7 >= 0.0F && var7 < 1.0F) {
                        var1.drawDisc(var6.source(), var3.size(), var7, var6.seed(), var3.primary(), var3.style(), var3.walls(), var4);
                     }
                  }
               }
            }

            var1.blend(HitEffectConfig.BlendMode.ADDITIVE);
            double var31 = TickTimer.getTime();

            for (PulseWaveTracker.Wave var33 : this.waveTracker.getWaves()) {
               if (!var2 || !(var33.glow() <= 0.0)) {
                  double var34 = Math.clamp((var31 - var33.born()) / var33.duration(), 0.0, 1.0);
                  int var9 = ColorUtil.lerpColor(var33.from(), var33.endColor(), var34);
                  int var10 = var33.style() == PulseWaveTracker.Style.STAR ? 1 : 0;
                  double var11 = var10 != 0 ? PulseWaveTracker.currentRadius(var33, var31) : 1.0;

                  for (SurfaceEdgeFinder.Edge var14 : var33.edges()) {
                     double var15 = (var10 != 0 ? PulseWaveTracker.fade(var33, var31) : PulseWaveTracker.intensityAt(var33, var14.distance(), var31))
                        * var33.opacity();
                     if (!(var15 < 0.003)) {
                        Vec3 var17 = var14.from();
                        Vec3 var18 = var14.end();
                        double var19 = var17.x;
                        double var21 = var17.y;
                        double var23 = var17.z;
                        double var25 = var18.x;
                        double var27 = var18.y;
                        double var29 = var18.z;
                        if (var10 != 0) {
                           var19 = HexagonGeometry.lerp(var33.source().x, var19, var11);
                           var23 = HexagonGeometry.lerp(var33.source().z, var23, var11);
                           var25 = HexagonGeometry.lerp(var33.source().x, var25, var11);
                           var29 = HexagonGeometry.lerp(var33.source().z, var29, var11);
                           var21 = var27 = var33.source().y + 0.012;
                        }

                        drawEdge(var1, var33, var2, var9, var15, var19, var21, var23, var25, var27, var29);
                     }
                  }
               }
            }

            var1.blend(HitEffectConfig.BlendMode.NORMAL);
         }
      }
   }

   private static void drawEdge(
      EffectsRenderer var0,
      PulseWaveTracker.Wave var1,
      boolean var2,
      int var3,
      double var4,
      double var6,
      double var8,
      double var10,
      double var12,
      double var14,
      double var16
   ) {
      if (var2) {
         int var18 = ColorUtil.multiplyAlpha(var3, var4 * var1.glow() * 0.2);
         var0.drawRibbon(var6, var8, var10, var12, var14, var16, var1.thickness() * 2.0, var1.thickness() * 2.0, var18, var18);
      } else {
         for (int var22 = 0; var22 < 3 && var1.glow() > 0.0; var22++) {
            double var19 = var1.thickness() * (2.0 + var22 * 1.5);
            int var21 = ColorUtil.multiplyAlpha(var3, var4 * var1.glow() * 0.065 / (var22 + 1));
            var0.drawRibbon(var6, var8, var10, var12, var14, var16, var19, var19, var21, var21);
         }

         int var23 = ColorUtil.multiplyAlpha(ColorUtil.lerpColor(var3, var3 & 0xFF000000 | 16777215, 0.4), var4);
         var0.drawRibbon(var6, var8, var10, var12, var14, var16, var1.thickness(), var1.thickness(), var23, var23);
      }
   }

   private record WaveParams(int style, int primary, double size, double duration, boolean walls, float glow, double now) {
   }
}
