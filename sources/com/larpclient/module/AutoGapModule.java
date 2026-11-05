package com.larpclient.module;

import com.larpclient.eo;
import com.larpclient.t2;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

public class AutoGapModule extends Module {
   private final d9 J = this.addSetting(new d9("Health", "Eat below this HP", 14.0, 2.0, 20.0, 1.0));
   private final dh I = this.addSetting(new dh("Golden Apple", "Use golden apples", true));
   private final dh X = this.addSetting(new dh("Enchanted", "Use enchanted golden apples", true));

   public AutoGapModule() {
      super("AutoGap", "Automatically eat golden apples", Category.PLAYER, -1);
   }

   @Override
   public void onTick() {
      if (this.isInGame()) {
         double var15;
         int var10000 = (var15 = mc.player.getHealth() - this.J.P()) == 0.0 ? 0 : (var15 < 0.0 ? -1 : 1);
         boolean var3 = false;
         int var2 = var10000;
         if ((-Integer.signum(var2) >>> 31 ^ 1) != 0) {
            int var10 = this.X.P();
            boolean var9 = false;
            byte var8 = (byte)var10;
            var10 = Integer.signum(var8);
            int var1 = ((var10 | -var10) >>> 31 ^ 1) == 0 ? this.j(Items.ENCHANTED_GOLDEN_APPLE) : -1;
            byte var5 = -1;
            int var4 = var1;
            var10 = Integer.compare(var4, var5);
            if ((var10 | -var10) >>> 31 == 0) {
               int var13 = this.I.P();
               boolean var7 = false;
               byte var6 = (byte)var13;
               var13 = Integer.signum(var6);
               if (((var13 | -var13) >>> 31 ^ 1) == 0) {
                  var1 = this.j(Items.GOLDEN_APPLE);
               }
            }

            t2.U(eo.r.V, var1, var1x -> this.I.P() && var1x.is(Items.GOLDEN_APPLE) || this.X.P() && var1x.is(Items.ENCHANTED_GOLDEN_APPLE));
            return;
         }
      }

      this.onDisable();
   }

   @Override
   public void onDisable() {
      t2.D(eo.r.V);
   }

   private int j(Item var1) {
      for (int var2 = 0; var2 < 9; var2++) {
         if (mc.player.getInventory().getItem(var2).is(var1)) {
            return var2;
         }
      }

      return -1;
   }
}
