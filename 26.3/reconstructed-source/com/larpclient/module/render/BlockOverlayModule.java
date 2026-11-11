package com.larpclient.module.render;

import com.larpclient.gui.theme.Theme;
import com.larpclient.module.misc.BooleanSetting;
import com.larpclient.module.misc.Category;
import com.larpclient.module.misc.ColorSetting;
import com.larpclient.module.misc.ModeSetting;
import com.larpclient.module.misc.Module;
import com.larpclient.module.misc.NumberSetting;
import com.larpclient.module.misc.Setting;
import com.larpclient.render.misc.VisualsRenderer;
import com.larpclient.render.overlay.PostProcessing;
import com.larpclient.render.overlay.ShapeOutline;
import com.larpclient.render.world.BoxAnimator;
import com.larpclient.render.world.BoxRenderer;
import com.larpclient.render.world.EffectsRenderer;
import com.larpclient.util.data.TickTimer;
import com.larpclient.util.misc.LarpLog;
import com.larpclient.util.misc.PerfProfiler;
import java.util.List;
import java.util.Locale;
import net.fabricmc.fabric.api.client.rendering.v1.RenderStateDataKey;
import net.fabricmc.fabric.api.client.rendering.v1.level.LevelExtractionContext;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.state.level.BlockOutlineRenderState;
import net.minecraft.client.renderer.state.level.LevelRenderState;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.HitResult.Type;
import net.minecraft.world.phys.shapes.VoxelShape;

public final class BlockOverlayModule extends Module {
   private static BlockOverlayModule instance;
   private final NumberSetting transition = this.addSetting(
      new NumberSetting("Transition", "Time to glide to another block, in milliseconds; 0 is instant", 240.0, 0.0, 300.0, 10.0)
   );
   private final ModeSetting colorMode = this.addSetting(
      new ModeSetting("Color Mode", "Use your interface accent or a custom color", "Theme", "Theme", "Custom")
   );
   private final ColorSetting color = this.addSetting(new ColorSetting("Color", "Custom highlight color", -1381654));
   private final NumberSetting outlineOpacity = this.addSetting(createPercentSetting("Outline Opacity", "Outline visibility", 0.85, 0.1, 1.0, 0.05));
   private final NumberSetting outlineThickness = this.addSetting(new NumberSetting("Outline Thickness", "Stroke width in blocks", 0.015, 0.005, 0.05, 0.005) {
      @Override
      public String getValueAsString() {
         return String.format(Locale.ROOT, "%.3f", this.getValue());
      }
   });
   private final BooleanSetting fill = this.addSetting(new BooleanSetting("Fill", "Subtle tint over the selected shape", true));
   private final NumberSetting fillOpacity = this.addSetting(createPercentSetting("Fill Opacity", "Keep the block texture visible", 0.15, 0.01, 0.3, 0.01));
   private final NumberSetting glow = this.addSetting(createPercentSetting("Glow", "Soft edge halo and shared quality-controlled bloom", 1.5, 0.0, 1.5, 0.05));
   private final BoxAnimator animator = new BoxAnimator();
   private Object lastLevel;
   private Object lastPlayer;
   private Vec3 lastCameraPos;
   private VoxelShape lastShape;
   private ShapeOutline outline;
   private BlockPos selectedPos;
   private int lastReloadCount = -1;
   private boolean failed;
   private boolean warnedComplexShape;
   private static final RenderStateDataKey<BlockOverlayModule.OverlayState> stateKey = RenderStateDataKey.create(() -> "Larp block overlay");

   public BlockOverlayModule() {
      super("Block Overlay", "Highlight the block you aim at", Category.VISUALS, -1);
      instance = this;
      this.color.visibleWhen(() -> this.colorMode.isMode("Custom"));
      this.fillOpacity.visibleWhen(this.fill::getValue);
   }

