package com.larpclient;

import com.larpclient.module.AmbientParticlesModule;
import com.larpclient.module.BlockOverlayModule;
import com.larpclient.module.ChamsModule;
import com.larpclient.module.ChinaHatModule;
import com.larpclient.module.DamageMarkersModule;
import com.larpclient.module.HitFXModule;
import com.larpclient.module.JumpFXModule;
import com.larpclient.module.KillFXModule;
import com.larpclient.module.LineGlyphsModule;
import com.larpclient.module.OrderDropperModule;
import com.larpclient.module.PearlParticlesModule;
import com.larpclient.module.PlayerModelModule;
import com.larpclient.module.SelfVisualsModule;
import com.larpclient.module.SkyShaderModule;
import com.larpclient.module.TargetAuraModule;
import com.larpclient.module.WingsModule;
import com.larpclient.module.WorldLyricsModule;
import com.larpclient.module.an;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.textures.GpuTextureView;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents.ClientStopping;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents.EndTick;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents.StartTick;
import net.fabricmc.fabric.api.client.rendering.v1.world.WorldExtractionContext;
import net.fabricmc.fabric.api.client.rendering.v1.world.WorldRenderContext;
import net.fabricmc.fabric.api.client.rendering.v1.world.WorldRenderEvents;
import net.fabricmc.fabric.api.client.rendering.v1.world.WorldRenderEvents.AfterBlockOutlineExtraction;
import net.fabricmc.fabric.api.client.rendering.v1.world.WorldRenderEvents.BeforeBlockOutline;
import net.fabricmc.fabric.api.client.rendering.v1.world.WorldRenderEvents.BeforeEntities;
import net.fabricmc.fabric.api.client.rendering.v1.world.WorldRenderEvents.BeforeTranslucent;
import net.fabricmc.fabric.api.client.rendering.v1.world.WorldRenderEvents.EndMain;
import net.minecraft.class_10017;
import net.minecraft.class_11658;
import net.minecraft.class_12075;
import net.minecraft.class_1309;
import net.minecraft.class_276;
import net.minecraft.class_310;
import net.minecraft.class_332;
import net.minecraft.class_638;

public final class wc {
   public static final gd i = new gd();
   public static final ce L = new ce();
   public static final s9 O = new s9();
   public static final f0 h = new f0();
   private static List<uu> q = List.of();
   private static cw M;
   private static class_638 N;
   private static boolean c;
   private static int j = -1;
   private static class_1309 z;
   private static double f;
   private static class_11658 g;
   private static float U;
   private static class_10017 K;
   private static gv.y d;
   private static double S;
   private static double k;
   private static int t;
   private static boolean u;
   private static boolean B;
   private static Boolean a;
   private static final Set<String> w = new HashSet<>();
   private static int l = -1;

   static void y(String var0, Runnable var1) {
      if (l != t_.g) {
         w.clear();
         l = t_.g;
      }

      if (!w.contains(var0)) {
         try {
            var1.run();
         } catch (RuntimeException var4) {
            w.add(var0);
            f5.i(var0);
            if (var0.equals("Chams")) {
               ChamsModule var3 = ChamsModule.K();
               if (var3 != null) {
                  var3.n();
               }
            }

            if (var0.equals("CrystalChams")) {
               CrystalChamsModule var5 = CrystalChamsModule.X();
               if (var5 != null) {
                  var5.g();
               }
            }

            if (var0.equals("Pets")) {
               PetsModule var6 = PetsModule.F();
               if (var6 != null) {
                  var6.p();
               }
            }

            if (var0.equals("CryptoPets")) {
               CryptoPetsModule var7 = CryptoPetsModule.M();
               if (var7 != null) {
                  var7.M();
               }
            }

            if (var0.equals("EmoteProps")) {
               is.b();
            }

            if (var0.equals("PlayerModel")) {
               PlayerModelModule var8 = PlayerModelModule.h();
               if (var8 != null) {
                  var8.X();
               }
            }

            if (var0.equals("SnowGround")) {
               SnowGroundModule var9 = SnowGroundModule.R();
               if (var9 != null) {
                  var9.K();
               }
            }

            if (var0.equals("KillFX")) {
               KillFXModule var10 = KillFXModule.y();
               if (var10 != null) {
                  var10.w();
               }
            }

            if (M != null) {
               M.j();
            }

            q8.u("Render", "Cosmetic stage=" + var0 + "; isolated until resource reload", var4);
         }
      }
   }

