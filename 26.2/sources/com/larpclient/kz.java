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

public final class kz {
   public static final c3 J = new c3();
   public static final ub c = new ub();
   public static final t8 b = new t8();
   public static final lt P = new lt();
   private static List<mr> M = List.of();
   private static hh D;
   private static ClientLevel q;
   private static boolean d;
   private static int E = -1;
   private static LivingEntity Y;
   private static double H;
   private static LevelRenderState j;
   private static float Z;
   private static EntityRenderState h;
   private static j4.j z;
   private static double y;
   private static double Q;
   private static int B;
   private static boolean m;
   private static boolean x;
   private static Boolean i;
   private static final Set<String> p = new HashSet<>();
   private static int C = -1;

   static void W(String var0, Runnable var1) {
      if (C != wa.s) {
         p.clear();
         C = wa.s;
      }

      if (!p.contains(var0)) {
         try {
            var1.run();
         } catch (RuntimeException var4) {
            p.add(var0);
            wv.f(var0);
            if (var0.equals("Chams")) {
               ChamsModule var3 = ChamsModule.C();
               if (var3 != null) {
                  var3.C();
               }
            }

            if (var0.equals("CrystalChams")) {
               CrystalChamsModule var5 = CrystalChamsModule.Y();
               if (var5 != null) {
                  var5.W();
               }
            }

            if (var0.equals("Pets")) {
               PetsModule var6 = PetsModule.I();
               if (var6 != null) {
                  var6.Q();
               }
            }

            if (var0.equals("CryptoPets")) {
               CryptoPetsModule var7 = CryptoPetsModule.O();
               if (var7 != null) {
                  var7.E();
               }
            }

            if (var0.equals("EmoteProps")) {
               la.E();
            }

            if (var0.equals("PlayerModel")) {
               PlayerModelModule var8 = PlayerModelModule.E();
               if (var8 != null) {
                  var8.M();
               }
            }

            if (var0.equals("SnowGround")) {
               SnowGroundModule var9 = SnowGroundModule.C();
               if (var9 != null) {
                  var9.D();
               }
            }

            if (var0.equals("KillFX")) {
               KillFXModule var10 = KillFXModule.t();
               if (var10 != null) {
                  var10.r();
               }
            }

            if (D != null) {
               D.T();
            }

            com.larpclient.h.m("Render", "Cosmetic stage=" + var0 + "; isolated until resource reload", var4);
         }
      }
   }

   static boolean B(String var0) {
      return !u(var0);
   }

   public static boolean u(String var0) {
      return C != wa.s || !p.contains(var0);
   }

   private static void a(String var0, kj var1, Runnable var2) {
      W(var0, () -> {
         try {
            var2.run();
         } finally {
            var1.j(false);
            var1.blend(gl.c.K);
         }
      });
   }

