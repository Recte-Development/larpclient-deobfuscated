package com.larpclient;

import com.larpclient.module.WorldMenuModule;
import java.util.Locale;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance.Attenuation;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundSource;

public final class hx {
   private static long g;
   private static boolean z;

   private hx() {
   }

   public static SimpleSoundInstance X(hx.t var0) {
      return new SimpleSoundInstance(
         var0.G(), SoundSource.MASTER, var0.w, 1.0F, SoundInstance.createUnseededRandom(), false, 0, Attenuation.NONE, 0.0, 0.0, 0.0, true
      );
   }

   public static void w(hx.t var0) {
      if (var0 != null) {
         Minecraft var1 = Minecraft.getInstance();
         if (var1 != null && var1.gui != null && (pc.s(var1.gui.screen()) || var1.gui.screen() instanceof ChatScreen || WorldMenuModule.j())) {
            if (var0 == hx.t.D) {
               long var2 = System.nanoTime();
               if (var2 - g < 40000000L) {
                  return;
               }

               g = var2;
            }

            try {
               var1.getSoundManager().play(X(var0));
            } catch (RuntimeException var4) {
               if (!z) {
                  z = (boolean)1;
                  r4.r("GUI", "GUI sample playback failed; controls remain available", var4);
               }
            }
         }
      }
   }

   public enum t {
      O("open-gui", 0.8F),
      I("close-gui", 0.8F),
      X("buttonclick", 0.8F),
      D("slider", 0.8F),
      i("searchtyping", 0.7F),
      Y("switchcategory", 0.8F),
      F("switch-left", 0.8F),
      N("switch-right", 0.8F),
      h("settings-open", 0.8F),
      S("settings-close", 0.8F);

      public final String t;
      public final float w;

      t(String var3, float var4) {
         this.t = var3;
         this.w = var4;
      }

      public Identifier G() {
         return Identifier.fromNamespaceAndPath("larp-client", "gui." + this.name().toLowerCase(Locale.ROOT));
      }
   }
}
