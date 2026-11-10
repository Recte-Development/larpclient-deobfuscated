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
import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.renderpearl.api.textures.GpuTextureView;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents.ClientStopping;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents.EndTick;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents.StartTick;
import net.fabricmc.fabric.api.client.rendering.v1.level.LevelExtractionContext;
import net.fabricmc.fabric.api.client.rendering.v1.level.LevelExtractionEvents;
import net.fabricmc.fabric.api.client.rendering.v1.level.LevelRenderContext;
import net.fabricmc.fabric.api.client.rendering.v1.level.LevelRenderEvents;
import net.fabricmc.fabric.api.client.rendering.v1.level.LevelExtractionEvents.AfterBlockOutlineExtraction;
import net.fabricmc.fabric.api.client.rendering.v1.level.LevelRenderEvents.AfterOpaqueTerrain;
import net.fabricmc.fabric.api.client.rendering.v1.level.LevelRenderEvents.BeforeBlockOutline;
import net.fabricmc.fabric.api.client.rendering.v1.level.LevelRenderEvents.BeforeTranslucentTerrain;
import net.fabricmc.fabric.api.client.rendering.v1.level.LevelRenderEvents.EndMain;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.state.level.LevelRenderState;
import net.minecraft.world.entity.LivingEntity;

public final class wp {
   public static final l Q = new l();
   public static final vw Y = new vw();
   public static final ws S = new ws();
   public static final pw V = new pw();
   private static List<ai> b = List.of();
   private static wh q;
   private static ClientLevel t;
   private static boolean P;
   private static int W = -1;
   private static LivingEntity i;
   private static double M;
   private static LevelRenderState k;
   private static float v;
   private static EntityRenderState E;
   private static w7.b l;
   private static double n;
   private static double o;
   private static int p;
   private static boolean A;
   private static boolean R;
   private static Boolean G;
   private static final Set<String> X = new HashSet<>();
   private static int C = -1;

   static void X(String var0, Runnable var1) {
      if (C != nl.q) {
         X.clear();
         C = nl.q;
      }

      if (!X.contains(var0)) {
         try {
            var1.run();
         } catch (RuntimeException var4) {
            X.add(var0);
            q3.L(var0);
            if (var0.equals("Chams")) {
               ChamsModule var3 = ChamsModule.O();
               if (var3 != null) {
                  var3.g();
               }
            }

            if (var0.equals("CrystalChams")) {
               CrystalChamsModule var5 = CrystalChamsModule.W();
               if (var5 != null) {
                  var5.r();
               }
            }

            if (var0.equals("Pets")) {
               PetsModule var6 = PetsModule.X();
               if (var6 != null) {
                  var6.l();
               }
            }

            if (var0.equals("CryptoPets")) {
               CryptoPetsModule var7 = CryptoPetsModule.v();
               if (var7 != null) {
                  var7.Y();
               }
            }

            if (var0.equals("EmoteProps")) {
               iz.O();
            }

            if (var0.equals("PlayerModel")) {
               PlayerModelModule var8 = PlayerModelModule.k();
               if (var8 != null) {
                  var8.m();
               }
            }

            if (var0.equals("SnowGround")) {
               SnowGroundModule var9 = SnowGroundModule.q();
               if (var9 != null) {
                  var9.n();
               }
            }

            if (var0.equals("KillFX")) {
               KillFXModule var10 = KillFXModule.J();
               if (var10 != null) {
                  var10.D();
               }
            }

            if (q != null) {
               q.R();
            }

            _q.o("Render", "Cosmetic stage=" + var0 + "; isolated until resource reload", var4);
         }
      }
   }

   static boolean M(String var0) {
      return !Z(var0);
   }

   public static boolean Z(String var0) {
      return C != nl.q || !X.contains(var0);
   }

   private static void Y(String var0, fz var1, Runnable var2) {
      X(var0, () -> {
         try {
            var2.run();
         } finally {
            var1.x(false);
            var1.blend(_0.v.z);
         }
      });
   }

