package com.larpclient.module;

import com.larpclient.cr;
import com.larpclient.q8;
import java.util.Set;
import net.minecraft.class_1747;
import net.minecraft.class_1776;
import net.minecraft.class_1792;
import net.minecraft.class_1799;
import net.minecraft.class_746;
import net.minecraft.class_9463;

public final class FastUseModule extends Module {
   public static final int b = 4;
   private static FastUseModule t;
   private final lo D = this.addSetting(new lo("Use Interval", "Ticks between uses while Use is held. Vanilla is 4", 1.0, 1.0, 4.0, 1.0));
   private final l3 c = this.addSetting(new l3("Blocks", "Place blocks faster", true));
   private final l3 T = this.addSetting(
      new l3("Projectiles", "Throw experience bottles, pearls, snowballs, eggs, potions, wind charges and fireworks faster", true)
   );
   private final l3 C = this.addSetting(new l3("Other Items", "Use every other item faster", true));

   public FastUseModule() {
      super("Fast Use", "Remove the delay between item uses", Category.PLAYER, -1);
      t = this;
   }

   public static int K(int var0) {
      return Math.clamp(var0, 1, 4);
   }

   public static boolean g(class_1799 var0, boolean var1, boolean var2, boolean var3) {
      if (var0 != null && !var0.method_7960()) {
         class_1792 var4 = var0.method_7909();
         if (var4 instanceof class_1747) {
            return var1;
         } else {
            return !(var4 instanceof class_9463) && !(var4 instanceof class_1776) ? var3 : var2;
         }
      } else {
         return false;
      }
   }

   public static int q(int var0) {
      if (t != null && t.isEnabled()) {
         FastUseModule var1 = t;
         if (var1 != null && var1.isRuntimeActive() && !cr.j()) {
            class_746 var2 = getMc().field_1724;
            if (var2 == null) {
               return var0;
            }

            boolean var3 = var1.c.getValue();
            boolean var4 = var1.T.getValue();
            boolean var5 = var1.C.getValue();
            return !g(var2.method_6047(), var3, var4, var5) && !g(var2.method_6079(), var3, var4, var5)
               ? var0
               : Math.min(var0, K(var1.D.getValue().intValue()));
         } else {
            return var0;
         }
      } else {
         return var0;
      }
   }

   @Override
   public Set<l5> getNetworkImpacts() {
      return Set.of(l5.F);
   }

   @Override
   public void onEnable() {
      q8.v(
         "FastUse",
         "Enabled; use interval {} ticks (blocks {}, projectiles {}, other items {})",
         this.D.getValue().intValue(),
         this.c.getValue(),
         this.T.getValue(),
         this.C.getValue()
      );
   }

   @Override
   public String getStatus() {
      int var1 = K(this.D.getValue().intValue());
      return var1 == 1 ? "Every tick" : var1 + " ticks";
   }
}
