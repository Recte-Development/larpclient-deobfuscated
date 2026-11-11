package com.larpclient.module.render;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.larpclient.config.HitEffectConfig;
import com.larpclient.gui.theme.Theme;
import com.larpclient.module.misc.BooleanSetting;
import com.larpclient.module.misc.Category;
import com.larpclient.module.misc.ColorSetting;
import com.larpclient.module.misc.Module;
import com.larpclient.module.misc.NumberSetting;
import com.larpclient.module.misc.Setting;
import com.larpclient.render.misc.EffectBatch;
import com.larpclient.render.model.ChinaHatHorns;
import com.larpclient.render.model.ModelVisualRenderer;
import com.larpclient.render.particle.ChinaHatFireParticles;
import com.larpclient.render.world.EffectsRenderer;
import com.larpclient.social.cosmetic.DominusHatMesh;
import com.larpclient.util.data.TickTimer;
import com.larpclient.util.misc.LarpLog;
import com.larpclient.util.render.ColorUtil;
import com.larpclient.util.render.HeadCoinLayout;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;

public final class ChinaHatModule extends Module {
   private static ChinaHatModule instance;
   private final BooleanSetting cone = this.addSetting(new BooleanSetting("Cone", "Wear the cone hat", true));
   private final BooleanSetting kippah = this.addSetting(new BooleanSetting("Kippah", "Wear the kippah", false));
   private final BooleanSetting crown = this.addSetting(new BooleanSetting("Crown", "Wear the crown", false));
   private final BooleanSetting halo = this.addSetting(new BooleanSetting("Halo", "Wear the halo", false));
   private final BooleanSetting horns = this.addSetting(new BooleanSetting("Horns", "Wear the fiery horns", false));
   private final BooleanSetting dominus = this.addSetting(new BooleanSetting("Dominus", "Wear the Dominus hood", false));
   private final ColorSetting kippahColor = this.addSetting(new ColorSetting("Kippah Color", "Skullcap fabric color", -15258800));
   private final ColorSetting starColor = this.addSetting(new ColorSetting("Star Color", "Star of David embroidery", -7517));
   private final NumberSetting kippahSize = this.addSetting(createDecimalSetting("Kippah Size", "Skullcap radius in blocks", 0.22, 0.15, 0.3, 0.01));
   private final NumberSetting kippahHeight = this.addSetting(createDecimalSetting("Kippah Height", "Rounded dome height in blocks", 0.08, 0.03, 0.14, 0.01));
   private final NumberSetting kippahOffset = this.addSetting(
      new NumberSetting("Kippah Offset", "Clearance above the head in blocks", 0.005, 0.0, 0.1, 0.005) {
         @Override
         public String getValueAsString() {
            return String.format(Locale.ROOT, "%.3f", this.getValue());
         }
      }
   );
   private final NumberSetting kippahOpacity = this.addSetting(createDecimalSetting("Kippah Opacity", "Kippah fabric transparency", 1.0, 0.1, 1.0, 0.05));
   private final NumberSetting accessorySize = this.addSetting(
      createDecimalSetting("Accessory Size", "Crown, halo, horns or Dominus scale", 1.0, 0.6, 1.4, 0.05)
   );
   private final NumberSetting accessoryOpacity = this.addSetting(createDecimalSetting("Accessory Opacity", "Crown or halo transparency", 1.0, 0.1, 1.0, 0.05));
   private final BooleanSetting self = this.addSetting(new BooleanSetting("Self", "Show your hat in third person", true));
   private final BooleanSetting otherPlayers = this.addSetting(
      new BooleanSetting("Other Players", "Local preview on nearby players; never advertises their hats", false)
   );
   private final BooleanSetting themeColors = this.addSetting(new BooleanSetting("Theme Colors", "Use the current theme gradient", true));
   private final ColorSetting topColor = this.addSetting(new ColorSetting("Top Color", "Cone tip color", -3429889));
   private final ColorSetting rimColor = this.addSetting(new ColorSetting("Rim Color", "Brim color", -8890948));
   private final NumberSetting radius = this.addSetting(new NumberSetting("Radius", "Brim radius in blocks", 0.6, 0.25, 1.0, 0.05));
   private final NumberSetting height = this.addSetting(new NumberSetting("Height", "Cone height in blocks", 0.3, 0.1, 0.7, 0.05));
   private final NumberSetting offset = this.addSetting(new NumberSetting("Offset", "Height above the head", 0.04, 0.0, 0.5, 0.01));
   private final NumberSetting opacity = this.addSetting(new NumberSetting("Opacity", "Cone transparency", 0.65, 0.1, 1.0, 0.05));
   private final BooleanSetting outline = this.addSetting(new BooleanSetting("Outline", "Fine outline on the cone brim and kippah hem", true));
   private final ColorSetting hornColor = this.addSetting(new ColorSetting("Horn Color", "Horn and thorn color", -61184));
   private final BooleanSetting hornTheme = this.addSetting(new BooleanSetting("Horn Theme", "Horns use the theme's second color", false));
   private final ColorSetting fireColor = this.addSetting(new ColorSetting("Fire Color", "Outer flames and the horn tips", -42752));
   private final ColorSetting fireCoreColor = this.addSetting(new ColorSetting("Fire Core Color", "Bright inner flames and the hottest tip points", -2304));
   private final BooleanSetting fireTheme = this.addSetting(new BooleanSetting("Fire Theme", "Fire uses the theme accent", false));
   private final NumberSetting fireSize = this.addSetting(
      createDecimalSetting("Fire Size", "Width and height of the fire between the horns", 1.0, 0.5, 2.0, 0.05)
   );
   private final NumberSetting fireHeat = this.addSetting(createDecimalSetting("Fire Heat", "How fast and high the flames rise", 1.0, 0.5, 2.0, 0.05));
   private final ColorSetting dominusColor = this.addSetting(
      new ColorSetting("Dominus Color", "Tints the hood and wings; white keeps the original shading", -1)
   );
   private final List<ChinaHatModule.GlowEntry> glowEntries = new ArrayList<>();
   private List<ChinaHatModule.RenderEntry> renderEntries = List.of();
   private ClientLevel renderLevel;
   private final Map<Integer, ChinaHatFireParticles> fireParticles = new HashMap<>();
   private final Set<Integer> dominusEntityIds = new HashSet<>();
   private long frameCounter;
   private long lastGlowFrame = -1L;

