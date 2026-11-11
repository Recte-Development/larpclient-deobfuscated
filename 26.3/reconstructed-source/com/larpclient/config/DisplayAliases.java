package com.larpclient.config;

import com.larpclient.social.cosmetic.CosmeticOverrides;
import com.larpclient.util.misc.LarpLog;
import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.util.StringDecomposer;

public final class DisplayAliases {
   private static Map<String, String> aliases = Map.of();
   private static Object lastLevel;
   private static long version;
   private static final ReferenceQueue<FormattedText> rewrittenQueue = new ReferenceQueue<>();
   private static final Set<DisplayAliases.IdentityWeakRef> rewrittenCache = new HashSet<>();

   public static long getVersion() {
      return version;
   }

   public static void tick() {
      Minecraft var0 = Minecraft.getInstance();
      HashMap var1 = new HashMap();
      if (var0.level != null && CosmeticOverrides.isActive()) {
         String var2 = CosmeticOverrides.getName(var0.getUser().getProfileId());
         putAlias(var1, var0.getUser().getName(), var2);
         if (var0.player != null) {
            putAlias(var1, var0.player.getGameProfile().name(), var2);
         }
      }

      applyAliases(var0.level, var1);

      for (Reference var3 = rewrittenQueue.poll(); var3 != null; var3 = rewrittenQueue.poll()) {
         rewrittenCache.remove(var3);
      }
   }

   static void applyAliases(Object var0, Map<String, String> var1) {
      if (lastLevel != var0 || !var1.equals(aliases)) {
         lastLevel = var0;
         aliases = Map.copyOf(var1);
         version++;
         rewrittenCache.clear();
         LarpLog.debug("Appearance", "Display aliases refreshed: {} participants; chat will reflow", aliases.size());
      }
   }

   private static void putAlias(Map<String, String> var0, String var1, String var2) {
      if (!var2.isEmpty() && !var1.equals(var2)) {
         var0.put(var1.toLowerCase(Locale.ROOT), var2);
      }
   }

   public static Component rewriteComponent(Component var0) {
      return (Component)rewrite(var0);
   }

   public static FormattedText rewrite(FormattedText var0) {
      return !aliases.isEmpty() && CosmeticOverrides.isActive() && Minecraft.getInstance().level != null ? rewriteUncached(var0) : var0;
   }

   static FormattedText rewriteUncached(FormattedText var0) {
      if (rewrittenCache.contains(new DisplayAliases.IdentityWeakRef(var0, null))) {
         return var0;
      }

      Component var1 = replaceNames(var0, aliases);
      if (var1 != var0) {
         rewrittenCache.add(new DisplayAliases.IdentityWeakRef(var1, rewrittenQueue));
      }

      return var1;
   }

   public static Component rewriteForPlayer(UUID var0, String var1, Component var2) {
      String var3 = CosmeticOverrides.getName(var0);
      if (var3.isEmpty()) {
         return var2;
      }

      Component var4 = replaceNames(var2, Map.of(var1.toLowerCase(Locale.ROOT), var3));
      if (var4 != var2) {
         rewrittenCache.add(new DisplayAliases.IdentityWeakRef(var4, rewrittenQueue));
      }

      return var4;
   }

