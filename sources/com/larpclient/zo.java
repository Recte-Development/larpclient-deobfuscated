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

public final class zo {
   private static final ee t = new ee();
   private static final SimpleGizmoCollector m = new SimpleGizmoCollector();
   private static wl C;
   private static String Q;
   private static boolean P;
   private static boolean H;
   private static boolean s;
   private static long f;
   private static long T;
   private static long p = Long.MAX_VALUE;
   private static long D = -1L;
   private static GuiGraphicsExtractor x;
   private static List<GizmoInstance> M = List.of();
   private static int y;
   private static boolean l;
   private static String n;

   private zo() {
   }

   public static void A() {
      f++;
      Minecraft var0 = Minecraft.getInstance();
      StreamProofModule var1 = StreamProofModule.s();
      int var2 = var1 != null && var1.isOperational() ? 1 : 0;
      if (var2 == 0) {
         Q = null;
      }

      int var3 = var2 != 0 && var0.level != null && Q == null && var0.getWindow() != null ? 1 : 0;
      if (var3 == 0) {
         if (C != null) {
            N(var2 != 0 ? "left the world" : "module turned off");
         }

         H = (boolean)0;
         P = (boolean)0;
         d(false);
      } else {
         if (C == null) {
            g(var0);
         }

         if (C != null && (C.m() != null || t.E())) {
            r(C.m() != null ? "overlay presentation failed (" + C.m() + ")" : "layer rendering failed");
         }

         P = (boolean)(C != null ? 1 : 0);
         H = (boolean)(P && var1.H() ? 1 : 0);
         d(H && var1.Y());
      }
   }

   private static void d(boolean var0) {
      if (var0 != cf.t()) {
         cf.B(var0);
         if (!var0) {
            mt.K("StreamProof", "Scene-altering visuals resume");
         } else {
            ArrayList var1 = new ArrayList();

            for (Module var3 : LarpClient.F().x().C()) {
               if (var3.isEnabled() && StreamProofModule.d.contains(var3.getClass())) {
                  var3.suspendForDisconnect();
                  var1.add(var3.getName());
               }
            }

            mt.K("StreamProof", "Hide Everything paused scene-altering visuals for the stream: {}", var1);
         }
      }
   }

   private static void g(Minecraft var0) {
      if (!Platform.isWindows()) {
         Q = "Stream Proof is only available on Windows";
         mt.T("StreamProof", Q);
         t("Stream Proof unavailable", Q + ".");
      } else {
         try {
            C = wl.J(var0.getWindow().handle());
            p = Long.MAX_VALUE;
            s = (boolean)0;
            y = Boolean.getBoolean("larp.streamProof.dump") ? 3 : 0;
            mt.K("StreamProof", "Enabled; backend={}, Windows build={}, display capture exclusion={}", RenderSystem.getBackendDescription(), wl.m(), C.Q());
            if (!C.Q()) {
               t("Stream Proof limited", "Game and window capture are hidden; this Windows build cannot hide display capture.");
            }
         } catch (RuntimeException | UnsatisfiedLinkError var3) {
            Q = "overlay window could not be created";
            mt.P(
               "StreamProof",
               "Stream Proof could not create its overlay window; the client is drawn normally",
               var3 instanceof RuntimeException var2 ? var2 : new RuntimeException(var3)
            );
            t("Stream Proof unavailable", "The capture-hidden overlay could not start; the client is drawn normally.");
         }
      }
   }

   private static void r(String var0) {
      Q = var0;
      N(var0);
      t("Stream Proof stopped", "The client is drawn normally again. Reason: " + var0 + ".");
   }

   private static void N(String var0) {
      if (C != null) {
         C.T();
         C = null;
      }

      t.K();
      M = List.of();
      p = Long.MAX_VALUE;
      mt.K("StreamProof", "Disabled ({})", var0);
   }

   public static void e() {
      cf.B(false);
      cf.l(false);
      if (C != null) {
         N("client shutdown");
      } else {
         t.K();
      }

      Q = null;
   }

   public static void w() {
      t.K();
      M = List.of();
   }

   public static boolean k() {
      return C != null;
   }

   public static String J(StreamProofModule var0) {
      if (C == null) {
         return Q != null ? "Unavailable: " + Q : "";
      }

      String var1 = C.Q() ? "game, window, display" : "game, window";
      return (var0.H() ? "Hiding all (" : "Keeping visuals (") + var1 + ")";
   }

   private static void t(String var0, String var1) {
      try {
         oj.K().v(var0, var1, oj.u.B, 6000L);
      } catch (RuntimeException var3) {
      }
   }

   public static GuiGraphicsExtractor R(GuiGraphicsExtractor var0, boolean var1, boolean var2) {
      boolean var3 = var1 ? H : P;
      return !var3 ? var0 : R();
   }

   public static GuiGraphicsExtractor K() {
      return R();
   }

   public static boolean Q(Object var0) {
      return t.d(var0) && t.q();
   }

   public static RenderTarget k(Object var0) {
      return t.d(var0) ? t.u() : null;
   }

