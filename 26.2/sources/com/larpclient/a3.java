package com.larpclient;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public final class a3 extends ar {
   private final com.larpclient.module.s f = this.U("Legit", "Look at reachable crops and harvest through the real crosshair", false);
   private final com.larpclient.module.e X = this.Z("Range", "Harvest radius measured from your eyes", 4.0, 1.0, 6.0, 1.0);

   public a3() {
      super("Auto Farm", "Harvest mature crops around you while walking through a farm");
   }

   private boolean S(BlockPos var1) {
      if (!mc.level.hasChunkAt(var1)) {
         return false;
      }

      BlockState var2 = mc.level.getBlockState(var1);
      return var2.getBlock() instanceof CropBlock var3 && var3.isMaxAge(var2);
   }

   @Override
   public void onTick() {
      int var10000 = this.I();
      boolean var13 = false;
      byte var12 = (byte)var10000;
      var10000 = Integer.signum(var12);
      if ((var10000 | -var10000) >>> 31 == 0) {
         l0.h(this);
      } else {
         double var1 = Math.min(this.X.getValue(), mc.player.blockInteractionRange());
         int var3 = (int)Math.ceil(var1);
         BlockPos var4 = mc.player.blockPosition();
         BlockPos var5 = null;
         double var6 = var1 * var1;
         boolean var15 = false;
         int var14 = var3;
         int var10001 = -var14;
         boolean var17 = false;
         int var16 = var3;
         int var10002 = -var16;
         boolean var19 = false;
         int var18 = var3;

         for (BlockPos var9 : BlockPos.betweenClosed(var4.offset(var10001, var10002, -var18), var4.offset(var3, var3, var3))) {
            if (this.S(var9)) {
               double var10 = mc.player.getEyePosition().distanceToSqr(Vec3.atCenterOf(var9));
               if (!(var10 >= var6) && (!this.f.getValue() || l0.V(var9, var1) != null)) {
                  var5 = var9.immutable();
                  var6 = var10;
               }
            }
         }

         if (var5 == null) {
            l0.h(this);
            this.e("No mature crops in reach");
         } else {
            BlockPos var20 = var5;
            l0.M(this, var20, var1, this.f.getValue(), 35.0F, false, () -> this.S(var20));
         }
      }
   }
}
