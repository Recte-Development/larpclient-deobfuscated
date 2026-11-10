package com.larp.obf.qa300483f54d804a3;

import com.larpclient.j4;
import com.larpclient.ks;
import com.larpclient.p_;
import com.larpclient.pe;
import com.larpclient.s8;
import com.larpclient.w;
import com.larpclient.module.AnimationsModule;
import com.larpclient.module.ViewModelModule;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.player.PlayerModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.player.AvatarRenderer;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwingAnimationType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemInHandRenderer.class)
public class q431b4321274e0de8 {
   @Unique
   private float G;
   @Unique
   private float z;
   @Unique
   private InteractionHand S = InteractionHand.MAIN_HAND;

   @Inject(method = "renderItem", at = @At("HEAD"))
   private void x(LivingEntity var1, ItemStack var2, ItemDisplayContext var3, PoseStack var4, SubmitNodeCollector var5, int var6, CallbackInfo var7) {
      var4.pushPose();
      if (var3.firstPerson() && var1 == Minecraft.getInstance().player) {
         ViewModelModule var8 = ViewModelModule.b();
         if (var8 != null && var8.isRuntimeActive()) {
            var8.O(var4, this.S, var3 == ItemDisplayContext.FIRST_PERSON_LEFT_HAND);
         }

         AnimationsModule var9 = AnimationsModule.I();
         if (var9 != null && !var2.isEmpty() && !var2.has(DataComponents.MAP_ID)) {
            var9.w(var4);
         }
      }
   }

   @Inject(method = "renderItem", at = @At("RETURN"))
   private void V(LivingEntity var1, ItemStack var2, ItemDisplayContext var3, PoseStack var4, SubmitNodeCollector var5, int var6, CallbackInfo var7) {
      var4.popPose();
   }

