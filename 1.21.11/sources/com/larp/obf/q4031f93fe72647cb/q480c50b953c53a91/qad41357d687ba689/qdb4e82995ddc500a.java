package com.larp.obf.q4031f93fe72647cb.q480c50b953c53a91.qad41357d687ba689;

import com.larpclient.bb;
import net.minecraft.class_10014;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(class_10014.class)
public class qdb4e82995ddc500a implements bb {
   @Unique
   private int B = Integer.MIN_VALUE;

   @Override
   public int larpCrystalId() {
      return this.B;
   }

   @Override
   public void larpCrystalId(int var1) {
      this.B = var1;
   }
}
