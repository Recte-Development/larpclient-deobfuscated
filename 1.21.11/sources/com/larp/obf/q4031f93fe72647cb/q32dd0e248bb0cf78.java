package com.larp.obf.q4031f93fe72647cb;

import com.larpclient.module.XRayModule;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import java.util.List;
import net.minecraft.class_10889;
import net.minecraft.class_1920;
import net.minecraft.class_2338;
import net.minecraft.class_2680;
import net.minecraft.class_4587;
import net.minecraft.class_4588;
import net.minecraft.class_777;
import net.minecraft.class_778;
import net.minecraft.class_778.class_10931;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(class_778.class)
public abstract class q32dd0e248bb0cf78 {
   private static final String E = "Lnet/minecraft/client/renderer/block/ModelBlockRenderer;renderModelFaceFlat(Lnet/minecraft/world/level/BlockAndTintGetter;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/core/BlockPos;IIZLcom/mojang/blaze3d/vertex/PoseStack;Lcom/mojang/blaze3d/vertex/VertexConsumer;Ljava/util/List;Lnet/minecraft/client/renderer/block/ModelBlockRenderer$CommonRenderStorage;)V";

   @Inject(method = "method_3361", at = @At("HEAD"), cancellable = true)
   private void b(
      class_1920 var1, List<class_10889> var2, class_2680 var3, class_2338 var4, class_4587 var5, class_4588 var6, boolean var7, int var8, CallbackInfo var9
   ) {
      if (XRayModule.m() && XRayModule.r(var3.method_26204())) {
         ((class_778)this).method_3373(var1, var2, var3, var4, var5, var6, var7, var8);
         var9.cancel();
      }
   }

   @WrapOperation(
      method = "method_3373",
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/class_778;method_3370(Lnet/minecraft/class_1920;Lnet/minecraft/class_2680;Lnet/minecraft/class_2338;IIZLnet/minecraft/class_4587;Lnet/minecraft/class_4588;Ljava/util/List;Lnet/minecraft/class_778$class_10931;)V"
      )
   )
   private void T(
      class_778 var1,
      class_1920 var2,
      class_2680 var3,
      class_2338 var4,
      int var5,
      int var6,
      boolean var7,
      class_4587 var8,
      class_4588 var9,
      List<class_777> var10,
      class_10931 var11,
      Operation<Void> var12
   ) {
      boolean var13 = XRayModule.m() && XRayModule.r(var3.method_26204());
      var12.call(new Object[]{var1, var2, var3, var4, var13 ? 15728880 : var5, var6, var7 && !var13, var8, var9, var10, var11});
   }
}
