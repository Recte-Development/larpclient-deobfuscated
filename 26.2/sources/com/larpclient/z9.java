package com.larpclient;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.BiConsumer;
import java.util.function.BooleanSupplier;

final class z9<T> {
   private final Executor R;
   private final BooleanSupplier l;
   private CompletableFuture<T> C;

   z9(Executor var1, BooleanSupplier var2) {
      this.R = var1;
      this.l = var2;
   }

   boolean J(CompletableFuture<T> var1, BiConsumer<T, Throwable> var2) {
      if (this.C != null) {
         var1.cancel(true);
         return false;
      } else {
         this.C = var1;
         var1.whenComplete((var3, var4) -> this.R.execute(() -> {
            if (this.C == var1) {
               this.C = null;
               if (this.l.getAsBoolean()) {
                  var2.accept(var3, var4);
               }
            }
         }));
         return true;
      }
   }

   void v() {
      CompletableFuture var1 = this.C;
      this.C = null;
      if (var1 != null) {
         var1.cancel(true);
      }
   }
}
