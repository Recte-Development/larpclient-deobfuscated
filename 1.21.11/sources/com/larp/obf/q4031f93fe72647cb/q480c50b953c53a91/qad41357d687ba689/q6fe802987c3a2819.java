package com.larp.obf.q4031f93fe72647cb.q480c50b953c53a91.qad41357d687ba689;

import com.larpclient.wm;
import net.minecraft.class_1007;
import net.minecraft.class_11659;
import net.minecraft.class_2960;
import net.minecraft.class_4587;
import net.minecraft.class_630;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(class_1007.class)
public class q6fe802987c3a2819 {
   @Inject(
      method = "method_23205(Lnet/minecraft/class_4587;Lnet/minecraft/class_11659;ILnet/minecraft/class_2960;Lnet/minecraft/class_630;Z)V",
      at = @At("HEAD")
   )
   private void L(class_4587 var1, class_11659 var2, int var3, class_2960 var4, class_630 var5, boolean var6, CallbackInfo var7) {
      ((wm)((class_1007)this).method_4038()).larpRestoreWalkRig();
   }
}
