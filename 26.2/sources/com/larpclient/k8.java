package com.larpclient;

import com.larpclient.module.Module;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.ToIntFunction;

public final class k8 {
   private Module[] l = new Module[0];
   private boolean[] B = new boolean[0];
   private Object S;
   private int f;
   private int G;
   private List<k8.l> y = List.of();

   public List<k8.l> K(List<Module> var1, Object var2, int var3, int var4, ToIntFunction<String> var5) {
      int var6 = this.S == var2 && this.f == var3 && this.G == var4 && this.l.length == var1.size() ? 0 : 1;
      if (var6 == 0) {
         for (int var7 = 0; var7 < this.l.length; var7++) {
            Module var8 = (Module)var1.get(var7);
            if (this.l[var7] != var8 || this.B[var7] != var8.isEnabled()) {
               var6 = 1;
               break;
            }
         }
      }

      if (var6 == 0) {
         return this.y;
      }

      Module[] var12 = var1.toArray(Module[]::new);
      boolean[] var13 = new boolean[var12.length];
      ArrayList var9 = new ArrayList();

      for (int var10 = 0; var10 < var12.length; var10++) {
         Module var11 = var12[var10];
         var13[var10] = var11.isEnabled();
         if (var13[var10]) {
            var9.add(new k8.l(var11.getDisplayName(), var5.applyAsInt(var11.getDisplayName())));
         }
      }

      var9.sort(Comparator.comparingInt(k8.l::width).reversed().thenComparing(k8.l::name));
      this.y = List.copyOf(var9);
      this.l = var12;
      this.B = var13;
      this.S = var2;
      this.f = var3;
      this.G = var4;
      return this.y;
   }

   public record l(String name, int width) {
   }
}
