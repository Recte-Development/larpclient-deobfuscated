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
import com.larpclient.module.d4;
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

public final class h {
   public static final cg P = new cg();
   public static final uz e = new uz();
   public static final _2 G = new _2();
   public static final z0 H = new z0();
   private static List<pp> D = List.of();
   private static xh O;
   private static ClientLevel w;
   private static boolean E;
   private static int g = -1;
   private static LivingEntity C;
   private static double q;
   private static LevelRenderState A;
   private static float x;
   private static EntityRenderState n;
   private static tj.b B;
   private static double V;
   private static double b;
   private static int u;
   private static boolean T;
   private static boolean J;
   private static Boolean a;
   private static final Set<String> Z = new HashSet<>();
   private static int s = -1;

   static void K(String var0, Runnable var1) {
      if (s != m5.k) {
         Z.clear();
         s = m5.k;
      }

      if (!Z.contains(var0)) {
         try {
            var1.run();
         } catch (RuntimeException var4) {
            Z.add(var0);
            mb.A(var0);
            if (var0.equals("Chams")) {
               ChamsModule var3 = ChamsModule.u();
               if (var3 != null) {
                  var3.w();
               }
            }

            if (var0.equals("CrystalChams")) {
               CrystalChamsModule var5 = CrystalChamsModule.O();
               if (var5 != null) {
                  var5.k();
               }
            }

            if (var0.equals("Pets")) {
               PetsModule var6 = PetsModule.t();
               if (var6 != null) {
                  var6.B();
               }
            }

            if (var0.equals("CryptoPets")) {
               CryptoPetsModule var7 = CryptoPetsModule.C();
               if (var7 != null) {
                  var7.P();
               }
            }

            if (var0.equals("EmoteProps")) {
               hc.Q();
            }

            if (var0.equals("PlayerModel")) {
               PlayerModelModule var8 = PlayerModelModule.Y();
               if (var8 != null) {
                  var8.O();
               }
            }

            if (var0.equals("SnowGround")) {
               SnowGroundModule var9 = SnowGroundModule.d();
               if (var9 != null) {
                  var9.N();
               }
            }

            if (var0.equals("KillFX")) {
               KillFXModule var10 = KillFXModule.l();
               if (var10 != null) {
                  var10.c();
               }
            }

            if (O != null) {
               O.o();
            }

            mt.P("Render", "Cosmetic stage=" + var0 + "; isolated until resource reload", var4);
         }
      }
   }

   static boolean m(String var0) {
      return !R(var0);
   }

   public static boolean R(String var0) {
      return s != m5.k || !Z.contains(var0);
   }

   private static void I(String var0, ki var1, Runnable var2) {
      K(var0, () -> {
         try {
            var2.run();
         } finally {
            var1.v(false);
            var1.blend(pf.x.S);
         }
      });
   }

