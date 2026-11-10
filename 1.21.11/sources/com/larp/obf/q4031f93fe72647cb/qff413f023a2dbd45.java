package com.larp.obf.q4031f93fe72647cb;

import com.larpclient.module.RemovalsModule;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import net.minecraft.class_1113;
import net.minecraft.class_1140;
import net.minecraft.class_1140.class_11518;
import net.minecraft.class_4235.class_4236;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(class_1140.class)
public abstract class qff413f023a2dbd45 {
   @Shadow
   @Final
   private Map<class_1113, class_4236> field_18950;
   @Unique
   private final Set<class_1113> c = new HashSet<>();

   @Shadow
   private float method_4853(class_1113 var1) {
      throw new AssertionError("Mixin shadow");
   }

   @Inject(method = "method_4857", at = @At("TAIL"))
   private void S(CallbackInfo var1) {
      if (RemovalsModule.o() != null && (RemovalsModule.o().isRuntimeActive() || !this.c.isEmpty())) {
         this.c.retainAll(this.field_18950.keySet());

         for (Entry var3 : this.field_18950.entrySet()) {
            class_1113 var4 = (class_1113)var3.getKey();
            if (RemovalsModule.J(var4.method_4775().toString())) {
               if (this.c.add(var4)) {
                  ((class_4236)var3.getValue()).method_19735(var0 -> var0.method_19647(0.0F));
               }
            } else if (this.c.remove(var4)) {
               float var5 = this.method_4853(var4);
               ((class_4236)var3.getValue()).method_19735(var1x -> var1x.method_19647(var5));
            }
         }
      }
   }

   @Inject(method = "method_4854", at = @At("HEAD"), cancellable = true)
   private void R(class_1113 var1, CallbackInfoReturnable<class_11518> var2) {
      if (RemovalsModule.J(var1.method_4775().toString())) {
         var2.setReturnValue(class_11518.field_60956);
      }
   }

   @Inject(method = "method_4853(Lnet/minecraft/class_1113;)F", at = @At("RETURN"), cancellable = true)
   private void N(class_1113 var1, CallbackInfoReturnable<Float> var2) {
      if (RemovalsModule.J(var1.method_4775().toString())) {
         var2.setReturnValue(0.0F);
      }
   }
}
