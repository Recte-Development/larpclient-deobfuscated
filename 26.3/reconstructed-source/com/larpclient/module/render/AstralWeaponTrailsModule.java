package com.larpclient.module.render;

import com.larpclient.gui.theme.Theme;
import com.larpclient.module.misc.BooleanSetting;
import com.larpclient.module.misc.Category;
import com.larpclient.module.misc.ColorSetting;
import com.larpclient.module.misc.ModeSetting;
import com.larpclient.module.misc.Module;
import com.larpclient.module.misc.NumberSetting;
import com.larpclient.module.misc.Setting;
import com.larpclient.render.entity.WeaponTrailRenderer;
import com.larpclient.render.model.TrailMeshBuilder;
import com.larpclient.render.overlay.HighlightEffect;
import com.larpclient.render.overlay.PostProcessing;
import com.larpclient.render.world.EffectsRenderer;
import com.larpclient.util.data.TickTimer;
import com.larpclient.util.misc.LarpLog;
import com.larpclient.util.misc.SafetyLevel;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

public final class AstralWeaponTrailsModule extends Module implements HighlightEffect {
   private static AstralWeaponTrailsModule instance;
   private final ModeSetting style = this.addSetting(new ModeSetting("Style", "Energy ribbon appearance", "Astral", "Astral", "Prismatic", "Void"));
   private final NumberSetting trailDuration = this.addSetting(new NumberSetting("Trail Duration", "Seconds of recent blade motion", 0.28, 0.1, 0.7, 0.02));
   private final NumberSetting width = this.addSetting(new NumberSetting("Width", "Ribbon span along the actual blade", 0.65, 0.15, 1.0, 0.05));
   private final NumberSetting intensity = this.addSetting(new NumberSetting("Intensity", "Body and leading edge brightness", 1.0, 0.2, 2.0, 0.1));
   private final BooleanSetting themeColors = this.addSetting(new BooleanSetting("Theme Colors", "Use the current theme palette", true));
   private final ColorSetting primaryColor = this.addSetting(new ColorSetting("Primary Color", "Leading energy color", -8790017));
   private final ColorSetting secondaryColor = this.addSetting(new ColorSetting("Secondary Color", "Fading wake color", -9142785));
   private final BooleanSetting bloom = this.addSetting(new BooleanSetting("Bloom", "Soft emissive halo on the native hand ribbon", true));
   private final BooleanSetting sparks = this.addSetting(new BooleanSetting("Sparks", "Small streaks separating from the swing", true));
   private final BooleanSetting firstPerson = this.addSetting(new BooleanSetting("First Person", "Show trails on the local rendered weapon", true));
   private final BooleanSetting motionSmoothing = this.addSetting(new BooleanSetting("Motion Smoothing", "Curve between captured blade poses", true));
   private Object lastLevel;
   private Object lastPlayer;
   private Vec3 lastPosition;
   private int lastReloadCount = -1;
   private int lastSelectedSlot = -1;
   private boolean wasFirstPerson;

   public AstralWeaponTrailsModule() {
      super("Astral Weapon Trails", "Luminous ribbons following the rendered sword or knife", Category.VISUALS, -1);
      instance = this;
      this.primaryColor.visibleWhen(() -> !this.themeColors.getValue());
      this.secondaryColor.visibleWhen(() -> !this.themeColors.getValue());
      this.style.onChange(var0 -> {
         WeaponTrailRenderer.reset();
         LarpLog.debug("WeaponTrails", "Style changed to {}; history reset", var0);
      });
      this.firstPerson.onChange(var0 -> WeaponTrailRenderer.reset());
   }

   public static AstralWeaponTrailsModule getInstance() {
      return instance;
   }

   public List<Setting<?>> getStyleSettings() {
      return List.of(this.style, this.trailDuration, this.width, this.intensity);
   }

   public List<Setting<?>> getColorSettings() {
      return List.of(this.themeColors, this.primaryColor, this.secondaryColor, this.bloom);
   }

   public List<Setting<?>> getBehaviorSettings() {
      return List.of(this.sparks, this.firstPerson, this.motionSmoothing);
   }