   public ChinaHatModule() {
      super("China Hat", "Wear any mix of cone, kippah, crown, halo, fiery horns and Dominus", Category.VISUALS, -1);
      instance = this;

      for (Setting var4 : new Setting[]{this.kippahColor, this.starColor, this.kippahSize, this.kippahHeight, this.kippahOffset, this.kippahOpacity}) {
         var4.visibleWhen(this.kippah::getValue);
      }

      for (Setting var14 : new Setting[]{this.themeColors, this.radius, this.height, this.offset}) {
         var14.visibleWhen(this.cone::getValue);
      }

      this.themeColors.setDisplayName("Cone Theme");
      this.radius.setDisplayName("Cone Radius");
      this.height.setDisplayName("Cone Height");
      this.offset.setDisplayName("Cone Offset");
      this.accessorySize.visibleWhen(() -> this.crown.getValue() || this.halo.getValue() || this.horns.getValue() || this.dominus.getValue());
      this.dominusColor.visibleWhen(this.dominus::getValue);
      this.accessoryOpacity.visibleWhen(() -> this.crown.getValue() || this.halo.getValue());
      this.opacity.visibleWhen(this.cone::getValue);
      this.outline.visibleWhen(() -> this.cone.getValue() || this.kippah.getValue());

      for (Setting var15 : new Setting[]{this.hornTheme, this.fireTheme, this.fireSize, this.fireHeat}) {
         var15.visibleWhen(this.horns::getValue);
      }

      this.hornColor.visibleWhen(() -> this.horns.getValue() && !this.hornTheme.getValue());

      for (Setting var16 : new Setting[]{this.fireColor, this.fireCoreColor}) {
         var16.visibleWhen(() -> this.horns.getValue() && !this.fireTheme.getValue());
      }

      this.topColor.visibleWhen(() -> this.cone.getValue() && !this.themeColors.getValue());
      this.rimColor.visibleWhen(() -> this.cone.getValue() && !this.themeColors.getValue());
   }

