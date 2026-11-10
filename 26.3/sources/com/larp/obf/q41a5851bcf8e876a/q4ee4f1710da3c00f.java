package com.larp.obf.q41a5851bcf8e876a;

import com.larpclient.bc;
import com.larpclient.dh;
import com.larpclient.h8;
import com.larpclient.jp;
import com.larpclient.oj;
import com.larpclient.w7;
import com.larpclient.module.AnimationsModule;
import com.larpclient.module.ViewModelModule;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.player.PlayerModel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.FirstPersonHandsAndItemsRenderer;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.player.AvatarRenderer;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.state.level.FirstPersonHandsAndItemsRenderState;
import net.minecraft.client.renderer.state.level.PlayerRenderState;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.Identifier;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwingAnimationType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FirstPersonHandsAndItemsRenderer.class)
public class q4ee4f1710da3c00f {
   @Unique
   private float j;
   @Unique
   private float J;
   @Unique
   private InteractionHand v = InteractionHand.MAIN_HAND;
   @Unique
   private ItemStack U = ItemStack.EMPTY;
   @Unique
   private HumanoidArm p = HumanoidArm.RIGHT;
   @Unique
   private boolean f;

   @Shadow
   private void swingArm(float var1, PoseStack var2, int var3, HumanoidArm var4) {
      throw new AssertionError("Mixin shadow");
   }

   @Shadow
   private void applyItemArmTransform(PoseStack var1, HumanoidArm var2, float var3) {
      throw new AssertionError("Mixin shadow");
   }

   @Inject(method = "submitHandsWithItems", at = @At("HEAD"))
   private void A(float var1, PoseStack var2, SubmitNodeCollector var3, PlayerRenderState var4, FirstPersonHandsAndItemsRenderState var5, CallbackInfo var6) {
      jp.U();
   }

   @Inject(method = "submitArmWithItem", at = @At("HEAD"), cancellable = true)
   private void C(
      PlayerRenderState var1,
      FirstPersonHandsAndItemsRenderState var2,
      float var3,
      float var4,
      InteractionHand var5,
      float var6,
      ItemStack var7,
      float var8,
      PoseStack var9,
      SubmitNodeCollector var10,
      int var11,
      CallbackInfo var12
   ) {
      LocalPlayer var13 = Minecraft.getInstance().player;
      this.v = var5;
      this.j = var6;
      this.J = var8;
      this.U = var7;
      this.p = var13 != null && var5 != InteractionHand.MAIN_HAND ? var13.getMainArm().getOpposite() : (var13 == null ? HumanoidArm.RIGHT : var13.getMainArm());
      if (var13 != null && bc.r(var13, var5, var7, var6, var8, var3, var9, var10, var11)) {
         var12.cancel();
      } else {
         var9.pushPose();
         ViewModelModule var14 = ViewModelModule.t();
         if (var14 != null && var14.isRuntimeActive()) {
            var14.i(var9, var5, this.p == HumanoidArm.LEFT);
         }
      }
   }

   @Inject(method = "submitArmWithItem", at = @At("RETURN"))
   private void M(
      PlayerRenderState var1,
      FirstPersonHandsAndItemsRenderState var2,
      float var3,
      float var4,
      InteractionHand var5,
      float var6,
      ItemStack var7,
      float var8,
      PoseStack var9,
      SubmitNodeCollector var10,
      int var11,
      CallbackInfo var12
   ) {
      var9.popPose();
   }

