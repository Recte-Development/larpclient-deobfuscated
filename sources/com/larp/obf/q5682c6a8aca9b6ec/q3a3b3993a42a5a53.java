package com.larp.obf.q5682c6a8aca9b6ec;

import com.larpclient.lt;
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
public abstract class q3a3b3993a42a5a53 {
   @Shadow
   private String value;
   @Shadow
   private int textX;
   @Unique
   private final lt k = new lt();
   @Unique
   private int j;

   @Shadow
   public abstract int getInnerWidth();

   @Inject(method = "extractWidgetRenderState", at = @At("HEAD"))
   private void z(GuiGraphicsExtractor var1, int var2, int var3, float var4, CallbackInfo var5) {
      GUIAnimationModule var6 = GUIAnimationModule.s();
      this.k.p(this.value, System.nanoTime(), var6 != null && var6.n() && Minecraft.getInstance().gui.screen() instanceof ChatScreen);
   }

   @WrapOperation(
      method = "extractWidgetRenderState",
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/gui/components/EditBox;applyFormat(Ljava/lang/String;I)Lnet/minecraft/util/FormattedCharSequence;"
      )
   )
   private FormattedCharSequence G(EditBox var1, String var2, int var3, Operation<FormattedCharSequence> var4) {
      this.j = var3;
      return (FormattedCharSequence)var4.call(new Object[]{var1, var2, var3});
   }

   @WrapOperation(
      method = "extractWidgetRenderState",
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/gui/GuiGraphicsExtractor;text(Lnet/minecraft/client/gui/Font;Lnet/minecraft/util/FormattedCharSequence;IIIZ)V"
      )
   )
   private void P(GuiGraphicsExtractor var1, Font var2, FormattedCharSequence var3, int var4, int var5, int var6, boolean var7, Operation<Void> var8) {
      if (!this.k.n()) {
         var8.call(new Object[]{var1, var2, var3, var4, var5, var6, var7});
      } else {
         float[] var9 = new float[]{0.0F};
         lt.k var10 = new lt.k(this.j);
         EditBox var11 = (EditBox)this;
         var1.enableScissor(this.textX, var11.getY(), this.textX + this.getInnerWidth(), var11.getY() + var11.getHeight());

         try {
            var3.accept((var10x, var11x, var12) -> {
               int var13 = var10.k(var12);
               FormattedCharSequence var14x = FormattedCharSequence.codepoint(var12, var11x);
               var1.pose().pushMatrix();
               var1.pose().translate(var9[0], (float)this.k.O(var13));

               try {
                  var8.call(new Object[]{var1, var2, var14x, var4, var5, this.k.X(var6, var13), var7});
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
