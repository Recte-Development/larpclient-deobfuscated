package com.larpclient.module;

import com.larpclient.q8;
import java.util.Set;
import net.minecraft.class_1297;
import net.minecraft.class_1747;
import net.minecraft.class_1799;
import net.minecraft.class_1802;
import net.minecraft.class_310;

public final class IgnoreEntityModule extends Module {
   private static IgnoreEntityModule G;

   public IgnoreEntityModule() {
      super("IgnoreEntity", "Place blocks, crystals and fluids through entities", Category.COMBAT, -1);
      G = this;
   }

   public static boolean U(class_1297 var0) {
      if (G != null && G.isEnabled()) {
         class_310 var1 = class_310.method_1551();
         return G != null
            && G.isRuntimeActive()
            && var0 != null
            && var1.field_1724 != null
            && var1.field_1687 != null
            && var1.field_1690.field_1904.method_1434()
            && (Z(var1.field_1724.method_6047()) || Z(var1.field_1724.method_6079()));
      } else {
         return false;
      }
   }

   private static boolean Z(class_1799 var0) {
      return !var0.method_7960()
         && (
            var0.method_7909() instanceof class_1747
               || var0.method_31574(class_1802.field_8301)
               || var0.method_31574(class_1802.field_8705)
               || var0.method_31574(class_1802.field_8187)
         );
   }

   @Override
   public void onEnable() {
      q8.v("IgnoreEntity", "Placement entity pick filter active");
   }

   @Override
   public void onDisable() {
      q8.v("IgnoreEntity", "Placement entity pick filter released");
   }

   @Override
   public Set<l5> getNetworkImpacts() {
      return Set.of(l5.Y);
   }
}
