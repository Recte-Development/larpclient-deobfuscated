package com.larp.obf.q41a5851bcf8e876a;

import com.larpclient.LarpClient;
import com.larpclient._q;
import com.larpclient.c;
import com.larpclient.d7;
import com.larpclient.de;
import com.larpclient.f8;
import com.larpclient.h5;
import com.larpclient.i3;
import com.larpclient.na;
import com.larpclient.sd;
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
public abstract class qa215472f0256287f {
   @Unique
   private boolean q;

   @Redirect(
      method = "extractRenderStateWithTooltipAndSubtitles",
      at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screens/Screen;extractBackground(Lnet/minecraft/client/gui/GuiGraphicsExtractor;IIF)V")
   )
   private void p(Screen var1, GuiGraphicsExtractor var2, int var3, int var4, float var5) {
      boolean var6 = var1 instanceof LevelLoadingScreen;
      if ((Minecraft.getInstance().level == null || var6) && !(var1 instanceof ChatScreen) && N(var1)) {
         if (var6 && !this.q) {
            this.q = true;
            _q.c("Menu", "Terrain loading uses the shared custom menu background");
         }

         boolean var7 = var1 instanceof sd var8 && var8.larpDrawingMenuPopup();
         if (var7) {
            var2.pose().pushMatrix();
            var2.pose().mul(sd.R(var1).matrix(var1.width / 2.0, var1.height / 2.0).invert());
         }

         try {
            i3 var13 = LarpClient.N().h().k();
            if (var1 instanceof d7 var9) {
               na.Q(var2, var1.width, var1.height, var9.atmosphere(), var9.dimAtmosphere(), var13.w);
            } else {
               na.Q(var2, var1.width, var1.height, var13.A, var1.isInGameUi(), var13.w);
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
   private static boolean N(Screen var0) {
      return de.B() ? true : var0.getClass().getName().startsWith("com.larpclient.") && !(var0 instanceof f8);
   }

   @Inject(method = "extractMenuBackgroundTexture", at = @At("HEAD"), cancellable = true)
   private static void Y(GuiGraphicsExtractor var0, Identifier var1, int var2, int var3, float var4, float var5, int var6, int var7, CallbackInfo var8) {
      if (Minecraft.getInstance().level == null && de.B()) {
         h5.N(var0, var2, var3, var6, var7, c.I);
         var8.cancel();
      }
   }
}
