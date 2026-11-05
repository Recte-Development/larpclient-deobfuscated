package com.larpclient;

import java.util.ArrayDeque;
import java.util.Objects;
import java.util.function.BooleanSupplier;

public final class k8 {
   private final ArrayDeque<Runnable> f = new ArrayDeque<>();
   private final int F;
   private long Y;
   private boolean m;
   private boolean s;

   public k8(int var1) {
      if (var1 < 1) {
         throw new IllegalArgumentException("capacity");
      }

      this.F = var1;
   }

   public void b(long var1, long var3) {
      if (!this.m && !this.s && this.f.isEmpty() && var3 >= 0L) {
         this.Y = var1 + var3;
         this.m = (boolean)1;
      } else {
         throw new IllegalStateException("Invalid hold start");
      }
   }

   public boolean J(Runnable var1) {
      if (this.m && !this.s && this.f.size() < this.F) {
         this.f.addLast(Objects.requireNonNull(var1));
         return true;
      } else {
         return false;
      }
   }

   public boolean T(long var1) {
      return this.m && var1 - this.Y >= 0L;
   }

   public boolean W() {
      return this.m;
   }

   public boolean F() {
      return this.s;
   }

   public int u() {
      return this.f.size();
   }

   public void M(BooleanSupplier var1) {
      if (!this.s) {
         this.m = (boolean)0;
         this.s = (boolean)1;

         try {
            while (!this.f.isEmpty() && var1.getAsBoolean()) {
               this.f.removeFirst().run();
            }
         } finally {
            this.f.clear();
            this.s = (boolean)0;
            this.Y = 0L;
         }
      }
   }

   public void C() {
      this.f.clear();
      this.m = (boolean)0;
      this.Y = 0L;
   }
}
