package com.larp.obf.q41a5851bcf8e876a;

import com.larpclient.social.emote.EmoteAnimation;
import com.larpclient.social.emote.EmoteState;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(AvatarRenderState.class)
public abstract class qaef0630073b0e0da$q91b1b4fc62dcd63b implements EmoteState {
   @Unique
   private final float[] Z = new float[EmoteAnimation.totalChannels];
   @Unique
   private float k;
   @Unique
   private float L;
   @Unique
   private String g;

   @Override
   public float[] larp$emoteSample() {
      return this.Z;
   }

   @Override
   public float larp$emoteWeight() {
      return this.k;
   }

   @Override
   public void larp$setEmoteWeight(float var1) {
      this.k = var1;
   }

   @Override
   public String larp$emoteProp() {
      return this.g;
   }

   @Override
   public float larp$emoteCycle() {
      return this.L;
   }

   @Override
   public void larp$setEmoteProp(String var1, float var2) {
      this.g = var1;
      this.L = var2;
   }
}
