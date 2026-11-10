package com.larp.obf.qa300483f54d804a3;

import com.larpclient.h;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import net.minecraft.client.renderer.SubmitNodeCollection;
import net.minecraft.client.renderer.SubmitNodeStorage;
import net.minecraft.client.renderer.feature.FeatureFrameContext;
import net.minecraft.client.renderer.feature.FeatureRenderDispatcher.PreparedFrame;
import net.minecraft.client.renderer.feature.phase.FeatureRenderPhase;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PreparedFrame.class)
public abstract class q454b9155ea12ad5c {
   @Unique
   private static boolean g;
   @Shadow
   private FeatureFrameContext context;
   @Shadow
   private SubmitNodeStorage submitNodeStorage;
   @Unique
   private boolean q;

   @Shadow
   private void executePhase(FeatureRenderPhase<?> var1, FeatureFrameContext var2) {
      throw new AssertionError();
   }

   @WrapOperation(
      method = "executeTranslucent",
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/renderer/feature/FeatureRenderDispatcher$PreparedFrame;executePhase(Lnet/minecraft/client/renderer/feature/phase/FeatureRenderPhase;Lnet/minecraft/client/renderer/feature/FeatureFrameContext;)V"
      )
   )
   private void s(PreparedFrame var1, FeatureRenderPhase<?> var2, FeatureFrameContext var3, Operation<Void> var4, @Local SubmitNodeCollection var5) {
      if (var2 != var5.nameTags && var2 != var5.seeThroughNameTags) {
         var4.call(new Object[]{var1, var2, var3});
      } else {
         this.q = true;
      }
   }

   @Inject(method = "executeTranslucentAfterTerrain", at = @At("HEAD"))
   private void x(CallbackInfo var1) {
      if (this.q) {
         this.q = false;
         if (this.context != null && this.submitNodeStorage != null) {
            ObjectIterator var2 = this.submitNodeStorage.getSubmitsPerOrder().values().iterator();

            while (var2.hasNext()) {
               SubmitNodeCollection var3 = (SubmitNodeCollection)var2.next();
               this.executePhase(var3.seeThroughNameTags, this.context);
               this.executePhase(var3.nameTags, this.context);
            }

            if (!g) {
               g = true;
               h.A("Render", "Name tags draw after translucent terrain; water and glass keep label backgrounds");
            }
         }
      }
   }
}
