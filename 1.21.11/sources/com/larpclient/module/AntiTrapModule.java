package com.larpclient.module;

import com.larpclient.q8;
import java.util.Set;
import net.minecraft.class_10255;
import net.minecraft.class_1297;
import net.minecraft.class_1496;
import net.minecraft.class_1531;
import net.minecraft.class_1533;
import net.minecraft.class_1534;
import net.minecraft.class_1540;
import net.minecraft.class_1646;
import net.minecraft.class_1688;
import net.minecraft.class_2185;
import net.minecraft.class_2478;
import net.minecraft.class_2680;

public final class AntiTrapModule extends Module {
   private static AntiTrapModule N;
   private final l3 u = this.S("Paintings", true);
   private final l3 W = this.S("Item Frames", true);
   private final l3 g = this.S("Minecarts", true);
   private final l3 f = this.S("Falling Blocks", false);
   private final l3 o = this.S("Armor Stands", false);
   private final l3 z = this.S("Banners", false);
   private final l3 p = this.S("Villagers", false);
   private final l3 F = this.S("Horses", false);
   private final l3 B = this.S("Signs", false);
   private final l3 L = this.S("Boats", false);

   public AntiTrapModule() {
      super("AntiTrap", "Skip interactions with selected trap objects", Category.COMBAT, -1);
      N = this;
   }

   private l3 S(String var1, boolean var2) {
      return this.addSetting(new l3(var1, "Ignore " + var1.toLowerCase() + " when interacting", var2));
   }

   public static boolean C(class_1297 var0) {
      if (N != null && N.isEnabled()) {
         AntiTrapModule var1 = N;
         return var1 != null
            && var1.isRuntimeActive()
            && var0 != null
            && (
               var0 instanceof class_1534 && var1.u.getValue()
                  || var0 instanceof class_1533 && var1.W.getValue()
                  || var0 instanceof class_1688 && var1.g.getValue()
                  || var0 instanceof class_1540 && var1.f.getValue()
                  || var0 instanceof class_1531 && var1.o.getValue()
                  || var0 instanceof class_1646 && var1.p.getValue()
                  || var0 instanceof class_1496 && var1.F.getValue()
                  || var0 instanceof class_10255 && var1.L.getValue()
            );
      } else {
         return false;
      }
   }

   public static boolean J(class_2680 var0) {
      if (N != null && N.isEnabled()) {
         AntiTrapModule var1 = N;
         return var1 != null
            && var1.isRuntimeActive()
            && var0 != null
            && (var0.method_26204() instanceof class_2185 && var1.z.getValue() || var0.method_26204() instanceof class_2478 && var1.B.getValue());
      } else {
         return false;
      }
   }

   @Override
   public void onEnable() {
      q8.v("AntiTrap", "Interaction trap filters active");
   }

   @Override
   public void onDisable() {
      q8.v("AntiTrap", "Interaction trap filters released");
   }

   @Override
   public Set<l5> getNetworkImpacts() {
      return Set.of(l5.Y);
   }
}
