package com.larpclient.module.player;

import com.larpclient.module.misc.BooleanSetting;
import com.larpclient.module.misc.Category;
import com.larpclient.module.misc.Module;
import com.larpclient.module.misc.NumberSetting;
import com.larpclient.util.inventory.AutoEatHelper;
import com.larpclient.util.inventory.ItemUseController;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

public class AutoGapModule extends Module {
   private final NumberSetting health = this.addSetting(new NumberSetting("Health", "Eat below this HP", 14.0, 2.0, 20.0, 1.0));
   private final BooleanSetting goldenApple = this.addSetting(new BooleanSetting("Golden Apple", "Use golden apples", true));
   private final BooleanSetting enchanted = this.addSetting(new BooleanSetting("Enchanted", "Use enchanted golden apples", true));

   public AutoGapModule() {
      super("AutoGap", "Automatically eat golden apples", Category.PLAYER, -1);
   }

   @Override
   public void onTick() {
      if (this.isInGame()) {
         double var15;
         int var10000 = (var15 = minecraft.player.getHealth() - this.health.getValue()) == 0.0 ? 0 : (var15 < 0.0 ? -1 : 1);
         boolean var3 = false;
         int var2 = var10000;
         if ((-Integer.signum(var2) >>> 31 ^ 1) != 0) {
            int var10 = this.enchanted.getValue();
            boolean var9 = false;
            byte var8 = (byte)var10;
            var10 = Integer.signum(var8);
            int var1 = ((var10 | -var10) >>> 31 ^ 1) == 0 ? this.findHotbarSlot(Items.ENCHANTED_GOLDEN_APPLE) : -1;
            byte var5 = -1;
            int var4 = var1;
            var10 = Integer.compare(var4, var5);
            if ((var10 | -var10) >>> 31 == 0) {
               int var13 = this.goldenApple.getValue();
               boolean var7 = false;
               byte var6 = (byte)var13;
               var13 = Integer.signum(var6);
               if (((var13 | -var13) >>> 31 ^ 1) == 0) {
                  var1 = this.findHotbarSlot(Items.GOLDEN_APPLE);
               }
            }

            AutoEatHelper.start(
               ItemUseController.Owner.OVERRIDE,
               var1,
               var1x -> this.goldenApple.getValue() && var1x.is(Items.GOLDEN_APPLE) || this.enchanted.getValue() && var1x.is(Items.ENCHANTED_GOLDEN_APPLE)
            );
            return;
         }
      }

      this.onDisable();
   }

   @Override
   public void onDisable() {
      AutoEatHelper.cancel(ItemUseController.Owner.OVERRIDE);
   }

   private int findHotbarSlot(Item var1) {
      for (int var2 = 0; var2 < 9; var2++) {
         if (minecraft.player.getInventory().getItem(var2).is(var1)) {
            return var2;
         }
      }

      return -1;
   }
}
