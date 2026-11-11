package com.larpclient.util.render;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.IntSupplier;
import java.util.function.Supplier;

public final class TextMeasureCache {
   private final Map<TextMeasureCache.Anonymous2, Integer> widthCache = createLruCache();
   private final Map<TextMeasureCache.Anonymous2, String> stringCache = createLruCache();
   private Object owner;
   private int version;

   private static <V> Map<TextMeasureCache.Anonymous2, V> createLruCache() {
      return new LinkedHashMap<TextMeasureCache.Anonymous2, V>(64, 0.75F, true) {
         @Override
         protected boolean removeEldestEntry(Entry<TextMeasureCache.Anonymous2, V> var1) {
            return this.size() > 512;
         }
      };
   }

   public void invalidateIfChanged(Object var1, int var2) {
      if (this.owner != var1 || this.version != var2) {
         this.widthCache.clear();
         this.stringCache.clear();
         this.owner = var1;
         this.version = var2;
      }
   }

   public int getWidth(String var1, int var2, IntSupplier var3) {
      return var1.length() > 256 ? var3.getAsInt() : this.widthCache.computeIfAbsent(new TextMeasureCache.Anonymous2(var1, var2, 0), var1x -> var3.getAsInt());
   }

   public String getString(String var1, int var2, int var3, Supplier<String> var4) {
      return var1.length() > 256
         ? (String)var4.get()
         : this.stringCache.computeIfAbsent(new TextMeasureCache.Anonymous2(var1, var2, var3), var1x -> (String)var4.get());
   }

   int widthCacheSize() {
      return this.widthCache.size();
   }

   int stringCacheSize() {
      return this.stringCache.size();
   }

   private record Anonymous2(String text, int style, int width) {
   }
}