   private static GuiGraphicsExtractor R() {
      if (D != f) {
         Minecraft var0 = Minecraft.getInstance();
         int var1 = (int)Math.round(var0.mouseHandler.getScaledXPos(var0.getWindow()));
         int var2 = (int)Math.round(var0.mouseHandler.getScaledYPos(var0.getWindow()));
         x = new GuiGraphicsExtractor(var0, t.N(), var1, var2);
         D = f;
      }

      return x;
   }

   public static GuiRenderState t(GuiRenderState var0) {
      return P ? t.N() : var0;
   }

   public static boolean O(RenderTarget var0) {
      RenderTarget var1 = t.u();
      if (P && var1 != null && var1.width == var0.width && var1.height == var0.height) {
         m5.l(ea.m, var1, var0);
         return true;
      } else {
         return false;
      }
   }

   public static boolean v() {
      return P;
   }

   public static boolean m(Screen var0) {
      return P && var0 instanceof o3;
   }

   public static boolean Q() {
      return P;
   }

   public static boolean W() {
      return H;
   }

   public static void v(LevelRenderContext var0, Runnable var1) {
      if (!H) {
         var1.run();
      } else {
         t.W(f, () -> {
            var1.run();
            if (!M.isEmpty()) {
               t.U(M, var0.levelState().cameraRenderState, Minecraft.getInstance().gameRenderer.featureRenderDispatcher());
            }
         });
      }
   }

   public static void z() {
      if (H) {
         t.z(f);
      }
   }

   public static boolean l() {
      return H;
   }

   public static RenderTarget Z(RenderTarget var0) {
      return H ? t.n(f) : var0;
   }

   public static RenderTarget t(RenderTarget var0) {
      return H ? t.P(f) : var0;
   }

   public static void k() {
      if (H) {
         t.e(f, K());
      }
   }

   private static boolean K() {
      if (CustomSkyModule.a() && !CustomSkyModule.Y().Q()) {
         return true;
      }

      SkyShaderModule var0 = SkyShaderModule.U();
      return wy.a() || var0 != null && var0.d();
   }

   public static boolean T() {
      return H;
   }

   public static void c() {
      t.p(f);
      cf.l(true);
   }

   public static void s() {
      cf.l(false);
      t.c(f);
      t.X(true);
   }

   public static void Z(Runnable var0) {
      t.e(f, var0);
   }

   public static void U() {
      t.X(false);
   }

   public static RenderTarget E(RenderTarget var0) {
      return t.K(var0);
   }

   public static void F(Runnable var0) {
      if (H) {
         t.O(f, var0);
      } else {
         var0.run();
      }
   }

   public static void P() {
      if (H) {
         t.g(f);
      }
   }

   public static void w(Runnable var0) {
      if (H) {
         t.T(var0);
      } else {
         var0.run();
      }
   }

   public static List<GizmoInstance> P(List<GizmoInstance> var0) {
      if (!H) {
         return var0;
      }

      M = var0.isEmpty() ? List.of() : List.copyOf(var0);
      return List.of();
   }

   public static void d() {
      if (C == null) {
         t.L();
         M = List.of();
      } else {
         int var0 = H && t.P(f) ? 1 : 0;
         long var1 = T + 1L;
         int var3 = !l && (y <= 0 || var1 != 1L && var1 != 60L && var1 != 180L) ? 0 : 1;
         if (l) {
            l = (boolean)0;
            y = Math.max(y, 1);
         }

         int var4 = var3 != 0 && t.p() ? 1 : 0;
         wl var5 = C;
         ee.g var6 = var3 == 0 ? var5::Q : (var3x, var5x, var6x, var7x) -> {
            L(var3x, var5x, var6x, var7x, var4, var0);
            var5.Q(var3x, var5x, var6x, var7x);
         };
         boolean var7 = t.D(f, var1, (boolean)var0, t::N, var6);
         M = List.of();
         if (var7) {
            T = var1;
            if (p == Long.MAX_VALUE) {
               p = var1;
            }

            RenderTarget var8 = Minecraft.getInstance().gameRenderer.mainRenderTarget();
            C.E(true, p, var8.width, var8.height);
            if (!s && var1 - p > 240L && C.p() < p) {
               s = (boolean)1;
               mt.T(
                  "StreamProof",
                  "Overlay has not presented a frame after {} submitted frames (presented={}, size {}x{}); read-back delivery or UpdateLayeredWindow is failing",
                  var1 - p,
                  C.p(),
                  var8.width,
                  var8.height
               );
            }
         }
      }
   }

   public static void J() {
      x(null);
   }

   public static void x(String var0) {
      l = (boolean)1;
      n = var0;
   }

   private static void L(long var0, int var2, int var3, ByteBuffer var4, boolean var5, boolean var6) {
      if (y > 0) {
         y--;
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
            String var34 = n != null ? n : String.valueOf(var0);
            n = null;
            Path var35 = var33.resolve("streamproof-overlay-" + var34 + ".png");
            var15.writeToFile(var35);
            long var36 = (long)var2 * var3;
            mt.K(
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
            mt.P("StreamProof", "Overlay frame dump failed", var30 instanceof RuntimeException var17 ? var17 : new RuntimeException(var30));
         } finally {
            var15.close();
         }
      }
   }
}
