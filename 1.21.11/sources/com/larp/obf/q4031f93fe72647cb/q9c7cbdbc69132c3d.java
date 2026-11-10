package com.larp.obf.q4031f93fe72647cb;

import com.larpclient.g1;
import com.larpclient.ha;
import com.larpclient.io;
import com.larpclient.q6;
import com.larpclient.q8;
import com.larpclient.yv;
import net.minecraft.class_10055;
import net.minecraft.class_1007;
import net.minecraft.class_11890;
import net.minecraft.class_5617.class_5618;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(class_1007.class)
public abstract class q9c7cbdbc69132c3d {
   @Inject(method = "<init>(Lnet/minecraft/class_5617$class_5618;Z)V", at = @At("TAIL"))
   private void H(class_5618 var1, boolean var2, CallbackInfo var3) {
      class_1007 var4 = (class_1007)this;
      ((q6)var4.method_4038()).larp$markSkin();
      ((q03091a017f3d0273)this).larp$addLayer(new io(var4));
   }

   @Inject(method = "method_62604(Lnet/minecraft/class_11890;Lnet/minecraft/class_10055;F)V", at = @At("TAIL"))
   private void L(class_11890 var1, class_10055 var2, float var3, CallbackInfo var4) {
      try {
         ha.o(var1, var2);
      } catch (RuntimeException var6) {
         ha.m(var2);
         q8.O("Roblox", "Headless/Korblox extraction failed; native head and legs kept", var6);
      }
   }

   @Mixin(class_10055.class)
   public abstract static class q04c433d4afc7fd6c implements g1 {
      @Unique
      private int h;
      @Unique
      private yv g;

      @Override
      public int larp$headless() {
         return this.h;
      }

      @Override
      public yv larp$korblox() {
         return this.g;
      }

      @Override
      public void larp$roblox(int var1, yv var2) {
         this.h = var1;
         this.g = var2;
      }
   }
}
