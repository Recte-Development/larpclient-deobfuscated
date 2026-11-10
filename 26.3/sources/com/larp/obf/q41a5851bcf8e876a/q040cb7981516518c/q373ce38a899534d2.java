package com.larp.obf.q41a5851bcf8e876a.q040cb7981516518c;

import com.mojang.renderpearl.api.pipeline.ColorTargetState;
import com.mojang.renderpearl.api.pipeline.RenderPipeline;
import com.mojang.renderpearl.api.pipeline.RenderPipeline.Builder;
import java.util.Optional;
import net.minecraft.resources.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Builder.class)
public abstract class q373ce38a899534d2 {
   @Shadow
   private Optional<Identifier> location;
   @Shadow
   private int activeColorTargetStateCount;

   @Shadow
   public abstract Builder withColorTargetState(ColorTargetState var1);

   @Inject(method = "build", at = @At("HEAD"))
   private void e(CallbackInfoReturnable<RenderPipeline> var1) {
      if (this.activeColorTargetStateCount == 0 && this.location.isPresent() && this.location.get().getNamespace().startsWith("larp")) {
         this.withColorTargetState(ColorTargetState.DEFAULT);
      }
   }
}
