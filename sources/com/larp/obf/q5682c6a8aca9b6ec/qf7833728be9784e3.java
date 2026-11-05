package com.larp.obf.q5682c6a8aca9b6ec;

import com.larpclient.fg;
import com.larpclient.hp;
import com.larpclient.m7;
import com.larpclient.uv;
import com.larpclient.yb;
import com.larpclient.ym;
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
public class qf7833728be9784e3 {
   @Unique
   private float C;
   @Unique
   private float i;
   @Unique
   private InteractionHand g = InteractionHand.MAIN_HAND;
   @Unique
   private ItemStack l = ItemStack.EMPTY;
   @Unique
   private HumanoidArm J = HumanoidArm.RIGHT;
   @Unique
   private boolean U;

   @Shadow
   private void swingArm(float var1, PoseStack var2, int var3, HumanoidArm var4) {
      throw new AssertionError("Mixin shadow");
   }

   @Shadow
   private void applyItemArmTransform(PoseStack var1, HumanoidArm var2, float var3) {
      throw new AssertionError("Mixin shadow");
   }

   @Inject(method = "submitHandsWithItems", at = @At("HEAD"))
   private void l(float var1, PoseStack var2, SubmitNodeCollector var3, PlayerRenderState var4, FirstPersonHandsAndItemsRenderState var5, CallbackInfo var6) {
      fg.Z();
   }

   @Inject(method = "submitArmWithItem", at = @At("HEAD"), cancellable = true)
   private void T(
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
      this.g = var5;
      this.C = var6;
      this.i = var8;
      this.l = var7;
      this.J = var13 != null && var5 != InteractionHand.MAIN_HAND ? var13.getMainArm().getOpposite() : (var13 == null ? HumanoidArm.RIGHT : var13.getMainArm());
      if (var13 != null && yb.f(var13, var5, var7, var6, var8, var3, var9, var10, var11)) {
         var12.cancel();
      } else {
         var9.pushPose();
         ViewModelModule var14 = ViewModelModule.p();
         if (var14 != null && var14.isRuntimeActive()) {
            var14.X(var9, var5, this.J == HumanoidArm.LEFT);
         }
      }
   }

   @Inject(method = "submitArmWithItem", at = @At("RETURN"))
   private void R(
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
   private void b(FirstPersonHandsAndItemsRenderer var1, float var2, PoseStack var3, int var4, HumanoidArm var5) {
      AnimationsModule var6 = AnimationsModule.K();
      if (var6 != null && var6.s(var5)) {
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
   private void P(FirstPersonHandsAndItemsRenderer var1, PoseStack var2, HumanoidArm var3, float var4) {
      AnimationsModule var5 = AnimationsModule.K();
      boolean var6 = var5 != null && var5.s(var3) && hp.m(this.l).type() == SwingAnimationType.WHACK;
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
   private void X(ItemStackRenderState var1, PoseStack var2, SubmitNodeCollector var3, int var4, int var5, int var6, Operation<Void> var7) {
      boolean var8 = this.J == HumanoidArm.LEFT;
      ItemDisplayContext var9 = var8 ? ItemDisplayContext.FIRST_PERSON_LEFT_HAND : ItemDisplayContext.FIRST_PERSON_RIGHT_HAND;
      if (!this.l.isEmpty()) {
         m7.d(this.J, var2, var3, true);
      }

      var2.pushPose();

      try {
         ViewModelModule var10 = ViewModelModule.p();
         if (var10 != null && var10.isRuntimeActive()) {
            var10.o(var2, this.g, var8);
         }

         AnimationsModule var11 = AnimationsModule.K();
         if (var11 != null && !this.l.isEmpty() && !this.l.has(DataComponents.MAP_ID)) {
            var11.p(var2);
         }

         SubmitNodeCollector var12 = uv.m(var3);
         fg.E(this.g, var8, var2, var1);
         ym.v(var1, var2, var12, var3, this.l, var9, this.g, this.C, this.i, var4, var5, var6);
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
   private void L(
      FirstPersonHandsAndItemsRenderer var1, PoseStack var2, SubmitNodeCollector var3, int var4, HumanoidArm var5, PlayerRenderState var6, Operation<Void> var7
   ) {
      this.U = true;

      try {
         var7.call(new Object[]{var1, var2, var3, var4, var5, var6});
      } finally {
         this.U = false;
      }

      LocalPlayer var8 = Minecraft.getInstance().player;
      if (var8 != null) {
         ItemStack var9 = var5 == var8.getMainArm() ? var8.getMainHandItem() : var8.getOffhandItem();
         m7.d(var5, var2, var3, !var9.isEmpty());
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
   private void u(AvatarRenderer<?> var1, PoseStack var2, SubmitNodeCollector var3, int var4, Identifier var5, boolean var6, Operation<Void> var7) {
      if (this.U) {
         L(var2, false);
      }

      var7.call(new Object[]{var1, var2, var3, var4, var5, var6});
      if (this.U) {
         fg.J(InteractionHand.MAIN_HAND, false, var2, ((PlayerModel)var1.getModel()).rightArm);
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
   private void J(AvatarRenderer<?> var1, PoseStack var2, SubmitNodeCollector var3, int var4, Identifier var5, boolean var6, Operation<Void> var7) {
      if (this.U) {
         L(var2, true);
      }

      var7.call(new Object[]{var1, var2, var3, var4, var5, var6});
      if (this.U) {
         fg.J(InteractionHand.MAIN_HAND, true, var2, ((PlayerModel)var1.getModel()).leftArm);
      }
   }

   @Inject(
      method = "renderMapHand",
      at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/vertex/PoseStack;popPose()V", shift = Shift.BEFORE),
      require = 1,
      allow = 1
   )
   private void Z(PoseStack var1, SubmitNodeCollector var2, int var3, HumanoidArm var4, PlayerRenderState var5, CallbackInfo var6) {
      m7.d(var4, var1, var2, true);
   }

   @Unique
   private static void L(PoseStack var0, boolean var1) {
      ViewModelModule var2 = ViewModelModule.p();
      if (var2 != null && var2.isRuntimeActive()) {
         var2.u(var0, InteractionHand.MAIN_HAND, var1);
      }
   }
}
