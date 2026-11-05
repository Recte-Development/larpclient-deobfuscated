package com.larpclient;

import com.larpclient.module.FakePayModule;
import com.larpclient.module.FakeSpawnerModule;
import com.larpclient.module.Module;
import com.larpclient.module.OrderDropperModule;
import com.larpclient.module.PerformanceModule;
import com.larpclient.module.StorageESPModule;
import com.larpclient.module.StreamProofModule;
import com.larpclient.module.tf;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

public enum lw {
   l("Favorites", "star"),
   i("Combat", "swords"),
   X("Movement", "footprints"),
   z("Player", "user"),
   d("Visuals", "eye"),
   e("Cosmetics", "paintbrush"),
   u("Base Finding", "search"),
   o("Misc", "list"),
   g("DonutSmp", "store");

   public final String C;
   public final String A;
   private static final Set<String> I = Set.of("AutoArmor", "AutoGap", "Fake Lag");
   private static final Set<String> h = Set.of("IgnoreEntity");
   private static final Set<String> P = Set.of("Freecam", "SmoothCamera", "TargetHUD", "CustomCrosshair", "Damage Markers", "Stretched Res");
   private static final Set<String> f = Set.of("Light Debug", "Motion Scanner", "Holes ESP");
   private static final Set<String> N = Set.of(
      "Animations",
      "ViewModel",
      "Sword Models",
      "Player Trail",
      "HitFX",
      "Hit Sounds",
      "Click Pulse",
      "JumpFX",
      "TargetAura",
      "SelfVisuals",
      "CustomSky",
      "Sky Shader",
      "China Hat",
      "Wings",
      "Headless Korblox",
      "Ambient Particles",
      "Item Physic",
      "EchoHand",
      "Hand Chams",
      "Flame Hands",
      "Player Model",
      "Skin Changer",
      "Capes",
      "Crystal Chams",
      "Walk Style",
      "Emotes",
      "Pets",
      "Crypto Pets",
      "Astral Weapon Trails",
      "Celestial Weather",
      "Fog",
      "WorldLyrics",
      "KillFX",
      "GUI Animation",
      "3D UI",
      "World Menu",
      "Line Glyphs",
      "Pearl Particles",
      "No Block Rotation",
      "TimeChanger",
      "WorldColor",
      "Motion Blur",
      "Realistic Water",
      "Waving Foliage",
      "Wet Ground",
      "Snow Ground"
   );

   lw(String var3, String var4) {
      this.C = var3;
      this.A = var4;
   }

   public boolean n(lw var1, Runnable var2) {
      if (this == var1) {
         return false;
      }

      var2.run();
      return true;
   }

   public static lw k(Module var0) {
      if (var0.getName().equals("LARP") || var0.getName().equals("Name Changer") || var0 instanceof PerformanceModule || var0 instanceof StreamProofModule) {
         return o;
      }

      if (I.contains(var0.getName())) {
         return i;
      }

      if (h.contains(var0.getName())) {
         return z;
      }

      if (P.contains(var0.getName())) {
         return d;
      }

      if (N.contains(var0.getName())) {
         return e;
      }

      if (f.contains(var0.getName()) || var0 instanceof tf || var0 instanceof StorageESPModule) {
         return u;
      }

      if (!(var0 instanceof com.larpclient.module.a)
         && !(var0 instanceof FakeSpawnerModule)
         && !(var0 instanceof FakePayModule)
         && !(var0 instanceof OrderDropperModule)) {
         return switch (var0.getCategory()) {
            case COMBAT -> i;
            case MOVEMENT -> X;
            case PLAYER -> z;
            case RENDER, VISUALS -> d;
         };
      } else {
         return g;
      }
   }

   public static String t(Module var0) {
      return var0 instanceof com.larpclient.module.a ? "DonutSmp Gambling Rigs" : "";
   }

   public boolean s(String var1) {
      return var1.isBlank() && this != l;
   }

   public boolean X(Module var1, String var2, fs var3) {
      if (!var2.isBlank()) {
         return d3.i(var1, var2) != Integer.MAX_VALUE;
      } else {
         return this == l ? var3.s(var1.getName()) : k(var1) == this;
      }
   }

   public List<Module> f(List<Module> var1, String var2, fs var3) {
      return ChooseASkyEffectAndAdjustItsAppearanceModule.O(var1, var3)
         .stream()
         .filter(var3x -> this.X(var3x, var2, var3))
         .sorted(
            Comparator.<Module, String>comparing(var2x -> this.s(var2) ? t(var2x) : "")
               .thenComparing(var1x -> !var3.s(var1x.getName()))
               .thenComparing(Module::getDisplayName, String.CASE_INSENSITIVE_ORDER)
               .thenComparing(Module::getName)
         )
         .toList();
   }

   public static lw z(String var0) {
      if (var0 == null) {
         return d;
      }

      if (!var0.equals("RENDER") && !var0.equals("WORLD")) {
         try {
            return valueOf(var0);
         } catch (IllegalArgumentException var2) {
            return d;
         }
      } else {
         return d;
      }
   }
}
