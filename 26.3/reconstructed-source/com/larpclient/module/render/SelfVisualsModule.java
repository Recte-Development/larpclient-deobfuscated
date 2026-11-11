package com.larpclient.module.render;

import com.larpclient.core.StreamProofManager;
import com.larpclient.gui.theme.Theme;
import com.larpclient.module.misc.BooleanSetting;
import com.larpclient.module.misc.Category;
import com.larpclient.module.misc.ColorSetting;
import com.larpclient.module.misc.ModeSetting;
import com.larpclient.module.misc.Module;
import com.larpclient.module.misc.NumberSetting;
import com.larpclient.module.misc.Setting;
import com.larpclient.render.entity.EntityHaloRenderer;
import com.larpclient.render.misc.VisualsRenderer;
import com.larpclient.render.model.ModelVisualRenderer;
import com.larpclient.render.overlay.LiquidGlass;
import com.larpclient.render.particle.CometTrailRenderer;
import com.larpclient.util.misc.LarpLog;
import java.util.List;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;

public final class SelfVisualsModule extends Module {
   private static SelfVisualsModule instance;
   public final ModeSetting style = this.addSetting(
      new ModeSetting(
         "Style",
         "Player material",
         "Rainbow Outline",
         "Rainbow Outline",
         "Hologram",
         "Comets",
         "Outline",
         "Liquid Glass",
         "Solid",
         "Glow",
         "Metallic",
         "Camo",
         "Marble",
         "Glitter",
         "Ghost"
      )
   );
   public final NumberSetting rainbowSpeed = this.addSetting(new NumberSetting("Rainbow Speed", "Cycles per second", 0.18, 0.0, 2.0, 0.01));
   public final ModeSetting direction = this.addSetting(new ModeSetting("Direction", "Gradient flow", "Downward", "Downward", "Upward", "Horizontal", "Static"));
   public final NumberSetting gradientScale = this.addSetting(new NumberSetting("Gradient Scale", "Rainbow cycles per block", 0.5, 0.1, 2.0, 0.05));
   public final NumberSetting saturation = this.addSetting(new NumberSetting("Saturation", "Rainbow saturation", 0.62, 0.0, 1.0, 0.02));
   public final NumberSetting brightness = this.addSetting(new NumberSetting("Brightness", "Rainbow brightness", 0.95, 0.1, 1.0, 0.025));
   public final NumberSetting modelOpacity = this.addSetting(new NumberSetting("Model Opacity", "Body opacity", 1.0, 0.05, 1.0, 0.05));
   public final BooleanSetting outline = this.addSetting(new BooleanSetting("Outline", "Screen-space silhouette outline", true));
   public final ColorSetting outlineColor = this.addSetting(new ColorSetting("Outline Color", "Silhouette border", -1));
   public final NumberSetting outlineThickness = this.addSetting(new NumberSetting("Outline Thickness", "Screen pixels", 1.5, 0.5, 5.0, 0.5));
   public final NumberSetting outlineOpacity = this.addSetting(new NumberSetting("Outline Opacity", "Border opacity", 0.7, 0.0, 1.0, 0.05));
   public final BooleanSetting outlineBloom = this.addSetting(new BooleanSetting("Outline Bloom", "Soft halo outside border", false));
   public final BooleanSetting modelBloom = this.addSetting(new BooleanSetting("Model Bloom", "Explicit body glow", false));
   public final BooleanSetting affectCape = this.addSetting(new BooleanSetting("Affect Cape", "Apply Rainbow Outline to your Minecraft cape", false));
   public final NumberSetting cometSize = this.addSetting(new NumberSetting("Comet Size", "Core radius in blocks", 0.055, 0.025, 0.12, 0.005));
   public final NumberSetting orbitSpeed = this.addSetting(new NumberSetting("Orbit Speed", "Orbits per second", 0.32, 0.05, 1.2, 0.01));
   public final NumberSetting orbitRadius = this.addSetting(new NumberSetting("Orbit Radius", "Blocks; kept outside the current body", 0.52, 0.3, 1.0, 0.01));
   public final NumberSetting trailLength = this.addSetting(new NumberSetting("Trail Length", "Seconds of previous orbit positions", 0.24, 0.08, 0.6, 0.01));
   public final NumberSetting glowIntensity = this.addSetting(new NumberSetting("Glow Intensity", "Halo and trail brightness", 1.0, 0.2, 2.0, 0.05));
   public final BooleanSetting cometBloom = this.addSetting(new BooleanSetting("Comet Bloom", "Submit comets to the shared bloom pass", true));
   public final BooleanSetting themeColors = this.addSetting(new BooleanSetting("Theme Colors", "Use current theme accents for comets", true));
   public final ColorSetting cometColor = this.addSetting(new ColorSetting("Comet Color", "Core and trail head", Theme.accent));
   public final ColorSetting tailColor = this.addSetting(new ColorSetting("Tail Color", "Faded trail end", Theme.accentEnd));
   public final NumberSetting glassOpacity = this.addSetting(new NumberSetting("Glass Opacity", "Liquid surface opacity", 0.62, 0.1, 1.0, 0.02));
   public final NumberSetting refractionStrength = this.addSetting(
      new NumberSetting("Refraction Strength", "Screen displacement in pixels", 3.5, 0.0, 12.0, 0.25)
   );
   public final NumberSetting surfaceFlow = this.addSetting(new NumberSetting("Surface Flow", "Fluid surface motion speed", 0.65, 0.0, 2.0, 0.05));
   public final NumberSetting gloss = this.addSetting(new NumberSetting("Gloss", "Broad soft reflections to tight highlights", 0.78, 0.0, 1.0, 0.02));
   public final NumberSetting edgeHighlight = this.addSetting(new NumberSetting("Edge Highlight", "Viewing-angle surface highlights", 0.8, 0.0, 1.0, 0.02));
   public final NumberSetting iridescence = this.addSetting(new NumberSetting("Iridescence", "Mild viewing-angle interference", 0.18, 0.0, 1.0, 0.02));
   public final BooleanSetting enhancedRefraction = this.addSetting(
      new BooleanSetting("Enhanced Refraction", "Local scene refraction where effects quality permits", true)
   );
   public final BooleanSetting themePalette = this.addSetting(new BooleanSetting("Theme Palette", "Use current theme accents for chams colors", false));
   public final ColorSetting primaryColor = this.addSetting(new ColorSetting("Primary Color", "Main chams color", -15814));
   public final ColorSetting secondaryColor = this.addSetting(new ColorSetting("Secondary Color", "Pattern, vein and shadow color", -14018044));
   public final NumberSetting shading = this.addSetting(new NumberSetting("Shading", "Flat color to fully lit faces", 0.6, 0.0, 1.0, 0.05));
   public final NumberSetting patternScale = this.addSetting(new NumberSetting("Pattern Scale", "Pattern cells per skin area", 1.0, 0.25, 4.0, 0.05));
   public final NumberSetting patternSpeed = this.addSetting(
      new NumberSetting("Pattern Speed", "Pattern drift and twinkle cycles per second", 0.25, 0.0, 2.0, 0.05)
   );
   private static final List<String> cHAMS_STYLES = List.of("Solid", "Glow", "Metallic", "Camo", "Marble", "Glitter", "Ghost");
   public final BooleanSetting affectArmor = this.addSetting(new BooleanSetting("Affect Armor", "Apply material to armor", false));
   public final BooleanSetting affectHeldItems = this.addSetting(new BooleanSetting("Affect Held Items", "Apply material to equipped items", false));
   public final BooleanSetting firstPersonArms = this.addSetting(
      new BooleanSetting("First Person Arms", "Apply material to arms independently of held items", false)
   );
   public final BooleanSetting thirdPerson = this.addSetting(new BooleanSetting("Third Person", "Style the local player in third person", true));

