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
import com.larpclient.module.ke;
import com.larpclient.module.kj;
import com.larpclient.module.kq;
import com.larpclient.module.kv;
import java.util.ArrayList;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Set;

public final class ag {
   public static List<ag.g> G(Module var0) {
      if (var0 instanceof ChooseASkyEffectAndAdjustItsAppearanceModule var6) {
         ArrayList var7 = new ArrayList();
         var7.add(new ag.g("Sky", List.of(var6.l())));
         if (var6.L() instanceof CustomSkyModule var80) {
            var7.addAll(x(var80));
         } else if (var6.L() instanceof SkyShaderModule var85) {
            var7.addAll(k(var85));
         }

         var7.add(ag.g.advanced("Existing keybinds", var6.p()));
         return List.copyOf(var7);
      } else {
         ArrayList var1 = new ArrayList();
         Set var2 = Collections.newSetFromMap(new IdentityHashMap());
         if (var0.keybindMode() != null) {
            var2.add(var0.keybindMode());
         }

         if (var0 instanceof AutoClickerModule var3) {
            var1.add(new ag.g("Click controls", var3.a()));
            var1.add(new ag.g("Timing", var3.D()));
            var1.add(new ag.g("Targets and mining", var3.g()));
         }

         if (var0 instanceof kj var8) {
            var2.addAll(var8.w());
         }

         if (var0 instanceof RodSwapModule var9) {
            var1.add(new ag.g("Rod controls", var9.Y()));
            var1.add(new ag.g("Range and timing", var9.t()));
         }

         if (var0 instanceof ZoomModule var10) {
            var1.add(new ag.g("Zoom controls", var10.v()));
            var1.add(new ag.g("View", var10.B()));
         }

         if (var0 instanceof RemovalsModule var11) {
            var1.add(new ag.g("Effects", var11.K(), true));
            var1.add(new ag.g("Sounds", var11.S(), true));
         }

         if (var0 instanceof AutoCrystalModule var12) {
            var1.add(new ag.g("Actions", var12.x()));
            var1.add(new ag.g("Obsidian bases", var12.B()));
            var1.add(new ag.g("Targets", var12.Q()));
            var1.add(new ag.g("Aim", var12.z()));
            var1.add(new ag.g("Damage and pauses", var12.Z()));
         }

         if (var0 instanceof AutoAnchorModule var13) {
            var1.add(new ag.g("Actions", var13.O()));
            var1.add(new ag.g("Targets", var13.F()));
            var1.add(new ag.g("Aim", var13.G()));
            var1.add(new ag.g("Damage and pauses", var13.o()));
         }

         if (var0 instanceof AutoCartModule var14) {
            var1.add(new ag.g("Aim", var14.X()));
            var1.add(new ag.g("Shot", var14.o()));
            var1.add(new ag.g("Targets", var14.Q()));
            var1.add(new ag.g("Damage", var14.g()));
         }

         if (var0 instanceof AutoPearlCatchModule var15) {
            var1.add(new ag.g("Catch", var15.J()));
            var1.add(new ag.g("Aim", var15.X()));
         }

         if (var0 instanceof PerformanceModule var16) {
            var1.add(new ag.g("Render quality", var16.A()));
            var1.add(new ag.g("Chunks and frame rate", var16.I()));
            var1.add(new ag.g("Aggressive", var16.F()));
         }

         if (var0 instanceof AutoArmorModule var17) {
            var1.add(new ag.g("Armor materials", var17.I()));
         }

         if (var0 instanceof AuraModule var18) {
            var1.add(new ag.g("Rotation", var18.T()));
            var1.add(new ag.g("Attack", var18.H()));
            var1.add(new ag.g("Targets", var18.b(), true));
            var1.add(new ag.g("Selection", var18.Y()));
            var1.add(new ag.g("Utils", var18.W(), true));
            var1.add(new ag.g("No Attack When", var18.d(), true));
            var1.add(new ag.g("Indicators", var18.Q()));
         }

         if (var0 instanceof HitSoundsModule var19) {
            var1.add(new ag.g("Custom sound", var19.G()));
            var1.add(new ag.g("Behavior", var19.d()));
         }

         if (var0 instanceof HitFXModule var20) {
            var1.add(new ag.g("Impact style", var20.O()));
            var1.add(new ag.g("Hit rings", var20.I()));
            var1.add(new ag.g("Colors", var20.o()));
            var1.add(ag.g.advanced("Advanced", var20.x()));
         }

         if (var0 instanceof KillFXModule var21) {
            var1.add(new ag.g("Finisher", var21.j()));
            var1.add(new ag.g("Feedback", var21.C()));
            var1.add(ag.g.advanced("Distance", var21.e()));
         }

         if (var0 instanceof CustomSkyModule var22) {
            var1.add(new ag.g("Sky", List.of(var22.Z())));
            var1.addAll(x(var22));
         }

         if (var0 instanceof SkyShaderModule var23) {
            var1.add(new ag.g("Sky", List.of(var23.E())));
            var1.addAll(k(var23));
         }

         if (var0 instanceof WorldColorModule var24) {
            var1.add(new ag.g("World ambience", List.of(var24.u, var24.o)));
            var1.add(new ag.g("Color grading", List.of(var24.E, var24.Q, var24.i)));
            var1.add(ag.g.advanced("Fine grading", List.of(var24.p, var24.B, var24.T, var24.M, var24.I)));
         }

         if (var0 instanceof MotionBlurModule var25) {
            var1.add(new ag.g("Motion blur", List.of(var25.W, var25.T)));
            var1.add(ag.g.advanced("Streaks", List.of(var25.u, var25.V)));
         }

         if (var0 instanceof AmbientParticlesModule var26) {
            var1.add(new ag.g("World particles", List.of(var26.P, var26.w, var26.L, var26.S, var26.D, var26.d)));
            var1.add(new ag.g("Floating motion", List.of(var26.A, var26.u, var26.W, var26.G)));
            var1.add(new ag.g("Flying comets", List.of(var26.J, var26.t, var26.b)));
            var1.add(new ag.g("Appearance", List.of(var26.r, var26.i, var26.s, var26.T, var26.O)));
            var1.add(ag.g.advanced("Particle limit", List.of(var26.l)));
         }

         if (var0 instanceof WingsModule var27) {
            var1.add(new ag.g("Wearers", var27.V(), true));
            var1.add(new ag.g("Appearance", var27.r()));
         }

         if (var0 instanceof HeadlessKorbloxModule var28) {
            var1.add(new ag.g("Wearers", List.of(var28.w, var28.i), true));
            var1.add(new ag.g("Headless head", List.of(var28.J, var28.S)));
            var1.add(new ag.g("Korblox leg", List.of(var28.y, var28.Y, var28.o, var28.v)));
            var1.add(new ag.g("Spike colors", List.of(var28.H, var28.u, var28.P)));
         }

         if (var0 instanceof ChinaHatModule var29) {
            var1.add(new ag.g("Hats", var29.u(), true));
            var1.add(new ag.g("Wearers", var29.J(), true));
            var1.add(new ag.g("Appearance", var29.K()));
            var1.add(ag.g.advanced("Shape", var29.r()));
         }

         if (var0 instanceof PlayerTrailModule var30) {
            var1.add(new ag.g("Trail", var30.I()));
            var1.add(new ag.g("Appearance", var30.e()));
            var1.add(new ag.g("Motion", var30.N()));
            var1.add(ag.g.advanced("History and limits", var30.F()));
         }

         if (var0 instanceof AstralWeaponTrailsModule var31) {
            var1.add(new ag.g("Effect", var31.U()));
            var1.add(new ag.g("Appearance", var31.v()));
            var1.add(ag.g.advanced("Advanced", var31.W()));
         }

         if (var0 instanceof CelestialWeatherModule var32) {
            var1.add(new ag.g("Effect", var32.q()));
            var1.add(new ag.g("Appearance", var32.V()));
            var1.add(ag.g.advanced("Advanced", var32.o()));
         }

         if (var0 instanceof FogModule var33) {
            var1.add(new ag.g("Distance", var33.e()));
            var1.add(new ag.g("Atmosphere", var33.W()));
            var1.add(new ag.g("Sky", var33.w()));
            var1.add(ag.g.advanced("Where", var33.S()));
         }

         if (var0 instanceof SelfVisualsModule var34) {
            var1.add(new ag.g("Body effect", var34.s()));
            var1.add(new ag.g("Appearance", var34.W()));
            var1.add(new ag.g("Motion", var34.o()));
            var1.add(ag.g.advanced("Refraction", var34.A()));
         }

         if (var0 instanceof JumpFXModule var35) {
            var1.add(new ag.g("Jump effect", var35.h()));
            var1.add(new ag.g("Shape and timing", var35.D()));
            var1.add(new ag.g("Appearance", var35.I()));
         }

         if (var0 instanceof TargetAuraModule var36) {
            var1.add(new ag.g("Target effect", var36.G()));
            var1.add(new ag.g("Colors", var36.A()));
         }

         if (var0 instanceof CustomCrosshairModule var37) {
            var1.add(new ag.g("Shape", var37.f()));
            var1.add(new ag.g("Appearance", var37.x()));
         }

         if (var0 instanceof EchoHandModule var38) {
            var1.add(new ag.g("Hand effect", var38.L()));
            var1.add(new ag.g("Appearance", var38.y()));
            var1.add(ag.g.advanced("History and motion", var38.O()));
         }

         if (var0 instanceof FlameHandsModule var39) {
            var1.add(new ag.g("Hand flames", var39.u()));
            var1.add(new ag.g("Colors", var39.i()));
            var1.add(new ag.g("Motion", var39.X()));
         }

         if (var0 instanceof PearlParticlesModule var40) {
            var1.add(new ag.g("Pearl trail", var40.C()));
            var1.add(new ag.g("Colors", var40.X()));
            var1.add(ag.g.advanced("Drift and fade", var40.x()));
         }

         if (var0 instanceof DamageMarkersModule var41) {
            var1.add(new ag.g("Damage sources", var41.y(), true));
            var1.add(new ag.g("Labels", var41.V()));
         }

         if (var0 instanceof PlayerModelModule var42) {
            var1.add(new ag.g("Character", var42.A()));
            var1.add(ag.g.advanced("Diagnostics", var42.m()));
         }

         if (var0 instanceof GUIAnimationModule var43) {
            var1.add(new ag.g("Interface", var43.w(), true));
            var1.add(new ag.g("Camera", var43.U(), true));
            var1.add(new ag.g("World and timing", var43.F()));
         }

         if (var0 instanceof ChamsModule var44) {
            var1.add(new ag.g("Entities", var44.r(), true));
            var1.add(new ag.g("Colors", var44.s()));
            var1.add(new ag.g("Outline and fill", var44.h.m()));
            var1.add(new ag.g("Glow", var44.h.P()));
            var1.add(new ag.g("Gradient motion", var44.h.y()));
            var1.add(new ag.g("Occlusion", var44.R()));
            var1.add(ag.g.advanced("Distance and limits", var44.q()));
         }

         if (var0 instanceof HandChamsModule var45) {
            var1.add(new ag.g("Colors", var45.T.t()));
            var1.add(new ag.g("Outline and fill", var45.T.m()));
            var1.add(new ag.g("Glow", var45.T.P()));
            var1.add(new ag.g("Gradient motion", var45.T.y()));
         }

         if (var0 instanceof ESPModule var46) {
            var1.add(new ag.g("Entities", var46.v(), true));
            var1.add(new ag.g("Highlight", var46.i()));
            var1.add(new ag.g("Colors", var46.u()));
            var1.add(new ag.g("Stroke", var46.k()));
            var1.add(new ag.g("Health", var46.p()));
            var1.add(ag.g.advanced("Limits and alerts", var46.X()));
         }

         if (var0 instanceof ItemESPModule var47) {
            var1.add(new ag.g("Drops", var47.p()));
            var1.add(new ag.g("Appearance", var47.b()));
            var1.add(ag.g.advanced("Item limit", var47.m()));
         }

         if (var0 instanceof TracersModule var48) {
            var1.add(new ag.g("Entities", var48.V(), true));
            var1.add(new ag.g("Lines", var48.N()));
            var1.add(ag.g.advanced("Entity limit", var48.B()));
         }

         if (var0 instanceof NametagsModule var49) {
            var1.add(new ag.g("Entities", var49.A(), true));
            var1.add(new ag.g("Label content", var49.I(), true));
            var1.add(new ag.g("Placement", var49.X()));
         }

         if (var0 instanceof XRayModule var50) {
            var1.add(new ag.g("Ore view", var50.Q()));
            var1.add(new ag.g("Show", var50.C(), true));
            var1.add(new ag.g("Markers", var50.G()));
            var1.add(ag.g.advanced("Marker limit", var50.w()));
         }

         if (var0 instanceof CrystalChamsModule var51) {
            var1.add(new ag.g("Crystal", var51.S()));
            var1.add(new ag.g("Appearance", var51.B()));
            var1.add(new ag.g("Motion", var51.u()));
         }

         if (var0 instanceof PetsModule var52) {
            var1.add(new ag.g("Companion", var52.t()));
            var1.add(new ag.g("Follow", var52.c()));
            var1.add(new ag.g("Finish", var52.G()));
            var1.add(new ag.g("Glow", var52.f()));
         }

         if (var0 instanceof CryptoPetsModule var53) {
            var1.add(new ag.g("Coins", var53.D(), true));
            var1.add(new ag.g("Ring", var53.h()));
         }

         if (var0 instanceof RealisticWaterModule var54) {
            var1.add(new ag.g("Surface", List.of(var54.I, var54.N, var54.S, var54.a, var54.e)));
            var1.add(new ag.g("Water view", List.of(var54.W, var54.A)));
         }

         if (var0 instanceof WavingFoliageModule var55) {
            var1.add(new ag.g("Foliage", List.of(var55.N, var55.C, var55.E), true));
            var1.add(new ag.g("Wind", List.of(var55.u, var55.i, var55.L)));
         }

         if (var0 instanceof WetGroundModule var56) {
            var1.add(new ag.g("Puddles", List.of(var56.H, var56.p, var56.q, var56.h, var56.B)));
            var1.add(new ag.g("Rain", List.of(var56.y, var56.C), true));
         }

         if (var0 instanceof SnowGroundModule var57) {
            var1.add(new ag.g("Ground", List.of(var57.p, var57.L, var57.q, var57.j)));
            var1.add(new ag.g("Snowfall", List.of(var57.W, var57.v, var57.s, var57.S)));
            var1.add(new ag.g("Footsteps", List.of(var57.Z, var57.R)));
         }

         if (var0 instanceof BlockOverlayModule var58) {
            var1.add(new ag.g("Appearance", var58.w()));
            var1.add(new ag.g("Animation", var58.T()));
         }

         if (var0 instanceof ke var59) {
            var1.add(new ag.g("Detection", var59.detectionSettings()));
            if (var59 instanceof kq var4) {
               var1.add(new ag.g("Diagnostics", var4.x()));
            }

            var1.add(new ag.g("Scanning", var59.r()));
            var1.add(new ag.g("Markers", var59.markerSettings()));
            var1.add(new ag.g("Alerts", var59.T()));
         }

         if (var0 instanceof AutoSellModule var60) {
            var1.add(new ag.g("Items", var60.P()));
            var1.add(new ag.g("Sell menu", var60.K()));
            var1.add(new ag.g("Timing", var60.a()));
            var1.add(new ag.g("Feedback", var60.g()));
         }

         if (var0 instanceof MotionScannerModule var61) {
            var1.add(new ag.g("Detection", var61.y()));
            var1.add(new ag.g("Alerts", var61.z()));
            var1.add(new ag.g("Appearance", var61.G()));
         }

         if (var0 instanceof HomeMacroModule var62) {
            var1.add(new ag.g("Home replacement", var62.u()));
         }

         if (var0 instanceof ac var63) {
            for (ac.r var5 : var63.settingGroups()) {
               var1.add(var5.advanced() ? ag.g.advanced(var5.title(), var5.settings()) : new ag.g(var5.title(), var5.settings()));
            }
         }

         if (var0 instanceof AutoGGModule var64) {
            var1.add(new ag.g("Messages", var64.n()));
            var1.add(new ag.g("Timing", var64.m()));
         }

         if (var0 instanceof WaypointsModule var65) {
            var1.add(new ag.g("Display", var65.r()));
            var1.add(new ag.g("Shortcut", var65.C()));
         }

         if (var0 instanceof OrderDropperModule var66) {
            var1.add(new ag.g("Orders", var66.q()));
            var1.add(new ag.g("Shulkers", var66.D()));
            var1.add(new ag.g("Timing and safety", var66.l()));
         }

         if (var0 instanceof LightDebugModule var67) {
            var1.add(new ag.g("Scanning", var67.e()));
            var1.add(new ag.g("Appearance", var67.W()));
         }

         if (var0 instanceof HolesESPModule var68) {
            var1.add(new ag.g("Passage colors", var68.F()));
            var1.add(new ag.g("Rendering", var68.p()));
         }

         if (var0 instanceof StorageESPModule var69) {
            var1.add(new ag.g("Storage types", var69.J(), true));
            var1.add(new ag.g("Highlights", var69.Y()));
            var1.add(new ag.g("Appearance", var69.B()));
            var1.add(ag.g.advanced("Distance and limits", var69.k()));
         }

         if (var0 instanceof HUDModule var79) {
            return List.of(new ag.g("Widgets", var79.f(), true), new ag.g("Panels", var79.z()));
         } else {
            if (var0 instanceof AnimationsModule var70) {
               var1.add(new ag.g("Swing", var70.e()));
               var1.add(ag.g.advanced("Fine tuning", var70.R()));
            }

            if (var0 instanceof ViewModelModule var71) {
               var1.add(new ag.g("Swing", var71.R()));
               var1.add(new ag.g("Main hand", var71.K()));
               var1.add(new ag.g("Offhand", var71.t()));
            }

            if (var0 instanceof SwordModelsModule var72) {
               var1.add(new ag.g("Appearance", List.of(var72.Z, var72.d, var72.j)));
               var1.add(new ag.g("Inspect", List.of(var72.H, var72.v, var72.V)));
               var1.add(ag.g.advanced("Hand placement", List.of(var72.t, var72.r, var72.x, var72.i)));
            }

            if (var0 instanceof FakeSpawnerModule var73) {
               var1.add(new ag.g("Backing blocks", var73.c()));
               var1.add(new ag.g("Appearance", var73.h()));
               var1.add(new ag.g("Loot worth", var73.W()));
            }

            if (var0 instanceof kv var74) {
               var1.add(new ag.g("Trap", var74.l()));
               var1.add(ag.g.advanced("Advanced", var74.M()));
            }

            if (var0 instanceof ScaffoldModule var75) {
               var1.add(new ag.g("Bridge", var75.n()));
            }

            if (var0 instanceof ai.e var76) {
               var1.add(var0 instanceof kv ? ag.g.advanced("Highlight", var76.placementHighlight().g()) : new ag.g("Highlight", var76.placementHighlight().g()));
            }

            for (ag.g var82 : var1) {
               var2.addAll(var82.settings());
            }

            List var78 = var0.getSettings().stream().filter(var1x -> !var2.contains(var1x)).toList();
            if (!var78.isEmpty()) {
               String var83 = var1.isEmpty() ? "Settings" : (var0 instanceof CustomSkyModule ? "General" : "Defaults");
               if (var0 instanceof kq || var0 instanceof StorageESPModule) {
                  var83 = "Appearance";
               }

               var1.add(new ag.g(var83, var78));
            }

            if (var0 instanceof kj var84) {
               var1.add(ag.g.advanced("Shortcuts", var84.w()));
            }

            return List.copyOf(var1);
         }
      }
   }

   private static List<ag.g> x(CustomSkyModule var0) {
      return List.of(
         new ag.g("Appearance", var0.u()), new ag.g("Motion", var0.V()), new ag.g("Atmosphere", var0.R()), ag.g.advanced("Detail and placement", var0.o())
      );
   }

   private static List<ag.g> k(SkyShaderModule var0) {
      return List.of(new ag.g("Appearance", var0.A()), new ag.g("Motion and pattern", var0.f()));
   }

   public record g(String title, List<com.larpclient.module.r<?>> settings, boolean chips, boolean collapsed) {
      public g(String var1, List<com.larpclient.module.r<?>> var2) {
         this(var1, var2, false, false);
      }

      public g(String var1, List<com.larpclient.module.r<?>> var2, boolean var3) {
         this(var1, var2, var3, false);
      }

      public static ag.g advanced(String var0, List<com.larpclient.module.r<?>> var1) {
         return new ag.g(var0, var1, false, true);
      }
   }
}
