package com.larpclient;

import com.larpclient.module.AmbientParticlesModule;
import com.larpclient.module.AnimationsModule;
import com.larpclient.module.AstralWeaponTrailsModule;
import com.larpclient.module.AuraModule;
import com.larpclient.module.AutoAnchorModule;
import com.larpclient.module.AutoArmorModule;
import com.larpclient.module.AutoCartModule;
import com.larpclient.module.AutoClickerModule;
import com.larpclient.module.AutoCrystalModule;
import com.larpclient.module.AutoGGModule;
import com.larpclient.module.AutoPearlCatchModule;
import com.larpclient.module.AutoSellModule;
import com.larpclient.module.BlockOverlayModule;
import com.larpclient.module.CelestialWeatherModule;
import com.larpclient.module.ChamsModule;
import com.larpclient.module.ChinaHatModule;
import com.larpclient.module.CustomCrosshairModule;
import com.larpclient.module.CustomSkyModule;
import com.larpclient.module.DamageMarkersModule;
import com.larpclient.module.ESPModule;
import com.larpclient.module.EchoHandModule;
import com.larpclient.module.FakeSpawnerModule;
import com.larpclient.module.FlameHandsModule;
import com.larpclient.module.FogModule;
import com.larpclient.module.GUIAnimationModule;
import com.larpclient.module.HUDModule;
import com.larpclient.module.HandChamsModule;
import com.larpclient.module.HeadlessKorbloxModule;
import com.larpclient.module.HitFXModule;
import com.larpclient.module.HitSoundsModule;
import com.larpclient.module.HolesESPModule;
import com.larpclient.module.HomeMacroModule;
import com.larpclient.module.ItemESPModule;
import com.larpclient.module.JumpFXModule;
import com.larpclient.module.KillFXModule;
import com.larpclient.module.LightDebugModule;
import com.larpclient.module.Module;
import com.larpclient.module.MotionBlurModule;
import com.larpclient.module.MotionScannerModule;
import com.larpclient.module.NametagsModule;
import com.larpclient.module.OrderDropperModule;
import com.larpclient.module.PearlParticlesModule;
import com.larpclient.module.PerformanceModule;
import com.larpclient.module.PlayerModelModule;
import com.larpclient.module.PlayerTrailModule;
import com.larpclient.module.RemovalsModule;
import com.larpclient.module.RodSwapModule;
import com.larpclient.module.ScaffoldModule;
import com.larpclient.module.SelfVisualsModule;
import com.larpclient.module.SkyShaderModule;
import com.larpclient.module.StorageESPModule;
import com.larpclient.module.SwordModelsModule;
import com.larpclient.module.TargetAuraModule;
import com.larpclient.module.TracersModule;
import com.larpclient.module.ViewModelModule;
import com.larpclient.module.WingsModule;
import com.larpclient.module.WorldColorModule;
import com.larpclient.module.XRayModule;
import com.larpclient.module.ZoomModule;
import com.larpclient.module.z8;
import com.larpclient.module.zu;
import java.util.ArrayList;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Set;

