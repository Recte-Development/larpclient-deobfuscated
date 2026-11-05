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
import com.larpclient.module.l1;
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

public final class f8 {
   public static final f4 D = new f4();
   public static final xg f = new xg();
   public static final rw X = new rw();
   public static final f_ O = new f_();
   private static List<hy> j = List.of();
   private static y2 V;
   private static ClientLevel k;
   private static boolean g;
   private static int W = -1;
   private static LivingEntity m;
   private static double S;
   private static LevelRenderState G;
   private static float J;
   private static EntityRenderState C;
   private static uv.w Q;
   private static double n;
   private static double d;
   private static int c;
   private static boolean F;
   private static boolean s;
   private static Boolean h;
   private static final Set<String> N = new HashSet<>();
   private static int P = -1;

   static void M(String var0, Runnable var1) {
      if (P != jk.v) {
         N.clear();
         P = jk.v;
      }

      if (!N.contains(var0)) {
         try {
            var1.run();
         } catch (RuntimeException var4) {
            N.add(var0);
            com.larpclient.f.n(var0);
            if (var0.equals("Chams")) {
               ChamsModule var3 = ChamsModule.A();
               if (var3 != null) {
                  var3.K();
               }
            }

            if (var0.equals("CrystalChams")) {
               CrystalChamsModule var5 = CrystalChamsModule.l();
               if (var5 != null) {
                  var5.h();
               }
            }

            if (var0.equals("Pets")) {
               PetsModule var6 = PetsModule.s();
               if (var6 != null) {
                  var6.N();
               }
            }

            if (var0.equals("CryptoPets")) {
               CryptoPetsModule var7 = CryptoPetsModule.q();
               if (var7 != null) {
                  var7.c();
               }
            }

            if (var0.equals("EmoteProps")) {
               xx.j();
            }

            if (var0.equals("PlayerModel")) {
               PlayerModelModule var8 = PlayerModelModule.r();
               if (var8 != null) {
                  var8.x();
               }
            }

            if (var0.equals("SnowGround")) {
               SnowGroundModule var9 = SnowGroundModule.x();
               if (var9 != null) {
                  var9.h();
               }
            }

            if (var0.equals("KillFX")) {
               KillFXModule var10 = KillFXModule.X();
               if (var10 != null) {
                  var10.d();
               }
            }

            if (V != null) {
               V.D();
            }

            r4.r("Render", "Cosmetic stage=" + var0 + "; isolated until resource reload", var4);
         }
      }
   }

   static boolean H(String var0) {
      return !p(var0);
   }

   public static boolean p(String var0) {
      return P != jk.v || !N.contains(var0);
   }

   private static void e(String var0, r6 var1, Runnable var2) {
      M(var0, () -> {
         try {
            var2.run();
         } finally {
            var1.T(false);
            var1.blend(fk.h.z);
         }
      });
   }

