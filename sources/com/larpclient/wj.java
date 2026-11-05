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

public final class wj implements AutoCloseable {
   private final to p;
   private final er v;
   private final ScheduledExecutorService x;
   private final LinkedHashMap<String, wj.r> e = new LinkedHashMap<>(16, 0.75F, true);
   private volatile wj.h a = new wj.h(null, null, "Looking for Spotify desktop…");
   private volatile boolean n;
   private volatile CompletableFuture<com.larpclient.r> P;
   private String X;
   private long R;
   private static wj r;
   private static final Set<Object> i = Collections.newSetFromMap(new IdentityHashMap<>());

   public wj() {
      this(to.K(), new er(), 750L);
   }

   wj(to var1, er var2, long var3) {
      this.p = var1;
      this.v = var2;
      this.x = Executors.newSingleThreadScheduledExecutor(var0 -> {
         Thread var1x = new Thread(var0, "Larp Spotify lyrics");
         var1x.setDaemon(true);
         return var1x;
      });
      this.x.scheduleWithFixedDelay(this::l, 0L, var3, TimeUnit.MILLISECONDS);
   }

   public wj.h U() {
      return this.a;
   }

   public static synchronized wj O(Object var0) {
      if (r == null) {
         r = new wj();
      }

      i.add(var0);
      return r;
   }

   public static synchronized void U(Object var0) {
      if (i.remove(var0) && i.isEmpty() && r != null) {
         r.close();
         r = null;
      }
   }

   private void l() {
      if (!this.n) {
         try {
            long var1 = System.nanoTime();
            if (this.P != null && this.P.isDone()) {
               com.larpclient.r var3 = this.P.join();
               long var4 = Math.min(var3.retryAfterSeconds(), 31536000L);
               long var6 = TimeUnit.SECONDS.toNanos(var4);
               long var8 = var4 > 0L ? ThreadLocalRandom.current().nextLong(var6 / 4L + 1L) : 0L;
               long var10 = var4 > 0L ? var1 + var6 + var8 : Long.MAX_VALUE;
               this.e.put(this.X, new wj.r(var3, var10));
               if (this.e.size() > 16) {
                  this.e.remove(this.e.keySet().iterator().next());
               }

               if (var3.status().startsWith("Lyrics lookup rate limited") || var3.status().startsWith("Lyrics provider busy; retrying shortly")) {
                  this.R = var10;
               }

               this.P = null;
            }

            ha var14 = this.p.read();
            if (this.n) {
               return;
            }

            if (var14 == null) {
               this.V(new wj.h(null, null, "Open Spotify desktop and play a song"));
               return;
            }

            String var15 = var14.key();
            wj.r var5 = this.e.get(var15);
            if (var5 != null && var5.expires != Long.MAX_VALUE && var1 >= var5.expires) {
               this.e.remove(var15);
               var5 = null;
            }

            if (var5 == null && this.P == null && var1 >= this.R) {
               this.X = var15;
               this.P = this.v.m(var14);
               this.R = var1 + TimeUnit.SECONDS.toNanos(1L);
            }

            com.larpclient.r var16 = var5 == null ? null : var5.value;
            String var7 = var5 == null ? "Looking up lyrics…" : var16.status();
            if (!var14.playing()) {
               var7 = "Spotify paused: " + var7;
            }

            this.V(new wj.h(var14, var16, var7));
         } catch (InterruptedException var12) {
            Thread.currentThread().interrupt();
         } catch (Exception var13) {
            if (!this.n) {
               this.V(new wj.h(null, null, var13 instanceof IOException ? var13.getMessage() : "Spotify lyrics unavailable"));
            }
         }
      }
   }

   private void V(wj.h var1) {
      if (!this.n) {
         if (!this.a.status.equals(var1.status)) {
            mt.K("WorldLyrics", "{}", var1.status);
         }

         this.a = var1;
      }
   }

   @Override
   public void close() {
      this.n = (boolean)1;
      this.x.shutdownNow();
      CompletableFuture var1 = this.P;
      if (var1 != null) {
         var1.cancel(true);
      }

      this.p.close();
      this.v.close();
      this.a = new wj.h(null, null, "Spotify lyrics stopped");
   }

   public record h(ha playback, com.larpclient.r lyrics, String status) {
   }

   private record r(com.larpclient.r value, long expires) {
   }
}
