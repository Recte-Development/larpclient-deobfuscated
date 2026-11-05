package com.larpclient;

import java.util.function.DoubleSupplier;
import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance.Attenuation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;

public final class m7 extends AbstractTickableSoundInstance {
   private final DoubleSupplier n;
   private final int a;
   private int L = -1;

   public m7(p0.u var1, DoubleSupplier var2, int var3) {
      super(SoundEvent.createVariableRangeEvent(var1.music()), SoundSource.PLAYERS, SoundInstance.createUnseededRandom());
      this.n = var2;
      this.a = Math.max(1, var3);
      this.looping = (boolean)1;
      this.relative = (boolean)1;
      this.attenuation = Attenuation.NONE;
      this.volume = (float)var2.getAsDouble();
   }

   public void t() {
      if (this.L < 0) {
         this.L = this.a;
      }
   }

   public void q() {
      this.stop();
   }

   public void tick() {
      float var1 = (float)Math.clamp(this.n.getAsDouble(), 0.0, 1.0);
      if (this.L >= 0) {
         if (this.L-- <= 0) {
            this.stop();
            return;
         }

         var1 *= (float)(this.L + 1) / (this.a + 1);
      }

      this.volume = var1;
   }

   public boolean canStartSilent() {
      return true;
   }
}
