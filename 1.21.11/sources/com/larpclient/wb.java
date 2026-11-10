package com.larpclient;

import java.util.ArrayDeque;
import java.util.Objects;
import java.util.function.BooleanSupplier;

public final class wb {
   private final ArrayDeque<Runnable> C = new ArrayDeque<>();
   private final int b;
   private long m;
   private boolean F;
   private boolean r;

   public wb(int var1) {
      if (var1 < 1) {
         throw new IllegalArgumentException("capacity");
      }

      this.b = var1;
   }

   public void m(long var1, long var3) {
      if (!this.F && !this.r && this.C.isEmpty() && var3 >= 0L) {
         this.m = var1 + var3;
         this.F = (boolean)1;
      } else {
         throw new IllegalStateException("Invalid hold start");
      }
   }

   public boolean N(Runnable var1) {
      if (this.F && !this.r && this.C.size() < this.b) {
         this.C.addLast(Objects.requireNonNull(var1));
         return true;
      } else {
         return false;
      }
   }

   public boolean F(long var1) {
      return this.F && var1 - this.m >= 0L;
   }

   public boolean G() {
      return this.F;
   }

   public boolean w() {
      return this.r;
   }

   public int r() {
      return this.C.size();
   }

   public void U(BooleanSupplier var1) {
      if (!this.r) {
         this.F = (boolean)0;
         this.r = (boolean)1;

         try {
            while (!this.C.isEmpty() && var1.getAsBoolean()) {
               this.C.removeFirst().run();
            }
         } finally {
            this.C.clear();
            this.r = (boolean)0;
            this.m = 0L;
         }
      }
   }

   public void i() {
      this.C.clear();
      this.F = (boolean)0;
      this.m = 0L;
   }
}
