package com.larpclient;

import com.mojang.renderpearl.api.textures.GpuTexture;
import java.util.Arrays;
import java.util.Collections;
import java.util.Locale;
import java.util.Set;
import java.util.WeakHashMap;
import java.util.stream.Collectors;

public final class _s {
   private static final Set<GpuTexture> T = Collections.synchronizedSet(Collections.newSetFromMap(new WeakHashMap<>()));
   private static final Set<String> P = Arrays.stream(k6.v.values())
      .filter(var0 -> var0 != k6.v.U)
      .map(var0 -> var0.name().toLowerCase(Locale.ROOT))
      .collect(Collectors.toUnmodifiableSet());

   private _s() {
   }

   public static boolean s(GpuTexture var0, String var1, boolean var2) {
      if (!w(var1, var2)) {
         return false;
      }

      T.add(var0);
      return true;
   }

   public static boolean p(GpuTexture var0) {
      return T.contains(var0);
   }

   public static boolean w(String var0, boolean var1) {
      if (!var1 && var0 != null && var0.startsWith("larp-client:")) {
         int var2 = var0.indexOf(47);
         return var2 > 0 && P.contains(var0.substring("larp-client:".length(), var2));
      } else {
         return false;
      }
   }
}
