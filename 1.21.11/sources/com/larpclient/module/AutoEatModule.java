package com.larpclient.module;

import com.larpclient.b_;
import com.larpclient.wv;
import net.minecraft.class_1799;
import net.minecraft.class_4174;
import net.minecraft.class_9334;

public class AutoEatModule extends Module {
   private final lo w = this.addSetting(new lo("Hunger", "Eat below this hunger", 14.0, 1.0, 20.0, 1.0));

   public AutoEatModule() {
      super("AutoEat", "Automatically eat food", Category.PLAYER, -1);
   }

   @Override
   public void onTick() {
      if (this.isInGame()) {
         double var8;
         int var10000 = (var8 = mc.field_1724.method_7344().method_7586() - this.w.getValue()) == 0.0 ? 0 : (var8 < 0.0 ? -1 : 1);
         boolean var7 = false;
         int var6 = var10000;
         if (Integer.signum(var6) >>> 31 != 0) {
            int var1 = -1;
            int var2 = 0;

            for (int var3 = 0; var3 < 9; var3++) {
               class_1799 var4 = mc.field_1724.method_31548().method_5438(var3);
               class_4174 var5 = (class_4174)var4.method_58694(class_9334.field_50075);
               if (!var4.method_7960() && var5 != null && var5.comp_2491() > var2) {
                  var2 = var5.comp_2491();
                  var1 = var3;
               }
            }

            wv.D(b_.s.z, var1, var0 -> var0.method_58694(class_9334.field_50075) != null);
            return;
         }
      }

      this.onDisable();
   }

   @Override
   public void onDisable() {
      wv.P(b_.s.z);
   }
}
