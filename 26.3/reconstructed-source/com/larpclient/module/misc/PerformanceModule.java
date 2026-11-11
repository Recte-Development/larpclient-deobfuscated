package com.larpclient.module.misc;

import com.larpclient.LarpClient;
import com.larpclient.config.ConfigManager;
import com.larpclient.render.overlay.PostProcessing;
import com.larpclient.render.particle.SoftParticleRenderer;
import com.larpclient.util.misc.LarpLog;
import com.larpclient.util.misc.SafetyLevel;
import java.util.List;
import net.minecraft.client.CloudStatus;
import net.minecraft.client.PrioritizeChunkUpdates;
import net.minecraft.server.level.ParticleStatus;

public final class PerformanceModule extends Module {
   private static PerformanceModule instance;
   public final ModeSetting chunkUpdates = this.addSetting(
      new ModeSetting(
         "Chunk Updates",
         "Balanced threads terrain updates but keeps player edits immediate; Threaded may delay visible block updates",
         "Balanced",
         "Balanced",
         "Threaded",
         "Unchanged"
      )
   );
   public final BooleanSetting streamingFpsLimit = this.addSetting(
      new BooleanSetting("Streaming FPS Limit", "Cap gameplay FPS to leave GPU headroom for recording; never raises Minecraft's existing limit", false)
   );
   public final NumberSetting streamingFps = this.addSetting(
      new NumberSetting("Streaming FPS", "Gameplay frame limit while recording or streaming", 120.0, 30.0, 240.0, 1.0)
   );
   public final BooleanSetting aggressiveMode = this.addSetting(
      new BooleanSetting(
         "Aggressive Mode", "Applies the reductions below together while Performance is on. Your video, effect and module settings stay as saved", false
      )
   );
   public final BooleanSetting singleColorBlocks = this.addSetting(
      new BooleanSetting(
         "Single Color Blocks",
         "Draws solid and cutout terrain in one color with its shapes, face shading and light. Water and glass keep their textures",
         false
      )
   );
   public final ColorSetting blockColor = this.addSetting(new ColorSetting("Block Color", "Terrain color for Single Color Blocks", -4932926));
   public final ModeSetting particles = this.addSetting(
      new ModeSetting("Particles", "Caps Minecraft's particle setting; Unchanged keeps your video setting", "Decreased", "Unchanged", "Decreased", "Minimal")
   );
   public final ModeSetting clientEffects = this.addSetting(
      new ModeSetting("Client Effects", "Caps Larp effect detail and glow; Unchanged keeps your Render quality choices", "Low", "Unchanged", "Low", "Off")
   );
   public final BooleanSetting hideClouds = this.addSetting(new BooleanSetting("Hide Clouds", "Skips cloud rendering", true));
   public final NumberSetting entityDistance = this.addSetting(
      new NumberSetting("Entity Distance", "Share of your entity render distance; 100 keeps it", 100.0, 25.0, 100.0, 5.0)
   );

   public PerformanceModule() {
      super(
         "Performance",
         "Render quality for client effects always applies. Turn the module on for smoother chunk updates and an optional recording FPS cap",
         Category.PLAYER,
         -1
      );
      instance = this;
      this.streamingFps.visibleWhen(this.streamingFpsLimit::getValue);
      this.chunkUpdates.onChange(var1 -> this.onSettingChanged());
      this.streamingFpsLimit.onChange(var1 -> this.onSettingChanged());
      this.streamingFps.onChange(var1 -> this.onSettingChanged());

      for (Setting var2 : this.getAggressiveSettings()) {
         if (var2 != this.aggressiveMode) {
            var2.visibleWhen(this.aggressiveMode::getValue);
         }

         var2.onChange(var1 -> this.onSettingChanged());
      }

      this.blockColor.visibleWhen(() -> this.aggressiveMode.getValue() && this.singleColorBlocks.getValue());
      PostProcessing.qualityOverrideSupplier = PerformanceModule::getEffectsQualityOverride;
   }

   public List<Setting<?>> getStreamingSettings() {
      return List.of(this.chunkUpdates, this.streamingFpsLimit, this.streamingFps);
   }

   public List<Setting<?>> getAggressiveSettings() {
      return List.of(this.aggressiveMode, this.singleColorBlocks, this.blockColor, this.particles, this.clientEffects, this.hideClouds, this.entityDistance);
   }

