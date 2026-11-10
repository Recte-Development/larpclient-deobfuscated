package com.larpclient;

import com.larpclient.module.Module;
import com.larpclient.module.StreamProofModule;

public final class jj {
   private static volatile boolean k;
   private static volatile boolean y;

   private jj() {
   }

   public static boolean T(Module var0) {
      return y && StreamProofModule.m.contains(var0.getClass()) ? true : k && StreamProofModule.I.contains(var0.getClass());
   }

   public static boolean I() {
      return y;
   }

   static void E(boolean var0) {
      y = var0;
   }

   static boolean n() {
      return k;
   }

   static void x(boolean var0) {
      k = var0;
   }
}
