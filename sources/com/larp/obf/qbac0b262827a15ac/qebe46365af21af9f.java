package com.larp.obf.qbac0b262827a15ac;

import com.larpclient.module.RemovalsModule;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.sounds.SoundEngine;
import net.minecraft.client.sounds.ChannelAccess.ChannelHandle;
import net.minecraft.client.sounds.SoundEngine.PlayResult;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SoundEngine.class)
public abstract class qebe46365af21af9f {
   @Shadow
   @Final
   private Map<SoundInstance, ChannelHandle> instanceToChannel;
   @Unique
   private final Set<SoundInstance> c = new HashSet<>();

   @Shadow
   private float calculateVolume(SoundInstance var1) {
      throw new AssertionError("Mixin shadow");
   }

   @Inject(method = "tickInGameSound", at = @At("TAIL"))
   private void v(CallbackInfo var1) {
      if (RemovalsModule.e() != null && (RemovalsModule.e().isRuntimeActive() || !this.c.isEmpty())) {
         this.c.retainAll(this.instanceToChannel.keySet());

         for (Entry var3 : this.instanceToChannel.entrySet()) {
            SoundInstance var4 = (SoundInstance)var3.getKey();
            if (RemovalsModule.e(var4.getIdentifier().toString())) {
               if (this.c.add(var4)) {
                  ((ChannelHandle)var3.getValue()).execute(var0 -> var0.setVolume(0.0F));
               }
            } else if (this.c.remove(var4)) {
               float var5 = this.calculateVolume(var4);
               ((ChannelHandle)var3.getValue()).execute(var1x -> var1x.setVolume(var5));
            }
         }
      }
   }

   @Inject(method = "play", at = @At("HEAD"), cancellable = true)
   private void Y(SoundInstance var1, CallbackInfoReturnable<PlayResult> var2) {
      if (RemovalsModule.e(var1.getIdentifier().toString())) {
         var2.setReturnValue(PlayResult.NOT_STARTED);
      }
   }

   @Inject(method = "calculateVolume(Lnet/minecraft/client/resources/sounds/SoundInstance;)F", at = @At("RETURN"), cancellable = true)
   private void N(SoundInstance var1, CallbackInfoReturnable<Float> var2) {
      if (RemovalsModule.e(var1.getIdentifier().toString())) {
         var2.setReturnValue(0.0F);
      }
   }
}
