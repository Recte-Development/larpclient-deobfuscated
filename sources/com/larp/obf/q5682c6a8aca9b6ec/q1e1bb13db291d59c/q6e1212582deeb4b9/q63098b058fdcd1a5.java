package com.larp.obf.q5682c6a8aca9b6ec.q1e1bb13db291d59c.q6e1212582deeb4b9;

import com.larpclient.yn;
import net.minecraft.client.renderer.entity.state.EndCrystalRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(EndCrystalRenderState.class)
public class q63098b058fdcd1a5 implements yn {
   @Unique
   private int Y = Integer.MIN_VALUE;

   @Override
   public int larpCrystalId() {
      return this.Y;
   }

   @Override
   public void larpCrystalId(int var1) {
      this.Y = var1;
   }
}
