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

public final class y8 {
   private static final he V = new he();
   private static final SimpleGizmoCollector t = new SimpleGizmoCollector();
   private static ua q;
   private static String R;
   private static boolean U;
   private static boolean v;
   private static boolean j;
   private static long x;
   private static long b;
   private static long i = Long.MAX_VALUE;
   private static long o = -1L;
   private static GuiGraphicsExtractor n;
   private static List<GizmoInstance> f = List.of();
   private static int h;
   private static boolean w;
   private static String I;

   private y8() {
   }

   public static void P() {
      x++;
      Minecraft var0 = Minecraft.getInstance();
      StreamProofModule var1 = StreamProofModule.I();
      int var2 = var1 != null && var1.isOperational() ? 1 : 0;
      if (var2 == 0) {
         R = null;
      }

      int var3 = var2 != 0 && var0.level != null && R == null && var0.getWindow() != null ? 1 : 0;
      if (var3 == 0) {
         if (q != null) {
            n(var2 != 0 ? "left the world" : "module turned off");
         }

         v = (boolean)0;
         U = (boolean)0;
         T(false);
      } else {
         if (q == null) {
            M(var0);
         }

         if (q != null && (q.D() != null || V.g())) {
            m(q.D() != null ? "overlay presentation failed (" + q.D() + ")" : "layer rendering failed");
         }

         U = (boolean)(q != null ? 1 : 0);
         v = (boolean)(U && var1.F() ? 1 : 0);
         T(v && var1.r());
      }
   }

   private static void T(boolean var0) {
      if (var0 != u8.B()) {
         u8.y(var0);
         if (!var0) {
            r4.V("StreamProof", "Scene-altering visuals resume");
         } else {
            ArrayList var1 = new ArrayList();

            for (Module var3 : LarpClient.y().e().X()) {
               if (var3.isEnabled() && StreamProofModule.y.contains(var3.getClass())) {
                  var3.suspendForDisconnect();
                  var1.add(var3.getName());
               }
            }

            r4.V("StreamProof", "Hide Everything paused scene-altering visuals for the stream: {}", var1);
         }
      }
   }

   private static void M(Minecraft var0) {
      if (!Platform.isWindows()) {
         R = "Stream Proof is only available on Windows";
         r4.m("StreamProof", R);
         r("Stream Proof unavailable", R + ".");
      } else {
         try {
            q = ua.F(var0.getWindow().handle());
            i = Long.MAX_VALUE;
            j = (boolean)0;
            h = Boolean.getBoolean("larp.streamProof.dump") ? 3 : 0;
            r4.V("StreamProof", "Enabled; backend={}, Windows build={}, display capture exclusion={}", RenderSystem.getBackendDescription(), ua.y(), q.d());
            if (!q.d()) {
               r("Stream Proof limited", "Game and window capture are hidden; this Windows build cannot hide display capture.");
            }
         } catch (RuntimeException | UnsatisfiedLinkError var3) {
            R = "overlay window could not be created";
            r4.r(
               "StreamProof",
               "Stream Proof could not create its overlay window; the client is drawn normally",
               var3 instanceof RuntimeException var2 ? var2 : new RuntimeException(var3)
            );
            r("Stream Proof unavailable", "The capture-hidden overlay could not start; the client is drawn normally.");
         }
      }
   }

   private static void m(String var0) {
      R = var0;
      n(var0);
      r("Stream Proof stopped", "The client is drawn normally again. Reason: " + var0 + ".");
   }

   private static void n(String var0) {
      if (q != null) {
         q.y();
         q = null;
      }

      V.u();
      f = List.of();
      i = Long.MAX_VALUE;
      r4.V("StreamProof", "Disabled ({})", var0);
   }

   public static void U() {
      u8.y(false);
      u8.D(false);
      if (q != null) {
         n("client shutdown");
      } else {
         V.u();
      }

      R = null;
   }

   public static void T() {
      V.u();
      f = List.of();
   }

   public static boolean k() {
      return q != null;
   }

   public static String J(StreamProofModule var0) {
      if (q == null) {
         return R != null ? "Unavailable: " + R : "";
      }

      String var1 = q.d() ? "game, window, display" : "game, window";
      return (var0.F() ? "Hiding all (" : "Keeping visuals (") + var1 + ")";
   }

   private static void r(String var0, String var1) {
      try {
         hg.B().J(var0, var1, hg.u.Z, 6000L);
      } catch (RuntimeException var3) {
      }
   }

   public static GuiGraphicsExtractor U(GuiGraphicsExtractor var0, boolean var1, boolean var2) {
      boolean var3 = var1 ? v : U;
      return !var3 ? var0 : G();
   }

   public static GuiGraphicsExtractor P() {
      return G();
   }

   public static boolean x(Object var0) {
      return V.y(var0) && V.H();
   }

   public static RenderTarget U(Object var0) {
      return V.y(var0) ? V.j() : null;
   }

