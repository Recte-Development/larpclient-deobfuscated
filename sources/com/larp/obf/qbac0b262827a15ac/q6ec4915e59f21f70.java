package com.larp.obf.qbac0b262827a15ac;

import com.larpclient._8;
import com.larpclient.m2;
import com.larpclient.ma;
import com.larpclient.tj;
import com.larpclient.x3;
import com.larpclient.x4;
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
public class q6ec4915e59f21f70 {
   @Unique
   private float z;
   @Unique
   private float c;
   @Unique
   private InteractionHand B = InteractionHand.MAIN_HAND;
   @Unique
   private ItemStack m = ItemStack.EMPTY;
   @Unique
   private HumanoidArm d = HumanoidArm.RIGHT;
   @Unique
   private boolean C;

   @Shadow
   private void swingArm(float var1, PoseStack var2, int var3, HumanoidArm var4) {
      throw new AssertionError("Mixin shadow");
   }

   @Shadow
   private void applyItemArmTransform(PoseStack var1, HumanoidArm var2, float var3) {
      throw new AssertionError("Mixin shadow");
   }

   @Inject(method = "submitHandsWithItems", at = @At("HEAD"))
   private void t(float var1, PoseStack var2, SubmitNodeCollector var3, PlayerRenderState var4, FirstPersonHandsAndItemsRenderState var5, CallbackInfo var6) {
      x3.M();
   }

   @Inject(method = "submitArmWithItem", at = @At("HEAD"), cancellable = true)
   private void x(
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
      this.B = var5;
      this.z = var6;
      this.c = var8;
      this.m = var7;
      this.d = var13 != null && var5 != InteractionHand.MAIN_HAND ? var13.getMainArm().getOpposite() : (var13 == null ? HumanoidArm.RIGHT : var13.getMainArm());
      if (var13 != null && m2.I(var13, var5, var7, var6, var8, var3, var9, var10, var11)) {
         var12.cancel();
      } else {
         var9.pushPose();
         ViewModelModule var14 = ViewModelModule.Z();
         if (var14 != null && var14.isRuntimeActive()) {
            var14.z(var9, var5, this.d == HumanoidArm.LEFT);
         }
      }
   }

   @Inject(method = "submitArmWithItem", at = @At("RETURN"))
   private void H(
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
   private void j(FirstPersonHandsAndItemsRenderer var1, float var2, PoseStack var3, int var4, HumanoidArm var5) {
      AnimationsModule var6 = AnimationsModule.g();
      if (var6 != null && var6.M(var5)) {
         var6.O(var3, var5, var2);
      } else {
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
   private void o(FirstPersonHandsAndItemsRenderer var1, PoseStack var2, HumanoidArm var3, float var4) {
      AnimationsModule var5 = AnimationsModule.g();
      boolean var6 = var5 != null && var5.M(var3) && _8.c(this.m).type() == SwingAnimationType.WHACK;
      if (!var6) {
         this.applyItemArmTransform(var2, var3, var4);
      }
   }

   @WrapOperation(
      method = "submitArmWithItem",
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/renderer/item/ItemStackRenderState;submit(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;III)V"
      )
   )
   private void F(ItemStackRenderState var1, PoseStack var2, SubmitNodeCollector var3, int var4, int var5, int var6, Operation<Void> var7) {
      boolean var8 = this.d == HumanoidArm.LEFT;
      ItemDisplayContext var9 = var8 ? ItemDisplayContext.FIRST_PERSON_LEFT_HAND : ItemDisplayContext.FIRST_PERSON_RIGHT_HAND;
      if (!this.m.isEmpty()) {
         ma.U(this.d, var2, var3, true);
      }

      var2.pushPose();

      try {
         ViewModelModule var10 = ViewModelModule.Z();
         if (var10 != null && var10.isRuntimeActive()) {
            var10.f(var2, this.B, var8);
         }

         AnimationsModule var11 = AnimationsModule.g();
         if (var11 != null && !this.m.isEmpty() && !this.m.has(DataComponents.MAP_ID)) {
            var11.z(var2);
         }

         SubmitNodeCollector var12 = tj.z(var3);
         x3.f(this.B, var8, var2, var1);
         x4.O(var1, var2, var12, var3, this.m, var9, this.B, this.z, this.c, var4, var5, var6);
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
   private void G(
      FirstPersonHandsAndItemsRenderer var1, PoseStack var2, SubmitNodeCollector var3, int var4, HumanoidArm var5, PlayerRenderState var6, Operation<Void> var7
   ) {
      this.C = true;

      try {
         var7.call(new Object[]{var1, var2, var3, var4, var5, var6});
      } finally {
         this.C = false;
      }

      LocalPlayer var8 = Minecraft.getInstance().player;
      if (var8 != null) {
         ItemStack var9 = var5 == var8.getMainArm() ? var8.getMainHandItem() : var8.getOffhandItem();
         ma.U(var5, var2, var3, !var9.isEmpty());
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
   private void T(AvatarRenderer<?> var1, PoseStack var2, SubmitNodeCollector var3, int var4, Identifier var5, boolean var6, Operation<Void> var7) {
      if (this.C) {
         K(var2, false);
      }

      var7.call(new Object[]{var1, var2, var3, var4, var5, var6});
      if (this.C) {
         x3.B(InteractionHand.MAIN_HAND, false, var2, ((PlayerModel)var1.getModel()).rightArm);
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
   private void c(AvatarRenderer<?> var1, PoseStack var2, SubmitNodeCollector var3, int var4, Identifier var5, boolean var6, Operation<Void> var7) {
      if (this.C) {
         K(var2, true);
      }

      var7.call(new Object[]{var1, var2, var3, var4, var5, var6});
      if (this.C) {
         x3.B(InteractionHand.MAIN_HAND, true, var2, ((PlayerModel)var1.getModel()).leftArm);
      }
   }

   @Inject(
      method = "renderMapHand",
      at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/vertex/PoseStack;popPose()V", shift = Shift.BEFORE),
      require = 1,
      allow = 1
   )
   private void X(PoseStack var1, SubmitNodeCollector var2, int var3, HumanoidArm var4, PlayerRenderState var5, CallbackInfo var6) {
      ma.U(var4, var1, var2, true);
   }

   @Unique
   private static void K(PoseStack var0, boolean var1) {
      ViewModelModule var2 = ViewModelModule.Z();
      if (var2 != null && var2.isRuntimeActive()) {
         var2.I(var0, InteractionHand.MAIN_HAND, var1);
      }
   }
}
