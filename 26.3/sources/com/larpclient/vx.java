package com.larpclient;

import java.util.ArrayDeque;
import java.util.Objects;
import java.util.function.BooleanSupplier;

public final class vx {
   private final ArrayDeque<Runnable> E = new ArrayDeque<>();
   private final int N;
   private long p;
   private boolean v;
   private boolean a;

   public vx(int var1) {
      if (var1 < 1) {
         throw new IllegalArgumentException("capacity");
      }

      this.N = var1;
   }

   public void y(long var1, long var3) {
      if (!this.v && !this.a && this.E.isEmpty() && var3 >= 0L) {
         this.p = var1 + var3;
         this.v = (boolean)1;
      } else {
         throw new IllegalStateException("Invalid hold start");
      }
   }

   public boolean Y(Runnable var1) {
      if (this.v && !this.a && this.E.size() < this.N) {
         this.E.addLast(Objects.requireNonNull(var1));
         return true;
      } else {
         return false;
      }
   }

   public boolean c(long var1) {
      return this.v && var1 - this.p >= 0L;
   }

   public boolean k() {
      return this.v;
   }

   public boolean h() {
      return this.a;
   }

   public int b() {
      return this.E.size();
   }

   public void L(BooleanSupplier var1) {
      if (!this.a) {
         this.v = (boolean)0;
         this.a = (boolean)1;

         try {
            while (!this.E.isEmpty() && var1.getAsBoolean()) {
               this.E.removeFirst().run();
            }
         } finally {
            this.E.clear();
            this.a = (boolean)0;
            this.p = 0L;
         }
      }
   }

   public void v() {
      this.E.clear();
      this.v = (boolean)0;
      this.p = 0L;
   }
}
