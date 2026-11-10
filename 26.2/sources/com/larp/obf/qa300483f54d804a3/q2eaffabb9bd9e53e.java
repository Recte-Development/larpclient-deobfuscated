package com.larp.obf.qa300483f54d804a3;

import com.larpclient.my;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemInHandRenderer.class)
public final class q2eaffabb9bd9e53e {
   @Inject(method = "renderItem", at = @At("HEAD"))
   private void a(LivingEntity var1, ItemStack var2, ItemDisplayContext var3, PoseStack var4, SubmitNodeCollector var5, int var6, CallbackInfo var7) {
      if (var1 == Minecraft.getInstance().player && var3.firstPerson() && !var2.isEmpty()) {
         my.M(var3 == ItemDisplayContext.FIRST_PERSON_LEFT_HAND ? HumanoidArm.LEFT : HumanoidArm.RIGHT, var4, var5, true);
      }
   }

   @Inject(method = "renderPlayerArm", at = @At("RETURN"))
   private void U(PoseStack var1, SubmitNodeCollector var2, int var3, float var4, float var5, HumanoidArm var6, CallbackInfo var7) {
      Minecraft var8 = Minecraft.getInstance();
      if (var8.player != null) {
         ItemStack var9 = var6 == var8.player.getMainArm() ? var8.player.getMainHandItem() : var8.player.getOffhandItem();
         my.M(var6, var1, var2, !var9.isEmpty());
      }
   }

   @Inject(
      method = "renderMapHand",
      at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/vertex/PoseStack;popPose()V", shift = Shift.BEFORE),
      require = 1,
      allow = 1
   )
   private void n(PoseStack var1, SubmitNodeCollector var2, int var3, HumanoidArm var4, CallbackInfo var5) {
      my.M(var4, var1, var2, true);
   }
}
