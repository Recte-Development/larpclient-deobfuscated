package com.larpclient.hud;

import com.larpclient.util.misc.MediaSession;
import com.larpclient.util.misc.SpotifyMprisReader;
import java.io.IOException;
import java.util.Locale;

public interface SpotifyLyricsSource extends AutoCloseable {
   MediaSession read() throws IOException, InterruptedException;

   @Override
   default void close() {
   }

   static SpotifyLyricsSource forCurrentOs() {
      String var0 = System.getProperty("os.name", "").toLowerCase(Locale.ROOT);
      if (var0.contains("linux")) {
         return new SpotifyMprisReader();
      } else {
         return var0.contains("windows") ? new WindowsSpotifyReader() : () -> {
            throw new IOException("Spotify lyrics support Windows and Linux desktop apps");
         };
      }
   }
}
