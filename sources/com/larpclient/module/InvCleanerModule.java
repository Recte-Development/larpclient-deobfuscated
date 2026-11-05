package com.larpclient.module;

import net.minecraft.world.inventory.ContainerInput;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class InvCleanerModule extends Module {
   private final dh z = this.addSetting(new dh("Dirt", "Drop dirt blocks", true));
   private final dh k = this.addSetting(new dh("Cobble", "Drop cobblestone", true));
   private final dh q = this.addSetting(new dh("Gravel", "Drop gravel", true));
   private final dh Y = this.addSetting(new dh("Seeds", "Drop seeds", true));
   private final dh d = this.addSetting(new dh("Rotten Flesh", "Drop rotten flesh", true));
   private int V = 0;

   public InvCleanerModule() {
      super("InvCleaner", "Automatically drop junk items", Category.PLAYER, -1);
   }

   @Override
   public void onTick() {
      if (this.isInGame()) {
         if (mc.gui.screen() == null) {
            boolean var5 = false;
            int var4 = this.V;
            if ((-Integer.signum(var4) >>> 31 ^ 1) == 0) {
               byte var7 = 1;
               int var6 = this.V;
               this.V = var6 - var7;
            } else {
               for (int var1 = 0; var1 < 36; var1++) {
                  ItemStack var2 = mc.player.getInventory().getItem(var1);
                  if (!var2.isEmpty() && this.V(var2)) {
                     int var3 = var1 < 9 ? var1 + 36 : var1;
                     mc.gameMode.handleContainerInput(mc.player.containerMenu.containerId, var3, 1, ContainerInput.THROW, mc.player);
                     this.V = 3;
                     return;
                  }
               }
            }
         }
      }
   }

   private boolean V(ItemStack var1) {
      Item var2 = var1.getItem();
      if (!this.z.P() || var2 != Items.DIRT && var2 != Items.COARSE_DIRT) {
         if (this.k.P() && var2 == Items.COBBLESTONE) {
            return true;
         } else if (!this.q.P() || var2 != Items.GRAVEL && var2 != Items.FLINT) {
            return !this.Y.P() || var2 != Items.WHEAT_SEEDS && var2 != Items.BEETROOT_SEEDS && var2 != Items.MELON_SEEDS && var2 != Items.PUMPKIN_SEEDS
               ? this.d.P() && var2 == Items.ROTTEN_FLESH
               : true;
         } else {
            return true;
         }
      } else {
         return true;
      }
   }
}
