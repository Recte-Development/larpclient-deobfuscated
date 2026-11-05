package com.larp.obf.qbac0b262827a15ac.q1c197ccb5b4bc550;

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
public abstract class qd9dc3502d359d44e {
   @Shadow
   private Optional<Identifier> location;
   @Shadow
   private int activeColorTargetStateCount;

   @Shadow
   public abstract Builder withColorTargetState(ColorTargetState var1);

   @Inject(method = "build", at = @At("HEAD"))
   private void p(CallbackInfoReturnable<RenderPipeline> var1) {
      if (this.activeColorTargetStateCount == 0 && this.location.isPresent() && this.location.get().getNamespace().startsWith("larp")) {
         this.withColorTargetState(ColorTargetState.DEFAULT);
      }
   }
}