   private static void d(LevelExtractionContext var0) {
      k = var0.levelState();
      R = (boolean)0;

      for (ai var2 : b) {
         X(var2.M(), () -> var2.v(var0));
      }

      v = var0.deltaTracker().getGameTimeDeltaPartialTick(false);
      CameraRenderState var17 = k.cameraRenderState;
      py.M(var17);
      q3.o(v, var17);
      PlayerModelModule var18 = PlayerModelModule.k();
      if (var18 != null) {
         X("PlayerModel", () -> var18.W(v, var17.pos));
      }

      JumpFXModule var3 = JumpFXModule.f();
      if (var3 != null) {
         X("JumpFX", var3::X);
      }

      KillFXModule var4 = KillFXModule.J();
      if (var4 != null) {
         X("KillFX", () -> var4.N(var17));
      }

      CrystalChamsModule var5 = CrystalChamsModule.W();
      if (var5 != null) {
         X("CrystalChams", () -> var5.d(v));
      }

      PetsModule var6 = PetsModule.X();
      if (var6 != null) {
         X("Pets", () -> var6.f(v, var17));
      }

      CryptoPetsModule var7 = CryptoPetsModule.v();
      if (var7 != null) {
         X("CryptoPets", () -> var7.P(v, var17));
      }

      X("EmoteProps", () -> iz.Y(v));
      SnowGroundModule var8 = SnowGroundModule.q();
      if (var8 != null) {
         X("SnowGround", () -> var8.E(v, var17));
      }

      X("Trails", () -> Y.J(var17.pos, v));
      X("Body Trails", () -> S.J(var17.pos, v));
      X("GhostEcho", () -> V.l(var17.pos));
      X("Comets", () -> d6.Z(v));
      X("HitZone", com.larpclient.module.s::r);
      TargetAuraModule var9 = TargetAuraModule.v();
      if (var9 != null) {
         X("TargetAura", () -> var9.Q(v));
      }

      X("Particles", () -> Q.p(var17.pos, v, var17.cullFrustum));
      AmbientParticlesModule var10 = AmbientParticlesModule.w();
      if (var10 != null && var10.isOperational()) {
         X("Ambient Particles", () -> var10.L().Q(var17.pos, v, var17.cullFrustum));
      }

      ChamsModule var11 = ChamsModule.O();
      if (var11 != null) {
         X("Chams", () -> var11.j(v));
      }

      ChinaHatModule var12 = ChinaHatModule.E();
      if (var12 != null) {
         X("ChinaHat", () -> var12.f(v));
      }

      WingsModule var13 = WingsModule.i();
      if (var13 != null) {
         X("Wings", () -> var13.l(v));
      }

      DamageMarkersModule var14 = DamageMarkersModule.D();
      if (var14 != null) {
         X("DamageMarkers", () -> var14.w(var17));
      }

      WorldLyricsModule var15 = WorldLyricsModule.f();
      if (var15 != null) {
         X("WorldLyrics", () -> var15.N(var17));
      }

      E = null;
      SelfVisualsModule var16 = SelfVisualsModule.v();
      l = null;
      A = (boolean)0;
      o = 0.0;
      if (var16 != null && var16.g() && var16.k()) {
         X("SelfVisuals", () -> {
            E = w7.u(Minecraft.getInstance().player, v);
            l = !var16.l() ? null : (var16.E() == null ? w7.b.E : var16.E());
            n = var16.v();
            p = c.y;
            o = var16.m() && var16.P.getValue() ? var16.t.getValue() : 0.0;
            A = (boolean)(!var16.l() || !var16.U.getValue() || var16.m() && a4.h == nl.q.q ? 0 : 1);
         });
      }
   }

   public static boolean Q() {
      return !P;
   }

   public static boolean i(EntityRenderState var0) {
      return E != null && l != null && k != null && k.entityRenderStates.contains(var0);
   }

