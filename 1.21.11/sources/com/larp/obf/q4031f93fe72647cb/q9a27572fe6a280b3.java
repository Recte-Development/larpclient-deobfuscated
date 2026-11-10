package com.larp.obf.q4031f93fe72647cb;

import com.larpclient.LarpClient;
import com.larpclient.er;
import com.larpclient.module.NametagsModule;
import net.minecraft.class_10017;
import net.minecraft.class_10055;
import net.minecraft.class_1297;
import net.minecraft.class_1657;
import net.minecraft.class_897;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(class_897.class)
public class q9a27572fe6a280b3 {
   @Inject(method = "method_62354(Lnet/minecraft/class_1297;Lnet/minecraft/class_10017;F)V", at = @At("TAIL"))
   private void J(class_1297 var1, class_10017 var2, float var3, CallbackInfo var4) {
      if (var1 instanceof class_1657 && var2.field_53337 != null) {
         var2.field_53337 = er.J(var1.method_5667(), ((class_1657)var1).method_7334().name(), var2.field_53337);
      }

      LarpClient var5 = LarpClient.b();
      if (var5 != null) {
         NametagsModule var6 = var5.G().b(NametagsModule.class);
         if (var6 != null && var6.isEnabled() && var6.M(var1)) {
            var2.field_53337 = null;
            if (var2 instanceof class_10055 var7) {
               var7.field_53525 = null;
            }
         }
      }
   }
}
