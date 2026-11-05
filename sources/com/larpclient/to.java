package com.larpclient;

import java.io.IOException;
import java.util.Locale;

public interface to extends AutoCloseable {
   ha read() throws IOException, InterruptedException;

   @Override
   default void close() {
   }

   static to K() {
      String var0 = System.getProperty("os.name", "").toLowerCase(Locale.ROOT);
      if (var0.contains("linux")) {
         return new wc();
      } else {
         return var0.contains("windows") ? new _1() : () -> {
            throw new IOException("Spotify lyrics support Windows and Linux desktop apps");
         };
      }
   }
}
