package com.larpclient;

import java.util.ArrayDeque;
import java.util.Objects;
import java.util.function.BooleanSupplier;

public final class pu {
   private final ArrayDeque<pu.x> Z = new ArrayDeque<>();
   private final int T;
   private boolean f;

   public pu(int var1) {
      if (var1 < 1) {
         throw new IllegalArgumentException("capacity");
      }

      this.T = var1;
   }

   public boolean D(long var1, long var3, Runnable var5) {
      if (var3 < 0L) {
         throw new IllegalArgumentException("duration");
      } else if (!this.f && this.Z.size() < this.T) {
         this.Z.addLast(new pu.x(var1 + var3, Objects.requireNonNull(var5)));
         return true;
      } else {
         return false;
      }
   }

   public int Y() {
      return this.Z.size();
   }

   public boolean C() {
      return this.f;
   }

   public void H() {
      this.Z.clear();
   }

   public void z(long var1, boolean var3, BooleanSupplier var4) {
      if (!this.f) {
         this.f = (boolean)1;

         try {
            while (!this.Z.isEmpty() && var4.getAsBoolean() && (var3 || var1 - this.Z.peekFirst().deadline() >= 0L)) {
               this.Z.removeFirst().action().run();
            }

            if (!var4.getAsBoolean()) {
               this.Z.clear();
            }
         } catch (RuntimeException | Error var9) {
            this.Z.clear();
            throw var9;
         } finally {
            this.f = (boolean)0;
         }
      }
   }

   private record x(long deadline, Runnable action) {
   }
}
