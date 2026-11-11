package com.larpclient.util.misc;

import com.larpclient.hud.SpotifyLyricsSource;
import com.larpclient.network.LeaderboardResult;
import com.larpclient.network.LyricsClient;
import java.io.IOException;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.LinkedHashMap;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

public final class SpotifyLyricsService implements AutoCloseable {
   private final SpotifyLyricsSource playbackReader;
   private final LyricsClient lyricsClient;
   private final ScheduledExecutorService scheduler;
   private final LinkedHashMap<String, SpotifyLyricsService.CachedLyrics> lyricsCache = new LinkedHashMap<>(16, 0.75F, true);
   private volatile SpotifyLyricsService.PlaybackSnapshot snapshot = new SpotifyLyricsService.PlaybackSnapshot(null, null, "Looking for Spotify desktop…");
   private volatile boolean closed;
   private volatile CompletableFuture<LeaderboardResult> pendingLookup;
   private String pendingKey;
   private long nextLookupAt;
   private static SpotifyLyricsService instance;
   private static final Set<Object> users = Collections.newSetFromMap(new IdentityHashMap<>());

   public SpotifyLyricsService() {
      this(SpotifyLyricsSource.forCurrentOs(), new LyricsClient(), 750L);
   }

   SpotifyLyricsService(SpotifyLyricsSource var1, LyricsClient var2, long var3) {
      this.playbackReader = var1;
      this.lyricsClient = var2;
      this.scheduler = Executors.newSingleThreadScheduledExecutor(var0 -> {
         Thread var1x = new Thread(var0, "Larp Spotify lyrics");
         var1x.setDaemon(true);
         return var1x;
      });
      this.scheduler.scheduleWithFixedDelay(this::poll, 0L, var3, TimeUnit.MILLISECONDS);
   }

   public SpotifyLyricsService.PlaybackSnapshot snapshot() {
      return this.snapshot;
   }

   public static synchronized SpotifyLyricsService acquire(Object var0) {
      if (instance == null) {
         instance = new SpotifyLyricsService();
      }

      users.add(var0);
      return instance;
   }

   public static synchronized void release(Object var0) {
      if (users.remove(var0) && users.isEmpty() && instance != null) {
         instance.close();
         instance = null;
      }
   }

   private void poll() {
      if (!this.closed) {
         try {
            long var1 = System.nanoTime();
            if (this.pendingLookup != null && this.pendingLookup.isDone()) {
               LeaderboardResult var3 = this.pendingLookup.join();
               long var4 = Math.min(var3.retryAfterSeconds(), 31536000L);
               long var6 = TimeUnit.SECONDS.toNanos(var4);
               long var8 = var4 > 0L ? ThreadLocalRandom.current().nextLong(var6 / 4L + 1L) : 0L;
               long var10 = var4 > 0L ? var1 + var6 + var8 : Long.MAX_VALUE;
               this.lyricsCache.put(this.pendingKey, new SpotifyLyricsService.CachedLyrics(var3, var10));
               if (this.lyricsCache.size() > 16) {
                  this.lyricsCache.remove(this.lyricsCache.keySet().iterator().next());
               }

               if (var3.status().startsWith("Lyrics lookup rate limited") || var3.status().startsWith("Lyrics provider busy; retrying shortly")) {
                  this.nextLookupAt = var10;
               }

               this.pendingLookup = null;
            }

            MediaSession var14 = this.playbackReader.read();
            if (this.closed) {
               return;
            }

            if (var14 == null) {
               this.poll(new SpotifyLyricsService.PlaybackSnapshot(null, null, "Open Spotify desktop and play a song"));
               return;
            }

            String var15 = var14.key();
            SpotifyLyricsService.CachedLyrics var5 = this.lyricsCache.get(var15);
            if (var5 != null && var5.expires != Long.MAX_VALUE && var1 >= var5.expires) {
               this.lyricsCache.remove(var15);
               var5 = null;
            }

            if (var5 == null && this.pendingLookup == null && var1 >= this.nextLookupAt) {
               this.pendingKey = var15;
               this.pendingLookup = this.lyricsClient.fetchLyrics(var14);
               this.nextLookupAt = var1 + TimeUnit.SECONDS.toNanos(1L);
            }

            LeaderboardResult var16 = var5 == null ? null : var5.value;
            String var7 = var5 == null ? "Looking up lyrics…" : var16.status();
            if (!var14.playing()) {
               var7 = "Spotify paused: " + var7;
            }

            this.poll(new SpotifyLyricsService.PlaybackSnapshot(var14, var16, var7));
         } catch (InterruptedException var12) {
            Thread.currentThread().interrupt();
         } catch (Exception var13) {
            if (!this.closed) {
               this.poll(
                  new SpotifyLyricsService.PlaybackSnapshot(null, null, var13 instanceof IOException ? var13.getMessage() : "Spotify lyrics unavailable")
               );
            }
         }
      }
   }

   private void poll(SpotifyLyricsService.PlaybackSnapshot var1) {
      if (!this.closed) {
         if (!this.snapshot.status.equals(var1.status)) {
            LarpLog.info("WorldLyrics", "{}", var1.status);
         }

         this.snapshot = var1;
      }
   }

   @Override
   public void close() {
      this.closed = (boolean)1;
      this.scheduler.shutdownNow();
      CompletableFuture var1 = this.pendingLookup;
      if (var1 != null) {
         var1.cancel(true);
      }

      this.playbackReader.close();
      this.lyricsClient.close();
      this.snapshot = new SpotifyLyricsService.PlaybackSnapshot(null, null, "Spotify lyrics stopped");
   }

   private record CachedLyrics(LeaderboardResult value, long expires) {
   }

   public record PlaybackSnapshot(MediaSession playback, LeaderboardResult lyrics, String status) {
   }
}