   public int getUpdateInterval() {
      return Math.max(16, SafetyLevel.scaleDelay(64));
   }

   public TrailMeshBuilder.TrailStyle buildTrailStyle() {
      return new TrailMeshBuilder.TrailStyle(
         this.style.getValue(),
         this.trailDuration.getValue(),
         this.width.getValue(),
         this.intensity.getValue(),
         this.themeColors.getValue() ? Theme.accent : this.primaryColor.getValue(),
         this.themeColors.getValue() ? Theme.accentEnd : this.secondaryColor.getValue(),
         this.bloom.getValue() && SafetyLevel.current != PostProcessing.Quality.OFF,
         this.sparks.getValue(),
         this.motionSmoothing.getValue(),
         SafetyLevel.isAggressive() ? 3 : 2
      );
   }

   public boolean shouldRenderFirstPerson(ItemStack var1) {
      if (!this.isEnabled()) {
         return false;
      }

      Minecraft var2 = getMc();
      return this.isRuntimeActive()
         && this.firstPerson.getValue()
         && SwordModelsModule.isSword(var1)
         && var2.player != null
         && var2.level != null
         && var2.player.isAlive()
         && !var2.player.isScoping()
         && var2.options.getCameraType().isFirstPerson();
   }

   @Override
   public String effectName() {
      return "WeaponTrails";
   }

   @Override
   public void onEnable() {
      this.clear();
      LarpLog.info(
         "WeaponTrails", "Enabled {}; native sword layer and articulated knife blade capture, two hands, at most 128 retained samples", this.style.getValue()
      );
   }

   @Override
   public void onDisable() {
      this.clear();
   }

   @Override
   public void tick(Minecraft var1) {
      if (this.isEnabled()) {
         if (!this.isRuntimeActive()) {
            this.clear();
         } else {
            boolean var2 = var1.options.getCameraType().isFirstPerson();
            if (this.lastLevel != var1.level
               || this.lastPlayer != var1.player
               || this.wasFirstPerson != var2
               || this.lastReloadCount != PostProcessing.reloadCount) {
               this.clear();
               this.lastLevel = var1.level;
               this.lastPlayer = var1.player;
               this.wasFirstPerson = var2;
               this.lastReloadCount = PostProcessing.reloadCount;
            }

            if (var1.player != null && var1.level != null && var1.player.isAlive() && var2) {
               Vec3 var3 = var1.player.position();
               if (this.lastPosition != null && this.lastPosition.distanceToSqr(var3) > 64.0) {
                  WeaponTrailRenderer.reset();
                  LarpLog.debug("WeaponTrails", "Cleared blade history after player relocation");
               }

               this.lastPosition = var3;
               int var4 = var1.player.getInventory().getSelectedSlot();
               if (this.lastSelectedSlot != var4) {
                  WeaponTrailRenderer.resetHand(InteractionHand.MAIN_HAND);
               }

               this.lastSelectedSlot = var4;
               if (!SwordModelsModule.isSword(var1.player.getMainHandItem())) {
                  WeaponTrailRenderer.resetHand(InteractionHand.MAIN_HAND);
               }

               if (!SwordModelsModule.isSword(var1.player.getOffhandItem())) {
                  WeaponTrailRenderer.resetHand(InteractionHand.OFF_HAND);
               }

               WeaponTrailRenderer.setCameraMotion(TickTimer.getTickTime(), this.trailDuration.getValue());
            } else {
               WeaponTrailRenderer.reset();
            }
         }
      }
   }

   @Override
   public void prepare(float var1, CameraRenderState var2) {
      if (!this.isEnabled()) {
         ;
      }
   }

   @Override
   public void render(EffectsRenderer var1, boolean var2) {
      if (!this.isEnabled()) {
         ;
      }
   }

   @Override
   public boolean glowing() {
      return this.isEnabled() ? false : false;
   }

   @Override
   public void clear() {
      WeaponTrailRenderer.reset();
      this.lastLevel = this.lastPlayer = null;
      this.lastPosition = null;
      this.lastSelectedSlot = -1;
   }
}
