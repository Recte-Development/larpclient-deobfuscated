package com.larpclient;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.state.level.CameraRenderState;

public interface qe {
   String effectName();

   void tick(Minecraft var1);

   void prepare(float var1, CameraRenderState var2);

   void render(fz var1, boolean var2);

   boolean glowing();

   default boolean soft() {
      return false;
   }

   void clear();

   default void n() {
      this.clear();
   }
}