   static boolean J(String var0) {
      return !U(var0);
   }

   public static boolean U(String var0) {
      return l != t_.g || !w.contains(var0);
   }

   private static void v(String var0, f1 var1, Runnable var2) {
      y(var0, () -> {
         try {
            var2.run();
         } finally {
            var1.I(false);
            var1.blend(ti.d.G);
         }
      });
   }

   private static void U(WorldExtractionContext var0) {
      b9.W(var0.frustum());
      g = var0.worldState();
      B = (boolean)0;

      for (uu var2 : q) {
         y(var2.V(), () -> var2.U(var0));
      }

      U = var0.tickCounter().method_60637(false);
      class_12075 var17 = g.field_63082;
      c1.u(var17);
      f5.i(U, var17);
      PlayerModelModule var18 = PlayerModelModule.h();
      if (var18 != null) {
         y("PlayerModel", () -> var18.w(U, var17.field_63078));
      }

      JumpFXModule var3 = JumpFXModule.p();
      if (var3 != null) {
         y("JumpFX", var3::I);
      }

      KillFXModule var4 = KillFXModule.y();
      if (var4 != null) {
         y("KillFX", () -> var4.j(var17));
      }

      CrystalChamsModule var5 = CrystalChamsModule.X();
      if (var5 != null) {
         y("CrystalChams", () -> var5.E(U));
      }

      PetsModule var6 = PetsModule.F();
      if (var6 != null) {
         y("Pets", () -> var6.V(U, var17));
      }

      CryptoPetsModule var7 = CryptoPetsModule.M();
      if (var7 != null) {
         y("CryptoPets", () -> var7.I(U, var17));
      }

      y("EmoteProps", () -> is.P(U));
      SnowGroundModule var8 = SnowGroundModule.R();
      if (var8 != null) {
         y("SnowGround", () -> var8.J(U, var17));
      }

      y("Trails", () -> L.j(var17.field_63078, U));
      y("Body Trails", () -> O.n(var17.field_63078, U));
      y("GhostEcho", () -> h.c(var17.field_63078));
      y("Comets", () -> e_.i(U));
      y("HitZone", an::h);
      TargetAuraModule var9 = TargetAuraModule.p();
      if (var9 != null) {
         y("TargetAura", () -> var9.G(U));
      }

      y("Particles", () -> i.c(var17.field_63078, U, var0.frustum()));
      AmbientParticlesModule var10 = AmbientParticlesModule.q();
      if (var10 != null && var10.isOperational()) {
         y("Ambient Particles", () -> var10.r().G(var17.field_63078, U, var0.frustum()));
      }

      ChamsModule var11 = ChamsModule.K();
      if (var11 != null) {
         y("Chams", () -> var11.t(U));
      }

      ChinaHatModule var12 = ChinaHatModule.h();
      if (var12 != null) {
         y("ChinaHat", () -> var12.d(U));
      }

      WingsModule var13 = WingsModule.B();
      if (var13 != null) {
         y("Wings", () -> var13.d(U));
      }

      DamageMarkersModule var14 = DamageMarkersModule.g();
      if (var14 != null) {
         y("DamageMarkers", () -> var14.k(var17));
      }

      WorldLyricsModule var15 = WorldLyricsModule.k();
      if (var15 != null) {
         y("WorldLyrics", () -> var15.Y(var17));
      }

      K = null;
      SelfVisualsModule var16 = SelfVisualsModule.o();
      d = null;
      u = (boolean)0;
      k = 0.0;
      if (var16 != null && var16.Q() && var16.T()) {
         y("SelfVisuals", () -> {
            K = gv.i(class_310.method_1551().field_1724, U);
            d = !var16.s() ? null : (var16.A() == null ? gv.y.V : var16.A());
            S = var16.S();
            t = ez.a;
            k = var16.d() && var16.j.getValue() ? var16.r.getValue() : 0.0;
            u = (boolean)(!var16.s() || !var16.z.getValue() || var16.d() && b0.r == t_.t.A ? 0 : 1);
         });
      }
   }

