package com.larpclient;

import java.io.IOException;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

final class yj implements AutoCloseable {
   private final yj.k u;
   private final Map<Path, String> H = new LinkedHashMap<>();
   private final ScheduledThreadPoolExecutor B;
   private ScheduledFuture<?> Q;
   private volatile String P = "";
   private boolean I;

   yj(yj.k var1) {
      this.u = var1;
      this.B = new ScheduledThreadPoolExecutor(1, var0 -> {
         Thread var1x = new Thread(var0, "Larp preferences");
         var1x.setDaemon(true);
         return var1x;
      });
      this.B.setRemoveOnCancelPolicy(true);
      this.B.setExecuteExistingDelayedTasksAfterShutdownPolicy(false);
   }

   synchronized void K(Path var1, String var2) {
      if (this.I) {
         try {
            this.u.c(var1, var2);
            q8.G("Config", "Save requested after shutdown began; wrote {} directly", var1.getFileName());
         } catch (IOException | RuntimeException var4) {
            this.P = "Save failed while closing; see the Config log.";
            q8.u("Config", "Save after shutdown failed for " + var1.getFileName(), var4);
         }
      } else {
         this.H.put(var1, var2);
         if (this.Q != null) {
            this.Q.cancel(false);
         }

         this.Q = this.B.schedule(this::a, 250L, TimeUnit.MILLISECONDS);
      }
   }

   String P() {
      return this.P;
   }

   private void a() {
      LinkedHashMap var1;
      synchronized (this) {
         var1 = new LinkedHashMap<>(this.H);
         this.H.clear();
      }

      for (Entry var3 : var1.entrySet()) {
         try {
            this.u.c((Path)var3.getKey(), (String)var3.getValue());
         } catch (IOException | RuntimeException var10) {
            synchronized (this) {
               this.H.putIfAbsent((Path)var3.getKey(), (String)var3.getValue());
            }

            if (this.P.isEmpty()) {
               q8.u("Config", "Deferred save failed; snapshot retained for retry", var10);
            }

            this.P = "Save failed. Changes are retained for retry; see the Config log.";
         }
      }

      synchronized (this) {
         if (this.H.isEmpty()) {
            this.P = "";
         }
      }
   }

   void D(Path var1, String var2) throws IOException {
      synchronized (this) {
         if (this.I) {
            this.H.remove(var1);
            this.u.c(var1, var2);
            return;
         }
      }

      F(this.B.submit(() -> {
         synchronized (this) {
            this.H.remove(var1);
         }

         this.u.c(var1, var2);
         return null;
      }));
      synchronized (this) {
         if (this.H.isEmpty()) {
            this.P = "";
         }
      }
   }

   void I() throws IOException {
      boolean var1;
      synchronized (this) {
         var1 = this.I;
      }

      if (var1) {
         this.a();
      } else {
         F(this.B.submit(this::a));
      }

      synchronized (this) {
         if (!this.H.isEmpty()) {
            throw new IOException(this.P);
         }
      }
   }

   private static void F(Future<?> var0) throws IOException {
      try {
         var0.get();
      } catch (InterruptedException var2) {
         Thread.currentThread().interrupt();
         throw new IOException("Save interrupted", var2);
      } catch (ExecutionException var3) {
         throw new IOException("Save failed", var3.getCause());
      }
   }

   @Override
   public void close() throws IOException {
      try {
         this.I();
      } finally {
         synchronized (this) {
            this.I = (boolean)1;
         }

         this.B.shutdown();
      }
   }

   @FunctionalInterface
   interface k {
      void c(Path var1, String var2) throws IOException;
   }
}
