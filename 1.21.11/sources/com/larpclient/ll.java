package com.larpclient;

import net.minecraft.class_12075;
import net.minecraft.class_310;

public interface ll {
   String effectName();

   void tick(class_310 var1);

   void prepare(float var1, class_12075 var2);

   void render(f1 var1, boolean var2);

   boolean glowing();

   default boolean soft() {
      return false;
   }

   void clear();

   default void T() {
      this.clear();
   }
}
