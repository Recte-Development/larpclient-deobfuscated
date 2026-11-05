package com.larpclient;

import com.larpclient.module.FakePayModule;
import com.larpclient.module.FakeSpawnerModule;
import com.larpclient.module.Module;
import com.larpclient.module.OrderDropperModule;
import com.larpclient.module.PerformanceModule;
import com.larpclient.module.StorageESPModule;
import com.larpclient.module.StreamProofModule;
import com.larpclient.module.az;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

public enum om {
   L("Favorites", "star"),
   F("Combat", "swords"),
   e("Movement", "footprints"),
   T("Player", "user"),
   P("Visuals", "eye"),
   R("Cosmetics", "paintbrush"),
   O("Base Finding", "search"),
   G("Misc", "list"),
   z("DonutSmp", "store");

   public final String D;
   public final String u;
   private static final Set<String> b = Set.of("AutoArmor", "AutoGap", "Fake Lag");
   private static final Set<String> S = Set.of("IgnoreEntity");
   private static final Set<String> o = Set.of("Freecam", "SmoothCamera", "TargetHUD", "CustomCrosshair", "Damage Markers", "Stretched Res");
   private static final Set<String> g = Set.of("Light Debug", "Motion Scanner", "Holes ESP");
   private static final Set<String> l = Set.of(
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

   om(String var3, String var4) {
      this.D = var3;
      this.u = var4;
   }

   public boolean m(om var1, Runnable var2) {
      if (this == var1) {
         return false;
      }

      var2.run();
      return true;
   }

   public static om d(Module var0) {
      if (var0.getName().equals("LARP") || var0.getName().equals("Name Changer") || var0 instanceof PerformanceModule || var0 instanceof StreamProofModule) {
         return G;
      }

      if (b.contains(var0.getName())) {
         return F;
      }

      if (S.contains(var0.getName())) {
         return T;
      }

      if (o.contains(var0.getName())) {
         return P;
      }

      if (l.contains(var0.getName())) {
         return R;
      }

      if (g.contains(var0.getName()) || var0 instanceof az || var0 instanceof StorageESPModule) {
         return O;
      }

      if (!(var0 instanceof com.larpclient.module.v)
         && !(var0 instanceof FakeSpawnerModule)
         && !(var0 instanceof FakePayModule)
         && !(var0 instanceof OrderDropperModule)) {
         return switch (var0.getCategory()) {
            case COMBAT -> F;
            case MOVEMENT -> e;
            case PLAYER -> T;
            case RENDER, VISUALS -> P;
         };
      } else {
         return z;
      }
   }

   public static String i(Module var0) {
      return var0 instanceof com.larpclient.module.v ? "DonutSmp Gambling Rigs" : "";
   }

   public boolean y(String var1) {
      return var1.isBlank() && this != L;
   }

   public boolean U(Module var1, String var2, xv var3) {
      if (!var2.isBlank()) {
         return py.N(var1, var2) != Integer.MAX_VALUE;
      } else {
         return this == L ? var3.J(var1.getName()) : d(var1) == this;
      }
   }

   public List<Module> k(List<Module> var1, String var2, xv var3) {
      return ChooseASkyEffectAndAdjustItsAppearanceModule.C(var1, var3)
         .stream()
         .filter(var3x -> this.U(var3x, var2, var3))
         .sorted(
            Comparator.<Module, String>comparing(var2x -> this.y(var2) ? i(var2x) : "")
               .thenComparing(var1x -> !var3.J(var1x.getName()))
               .thenComparing(Module::getDisplayName, String.CASE_INSENSITIVE_ORDER)
               .thenComparing(Module::getName)
         )
         .toList();
   }

   public static om D(String var0) {
      if (var0 == null) {
         return P;
      }

      if (!var0.equals("RENDER") && !var0.equals("WORLD")) {
         try {
            return valueOf(var0);
         } catch (IllegalArgumentException var2) {
            return P;
         }
      } else {
         return P;
      }
   }
}
