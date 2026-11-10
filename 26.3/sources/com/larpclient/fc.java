package com.larpclient;

import java.io.IOException;
import java.util.Locale;

public interface fc extends AutoCloseable {
   p6 read() throws IOException, InterruptedException;

   @Override
   default void close() {
   }

   static fc y() {
      String var0 = System.getProperty("os.name", "").toLowerCase(Locale.ROOT);
      if (var0.contains("linux")) {
         return new p_();
      } else {
         return var0.contains("windows") ? new o() : () -> {
            throw new IOException("Spotify lyrics support Windows and Linux desktop apps");
         };
      }
   }
}
