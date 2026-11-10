package com.larp.obf.q4031f93fe72647cb;

import com.larpclient.em;
import com.larpclient.i0;
import com.larpclient.jl;
import com.larpclient.module.ItemPhysicModule;
import net.minecraft.class_10039;
import net.minecraft.class_11659;
import net.minecraft.class_12075;
import net.minecraft.class_1542;
import net.minecraft.class_238;
import net.minecraft.class_4587;
import net.minecraft.class_7833;
import net.minecraft.class_916;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(class_916.class)
public abstract class q223bdd019f8bf9af {
   @Inject(method = "method_62470(Lnet/minecraft/class_1542;Lnet/minecraft/class_10039;F)V", at = @At("TAIL"))
   private void P(class_1542 var1, class_10039 var2, float var3, CallbackInfo var4) {
      ((jl)var2).larp$setGrounded(var1.method_24828());
      ((jl)var2).larp$setFloating(!var1.method_24828() && var1.method_5799());
   }

   @Inject(
      method = "method_3996(Lnet/minecraft/class_10039;Lnet/minecraft/class_4587;Lnet/minecraft/class_11659;Lnet/minecraft/class_12075;)V",
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/class_916;method_72986(Lnet/minecraft/class_4587;Lnet/minecraft/class_11659;ILnet/minecraft/class_10428;Lnet/minecraft/class_5819;Lnet/minecraft/class_238;)V"
      )
   )
   private void Z(class_10039 var1, class_4587 var2, class_11659 var3, class_12075 var4, CallbackInfo var5) {
      ItemPhysicModule var6 = ItemPhysicModule.o();
      if (var6 != null && var6.V()) {
         class_238 var7 = var1.field_55310.method_72173();
         float var8 = (float)Math.sin(var1.field_53328 / 10.0F + var1.field_53435) * 0.1F + 0.1F;
         float var9 = -((float)var7.field_1322) + 0.0625F;
         float var10 = class_1542.method_27314(var1.field_53328, var1.field_53435);
         em.W(var2, class_7833.field_40716.rotation(-var10));
         var2.method_46416(0.0F, -var8 - var9, 0.0F);
         jl var11 = (jl)var1;
         i0.y var12 = i0.L(
            var11.larp$isGrounded(), var11.larp$isFloating(), var6.J(), var1.field_53328, var1.field_53435, var7.field_1322, var7.field_1321, var7.field_1324
         );
         var2.method_22904(0.0, var12.height(), 0.0);
         if (var12.billboard()) {
            em.W(var2, var4.field_63081);
            var2.method_22905(1.1F, 1.1F, 1.0F);
         } else {
            em.W(var2, class_7833.field_40716.rotation((float)var12.yaw()));
            em.W(var2, class_7833.field_40714.rotation((float)var12.pitch()));
            em.W(var2, class_7833.field_40718.rotation((float)var12.roll()));
         }
      }
   }

   @Mixin(class_10039.class)
   public abstract static class qcfe355e8e37df137 implements jl {
      @Unique
      private boolean B;
      @Unique
      private boolean z;

      @Override
      public void larp$setGrounded(boolean var1) {
         this.B = var1;
      }

      @Override
      public boolean larp$isGrounded() {
         return this.B;
      }

      @Override
      public void larp$setFloating(boolean var1) {
         this.z = var1;
      }

      @Override
      public boolean larp$isFloating() {
         return this.z;
      }
   }
}
