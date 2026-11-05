package com.larp.obf.qbac0b262827a15ac;

import com.larpclient.LarpClient;
import com.larpclient.c7;
import com.larpclient.dg;
import com.larpclient.f;
import com.larpclient.hw;
import com.larpclient.mt;
import com.larpclient.uj;
import com.larpclient.wv;
import com.larpclient.xv;
import com.larpclient.z2;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.client.gui.screens.LevelLoadingScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.resources.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Screen.class)
public abstract class q6b79c50edcf0b80d {
   @Unique
   private boolean E;

   @Redirect(
      method = "extractRenderStateWithTooltipAndSubtitles",
      at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screens/Screen;extractBackground(Lnet/minecraft/client/gui/GuiGraphicsExtractor;IIF)V")
   )
   private void M(Screen var1, GuiGraphicsExtractor var2, int var3, int var4, float var5) {
      boolean var6 = var1 instanceof LevelLoadingScreen;
      if ((Minecraft.getInstance().level == null || var6) && !(var1 instanceof ChatScreen) && a(var1)) {
         if (var6 && !this.E) {
            this.E = true;
            mt.U("Menu", "Terrain loading uses the shared custom menu background");
         }

         boolean var7 = var1 instanceof wv var8 && var8.larpDrawingMenuPopup();
         if (var7) {
            var2.pose().pushMatrix();
            var2.pose().mul(wv.K(var1).matrix(var1.width / 2.0, var1.height / 2.0).invert());
         }

         try {
            xv var13 = LarpClient.F().f().t();
            if (var1 instanceof z2 var9) {
               dg.r(var2, var1.width, var1.height, var9.atmosphere(), var9.dimAtmosphere(), var13.V);
            } else {
               dg.r(var2, var1.width, var1.height, var13.n, var1.isInGameUi(), var13.V);
            }

            Minecraft.getInstance().gui.hud.extractDeferredSubtitles();
         } finally {
            if (var7) {
               var2.pose().popMatrix();
            }
         }
      } else {
         var1.extractBackground(var2, var3, var4, var5);
      }
   }

   @Unique
   private static boolean a(Screen var0) {
      return uj.l() ? true : var0.getClass().getName().startsWith("com.larpclient.") && !(var0 instanceof hw);
   }

   @Inject(method = "extractMenuBackgroundTexture", at = @At("HEAD"), cancellable = true)
   private static void Z(GuiGraphicsExtractor var0, Identifier var1, int var2, int var3, float var4, float var5, int var6, int var7, CallbackInfo var8) {
      if (Minecraft.getInstance().level == null && uj.l()) {
         c7.s(var0, var2, var3, var6, var7, f.m);
         var8.cancel();
      }
   }
}