   private static void z(LevelExtractionContext var0) {
      A = var0.levelState();
      J = (boolean)0;

      for (pp var2 : D) {
         K(var2.u(), () -> var2.Y(var0));
      }

      x = var0.deltaTracker().getGameTimeDeltaPartialTick(false);
      CameraRenderState var17 = A.cameraRenderState;
      p7.t(var17);
      mb.P(x, var17);
      PlayerModelModule var18 = PlayerModelModule.Y();
      if (var18 != null) {
         K("PlayerModel", () -> var18.r(x, var17.pos));
      }

      JumpFXModule var3 = JumpFXModule.m();
      if (var3 != null) {
         K("JumpFX", var3::l);
      }

      KillFXModule var4 = KillFXModule.l();
      if (var4 != null) {
         K("KillFX", () -> var4.P(var17));
      }

      CrystalChamsModule var5 = CrystalChamsModule.O();
      if (var5 != null) {
         K("CrystalChams", () -> var5.k(x));
      }

      PetsModule var6 = PetsModule.t();
      if (var6 != null) {
         K("Pets", () -> var6.r(x, var17));
      }

      CryptoPetsModule var7 = CryptoPetsModule.C();
      if (var7 != null) {
         K("CryptoPets", () -> var7.I(x, var17));
      }

      K("EmoteProps", () -> hc.H(x));
      SnowGroundModule var8 = SnowGroundModule.d();
      if (var8 != null) {
         K("SnowGround", () -> var8.o(x, var17));
      }

      K("Trails", () -> e.P(var17.pos, x));
      K("Body Trails", () -> G.U(var17.pos, x));
      K("GhostEcho", () -> H.b(var17.pos));
      K("Comets", () -> mu.s(x));
      K("HitZone", d4::O);
      TargetAuraModule var9 = TargetAuraModule.k();
      if (var9 != null) {
         K("TargetAura", () -> var9.N(x));
      }

      K("Particles", () -> P.q(var17.pos, x, var17.cullFrustum));
      AmbientParticlesModule var10 = AmbientParticlesModule.v();
      if (var10 != null && var10.isOperational()) {
         K("Ambient Particles", () -> var10.E().G(var17.pos, x, var17.cullFrustum));
      }

      ChamsModule var11 = ChamsModule.u();
      if (var11 != null) {
         K("Chams", () -> var11.s(x));
      }

      ChinaHatModule var12 = ChinaHatModule.i();
      if (var12 != null) {
         K("ChinaHat", () -> var12.w(x));
      }

      WingsModule var13 = WingsModule.d();
      if (var13 != null) {
         K("Wings", () -> var13.E(x));
      }

      DamageMarkersModule var14 = DamageMarkersModule.U();
      if (var14 != null) {
         K("DamageMarkers", () -> var14.G(var17));
      }

      WorldLyricsModule var15 = WorldLyricsModule.s();
      if (var15 != null) {
         K("WorldLyrics", () -> var15.H(var17));
      }

      n = null;
      SelfVisualsModule var16 = SelfVisualsModule.V();
      B = null;
      T = (boolean)0;
      b = 0.0;
      if (var16 != null && var16.M() && var16.T()) {
         K("SelfVisuals", () -> {
            n = tj.s(Minecraft.getInstance().player, x);
            B = !var16.v() ? null : (var16.S() == null ? tj.b.n : var16.S());
            V = var16.A();
            u = f.C;
            b = var16.B() && var16.M.P() ? var16.G.P() : 0.0;
            T = (boolean)(!var16.v() || !var16.V.P() || var16.B() && _k.t == m5.n.N ? 0 : 1);
         });
      }
   }

   public static boolean V() {
      return !E;
   }

   public static boolean c(EntityRenderState var0) {
      return n != null && B != null && A != null && A.entityRenderStates.contains(var0);
   }

