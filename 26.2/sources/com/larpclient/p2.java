package com.larpclient;

import com.larpclient.module.CustomSkyModule;
import com.larpclient.module.Module;
import com.larpclient.module.SkyShaderModule;
import com.larpclient.module.StreamProofModule;
import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.platform.NativeImage;
import com.sun.jna.Platform;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import net.fabricmc.fabric.api.client.rendering.v1.level.LevelRenderContext;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.state.gui.GuiRenderState;
import net.minecraft.gizmos.SimpleGizmoCollector;
import net.minecraft.gizmos.SimpleGizmoCollector.GizmoInstance;
import net.minecraft.util.Util;

public final class p2 {
   private static final pp P = new pp();
   private static final SimpleGizmoCollector Q = new SimpleGizmoCollector();
   private static uj T;
   private static String Z;
   private static boolean S;
   private static boolean j;
   private static boolean B;
   private static long v;
   private static long m;
   private static long z = Long.MAX_VALUE;
   private static long Y = -1L;
   private static boolean u;
   private static long o;
   private static final long V = 400L;
   private static GuiGraphicsExtractor s;
   private static List<GizmoInstance> F = List.of();
   private static int p;
   private static boolean W;
   private static String x;

   private p2() {
   }

   public static void I() {
      v++;
      Minecraft var0 = Minecraft.getInstance();
      StreamProofModule var1 = StreamProofModule.K();
      int var2 = var1 != null && var1.isOperational() ? 1 : 0;
      if (var2 == 0) {
         Z = null;
      }

      int var3 = var2 != 0 && var0.level != null && Z == null && var0.getWindow() != null ? 1 : 0;
      if (var3 == 0) {
         if (T != null) {
            g(var2 != 0 ? "left the world" : "module turned off");
         }

         j = (boolean)0;
         S = (boolean)0;
         l(false);
      } else {
         if (T == null) {
            o(var0);
         } else {
            F(var0);
         }

         if (T != null && (T.L() != null || P.r())) {
            H(T.L() != null ? "overlay presentation failed (" + T.L() + ")" : "layer rendering failed");
         }

         S = (boolean)(T != null ? 1 : 0);
         j = (boolean)(S && var1.z() ? 1 : 0);
         l(j && var1.b());
      }
   }

   private static void l(boolean var0) {
      if (var0 != com.larpclient.o.z()) {
         com.larpclient.o.M(var0);
         if (!var0) {
            h.A("StreamProof", "Scene-altering visuals resume");
         } else {
            ArrayList var1 = new ArrayList();

            for (Module var3 : LarpClient.R().z().h()) {
               if (var3.isEnabled() && StreamProofModule.F.contains(var3.getClass())) {
                  var3.suspendForDisconnect();
                  var1.add(var3.getName());
               }
            }

            h.A("StreamProof", "Hide Everything paused scene-altering visuals for the stream: {}", var1);
         }
      }
   }

   private static void o(Minecraft var0) {
      if (!Platform.isWindows()) {
         Z = "Stream Proof is only available on Windows";
         h.g("StreamProof", Z);
         q("Stream Proof unavailable", Z + ".");
      } else {
         try {
            T = uj.m(var0.getWindow().handle());
            u = Z(var0);
            o = 0L;
            z = Long.MAX_VALUE;
            B = (boolean)0;
            p = Boolean.getBoolean("larp.streamProof.dump") ? 3 : 0;
            h.A("StreamProof", "Enabled; backend={}, Windows build={}, display capture exclusion={}", var0.getWindow().backend().getName(), uj.Z(), T.f());
            if (!T.f()) {
               q("Stream Proof limited", "Game and window capture are hidden; this Windows build cannot hide display capture.");
            }
         } catch (RuntimeException | UnsatisfiedLinkError var3) {
            Z = "overlay window could not be created";
            h.m(
               "StreamProof",
               "Stream Proof could not create its overlay window; the client is drawn normally",
               var3 instanceof RuntimeException var2 ? var2 : new RuntimeException(var3)
            );
            q("Stream Proof unavailable", "The capture-hidden overlay could not start; the client is drawn normally.");
         }
      }
   }

   private static boolean Z(Minecraft var0) {
      return var0.getWindow().isFullscreen();
   }

   private static void F(Minecraft var0) {
      boolean var1 = Z(var0);
      long var2 = Util.getMillis();
      if (var1 != u) {
         u = var1;
         o = var2;
         h.A("StreamProof", "Game window {} fullscreen; recreating the overlay once it settles", var1 ? "entered" : "left");
      } else if (o != 0L && var2 - o >= 400L) {
         o = 0L;
         T.c();
         T = null;
         o(var0);
         if (T != null) {
            h.A("StreamProof", "Overlay recreated after the fullscreen change (fullscreen={})", var1);
         }
      }
   }