   private static void x(LevelExtractionContext var0) {
      j = var0.levelState();
      x = (boolean)0;

      for (mr var2 : M) {
         W(var2.Z(), () -> var2.T(var0));
      }

      Z = var0.deltaTracker().getGameTimeDeltaPartialTick(false);
      CameraRenderState var17 = j.cameraRenderState;
      g8.Z(var17);
      wv.m(Z, var17);
      PlayerModelModule var18 = PlayerModelModule.E();
      if (var18 != null) {
         W("PlayerModel", () -> var18.c(Z, var17.pos));
      }

      JumpFXModule var3 = JumpFXModule.f();
      if (var3 != null) {
         W("JumpFX", var3::i);
      }

      KillFXModule var4 = KillFXModule.t();
      if (var4 != null) {
         W("KillFX", () -> var4.o(var17));
      }

      CrystalChamsModule var5 = CrystalChamsModule.Y();
      if (var5 != null) {
         W("CrystalChams", () -> var5.r(Z));
      }

      PetsModule var6 = PetsModule.I();
      if (var6 != null) {
         W("Pets", () -> var6.s(Z, var17));
      }

      CryptoPetsModule var7 = CryptoPetsModule.O();
      if (var7 != null) {
         W("CryptoPets", () -> var7.Q(Z, var17));
      }

      W("EmoteProps", () -> la.O(Z));
      SnowGroundModule var8 = SnowGroundModule.C();
      if (var8 != null) {
         W("SnowGround", () -> var8.d(Z, var17));
      }

      W("Trails", () -> c.e(var17.pos, Z));
      W("Body Trails", () -> b.O(var17.pos, Z));
      W("GhostEcho", () -> P.N(var17.pos));
      W("Comets", () -> l_.D(Z));
      W("HitZone", com.larpclient.module.k::N);
      TargetAuraModule var9 = TargetAuraModule.B();
      if (var9 != null) {
         W("TargetAura", () -> var9.x(Z));
      }

      W("Particles", () -> J.B(var17.pos, Z, var17.cullFrustum));
      AmbientParticlesModule var10 = AmbientParticlesModule.f();
      if (var10 != null && var10.isOperational()) {
         W("Ambient Particles", () -> var10.B().Q(var17.pos, Z, var17.cullFrustum));
      }

      ChamsModule var11 = ChamsModule.C();
      if (var11 != null) {
         W("Chams", () -> var11.O(Z));
      }

      ChinaHatModule var12 = ChinaHatModule.N();
      if (var12 != null) {
         W("ChinaHat", () -> var12.i(Z));
      }

      WingsModule var13 = WingsModule.D();
      if (var13 != null) {
         W("Wings", () -> var13.z(Z));
      }

      DamageMarkersModule var14 = DamageMarkersModule.t();
      if (var14 != null) {
         W("DamageMarkers", () -> var14.g(var17));
      }

      WorldLyricsModule var15 = WorldLyricsModule.e();
      if (var15 != null) {
         W("WorldLyrics", () -> var15.D(var17));
      }

      h = null;
      SelfVisualsModule var16 = SelfVisualsModule.k();
      z = null;
      m = (boolean)0;
      Q = 0.0;
      if (var16 != null && var16.S() && var16.Z()) {
         W("SelfVisuals", () -> {
            h = j4.d(Minecraft.getInstance().player, Z);
            z = !var16.J() ? null : (var16.t() == null ? j4.j.o : var16.t());
            y = var16.o();
            B = ho.J;
            Q = var16.P() && var16.M.getValue() ? var16.o.getValue() : 0.0;
            m = (boolean)(!var16.J() || !var16.Y.getValue() || var16.P() && com.larpclient.y.m == wa.a.Z ? 0 : 1);
         });
      }
   }

   public static boolean x() {
      return !d;
   }

   public static boolean V(EntityRenderState var0) {
      return h != null && z != null && j != null && j.entityRenderStates.contains(var0);
   }