   public static void s() {
      mb.i(LarpClient.F().x().C());
      D = LarpClient.F().x().C().stream().filter(var0 -> var0 instanceof pp.x).map(var0 -> ((pp.x)var0).placementHighlight()).toList();
      ClientTickEvents.END_CLIENT_TICK.register((EndTick)var0 -> {
         JumpFXModule var1 = JumpFXModule.m();
         if (var1 != null && !var0.isPaused()) {
            var1.F();
         }

         if (!var0.isPaused()) {
            mb.i(var0);
         }
      });
      ClientTickEvents.START_CLIENT_TICK.register((StartTick)var0 -> {
         ma.a();
         if (var0.level != w) {
            mb.e();
            K("Scene resource cleanup", ex::x);
            K("Refraction cleanup", _c::o);
            m1.f();
            xw.F();
            ma.B();
            CrystalChamsModule var1 = CrystalChamsModule.O();
            if (var1 != null) {
               var1.k();
            }

            PetsModule var2 = PetsModule.t();
            if (var2 != null) {
               var2.B();
            }

            CryptoPetsModule var3 = CryptoPetsModule.C();
            if (var3 != null) {
               var3.P();
            }

            DamageMarkersModule var4 = DamageMarkersModule.U();
            if (var4 != null) {
               var4.b();
            }

            WorldLyricsModule var5 = WorldLyricsModule.s();
            if (var5 != null) {
               var5.n();
            }

            KillFXModule var6 = KillFXModule.l();
            if (var6 != null) {
               var6.c();
            }

            D.forEach(pp::P);
            w = var0.level;
            A = null;
            n = null;
            P.J();
            e.p();
            G.o();
            H.C();
            mu.s();
            AmbientParticlesModule var7 = AmbientParticlesModule.v();
            if (var7 != null) {
               var7.e();
            }

            JumpFXModule var8 = JumpFXModule.m();
            if (var8 != null) {
               var8.o();
            }

            BlockOverlayModule var9 = BlockOverlayModule.c();
            if (var9 != null) {
               var9.r();
            }

            eb.s();
            x6.o();
            u1.I();
            zj.g();
            _v.w();
            tj.Q();
            ChamsModule var10 = ChamsModule.u();
            if (var10 != null) {
               var10.w();
            }

            PlayerModelModule var11 = PlayerModelModule.Y();
            if (var11 != null) {
               var11.a();
            }

            C = null;
            mq.M();
            mq.N();
            mt.U("Render", "World changed; transient effects cleared");
         }

         if (!var0.isPaused()) {
            ty.w();
            H.y();
            P.O(1.0, (var1x, var2x) -> w8.G(var0.level, var1x, var2x));
            e.u();
            G.q();
            KillFXModule var12 = KillFXModule.l();
            if (var12 != null) {
               K("KillFX", () -> var12.J());
            }
         }
      });
      LevelExtractionEvents.AFTER_BLOCK_OUTLINE_EXTRACTION.register((AfterBlockOutlineExtraction)(var0, var1) -> {
         BlockOverlayModule var2 = BlockOverlayModule.c();
         if (var2 != null) {
            var2.m(var0, var1);
         }
      });
      LevelRenderEvents.BEFORE_BLOCK_OUTLINE.register((BeforeBlockOutline)(var0, var1) -> {
         BlockOverlayModule var2 = BlockOverlayModule.c();
         return var2 == null || zo.W() || !var2.W(var1.pos(), var0.levelState());
      });
      LevelExtractionEvents.END_EXTRACTION.register(h::z);
      LevelRenderEvents.AFTER_OPAQUE_TERRAIN.register((AfterOpaqueTerrain)var0 -> zo.P());
      LevelRenderEvents.BEFORE_TRANSLUCENT_TERRAIN.register((BeforeTranslucentTerrain)var0 -> zo.F(() -> U(var0)));
      LevelRenderEvents.END_MAIN.register((EndMain)var0 -> zo.v(var0, () -> O(var0)));
      u1.w();
      ClientLifecycleEvents.CLIENT_STOPPING.register((ClientStopping)var0 -> {
         zo.e();
         mb.e();
         K("Scene resource cleanup", ex::x);
         K("Refraction cleanup", _c::o);
         m1.f();
         ma.B();
         DamageMarkersModule var1 = DamageMarkersModule.U();
         if (var1 != null) {
            var1.b();
         }

         WorldLyricsModule var2 = WorldLyricsModule.s();
         if (var2 != null) {
            var2.d();
         }

         KillFXModule var3 = KillFXModule.l();
         if (var3 != null) {
            var3.c();
         }

         D.forEach(pp::P);
         m5.A();
         _v.w();
         u1.I();
         zj.g();
         ChamsModule var4 = ChamsModule.u();
         if (var4 != null) {
            var4.w();
         }

         H.C();
         mu.s();
         AmbientParticlesModule var5 = AmbientParticlesModule.v();
         if (var5 != null) {
            var5.e();
         }

         JumpFXModule var6 = JumpFXModule.m();
         if (var6 != null) {
            var6.o();
         }

         BlockOverlayModule var7 = BlockOverlayModule.c();
         if (var7 != null) {
            var7.r();
         }

         LineGlyphsModule var8 = LineGlyphsModule.I();
         if (var8 != null) {
            var8.h();
         }

         PearlParticlesModule var9 = PearlParticlesModule.r();
         if (var9 != null) {
            var9.E();
         }

         SkyShaderModule var10 = SkyShaderModule.U();
         if (var10 != null) {
            var10.m();
         }

         SnowGroundModule var11 = SnowGroundModule.d();
         if (var11 != null) {
            var11.v();
         }

         dg.b();
         xs.d();
         zh.O();
         oa.U();
         if (O != null) {
            O.close();
         }
      });
      mt.K("Render", "Shared effects initialized; depth-tested ribbons, particle pool, bloom and blur");
   }

   public static void D(LivingEntity var0) {
      z(var0);
      HitFXModule var1 = HitFXModule.q();
      if (var1 != null && var1.isOperational()) {
         var1.v(var0);
      }
   }

   public static void z(LivingEntity var0) {
      C = var0;
      q = ty.P();
   }

   public static LivingEntity o() {
      return C != null && C.isAlive() && C.level() == Minecraft.getInstance().level && ty.P() - q < 3.0 ? C : null;
   }

   private static void T() {
      ex.L();
      _c.U();
      if (g != m5.k) {
         g = m5.k;
         ea.z(ea.U);
         ki.R();
      }
   }

