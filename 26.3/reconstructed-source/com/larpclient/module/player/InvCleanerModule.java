package com.larpclient.module.player;

import com.larpclient.module.misc.BooleanSetting;
import com.larpclient.module.misc.Category;
import com.larpclient.module.misc.Module;
import net.minecraft.world.inventory.ContainerInput;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class InvCleanerModule extends Module {
   private final BooleanSetting dirt = this.addSetting(new BooleanSetting("Dirt", "Drop dirt blocks", true));
   private final BooleanSetting cobble = this.addSetting(new BooleanSetting("Cobble", "Drop cobblestone", true));
   private final BooleanSetting gravel = this.addSetting(new BooleanSetting("Gravel", "Drop gravel", true));
   private final BooleanSetting seeds = this.addSetting(new BooleanSetting("Seeds", "Drop seeds", true));
   private final BooleanSetting rottenFlesh = this.addSetting(new BooleanSetting("Rotten Flesh", "Drop rotten flesh", true));
   private int dropCooldown = 0;

   public InvCleanerModule() {
      super("InvCleaner", "Automatically drop junk items", Category.PLAYER, -1);
   }

   @Override
   public void onTick() {
      if (this.isInGame()) {
         if (minecraft.gui.screen() == null) {
            boolean var5 = false;
            int var4 = this.dropCooldown;
            if ((-Integer.signum(var4) >>> 31 ^ 1) == 0) {
               byte var7 = 1;
               int var6 = this.dropCooldown;
               this.dropCooldown = var6 - var7;
            } else {
               for (int var1 = 0; var1 < 36; var1++) {
                  ItemStack var2 = minecraft.player.getInventory().getItem(var1);
                  if (!var2.isEmpty() && this.isJunk(var2)) {
                     int var3 = var1 < 9 ? var1 + 36 : var1;
                     minecraft.gameMode.handleContainerInput(minecraft.player.containerMenu.containerId, var3, 1, ContainerInput.THROW, minecraft.player);
                     this.dropCooldown = 3;
                     return;
                  }
               }
            }
         }
      }
   }

   private boolean isJunk(ItemStack var1) {
      Item var2 = var1.getItem();
      if (!this.dirt.getValue() || var2 != Items.DIRT && var2 != Items.COARSE_DIRT) {
         if (this.cobble.getValue() && var2 == Items.COBBLESTONE) {
            return true;
         } else if (!this.gravel.getValue() || var2 != Items.GRAVEL && var2 != Items.FLINT) {
            return !this.seeds.getValue()
                  || var2 != Items.WHEAT_SEEDS && var2 != Items.BEETROOT_SEEDS && var2 != Items.MELON_SEEDS && var2 != Items.PUMPKIN_SEEDS
               ? this.rottenFlesh.getValue() && var2 == Items.ROTTEN_FLESH
               : true;
         } else {
            return true;
         }
      } else {
         return true;
      }
   }
}
