package com.larpclient.module;

import net.minecraft.world.inventory.ContainerInput;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class InvCleanerModule extends Module {
   private final m F = this.addSetting(new m("Dirt", "Drop dirt blocks", true));
   private final m c = this.addSetting(new m("Cobble", "Drop cobblestone", true));
   private final m i = this.addSetting(new m("Gravel", "Drop gravel", true));
   private final m h = this.addSetting(new m("Seeds", "Drop seeds", true));
   private final m n = this.addSetting(new m("Rotten Flesh", "Drop rotten flesh", true));
   private int y = 0;

   public InvCleanerModule() {
      super("InvCleaner", "Automatically drop junk items", Category.PLAYER, -1);
   }

   @Override
   public void onTick() {
      if (this.isInGame()) {
         if (mc.gui.screen() == null) {
            boolean var5 = false;
            int var4 = this.y;
            if ((-Integer.signum(var4) >>> 31 ^ 1) == 0) {
               byte var7 = 1;
               int var6 = this.y;
               this.y = var6 - var7;
            } else {
               for (int var1 = 0; var1 < 36; var1++) {
                  ItemStack var2 = mc.player.getInventory().getItem(var1);
                  if (!var2.isEmpty() && this.E(var2)) {
                     int var3 = var1 < 9 ? var1 + 36 : var1;
                     mc.gameMode.handleContainerInput(mc.player.containerMenu.containerId, var3, 1, ContainerInput.THROW, mc.player);
                     this.y = 3;
                     return;
                  }
               }
            }
         }
      }
   }

   private boolean E(ItemStack var1) {
      Item var2 = var1.getItem();
      if (!this.F.getValue() || var2 != Items.DIRT && var2 != Items.COARSE_DIRT) {
         if (this.c.getValue() && var2 == Items.COBBLESTONE) {
            return true;
         } else if (!this.i.getValue() || var2 != Items.GRAVEL && var2 != Items.FLINT) {
            return !this.h.getValue() || var2 != Items.WHEAT_SEEDS && var2 != Items.BEETROOT_SEEDS && var2 != Items.MELON_SEEDS && var2 != Items.PUMPKIN_SEEDS
               ? this.n.getValue() && var2 == Items.ROTTEN_FLESH
               : true;
         } else {
            return true;
         }
      } else {
         return true;
      }
   }
}