   public static boolean l() {
      return !c;
   }

   public static boolean L(class_10017 var0) {
      return K != null && d != null && g != null && g.field_61735.contains(var0);
   }

   public static void f() {
      f5.X(LarpClient.b().G().M());
      q = LarpClient.b().G().M().stream().filter(var0 -> var0 instanceof uu.m).map(var0 -> ((uu.m)var0).placementHighlight()).toList();
      ClientTickEvents.END_CLIENT_TICK.register((EndTick)var0 -> {
         JumpFXModule var1 = JumpFXModule.p();
         if (var1 != null && !var0.method_1493()) {
            var1.v();
         }

         if (!var0.method_1493()) {
            f5.X(var0);
         }
      });
      ClientTickEvents.START_CLIENT_TICK.register((StartTick)var0 -> {
         sg.N();
         if (var0.field_1687 != N) {
            f5.V();
            y("Scene resource cleanup", h7::Q);
            y("Refraction cleanup", gr::H);
            jf.o();
            hi.c();
            sg.E();
            CrystalChamsModule var1 = CrystalChamsModule.X();
            if (var1 != null) {
               var1.g();
            }

            PetsModule var2 = PetsModule.F();
            if (var2 != null) {
               var2.p();
            }

            CryptoPetsModule var3 = CryptoPetsModule.M();
            if (var3 != null) {
               var3.M();
            }

            DamageMarkersModule var4 = DamageMarkersModule.g();
            if (var4 != null) {
               var4.H();
            }

            WorldLyricsModule var5 = WorldLyricsModule.k();
            if (var5 != null) {
               var5.y();
            }

            KillFXModule var6 = KillFXModule.y();
            if (var6 != null) {
               var6.w();
            }

            q.forEach(uu::p);
            N = var0.field_1687;
            g = null;
            K = null;
            i.q();
            L.f();
            O.d();
            h.e();
            e_.Y();
            AmbientParticlesModule var7 = AmbientParticlesModule.q();
            if (var7 != null) {
               var7.a();
            }

            JumpFXModule var8 = JumpFXModule.p();
            if (var8 != null) {
               var8.w();
            }

            BlockOverlayModule var9 = BlockOverlayModule.Q();
            if (var9 != null) {
               var9.Z();
            }

            cf.z();
            bi.M();
            sm.X();
            ww.E();
            y1.P();
            lj.m();
            gv.A();
            ChamsModule var10 = ChamsModule.K();
            if (var10 != null) {
               var10.n();
            }

            PlayerModelModule var11 = PlayerModelModule.h();
            if (var11 != null) {
               var11.B();
            }

            z = null;
            com.larpclient.k.q();
            com.larpclient.k.Y();
            q8.v("Render", "World changed; transient effects cleared");
         }

         if (!var0.method_1493()) {
            qo.q();
            h.D();
            i.o(1.0, (var1x, var2x) -> i3.c(var0.field_1687, var1x, var2x));
            L.b();
            O.n();
            KillFXModule var12 = KillFXModule.y();
            if (var12 != null) {
               y("KillFX", () -> var12.j());
            }
         }
      });
      WorldRenderEvents.AFTER_BLOCK_OUTLINE_EXTRACTION.register((AfterBlockOutlineExtraction)(var0, var1) -> {
         BlockOverlayModule var2 = BlockOverlayModule.Q();
         if (var2 != null) {
            var2.b(var0, var1);
         }
      });
      WorldRenderEvents.BEFORE_BLOCK_OUTLINE.register((BeforeBlockOutline)(var0, var1) -> {
         BlockOverlayModule var2 = BlockOverlayModule.Q();
         return var2 == null || wi.E() || !var2.n(var1.comp_4932(), var0.worldState());
      });
      WorldRenderEvents.END_EXTRACTION.register(wc::U);
      WorldRenderEvents.BEFORE_ENTITIES.register((BeforeEntities)var0 -> wi.I());
      WorldRenderEvents.BEFORE_TRANSLUCENT.register((BeforeTranslucent)var0 -> wi.i(() -> X(var0)));
      WorldRenderEvents.END_MAIN.register((EndMain)var0 -> wi.o(var0, () -> B(var0)));
      sm.O();
      ClientLifecycleEvents.CLIENT_STOPPING.register((ClientStopping)var0 -> {
         wi.H();
         f5.V();
         y("Scene resource cleanup", h7::Q);
         y("Refraction cleanup", gr::H);
         jf.o();
         sg.E();
         DamageMarkersModule var1 = DamageMarkersModule.g();
         if (var1 != null) {
            var1.H();
         }

         WorldLyricsModule var2 = WorldLyricsModule.k();
         if (var2 != null) {
            var2.C();
         }

         KillFXModule var3 = KillFXModule.y();
         if (var3 != null) {
            var3.w();
         }

         q.forEach(uu::p);
         t_.b();
         lj.m();
         sm.X();
         ww.E();
         y1.P();
         ChamsModule var4 = ChamsModule.K();
         if (var4 != null) {
            var4.n();
         }

         h.e();
         e_.Y();
         AmbientParticlesModule var5 = AmbientParticlesModule.q();
         if (var5 != null) {
            var5.a();
         }

         JumpFXModule var6 = JumpFXModule.p();
         if (var6 != null) {
            var6.w();
         }

         BlockOverlayModule var7 = BlockOverlayModule.Q();
         if (var7 != null) {
            var7.Z();
         }

         LineGlyphsModule var8 = LineGlyphsModule.a();
         if (var8 != null) {
            var8.s();
         }

         PearlParticlesModule var9 = PearlParticlesModule.U();
         if (var9 != null) {
            var9.R();
         }

         SkyShaderModule var10 = SkyShaderModule.y();
         if (var10 != null) {
            var10.w();
         }

         SnowGroundModule var11 = SnowGroundModule.R();
         if (var11 != null) {
            var11.L();
         }

         ly.y();
         tg.d();
         te.S();
         lr.v();
         if (M != null) {
            M.close();
         }
      });
      q8.G("Render", "Shared effects initialized; depth-tested ribbons, particle pool, bloom and blur");
   }

