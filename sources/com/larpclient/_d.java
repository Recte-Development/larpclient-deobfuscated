package com.larpclient;

import com.mojang.renderpearl.api.textures.GpuTexture;
import java.util.Arrays;
import java.util.Collections;
import java.util.Locale;
import java.util.Set;
import java.util.WeakHashMap;
import java.util.stream.Collectors;

public final class _d {
   private static final Set<GpuTexture> f = Collections.synchronizedSet(Collections.newSetFromMap(new WeakHashMap<>()));
   private static final Set<String> x = Arrays.stream(j_.w.values())
      .filter(var0 -> var0 != j_.w.Y)
      .map(var0 -> var0.name().toLowerCase(Locale.ROOT))
      .collect(Collectors.toUnmodifiableSet());

   private _d() {
   }

   public static boolean R(GpuTexture var0, String var1, boolean var2) {
      if (!W(var1, var2)) {
         return false;
      }

      f.add(var0);
      return true;
   }

   public static boolean j(GpuTexture var0) {
      return f.contains(var0);
   }

   public static boolean W(String var0, boolean var1) {
      if (!var1 && var0 != null && var0.startsWith("larp-client:")) {
         int var2 = var0.indexOf(47);
         return var2 > 0 && x.contains(var0.substring("larp-client:".length(), var2));
      } else {
         return false;
      }
   }
}