   private static void W(LevelExtractionContext var0) {
      G = var0.levelState();
      s = (boolean)0;

      for (hy var2 : j) {
         M(var2.C(), () -> var2.k(var0));
      }

      J = var0.deltaTracker().getGameTimeDeltaPartialTick(false);
      CameraRenderState var17 = G.cameraRenderState;
      my.T(var17);
      com.larpclient.f.j(J, var17);
      PlayerModelModule var18 = PlayerModelModule.r();
      if (var18 != null) {
         M("PlayerModel", () -> var18.g(J, var17.pos));
      }

      JumpFXModule var3 = JumpFXModule.m();
      if (var3 != null) {
         M("JumpFX", var3::F);
      }

      KillFXModule var4 = KillFXModule.X();
      if (var4 != null) {
         M("KillFX", () -> var4.g(var17));
      }

      CrystalChamsModule var5 = CrystalChamsModule.l();
      if (var5 != null) {
         M("CrystalChams", () -> var5.q(J));
      }

      PetsModule var6 = PetsModule.s();
      if (var6 != null) {
         M("Pets", () -> var6.e(J, var17));
      }

      CryptoPetsModule var7 = CryptoPetsModule.q();
      if (var7 != null) {
         M("CryptoPets", () -> var7.P(J, var17));
      }

      M("EmoteProps", () -> xx.i(J));
      SnowGroundModule var8 = SnowGroundModule.x();
      if (var8 != null) {
         M("SnowGround", () -> var8.i(J, var17));
      }

      M("Trails", () -> f.X(var17.pos, J));
      M("Body Trails", () -> X.K(var17.pos, J));
      M("GhostEcho", () -> O.O(var17.pos));
      M("Comets", () -> _0.H(J));
      M("HitZone", l1::R);
      TargetAuraModule var9 = TargetAuraModule.X();
      if (var9 != null) {
         M("TargetAura", () -> var9.r(J));
      }

      M("Particles", () -> D.j(var17.pos, J, var17.cullFrustum));
      AmbientParticlesModule var10 = AmbientParticlesModule.E();
      if (var10 != null && var10.isOperational()) {
         M("Ambient Particles", () -> var10.h().o(var17.pos, J, var17.cullFrustum));
      }

      ChamsModule var11 = ChamsModule.A();
      if (var11 != null) {
         M("Chams", () -> var11.b(J));
      }

      ChinaHatModule var12 = ChinaHatModule.J();
      if (var12 != null) {
         M("ChinaHat", () -> var12.W(J));
      }

      WingsModule var13 = WingsModule.i();
      if (var13 != null) {
         M("Wings", () -> var13.i(J));
      }

      DamageMarkersModule var14 = DamageMarkersModule.c();
      if (var14 != null) {
         M("DamageMarkers", () -> var14.U(var17));
      }

      WorldLyricsModule var15 = WorldLyricsModule.A();
      if (var15 != null) {
         M("WorldLyrics", () -> var15.v(var17));
      }

      C = null;
      SelfVisualsModule var16 = SelfVisualsModule.f();
      Q = null;
      F = (boolean)0;
      d = 0.0;
      if (var16 != null && var16.b() && var16.Z()) {
         M("SelfVisuals", () -> {
            C = uv.v(Minecraft.getInstance().player, J);
            Q = !var16.w() ? null : (var16.c() == null ? uv.w.V : var16.c());
            n = var16.K();
            c = rv.e;
            d = var16.U() && var16.w.getValue() ? var16.J.getValue() : 0.0;
            F = (boolean)(!var16.w() || !var16.q.getValue() || var16.U() && lo.q == jk.x.i ? 0 : 1);
         });
      }
   }

   public static boolean c() {
      return !g;
   }

   public static boolean m(EntityRenderState var0) {
      return C != null && Q != null && G != null && G.entityRenderStates.contains(var0);
   }

