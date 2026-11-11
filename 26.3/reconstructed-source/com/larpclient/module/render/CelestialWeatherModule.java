package com.larpclient.module.render;

import com.larpclient.gui.theme.Theme;
import com.larpclient.module.misc.BooleanSetting;
import com.larpclient.module.misc.Category;
import com.larpclient.module.misc.ColorSetting;
import com.larpclient.module.misc.ModeSetting;
import com.larpclient.module.misc.Module;
import com.larpclient.module.misc.NumberSetting;
import com.larpclient.module.misc.Setting;
import com.larpclient.render.overlay.HighlightEffect;
import com.larpclient.render.sky.CelestialWeatherSystem;
import com.larpclient.render.world.EffectsRenderer;
import com.larpclient.util.misc.LarpLog;
import com.larpclient.util.misc.SafetyLevel;
import com.larpclient.util.world.BlockRaycast;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.dimension.DimensionType.Skybox;
import net.minecraft.world.level.levelgen.Heightmap.Types;
import net.minecraft.world.phys.Vec3;

public final class CelestialWeatherModule extends Module implements HighlightEffect {
   private static CelestialWeatherModule instance;
   public final ModeSetting style = this.addSetting(
      new ModeSetting("Style", "Local celestial atmosphere", "Meteor Shower", "Meteor Shower", "Stardust", "Astral Rain", "Glowing Snow")
   );
   public final NumberSetting density = this.addNumberSetting("Density", "Emission strength within the weather budget", 1.0, 0.1, 4.0, 0.1);
   public final NumberSetting speed = this.addNumberSetting("Speed", "Flight and floating motion", 1.0, 0.0, 3.0, 0.1);
   public final NumberSetting size = this.addNumberSetting("Size", "Meteor cores, dust, droplets and snowflakes", 1.0, 0.4, 2.5, 0.1);
   public final BooleanSetting themeColors = this.addSetting(new BooleanSetting("Theme Colors", "Use the current theme palette", true));
   public final ColorSetting color = this.addSetting(new ColorSetting("Color", "Celestial cores and dust", -2561281));
   public final ColorSetting secondaryColor = this.addSetting(new ColorSetting("Secondary Color", "Celestial tails and contact glow", -6059009));
   public final BooleanSetting bloom = this.addSetting(new BooleanSetting("Bloom", "Use shared world glow", true));
   public final NumberSetting radius = this.addNumberSetting("Radius", "World-space emission region in blocks", 28.0, 8.0, 48.0, 1.0);
   public final NumberSetting windStrength = this.addNumberSetting("Wind Strength", "Coherent horizontal drift", 0.8, 0.0, 6.0, 0.1);
   public final BooleanSetting surfaceImpacts = this.addSetting(
      new BooleanSetting("Surface Impacts", "Selected nearby flights contact actual block surfaces", true)
   );
   public final NumberSetting renderDistance = this.addNumberSetting("Render Distance", "Fade celestial weather beyond this distance", 80.0, 24.0, 128.0, 4.0);
   public final ModeSetting dimensionFilter = this.addSetting(
      new ModeSetting("Dimension Filter", "Choose where local weather appears", "Overworld", "Overworld", "All")
   );
   public final ModeSetting indoors = this.addSetting(new ModeSetting("Indoors", "Fade weather when the camera is sheltered", "Fade", "Fade", "Allow"));
   private final CelestialWeatherSystem.TerrainHeights terrainHeights = this::getTerrainHeight;
   private final CelestialWeatherSystem weatherSystem = new CelestialWeatherSystem();
   private Object lastLevel;
   private Object lastPlayer;
   private Vec3 lastCameraPos;
   private int tickCounter;
   private boolean outdoors;
   private boolean paused;

   public CelestialWeatherModule() {
      super("Celestial Weather", "Meteor showers, stardust, luminous rain and glowing snow", Category.VISUALS, -1);
      instance = this;
      this.color.visibleWhen(() -> !this.themeColors.getValue());
      this.secondaryColor.visibleWhen(() -> !this.themeColors.getValue());
      this.surfaceImpacts.visibleWhen(() -> !this.style.isMode("Stardust") && !this.style.isMode("Glowing Snow"));
      this.indoors.visibleWhen(() -> !this.style.isMode("Glowing Snow"));
      this.style.onChange(var1 -> this.clear());
      this.dimensionFilter.onChange(var1 -> this.clear());
   }

   private NumberSetting addNumberSetting(String var1, String var2, double var3, double var5, double var7, double var9) {
      return this.addSetting(new NumberSetting(var1, var2, var3, var5, var7, var9));
   }

   public List<Setting<?>> getMainSettings() {
      return List.of(this.style, this.density, this.speed, this.size);
   }

   public List<Setting<?>> getColorSettings() {
      return List.of(this.themeColors, this.color, this.secondaryColor, this.bloom);
   }

   public List<Setting<?>> getAreaSettings() {
      return List.of(this.radius, this.windStrength, this.surfaceImpacts, this.renderDistance, this.dimensionFilter, this.indoors);
   }

