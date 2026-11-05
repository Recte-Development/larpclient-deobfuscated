package com.larp.obf.qbac0b262827a15ac;

import com.larpclient.LarpClient;
import com.larpclient.mt;
import com.larpclient.th;
import com.larpclient.ux;
import com.larpclient.v;
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
public class q6bac70889f54bf28 {
   @Shadow
   @Final
   private Minecraft minecraft;
   @Shadow
   @Final
   private boolean fadeIn;
   @Shadow
   private long fadeOutStart;
   @Unique
   private boolean v;
   @Unique
   private boolean X;
   @Unique
   private static boolean c;
   @Unique
   private static boolean O = true;

   @Inject(method = "registerTextures", at = @At("TAIL"))
   private static void X(TextureManager var0, CallbackInfo var1) {
      try {
         var0.registerAndLoad(ux.x, new ux());
         var0.registerAndLoad(ux.M, new ux(ux.M, "textures/gui/startup-brand.png"));
         c = true;
      } catch (RuntimeException var3) {
         var0.release(ux.x);
         var0.release(ux.M);
         c = false;
         LoggerFactory.getLogger("Larp/Startup").warn("Intro assets unavailable; vanilla startup fallback retained", var3);
      }

      O = ux.X(FabricLoader.getInstance().getConfigDir().resolve("larp-client/menu.json")).motion();
   }

   @Inject(method = "<init>", at = @At("RETURN"))
   private void v(CallbackInfo var1) {
      this.v = !this.fadeIn && !LarpClient.h;
      if (!this.v) {
         mt.U("Loading", "Resource reload overlay hidden; vanilla completion retained");
      }
   }

   @Inject(method = "isReadyToFadeOut", at = @At("HEAD"), cancellable = true)
   private void X(CallbackInfoReturnable<Boolean> var1) {
      if (!this.v) {
         var1.setReturnValue(true);
      }
   }

   @Inject(method = "tick", at = @At("TAIL"))
   private void l(CallbackInfo var1) {
      if (this.fadeOutStart >= 0L && this.minecraft.gui.overlay() == this && (!this.v || c)) {
         if (this.v) {
            long var2 = System.nanoTime();
            if (!this.X) {
               this.X = true;
               com.larpclient.v.s.b(var2);
               mt.K("Startup", "Resources ready; finishing logo/title intro before menu reveal");
            }

            if (!com.larpclient.v.s.x(var2, O)) {
               return;
            }

            this.minecraft.getTextureManager().release(ux.x);
            this.minecraft.getTextureManager().release(ux.M);
            LarpClient.h = true;
            mt.K("Startup", "Black intro complete; destination visible and input released");
         }

         this.minecraft.gui.setOverlay(null);
      }
   }

   @Inject(method = "extractRenderState", at = @At("HEAD"), cancellable = true)
   private void B(GuiGraphicsExtractor var1, int var2, int var3, float var4, CallbackInfo var5) {
      if (!this.v) {
         var5.cancel();
         Screen var10 = this.minecraft.gui.screen();
         if (var10 != null) {
            var10.extractRenderStateWithTooltipAndSubtitles(var1, var2, var3, var4);
         } else {
            this.minecraft.gui.hud.extractDeferredSubtitles();
         }
      } else if (c) {
         var5.cancel();
         long var6 = System.nanoTime();
         v var8 = com.larpclient.v.s;
         if (var8.L(var6, O) == v.o.H) {
            mt.K("Startup", "Black intro started; logo spin, soft glow and typewriter title");
         }

         var8.W(var6);
         if (var8.M(var6, O) > 0.0) {
            Screen var9 = this.minecraft.gui.screen();
            if (var9 != null) {
               var9.extractRenderStateWithTooltipAndSubtitles(var1, -1, -1, var4);
            } else {
               this.minecraft.gui.hud.extractDeferredSubtitles();
            }

            var1.nextStratum();
         }

         th.P(var1, var6, O);
      }
   }
}
