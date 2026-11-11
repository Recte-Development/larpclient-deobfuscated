package com.larpclient.util.misc;

import com.larpclient.hud.SpotifyLyricsSource;
import java.io.IOException;
import java.io.StringReader;
import java.lang.ProcessBuilder.Redirect;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.StringJoiner;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

public final class SpotifyMprisReader implements SpotifyLyricsSource {
   private static final String mPRIS_PREFIX = "org.mpris.MediaPlayer2.";
   private static final String dBUS_NAME = "org.freedesktop.DBus";
   private static final long tIMEOUT_NANOS = 2000000000L;
   private static final String pLAYERCTL_FORMAT = "<track><id>{{markup_escape(mpris:trackid)}}</id><title>{{markup_escape(title)}}</title><artist>{{markup_escape(artist)}}</artist><album>{{markup_escape(album)}}</album><duration>{{mpris:length}}</duration><position>{{position}}</position><status>{{status}}</status></track>";
   private volatile DBusConnection connection;
   private volatile Process playerctlProcess;
   private boolean closed;
   private boolean readerReadyLogged;
   private boolean fallbackLogged;
   private boolean flatpakHintLogged;

   @Override
   public MediaSession read() throws IOException, InterruptedException {
      if (this.isClosed()) {
         return null;
      }

      long var1 = System.nanoTime() + 2000000000L;
      DBusConnection var3 = this.connection;
      if (var3 == null) {
         try {
            var3 = DBusConnection.connectSession(var1);
         } catch (IOException var8) {
            if (!this.fallbackLogged) {
               LarpLog.warn("WorldLyrics", "Linux session bus unavailable ({}); trying playerctl", var8.getMessage());
               this.fallbackLogged = (boolean)1;
            }

            return this.readViaPlayerctl(var8);
         }

         synchronized (this) {
            if (this.closed) {
               var3.close();
               return null;
            }

            this.connection = var3;
         }

         if (!this.readerReadyLogged) {
            LarpLog.info("WorldLyrics", "Direct Linux MPRIS reader ready on the D-Bus session bus");
            this.readerReadyLogged = (boolean)1;
         }

         this.fallbackLogged = (boolean)0;
      }

      try {
         MediaSession var4 = queryPlayers(var3, var1);
         if (var4 == null && !this.flatpakHintLogged && Files.exists(Path.of("/.flatpak-info"))) {
            LarpLog.info(
               "WorldLyrics", "Flatpak launcher detected; if Spotify is playing, allow org.mpris.MediaPlayer2.spotify with flatpak override --talk-name"
            );
            this.flatpakHintLogged = (boolean)1;
         }

         return var4;
      } catch (IOException | InterruptedException var6) {
         this.dropConnection(var3);
         if (!(var6 instanceof InterruptedException) && !this.isClosed()) {
            throw new IOException("Spotify media session query failed; retrying", var6);
         } else {
            throw var6;
         }
      } catch (RuntimeException var7) {
         this.dropConnection(var3);
         if (this.isClosed()) {
            return null;
         } else {
            throw new IOException("Spotify returned invalid media metadata", var7);
         }
      }
   }

   static MediaSession queryPlayers(DBusConnection var0, long var1) throws IOException, InterruptedException {
      List var3 = var0.callMethod("org.freedesktop.DBus", "/org/freedesktop/DBus", "org.freedesktop.DBus", "ListNames", var1);
      if (var3 != null && var3.size() == 1 && var3.getFirst() instanceof List var4) {
         MediaSession var11 = null;

         for (Object var7 : var4.stream().filter(var0x -> var0x instanceof String var1x && isSpotifyPlayer(var1x)).sorted().toList()) {
            List var8 = var0.callMethod(
               (String)var7, "/org/mpris/MediaPlayer2", "org.freedesktop.DBus.Properties", "GetAll", var1, "org.mpris.MediaPlayer2.Player"
            );
            if (var8 != null && var8.size() == 1 && var8.getFirst() instanceof Map var9) {
               MediaSession var12 = parseProperties(var9, System.nanoTime());
               if (var12 != null && (var11 == null || var12.playing() && !var11.playing())) {
                  var11 = var12;
               }

               if (var11 != null && var11.playing()) {
                  break;
               }
            }
         }

         return var11;
      } else {
         throw new IOException("D-Bus session returned invalid player names");
      }
   }

   static boolean isSpotifyPlayer(String var0) {
      return var0.startsWith("org.mpris.MediaPlayer2.") && var0.substring("org.mpris.MediaPlayer2.".length()).toLowerCase(Locale.ROOT).contains("spotify");
   }