   public List<Setting<?>> createQualitySettings() {
      ModeSetting var1 = new ModeSetting(
         "Bloom", "Glow quality for effects that request bloom", PostProcessing.bloomQuality.name(), "Off", "Low", "Medium", "High", "Ultra"
      );
      var1.onChange(var0 -> PostProcessing.bloomQuality = PostProcessing.Quality.valueOf(var0));
      ModeSetting var2 = new ModeSetting(
         "Blur", "Blur quality behind the ClickGUI window and HUD panels", PostProcessing.blurQuality.name(), "Off", "Low", "Medium", "High", "Ultra"
      );
      var2.onChange(var0 -> PostProcessing.blurQuality = PostProcessing.Quality.valueOf(var0));
      PostProcessing.Quality var3 = SafetyLevel.getBaseLevel();
      ModeSetting var4 = new ModeSetting(
         "Effects",
         "Particle detail, glow and refraction for trails and weather",
         var3 == PostProcessing.Quality.BALANCED ? "Balanced" : var3.name(),
         "Off",
         "Low",
         "Balanced",
         "High",
         "Ultra"
      );
      var4.onChange(var1x -> {
         ConfigManager var2x = LarpClient.getInstance().getConfigManager();
         PostProcessing.Quality var3x = var1x.equals("Balanced") ? PostProcessing.Quality.BALANCED : PostProcessing.Quality.valueOf(var1x);
         if (!var2x.setTheme(var3x)) {
            PostProcessing.Quality var4x = var2x.getPreferences().effectsQuality;
            var4.setValue(var4x == PostProcessing.Quality.BALANCED ? "Balanced" : var4x.name());
         }
      });
      BooleanSetting var5 = new BooleanSetting("Soft particles", "Fade glow and smoke where they meet solid surfaces", SoftParticleRenderer.enabled);
      var5.onChange(var1x -> {
         ConfigManager var2x = LarpClient.getInstance().getConfigManager();
         if (!var2x.saveRecoveryProfile(var1x)) {
            var5.setValue(var2x.getPreferences().softParticles);
         }
      });
      return List.of(var1, var2, var4, var5);
   }

   public static PrioritizeChunkUpdates adjustChunkUpdates(PrioritizeChunkUpdates var0) {
      if (instance != null && instance.isEnabled()) {
         PerformanceModule var1 = instance;
         if (var1 == null || !var1.isRuntimeActive()) {
            return var0;
         } else if (var1.chunkUpdates.isMode("Threaded")) {
            return PrioritizeChunkUpdates.NONE;
         } else {
            return var1.chunkUpdates.isMode("Balanced") && var0 == PrioritizeChunkUpdates.NEARBY ? PrioritizeChunkUpdates.PLAYER_AFFECTED : var0;
         }
      } else {
         return var0;
      }
   }

   private static PerformanceModule getActiveAggressive() {
      PerformanceModule var0 = instance;
      return var0 != null && var0.isRuntimeActive() && var0.aggressiveMode.getValue() ? var0 : null;
   }

   public static boolean isSingleColorBlocks() {
      if (instance != null && instance.isEnabled()) {
         PerformanceModule var0 = getActiveAggressive();
         return var0 != null && var0.singleColorBlocks.getValue();
      } else {
         return false;
      }
   }

   public static int getBlockColor() {
      PerformanceModule var0 = instance;
      return var0 == null ? -4932926 : var0.blockColor.getValue();
   }

   public static ParticleStatus limitParticles(ParticleStatus var0) {
      if (instance != null && instance.isEnabled()) {
         PerformanceModule var1 = getActiveAggressive();
         if (var1 != null && !var1.particles.isMode("Unchanged")) {
            ParticleStatus var2 = var1.particles.isMode("Minimal") ? ParticleStatus.MINIMAL : ParticleStatus.DECREASED;
            return var0.ordinal() >= var2.ordinal() ? var0 : var2;
         } else {
            return var0;
         }
      } else {
         return var0;
      }
   }

   public static CloudStatus adjustCloudStatus(CloudStatus var0) {
      if (instance != null && instance.isEnabled()) {
         PerformanceModule var1 = getActiveAggressive();
         return var1 != null && var1.hideClouds.getValue() ? CloudStatus.OFF : var0;
      } else {
         return var0;
      }
   }

   public static double adjustCloudStatus(double var0) {
      if (instance != null && instance.isEnabled()) {
         PerformanceModule var2 = getActiveAggressive();
         return var2 == null ? var0 : var0 * var2.entityDistance.getValue() / 100.0;
      } else {
         return var0;
      }
   }

   public static PostProcessing.Quality getEffectsQualityOverride() {
      if (instance != null && instance.isEnabled()) {
         PerformanceModule var0 = getActiveAggressive();
         if (var0 != null && !var0.clientEffects.isMode("Unchanged")) {
            return var0.clientEffects.isMode("Off") ? PostProcessing.Quality.OFF : PostProcessing.Quality.LOW;
         } else {
            return null;
         }
      } else {
         return null;
      }
   }

   public static int limitFps(int var0) {
      if (instance != null && instance.isEnabled()) {
         PerformanceModule var1 = instance;
         return var1 != null && var1.isRuntimeActive() && var1.streamingFpsLimit.getValue() ? Math.min(var0, var1.streamingFps.getValue().intValue()) : var0;
      } else {
         return var0;
      }
   }

   private void onSettingChanged() {
      if (this.isRuntimeActive()) {
         this.logSettings();
      }
   }

   private void logSettings() {
      LarpLog.info(
         "Performance",
         "Chunk updates={}; recording FPS limit={}",
         this.chunkUpdates.getValue(),
         this.streamingFpsLimit.getValue() ? this.streamingFps.getValue().intValue() : "off"
      );
      if (this.aggressiveMode.getValue()) {
         LarpLog.info(
            "Performance",
            "Aggressive: single color={} ({}), particles={}, client effects={}, clouds={}, entity distance={}%",
            this.singleColorBlocks.getValue(),
            this.blockColor.getValueAsString(),
            this.particles.getValue(),
            this.clientEffects.getValue(),
            this.hideClouds.getValue() ? "hidden" : "unchanged",
            this.entityDistance.getValue().intValue()
         );
      }
   }

   @Override
   public void onEnable() {
      this.logSettings();
   }

   @Override
   public void onDisable() {
      LarpLog.info("Performance", "Native render policy released; video preferences retained");
   }
}
