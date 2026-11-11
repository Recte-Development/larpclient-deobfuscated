package com.larpclient.util.data;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.BiConsumer;
import java.util.function.BooleanSupplier;

final class LatestFutureSlot<T> {
   private final Executor executor;
   private final BooleanSupplier isValid;
   private CompletableFuture<T> pending;

   LatestFutureSlot(Executor var1, BooleanSupplier var2) {
      this.executor = var1;
      this.isValid = var2;
   }

   boolean submit(CompletableFuture<T> var1, BiConsumer<T, Throwable> var2) {
      if (this.pending != null) {
         var1.cancel(true);
         return false;
      } else {
         this.pending = var1;
         var1.whenComplete((var3, var4) -> this.executor.execute(() -> {
            if (this.pending == var1) {
               this.pending = null;
               if (this.isValid.getAsBoolean()) {
                  var2.accept(var3, var4);
               }
            }
         }));
         return true;
      }
   }

   void cancel() {
      CompletableFuture var1 = this.pending;
      this.pending = null;
      if (var1 != null) {
         var1.cancel(true);
      }
   }
}
