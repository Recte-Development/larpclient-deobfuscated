package com.larp.obf.q41a5851bcf8e876a;

import com.larpclient._q;
import com.larpclient.hr;
import com.larpclient.jr;
import com.larpclient.qn;
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
public abstract class q787af4a0a234f353 {
   @Inject(
      method = "submit(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;ILnet/minecraft/client/renderer/entity/state/ArmedEntityRenderState;FF)V",
      at = @At("HEAD"),
      cancellable = true
   )
   private void x(PoseStack var1, SubmitNodeCollector var2, int var3, ArmedEntityRenderState var4, float var5, float var6, CallbackInfo var7) {
      if (var4 instanceof AvatarRenderState var8 && ((RenderLayer)this).getParentModel() instanceof PlayerModel var9) {
         try {
            if (jr.A(var9, var8, var1, var2, var3)) {
               var7.cancel();
               return;
            }
         } catch (RuntimeException var13) {
            _q.g("Emotes", "Cash fan submission failed; held items stay visible", var13);
         }

         try {
            if (hr.s(var9, var8, var1, var2, var3)) {
               var7.cancel();
               return;
            }
         } catch (RuntimeException var12) {
            _q.g("Emotes", "Rifle submission failed; held items stay visible", var12);
         }

         try {
            if (qn.M(var9, var8, var1, var2, var3)) {
               var7.cancel();
            }
         } catch (RuntimeException var11) {
            _q.g("Emotes", "Pickaxe submission failed; held items stay visible", var11);
         }
      }
   }
}
