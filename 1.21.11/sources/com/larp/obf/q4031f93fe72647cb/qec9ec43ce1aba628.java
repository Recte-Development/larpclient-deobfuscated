package com.larp.obf.q4031f93fe72647cb;

import com.larpclient.module.BacktrackModule;
import net.minecraft.class_148;
import net.minecraft.class_2547;
import net.minecraft.class_2596;
import net.minecraft.class_2600;
import net.minecraft.class_634;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(class_634.class)
public abstract class qec9ec43ce1aba628 {
   @Redirect(method = "method_48294", at = @At(value = "INVOKE", target = "Lnet/minecraft/class_2596;method_65081(Lnet/minecraft/class_2547;)V"))
   private <T extends class_2547> void m(class_2596<T> var1, T var2) {
      Runnable var3 = () -> {
         if (var2.method_52413(var1)) {
            try {
               var1.method_65081(var2);
            } catch (Exception var4) {
               if (var4 instanceof class_148 var3x && var3x.getCause() instanceof OutOfMemoryError) {
                  throw class_2600.method_59854(var4, var1, var2);
               }

               var2.method_59807(var1, var4);
            }
         }
      };
      if (BacktrackModule.m() || !BacktrackModule.r(var1, var2, var3)) {
         var1.method_65081(var2);
      }
   }
}
