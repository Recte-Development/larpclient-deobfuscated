package com.larpclient.mixin;

import com.larpclient.render.entity.CashFanRenderer;
import com.larpclient.social.emote.PickaxeEmoteRenderer;
import com.larpclient.social.emote.RifleEmoteRenderer;
import com.larpclient.util.misc.LarpLog;
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
public abstract class ItemInHandLayerMixin_2 {
   @Inject(
      method = "submit(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;ILnet/minecraft/client/renderer/entity/state/ArmedEntityRenderState;FF)V",
      at = @At("HEAD"),
      cancellable = true
   )
   private void x(PoseStack var1, SubmitNodeCollector var2, int var3, ArmedEntityRenderState var4, float var5, float var6, CallbackInfo var7) {
      if (var4 instanceof AvatarRenderState var8 && ((RenderLayer)this).getParentModel() instanceof PlayerModel var9) {
         try {
            if (CashFanRenderer.renderCashFans(var9, var8, var1, var2, var3)) {
               var7.cancel();
               return;
            }
         } catch (RuntimeException var13) {
            LarpLog.errorOnce("Emotes", "Cash fan submission failed; held items stay visible", var13);
         }

         try {
            if (RifleEmoteRenderer.render(var9, var8, var1, var2, var3)) {
               var7.cancel();
               return;
            }
         } catch (RuntimeException var12) {
            LarpLog.errorOnce("Emotes", "Rifle submission failed; held items stay visible", var12);
         }

         try {
            if (PickaxeEmoteRenderer.renderPickaxe(var9, var8, var1, var2, var3)) {
               var7.cancel();
            }
         } catch (RuntimeException var11) {
            LarpLog.errorOnce("Emotes", "Pickaxe submission failed; held items stay visible", var11);
         }
      }
   }
}
