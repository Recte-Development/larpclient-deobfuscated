package com.larp.obf.q4031f93fe72647cb.q480c50b953c53a91.q761e5c7844d57377;

import com.larpclient.j5;
import net.minecraft.class_287;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(class_287.class)
public abstract class qd545124a78431d8d {
   @ModifyVariable(method = "method_23919(FFFIFFIIFFF)V", at = @At("HEAD"), argsOnly = true, ordinal = 0)
   private int U(int var1, float var2, float var3, float var4) {
      return j5.y(var1, var2, var3, var4);
   }
}
