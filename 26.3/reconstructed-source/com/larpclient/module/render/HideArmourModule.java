package com.larpclient.module.render;

import com.larpclient.module.misc.BooleanSetting;
import com.larpclient.module.misc.Category;
import com.larpclient.module.misc.Module;
import com.larpclient.util.misc.LarpLog;
import java.util.List;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;

public final class HideArmourModule extends Module {
   private static volatile HideArmourModule instance;
   public final BooleanSetting selfOnly = this.addSetting(new BooleanSetting("Self Only", "Hide only your own armour and keep it on other players", false));

   public HideArmourModule() {
      super("Hide Armour", "Stop worn armour from showing on players", Category.RENDER, -1);
      instance = this;
      this.selfOnly.onChange(var0 -> LarpLog.info("HideArmour", "Hiding armour on {}", describeTarget(var0.booleanValue())));
   }

   @Override
   public List<String> getSearchAliases() {
      return List.of("Hide Armor", "No Armor", "No Armour");
   }

   public static boolean shouldHideArmour(AvatarRenderState var0) {
      if (instance != null && instance.isEnabled()) {
         HideArmourModule var1 = instance;
         if (var1 == null || !var1.isOperational()) {
            return false;
         }

         if (!var1.selfOnly.getValue()) {
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
      LarpLog.info("HideArmour", "Hiding armour on {}", describeTarget(this.selfOnly.getValue().booleanValue()));
   }

   private static String describeTarget(boolean var0) {
      return var0 ? "your own player only" : "every player";
   }
}
