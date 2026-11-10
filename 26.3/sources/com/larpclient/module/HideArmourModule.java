package com.larpclient.module;

import com.larpclient._q;
import java.util.List;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;

public final class HideArmourModule extends Module {
   private static volatile HideArmourModule C;
   public final m M = this.addSetting(new m("Self Only", "Hide only your own armour and keep it on other players", false));

   public HideArmourModule() {
      super("Hide Armour", "Stop worn armour from showing on players", Category.RENDER, -1);
      C = this;
      this.M.w(var0 -> _q.z("HideArmour", "Hiding armour on {}", c(var0.booleanValue())));
   }

   @Override
   public List<String> getSearchAliases() {
      return List.of("Hide Armor", "No Armor", "No Armour");
   }

   public static boolean m(AvatarRenderState var0) {
      if (C != null && C.isEnabled()) {
         HideArmourModule var1 = C;
         if (var1 == null || !var1.isOperational()) {
            return false;
         }

         if (!var1.M.getValue()) {
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
      _q.z("HideArmour", "Hiding armour on {}", c(this.M.getValue().booleanValue()));
   }

   private static String c(boolean var0) {
      return var0 ? "your own player only" : "every player";
   }
}
