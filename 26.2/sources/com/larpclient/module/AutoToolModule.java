package com.larpclient.module;

import com.larpclient.l5;
import com.larpclient.ta;
import java.util.ArrayList;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult.Type;

public final class AutoToolModule extends Module {
   private static AutoToolModule b;
   private final s I = this.addSetting(new s("Switch Back", "Restore the previous slot after mining", true));
   private final s E = this.addSetting(new s("Protect Tools", "Avoid tools with only one durability remaining", true));
   private final l5.u R = new l5.u();
   private Object Q;
   private Object G;
   private int f = -1;
   private ItemStack w;

   public AutoToolModule() {
      super("AutoTool", "Select the best hotbar tool while mining", Category.PLAYER, -1);
      b = this;
   }

   @Override
   public void onEnable() {
      this.R.G(-1, false);
      this.f = -1;
      this.w = null;
      this.Q = getMc().level;
      this.G = getMc().player;
      com.larpclient.h.A("AutoTool", "AutoTool active: hotbar tools, harvestability, Efficiency and durability protection");
   }

   @Override
   public void onDisable() {
      this.Y();
   }

   private boolean X() {
      return ta.Y()
         && !mc.player.isCreative()
         && !mc.player.isUsingItem()
         && mc.options.keyAttack.isDown()
         && mc.hitResult instanceof BlockHitResult var1
         && var1.getType() == Type.BLOCK;
   }

   @Override
   public void onTick() {
      if (this.Q != getMc().level || this.G != getMc().player) {
         this.R.G(-1, false);
         this.f = -1;
         this.w = null;
         this.Q = mc.level;
         this.G = mc.player;
      }

      int var10000 = this.X();
      boolean var2 = false;
      byte var1 = (byte)var10000;
      var10000 = Integer.signum(var1);
      if ((var10000 | -var10000) >>> 31 == 0) {
         this.Y();
      }
   }

   private void Y() {
      if (getMc().level == this.Q && mc.player == this.G && mc.player != null) {
         Inventory var1 = mc.player.getInventory();
         var1.setSelectedSlot(this.R.G(var1.getSelectedSlot(), this.I.getValue() && this.f >= 0 && var1.getItem(this.f) == this.w));
      } else {
         this.R.G(-1, false);
      }

      this.f = -1;
      this.w = null;
   }

   public static boolean C(BlockPos var0) {
      if (b != null && b.isEnabled()) {
         AutoToolModule var1 = b;
         if (var1 == null || !var1.isRuntimeActive() || ta.d() || !var1.X()) {
            return false;
         }

         if (var1.Q == getMc().level && var1.G == getMc().player) {
            if (!(mc.hitResult instanceof BlockHitResult var2 && var2.getBlockPos().equals(var0) && mc.level.hasChunkAt(var0))) {
               return false;
            } else if (!mc.player.blockActionRestricted(mc.level, var0, mc.gameMode.getPlayerMode()) && mc.level.getWorldBorder().isWithinBounds(var0)) {
               BlockState var8 = mc.level.getBlockState(var0);
               if (!var8.isAir() && !(var8.getDestroySpeed(mc.level, var0) <= 0.0F)) {
                  Inventory var4 = mc.player.getInventory();
                  ArrayList var5 = new ArrayList(9);

                  for (int var6 = 0; var6 < 9; var6++) {
                     var5.add(l5.F(var4.getItem(var6), var8, var1.E.getValue()));
                  }

                  int var9 = var4.getSelectedSlot();
                  if (var1.f == var9 && var4.getItem(var9) != var1.w) {
                     var1.R.v();
                  }

                  int var7 = var1.R.T(var9, l5.H(var5, var9));
                  if (var7 != var9) {
                     var4.setSelectedSlot(var7);
                     var1.f = var7;
                     var1.w = var4.getItem(var7);
                  }

                  return var7 != var9;
               } else {
                  return false;
               }
            } else {
               return false;
            }
         } else {
            return false;
         }
      } else {
         return false;
      }
   }
}
