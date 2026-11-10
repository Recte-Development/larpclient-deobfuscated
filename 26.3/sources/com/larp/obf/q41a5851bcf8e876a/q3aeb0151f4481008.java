package com.larp.obf.q41a5851bcf8e876a;

import com.larpclient.ic;
import net.minecraft.client.gui.font.FontManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FontManager.class)
public class q3aeb0151f4481008 {
   @Inject(method = {"apply", "updateOptions"}, at = @At("TAIL"))
   private void f(CallbackInfo var1) {
      ic.C();
   }
}
