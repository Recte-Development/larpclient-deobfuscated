package com.larpclient;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance.Attenuation;
import net.minecraft.client.sounds.SoundEngine.PlayResult;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundSource;

public final class ms {
   private static final Identifier K = Identifier.fromNamespaceAndPath("larp-client", "notification");
   private static boolean k;

   private ms() {
   }

   public static SimpleSoundInstance W() {
      return new SimpleSoundInstance(K, SoundSource.MASTER, 1.0F, 1.0F, SoundInstance.createUnseededRandom(), false, 0, Attenuation.NONE, 0.0, 0.0, 0.0, true);
   }

   public static void o() {
      Minecraft var0 = Minecraft.getInstance();
      if (var0 != null) {
         if (!var0.isSameThread()) {
            var0.execute(ms::o);
         } else if (!(var0.options.getFinalSoundSourceVolume(SoundSource.MASTER) <= 0.0F)) {
            try {
               PlayResult var1 = var0.getSoundManager().play(W());
               if (!k) {
                  k = (boolean)1;
                  h.A("ESP", "Notification sound uses Minecraft Master volume; playback={}", var1);
               }
            } catch (RuntimeException var2) {
               h.w("ESP", "Notification playback failed; alerts remain available, reload resources to retry", var2);
            }
         }
      }
   }
}
