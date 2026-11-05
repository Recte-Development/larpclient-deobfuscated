package com.larpclient;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public final class t extends s {
   private final com.larpclient.module.ln P = this.S("Legit", "Look at reachable crops and harvest through the real crosshair", false);
   private final com.larpclient.module.lc f = this.H("Range", "Harvest radius measured from your eyes", 4.0, 1.0, 6.0, 1.0);

   public t() {
      super("Auto Farm", "Harvest mature crops around you while walking through a farm");
   }

   private boolean J(BlockPos var1) {
      if (!mc.level.hasChunkAt(var1)) {
         return false;
      }

      BlockState var2 = mc.level.getBlockState(var1);
      return var2.getBlock() instanceof CropBlock var3 && var3.isMaxAge(var2);
   }

   @Override
   public void onTick() {
      int var10000 = this.G();
      boolean var13 = false;
      byte var12 = (byte)var10000;
      var10000 = Integer.signum(var12);
      if ((var10000 | -var10000) >>> 31 == 0) {
         yx.H(this);
      } else {
         double var1 = Math.min(this.f.getValue(), mc.player.blockInteractionRange());
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
            if (this.J(var9)) {
               double var10 = mc.player.getEyePosition().distanceToSqr(Vec3.atCenterOf(var9));
               if (!(var10 >= var6) && (!this.P.getValue() || yx.E(var9, var1) != null)) {
                  var5 = var9.immutable();
                  var6 = var10;
               }
            }
         }

         if (var5 == null) {
            yx.H(this);
            this.I("No mature crops in reach");
         } else {
            BlockPos var20 = var5;
            yx.Y(this, var20, var1, this.P.getValue(), 35.0F, false, () -> this.J(var20));
         }
      }
   }
}
