package com.larpclient.module.player;

import com.larpclient.module.misc.Category;
import com.larpclient.module.misc.Module;
import com.larpclient.module.misc.NumberSetting;
import com.larpclient.util.inventory.AutoEatHelper;
import com.larpclient.util.inventory.ItemUseController;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;

public class AutoEatModule extends Module {
   private final NumberSetting hunger = this.addSetting(new NumberSetting("Hunger", "Eat below this hunger", 14.0, 1.0, 20.0, 1.0));

   public AutoEatModule() {
      super("AutoEat", "Automatically eat food", Category.PLAYER, -1);
   }

   @Override
   public void onTick() {
      if (this.isInGame()) {
         double var8;
         int var10000 = (var8 = minecraft.player.getFoodData().getFoodLevel() - this.hunger.getValue()) == 0.0 ? 0 : (var8 < 0.0 ? -1 : 1);
         boolean var7 = false;
         int var6 = var10000;
         if (Integer.signum(var6) >>> 31 != 0) {
            int var1 = -1;
            int var2 = 0;

            for (int var3 = 0; var3 < 9; var3++) {
               ItemStack var4 = minecraft.player.getInventory().getItem(var3);
               FoodProperties var5 = (FoodProperties)var4.get(DataComponents.FOOD);
               if (!var4.isEmpty() && var5 != null && var5.nutrition() > var2) {
                  var2 = var5.nutrition();
                  var1 = var3;
               }
            }

            AutoEatHelper.start(ItemUseController.Owner.NORMAL, var1, var0 -> var0.get(DataComponents.FOOD) != null);
            return;
         }
      }

      this.onDisable();
   }

   @Override
   public void onDisable() {
      AutoEatHelper.cancel(ItemUseController.Owner.NORMAL);
   }
}
