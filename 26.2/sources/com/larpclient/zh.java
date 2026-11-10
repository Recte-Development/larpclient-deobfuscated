package com.larpclient;

import java.io.IOException;
import java.util.Locale;

public interface zh extends AutoCloseable {
   mt read() throws IOException, InterruptedException;

   @Override
   default void close() {
   }

   static zh X() {
      String var0 = System.getProperty("os.name", "").toLowerCase(Locale.ROOT);
      if (var0.contains("linux")) {
         return new bk();
      } else {
         return var0.contains("windows") ? new r() : () -> {
            throw new IOException("Spotify lyrics support Windows and Linux desktop apps");
         };
      }
   }
}
