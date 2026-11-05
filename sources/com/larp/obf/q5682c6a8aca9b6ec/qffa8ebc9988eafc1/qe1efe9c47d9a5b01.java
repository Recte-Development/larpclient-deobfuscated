package com.larp.obf.q5682c6a8aca9b6ec.qffa8ebc9988eafc1;

import com.larpclient.r4;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.renderpearl.api.commands.RenderPass;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import net.minecraft.client.renderer.SubmitNodeCollection;
import net.minecraft.client.renderer.SubmitNodeStorage;
import net.minecraft.client.renderer.feature.FeatureFrameContext;
import net.minecraft.client.renderer.feature.FeatureRenderDispatcher.PreparedFrame;
import net.minecraft.client.renderer.feature.phase.FeatureRenderPhase;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PreparedFrame.class)
public abstract class qe1efe9c47d9a5b01 {
   @Unique
   private static boolean i;
   @Shadow
   private @Nullable FeatureFrameContext context;
   @Shadow
   private @Nullable SubmitNodeStorage submitNodeStorage;
   @Unique
   private boolean P;

   @Shadow
   private void executePhase(FeatureRenderPhase<?> var1, FeatureFrameContext var2, RenderPass var3) {
      throw new AssertionError();
   }

   @WrapOperation(
      method = "executeTranslucent",
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/renderer/feature/FeatureRenderDispatcher$PreparedFrame;executePhase(Lnet/minecraft/client/renderer/feature/phase/FeatureRenderPhase;Lnet/minecraft/client/renderer/feature/FeatureFrameContext;Lcom/mojang/renderpearl/api/commands/RenderPass;)V"
      )
   )
   private void Z(
      PreparedFrame var1, FeatureRenderPhase<?> var2, FeatureFrameContext var3, RenderPass var4, Operation<Void> var5, @Local SubmitNodeCollection var6
   ) {
      if (var2 == var6.nameTags) {
         this.P = true;
      } else {
         var5.call(new Object[]{var1, var2, var3, var4});
      }
   }

   @Inject(method = "executeTranslucentAfterTerrain", at = @At("HEAD"))
   private void A(RenderPass var1, CallbackInfo var2) {
      if (this.P) {
         this.P = false;
         if (this.context != null && this.submitNodeStorage != null) {
            ObjectIterator var3 = this.submitNodeStorage.getSubmitsPerOrder().values().iterator();

            while (var3.hasNext()) {
               SubmitNodeCollection var4 = (SubmitNodeCollection)var3.next();
               this.executePhase(var4.nameTags, this.context, var1);
            }

            if (!i) {
               i = true;
               r4.V("Render", "Name tags draw after translucent terrain; water and glass keep label backgrounds");
            }
         }
      }
   }
}
