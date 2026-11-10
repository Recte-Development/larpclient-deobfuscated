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

final class zc implements AutoCloseable {
   private final zc.g f;
   private final Map<Path, String> M = new LinkedHashMap<>();
   private final ScheduledThreadPoolExecutor r;
   private ScheduledFuture<?> S;
   private volatile String R = "";
   private boolean A;

   zc(zc.g var1) {
      this.f = var1;
      this.r = new ScheduledThreadPoolExecutor(1, var0 -> {
         Thread var1x = new Thread(var0, "Larp preferences");
         var1x.setDaemon(true);
         return var1x;
      });
      this.r.setRemoveOnCancelPolicy(true);
      this.r.setExecuteExistingDelayedTasksAfterShutdownPolicy(false);
   }

   synchronized void q(Path var1, String var2) {
      if (this.A) {
         try {
            this.f.L(var1, var2);
            h.A("Config", "Save requested after shutdown began; wrote {} directly", var1.getFileName());
         } catch (IOException | RuntimeException var4) {
            this.R = "Save failed while closing; see the Config log.";
            h.m("Config", "Save after shutdown failed for " + var1.getFileName(), var4);
         }
      } else {
         this.M.put(var1, var2);
         if (this.S != null) {
            this.S.cancel(false);
         }

         this.S = this.r.schedule(this::C, 250L, TimeUnit.MILLISECONDS);
      }
   }

   String x() {
      return this.R;
   }

   private void C() {
      LinkedHashMap var1;
      synchronized (this) {
         var1 = new LinkedHashMap<>(this.M);
         this.M.clear();
      }

      for (Entry var3 : var1.entrySet()) {
         try {
            this.f.L((Path)var3.getKey(), (String)var3.getValue());
         } catch (IOException | RuntimeException var10) {
            synchronized (this) {
               this.M.putIfAbsent((Path)var3.getKey(), (String)var3.getValue());
            }

            if (this.R.isEmpty()) {
               h.m("Config", "Deferred save failed; snapshot retained for retry", var10);
            }

            this.R = "Save failed. Changes are retained for retry; see the Config log.";
         }
      }

      synchronized (this) {
         if (this.M.isEmpty()) {
            this.R = "";
         }
      }
   }

   void p(Path var1, String var2) throws IOException {
      synchronized (this) {
         if (this.A) {
            this.M.remove(var1);
            this.f.L(var1, var2);
            return;
         }
      }

      h(this.r.submit(() -> {
         synchronized (this) {
            this.M.remove(var1);
         }

         this.f.L(var1, var2);
         return null;
      }));
      synchronized (this) {
         if (this.M.isEmpty()) {
            this.R = "";
         }
      }
   }

   void g() throws IOException {
      boolean var1;
      synchronized (this) {
         var1 = this.A;
      }

      if (var1) {
         this.C();
      } else {
         h(this.r.submit(this::C));
      }

      synchronized (this) {
         if (!this.M.isEmpty()) {
            throw new IOException(this.R);
         }
      }
   }

   private static void h(Future<?> var0) throws IOException {
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
         this.g();
      } finally {
         synchronized (this) {
            this.A = (boolean)1;
         }

         this.r.shutdown();
      }
   }

   @FunctionalInterface
   interface g {
      void L(Path var1, String var2) throws IOException;
   }
}
