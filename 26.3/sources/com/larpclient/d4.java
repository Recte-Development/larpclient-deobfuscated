package com.larpclient;

import com.larpclient.module.ChinaHatModule;
import com.larpclient.module.HeadlessKorbloxModule;
import net.minecraft.client.model.player.PlayerModel;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.world.entity.Avatar;
import net.minecraft.world.item.ItemStack;

public final class d4 {
   private d4() {
   }

   public static void e(Avatar var0, AvatarRenderState var1) {
      qr var2 = (qr)var1;
      var2.larp$roblox(0, null);
      HeadlessKorbloxModule var3 = HeadlessKorbloxModule.W();
      if (var3 != null && !var3.isEnabled()) {
         var3 = null;
      }

      int var4 = var3 == null ? 0 : var3.q(var0);
      ChinaHatModule var5 = ChinaHatModule.E();
      if (var4 == 0 && var5 != null && var5.isEnabled() && var5.Q(var0.getId())) {
         var4 = 1;
      }

      a1 var6 = var3 == null ? null : var3.X(var0);
      if (var6 != null && !f3.M()) {
         var6 = null;
      }

      var2.larp$roblox(var4, var6);
      if (var4 == 2) {
         H(var1);
      }
   }

   public static void H(HumanoidRenderState var0) {
      var0.headEquipment = ItemStack.EMPTY;
      var0.headItem.clear();
      var0.wornHeadType = null;
      var0.wornHeadProfile = null;
   }

   public static boolean C(PlayerModel var0, boolean var1, int var2, a1 var3) {
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
      ((qr)var0).larp$roblox(0, null);
   }
}
