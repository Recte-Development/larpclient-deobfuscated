package com.larpclient;

import java.util.function.DoubleSupplier;
import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance.Attenuation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;

public final class bp extends AbstractTickableSoundInstance {
   private final DoubleSupplier T;
   private final int d;
   private int q = -1;

   public bp(mf.h var1, DoubleSupplier var2, int var3) {
      super(SoundEvent.createVariableRangeEvent(var1.music()), SoundSource.PLAYERS, SoundInstance.createUnseededRandom());
      this.T = var2;
      this.d = Math.max(1, var3);
      this.looping = (boolean)1;
      this.relative = (boolean)1;
      this.attenuation = Attenuation.NONE;
      this.volume = (float)var2.getAsDouble();
   }

   public void p() {
      if (this.q < 0) {
         this.q = this.d;
      }
   }

   public void C() {
      this.stop();
   }

   public void tick() {
      float var1 = (float)Math.clamp(this.T.getAsDouble(), 0.0, 1.0);
      if (this.q >= 0) {
         if (this.q-- <= 0) {
            this.stop();
            return;
         }

         var1 *= (float)(this.q + 1) / (this.d + 1);
      }

      this.volume = var1;
   }

   public boolean canStartSilent() {
      return true;
   }
}
