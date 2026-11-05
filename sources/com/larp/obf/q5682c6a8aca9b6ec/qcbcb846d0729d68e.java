package com.larp.obf.q5682c6a8aca9b6ec;

import com.larpclient.j_;
import net.minecraft.client.gui.font.FontManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FontManager.class)
public class qcbcb846d0729d68e {
   @Inject(method = {"apply", "updateOptions"}, at = @At("TAIL"))
   private void T(CallbackInfo var1) {
      j_.H();
   }
}