   @Override
   public String getDisplayName() {
      return "Hats";
   }

   @Override
   public List<String> getSearchAliases() {
      return List.of("China Hat");
   }

   public static void migrateLegacyMode(JsonObject var0) {
      JsonElement var1 = var0.remove("Mode");
      if (var1 != null && var1.isJsonPrimitive() && var1.getAsJsonPrimitive().isString()) {
         List var2 = List.of("Cone", "Kippah", "Crown", "Halo", "Horns", "Dominus");
         String var3 = var1.getAsString();
         if (var2.contains(var3) && !var2.stream().anyMatch(var0::has)) {
            for (String var5 : var2) {
               var0.addProperty(var5, var5.equals(var3));
            }
         }
      }
   }

   private static NumberSetting createDecimalSetting(String var0, String var1, double var2, double var4, double var6, double var8) {
      return new NumberSetting(var0, var1, var2, var4, var6, var8) {
         @Override
         public String getValueAsString() {
            return String.format(Locale.ROOT, "%.2f", this.getValue());
         }
      };
   }

   private List<HeadCoinLayout.Accessory> collectRenderEntries() {
      if (this.crown.getValue()) {
         return this.halo.getValue() ? List.of(HeadCoinLayout.Accessory.CROWN, HeadCoinLayout.Accessory.HALO) : List.of(HeadCoinLayout.Accessory.CROWN);
      } else {
         return this.halo.getValue() ? List.of(HeadCoinLayout.Accessory.HALO) : List.of();
      }
   }

   private ChinaHatModule.HornStyle buildHornStyle() {
      int var1 = this.fireTheme.getValue() ? Theme.accent : this.fireColor.getValue();
      return new ChinaHatModule.HornStyle(
         this.hornTheme.getValue() ? Theme.accentEnd : this.hornColor.getValue(),
         var1,
         this.fireTheme.getValue() ? ChinaHatHorns.lighten(Theme.accent) : this.fireCoreColor.getValue(),
         this.fireSize.getValue(),
         this.fireHeat.getValue()
      );
   }

   public List<Setting<?>> getHatToggles() {
      return List.of(this.cone, this.kippah, this.crown, this.halo, this.horns, this.dominus);
   }

   public Setting<?> getSelfSetting() {
      return this.self;
   }

   public static ChinaHatModule getInstance() {
      return instance;
   }

   @Override
   public void onEnable() {
      LarpLog.info(
         "ChinaHat",
         "Cosmetic enabled; hats={}, cached geometry, depth tested; Kippah, Crown, Halo, Horns and Dominus follow vanilla head pose",
         this.getHatToggles().stream().filter(var0 -> Boolean.TRUE.equals(var0.getValue())).map(Setting::getName).toList()
      );
   }

   private JsonObject buildConeConfig() {
      JsonObject var1 = new JsonObject();
      var1.addProperty("version", 1);
      var1.addProperty("enabled", this.isOperational());
      var1.addProperty("top", this.themeColors.getValue() ? Theme.accent : this.topColor.getValue());
      var1.addProperty("rim", this.themeColors.getValue() ? Theme.accentEnd : this.rimColor.getValue());
      var1.addProperty("radius", this.radius.getValue());
      var1.addProperty("height", this.height.getValue());
      var1.addProperty("offset", this.offset.getValue());
      var1.addProperty("opacity", this.opacity.getValue());
      var1.addProperty("outline", this.outline.getValue());
      return var1;
   }

