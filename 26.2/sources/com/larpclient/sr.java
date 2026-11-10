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

public final class sr {
   private static long U;
   private static boolean H;

   private sr() {
   }

   public static SimpleSoundInstance v(sr.e var0) {
      return new SimpleSoundInstance(
         var0.f(), SoundSource.MASTER, var0.U, 1.0F, SoundInstance.createUnseededRandom(), false, 0, Attenuation.NONE, 0.0, 0.0, 0.0, true
      );
   }

   public static void v(sr.e var0) {
      if (var0 != null) {
         Minecraft var1 = Minecraft.getInstance();
         if (var1 != null && var1.gui != null && (hi.X(var1.gui.screen()) || var1.gui.screen() instanceof ChatScreen || WorldMenuModule.e())) {
            if (var0 == sr.e.Q) {
               long var2 = System.nanoTime();
               if (var2 - U < 40000000L) {
                  return;
               }

               U = var2;
            }

            try {
               var1.getSoundManager().play(v(var0));
            } catch (RuntimeException var4) {
               if (!H) {
                  H = (boolean)1;
                  h.m("GUI", "GUI sample playback failed; controls remain available", var4);
               }
            }
         }
      }
   }

   public enum e {
      X("open-gui", 0.8F),
      s("close-gui", 0.8F),
      q("buttonclick", 0.8F),
      Q("slider", 0.8F),
      g("searchtyping", 0.7F),
      T("switchcategory", 0.8F),
      v("switch-left", 0.8F),
      J("switch-right", 0.8F),
      S("settings-open", 0.8F),
      d("settings-close", 0.8F);

      public final String Z;
      public final float U;

      e(String var3, float var4) {
         this.Z = var3;
         this.U = var4;
      }

      public Identifier f() {
         return Identifier.fromNamespaceAndPath("larp-client", "gui." + this.name().toLowerCase(Locale.ROOT));
      }
   }
}
