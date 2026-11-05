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
import com.larpclient.module.HalloweenModule;
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
import com.larpclient.module.a0;
import com.larpclient.module.az;
import com.larpclient.module.df;
import java.util.ArrayList;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Set;

public final class ok {
   public static List<ok.m> t(Module var0) {
      if (var0 instanceof ChooseASkyEffectAndAdjustItsAppearanceModule var6) {
         ArrayList var7 = new ArrayList();
         var7.add(new ok.m("Sky", List.of(var6.q())));
         if (var6.d() instanceof CustomSkyModule var78) {
            var7.addAll(o(var78));
         } else if (var6.d() instanceof SkyShaderModule var82) {
            var7.addAll(L(var82));
         }

         var7.add(ok.m.advanced("Existing keybinds", var6.V()));
         return List.copyOf(var7);
      } else {
         ArrayList var1 = new ArrayList();
         Set var2 = Collections.newSetFromMap(new IdentityHashMap());
         if (var0.keybindMode() != null) {
            var2.add(var0.keybindMode());
         }

         if (var0 instanceof AutoClickerModule var3) {
            var1.add(new ok.m("Click controls", var3.Z()));
            var1.add(new ok.m("Timing", var3.w()));
            var1.add(new ok.m("Targets and mining", var3.l()));
         }

         if (var0 instanceof com.larpclient.module.v var8) {
            var2.addAll(var8.s());
         }

         if (var0 instanceof RodSwapModule var9) {
            var1.add(new ok.m("Rod controls", var9.n()));
            var1.add(new ok.m("Range and timing", var9.H()));
         }

         if (var0 instanceof HalloweenModule var10) {
            var1.add(new ok.m("After Dark", var10.E()));
            var1.add(ok.m.advanced("Customize Effects", var10.o()));
         }

         if (var0 instanceof ZoomModule var11) {
            var1.add(new ok.m("Zoom controls", var11.U()));
            var1.add(new ok.m("View", var11.G()));
         }

         if (var0 instanceof RemovalsModule var12) {
            var1.add(new ok.m("Effects", var12.x(), true));
            var1.add(new ok.m("Sounds", var12.L(), true));
         }

         if (var0 instanceof AutoCrystalModule var13) {
            var1.add(new ok.m("Actions", var13.L()));
            var1.add(new ok.m("Obsidian bases", var13.b()));
            var1.add(new ok.m("Targets", var13.O()));
            var1.add(new ok.m("Aim", var13.Z()));
            var1.add(new ok.m("Damage and pauses", var13.I()));
         }

         if (var0 instanceof AutoAnchorModule var14) {
            var1.add(new ok.m("Actions", var14.H()));
            var1.add(new ok.m("Targets", var14.j()));
            var1.add(new ok.m("Aim", var14.k()));
            var1.add(new ok.m("Damage and pauses", var14.E()));
         }

         if (var0 instanceof AutoCartModule var15) {
            var1.add(new ok.m("Aim", var15.s()));
            var1.add(new ok.m("Shot", var15.I()));
            var1.add(new ok.m("Targets", var15.P()));
            var1.add(new ok.m("Damage", var15.k()));
         }

         if (var0 instanceof AutoPearlCatchModule var16) {
            var1.add(new ok.m("Catch", var16.V()));
            var1.add(new ok.m("Aim", var16.Q()));
         }

         if (var0 instanceof PerformanceModule var17) {
            var1.add(new ok.m("Render quality", var17.P()));
            var1.add(new ok.m("Chunks and frame rate", var17.f()));
            var1.add(new ok.m("Aggressive", var17.Y()));
         }

         if (var0 instanceof AutoArmorModule var18) {
            var1.add(new ok.m("Armor materials", var18.s()));
         }

         if (var0 instanceof AuraModule var19) {
            var1.add(new ok.m("Rotation", var19.o()));
            var1.add(new ok.m("Attack", var19.C()));
            var1.add(new ok.m("Targets", var19.H(), true));
            var1.add(new ok.m("Selection", var19.r()));
            var1.add(new ok.m("Utils", var19.x(), true));
            var1.add(new ok.m("No Attack When", var19.p(), true));
            var1.add(new ok.m("Indicators", var19.i()));
         }

         if (var0 instanceof HitSoundsModule var20) {
            var1.add(new ok.m("Custom sound", var20.p()));
            var1.add(new ok.m("Behavior", var20.k()));
         }

         if (var0 instanceof HitFXModule var21) {
            var1.add(new ok.m("Impact style", var21.S()));
            var1.add(new ok.m("Hit rings", var21.N()));
            var1.add(new ok.m("Colors", var21.o()));
            var1.add(ok.m.advanced("Advanced", var21.e()));
         }

         if (var0 instanceof KillFXModule var22) {
            var1.add(new ok.m("Finisher", var22.q()));
            var1.add(new ok.m("Feedback", var22.B()));
            var1.add(ok.m.advanced("Distance", var22.F()));
         }

         if (var0 instanceof CustomSkyModule var23) {
            var1.add(new ok.m("Sky", List.of(var23.N())));
            var1.addAll(o(var23));
         }

         if (var0 instanceof SkyShaderModule var24) {
            var1.add(new ok.m("Sky", List.of(var24.p())));
            var1.addAll(L(var24));
         }

         if (var0 instanceof WorldColorModule var25) {
            var1.add(new ok.m("World ambience", List.of(var25.B, var25.T)));
            var1.add(new ok.m("Color grading", List.of(var25.w, var25.Q, var25.S)));
            var1.add(ok.m.advanced("Fine grading", List.of(var25.A, var25.K, var25.G, var25.i, var25.D)));
         }

         if (var0 instanceof MotionBlurModule var26) {
            var1.add(new ok.m("Motion blur", List.of(var26.n, var26.K)));
            var1.add(ok.m.advanced("Streaks", List.of(var26.v, var26.V)));
         }

         if (var0 instanceof AmbientParticlesModule var27) {
            var1.add(new ok.m("World particles", List.of(var27.M, var27.N, var27.a, var27.x, var27.z, var27.b)));
            var1.add(new ok.m("Floating motion", List.of(var27.L, var27.s, var27.I, var27.d)));
            var1.add(new ok.m("Flying comets", List.of(var27.l, var27.O, var27.C)));
            var1.add(new ok.m("Appearance", List.of(var27.i, var27.o, var27.e, var27.F, var27.P)));
            var1.add(ok.m.advanced("Particle limit", List.of(var27.g)));
         }

         if (var0 instanceof WingsModule var28) {
            var1.add(new ok.m("Wearers", var28.x(), true));
            var1.add(new ok.m("Appearance", var28.o()));
         }

         if (var0 instanceof HeadlessKorbloxModule var29) {
            var1.add(new ok.m("Wearers", List.of(var29.c, var29.d), true));
            var1.add(new ok.m("Headless head", List.of(var29.Y, var29.C)));
            var1.add(new ok.m("Korblox leg", List.of(var29.H, var29.S, var29.X, var29.F)));
            var1.add(new ok.m("Spike colors", List.of(var29.J, var29.z, var29.o)));
         }

         if (var0 instanceof ChinaHatModule var30) {
            var1.add(new ok.m("Wearers", var30.D(), true));
            var1.add(new ok.m("Appearance", var30.G()));
            var1.add(ok.m.advanced("Shape", var30.a()));
         }

         if (var0 instanceof PlayerTrailModule var31) {
            var1.add(new ok.m("Trail", var31.z()));
            var1.add(new ok.m("Appearance", var31.X()));
            var1.add(new ok.m("Motion", var31.Z()));
            var1.add(ok.m.advanced("History and limits", var31.L()));
         }

         if (var0 instanceof AstralWeaponTrailsModule var32) {
            var1.add(new ok.m("Effect", var32.f()));
            var1.add(new ok.m("Appearance", var32.u()));
            var1.add(ok.m.advanced("Advanced", var32.b()));
         }

         if (var0 instanceof CelestialWeatherModule var33) {
            var1.add(new ok.m("Effect", var33.R()));
            var1.add(new ok.m("Appearance", var33.F()));
            var1.add(ok.m.advanced("Advanced", var33.j()));
         }

         if (var0 instanceof FogModule var34) {
            var1.add(new ok.m("Distance", var34.Y()));
            var1.add(new ok.m("Atmosphere", var34.l()));
            var1.add(new ok.m("Sky", var34.A()));
            var1.add(ok.m.advanced("Where", var34.Z()));
         }

         if (var0 instanceof SelfVisualsModule var35) {
            var1.add(new ok.m("Body effect", var35.o()));
            var1.add(new ok.m("Appearance", var35.R()));
            var1.add(new ok.m("Motion", var35.l()));
            var1.add(ok.m.advanced("Refraction", var35.m()));
         }

         if (var0 instanceof JumpFXModule var36) {
            var1.add(new ok.m("Jump effect", var36.Y()));
            var1.add(new ok.m("Shape and timing", var36.H()));
            var1.add(new ok.m("Appearance", var36.k()));
         }

         if (var0 instanceof TargetAuraModule var37) {
            var1.add(new ok.m("Target effect", var37.l()));
            var1.add(new ok.m("Colors", var37.V()));
         }

         if (var0 instanceof CustomCrosshairModule var38) {
            var1.add(new ok.m("Shape", var38.S()));
            var1.add(new ok.m("Appearance", var38.V()));
         }

         if (var0 instanceof EchoHandModule var39) {
            var1.add(new ok.m("Hand effect", var39.p()));
            var1.add(new ok.m("Appearance", var39.P()));
            var1.add(ok.m.advanced("History and motion", var39.W()));
         }

         if (var0 instanceof FlameHandsModule var40) {
            var1.add(new ok.m("Hand flames", var40.f()));
            var1.add(new ok.m("Colors", var40.I()));
            var1.add(new ok.m("Motion", var40.O()));
         }

         if (var0 instanceof PearlParticlesModule var41) {
            var1.add(new ok.m("Pearl trail", var41.J()));
            var1.add(new ok.m("Colors", var41.M()));
            var1.add(ok.m.advanced("Drift and fade", var41.N()));
         }

         if (var0 instanceof DamageMarkersModule var42) {
            var1.add(new ok.m("Damage sources", var42.O(), true));
            var1.add(new ok.m("Labels", var42.s()));
         }

         if (var0 instanceof PlayerModelModule var43) {
            var1.add(new ok.m("Character", var43.u()));
            var1.add(ok.m.advanced("Diagnostics", var43.T()));
         }

         if (var0 instanceof GUIAnimationModule var44) {
            var1.add(new ok.m("Interface", var44.N(), true));
            var1.add(new ok.m("Camera", var44.q(), true));
            var1.add(new ok.m("World and timing", var44.E()));
         }

         if (var0 instanceof ChamsModule var45) {
            var1.add(new ok.m("Entities", var45.X(), true));
            var1.add(new ok.m("Colors", var45.y()));
            var1.add(new ok.m("Outline and fill", var45.O.F()));
            var1.add(new ok.m("Glow", var45.O.w()));
            var1.add(new ok.m("Gradient motion", var45.O.f()));
            var1.add(new ok.m("Occlusion", var45.q()));
            var1.add(ok.m.advanced("Distance and limits", var45.s()));
         }

         if (var0 instanceof HandChamsModule var46) {
            var1.add(new ok.m("Colors", var46.i.N()));
            var1.add(new ok.m("Outline and fill", var46.i.F()));
            var1.add(new ok.m("Glow", var46.i.w()));
            var1.add(new ok.m("Gradient motion", var46.i.f()));
         }

         if (var0 instanceof ESPModule var47) {
            var1.add(new ok.m("Entities", var47.C(), true));
            var1.add(new ok.m("Highlight", var47.g()));
            var1.add(new ok.m("Colors", var47.E()));
            var1.add(new ok.m("Stroke", var47.j()));
            var1.add(new ok.m("Health", var47.n()));
            var1.add(ok.m.advanced("Limits and alerts", var47.U()));
         }

         if (var0 instanceof ItemESPModule var48) {
            var1.add(new ok.m("Drops", var48.h()));
            var1.add(new ok.m("Appearance", var48.F()));
            var1.add(ok.m.advanced("Item limit", var48.N()));
         }

         if (var0 instanceof TracersModule var49) {
            var1.add(new ok.m("Entities", var49.c(), true));
            var1.add(new ok.m("Lines", var49.U()));
            var1.add(ok.m.advanced("Entity limit", var49.J()));
         }

         if (var0 instanceof NametagsModule var50) {
            var1.add(new ok.m("Entities", var50.a(), true));
            var1.add(new ok.m("Label content", var50.z(), true));
            var1.add(new ok.m("Placement", var50.t()));
         }

         if (var0 instanceof XRayModule var51) {
            var1.add(new ok.m("Ore view", var51.b()));
            var1.add(new ok.m("Show", var51.j(), true));
            var1.add(new ok.m("Markers", var51.B()));
            var1.add(ok.m.advanced("Marker limit", var51.L()));
         }

         if (var0 instanceof CrystalChamsModule var52) {
            var1.add(new ok.m("Crystal", var52.f()));
            var1.add(new ok.m("Appearance", var52.E()));
            var1.add(new ok.m("Motion", var52.k()));
         }

         if (var0 instanceof PetsModule var53) {
            var1.add(new ok.m("Companion", var53.J()));
            var1.add(new ok.m("Follow", var53.Y()));
            var1.add(new ok.m("Finish", var53.j()));
            var1.add(new ok.m("Glow", var53.S()));
         }

         if (var0 instanceof CryptoPetsModule var54) {
            var1.add(new ok.m("Coins", var54.E(), true));
            var1.add(new ok.m("Ring", var54.b()));
         }

         if (var0 instanceof RealisticWaterModule var55) {
            var1.add(new ok.m("Surface", List.of(var55.J, var55.F, var55.D, var55.E, var55.H)));
            var1.add(new ok.m("Water view", List.of(var55.X, var55.V)));
         }

         if (var0 instanceof WavingFoliageModule var56) {
            var1.add(new ok.m("Foliage", List.of(var56.x, var56.H, var56.F), true));
            var1.add(new ok.m("Wind", List.of(var56.O, var56.G, var56.t)));
         }

         if (var0 instanceof WetGroundModule var57) {
            var1.add(new ok.m("Puddles", List.of(var57.S, var57.G, var57.L, var57.g, var57.M)));
            var1.add(new ok.m("Rain", List.of(var57.O, var57.q), true));
         }

         if (var0 instanceof SnowGroundModule var58) {
            var1.add(new ok.m("Ground", List.of(var58.J, var58.W, var58.H, var58.Q)));
            var1.add(new ok.m("Snowfall", List.of(var58.w, var58.g, var58.i, var58.e)));
            var1.add(new ok.m("Footsteps", List.of(var58.C, var58.I)));
         }

         if (var0 instanceof BlockOverlayModule var59) {
            var1.add(new ok.m("Appearance", var59.t()));
            var1.add(new ok.m("Animation", var59.e()));
         }

         if (var0 instanceof az var60) {
            var1.add(new ok.m("Detection", var60.detectionSettings()));
            if (var60 instanceof a0 var4) {
               var1.add(new ok.m("Diagnostics", var4.d()));
            }

            var1.add(new ok.m("Scanning", var60.h()));
            var1.add(new ok.m("Markers", var60.markerSettings()));
            var1.add(new ok.m("Alerts", var60.I()));
         }

         if (var0 instanceof AutoSellModule var61) {
            var1.add(new ok.m("Items", var61.L()));
            var1.add(new ok.m("Sell menu", var61.i()));
            var1.add(new ok.m("Timing", var61.M()));
            var1.add(new ok.m("Feedback", var61.p()));
         }

         if (var0 instanceof MotionScannerModule var62) {
            var1.add(new ok.m("Detection", var62.Z()));
            var1.add(new ok.m("Alerts", var62.J()));
            var1.add(new ok.m("Appearance", var62.c()));
         }

         if (var0 instanceof HomeMacroModule var63) {
            var1.add(new ok.m("Home replacement", var63.v()));
         }

         if (var0 instanceof OrderDropperModule var64) {
            var1.add(new ok.m("Orders", var64.e()));
            var1.add(new ok.m("Shulkers", var64.E()));
            var1.add(new ok.m("Timing and safety", var64.L()));
         }

         if (var0 instanceof LightDebugModule var65) {
            var1.add(new ok.m("Scanning", var65.v()));
            var1.add(new ok.m("Appearance", var65.J()));
         }

         if (var0 instanceof HolesESPModule var66) {
            var1.add(new ok.m("Passage colors", var66.m()));
            var1.add(new ok.m("Rendering", var66.X()));
         }

         if (var0 instanceof StorageESPModule var67) {
            var1.add(new ok.m("Storage types", var67.i(), true));
            var1.add(new ok.m("Highlights", var67.R()));
            var1.add(new ok.m("Appearance", var67.m()));
            var1.add(ok.m.advanced("Distance and limits", var67.U()));
         }

         if (var0 instanceof HUDModule var77) {
            return List.of(new ok.m("Widgets", var77.d(), true), new ok.m("Panels", var77.T()));
         } else {
            if (var0 instanceof AnimationsModule var68) {
               var1.add(new ok.m("Swing", var68.u()));
               var1.add(ok.m.advanced("Fine tuning", var68.I()));
            }

            if (var0 instanceof ViewModelModule var69) {
               var1.add(new ok.m("Main hand", var69.K()));
               var1.add(new ok.m("Offhand", var69.c()));
            }

            if (var0 instanceof SwordModelsModule var70) {
               var1.add(new ok.m("Appearance", List.of(var70.n, var70.c, var70.j)));
               var1.add(new ok.m("Inspect", List.of(var70.i, var70.T, var70.S)));
               var1.add(ok.m.advanced("Hand placement", List.of(var70.R, var70.z, var70.s, var70.E)));
            }

            if (var0 instanceof FakeSpawnerModule var71) {
               var1.add(new ok.m("Backing blocks", var71.Q()));
               var1.add(new ok.m("Appearance", var71.s()));
               var1.add(new ok.m("Loot worth", var71.W()));
            }

            if (var0 instanceof com.larpclient.module.f var72) {
               var1.add(new ok.m("Trap", var72.u()));
               var1.add(ok.m.advanced("Advanced", var72.S()));
            }

            if (var0 instanceof ScaffoldModule var73) {
               var1.add(new ok.m("Bridge", var73.p()));
               var1.add(ok.m.advanced("Advanced", var73.d()));
            }

            if (var0 instanceof pp.x var74) {
               var1.add(
                  !(var0 instanceof com.larpclient.module.f) && !(var0 instanceof ScaffoldModule)
                     ? new ok.m("Highlight", var74.placementHighlight().t())
                     : ok.m.advanced("Highlight", var74.placementHighlight().t())
               );
            }

            for (ok.m var79 : var1) {
               var2.addAll(var79.settings());
            }

            List var76 = var0.getSettings().stream().filter(var1x -> !var2.contains(var1x)).toList();
            if (!var76.isEmpty()) {
               String var80 = var1.isEmpty() ? "Settings" : (var0 instanceof CustomSkyModule ? "General" : "Defaults");
               if (var0 instanceof a0 || var0 instanceof StorageESPModule) {
                  var80 = "Appearance";
               }

               var1.add(new ok.m(var80, var76));
            }

            if (var0 instanceof com.larpclient.module.v var81) {
               var1.add(ok.m.advanced("Shortcuts", var81.s()));
            }

            return List.copyOf(var1);
         }
      }
   }

   private static List<ok.m> o(CustomSkyModule var0) {
      return List.of(
         new ok.m("Appearance", var0.P()), new ok.m("Motion", var0.m()), new ok.m("Atmosphere", var0.N()), ok.m.advanced("Detail and placement", var0.M())
      );
   }

   private static List<ok.m> L(SkyShaderModule var0) {
      return List.of(new ok.m("Appearance", var0.E()), new ok.m("Motion and pattern", var0.x()));
   }

   public record m(String title, List<df<?>> settings, boolean chips, boolean collapsed) {
      public m(String var1, List<df<?>> var2) {
         this(var1, var2, false, false);
      }

      public m(String var1, List<df<?>> var2, boolean var3) {
         this(var1, var2, var3, false);
      }

      public static ok.m advanced(String var0, List<df<?>> var1) {
         return new ok.m(var0, var1, false, true);
      }
   }
}
