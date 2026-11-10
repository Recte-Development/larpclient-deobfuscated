package com.larp.obf.qa300483f54d804a3;

import com.larpclient.kt;
import com.larpclient.lk;
import com.larpclient.m_;
import com.larpclient.mb;
import com.larpclient.tw;
import com.larpclient.module.Feature3DUIModule;
import com.larpclient.module.GUIAnimationModule;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Screen.class)
public abstract class q99f92eb0aebce468 implements lk, tw {
   @Shadow
   public int width;
   @Shadow
   public int height;
   @Unique
   private long M;
   @Unique
   private float f = 1.0F;
   @Unique
   private boolean O;
   @Unique
   private boolean x;
   @Unique
   private kt z;
   @Unique
   private boolean C;

   @Override
   public void larpOpenMenuPopup() {
      this.z = new kt();
   }

   @Override
   public kt larpMenuPopup() {
      return this.z;
   }

   @Override
   public boolean larpDrawingMenuPopup() {
      return this.C;
   }

   @Inject(method = "init(II)V", at = @At("TAIL"))
   private void h(int var1, int var2, CallbackInfo var3) {
      this.M = System.nanoTime();
      this.f = 1.0F;
   }

   @Override
   public float larpContainerScale() {
      return this.f;
   }

   @WrapMethod(method = "extractRenderStateWithTooltipAndSubtitles")
   private void U(GuiGraphicsExtractor var1, int var2, int var3, float var4, Operation<Void> var5) {
      Minecraft var6 = Minecraft.getInstance();
      if (this.z != null && var6.level == null) {
         mb.d var7 = this.z.h(System.nanoTime(), var6.gui.overlay() == null && var6.isWindowActive(), tw.D());
         if (!this.z.n()) {
            var1.pose().pushMatrix();
            var1.pose().mul(var7.matrix(this.width / 2.0, this.height / 2.0));
            this.C = true;

            try {
               var5.call(new Object[]{var1, (int)Math.round(var7.localX(var2, this.width / 2.0)), (int)Math.round(var7.localY(var3, this.height / 2.0)), var4});
            } finally {
               this.C = false;
               var1.pose().popMatrix();
            }

            return;
         }
      }

      m_.s var16 = Feature3DUIModule.t((Screen)this);
      if (var16 == null) {
         this.p(var1, var2, var3, var4, var5);
      } else {
         var1.nextStratum();
         Feature3DUIModule.b(var1, this.width, this.height);
         Minecraft.getInstance().gui.hud.extractDeferredSubtitles();
         double[] var8 = var16.inverse(var2, var3);

         try {
            m_.T(var1, var16, true, () -> this.p(var1, (int)Math.floor(var8[0]), (int)Math.floor(var8[1]), var4, var5));
         } finally {
            var1.nextStratum();
            m_.B(this.f, this.width / 2.0, this.height / 2.0);
         }
      }
   }

   @Unique
   private void p(GuiGraphicsExtractor var1, int var2, int var3, float var4, Operation<Void> var5) {
      GUIAnimationModule var6 = GUIAnimationModule.a();
      this.O = this instanceof AbstractContainerScreen && var6 != null && var6.w();
      this.f = this.O ? var6.f(this.M) : 1.0F;
      if (!this.O) {
         var5.call(new Object[]{var1, var2, var3, var4});
      } else {
         var1.pose().pushMatrix();
         this.Y(var1, this.f);
         int var7 = (int)Math.round(var6.d(var2, this.width / 2.0, this.f));
         int var8 = (int)Math.round(var6.d(var3, this.height / 2.0, this.f));

         try {
            var5.call(new Object[]{var1, var7, var8, var4});
         } finally {
            var1.pose().popMatrix();
            this.O = false;
            this.x = false;
         }
      }
   }

   @Unique
   private void Y(GuiGraphicsExtractor var1, float var2) {
      var1.pose().translate(this.width / 2.0F, this.height / 2.0F);
      var1.pose().scale(var2, var2);
      var1.pose().translate(-this.width / 2.0F, -this.height / 2.0F);
   }

   @Inject(method = "extractTransparentBackground", at = @At("HEAD"), cancellable = true)
   private void B(GuiGraphicsExtractor var1, CallbackInfo var2) {
      if (m_.U(this) != null) {
         var2.cancel();
      }
   }

   @Inject(method = "extractBackground", at = @At("HEAD"))
   private void W(GuiGraphicsExtractor var1, int var2, int var3, float var4, CallbackInfo var5) {
      this.x = this.O;
      if (this.x) {
         var1.pose().pushMatrix();
         this.Y(var1, 1.0F / this.f);
      }
   }

   @Inject(method = "extractBackground", at = @At("RETURN"))
   private void v(GuiGraphicsExtractor var1, int var2, int var3, float var4, CallbackInfo var5) {
      if (this.x) {
         var1.pose().popMatrix();
         this.x = false;
      }
   }
}
