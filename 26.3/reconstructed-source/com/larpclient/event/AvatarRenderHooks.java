package com.larpclient.event;

import com.larpclient.module.render.ChinaHatModule;
import com.larpclient.module.render.HeadlessKorbloxModule;
import com.larpclient.render.entity.KorbloxLegRenderer;
import com.larpclient.render.entity.LegTrail;
import com.larpclient.social.cosmetic.SkinOverrideAccess;
import net.minecraft.client.model.player.PlayerModel;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.world.entity.Avatar;
import net.minecraft.world.item.ItemStack;

public final class AvatarRenderHooks {
   private AvatarRenderHooks() {
   }

   public static void extractRenderState(Avatar var0, AvatarRenderState var1) {
      SkinOverrideAccess var2 = (SkinOverrideAccess)var1;
      var2.larp$roblox(0, null);
      HeadlessKorbloxModule var3 = HeadlessKorbloxModule.getInstance();
      if (var3 != null && !var3.isEnabled()) {
         var3 = null;
      }

      int var4 = var3 == null ? 0 : var3.getHeadMode(var0);
      ChinaHatModule var5 = ChinaHatModule.getInstance();
      if (var4 == 0 && var5 != null && var5.isEnabled() && var5.migrateLegacyMode(var0.getId())) {
         var4 = 1;
      }

      LegTrail var6 = var3 == null ? null : var3.getLegTrail(var0);
      if (var6 != null && !KorbloxLegRenderer.ensureTexture()) {
         var6 = null;
      }

      var2.larp$roblox(var4, var6);
      if (var4 == 2) {
         clearHeadEquipment(var1);
      }
   }

   public static void clearHeadEquipment(HumanoidRenderState var0) {
      var0.headEquipment = ItemStack.EMPTY;
      var0.headItem.clear();
      var0.wornHeadType = null;
      var0.wornHeadProfile = null;
   }

   public static boolean hideModelParts(PlayerModel var0, boolean var1, int var2, LegTrail var3) {
      int var4 = var1 && var2 != 0 && var0.head.visible ? 1 : 0;
      if (var4 != 0) {
         var0.head.visible = (boolean)0;
      }

      if (var3 != null) {
         if (var3.right()) {
            var0.rightLeg.visible = (boolean)0;
         }

         if (var3.left()) {
            var0.leftLeg.visible = (boolean)0;
         }
      }

      return (boolean)var4;
   }

   public static void resetRenderState(AvatarRenderState var0) {
      ((SkinOverrideAccess)var0).larp$roblox(0, null);
   }
}
