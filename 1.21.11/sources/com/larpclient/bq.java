package com.larpclient;

import java.io.IOException;
import java.util.Locale;

public interface bq extends AutoCloseable {
   yl read() throws IOException, InterruptedException;

   @Override
   default void close() {
   }

   static bq h() {
      String var0 = System.getProperty("os.name", "").toLowerCase(Locale.ROOT);
      if (var0.contains("linux")) {
         return new lf();
      } else {
         return var0.contains("windows") ? new jt() : () -> {
            throw new IOException("Spotify lyrics support Windows and Linux desktop apps");
         };
      }
   }
}
