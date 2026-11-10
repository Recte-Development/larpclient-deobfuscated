package com.larpclient;

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

public final class h0 implements AutoCloseable {
   private final bq x;
   private final ev h;
   private final ScheduledExecutorService Y;
   private final LinkedHashMap<String, h0.g> t = new LinkedHashMap<>(16, 0.75F, true);
   private volatile h0.s s = new h0.s(null, null, "Looking for Spotify desktop…");
   private volatile boolean M;
   private volatile CompletableFuture<jy> k;
   private String C;
   private long G;
   private static h0 u;
   private static final Set<Object> D = Collections.newSetFromMap(new IdentityHashMap<>());

   public h0() {
      this(bq.h(), new ev(), 750L);
   }

   h0(bq var1, ev var2, long var3) {
      this.x = var1;
      this.h = var2;
      this.Y = Executors.newSingleThreadScheduledExecutor(var0 -> {
         Thread var1x = new Thread(var0, "Larp Spotify lyrics");
         var1x.setDaemon(true);
         return var1x;
      });
      this.Y.scheduleWithFixedDelay(this::a, 0L, var3, TimeUnit.MILLISECONDS);
   }

   public h0.s v() {
      return this.s;
   }

   public static synchronized h0 C(Object var0) {
      if (u == null) {
         u = new h0();
      }

      D.add(var0);
      return u;
   }

   public static synchronized void w(Object var0) {
      if (D.remove(var0) && D.isEmpty() && u != null) {
         u.close();
         u = null;
      }
   }

   private void a() {
      if (!this.M) {
         try {
            long var1 = System.nanoTime();
            if (this.k != null && this.k.isDone()) {
               jy var3 = this.k.join();
               long var4 = Math.min(var3.retryAfterSeconds(), 31536000L);
               long var6 = TimeUnit.SECONDS.toNanos(var4);
               long var8 = var4 > 0L ? ThreadLocalRandom.current().nextLong(var6 / 4L + 1L) : 0L;
               long var10 = var4 > 0L ? var1 + var6 + var8 : Long.MAX_VALUE;
               this.t.put(this.C, new h0.g(var3, var10));
               if (this.t.size() > 16) {
                  this.t.remove(this.t.keySet().iterator().next());
               }

               if (var3.status().startsWith("Lyrics lookup rate limited") || var3.status().startsWith("Lyrics provider busy; retrying shortly")) {
                  this.G = var10;
               }

               this.k = null;
            }

            yl var14 = this.x.read();
            if (this.M) {
               return;
            }

            if (var14 == null) {
               this.h(new h0.s(null, null, "Open Spotify desktop and play a song"));
               return;
            }

            String var15 = var14.key();
            h0.g var5 = this.t.get(var15);
            if (var5 != null && var5.expires != Long.MAX_VALUE && var1 >= var5.expires) {
               this.t.remove(var15);
               var5 = null;
            }

            if (var5 == null && this.k == null && var1 >= this.G) {
               this.C = var15;
               this.k = this.h.e(var14);
               this.G = var1 + TimeUnit.SECONDS.toNanos(1L);
            }

            jy var16 = var5 == null ? null : var5.value;
            String var7 = var5 == null ? "Looking up lyrics…" : var16.status();
            if (!var14.playing()) {
               var7 = "Spotify paused: " + var7;
            }

            this.h(new h0.s(var14, var16, var7));
         } catch (InterruptedException var12) {
            Thread.currentThread().interrupt();
         } catch (Exception var13) {
            if (!this.M) {
               this.h(new h0.s(null, null, var13 instanceof IOException ? var13.getMessage() : "Spotify lyrics unavailable"));
            }
         }
      }
   }

   private void h(h0.s var1) {
      if (!this.M) {
         if (!this.s.status.equals(var1.status)) {
            q8.G("WorldLyrics", "{}", var1.status);
         }

         this.s = var1;
      }
   }

   @Override
   public void close() {
      this.M = (boolean)1;
      this.Y.shutdownNow();
      CompletableFuture var1 = this.k;
      if (var1 != null) {
         var1.cancel(true);
      }

      this.x.close();
      this.h.close();
      this.s = new h0.s(null, null, "Spotify lyrics stopped");
   }

   private record g(jy value, long expires) {
   }

   public record s(yl playback, jy lyrics, String status) {
   }
}
