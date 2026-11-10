package com.larp.obf.q4031f93fe72647cb;

import com.larpclient.cn;
import com.larpclient.fm;
import com.larpclient.q8;
import com.larpclient.yw;
import net.minecraft.class_10055;
import net.minecraft.class_10426;
import net.minecraft.class_11659;
import net.minecraft.class_3887;
import net.minecraft.class_4587;
import net.minecraft.class_591;
import net.minecraft.class_989;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(class_989.class)
public abstract class q5ba856aa5a520485 {
   @Inject(method = "method_17162(Lnet/minecraft/class_4587;Lnet/minecraft/class_11659;ILnet/minecraft/class_10426;FF)V", at = @At("HEAD"), cancellable = true)
   private void G(class_4587 var1, class_11659 var2, int var3, class_10426 var4, float var5, float var6, CallbackInfo var7) {
      if (var4 instanceof class_10055 var8 && ((class_3887)this).method_17165() instanceof class_591 var9) {
         try {
            if (fm.Y(var9, var8, var1, var2, var3)) {
               var7.cancel();
               return;
            }
         } catch (RuntimeException var13) {
            q8.O("Emotes", "Cash fan submission failed; held items stay visible", var13);
         }

         try {
            if (yw.B(var9, var8, var1, var2, var3)) {
               var7.cancel();
               return;
            }
         } catch (RuntimeException var12) {
            q8.O("Emotes", "Rifle submission failed; held items stay visible", var12);
         }

         try {
            if (cn.V(var9, var8, var1, var2, var3)) {
               var7.cancel();
            }
         } catch (RuntimeException var11) {
            q8.O("Emotes", "Pickaxe submission failed; held items stay visible", var11);
         }
      }
   }
}
