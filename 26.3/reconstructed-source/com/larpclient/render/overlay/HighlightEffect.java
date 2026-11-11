package com.larpclient.render.overlay;

import com.larpclient.render.world.EffectsRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.state.level.CameraRenderState;

public interface HighlightEffect {
   String effectName();

   void tick(Minecraft var1);

   void prepare(float var1, CameraRenderState var2);

   void render(EffectsRenderer var1, boolean var2);

   boolean glowing();

   default boolean soft() {
      return false;
   }

   void clear();

   default void reset() {
      this.clear();
   }
}
