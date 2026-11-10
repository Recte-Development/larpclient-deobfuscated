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
import java.util.ArrayList;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Set;

public final class pa {
   public static List<pa.o> I(Module var0) {
      if (var0 instanceof ChooseASkyEffectAndAdjustItsAppearanceModule var6) {
         ArrayList var7 = new ArrayList();
         var7.add(new pa.o("Sky", List.of(var6.a())));
         if (var6.Y() instanceof CustomSkyModule var80) {
            var7.addAll(J(var80));
         } else if (var6.Y() instanceof SkyShaderModule var85) {
            var7.addAll(p(var85));
         }

         var7.add(pa.o.advanced("Existing keybinds", var6.U()));
         return List.copyOf(var7);
      } else {
         ArrayList var1 = new ArrayList();
         Set var2 = Collections.newSetFromMap(new IdentityHashMap());
         if (var0.keybindMode() != null) {
            var2.add(var0.keybindMode());
         }

         if (var0 instanceof AutoClickerModule var3) {
            var1.add(new pa.o("Click controls", var3.m()));
            var1.add(new pa.o("Timing", var3.Q()));
            var1.add(new pa.o("Targets and mining", var3.Z()));
         }

         if (var0 instanceof com.larpclient.module.to var8) {
            var2.addAll(var8.e());
         }

         if (var0 instanceof RodSwapModule var9) {
            var1.add(new pa.o("Rod controls", var9.D()));
            var1.add(new pa.o("Range and timing", var9.Q()));
         }

         if (var0 instanceof ZoomModule var10) {
            var1.add(new pa.o("Zoom controls", var10.Q()));
            var1.add(new pa.o("View", var10.r()));
         }

         if (var0 instanceof RemovalsModule var11) {
            var1.add(new pa.o("Effects", var11.Y(), true));
            var1.add(new pa.o("Sounds", var11.b(), true));
         }

         if (var0 instanceof AutoCrystalModule var12) {
            var1.add(new pa.o("Actions", var12.r()));
            var1.add(new pa.o("Obsidian bases", var12.l()));
            var1.add(new pa.o("Targets", var12.C()));
            var1.add(new pa.o("Aim", var12.n()));
            var1.add(new pa.o("Damage and pauses", var12.v()));
         }

         if (var0 instanceof AutoAnchorModule var13) {
            var1.add(new pa.o("Actions", var13.Z()));
            var1.add(new pa.o("Targets", var13.A()));
            var1.add(new pa.o("Aim", var13.Q()));
            var1.add(new pa.o("Damage and pauses", var13.W()));
         }

         if (var0 instanceof AutoCartModule var14) {
            var1.add(new pa.o("Aim", var14.y()));
            var1.add(new pa.o("Shot", var14.K()));
            var1.add(new pa.o("Targets", var14.f()));
            var1.add(new pa.o("Damage", var14.b()));
         }

         if (var0 instanceof AutoPearlCatchModule var15) {
            var1.add(new pa.o("Catch", var15.a()));
            var1.add(new pa.o("Aim", var15.l()));
         }

         if (var0 instanceof PerformanceModule var16) {
            var1.add(new pa.o("Render quality", var16.A()));
            var1.add(new pa.o("Chunks and frame rate", var16.j()));
            var1.add(new pa.o("Aggressive", var16.Y()));
         }

         if (var0 instanceof AutoArmorModule var17) {
            var1.add(new pa.o("Armor materials", var17.D()));
         }

         if (var0 instanceof AuraModule var18) {
            var1.add(new pa.o("Rotation", var18.V()));
            var1.add(new pa.o("Attack", var18.t()));
            var1.add(new pa.o("Targets", var18.q(), true));
            var1.add(new pa.o("Selection", var18.u()));
            var1.add(new pa.o("Utils", var18.s(), true));
            var1.add(new pa.o("No Attack When", var18.Q(), true));
            var1.add(new pa.o("Indicators", var18.B()));
         }

         if (var0 instanceof HitSoundsModule var19) {
            var1.add(new pa.o("Custom sound", var19.A()));
            var1.add(new pa.o("Behavior", var19.I()));
         }

         if (var0 instanceof HitFXModule var20) {
            var1.add(new pa.o("Impact style", var20.a()));
            var1.add(new pa.o("Hit rings", var20.O()));
            var1.add(new pa.o("Colors", var20.R()));
            var1.add(pa.o.advanced("Advanced", var20.c()));
         }

         if (var0 instanceof KillFXModule var21) {
            var1.add(new pa.o("Finisher", var21.D()));
            var1.add(new pa.o("Feedback", var21.t()));
            var1.add(pa.o.advanced("Distance", var21.F()));
         }

         if (var0 instanceof CustomSkyModule var22) {
            var1.add(new pa.o("Sky", List.of(var22.d())));
            var1.addAll(J(var22));
         }

         if (var0 instanceof SkyShaderModule var23) {
            var1.add(new pa.o("Sky", List.of(var23.S())));
            var1.addAll(p(var23));
         }

         if (var0 instanceof WorldColorModule var24) {
            var1.add(new pa.o("World ambience", List.of(var24.N, var24.T)));
            var1.add(new pa.o("Color grading", List.of(var24.I, var24.l, var24.J)));
            var1.add(pa.o.advanced("Fine grading", List.of(var24.e, var24.u, var24.C, var24.c, var24.V)));
         }

         if (var0 instanceof MotionBlurModule var25) {
            var1.add(new pa.o("Motion blur", List.of(var25.g, var25.N)));
            var1.add(pa.o.advanced("Streaks", List.of(var25.i, var25.q)));
         }

         if (var0 instanceof AmbientParticlesModule var26) {
            var1.add(new pa.o("World particles", List.of(var26.T, var26.l, var26.n, var26.m, var26.G, var26.y)));
            var1.add(new pa.o("Floating motion", List.of(var26.I, var26.S, var26.A, var26.z)));
            var1.add(new pa.o("Flying comets", List.of(var26.b, var26.N, var26.Y)));
            var1.add(new pa.o("Appearance", List.of(var26.D, var26.E, var26.r, var26.h, var26.w)));
            var1.add(pa.o.advanced("Particle limit", List.of(var26.u)));
         }

         if (var0 instanceof WingsModule var27) {
            var1.add(new pa.o("Wearers", var27.j(), true));
            var1.add(new pa.o("Appearance", var27.N()));
         }

         if (var0 instanceof HeadlessKorbloxModule var28) {
            var1.add(new pa.o("Wearers", List.of(var28.v, var28.Z), true));
            var1.add(new pa.o("Headless head", List.of(var28.j, var28.J)));
            var1.add(new pa.o("Korblox leg", List.of(var28.K, var28.Y, var28.L, var28.r)));
            var1.add(new pa.o("Spike colors", List.of(var28.a, var28.X, var28.l)));
         }

         if (var0 instanceof ChinaHatModule var29) {
            var1.add(new pa.o("Hats", var29.B(), true));
            var1.add(new pa.o("Wearers", var29.e(), true));
            var1.add(new pa.o("Appearance", var29.q()));
            var1.add(pa.o.advanced("Shape", var29.Q()));
         }

         if (var0 instanceof PlayerTrailModule var30) {
            var1.add(new pa.o("Trail", var30.f()));
            var1.add(new pa.o("Appearance", var30.M()));
            var1.add(new pa.o("Motion", var30.T()));
            var1.add(pa.o.advanced("History and limits", var30.n()));
         }

         if (var0 instanceof AstralWeaponTrailsModule var31) {
            var1.add(new pa.o("Effect", var31.N()));
            var1.add(new pa.o("Appearance", var31.z()));
            var1.add(pa.o.advanced("Advanced", var31.d()));
         }

         if (var0 instanceof CelestialWeatherModule var32) {
            var1.add(new pa.o("Effect", var32.R()));
            var1.add(new pa.o("Appearance", var32.F()));
            var1.add(pa.o.advanced("Advanced", var32.h()));
         }

         if (var0 instanceof FogModule var33) {
            var1.add(new pa.o("Distance", var33.h()));
            var1.add(new pa.o("Atmosphere", var33.M()));
            var1.add(new pa.o("Sky", var33.m()));
            var1.add(pa.o.advanced("Where", var33.G()));
         }

         if (var0 instanceof SelfVisualsModule var34) {
            var1.add(new pa.o("Body effect", var34.b()));
            var1.add(new pa.o("Appearance", var34.p()));
            var1.add(new pa.o("Motion", var34.M()));
            var1.add(pa.o.advanced("Refraction", var34.J()));
         }

         if (var0 instanceof JumpFXModule var35) {
            var1.add(new pa.o("Jump effect", var35.K()));
            var1.add(new pa.o("Shape and timing", var35.M()));
            var1.add(new pa.o("Appearance", var35.R()));
         }

         if (var0 instanceof TargetAuraModule var36) {
            var1.add(new pa.o("Target effect", var36.P()));
            var1.add(new pa.o("Colors", var36.C()));
         }

         if (var0 instanceof CustomCrosshairModule var37) {
            var1.add(new pa.o("Shape", var37.H()));
            var1.add(new pa.o("Appearance", var37.Z()));
         }

         if (var0 instanceof EchoHandModule var38) {
            var1.add(new pa.o("Hand effect", var38.m()));
            var1.add(new pa.o("Appearance", var38.y()));
            var1.add(pa.o.advanced("History and motion", var38.j()));
         }

         if (var0 instanceof FlameHandsModule var39) {
            var1.add(new pa.o("Hand flames", var39.S()));
            var1.add(new pa.o("Colors", var39.A()));
            var1.add(new pa.o("Motion", var39.M()));
         }

         if (var0 instanceof PearlParticlesModule var40) {
            var1.add(new pa.o("Pearl trail", var40.R()));
            var1.add(new pa.o("Colors", var40.G()));
            var1.add(pa.o.advanced("Drift and fade", var40.e()));
         }

         if (var0 instanceof DamageMarkersModule var41) {
            var1.add(new pa.o("Damage sources", var41.B(), true));
            var1.add(new pa.o("Labels", var41.D()));
         }

         if (var0 instanceof PlayerModelModule var42) {
            var1.add(new pa.o("Character", var42.Y()));
            var1.add(pa.o.advanced("Diagnostics", var42.A()));
         }

         if (var0 instanceof GUIAnimationModule var43) {
            var1.add(new pa.o("Interface", var43.m(), true));
            var1.add(new pa.o("Camera", var43.w(), true));
            var1.add(new pa.o("World and timing", var43.K()));
         }

         if (var0 instanceof ChamsModule var44) {
            var1.add(new pa.o("Entities", var44.H(), true));
            var1.add(new pa.o("Colors", var44.E()));
            var1.add(new pa.o("Outline and fill", var44.E.y()));
            var1.add(new pa.o("Glow", var44.E.q()));
            var1.add(new pa.o("Gradient motion", var44.E.m()));
            var1.add(new pa.o("Occlusion", var44.Z()));
            var1.add(pa.o.advanced("Distance and limits", var44.k()));
         }

         if (var0 instanceof HandChamsModule var45) {
            var1.add(new pa.o("Colors", var45.F.f()));
            var1.add(new pa.o("Outline and fill", var45.F.y()));
            var1.add(new pa.o("Glow", var45.F.q()));
            var1.add(new pa.o("Gradient motion", var45.F.m()));
         }

         if (var0 instanceof ESPModule var46) {
            var1.add(new pa.o("Entities", var46.N(), true));
            var1.add(new pa.o("Highlight", var46.L()));
            var1.add(new pa.o("Colors", var46.D()));
            var1.add(new pa.o("Stroke", var46.J()));
            var1.add(new pa.o("Health", var46.Y()));
            var1.add(pa.o.advanced("Limits and alerts", var46.I()));
         }

         if (var0 instanceof ItemESPModule var47) {
            var1.add(new pa.o("Drops", var47.r()));
            var1.add(new pa.o("Appearance", var47.E()));
            var1.add(pa.o.advanced("Item limit", var47.o()));
         }

         if (var0 instanceof TracersModule var48) {
            var1.add(new pa.o("Entities", var48.t(), true));
            var1.add(new pa.o("Lines", var48.W()));
            var1.add(pa.o.advanced("Entity limit", var48.d()));
         }

         if (var0 instanceof NametagsModule var49) {
            var1.add(new pa.o("Entities", var49.a(), true));
            var1.add(new pa.o("Label content", var49.K(), true));
            var1.add(new pa.o("Placement", var49.y()));
         }

         if (var0 instanceof XRayModule var50) {
            var1.add(new pa.o("Ore view", var50.i()));
            var1.add(new pa.o("Show", var50.N(), true));
            var1.add(new pa.o("Markers", var50.J()));
            var1.add(pa.o.advanced("Marker limit", var50.r()));
         }

         if (var0 instanceof CrystalChamsModule var51) {
            var1.add(new pa.o("Crystal", var51.C()));
            var1.add(new pa.o("Appearance", var51.m()));
            var1.add(new pa.o("Motion", var51.X()));
         }

         if (var0 instanceof PetsModule var52) {
            var1.add(new pa.o("Companion", var52.y()));
            var1.add(new pa.o("Follow", var52.F()));
            var1.add(new pa.o("Finish", var52.r()));
            var1.add(new pa.o("Glow", var52.X()));
         }

         if (var0 instanceof CryptoPetsModule var53) {
            var1.add(new pa.o("Coins", var53.x(), true));
            var1.add(new pa.o("Ring", var53.u()));
         }

         if (var0 instanceof RealisticWaterModule var54) {
            var1.add(new pa.o("Surface", List.of(var54.t, var54.U, var54.y, var54.g, var54.M)));
            var1.add(new pa.o("Water view", List.of(var54.S, var54.k)));
         }

         if (var0 instanceof WavingFoliageModule var55) {
            var1.add(new pa.o("Foliage", List.of(var55.F, var55.z, var55.N), true));
            var1.add(new pa.o("Wind", List.of(var55.v, var55.G, var55.t)));
         }

         if (var0 instanceof WetGroundModule var56) {
            var1.add(new pa.o("Puddles", List.of(var56.q, var56.B, var56.M, var56.Q, var56.W)));
            var1.add(new pa.o("Rain", List.of(var56.R, var56.m), true));
         }

         if (var0 instanceof SnowGroundModule var57) {
            var1.add(new pa.o("Ground", List.of(var57.H, var57.t, var57.l, var57.j)));
            var1.add(new pa.o("Snowfall", List.of(var57.J, var57.M, var57.u, var57.Y)));
            var1.add(new pa.o("Footsteps", List.of(var57.W, var57.r)));
         }

         if (var0 instanceof BlockOverlayModule var58) {
            var1.add(new pa.o("Appearance", var58.o()));
            var1.add(new pa.o("Animation", var58.N()));
         }

         if (var0 instanceof com.larpclient.module.pm var59) {
            var1.add(new pa.o("Detection", var59.detectionSettings()));
            if (var59 instanceof com.larpclient.module.pi var4) {
               var1.add(new pa.o("Diagnostics", var4.Q()));
            }

            var1.add(new pa.o("Scanning", var59.h()));
            var1.add(new pa.o("Markers", var59.markerSettings()));
            var1.add(new pa.o("Alerts", var59.M()));
         }

         if (var0 instanceof AutoSellModule var60) {
            var1.add(new pa.o("Items", var60.H()));
            var1.add(new pa.o("Sell menu", var60.Z()));
            var1.add(new pa.o("Timing", var60.a()));
            var1.add(new pa.o("Feedback", var60.T()));
         }

         if (var0 instanceof MotionScannerModule var61) {
            var1.add(new pa.o("Detection", var61.C()));
            var1.add(new pa.o("Alerts", var61.N()));
            var1.add(new pa.o("Appearance", var61.w()));
         }

         if (var0 instanceof HomeMacroModule var62) {
            var1.add(new pa.o("Home replacement", var62.n()));
         }

         if (var0 instanceof s1 var63) {
            for (s1.w var5 : var63.settingGroups()) {
               var1.add(var5.advanced() ? pa.o.advanced(var5.title(), var5.settings()) : new pa.o(var5.title(), var5.settings()));
            }
         }

         if (var0 instanceof AutoGGModule var64) {
            var1.add(new pa.o("Messages", var64.o()));
            var1.add(new pa.o("Timing", var64.Q()));
         }

         if (var0 instanceof WaypointsModule var65) {
            var1.add(new pa.o("Display", var65.p()));
            var1.add(new pa.o("Shortcut", var65.Y()));
         }

         if (var0 instanceof OrderDropperModule var66) {
            var1.add(new pa.o("Orders", var66.g()));
            var1.add(new pa.o("Shulkers", var66.t()));
            var1.add(new pa.o("Timing and safety", var66.U()));
         }

         if (var0 instanceof LightDebugModule var67) {
            var1.add(new pa.o("Scanning", var67.V()));
            var1.add(new pa.o("Appearance", var67.I()));
         }

         if (var0 instanceof HolesESPModule var68) {
            var1.add(new pa.o("Passage colors", var68.l()));
            var1.add(new pa.o("Rendering", var68.G()));
         }

         if (var0 instanceof StorageESPModule var69) {
            var1.add(new pa.o("Storage types", var69.D(), true));
            var1.add(new pa.o("Highlights", var69.U()));
            var1.add(new pa.o("Appearance", var69.d()));
            var1.add(pa.o.advanced("Distance and limits", var69.o()));
         }

         if (var0 instanceof HUDModule var79) {
            return List.of(new pa.o("Widgets", var79.z(), true), new pa.o("Panels", var79.C()));
         } else {
            if (var0 instanceof AnimationsModule var70) {
               var1.add(new pa.o("Swing", var70.W()));
               var1.add(pa.o.advanced("Fine tuning", var70.h()));
            }

            if (var0 instanceof ViewModelModule var71) {
               var1.add(new pa.o("Swing", var71.i()));
               var1.add(new pa.o("Main hand", var71.X()));
               var1.add(new pa.o("Offhand", var71.u()));
            }

            if (var0 instanceof SwordModelsModule var72) {
               var1.add(new pa.o("Appearance", List.of(var72.Y, var72.l, var72.b)));
               var1.add(new pa.o("Inspect", List.of(var72.X, var72.n, var72.y)));
               var1.add(pa.o.advanced("Hand placement", List.of(var72.m, var72.c, var72.j, var72.Q)));
            }

            if (var0 instanceof FakeSpawnerModule var73) {
               var1.add(new pa.o("Backing blocks", var73.S()));
               var1.add(new pa.o("Appearance", var73.R()));
               var1.add(new pa.o("Loot worth", var73.o()));
            }

            if (var0 instanceof com.larpclient.module.tb var74) {
               var1.add(new pa.o("Trap", var74.u()));
               var1.add(pa.o.advanced("Advanced", var74.I()));
            }

            if (var0 instanceof ScaffoldModule var75) {
               var1.add(new pa.o("Bridge", var75.A()));
            }

            if (var0 instanceof mr.n var76) {
               var1.add(
                  var0 instanceof com.larpclient.module.tb
                     ? pa.o.advanced("Highlight", var76.placementHighlight().c())
                     : new pa.o("Highlight", var76.placementHighlight().c())
               );
            }

            for (pa.o var82 : var1) {
               var2.addAll(var82.settings());
            }

            List var78 = var0.getSettings().stream().filter(var1x -> !var2.contains(var1x)).toList();
            if (!var78.isEmpty()) {
               String var83 = var1.isEmpty() ? "Settings" : (var0 instanceof CustomSkyModule ? "General" : "Defaults");
               if (var0 instanceof com.larpclient.module.pi || var0 instanceof StorageESPModule) {
                  var83 = "Appearance";
               }

               var1.add(new pa.o(var83, var78));
            }

            if (var0 instanceof com.larpclient.module.to var84) {
               var1.add(pa.o.advanced("Shortcuts", var84.e()));
            }

            return List.copyOf(var1);
         }
      }
   }

   private static List<pa.o> J(CustomSkyModule var0) {
      return List.of(
         new pa.o("Appearance", var0.q()), new pa.o("Motion", var0.J()), new pa.o("Atmosphere", var0.k()), pa.o.advanced("Detail and placement", var0.h())
      );
   }

   private static List<pa.o> p(SkyShaderModule var0) {
      return List.of(new pa.o("Appearance", var0.Y()), new pa.o("Motion and pattern", var0.i()));
   }

   public record o(String title, List<com.larpclient.module.q<?>> settings, boolean chips, boolean collapsed) {
      public o(String var1, List<com.larpclient.module.q<?>> var2) {
         this(var1, var2, false, false);
      }

      public o(String var1, List<com.larpclient.module.q<?>> var2, boolean var3) {
         this(var1, var2, var3, false);
      }

      public static pa.o advanced(String var0, List<com.larpclient.module.q<?>> var1) {
         return new pa.o(var0, var1, false, true);
      }
   }
}
