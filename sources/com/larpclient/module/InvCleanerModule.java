package com.larpclient.module;

import net.minecraft.world.inventory.ContainerInput;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class InvCleanerModule extends Module {
   private final ln S = this.addSetting(new ln("Dirt", "Drop dirt blocks", true));
   private final ln c = this.addSetting(new ln("Cobble", "Drop cobblestone", true));
   private final ln K = this.addSetting(new ln("Gravel", "Drop gravel", true));
   private final ln y = this.addSetting(new ln("Seeds", "Drop seeds", true));
   private final ln m = this.addSetting(new ln("Rotten Flesh", "Drop rotten flesh", true));
   private int l = 0;

   public InvCleanerModule() {
      super("InvCleaner", "Automatically drop junk items", Category.PLAYER, -1);
   }

   @Override
   public void onTick() {
      if (this.isInGame()) {
         if (mc.gui.screen() == null) {
            boolean var5 = false;
            int var4 = this.l;
            if ((-Integer.signum(var4) >>> 31 ^ 1) == 0) {
               byte var7 = 1;
               int var6 = this.l;
               this.l = var6 - var7;
            } else {
               for (int var1 = 0; var1 < 36; var1++) {
                  ItemStack var2 = mc.player.getInventory().getItem(var1);
                  if (!var2.isEmpty() && this.c(var2)) {
                     int var3 = var1 < 9 ? var1 + 36 : var1;
                     mc.gameMode.handleContainerInput(mc.player.containerMenu.containerId, var3, 1, ContainerInput.THROW, mc.player);
                     this.l = 3;
                     return;
                  }
               }
            }
         }
      }
   }

   private boolean c(ItemStack var1) {
      Item var2 = var1.getItem();
      if (!this.S.getValue() || var2 != Items.DIRT && var2 != Items.COARSE_DIRT) {
         if (this.c.getValue() && var2 == Items.COBBLESTONE) {
            return true;
         } else if (!this.K.getValue() || var2 != Items.GRAVEL && var2 != Items.FLINT) {
            return !this.y.getValue() || var2 != Items.WHEAT_SEEDS && var2 != Items.BEETROOT_SEEDS && var2 != Items.MELON_SEEDS && var2 != Items.PUMPKIN_SEEDS
               ? this.m.getValue() && var2 == Items.ROTTEN_FLESH
               : true;
         } else {
            return true;
         }
      } else {
         return true;
      }
   }
}
