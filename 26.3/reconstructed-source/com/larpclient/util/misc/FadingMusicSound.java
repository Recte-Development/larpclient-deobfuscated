package com.larpclient.util.misc;

import com.larpclient.social.emote.EmoteRegistry;
import java.util.function.DoubleSupplier;
import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance.Attenuation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;

public final class FadingMusicSound extends AbstractTickableSoundInstance {
   private final DoubleSupplier volumeSupplier;
   private final int fadeTicks;
   private int fadeRemaining = -1;

   public FadingMusicSound(EmoteRegistry.Emote var1, DoubleSupplier var2, int var3) {
      super(SoundEvent.createVariableRangeEvent(var1.music()), SoundSource.PLAYERS, SoundInstance.createUnseededRandom());
      this.volumeSupplier = var2;
      this.fadeTicks = Math.max(1, var3);
      this.looping = (boolean)1;
      this.relative = (boolean)1;
      this.attenuation = Attenuation.NONE;
      this.volume = (float)var2.getAsDouble();
   }

   public void startFadeOut() {
      if (this.fadeRemaining < 0) {
         this.fadeRemaining = this.fadeTicks;
      }
   }

   public void stopNow() {
      this.stop();
   }

   public void tick() {
      float var1 = (float)Math.clamp(this.volumeSupplier.getAsDouble(), 0.0, 1.0);
      if (this.fadeRemaining >= 0) {
         if (this.fadeRemaining-- <= 0) {
            this.stop();
            return;
         }

         var1 *= (float)(this.fadeRemaining + 1) / (this.fadeTicks + 1);
      }

      this.volume = var1;
   }

   public boolean canStartSilent() {
      return true;
   }
}