   public List<ChinaHatModule.HatVariant> getHatVariants() {
      if (this.isOperational() && this.self.getValue()) {
         ArrayList var1 = new ArrayList(6);
         if (this.horns.getValue()) {
            double var2 = this.accessorySize.getValue();
            double var4 = (0.24F + ChinaHatFireParticles.getCount(1.0, this.fireHeat.getValue()) * this.fireSize.getValue()) * var2;
            var1.add(new ChinaHatModule.HatVariant(false, var2, Math.max(ChinaHatHorns.hornHeight(var2), var4), 0.0, -1, 0, false, null, this.buildHornStyle()));
         }

         if (this.dominus.getValue()) {
            var1.add(
               new ChinaHatModule.HatVariant(
                  false,
                  this.accessorySize.getValue(),
                  DominusHatMesh.loadMesh(this.accessorySize.getValue()),
                  0.0,
                  this.dominusColor.getValue() | 0xFF000000,
                  0,
                  false,
                  null,
                  null,
                  true
               )
            );
         }

         for (HeadCoinLayout.Accessory var3 : this.collectRenderEntries()) {
            var1.add(
               new ChinaHatModule.HatVariant(
                  false,
                  this.accessorySize.getValue(),
                  HeadCoinLayout.riseFor(var3, this.accessorySize.getValue()),
                  0.0,
                  ColorUtil.withAlpha(16777215, this.accessoryOpacity.getValue()),
                  0,
                  false,
                  var3
               )
            );
         }

         if (this.kippah.getValue()) {
            var1.add(
               new ChinaHatModule.HatVariant(
                  true,
                  this.kippahSize.getValue(),
                  this.kippahHeight.getValue(),
                  this.kippahOffset.getValue(),
                  ColorUtil.withAlpha(this.kippahColor.getValue(), this.kippahOpacity.getValue()),
                  ColorUtil.withAlpha(this.starColor.getValue(), this.kippahOpacity.getValue()),
                  this.outline.getValue()
               )
            );
         }

         double var7 = this.opacity.getValue();
         if (this.cone.getValue()) {
            var1.add(
               new ChinaHatModule.HatVariant(
                  false,
                  this.radius.getValue(),
                  this.height.getValue(),
                  this.offset.getValue(),
                  ColorUtil.withAlpha(this.themeColors.getValue() ? Theme.accent : this.topColor.getValue(), var7),
                  ColorUtil.withAlpha(this.themeColors.getValue() ? Theme.accentEnd : this.rimColor.getValue(), var7),
                  this.outline.getValue()
               )
            );
         }

         return var1;
      } else {
         return List.of();
      }
   }

   public List<Setting<?>> getVisibilitySettings() {
      return List.of(this.self, this.otherPlayers);
   }

   public List<Setting<?>> getColorSettings() {
      return List.of(
         this.themeColors,
         this.topColor,
         this.rimColor,
         this.kippahColor,
         this.starColor,
         this.kippahOpacity,
         this.hornTheme,
         this.hornColor,
         this.fireTheme,
         this.fireColor,
         this.fireCoreColor,
         this.dominusColor,
         this.opacity,
         this.accessoryOpacity,
         this.outline,
         this.fireSize,
         this.fireHeat
      );
   }

   public List<Setting<?>> getSizeSettings() {
      return List.of(this.radius, this.height, this.offset, this.kippahSize, this.kippahHeight, this.kippahOffset, this.accessorySize);
   }

   @Override
   public void onDisable() {
      this.renderEntries = List.of();
      this.renderLevel = null;
      this.fireParticles.clear();
      this.lastGlowFrame = -1L;
      this.dominusEntityIds.clear();
      this.glowEntries.clear();
   }

