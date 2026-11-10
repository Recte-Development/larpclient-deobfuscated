package com.larpclient;

import com.mojang.blaze3d.textures.GpuTexture;
import java.util.Arrays;
import java.util.Collections;
import java.util.Locale;
import java.util.Set;
import java.util.WeakHashMap;
import java.util.stream.Collectors;

public final class g9 {
   private static final Set<GpuTexture> Q = Collections.synchronizedSet(Collections.newSetFromMap(new WeakHashMap<>()));
   private static final Set<String> K = Arrays.stream(bj.j.values())
      .filter(var0 -> var0 != bj.j.M)
      .map(var0 -> var0.name().toLowerCase(Locale.ROOT))
      .collect(Collectors.toUnmodifiableSet());

   private g9() {
   }

   public static boolean z(GpuTexture var0, String var1, boolean var2) {
      if (!W(var1, var2)) {
         return false;
      }

      Q.add(var0);
      return true;
   }

   public static boolean E(GpuTexture var0) {
      return Q.contains(var0);
   }

   public static boolean W(String var0, boolean var1) {
      if (!var1 && var0 != null && var0.startsWith("larp-client:")) {
         int var2 = var0.indexOf(47);
         return var2 > 0 && K.contains(var0.substring("larp-client:".length(), var2));
      } else {
         return false;
      }
   }
}
