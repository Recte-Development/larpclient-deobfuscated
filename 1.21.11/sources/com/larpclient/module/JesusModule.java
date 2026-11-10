package com.larpclient.module;

import com.larpclient.j;
import com.larpclient.q8;
import java.util.Set;
import net.minecraft.class_1941;
import net.minecraft.class_2338;
import net.minecraft.class_2404;
import net.minecraft.class_259;
import net.minecraft.class_265;
import net.minecraft.class_2680;
import net.minecraft.class_3486;
import net.minecraft.class_3610;
import net.minecraft.class_3727;
import net.minecraft.class_746;

public final class JesusModule extends Module {
   private static JesusModule A;
   private static final class_265[] E = o();
   private long B = Long.MIN_VALUE;
   private boolean r;

   public JesusModule() {
      super("Jesus", "Walk on water surfaces; hold sneak to swim normally", Category.MOVEMENT, -1);
      A = this;
   }

   @Override
   public Set<l5> getNetworkImpacts() {
      return Set.of(l5.F);
   }

   @Override
   public void onEnable() {
      this.B = Long.MIN_VALUE;
      this.r = (boolean)0;
      q8.v("Jesus", "Enabled: water surfaces only; sneak and long falls retain normal swimming");
   }

   @Override
   public void onDisable() {
      this.B = Long.MIN_VALUE;
      this.r = (boolean)0;
      q8.v("Jesus", "Released water support; native collision restored");
   }

   @Override
   public String getStatus() {
      if (!this.isRuntimeActive()) {
         return "Inactive";
      } else if (!this.e()) {
         return "Paused; native water movement";
      } else {
         return this.B != Long.MIN_VALUE && mc.field_1687.method_75260() - this.B <= 1L ? "Walking on water" : "Approach the water surface from above";
      }
   }

   private boolean e() {
      if (this.isInGame() && j.k()) {
         class_746 var1 = mc.field_1724;
         if (!var1.method_7325()
            && !var1.method_5765()
            && !var1.method_6128()
            && !var1.method_31549().field_7479
            && !var1.field_5960
            && !var1.method_6113()
            && !var1.method_74025()
            && !var1.method_6101()
            && !var1.method_5771()
            && !var1.method_5715()
            && !mc.field_1690.field_1832.method_1434()
            && !(var1.field_6017 > 3.0)) {
            FlyModule var2 = FlyModule.W();
            AirStuckModule var3 = AirStuckModule.U();
            ElytraMotionModule var4 = ElytraMotionModule.N();
            return (var2 == null || !var2.P()) && (var3 == null || !var3.E()) && (var4 == null || !var4.k());
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   public static class_265 a(class_2680 var0, class_1941 var1, class_2338 var2, class_3727 var3, class_265 var4) {
      JesusModule var5 = A;
      if (var5 != null && var5.isRuntimeActive()) {
         try {
            if (var1 == getMc().field_1687
               && var3.method_32480() == mc.field_1724
               && var0.method_26204() instanceof class_2404
               && var4.method_1110()
               && !var3.method_16193()
               && var5.e()) {
               class_3610 var6 = var0.method_26227();
               if (var6.method_15767(class_3486.field_15517) && !var1.method_8316(var2.method_10084()).method_15767(class_3486.field_15517)) {
                  float var7 = var6.method_15763(var1, var2);
                  if (Float.isFinite(var7) && !(var7 <= 0.0F) && !(var7 > 1.0F)) {
                     int var8 = Math.round(var7 * 9.0F);
                     class_265 var9 = Math.abs(var7 - var8 / 9.0F) < 1.0E-6 ? E[var8] : class_259.method_1081(0.0, 0.0, 0.0, 1.0, var7, 1.0);
                     if (!var3.method_16192(var9, var2, false)) {
                        return var4;
                     }

                     var5.B = mc.field_1687.method_75260();
                     if (!var5.r) {
                        var5.r = (boolean)1;
                        q8.v("Jesus", "Water support active at native fluid height; no forced motion or extra packets");
                     }

                     return var9;
                  } else {
                     return var4;
                  }
               } else {
                  return var4;
               }
            } else {
               return var4;
            }
         } catch (RuntimeException var10) {
            q8.u("Jesus", "Water collision query failed; disabling and returning native collision", var10);
            var5.disable();
            return var4;
         }
      } else {
         return var4;
      }
   }

   private static class_265[] o() {
      class_265[] var0 = new class_265[10];
      var0[0] = class_259.method_1073();

      for (int var1 = 1; var1 <= 9; var1++) {
         var0[var1] = class_259.method_1081(0.0, 0.0, 0.0, 1.0, var1 / 9.0F, 1.0);
      }

      return var0;
   }
}
