package com.larpclient.gui.misc;

import com.larpclient.gui.clickgui.LegacyClickGuiScreen;
import com.larpclient.module.misc.WorldMenuModule;
import com.larpclient.util.misc.LarpLog;
import java.util.Locale;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance.Attenuation;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundSource;

public final class GuiSounds {
   private static long lastSliderSoundNanos;
   private static boolean playbackFailureLogged;

   private GuiSounds() {
   }

   public static SimpleSoundInstance createSound(GuiSounds.Sound var0) {
      return new SimpleSoundInstance(
         var0.soundId(), SoundSource.MASTER, var0.VOLUME, 1.0F, SoundInstance.createUnseededRandom(), false, 0, Attenuation.NONE, 0.0, 0.0, 0.0, true
      );
   }

   public static void play(GuiSounds.Sound var0) {
      if (var0 != null) {
         Minecraft var1 = Minecraft.getInstance();
         if (var1 != null
            && var1.gui != null
            && (LegacyClickGuiScreen.isClickGuiScreen(var1.gui.screen()) || var1.gui.screen() instanceof ChatScreen || WorldMenuModule.hasOpenMenu())) {
            if (var0 == GuiSounds.Sound.SLIDER) {
               long var2 = System.nanoTime();
               if (var2 - lastSliderSoundNanos < 40000000L) {
                  return;
               }

               lastSliderSoundNanos = var2;
            }

            try {
               var1.getSoundManager().play(createSound(var0));
            } catch (RuntimeException var4) {
               if (!playbackFailureLogged) {
                  playbackFailureLogged = (boolean)1;
                  LarpLog.error("GUI", "GUI sample playback failed; controls remain available", var4);
               }
            }
         }
      }
   }

   public enum Sound {
      OPEN("open-gui", 0.8F),
      CLOSE("close-gui", 0.8F),
      CLICK("buttonclick", 0.8F),
      SLIDER("slider", 0.8F),
      TYPING("searchtyping", 0.7F),
      SWITCH_CATEGORY("switchcategory", 0.8F),
      SWITCH_OFF("switch-left", 0.8F),
      SWITCH_ON("switch-right", 0.8F),
      SETTINGS_OPEN("settings-open", 0.8F),
      SETTINGS_CLOSE("settings-close", 0.8F);

      public final String FILENAME;
      public final float VOLUME;

      Sound(String var3, float var4) {
         this.FILENAME = var3;
         this.VOLUME = var4;
      }

      public Identifier soundId() {
         return Identifier.fromNamespaceAndPath("larp-client", "gui." + this.name().toLowerCase(Locale.ROOT));
      }
   }
}
