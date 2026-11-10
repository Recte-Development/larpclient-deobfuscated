package com.larpclient;

import com.larpclient.module.FakePayModule;
import com.larpclient.module.FakePlayerModule;
import com.larpclient.module.FakeScoreboardModule;
import com.larpclient.module.FakeSpawnerModule;
import com.larpclient.module.Module;
import com.larpclient.module.OrderDropperModule;
import com.larpclient.module.PerformanceModule;
import com.larpclient.module.StorageESPModule;
import com.larpclient.module.StreamProofModule;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

public enum pz {
   s("Favorites", "star"),
   C("Combat", "swords"),
   V("Movement", "footprints"),
   P("Player", "user"),
   F("Visuals", "eye"),
   E("Cosmetics", "paintbrush"),
   x("Base Finding", "search"),
   j("Misc", "list"),
   t("DonutSmp", "store");

   public final String u;
   public final String D;
   private static final Set<String> N = Set.of("AutoArmor", "AutoGap", "Fake Lag");
   private static final Set<String> w = Set.of("IgnoreEntity");
   private static final Set<String> n = Set.of("Freecam", "SmoothCamera", "TargetHUD", "CustomCrosshair", "Damage Markers", "Stretched Res");
   private static final Set<String> T = Set.of("Light Debug", "Motion Scanner", "Holes ESP");
   private static final Set<String> A = Set.of(
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
      "Hide Armour",
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

   pz(String var3, String var4) {
      this.u = var3;
      this.D = var4;
   }

   public static List<pz> Y() {
      return com.larpclient.module.c.p() ? List.of(s, F, E, j) : List.of(values());
   }

   public boolean M(pz var1, Runnable var2) {
      if (this == var1) {
         return false;
      }

      var2.run();
      return true;
   }

   public static pz B(Module var0) {
      if (var0.getName().equals("LARP")
         || var0.getName().equals("Name Changer")
         || var0.getName().equals("Fake FPS")
         || var0 instanceof PerformanceModule
         || var0 instanceof StreamProofModule) {
         return j;
      }

      if (N.contains(var0.getName())) {
         return C;
      }

      if (w.contains(var0.getName())) {
         return P;
      }

      if (n.contains(var0.getName())) {
         return F;
      }

      if (A.contains(var0.getName())) {
         return E;
      }

      if (T.contains(var0.getName()) || var0 instanceof com.larpclient.module.pm || var0 instanceof StorageESPModule) {
         return x;
      }

      if (var0 instanceof com.larpclient.module.to || var0 instanceof FakeSpawnerModule || var0 instanceof FakePayModule || var0 instanceof OrderDropperModule) {
         return t;
      }

      if (var0 instanceof s1 || var0 instanceof FakeScoreboardModule) {
         return t;
      }

      if (var0 instanceof FakePlayerModule) {
         return j;
      }

      return switch (var0.getCategory()) {
         case COMBAT -> C;
         case MOVEMENT -> V;
         case PLAYER -> P;
         case RENDER, VISUALS -> F;
      };
   }

   public static String W(Module var0) {
      if (var0 instanceof s1) {
         return "DonutSmp Bots";
      } else {
         return var0 instanceof com.larpclient.module.to ? "DonutSmp Gambling Rigs" : "";
      }
   }

   public boolean Y(String var1) {
      return var1.isBlank() && this != s;
   }

   public boolean f(Module var1, String var2, hb var3) {
      if (!var2.isBlank()) {
         return sy.G(var1, var2) != Integer.MAX_VALUE;
      } else {
         return this == s ? var3.M(var1.getName()) : B(var1) == this;
      }
   }

   public List<Module> P(List<Module> var1, String var2, hb var3) {
      return ChooseASkyEffectAndAdjustItsAppearanceModule.Z(var1, var3)
         .stream()
         .filter(var3x -> this.f(var3x, var2, var3))
         .sorted(
            Comparator.<Module, String>comparing(var2x -> this.Y(var2) ? W(var2x) : "")
               .thenComparing(var1x -> !var3.M(var1x.getName()))
               .thenComparing(Module::getDisplayName, String.CASE_INSENSITIVE_ORDER)
               .thenComparing(Module::getName)
         )
         .toList();
   }

   public static pz m(String var0) {
      if (var0 == null) {
         return F;
      }

      if (!var0.equals("RENDER") && !var0.equals("WORLD")) {
         try {
            return valueOf(var0);
         } catch (IllegalArgumentException var2) {
            return F;
         }
      } else {
         return F;
      }
   }
}