   public static void L() {
      com.larpclient.f.E(LarpClient.y().e().X());
      j = LarpClient.y().e().X().stream().filter(var0 -> var0 instanceof hy.c).map(var0 -> ((hy.c)var0).placementHighlight()).toList();
      ClientTickEvents.END_CLIENT_TICK.register((EndTick)var0 -> {
         JumpFXModule var1 = JumpFXModule.m();
         if (var1 != null && !var0.isPaused()) {
            var1.H();
         }

         if (!var0.isPaused()) {
            com.larpclient.f.T(var0);
         }
      });
      ClientTickEvents.START_CLIENT_TICK.register((StartTick)var0 -> {
         m7.g();
         if (var0.level != k) {
            com.larpclient.f.l();
            M("Scene resource cleanup", x1::k);
            M("Refraction cleanup", m6::D);
            ly.d();
            dy.z();
            m7.V();
            CrystalChamsModule var1 = CrystalChamsModule.l();
            if (var1 != null) {
               var1.h();
            }

            PetsModule var2 = PetsModule.s();
            if (var2 != null) {
               var2.N();
            }

            CryptoPetsModule var3 = CryptoPetsModule.q();
            if (var3 != null) {
               var3.c();
            }

            DamageMarkersModule var4 = DamageMarkersModule.c();
            if (var4 != null) {
               var4.D();
            }

            WorldLyricsModule var5 = WorldLyricsModule.A();
            if (var5 != null) {
               var5.Q();
            }

            KillFXModule var6 = KillFXModule.X();
            if (var6 != null) {
               var6.d();
            }

            j.forEach(hy::C);
            k = var0.level;
            G = null;
            C = null;
            D.U();
            f.m();
            X.U();
            O.v();
            _0.q();
            AmbientParticlesModule var7 = AmbientParticlesModule.E();
            if (var7 != null) {
               var7.w();
            }

            JumpFXModule var8 = JumpFXModule.m();
            if (var8 != null) {
               var8.p();
            }

            BlockOverlayModule var9 = BlockOverlayModule.z();
            if (var9 != null) {
               var9.A();
            }

            mu.f();
            mc.J();
            fo.v();
            la.J();
            jx.E();
            uv.S();
            ChamsModule var10 = ChamsModule.A();
            if (var10 != null) {
               var10.K();
            }

            PlayerModelModule var11 = PlayerModelModule.r();
            if (var11 != null) {
               var11.b();
            }

            m = null;
            o8.A();
            o8.N();
            r4.g("Render", "World changed; transient effects cleared");
         }

         if (!var0.isPaused()) {
            ea.g();
            O.o();
            D.m(1.0, (var1x, var2x) -> et.Q(var0.level, var1x, var2x));
            f.N();
            X.C();
            KillFXModule var12 = KillFXModule.X();
            if (var12 != null) {
               M("KillFX", () -> var12.q());
            }
         }
      });
      LevelExtractionEvents.AFTER_BLOCK_OUTLINE_EXTRACTION.register((AfterBlockOutlineExtraction)(var0, var1) -> {
         BlockOverlayModule var2 = BlockOverlayModule.z();
         if (var2 != null) {
            var2.j(var0, var1);
         }
      });
      LevelRenderEvents.BEFORE_BLOCK_OUTLINE.register((BeforeBlockOutline)(var0, var1) -> {
         BlockOverlayModule var2 = BlockOverlayModule.z();
         return var2 == null || y8.Y() || !var2.f(var1.pos(), var0.levelState());
      });
      LevelExtractionEvents.END_EXTRACTION.register(f8::W);
      LevelRenderEvents.AFTER_OPAQUE_TERRAIN.register((AfterOpaqueTerrain)var0 -> y8.Y());
      LevelRenderEvents.BEFORE_TRANSLUCENT_TERRAIN.register((BeforeTranslucentTerrain)var0 -> y8.v(() -> J(var0)));
      LevelRenderEvents.END_MAIN.register((EndMain)var0 -> y8.e(var0, () -> j(var0)));
      fo.B();
      ClientLifecycleEvents.CLIENT_STOPPING.register((ClientStopping)var0 -> {
         y8.U();
         com.larpclient.f.l();
         M("Scene resource cleanup", x1::k);
         M("Refraction cleanup", m6::D);
         ly.d();
         m7.V();
         DamageMarkersModule var1 = DamageMarkersModule.c();
         if (var1 != null) {
            var1.D();
         }

         WorldLyricsModule var2 = WorldLyricsModule.A();
         if (var2 != null) {
            var2.v();
         }

         KillFXModule var3 = KillFXModule.X();
         if (var3 != null) {
            var3.d();
         }

         j.forEach(hy::C);
         jk.g();
         jx.E();
         fo.v();
         la.J();
         ChamsModule var4 = ChamsModule.A();
         if (var4 != null) {
            var4.K();
         }

         O.v();
         _0.q();
         AmbientParticlesModule var5 = AmbientParticlesModule.E();
         if (var5 != null) {
            var5.w();
         }

         JumpFXModule var6 = JumpFXModule.m();
         if (var6 != null) {
            var6.p();
         }

         BlockOverlayModule var7 = BlockOverlayModule.z();
         if (var7 != null) {
            var7.A();
         }

         LineGlyphsModule var8 = LineGlyphsModule.G();
         if (var8 != null) {
            var8.Q();
         }

         PearlParticlesModule var9 = PearlParticlesModule.D();
         if (var9 != null) {
            var9.Y();
         }

         SkyShaderModule var10 = SkyShaderModule.B();
         if (var10 != null) {
            var10.S();
         }

         SnowGroundModule var11 = SnowGroundModule.x();
         if (var11 != null) {
            var11.X();
         }

         dd.S();
         y4.m();
         jn.G();
         _8.Q();
         if (V != null) {
            V.close();
         }
      });
      r4.V("Render", "Shared effects initialized; depth-tested ribbons, particle pool, bloom and blur");
   }

   public static void o(LivingEntity var0) {
      s(var0);
      HitFXModule var1 = HitFXModule.y();
      if (var1 != null && var1.isOperational()) {
         var1.S(var0);
      }
   }

   public static void s(LivingEntity var0) {
      m = var0;
      S = ea.t();
   }

   public static LivingEntity m() {
      return m != null && m.isAlive() && m.level() == Minecraft.getInstance().level && ea.t() - S < 3.0 ? m : null;
   }

   private static void r() {
      x1.X();
      m6.w();
      if (W != jk.v) {
         W = jk.v;
         e4.U(e4.e);
         r6.C();
      }
   }