   public static void s() {
      wv.l(LarpClient.R().z().h());
      M = LarpClient.R().z().h().stream().filter(var0 -> var0 instanceof mr.n).map(var0 -> ((mr.n)var0).placementHighlight()).toList();
      ClientTickEvents.END_CLIENT_TICK.register((EndTick)var0 -> {
         JumpFXModule var1 = JumpFXModule.f();
         if (var1 != null && !var0.isPaused()) {
            var1.T();
         }

         if (!var0.isPaused()) {
            wv.a(var0);
         }
      });
      ClientTickEvents.START_CLIENT_TICK.register((StartTick)var0 -> {
         my.A();
         if (var0.level != q) {
            wv.h();
            W("Scene resource cleanup", so::b);
            W("Refraction cleanup", l3::l);
            ly.a();
            be.o();
            my.K();
            CrystalChamsModule var1 = CrystalChamsModule.Y();
            if (var1 != null) {
               var1.W();
            }

            PetsModule var2 = PetsModule.I();
            if (var2 != null) {
               var2.Q();
            }

            CryptoPetsModule var3 = CryptoPetsModule.O();
            if (var3 != null) {
               var3.E();
            }

            DamageMarkersModule var4 = DamageMarkersModule.t();
            if (var4 != null) {
               var4.M();
            }

            WorldLyricsModule var5 = WorldLyricsModule.e();
            if (var5 != null) {
               var5.A();
            }

            KillFXModule var6 = KillFXModule.t();
            if (var6 != null) {
               var6.r();
            }

            M.forEach(mr::c);
            q = var0.level;
            j = null;
            h = null;
            J.J();
            c.V();
            b.k();
            P.x();
            l_.f();
            AmbientParticlesModule var7 = AmbientParticlesModule.f();
            if (var7 != null) {
               var7.a();
            }

            JumpFXModule var8 = JumpFXModule.f();
            if (var8 != null) {
               var8.q();
            }

            BlockOverlayModule var9 = BlockOverlayModule.T();
            if (var9 != null) {
               var9.a();
            }

            cd.R();
            sa.u();
            bu.D();
            sm.q();
            he.t();
            j4.r();
            ChamsModule var10 = ChamsModule.C();
            if (var10 != null) {
               var10.C();
            }

            PlayerModelModule var11 = PlayerModelModule.E();
            if (var11 != null) {
               var11.z();
            }

            Y = null;
            m4.s();
            m4.i();
            com.larpclient.h.U("Render", "World changed; transient effects cleared");
         }

         if (!var0.isPaused()) {
            cf.o();
            P.n();
            J.C(1.0, (var1x, var2x) -> b5.r(var0.level, var1x, var2x));
            c.w();
            b.G();
            KillFXModule var12 = KillFXModule.t();
            if (var12 != null) {
               W("KillFX", () -> var12.i());
            }
         }
      });
      LevelExtractionEvents.AFTER_BLOCK_OUTLINE_EXTRACTION.register((AfterBlockOutlineExtraction)(var0, var1) -> {
         BlockOverlayModule var2 = BlockOverlayModule.T();
         if (var2 != null) {
            var2.C(var0, var1);
         }
      });
      LevelRenderEvents.BEFORE_BLOCK_OUTLINE.register((BeforeBlockOutline)(var0, var1) -> {
         BlockOverlayModule var2 = BlockOverlayModule.T();
         return var2 == null || p2.c() || !var2.k(var1.pos(), var0.levelState());
      });
      LevelExtractionEvents.END_EXTRACTION.register(kz::x);
      LevelRenderEvents.AFTER_OPAQUE_TERRAIN.register((AfterOpaqueTerrain)var0 -> p2.i());
      LevelRenderEvents.BEFORE_TRANSLUCENT_TERRAIN.register((BeforeTranslucentTerrain)var0 -> p2.U(() -> T(var0)));
      LevelRenderEvents.END_MAIN.register((EndMain)var0 -> p2.u(var0, () -> B(var0)));
      bu.r();
      ClientLifecycleEvents.CLIENT_STOPPING.register((ClientStopping)var0 -> {
         p2.q();
         wv.h();
         W("Scene resource cleanup", so::b);
         W("Refraction cleanup", l3::l);
         ly.a();
         my.K();
         DamageMarkersModule var1 = DamageMarkersModule.t();
         if (var1 != null) {
            var1.M();
         }

         WorldLyricsModule var2 = WorldLyricsModule.e();
         if (var2 != null) {
            var2.L();
         }

         KillFXModule var3 = KillFXModule.t();
         if (var3 != null) {
            var3.r();
         }

         M.forEach(mr::c);
         wa.b();
         he.t();
         bu.D();
         sm.q();
         ChamsModule var4 = ChamsModule.C();
         if (var4 != null) {
            var4.C();
         }

         P.x();
         l_.f();
         AmbientParticlesModule var5 = AmbientParticlesModule.f();
         if (var5 != null) {
            var5.a();
         }

         JumpFXModule var6 = JumpFXModule.f();
         if (var6 != null) {
            var6.q();
         }

         BlockOverlayModule var7 = BlockOverlayModule.T();
         if (var7 != null) {
            var7.a();
         }

         LineGlyphsModule var8 = LineGlyphsModule.w();
         if (var8 != null) {
            var8.i();
         }

         PearlParticlesModule var9 = PearlParticlesModule.G();
         if (var9 != null) {
            var9.Q();
         }

         SkyShaderModule var10 = SkyShaderModule.t();
         if (var10 != null) {
            var10.O();
         }

         SnowGroundModule var11 = SnowGroundModule.C();
         if (var11 != null) {
            var11.N();
         }

         l2.C();
         sx.t();
         com.larpclient.m.V();
         lg.S();
         if (D != null) {
            D.close();
         }
      });
      com.larpclient.h.A("Render", "Shared effects initialized; depth-tested ribbons, particle pool, bloom and blur");
   }

   public static void R(LivingEntity var0) {
      H(var0);
      HitFXModule var1 = HitFXModule.N();
      if (var1 != null && var1.isOperational()) {
         var1.M(var0);
      }
   }

   public static void H(LivingEntity var0) {
      Y = var0;
      H = cf.I();
   }

   public static LivingEntity p() {
      return Y != null && Y.isAlive() && Y.level() == Minecraft.getInstance().level && cf.I() - H < 3.0 ? Y : null;
   }

   private static void b() {
      so.W();
      l3.E();
      if (E != wa.s) {
         E = wa.s;
         ai.U(ai.N);
         kj.e();
      }
   }

