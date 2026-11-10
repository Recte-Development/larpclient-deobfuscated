package com.larpclient;

import com.larpclient.module.Module;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.ToIntFunction;

public final class n7 {
   private Module[] O = new Module[0];
   private boolean[] D = new boolean[0];
   private Object C;
   private int v;
   private int b;
   private List<n7.p> N = List.of();

   public List<n7.p> K(List<Module> var1, Object var2, int var3, int var4, ToIntFunction<String> var5) {
      int var6 = this.C == var2 && this.v == var3 && this.b == var4 && this.O.length == var1.size() ? 0 : 1;
      if (var6 == 0) {
         for (int var7 = 0; var7 < this.O.length; var7++) {
            Module var8 = (Module)var1.get(var7);
            if (this.O[var7] != var8 || this.D[var7] != var8.isEnabled()) {
               var6 = 1;
               break;
            }
         }
      }

      if (var6 == 0) {
         return this.N;
      }

      Module[] var12 = var1.toArray(Module[]::new);
      boolean[] var13 = new boolean[var12.length];
      ArrayList var9 = new ArrayList();

      for (int var10 = 0; var10 < var12.length; var10++) {
         Module var11 = var12[var10];
         var13[var10] = var11.isEnabled();
         if (var13[var10]) {
            var9.add(new n7.p(var11.getDisplayName(), var5.applyAsInt(var11.getDisplayName())));
         }
      }

      var9.sort(Comparator.comparingInt(n7.p::width).reversed().thenComparing(n7.p::name));
      this.N = List.copyOf(var9);
      this.O = var12;
      this.D = var13;
      this.C = var2;
      this.v = var3;
      this.b = var4;
      return this.N;
   }

   public record p(String name, int width) {
   }
}
