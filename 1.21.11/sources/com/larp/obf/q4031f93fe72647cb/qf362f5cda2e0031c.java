package com.larp.obf.q4031f93fe72647cb;

import com.larpclient.uw;
import net.minecraft.class_378;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(class_378.class)
public class qf362f5cda2e0031c {
   @Inject(method = {"method_51614", "method_57024"}, at = @At("TAIL"))
   private void d(CallbackInfo var1) {
      uw.W();
   }
}
