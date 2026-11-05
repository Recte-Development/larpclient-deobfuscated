package com.larpclient;

import java.io.IOException;
import java.util.Locale;

public interface pg extends AutoCloseable {
   _k read() throws IOException, InterruptedException;

   @Override
   default void close() {
   }

   static pg H() {
      String var0 = System.getProperty("os.name", "").toLowerCase(Locale.ROOT);
      if (var0.contains("linux")) {
         return new xw();
      } else {
         return var0.contains("windows") ? new r1() : () -> {
            throw new IOException("Spotify lyrics support Windows and Linux desktop apps");
         };
      }
   }
}
