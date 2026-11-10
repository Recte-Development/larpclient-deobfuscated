package com.larp.obf.q4031f93fe72647cb;

import com.larpclient.bp;
import com.larpclient.module.GUIAnimationModule;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.class_310;
import net.minecraft.class_327;
import net.minecraft.class_332;
import net.minecraft.class_342;
import net.minecraft.class_408;
import net.minecraft.class_5481;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(class_342.class)
public abstract class q91a0fd665cfb1f14 {
   @Shadow
   private String field_2092;
   @Shadow
   private int field_60435;
   @Unique
   private final bp p = new bp();
   @Unique
   private int Y;

   @Shadow
   public abstract int method_1859();

   @Inject(method = "method_48579", at = @At("HEAD"))
   private void j(class_332 var1, int var2, int var3, float var4, CallbackInfo var5) {
      GUIAnimationModule var6 = GUIAnimationModule.Z();
      this.p.r(this.field_2092, System.nanoTime(), var6 != null && var6.r() && class_310.method_1551().field_1755 instanceof class_408);
   }

   @WrapOperation(
      method = "method_48579",
      at = @At(value = "INVOKE", target = "Lnet/minecraft/class_342;method_73211(Ljava/lang/String;I)Lnet/minecraft/class_5481;")
   )
   private class_5481 k(class_342 var1, String var2, int var3, Operation<class_5481> var4) {
      this.Y = var3;
      return (class_5481)var4.call(new Object[]{var1, var2, var3});
   }

   @WrapOperation(
      method = "method_48579",
      at = @At(value = "INVOKE", target = "Lnet/minecraft/class_332;method_51430(Lnet/minecraft/class_327;Lnet/minecraft/class_5481;IIIZ)V")
   )
   private void n(class_332 var1, class_327 var2, class_5481 var3, int var4, int var5, int var6, boolean var7, Operation<Void> var8) {
      if (!this.p.q()) {
         var8.call(new Object[]{var1, var2, var3, var4, var5, var6, var7});
      } else {
         float[] var9 = new float[]{0.0F};
         bp.s var10 = new bp.s(this.Y);
         class_342 var11 = (class_342)this;
         var1.method_44379(this.field_60435, var11.method_46427(), this.field_60435 + this.method_1859(), var11.method_46427() + var11.method_25364());

         try {
            var3.accept((var10x, var11x, var12) -> {
               int var13 = var10.t(var12);
               class_5481 var14x = class_5481.method_30741(var12, var11x);
               var1.method_51448().pushMatrix();
               var1.method_51448().translate(var9[0], (float)this.p.j(var13));

               try {
                  var8.call(new Object[]{var1, var2, var14x, var4, var5, this.p.d(var6, var13), var7});
               } finally {
                  var1.method_51448().popMatrix();
               }

               var9[0] += var2.method_27527().method_30875(var14x);
               return true;
            });
         } finally {
            var1.method_44380();
         }
      }
   }
}
