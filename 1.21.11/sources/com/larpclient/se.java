package com.larpclient;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.BiConsumer;
import java.util.function.BooleanSupplier;

final class se<T> {
   private final Executor C;
   private final BooleanSupplier U;
   private CompletableFuture<T> q;

   se(Executor var1, BooleanSupplier var2) {
      this.C = var1;
      this.U = var2;
   }

   boolean P(CompletableFuture<T> var1, BiConsumer<T, Throwable> var2) {
      if (this.q != null) {
         var1.cancel(true);
         return false;
      } else {
         this.q = var1;
         var1.whenComplete((var3, var4) -> this.C.execute(() -> {
            if (this.q == var1) {
               this.q = null;
               if (this.U.getAsBoolean()) {
                  var2.accept(var3, var4);
               }
            }
         }));
         return true;
      }
   }

   void K() {
      CompletableFuture var1 = this.q;
      this.q = null;
      if (var1 != null) {
         var1.cancel(true);
      }
   }
}