   private static GuiGraphicsExtractor G() {
      if (o != x) {
         Minecraft var0 = Minecraft.getInstance();
         int var1 = (int)Math.round(var0.mouseHandler.getScaledXPos(var0.getWindow()));
         int var2 = (int)Math.round(var0.mouseHandler.getScaledYPos(var0.getWindow()));
         n = new GuiGraphicsExtractor(var0, V.D(), var1, var2);
         o = x;
      }

      return n;
   }

   public static GuiRenderState p(GuiRenderState var0) {
      return U ? V.D() : var0;
   }

   public static boolean v(RenderTarget var0) {
      RenderTarget var1 = V.j();
      if (U && var1 != null && var1.width == var0.width && var1.height == var0.height) {
         jk.y(e4.g, var1, var0);
         return true;
      } else {
         return false;
      }
   }

   public static boolean u() {
      return U;
   }

   public static boolean S(Screen var0) {
      return U && var0 instanceof h7;
   }

   public static boolean e() {
      return U;
   }

   public static boolean Y() {
      return v;
   }

   public static void e(LevelRenderContext var0, Runnable var1) {
      if (!v) {
         var1.run();
      } else {
         V.P(x, () -> {
            var1.run();
            if (!f.isEmpty()) {
               V.D(f, var0.levelState().cameraRenderState, Minecraft.getInstance().gameRenderer.featureRenderDispatcher());
            }
         });
      }
   }

   public static void f() {
      if (v) {
         V.k(x);
      }
   }

   public static boolean J() {
      return v;
   }

   public static RenderTarget r(RenderTarget var0) {
      return v ? V.n(x) : var0;
   }

   public static RenderTarget m(RenderTarget var0) {
      return v ? V.M(x) : var0;
   }

   public static void e() {
      if (v) {
         V.J(x, S());
      }
   }

   private static boolean S() {
      if (CustomSkyModule.i() && !CustomSkyModule.Y().d()) {
         return true;
      }

      SkyShaderModule var0 = SkyShaderModule.B();
      return yv.K() || var0 != null && var0.Y();
   }

   public static boolean Q() {
      return v;
   }

   public static void A() {
      V.t(x);
      u8.D(true);
   }

   public static void k() {
      u8.D(false);
      V.Z(x);
      V.Q(true);
   }

   public static void y(Runnable var0) {
      V.Z(x, var0);
   }

   public static void r() {
      V.Q(false);
   }

   public static RenderTarget s(RenderTarget var0) {
      return V.V(var0);
   }

   public static void v(Runnable var0) {
      if (v) {
         V.z(x, var0);
      } else {
         var0.run();
      }
   }

   public static void Y() {
      if (v) {
         V.m(x);
      }
   }

   public static void d(Runnable var0) {
      if (v) {
         V.N(var0);
      } else {
         var0.run();
      }
   }

   public static List<GizmoInstance> c(List<GizmoInstance> var0) {
      if (!v) {
         return var0;
      }

      f = var0.isEmpty() ? List.of() : List.copyOf(var0);
      return List.of();
   }

   public static void p() {
      if (q == null) {
         V.l();
         f = List.of();
      } else {
         int var0 = v && V.M(x) ? 1 : 0;
         long var1 = b + 1L;
         int var3 = !w && (h <= 0 || var1 != 1L && var1 != 60L && var1 != 180L) ? 0 : 1;
         if (w) {
            w = (boolean)0;
            h = Math.max(h, 1);
         }

         int var4 = var3 != 0 && V.b() ? 1 : 0;
         ua var5 = q;
         he.l var6 = var3 == 0 ? var5::Y : (var3x, var5x, var6x, var7x) -> {
            T(var3x, var5x, var6x, var7x, var4, var0);
            var5.Y(var3x, var5x, var6x, var7x);
         };
         boolean var7 = V.y(x, var1, (boolean)var0, V::R, var6);
         f = List.of();
         if (var7) {
            b = var1;
            if (i == Long.MAX_VALUE) {
               i = var1;
            }

            RenderTarget var8 = Minecraft.getInstance().gameRenderer.mainRenderTarget();
            q.w(true, i, var8.width, var8.height);
            if (!j && var1 - i > 240L && q.v() < i) {
               j = (boolean)1;
               r4.m(
                  "StreamProof",
                  "Overlay has not presented a frame after {} submitted frames (presented={}, size {}x{}); read-back delivery or UpdateLayeredWindow is failing",
                  var1 - i,
                  q.v(),
                  var8.width,
                  var8.height
               );
            }
         }
      }
   }

   public static void J() {
      s((String)null);
   }

   public static void s(String var0) {
      w = (boolean)1;
      I = var0;
   }

   private static void T(long var0, int var2, int var3, ByteBuffer var4, boolean var5, boolean var6) {
      if (h > 0) {
         h--;
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
            String var34 = I != null ? I : String.valueOf(var0);
            I = null;
            Path var35 = var33.resolve("streamproof-overlay-" + var34 + ".png");
            var15.writeToFile(var35);
            long var36 = (long)var2 * var3;
            r4.V(
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
            r4.r("StreamProof", "Overlay frame dump failed", var30 instanceof RuntimeException var17 ? var17 : new RuntimeException(var30));
         } finally {
            var15.close();
         }
      }
   }
}
