package com.larpclient.module;

import com.larpclient.c5;
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
   private static JesusModule m;
   private static final VoxelShape[] h = Q();
   private long M = Long.MIN_VALUE;
   private boolean J;

   public JesusModule() {
      super("Jesus", "Walk on water surfaces; hold sneak to swim normally", Category.MOVEMENT, -1);
      m = this;
   }

   @Override
   public Set<m> getNetworkImpacts() {
      return Set.of(com.larpclient.module.m.U);
   }

   @Override
   public void onEnable() {
      this.M = Long.MIN_VALUE;
      this.J = (boolean)0;
      com.larpclient.h.U("Jesus", "Enabled: water surfaces only; sneak and long falls retain normal swimming");
   }

   @Override
   public void onDisable() {
      this.M = Long.MIN_VALUE;
      this.J = (boolean)0;
      com.larpclient.h.U("Jesus", "Released water support; native collision restored");
   }

   @Override
   public String getStatus() {
      if (!this.isRuntimeActive()) {
         return "Inactive";
      } else if (!this.n()) {
         return "Paused; native water movement";
      } else {
         return this.M != Long.MIN_VALUE && mc.level.getGameTime() - this.M <= 1L ? "Walking on water" : "Approach the water surface from above";
      }
   }

   private boolean n() {
      if (this.isInGame() && c5.J()) {
         LocalPlayer var1 = mc.player;
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
            && !mc.options.keyShift.isDown()
            && !(var1.fallDistance > 3.0)) {
            FlyModule var2 = FlyModule.S();
            AirStuckModule var3 = AirStuckModule.T();
            ElytraMotionModule var4 = ElytraMotionModule.v();
            return (var2 == null || !var2.f()) && (var3 == null || !var3.I()) && (var4 == null || !var4.K());
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   public static VoxelShape d(BlockState var0, CollisionGetter var1, BlockPos var2, EntityCollisionContext var3, VoxelShape var4) {
      JesusModule var5 = m;
      if (var5 != null && var5.isRuntimeActive()) {
         try {
            if (var1 == getMc().level
               && var3.getEntity() == mc.player
               && var0.getBlock() instanceof LiquidBlock
               && var4.isEmpty()
               && !var3.isDescending()
               && var5.n()) {
               FluidState var6 = var0.getFluidState();
               if (var6.is(FluidTags.WATER) && !var1.getFluidState(var2.above()).is(FluidTags.WATER)) {
                  float var7 = var6.getHeight(var1, var2);
                  if (Float.isFinite(var7) && !(var7 <= 0.0F) && !(var7 > 1.0F)) {
                     int var8 = Math.round(var7 * 9.0F);
                     VoxelShape var9 = Math.abs(var7 - var8 / 9.0F) < 1.0E-6 ? h[var8] : Shapes.box(0.0, 0.0, 0.0, 1.0, var7, 1.0);
                     if (!var3.isAbove(var9, var2, false)) {
                        return var4;
                     }

                     var5.M = mc.level.getGameTime();
                     if (!var5.J) {
                        var5.J = (boolean)1;
                        com.larpclient.h.U("Jesus", "Water support active at native fluid height; no forced motion or extra packets");
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
            com.larpclient.h.m("Jesus", "Water collision query failed; disabling and returning native collision", var10);
            var5.disable();
            return var4;
         }
      } else {
         return var4;
      }
   }

   private static VoxelShape[] Q() {
      VoxelShape[] var0 = new VoxelShape[10];
      var0[0] = Shapes.empty();

      for (int var1 = 1; var1 <= 9; var1++) {
         var0[var1] = Shapes.box(0.0, 0.0, 0.0, 1.0, var1 / 9.0F, 1.0);
      }

      return var0;
   }
}
