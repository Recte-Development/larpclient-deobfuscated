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

public final class ov implements AutoCloseable {
   private final fc W;
   private final hf z;
   private final ScheduledExecutorService n;
   private final LinkedHashMap<String, ov.s> y = new LinkedHashMap<>(16, 0.75F, true);
   private volatile ov.i f = new ov.i(null, null, "Looking for Spotify desktop…");
   private volatile boolean E;
   private volatile CompletableFuture<jx> F;
   private String t;
   private long U;
   private static ov x;
   private static final Set<Object> Q = Collections.newSetFromMap(new IdentityHashMap<>());

   public ov() {
      this(fc.y(), new hf(), 750L);
   }

   ov(fc var1, hf var2, long var3) {
      this.W = var1;
      this.z = var2;
      this.n = Executors.newSingleThreadScheduledExecutor(var0 -> {
         Thread var1x = new Thread(var0, "Larp Spotify lyrics");
         var1x.setDaemon(true);
         return var1x;
      });
      this.n.scheduleWithFixedDelay(this::m, 0L, var3, TimeUnit.MILLISECONDS);
   }

   public ov.i X() {
      return this.f;
   }

   public static synchronized ov Y(Object var0) {
      if (x == null) {
         x = new ov();
      }

      Q.add(var0);
      return x;
   }

   public static synchronized void E(Object var0) {
      if (Q.remove(var0) && Q.isEmpty() && x != null) {
         x.close();
         x = null;
      }
   }

   private void m() {
      if (!this.E) {
         try {
            long var1 = System.nanoTime();
            if (this.F != null && this.F.isDone()) {
               jx var3 = this.F.join();
               long var4 = Math.min(var3.retryAfterSeconds(), 31536000L);
               long var6 = TimeUnit.SECONDS.toNanos(var4);
               long var8 = var4 > 0L ? ThreadLocalRandom.current().nextLong(var6 / 4L + 1L) : 0L;
               long var10 = var4 > 0L ? var1 + var6 + var8 : Long.MAX_VALUE;
               this.y.put(this.t, new ov.s(var3, var10));
               if (this.y.size() > 16) {
                  this.y.remove(this.y.keySet().iterator().next());
               }

               if (var3.status().startsWith("Lyrics lookup rate limited") || var3.status().startsWith("Lyrics provider busy; retrying shortly")) {
                  this.U = var10;
               }

               this.F = null;
            }

            p6 var14 = this.W.read();
            if (this.E) {
               return;
            }

            if (var14 == null) {
               this.m(new ov.i(null, null, "Open Spotify desktop and play a song"));
               return;
            }

            String var15 = var14.key();
            ov.s var5 = this.y.get(var15);
            if (var5 != null && var5.expires != Long.MAX_VALUE && var1 >= var5.expires) {
               this.y.remove(var15);
               var5 = null;
            }

            if (var5 == null && this.F == null && var1 >= this.U) {
               this.t = var15;
               this.F = this.z.l(var14);
               this.U = var1 + TimeUnit.SECONDS.toNanos(1L);
            }

            jx var16 = var5 == null ? null : var5.value;
            String var7 = var5 == null ? "Looking up lyrics…" : var16.status();
            if (!var14.playing()) {
               var7 = "Spotify paused: " + var7;
            }

            this.m(new ov.i(var14, var16, var7));
         } catch (InterruptedException var12) {
            Thread.currentThread().interrupt();
         } catch (Exception var13) {
            if (!this.E) {
               this.m(new ov.i(null, null, var13 instanceof IOException ? var13.getMessage() : "Spotify lyrics unavailable"));
            }
         }
      }
   }

   private void m(ov.i var1) {
      if (!this.E) {
         if (!this.f.status.equals(var1.status)) {
            _q.z("WorldLyrics", "{}", var1.status);
         }

         this.f = var1;
      }
   }

   @Override
   public void close() {
      this.E = (boolean)1;
      this.n.shutdownNow();
      CompletableFuture var1 = this.F;
      if (var1 != null) {
         var1.cancel(true);
      }

      this.W.close();
      this.z.close();
      this.f = new ov.i(null, null, "Spotify lyrics stopped");
   }

   public record i(p6 playback, jx lyrics, String status) {
   }

   private record s(jx value, long expires) {
   }
}
