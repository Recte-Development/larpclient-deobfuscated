package com.larp.obf.q5682c6a8aca9b6ec.qffa8ebc9988eafc1;

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
public abstract class q39fe0e170ca05589 {
   @Shadow
   private Optional<Identifier> location;
   @Shadow
   private int activeColorTargetStateCount;

   @Shadow
   public abstract Builder withColorTargetState(ColorTargetState var1);

   @Inject(method = "build", at = @At("HEAD"))
   private void r(CallbackInfoReturnable<RenderPipeline> var1) {
      if (this.activeColorTargetStateCount == 0 && this.location.isPresent() && this.location.get().getNamespace().startsWith("larp")) {
         this.withColorTargetState(ColorTargetState.DEFAULT);
      }
   }
}
