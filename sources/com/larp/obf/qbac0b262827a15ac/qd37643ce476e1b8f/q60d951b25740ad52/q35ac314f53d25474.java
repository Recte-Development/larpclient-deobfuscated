package com.larp.obf.qbac0b262827a15ac.qd37643ce476e1b8f.q60d951b25740ad52;

import com.larpclient.ev;
import net.minecraft.client.renderer.entity.state.EndCrystalRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(EndCrystalRenderState.class)
public class q35ac314f53d25474 implements ev {
   @Unique
   private int U = Integer.MIN_VALUE;

   @Override
   public int larpCrystalId() {
      return this.U;
   }

   @Override
   public void larpCrystalId(int var1) {
      this.U = var1;
   }
}
