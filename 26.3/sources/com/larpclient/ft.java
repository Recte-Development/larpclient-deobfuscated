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

public final class ft {
   private static long s;
   private static boolean U;

   private ft() {
   }

   public static SimpleSoundInstance a(ft.e var0) {
      return new SimpleSoundInstance(
         var0.w(), SoundSource.MASTER, var0.F, 1.0F, SoundInstance.createUnseededRandom(), false, 0, Attenuation.NONE, 0.0, 0.0, 0.0, true
      );
   }

   public static void h(ft.e var0) {
      if (var0 != null) {
         Minecraft var1 = Minecraft.getInstance();
         if (var1 != null && var1.gui != null && (fj.X(var1.gui.screen()) || var1.gui.screen() instanceof ChatScreen || WorldMenuModule.m())) {
            if (var0 == ft.e.C) {
               long var2 = System.nanoTime();
               if (var2 - s < 40000000L) {
                  return;
               }

               s = var2;
            }

            try {
               var1.getSoundManager().play(a(var0));
            } catch (RuntimeException var4) {
               if (!U) {
                  U = (boolean)1;
                  _q.o("GUI", "GUI sample playback failed; controls remain available", var4);
               }
            }
         }
      }
   }

   public enum e {
      D("open-gui", 0.8F),
      n("close-gui", 0.8F),
      K("buttonclick", 0.8F),
      C("slider", 0.8F),
      t("searchtyping", 0.7F),
      E("switchcategory", 0.8F),
      J("switch-left", 0.8F),
      y("switch-right", 0.8F),
      Q("settings-open", 0.8F),
      X("settings-close", 0.8F);

      public final String u;
      public final float F;

      e(String var3, float var4) {
         this.u = var3;
         this.F = var4;
      }

      public Identifier w() {
         return Identifier.fromNamespaceAndPath("larp-client", "gui." + this.name().toLowerCase(Locale.ROOT));
      }
   }
}
