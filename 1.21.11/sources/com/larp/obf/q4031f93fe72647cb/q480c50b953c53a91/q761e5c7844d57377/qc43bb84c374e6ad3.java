package com.larp.obf.q4031f93fe72647cb.q480c50b953c53a91.q761e5c7844d57377;

import com.larpclient.cx;
import com.larpclient.j5;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import java.util.List;
import net.minecraft.class_10889;
import net.minecraft.class_1920;
import net.minecraft.class_2338;
import net.minecraft.class_2680;
import net.minecraft.class_4587;
import net.minecraft.class_4588;
import net.minecraft.class_776;
import net.minecraft.class_9810;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(class_9810.class)
public abstract class qc43bb84c374e6ad3 {
   @WrapOperation(
      method = "method_60904",
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/class_776;method_3355(Lnet/minecraft/class_2680;Lnet/minecraft/class_2338;Lnet/minecraft/class_1920;Lnet/minecraft/class_4587;Lnet/minecraft/class_4588;ZLjava/util/List;)V"
      )
   )
   private void M(
      class_776 var1,
      class_2680 var2,
      class_2338 var3,
      class_1920 var4,
      class_4587 var5,
      class_4588 var6,
      boolean var7,
      List<class_10889> var8,
      Operation<Void> var9
   ) {
      if (!j5.E(var2, var3.method_10263() & 15, var3.method_10264() & 15, var3.method_10260() & 15, var4, var3)) {
         var9.call(new Object[]{var1, var2, var3, var4, var5, var6, var7, var8});
      } else {
         try {
            var9.call(new Object[]{var1, var2, var3, var4, var5, cx.r(var6), var7, var8});
         } finally {
            j5.O();
         }
      }
   }
}
