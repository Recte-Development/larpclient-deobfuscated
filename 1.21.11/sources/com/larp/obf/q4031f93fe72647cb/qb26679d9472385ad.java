package com.larp.obf.q4031f93fe72647cb;

import com.larpclient.module.FakeLagModule;
import net.minecraft.class_746;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(class_746.class)
public class qb26679d9472385ad {
   @Shadow
   private int field_3923;
   @Shadow
   private float field_3925;
   @Shadow
   private float field_3941;

   @Inject(method = "method_3136", at = @At("HEAD"))
   private void P(CallbackInfo var1) {
      if (FakeLagModule.h((class_746)this)) {
         this.field_3923 = 20;
         this.field_3925 = Float.NaN;
         this.field_3941 = Float.NaN;
      }
   }
}
