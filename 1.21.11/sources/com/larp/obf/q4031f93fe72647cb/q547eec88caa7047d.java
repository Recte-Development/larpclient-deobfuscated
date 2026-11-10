package com.larp.obf.q4031f93fe72647cb;

import com.larpclient.LarpClient;
import com.larpclient.c4;
import com.larpclient.q8;
import com.larpclient.sx;
import com.larpclient.wo;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.class_1060;
import net.minecraft.class_310;
import net.minecraft.class_332;
import net.minecraft.class_425;
import net.minecraft.class_437;
import org.slf4j.LoggerFactory;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(class_425.class)
public class q547eec88caa7047d {
   @Shadow
   @Final
   private class_310 field_18217;
   @Shadow
   @Final
   private boolean field_18219;
   @Shadow
   private long field_17771;
   @Unique
   private boolean A;
   @Unique
   private boolean M;
   @Unique
   private static boolean E;
   @Unique
   private static boolean a = true;

   @Inject(method = "method_18819", at = @At("TAIL"))
   private static void a(class_1060 var0, CallbackInfo var1) {
      try {
         var0.method_65876(c4.W, new c4());
         var0.method_65876(c4.C, new c4(c4.C, "textures/gui/startup-brand.png"));
         E = true;
      } catch (RuntimeException var3) {
         var0.method_4615(c4.W);
         var0.method_4615(c4.C);
         E = false;
         LoggerFactory.getLogger("Larp/Startup").warn("Intro assets unavailable; vanilla startup fallback retained", var3);
      }

      a = c4.p(FabricLoader.getInstance().getConfigDir().resolve("larp-client/menu.json")).motion();
   }

   @Inject(method = "<init>", at = @At("RETURN"))
   private void M(CallbackInfo var1) {
      this.A = !this.field_18219 && !LarpClient.q;
      if (!this.A) {
         q8.v("Loading", "Resource reload overlay hidden; vanilla completion retained");
      }
   }

   @Inject(method = "method_74218", at = @At("HEAD"), cancellable = true)
   private void Y(CallbackInfoReturnable<Boolean> var1) {
      if (!this.A) {
         var1.setReturnValue(true);
      }
   }

   @Inject(method = "method_74217", at = @At("TAIL"))
   private void w(CallbackInfo var1) {
      if (this.field_17771 >= 0L && this.field_18217.method_18506() == this && (!this.A || E)) {
         if (this.A) {
            long var2 = System.nanoTime();
            if (!this.M) {
               this.M = true;
               wo.K.a(var2);
               q8.G("Startup", "Resources ready; finishing logo/title intro before menu reveal");
            }

            if (!wo.K.q(var2, a)) {
               return;
            }

            this.field_18217.method_1531().method_4615(c4.W);
            this.field_18217.method_1531().method_4615(c4.C);
            LarpClient.q = true;
            q8.G("Startup", "Black intro complete; destination visible and input released");
         }

         this.field_18217.method_18502(null);
      }
   }

   @Inject(method = "method_25394", at = @At("HEAD"), cancellable = true)
   private void k(class_332 var1, int var2, int var3, float var4, CallbackInfo var5) {
      if (!this.A) {
         var5.cancel();
         class_437 var10 = this.field_18217.field_1755;
         if (var10 != null) {
            var10.method_47413(var1, var2, var3, var4);
         } else {
            this.field_18217.field_1705.method_72738();
         }
      } else if (E) {
         var5.cancel();
         long var6 = System.nanoTime();
         wo var8 = wo.K;
         if (var8.L(var6, a) == wo.e.B) {
            q8.G("Startup", "Black intro started; logo spin, soft glow and typewriter title");
         }

         var8.n(var6);
         if (var8.z(var6, a) > 0.0) {
            class_437 var9 = this.field_18217.field_1755;
            if (var9 != null) {
               var9.method_47413(var1, -1, -1, var4);
            } else {
               this.field_18217.field_1705.method_72738();
            }

            var1.method_71048();
         }

         sx.z(var1, var6, a);
      }
   }
}