   public static void T() {
      q3.C(LarpClient.N().j().d());
      b = LarpClient.N().j().d().stream().filter(var0 -> var0 instanceof ai.e).map(var0 -> ((ai.e)var0).placementHighlight()).toList();
      ClientTickEvents.END_CLIENT_TICK.register((EndTick)var0 -> {
         JumpFXModule var1 = JumpFXModule.f();
         if (var1 != null && !var0.isPaused()) {
            var1.m();
         }

         if (!var0.isPaused()) {
            q3.X(var0);
         }
      });
      ClientTickEvents.START_CLIENT_TICK.register((StartTick)var0 -> {
         oj.B();
         if (var0.level != t) {
            q3.z();
            X("Scene resource cleanup", _s::m);
            X("Refraction cleanup", bq::r);
            pm.C();
            com.larpclient.v.H();
            oj.G();
            CrystalChamsModule var1 = CrystalChamsModule.W();
            if (var1 != null) {
               var1.r();
            }

            PetsModule var2 = PetsModule.X();
            if (var2 != null) {
               var2.l();
            }

            CryptoPetsModule var3 = CryptoPetsModule.v();
            if (var3 != null) {
               var3.Y();
            }

            DamageMarkersModule var4 = DamageMarkersModule.D();
            if (var4 != null) {
               var4.i();
            }

            WorldLyricsModule var5 = WorldLyricsModule.f();
            if (var5 != null) {
               var5.y();
            }

            KillFXModule var6 = KillFXModule.J();
            if (var6 != null) {
               var6.D();
            }

            b.forEach(ai::m);
            t = var0.level;
            k = null;
            E = null;
            Q.k();
            Y.Y();
            S.v();
            V.t();
            d6.N();
            AmbientParticlesModule var7 = AmbientParticlesModule.w();
            if (var7 != null) {
               var7.a();
            }

            JumpFXModule var8 = JumpFXModule.f();
            if (var8 != null) {
               var8.x();
            }

            BlockOverlayModule var9 = BlockOverlayModule.c();
            if (var9 != null) {
               var9.N();
            }

            br.V();
            u.e();
            bj.t();
            h9.L();
            d3.X();
            w7.A();
            ChamsModule var10 = ChamsModule.O();
            if (var10 != null) {
               var10.g();
            }

            PlayerModelModule var11 = PlayerModelModule.k();
            if (var11 != null) {
               var11.Y();
            }

            i = null;
            dk.X();
            dk.C();
            _q.c("Render", "World changed; transient effects cleared");
         }

         if (!var0.isPaused()) {
            hz.A();
            V.R();
            Q.G(1.0, (var1x, var2x) -> v9.u(var0.level, var1x, var2x));
            Y.l();
            S.o();
            KillFXModule var12 = KillFXModule.J();
            if (var12 != null) {
               X("KillFX", () -> var12.a());
            }
         }
      });
      LevelExtractionEvents.AFTER_BLOCK_OUTLINE_EXTRACTION.register((AfterBlockOutlineExtraction)(var0, var1) -> {
         BlockOverlayModule var2 = BlockOverlayModule.c();
         if (var2 != null) {
            var2.e(var0, var1);
         }
      });
      LevelRenderEvents.BEFORE_BLOCK_OUTLINE.register((BeforeBlockOutline)(var0, var1) -> {
         BlockOverlayModule var2 = BlockOverlayModule.c();
         return var2 == null || qc.w() || !var2.R(var1.pos(), var0.levelState());
      });
      LevelExtractionEvents.END_EXTRACTION.register(wp::d);
      LevelRenderEvents.AFTER_OPAQUE_TERRAIN.register((AfterOpaqueTerrain)var0 -> qc.b());
      LevelRenderEvents.BEFORE_TRANSLUCENT_TERRAIN.register((BeforeTranslucentTerrain)var0 -> qc.C(() -> F(var0)));
      LevelRenderEvents.END_MAIN.register((EndMain)var0 -> qc.m(var0, () -> T(var0)));
      bj.K();
      ClientLifecycleEvents.CLIENT_STOPPING.register((ClientStopping)var0 -> {
         qc.E();
         q3.z();
         X("Scene resource cleanup", _s::m);
         X("Refraction cleanup", bq::r);
         pm.C();
         oj.G();
         DamageMarkersModule var1 = DamageMarkersModule.D();
         if (var1 != null) {
            var1.i();
         }

         WorldLyricsModule var2 = WorldLyricsModule.f();
         if (var2 != null) {
            var2.i();
         }

         KillFXModule var3 = KillFXModule.J();
         if (var3 != null) {
            var3.D();
         }

         b.forEach(ai::m);
         nl.y();
         d3.X();
         bj.t();
         h9.L();
         ChamsModule var4 = ChamsModule.O();
         if (var4 != null) {
            var4.g();
         }

         V.t();
         d6.N();
         AmbientParticlesModule var5 = AmbientParticlesModule.w();
         if (var5 != null) {
            var5.a();
         }

         JumpFXModule var6 = JumpFXModule.f();
         if (var6 != null) {
            var6.x();
         }

         BlockOverlayModule var7 = BlockOverlayModule.c();
         if (var7 != null) {
            var7.N();
         }

         LineGlyphsModule var8 = LineGlyphsModule.d();
         if (var8 != null) {
            var8.z();
         }

         PearlParticlesModule var9 = PearlParticlesModule.w();
         if (var9 != null) {
            var9.j();
         }

         SkyShaderModule var10 = SkyShaderModule.R();
         if (var10 != null) {
            var10.c();
         }

         SnowGroundModule var11 = SnowGroundModule.q();
         if (var11 != null) {
            var11.U();
         }

         na.H();
         v1.P();
         wf.F();
         _9.u();
         if (q != null) {
            q.close();
         }
      });
      _q.z("Render", "Shared effects initialized; depth-tested ribbons, particle pool, bloom and blur");
   }

