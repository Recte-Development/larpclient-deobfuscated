package com.larp.obf.qa300483f54d804a3;

import com.larpclient.LarpClient;
import com.larpclient.h;
import com.larpclient.h2;
import com.larpclient.m0;
import com.larpclient.t7;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.LoadingOverlay;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.texture.TextureManager;
import org.slf4j.LoggerFactory;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LoadingOverlay.class)
public class q927769385ccf7ed9 {
   @Shadow
   @Final
   private Minecraft minecraft;
   @Shadow
   @Final
   private boolean fadeIn;
   @Shadow
   private long fadeOutStart;
   @Unique
   private boolean C;
   @Unique
   private boolean U;
   @Unique
   private static boolean X;
   @Unique
   private static boolean e = true;

   @Inject(method = "registerTextures", at = @At("TAIL"))
   private static void P(TextureManager var0, CallbackInfo var1) {
      try {
         var0.registerAndLoad(m0.r, new m0());
         var0.registerAndLoad(m0.q, new m0(m0.q, "textures/gui/startup-brand.png"));
         X = true;
      } catch (RuntimeException var3) {
         var0.release(m0.r);
         var0.release(m0.q);
         X = false;
         LoggerFactory.getLogger("Larp/Startup").warn("Intro assets unavailable; vanilla startup fallback retained", var3);
      }

      e = m0.S(FabricLoader.getInstance().getConfigDir().resolve("larp-client/menu.json")).motion();
   }

   @Inject(method = "<init>", at = @At("RETURN"))
   private void M(CallbackInfo var1) {
      this.C = !this.fadeIn && !LarpClient.A;
      if (!this.C) {
         h.U("Loading", "Resource reload overlay hidden; vanilla completion retained");
      }
   }

   @Inject(method = "isReadyToFadeOut", at = @At("HEAD"), cancellable = true)
   private void g(CallbackInfoReturnable<Boolean> var1) {
      if (!this.C) {
         var1.setReturnValue(true);
      }
   }

   @Inject(method = "tick", at = @At("TAIL"))
   private void v(CallbackInfo var1) {
      if (this.fadeOutStart >= 0L && this.minecraft.gui.overlay() == this && (!this.C || X)) {
         if (this.C) {
            long var2 = System.nanoTime();
            if (!this.U) {
               this.U = true;
               h2.j.b(var2);
               h.A("Startup", "Resources ready; finishing logo/title intro before menu reveal");
            }

            if (!h2.j.H(var2, e)) {
               return;
            }

            this.minecraft.getTextureManager().release(m0.r);
            this.minecraft.getTextureManager().release(m0.q);
            LarpClient.A = true;
            h.A("Startup", "Black intro complete; destination visible and input released");
         }

         this.minecraft.gui.setOverlay(null);
      }
   }

   @Inject(method = "extractRenderState", at = @At("HEAD"), cancellable = true)
   private void i(GuiGraphicsExtractor var1, int var2, int var3, float var4, CallbackInfo var5) {
      if (!this.C) {
         var5.cancel();
         Screen var10 = this.minecraft.gui.screen();
         if (var10 != null) {
            var10.extractRenderStateWithTooltipAndSubtitles(var1, var2, var3, var4);
         } else {
            this.minecraft.gui.hud.extractDeferredSubtitles();
         }
      } else if (X) {
         var5.cancel();
         long var6 = System.nanoTime();
         h2 var8 = h2.j;
         if (var8.O(var6, e) == h2.e.N) {
            h.A("Startup", "Black intro started; logo spin, soft glow and typewriter title");
         }

         var8.W(var6);
         if (var8.M(var6, e) > 0.0) {
            Screen var9 = this.minecraft.gui.screen();
            if (var9 != null) {
               var9.extractRenderStateWithTooltipAndSubtitles(var1, -1, -1, var4);
            } else {
               this.minecraft.gui.hud.extractDeferredSubtitles();
            }

            var1.nextStratum();
         }

         t7.J(var1, var6, e);
      }
   }
}
