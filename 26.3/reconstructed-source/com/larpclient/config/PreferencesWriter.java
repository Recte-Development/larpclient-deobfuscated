package com.larpclient.config;

import com.larpclient.util.misc.LarpLog;
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

final class PreferencesWriter implements AutoCloseable {
   private final PreferencesWriter.FileSink sink;
   private final Map<Path, String> pending = new LinkedHashMap<>();
   private final ScheduledThreadPoolExecutor executor;
   private ScheduledFuture<?> scheduledSave;
   private volatile String status = "";
   private boolean closed;

   PreferencesWriter(PreferencesWriter.FileSink var1) {
      this.sink = var1;
      this.executor = new ScheduledThreadPoolExecutor(1, var0 -> {
         Thread var1x = new Thread(var0, "Larp preferences");
         var1x.setDaemon(true);
         return var1x;
      });
      this.executor.setRemoveOnCancelPolicy(true);
      this.executor.setExecuteExistingDelayedTasksAfterShutdownPolicy(false);
   }

   synchronized void scheduleSave(Path var1, String var2) {
      if (this.closed) {
         try {
            this.sink.write(var1, var2);
            LarpLog.info("Config", "Save requested after shutdown began; wrote {} directly", var1.getFileName());
         } catch (IOException | RuntimeException var4) {
            this.status = "Save failed while closing; see the Config log.";
            LarpLog.error("Config", "Save after shutdown failed for " + var1.getFileName(), var4);
         }
      } else {
         this.pending.put(var1, var2);
         if (this.scheduledSave != null) {
            this.scheduledSave.cancel(false);
         }

         this.scheduledSave = this.executor.schedule(this::scheduleSave, 250L, TimeUnit.MILLISECONDS);
      }
   }

   String getStatus() {
      return this.status;
   }

   private void scheduleSave() {
      LinkedHashMap var1;
      synchronized (this) {
         var1 = new LinkedHashMap<>(this.pending);
         this.pending.clear();
      }

      for (Entry var3 : var1.entrySet()) {
         try {
            this.sink.write((Path)var3.getKey(), (String)var3.getValue());
         } catch (IOException | RuntimeException var10) {
            synchronized (this) {
               this.pending.putIfAbsent((Path)var3.getKey(), (String)var3.getValue());
            }

            if (this.status.isEmpty()) {
               LarpLog.error("Config", "Deferred save failed; snapshot retained for retry", var10);
            }

            this.status = "Save failed. Changes are retained for retry; see the Config log.";
         }
      }

      synchronized (this) {
         if (this.pending.isEmpty()) {
            this.status = "";
         }
      }
   }

   void saveNow(Path var1, String var2) throws IOException {
      synchronized (this) {
         if (this.closed) {
            this.pending.remove(var1);
            this.sink.write(var1, var2);
            return;
         }
      }

      awaitFuture(this.executor.submit(() -> {
         synchronized (this) {
            this.pending.remove(var1);
         }

         this.sink.write(var1, var2);
         return null;
      }));
      synchronized (this) {
         if (this.pending.isEmpty()) {
            this.status = "";
         }
      }
   }

   void flush() throws IOException {
      boolean var1;
      synchronized (this) {
         var1 = this.closed;
      }

      if (var1) {
         this.scheduleSave();
      } else {
         awaitFuture(this.executor.submit(this::scheduleSave));
      }

      synchronized (this) {
         if (!this.pending.isEmpty()) {
            throw new IOException(this.status);
         }
      }
   }

   private static void awaitFuture(Future<?> var0) throws IOException {
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
         this.flush();
      } finally {
         synchronized (this) {
            this.closed = (boolean)1;
         }

         this.executor.shutdown();
      }
   }

   @FunctionalInterface
   interface FileSink {
      void write(Path var1, String var2) throws IOException;
   }
}
