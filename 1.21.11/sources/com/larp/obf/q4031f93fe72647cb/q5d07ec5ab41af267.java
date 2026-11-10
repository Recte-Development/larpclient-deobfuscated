package com.larp.obf.q4031f93fe72647cb;

import com.larpclient.LarpClient;
import com.larpclient.c2;
import com.larpclient.ez;
import com.larpclient.gu;
import com.larpclient.hh;
import com.larpclient.ly;
import com.larpclient.q8;
import com.larpclient.qx;
import com.larpclient.tk;
import com.larpclient.y0;
import net.minecraft.class_2960;
import net.minecraft.class_310;
import net.minecraft.class_332;
import net.minecraft.class_3928;
import net.minecraft.class_408;
import net.minecraft.class_437;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(class_437.class)
public abstract class q5d07ec5ab41af267 {
   @Unique
   private boolean x;

   @Redirect(method = "method_47413", at = @At(value = "INVOKE", target = "Lnet/minecraft/class_437;method_25420(Lnet/minecraft/class_332;IIF)V"))
   private void B(class_437 var1, class_332 var2, int var3, int var4, float var5) {
      boolean var6 = var1 instanceof class_3928;
      if ((class_310.method_1551().field_1687 == null || var6) && !(var1 instanceof class_408) && J(var1)) {
         if (var6 && !this.x) {
            this.x = true;
            q8.v("Menu", "Terrain loading uses the shared custom menu background");
         }

         boolean var7 = var1 instanceof qx var8 && var8.larpDrawingMenuPopup();
         if (var7) {
            var2.method_51448().pushMatrix();
            var2.method_51448().mul(qx.A(var1).matrix(var1.field_22789 / 2.0, var1.field_22790 / 2.0).invert());
         }

         try {
            y0 var13 = LarpClient.b().e().F();
            if (var1 instanceof c2 var9) {
               ly.z(var2, var1.field_22789, var1.field_22790, var9.atmosphere(), var9.dimAtmosphere(), var13.k);
            } else {
               ly.z(var2, var1.field_22789, var1.field_22790, var13.t, var1.method_73150(), var13.k);
            }

            class_310.method_1551().field_1705.method_72738();
         } finally {
            if (var7) {
               var2.method_51448().popMatrix();
            }
         }
      } else {
         var1.method_25420(var2, var3, var4, var5);
      }
   }

   @Unique
   private static boolean J(class_437 var0) {
      return hh.D() ? true : var0.getClass().getName().startsWith("com.larpclient.") && !(var0 instanceof tk);
   }

   @Inject(method = "method_57737", at = @At("HEAD"), cancellable = true)
   private static void L(class_332 var0, class_2960 var1, int var2, int var3, float var4, float var5, int var6, int var7, CallbackInfo var8) {
      if (class_310.method_1551().field_1687 == null && hh.D()) {
         gu.Z(var0, var2, var3, var6, var7, ez.C);
         var8.cancel();
      }
   }
}
