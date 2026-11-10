package com.larp.obf.q4031f93fe72647cb.q480c50b953c53a91.qad41357d687ba689;

import com.larpclient.WalkStyleModule;
import com.larpclient.fg;
import com.larpclient.wm;
import net.minecraft.class_10055;
import net.minecraft.class_1306;
import net.minecraft.class_4587;
import net.minecraft.class_591;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(class_591.class)
public abstract class q7f793e27a73b8ec7 implements wm {
   @Unique
   private fg d;

   @Unique
   @Override
   public void larpRestoreWalkRig() {
      if (this.d != null) {
         this.d.r();
      }
   }

   @Unique
   @Override
   public void larpApplyJoints(float[] var1, class_10055 var2) {
      if (this.d == null) {
         this.d = new fg((class_591)this);
      }

      this.d.E(var1, var2);
   }

   @Inject(method = "method_62110(Lnet/minecraft/class_10055;)V", at = @At("HEAD"))
   private void f(class_10055 var1, CallbackInfo var2) {
      if (this.d != null) {
         this.d.r();
      }
   }

   @Inject(method = "method_72859(Lnet/minecraft/class_10055;Lnet/minecraft/class_1306;Lnet/minecraft/class_4587;)V", at = @At("RETURN"))
   private void E(class_10055 var1, class_1306 var2, class_4587 var3, CallbackInfo var4) {
      if (this.d != null) {
         this.d.C(var2, var3);
      }
   }

   @Inject(method = "method_62110(Lnet/minecraft/class_10055;)V", at = @At("TAIL"))
   private void H(class_10055 var1, CallbackInfo var2) {
      WalkStyleModule var3 = WalkStyleModule.T();
      if (var3 != null) {
         class_591 var4 = (class_591)this;
         float[] var5 = var3.S(var4, var1);
         if (var5 != null) {
            if (this.d == null) {
               this.d = new fg(var4);
            }

            this.d.E(var5, var1);
         }
      }
   }
}
