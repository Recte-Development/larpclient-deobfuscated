package com.larpclient.module.render;

import com.larpclient.config.GradientOutlineSettings;
import com.larpclient.module.misc.BooleanSetting;
import com.larpclient.module.misc.Category;
import com.larpclient.module.misc.ColorSetting;
import com.larpclient.module.misc.Module;
import com.larpclient.module.misc.NumberSetting;
import com.larpclient.module.misc.Setting;
import com.larpclient.render.entity.EspRenderer;
import com.larpclient.render.entity.GradientChamsRenderer;
import com.larpclient.render.misc.EffectBatch;
import com.larpclient.render.model.ModelVisualRenderer;
import com.larpclient.util.misc.LarpLog;
import com.larpclient.util.render.ColorUtil;
import com.mojang.blaze3d.vertex.PoseStack;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

public class ChamsModule extends Module {
   private final BooleanSetting players = this.addSetting(new BooleanSetting("Players", "Apply to players", true));
   private final BooleanSetting mobs = this.addSetting(new BooleanSetting("Mobs", "Apply to mobs", false));
   private final NumberSetting red = this.addSetting(new NumberSetting("Red", "Red color component", 1.0, 0.0, 1.0, 0.05));
   private final NumberSetting green = this.addSetting(new NumberSetting("Green", "Green color component", 0.3, 0.0, 1.0, 0.05));
   private final NumberSetting blue = this.addSetting(new NumberSetting("Blue", "Blue color component", 0.3, 0.0, 1.0, 0.05));
   private final BooleanSetting themeColors = this.addSetting(new BooleanSetting("Theme Colors", "Use the live Larp accent instead of RGB", true));
   private final NumberSetting range = this.addSetting(new NumberSetting("Range", "Maximum material distance", 128.0, 16.0, 512.0, 16.0));
   private final NumberSetting visibleOpacity = this.addSetting(
      new NumberSetting("Visible Opacity", "Material strength on visible surfaces", 0.3, 0.0, 1.0, 0.05)
   );
   private final NumberSetting hiddenOpacity = this.addSetting(new NumberSetting("Hidden Opacity", "Material strength behind walls", 0.55, 0.0, 1.0, 0.05));
   private final ColorSetting hiddenColor = this.addSetting(new ColorSetting("Hidden Color", "Color of occluded surfaces", -1077331));
   private final BooleanSetting throughWalls = this.addSetting(new BooleanSetting("Through Walls", "Draw the occluded material pass", true));
   private final BooleanSetting outline = this.addSetting(new BooleanSetting("Outline", "Add a vanilla silhouette outline", false));
   private final NumberSetting maxEntities = this.addSetting(new NumberSetting("Max Entities", "Nearest models drawn", 32.0, 8.0, 128.0, 8.0));
   public final GradientOutlineSettings outlineSettings;
   private final BooleanSetting renderSelf;
   private final BooleanSetting teamColors;
   private final GradientChamsRenderer renderer;
   private static ChamsModule instance;
   private List<GradientChamsRenderer.ChamsEntity> entities;
   private GradientOutlineSettings.Snapshot outlineSnapshot;
   private Object renderLevel;
   private Object renderPlayer;
   private boolean drawThroughWalls;

   public ChamsModule() {
      super("Chams", "Animated gradient outlines, fill and glow on entities", Category.RENDER, -1);
      ChamsModule var1 = this;
      this.outlineSettings = new GradientOutlineSettings(var1x -> var1.addSetting(var1x));
      this.renderSelf = this.addSetting(new BooleanSetting("Render Self", "Include your model in third person", false));
      this.teamColors = this.addSetting(new BooleanSetting("Team Colors", "Use entity team colors instead of the gradient", false));
      this.renderer = new GradientChamsRenderer("Chams");
      this.entities = List.of();
      instance = this;

      for (Setting var2 : List.of(this.red, this.green, this.blue, this.themeColors, this.visibleOpacity, this.hiddenOpacity, this.hiddenColor, this.outline)) {
         var2.visibleWhen(() -> false);
      }

      this.outlineSettings.color1.visibleWhen(() -> !this.teamColors.getValue());
      this.outlineSettings.color2.visibleWhen(() -> !this.teamColors.getValue());
   }

