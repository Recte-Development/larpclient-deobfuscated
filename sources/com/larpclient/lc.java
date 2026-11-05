package com.larpclient;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance.Attenuation;
import net.minecraft.client.sounds.SoundEngine.PlayResult;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundSource;

public final class lc {
   private static final Identifier e = Identifier.fromNamespaceAndPath("larp-client", "notification");
   private static boolean R;

   private lc() {
   }

   public static SimpleSoundInstance g() {
      return new SimpleSoundInstance(e, SoundSource.MASTER, 1.0F, 1.0F, SoundInstance.createUnseededRandom(), false, 0, Attenuation.NONE, 0.0, 0.0, 0.0, true);
   }

   public static void r() {
      Minecraft var0 = Minecraft.getInstance();
      if (var0 != null) {
         if (!var0.isSameThread()) {
            var0.execute(lc::r);
         } else if (!(var0.options.getFinalSoundSourceVolume(SoundSource.MASTER) <= 0.0F)) {
            try {
               PlayResult var1 = var0.getSoundManager().play(g());
               if (!R) {
                  R = (boolean)1;
                  r4.V("ESP", "Notification sound uses Minecraft Master volume; playback={}", var1);
               }
            } catch (RuntimeException var2) {
               r4.T("ESP", "Notification playback failed; alerts remain available, reload resources to retry", var2);
            }
         }
      }
   }
}
