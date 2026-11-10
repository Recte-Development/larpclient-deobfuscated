package com.larpclient.module;

import com.larpclient._p;
import com.larpclient._q;
import com.larpclient.s5;
import java.util.ArrayList;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult.Type;

public final class AutoToolModule extends Module {
   private static AutoToolModule X;
   private final m h = this.addSetting(new m("Switch Back", "Restore the previous slot after mining", true));
   private final m W = this.addSetting(new m("Protect Tools", "Avoid tools with only one durability remaining", true));
   private final s5.w N = new s5.w();
   private Object U;
   private Object e;
   private int o = -1;
   private ItemStack V;

   public AutoToolModule() {
      super("AutoTool", "Select the best hotbar tool while mining", Category.PLAYER, -1);
      X = this;
   }

   @Override
   public void onEnable() {
      this.N.b(-1, false);
      this.o = -1;
      this.V = null;
      this.U = getMc().level;
      this.e = getMc().player;
      _q.z("AutoTool", "AutoTool active: hotbar tools, harvestability, Efficiency and durability protection");
   }

   @Override
   public void onDisable() {
      this.h();
   }

   private boolean Y() {
      return _p.g()
         && !mc.player.isCreative()
         && !mc.player.isUsingItem()
         && mc.options.keyAttack.isDown()
         && mc.hitResult instanceof BlockHitResult var1
         && var1.getType() == Type.BLOCK;
   }

   @Override
   public void onTick() {
      if (this.U != getMc().level || this.e != getMc().player) {
         this.N.b(-1, false);
         this.o = -1;
         this.V = null;
         this.U = mc.level;
         this.e = mc.player;
      }

      int var10000 = this.Y();
      boolean var2 = false;
      byte var1 = (byte)var10000;
      var10000 = Integer.signum(var1);
      if ((var10000 | -var10000) >>> 31 == 0) {
         this.h();
      }
   }

   private void h() {
      if (getMc().level == this.U && mc.player == this.e && mc.player != null) {
         Inventory var1 = mc.player.getInventory();
         var1.setSelectedSlot(this.N.b(var1.getSelectedSlot(), this.h.getValue() && this.o >= 0 && var1.getItem(this.o) == this.V));
      } else {
         this.N.b(-1, false);
      }

      this.o = -1;
      this.V = null;
   }

   public static boolean h(BlockPos var0) {
      if (X != null && X.isEnabled()) {
         AutoToolModule var1 = X;
         if (var1 == null || !var1.isRuntimeActive() || _p.I() || !var1.Y()) {
            return false;
         }

         if (var1.U == getMc().level && var1.e == getMc().player) {
            if (!(mc.hitResult instanceof BlockHitResult var2 && var2.getBlockPos().equals(var0) && mc.level.hasChunkAt(var0))) {
               return false;
            } else if (!mc.player.blockActionRestricted(mc.level, var0, mc.gameMode.getPlayerMode()) && mc.level.getWorldBorder().isWithinBounds(var0)) {
               BlockState var8 = mc.level.getBlockState(var0);
               if (!var8.isAir() && !(var8.getDestroySpeed(mc.level, var0) <= 0.0F)) {
                  Inventory var4 = mc.player.getInventory();
                  ArrayList var5 = new ArrayList(9);

                  for (int var6 = 0; var6 < 9; var6++) {
                     var5.add(s5.d(var4.getItem(var6), var8, var1.W.getValue()));
                  }

                  int var9 = var4.getSelectedSlot();
                  if (var1.o == var9 && var4.getItem(var9) != var1.V) {
                     var1.N.R();
                  }

                  int var7 = var1.N.z(var9, s5.M(var5, var9));
                  if (var7 != var9) {
                     var4.setSelectedSlot(var7);
                     var1.o = var7;
                     var1.V = var4.getItem(var7);
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
