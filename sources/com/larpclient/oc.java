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

public final class oc implements AutoCloseable {
   private final pg u;
   private final jh x;
   private final ScheduledExecutorService R;
   private final LinkedHashMap<String, oc.o> K = new LinkedHashMap<>(16, 0.75F, true);
   private volatile oc.k X = new oc.k(null, null, "Looking for Spotify desktop…");
   private volatile boolean g;
   private volatile CompletableFuture<h2> n;
   private String q;
   private long i;
   private static oc C;
   private static final Set<Object> Q = Collections.newSetFromMap(new IdentityHashMap<>());

   public oc() {
      this(pg.H(), new jh(), 750L);
   }

   oc(pg var1, jh var2, long var3) {
      this.u = var1;
      this.x = var2;
      this.R = Executors.newSingleThreadScheduledExecutor(var0 -> {
         Thread var1x = new Thread(var0, "Larp Spotify lyrics");
         var1x.setDaemon(true);
         return var1x;
      });
      this.R.scheduleWithFixedDelay(this::u, 0L, var3, TimeUnit.MILLISECONDS);
   }

   public oc.k z() {
      return this.X;
   }

   public static synchronized oc I(Object var0) {
      if (C == null) {
         C = new oc();
      }

      Q.add(var0);
      return C;
   }

   public static synchronized void m(Object var0) {
      if (Q.remove(var0) && Q.isEmpty() && C != null) {
         C.close();
         C = null;
      }
   }

   private void u() {
      if (!this.g) {
         try {
            long var1 = System.nanoTime();
            if (this.n != null && this.n.isDone()) {
               h2 var3 = this.n.join();
               long var4 = Math.min(var3.retryAfterSeconds(), 31536000L);
               long var6 = TimeUnit.SECONDS.toNanos(var4);
               long var8 = var4 > 0L ? ThreadLocalRandom.current().nextLong(var6 / 4L + 1L) : 0L;
               long var10 = var4 > 0L ? var1 + var6 + var8 : Long.MAX_VALUE;
               this.K.put(this.q, new oc.o(var3, var10));
               if (this.K.size() > 16) {
                  this.K.remove(this.K.keySet().iterator().next());
               }

               if (var3.status().startsWith("Lyrics lookup rate limited") || var3.status().startsWith("Lyrics provider busy; retrying shortly")) {
                  this.i = var10;
               }

               this.n = null;
            }

            _k var14 = this.u.read();
            if (this.g) {
               return;
            }

            if (var14 == null) {
               this.j(new oc.k(null, null, "Open Spotify desktop and play a song"));
               return;
            }

            String var15 = var14.key();
            oc.o var5 = this.K.get(var15);
            if (var5 != null && var5.expires != Long.MAX_VALUE && var1 >= var5.expires) {
               this.K.remove(var15);
               var5 = null;
            }

            if (var5 == null && this.n == null && var1 >= this.i) {
               this.q = var15;
               this.n = this.x.O(var14);
               this.i = var1 + TimeUnit.SECONDS.toNanos(1L);
            }

            h2 var16 = var5 == null ? null : var5.value;
            String var7 = var5 == null ? "Looking up lyrics…" : var16.status();
            if (!var14.playing()) {
               var7 = "Spotify paused: " + var7;
            }

            this.j(new oc.k(var14, var16, var7));
         } catch (InterruptedException var12) {
            Thread.currentThread().interrupt();
         } catch (Exception var13) {
            if (!this.g) {
               this.j(new oc.k(null, null, var13 instanceof IOException ? var13.getMessage() : "Spotify lyrics unavailable"));
            }
         }
      }
   }

   private void j(oc.k var1) {
      if (!this.g) {
         if (!this.X.status.equals(var1.status)) {
            r4.V("WorldLyrics", "{}", var1.status);
         }

         this.X = var1;
      }
   }

   @Override
   public void close() {
      this.g = (boolean)1;
      this.R.shutdownNow();
      CompletableFuture var1 = this.n;
      if (var1 != null) {
         var1.cancel(true);
      }

      this.u.close();
      this.x.close();
      this.X = new oc.k(null, null, "Spotify lyrics stopped");
   }

   public record k(_k playback, h2 lyrics, String status) {
   }

   private record o(h2 value, long expires) {
   }
}
