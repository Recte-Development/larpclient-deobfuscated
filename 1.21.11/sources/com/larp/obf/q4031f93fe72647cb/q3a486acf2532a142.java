package com.larp.obf.q4031f93fe72647cb;

import com.larpclient.module.GUIAnimationModule;
import net.minecraft.class_266;
import net.minecraft.class_269;
import net.minecraft.class_332;
import net.minecraft.class_355;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(class_355.class)
public class q3a486acf2532a142 {
   @Unique
   private boolean Y;

   @Inject(method = "method_1919", at = @At("HEAD"))
   private void Y(class_332 var1, int var2, class_269 var3, class_266 var4, CallbackInfo var5) {
      GUIAnimationModule var6 = GUIAnimationModule.Z();
      this.Y = var6 != null && var6.c();
      if (this.Y) {
         var1.method_51448().pushMatrix();
         var1.method_51448().translate(0.0F, -var1.method_51443() * (1.0F - var6.L()));
      }
   }

   @Inject(method = "method_1919", at = @At("RETURN"))
   private void A(class_332 var1, int var2, class_269 var3, class_266 var4, CallbackInfo var5) {
      if (this.Y) {
         var1.method_51448().popMatrix();
         this.Y = false;
      }
   }
}
