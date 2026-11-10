package com.larp.obf.qa300483f54d804a3;

import com.larpclient.LarpClient;
import com.larpclient.h;
import com.larpclient.hb;
import com.larpclient.hf;
import com.larpclient.ho;
import com.larpclient.kv;
import com.larpclient.l2;
import com.larpclient.ma;
import com.larpclient.mh;
import com.larpclient.tw;
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
public abstract class qddf8e3b1c9a9fe49 {
   @Unique
   private boolean R;

   @Redirect(
      method = "extractRenderStateWithTooltipAndSubtitles",
      at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screens/Screen;extractBackground(Lnet/minecraft/client/gui/GuiGraphicsExtractor;IIF)V")
   )
   private void o(Screen var1, GuiGraphicsExtractor var2, int var3, int var4, float var5) {
      boolean var6 = var1 instanceof LevelLoadingScreen;
      if ((Minecraft.getInstance().level == null || var6) && !(var1 instanceof ChatScreen) && S(var1)) {
         if (var6 && !this.R) {
            this.R = true;
            h.U("Menu", "Terrain loading uses the shared custom menu background");
         }

         boolean var7 = var1 instanceof tw var8 && var8.larpDrawingMenuPopup();
         if (var7) {
            var2.pose().pushMatrix();
            var2.pose().mul(tw.E(var1).matrix(var1.width / 2.0, var1.height / 2.0).invert());
         }

         try {
            hb var13 = LarpClient.R().o().g();
            if (var1 instanceof ma var9) {
               l2.X(var2, var1.width, var1.height, var9.atmosphere(), var9.dimAtmosphere(), var13.L);
            } else {
               l2.X(var2, var1.width, var1.height, var13.FH, var1.isInGameUi(), var13.L);
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
   private static boolean S(Screen var0) {
      return mh.j() ? true : var0.getClass().getName().startsWith("com.larpclient.") && !(var0 instanceof hf);
   }

   @Inject(method = "extractMenuBackgroundTexture", at = @At("HEAD"), cancellable = true)
   private static void j(GuiGraphicsExtractor var0, Identifier var1, int var2, int var3, float var4, float var5, int var6, int var7, CallbackInfo var8) {
      if (Minecraft.getInstance().level == null && mh.j()) {
         kv.d(var0, var2, var3, var6, var7, ho.dy);
         var8.cancel();
      }
   }
}
