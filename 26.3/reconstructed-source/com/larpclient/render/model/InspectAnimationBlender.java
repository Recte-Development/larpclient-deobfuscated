package com.larpclient.render.model;

import com.larpclient.render.entity.ViewModelTransforms;
import com.larpclient.util.misc.LarpLog;

public final class InspectAnimationBlender {
   public static final double rELEASE_SECONDS = 0.14;
   private static final InspectAnimationBlender.BlendState nONE = new InspectAnimationBlender.BlendState(0.0F, 0.0F);
   private float phase;
   private float weight;
   private float releaseStartWeight;
   private double releaseStartTime = Double.NaN;

   public void reset() {
      this.phase = this.weight = this.releaseStartWeight = 0.0F;
      this.releaseStartTime = Double.NaN;
   }

   public InspectAnimationBlender.BlendState update(WeaponAnimator.Frame var1, double var2) {
      if (var1.stage == 2) {
         this.releaseStartTime = Double.NaN;
         this.phase = var1.progress;
         this.weight = ViewModelTransforms.easeInOut(this.phase);
         return new InspectAnimationBlender.BlendState(this.phase, this.weight);
      }

      if (var1.interruptedAttack && this.weight > 0.0F) {
         this.releaseStartTime = var2;
         this.releaseStartWeight = this.weight;
         LarpLog.debug("SwordModels", "Watch inspect interrupted at phase {}, releasing over {}s", String.format("%.2f", this.phase), 0.14);
      }

      this.weight = 0.0F;
      if (Double.isFinite(this.releaseStartTime) && Double.isFinite(var2)) {
         float var4 = (float)Math.clamp((var2 - this.releaseStartTime) / 0.14, 0.0, 1.0);
         if (!(var4 >= 1.0F) && !(var2 < this.releaseStartTime)) {
            float var5 = 1.0F - var4 * var4 * (3.0F - 2.0F * var4);
            return new InspectAnimationBlender.BlendState(this.phase, this.releaseStartWeight * var5);
         } else {
            this.releaseStartTime = Double.NaN;
            return nONE;
         }
      } else {
         return nONE;
      }
   }

   public record BlendState(float phase, float weight) {
   }
}
