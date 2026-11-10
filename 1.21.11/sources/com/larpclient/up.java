package com.larpclient;

import com.larpclient.module.Module;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.ToIntFunction;

public final class up {
   private Module[] q = new Module[0];
   private boolean[] J = new boolean[0];
   private Object u;
   private int M;
   private int E;
   private List<up.d> F = List.of();

   public List<up.d> b(List<Module> var1, Object var2, int var3, int var4, ToIntFunction<String> var5) {
      int var6 = this.u == var2 && this.M == var3 && this.E == var4 && this.q.length == var1.size() ? 0 : 1;
      if (var6 == 0) {
         for (int var7 = 0; var7 < this.q.length; var7++) {
            Module var8 = (Module)var1.get(var7);
            if (this.q[var7] != var8 || this.J[var7] != var8.isEnabled()) {
               var6 = 1;
               break;
            }
         }
      }

      if (var6 == 0) {
         return this.F;
      }

      Module[] var12 = var1.toArray(Module[]::new);
      boolean[] var13 = new boolean[var12.length];
      ArrayList var9 = new ArrayList();

      for (int var10 = 0; var10 < var12.length; var10++) {
         Module var11 = var12[var10];
         var13[var10] = var11.isEnabled();
         if (var13[var10]) {
            var9.add(new up.d(var11.getDisplayName(), var5.applyAsInt(var11.getDisplayName())));
         }
      }

      var9.sort(Comparator.comparingInt(up.d::width).reversed().thenComparing(up.d::name));
      this.F = List.copyOf(var9);
      this.q = var12;
      this.J = var13;
      this.u = var2;
      this.M = var3;
      this.E = var4;
      return this.F;
   }

   public record d(String name, int width) {
   }
}