   @Redirect(
      method = "submitArmWithItem",
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/renderer/FirstPersonHandsAndItemsRenderer;swingArm(FLcom/mojang/blaze3d/vertex/PoseStack;ILnet/minecraft/world/entity/HumanoidArm;)V",
         ordinal = 2
      ),
      require = 1,
      allow = 1
   )
   private void T(FirstPersonHandsAndItemsRenderer var1, float var2, PoseStack var3, int var4, HumanoidArm var5) {
      if (!this.X(var5)) {
         this.swingArm(var2, var3, var4, var5);
      }
   }

   @Redirect(
      method = "submitArmWithItem",
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/renderer/FirstPersonHandsAndItemsRenderer;applyItemArmTransform(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/world/entity/HumanoidArm;F)V",
         ordinal = 4
      ),
      require = 1,
      allow = 1
   )
   private void a(FirstPersonHandsAndItemsRenderer var1, PoseStack var2, HumanoidArm var3, float var4) {
      if (this.X(var3)) {
         AnimationsModule.a().X(var2, var3, this.j);
      } else {
         this.applyItemArmTransform(var2, var3, var4);
      }
   }

   @Unique
   private boolean X(HumanoidArm var1) {
      AnimationsModule var2 = AnimationsModule.a();
      return var2 != null && var2.A(var1) && h8.d(this.U).type() == SwingAnimationType.WHACK;
   }

   @WrapOperation(
      method = "submitArmWithItem",
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/renderer/item/ItemStackRenderState;submit(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;III)V"
      )
   )
   private void a(ItemStackRenderState var1, PoseStack var2, SubmitNodeCollector var3, int var4, int var5, int var6, Operation<Void> var7) {
      boolean var8 = this.p == HumanoidArm.LEFT;
      ItemDisplayContext var9 = var8 ? ItemDisplayContext.FIRST_PERSON_LEFT_HAND : ItemDisplayContext.FIRST_PERSON_RIGHT_HAND;
      if (!this.U.isEmpty()) {
         oj.L(this.p, var2, var3, true);
      }

      var2.pushPose();

      try {
         ViewModelModule var10 = ViewModelModule.t();
         if (var10 != null && var10.isRuntimeActive()) {
            var10.n(var2, this.v, var8);
         }

         AnimationsModule var11 = AnimationsModule.a();
         if (var11 != null && !this.U.isEmpty() && !this.U.has(DataComponents.MAP_ID)) {
            var11.v(var2);
         }

         SubmitNodeCollector var12 = w7.v(var3);
         jp.c(this.v, var8, var2, var1);
         dh.R(var1, var2, var12, var3, this.U, var9, this.v, this.j, this.J, var4, var5, var6);
      } finally {
         var2.popPose();
      }
   }

   @WrapOperation(
      method = "renderPlayerArm",
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/renderer/FirstPersonHandsAndItemsRenderer;renderPlayerHand(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;ILnet/minecraft/world/entity/HumanoidArm;Lnet/minecraft/client/renderer/state/level/PlayerRenderState;)V"
      ),
      require = 1,
      allow = 1
   )
   private void n(
      FirstPersonHandsAndItemsRenderer var1, PoseStack var2, SubmitNodeCollector var3, int var4, HumanoidArm var5, PlayerRenderState var6, Operation<Void> var7
   ) {
      this.f = true;

      try {
         var7.call(new Object[]{var1, var2, var3, var4, var5, var6});
      } finally {
         this.f = false;
      }

      LocalPlayer var8 = Minecraft.getInstance().player;
      if (var8 != null) {
         ItemStack var9 = var5 == var8.getMainArm() ? var8.getMainHandItem() : var8.getOffhandItem();
         oj.L(var5, var2, var3, !var9.isEmpty());
      }
   }

   @WrapOperation(
      method = "renderPlayerHand",
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/renderer/entity/player/AvatarRenderer;renderRightHand(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;ILnet/minecraft/resources/Identifier;Z)V"
      ),
      require = 1,
      allow = 1
   )
   private void L(AvatarRenderer<?> var1, PoseStack var2, SubmitNodeCollector var3, int var4, Identifier var5, boolean var6, Operation<Void> var7) {
      if (this.f) {
         l(var2, false);
      }

      var7.call(new Object[]{var1, var2, var3, var4, var5, var6});
      if (this.f) {
         jp.B(InteractionHand.MAIN_HAND, false, var2, ((PlayerModel)var1.getModel()).rightArm);
      }
   }

   @WrapOperation(
      method = "renderPlayerHand",
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/renderer/entity/player/AvatarRenderer;renderLeftHand(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;ILnet/minecraft/resources/Identifier;Z)V"
      ),
      require = 1,
      allow = 1
   )
   private void F(AvatarRenderer<?> var1, PoseStack var2, SubmitNodeCollector var3, int var4, Identifier var5, boolean var6, Operation<Void> var7) {
      if (this.f) {
         l(var2, true);
      }

      var7.call(new Object[]{var1, var2, var3, var4, var5, var6});
      if (this.f) {
         jp.B(InteractionHand.MAIN_HAND, true, var2, ((PlayerModel)var1.getModel()).leftArm);
      }
   }

   @Inject(
      method = "renderMapHand",
      at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/vertex/PoseStack;popPose()V", shift = Shift.BEFORE),
      require = 1,
      allow = 1
   )
   private void B(PoseStack var1, SubmitNodeCollector var2, int var3, HumanoidArm var4, PlayerRenderState var5, CallbackInfo var6) {
      oj.L(var4, var1, var2, true);
   }

   @Unique
   private static void l(PoseStack var0, boolean var1) {
      ViewModelModule var2 = ViewModelModule.t();
      if (var2 != null && var2.isRuntimeActive()) {
         var2.U(var0, InteractionHand.MAIN_HAND, var1);
      }
   }
}