   public static void W(LivingEntity var0) {
      S(var0);
      HitFXModule var1 = HitFXModule.U();
      if (var1 != null && var1.isOperational()) {
         var1.m(var0);
      }
   }

   public static void S(LivingEntity var0) {
      i = var0;
      M = hz.S();
   }

   public static LivingEntity b() {
      return i != null && i.isAlive() && i.level() == Minecraft.getInstance().level && hz.S() - M < 3.0 ? i : null;
   }

   private static void O() {
      _s.v();
      bq.K();
      if (W != nl.q) {
         W = nl.q;
         se.j(se.Q);
         fz.C();
      }
   }

   private static void F(LevelRenderContext var0) {
      if (W != nl.q) {
         P = (boolean)0;
      }

      if (!P && var0.poseStack() != null && var0.levelState() == k) {
         if (Minecraft.getInstance().level != null) {
            if (q == null) {
               q = new wh();
            }

            GpuTextureView var1 = jf.b;
            GpuTextureView var2 = jf.q;
            String var3 = "shader initialization before translucent terrain";

            try {
               O();
               R = (boolean)1;
               EntityRenderState var4 = E;
               var3 = "SelfVisuals outline mask / player material before translucent terrain";
               Q(var0, var4);
               var3 = "Comets before translucent terrain";
               fz var5 = new fz(var0.poseStack(), q, var0.levelState().cameraRenderState);
               Y("Comets", var5, () -> {
                  d6.G(var5, false);
                  q.S();
               });
               _s.g();
               _s.i();
               if (var4 != null && A || d6.F()) {
                  _s.b();
               }
            } catch (RuntimeException var9) {
               P = (boolean)1;
               _q.o("Render", "VisualRuntime stage=" + var3 + "; output attachments restored; retry on resource reload", var9);
            } finally {
               jf.b = var1;
               jf.q = var2;
            }
         }
      }
   }

