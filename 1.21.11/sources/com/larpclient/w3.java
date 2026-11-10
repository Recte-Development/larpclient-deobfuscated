package com.larpclient;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.class_2338;

public final class w3 {
   public static final int o = 8;
   public static final int L = 40;
   private final List<w3.l> Y = new ArrayList<>(8);

   public void s(class_2338 var1, long var2, int var4) {
      this.Y.removeIf(var1x -> var1x.z.equals(var1));
      if (this.Y.size() == 8) {
         this.Y.remove(0);
      }

      this.Y.add(new w3.l(var1.method_10062(), var2, Math.max(0, var4)));
   }

   public List<class_2338> f() {
      ArrayList var1 = new ArrayList(this.Y.size());

      for (w3.l var3 : this.Y) {
         var1.add(var3.z);
      }

      return var1;
   }

   public w3.s W(class_2338 var1, boolean var2, long var3, int var5) {
      w3.l var6 = this.q(var1);
      if (var6 == null) {
         return w3.s.Y;
      }

      if (!var2) {
         if (var6.R < 0L && var3 - var6.k <= 40L) {
            return w3.s.a;
         }

         this.Y.remove(var6);
         return w3.s.Y;
      } else {
         if (var6.R < 0L) {
            var6.R = Math.max(var3 + var6.S, var6.k + 1L);
         }

         if (var3 >= var6.R + Math.max(1, var5)) {
            this.Y.remove(var6);
            return w3.s.Y;
         } else {
            return var3 >= var6.R ? w3.s.m : w3.s.a;
         }
      }
   }

   public long R(class_2338 var1, long var2) {
      w3.l var4 = this.q(var1);
      return var4 == null ? -1L : var2 - var4.k;
   }

   public void z(class_2338 var1) {
      this.Y.removeIf(var1x -> var1x.z.equals(var1));
   }

   public boolean C() {
      return this.Y.isEmpty();
   }

   public void H() {
      this.Y.clear();
   }

   private w3.l q(class_2338 var1) {
      for (w3.l var3 : this.Y) {
         if (var3.z.equals(var1)) {
            return var3;
         }
      }

      return null;
   }

   private static final class l {
      final class_2338 z;
      final long k;
      final int S;
      long R = -1L;

      l(class_2338 var1, long var2, int var4) {
         this.z = var1;
         this.k = var2;
         this.S = var4;
      }
   }

   public enum s {
      a,
      m,
      Y;
   }
}
