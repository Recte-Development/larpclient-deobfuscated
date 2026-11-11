package com.larpclient.render.world;

import com.larpclient.gui.theme.Theme;
import com.larpclient.module.misc.BooleanSetting;
import com.larpclient.module.misc.ColorSetting;
import com.larpclient.module.misc.ModeSetting;
import com.larpclient.module.misc.Module;
import com.larpclient.module.misc.NumberSetting;
import com.larpclient.module.misc.Setting;
import com.larpclient.render.overlay.PostProcessing;
import com.larpclient.render.overlay.ShapeOutline;
import com.larpclient.util.data.TickTimer;
import com.larpclient.util.misc.LarpLog;
import com.larpclient.util.misc.PerfProfiler;
import com.larpclient.util.world.NativePlacement;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.function.Consumer;
import net.fabricmc.fabric.api.client.rendering.v1.RenderStateDataKey;
import net.fabricmc.fabric.api.client.rendering.v1.level.LevelExtractionContext;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.state.level.LevelRenderState;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.Shapes;

public final class PlacementHighlight {
   private static final ShapeOutline uNIT_BOX = ShapeOutline.build(Shapes.block());
   private final Module module;
   private final BooleanSetting enabled = new BooleanSetting("Highlight", "Show the next placement with a soft block outline", true);
   private final ModeSetting colorMode = new ModeSetting("Highlight Color Mode", "Follow your theme or choose a custom color", "Theme", "Theme", "Custom");
   private final ColorSetting color = new ColorSetting("Highlight Color", "Custom placement highlight color", Theme.accent);
   private final NumberSetting opacity = percentSetting("Highlight Opacity", "Outline visibility", 0.85, 0.1, 1.0, 0.05);
   private final NumberSetting thickness = new NumberSetting("Highlight Thickness", "Outline width in blocks", 0.015, 0.005, 0.04, 0.005) {
      @Override
      public String getValueAsString() {
         return String.format(Locale.ROOT, "%.3f", this.getValue());
      }
   };
   private final BooleanSetting fillEnabled = new BooleanSetting("Highlight Fill", "Subtle tint inside the placement box", true);
   private final NumberSetting fillOpacity = percentSetting("Highlight Fill Opacity", "Keep the scene visible through the fill", 0.08, 0.01, 0.3, 0.01);
   private final NumberSetting glow = percentSetting("Highlight Glow", "Soft edge halo and shared quality-controlled bloom", 0.65, 0.0, 1.5, 0.05);
   private final NumberSetting holdSeconds;
   private final NumberSetting fadeSeconds;
   private final List<Setting<?>> settings;
   private final BlockHighlightFader trail = new BlockHighlightFader();
   private final RenderStateDataKey<PlacementHighlight.HighlightRenderState> stateKey;
   private BlockPos targetPos;
   private Object targetLevel;
   private Object targetPlayer;
   private Object renderLevel;
   private Object renderPlayer;
   private int targetTick;
   private int renderEpoch = -1;
   private Vec3 lastCameraPos;

   public PlacementHighlight(Module var1, Consumer<Setting<?>> var2) {
      this(var1, var2, -1.0, -1.0);
   }

   public PlacementHighlight(Module var1, Consumer<Setting<?>> var2, double var3, double var5) {
      this.module = var1;
      this.holdSeconds = var3 < 0.0 ? null : secondsSetting("Highlight Hold", "Seconds to retain the previous placement box", var3, 0.0, 3.0);
      this.fadeSeconds = var5 < 0.0 ? null : secondsSetting("Highlight Fade", "Seconds for the previous box to fade out", var5, 0.1, 2.0);
      ArrayList var7 = new ArrayList<>(
         List.of(this.enabled, this.colorMode, this.color, this.opacity, this.thickness, this.fillEnabled, this.fillOpacity, this.glow)
      );
      if (this.holdSeconds != null) {
         var7.add(this.holdSeconds);
         var7.add(this.fadeSeconds);
      }

      this.settings = List.copyOf(var7);
      this.stateKey = RenderStateDataKey.create(() -> var1.getName() + " placement highlight");
      this.settings.forEach(var2);

      for (Setting var9 : this.settings) {
         if (var9 != this.enabled) {
            var9.visibleWhen(this.enabled::getValue);
         }
      }

      this.color.visibleWhen(() -> this.enabled.getValue() && this.colorMode.isMode("Custom"));
      this.fillOpacity.visibleWhen(() -> this.enabled.getValue() && this.fillEnabled.getValue());
      this.enabled.onChange(var1x -> this.clear());
   }

   public boolean isEnabled() {
      return this.enabled.getValue();
   }

   public String getLabel() {
      return this.module.getName() + " highlight";
   }

   public List<Setting<?>> getSettings() {
      return this.settings;
   }

   public void setTarget(BlockPos var1) {
      Minecraft var2 = Minecraft.getInstance();
      this.targetPos = this.enabled.getValue() && var1 != null ? var1.immutable() : null;
      this.targetLevel = var2.level;
      this.targetPlayer = var2.player;
      this.targetTick = var2.player == null ? 0 : var2.player.tickCount;
   }

