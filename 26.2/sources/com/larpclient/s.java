package com.larpclient;

import java.util.ArrayDeque;
import java.util.Objects;
import java.util.function.BooleanSupplier;

public final class s {
   private final ArrayDeque<Runnable> D = new ArrayDeque<>();
   private final int m;
   private long r;
   private boolean c;
   private boolean b;

   public s(int var1) {
      if (var1 < 1) {
         throw new IllegalArgumentException("capacity");
      }

      this.m = var1;
   }

   public void o(long var1, long var3) {
      if (!this.c && !this.b && this.D.isEmpty() && var3 >= 0L) {
         this.r = var1 + var3;
         this.c = (boolean)1;
      } else {
         throw new IllegalStateException("Invalid hold start");
      }
   }

   public boolean n(Runnable var1) {
      if (this.c && !this.b && this.D.size() < this.m) {
         this.D.addLast(Objects.requireNonNull(var1));
         return true;
      } else {
         return false;
      }
   }

   public boolean s(long var1) {
      return this.c && var1 - this.r >= 0L;
   }

   public boolean s() {
      return this.c;
   }

   public boolean B() {
      return this.b;
   }

   public int w() {
      return this.D.size();
   }

   public void b(BooleanSupplier var1) {
      if (!this.b) {
         this.c = (boolean)0;
         this.b = (boolean)1;

         try {
            while (!this.D.isEmpty() && var1.getAsBoolean()) {
               this.D.removeFirst().run();
            }
         } finally {
            this.D.clear();
            this.b = (boolean)0;
            this.r = 0L;
         }
      }
   }

   public void q() {
      this.D.clear();
      this.c = (boolean)0;
      this.r = 0L;
   }
}
