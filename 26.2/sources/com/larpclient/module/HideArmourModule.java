package com.larpclient.module;

import java.util.List;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;

public final class HideArmourModule extends Module {
   private static volatile HideArmourModule J;
   public final s w = this.addSetting(new s("Self Only", "Hide only your own armour and keep it on other players", false));

   public HideArmourModule() {
      super("Hide Armour", "Stop worn armour from showing on players", Category.RENDER, -1);
      J = this;
      this.w.o(var0 -> com.larpclient.h.A("HideArmour", "Hiding armour on {}", I(var0)));
   }

   @Override
   public List<String> getSearchAliases() {
      return List.of("Hide Armor", "No Armor", "No Armour");
   }

   public static boolean Y(AvatarRenderState var0) {
      if (J != null && J.isEnabled()) {
         HideArmourModule var1 = J;
         if (var1 == null || !var1.isOperational()) {
            return false;
         }

         if (!var1.w.getValue()) {
            return true;
         }

         LocalPlayer var2 = var1.getPlayer();
         return var2 != null && var0.id == var2.getId();
      } else {
         return false;
      }
   }

   @Override
   public void onEnable() {
      com.larpclient.h.A("HideArmour", "Hiding armour on {}", I(this.w.getValue()));
   }

   private static String I(boolean var0) {
      return var0 ? "your own player only" : "every player";
   }
}
