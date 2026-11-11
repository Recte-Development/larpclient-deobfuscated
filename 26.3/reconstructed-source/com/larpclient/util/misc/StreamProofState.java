package com.larpclient.util.misc;

import com.larpclient.module.misc.Module;
import com.larpclient.module.misc.StreamProofModule;

public final class StreamProofState {
   private static volatile boolean hiddenGlobally;
   private static volatile boolean active;

   private StreamProofState() {
   }

   public static boolean isHidden(Module var0) {
      return active && StreamProofModule.viewModelModules.contains(var0.getClass())
         ? true
         : hiddenGlobally && StreamProofModule.sceneModules.contains(var0.getClass());
   }

   public static boolean isActive() {
      return active;
   }

   static void setActive(boolean var0) {
      active = var0;
   }

   static boolean isHiddenGlobally() {
      return hiddenGlobally;
   }

   static void setHiddenGlobally(boolean var0) {
      hiddenGlobally = var0;
   }
}
