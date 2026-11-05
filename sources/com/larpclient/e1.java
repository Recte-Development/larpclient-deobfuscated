package com.larpclient;

import java.util.function.DoubleSupplier;
import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance.Attenuation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;

public final class e1 extends AbstractTickableSoundInstance {
   private final DoubleSupplier Z;
   private final int w;
   private int M = -1;

   public e1(ya.g var1, DoubleSupplier var2, int var3) {
      super(SoundEvent.createVariableRangeEvent(var1.music()), SoundSource.PLAYERS, SoundInstance.createUnseededRandom());
      this.Z = var2;
      this.w = Math.max(1, var3);
      this.looping = (boolean)1;
      this.relative = (boolean)1;
      this.attenuation = Attenuation.NONE;
      this.volume = (float)var2.getAsDouble();
   }

   public void G() {
      if (this.M < 0) {
         this.M = this.w;
      }
   }

   public void a() {
      this.stop();
   }

   public void tick() {
      float var1 = (float)Math.clamp(this.Z.getAsDouble(), 0.0, 1.0);
      if (this.M >= 0) {
         if (this.M-- <= 0) {
            this.stop();
            return;
         }

         var1 *= (float)(this.M + 1) / (this.w + 1);
      }

      this.volume = var1;
   }

   public boolean canStartSilent() {
      return true;
   }
}
