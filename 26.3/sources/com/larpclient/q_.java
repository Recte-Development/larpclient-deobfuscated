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
import com.larpclient.module.ke;
import com.larpclient.module.kj;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

public enum q_ {
   N("Favorites", "star"),
   B("Combat", "swords"),
   g("Movement", "footprints"),
   S("Player", "user"),
   n("Visuals", "eye"),
   T("Cosmetics", "paintbrush"),
   V("Base Finding", "search"),
   h("Misc", "list"),
   m("DonutSmp", "store");

   public final String f;
   public final String F;
   private static final Set<String> W = Set.of("AutoArmor", "AutoGap", "Fake Lag");
   private static final Set<String> A = Set.of("IgnoreEntity");
   private static final Set<String> Q = Set.of("Freecam", "SmoothCamera", "TargetHUD", "CustomCrosshair", "Damage Markers", "Stretched Res");
   private static final Set<String> l = Set.of("Light Debug", "Motion Scanner", "Holes ESP");
   private static final Set<String> I = Set.of(
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

   q_(String var3, String var4) {
      this.f = var3;
      this.F = var4;
   }

   public static List<q_> H() {
      return com.larpclient.module.fj.r() ? List.of(N, n, T, h) : List.of(values());
   }

   public boolean A(q_ var1, Runnable var2) {
      if (this == var1) {
         return false;
      }

      var2.run();
      return true;
   }

   public static q_ U(Module var0) {
      if (var0.getName().equals("LARP")
         || var0.getName().equals("Name Changer")
         || var0.getName().equals("Fake FPS")
         || var0 instanceof PerformanceModule
         || var0 instanceof StreamProofModule) {
         return h;
      }

      if (W.contains(var0.getName())) {
         return B;
      }

      if (A.contains(var0.getName())) {
         return S;
      }

      if (Q.contains(var0.getName())) {
         return n;
      }

      if (I.contains(var0.getName())) {
         return T;
      }

      if (l.contains(var0.getName()) || var0 instanceof ke || var0 instanceof StorageESPModule) {
         return V;
      }

      if (var0 instanceof kj || var0 instanceof FakeSpawnerModule || var0 instanceof FakePayModule || var0 instanceof OrderDropperModule) {
         return m;
      }

      if (var0 instanceof ac || var0 instanceof FakeScoreboardModule) {
         return m;
      }

      if (var0 instanceof FakePlayerModule) {
         return h;
      }

      return switch (var0.getCategory()) {
         case COMBAT -> B;
         case MOVEMENT -> g;
         case PLAYER -> S;
         case RENDER, VISUALS -> n;
      };
   }

   public static String C(Module var0) {
      if (var0 instanceof ac) {
         return "DonutSmp Bots";
      } else {
         return var0 instanceof kj ? "DonutSmp Gambling Rigs" : "";
      }
   }

   public boolean D(String var1) {
      return var1.isBlank() && this != N;
   }

   public boolean U(Module var1, String var2, i3 var3) {
      if (!var2.isBlank()) {
         return q.E(var1, var2) != Integer.MAX_VALUE;
      } else {
         return this == N ? var3.m(var1.getName()) : U(var1) == this;
      }
   }

   public List<Module> l(List<Module> var1, String var2, i3 var3) {
      return ChooseASkyEffectAndAdjustItsAppearanceModule.Z(var1, var3)
         .stream()
         .filter(var3x -> this.U(var3x, var2, var3))
         .sorted(
            Comparator.<Module, String>comparing(var2x -> this.D(var2) ? C(var2x) : "")
               .thenComparing(var1x -> !var3.m(var1x.getName()))
               .thenComparing(Module::getDisplayName, String.CASE_INSENSITIVE_ORDER)
               .thenComparing(Module::getName)
         )
         .toList();
   }

   public static q_ j(String var0) {
      if (var0 == null) {
         return n;
      }

      if (!var0.equals("RENDER") && !var0.equals("WORLD")) {
         try {
            return valueOf(var0);
         } catch (IllegalArgumentException var2) {
            return n;
         }
      } else {
         return n;
      }
   }
}
