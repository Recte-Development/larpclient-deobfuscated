package com.larpclient;

import com.mojang.renderpearl.api.textures.GpuTexture;
import java.util.Arrays;
import java.util.Collections;
import java.util.Locale;
import java.util.Set;
import java.util.WeakHashMap;
import java.util.stream.Collectors;

public final class f5 {
   private static final Set<GpuTexture> I = Collections.synchronizedSet(Collections.newSetFromMap(new WeakHashMap<>()));
   private static final Set<String> V = Arrays.stream(ic.b.values())
      .filter(var0 -> var0 != ic.b.r)
      .map(var0 -> var0.name().toLowerCase(Locale.ROOT))
      .collect(Collectors.toUnmodifiableSet());

   private f5() {
   }

   public static boolean P(GpuTexture var0, String var1, boolean var2) {
      if (!W(var1, var2)) {
         return false;
      }

      I.add(var0);
      return true;
   }

   public static boolean i(GpuTexture var0) {
      return I.contains(var0);
   }

   public static boolean W(String var0, boolean var1) {
      if (!var1 && var0 != null && var0.startsWith("larp-client:")) {
         int var2 = var0.indexOf(47);
         return var2 > 0 && V.contains(var0.substring("larp-client:".length(), var2));
      } else {
         return false;
      }
   }
}