   public boolean migrateLegacyMode(int var1) {
      return !this.isEnabled() ? false : this.isOperational() && this.dominusEntityIds.contains(var1);
   }

   public void collectRenderEntries(float var1) {
      if (this.isEnabled()) {
         if (this.renderLevel != null && this.renderLevel != minecraft.level) {
            this.fireParticles.clear();
         }

         this.renderEntries = List.of();
         this.renderLevel = null;
         this.lastGlowFrame = -1L;
         this.dominusEntityIds.clear();
         if (!this.isInGame()) {
            this.fireParticles.clear();
         } else {
            ArrayList var2 = new ArrayList();
            JsonObject var3 = this.buildConeConfig();
            var3.addProperty("enabled", this.isEnabled());
            List var4 = this.collectRenderEntries();
            ChinaHatHorns.HornMesh var5 = this.horns.getValue() ? ChinaHatHorns.loadMesh() : null;
            ChinaHatModule.HornStyle var6 = var5 == null ? null : this.buildHornStyle();
            int var7 = this.dominus.getValue() && DominusHatMesh.loadMesh() != null ? 1 : 0;
            int var8 = var4.size() + (var5 != null ? 1 : 0) + (var7 != 0 ? 1 : 0) + (this.kippah.getValue() ? 1 : 0);
            int var9 = 0;

            for (AbstractClientPlayer var11 : minecraft.level.players()) {
               JsonObject var12 = var3;
               if ((
                     var11 == minecraft.player
                        ? this.isOperational() && this.self.getValue() && !minecraft.options.getCameraType().isFirstPerson()
                        : this.isOperational() && this.otherPlayers.getValue()
                  )
                  && var12.get("enabled").getAsBoolean()) {
                  int var13 = ColorUtil.withAlpha(var12.get("top").getAsInt(), var12.get("opacity").getAsDouble());
                  int var14 = ColorUtil.withAlpha(var12.get("rim").getAsInt(), var12.get("opacity").getAsDouble());
                  if (var11.isAlive()
                     && !var11.isInvisible()
                     && !var11.isSpectator()
                     && !var11.isSwimming()
                     && !var11.isFallFlying()
                     && !var11.isVisuallyCrawling()
                     && !var11.isSleeping()
                     && !(var11.distanceToSqr(minecraft.player) > 2304.0)) {
                     int var15 = var8 > 0 && (var11 == minecraft.player || var9 + var8 <= 16) ? 1 : 0;
                     if (var15 != 0 || this.cone.getValue()) {
                        if (var15 != 0 && var11 != minecraft.player) {
                           var9 += var8;
                        }

                        EntityRenderState var16 = ModelVisualRenderer.extractEntity(var11, var1);
                        double var17 = 0.28
                           + 0.72 * minecraft.level.getRawBrightness(BlockPos.containing(var11.getEyePosition(var1)), minecraft.level.getSkyDarken()) / 15.0;
                        if (var15 != 0) {
                           if (var5 != null) {
                              ChinaHatFireParticles var19 = this.fireParticles
                                 .computeIfAbsent(var11.getId(), var0 -> new ChinaHatFireParticles(var0.intValue()));
                              var2.add(
                                 new ChinaHatModule.RenderEntry(
                                    false, null, var16, this.accessorySize.getValue(), 0.0, 0.0, -1, 0, false, null, var6, var19, false, var17
                                 )
                              );
                           }

                           if (var7 != 0) {
                              var2.add(
                                 new ChinaHatModule.RenderEntry(
                                    false,
                                    null,
                                    var16,
                                    this.accessorySize.getValue(),
                                    0.0,
                                    0.0,
                                    this.dominusColor.getValue() | 0xFF000000,
                                    0,
                                    false,
                                    null,
                                    null,
                                    null,
                                    true,
                                    var17
                                 )
                              );
                              this.dominusEntityIds.add(var11.getId());
                           }

                           for (HeadCoinLayout.Accessory var20 : var4) {
                              var2.add(
                                 new ChinaHatModule.RenderEntry(
                                    false,
                                    null,
                                    var16,
                                    this.accessorySize.getValue(),
                                    0.0,
                                    0.0,
                                    ColorUtil.withAlpha(16777215, this.accessoryOpacity.getValue()),
                                    0,
                                    false,
                                    var20,
                                    null,
                                    null,
                                    false,
                                    var17
                                 )
                              );
                           }

                           if (this.kippah.getValue()) {
                              double var22 = this.kippahOpacity.getValue();
                              var2.add(
                                 new ChinaHatModule.RenderEntry(
                                    true,
                                    null,
                                    var16,
                                    this.kippahSize.getValue(),
                                    this.kippahHeight.getValue(),
                                    this.kippahOffset.getValue(),
                                    ColorUtil.withAlpha(this.kippahColor.getValue(), var22),
                                    ColorUtil.withAlpha(this.starColor.getValue(), var22),
                                    this.outline.getValue(),
                                    var17
                                 )
                              );
                           }
                        }

                        if (this.cone.getValue()) {
                           Vec3 var23 = var11.getPosition(var1).add(0.0, var11.getBbHeight() + var12.get("offset").getAsDouble(), 0.0);
                           var2.add(
                              new ChinaHatModule.RenderEntry(
                                 false,
                                 var23,
                                 var16,
                                 var12.get("radius").getAsDouble(),
                                 var12.get("height").getAsDouble(),
                                 var12.get("offset").getAsDouble(),
                                 var13,
                                 var14,
                                 var12.get("outline").getAsBoolean(),
                                 var17
                              )
                           );
                        }
                     }
                  }
               }
            }

            this.renderLevel = minecraft.level;
            this.renderEntries = List.copyOf(var2);
            this.fireParticles.values().removeIf(var1x -> var2.stream().noneMatch(var1xx -> var1xx.fire == var1x));
         }
      }
   }

