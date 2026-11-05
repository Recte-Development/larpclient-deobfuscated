package com.larpclient;

import com.larpclient.module.HeadlessKorbloxModule;
import net.minecraft.client.model.player.PlayerModel;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.world.entity.Avatar;
import net.minecraft.world.item.ItemStack;

public final class kv {
   private kv() {
   }

   public static void P(Avatar var0, AvatarRenderState var1) {
      q var2 = (q)var1;
      var2.larp$roblox(0, null);
      HeadlessKorbloxModule var3 = HeadlessKorbloxModule.k();
      int var4 = var3 == null ? 0 : var3.U(var0);
      u3 var5 = var3 == null ? null : var3.e(var0);
      if (var5 != null && !uf.J()) {
         var5 = null;
      }

      var2.larp$roblox(var4, var5);
      if (var4 == 2) {
         P(var1);
      }
   }

   public static void P(HumanoidRenderState var0) {
      var0.headEquipment = ItemStack.EMPTY;
      var0.headItem.clear();
      var0.wornHeadType = null;
      var0.wornHeadProfile = null;
   }

   public static boolean Y(PlayerModel var0, boolean var1, int var2, u3 var3) {
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

   public static void k(AvatarRenderState var0) {
      ((q)var0).larp$roblox(0, null);
   }
}
