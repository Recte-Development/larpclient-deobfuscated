package com.larpclient;

import com.larpclient.module.Module;
import com.larpclient.module.StreamProofModule;

public final class cf {
   private static volatile boolean Q;
   private static volatile boolean v;

   private cf() {
   }

   public static boolean W(Module var0) {
      return v && StreamProofModule.w.contains(var0.getClass()) ? true : Q && StreamProofModule.d.contains(var0.getClass());
   }

   public static boolean J() {
      return v;
   }

   static void l(boolean var0) {
      v = var0;
   }

   static boolean t() {
      return Q;
   }

   static void B(boolean var0) {
      Q = var0;
   }
}