   @Redirect(
      method = "renderItem",
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/renderer/item/ItemStackRenderState;submit(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;III)V"
      )
   )
   private void I(
      ItemStackRenderState var1,
      PoseStack var2,
      SubmitNodeCollector var3,
      int var4,
      int var5,
      int var6,
      LivingEntity var7,
      ItemStack var8,
      ItemDisplayContext var9,
      PoseStack var10,
      SubmitNodeCollector var11,
      int var12
   ) {
      SubmitNodeCollector var13 = var9.firstPerson() && var7 == Minecraft.getInstance().player ? j4.B(var3) : var3;
      if (var9.firstPerson() && var7 == Minecraft.getInstance().player) {
         s8.O(this.S, var9 == ItemDisplayContext.FIRST_PERSON_LEFT_HAND, var2, var1);
      }

      if (var9.firstPerson() && var7 == Minecraft.getInstance().player) {
         w.l(var1, var2, var13, var3, var8, var9, this.S, this.G, this.z, var4, var5, var6);
      } else {
         var1.submit(var2, var13, var4, var5, var6);
      }
   }

   @Shadow
   private void swingArm(float var1, PoseStack var2, int var3, HumanoidArm var4) {
      throw new AssertionError("Mixin shadow");
   }

   @Shadow
   private void applyItemArmTransform(PoseStack var1, HumanoidArm var2, float var3) {
      throw new AssertionError("Mixin shadow");
   }

   @Redirect(
      method = "submitArmWithItem",
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/renderer/ItemInHandRenderer;swingArm(FLcom/mojang/blaze3d/vertex/PoseStack;ILnet/minecraft/world/entity/HumanoidArm;)V",
         ordinal = 2
      ),
      require = 1,
      allow = 1
   )
   private void H(ItemInHandRenderer var1, float var2, PoseStack var3, int var4, HumanoidArm var5) {
      AnimationsModule var6 = AnimationsModule.I();
      if (var6 != null && var6.Y(var5)) {
         var6.d(var3, var5, var2);
      } else {
         this.swingArm(var2, var3, var4, var5);
      }
   }

   @Redirect(
      method = "submitArmWithItem",
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/renderer/ItemInHandRenderer;applyItemArmTransform(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/world/entity/HumanoidArm;F)V",
         ordinal = 4
      ),
      require = 1,
      allow = 1
   )
   private void v(
      ItemInHandRenderer var1,
      PoseStack var2,
      HumanoidArm var3,
      float var4,
      AbstractClientPlayer var5,
      float var6,
      float var7,
      InteractionHand var8,
      float var9,
      ItemStack var10,
      float var11,
      PoseStack var12,
      SubmitNodeCollector var13,
      int var14
   ) {
      AnimationsModule var15 = AnimationsModule.I();
      boolean var16 = var15 != null && var15.Y(var3) && ks.P(var10).type() == SwingAnimationType.WHACK;
      if (!var16) {
         this.applyItemArmTransform(var2, var3, var4);
      }
   }

   @Inject(method = "submitArmWithItem", at = @At("HEAD"), cancellable = true)
   private void t(
      AbstractClientPlayer var1,
      float var2,
      float var3,
      InteractionHand var4,
      float var5,
      ItemStack var6,
      float var7,
      PoseStack var8,
      SubmitNodeCollector var9,
      int var10,
      CallbackInfo var11
   ) {
      this.S = var4;
      this.G = var5;
      this.z = var7;
      if (pe.U(var1, var4, var6, var5, var7, var2, var8, var9, var10)) {
         var11.cancel();
      } else {
         var8.pushPose();
         ViewModelModule var12 = ViewModelModule.b();
         if (var12 != null && var12.isRuntimeActive()) {
            boolean var13 = var4 == InteractionHand.MAIN_HAND ? var1.getMainArm() == HumanoidArm.LEFT : var1.getMainArm() != HumanoidArm.LEFT;
            var12.t(var8, var4, var13);
         }
      }
   }

   @Inject(method = "submitHandsWithItems", at = @At("HEAD"))
   private void t(float var1, PoseStack var2, SubmitNodeCollector var3, LocalPlayer var4, int var5, CallbackInfo var6) {
      s8.c();
   }

   @WrapOperation(
      method = "submitHandsWithItems",
      at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;getViewYRot(F)F"),
      require = 1,
      allow = 1
   )
   private float t(LocalPlayer var1, float var2, Operation<Float> var3) {
      float var4 = p_.M(Mth.lerp(var2, var1.yBobO, var1.yBob));
      return Float.isNaN(var4) ? (Float)var3.call(new Object[]{var1, var2}) : var4;
   }

   @WrapOperation(
      method = "submitHandsWithItems",
      at = {
            @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;getViewXRot(F)F"),
            @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;getXRot(F)F")
      },
      require = 2,
      allow = 2
   )
   private float J(LocalPlayer var1, float var2, Operation<Float> var3) {
      float var4 = p_.f();
      return Float.isNaN(var4) ? (Float)var3.call(new Object[]{var1, var2}) : var4;
   }

   @WrapOperation(
      method = "renderPlayerArm",
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/renderer/entity/player/AvatarRenderer;renderRightHand(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;ILnet/minecraft/resources/Identifier;Z)V"
      ),
      require = 1,
      allow = 1
   )
   private void X(AvatarRenderer var1, PoseStack var2, SubmitNodeCollector var3, int var4, Identifier var5, boolean var6, Operation<Void> var7) {
      l(var2, false);
      var7.call(new Object[]{var1, var2, var3, var4, var5, var6});
      s8.H(InteractionHand.MAIN_HAND, false, var2, ((PlayerModel)var1.getModel()).rightArm);
   }

   @WrapOperation(
      method = "renderPlayerArm",
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/renderer/entity/player/AvatarRenderer;renderLeftHand(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;ILnet/minecraft/resources/Identifier;Z)V"
      ),
      require = 1,
      allow = 1
   )
   private void A(AvatarRenderer var1, PoseStack var2, SubmitNodeCollector var3, int var4, Identifier var5, boolean var6, Operation<Void> var7) {
      l(var2, true);
      var7.call(new Object[]{var1, var2, var3, var4, var5, var6});
      s8.H(InteractionHand.MAIN_HAND, true, var2, ((PlayerModel)var1.getModel()).leftArm);
   }

   @Unique
   private static void l(PoseStack var0, boolean var1) {
      ViewModelModule var2 = ViewModelModule.b();
      if (var2 != null && var2.isRuntimeActive()) {
         var2.b(var0, InteractionHand.MAIN_HAND, var1);
      }
   }

   @Inject(method = "submitArmWithItem", at = @At("RETURN"))
   private void q(
      AbstractClientPlayer var1,
      float var2,
      float var3,
      InteractionHand var4,
      float var5,
      ItemStack var6,
      float var7,
      PoseStack var8,
      SubmitNodeCollector var9,
      int var10,
      CallbackInfo var11
   ) {
      var8.popPose();
   }
}
