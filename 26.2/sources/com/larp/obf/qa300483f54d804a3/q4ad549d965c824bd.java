package com.larp.obf.qa300483f54d804a3;

import com.larpclient.bj;
import net.minecraft.client.gui.font.FontManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FontManager.class)
public class q4ad549d965c824bd {
   @Inject(method = {"apply", "updateOptions"}, at = @At("TAIL"))
   private void T(CallbackInfo var1) {
      bj.I();
   }
}
