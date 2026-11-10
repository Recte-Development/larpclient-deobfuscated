package com.larpclient;

import com.mojang.blaze3d.textures.GpuTexture;
import java.util.Arrays;
import java.util.Collections;
import java.util.Locale;
import java.util.Set;
import java.util.WeakHashMap;
import java.util.stream.Collectors;

public final class y7 {
   private static final Set<GpuTexture> w = Collections.synchronizedSet(Collections.newSetFromMap(new WeakHashMap<>()));
   private static final Set<String> H = Arrays.stream(uw.r.values())
      .filter(var0 -> var0 != uw.r.J)
      .map(var0 -> var0.name().toLowerCase(Locale.ROOT))
      .collect(Collectors.toUnmodifiableSet());

   private y7() {
   }

   public static boolean g(GpuTexture var0, String var1, boolean var2) {
      if (!S(var1, var2)) {
         return false;
      }

      w.add(var0);
      return true;
   }

   public static boolean Z(GpuTexture var0) {
      return w.contains(var0);
   }

   public static boolean S(String var0, boolean var1) {
      if (!var1 && var0 != null && var0.startsWith("larp-client:")) {
         int var2 = var0.indexOf(47);
         return var2 > 0 && H.contains(var0.substring("larp-client:".length(), var2));
      } else {
         return false;
      }
   }
}
