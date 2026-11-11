package com.larpclient.util.data;

import com.larpclient.util.misc.LarpLog;

public final class BaccaratLog {
   private BaccaratLog() {
   }

   public static void log(String var0) {
      if (var0 != null && !var0.isBlank()) {
         LarpLog.debug("Rig/Baccarat", "{}", var0.replace('\r', ' ').replace('\n', ' '));
      }
   }
}
