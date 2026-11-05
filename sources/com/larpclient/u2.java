package com.larpclient;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.state.level.CameraRenderState;

public interface u2 {
   String effectName();

   void tick(Minecraft var1);

   void prepare(float var1, CameraRenderState var2);

   void render(r6 var1, boolean var2);

   boolean glowing();

   default boolean soft() {
      return false;
   }

   void clear();

   default void c() {
      this.clear();
   }
}