   public boolean isLiquidGlass() {
      return this.style.isMode("Liquid Glass");
   }

   public boolean isChamsStyle() {
      return cHAMS_STYLES.contains(this.style.getValue());
   }

   private boolean isPatternStyle() {
      return this.style.isMode("Camo") || this.style.isMode("Marble") || this.style.isMode("Glitter");
   }

   public int getPrimaryColor() {
      return this.themePalette.getValue() ? Theme.accent : this.primaryColor.getValue();
   }

   public int getSecondaryColor() {
      return this.themePalette.getValue() ? Theme.accentEnd : this.secondaryColor.getValue();
   }

   public ModelVisualRenderer.Material getMaterial() {
      if (this.isEnabled()) {
         return switch ((String)this.style.getValue()) {
            case "Rainbow Outline" -> ModelVisualRenderer.Material.RAINBOW;
            case "Hologram" -> ModelVisualRenderer.Material.HOLOGRAM;
            case "Liquid Glass" -> ModelVisualRenderer.Material.LIQUID_GLASS;
            case "Solid" -> ModelVisualRenderer.Material.CHAMS_STYLE_1;
            case "Glow" -> ModelVisualRenderer.Material.CHAMS_STYLE_2;
            case "Metallic" -> ModelVisualRenderer.Material.CHAMS_STYLE_3;
            case "Camo" -> ModelVisualRenderer.Material.CHAMS_STYLE_4;
            case "Marble" -> ModelVisualRenderer.Material.CHAMS_STYLE_5;
            case "Glitter" -> ModelVisualRenderer.Material.CHAMS_STYLE_6;
            case "Ghost" -> ModelVisualRenderer.Material.CHAMS_STYLE_7;
            default -> null;
         };
      } else {
         return null;
      }
   }

