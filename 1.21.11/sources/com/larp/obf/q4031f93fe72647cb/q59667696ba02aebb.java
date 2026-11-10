package com.larp.obf.q4031f93fe72647cb;

import com.larpclient.g1;
import com.larpclient.ha;
import com.larpclient.q6;
import net.minecraft.class_10055;
import net.minecraft.class_591;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(class_591.class)
public abstract class q59667696ba02aebb implements q6 {
   private static final String H = "setupAnim(Lnet/minecraft/client/renderer/entity/state/AvatarRenderState;)V";
   @Unique
   private boolean N;
   @Unique
   private boolean Y;

   @Override
   public void larp$markSkin() {
      this.N = true;
   }

   @Inject(method = "method_62110(Lnet/minecraft/class_10055;)V", at = @At("HEAD"))
   private void h(class_10055 var1, CallbackInfo var2) {
      if (this.Y) {
         ((class_591)this).field_3398.field_3665 = true;
         this.Y = false;
      }
   }

   @Inject(method = "method_62110(Lnet/minecraft/class_10055;)V", at = @At("TAIL"))
   private void e(class_10055 var1, CallbackInfo var2) {
      g1 var3 = (g1)var1;
      if (ha.g((class_591)this, this.N, var3.larp$headless(), var3.larp$korblox())) {
         this.Y = true;
      }
   }
}