   public static CelestialWeatherModule getInstance() {
      return instance;
   }

   public CelestialWeatherSystem getSystem() {
      return this.weatherSystem;
   }

   public CelestialWeatherSystem.Settings buildSettings() {
      CelestialWeatherSystem.Style var1 = this.style.isMode("Stardust")
         ? CelestialWeatherSystem.Style.MOTES
         : (
            this.style.isMode("Astral Rain")
               ? CelestialWeatherSystem.Style.RAIN
               : (this.style.isMode("Glowing Snow") ? CelestialWeatherSystem.Style.SNOW : CelestialWeatherSystem.Style.METEOR)
         );
      return new CelestialWeatherSystem.Settings(
         var1,
         this.density.getValue(),
         this.radius.getValue(),
         this.speed.getValue(),
         this.size.getValue(),
         this.windStrength.getValue(),
         this.renderDistance.getValue(),
         this.themeColors.getValue() ? Theme.accent : this.color.getValue(),
         this.themeColors.getValue() ? Theme.accentEnd : this.secondaryColor.getValue(),
         this.bloom.getValue(),
         this.surfaceImpacts.getValue(),
         SafetyLevel.scaleDelay(256)
      );
   }

   @Override
   public String effectName() {
      return "Celestial Weather";
   }

   @Override
   public void tick(Minecraft var1) {
      if (this.isEnabled()) {
         if (this.isRuntimeActive() && var1.level != null && var1.player != null && var1.player.isAlive()) {
            this.paused = var1.isPaused();
            if (!this.paused) {
               Vec3 var2 = var1.gameRenderer.mainCamera().position();
               if (this.lastLevel != var1.level
                  || this.lastPlayer != var1.player
                  || this.lastCameraPos != null && this.lastCameraPos.distanceToSqr(var2) > 4096.0) {
                  this.clear();
                  this.lastLevel = var1.level;
                  this.lastPlayer = var1.player;
                  LarpLog.debug(
                     this.effectName(),
                     "World field initialized; style={}; max flights={}; max sprites={}; max terrain queries/tick={}",
                     this.style.getValue(),
                     256,
                     1024,
                     4
                  );
               }

               this.lastCameraPos = var2;
               if (this.dimensionFilter.isMode("Overworld") && var1.level.dimensionType().skybox() != Skybox.OVERWORLD) {
                  this.weatherSystem.reset();
               } else {
                  if (this.tickCounter++ % 5 == 0) {
                     BlockPos var3 = BlockPos.containing(var2);
                     int var4 = this.indoors.isMode("Allow") || this.style.isMode("Glowing Snow") || var1.level.hasChunkAt(var3) && var1.level.canSeeSky(var3)
                        ? 1
                        : 0;
                     if (var4 != this.outdoors) {
                        LarpLog.debug(this.effectName(), "Exposure changed: {}; doorway fade active", var4 != 0 ? "outdoors" : "sheltered");
                     }

                     this.outdoors = (boolean)var4;
                  }

                  this.weatherSystem
                     .tickWithTerrain(
                        var2,
                        this.buildSettings(),
                        this.outdoors,
                        (var1x, var2x) -> BlockRaycast.raycastBlock(var1.level, var1x, var2x),
                        this.style.isMode("Glowing Snow") ? this.terrainHeights : null
                     );
               }
            }
         } else {
            this.clear();
         }
      }
   }

   private double getTerrainHeight(int var1, int var2) {
      ClientLevel var3 = Minecraft.getInstance().level;
      return var3 != null && var3.hasChunk(var1 >> 4, var2 >> 4) ? var3.getHeight(Types.MOTION_BLOCKING, var1, var2) : Double.NaN;
   }

   @Override
   public void prepare(float var1, CameraRenderState var2) {
      if (this.isEnabled()) {
         if (!this.isRuntimeActive()) {
            this.weatherSystem.reset();
         } else {
            this.weatherSystem.prepareRender(var2.pos, this.paused ? 1.0F : var1, var2.cullFrustum);
         }
      }
   }

   @Override
   public void render(EffectsRenderer var1, boolean var2) {
      if (this.isEnabled()) {
         this.weatherSystem.render(var1, var2);
      }
   }

   @Override
   public boolean glowing() {
      return this.isEnabled() ? this.weatherSystem.spawnMote() : false;
   }

   @Override
   public boolean soft() {
      return this.weatherSystem.hasSprites();
   }

   @Override
   public void clear() {
      this.weatherSystem.reset();
      this.lastLevel = this.lastPlayer = null;
      this.lastCameraPos = null;
      this.tickCounter = 0;
      this.outdoors = this.paused = (boolean)0;
   }

   @Override
   public void onEnable() {
      this.clear();
      LarpLog.info(this.effectName(), "Enabled {}; isolated weather pool; terrain queries capped at {}/tick", this.style.getValue(), 4);
   }

   @Override
   public void onDisable() {
      this.clear();
   }
}
