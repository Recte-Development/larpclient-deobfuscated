package com.larp.obf.q4031f93fe72647cb;

import com.larpclient.bd;
import com.larpclient.module.BacktrackModule;
import com.larpclient.module.FakeLagModule;
import com.larpclient.module.FakePayModule;
import io.netty.channel.ChannelFutureListener;
import net.minecraft.class_2535;
import net.minecraft.class_2547;
import net.minecraft.class_2596;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(class_2535.class)
public class qf9eb6baae55286d0 {
   @Inject(method = "method_52906(Lnet/minecraft/class_2596;Lio/netty/channel/ChannelFutureListener;Z)V", at = @At("HEAD"), cancellable = true)
   private void W(class_2596<?> var1, ChannelFutureListener var2, boolean var3, CallbackInfo var4) {
      if (!BacktrackModule.b()) {
         if (FakePayModule.L(var1)) {
            var4.cancel();
         } else if (BacktrackModule.p() && BacktrackModule.N((class_2535)this, var1, var2, var3)) {
            var4.cancel();
         } else if (FakeLagModule.X() && FakeLagModule.P((class_2535)this, var1, var2, var3)) {
            var4.cancel();
         } else {
            bd.I(var1);
         }
      }
   }

   @Inject(method = "method_10759", at = @At("HEAD"))
   private static void I(class_2596<?> var0, class_2547 var1, CallbackInfo var2) {
      BacktrackModule.i(var0, var1);
   }
}