   public void renderHats(EffectsRenderer var1, float var2) {
      if (this.isEnabled()) {
         this.frameCounter++;
         this.glowEntries.clear();
         if (this.renderLevel == minecraft.level) {
            var1.blend(HitEffectConfig.BlendMode.NORMAL);
            double var3 = TickTimer.getTime();
            byte var5 = 0;

            for (ChinaHatModule.RenderEntry var7 : this.renderEntries) {
               if (var7.dominus) {
                  if (var7.model != null) {
                     var1.drawHatGlow(var7.model, var7.radius, var7.top);
                  }
               } else if (var7.horns != null) {
                  if (var5 != 0 || (var5 = ChinaHatHorns.updateTexture(var7.horns.horn(), var7.horns.fire(), var7.horns.core())) != 0) {
                     Matrix4f var13 = var7.model == null ? null : var1.drawChinaHat(var7.model, var7.radius);
                     if (var13 != null) {
                        var7.fire.pipeline.set(var13);
                        var7.fire.textureGeneration = this.lastGlowFrame = this.frameCounter;
                     }
                  }
               } else if (var7.accessory != null) {
                  if (var7.model != null) {
                     double var8 = (var7.top >>> 24) / 255.0;
                     Matrix4f var10 = var1.drawEntityHat(var7.model, var7.accessory, var7.radius, var8, var7.lit, var3);
                     if (var10 != null && var7.accessory == HeadCoinLayout.Accessory.HALO) {
                        this.glowEntries.add(new ChinaHatModule.GlowEntry(var10, var7.radius, var8));
                        this.lastGlowFrame = this.frameCounter;
                     }
                  }
               } else if (var7.kippah && var7.model != null) {
                  var1.registerSoftPipelines(var7.model, var7.radius, var7.height, var7.offset, var7.top, var7.rim, var7.outline, var7.lit);
               } else if (!var7.kippah) {
                  var1.drawBurst(var7.model, var7.base, var7.radius, var7.height, var7.offset, var7.top, var7.rim, var7.outline);
               }
            }

            for (ChinaHatModule.RenderEntry var12 : this.renderEntries) {
               if (var12.fire != null && var12.fire.textureGeneration == this.frameCounter) {
                  var1.drawDisc(
                     var12.fire, var12.fire.pipeline, var12.radius, var12.horns.size(), var12.horns.heat(), var12.horns.fire(), var12.horns.core(), var3
                  );
               }
            }
         }
      }
   }

