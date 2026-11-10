package com.larpclient;

import java.util.function.DoubleSupplier;
import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance.Attenuation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;

public final class hb extends AbstractTickableSoundInstance {
   private final DoubleSupplier F;
   private final int B;
   private int g = -1;

   public hb(il.w var1, DoubleSupplier var2, int var3) {
      super(SoundEvent.createVariableRangeEvent(var1.music()), SoundSource.PLAYERS, SoundInstance.createUnseededRandom());
      this.F = var2;
      this.B = Math.max(1, var3);
      this.looping = (boolean)1;
      this.relative = (boolean)1;
      this.attenuation = Attenuation.NONE;
      this.volume = (float)var2.getAsDouble();
   }

   public void v() {
      if (this.g < 0) {
         this.g = this.B;
      }
   }

   public void U() {
      this.stop();
   }

   public void tick() {
      float var1 = (float)Math.clamp(this.F.getAsDouble(), 0.0, 1.0);
      if (this.g >= 0) {
         if (this.g-- <= 0) {
            this.stop();
            return;
         }

         var1 *= (float)(this.g + 1) / (this.B + 1);
      }

      this.volume = var1;
   }

   public boolean canStartSilent() {
      return true;
   }
}
