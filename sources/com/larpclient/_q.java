package com.larpclient;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance.Attenuation;
import net.minecraft.client.sounds.SoundEngine.PlayResult;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundSource;

public final class _q {
   private static final Identifier h = Identifier.fromNamespaceAndPath("larp-client", "notification");
   private static boolean u;

   private _q() {
   }

   public static SimpleSoundInstance W() {
      return new SimpleSoundInstance(h, SoundSource.MASTER, 1.0F, 1.0F, SoundInstance.createUnseededRandom(), false, 0, Attenuation.NONE, 0.0, 0.0, 0.0, true);
   }

   public static void F() {
      Minecraft var0 = Minecraft.getInstance();
      if (var0 != null) {
         if (!var0.isSameThread()) {
            var0.execute(_q::F);
         } else if (!(var0.options.getFinalSoundSourceVolume(SoundSource.MASTER) <= 0.0F)) {
            try {
               PlayResult var1 = var0.getSoundManager().play(W());
               if (!u) {
                  u = (boolean)1;
                  mt.K("ESP", "Notification sound uses Minecraft Master volume; playback={}", var1);
               }
            } catch (RuntimeException var2) {
               mt.B("ESP", "Notification playback failed; alerts remain available, reload resources to retry", var2);
            }
         }
      }
   }
}
