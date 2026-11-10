package com.larp.obf.q4031f93fe72647cb;

import com.larpclient.bn;
import com.larpclient.module.AutoDrainModule;
import com.larpclient.module.CrystalTapModule;
import net.minecraft.class_1268;
import net.minecraft.class_1269;
import net.minecraft.class_1297;
import net.minecraft.class_1657;
import net.minecraft.class_3965;
import net.minecraft.class_3966;
import net.minecraft.class_636;
import net.minecraft.class_746;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(class_636.class)
public abstract class qe0dce64439372487 {
   @Inject(method = "method_2919(Lnet/minecraft/class_1657;Lnet/minecraft/class_1268;)Lnet/minecraft/class_1269;", at = @At("HEAD"))
   private void b(class_1657 var1, class_1268 var2, CallbackInfoReturnable<class_1269> var3) {
      AutoDrainModule.v(var1, var2);
   }

   @Inject(method = "method_2919(Lnet/minecraft/class_1657;Lnet/minecraft/class_1268;)Lnet/minecraft/class_1269;", at = @At("RETURN"))
   private void x(class_1657 var1, class_1268 var2, CallbackInfoReturnable<class_1269> var3) {
      bn.b(var1);
   }

   @Inject(method = "method_2896(Lnet/minecraft/class_746;Lnet/minecraft/class_1268;Lnet/minecraft/class_3965;)Lnet/minecraft/class_1269;", at = @At("HEAD"))
   private void X(class_746 var1, class_1268 var2, class_3965 var3, CallbackInfoReturnable<class_1269> var4) {
      CrystalTapModule.y(var1, var2, var3);
   }

   @Inject(method = "method_2896(Lnet/minecraft/class_746;Lnet/minecraft/class_1268;Lnet/minecraft/class_3965;)Lnet/minecraft/class_1269;", at = @At("RETURN"))
   private void F(class_746 var1, class_1268 var2, class_3965 var3, CallbackInfoReturnable<class_1269> var4) {
      bn.b(var1);
      CrystalTapModule.Z((class_1269)var4.getReturnValue());
   }

   @Inject(
      method = "method_2917(Lnet/minecraft/class_1657;Lnet/minecraft/class_1297;Lnet/minecraft/class_3966;Lnet/minecraft/class_1268;)Lnet/minecraft/class_1269;",
      at = @At("RETURN")
   )
   private void x(class_1657 var1, class_1297 var2, class_3966 var3, class_1268 var4, CallbackInfoReturnable<class_1269> var5) {
      bn.b(var1);
   }

   @Inject(
      method = "method_2905(Lnet/minecraft/class_1657;Lnet/minecraft/class_1297;Lnet/minecraft/class_1268;)Lnet/minecraft/class_1269;",
      at = @At("RETURN")
   )
   private void N(class_1657 var1, class_1297 var2, class_1268 var3, CallbackInfoReturnable<class_1269> var4) {
      bn.b(var1);
   }

   @Inject(method = "method_2897(Lnet/minecraft/class_1657;)V", at = @At("RETURN"))
   private void Y(class_1657 var1, CallbackInfo var2) {
      bn.b(var1);
   }
}
