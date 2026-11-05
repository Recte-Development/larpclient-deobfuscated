package com.larpclient;

import com.larpclient.module.Module;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.ToIntFunction;

public final class _u {
   private Module[] F = new Module[0];
   private boolean[] Q = new boolean[0];
   private Object n;
   private int E;
   private int k;
   private List<_u.d> r = List.of();

   public List<_u.d> n(List<Module> var1, Object var2, int var3, int var4, ToIntFunction<String> var5) {
      int var6 = this.n == var2 && this.E == var3 && this.k == var4 && this.F.length == var1.size() ? 0 : 1;
      if (var6 == 0) {
         for (int var7 = 0; var7 < this.F.length; var7++) {
            Module var8 = (Module)var1.get(var7);
            if (this.F[var7] != var8 || this.Q[var7] != var8.isEnabled()) {
               var6 = 1;
               break;
            }
         }
      }

      if (var6 == 0) {
         return this.r;
      }

      Module[] var12 = var1.toArray(Module[]::new);
      boolean[] var13 = new boolean[var12.length];
      ArrayList var9 = new ArrayList();

      for (int var10 = 0; var10 < var12.length; var10++) {
         Module var11 = var12[var10];
         var13[var10] = var11.isEnabled();
         if (var13[var10]) {
            var9.add(new _u.d(var11.getDisplayName(), var5.applyAsInt(var11.getDisplayName())));
         }
      }

      var9.sort(Comparator.comparingInt(_u.d::width).reversed().thenComparing(_u.d::name));
      this.r = List.copyOf(var9);
      this.F = var12;
      this.Q = var13;
      this.n = var2;
      this.E = var3;
      this.k = var4;
      return this.r;
   }

   public record d(String name, int width) {
   }
}
