package com.larpclient.util.data;

import com.larpclient.module.misc.Module;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.ToIntFunction;

public final class ModuleListCache {
   private Module[] cachedModules = new Module[0];
   private boolean[] cachedEnabled = new boolean[0];
   private Object cachedFontKey;
   private int cachedSize;
   private int cachedStyle;
   private List<ModuleListCache.ModuleEntry> entries = List.of();

   public List<ModuleListCache.ModuleEntry> getEntries(List<Module> var1, Object var2, int var3, int var4, ToIntFunction<String> var5) {
      int var6 = this.cachedFontKey == var2 && this.cachedSize == var3 && this.cachedStyle == var4 && this.cachedModules.length == var1.size() ? 0 : 1;
      if (var6 == 0) {
         for (int var7 = 0; var7 < this.cachedModules.length; var7++) {
            Module var8 = (Module)var1.get(var7);
            if (this.cachedModules[var7] != var8 || this.cachedEnabled[var7] != var8.isEnabled()) {
               var6 = 1;
               break;
            }
         }
      }

      if (var6 == 0) {
         return this.entries;
      }

      Module[] var12 = var1.toArray(Module[]::new);
      boolean[] var13 = new boolean[var12.length];
      ArrayList var9 = new ArrayList();

      for (int var10 = 0; var10 < var12.length; var10++) {
         Module var11 = var12[var10];
         var13[var10] = var11.isEnabled();
         if (var13[var10]) {
            var9.add(new ModuleListCache.ModuleEntry(var11.getDisplayName(), var5.applyAsInt(var11.getDisplayName())));
         }
      }

      var9.sort(Comparator.comparingInt(ModuleListCache.ModuleEntry::width).reversed().thenComparing(ModuleListCache.ModuleEntry::name));
      this.entries = List.copyOf(var9);
      this.cachedModules = var12;
      this.cachedEnabled = var13;
      this.cachedFontKey = var2;
      this.cachedSize = var3;
      this.cachedStyle = var4;
      return this.entries;
   }

   public record ModuleEntry(String name, int width) {
   }
}
