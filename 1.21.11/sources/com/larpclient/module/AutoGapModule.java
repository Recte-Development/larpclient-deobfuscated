package com.larpclient.module;

import com.larpclient.b_;
import com.larpclient.wv;
import net.minecraft.class_1792;
import net.minecraft.class_1802;

public class AutoGapModule extends Module {
   private final lo S = this.addSetting(new lo("Health", "Eat below this HP", 14.0, 2.0, 20.0, 1.0));
   private final l3 r = this.addSetting(new l3("Golden Apple", "Use golden apples", true));
   private final l3 o = this.addSetting(new l3("Enchanted", "Use enchanted golden apples", true));

   public AutoGapModule() {
      super("AutoGap", "Automatically eat golden apples", Category.PLAYER, -1);
   }

   @Override
   public void onTick() {
      if (this.isInGame()) {
         double var15;
         int var10000 = (var15 = mc.field_1724.method_6032() - this.S.getValue()) == 0.0 ? 0 : (var15 < 0.0 ? -1 : 1);
         boolean var3 = false;
         int var2 = var10000;
         if ((-Integer.signum(var2) >>> 31 ^ 1) != 0) {
            int var10 = this.o.getValue();
            boolean var9 = false;
            byte var8 = (byte)var10;
            var10 = Integer.signum(var8);
            int var1 = ((var10 | -var10) >>> 31 ^ 1) == 0 ? this.t(class_1802.field_8367) : -1;
            byte var5 = -1;
            int var4 = var1;
            var10 = Integer.compare(var4, var5);
            if ((var10 | -var10) >>> 31 == 0) {
               int var13 = this.r.getValue();
               boolean var7 = false;
               byte var6 = (byte)var13;
               var13 = Integer.signum(var6);
               if (((var13 | -var13) >>> 31 ^ 1) == 0) {
                  var1 = this.t(class_1802.field_8463);
               }
            }

            wv.D(
               b_.s.t,
               var1,
               var1x -> this.r.getValue() && var1x.method_31574(class_1802.field_8463) || this.o.getValue() && var1x.method_31574(class_1802.field_8367)
            );
            return;
         }
      }

      this.onDisable();
   }

   @Override
   public void onDisable() {
      wv.P(b_.s.t);
   }

   private int t(class_1792 var1) {
      for (int var2 = 0; var2 < 9; var2++) {
         if (mc.field_1724.method_31548().method_5438(var2).method_31574(var1)) {
            return var2;
         }
      }

      return -1;
   }
}
