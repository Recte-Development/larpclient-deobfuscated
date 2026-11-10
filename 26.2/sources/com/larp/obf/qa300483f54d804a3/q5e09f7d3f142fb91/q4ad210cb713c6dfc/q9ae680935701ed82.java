package com.larp.obf.qa300483f54d804a3.q5e09f7d3f142fb91.q4ad210cb713c6dfc;

import com.larpclient.c;
import net.minecraft.client.renderer.entity.state.EndCrystalRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(EndCrystalRenderState.class)
public class q9ae680935701ed82 implements c {
   @Unique
   private int E = Integer.MIN_VALUE;

   @Override
   public int larpCrystalId() {
      return this.E;
   }

   @Override
   public void larpCrystalId(int var1) {
      this.E = var1;
   }
}