   public void clear() {
      this.targetPos = null;
      this.targetLevel = this.targetPlayer = this.renderLevel = this.renderPlayer = null;
      this.lastCameraPos = null;
      this.trail.clear();
   }

   public void reset() {
      this.clear();
      LarpLog.info(
         this.module.getName(),
         "Placement highlight {}; theme/custom color, world-fixed fades, shared depth-tested glow",
         this.enabled.getValue() ? "enabled" : "disabled"
      );
   }

   public void extract(LevelExtractionContext var1) {
      LevelRenderState var2 = var1.levelState();
      var2.setData(this.stateKey, null);
      Minecraft var3 = Minecraft.getInstance();
      if (this.module.isRuntimeActive()
         && this.enabled.getValue()
         && var3.player != null
         && var3.player.isAlive()
         && var3.gui.screen() == null
         && !var3.gameRenderer.gameRenderState().guiRenderState.isHudHidden
         && var3.isWindowActive()) {
         Vec3 var4 = var1.camera().position();
         int var5 = this.lastCameraPos != null && this.lastCameraPos.distanceTo(var4) > 8.0 ? 1 : 0;
         if (this.renderLevel != var1.level() || this.renderPlayer != var3.player || this.renderEpoch != PostProcessing.reloadCount || var5 != 0) {
            this.trail.clear();
            if (var5 != 0) {
               this.targetPos = null;
            }

            this.renderLevel = var1.level();
            this.renderPlayer = var3.player;
            this.renderEpoch = PostProcessing.reloadCount;
         }

         this.lastCameraPos = var4;
         BlockPos var6 = NativePlacement.isSameContext(this.targetLevel, var3.level, this.targetPlayer, var3.player, this.targetTick, var3.player.tickCount)
            ? this.targetPos
            : null;
         List var7 = this.trail
            .updateWithTimings(
               var6,
               TickTimer.getTime(),
               this.holdSeconds == null ? 0.06 : this.holdSeconds.getValue(),
               this.fadeSeconds == null ? 0.18 : this.fadeSeconds.getValue()
            );
         if (!var7.isEmpty()) {
            var2.setData(
               this.stateKey,
               new PlacementHighlight.HighlightRenderState(
                  var7,
                  this.colorMode.isMode("Theme") ? Theme.accent : this.color.getValue(),
                  this.opacity.getValue(),
                  this.thickness.getValue(),
                  this.fillEnabled.getValue() ? this.fillOpacity.getValue() : 0.0,
                  this.glow.getValue()
               )
            );
         }
      } else {
         this.clear();
      }
   }

   private PlacementHighlight.HighlightRenderState setTarget(LevelRenderState var1) {
      return (PlacementHighlight.HighlightRenderState)var1.getData(this.stateKey);
   }

   public boolean reset(LevelRenderState var1) {
      PlacementHighlight.HighlightRenderState var2 = this.setTarget(var1);
      return this.module.isRuntimeActive() && this.enabled.getValue() && var2 != null && var2.glow > 0.0;
   }

   public void render(EffectsRenderer var1, boolean var2, LevelRenderState var3) {
      PlacementHighlight.HighlightRenderState var4 = this.setTarget(var3);
      if (this.module.isRuntimeActive() && this.enabled.getValue() && var4 != null && (!var2 || !(var4.glow <= 0.0))) {
         long var5 = PerfProfiler.startTimer();

         for (BlockHighlightFader.FadedBlock var8 : var4.boxes) {
            BoxRenderer.renderBox(
               var1, var2, uNIT_BOX, new AABB(var8.pos()).inflate(0.002), var8.alpha(), var4.color, var4.opacity, var4.width, var4.fillOpacity, var4.glow
            );
         }

         PerfProfiler.endTimer(this.module.getName() + (var2 ? " highlight glow" : " highlight base"), var5);
      }
   }

   private static NumberSetting secondsSetting(String var0, String var1, double var2, double var4, double var6) {
      return new NumberSetting(var0, var1, var2, var4, var6, 0.05) {
         @Override
         public String getValueAsString() {
            return this.getEditValueAsString() + " s";
         }
      };
   }

   private static NumberSetting percentSetting(String var0, String var1, double var2, double var4, double var6, double var8) {
      return new NumberSetting(var0, var1, var2, var4, var6, var8) {
         @Override
         public String getValueAsString() {
            return this.getEditValueAsString() + "%";
         }

         @Override
         public String getEditValueAsString() {
            return Long.toString(Math.round(this.getValue() * 100.0));
         }

         @Override
         public void setFromEditString(String var1) {
            this.getDisplayValue(Double.parseDouble(var1) / 100.0);
         }
      };
   }

   private record HighlightRenderState(List<BlockHighlightFader.FadedBlock> boxes, int color, double opacity, double width, double fillOpacity, double glow) {
   }

   public interface PlacementHighlightHolder {
      PlacementHighlight placementHighlight();
   }
}
