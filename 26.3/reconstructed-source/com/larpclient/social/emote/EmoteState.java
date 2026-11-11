package com.larpclient.social.emote;

public interface EmoteState {
   float[] larp$emoteSample();

   float larp$emoteWeight();

   void larp$setEmoteWeight(float var1);

   String larp$emoteProp();

   float larp$emoteCycle();

   void larp$setEmoteProp(String var1, float var2);
}