   private static void U(LevelRenderContext var0) {
      if (g != m5.k) {
         E = (boolean)0;
      }

      if (!E && var0.poseStack() != null && var0.levelState() == A) {
         if (Minecraft.getInstance().level != null) {
            if (O == null) {
               O = new xh();
            }

            GpuTextureView var1 = xn.j;
            GpuTextureView var2 = xn.o;
            String var3 = "shader initialization before translucent terrain";

            try {
               T();
               J = (boolean)1;
               EntityRenderState var4 = n;
               var3 = "SelfVisuals outline mask / player material before translucent terrain";
               H(var0, var4);
               var3 = "Comets before translucent terrain";
               ki var5 = new ki(var0.poseStack(), O, var0.levelState().cameraRenderState);
               I("Comets", var5, () -> {
                  mu.b(var5, false);
                  O.b();
               });
               ex.w();
               ex.n();
               if (var4 != null && T || mu.b()) {
                  ex.s();
               }
            } catch (RuntimeException var9) {
               E = (boolean)1;
               mt.P("Render", "VisualRuntime stage=" + var3 + "; output attachments restored; retry on resource reload", var9);
            } finally {
               xn.j = var1;
               xn.o = var2;
            }
         }
      }
   }

   private static void H(LevelRenderContext var0, EntityRenderState var1) {
      if (var1 != null && !Boolean.valueOf(J).equals(a)) {
         a = J;
         if (J) {
            mt.K("SelfVisuals", "Drawing before translucent terrain; glass, water and ice blend over the player");
         } else {
            mt.T("SelfVisuals", "Hook before translucent terrain did not run; drawing at END_MAIN, where glass hides the player");
         }
      }

      K("SelfVisuals outline", () -> zj.G(var0.poseStack(), var1));
      if (var1 != null && B == tj.b.X && b > 0.0 && p7.B()) {
         K("Liquid Glass refraction", () -> _c.i("Liquid Glass", () -> {
            try {
               tj.E(var1, var0.poseStack(), O, tj.b.j, -1, V, true, false);
               O.b();
            } finally {
               O.o();
            }
         }, b));
      }

      if (var1 != null && B != null && (B != tj.b.X || p7.B())) {
         K("SelfVisuals", () -> {
            long var2 = kx.G();
            tj.u(var1, var0.poseStack(), O, B, u, V, true);
            O.b();
            ex.w();
            ex.n();
            kx.u("SelfVisuals body material", var2);
         });
      }
   }