   public static void P(class_1309 var0) {
      t(var0);
      HitFXModule var1 = HitFXModule.H();
      if (var1 != null && var1.isOperational()) {
         var1.u(var0);
      }
   }

   public static void t(class_1309 var0) {
      z = var0;
      f = qo.K();
   }

   public static class_1309 X() {
      return z != null && z.method_5805() && z.method_73183() == class_310.method_1551().field_1687 && qo.K() - f < 3.0 ? z : null;
   }

   private static void H() {
      h7.x();
      gr.v();
      if (j != t_.g) {
         j = t_.g;
         f2.a(f2.L);
         f1.L();
      }
   }

   private static void X(WorldRenderContext var0) {
      if (j != t_.g) {
         c = (boolean)0;
      }

      if (!c && var0.matrices() != null && var0.worldState() == g) {
         if (class_310.method_1551().field_1687 != null) {
            if (M == null) {
               M = new cw();
            }

            GpuTextureView var1 = RenderSystem.outputColorTextureOverride;
            GpuTextureView var2 = RenderSystem.outputDepthTextureOverride;
            String var3 = "shader initialization before translucent terrain";

            try {
               H();
               B = (boolean)1;
               class_10017 var4 = K;
               var3 = "SelfVisuals outline mask / player material before translucent terrain";
               m(var0, var4);
               var3 = "Comets before translucent terrain";
               f1 var5 = new f1(var0.matrices(), M, var0.worldState().field_63082);
               v("Comets", var5, () -> {
                  e_.p(var5, false);
                  M.P();
               });
               h7.G();
               h7.c();
               if (var4 != null && u || e_.C()) {
                  h7.S();
               }
            } catch (RuntimeException var9) {
               c = (boolean)1;
               q8.u("Render", "VisualRuntime stage=" + var3 + "; output attachments restored; retry on resource reload", var9);
            } finally {
               RenderSystem.outputColorTextureOverride = var1;
               RenderSystem.outputDepthTextureOverride = var2;
            }
         }
      }
   }

