package com.larpclient.module.player;

import com.larpclient.module.misc.BooleanSetting;
import com.larpclient.module.misc.Category;
import com.larpclient.module.misc.Module;
import com.larpclient.util.inventory.ToolSelector;
import com.larpclient.util.misc.InteractionArbiter;
import com.larpclient.util.misc.LarpLog;
import java.util.ArrayList;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult.Type;

public final class AutoToolModule extends Module {
   private static AutoToolModule instance;
   private final BooleanSetting switchBack = this.addSetting(new BooleanSetting("Switch Back", "Restore the previous slot after mining", true));
   private final BooleanSetting protectTools = this.addSetting(new BooleanSetting("Protect Tools", "Avoid tools with only one durability remaining", true));
   private final ToolSelector.SlotSwitcher slotSwitcher = new ToolSelector.SlotSwitcher();
   private Object level;
   private Object player;
   private int switchedSlot = -1;
   private ItemStack switchedStack;

   public AutoToolModule() {
      super("AutoTool", "Select the best hotbar tool while mining", Category.PLAYER, -1);
      instance = this;
   }

   @Override
   public void onEnable() {
      this.slotSwitcher.restore(-1, false);
      this.switchedSlot = -1;
      this.switchedStack = null;
      this.level = getMc().level;
      this.player = getMc().player;
      LarpLog.info("AutoTool", "AutoTool active: hotbar tools, harvestability, Efficiency and durability protection");
   }

   @Override
   public void onDisable() {
      this.restoreSlot();
   }

   private boolean isMiningBlock() {
      return InteractionArbiter.canInteract()
         && !minecraft.player.isCreative()
         && !minecraft.player.isUsingItem()
         && minecraft.options.keyAttack.isDown()
         && minecraft.hitResult instanceof BlockHitResult var1
         && var1.getType() == Type.BLOCK;
   }

   @Override
   public void onTick() {
      if (this.level != getMc().level || this.player != getMc().player) {
         this.slotSwitcher.restore(-1, false);
         this.switchedSlot = -1;
         this.switchedStack = null;
         this.level = minecraft.level;
         this.player = minecraft.player;
      }

      int var10000 = this.isMiningBlock();
      boolean var2 = false;
      byte var1 = (byte)var10000;
      var10000 = Integer.signum(var1);
      if ((var10000 | -var10000) >>> 31 == 0) {
         this.restoreSlot();
      }
   }

   private void restoreSlot() {
      if (getMc().level == this.level && minecraft.player == this.player && minecraft.player != null) {
         Inventory var1 = minecraft.player.getInventory();
         var1.setSelectedSlot(
            this.slotSwitcher
               .restore(var1.getSelectedSlot(), this.switchBack.getValue() && this.switchedSlot >= 0 && var1.getItem(this.switchedSlot) == this.switchedStack)
         );
      } else {
         this.slotSwitcher.restore(-1, false);
      }

      this.switchedSlot = -1;
      this.switchedStack = null;
   }

   public static boolean restoreSlot(BlockPos var0) {
      if (instance != null && instance.isEnabled()) {
         AutoToolModule var1 = instance;
         if (var1 == null || !var1.isRuntimeActive() || InteractionArbiter.isModuleInteracting() || !var1.isMiningBlock()) {
            return false;
         }

         if (var1.level == getMc().level && var1.player == getMc().player) {
            if (!(minecraft.hitResult instanceof BlockHitResult var2 && var2.getBlockPos().equals(var0) && minecraft.level.hasChunkAt(var0))) {
               return false;
            } else if (!minecraft.player.blockActionRestricted(minecraft.level, var0, minecraft.gameMode.getPlayerMode())
               && minecraft.level.getWorldBorder().isWithinBounds(var0)) {
               BlockState var8 = minecraft.level.getBlockState(var0);
               if (!var8.isAir() && !(var8.getDestroySpeed(minecraft.level, var0) <= 0.0F)) {
                  Inventory var4 = minecraft.player.getInventory();
                  ArrayList var5 = new ArrayList(9);

                  for (int var6 = 0; var6 < 9; var6++) {
                     var5.add(ToolSelector.evaluateTool(var4.getItem(var6), var8, var1.protectTools.getValue()));
                  }

                  int var9 = var4.getSelectedSlot();
                  if (var1.switchedSlot == var9 && var4.getItem(var9) != var1.switchedStack) {
                     var1.slotSwitcher.suppress();
                  }

                  int var7 = var1.slotSwitcher.select(var9, ToolSelector.pickBestTool(var5, var9));
                  if (var7 != var9) {
                     var4.setSelectedSlot(var7);
                     var1.switchedSlot = var7;
                     var1.switchedStack = var4.getItem(var7);
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
