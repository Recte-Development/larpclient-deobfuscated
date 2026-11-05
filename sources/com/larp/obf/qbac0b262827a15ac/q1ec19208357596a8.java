package com.larp.obf.qbac0b262827a15ac;

import com.larpclient.k6;
import net.minecraft.client.gui.font.FontManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FontManager.class)
public class q1ec19208357596a8 {
   @Inject(method = {"apply", "updateOptions"}, at = @At("TAIL"))
   private void X(CallbackInfo var1) {
      k6.V();
   }
}
