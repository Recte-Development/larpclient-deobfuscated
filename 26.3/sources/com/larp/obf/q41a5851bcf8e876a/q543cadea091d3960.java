package com.larp.obf.q41a5851bcf8e876a;

import com.larpclient.ar;
import com.larpclient.module.GUIAnimationModule;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.util.FormattedCharSequence;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EditBox.class)
public abstract class q543cadea091d3960 {
   @Shadow
   private String value;
   @Shadow
   private int textX;
   @Unique
   private final ar F = new ar();
   @Unique
   private int I;

   @Shadow
   public abstract int getInnerWidth();

   @Inject(method = "extractWidgetRenderState", at = @At("HEAD"))
   private void r(GuiGraphicsExtractor var1, int var2, int var3, float var4, CallbackInfo var5) {
      GUIAnimationModule var6 = GUIAnimationModule.O();
      this.F.l(this.value, System.nanoTime(), var6 != null && var6.I() && Minecraft.getInstance().gui.screen() instanceof ChatScreen);
   }

   @WrapOperation(
      method = "extractWidgetRenderState",
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/gui/components/EditBox;applyFormat(Ljava/lang/String;I)Lnet/minecraft/util/FormattedCharSequence;"
      )
   )
   private FormattedCharSequence t(EditBox var1, String var2, int var3, Operation<FormattedCharSequence> var4) {
      this.I = var3;
      return (FormattedCharSequence)var4.call(new Object[]{var1, var2, var3});
   }

   @WrapOperation(
      method = "extractWidgetRenderState",
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/gui/GuiGraphicsExtractor;text(Lnet/minecraft/client/gui/Font;Lnet/minecraft/util/FormattedCharSequence;IIIZ)V"
      )
   )
   private void i(GuiGraphicsExtractor var1, Font var2, FormattedCharSequence var3, int var4, int var5, int var6, boolean var7, Operation<Void> var8) {
      if (!this.F.l()) {
         var8.call(new Object[]{var1, var2, var3, var4, var5, var6, var7});
      } else {
         float[] var9 = new float[]{0.0F};
         ar.b var10 = new ar.b(this.I);
         EditBox var11 = (EditBox)this;
         var1.enableScissor(this.textX, var11.getY(), this.textX + this.getInnerWidth(), var11.getY() + var11.getHeight());

         try {
            var3.accept((var10x, var11x, var12) -> {
               int var13 = var10.j(var12);
               FormattedCharSequence var14x = FormattedCharSequence.codepoint(var12, var11x);
               var1.pose().pushMatrix();
               var1.pose().translate(var9[0], (float)this.F.i(var13));

               try {
                  var8.call(new Object[]{var1, var2, var14x, var4, var5, this.F.B(var6, var13), var7});
               } finally {
                  var1.pose().popMatrix();
               }

               var9[0] += var2.getSplitter().stringWidth(var14x);
               return true;
            });
         } finally {
            var1.disableScissor();
         }
      }
   }
}
