package com.larpclient;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.BiConsumer;
import java.util.function.BooleanSupplier;

final class ps<T> {
   private final Executor k;
   private final BooleanSupplier C;
   private CompletableFuture<T> q;

   ps(Executor var1, BooleanSupplier var2) {
      this.k = var1;
      this.C = var2;
   }

   boolean o(CompletableFuture<T> var1, BiConsumer<T, Throwable> var2) {
      if (this.q != null) {
         var1.cancel(true);
         return false;
      } else {
         this.q = var1;
         var1.whenComplete((var3, var4) -> this.k.execute(() -> {
            if (this.q == var1) {
               this.q = null;
               if (this.C.getAsBoolean()) {
                  var2.accept(var3, var4);
               }
            }
         }));
         return true;
      }
   }

   void m() {
      CompletableFuture var1 = this.q;
      this.q = null;
      if (var1 != null) {
         var1.cancel(true);
      }
   }
}
