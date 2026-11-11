package com.larpclient.util.inventory;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

final class MapSlotTracker {
   final List<MapSlotTracker.MapMove> moves = new ArrayList<>();
   private Map<MapSlotTracker.SlotRef, MapSlotTracker.SlotContents> lastSnapshot = Map.of();
   private final Map<MapSlotTracker.SlotRef, Integer> pendingCounts = new LinkedHashMap<>();
   private final Map<MapSlotTracker.SlotRef, Integer> lastSeenGeneration = new HashMap<>();
   private int generation;
   private final Map<MapSlotTracker.SlotRef, Integer> expectedMaps = new HashMap<>();

   void expectMap(MapSlotTracker.SlotRef var1, int var2) {
      this.expectedMaps.put(var1, var2);
   }

   void recordMove(String var1, int var2, int var3, MapSlotTracker.SlotRef var4, Map<MapSlotTracker.SlotRef, MapSlotTracker.SlotContents> var5) {
      this.observe(var5);
      this.moves.add(new MapSlotTracker.MapMove(var1, var2, var3, var4));
      this.pendingCounts.clear();
      this.lastSeenGeneration.clear();
   }

   void observe(Map<MapSlotTracker.SlotRef, MapSlotTracker.SlotContents> var1) {
      if (!this.lastSnapshot.equals(var1)) {
         this.generation++;
         this.pendingCounts
            .keySet()
            .removeIf(
               var2x -> this.generation - this.lastSeenGeneration.getOrDefault(var2x, 0) > 4
                  || !var1.containsKey(var2x)
                  || ((MapSlotTracker.SlotContents)var1.get(var2x)).count == 0
            );
         LinkedHashSet var2 = new LinkedHashSet();

         for (Entry var4 : var1.entrySet()) {
            MapSlotTracker.SlotContents var5 = this.lastSnapshot.get(var4.getKey());
            MapSlotTracker.SlotContents var6 = (MapSlotTracker.SlotContents)var4.getValue();
            int var7 = var6.count - (var5 != null && var5.map == var6.map ? var5.count : 0);
            if (var5 != null && var5.map != var6.map) {
               this.pendingCounts.remove(var4.getKey());
            }

            if (var7 > 0 && (var5 != null || !((MapSlotTracker.SlotRef)var4.getKey()).kind.equals("stock"))) {
               var2.add((MapSlotTracker.SlotRef)var4.getKey());
               this.pendingCounts.merge((MapSlotTracker.SlotRef)var4.getKey(), var7, Integer::sum);
               this.lastSeenGeneration.put((MapSlotTracker.SlotRef)var4.getKey(), this.generation);
            }
         }

         for (Entry var16 : this.lastSnapshot.entrySet()) {
            MapSlotTracker.SlotRef var18 = (MapSlotTracker.SlotRef)var16.getKey();
            MapSlotTracker.SlotContents var20 = (MapSlotTracker.SlotContents)var16.getValue();
            MapSlotTracker.SlotContents var22 = (MapSlotTracker.SlotContents)var1.get(var18);
            if (var22 != null || var18.kind.equals("inventory") || var18.kind.equals("cursor")) {
               int var8 = var20.count - (var22 != null && var20.map == var22.map ? var22.count : 0);
               if (var8 > 0) {
                  this.pendingCounts.computeIfPresent(var18, (var2x, var3) -> Math.min(var3, var22 != null && var20.map == var22.map ? var22.count : 0));
               }

               if (var8 > 0) {
                  Integer var9 = this.expectedMaps.remove(var18);
                  List var10 = this.moves.stream().filter(var2x -> var18.equals(var2x.location) && var2x.destMap == var20.map).toList();
                  if (var9 != null && var9 == var20.map) {
                     var8 -= Math.min(var8, Math.max(0, var20.count - var10.size()));
                  }

                  ArrayList var11 = new ArrayList(var10);
                  if (var9 != null) {
                     var11.sort(Comparator.comparingInt(var1x -> var1x.sourceMap == var9 ? 0 : 1));
                  }

                  for (MapSlotTracker.MapMove var13 : var11) {
                     if (var8 <= 0) {
                        break;
                     }

                     var13.location = null;
                     var8--;
                  }
               }
            }
         }

         var2.addAll(this.pendingCounts.keySet());

         for (MapSlotTracker.MapMove var17 : this.moves) {
            if (var17.location == null) {
               for (MapSlotTracker.SlotRef var21 : var2) {
                  if (this.pendingCounts.getOrDefault(var21, 0) > 0 && ((MapSlotTracker.SlotContents)var1.get(var21)).map == var17.destMap) {
                     var17.location = var21;
                     this.pendingCounts.merge(var21, -1, Integer::sum);
                     break;
                  }
               }
            }
         }

         this.lastSnapshot = new LinkedHashMap<>(var1);
      }
   }

   int netCount(String var1, int var2) {
      int var3 = 0;

      for (MapSlotTracker.MapMove var5 : this.moves) {
         if (var5.stockKey.equals(var1) && !var5.isFromStock()) {
            if (var5.destMap == var2) {
               var3++;
            }

            if (var5.sourceMap == var2) {
               var3--;
            }
         }
      }

      return var3;
   }

   MapSlotTracker.MapMove findMove(MapSlotTracker.SlotRef var1, int var2) {
      for (MapSlotTracker.MapMove var4 : this.moves) {
         if (var1.equals(var4.location) && var4.destMap == var2 && !var4.isFromStock()) {
            return var4;
         }
      }

      return null;
   }

   void clearStockMoves() {
      this.moves.removeIf(MapSlotTracker.MapMove::isFromStock);
   }

   void clearStockMovesFor(MapSlotTracker.SlotRef var1) {
      this.moves.removeIf(var1x -> var1x.isFromStock() && var1.equals(var1x.location));
   }

   void reset() {
      this.moves.clear();
      this.lastSnapshot = Map.of();
      this.pendingCounts.clear();
      this.lastSeenGeneration.clear();
      this.generation = 0;
      this.expectedMaps.clear();
   }

   static final class MapMove {
      final String stockKey;
      final int destMap;
      final int sourceMap;
      MapSlotTracker.SlotRef location;

      MapMove(String var1, int var2, int var3, MapSlotTracker.SlotRef var4) {
         this.stockKey = var1;
         this.destMap = var2;
         this.sourceMap = var3;
         this.location = var4;
      }

      boolean isFromStock() {
         return this.location != null && this.location.kind.equals("stock") && this.location.key.equals(this.stockKey);
      }
   }

   record SlotContents(int map, int count) {
   }

   record SlotRef(String kind, String key, int slot) {
   }
}