   public double getPrimaryColor() {
      return this.isLiquidGlass() ? this.glassOpacity.getValue() : this.modelOpacity.getValue();
   }

   public boolean rendersModel() {
      return !this.style.isMode("Comets") && !this.style.isMode("Outline");
   }

   public boolean shouldRenderOutline() {
      return this.rendersModel() || this.style.isMode("Outline");
   }

   public boolean getMaterial() {
      return !this.isEnabled() ? false : this.style.isMode("Rainbow Outline") && this.affectCape.getValue();
   }

   public SelfVisualsModule() {
      super("SelfVisuals", "Style your player with colors and comets", Category.VISUALS, -1);
      instance = this;

      for (Setting var4 : new Setting[]{this.rainbowSpeed, this.direction, this.gradientScale, this.saturation, this.brightness}) {
         var4.visibleWhen(() -> this.style.isMode("Rainbow Outline"));
      }

      this.rainbowSpeed.visibleWhen(() -> this.style.isMode("Rainbow Outline") && !this.direction.isMode("Static"));

      for (Setting var17 : new Setting[]{this.outlineColor, this.outlineThickness, this.outlineOpacity, this.outlineBloom}) {
         var17.visibleWhen(() -> this.shouldRenderOutline() && this.outline.getValue());
      }

      this.affectCape.visibleWhen(() -> this.style.isMode("Rainbow Outline"));
      this.themePalette.visibleWhen(this::isChamsStyle);
      this.primaryColor.visibleWhen(() -> this.isChamsStyle() && !this.themePalette.getValue());
      this.secondaryColor.visibleWhen(() -> (this.isPatternStyle() || this.style.isMode("Metallic")) && !this.themePalette.getValue());
      this.shading.visibleWhen(() -> this.isChamsStyle() && !this.style.isMode("Glow") && !this.style.isMode("Ghost"));
      this.patternScale.visibleWhen(this::isPatternStyle);
      this.patternSpeed.visibleWhen(this::isPatternStyle);

      for (Setting var18 : new Setting[]{this.modelOpacity, this.outline, this.modelBloom, this.affectArmor, this.affectHeldItems, this.firstPersonArms}) {
         var18.visibleWhen(this::rendersModel);
      }

      this.modelOpacity.visibleWhen(() -> this.rendersModel() && !this.isLiquidGlass());
      this.affectHeldItems.visibleWhen(() -> this.rendersModel() && !this.isLiquidGlass());

      for (Setting var19 : new Setting[]{this.glassOpacity, this.surfaceFlow, this.gloss, this.edgeHighlight, this.iridescence, this.enhancedRefraction}) {
         var19.visibleWhen(this::isLiquidGlass);
      }

      this.refractionStrength.visibleWhen(() -> this.isLiquidGlass() && this.enhancedRefraction.getValue());
      this.outline.visibleWhen(this::shouldRenderOutline);

      for (Setting var20 : new Setting[]{
         this.cometSize, this.orbitSpeed, this.orbitRadius, this.trailLength, this.glowIntensity, this.cometBloom, this.themeColors
      }) {
         var20.visibleWhen(() -> this.style.isMode("Comets"));
      }

      this.cometColor.visibleWhen(() -> this.style.isMode("Comets") && !this.themeColors.getValue());
      this.tailColor.visibleWhen(() -> this.style.isMode("Comets") && !this.themeColors.getValue());
      this.style.onChange(var0 -> {
         CometTrailRenderer.reset();
         LarpLog.info("SelfVisuals", "Style: {}", var0);
      });
   }