   private static void T(LevelRenderContext var0) {
      if (E != wa.s) {
         d = (boolean)0;
      }

      if (!d && var0.poseStack() != null && var0.levelState() == j) {
         if (Minecraft.getInstance().level != null) {
            if (D == null) {
               D = new hh();
            }

            GpuTextureView var1 = RenderSystem.outputColorTextureOverride;
            GpuTextureView var2 = RenderSystem.outputDepthTextureOverride;
            String var3 = "shader initialization before translucent terrain";

            try {
               b();
               x = (boolean)1;
               EntityRenderState var4 = h;
               var3 = "SelfVisuals outline mask / player material before translucent terrain";
               J(var0, var4);
               var3 = "Comets before translucent terrain";
               kj var5 = new kj(var0.poseStack(), D, var0.levelState().cameraRenderState);
               a("Comets", var5, () -> {
                  l_.i(var5, false);
                  D.K();
               });
               so.K();
               so.v();
               if (var4 != null && m || l_.A()) {
                  so.k();
               }
            } catch (RuntimeException var9) {
               d = (boolean)1;
               com.larpclient.h.m("Render", "VisualRuntime stage=" + var3 + "; output attachments restored; retry on resource reload", var9);
            } finally {
               RenderSystem.outputColorTextureOverride = var1;
               RenderSystem.outputDepthTextureOverride = var2;
            }
         }
      }
   }

   private static void J(LevelRenderContext var0, EntityRenderState var1) {
      if (var1 != null && !Boolean.valueOf(x).equals(i)) {
         i = x;
         if (x) {
            com.larpclient.h.A("SelfVisuals", "Drawing before translucent terrain; glass, water and ice blend over the player");
         } else {
            com.larpclient.h.g("SelfVisuals", "Hook before translucent terrain did not run; drawing at END_MAIN, where glass hides the player");
         }
      }

      W("SelfVisuals outline", () -> sm.M(var0.poseStack(), var1));
      if (var1 != null && z == j4.j.e && Q > 0.0 && g8.s()) {
         W("Liquid Glass refraction", () -> l3.N("Liquid Glass", () -> {
            try {
               j4.b(var1, var0.poseStack(), D, j4.j.M, -1, y, true, false);
               D.K();
            } finally {
               D.T();
            }
         }, Q));
      }

      if (var1 != null && z != null && (z != j4.j.e || g8.s())) {
         W("SelfVisuals", () -> {
            long var2 = zj.m();
            j4.I(var1, var0.poseStack(), D, z, B, y, true);
            D.K();
            so.K();
            so.v();
            zj.l("SelfVisuals body material", var2);
         });
      }
   }