   private static void Q(LevelRenderContext var0, EntityRenderState var1) {
      if (var1 != null && !Boolean.valueOf(R).equals(G)) {
         G = R;
         if (R) {
            _q.z("SelfVisuals", "Drawing before translucent terrain; glass, water and ice blend over the player");
         } else {
            _q.R("SelfVisuals", "Hook before translucent terrain did not run; drawing at END_MAIN, where glass hides the player");
         }
      }

      X("SelfVisuals outline", () -> h9.W(var0.poseStack(), var1));
      if (var1 != null && l == w7.b.I && o > 0.0 && py.Q()) {
         X("Liquid Glass refraction", () -> bq.W("Liquid Glass", () -> {
            try {
               w7.r(var1, var0.poseStack(), q, w7.b.T, -1, n, true, false);
               q.S();
            } finally {
               q.R();
            }
         }, o));
      }

      if (var1 != null && l != null && (l != w7.b.I || py.Q())) {
         X("SelfVisuals", () -> {
            long var2 = he.s();
            w7.Q(var1, var0.poseStack(), q, l, p, n, true);
            q.S();
            _s.g();
            _s.i();
            he.T("SelfVisuals body material", var2);
         });
      }
   }

   private static void T(LevelRenderContext var0) {
      if (W != nl.q) {
         P = (boolean)0;
      }

      if (!P && var0.poseStack() != null && var0.levelState() == k) {
         Minecraft var1 = Minecraft.getInstance();
         if (var1.level != null) {
            if (q == null) {
               q = new wh();
            }

            GpuTextureView var2 = jf.b;
            GpuTextureView var3 = jf.q;
            String var4 = "shader initialization";

            try {
               if (R) {
                  _s.g();
                  _s.i();
               } else {
                  O();
               }

               float var5 = v;
               fz var6 = new fz(var0.poseStack(), q, var0.levelState().cameraRenderState);
               var4 = "Wings Ender Dragon opaque pass";
               WingsModule var7 = WingsModule.i();
               if (var7 != null && var7.isOperational() && var7.Z()) {
                  X("Wings", () -> {
                     if (var7.B(var0.poseStack(), q)) {
                        q.S();
                        _s.g();
                        _s.i();
                     }
                  });
               }

               AmbientParticlesModule var8 = AmbientParticlesModule.w();
               int var9 = var8 != null && var8.isOperational() && !X.contains("Ambient Particles") ? 1 : 0;
               d3.G(!X.contains("Particles") && Q.J() || var9 != 0 && var8.L().F() || q3.J());
               var4 = "Particles/Trails/Body Trails/TargetAura/GhostEcho/Comets/HitZone/BlockOverlay base geometry";
               L(var0, var6, var5, false);
               PlayerModelModule var10 = PlayerModelModule.k();
               if (var10 != null) {
                  X("PlayerModel", () -> var10.E(var0.poseStack(), q));
               }

               OrderDropperModule var11 = OrderDropperModule.U();
               if (var11 != null) {
                  X("Order Dropper", () -> var11.X(var0.poseStack(), q, var0.levelState().cameraRenderState.pos));
               }

               q.S();
               _s.g();
               _s.i();
               var4 = "Chams nearest-surface gradient silhouette";
               ChamsModule var12 = ChamsModule.O();
               if (var12 != null) {
                  X("Chams", () -> qc.J(() -> var12.g(var0.poseStack(), q, var5)));
               }

               EntityRenderState var13 = E;
               var4 = "SelfVisuals outline mask / player material";
               if (!R) {
                  Q(var0, var13);
               }

               TargetAuraModule var14 = TargetAuraModule.v();
               DamageMarkersModule var15 = DamageMarkersModule.D();
               if (var15 != null) {
                  X("DamageMarkers", () -> var15.O(var0.poseStack(), var0.levelState().cameraRenderState));
               }

               WorldLyricsModule var16 = WorldLyricsModule.f();
               if (var16 != null) {
                  X("WorldLyrics", () -> var16.T(var0.poseStack(), var0.levelState().cameraRenderState, false));
               }

               JumpFXModule var17 = JumpFXModule.f();
               BlockOverlayModule var18 = BlockOverlayModule.c();
               KillFXModule var19 = KillFXModule.J();
               var4 = "ChinaHat after player materials";
               ChinaHatModule var20 = ChinaHatModule.E();
               if (var20 != null) {
                  X("ChinaHat", () -> {
                     var20.j(var6, var5);
                     q.S();
                  });
               }

               var4 = "Wings after player materials";
               if (var7 != null && var7.isOperational() && !var7.Z()) {
                  Y("Wings", var6, () -> var7.B(var6, q));
               }

               RenderTarget var21 = !q3.h()
                     && !t(hz.Q())
                     && (X.contains("Particles") || !Q.d())
                     && (var9 == 0 || !var8.L().e())
                     && (X.contains("Trails") || !Y.S())
                     && (X.contains("Body Trails") || !S.M())
                     && (X.contains("BlockOverlay") || var18 == null || !var18.t(var0.levelState()))
                     && !b.stream().anyMatch(var1x -> !X.contains(var1x.M()) && var1x.b(var0.levelState()))
                     && (X.contains("Comets") || !d6.F())
                     && (X.contains("JumpFX") || var17 == null || !var17.q())
                     && (X.contains("KillFX") || var19 == null || !var19.L())
                     && (X.contains("HitZone") || !com.larpclient.module.s.i())
                     && (X.contains("GhostEcho") || !V.C())
                     && (X.contains("TargetAura") || var14 == null || !var14.q())
                     && (X.contains("SelfVisuals") || var13 == null || !A || !Z("SelfVisuals bloom"))
                     && (X.contains("Wings") || var7 == null || !var7.L())
                     && (X.contains("ChinaHat") || var20 == null || !var20.d())
                     && (X.contains("WorldLyrics") || var16 == null || !var16.B())
                  ? null
                  : nl.O();
               var4 = "explicit bloom mask / composite";
               if (var21 != null) {
                  jf.b = var21.getColorTextureView();
                  jf.q = var21.getDepthTextureView();
                  L(var0, var6, var5, true);
                  if (var16 != null && var16.B()) {
                     X("WorldLyrics", () -> var16.T(var0.poseStack(), var0.levelState().cameraRenderState, true));
                  }

                  if (var7 != null && var7.L()) {
                     Y("Wings", var6, () -> var7.p(var6, q));
                  }

                  if (var20 != null && !X.contains("ChinaHat") && var20.d()) {
                     X("ChinaHat", () -> var20.Q(var6, q));
                  }

                  GpuTextureView var22 = R ? _s.v() : null;
                  if (var22 != null) {
                     jf.q = var22;
                  }

                  if (R) {
                     Y("Comets", var6, () -> {
                        d6.G(var6, true);
                        q.S();
                     });
                  }

                  if (var13 != null && A) {
                     X("SelfVisuals bloom", () -> {
                        w7.r(var13, var0.poseStack(), q, l == w7.b.I ? w7.b.L : l, p, l == w7.b.I ? n * 0.25 : 0.25, true, false);
                        q.S();
                     });
                  }

                  jf.b = var2;
                  jf.q = var3;
                  nl.X();
               }
            } catch (RuntimeException var28) {
               P = (boolean)1;
               _q.o("Render", "VisualRuntime stage=" + var4 + "; output attachments restored; retry on resource reload", var28);
            } finally {
               jf.b = var2;
               jf.q = var3;
               DamageMarkersModule var24 = DamageMarkersModule.D();
               if (var24 != null) {
                  var24.c();
               }

               WorldLyricsModule var25 = WorldLyricsModule.f();
               if (var25 != null) {
                  var25.J();
               }

               if (q != null) {
                  q.A();
               }

               X("Refraction cleanup", bq::P);
               X("Scene resource cleanup", _s::X);
            }
         }
      }
   }

