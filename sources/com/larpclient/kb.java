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

public final class kb {
   private static long r;
   private static boolean g;

   private kb() {
   }

   public static SimpleSoundInstance O(kb.n var0) {
      return new SimpleSoundInstance(
         var0.Z(), SoundSource.MASTER, var0.x, 1.0F, SoundInstance.createUnseededRandom(), false, 0, Attenuation.NONE, 0.0, 0.0, 0.0, true
      );
   }

   public static void d(kb.n var0) {
      if (var0 != null) {
         Minecraft var1 = Minecraft.getInstance();
         if (var1 != null && var1.gui != null && (h1.I(var1.gui.screen()) || var1.gui.screen() instanceof ChatScreen || WorldMenuModule.n())) {
            if (var0 == kb.n.m) {
               long var2 = System.nanoTime();
               if (var2 - r < 40000000L) {
                  return;
               }

               r = var2;
            }

            try {
               var1.getSoundManager().play(O(var0));
            } catch (RuntimeException var4) {
               if (!g) {
                  g = (boolean)1;
                  mt.P("GUI", "GUI sample playback failed; controls remain available", var4);
               }
            }
         }
      }
   }

   public enum n {
      K("open-gui", 0.8F),
      G("close-gui", 0.8F),
      w("buttonclick", 0.8F),
      m("slider", 0.8F),
      C("searchtyping", 0.7F),
      Q("switchcategory", 0.8F),
      O("switch-left", 0.8F),
      l("switch-right", 0.8F),
      T("settings-open", 0.8F),
      z("settings-close", 0.8F);

      public final String b;
      public final float x;

      n(String var3, float var4) {
         this.b = var3;
         this.x = var4;
      }

      public Identifier Z() {
         return Identifier.fromNamespaceAndPath("larp-client", "gui." + this.name().toLowerCase(Locale.ROOT));
      }
   }
}