   private static void B(LevelRenderContext var0) {
      if (E != wa.s) {
         d = (boolean)0;
      }

      if (!d && var0.poseStack() != null && var0.levelState() == j) {
         Minecraft var1 = Minecraft.getInstance();
         if (var1.level != null) {
            if (D == null) {
               D = new hh();
            }

            GpuTextureView var2 = RenderSystem.outputColorTextureOverride;
            GpuTextureView var3 = RenderSystem.outputDepthTextureOverride;
            String var4 = "shader initialization";

            try {
               if (x) {
                  so.K();
                  so.v();
               } else {
                  b();
               }

               float var5 = Z;
               kj var6 = new kj(var0.poseStack(), D, var0.levelState().cameraRenderState);
               var4 = "Wings Ender Dragon opaque pass";
               WingsModule var7 = WingsModule.D();
               if (var7 != null && var7.isOperational() && var7.C()) {
                  W("Wings", () -> {
                     if (var7.p(var0.poseStack(), D)) {
                        D.K();
                        so.K();
                        so.v();
                     }
                  });
               }

               AmbientParticlesModule var8 = AmbientParticlesModule.f();
               int var9 = var8 != null && var8.isOperational() && !p.contains("Ambient Particles") ? 1 : 0;
               he.W(!p.contains("Particles") && J.L() || var9 != 0 && var8.B().p() || wv.W());
               var4 = "Particles/Trails/Body Trails/TargetAura/GhostEcho/Comets/HitZone/BlockOverlay base geometry";
               S(var0, var6, var5, false);
               PlayerModelModule var10 = PlayerModelModule.E();
               if (var10 != null) {
                  W("PlayerModel", () -> var10.F(var0.poseStack(), D));
               }

               OrderDropperModule var11 = OrderDropperModule.V();
               if (var11 != null) {
                  W("Order Dropper", () -> var11.F(var0.poseStack(), D, var0.levelState().cameraRenderState.pos));
               }

               D.K();
               so.K();
               so.v();
               var4 = "Chams nearest-surface gradient silhouette";
               ChamsModule var12 = ChamsModule.C();
               if (var12 != null) {
                  W("Chams", () -> p2.A(() -> var12.l(var0.poseStack(), D, var5)));
               }

               EntityRenderState var13 = h;
               var4 = "SelfVisuals outline mask / player material";
               if (!x) {
                  J(var0, var13);
               }

               TargetAuraModule var14 = TargetAuraModule.B();
               DamageMarkersModule var15 = DamageMarkersModule.t();
               if (var15 != null) {
                  W("DamageMarkers", () -> var15.D(var0.poseStack(), var0.levelState().cameraRenderState));
               }

               WorldLyricsModule var16 = WorldLyricsModule.e();
               if (var16 != null) {
                  W("WorldLyrics", () -> var16.e(var0.poseStack(), var0.levelState().cameraRenderState, false));
               }

               JumpFXModule var17 = JumpFXModule.f();
               BlockOverlayModule var18 = BlockOverlayModule.T();
               KillFXModule var19 = KillFXModule.t();
               var4 = "ChinaHat after player materials";
               ChinaHatModule var20 = ChinaHatModule.N();
               if (var20 != null) {
                  W("ChinaHat", () -> {
                     var20.R(var6, var5);
                     D.K();
                  });
               }

               var4 = "Wings after player materials";
               if (var7 != null && var7.isOperational() && !var7.C()) {
                  a("Wings", var6, () -> var7.d(var6, D));
               }

               RenderTarget var21 = !wv.n()
                     && !S(cf.F())
                     && (p.contains("Particles") || !J.o())
                     && (var9 == 0 || !var8.B().g())
                     && (p.contains("Trails") || !c.P())
                     && (p.contains("Body Trails") || !b.u())
                     && (p.contains("BlockOverlay") || var18 == null || !var18.g(var0.levelState()))
                     && !M.stream().anyMatch(var1x -> !p.contains(var1x.Z()) && var1x.S(var0.levelState()))
                     && (p.contains("Comets") || !l_.A())
                     && (p.contains("JumpFX") || var17 == null || !var17.d())
                     && (p.contains("KillFX") || var19 == null || !var19.t())
                     && (p.contains("HitZone") || !com.larpclient.module.k.E())
                     && (p.contains("GhostEcho") || !P.T())
                     && (p.contains("TargetAura") || var14 == null || !var14.j())
                     && (p.contains("SelfVisuals") || var13 == null || !m || !u("SelfVisuals bloom"))
                     && (p.contains("Wings") || var7 == null || !var7.w())
                     && (p.contains("ChinaHat") || var20 == null || !var20.d())
                     && (p.contains("WorldLyrics") || var16 == null || !var16.l())
                  ? null
                  : wa.x();
               var4 = "explicit bloom mask / composite";
               if (var21 != null) {
                  RenderSystem.outputColorTextureOverride = var21.getColorTextureView();
                  RenderSystem.outputDepthTextureOverride = var21.getDepthTextureView();
                  S(var0, var6, var5, true);
                  if (var16 != null && var16.l()) {
                     W("WorldLyrics", () -> var16.e(var0.poseStack(), var0.levelState().cameraRenderState, true));
                  }

                  if (var7 != null && var7.w()) {
                     a("Wings", var6, () -> var7.x(var6, D));
                  }

                  if (var20 != null && !p.contains("ChinaHat") && var20.d()) {
                     W("ChinaHat", () -> var20.u(var6, D));
                  }

                  GpuTextureView var22 = x ? so.N() : null;
                  if (var22 != null) {
                     RenderSystem.outputDepthTextureOverride = var22;
                  }

                  if (x) {
                     a("Comets", var6, () -> {
                        l_.i(var6, true);
                        D.K();
                     });
                  }

                  if (var13 != null && m) {
                     W("SelfVisuals bloom", () -> {
                        j4.b(var13, var0.poseStack(), D, z == j4.j.e ? j4.j.y : z, B, z == j4.j.e ? y * 0.25 : 0.25, true, false);
                        D.K();
                     });
                  }

                  RenderSystem.outputColorTextureOverride = var2;
                  RenderSystem.outputDepthTextureOverride = var3;
                  wa.E();
               }
            } catch (RuntimeException var28) {
               d = (boolean)1;
               com.larpclient.h.m("Render", "VisualRuntime stage=" + var4 + "; output attachments restored; retry on resource reload", var28);
            } finally {
               RenderSystem.outputColorTextureOverride = var2;
               RenderSystem.outputDepthTextureOverride = var3;
               DamageMarkersModule var24 = DamageMarkersModule.t();
               if (var24 != null) {
                  var24.w();
               }

               WorldLyricsModule var25 = WorldLyricsModule.e();
               if (var25 != null) {
                  var25.n();
               }

               if (D != null) {
                  D.g();
               }

               W("Refraction cleanup", l3::e);
               W("Scene resource cleanup", so::V);
            }
         }
      }
   }

