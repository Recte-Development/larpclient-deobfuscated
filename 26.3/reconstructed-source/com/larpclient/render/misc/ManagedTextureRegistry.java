package com.larpclient.render.misc;

import com.larpclient.render.overlay.FontRenderer;
import com.mojang.renderpearl.api.textures.GpuTexture;
import java.util.Arrays;
import java.util.Collections;
import java.util.Locale;
import java.util.Set;
import java.util.WeakHashMap;
import java.util.stream.Collectors;

public final class ManagedTextureRegistry {
   private static final Set<GpuTexture> trackedTextures = Collections.synchronizedSet(Collections.newSetFromMap(new WeakHashMap<>()));
   private static final Set<String> managedPrefixes = Arrays.stream(FontRenderer.Face.values())
      .filter(var0 -> var0 != FontRenderer.Face.HUD)
      .map(var0 -> var0.name().toLowerCase(Locale.ROOT))
      .collect(Collectors.toUnmodifiableSet());

   private ManagedTextureRegistry() {
   }

   public static boolean track(GpuTexture var0, String var1, boolean var2) {
      if (!isManagedLabel(var1, var2)) {
         return false;
      }

      trackedTextures.add(var0);
      return true;
   }

   public static boolean isTracked(GpuTexture var0) {
      return trackedTextures.contains(var0);
   }

   public static boolean isManagedLabel(String var0, boolean var1) {
      if (!var1 && var0 != null && var0.startsWith("larp-client:")) {
         int var2 = var0.indexOf(47);
         return var2 > 0 && managedPrefixes.contains(var0.substring("larp-client:".length(), var2));
      } else {
         return false;
      }
   }
}