   private static NumberSetting createPercentSetting(String var0, String var1, double var2, double var4, double var6, double var8) {
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

   public static BlockOverlayModule getInstance() {
      return instance;
   }

   public List<Setting<?>> getAppearanceSettings() {
      return List.of(this.colorMode, this.color, this.outlineOpacity, this.outlineThickness, this.fill, this.fillOpacity, this.glow);
   }

   public List<Setting<?>> getAnimationSettings() {
      return List.of(this.transition);
   }

   @Override
   public void onEnable() {
      this.reset();
      LarpLog.info("BlockOverlay", "Selection overlay enabled; cached native shapes, interruptible {} ms transition, shared glow", this.transition.getValue());
   }

   @Override
   public void onDisable() {
      this.reset();
   }

   public void reset() {
      this.animator.reset();
      this.outline = null;
      this.lastShape = null;
      this.selectedPos = null;
      this.lastLevel = this.lastPlayer = null;
      this.lastCameraPos = null;
      this.failed = this.warnedComplexShape = (boolean)0;
   }

   public void extract(LevelExtractionContext var1, HitResult var2) {
      if (this.isEnabled()) {
         LevelRenderState var3 = var1.levelState();
         var3.setData(stateKey, null);
         if (!this.isRuntimeActive()) {
            this.reset();
         } else {
            Minecraft var4 = getMc();
            if (var4.gui.screen() == null
               && !var4.gameRenderer.gameRenderState().guiRenderState.isHudHidden
               && var4.isWindowActive()
               && var4.player != null
               && var4.player.isAlive()) {
               Vec3 var5 = var1.camera().position();
               if (this.lastLevel != var1.level()
                  || this.lastPlayer != var4.player
                  || this.lastReloadCount != PostProcessing.reloadCount
                  || this.lastCameraPos != null && this.lastCameraPos.distanceTo(var5) > 8.0) {
                  this.reset();
                  this.lastLevel = var1.level();
                  this.lastPlayer = var4.player;
                  this.lastReloadCount = PostProcessing.reloadCount;
               }

               this.lastCameraPos = var5;
               if (!this.failed) {
                  try {
                     BlockOutlineRenderState var6 = var1.levelState().blockOutlineRenderState;
                     AABB var7 = null;
                     this.selectedPos = null;
                     if (var6 != null && var2 != null && var2.getType() == Type.BLOCK) {
                        VoxelShape var8 = var6.shape();
                        this.outline = this.getOutline(var8);
                        if (this.outline != null) {
                           this.selectedPos = var6.pos();
                           var7 = this.outline.bounds().move(this.selectedPos);
                        } else if (!this.warnedComplexShape) {
                           this.warnedComplexShape = (boolean)1;
                           LarpLog.info("BlockOverlay", "Empty or complex selection shape; keeping native outline (limits: {} edges, {} boxes)", 128, 32);
                        }
                     }

                     BoxAnimator.AnimatedBox var11 = this.animator.update(var7, TickTimer.getTime(), this.transition.getValue() / 1000.0);
                     BlockOverlayModule.OverlayState var9 = this.outline != null && var11.bounds() != null
                        ? new BlockOverlayModule.OverlayState(
                           this.selectedPos,
                           this.outline,
                           var11.bounds().inflate(0.002),
                           var11.alpha(),
                           this.colorMode.isMode("Theme") ? Theme.accent : this.color.getValue(),
                           this.outlineOpacity.getValue(),
                           this.outlineThickness.getValue(),
                           this.fill.getValue() ? this.fillOpacity.getValue() : 0.0,
                           this.glow.getValue()
                        )
                        : null;
                     var3.setData(stateKey, var9);
                  } catch (RuntimeException var10) {
                     this.failed = (boolean)1;
                     this.selectedPos = null;
                     LarpLog.error("BlockOverlay", "Selection extraction failed; native outline retained, retry on resource reload", var10);
                  }
               }
            } else {
               this.reset();
            }
         }
      }
   }

   private ShapeOutline getOutline(VoxelShape var1) {
      if (var1 != this.lastShape) {
         this.outline = ShapeOutline.build(var1);
         this.lastShape = var1;
      }

      return this.outline;
   }

   private static BlockOverlayModule.OverlayState shouldReplaceOutline(LevelRenderState var0) {
      return (BlockOverlayModule.OverlayState)var0.getData(stateKey);
   }

   public boolean shouldReplaceOutline(BlockPos var1, LevelRenderState var2) {
      if (!this.isEnabled()) {
         return false;
      }

      BlockOverlayModule.OverlayState var3 = shouldReplaceOutline(var2);
      return this.isRuntimeActive() && !this.failed && VisualsRenderer.isRenderingEnabled() && var3 != null && var1.equals(var3.selected);
   }

   public boolean hasGlow(LevelRenderState var1) {
      if (!this.isEnabled()) {
         return false;
      }

      BlockOverlayModule.OverlayState var2 = shouldReplaceOutline(var1);
      return this.isRuntimeActive() && var2 != null && var2.alpha > 0.001 && var2.glow > 0.0;
   }

   public void render(EffectsRenderer var1, boolean var2, LevelRenderState var3) {
      if (this.isEnabled()) {
         BlockOverlayModule.OverlayState var4 = shouldReplaceOutline(var3);
         if (this.isRuntimeActive() && var4 != null && !(var4.alpha < 0.001) && (!var2 || !(var4.glow <= 0.0))) {
            long var5 = PerfProfiler.startTimer();
            BoxRenderer.renderBox(var1, var2, var4.geometry, var4.bounds, var4.alpha, var4.color, var4.opacity, var4.width, var4.fillOpacity, var4.glow);
            PerfProfiler.endTimer(var2 ? "BlockOverlay glow" : "BlockOverlay base", var5);
         }
      }
   }

   private record OverlayState(
      BlockPos selected, ShapeOutline geometry, AABB bounds, double alpha, int color, double opacity, double width, double fillOpacity, double glow
   ) {
   }
}