   private static void H(String var0) {
      Z = var0;
      g(var0);
      q("Stream Proof stopped", "The client is drawn normally again. Reason: " + var0 + ".");
   }

   private static void g(String var0) {
      if (T != null) {
         T.c();
         T = null;
      }

      P.u();
      F = List.of();
      z = Long.MAX_VALUE;
      h.A("StreamProof", "Disabled ({})", var0);
   }

   public static void q() {
      com.larpclient.o.M(false);
      com.larpclient.o.O(false);
      if (T != null) {
         g("client shutdown");
      } else {
         P.u();
      }

      Z = null;
   }

   public static void P() {
      P.u();
      F = List.of();
   }

   public static boolean q() {
      return T != null;
   }

   public static String u(StreamProofModule var0) {
      if (T == null) {
         return Z != null ? "Unavailable: " + Z : "";
      }

      String var1 = T.f() ? "game, window, display" : "game, window";
      return (var0.z() ? "Hiding all (" : "Keeping visuals (") + var1 + ")";
   }

   private static void q(String var0, String var1) {
      try {
         s0.s().Z(var0, var1, s0.u.p, 6000L);
      } catch (RuntimeException var3) {
      }
   }

   public static GuiGraphicsExtractor x(GuiGraphicsExtractor var0, boolean var1, boolean var2) {
      boolean var3 = var1 ? j : S;
      return !var3 ? var0 : M();
   }

   public static GuiGraphicsExtractor n() {
      return M();
   }

   public static boolean o(Object var0) {
      return P.W(var0) && P.i();
   }

   public static RenderTarget D(Object var0) {
      return P.W(var0) ? P.E() : null;
   }

   private static GuiGraphicsExtractor M() {
      if (Y != v) {
         Minecraft var0 = Minecraft.getInstance();
         int var1 = (int)Math.round(var0.mouseHandler.getScaledXPos(var0.getWindow()));
         int var2 = (int)Math.round(var0.mouseHandler.getScaledYPos(var0.getWindow()));
         s = new GuiGraphicsExtractor(var0, P.v(), var1, var2);
         Y = v;
      }

      return s;
   }

   public static GuiRenderState M(GuiRenderState var0) {
      return S ? P.v() : var0;
   }

   public static boolean E(RenderTarget var0) {
      RenderTarget var1 = P.E();
      if (S && var1 != null && var1.width == var0.width && var1.height == var0.height) {
         wa.Y(ai.C, var1, var0);
         return true;
      } else {
         return false;
      }
   }

   public static boolean I() {
      return S;
   }

   public static boolean R(Screen var0) {
      return S && var0 instanceof bg;
   }

   public static boolean K() {
      return S;
   }

   public static boolean c() {
      return j;
   }

   public static void u(LevelRenderContext var0, Runnable var1) {
      if (!j) {
         var1.run();
      } else {
         P.u(v, () -> {
            var1.run();
            if (!F.isEmpty()) {
               P.l(F, var0.levelState().cameraRenderState, Minecraft.getInstance().gameRenderer.featureRenderDispatcher());
            }
         });
      }
   }

   public static void n() {
      if (j) {
         P.P(v);
      }
   }

   public static boolean p() {
      return j;
   }

   public static RenderTarget R(RenderTarget var0) {
      return j ? P.d(v) : var0;
   }

   public static RenderTarget Q(RenderTarget var0) {
      return j ? P.R(v) : var0;
   }

   public static void f() {
      if (j) {
         P.A(v, J());
      }
   }

   private static boolean J() {
      if (CustomSkyModule.i() && !CustomSkyModule.K().q()) {
         return true;
      }

      SkyShaderModule var0 = SkyShaderModule.t();
      return var0 != null && var0.Q();
   }

   public static boolean X() {
      return j;
   }

   public static void V() {
      P.X(v);
      com.larpclient.o.O(true);
   }

   public static void O() {
      com.larpclient.o.O(false);
      P.B(v);
      P.i(true);
   }

   public static void m(Runnable var0) {
      P.N(v, var0);
   }

   public static void F() {
      P.i(false);
   }

   public static RenderTarget d(RenderTarget var0) {
      return P.K(var0);
   }

   public static void U(Runnable var0) {
      if (j) {
         P.n(v, var0);
      } else {
         var0.run();
      }
   }

