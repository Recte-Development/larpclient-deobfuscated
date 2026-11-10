package com.larpclient.module;

import com.larpclient.q8;
import java.util.List;
import net.minecraft.class_10055;
import net.minecraft.class_746;

public final class HideArmourModule extends Module {
   private static volatile HideArmourModule e;
   public final l3 m = this.addSetting(new l3("Self Only", "Hide only your own armour and keep it on other players", false));

   public HideArmourModule() {
      super("Hide Armour", "Stop worn armour from showing on players", Category.RENDER, -1);
      e = this;
      this.m.a(var0 -> q8.G("HideArmour", "Hiding armour on {}", k(var0)));
   }

   @Override
   public List<String> getSearchAliases() {
      return List.of("Hide Armor", "No Armor", "No Armour");
   }

   public static boolean z(class_10055 var0) {
      if (e != null && e.isEnabled()) {
         HideArmourModule var1 = e;
         if (var1 == null || !var1.isOperational()) {
            return false;
         }

         if (!var1.m.getValue()) {
            return true;
         }

         class_746 var2 = var1.getPlayer();
         return var2 != null && var0.field_53528 == var2.method_5628();
      } else {
         return false;
      }
   }

   @Override
   public void onEnable() {
      q8.G("HideArmour", "Hiding armour on {}", k(this.m.getValue()));
   }

   private static String k(boolean var0) {
      return var0 ? "your own player only" : "every player";
   }
}
