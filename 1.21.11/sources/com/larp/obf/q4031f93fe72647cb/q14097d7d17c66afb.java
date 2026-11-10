package com.larp.obf.q4031f93fe72647cb;

import com.larpclient.cb;
import com.larpclient.ej;
import com.larpclient.q1;
import com.larpclient.q8;
import com.larpclient.module.EmotesModule;
import net.minecraft.class_10055;
import net.minecraft.class_1007;
import net.minecraft.class_11890;
import net.minecraft.class_4587;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(class_1007.class)
public abstract class q14097d7d17c66afb {
   @Inject(method = "method_62604(Lnet/minecraft/class_11890;Lnet/minecraft/class_10055;F)V", at = @At("TAIL"))
   private void N(class_11890 var1, class_10055 var2, float var3, CallbackInfo var4) {
      try {
         EmotesModule.V(var1, var2, var3);
      } catch (RuntimeException var6) {
         ((q1)var2).larp$setEmoteWeight(0.0F);
         q8.O("Emotes", "Emote sampling failed; native pose kept", var6);
      }
   }

   @Inject(method = "method_4212(Lnet/minecraft/class_10055;Lnet/minecraft/class_4587;FF)V", at = @At("RETURN"))
   private void D(class_10055 var1, class_4587 var2, float var3, float var4, CallbackInfo var5) {
      q1 var6 = (q1)var1;
      if (var6.larp$emoteWeight() > 0.0F) {
         ej.m(var2, var6.larp$emoteSample(), var6.larp$emoteWeight());
      }
   }

   @Mixin(class_10055.class)
   public abstract static class q8d5da015334aaff1 implements q1 {
      @Unique
      private final float[] q = new float[cb.w];
      @Unique
      private float B;
      @Unique
      private float s;
      @Unique
      private String X;

      @Override
      public float[] larp$emoteSample() {
         return this.q;
      }

      @Override
      public float larp$emoteWeight() {
         return this.B;
      }

      @Override
      public void larp$setEmoteWeight(float var1) {
         this.B = var1;
      }

      @Override
      public String larp$emoteProp() {
         return this.X;
      }

      @Override
      public float larp$emoteCycle() {
         return this.s;
      }

      @Override
      public void larp$setEmoteProp(String var1, float var2) {
         this.X = var1;
         this.s = var2;
      }
   }
}