   public static void E(GuiGraphicsExtractor var0) {
      if (x()) {
         PetsModule var1 = PetsModule.I();
         if (var1 != null) {
            W("Pets", () -> var1.b(var0));
         }
      }
   }

   private static boolean S(double var0) {
      LineGlyphsModule var2 = LineGlyphsModule.w();
      PearlParticlesModule var3 = PearlParticlesModule.G();
      CrystalChamsModule var4 = CrystalChamsModule.Y();
      PetsModule var5 = PetsModule.I();
      CryptoPetsModule var6 = CryptoPetsModule.O();
      return !p.contains("Pets") && var5 != null && var5.I()
         || !p.contains("CryptoPets") && var6 != null && var6.m()
         || !p.contains("CrystalChams") && var4 != null && var4.B()
         || !p.contains("LineGlyphs") && var2 != null && var2.O(var0)
         || !p.contains("PearlParticles") && var3 != null && var3.n(var0);
   }

   private static void S(LevelRenderContext var0, kj var1, float var2, boolean var3) {
      hh var4 = D;
      wv.K(var1, var3);

      for (mr var6 : M) {
         a(var6.Z(), var1, () -> var6.H(var1, var3, var0.levelState()));
      }

      SnowGroundModule var16 = SnowGroundModule.C();
      if (!var3 && var16 != null) {
         a("SnowGround", var1, () -> var16.p(var1));
      }

      CrystalChamsModule var17 = CrystalChamsModule.Y();
      if (var17 != null) {
         a("CrystalChams", var1, () -> var17.E(var1, var3));
      }

      PetsModule var7 = PetsModule.I();
      if (var7 != null) {
         a("Pets", var1, () -> var7.X(var1, var3));
      }

      CryptoPetsModule var8 = CryptoPetsModule.O();
      if (var8 != null) {
         a("CryptoPets", var1, () -> var8.I(var1, var3));
      }

      a("EmoteProps", var1, () -> la.t(var1, var3));
      a("Particles", var1, () -> J.G(var1, var0.levelState().cameraRenderState.pos, var2, var3));
      a("Trails", var1, () -> c.j(var1, var2, var3));
      a("Body Trails", var1, () -> b.B(var1, var3));
      if (!x) {
         a("Comets", var1, () -> l_.i(var1, var3));
      }

      AmbientParticlesModule var9 = AmbientParticlesModule.f();
      if (var9 != null && var9.isOperational()) {
         a("Ambient Particles", var1, () -> var9.B().V(var1, var0.levelState().cameraRenderState.pos, var2, var3));
      }

      JumpFXModule var10 = JumpFXModule.f();
      if (var10 != null) {
         a("JumpFX", var1, () -> var10.Z(var1, var3));
      }

      KillFXModule var11 = KillFXModule.t();
      if (var11 != null) {
         a("KillFX", var1, () -> var11.H(var1, var0.poseStack(), var4, var3));
      }

      BlockOverlayModule var12 = BlockOverlayModule.T();
      if (var12 != null) {
         a("BlockOverlay", var1, () -> var12.b(var1, var3, var0.levelState()));
      }

      a("HitZone", var1, () -> com.larpclient.module.k.h(var1, var3));
      TargetAuraModule var13 = TargetAuraModule.B();
      if (var13 != null) {
         a("TargetAura", var1, () -> var13.X(var1, var2, var3));
      }

      a("GhostEcho", var1, () -> P.f(var0.poseStack(), var4, var3));
      LineGlyphsModule var14 = LineGlyphsModule.w();
      if (var14 != null) {
         W("LineGlyphs", () -> var14.S(var0.poseStack(), var4, var0.levelState().cameraRenderState, cf.F(), var3));
      }

      PearlParticlesModule var15 = PearlParticlesModule.G();
      if (var15 != null) {
         W("PearlParticles", () -> var15.F(var0.poseStack(), var4, var0.levelState().cameraRenderState, cf.F(), var3));
      }

      W("Shared geometry", var4::K);
   }
}
