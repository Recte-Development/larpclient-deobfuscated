package com.larp.obf.q4031f93fe72647cb;

import com.larpclient.module.CustomFOVModule;
import com.larpclient.module.GUIAnimationModule;
import com.larpclient.module.RemovalsModule;
import com.larpclient.module.ZoomModule;
import net.minecraft.class_1291;
import net.minecraft.class_1294;
import net.minecraft.class_310;
import net.minecraft.class_3532;
import net.minecraft.class_4184;
import net.minecraft.class_4587;
import net.minecraft.class_6880;
import net.minecraft.class_746;
import net.minecraft.class_757;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(class_757.class)
public class q37fc3886079b0d58 {
   @Inject(method = "method_3198", at = @At("HEAD"), cancellable = true)
   private void G(class_4587 var1, float var2, CallbackInfo var3) {
      if (RemovalsModule.o("hurtCam")) {
         var3.cancel();
      }
   }

   @Redirect(method = "method_3188", at = @At(value = "INVOKE", target = "Lnet/minecraft/class_3532;method_16439(FFF)F"), require = 1, allow = 1)
   private float j(float var1, float var2, float var3) {
      return RemovalsModule.e() ? 0.0F : class_3532.method_16439(var1, var2, var3);
   }

   @Redirect(
      method = "method_3188",
      at = @At(value = "INVOKE", target = "Lnet/minecraft/class_746;method_66279(Lnet/minecraft/class_6880;F)F"),
      require = 1,
      allow = 1
   )
   private float L(class_746 var1, class_6880<class_1291> var2, float var3) {
      return var2.equals(class_1294.field_5916) && RemovalsModule.e() ? 0.0F : var1.method_66279(var2, var3);
   }

   @Inject(method = "method_3196", at = @At("RETURN"), cancellable = true)
   private void q(class_4184 var1, float var2, boolean var3, CallbackInfoReturnable<Float> var4) {
      class_757 var5 = (class_757)this;
      if (!var5.method_35765()) {
         if (RemovalsModule.o("fov")) {
            var4.setReturnValue(CustomFOVModule.W(var3 ? ((Integer)class_310.method_1551().field_1690.method_41808().method_41753()).floatValue() : 70.0F));
         } else {
            GUIAnimationModule var6 = GUIAnimationModule.Z();
            if (var6 != null && var3) {
               var4.setReturnValue(var6.e((Float)var4.getReturnValue()));
            }

            float var7 = CustomFOVModule.W((Float)var4.getReturnValue());
            var4.setReturnValue(ZoomModule.e(var7, var5.method_35765()));
         }
      }
   }
}
