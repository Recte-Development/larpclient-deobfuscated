package com.larp.obf.qa300483f54d804a3;

import com.larpclient.g6;
import com.larpclient.h;
import com.larpclient.kn;
import com.larpclient.pb;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.player.PlayerModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.state.ArmedEntityRenderState;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemInHandLayer.class)
public abstract class q68f03f1da3f638a7 {
   @Inject(
      method = "submit(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;ILnet/minecraft/client/renderer/entity/state/ArmedEntityRenderState;FF)V",
      at = @At("HEAD"),
      cancellable = true
   )
   private void f(PoseStack var1, SubmitNodeCollector var2, int var3, ArmedEntityRenderState var4, float var5, float var6, CallbackInfo var7) {
      if (var4 instanceof AvatarRenderState var8 && ((RenderLayer)this).getParentModel() instanceof PlayerModel var9) {
         try {
            if (g6.d(var9, var8, var1, var2, var3)) {
               var7.cancel();
               return;
            }
         } catch (RuntimeException var13) {
            h.w("Emotes", "Cash fan submission failed; held items stay visible", var13);
         }

         try {
            if (pb.N(var9, var8, var1, var2, var3)) {
               var7.cancel();
               return;
            }
         } catch (RuntimeException var12) {
            h.w("Emotes", "Rifle submission failed; held items stay visible", var12);
         }

         try {
            if (kn.W(var9, var8, var1, var2, var3)) {
               var7.cancel();
            }
         } catch (RuntimeException var11) {
            h.w("Emotes", "Pickaxe submission failed; held items stay visible", var11);
         }
      }
   }
}
