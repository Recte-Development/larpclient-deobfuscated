package com.larp.obf.q5682c6a8aca9b6ec;

import com.larpclient.mv;
import com.larpclient.r3;
import com.larpclient.rk;
import com.larpclient.y0;
import com.larpclient.ys;
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
public abstract class q9e8cabb59baecc01 implements mv, ys {
   @Shadow
   public int width;
   @Shadow
   public int height;
   @Unique
   private long F;
   @Unique
   private float w = 1.0F;
   @Unique
   private boolean M;
   @Unique
   private boolean U;
   @Unique
   private y0 E;
   @Unique
   private boolean K;

   @Override
   public void larpOpenMenuPopup() {
      this.E = new y0();
   }

   @Override
   public y0 larpMenuPopup() {
      return this.E;
   }

   @Override
   public boolean larpDrawingMenuPopup() {
      return this.K;
   }

   @Inject(method = "init(II)V", at = @At("TAIL"))
   private void Q(int var1, int var2, CallbackInfo var3) {
      this.F = System.nanoTime();
      this.w = 1.0F;
   }

   @Override
   public float larpContainerScale() {
      return this.w;
   }

   @WrapMethod(method = "extractRenderStateWithTooltipAndSubtitles")
   private void z(GuiGraphicsExtractor var1, int var2, int var3, float var4, Operation<Void> var5) {
      Minecraft var6 = Minecraft.getInstance();
      if (this.E != null && var6.level == null) {
         r3.h var7 = this.E.p(System.nanoTime(), var6.gui.overlay() == null && var6.isWindowActive(), ys.O());
         if (!this.E.W()) {
            var1.pose().pushMatrix();
            var1.pose().mul(var7.matrix(this.width / 2.0, this.height / 2.0));
            this.K = true;

            try {
               var5.call(new Object[]{var1, (int)Math.round(var7.localX(var2, this.width / 2.0)), (int)Math.round(var7.localY(var3, this.height / 2.0)), var4});
            } finally {
               this.K = false;
               var1.pose().popMatrix();
            }

            return;
         }
      }

      rk.x var16 = Feature3DUIModule.l((Screen)this);
      if (var16 == null) {
         this.C(var1, var2, var3, var4, var5);
      } else {
         var1.nextStratum();
         Feature3DUIModule.w(var1, this.width, this.height);
         Minecraft.getInstance().gui.hud.extractDeferredSubtitles();
         double[] var8 = var16.inverse(var2, var3);

         try {
            rk.H(var1, var16, true, () -> this.C(var1, (int)Math.floor(var8[0]), (int)Math.floor(var8[1]), var4, var5));
         } finally {
            var1.nextStratum();
            rk.R(this.w, this.width / 2.0, this.height / 2.0);
         }
      }
   }

   @Unique
   private void C(GuiGraphicsExtractor var1, int var2, int var3, float var4, Operation<Void> var5) {
      GUIAnimationModule var6 = GUIAnimationModule.s();
      this.M = this instanceof AbstractContainerScreen && var6 != null && var6.f();
      this.w = this.M ? var6.H(this.F) : 1.0F;
      if (!this.M) {
         var5.call(new Object[]{var1, var2, var3, var4});
      } else {
         var1.pose().pushMatrix();
         this.B(var1, this.w);
         int var7 = (int)Math.round(var6.E(var2, this.width / 2.0, this.w));
         int var8 = (int)Math.round(var6.E(var3, this.height / 2.0, this.w));

         try {
            var5.call(new Object[]{var1, var7, var8, var4});
         } finally {
            var1.pose().popMatrix();
            this.M = false;
            this.U = false;
         }
      }
   }

   @Unique
   private void B(GuiGraphicsExtractor var1, float var2) {
      var1.pose().translate(this.width / 2.0F, this.height / 2.0F);
      var1.pose().scale(var2, var2);
      var1.pose().translate(-this.width / 2.0F, -this.height / 2.0F);
   }

   @Inject(method = "extractTransparentBackground", at = @At("HEAD"), cancellable = true)
   private void z(GuiGraphicsExtractor var1, CallbackInfo var2) {
      if (rk.t(this) != null) {
         var2.cancel();
      }
   }

   @Inject(method = "extractBackground", at = @At("HEAD"))
   private void y(GuiGraphicsExtractor var1, int var2, int var3, float var4, CallbackInfo var5) {
      this.U = this.M;
      if (this.U) {
         var1.pose().pushMatrix();
         this.B(var1, 1.0F / this.w);
      }
   }

   @Inject(method = "extractBackground", at = @At("RETURN"))
   private void c(GuiGraphicsExtractor var1, int var2, int var3, float var4, CallbackInfo var5) {
      if (this.U) {
         var1.pose().popMatrix();
         this.U = false;
      }
   }
}
