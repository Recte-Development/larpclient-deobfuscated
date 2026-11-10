package com.larpclient.module;

import com.larpclient.j;
import com.larpclient.w;
import java.util.Set;
import net.minecraft.class_243;
import net.minecraft.class_746;

public class ElytraFlyModule extends Module {
   private final lo R = this.addSetting(new lo("Speed", "Target flight speed", 1.5, 0.5, 5.0, 0.1));
   private final li E = this.addSetting(new li("Mode", "Bounded elytra assistance", "Boost", "Boost", "Glide", "Control"));

   @Override
   public Set<l5> getNetworkImpacts() {
      return Set.of(l5.F);
   }

   public ElytraFlyModule() {
      super("ElytraFly", "Control elytra speed and flight", Category.MOVEMENT, -1);
   }

   @Override
   public void onTick() {
      if (this.isInGame() && mc.field_1724.method_6128()) {
         int var10000 = j.k();
         boolean var5 = false;
         byte var4 = (byte)var10000;
         var10000 = Integer.signum(var4);
         if (((var10000 | -var10000) >>> 31 ^ 1) == 0 && !w.o(this)) {
            class_243 var1 = mc.field_1724.method_18798();
            class_243 var2 = mc.field_1724.method_5720().method_1021(this.R.getValue());
            int var13 = this.E.G("Glide");
            boolean var7 = false;
            byte var6 = (byte)var13;
            var13 = Integer.signum(var6);
            if (((var13 | -var13) >>> 31 ^ 1) == 0) {
               class_243 var3 = new class_243(var1.field_1352, 0.0, var1.field_1350);
               double var18;
               var13 = (var18 = var3.method_1033() - this.R.getValue()) == 0.0 ? 0 : (var18 < 0.0 ? -1 : 1);
               boolean var9 = false;
               int var8 = var13;
               if ((-Integer.signum(var8) >>> 31 ^ 1) == 0) {
                  var3 = var3.method_1029().method_1021(this.R.getValue());
               }

               var2 = new class_243(var3.field_1352, Math.max(var1.field_1351, -0.02), var3.field_1350);
            }

            class_746 var16 = mc.field_1724;
            int var10003 = this.E.G("Boost");
            boolean var11 = false;
            byte var10 = (byte)var10003;
            var10003 = Integer.signum(var10);
            var16.method_18799(j.N(var1, var2, ((var10003 | -var10003) >>> 31 ^ 1) == 0 ? 0.04 : 0.08));
            return;
         }
      }
   }
}
