package com.larp.obf.q41a5851bcf8e876a.qb12a7b6d04ee2fc9.q3f4e251a0dcf56a9;

import com.larpclient.ve;
import net.minecraft.client.renderer.entity.state.EndCrystalRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(EndCrystalRenderState.class)
public class q5d4ae3115b46b6d7 implements ve {
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