   private static void O(LevelRenderContext var0) {
      if (g != m5.k) {
         E = (boolean)0;
      }

      if (!E && var0.poseStack() != null && var0.levelState() == A) {
         Minecraft var1 = Minecraft.getInstance();
         if (var1.level != null) {
            if (O == null) {
               O = new xh();
            }

            GpuTextureView var2 = xn.j;
            GpuTextureView var3 = xn.o;
            String var4 = "shader initialization";

            try {
               if (J) {
                  ex.w();
                  ex.n();
               } else {
                  T();
               }

               float var5 = x;
               ki var6 = new ki(var0.poseStack(), O, var0.levelState().cameraRenderState);
               var4 = "Wings Ender Dragon opaque pass";
               WingsModule var7 = WingsModule.d();
               if (var7 != null && var7.isOperational() && var7.P()) {
                  K("Wings", () -> {
                     if (var7.D(var0.poseStack(), O)) {
                        O.b();
                        ex.w();
                        ex.n();
                     }
                  });
               }

               AmbientParticlesModule var8 = AmbientParticlesModule.v();
               int var9 = var8 != null && var8.isOperational() && !Z.contains("Ambient Particles") ? 1 : 0;
               _v.n(!Z.contains("Particles") && P.C() || var9 != 0 && var8.E().P() || mb.f());
               var4 = "Particles/Trails/Body Trails/TargetAura/GhostEcho/Comets/HitZone/BlockOverlay base geometry";
               s(var0, var6, var5, false);
               PlayerModelModule var10 = PlayerModelModule.Y();
               if (var10 != null) {
                  K("PlayerModel", () -> var10.a(var0.poseStack(), O));
               }

               OrderDropperModule var11 = OrderDropperModule.i();
               if (var11 != null) {
                  K("Order Dropper", () -> var11.b(var0.poseStack(), O, var0.levelState().cameraRenderState.pos));
               }

               O.b();
               ex.w();
               ex.n();
               var4 = "Chams nearest-surface gradient silhouette";
               ChamsModule var12 = ChamsModule.u();
               if (var12 != null) {
                  K("Chams", () -> zo.w(() -> var12.N(var0.poseStack(), O, var5)));
               }

               EntityRenderState var13 = n;
               var4 = "SelfVisuals outline mask / player material";
               if (!J) {
                  H(var0, var13);
               }

               TargetAuraModule var14 = TargetAuraModule.k();
               DamageMarkersModule var15 = DamageMarkersModule.U();
               if (var15 != null) {
                  K("DamageMarkers", () -> var15.x(var0.poseStack(), var0.levelState().cameraRenderState));
               }

               WorldLyricsModule var16 = WorldLyricsModule.s();
               if (var16 != null) {
                  K("WorldLyrics", () -> var16.N(var0.poseStack(), var0.levelState().cameraRenderState, false));
               }

               JumpFXModule var17 = JumpFXModule.m();
               BlockOverlayModule var18 = BlockOverlayModule.c();
               KillFXModule var19 = KillFXModule.l();
               var4 = "ChinaHat after player materials";
               ChinaHatModule var20 = ChinaHatModule.i();
               if (var20 != null) {
                  K("ChinaHat", () -> {
                     var20.h(var6, var5);
                     O.b();
                  });
               }

               var4 = "Wings after player materials";
               if (var7 != null && var7.isOperational() && !var7.P()) {
                  I("Wings", var6, () -> var7.b(var6, O));
               }

               RenderTarget var21 = !mb.h()
                     && !r(ty.K())
                     && (Z.contains("Particles") || !P.r())
                     && (var9 == 0 || !var8.E().N())
                     && (Z.contains("Trails") || !e.f())
                     && (Z.contains("Body Trails") || !G.E())
                     && (Z.contains("BlockOverlay") || var18 == null || !var18.o(var0.levelState()))
                     && !D.stream().anyMatch(var1x -> !Z.contains(var1x.u()) && var1x.j(var0.levelState()))
                     && (Z.contains("Comets") || !mu.b())
                     && (Z.contains("JumpFX") || var17 == null || !var17.I())
                     && (Z.contains("KillFX") || var19 == null || !var19.U())
                     && (Z.contains("HitZone") || !d4.Y())
                     && (Z.contains("GhostEcho") || !H.J())
                     && (Z.contains("TargetAura") || var14 == null || !var14.I())
                     && (Z.contains("SelfVisuals") || var13 == null || !T || !R("SelfVisuals bloom"))
                     && (Z.contains("Wings") || var7 == null || !var7.K())
                     && (Z.contains("ChinaHat") || var20 == null || !var20.j())
                     && (Z.contains("WorldLyrics") || var16 == null || !var16.L())
                  ? null
                  : m5.T();
               var4 = "explicit bloom mask / composite";
               if (var21 != null) {
                  xn.j = var21.getColorTextureView();
                  xn.o = var21.getDepthTextureView();
                  s(var0, var6, var5, true);
                  if (var16 != null && var16.L()) {
                     K("WorldLyrics", () -> var16.N(var0.poseStack(), var0.levelState().cameraRenderState, true));
                  }

                  if (var7 != null && var7.K()) {
                     I("Wings", var6, () -> var7.E(var6, O));
                  }

                  if (var20 != null && !Z.contains("ChinaHat") && var20.j()) {
                     K("ChinaHat", () -> var20.O(var6, O));
                  }

                  GpuTextureView var22 = J ? ex.t() : null;
                  if (var22 != null) {
                     xn.o = var22;
                  }

                  if (J) {
                     I("Comets", var6, () -> {
                        mu.b(var6, true);
                        O.b();
                     });
                  }

                  if (var13 != null && T) {
                     K("SelfVisuals bloom", () -> {
                        tj.E(var13, var0.poseStack(), O, B == tj.b.X ? tj.b.T : B, u, B == tj.b.X ? V * 0.25 : 0.25, true, false);
                        O.b();
                     });
                  }

                  xn.j = var2;
                  xn.o = var3;
                  m5.w();
               }
            } catch (RuntimeException var28) {
               E = (boolean)1;
               mt.P("Render", "VisualRuntime stage=" + var4 + "; output attachments restored; retry on resource reload", var28);
            } finally {
               xn.j = var2;
               xn.o = var3;
               DamageMarkersModule var24 = DamageMarkersModule.U();
               if (var24 != null) {
                  var24.h();
               }

               WorldLyricsModule var25 = WorldLyricsModule.s();
               if (var25 != null) {
                  var25.r();
               }

               if (O != null) {
                  O.s();
               }

               K("Refraction cleanup", _c::M);
               K("Scene resource cleanup", ex::h);
            }
         }
      }
   }