   public static void j(GuiGraphicsExtractor var0) {
      if (Q()) {
         PetsModule var1 = PetsModule.X();
         if (var1 != null) {
            X("Pets", () -> var1.P(var0));
         }
      }
   }

   private static boolean t(double var0) {
      LineGlyphsModule var2 = LineGlyphsModule.d();
      PearlParticlesModule var3 = PearlParticlesModule.w();
      CrystalChamsModule var4 = CrystalChamsModule.W();
      PetsModule var5 = PetsModule.X();
      CryptoPetsModule var6 = CryptoPetsModule.v();
      return !X.contains("Pets") && var5 != null && var5.G()
         || !X.contains("CryptoPets") && var6 != null && var6.B()
         || !X.contains("CrystalChams") && var4 != null && var4.R()
         || !X.contains("LineGlyphs") && var2 != null && var2.K(var0)
         || !X.contains("PearlParticles") && var3 != null && var3.P(var0);
   }

   private static void L(LevelRenderContext var0, fz var1, float var2, boolean var3) {
      wh var4 = q;
      q3.v(var1, var3);

      for (ai var6 : b) {
         Y(var6.M(), var1, () -> var6.r(var1, var3, var0.levelState()));
      }

      SnowGroundModule var16 = SnowGroundModule.q();
      if (!var3 && var16 != null) {
         Y("SnowGround", var1, () -> var16.f(var1));
      }

      CrystalChamsModule var17 = CrystalChamsModule.W();
      if (var17 != null) {
         Y("CrystalChams", var1, () -> var17.o(var1, var3));
      }

      PetsModule var7 = PetsModule.X();
      if (var7 != null) {
         Y("Pets", var1, () -> var7.E(var1, var3));
      }

      CryptoPetsModule var8 = CryptoPetsModule.v();
      if (var8 != null) {
         Y("CryptoPets", var1, () -> var8.p(var1, var3));
      }

      Y("EmoteProps", var1, () -> iz.i(var1, var3));
      Y("Particles", var1, () -> Q.z(var1, var0.levelState().cameraRenderState.pos, var2, var3));
      Y("Trails", var1, () -> Y.Q(var1, var2, var3));
      Y("Body Trails", var1, () -> S.A(var1, var3));
      if (!R) {
         Y("Comets", var1, () -> d6.G(var1, var3));
      }

      AmbientParticlesModule var9 = AmbientParticlesModule.w();
      if (var9 != null && var9.isOperational()) {
         Y("Ambient Particles", var1, () -> var9.L().w(var1, var0.levelState().cameraRenderState.pos, var2, var3));
      }

      JumpFXModule var10 = JumpFXModule.f();
      if (var10 != null) {
         Y("JumpFX", var1, () -> var10.S(var1, var3));
      }

      KillFXModule var11 = KillFXModule.J();
      if (var11 != null) {
         Y("KillFX", var1, () -> var11.l(var1, var0.poseStack(), var4, var3));
      }

      BlockOverlayModule var12 = BlockOverlayModule.c();
      if (var12 != null) {
         Y("BlockOverlay", var1, () -> var12.l(var1, var3, var0.levelState()));
      }

      Y("HitZone", var1, () -> com.larpclient.module.s.e(var1, var3));
      TargetAuraModule var13 = TargetAuraModule.v();
      if (var13 != null) {
         Y("TargetAura", var1, () -> var13.s(var1, var2, var3));
      }

      Y("GhostEcho", var1, () -> V.N(var0.poseStack(), var4, var3));
      LineGlyphsModule var14 = LineGlyphsModule.d();
      if (var14 != null) {
         X("LineGlyphs", () -> var14.R(var0.poseStack(), var4, var0.levelState().cameraRenderState, hz.Q(), var3));
      }

      PearlParticlesModule var15 = PearlParticlesModule.w();
      if (var15 != null) {
         X("PearlParticles", () -> var15.Y(var0.poseStack(), var4, var0.levelState().cameraRenderState, hz.Q(), var3));
      }

      X("Shared geometry", var4::S);
   }
}
