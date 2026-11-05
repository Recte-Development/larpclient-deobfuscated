package com.larpclient;

import com.larpclient.module.Module;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.ToIntFunction;

public final class wo {
   private Module[] B = new Module[0];
   private boolean[] C = new boolean[0];
   private Object Z;
   private int F;
   private int I;
   private List<wo.i> O = List.of();

   public List<wo.i> O(List<Module> var1, Object var2, int var3, int var4, ToIntFunction<String> var5) {
      int var6 = this.Z == var2 && this.F == var3 && this.I == var4 && this.B.length == var1.size() ? 0 : 1;
      if (var6 == 0) {
         for (int var7 = 0; var7 < this.B.length; var7++) {
            Module var8 = (Module)var1.get(var7);
            if (this.B[var7] != var8 || this.C[var7] != var8.isEnabled()) {
               var6 = 1;
               break;
            }
         }
      }

      if (var6 == 0) {
         return this.O;
      }

      Module[] var12 = var1.toArray(Module[]::new);
      boolean[] var13 = new boolean[var12.length];
      ArrayList var9 = new ArrayList();

      for (int var10 = 0; var10 < var12.length; var10++) {
         Module var11 = var12[var10];
         var13[var10] = var11.isEnabled();
         if (var13[var10]) {
            var9.add(new wo.i(var11.getDisplayName(), var5.applyAsInt(var11.getDisplayName())));
         }
      }

      var9.sort(Comparator.comparingInt(wo.i::width).reversed().thenComparing(wo.i::name));
      this.O = List.copyOf(var9);
      this.B = var12;
      this.C = var13;
      this.Z = var2;
      this.F = var3;
      this.I = var4;
      return this.O;
   }

   public record i(String name, int width) {
   }
}
