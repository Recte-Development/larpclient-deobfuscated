package com.larp.obf.qbac0b262827a15ac.q1c197ccb5b4bc550;

import com.larpclient.mt;
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
public abstract class q2aea472954ca4f48 {
   @Unique
   private static boolean i;
   @Shadow
   private @Nullable FeatureFrameContext context;
   @Shadow
   private @Nullable SubmitNodeStorage submitNodeStorage;
   @Unique
   private boolean F;

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
   private void j(
      PreparedFrame var1, FeatureRenderPhase<?> var2, FeatureFrameContext var3, RenderPass var4, Operation<Void> var5, @Local SubmitNodeCollection var6
   ) {
      if (var2 == var6.nameTags) {
         this.F = true;
      } else {
         var5.call(new Object[]{var1, var2, var3, var4});
      }
   }

   @Inject(method = "executeTranslucentAfterTerrain", at = @At("HEAD"))
   private void o(RenderPass var1, CallbackInfo var2) {
      if (this.F) {
         this.F = false;
         if (this.context != null && this.submitNodeStorage != null) {
            ObjectIterator var3 = this.submitNodeStorage.getSubmitsPerOrder().values().iterator();

            while (var3.hasNext()) {
               SubmitNodeCollection var4 = (SubmitNodeCollection)var3.next();
               this.executePhase(var4.nameTags, this.context, var1);
            }

            if (!i) {
               i = true;
               mt.K("Render", "Name tags draw after translucent terrain; water and glass keep label backgrounds");
            }
         }
      }
   }
}
