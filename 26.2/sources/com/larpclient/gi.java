package com.larpclient;

import com.larpclient.module.ChinaHatModule;
import com.larpclient.module.HeadlessKorbloxModule;
import net.minecraft.client.model.player.PlayerModel;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.world.entity.Avatar;
import net.minecraft.world.item.ItemStack;

public final class gi {
   private gi() {
   }

   public static void r(Avatar var0, AvatarRenderState var1) {
      p7 var2 = (p7)var1;
      var2.larp$roblox(0, null);
      HeadlessKorbloxModule var3 = HeadlessKorbloxModule.I();
      if (var3 != null && !var3.isEnabled()) {
         var3 = null;
      }

      int var4 = var3 == null ? 0 : var3.f(var0);
      ChinaHatModule var5 = ChinaHatModule.N();
      if (var4 == 0 && var5 != null && var5.isEnabled() && var5.E(var0.getId())) {
         var4 = 1;
      }

      l8 var6 = var3 == null ? null : var3.R(var0);
      if (var6 != null && !wg.H()) {
         var6 = null;
      }

      var2.larp$roblox(var4, var6);
      if (var4 == 2) {
         I(var1);
      }
   }

   public static void I(HumanoidRenderState var0) {
      var0.headEquipment = ItemStack.EMPTY;
      var0.headItem.clear();
      var0.wornHeadType = null;
      var0.wornHeadProfile = null;
   }

   public static boolean M(PlayerModel var0, boolean var1, int var2, l8 var3) {
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

   public static void y(AvatarRenderState var0) {
      ((p7)var0).larp$roblox(0, null);
   }
}