   private static void J(LevelRenderContext var0) {
      if (W != jk.v) {
         g = (boolean)0;
      }

      if (!g && var0.poseStack() != null && var0.levelState() == G) {
         if (Minecraft.getInstance().level != null) {
            if (V == null) {
               V = new y2();
            }

            GpuTextureView var1 = _m.M;
            GpuTextureView var2 = _m.U;
            String var3 = "shader initialization before translucent terrain";

            try {
               r();
               s = (boolean)1;
               EntityRenderState var4 = C;
               var3 = "SelfVisuals outline mask / player material before translucent terrain";
               i(var0, var4);
               var3 = "Comets before translucent terrain";
               r6 var5 = new r6(var0.poseStack(), V, var0.levelState().cameraRenderState);
               e("Comets", var5, () -> {
                  _0.I(var5, false);
                  V.v();
               });
               x1.N();
               x1.p();
               if (var4 != null && F || _0.L()) {
                  x1.t();
               }
            } catch (RuntimeException var9) {
               g = (boolean)1;
               r4.r("Render", "VisualRuntime stage=" + var3 + "; output attachments restored; retry on resource reload", var9);
            } finally {
               _m.M = var1;
               _m.U = var2;
            }
         }
      }
   }

   private static void i(LevelRenderContext var0, EntityRenderState var1) {
      if (var1 != null && !Boolean.valueOf(s).equals(h)) {
         h = s;
         if (s) {
            r4.V("SelfVisuals", "Drawing before translucent terrain; glass, water and ice blend over the player");
         } else {
            r4.m("SelfVisuals", "Hook before translucent terrain did not run; drawing at END_MAIN, where glass hides the player");
         }
      }

      M("SelfVisuals outline", () -> la.x(var0.poseStack(), var1));
      if (var1 != null && Q == uv.w.w && d > 0.0 && my.f()) {
         M("Liquid Glass refraction", () -> m6.a("Liquid Glass", () -> {
            try {
               uv.V(var1, var0.poseStack(), V, uv.w.F, -1, n, true, false);
               V.v();
            } finally {
               V.D();
            }
         }, d));
      }

      if (var1 != null && Q != null && (Q != uv.w.w || my.f())) {
         M("SelfVisuals", () -> {
            long var2 = h_.U();
            uv.k(var1, var0.poseStack(), V, Q, c, n, true);
            V.v();
            x1.N();
            x1.p();
            h_.n("SelfVisuals body material", var2);
         });
      }
   }

