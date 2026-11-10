package com.larp.obf.q4031f93fe72647cb.qfadb95a24329ee59;

import com.larpclient.bk;
import com.larpclient.il;
import com.larpclient.q7;
import net.minecraft.class_1661;
import net.minecraft.class_1799;
import net.minecraft.class_310;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(class_1661.class)
public class q9252b0aa10c6223f {
   @Unique
   private boolean H;

   @ModifyVariable(method = "method_61496", at = @At("HEAD"), argsOnly = true)
   private int I(int var1) {
      return ((class_1661)this).field_7546 == class_310.method_1551().field_1724 && !bk.W().E() ? il.L(var1) : var1;
   }

   @Inject(method = "method_37417", at = @At("HEAD"))
   private void N(boolean var1, CallbackInfoReturnable<class_1799> var2) {
      if (((class_1661)this).field_7546 == class_310.method_1551().field_1724) {
         il.w();
      }
   }

   @Inject(method = "method_37417", at = @At("RETURN"))
   private void x(boolean var1, CallbackInfoReturnable<class_1799> var2) {
      class_1661 var3 = (class_1661)this;
      if (var3.field_7546 == class_310.method_1551().field_1724 && !((class_1799)var2.getReturnValue()).method_7960()) {
         q7.L().I(var3.method_67532());
      }
   }

   @Inject(method = "method_61496", at = @At("HEAD"), cancellable = true)
   private void z(int var1, CallbackInfo var2) {
      if (!this.H) {
         bk var3 = bk.W();
         if (var3.E()) {
            var3.V(var1);
            if (var1 == var3.n()) {
               int var4 = (var1 + 8) % 9;
               var2.cancel();
               this.H = true;

               try {
                  ((class_1661)this).method_61496(var4);
               } finally {
                  this.H = false;
               }
            }
         }
      }
   }
}
