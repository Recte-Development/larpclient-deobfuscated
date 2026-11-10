package com.larpclient;

import com.larpclient.module.HandChamsModule;
import net.minecraft.class_310;

public final class hi {
   private static final wd E = new wd("HandChams");
   private static ba.e j;
   private static Object J;
   private static Object H;

   public static boolean c() {
      class_310 var0 = class_310.method_1551();
      HandChamsModule var1 = HandChamsModule.Z();
      return var1 != null
         && var1.isRuntimeActive()
         && var0.field_1687 != null
         && var0.field_1724 != null
         && var0.field_1690.method_31044().method_31034()
         && !var0.field_1690.field_1842
         && !var0.field_1724.method_6113()
         && !var0.field_1724.method_7325();
   }

   public static boolean Y() {
      if (!c()) {
         c();
         return false;
      }

      class_310 var0 = class_310.method_1551();
      if (J != var0.field_1687 || H != var0.field_1724) {
         c();
      }

      J = var0.field_1687;
      H = var0.field_1724;
      j = HandChamsModule.Z().C.g(false);
      if (E.P()) {
         return true;
      }

      j = null;
      return false;
   }

   public static void J() {
      if (j != null) {
         if (c() && J == class_310.method_1551().field_1687 && H == class_310.method_1551().field_1724) {
            ba.e var0 = j;
            j = null;
            E.H(var0);
         } else {
            c();
         }
      }
   }

   public static void P() {
      if (!c() || J != null && (J != class_310.method_1551().field_1687 || H != class_310.method_1551().field_1724)) {
         c();
      }
   }

   public static void c() {
      j = null;
      H = null;
      J = null;
      E.close();
   }

   private hi() {
   }
}