   private static void j(LevelRenderContext var0) {
      if (W != jk.v) {
         g = (boolean)0;
      }

      if (!g && var0.poseStack() != null && var0.levelState() == G) {
         Minecraft var1 = Minecraft.getInstance();
         if (var1.level != null) {
            if (V == null) {
               V = new y2();
            }

            GpuTextureView var2 = _m.M;
            GpuTextureView var3 = _m.U;
            String var4 = "shader initialization";

            try {
               if (s) {
                  x1.N();
                  x1.p();
               } else {
                  r();
               }

               float var5 = J;
               r6 var6 = new r6(var0.poseStack(), V, var0.levelState().cameraRenderState);
               var4 = "Wings Ender Dragon opaque pass";
               WingsModule var7 = WingsModule.i();
               if (var7 != null && var7.isOperational() && var7.G()) {
                  M("Wings", () -> {
                     if (var7.C(var0.poseStack(), V)) {
                        V.v();
                        x1.N();
                        x1.p();
                     }
                  });
               }

               AmbientParticlesModule var8 = AmbientParticlesModule.E();
               int var9 = var8 != null && var8.isOperational() && !N.contains("Ambient Particles") ? 1 : 0;
               jx.O(!N.contains("Particles") && D.C() || var9 != 0 && var8.h().Z() || com.larpclient.f.o());
               var4 = "Particles/Trails/Body Trails/TargetAura/GhostEcho/Comets/HitZone/BlockOverlay base geometry";
               k(var0, var6, var5, false);
               PlayerModelModule var10 = PlayerModelModule.r();
               if (var10 != null) {
                  M("PlayerModel", () -> var10.L(var0.poseStack(), V));
               }

               OrderDropperModule var11 = OrderDropperModule.Y();
               if (var11 != null) {
                  M("Order Dropper", () -> var11.l(var0.poseStack(), V, var0.levelState().cameraRenderState.pos));
               }

               V.v();
               x1.N();
               x1.p();
               var4 = "Chams nearest-surface gradient silhouette";
               ChamsModule var12 = ChamsModule.A();
               if (var12 != null) {
                  M("Chams", () -> y8.d(() -> var12.r(var0.poseStack(), V, var5)));
               }

               EntityRenderState var13 = C;
               var4 = "SelfVisuals outline mask / player material";
               if (!s) {
                  i(var0, var13);
               }

               TargetAuraModule var14 = TargetAuraModule.X();
               DamageMarkersModule var15 = DamageMarkersModule.c();
               if (var15 != null) {
                  M("DamageMarkers", () -> var15.f(var0.poseStack(), var0.levelState().cameraRenderState));
               }

               WorldLyricsModule var16 = WorldLyricsModule.A();
               if (var16 != null) {
                  M("WorldLyrics", () -> var16.e(var0.poseStack(), var0.levelState().cameraRenderState, false));
               }

               JumpFXModule var17 = JumpFXModule.m();
               BlockOverlayModule var18 = BlockOverlayModule.z();
               KillFXModule var19 = KillFXModule.X();
               var4 = "ChinaHat after player materials";
               ChinaHatModule var20 = ChinaHatModule.J();
               if (var20 != null) {
                  M("ChinaHat", () -> {
                     var20.G(var6, var5);
                     V.v();
                  });
               }

               var4 = "Wings after player materials";
               if (var7 != null && var7.isOperational() && !var7.G()) {
                  e("Wings", var6, () -> var7.D(var6, V));
               }

               RenderTarget var21 = !com.larpclient.f.l()
                     && !o(ea.y())
                     && (N.contains("Particles") || !D.t())
                     && (var9 == 0 || !var8.h().i())
                     && (N.contains("Trails") || !f.Z())
                     && (N.contains("Body Trails") || !X.Q())
                     && (N.contains("BlockOverlay") || var18 == null || !var18.e(var0.levelState()))
                     && !j.stream().anyMatch(var1x -> !N.contains(var1x.C()) && var1x.u(var0.levelState()))
                     && (N.contains("Comets") || !_0.L())
                     && (N.contains("JumpFX") || var17 == null || !var17.A())
                     && (N.contains("KillFX") || var19 == null || !var19.C())
                     && (N.contains("HitZone") || !l1.u())
                     && (N.contains("GhostEcho") || !O.W())
                     && (N.contains("TargetAura") || var14 == null || !var14.i())
                     && (N.contains("SelfVisuals") || var13 == null || !F || !p("SelfVisuals bloom"))
                     && (N.contains("Wings") || var7 == null || !var7.j())
                     && (N.contains("ChinaHat") || var20 == null || !var20.K())
                     && (N.contains("WorldLyrics") || var16 == null || !var16.I())
                  ? null
                  : jk.J();
               var4 = "explicit bloom mask / composite";
               if (var21 != null) {
                  _m.M = var21.getColorTextureView();
                  _m.U = var21.getDepthTextureView();
                  k(var0, var6, var5, true);
                  if (var16 != null && var16.I()) {
                     M("WorldLyrics", () -> var16.e(var0.poseStack(), var0.levelState().cameraRenderState, true));
                  }

                  if (var7 != null && var7.j()) {
                     e("Wings", var6, () -> var7.m(var6, V));
                  }

                  if (var20 != null && !N.contains("ChinaHat") && var20.K()) {
                     M("ChinaHat", () -> var20.f(var6, V));
                  }

                  GpuTextureView var22 = s ? x1.G() : null;
                  if (var22 != null) {
                     _m.U = var22;
                  }

                  if (s) {
                     e("Comets", var6, () -> {
                        _0.I(var6, true);
                        V.v();
                     });
                  }

                  if (var13 != null && F) {
                     M("SelfVisuals bloom", () -> {
                        uv.V(var13, var0.poseStack(), V, Q == uv.w.w ? uv.w.q : Q, c, Q == uv.w.w ? n * 0.25 : 0.25, true, false);
                        V.v();
                     });
                  }

                  _m.M = var2;
                  _m.U = var3;
                  jk.Y();
               }
            } catch (RuntimeException var28) {
               g = (boolean)1;
               r4.r("Render", "VisualRuntime stage=" + var4 + "; output attachments restored; retry on resource reload", var28);
            } finally {
               _m.M = var2;
               _m.U = var3;
               DamageMarkersModule var24 = DamageMarkersModule.c();
               if (var24 != null) {
                  var24.s();
               }

               WorldLyricsModule var25 = WorldLyricsModule.A();
               if (var25 != null) {
                  var25.w();
               }

               if (V != null) {
                  V.R();
               }

               M("Refraction cleanup", m6::J);
               M("Scene resource cleanup", x1::V);
            }
         }
      }
   }

