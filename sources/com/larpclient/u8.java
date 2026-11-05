package com.larpclient;

import com.larpclient.module.Module;
import com.larpclient.module.StreamProofModule;

public final class u8 {
   private static volatile boolean l;
   private static volatile boolean U;

   private u8() {
   }

   public static boolean G(Module var0) {
      return U && StreamProofModule.B.contains(var0.getClass()) ? true : l && StreamProofModule.y.contains(var0.getClass());
   }

   public static boolean a() {
      return U;
   }

   static void D(boolean var0) {
      U = var0;
   }

   static boolean B() {
      return l;
   }

   static void y(boolean var0) {
      l = var0;
   }
}