   static MediaSession parseProperties(Map<?, ?> var0, long var1) throws IOException {
      if (var0.get("Metadata") instanceof Map var3) {
         String var12 = readString(var3.get("xesam:title"));
         String var5 = readString(var3.get("xesam:artist"));
         if (!var12.isBlank() && !var5.isBlank()) {
            String var6 = readString(var3.get("xesam:album"));
            String var7 = readString(var3.get("mpris:trackid"));
            if (var12.length() <= 512 && var5.length() <= 512 && var6.length() <= 512 && var7.length() <= 1024) {
               double var8 = Math.max(0.0, readSeconds(var3.get("mpris:length")));
               double var10 = Math.max(0.0, readSeconds(var0.get("Position")));
               if (var8 > 0.0) {
                  var10 = Math.min(var8, var10);
               }

               return new MediaSession(var7, var12, var5, var6, var8, var10, "Playing".equals(var0.get("PlaybackStatus")), var1);
            } else {
               throw new IOException("Oversized Spotify media metadata");
            }
         } else {
            return null;
         }
      } else {
         return null;
      }
   }

   private static String readString(Object var0) {
      if (var0 instanceof String var6) {
         return var6.strip();
      } else if (var0 instanceof List var1) {
         StringJoiner var2 = new StringJoiner(", ");

         for (Object var4 : var1) {
            if (var4 instanceof String var5 && !var5.isBlank()) {
               var2.add(var5.strip());
            }
         }

         return var2.toString();
      } else {
         return "";
      }
   }

   private static double readSeconds(Object var0) {
      if (var0 instanceof Double var2) {
         return Double.isFinite(var2) ? var2 / 1000000.0 : 0.0;
      } else {
         return var0 instanceof Long var1 ? var1.longValue() / 1000000.0 : 0.0;
      }
   }

   private MediaSession readViaPlayerctl(IOException var1) throws IOException, InterruptedException {
      Process var2;
      try {
         synchronized (this) {
            if (this.closed) {
               return null;
            }

            var2 = new ProcessBuilder(
                  "playerctl",
                  "--player=spotify",
                  "metadata",
                  "--format",
                  "<track><id>{{markup_escape(mpris:trackid)}}</id><title>{{markup_escape(title)}}</title><artist>{{markup_escape(artist)}}</artist><album>{{markup_escape(album)}}</album><duration>{{mpris:length}}</duration><position>{{position}}</position><status>{{status}}</status></track>"
               )
               .redirectError(Redirect.DISCARD)
               .start();
            this.playerctlProcess = var2;
         }
      } catch (IOException var14) {
         IOException var4 = new IOException("Spotify needs the desktop D-Bus session or playerctl (" + var1.getMessage() + ")", var14);
         var4.addSuppressed(var1);
         throw var4;
      }

      try {
         CompletableFuture var3 = CompletableFuture.supplyAsync(() -> {
            try {
               return var2.getInputStream().readNBytes(16385);
            } catch (IOException var2x) {
               return new byte[0];
            }
         });
         if (!var2.waitFor(2L, TimeUnit.SECONDS)) {
            throw new IOException("Spotify media query timed out");
         } else if (var2.exitValue() != 0) {
            return null;
         } else {
            byte[] var15 = (byte[])var3.get(1L, TimeUnit.SECONDS);
            if (var15.length > 16384) {
               throw new IOException("Spotify metadata is too large");
            } else {
               return parsePlayerctlOutput(new String(var15, StandardCharsets.UTF_8), System.nanoTime());
            }
         }
      } catch (ExecutionException | TimeoutException var11) {
         throw new IOException("Spotify media query failed", var11);
      } finally {
         var2.destroyForcibly();
         this.playerctlProcess = null;
      }
   }

   public static MediaSession parsePlayerctlOutput(String var0, long var1) throws IOException {
      try {
         DocumentBuilderFactory var3 = DocumentBuilderFactory.newInstance();
         var3.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
         var3.setFeature("http://xml.org/sax/features/external-general-entities", false);
         var3.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
         Element var4 = var3.newDocumentBuilder().parse(new InputSource(new StringReader(var0))).getDocumentElement();
         String var5 = childText(var4, "title");
         String var6 = childText(var4, "artist");
         return !var5.isBlank() && !var6.isBlank()
            ? new MediaSession(
               childText(var4, "id"),
               var5,
               var6,
               childText(var4, "album"),
               childNumber(var4, "duration") / 1000000.0,
               childNumber(var4, "position") / 1000000.0,
               childText(var4, "status").equals("Playing"),
               var1
            )
            : null;
      } catch (Exception var7) {
         throw new IOException("Spotify returned invalid media metadata", var7);
      }
   }

   private static String childText(Element var0, String var1) {
      NodeList var2 = var0.getElementsByTagName(var1);
      return var2.getLength() == 0 ? "" : var2.item(0).getTextContent().strip();
   }

   private static double childNumber(Element var0, String var1) {
      String var2 = childText(var0, var1);
      return var2.isEmpty() ? 0.0 : Double.parseDouble(var2);
   }

   private synchronized boolean isClosed() {
      return this.closed;
   }

   private synchronized void dropConnection(DBusConnection var1) {
      if (this.connection == var1) {
         this.connection = null;
      }

      var1.close();
   }

   @Override
   public void close() {
      DBusConnection var1;
      Process var2;
      synchronized (this) {
         this.closed = (boolean)1;
         var1 = this.connection;
         this.connection = null;
         var2 = this.playerctlProcess;
      }

      if (var1 != null) {
         var1.close();
      }

      if (var2 != null) {
         var2.destroyForcibly();
      }
   }
}
