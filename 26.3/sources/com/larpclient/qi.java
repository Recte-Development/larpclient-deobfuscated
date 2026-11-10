package com.larpclient;

import java.util.ArrayDeque;
import java.util.Iterator;

final class qi {
   static final int r = 10;
   static final int y = 4;
   private final ArrayDeque<qi.z> Z = new ArrayDeque<>();

   void q(long var1) {
      this.Z.addLast(new qi.z(var1));
      if (this.Z.size() > 10) {
         this.Z.removeFirst();
      }
   }

   void M(long var1) {
      Iterator var3 = this.Z.descendingIterator();

      while (var3.hasNext()) {
         qi.z var4 = (qi.z)var3.next();
         if (var4.V == var1 && !var4.o) {
            var4.o = (boolean)1;
            return;
         }
      }
   }

   int L() {
      int var1 = 0;

      for (qi.z var3 : this.Z) {
         if (var3.o) {
            var1++;
         }
      }

      return var1;
   }

   boolean V() {
      return this.L() >= 4;
   }

   void N() {
      this.Z.clear();
   }

   private static final class z {
      final long V;
      boolean o;

      z(long var1) {
         this.V = var1;
      }
   }
}
