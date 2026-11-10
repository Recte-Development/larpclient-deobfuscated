package com.larp.obf.q41a5851bcf8e876a;

import com.larpclient.LarpClient;
import com.larpclient._q;
import com.larpclient.sn;
import com.larpclient.v8;
import com.larpclient.wg;
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
public class q47eb630f0a3b5b93 {
   @Shadow
   @Final
   private Minecraft minecraft;
   @Shadow
   @Final
   private boolean fadeIn;
   @Shadow
   private long fadeOutStart;
   @Unique
   private boolean d;
   @Unique
   private boolean H;
   @Unique
   private static boolean h;
   @Unique
   private static boolean U = true;

   @Inject(method = "registerTextures", at = @At("TAIL"))
   private static void V(TextureManager var0, CallbackInfo var1) {
      try {
         var0.registerAndLoad(wg.c, new wg());
         var0.registerAndLoad(wg.R, new wg(wg.R, "textures/gui/startup-brand.png"));
         h = true;
      } catch (RuntimeException var3) {
         var0.release(wg.c);
         var0.release(wg.R);
         h = false;
         LoggerFactory.getLogger("Larp/Startup").warn("Intro assets unavailable; vanilla startup fallback retained", var3);
      }

      U = wg.M(FabricLoader.getInstance().getConfigDir().resolve("larp-client/menu.json")).motion();
   }

   @Inject(method = "<init>", at = @At("RETURN"))
   private void g(CallbackInfo var1) {
      this.d = !this.fadeIn && !LarpClient.W;
      if (!this.d) {
         _q.c("Loading", "Resource reload overlay hidden; vanilla completion retained");
      }
   }

   @Inject(method = "isReadyToFadeOut", at = @At("HEAD"), cancellable = true)
   private void K(CallbackInfoReturnable<Boolean> var1) {
      if (!this.d) {
         var1.setReturnValue(true);
      }
   }

   @Inject(method = "tick", at = @At("TAIL"))
   private void Y(CallbackInfo var1) {
      if (this.fadeOutStart >= 0L && this.minecraft.gui.overlay() == this && (!this.d || h)) {
         if (this.d) {
            long var2 = System.nanoTime();
            if (!this.H) {
               this.H = true;
               sn.p.s(var2);
               _q.z("Startup", "Resources ready; finishing logo/title intro before menu reveal");
            }

            if (!sn.p.R(var2, U)) {
               return;
            }

            this.minecraft.getTextureManager().release(wg.c);
            this.minecraft.getTextureManager().release(wg.R);
            LarpClient.W = true;
            _q.z("Startup", "Black intro complete; destination visible and input released");
         }

         this.minecraft.gui.setOverlay(null);
      }
   }

   @Inject(method = "extractRenderState", at = @At("HEAD"), cancellable = true)
   private void f(GuiGraphicsExtractor var1, int var2, int var3, float var4, CallbackInfo var5) {
      if (!this.d) {
         var5.cancel();
         Screen var10 = this.minecraft.gui.screen();
         if (var10 != null) {
            var10.extractRenderStateWithTooltipAndSubtitles(var1, var2, var3, var4);
         } else {
            this.minecraft.gui.hud.extractDeferredSubtitles();
         }
      } else if (h) {
         var5.cancel();
         long var6 = System.nanoTime();
         sn var8 = sn.p;
         if (var8.m(var6, U) == sn.j.O) {
            _q.z("Startup", "Black intro started; logo spin, soft glow and typewriter title");
         }

         var8.E(var6);
         if (var8.p(var6, U) > 0.0) {
            Screen var9 = this.minecraft.gui.screen();
            if (var9 != null) {
               var9.extractRenderStateWithTooltipAndSubtitles(var1, -1, -1, var4);
            } else {
               this.minecraft.gui.hud.extractDeferredSubtitles();
            }

            var1.nextStratum();
         }

         v8.F(var1, var6, U);
      }
   }
}
