package com.larp.obf.q41a5851bcf8e876a;

import com.larpclient.dc;
import com.larpclient.dx;
import com.larpclient.fr;
import com.larpclient.sc;
import com.larpclient.sd;
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
public abstract class qd26e0c2e1b3f8528 implements sc, sd {
   @Shadow
   public int width;
   @Shadow
   public int height;
   @Unique
   private long W;
   @Unique
   private float M = 1.0F;
   @Unique
   private boolean L;
   @Unique
   private boolean f;
   @Unique
   private dc s;
   @Unique
   private boolean H;

   @Override
   public void larpOpenMenuPopup() {
      this.s = new dc();
   }

   @Override
   public dc larpMenuPopup() {
      return this.s;
   }

   @Override
   public boolean larpDrawingMenuPopup() {
      return this.H;
   }

   @Inject(method = "init(II)V", at = @At("TAIL"))
   private void k(int var1, int var2, CallbackInfo var3) {
      this.W = System.nanoTime();
      this.M = 1.0F;
   }

   @Override
   public float larpContainerScale() {
      return this.M;
   }

   @WrapMethod(method = "extractRenderStateWithTooltipAndSubtitles")
   private void j(GuiGraphicsExtractor var1, int var2, int var3, float var4, Operation<Void> var5) {
      Minecraft var6 = Minecraft.getInstance();
      if (this.s != null && var6.level == null) {
         fr.z var7 = this.s.A(System.nanoTime(), var6.gui.overlay() == null && var6.isWindowActive(), sd.Z());
         if (!this.s.t()) {
            var1.pose().pushMatrix();
            var1.pose().mul(var7.matrix(this.width / 2.0, this.height / 2.0));
            this.H = true;

            try {
               var5.call(new Object[]{var1, (int)Math.round(var7.localX(var2, this.width / 2.0)), (int)Math.round(var7.localY(var3, this.height / 2.0)), var4});
            } finally {
               this.H = false;
               var1.pose().popMatrix();
            }

            return;
         }
      }

      dx.n var16 = Feature3DUIModule.Y((Screen)this);
      if (var16 == null) {
         this.p(var1, var2, var3, var4, var5);
      } else {
         var1.nextStratum();
         Feature3DUIModule.X(var1, this.width, this.height);
         Minecraft.getInstance().gui.hud.extractDeferredSubtitles();
         double[] var8 = var16.inverse(var2, var3);

         try {
            dx.X(var1, var16, true, () -> this.p(var1, (int)Math.floor(var8[0]), (int)Math.floor(var8[1]), var4, var5));
         } finally {
            var1.nextStratum();
            dx.C(this.M, this.width / 2.0, this.height / 2.0);
         }
      }
   }

   @Unique
   private void p(GuiGraphicsExtractor var1, int var2, int var3, float var4, Operation<Void> var5) {
      GUIAnimationModule var6 = GUIAnimationModule.O();
      this.L = this instanceof AbstractContainerScreen && var6 != null && var6.h();
      this.M = this.L ? var6.p(this.W) : 1.0F;
      if (!this.L) {
         var5.call(new Object[]{var1, var2, var3, var4});
      } else {
         var1.pose().pushMatrix();
         this.Q(var1, this.M);
         int var7 = (int)Math.round(var6.Q(var2, this.width / 2.0, this.M));
         int var8 = (int)Math.round(var6.Q(var3, this.height / 2.0, this.M));

         try {
            var5.call(new Object[]{var1, var7, var8, var4});
         } finally {
            var1.pose().popMatrix();
            this.L = false;
            this.f = false;
         }
      }
   }

   @Unique
   private void Q(GuiGraphicsExtractor var1, float var2) {
      var1.pose().translate(this.width / 2.0F, this.height / 2.0F);
      var1.pose().scale(var2, var2);
      var1.pose().translate(-this.width / 2.0F, -this.height / 2.0F);
   }

   @Inject(method = "extractTransparentBackground", at = @At("HEAD"), cancellable = true)
   private void w(GuiGraphicsExtractor var1, CallbackInfo var2) {
      if (dx.x(this) != null) {
         var2.cancel();
      }
   }

   @Inject(method = "extractBackground", at = @At("HEAD"))
   private void D(GuiGraphicsExtractor var1, int var2, int var3, float var4, CallbackInfo var5) {
      this.f = this.L;
      if (this.f) {
         var1.pose().pushMatrix();
         this.Q(var1, 1.0F / this.M);
      }
   }

   @Inject(method = "extractBackground", at = @At("RETURN"))
   private void E(GuiGraphicsExtractor var1, int var2, int var3, float var4, CallbackInfo var5) {
      if (this.f) {
         var1.pose().popMatrix();
         this.f = false;
      }
   }
}
