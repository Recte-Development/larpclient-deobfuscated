package com.larp.obf.q5682c6a8aca9b6ec;

import com.larpclient.LarpClient;
import com.larpclient.dd;
import com.larpclient.fr;
import com.larpclient.fs;
import com.larpclient.hd;
import com.larpclient.p0;
import com.larpclient.r4;
import com.larpclient.rf;
import com.larpclient.rv;
import com.larpclient.ys;
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
public abstract class q21c99fdde2209f4f {
   @Unique
   private boolean L;

   @Redirect(
      method = "extractRenderStateWithTooltipAndSubtitles",
      at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screens/Screen;extractBackground(Lnet/minecraft/client/gui/GuiGraphicsExtractor;IIF)V")
   )
   private void A(Screen var1, GuiGraphicsExtractor var2, int var3, int var4, float var5) {
      boolean var6 = var1 instanceof LevelLoadingScreen;
      if ((Minecraft.getInstance().level == null || var6) && !(var1 instanceof ChatScreen) && W(var1)) {
         if (var6 && !this.L) {
            this.L = true;
            r4.g("Menu", "Terrain loading uses the shared custom menu background");
         }

         boolean var7 = var1 instanceof ys var8 && var8.larpDrawingMenuPopup();
         if (var7) {
            var2.pose().pushMatrix();
            var2.pose().mul(ys.Z(var1).matrix(var1.width / 2.0, var1.height / 2.0).invert());
         }

         try {
            fs var13 = LarpClient.y().v().E();
            if (var1 instanceof hd var9) {
               dd.s(var2, var1.width, var1.height, var9.atmosphere(), var9.dimAtmosphere(), var13.o);
            } else {
               dd.s(var2, var1.width, var1.height, var13.A, var1.isInGameUi(), var13.o);
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
   private static boolean W(Screen var0) {
      return rf.H() ? true : var0.getClass().getName().startsWith("com.larpclient.") && !(var0 instanceof p0);
   }

   @Inject(method = "extractMenuBackgroundTexture", at = @At("HEAD"), cancellable = true)
   private static void R(GuiGraphicsExtractor var0, Identifier var1, int var2, int var3, float var4, float var5, int var6, int var7, CallbackInfo var8) {
      if (Minecraft.getInstance().level == null && rf.H()) {
         fr.k(var0, var2, var3, var6, var7, rv.q);
         var8.cancel();
      }
   }
}
