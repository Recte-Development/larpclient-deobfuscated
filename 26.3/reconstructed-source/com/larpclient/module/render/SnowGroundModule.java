package com.larpclient.module.render;

import com.larpclient.module.misc.BooleanSetting;
import com.larpclient.module.misc.Category;
import com.larpclient.module.misc.ModeSetting;
import com.larpclient.module.misc.Module;
import com.larpclient.module.misc.NumberSetting;
import com.larpclient.render.overlay.PostProcessing;
import com.larpclient.render.particle.SnowSimulation;
import com.larpclient.render.world.EffectsRenderer;
import com.larpclient.render.world.TerrainMaterials;
import com.larpclient.util.data.TickTimer;
import com.larpclient.util.data.TrailBuffer;
import com.larpclient.util.misc.LarpLog;
import java.nio.ByteBuffer;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction.Axis;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap.Types;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;

public final class SnowGroundModule extends Module {
   private static SnowGroundModule instance;
   public final NumberSetting coverage = this.addNumberSetting("Coverage", "Snow coverage on exposed ground and roofs, percent", 100.0, 0.0, 100.0, 5.0);
   public final NumberSetting depth = this.addNumberSetting("Depth", "Soft drift relief and snow along upper edges, percent", 75.0, 0.0, 100.0, 5.0);
   public final NumberSetting sparkle = this.addNumberSetting("Sparkle", "Small sunlit ice crystals, percent", 30.0, 0.0, 100.0, 5.0);
   public final BooleanSetting snowfall = this.addSetting(new BooleanSetting("Snowfall", "Falling snow even in clear server weather", true));
   public final NumberSetting density = this.addNumberSetting("Density", "Falling snow density, percent", 85.0, 0.0, 100.0, 5.0);
   public final NumberSetting wind = this.addNumberSetting("Wind", "Sideways drift and rolling gusts, percent", 35.0, 0.0, 100.0, 5.0);
   public final NumberSetting radius = this.addNumberSetting("Radius", "Snowfall radius around the camera in blocks", 24.0, 12.0, 40.0, 2.0);
   public final ModeSetting quality = this.addSetting(new ModeSetting("Quality", "Snow detail and bounded flake count", "High", "Low", "Medium", "High"));
   public final BooleanSetting footsteps = this.addSetting(new BooleanSetting("Footsteps", "Clear powder beneath you and leave a soft walking trail", true));
   public final NumberSetting trailTime = this.addNumberSetting("Trail Time", "Seconds before walking trails fill back in", 12.0, 2.0, 30.0, 1.0);
   private final TrailBuffer trailBuffer = new TrailBuffer();
   private Vec3 lastPlayerPos = Vec3.ZERO;
   private final SnowSimulation simulation = new SnowSimulation();
   private Object level;
   private Object player;
   private int suppressedReloadCount = -1;
   private int lastReloadCount = -1;

   private NumberSetting addNumberSetting(String var1, String var2, double var3, double var5, double var7, double var9) {
      return this.addSetting(new NumberSetting(var1, var2, var3, var5, var7, var9));
   }

   public SnowGroundModule() {
      super("Snow Ground", "Deep powder, wind-shaped snow drifts and dense falling snow", Category.RENDER, -1);
      instance = this;
      this.density.visibleWhen(this.snowfall::getValue);
      this.wind.visibleWhen(this.snowfall::getValue);
      this.radius.visibleWhen(this.snowfall::getValue);
      this.trailTime.visibleWhen(this.footsteps::getValue);
      this.footsteps.onChange(var1 -> this.trailBuffer.clear());
      this.snowfall.onChange(var1 -> this.simulation.reset());
      this.quality.onChange(var1 -> this.simulation.reset());
      this.radius.onChange(var1 -> this.simulation.reset());
   }

   public static boolean isActive() {
      return instance != null && instance.isEnabled() ? instance != null && instance.isRuntimeActive() : false;
   }

   public static SnowGroundModule getQualityLevel() {
      return instance;
   }

   public int getQualityLevel() {
      return this.quality.isMode("High") ? 2 : (this.quality.isMode("Medium") ? 1 : 0);
   }

   public static int flakeCap(int var0) {
      return var0 >= 2 ? 3200 : (var0 == 1 ? 1600 : 800);
   }

   public static boolean isSnowfallActive() {
      if (instance != null && instance.isEnabled()) {
         SnowGroundModule var0 = instance;
         return var0 != null
            && isActive()
            && var0.suppressedReloadCount != PostProcessing.reloadCount
            && var0.snowfall.getValue()
            && var0.density.getValue() > 0.0
            && !NoWeatherModule.isActive()
            && !RemovalsModule.isRemovalEnabled("weather");
      } else {
         return false;
      }
   }

   public SnowSimulation getSimulation() {
      return this.simulation;
   }

   public void reset() {
      this.simulation.reset();
      this.trailBuffer.clear();
      this.lastPlayerPos = Vec3.ZERO;
      this.level = this.player = null;
   }

   public TrailBuffer getTrailBuffer() {
      return this.trailBuffer;
   }

   public void writeTrail(ByteBuffer var1, Vec3 var2, double var3, boolean var5) {
      if (this.isEnabled()) {
         this.trailBuffer
            .size(
               var1,
               var2,
               this.lastPlayerPos,
               var3,
               this.trailTime.getValue(),
               var5 && isActive() && this.footsteps.getValue() && this.level == minecraft.level && this.player == minecraft.player
            );
      }
   }

