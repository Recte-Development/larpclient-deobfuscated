package com.larp.obf.q5682c6a8aca9b6ec;

import com.larpclient.LarpClient;
import com.larpclient._z;
import com.larpclient.eb;
import com.larpclient.ox;
import com.larpclient.r4;
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
public class qc6700023e81e6e58 {
   @Shadow
   @Final
   private Minecraft minecraft;
   @Shadow
   @Final
   private boolean fadeIn;
   @Shadow
   private long fadeOutStart;
   @Unique
   private boolean j;
   @Unique
   private boolean J;
   @Unique
   private static boolean o;
   @Unique
   private static boolean k = true;

   @Inject(method = "registerTextures", at = @At("TAIL"))
   private static void x(TextureManager var0, CallbackInfo var1) {
      try {
         var0.registerAndLoad(eb.j, new eb());
         var0.registerAndLoad(eb.C, new eb(eb.C, "textures/gui/startup-brand.png"));
         o = true;
      } catch (RuntimeException var3) {
         var0.release(eb.j);
         var0.release(eb.C);
         o = false;
         LoggerFactory.getLogger("Larp/Startup").warn("Intro assets unavailable; vanilla startup fallback retained", var3);
      }

      k = eb.R(FabricLoader.getInstance().getConfigDir().resolve("larp-client/menu.json")).motion();
   }

   @Inject(method = "<init>", at = @At("RETURN"))
   private void i(CallbackInfo var1) {
      this.j = !this.fadeIn && !LarpClient.N;
      if (!this.j) {
         r4.g("Loading", "Resource reload overlay hidden; vanilla completion retained");
      }
   }

   @Inject(method = "isReadyToFadeOut", at = @At("HEAD"), cancellable = true)
   private void r(CallbackInfoReturnable<Boolean> var1) {
      if (!this.j) {
         var1.setReturnValue(true);
      }
   }

   @Inject(method = "tick", at = @At("TAIL"))
   private void O(CallbackInfo var1) {
      if (this.fadeOutStart >= 0L && this.minecraft.gui.overlay() == this && (!this.j || o)) {
         if (this.j) {
            long var2 = System.nanoTime();
            if (!this.J) {
               this.J = true;
               ox.t.O(var2);
               r4.V("Startup", "Resources ready; finishing logo/title intro before menu reveal");
            }

            if (!ox.t.n(var2, k)) {
               return;
            }

            this.minecraft.getTextureManager().release(eb.j);
            this.minecraft.getTextureManager().release(eb.C);
            LarpClient.N = true;
            r4.V("Startup", "Black intro complete; destination visible and input released");
         }

         this.minecraft.gui.setOverlay(null);
      }
   }

   @Inject(method = "extractRenderState", at = @At("HEAD"), cancellable = true)
   private void q(GuiGraphicsExtractor var1, int var2, int var3, float var4, CallbackInfo var5) {
      if (!this.j) {
         var5.cancel();
         Screen var10 = this.minecraft.gui.screen();
         if (var10 != null) {
            var10.extractRenderStateWithTooltipAndSubtitles(var1, var2, var3, var4);
         } else {
            this.minecraft.gui.hud.extractDeferredSubtitles();
         }
      } else if (o) {
         var5.cancel();
         long var6 = System.nanoTime();
         ox var8 = ox.t;
         if (var8.m(var6, k) == ox.l.k) {
            r4.V("Startup", "Black intro started; logo spin, soft glow and typewriter title");
         }

         var8.j(var6);
         if (var8.B(var6, k) > 0.0) {
            Screen var9 = this.minecraft.gui.screen();
            if (var9 != null) {
               var9.extractRenderStateWithTooltipAndSubtitles(var1, -1, -1, var4);
            } else {
               this.minecraft.gui.hud.extractDeferredSubtitles();
            }

            var1.nextStratum();
         }

         _z.V(var1, var6, k);
      }
   }
}