   public List<Setting<?>> getStyleSettings() {
      return List.of(this.style, this.thirdPerson, this.outline, this.affectArmor, this.affectHeldItems, this.firstPersonArms, this.affectCape);
   }

   public List<Setting<?>> getSecondaryColor() {
      return List.of(
         this.gradientScale,
         this.saturation,
         this.brightness,
         this.themePalette,
         this.primaryColor,
         this.secondaryColor,
         this.shading,
         this.patternScale,
         this.modelOpacity,
         this.outlineColor,
         this.outlineThickness,
         this.outlineOpacity,
         this.outlineBloom,
         this.modelBloom,
         this.cometSize,
         this.glowIntensity,
         this.cometBloom,
         this.themeColors,
         this.cometColor,
         this.tailColor,
         this.glassOpacity,
         this.gloss,
         this.edgeHighlight,
         this.iridescence
      );
   }

   public List<Setting<?>> getMotionSettings() {
      return List.of(this.direction, this.rainbowSpeed, this.patternSpeed, this.orbitSpeed, this.orbitRadius, this.trailLength, this.surfaceFlow);
   }

   public List<Setting<?>> getRefractionSettings() {
      return List.of(this.enhancedRefraction, this.refractionStrength);
   }

   public static SelfVisualsModule getPrimaryColor() {
      return instance;
   }

   public boolean isActiveForSelf() {
      return !this.isEnabled()
         ? false
         : VisualsRenderer.isRenderingEnabled()
            && VisualsRenderer.isStageActive("SelfVisuals")
            && this.isOperational()
            && (!this.isLiquidGlass() || PlayerModelModule.getInstance() == null || !PlayerModelModule.getInstance().isActive())
            && this.thirdPerson.getValue()
            && this.isInGame()
            && !minecraft.options.getCameraType().isFirstPerson();
   }

   public static boolean shouldStyleRenderState(LivingEntityRenderState var0) {
      if (instance != null && instance.isEnabled()) {
         SelfVisualsModule var1 = instance;
         return var1 != null
            && var1.isActiveForSelf()
            && var1.rendersModel()
            && !StreamProofManager.submitFrame()
            && VisualsRenderer.isSelfRenderState(var0)
            && (!var1.isLiquidGlass() || LiquidGlass.isMaterialAvailable())
            && var0 instanceof AvatarRenderState var2
            && var2.id == getMc().player.getId();
      } else {
         return false;
      }
   }

   @Override
   public void onEnable() {
      CometTrailRenderer.reset();
      LarpLog.info(
         "SelfVisuals",
         "Style: {}; third-person: {}; first-person arms: {}",
         this.style.getValue(),
         this.thirdPerson.getValue(),
         this.firstPersonArms.getValue()
      );
   }

   @Override
   public void onDisable() {
      CometTrailRenderer.reset();
      ModelVisualRenderer.clearCaches();
      EntityHaloRenderer.close();
   }
}