   public void suppressUntilReload() {
      this.reset();
      this.suppressedReloadCount = PostProcessing.reloadCount;
   }

   public static boolean hasVisibleSnow() {
      return instance != null && instance.isEnabled() ? isSnowfallActive() && instance.simulation.getVisibleCount() > 0 : false;
   }

   @Override
   public void onEnable() {
      this.reset();
      TerrainMaterials.onNativeActivated();
      TerrainMaterials.refreshChunks();
      LarpLog.info(
         "SnowGround",
         "Enabled powder and snowfall; quality={}, flake cap={}, radius={} blocks, footsteps={}, trail={} seconds",
         this.quality.getValue(),
         flakeCap(this.getQualityLevel()),
         this.radius.getValue(),
         this.footsteps.getValue(),
         this.trailTime.getValue()
      );
   }

   @Override
   public void onDisable() {
      this.reset();
      TerrainMaterials.refreshChunks();
      TerrainMaterials.isNativeHookUnavailable();
   }

   @Override
   public String getStatus() {
      return this.isRuntimeActive() && TerrainMaterials.isHookDisabled() ? "Material fallback" : "";
   }

   @Override
   public void onTick() {
      int var3 = PostProcessing.reloadCount;
      int var2 = this.lastReloadCount;
      int var10000 = Integer.compare(var2, var3);
      if (((var10000 | -var10000) >>> 31 ^ 1) == 0) {
         this.reset();
         this.lastReloadCount = PostProcessing.reloadCount;
      }

      if (!this.isInGame()) {
         this.reset();
      } else {
         if (this.level != minecraft.level || this.player != minecraft.player) {
            this.reset();
            this.level = minecraft.level;
            this.player = minecraft.player;
         }

         if (!minecraft.isPaused()) {
            Vec3 var1 = new Vec3(minecraft.player.getX(), minecraft.player.getBoundingBox().minY, minecraft.player.getZ());
            this.lastPlayerPos = var1;
            int var10 = this.footsteps.getValue();
            boolean var5 = false;
            byte var4 = (byte)var10;
            var10 = Integer.signum(var4);
            if (((var10 | -var10) >>> 31 ^ 1) == 0) {
               int var15;
               label37: {
                  var12 = this.trailBuffer;
                  if (minecraft.player.onGround()) {
                     var15 = minecraft.player.isPassenger();
                     boolean var7 = false;
                     byte var6 = var15;
                     var15 = Integer.signum(var6);
                     if ((var15 | -var15) >>> 31 == 0 && !minecraft.player.isSpectator() && !minecraft.player.isInWater()) {
                        boolean var9 = false;
                        byte var8 = minecraft.player.getAbilities().flying;
                        var15 = Integer.signum(var8);
                        if ((var15 | -var15) >>> 31 == 0) {
                           var15 = 1;
                           break label37;
                        }
                     }
                  }

                  var15 = 0;
               }

               var12.record_(var1, (boolean)var15, TickTimer.getTickTime(), this.trailTime.getValue());
            } else {
               this.trailBuffer.clear();
            }

            if (!isSnowfallActive()) {
               this.simulation.reset();
            } else {
               this.simulation
                  .tick(
                     minecraft.gameRenderer.mainCamera().position(),
                     new SnowSimulation.Options(
                        this.radius.getValue(), this.density.getValue() / 100.0, this.wind.getValue() / 100.0, flakeCap(this.getQualityLevel())
                     ),
                     (var0, var1x) -> {
                        if (!minecraft.level.hasChunk(var0 >> 4, var1x >> 4)) {
                           return SnowSimulation.RoofSample.UNAVAILABLE;
                        }

                        int var2x = minecraft.level.getHeight(Types.MOTION_BLOCKING, var0, var1x) - 1;
                        BlockPos var3x = new BlockPos(var0, var2x, var1x);
                        BlockState var4x = minecraft.level.getBlockState(var3x);
                        VoxelShape var5x = var4x.getCollisionShape(minecraft.level, var3x);
                        double var6x = var2x + (!var5x.isEmpty() && var4x.getFluidState().isEmpty() ? var5x.max(Axis.Y) : 1.0);
                        int var8x = minecraft.level.getMaxLocalRawBrightness(var3x.above());
                        return new SnowSimulation.RoofSample(var6x, var8x / 15.0);
                     }
                  );
            }
         }
      }
   }

   public void prepareRender(float var1, CameraRenderState var2) {
      if (this.isEnabled()) {
         if (isActive() && this.level == minecraft.level && this.player == minecraft.player) {
            this.lastPlayerPos = minecraft.player.getEyePosition(var1).subtract(0.0, minecraft.player.getEyeHeight(), 0.0);
            if (!isSnowfallActive()) {
               this.simulation.reset();
            } else {
               this.simulation.prepareRender(var2.pos, var1);
            }
         } else {
            this.reset();
         }
      }
   }

   public void isSnowfallActive(EffectsRenderer var1) {
      if (this.isEnabled()) {
         if (isActive() && isSnowfallActive() && this.level == minecraft.level && this.player == minecraft.player) {
            this.simulation.render(var1);
         }
      }
   }
}