   public static Component replaceNames(FormattedText var0, Map<String, String> var1) {
      if (var1.isEmpty()) {
         return toComponent(var0);
      }

      String var2 = var0.getString();
      List var3 = null;
      if (var2.indexOf(167) >= 0) {
         var3 = splitSegments(var0);
         StringBuilder var4 = new StringBuilder();

         for (DisplayAliases.StyledRun var6 : var3) {
            var4.append(var6.text());
         }

         var2 = var4.toString();
      }

      ArrayList var12 = new ArrayList();
      int var13 = 0;

      while (var13 < var2.length()) {
         if (!putAlias(var2.charAt(var13))) {
            var13++;
         } else {
            int var15 = var13;

            while (var13 < var2.length() && putAlias(var2.charAt(var13))) {
               var13++;
            }

            String var7 = (String)var1.get(var2.substring(var15, var13).toLowerCase(Locale.ROOT));
            if (var7 != null && !var7.equals(var2.substring(var15, var13))) {
               var12.add(new DisplayAliases.AliasSpan(var15, var13, var7));
            }
         }
      }

      if (var12.isEmpty()) {
         return toComponent(var0);
      }

      if (var3 == null) {
         var3 = splitSegments(var0);
      }

      MutableComponent var14 = Component.empty();
      int var16 = 0;

      for (DisplayAliases.AliasSpan var8 : var12) {
         rewrite(var14, var3, var16, var8.start());
         Style var9 = Style.EMPTY;

         for (DisplayAliases.StyledRun var11 : var3) {
            if (var11.start() <= var8.start() && var11.end() > var8.start()) {
               var9 = var11.style();
               break;
            }
         }

         var14.append(Component.literal(var8.name()).setStyle(var9));
         var16 = var8.end();
      }

      rewrite(var14, var3, var16, var2.length());
      return var14;
   }

   private static List<DisplayAliases.StyledRun> splitSegments(FormattedText var0) {
      ArrayList var1 = new ArrayList();
      int[] var2 = new int[]{0};
      var0.visit((var2x, var3) -> {
         if (var3.indexOf(167) < 0) {
            var1.add(new DisplayAliases.StyledRun(var2[0], var2[0] + var3.length(), var2x, var3));
            var2[0] += var3.length();
         } else {
            StringBuilder var4 = new StringBuilder();
            Style[] var5 = new Style[]{var2x};
            StringDecomposer.iterateFormatted(var3, var2x, (var4x, var5x, var6) -> {
               if (!var5x.equals(var5[0])) {
                  flushSegment(var1, var2, var5[0], var4);
                  var5[0] = var5x;
               }

               var4.appendCodePoint(var6);
               return true;
            });
            flushSegment(var1, var2, var5[0], var4);
         }

         return Optional.empty();
      }, Style.EMPTY);
      return var1;
   }

   private static void flushSegment(List<DisplayAliases.StyledRun> var0, int[] var1, Style var2, StringBuilder var3) {
      if (!var3.isEmpty()) {
         String var4 = var3.toString();
         var0.add(new DisplayAliases.StyledRun(var1[0], var1[0] + var4.length(), var2, var4));
         var1[0] += var4.length();
         var3.setLength(0);
      }
   }

   private static Component toComponent(FormattedText var0) {
      if (var0 instanceof Component var2) {
         return var2;
      } else {
         MutableComponent var1 = Component.empty();
         var0.visit((var1x, var2x) -> {
            var1.append(Component.literal(var2x).setStyle(var1x));
            return Optional.empty();
         }, Style.EMPTY);
         return var1;
      }
   }

   private static void rewrite(MutableComponent var0, List<DisplayAliases.StyledRun> var1, int var2, int var3) {
      for (DisplayAliases.StyledRun var5 : var1) {
         int var6 = Math.max(var2, var5.start());
         int var7 = Math.min(var3, var5.end());
         if (var6 < var7) {
            var0.append(Component.literal(var5.text().substring(var6 - var5.start(), var7 - var5.start())).setStyle(var5.style()));
         }
      }
   }

   private static boolean putAlias(char var0) {
      return var0 >= 'a' && var0 <= 'z' || var0 >= 'A' && var0 <= 'Z' || var0 >= '0' && var0 <= '9' || var0 == '_';
   }

   private record AliasSpan(int start, int end, String name) {
   }

   private static final class IdentityWeakRef extends WeakReference<FormattedText> {
      private final int identityHash;

      IdentityWeakRef(FormattedText var1, ReferenceQueue<FormattedText> var2) {
         super(var1, var2);
         this.identityHash = System.identityHashCode(var1);
      }

      @Override
      public int hashCode() {
         return this.identityHash;
      }

      @Override
      public boolean equals(Object var1) {
         return this == var1 || var1 instanceof DisplayAliases.IdentityWeakRef var2 && this.get() != null && this.get() == var2.get();
      }
   }

   private record StyledRun(int start, int end, Style style, String text) {
   }
}