   public boolean hasActiveGlow() {
      return !this.isEnabled() ? false : this.isOperational() && this.renderLevel == minecraft.level && this.lastGlowFrame == this.frameCounter;
   }

   public void migrateLegacyMode(EffectsRenderer var1, EffectBatch var2) {
      if (this.isEnabled()) {
         if (this.hasActiveGlow()) {
            double var3 = TickTimer.getTime();

            for (ChinaHatModule.GlowEntry var6 : this.glowEntries) {
               var1.drawEntityEffect(var6.head, var6.scale, var6.opacity, var3);
            }

            for (ChinaHatModule.RenderEntry var9 : this.renderEntries) {
               if (var9.fire != null && var9.fire.textureGeneration == this.frameCounter) {
                  var1.drawChinaHatAt(var9.fire.pipeline, var9.radius);
               }
            }

            for (ChinaHatModule.RenderEntry var10 : this.renderEntries) {
               if (var10.fire != null && var10.fire.textureGeneration == this.frameCounter) {
                  var1.emitParticles(var10.fire, var10.horns.fire(), var10.horns.core(), 0.6);
               }
            }

            var2.flush();
         }
      }
   }

   private record GlowEntry(Matrix4f head, double scale, double opacity) {
   }

   public record HatVariant(
      boolean kippah,
      double radius,
      double height,
      double offset,
      int top,
      int rim,
      boolean outline,
      HeadCoinLayout.Accessory accessory,
      ChinaHatModule.HornStyle horns,
      boolean dominus
   ) {
      public HatVariant(boolean var1, double var2, double var4, double var6, int var8, int var9, boolean var10) {
         this(var1, var2, var4, var6, var8, var9, var10, null, null, false);
      }

      public HatVariant(boolean var1, double var2, double var4, double var6, int var8, int var9, boolean var10, HeadCoinLayout.Accessory var11) {
         this(var1, var2, var4, var6, var8, var9, var10, var11, null, false);
      }

      public HatVariant(
         boolean var1, double var2, double var4, double var6, int var8, int var9, boolean var10, HeadCoinLayout.Accessory var11, ChinaHatModule.HornStyle var12
      ) {
         this(var1, var2, var4, var6, var8, var9, var10, var11, var12, false);
      }

      public double rise() {
         return this.offset + this.height;
      }

      public String label() {
         return this.dominus ? "Dominus" : (this.horns != null ? "Horns" : (this.accessory != null ? this.accessory.LABEL : (this.kippah ? "Kippah" : "Cone")));
      }
   }

   public record HornStyle(int horn, int fire, int core, double size, double heat) {
   }

   private record RenderEntry(
      boolean kippah,
      Vec3 base,
      EntityRenderState model,
      double radius,
      double height,
      double offset,
      int top,
      int rim,
      boolean outline,
      HeadCoinLayout.Accessory accessory,
      ChinaHatModule.HornStyle horns,
      ChinaHatFireParticles fire,
      boolean dominus,
      double lit
   ) {
      RenderEntry(boolean var1, Vec3 var2, EntityRenderState var3, double var4, double var6, double var8, int var10, int var11, boolean var12, double var13) {
         this(var1, var2, var3, var4, var6, var8, var10, var11, var12, null, null, null, false, var13);
      }
   }
}
