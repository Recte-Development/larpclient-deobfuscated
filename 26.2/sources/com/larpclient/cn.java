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

public final class cn implements AutoCloseable {
   private final zh V;
   private final bv Y;
   private final ScheduledExecutorService H;
   private final LinkedHashMap<String, cn.o> l = new LinkedHashMap<>(16, 0.75F, true);
   private volatile cn.j A = new cn.j(null, null, "Looking for Spotify desktop…");
   private volatile boolean c;
   private volatile CompletableFuture<sd> R;
   private String s;
   private long D;
   private static cn d;
   private static final Set<Object> T = Collections.newSetFromMap(new IdentityHashMap<>());

   public cn() {
      this(zh.X(), new bv(), 750L);
   }

   cn(zh var1, bv var2, long var3) {
      this.V = var1;
      this.Y = var2;
      this.H = Executors.newSingleThreadScheduledExecutor(var0 -> {
         Thread var1x = new Thread(var0, "Larp Spotify lyrics");
         var1x.setDaemon(true);
         return var1x;
      });
      this.H.scheduleWithFixedDelay(this::Z, 0L, var3, TimeUnit.MILLISECONDS);
   }

   public cn.j g() {
      return this.A;
   }

   public static synchronized cn L(Object var0) {
      if (d == null) {
         d = new cn();
      }

      T.add(var0);
      return d;
   }

   public static synchronized void D(Object var0) {
      if (T.remove(var0) && T.isEmpty() && d != null) {
         d.close();
         d = null;
      }
   }

   private void Z() {
      if (!this.c) {
         try {
            long var1 = System.nanoTime();
            if (this.R != null && this.R.isDone()) {
               sd var3 = this.R.join();
               long var4 = Math.min(var3.retryAfterSeconds(), 31536000L);
               long var6 = TimeUnit.SECONDS.toNanos(var4);
               long var8 = var4 > 0L ? ThreadLocalRandom.current().nextLong(var6 / 4L + 1L) : 0L;
               long var10 = var4 > 0L ? var1 + var6 + var8 : Long.MAX_VALUE;
               this.l.put(this.s, new cn.o(var3, var10));
               if (this.l.size() > 16) {
                  this.l.remove(this.l.keySet().iterator().next());
               }

               if (var3.status().startsWith("Lyrics lookup rate limited") || var3.status().startsWith("Lyrics provider busy; retrying shortly")) {
                  this.D = var10;
               }

               this.R = null;
            }

            mt var14 = this.V.read();
            if (this.c) {
               return;
            }

            if (var14 == null) {
               this.W(new cn.j(null, null, "Open Spotify desktop and play a song"));
               return;
            }

            String var15 = var14.key();
            cn.o var5 = this.l.get(var15);
            if (var5 != null && var5.expires != Long.MAX_VALUE && var1 >= var5.expires) {
               this.l.remove(var15);
               var5 = null;
            }

            if (var5 == null && this.R == null && var1 >= this.D) {
               this.s = var15;
               this.R = this.Y.S(var14);
               this.D = var1 + TimeUnit.SECONDS.toNanos(1L);
            }

            sd var16 = var5 == null ? null : var5.value;
            String var7 = var5 == null ? "Looking up lyrics…" : var16.status();
            if (!var14.playing()) {
               var7 = "Spotify paused: " + var7;
            }

            this.W(new cn.j(var14, var16, var7));
         } catch (InterruptedException var12) {
            Thread.currentThread().interrupt();
         } catch (Exception var13) {
            if (!this.c) {
               this.W(new cn.j(null, null, var13 instanceof IOException ? var13.getMessage() : "Spotify lyrics unavailable"));
            }
         }
      }
   }

   private void W(cn.j var1) {
      if (!this.c) {
         if (!this.A.status.equals(var1.status)) {
            h.A("WorldLyrics", "{}", var1.status);
         }

         this.A = var1;
      }
   }

   @Override
   public void close() {
      this.c = (boolean)1;
      this.H.shutdownNow();
      CompletableFuture var1 = this.R;
      if (var1 != null) {
         var1.cancel(true);
      }

      this.V.close();
      this.Y.close();
      this.A = new cn.j(null, null, "Spotify lyrics stopped");
   }

   public record j(mt playback, sd lyrics, String status) {
   }

   private record o(sd value, long expires) {
   }
}