   public static void w(GuiGraphicsExtractor var0) {
      if (c()) {
         PetsModule var1 = PetsModule.s();
         if (var1 != null) {
            M("Pets", () -> var1.E(var0));
         }
      }
   }

   private static boolean o(double var0) {
      LineGlyphsModule var2 = LineGlyphsModule.G();
      PearlParticlesModule var3 = PearlParticlesModule.D();
      CrystalChamsModule var4 = CrystalChamsModule.l();
      PetsModule var5 = PetsModule.s();
      CryptoPetsModule var6 = CryptoPetsModule.q();
      return !N.contains("Pets") && var5 != null && var5.y()
         || !N.contains("CryptoPets") && var6 != null && var6.Q()
         || !N.contains("CrystalChams") && var4 != null && var4.t()
         || !N.contains("LineGlyphs") && var2 != null && var2.Z(var0)
         || !N.contains("PearlParticles") && var3 != null && var3.e(var0);
   }

   private static void k(LevelRenderContext var0, r6 var1, float var2, boolean var3) {
      y2 var4 = V;
      com.larpclient.f.E(var1, var3);

      for (hy var6 : j) {
         e(var6.C(), var1, () -> var6.H(var1, var3, var0.levelState()));
      }

      SnowGroundModule var16 = SnowGroundModule.x();
      if (!var3 && var16 != null) {
         e("SnowGround", var1, () -> var16.W(var1));
      }

      CrystalChamsModule var17 = CrystalChamsModule.l();
      if (var17 != null) {
         e("CrystalChams", var1, () -> var17.N(var1, var3));
      }

      PetsModule var7 = PetsModule.s();
      if (var7 != null) {
         e("Pets", var1, () -> var7.e(var1, var3));
      }

      CryptoPetsModule var8 = CryptoPetsModule.q();
      if (var8 != null) {
         e("CryptoPets", var1, () -> var8.L(var1, var3));
      }

      e("EmoteProps", var1, () -> xx.T(var1, var3));
      e("Particles", var1, () -> D.Y(var1, var0.levelState().cameraRenderState.pos, var2, var3));
      e("Trails", var1, () -> f.q(var1, var2, var3));
      e("Body Trails", var1, () -> X.G(var1, var3));
      if (!s) {
         e("Comets", var1, () -> _0.I(var1, var3));
      }

      AmbientParticlesModule var9 = AmbientParticlesModule.E();
      if (var9 != null && var9.isOperational()) {
         e("Ambient Particles", var1, () -> var9.h().Y(var1, var0.levelState().cameraRenderState.pos, var2, var3));
      }

      JumpFXModule var10 = JumpFXModule.m();
      if (var10 != null) {
         e("JumpFX", var1, () -> var10.d(var1, var3));
      }

      KillFXModule var11 = KillFXModule.X();
      if (var11 != null) {
         e("KillFX", var1, () -> var11.v(var1, var0.poseStack(), var4, var3));
      }

      BlockOverlayModule var12 = BlockOverlayModule.z();
      if (var12 != null) {
         e("BlockOverlay", var1, () -> var12.F(var1, var3, var0.levelState()));
      }

      e("HitZone", var1, () -> l1.u(var1, var3));
      TargetAuraModule var13 = TargetAuraModule.X();
      if (var13 != null) {
         e("TargetAura", var1, () -> var13.l(var1, var2, var3));
      }

      e("GhostEcho", var1, () -> O.a(var0.poseStack(), var4, var3));
      LineGlyphsModule var14 = LineGlyphsModule.G();
      if (var14 != null) {
         M("LineGlyphs", () -> var14.b(var0.poseStack(), var4, var0.levelState().cameraRenderState, ea.y(), var3));
      }

      PearlParticlesModule var15 = PearlParticlesModule.D();
      if (var15 != null) {
         M("PearlParticles", () -> var15.x(var0.poseStack(), var4, var0.levelState().cameraRenderState, ea.y(), var3));
      }

      M("Shared geometry", var4::v);
   }
}