   public static ChamsModule getInstance() {
      return instance;
   }

   @Override
   public void onEnable() {
      LarpLog.info("Chams", "Echo gradient enabled; nearest model surfaces, stable wall visibility and native skin retained");
   }

   public List<Setting<?>> getTargetSettings() {
      return List.of(this.players, this.mobs, this.renderSelf);
   }

   public List<Setting<?>> shouldRenderMobs() {
      return List.of(this.teamColors, this.outlineSettings.color1, this.outlineSettings.color2);
   }

   public List<Setting<?>> getGreen() {
      return List.of(this.throughWalls);
   }

   public List<Setting<?>> getRangeSettings() {
      return List.of(this.range, this.maxEntities);
   }

   public boolean shouldRenderPlayers() {
      return !this.isEnabled() ? false : this.isOperational() && this.players.getValue();
   }

   public boolean shouldRenderMobs() {
      return !this.isEnabled() ? false : this.isOperational() && this.mobs.getValue();
   }

   public float getRed() {
      return this.red.getValue().floatValue();
   }

   public float getGreen() {
      return this.green.getValue().floatValue();
   }

   public float getBlue() {
      return this.blue.getValue().floatValue();
   }

   public int shouldRenderPlayers(LivingEntity var1) {
      return this.isEnabled() ? 0 : 0;
   }

   public void extractEntities(float var1) {
      if (this.isEnabled()) {
         this.entities = List.of();
         this.outlineSnapshot = null;
         if (this.isRuntimeActive() && this.isInGame()) {
            this.renderLevel = minecraft.level;
            this.renderPlayer = minecraft.player;
            this.drawThroughWalls = this.throughWalls.getValue();
            this.outlineSnapshot = this.outlineSettings.snapshot(this.teamColors.getValue());
            ArrayList var2 = new ArrayList();
            if (this.renderSelf.getValue() && !minecraft.options.getCameraType().isFirstPerson() && minecraft.player.isAlive()) {
               var2.add(new GradientChamsRenderer.ChamsEntity(ModelVisualRenderer.extractEntity(minecraft.player, var1), this.getEntityColor(minecraft.player)));
            }

            for (Entity var4 : EspRenderer.nearbyEntities(
               minecraft.level.entitiesForRendering(),
               minecraft.player,
               this.range.getValue(),
               this.maxEntities.getValue().intValue() - var2.size(),
               var1x -> var1x instanceof LivingEntity var2x && var2x.isAlive() && var1x instanceof Player ? this.players.getValue() : this.mobs.getValue()
            )) {
               var2.add(
                  new GradientChamsRenderer.ChamsEntity(
                     ModelVisualRenderer.extractEntity(var4, var1),
                     ColorUtil.multiplyAlpha(this.getEntityColor(var4), EspRenderer.distanceFade(minecraft.player.distanceTo(var4), this.range.getValue()))
                  )
               );
            }

            this.entities = List.copyOf(var2);
         }
      }
   }

   public void reset(PoseStack var1, EffectBatch var2, float var3) {
      if (this.isEnabled()) {
         if (this.isRuntimeActive()
            && this.isInGame()
            && this.renderLevel == minecraft.level
            && this.renderPlayer == minecraft.player
            && this.outlineSnapshot != null) {
            var2.flush();
            this.renderer.renderEntities(this.entities, var1, this.outlineSnapshot, this.drawThroughWalls);
         } else {
            this.reset();
         }
      }
   }

   private int getEntityColor(Entity var1) {
      return this.teamColors.getValue() ? 0xFF000000 | var1.getTeamColor() : -1;
   }

   public void reset() {
      this.entities = List.of();
      this.outlineSnapshot = null;
      this.renderLevel = this.renderPlayer = null;
      this.renderer.close();
      this.outlineSettings.resetPhase();
   }

   @Override
   public void onDisable() {
      this.reset();
   }

   @Override
   public void onTick() {
      if (!this.isInGame() || this.renderLevel != null && (this.renderLevel != minecraft.level || this.renderPlayer != minecraft.player)) {
         this.reset();
      }
   }
}
