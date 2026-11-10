package com.larp.obf.q4031f93fe72647cb;

import com.larpclient.module.GUIAnimationModule;
import net.minecraft.class_1735;
import net.minecraft.class_332;
import net.minecraft.class_465;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(class_465.class)
public abstract class q753d48e088c96e46 {
   @Shadow
   protected class_1735 field_2787;
   @Unique
   private boolean l;

   @Inject(method = "method_2385", at = @At("HEAD"))
   private void x(class_332 var1, class_1735 var2, int var3, int var4, CallbackInfo var5) {
      GUIAnimationModule var6 = GUIAnimationModule.Z();
      this.l = var6 != null && var6.L();
      if (this.l) {
         float var7 = var6.R(var2, var2 == this.field_2787 && var2.method_7682());
         var1.method_51448().pushMatrix();
         var1.method_51448().translate(var2.field_7873 + 8.0F, var2.field_7872 + 8.0F);
         var1.method_51448().scale(var7, var7);
         var1.method_51448().translate(-var2.field_7873 - 8.0F, -var2.field_7872 - 8.0F);
      }
   }

   @Inject(method = "method_2385", at = @At("RETURN"))
   private void E(class_332 var1, class_1735 var2, int var3, int var4, CallbackInfo var5) {
      if (this.l) {
         var1.method_51448().popMatrix();
         this.l = false;
      }
   }
}
