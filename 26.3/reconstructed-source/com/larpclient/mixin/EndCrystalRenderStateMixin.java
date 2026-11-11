package com.larpclient.mixin;

import com.larpclient.util.entity.CrystalIdAccess;
import net.minecraft.client.renderer.entity.state.EndCrystalRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(EndCrystalRenderState.class)
public class EndCrystalRenderStateMixin implements CrystalIdAccess {
   @Unique
   private int F = Integer.MIN_VALUE;

   @Override
   public int larpCrystalId() {
      return this.F;
   }

   @Override
   public void larpCrystalId(int var1) {
      this.F = var1;
   }
}
