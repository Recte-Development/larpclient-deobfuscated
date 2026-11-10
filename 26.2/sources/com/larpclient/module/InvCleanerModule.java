package com.larpclient.module;

import net.minecraft.world.inventory.ContainerInput;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class InvCleanerModule extends Module {
   private final s h = this.addSetting(new s("Dirt", "Drop dirt blocks", true));
   private final s G = this.addSetting(new s("Cobble", "Drop cobblestone", true));
   private final s y = this.addSetting(new s("Gravel", "Drop gravel", true));
   private final s z = this.addSetting(new s("Seeds", "Drop seeds", true));
   private final s o = this.addSetting(new s("Rotten Flesh", "Drop rotten flesh", true));
   private int N = 0;

   public InvCleanerModule() {
      super("InvCleaner", "Automatically drop junk items", Category.PLAYER, -1);
   }

   @Override
   public void onTick() {
      if (this.isInGame()) {
         if (mc.gui.screen() == null) {
            boolean var5 = false;
            int var4 = this.N;
            if ((-Integer.signum(var4) >>> 31 ^ 1) == 0) {
               byte var7 = 1;
               int var6 = this.N;
               this.N = var6 - var7;
            } else {
               for (int var1 = 0; var1 < 36; var1++) {
                  ItemStack var2 = mc.player.getInventory().getItem(var1);
                  if (!var2.isEmpty() && this.u(var2)) {
                     int var3 = var1 < 9 ? var1 + 36 : var1;
                     mc.gameMode.handleContainerInput(mc.player.containerMenu.containerId, var3, 1, ContainerInput.THROW, mc.player);
                     this.N = 3;
                     return;
                  }
               }
            }
         }
      }
   }

   private boolean u(ItemStack var1) {
      Item var2 = var1.getItem();
      if (!this.h.getValue() || var2 != Items.DIRT && var2 != Items.COARSE_DIRT) {
         if (this.G.getValue() && var2 == Items.COBBLESTONE) {
            return true;
         } else if (!this.y.getValue() || var2 != Items.GRAVEL && var2 != Items.FLINT) {
            return !this.z.getValue() || var2 != Items.WHEAT_SEEDS && var2 != Items.BEETROOT_SEEDS && var2 != Items.MELON_SEEDS && var2 != Items.PUMPKIN_SEEDS
               ? this.o.getValue() && var2 == Items.ROTTEN_FLESH
               : true;
         } else {
            return true;
         }
      } else {
         return true;
      }
   }
}
