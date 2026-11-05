package com.larpclient.module;

import com.larpclient.ex;
import com.larpclient.je;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

public class AutoGapModule extends Module {
   private final lc n = this.addSetting(new lc("Health", "Eat below this HP", 14.0, 2.0, 20.0, 1.0));
   private final ln f = this.addSetting(new ln("Golden Apple", "Use golden apples", true));
   private final ln l = this.addSetting(new ln("Enchanted", "Use enchanted golden apples", true));

   public AutoGapModule() {
      super("AutoGap", "Automatically eat golden apples", Category.PLAYER, -1);
   }

   @Override
   public void onTick() {
      if (this.isInGame()) {
         double var15;
         int var10000 = (var15 = mc.player.getHealth() - this.n.getValue()) == 0.0 ? 0 : (var15 < 0.0 ? -1 : 1);
         boolean var3 = false;
         int var2 = var10000;
         if ((-Integer.signum(var2) >>> 31 ^ 1) != 0) {
            int var10 = this.l.getValue();
            boolean var9 = false;
            byte var8 = (byte)var10;
            var10 = Integer.signum(var8);
            int var1 = ((var10 | -var10) >>> 31 ^ 1) == 0 ? this.z(Items.ENCHANTED_GOLDEN_APPLE) : -1;
            byte var5 = -1;
            int var4 = var1;
            var10 = Integer.compare(var4, var5);
            if ((var10 | -var10) >>> 31 == 0) {
               int var13 = this.f.getValue();
               boolean var7 = false;
               byte var6 = (byte)var13;
               var13 = Integer.signum(var6);
               if (((var13 | -var13) >>> 31 ^ 1) == 0) {
                  var1 = this.z(Items.GOLDEN_APPLE);
               }
            }

            ex.S(je.b.P, var1, var1x -> this.f.getValue() && var1x.is(Items.GOLDEN_APPLE) || this.l.getValue() && var1x.is(Items.ENCHANTED_GOLDEN_APPLE));
            return;
         }
      }

      this.onDisable();
   }

   @Override
   public void onDisable() {
      ex.z(je.b.P);
   }

   private int z(Item var1) {
      for (int var2 = 0; var2 < 9; var2++) {
         if (mc.player.getInventory().getItem(var2).is(var1)) {
            return var2;
         }
      }

      return -1;
   }
}
