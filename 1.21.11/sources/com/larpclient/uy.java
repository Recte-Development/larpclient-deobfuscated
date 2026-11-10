package com.larpclient;

import java.util.Objects;
import net.minecraft.class_1799;

public final class uy {
   private class_1799 m;
   private ch.r i;
   private Object G;

   public void g() {
      this.m = null;
      this.i = null;
      this.G = null;
   }

   public Object i(class_1799 var1, ch.r var2) {
      if (this.m == null || !class_1799.method_31577(this.m, var1) || !Objects.equals(this.i, var2)) {
         this.m = var1.method_7972();
         this.i = var2;
         this.G = new Object();
      }

      return this.G;
   }
}