   public static void F(GuiGraphicsExtractor var0) {
      if (V()) {
         PetsModule var1 = PetsModule.t();
         if (var1 != null) {
            K("Pets", () -> var1.F(var0));
         }
      }
   }

   private static boolean r(double var0) {
      LineGlyphsModule var2 = LineGlyphsModule.I();
      PearlParticlesModule var3 = PearlParticlesModule.r();
      CrystalChamsModule var4 = CrystalChamsModule.O();
      PetsModule var5 = PetsModule.t();
      CryptoPetsModule var6 = CryptoPetsModule.C();
      return !Z.contains("Pets") && var5 != null && var5.t()
         || !Z.contains("CryptoPets") && var6 != null && var6.h()
         || !Z.contains("CrystalChams") && var4 != null && var4.W()
         || !Z.contains("LineGlyphs") && var2 != null && var2.p(var0)
         || !Z.contains("PearlParticles") && var3 != null && var3.n(var0);
   }

   private static void s(LevelRenderContext var0, ki var1, float var2, boolean var3) {
      xh var4 = O;
      mb.w(var1, var3);

      for (pp var6 : D) {
         I(var6.u(), var1, () -> var6.t(var1, var3, var0.levelState()));
      }

      SnowGroundModule var16 = SnowGroundModule.d();
      if (!var3 && var16 != null) {
         I("SnowGround", var1, () -> var16.h(var1));
      }

      CrystalChamsModule var17 = CrystalChamsModule.O();
      if (var17 != null) {
         I("CrystalChams", var1, () -> var17.G(var1, var3));
      }

      PetsModule var7 = PetsModule.t();
      if (var7 != null) {
         I("Pets", var1, () -> var7.k(var1, var3));
      }

      CryptoPetsModule var8 = CryptoPetsModule.C();
      if (var8 != null) {
         I("CryptoPets", var1, () -> var8.E(var1, var3));
      }

      I("EmoteProps", var1, () -> hc.b(var1, var3));
      I("Particles", var1, () -> P.e(var1, var0.levelState().cameraRenderState.pos, var2, var3));
      I("Trails", var1, () -> e.v(var1, var2, var3));
      I("Body Trails", var1, () -> G.K(var1, var3));
      if (!J) {
         I("Comets", var1, () -> mu.b(var1, var3));
      }

      AmbientParticlesModule var9 = AmbientParticlesModule.v();
      if (var9 != null && var9.isOperational()) {
         I("Ambient Particles", var1, () -> var9.E().n(var1, var0.levelState().cameraRenderState.pos, var2, var3));
      }

      JumpFXModule var10 = JumpFXModule.m();
      if (var10 != null) {
         I("JumpFX", var1, () -> var10.j(var1, var3));
      }

      KillFXModule var11 = KillFXModule.l();
      if (var11 != null) {
         I("KillFX", var1, () -> var11.X(var1, var0.poseStack(), var4, var3));
      }

      BlockOverlayModule var12 = BlockOverlayModule.c();
      if (var12 != null) {
         I("BlockOverlay", var1, () -> var12.s(var1, var3, var0.levelState()));
      }

      I("HitZone", var1, () -> d4.T(var1, var3));
      TargetAuraModule var13 = TargetAuraModule.k();
      if (var13 != null) {
         I("TargetAura", var1, () -> var13.v(var1, var2, var3));
      }

      I("GhostEcho", var1, () -> H.Y(var0.poseStack(), var4, var3));
      LineGlyphsModule var14 = LineGlyphsModule.I();
      if (var14 != null) {
         K("LineGlyphs", () -> var14.M(var0.poseStack(), var4, var0.levelState().cameraRenderState, ty.K(), var3));
      }

      PearlParticlesModule var15 = PearlParticlesModule.r();
      if (var15 != null) {
         K("PearlParticles", () -> var15.p(var0.poseStack(), var4, var0.levelState().cameraRenderState, ty.K(), var3));
      }

      K("Shared geometry", var4::b);
   }
}