   private static void m(WorldRenderContext var0, class_10017 var1) {
      if (var1 != null && !Boolean.valueOf(B).equals(a)) {
         a = B;
         if (B) {
            q8.G("SelfVisuals", "Drawing before translucent terrain; glass, water and ice blend over the player");
         } else {
            q8.L("SelfVisuals", "Hook before translucent terrain did not run; drawing at END_MAIN, where glass hides the player");
         }
      }

      y("SelfVisuals outline", () -> ww.I(var0.matrices(), var1));
      if (var1 != null && d == gv.y.D && k > 0.0 && c1.s()) {
         y("Liquid Glass refraction", () -> gr.b("Liquid Glass", () -> {
            try {
               gv.o(var1, var0.matrices(), M, gv.y.C, -1, S, true, false);
               M.P();
            } finally {
               M.j();
            }
         }, k));
      }

      if (var1 != null && d != null && (d != gv.y.D || c1.s())) {
         y("SelfVisuals", () -> {
            long var2 = c5.X();
            gv.x(var1, var0.matrices(), M, d, t, S, true);
            M.P();
            h7.G();
            h7.c();
            c5.w("SelfVisuals body material", var2);
         });
      }
   }

   private static void B(WorldRenderContext var0) {
      if (j != t_.g) {
         c = (boolean)0;
      }

      if (!c && var0.matrices() != null && var0.worldState() == g) {
         class_310 var1 = class_310.method_1551();
         if (var1.field_1687 != null) {
            if (M == null) {
               M = new cw();
            }

            GpuTextureView var2 = RenderSystem.outputColorTextureOverride;
            GpuTextureView var3 = RenderSystem.outputDepthTextureOverride;
            String var4 = "shader initialization";

            try {
               if (B) {
                  h7.G();
                  h7.c();
               } else {
                  H();
               }

               float var5 = U;
               f1 var6 = new f1(var0.matrices(), M, var0.worldState().field_63082);
               var4 = "Wings Ender Dragon opaque pass";
               WingsModule var7 = WingsModule.B();
               if (var7 != null && var7.isOperational() && var7.j()) {
                  y("Wings", () -> {
                     if (var7.L(var0.matrices(), M)) {
                        M.P();
                        h7.G();
                        h7.c();
                     }
                  });
               }

               AmbientParticlesModule var8 = AmbientParticlesModule.q();
               int var9 = var8 != null && var8.isOperational() && !w.contains("Ambient Particles") ? 1 : 0;
               lj.s(!w.contains("Particles") && i.A() || var9 != 0 && var8.r().y() || f5.s());
               var4 = "Particles/Trails/Body Trails/TargetAura/GhostEcho/Comets/HitZone/BlockOverlay base geometry";
               o(var0, var6, var5, false);
               PlayerModelModule var10 = PlayerModelModule.h();
               if (var10 != null) {
                  y("PlayerModel", () -> var10.J(var0.matrices(), M));
               }

               OrderDropperModule var11 = OrderDropperModule.I();
               if (var11 != null) {
                  y("Order Dropper", () -> var11.M(var0.matrices(), M, var0.worldState().field_63082.field_63078));
               }

               M.P();
               h7.G();
               h7.c();
               var4 = "Chams nearest-surface gradient silhouette";
               ChamsModule var12 = ChamsModule.K();
               if (var12 != null) {
                  y("Chams", () -> wi.D(() -> var12.N(var0.matrices(), M, var5)));
               }

               class_10017 var13 = K;
               var4 = "SelfVisuals outline mask / player material";
               if (!B) {
                  m(var0, var13);
               }

               TargetAuraModule var14 = TargetAuraModule.p();
               DamageMarkersModule var15 = DamageMarkersModule.g();
               if (var15 != null) {
                  y("DamageMarkers", () -> var15.N(var0.matrices(), var0.worldState().field_63082));
               }

               WorldLyricsModule var16 = WorldLyricsModule.k();
               if (var16 != null) {
                  y("WorldLyrics", () -> var16.p(var0.matrices(), var0.worldState().field_63082, false));
               }

               JumpFXModule var17 = JumpFXModule.p();
               BlockOverlayModule var18 = BlockOverlayModule.Q();
               KillFXModule var19 = KillFXModule.y();
               var4 = "ChinaHat after player materials";
               ChinaHatModule var20 = ChinaHatModule.h();
               if (var20 != null) {
                  y("ChinaHat", () -> {
                     var20.R(var6, var5);
                     M.P();
                  });
               }

               var4 = "Wings after player materials";
               if (var7 != null && var7.isOperational() && !var7.j()) {
                  v("Wings", var6, () -> var7.C(var6, M));
               }

               class_276 var21 = !f5.p()
                     && !E(qo.x())
                     && (w.contains("Particles") || !i.o())
                     && (var9 == 0 || !var8.r().f())
                     && (w.contains("Trails") || !L.x())
                     && (w.contains("Body Trails") || !O.U())
                     && (w.contains("BlockOverlay") || var18 == null || !var18.i(var0.worldState()))
                     && !q.stream().anyMatch(var1x -> !w.contains(var1x.V()) && var1x.b(var0.worldState()))
                     && (w.contains("Comets") || !e_.C())
                     && (w.contains("JumpFX") || var17 == null || !var17.n())
                     && (w.contains("KillFX") || var19 == null || !var19.E())
                     && (w.contains("HitZone") || !an.v())
                     && (w.contains("GhostEcho") || !h.h())
                     && (w.contains("TargetAura") || var14 == null || !var14.z())
                     && (w.contains("SelfVisuals") || var13 == null || !u || !U("SelfVisuals bloom"))
                     && (w.contains("Wings") || var7 == null || !var7.k())
                     && (w.contains("ChinaHat") || var20 == null || !var20.p())
                     && (w.contains("WorldLyrics") || var16 == null || !var16.S())
                  ? null
                  : t_.m();
               var4 = "explicit bloom mask / composite";
               if (var21 != null) {
                  RenderSystem.outputColorTextureOverride = var21.method_71639();
                  RenderSystem.outputDepthTextureOverride = var21.method_71640();
                  o(var0, var6, var5, true);
                  if (var16 != null && var16.S()) {
                     y("WorldLyrics", () -> var16.p(var0.matrices(), var0.worldState().field_63082, true));
                  }

                  if (var7 != null && var7.k()) {
                     v("Wings", var6, () -> var7.i(var6, M));
                  }

                  if (var20 != null && !w.contains("ChinaHat") && var20.p()) {
                     y("ChinaHat", () -> var20.A(var6, M));
                  }

                  GpuTextureView var22 = B ? h7.K() : null;
                  if (var22 != null) {
                     RenderSystem.outputDepthTextureOverride = var22;
                  }

                  if (B) {
                     v("Comets", var6, () -> {
                        e_.p(var6, true);
                        M.P();
                     });
                  }

                  if (var13 != null && u) {
                     y("SelfVisuals bloom", () -> {
                        gv.o(var13, var0.matrices(), M, d == gv.y.D ? gv.y.w : d, t, d == gv.y.D ? S * 0.25 : 0.25, true, false);
                        M.P();
                     });
                  }

                  RenderSystem.outputColorTextureOverride = var2;
                  RenderSystem.outputDepthTextureOverride = var3;
                  t_.s();
               }
            } catch (RuntimeException var28) {
               c = (boolean)1;
               q8.u("Render", "VisualRuntime stage=" + var4 + "; output attachments restored; retry on resource reload", var28);
            } finally {
               RenderSystem.outputColorTextureOverride = var2;
               RenderSystem.outputDepthTextureOverride = var3;
               DamageMarkersModule var24 = DamageMarkersModule.g();
               if (var24 != null) {
                  var24.R();
               }

               WorldLyricsModule var25 = WorldLyricsModule.k();
               if (var25 != null) {
                  var25.n();
               }

               if (M != null) {
                  M.W();
               }

               y("Refraction cleanup", gr::z);
               y("Scene resource cleanup", h7::d);
            }
         }
      }
   }

