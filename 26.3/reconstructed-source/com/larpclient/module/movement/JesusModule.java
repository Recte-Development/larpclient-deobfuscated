package com.larpclient.module.movement;

import com.larpclient.module.exploit.AirStuckModule;
import com.larpclient.module.misc.Category;
import com.larpclient.module.misc.Module;
import com.larpclient.module.world.ScanPhase;
import com.larpclient.util.entity.PlayerInputUtil;
import com.larpclient.util.misc.LarpLog;
import java.util.Set;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.level.CollisionGetter;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.shapes.EntityCollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public final class JesusModule extends Module {
   private static JesusModule instance;
   private static final VoxelShape[] waterShapes = buildWaterShapes();
   private long lastSupportTick = Long.MIN_VALUE;
   private boolean loggedSupport;

   public JesusModule() {
      super("Jesus", "Walk on water surfaces; hold sneak to swim normally", Category.MOVEMENT, -1);
      instance = this;
   }

   @Override
   public Set<ScanPhase> getNetworkImpacts() {
      return Set.of(ScanPhase.dONE);
   }

   @Override
   public void onEnable() {
      this.lastSupportTick = Long.MIN_VALUE;
      this.loggedSupport = (boolean)0;
      LarpLog.debug("Jesus", "Enabled: water surfaces only; sneak and long falls retain normal swimming");
   }

   @Override
   public void onDisable() {
      this.lastSupportTick = Long.MIN_VALUE;
      this.loggedSupport = (boolean)0;
      LarpLog.debug("Jesus", "Released water support; native collision restored");
   }

   @Override
   public String getStatus() {
      if (!this.isRuntimeActive()) {
         return "Inactive";
      } else if (!this.canWalkOnWater()) {
         return "Paused; native water movement";
      } else {
         return this.lastSupportTick != Long.MIN_VALUE && minecraft.level.getGameTime() - this.lastSupportTick <= 1L
            ? "Walking on water"
            : "Approach the water surface from above";
      }
   }

   private boolean canWalkOnWater() {
      if (this.isInGame() && PlayerInputUtil.isInWorld()) {
         LocalPlayer var1 = minecraft.player;
         if (!var1.isSpectator()
            && !var1.isPassenger()
            && !var1.isFallFlying()
            && !var1.getAbilities().flying
            && !var1.noPhysics
            && !var1.isSleeping()
            && !var1.isMobilityRestricted()
            && !var1.onClimbable()
            && !var1.isInLava()
            && !var1.isShiftKeyDown()
            && !minecraft.options.keyShift.isDown()
            && !(var1.fallDistance > 3.0)) {
            FlyModule var2 = FlyModule.getInstance();
            AirStuckModule var3 = AirStuckModule.getInstance();
            ElytraMotionModule var4 = ElytraMotionModule.getInstance();
            return (var2 == null || !var2.isFlightActive()) && (var3 == null || !var3.isHolding()) && (var4 == null || !var4.isHolding());
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   public static VoxelShape modifyCollisionShape(BlockState var0, CollisionGetter var1, BlockPos var2, EntityCollisionContext var3, VoxelShape var4) {
      JesusModule var5 = instance;
      if (var5 != null && var5.isRuntimeActive()) {
         try {
            if (var1 == getMc().level
               && var3.getEntity() == minecraft.player
               && var0.getBlock() instanceof LiquidBlock
               && var4.isEmpty()
               && !var3.isDescending()
               && var5.canWalkOnWater()) {
               FluidState var6 = var0.getFluidState();
               if (var6.is(FluidTags.WATER) && !var1.getFluidState(var2.above()).is(FluidTags.WATER)) {
                  float var7 = var6.getHeight(var1, var2);
                  if (Float.isFinite(var7) && !(var7 <= 0.0F) && !(var7 > 1.0F)) {
                     int var8 = Math.round(var7 * 9.0F);
                     VoxelShape var9 = Math.abs(var7 - var8 / 9.0F) < 1.0E-6 ? waterShapes[var8] : Shapes.box(0.0, 0.0, 0.0, 1.0, var7, 1.0);
                     if (!var3.isAbove(var9, var2, false)) {
                        return var4;
                     }

                     var5.lastSupportTick = minecraft.level.getGameTime();
                     if (!var5.loggedSupport) {
                        var5.loggedSupport = (boolean)1;
                        LarpLog.debug("Jesus", "Water support active at native fluid height; no forced motion or extra packets");
                     }

                     return var9;
                  } else {
                     return var4;
                  }
               } else {
                  return var4;
               }
            } else {
               return var4;
            }
         } catch (RuntimeException var10) {
            LarpLog.error("Jesus", "Water collision query failed; disabling and returning native collision", var10);
            var5.disable();
            return var4;
         }
      } else {
         return var4;
      }
   }

   private static VoxelShape[] buildWaterShapes() {
      VoxelShape[] var0 = new VoxelShape[10];
      var0[0] = Shapes.empty();

      for (int var1 = 1; var1 <= 9; var1++) {
         var0[var1] = Shapes.box(0.0, 0.0, 0.0, 1.0, var1 / 9.0F, 1.0);
      }

      return var0;
   }
}