public final class fl {
   public static List<fl.u> T(Module var0) {
      if (var0 instanceof ChooseASkyEffectAndAdjustItsAppearanceModule var6) {
         ArrayList var7 = new ArrayList();
         var7.add(new fl.u("Sky", List.of(var6.s())));
         if (var6.H() instanceof CustomSkyModule var80) {
            var7.addAll(q(var80));
         } else if (var6.H() instanceof SkyShaderModule var85) {
            var7.addAll(x(var85));
         }

         var7.add(fl.u.advanced("Existing keybinds", var6.B()));
         return List.copyOf(var7);
      } else {
         ArrayList var1 = new ArrayList();
         Set var2 = Collections.newSetFromMap(new IdentityHashMap());
         if (var0.keybindMode() != null) {
            var2.add(var0.keybindMode());
         }

         if (var0 instanceof AutoClickerModule var3) {
            var1.add(new fl.u("Click controls", var3.O()));
            var1.add(new fl.u("Timing", var3.v()));
            var1.add(new fl.u("Targets and mining", var3.L()));
         }

         if (var0 instanceof com.larpclient.module.r var8) {
            var2.addAll(var8.A());
         }

         if (var0 instanceof RodSwapModule var9) {
            var1.add(new fl.u("Rod controls", var9.f()));
            var1.add(new fl.u("Range and timing", var9.y()));
         }

         if (var0 instanceof ZoomModule var10) {
            var1.add(new fl.u("Zoom controls", var10.B()));
            var1.add(new fl.u("View", var10.r()));
         }

         if (var0 instanceof RemovalsModule var11) {
            var1.add(new fl.u("Effects", var11.C(), true));
            var1.add(new fl.u("Sounds", var11.V(), true));
         }

         if (var0 instanceof AutoCrystalModule var12) {
            var1.add(new fl.u("Actions", var12.s()));
            var1.add(new fl.u("Obsidian bases", var12.r()));
            var1.add(new fl.u("Targets", var12.f()));
            var1.add(new fl.u("Aim", var12.b()));
            var1.add(new fl.u("Damage and pauses", var12.m()));
         }

         if (var0 instanceof AutoAnchorModule var13) {
            var1.add(new fl.u("Actions", var13.b()));
            var1.add(new fl.u("Targets", var13.m()));
            var1.add(new fl.u("Aim", var13.h()));
            var1.add(new fl.u("Damage and pauses", var13.C()));
         }

         if (var0 instanceof AutoCartModule var14) {
            var1.add(new fl.u("Aim", var14.f()));
            var1.add(new fl.u("Shot", var14.W()));
            var1.add(new fl.u("Targets", var14.N()));
            var1.add(new fl.u("Damage", var14.M()));
         }

         if (var0 instanceof AutoPearlCatchModule var15) {
            var1.add(new fl.u("Catch", var15.t()));
            var1.add(new fl.u("Aim", var15.x()));
         }

         if (var0 instanceof PerformanceModule var16) {
            var1.add(new fl.u("Render quality", var16.m()));
            var1.add(new fl.u("Chunks and frame rate", var16.Z()));
            var1.add(new fl.u("Aggressive", var16.O()));
         }

         if (var0 instanceof AutoArmorModule var17) {
            var1.add(new fl.u("Armor materials", var17.b()));
         }

         if (var0 instanceof AuraModule var18) {
            var1.add(new fl.u("Rotation", var18.t()));
            var1.add(new fl.u("Attack", var18.x()));
            var1.add(new fl.u("Targets", var18.M(), true));
            var1.add(new fl.u("Selection", var18.u()));
            var1.add(new fl.u("Utils", var18.m(), true));
            var1.add(new fl.u("No Attack When", var18.n(), true));
            var1.add(new fl.u("Indicators", var18.y()));
         }

         if (var0 instanceof HitSoundsModule var19) {
            var1.add(new fl.u("Custom sound", var19.S()));
            var1.add(new fl.u("Behavior", var19.Y()));
         }

         if (var0 instanceof HitFXModule var20) {
            var1.add(new fl.u("Impact style", var20.e()));
            var1.add(new fl.u("Hit rings", var20.r()));
            var1.add(new fl.u("Colors", var20.H()));
            var1.add(fl.u.advanced("Advanced", var20.a()));
         }

         if (var0 instanceof KillFXModule var21) {
            var1.add(new fl.u("Finisher", var21.t()));
            var1.add(new fl.u("Feedback", var21.H()));
            var1.add(fl.u.advanced("Distance", var21.x()));
         }

         if (var0 instanceof CustomSkyModule var22) {
            var1.add(new fl.u("Sky", List.of(var22.r())));
            var1.addAll(q(var22));
         }

         if (var0 instanceof SkyShaderModule var23) {
            var1.add(new fl.u("Sky", List.of(var23.i())));
            var1.addAll(x(var23));
         }

         if (var0 instanceof WorldColorModule var24) {
            var1.add(new fl.u("World ambience", List.of(var24.P, var24.k)));
            var1.add(new fl.u("Color grading", List.of(var24.t, var24.S, var24.g)));
            var1.add(fl.u.advanced("Fine grading", List.of(var24.y, var24.w, var24.T, var24.Y, var24.j)));
         }

         if (var0 instanceof MotionBlurModule var25) {
            var1.add(new fl.u("Motion blur", List.of(var25.s, var25.h)));
            var1.add(fl.u.advanced("Streaks", List.of(var25.p, var25.N)));
         }

         if (var0 instanceof AmbientParticlesModule var26) {
            var1.add(new fl.u("World particles", List.of(var26.t, var26.m, var26.o, var26.W, var26.R, var26.e)));
            var1.add(new fl.u("Floating motion", List.of(var26.A, var26.k, var26.c, var26.l)));
            var1.add(new fl.u("Flying comets", List.of(var26.h, var26.v, var26.f)));
            var1.add(new fl.u("Appearance", List.of(var26.p, var26.V, var26.I, var26.g, var26.z)));
            var1.add(fl.u.advanced("Particle limit", List.of(var26.b)));
         }

         if (var0 instanceof WingsModule var27) {
            var1.add(new fl.u("Wearers", var27.r(), true));
            var1.add(new fl.u("Appearance", var27.A()));
         }

         if (var0 instanceof HeadlessKorbloxModule var28) {
            var1.add(new fl.u("Wearers", List.of(var28.w, var28.u), true));
            var1.add(new fl.u("Headless head", List.of(var28.N, var28.x)));
            var1.add(new fl.u("Korblox leg", List.of(var28.z, var28.c, var28.i, var28.G)));
            var1.add(new fl.u("Spike colors", List.of(var28.s, var28.W, var28.H)));
         }

         if (var0 instanceof ChinaHatModule var29) {
            var1.add(new fl.u("Hats", var29.L(), true));
            var1.add(new fl.u("Wearers", var29.E(), true));
            var1.add(new fl.u("Appearance", var29.a()));
            var1.add(fl.u.advanced("Shape", var29.o()));
         }

         if (var0 instanceof PlayerTrailModule var30) {
            var1.add(new fl.u("Trail", var30.b()));
            var1.add(new fl.u("Appearance", var30.W()));
            var1.add(new fl.u("Motion", var30.y()));
            var1.add(fl.u.advanced("History and limits", var30.u()));
         }

         if (var0 instanceof AstralWeaponTrailsModule var31) {
            var1.add(new fl.u("Effect", var31.p()));
            var1.add(new fl.u("Appearance", var31.l()));
            var1.add(fl.u.advanced("Advanced", var31.K()));
         }

         if (var0 instanceof CelestialWeatherModule var32) {
            var1.add(new fl.u("Effect", var32.T()));
            var1.add(new fl.u("Appearance", var32.l()));
            var1.add(fl.u.advanced("Advanced", var32.t()));
         }

         if (var0 instanceof FogModule var33) {
            var1.add(new fl.u("Distance", var33.f()));
            var1.add(new fl.u("Atmosphere", var33.z()));
            var1.add(new fl.u("Sky", var33.t()));
            var1.add(fl.u.advanced("Where", var33.C()));
         }

         if (var0 instanceof SelfVisualsModule var34) {
            var1.add(new fl.u("Body effect", var34.I()));
            var1.add(new fl.u("Appearance", var34.i()));
            var1.add(new fl.u("Motion", var34.N()));
            var1.add(fl.u.advanced("Refraction", var34.h()));
         }

         if (var0 instanceof JumpFXModule var35) {
            var1.add(new fl.u("Jump effect", var35.l()));
            var1.add(new fl.u("Shape and timing", var35.i()));
            var1.add(new fl.u("Appearance", var35.d()));
         }

         if (var0 instanceof TargetAuraModule var36) {
            var1.add(new fl.u("Target effect", var36.y()));
            var1.add(new fl.u("Colors", var36.l()));
         }

         if (var0 instanceof CustomCrosshairModule var37) {
            var1.add(new fl.u("Shape", var37.W()));
            var1.add(new fl.u("Appearance", var37.H()));
         }

         if (var0 instanceof EchoHandModule var38) {
            var1.add(new fl.u("Hand effect", var38.w()));
            var1.add(new fl.u("Appearance", var38.U()));
            var1.add(fl.u.advanced("History and motion", var38.F()));
         }

         if (var0 instanceof FlameHandsModule var39) {
            var1.add(new fl.u("Hand flames", var39.F()));
            var1.add(new fl.u("Colors", var39.n()));
            var1.add(new fl.u("Motion", var39.R()));
         }

         if (var0 instanceof PearlParticlesModule var40) {
            var1.add(new fl.u("Pearl trail", var40.B()));
            var1.add(new fl.u("Colors", var40.v()));
            var1.add(fl.u.advanced("Drift and fade", var40.T()));
         }

         if (var0 instanceof DamageMarkersModule var41) {
            var1.add(new fl.u("Damage sources", var41.e(), true));
            var1.add(new fl.u("Labels", var41.z()));
         }

         if (var0 instanceof PlayerModelModule var42) {
            var1.add(new fl.u("Character", var42.f()));
            var1.add(fl.u.advanced("Diagnostics", var42.g()));
         }

         if (var0 instanceof GUIAnimationModule var43) {
            var1.add(new fl.u("Interface", var43.a(), true));
            var1.add(new fl.u("Camera", var43.p(), true));
            var1.add(new fl.u("World and timing", var43.H()));
         }

         if (var0 instanceof ChamsModule var44) {
            var1.add(new fl.u("Entities", var44.F(), true));
            var1.add(new fl.u("Colors", var44.d()));
            var1.add(new fl.u("Outline and fill", var44.h.c()));
            var1.add(new fl.u("Glow", var44.h.k()));
            var1.add(new fl.u("Gradient motion", var44.h.m()));
            var1.add(new fl.u("Occlusion", var44.X()));
            var1.add(fl.u.advanced("Distance and limits", var44.p()));
         }

         if (var0 instanceof HandChamsModule var45) {
            var1.add(new fl.u("Colors", var45.C.g()));
            var1.add(new fl.u("Outline and fill", var45.C.c()));
            var1.add(new fl.u("Glow", var45.C.k()));
            var1.add(new fl.u("Gradient motion", var45.C.m()));
         }

         if (var0 instanceof ESPModule var46) {
            var1.add(new fl.u("Entities", var46.w(), true));
            var1.add(new fl.u("Highlight", var46.O()));
            var1.add(new fl.u("Colors", var46.o()));
            var1.add(new fl.u("Stroke", var46.D()));
            var1.add(new fl.u("Health", var46.c()));
            var1.add(fl.u.advanced("Limits and alerts", var46.K()));
         }

         if (var0 instanceof ItemESPModule var47) {
            var1.add(new fl.u("Drops", var47.U()));
            var1.add(new fl.u("Appearance", var47.D()));
            var1.add(fl.u.advanced("Item limit", var47.d()));
         }

         if (var0 instanceof TracersModule var48) {
            var1.add(new fl.u("Entities", var48.L(), true));
            var1.add(new fl.u("Lines", var48.O()));
            var1.add(fl.u.advanced("Entity limit", var48.o()));
         }

         if (var0 instanceof NametagsModule var49) {
            var1.add(new fl.u("Entities", var49.n(), true));
            var1.add(new fl.u("Label content", var49.A(), true));
            var1.add(new fl.u("Placement", var49.q()));
         }

         if (var0 instanceof XRayModule var50) {
            var1.add(new fl.u("Ore view", var50.j()));
            var1.add(new fl.u("Show", var50.E(), true));
            var1.add(new fl.u("Markers", var50.g()));
            var1.add(fl.u.advanced("Marker limit", var50.G()));
         }

         if (var0 instanceof CrystalChamsModule var51) {
            var1.add(new fl.u("Crystal", var51.e()));
            var1.add(new fl.u("Appearance", var51.L()));
            var1.add(new fl.u("Motion", var51.v()));
         }

         if (var0 instanceof PetsModule var52) {
            var1.add(new fl.u("Companion", var52.k()));
            var1.add(new fl.u("Follow", var52.G()));
            var1.add(new fl.u("Finish", var52.D()));
            var1.add(new fl.u("Glow", var52.q()));
         }

         if (var0 instanceof CryptoPetsModule var53) {
            var1.add(new fl.u("Coins", var53.V(), true));
            var1.add(new fl.u("Ring", var53.w()));
         }

         if (var0 instanceof RealisticWaterModule var54) {
            var1.add(new fl.u("Surface", List.of(var54.W, var54.Y, var54.G, var54.E, var54.V)));
            var1.add(new fl.u("Water view", List.of(var54.f, var54.M)));
         }

         if (var0 instanceof WavingFoliageModule var55) {
            var1.add(new fl.u("Foliage", List.of(var55.S, var55.K, var55.v), true));
            var1.add(new fl.u("Wind", List.of(var55.z, var55.Q, var55.G)));
         }

         if (var0 instanceof WetGroundModule var56) {
            var1.add(new fl.u("Puddles", List.of(var56.b, var56.f, var56.r, var56.q, var56.n)));
            var1.add(new fl.u("Rain", List.of(var56.m, var56.A), true));
         }

         if (var0 instanceof SnowGroundModule var57) {
            var1.add(new fl.u("Ground", List.of(var57.K, var57.G, var57.l, var57.Q)));
            var1.add(new fl.u("Snowfall", List.of(var57.E, var57.q, var57.H, var57.r)));
            var1.add(new fl.u("Footsteps", List.of(var57.v, var57.p)));
         }

         if (var0 instanceof BlockOverlayModule var58) {
            var1.add(new fl.u("Appearance", var58.l()));
            var1.add(new fl.u("Animation", var58.o()));
         }

         if (var0 instanceof z8 var59) {
            var1.add(new fl.u("Detection", var59.detectionSettings()));
            if (var59 instanceof zu var4) {
               var1.add(new fl.u("Diagnostics", var4.l()));
            }

            var1.add(new fl.u("Scanning", var59.S()));
            var1.add(new fl.u("Markers", var59.markerSettings()));
            var1.add(new fl.u("Alerts", var59.j()));
         }

         if (var0 instanceof AutoSellModule var60) {
            var1.add(new fl.u("Items", var60.d()));
            var1.add(new fl.u("Sell menu", var60.t()));
            var1.add(new fl.u("Timing", var60.J()));
            var1.add(new fl.u("Feedback", var60.m()));
         }

         if (var0 instanceof MotionScannerModule var61) {
            var1.add(new fl.u("Detection", var61.K()));
            var1.add(new fl.u("Alerts", var61.w()));
            var1.add(new fl.u("Appearance", var61.u()));
         }

         if (var0 instanceof HomeMacroModule var62) {
            var1.add(new fl.u("Home replacement", var62.p()));
         }

         if (var0 instanceof tc var63) {
            for (tc.v var5 : var63.settingGroups()) {
               var1.add(var5.advanced() ? fl.u.advanced(var5.title(), var5.settings()) : new fl.u(var5.title(), var5.settings()));
            }
         }

         if (var0 instanceof AutoGGModule var64) {
            var1.add(new fl.u("Messages", var64.J()));
            var1.add(new fl.u("Timing", var64.j()));
         }

         if (var0 instanceof WaypointsModule var65) {
            var1.add(new fl.u("Display", var65.s()));
            var1.add(new fl.u("Shortcut", var65.v()));
         }

         if (var0 instanceof OrderDropperModule var66) {
            var1.add(new fl.u("Orders", var66.j()));
            var1.add(new fl.u("Shulkers", var66.E()));
            var1.add(new fl.u("Timing and safety", var66.v()));
         }

         if (var0 instanceof LightDebugModule var67) {
            var1.add(new fl.u("Scanning", var67.b()));
            var1.add(new fl.u("Appearance", var67.V()));
         }

         if (var0 instanceof HolesESPModule var68) {
            var1.add(new fl.u("Passage colors", var68.i()));
            var1.add(new fl.u("Rendering", var68.e()));
         }

         if (var0 instanceof StorageESPModule var69) {
            var1.add(new fl.u("Storage types", var69.X(), true));
            var1.add(new fl.u("Highlights", var69.S()));
            var1.add(new fl.u("Appearance", var69.b()));
            var1.add(fl.u.advanced("Distance and limits", var69.V()));
         }

         if (var0 instanceof HUDModule var79) {
            return List.of(new fl.u("Widgets", var79.l(), true), new fl.u("Panels", var79.U()));
         } else {
            if (var0 instanceof AnimationsModule var70) {
               var1.add(new fl.u("Swing", var70.O()));
               var1.add(fl.u.advanced("Fine tuning", var70.T()));
            }

            if (var0 instanceof ViewModelModule var71) {
               var1.add(new fl.u("Swing", var71.x()));
               var1.add(new fl.u("Main hand", var71.E()));
               var1.add(new fl.u("Offhand", var71.a()));
            }

            if (var0 instanceof SwordModelsModule var72) {
               var1.add(new fl.u("Appearance", List.of(var72.e, var72.g, var72.Y)));
               var1.add(new fl.u("Inspect", List.of(var72.r, var72.c, var72.U)));
               var1.add(fl.u.advanced("Hand placement", List.of(var72.R, var72.N, var72.W, var72.O)));
            }

            if (var0 instanceof FakeSpawnerModule var73) {
               var1.add(new fl.u("Backing blocks", var73.W()));
               var1.add(new fl.u("Appearance", var73.H()));
               var1.add(new fl.u("Loot worth", var73.p()));
            }

            if (var0 instanceof com.larpclient.module.g var74) {
               var1.add(new fl.u("Trap", var74.F()));
               var1.add(fl.u.advanced("Advanced", var74.R()));
            }

            if (var0 instanceof ScaffoldModule var75) {
               var1.add(new fl.u("Bridge", var75.o()));
            }

            if (var0 instanceof uu.m var76) {
               var1.add(
                  var0 instanceof com.larpclient.module.g
                     ? fl.u.advanced("Highlight", var76.placementHighlight().l())
                     : new fl.u("Highlight", var76.placementHighlight().l())
               );
            }

            for (fl.u var82 : var1) {
               var2.addAll(var82.settings());
            }

            List var78 = var0.getSettings().stream().filter(var1x -> !var2.contains(var1x)).toList();
            if (!var78.isEmpty()) {
               String var83 = var1.isEmpty() ? "Settings" : (var0 instanceof CustomSkyModule ? "General" : "Defaults");
               if (var0 instanceof zu || var0 instanceof StorageESPModule) {
                  var83 = "Appearance";
               }

               var1.add(new fl.u(var83, var78));
            }

            if (var0 instanceof com.larpclient.module.r var84) {
               var1.add(fl.u.advanced("Shortcuts", var84.A()));
            }

            return List.copyOf(var1);
         }
      }
   }

   private static List<fl.u> q(CustomSkyModule var0) {
      return List.of(
         new fl.u("Appearance", var0.h()), new fl.u("Motion", var0.s()), new fl.u("Atmosphere", var0.y()), fl.u.advanced("Detail and placement", var0.C())
      );
   }

   private static List<fl.u> x(SkyShaderModule var0) {
      return List.of(new fl.u("Appearance", var0.q()), new fl.u("Motion and pattern", var0.D()));
   }

   public record u(String title, List<com.larpclient.module.lb<?>> settings, boolean chips, boolean collapsed) {
      public u(String var1, List<com.larpclient.module.lb<?>> var2) {
         this(var1, var2, false, false);
      }

      public u(String var1, List<com.larpclient.module.lb<?>> var2, boolean var3) {
         this(var1, var2, var3, false);
      }

      public static fl.u advanced(String var0, List<com.larpclient.module.lb<?>> var1) {
         return new fl.u(var0, var1, false, true);
      }
   }
}