   public static void G(class_332 var0) {
      if (l()) {
         PetsModule var1 = PetsModule.F();
         if (var1 != null) {
            y("Pets", () -> var1.i(var0));
         }
      }
   }

   private static boolean E(double var0) {
      LineGlyphsModule var2 = LineGlyphsModule.a();
      PearlParticlesModule var3 = PearlParticlesModule.U();
      CrystalChamsModule var4 = CrystalChamsModule.X();
      PetsModule var5 = PetsModule.F();
      CryptoPetsModule var6 = CryptoPetsModule.M();
      return !w.contains("Pets") && var5 != null && var5.D()
         || !w.contains("CryptoPets") && var6 != null && var6.r()
         || !w.contains("CrystalChams") && var4 != null && var4.U()
         || !w.contains("LineGlyphs") && var2 != null && var2.D(var0)
         || !w.contains("PearlParticles") && var3 != null && var3.l(var0);
   }

   private static void o(WorldRenderContext var0, f1 var1, float var2, boolean var3) {
      cw var4 = M;
      f5.r(var1, var3);

      for (uu var6 : q) {
         v(var6.V(), var1, () -> var6.G(var1, var3, var0.worldState()));
      }

      SnowGroundModule var16 = SnowGroundModule.R();
      if (!var3 && var16 != null) {
         v("SnowGround", var1, () -> var16.E(var1));
      }

      CrystalChamsModule var17 = CrystalChamsModule.X();
      if (var17 != null) {
         v("CrystalChams", var1, () -> var17.u(var1, var3));
      }

      PetsModule var7 = PetsModule.F();
      if (var7 != null) {
         v("Pets", var1, () -> var7.p(var1, var3));
      }

      CryptoPetsModule var8 = CryptoPetsModule.M();
      if (var8 != null) {
         v("CryptoPets", var1, () -> var8.m(var1, var3));
      }

      v("EmoteProps", var1, () -> is.j(var1, var3));
      v("Particles", var1, () -> i.v(var1, var0.worldState().field_63082.field_63078, var2, var3));
      v("Trails", var1, () -> L.t(var1, var2, var3));
      v("Body Trails", var1, () -> O.l(var1, var3));
      if (!B) {
         v("Comets", var1, () -> e_.p(var1, var3));
      }

      AmbientParticlesModule var9 = AmbientParticlesModule.q();
      if (var9 != null && var9.isOperational()) {
         v("Ambient Particles", var1, () -> var9.r().E(var1, var0.worldState().field_63082.field_63078, var2, var3));
      }

      JumpFXModule var10 = JumpFXModule.p();
      if (var10 != null) {
         v("JumpFX", var1, () -> var10.Y(var1, var3));
      }

      KillFXModule var11 = KillFXModule.y();
      if (var11 != null) {
         v("KillFX", var1, () -> var11.E(var1, var0.matrices(), var4, var3));
      }

      BlockOverlayModule var12 = BlockOverlayModule.Q();
      if (var12 != null) {
         v("BlockOverlay", var1, () -> var12.E(var1, var3, var0.worldState()));
      }

      v("HitZone", var1, () -> an.b(var1, var3));
      TargetAuraModule var13 = TargetAuraModule.p();
      if (var13 != null) {
         v("TargetAura", var1, () -> var13.G(var1, var2, var3));
      }

      v("GhostEcho", var1, () -> h.I(var0.matrices(), var4, var3));
      LineGlyphsModule var14 = LineGlyphsModule.a();
      if (var14 != null) {
         y("LineGlyphs", () -> var14.d(var0.matrices(), var4, var0.worldState().field_63082, qo.x(), var3));
      }

      PearlParticlesModule var15 = PearlParticlesModule.U();
      if (var15 != null) {
         y("PearlParticles", () -> var15.L(var0.matrices(), var4, var0.worldState().field_63082, qo.x(), var3));
      }

      y("Shared geometry", var4::P);
   }
}
