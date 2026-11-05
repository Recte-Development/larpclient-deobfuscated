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

final class fn implements AutoCloseable {
   private final fn.m I;
   private final Map<Path, String> p = new LinkedHashMap<>();
   private final ScheduledThreadPoolExecutor d;
   private ScheduledFuture<?> U;
   private volatile String w = "";
   private boolean r;

   fn(fn.m var1) {
      this.I = var1;
      this.d = new ScheduledThreadPoolExecutor(1, var0 -> {
         Thread var1x = new Thread(var0, "Larp preferences");
         var1x.setDaemon(true);
         return var1x;
      });
      this.d.setRemoveOnCancelPolicy(true);
      this.d.setExecuteExistingDelayedTasksAfterShutdownPolicy(false);
   }

   synchronized void n(Path var1, String var2) {
      if (this.r) {
         try {
            this.I.R(var1, var2);
            r4.V("Config", "Save requested after shutdown began; wrote {} directly", var1.getFileName());
         } catch (IOException | RuntimeException var4) {
            this.w = "Save failed while closing; see the Config log.";
            r4.r("Config", "Save after shutdown failed for " + var1.getFileName(), var4);
         }
      } else {
         this.p.put(var1, var2);
         if (this.U != null) {
            this.U.cancel(false);
         }

         this.U = this.d.schedule(this::A, 250L, TimeUnit.MILLISECONDS);
      }
   }

   String X() {
      return this.w;
   }

   private void A() {
      LinkedHashMap var1;
      synchronized (this) {
         var1 = new LinkedHashMap<>(this.p);
         this.p.clear();
      }

      for (Entry var3 : var1.entrySet()) {
         try {
            this.I.R((Path)var3.getKey(), (String)var3.getValue());
         } catch (IOException | RuntimeException var10) {
            synchronized (this) {
               this.p.putIfAbsent((Path)var3.getKey(), (String)var3.getValue());
            }

            if (this.w.isEmpty()) {
               r4.r("Config", "Deferred save failed; snapshot retained for retry", var10);
            }

            this.w = "Save failed. Changes are retained for retry; see the Config log.";
         }
      }

      synchronized (this) {
         if (this.p.isEmpty()) {
            this.w = "";
         }
      }
   }

   void R(Path var1, String var2) throws IOException {
      synchronized (this) {
         if (this.r) {
            this.p.remove(var1);
            this.I.R(var1, var2);
            return;
         }
      }

      g(this.d.submit(() -> {
         synchronized (this) {
            this.p.remove(var1);
         }

         this.I.R(var1, var2);
         return null;
      }));
      synchronized (this) {
         if (this.p.isEmpty()) {
            this.w = "";
         }
      }
   }

   void w() throws IOException {
      boolean var1;
      synchronized (this) {
         var1 = this.r;
      }

      if (var1) {
         this.A();
      } else {
         g(this.d.submit(this::A));
      }

      synchronized (this) {
         if (!this.p.isEmpty()) {
            throw new IOException(this.w);
         }
      }
   }

   private static void g(Future<?> var0) throws IOException {
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
         this.w();
      } finally {
         synchronized (this) {
            this.r = (boolean)1;
         }

         this.d.shutdown();
      }
   }

   @FunctionalInterface
   interface m {
      void R(Path var1, String var2) throws IOException;
   }
}
