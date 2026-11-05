package com.larpclient;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.BiConsumer;
import java.util.function.BooleanSupplier;

final class xd<T> {
   private final Executor v;
   private final BooleanSupplier p;
   private CompletableFuture<T> I;

   xd(Executor var1, BooleanSupplier var2) {
      this.v = var1;
      this.p = var2;
   }

   boolean F(CompletableFuture<T> var1, BiConsumer<T, Throwable> var2) {
      if (this.I != null) {
         var1.cancel(true);
         return false;
      } else {
         this.I = var1;
         var1.whenComplete((var3, var4) -> this.v.execute(() -> {
            if (this.I == var1) {
               this.I = null;
               if (this.p.getAsBoolean()) {
                  var2.accept(var3, var4);
               }
            }
         }));
         return true;
      }
   }

   void k() {
      CompletableFuture var1 = this.I;
      this.I = null;
      if (var1 != null) {
         var1.cancel(true);
      }
   }
}
