package com.larpclient;

import com.larpclient.module.CustomSkyModule;
import com.larpclient.module.Module;
import com.larpclient.module.SkyShaderModule;
import com.larpclient.module.StreamProofModule;
import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.blaze3d.systems.RenderSystem;
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
import org.lwjgl.sdl.SDLVideo;

public final class qc {
   private static final is M = new is();
   private static final SimpleGizmoCollector z = new SimpleGizmoCollector();
   private static pd n;
   private static String x;
   private static boolean F;
   private static boolean t;
   private static boolean f;
   private static long D;
   private static long u;
   private static long r = Long.MAX_VALUE;
   private static long d = -1L;
   private static boolean g;
   private static long H;
   private static final long P = 400L;
   private static GuiGraphicsExtractor I;
   private static List<GizmoInstance> R = List.of();
   private static int i;
   private static boolean k;
   private static String C;

   private qc() {
   }

   public static void c() {
      D++;
      Minecraft var0 = Minecraft.getInstance();
      StreamProofModule var1 = StreamProofModule.r();
      int var2 = var1 != null && var1.isOperational() ? 1 : 0;
      if (var2 == 0) {
         x = null;
      }

      int var3 = var2 != 0 && var0.level != null && x == null && var0.getWindow() != null ? 1 : 0;
      if (var3 == 0) {
         if (n != null) {
            K(var2 != 0 ? "left the world" : "module turned off");
         }

         t = (boolean)0;
         F = (boolean)0;
         Y(false);
      } else {
         if (n == null) {
            N(var0);
         } else {
            K(var0);
         }

         if (n != null && (n.h() != null || M.y())) {
            L(n.h() != null ? "overlay presentation failed (" + n.h() + ")" : "layer rendering failed");
         }

         F = (boolean)(n != null ? 1 : 0);
         t = (boolean)(F && var1.b() ? 1 : 0);
         Y(t && var1.B());
      }
   }

   private static void Y(boolean var0) {
      if (var0 != jj.n()) {
         jj.x(var0);
         if (!var0) {
            _q.z("StreamProof", "Scene-altering visuals resume");
         } else {
            ArrayList var1 = new ArrayList();

            for (Module var3 : LarpClient.N().j().d()) {
               if (var3.isEnabled() && StreamProofModule.I.contains(var3.getClass())) {
                  var3.suspendForDisconnect();
                  var1.add(var3.getName());
               }
            }

            _q.z("StreamProof", "Hide Everything paused scene-altering visuals for the stream: {}", var1);
         }
      }
   }

   private static void N(Minecraft var0) {
      if (!Platform.isWindows()) {
         x = "Stream Proof is only available on Windows";
         _q.R("StreamProof", x);
         l("Stream Proof unavailable", x + ".");
      } else {
         try {
            n = pd.i(var0.getWindow().handle());
            g = z(var0);
            H = 0L;
            r = Long.MAX_VALUE;
            f = (boolean)0;
            i = Boolean.getBoolean("larp.streamProof.dump") ? 3 : 0;
            _q.z("StreamProof", "Enabled; backend={}, Windows build={}, display capture exclusion={}", RenderSystem.getBackendDescription(), pd.v(), n.u());
            if (!n.u()) {
               l("Stream Proof limited", "Game and window capture are hidden; this Windows build cannot hide display capture.");
            }
         } catch (RuntimeException | UnsatisfiedLinkError var3) {
            x = "overlay window could not be created";
            _q.o(
               "StreamProof",
               "Stream Proof could not create its overlay window; the client is drawn normally",
               var3 instanceof RuntimeException var2 ? var2 : new RuntimeException(var3)
            );
            l("Stream Proof unavailable", "The capture-hidden overlay could not start; the client is drawn normally.");
         }
      }
   }

   private static boolean z(Minecraft var0) {
      return (SDLVideo.SDL_GetWindowFlags(var0.getWindow().handle()) & 1L) != 0L;
   }

   private static void K(Minecraft var0) {
      boolean var1 = z(var0);
      long var2 = Util.getMillis();
      if (var1 != g) {
         g = var1;
         H = var2;
         _q.z("StreamProof", "Game window {} fullscreen; recreating the overlay once it settles", var1 ? "entered" : "left");
      } else if (H != 0L && var2 - H >= 400L) {
         H = 0L;
         n.j();
         n = null;
         N(var0);
         if (n != null) {
            _q.z("StreamProof", "Overlay recreated after the fullscreen change (fullscreen={})", var1);
         }
      }
   }

   private static void L(String var0) {
      x = var0;
      K(var0);
      l("Stream Proof stopped", "The client is drawn normally again. Reason: " + var0 + ".");
   }

   private static void K(String var0) {
      if (n != null) {
         n.j();
         n = null;
      }

      M.U();
      R = List.of();
      r = Long.MAX_VALUE;
      _q.z("StreamProof", "Disabled ({})", var0);
   }

   public static void E() {
      jj.x(false);
      jj.E(false);
      if (n != null) {
         K("client shutdown");
      } else {
         M.U();
      }

      x = null;
   }

   public static void m() {
      M.U();
      R = List.of();
   }

   public static boolean C() {
      return n != null;
   }

   public static String G(StreamProofModule var0) {
      if (n == null) {
         return x != null ? "Unavailable: " + x : "";
      }

      String var1 = n.u() ? "game, window, display" : "game, window";
      return (var0.b() ? "Hiding all (" : "Keeping visuals (") + var1 + ")";
   }

   private static void l(String var0, String var1) {
      try {
         ib.L().l(var0, var1, ib.v.o, 6000L);
      } catch (RuntimeException var3) {
      }
   }

