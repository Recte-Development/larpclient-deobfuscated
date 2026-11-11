package com.larp.obf.q41a5851bcf8e876a;

import com.larpclient.util.entity.GroundedFloatingState;
import net.minecraft.client.renderer.entity.state.ItemEntityRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(ItemEntityRenderState.class)
public abstract class qf0529de3b6d15730$q65e3bd9f241ae13f implements GroundedFloatingState {
   @Unique
   private boolean U;
   @Unique
   private boolean A;

   @Override
   public void larp$setGrounded(boolean var1) {
      this.U = var1;
   }

   @Override
   public boolean larp$isGrounded() {
      return this.U;
   }

   @Override
   public void larp$setFloating(boolean var1) {
      this.A = var1;
   }

   @Override
   public boolean larp$isFloating() {
      return this.A;
   }
}
