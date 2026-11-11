package com.larpclient.util.misc;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance.Attenuation;
import net.minecraft.client.sounds.SoundEngine.PlayResult;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundSource;

public final class NotificationSound {
   private static final Identifier sOUND_ID = Identifier.fromNamespaceAndPath("larp-client", "notification");
   private static boolean volumeLogged;

   private NotificationSound() {
   }

   public static SimpleSoundInstance createSoundInstance() {
      return new SimpleSoundInstance(
         sOUND_ID, SoundSource.MASTER, 1.0F, 1.0F, SoundInstance.createUnseededRandom(), false, 0, Attenuation.NONE, 0.0, 0.0, 0.0, true
      );
   }

   public static void play() {
      Minecraft var0 = Minecraft.getInstance();
      if (var0 != null) {
         if (!var0.isSameThread()) {
            var0.execute(NotificationSound::play);
         } else if (!(var0.options.getFinalSoundSourceVolume(SoundSource.MASTER) <= 0.0F)) {
            try {
               PlayResult var1 = var0.getSoundManager().play(createSoundInstance());
               if (!volumeLogged) {
                  volumeLogged = (boolean)1;
                  LarpLog.info("ESP", "Notification sound uses Minecraft Master volume; playback={}", var1);
               }
            } catch (RuntimeException var2) {
               LarpLog.errorOnce("ESP", "Notification playback failed; alerts remain available, reload resources to retry", var2);
            }
         }
      }
   }
}
