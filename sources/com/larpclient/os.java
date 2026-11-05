package com.larpclient;

import java.util.ArrayDeque;
import java.util.Objects;
import java.util.function.BooleanSupplier;

public final class os {
   private final ArrayDeque<Runnable> B = new ArrayDeque<>();
   private final int c;
   private long e;
   private boolean J;
   private boolean a;

   public os(int var1) {
      if (var1 < 1) {
         throw new IllegalArgumentException("capacity");
      }

      this.c = var1;
   }

   public void e(long var1, long var3) {
      if (!this.J && !this.a && this.B.isEmpty() && var3 >= 0L) {
         this.e = var1 + var3;
         this.J = (boolean)1;
      } else {
         throw new IllegalStateException("Invalid hold start");
      }
   }

   public boolean I(Runnable var1) {
      if (this.J && !this.a && this.B.size() < this.c) {
         this.B.addLast(Objects.requireNonNull(var1));
         return true;
      } else {
         return false;
      }
   }

   public boolean f(long var1) {
      return this.J && var1 - this.e >= 0L;
   }

   public boolean D() {
      return this.J;
   }

   public boolean g() {
      return this.a;
   }

   public int I() {
      return this.B.size();
   }

   public void Q(BooleanSupplier var1) {
      if (!this.a) {
         this.J = (boolean)0;
         this.a = (boolean)1;

         try {
            while (!this.B.isEmpty() && var1.getAsBoolean()) {
               this.B.removeFirst().run();
            }
         } finally {
            this.B.clear();
            this.a = (boolean)0;
            this.e = 0L;
         }
      }
   }

   public void Q() {
      this.B.clear();
      this.J = (boolean)0;
      this.e = 0L;
   }
}
