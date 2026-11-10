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
import com.larpclient.module.z8;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

public enum yc {
   e("Favorites", "star"),
   m("Combat", "swords"),
   c("Movement", "footprints"),
   X("Player", "user"),
   I("Visuals", "eye"),
   A("Cosmetics", "paintbrush"),
   h("Base Finding", "search"),
   L("Misc", "list"),
   Y("DonutSmp", "store");

   public final String t;
   public final String W;
   private static final Set<String> j = Set.of("AutoArmor", "AutoGap", "Fake Lag");
   private static final Set<String> F = Set.of("IgnoreEntity");
   private static final Set<String> T = Set.of("Freecam", "SmoothCamera", "TargetHUD", "CustomCrosshair", "Damage Markers", "Stretched Res");
   private static final Set<String> y = Set.of("Light Debug", "Motion Scanner", "Prime Chunk Finder", "Holes ESP");
   private static final Set<String> d = Set.of(
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

   yc(String var3, String var4) {
      this.t = var3;
      this.W = var4;
   }

   public static List<yc> A() {
      return com.larpclient.module.lf.l() ? List.of(e, I, A, L) : List.of(values());
   }

   public boolean z(yc var1, Runnable var2) {
      if (this == var1) {
         return false;
      }

      var2.run();
      return true;
   }

   public static yc w(Module var0) {
      if (var0.getName().equals("LARP")
         || var0.getName().equals("Name Changer")
         || var0.getName().equals("Fake FPS")
         || var0 instanceof PerformanceModule
         || var0 instanceof StreamProofModule) {
         return L;
      }

      if (j.contains(var0.getName())) {
         return m;
      }

      if (F.contains(var0.getName())) {
         return X;
      }

      if (T.contains(var0.getName())) {
         return I;
      }

      if (d.contains(var0.getName())) {
         return A;
      }

      if (y.contains(var0.getName()) || var0 instanceof z8 || var0 instanceof StorageESPModule) {
         return h;
      }

      if (var0 instanceof com.larpclient.module.r || var0 instanceof FakeSpawnerModule || var0 instanceof FakePayModule || var0 instanceof OrderDropperModule) {
         return Y;
      }

      if (var0 instanceof tc || var0 instanceof FakeScoreboardModule) {
         return Y;
      }

      if (var0 instanceof FakePlayerModule) {
         return L;
      }

      return switch (var0.getCategory()) {
         case COMBAT -> m;
         case MOVEMENT -> c;
         case PLAYER -> X;
         case RENDER, VISUALS -> I;
      };
   }

   public static String q(Module var0) {
      if (var0 instanceof tc) {
         return "DonutSmp Bots";
      } else {
         return var0 instanceof com.larpclient.module.r ? "DonutSmp Gambling Rigs" : "";
      }
   }

   public boolean U(String var1) {
      return var1.isBlank() && this != e;
   }

   public boolean o(Module var1, String var2, y0 var3) {
      if (!var2.isBlank()) {
         return qi.H(var1, var2) != Integer.MAX_VALUE;
      } else {
         return this == e ? var3.H(var1.getName()) : w(var1) == this;
      }
   }

   public List<Module> T(List<Module> var1, String var2, y0 var3) {
      return ChooseASkyEffectAndAdjustItsAppearanceModule.C(var1, var3)
         .stream()
         .filter(var3x -> this.o(var3x, var2, var3))
         .sorted(
            Comparator.<Module, String>comparing(var2x -> this.U(var2) ? q(var2x) : "")
               .thenComparing(var1x -> !var3.H(var1x.getName()))
               .thenComparing(Module::getDisplayName, String.CASE_INSENSITIVE_ORDER)
               .thenComparing(Module::getName)
         )
         .toList();
   }

   public static yc p(String var0) {
      if (var0 == null) {
         return I;
      }

      if (!var0.equals("RENDER") && !var0.equals("WORLD")) {
         try {
            return valueOf(var0);
         } catch (IllegalArgumentException var2) {
            return I;
         }
      } else {
         return I;
      }
   }
}