   public static GuiGraphicsExtractor h(GuiGraphicsExtractor var0, boolean var1, boolean var2) {
      boolean var3 = var1 ? t : F;
      return !var3 ? var0 : W();
   }

   public static GuiGraphicsExtractor R() {
      return W();
   }

   public static boolean K(Object var0) {
      return M.l(var0) && M.P();
   }

   public static RenderTarget v(Object var0) {
      return M.l(var0) ? M.N() : null;
   }

   private static GuiGraphicsExtractor W() {
      if (d != D) {
         Minecraft var0 = Minecraft.getInstance();
         int var1 = (int)Math.round(var0.mouseHandler.getScaledXPos(var0.getWindow()));
         int var2 = (int)Math.round(var0.mouseHandler.getScaledYPos(var0.getWindow()));
         I = new GuiGraphicsExtractor(var0, M.Z(), var1, var2);
         d = D;
      }

      return I;
   }

   public static GuiRenderState p(GuiRenderState var0) {
      return F ? M.Z() : var0;
   }

   public static boolean A(RenderTarget var0) {
      RenderTarget var1 = M.N();
      if (F && var1 != null && var1.width == var0.width && var1.height == var0.height) {
         nl.G(se.j, var1, var0);
         return true;
      } else {
         return false;
      }
   }

   public static boolean i() {
      return F;
   }

   public static boolean b(Screen var0) {
      return F && var0 instanceof n1;
   }

   public static boolean R() {
      return F;
   }

   public static boolean w() {
      return t;
   }

   public static void m(LevelRenderContext var0, Runnable var1) {
      if (!t) {
         var1.run();
      } else {
         M.n(D, () -> {
            var1.run();
            if (!R.isEmpty()) {
               M.n(R, var0.levelState().cameraRenderState, Minecraft.getInstance().gameRenderer.featureRenderDispatcher());
            }
         });
      }
   }

   public static void F() {
      if (t) {
         M.t(D);
      }
   }

   public static boolean t() {
      return t;
   }

   public static RenderTarget c(RenderTarget var0) {
      return t ? M.x(D) : var0;
   }

   public static RenderTarget Y(RenderTarget var0) {
      return t ? M.m(D) : var0;
   }

   public static void G() {
      if (t) {
         M.I(D, D());
      }
   }

   private static boolean D() {
      if (CustomSkyModule.C() && !CustomSkyModule.p().r()) {
         return true;
      }

      SkyShaderModule var0 = SkyShaderModule.R();
      return var0 != null && var0.A();
   }

   public static boolean G() {
      return t;
   }

   public static void l() {
      M.Q(D);
      jj.E(true);
   }

   public static void R() {
      jj.E(false);
      M.m(D);
      M.W(true);
   }

   public static void o(Runnable var0) {
      M.u(D, var0);
   }

   public static void N() {
      M.W(false);
   }

   public static RenderTarget A(RenderTarget var0) {
      return M.q(var0);
   }

   public static void C(Runnable var0) {
      if (t) {
         M.U(D, var0);
      } else {
         var0.run();
      }
   }

   public static void b() {
      if (t) {
         M.r(D);
      }
   }

   public static void J(Runnable var0) {
      if (t) {
         M.p(var0);
      } else {
         var0.run();
      }
   }

   public static List<GizmoInstance> a(List<GizmoInstance> var0) {
      if (!t) {
         return var0;
      }

      R = var0.isEmpty() ? List.of() : List.copyOf(var0);
      return List.of();
   }

   public static void w() {
      if (n == null) {
         M.e();
         R = List.of();
      } else {
         int var0 = t && M.x(D) ? 1 : 0;
         long var1 = u + 1L;
         int var3 = !k && (i <= 0 || var1 != 1L && var1 != 60L && var1 != 180L) ? 0 : 1;
         if (k) {
            k = (boolean)0;
            i = Math.max(i, 1);
         }

         int var4 = var3 != 0 && M.l() ? 1 : 0;
         pd var5 = n;
         is.e var6 = var3 == 0 ? var5::d : (var3x, var5x, var6x, var7x) -> {
            J(var3x, var5x, var6x, var7x, var4, var0);
            var5.d(var3x, var5x, var6x, var7x);
         };
         boolean var7 = M.i(D, var1, (boolean)var0, M::E, var6);
         R = List.of();
         if (var7) {
            u = var1;
            if (r == Long.MAX_VALUE) {
               r = var1;
            }

            RenderTarget var8 = Minecraft.getInstance().gameRenderer.mainRenderTarget();
            n.a(true, r, var8.width, var8.height);
            if (!f && var1 - r > 240L && n.c() < r) {
               f = (boolean)1;
               _q.R(
                  "StreamProof",
                  "Overlay has not presented a frame after {} submitted frames (presented={}, size {}x{}); read-back delivery or UpdateLayeredWindow is failing",
                  var1 - r,
                  n.c(),
                  var8.width,
                  var8.height
               );
            }
         }
      }
   }

   public static void z() {
      f(null);
   }

   public static void f(String var0) {
      k = (boolean)1;
      C = var0;
   }

   private static void J(long var0, int var2, int var3, ByteBuffer var4, boolean var5, boolean var6) {
      if (i > 0) {
         i--;
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
            String var34 = C != null ? C : String.valueOf(var0);
            C = null;
            Path var35 = var33.resolve("streamproof-overlay-" + var34 + ".png");
            var15.writeToFile(var35);
            long var36 = (long)var2 * var3;
            _q.z(
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
            _q.o("StreamProof", "Overlay frame dump failed", var30 instanceof RuntimeException var17 ? var17 : new RuntimeException(var30));
         } finally {
            var15.close();
         }
      }
   }
}
