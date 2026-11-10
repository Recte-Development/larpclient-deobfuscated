package com.larp.obf.q4031f93fe72647cb;

import com.larpclient.gv;
import com.larpclient.q8;
import com.larpclient.qj;
import com.larpclient.module.SelfVisualsModule;
import net.minecraft.class_1007;
import net.minecraft.class_1058;
import net.minecraft.class_11659;
import net.minecraft.class_1921;
import net.minecraft.class_4587;
import net.minecraft.class_630;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(class_1007.class)
public class q57f092633ba91083 {
   @Redirect(
      method = "method_23205",
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/class_11659;method_73491(Lnet/minecraft/class_630;Lnet/minecraft/class_4587;Lnet/minecraft/class_1921;IILnet/minecraft/class_1058;)V"
      )
   )
   private void o(class_11659 var1, class_630 var2, class_4587 var3, class_1921 var4, int var5, int var6, class_1058 var7) {
      try {
         if (!qj.u() && gv.l(var2, var3, var1, var4, var5, var6, var7)) {
            return;
         }
      } catch (RuntimeException var10) {
         q8.O("SelfVisuals", "First-person arm stage=material shader=model/material failed; restoring vanilla arms", var10);
         SelfVisualsModule var9 = SelfVisualsModule.o();
         if (var9 != null) {
            var9.disable();
         }
      }

      var1.method_73491(var2, var3, var4, var5, var6, var7);
   }
}
