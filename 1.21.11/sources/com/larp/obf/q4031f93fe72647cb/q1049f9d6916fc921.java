package com.larp.obf.q4031f93fe72647cb;

import com.larpclient.gm;
import com.larpclient.jk;
import net.minecraft.class_11238;
import net.minecraft.class_11239;
import net.minecraft.class_11255;
import net.minecraft.class_4587;
import net.minecraft.class_4597.class_4598;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(class_11238.class)
public abstract class q1049f9d6916fc921 extends class_11239<class_11255> {
   private static final String X = "renderToTexture(Lnet/minecraft/client/gui/render/state/pip/GuiSkinRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;)V";
   @Unique
   private final Matrix4f a = new Matrix4f();

   protected q1049f9d6916fc921(class_4598 var1) {
      super(var1);
   }

   @Inject(method = "method_70912(Lnet/minecraft/class_11255;Lnet/minecraft/class_4587;)V", at = @At("HEAD"))
   private void F(class_11255 var1, class_4587 var2, CallbackInfo var3) {
      if (var1.comp_4141() instanceof jk var4) {
         var4.A(var2);
      }

      this.a.set(var2.method_23760().method_23761());
   }

   @Inject(
      method = "method_70912(Lnet/minecraft/class_11255;Lnet/minecraft/class_4587;)V",
      at = @At(value = "INVOKE", target = "Lnet/minecraft/class_4597$class_4598;method_22993()V", shift = Shift.AFTER)
   )
   private void J(class_11255 var1, class_4587 var2, CallbackInfo var3) {
      gm.i(var1, this.a, var2, super.field_59933);
   }
}
