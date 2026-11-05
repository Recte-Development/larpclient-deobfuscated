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
import com.larpclient.module.tf;
import com.larpclient.module.to;
import java.util.ArrayList;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Set;

public final class dp {
   public static List<dp.x> s(Module var0) {
      if (var0 instanceof ChooseASkyEffectAndAdjustItsAppearanceModule var6) {
         ArrayList var7 = new ArrayList();
         var7.add(new dp.x("Sky", List.of(var6.w())));
         if (var6.m() instanceof CustomSkyModule var78) {
            var7.addAll(U(var78));
         } else if (var6.m() instanceof SkyShaderModule var82) {
            var7.addAll(S(var82));
         }

         var7.add(dp.x.advanced("Existing keybinds", var6.R()));
         return List.copyOf(var7);
      } else {
         ArrayList var1 = new ArrayList();
         Set var2 = Collections.newSetFromMap(new IdentityHashMap());
         if (var0.keybindMode() != null) {
            var2.add(var0.keybindMode());
         }

         if (var0 instanceof AutoClickerModule var3) {
            var1.add(new dp.x("Click controls", var3.t()));
            var1.add(new dp.x("Timing", var3.m()));
            var1.add(new dp.x("Targets and mining", var3.E()));
         }

         if (var0 instanceof com.larpclient.module.a var8) {
            var2.addAll(var8.z());
         }

         if (var0 instanceof RodSwapModule var9) {
            var1.add(new dp.x("Rod controls", var9.C()));
            var1.add(new dp.x("Range and timing", var9.W()));
         }

         if (var0 instanceof HalloweenModule var10) {
            var1.add(new dp.x("After Dark", var10.a()));
            var1.add(dp.x.advanced("Customize Effects", var10.T()));
         }

         if (var0 instanceof ZoomModule var11) {
            var1.add(new dp.x("Zoom controls", var11.x()));
            var1.add(new dp.x("View", var11.m()));
         }

         if (var0 instanceof RemovalsModule var12) {
            var1.add(new dp.x("Effects", var12.e(), true));
            var1.add(new dp.x("Sounds", var12.w(), true));
         }

         if (var0 instanceof AutoCrystalModule var13) {
            var1.add(new dp.x("Actions", var13.n()));
            var1.add(new dp.x("Obsidian bases", var13.t()));
            var1.add(new dp.x("Targets", var13.P()));
            var1.add(new dp.x("Aim", var13.X()));
            var1.add(new dp.x("Damage and pauses", var13.T()));
         }

         if (var0 instanceof AutoAnchorModule var14) {
            var1.add(new dp.x("Actions", var14.I()));
            var1.add(new dp.x("Targets", var14.t()));
            var1.add(new dp.x("Aim", var14.e()));
            var1.add(new dp.x("Damage and pauses", var14.Z()));
         }

         if (var0 instanceof AutoCartModule var15) {
            var1.add(new dp.x("Aim", var15.U()));
            var1.add(new dp.x("Shot", var15.u()));
            var1.add(new dp.x("Targets", var15.g()));
            var1.add(new dp.x("Damage", var15.Q()));
         }

         if (var0 instanceof AutoPearlCatchModule var16) {
            var1.add(new dp.x("Catch", var16.y()));
            var1.add(new dp.x("Aim", var16.E()));
         }

         if (var0 instanceof PerformanceModule var17) {
            var1.add(new dp.x("Render quality", var17.p()));
            var1.add(new dp.x("Chunks and frame rate", var17.E()));
            var1.add(new dp.x("Aggressive", var17.r()));
         }

         if (var0 instanceof AutoArmorModule var18) {
            var1.add(new dp.x("Armor materials", var18.U()));
         }

         if (var0 instanceof AuraModule var19) {
            var1.add(new dp.x("Rotation", var19.o()));
            var1.add(new dp.x("Attack", var19.J()));
            var1.add(new dp.x("Targets", var19.B(), true));
            var1.add(new dp.x("Selection", var19.F()));
            var1.add(new dp.x("Utils", var19.Z(), true));
            var1.add(new dp.x("No Attack When", var19.X(), true));
            var1.add(new dp.x("Indicators", var19.g()));
         }

         if (var0 instanceof HitSoundsModule var20) {
            var1.add(new dp.x("Custom sound", var20.N()));
            var1.add(new dp.x("Behavior", var20.x()));
         }

         if (var0 instanceof HitFXModule var21) {
            var1.add(new dp.x("Impact style", var21.m()));
            var1.add(new dp.x("Hit rings", var21.T()));
            var1.add(new dp.x("Colors", var21.O()));
            var1.add(dp.x.advanced("Advanced", var21.p()));
         }

         if (var0 instanceof KillFXModule var22) {
            var1.add(new dp.x("Finisher", var22.F()));
            var1.add(new dp.x("Feedback", var22.K()));
            var1.add(dp.x.advanced("Distance", var22.M()));
         }

         if (var0 instanceof CustomSkyModule var23) {
            var1.add(new dp.x("Sky", List.of(var23.G())));
            var1.addAll(U(var23));
         }

         if (var0 instanceof SkyShaderModule var24) {
            var1.add(new dp.x("Sky", List.of(var24.d())));
            var1.addAll(S(var24));
         }

         if (var0 instanceof WorldColorModule var25) {
            var1.add(new dp.x("World ambience", List.of(var25.g, var25.z)));
            var1.add(new dp.x("Color grading", List.of(var25.H, var25.A, var25.R)));
            var1.add(dp.x.advanced("Fine grading", List.of(var25.S, var25.j, var25.q, var25.v, var25.a)));
         }

         if (var0 instanceof MotionBlurModule var26) {
            var1.add(new dp.x("Motion blur", List.of(var26.M, var26.J)));
            var1.add(dp.x.advanced("Streaks", List.of(var26.s, var26.U)));
         }

         if (var0 instanceof AmbientParticlesModule var27) {
            var1.add(new dp.x("World particles", List.of(var27.e, var27.q, var27.H, var27.u, var27.W, var27.Y)));
            var1.add(new dp.x("Floating motion", List.of(var27.w, var27.J, var27.r, var27.d)));
            var1.add(new dp.x("Flying comets", List.of(var27.D, var27.y, var27.B)));
            var1.add(new dp.x("Appearance", List.of(var27.R, var27.L, var27.x, var27.E, var27.t)));
            var1.add(dp.x.advanced("Particle limit", List.of(var27.Z)));
         }

         if (var0 instanceof WingsModule var28) {
            var1.add(new dp.x("Wearers", var28.j(), true));
            var1.add(new dp.x("Appearance", var28.o()));
         }

         if (var0 instanceof HeadlessKorbloxModule var29) {
            var1.add(new dp.x("Wearers", List.of(var29.n, var29.k), true));
            var1.add(new dp.x("Headless head", List.of(var29.M, var29.m)));
            var1.add(new dp.x("Korblox leg", List.of(var29.p, var29.T, var29.l, var29.o)));
            var1.add(new dp.x("Spike colors", List.of(var29.Z, var29.X, var29.G)));
         }

         if (var0 instanceof ChinaHatModule var30) {
            var1.add(new dp.x("Wearers", var30.B(), true));
            var1.add(new dp.x("Appearance", var30.N()));
            var1.add(dp.x.advanced("Shape", var30.o()));
         }

         if (var0 instanceof PlayerTrailModule var31) {
            var1.add(new dp.x("Trail", var31.V()));
            var1.add(new dp.x("Appearance", var31.E()));
            var1.add(new dp.x("Motion", var31.z()));
            var1.add(dp.x.advanced("History and limits", var31.Z()));
         }

         if (var0 instanceof AstralWeaponTrailsModule var32) {
            var1.add(new dp.x("Effect", var32.p()));
            var1.add(new dp.x("Appearance", var32.f()));
            var1.add(dp.x.advanced("Advanced", var32.c()));
         }

         if (var0 instanceof CelestialWeatherModule var33) {
            var1.add(new dp.x("Effect", var33.T()));
            var1.add(new dp.x("Appearance", var33.o()));
            var1.add(dp.x.advanced("Advanced", var33.W()));
         }

         if (var0 instanceof FogModule var34) {
            var1.add(new dp.x("Distance", var34.e()));
            var1.add(new dp.x("Atmosphere", var34.Z()));
            var1.add(new dp.x("Sky", var34.s()));
            var1.add(dp.x.advanced("Where", var34.O()));
         }

         if (var0 instanceof SelfVisualsModule var35) {
            var1.add(new dp.x("Body effect", var35.q()));
            var1.add(new dp.x("Appearance", var35.O()));
            var1.add(new dp.x("Motion", var35.Y()));
            var1.add(dp.x.advanced("Refraction", var35.j()));
         }

         if (var0 instanceof JumpFXModule var36) {
            var1.add(new dp.x("Jump effect", var36.X()));
            var1.add(new dp.x("Shape and timing", var36.C()));
            var1.add(new dp.x("Appearance", var36.T()));
         }

         if (var0 instanceof TargetAuraModule var37) {
            var1.add(new dp.x("Target effect", var37.c()));
            var1.add(new dp.x("Colors", var37.E()));
         }

         if (var0 instanceof CustomCrosshairModule var38) {
            var1.add(new dp.x("Shape", var38.B()));
            var1.add(new dp.x("Appearance", var38.t()));
         }

         if (var0 instanceof EchoHandModule var39) {
            var1.add(new dp.x("Hand effect", var39.S()));
            var1.add(new dp.x("Appearance", var39.c()));
            var1.add(dp.x.advanced("History and motion", var39.G()));
         }

         if (var0 instanceof FlameHandsModule var40) {
            var1.add(new dp.x("Hand flames", var40.g()));
            var1.add(new dp.x("Colors", var40.C()));
            var1.add(new dp.x("Motion", var40.j()));
         }

         if (var0 instanceof PearlParticlesModule var41) {
            var1.add(new dp.x("Pearl trail", var41.a()));
            var1.add(new dp.x("Colors", var41.L()));
            var1.add(dp.x.advanced("Drift and fade", var41.I()));
         }

         if (var0 instanceof DamageMarkersModule var42) {
            var1.add(new dp.x("Damage sources", var42.V(), true));
            var1.add(new dp.x("Labels", var42.L()));
         }

         if (var0 instanceof PlayerModelModule var43) {
            var1.add(new dp.x("Character", var43.S()));
            var1.add(dp.x.advanced("Diagnostics", var43.N()));
         }

         if (var0 instanceof GUIAnimationModule var44) {
            var1.add(new dp.x("Interface", var44.s(), true));
            var1.add(new dp.x("Camera", var44.o(), true));
            var1.add(new dp.x("World and timing", var44.i()));
         }

         if (var0 instanceof ChamsModule var45) {
            var1.add(new dp.x("Entities", var45.U(), true));
            var1.add(new dp.x("Colors", var45.t()));
            var1.add(new dp.x("Outline and fill", var45.V.w()));
            var1.add(new dp.x("Glow", var45.V.a()));
            var1.add(new dp.x("Gradient motion", var45.V.J()));
            var1.add(new dp.x("Occlusion", var45.P()));
            var1.add(dp.x.advanced("Distance and limits", var45.h()));
         }

         if (var0 instanceof HandChamsModule var46) {
            var1.add(new dp.x("Colors", var46.S.r()));
            var1.add(new dp.x("Outline and fill", var46.S.w()));
            var1.add(new dp.x("Glow", var46.S.a()));
            var1.add(new dp.x("Gradient motion", var46.S.J()));
         }

         if (var0 instanceof ESPModule var47) {
            var1.add(new dp.x("Entities", var47.v(), true));
            var1.add(new dp.x("Highlight", var47.n()));
            var1.add(new dp.x("Colors", var47.y()));
            var1.add(new dp.x("Stroke", var47.j()));
            var1.add(new dp.x("Health", var47.m()));
            var1.add(dp.x.advanced("Limits and alerts", var47.h()));
         }

         if (var0 instanceof ItemESPModule var48) {
            var1.add(new dp.x("Drops", var48.y()));
            var1.add(new dp.x("Appearance", var48.K()));
            var1.add(dp.x.advanced("Item limit", var48.x()));
         }

         if (var0 instanceof TracersModule var49) {
            var1.add(new dp.x("Entities", var49.H(), true));
            var1.add(new dp.x("Lines", var49.I()));
            var1.add(dp.x.advanced("Entity limit", var49.B()));
         }

         if (var0 instanceof NametagsModule var50) {
            var1.add(new dp.x("Entities", var50.s(), true));
            var1.add(new dp.x("Label content", var50.w(), true));
            var1.add(new dp.x("Placement", var50.A()));
         }

         if (var0 instanceof XRayModule var51) {
            var1.add(new dp.x("Ore view", var51.N()));
            var1.add(new dp.x("Show", var51.L(), true));
            var1.add(new dp.x("Markers", var51.u()));
            var1.add(dp.x.advanced("Marker limit", var51.C()));
         }

         if (var0 instanceof CrystalChamsModule var52) {
            var1.add(new dp.x("Crystal", var52.W()));
            var1.add(new dp.x("Appearance", var52.p()));
            var1.add(new dp.x("Motion", var52.b()));
         }

         if (var0 instanceof PetsModule var53) {
            var1.add(new dp.x("Companion", var53.n()));
            var1.add(new dp.x("Follow", var53.v()));
            var1.add(new dp.x("Finish", var53.J()));
            var1.add(new dp.x("Glow", var53.x()));
         }

         if (var0 instanceof CryptoPetsModule var54) {
            var1.add(new dp.x("Coins", var54.m(), true));
            var1.add(new dp.x("Ring", var54.J()));
         }

         if (var0 instanceof RealisticWaterModule var55) {
            var1.add(new dp.x("Surface", List.of(var55.h, var55.q, var55.F, var55.i, var55.D)));
            var1.add(new dp.x("Water view", List.of(var55.u, var55.B)));
         }

         if (var0 instanceof WavingFoliageModule var56) {
            var1.add(new dp.x("Foliage", List.of(var56.t, var56.M, var56.X), true));
            var1.add(new dp.x("Wind", List.of(var56.Q, var56.m, var56.g)));
         }

         if (var0 instanceof WetGroundModule var57) {
            var1.add(new dp.x("Puddles", List.of(var57.S, var57.M, var57.D, var57.Q, var57.B)));
            var1.add(new dp.x("Rain", List.of(var57.C, var57.N), true));
         }

         if (var0 instanceof SnowGroundModule var58) {
            var1.add(new dp.x("Ground", List.of(var58.a, var58.j, var58.m, var58.L)));
            var1.add(new dp.x("Snowfall", List.of(var58.p, var58.v, var58.r, var58.g)));
            var1.add(new dp.x("Footsteps", List.of(var58.Z, var58.s)));
         }

         if (var0 instanceof BlockOverlayModule var59) {
            var1.add(new dp.x("Appearance", var59.b()));
            var1.add(new dp.x("Animation", var59.S()));
         }

         if (var0 instanceof tf var60) {
            var1.add(new dp.x("Detection", var60.detectionSettings()));
            if (var60 instanceof to var4) {
               var1.add(new dp.x("Diagnostics", var4.L()));
            }

            var1.add(new dp.x("Scanning", var60.u()));
            var1.add(new dp.x("Markers", var60.markerSettings()));
            var1.add(new dp.x("Alerts", var60.W()));
         }

         if (var0 instanceof AutoSellModule var61) {
            var1.add(new dp.x("Items", var61.O()));
            var1.add(new dp.x("Sell menu", var61.E()));
            var1.add(new dp.x("Timing", var61.L()));
            var1.add(new dp.x("Feedback", var61.s()));
         }

         if (var0 instanceof MotionScannerModule var62) {
            var1.add(new dp.x("Detection", var62.h()));
            var1.add(new dp.x("Alerts", var62.s()));
            var1.add(new dp.x("Appearance", var62.j()));
         }

         if (var0 instanceof HomeMacroModule var63) {
            var1.add(new dp.x("Home replacement", var63.D()));
         }

         if (var0 instanceof OrderDropperModule var64) {
            var1.add(new dp.x("Orders", var64.n()));
            var1.add(new dp.x("Shulkers", var64.T()));
            var1.add(new dp.x("Timing and safety", var64.m()));
         }

         if (var0 instanceof LightDebugModule var65) {
            var1.add(new dp.x("Scanning", var65.O()));
            var1.add(new dp.x("Appearance", var65.w()));
         }

         if (var0 instanceof HolesESPModule var66) {
            var1.add(new dp.x("Passage colors", var66.Q()));
            var1.add(new dp.x("Rendering", var66.p()));
         }

         if (var0 instanceof StorageESPModule var67) {
            var1.add(new dp.x("Storage types", var67.J(), true));
            var1.add(new dp.x("Highlights", var67.Y()));
            var1.add(new dp.x("Appearance", var67.y()));
            var1.add(dp.x.advanced("Distance and limits", var67.U()));
         }

         if (var0 instanceof HUDModule var77) {
            return List.of(new dp.x("Widgets", var77.c(), true), new dp.x("Panels", var77.I()));
         } else {
            if (var0 instanceof AnimationsModule var68) {
               var1.add(new dp.x("Swing", var68.L()));
               var1.add(dp.x.advanced("Fine tuning", var68.R()));
            }

            if (var0 instanceof ViewModelModule var69) {
               var1.add(new dp.x("Main hand", var69.n()));
               var1.add(new dp.x("Offhand", var69.R()));
            }

            if (var0 instanceof SwordModelsModule var70) {
               var1.add(new dp.x("Appearance", List.of(var70.X, var70.U, var70.r)));
               var1.add(new dp.x("Inspect", List.of(var70.L, var70.j, var70.x)));
               var1.add(dp.x.advanced("Hand placement", List.of(var70.V, var70.d, var70.A, var70.q)));
            }

            if (var0 instanceof FakeSpawnerModule var71) {
               var1.add(new dp.x("Backing blocks", var71.K()));
               var1.add(new dp.x("Appearance", var71.g()));
               var1.add(new dp.x("Loot worth", var71.z()));
            }

            if (var0 instanceof com.larpclient.module.t var72) {
               var1.add(new dp.x("Trap", var72.t()));
               var1.add(dp.x.advanced("Advanced", var72.A()));
            }

            if (var0 instanceof ScaffoldModule var73) {
               var1.add(new dp.x("Bridge", var73.D()));
               var1.add(dp.x.advanced("Advanced", var73.U()));
            }

            if (var0 instanceof hy.c var74) {
               var1.add(
                  !(var0 instanceof com.larpclient.module.t) && !(var0 instanceof ScaffoldModule)
                     ? new dp.x("Highlight", var74.placementHighlight().c())
                     : dp.x.advanced("Highlight", var74.placementHighlight().c())
               );
            }

            for (dp.x var79 : var1) {
               var2.addAll(var79.settings());
            }

            List var76 = var0.getSettings().stream().filter(var1x -> !var2.contains(var1x)).toList();
            if (!var76.isEmpty()) {
               String var80 = var1.isEmpty() ? "Settings" : (var0 instanceof CustomSkyModule ? "General" : "Defaults");
               if (var0 instanceof to || var0 instanceof StorageESPModule) {
                  var80 = "Appearance";
               }

               var1.add(new dp.x(var80, var76));
            }

            if (var0 instanceof com.larpclient.module.a var81) {
               var1.add(dp.x.advanced("Shortcuts", var81.z()));
            }

            return List.copyOf(var1);
         }
      }
   }

   private static List<dp.x> U(CustomSkyModule var0) {
      return List.of(
         new dp.x("Appearance", var0.b()), new dp.x("Motion", var0.e()), new dp.x("Atmosphere", var0.X()), dp.x.advanced("Detail and placement", var0.l())
      );
   }

   private static List<dp.x> S(SkyShaderModule var0) {
      return List.of(new dp.x("Appearance", var0.V()), new dp.x("Motion and pattern", var0.d()));
   }

   public record x(String title, List<com.larpclient.module.l6<?>> settings, boolean chips, boolean collapsed) {
      public x(String var1, List<com.larpclient.module.l6<?>> var2) {
         this(var1, var2, false, false);
      }

      public x(String var1, List<com.larpclient.module.l6<?>> var2, boolean var3) {
         this(var1, var2, var3, false);
      }

      public static dp.x advanced(String var0, List<com.larpclient.module.l6<?>> var1) {
         return new dp.x(var0, var1, false, true);
      }
   }
}
