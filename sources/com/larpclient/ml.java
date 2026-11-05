package com.larpclient;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.BiConsumer;
import java.util.function.BooleanSupplier;

final class ml<T> {
   private final Executor m;
   private final BooleanSupplier l;
   private CompletableFuture<T> G;

   ml(Executor var1, BooleanSupplier var2) {
      this.m = var1;
      this.l = var2;
   }

   boolean I(CompletableFuture<T> var1, BiConsumer<T, Throwable> var2) {
      if (this.G != null) {
         var1.cancel(true);
         return false;
      } else {
         this.G = var1;
         var1.whenComplete((var3, var4) -> this.m.execute(() -> {
            if (this.G == var1) {
               this.G = null;
               if (this.l.getAsBoolean()) {
                  var2.accept(var3, var4);
               }
            }
         }));
         return true;
      }
   }

   void w() {
      CompletableFuture var1 = this.G;
      this.G = null;
      if (var1 != null) {
         var1.cancel(true);
      }
   }
}
