package com.larpclient;

import com.larpclient.module.Module;
import com.larpclient.module.StreamProofModule;

public final class o {
   private static volatile boolean A;
   private static volatile boolean R;

   private o() {
   }

   public static boolean W(Module var0) {
      return R && StreamProofModule.u.contains(var0.getClass()) ? true : A && StreamProofModule.F.contains(var0.getClass());
   }

   public static boolean K() {
      return R;
   }

   static void O(boolean var0) {
      R = var0;
   }

   static boolean z() {
      return A;
   }

   static void M(boolean var0) {
      A = var0;
   }
}
