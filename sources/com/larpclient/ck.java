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

final class ck implements AutoCloseable {
   private final ck.s R;
   private final Map<Path, String> G = new LinkedHashMap<>();
   private final ScheduledThreadPoolExecutor g;
   private ScheduledFuture<?> h;
   private volatile String x = "";
   private boolean d;

   ck(ck.s var1) {
      this.R = var1;
      this.g = new ScheduledThreadPoolExecutor(1, var0 -> {
         Thread var1x = new Thread(var0, "Larp preferences");
         var1x.setDaemon(true);
         return var1x;
      });
      this.g.setRemoveOnCancelPolicy(true);
      this.g.setExecuteExistingDelayedTasksAfterShutdownPolicy(false);
   }

   synchronized void v(Path var1, String var2) {
      if (this.d) {
         try {
            this.R.v(var1, var2);
            mt.K("Config", "Save requested after shutdown began; wrote {} directly", var1.getFileName());
         } catch (IOException | RuntimeException var4) {
            this.x = "Save failed while closing; see the Config log.";
            mt.P("Config", "Save after shutdown failed for " + var1.getFileName(), var4);
         }
      } else {
         this.G.put(var1, var2);
         if (this.h != null) {
            this.h.cancel(false);
         }

         this.h = this.g.schedule(this::M, 250L, TimeUnit.MILLISECONDS);
      }
   }

   String K() {
      return this.x;
   }

   private void M() {
      LinkedHashMap var1;
      synchronized (this) {
         var1 = new LinkedHashMap<>(this.G);
         this.G.clear();
      }

      for (Entry var3 : var1.entrySet()) {
         try {
            this.R.v((Path)var3.getKey(), (String)var3.getValue());
         } catch (IOException | RuntimeException var10) {
            synchronized (this) {
               this.G.putIfAbsent((Path)var3.getKey(), (String)var3.getValue());
            }

            if (this.x.isEmpty()) {
               mt.P("Config", "Deferred save failed; snapshot retained for retry", var10);
            }

            this.x = "Save failed. Changes are retained for retry; see the Config log.";
         }
      }

      synchronized (this) {
         if (this.G.isEmpty()) {
            this.x = "";
         }
      }
   }

   void W(Path var1, String var2) throws IOException {
      synchronized (this) {
         if (this.d) {
            this.G.remove(var1);
            this.R.v(var1, var2);
            return;
         }
      }

      l(this.g.submit(() -> {
         synchronized (this) {
            this.G.remove(var1);
         }

         this.R.v(var1, var2);
         return null;
      }));
      synchronized (this) {
         if (this.G.isEmpty()) {
            this.x = "";
         }
      }
   }

   void W() throws IOException {
      boolean var1;
      synchronized (this) {
         var1 = this.d;
      }

      if (var1) {
         this.M();
      } else {
         l(this.g.submit(this::M));
      }

      synchronized (this) {
         if (!this.G.isEmpty()) {
            throw new IOException(this.x);
         }
      }
   }

   private static void l(Future<?> var0) throws IOException {
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
         this.W();
      } finally {
         synchronized (this) {
            this.d = (boolean)1;
         }

         this.g.shutdown();
      }
   }

   @FunctionalInterface
   interface s {
      void v(Path var1, String var2) throws IOException;
   }
}
