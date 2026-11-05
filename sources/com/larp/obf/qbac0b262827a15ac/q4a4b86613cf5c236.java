package com.larp.obf.qbac0b262827a15ac;

import com.larpclient.cp;
import com.larpclient.m8;
import com.larpclient.wb;
import com.larpclient.wv;
import com.larpclient.xt;
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
public abstract class q4a4b86613cf5c236 implements wb, wv {
   @Shadow
   public int width;
   @Shadow
   public int height;
   @Unique
   private long o;
   @Unique
   private float a = 1.0F;
   @Unique
   private boolean U;
   @Unique
   private boolean K;
   @Unique
   private xt g;
   @Unique
   private boolean r;

   @Override
   public void larpOpenMenuPopup() {
      this.g = new xt();
   }

   @Override
   public xt larpMenuPopup() {
      return this.g;
   }

   @Override
   public boolean larpDrawingMenuPopup() {
      return this.r;
   }

   @Inject(method = "init(II)V", at = @At("TAIL"))
   private void c(int var1, int var2, CallbackInfo var3) {
      this.o = System.nanoTime();
      this.a = 1.0F;
   }

   @Override
   public float larpContainerScale() {
      return this.a;
   }

   @WrapMethod(method = "extractRenderStateWithTooltipAndSubtitles")
   private void Q(GuiGraphicsExtractor var1, int var2, int var3, float var4, Operation<Void> var5) {
      Minecraft var6 = Minecraft.getInstance();
      if (this.g != null && var6.level == null) {
         m8.w var7 = this.g.s(System.nanoTime(), var6.gui.overlay() == null && var6.isWindowActive(), wv.X());
         if (!this.g.W()) {
            var1.pose().pushMatrix();
            var1.pose().mul(var7.matrix(this.width / 2.0, this.height / 2.0));
            this.r = true;

            try {
               var5.call(new Object[]{var1, (int)Math.round(var7.localX(var2, this.width / 2.0)), (int)Math.round(var7.localY(var3, this.height / 2.0)), var4});
            } finally {
               this.r = false;
               var1.pose().popMatrix();
            }

            return;
         }
      }

      cp.k var16 = Feature3DUIModule.k((Screen)this);
      if (var16 == null) {
         this.j(var1, var2, var3, var4, var5);
      } else {
         var1.nextStratum();
         Feature3DUIModule.F(var1, this.width, this.height);
         Minecraft.getInstance().gui.hud.extractDeferredSubtitles();
         double[] var8 = var16.inverse(var2, var3);

         try {
            cp.H(var1, var16, true, () -> this.j(var1, (int)Math.floor(var8[0]), (int)Math.floor(var8[1]), var4, var5));
         } finally {
            var1.nextStratum();
            cp.e(this.a, this.width / 2.0, this.height / 2.0);
         }
      }
   }

   @Unique
   private void j(GuiGraphicsExtractor var1, int var2, int var3, float var4, Operation<Void> var5) {
      GUIAnimationModule var6 = GUIAnimationModule.F();
      this.U = this instanceof AbstractContainerScreen && var6 != null && var6.F();
      this.a = this.U ? var6.l(this.o) : 1.0F;
      if (!this.U) {
         var5.call(new Object[]{var1, var2, var3, var4});
      } else {
         var1.pose().pushMatrix();
         this.W(var1, this.a);
         int var7 = (int)Math.round(var6.j(var2, this.width / 2.0, this.a));
         int var8 = (int)Math.round(var6.j(var3, this.height / 2.0, this.a));

         try {
            var5.call(new Object[]{var1, var7, var8, var4});
         } finally {
            var1.pose().popMatrix();
            this.U = false;
            this.K = false;
         }
      }
   }

   @Unique
   private void W(GuiGraphicsExtractor var1, float var2) {
      var1.pose().translate(this.width / 2.0F, this.height / 2.0F);
      var1.pose().scale(var2, var2);
      var1.pose().translate(-this.width / 2.0F, -this.height / 2.0F);
   }

   @Inject(method = "extractTransparentBackground", at = @At("HEAD"), cancellable = true)
   private void s(GuiGraphicsExtractor var1, CallbackInfo var2) {
      if (cp.J(this) != null) {
         var2.cancel();
      }
   }

   @Inject(method = "extractBackground", at = @At("HEAD"))
   private void Y(GuiGraphicsExtractor var1, int var2, int var3, float var4, CallbackInfo var5) {
      this.K = this.U;
      if (this.K) {
         var1.pose().pushMatrix();
         this.W(var1, 1.0F / this.a);
      }
   }

   @Inject(method = "extractBackground", at = @At("RETURN"))
   private void C(GuiGraphicsExtractor var1, int var2, int var3, float var4, CallbackInfo var5) {
      if (this.K) {
         var1.pose().popMatrix();
         this.K = false;
      }
   }
}