   public static void i() {
      if (j) {
         P.E(v);
      }
   }

   public static void A(Runnable var0) {
      if (j) {
         P.E(var0);
      } else {
         var0.run();
      }
   }

   public static List<GizmoInstance> L(List<GizmoInstance> var0) {
      if (!j) {
         return var0;
      }

      F = var0.isEmpty() ? List.of() : List.copyOf(var0);
      return List.of();
   }

   public static void a() {
      if (T == null) {
         P.V();
         F = List.of();
      } else {
         int var0 = j && P.w(v) ? 1 : 0;
         long var1 = m + 1L;
         int var3 = !W && (p <= 0 || var1 != 1L && var1 != 60L && var1 != 180L) ? 0 : 1;
         if (W) {
            W = (boolean)0;
            p = Math.max(p, 1);
         }

         int var4 = var3 != 0 && P.m() ? 1 : 0;
         uj var5 = T;
         pp.u var6 = var3 == 0 ? var5::r : (var3x, var5x, var6x, var7x) -> {
            c(var3x, var5x, var6x, var7x, var4, var0);
            var5.r(var3x, var5x, var6x, var7x);
         };
         boolean var7 = P.p(v, var1, (boolean)var0, P::V, var6);
         F = List.of();
         if (var7) {
            m = var1;
            if (z == Long.MAX_VALUE) {
               z = var1;
            }

            RenderTarget var8 = Minecraft.getInstance().gameRenderer.mainRenderTarget();
            T.h(true, z, var8.width, var8.height);
            if (!B && var1 - z > 240L && T.g() < z) {
               B = (boolean)1;
               h.g(
                  "StreamProof",
                  "Overlay has not presented a frame after {} submitted frames (presented={}, size {}x{}); read-back delivery or UpdateLayeredWindow is failing",
                  var1 - z,
                  T.g(),
                  var8.width,
                  var8.height
               );
            }
         }
      }
   }

   public static void o() {
      p(null);
   }

   public static void p(String var0) {
      W = (boolean)1;
      x = var0;
   }

   private static void c(long var0, int var2, int var3, ByteBuffer var4, boolean var5, boolean var6) {
      if (p > 0) {
         p--;
         long var7 = 0L;
         long var9 = 0L;
         long var11 = 0L;
         long var13 = 0L;
         NativeImage var15 = new NativeImage(var2, var3, false);

         try {
            int var16 = var4.position();

            for (int var32 = 0; var32 < var3; var32++) {
               for (int var18 = 0; var18 < var2; var18++) {
                  int var19 = var16 + (var32 * var2 + var18) * 4;
                  int var20 = var4.get(var19) & 255;
                  int var21 = var4.get(var19 + 1) & 255;
                  int var22 = var4.get(var19 + 2) & 255;
                  int var23 = var4.get(var19 + 3) & 255;
                  if (var23 == 0) {
                     var7++;
                  } else if (var23 == 255) {
                     var9++;
                     if (var22 == 0 && var21 == 0 && var20 == 0) {
                        var13++;
                     }
                  } else {
                     var11++;
                  }

                  int var24 = var23 == 0 ? 0 : Math.min(255, var22 * 255 / var23);
                  int var25 = var23 == 0 ? 0 : Math.min(255, var21 * 255 / var23);
                  int var26 = var23 == 0 ? 0 : Math.min(255, var20 * 255 / var23);
                  var15.setPixelABGR(var18, var32, var23 << 24 | var26 << 16 | var25 << 8 | var24);
               }
            }

            Path var33 = Minecraft.getInstance().gameDirectory.toPath().resolve("screenshots");
            Files.createDirectories(var33);
            String var34 = x != null ? x : String.valueOf(var0);
            x = null;
            Path var35 = var33.resolve("streamproof-overlay-" + var34 + ".png");
            var15.writeToFile(var35);
            long var36 = (long)var2 * var3;
            h.A(
               "StreamProof",
               "Dumped overlay frame {} ({}x{}) to {}; transparent={}%, partial={}%, opaque={}% (opaque black={}%); interface content={}, effects layer={}",
               var0,
               var2,
               var3,
               var35,
               100L * var7 / var36,
               100L * var11 / var36,
               100L * var9 / var36,
               100L * var13 / var36,
               var5,
               var6
            );
         } catch (Exception var30) {
            h.m("StreamProof", "Overlay frame dump failed", var30 instanceof RuntimeException var17 ? var17 : new RuntimeException(var30));
         } finally {
            var15.close();
         }
      }
   }
}
