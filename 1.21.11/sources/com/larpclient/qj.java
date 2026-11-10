package com.larpclient;

import com.larpclient.module.Module;
import com.larpclient.module.StreamProofModule;

public final class qj {
   private static volatile boolean D;
   private static volatile boolean Y;

   private qj() {
   }

   public static boolean X(Module var0) {
      return Y && StreamProofModule.e.contains(var0.getClass()) ? true : D && StreamProofModule.w.contains(var0.getClass());
   }

   public static boolean u() {
      return Y;
   }

   static void B(boolean var0) {
      Y = var0;
   }

   static boolean r() {
      return D;
   }

   static void u(boolean var0) {
      D = var0;
   }
}
