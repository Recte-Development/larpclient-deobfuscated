package com.larpclient;

import net.minecraft.class_310;
import net.minecraft.class_437;

public interface qx {
   void larpOpenMenuPopup();

   jr larpMenuPopup();

   boolean larpDrawingMenuPopup();

   static jr P(class_437 var0) {
      return var0 instanceof qx var1 ? var1.larpMenuPopup() : null;
   }

   static ix.g A(class_437 var0) {
      if (class_310.method_1551().field_1687 != null) {
         return ix.g.REST;
      }

      jr var1 = P(var0);
      return var1 == null ? ix.g.REST : var1.V();
   }

   static boolean x() {
      class_310 var0 = class_310.method_1551();
      y0 var1 = LarpClient.b().e().F();
      return var1.k && var1.U != td.r.y && (Double)var0.field_1690.method_45581().method_41753() > 0.0;
   }
}
