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

final class og implements AutoCloseable {
   private final og.b S;
   private final Map<Path, String> H = new LinkedHashMap<>();
   private final ScheduledThreadPoolExecutor W;
   private ScheduledFuture<?> s;
   private volatile String I = "";
   private boolean D;

   og(og.b var1) {
      this.S = var1;
      this.W = new ScheduledThreadPoolExecutor(1, var0 -> {
         Thread var1x = new Thread(var0, "Larp preferences");
         var1x.setDaemon(true);
         return var1x;
      });
      this.W.setRemoveOnCancelPolicy(true);
      this.W.setExecuteExistingDelayedTasksAfterShutdownPolicy(false);
   }

   synchronized void a(Path var1, String var2) {
      if (this.D) {
         try {
            this.S.Z(var1, var2);
            _q.z("Config", "Save requested after shutdown began; wrote {} directly", var1.getFileName());
         } catch (IOException | RuntimeException var4) {
            this.I = "Save failed while closing; see the Config log.";
            _q.o("Config", "Save after shutdown failed for " + var1.getFileName(), var4);
         }
      } else {
         this.H.put(var1, var2);
         if (this.s != null) {
            this.s.cancel(false);
         }

         this.s = this.W.schedule(this::a, 250L, TimeUnit.MILLISECONDS);
      }
   }

   String t() {
      return this.I;
   }

   private void a() {
      LinkedHashMap var1;
      synchronized (this) {
         var1 = new LinkedHashMap<>(this.H);
         this.H.clear();
      }

      for (Entry var3 : var1.entrySet()) {
         try {
            this.S.Z((Path)var3.getKey(), (String)var3.getValue());
         } catch (IOException | RuntimeException var10) {
            synchronized (this) {
               this.H.putIfAbsent((Path)var3.getKey(), (String)var3.getValue());
            }

            if (this.I.isEmpty()) {
               _q.o("Config", "Deferred save failed; snapshot retained for retry", var10);
            }

            this.I = "Save failed. Changes are retained for retry; see the Config log.";
         }
      }

      synchronized (this) {
         if (this.H.isEmpty()) {
            this.I = "";
         }
      }
   }

   void X(Path var1, String var2) throws IOException {
      synchronized (this) {
         if (this.D) {
            this.H.remove(var1);
            this.S.Z(var1, var2);
            return;
         }
      }

      x(this.W.submit(() -> {
         synchronized (this) {
            this.H.remove(var1);
         }

         this.S.Z(var1, var2);
         return null;
      }));
      synchronized (this) {
         if (this.H.isEmpty()) {
            this.I = "";
         }
      }
   }

   void E() throws IOException {
      boolean var1;
      synchronized (this) {
         var1 = this.D;
      }

      if (var1) {
         this.a();
      } else {
         x(this.W.submit(this::a));
      }

      synchronized (this) {
         if (!this.H.isEmpty()) {
            throw new IOException(this.I);
         }
      }
   }

   private static void x(Future<?> var0) throws IOException {
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
         this.E();
      } finally {
         synchronized (this) {
            this.D = (boolean)1;
         }

         this.W.shutdown();
      }
   }

   @FunctionalInterface
   interface b {
      void Z(Path var1, String var2) throws IOException;
   }
}
