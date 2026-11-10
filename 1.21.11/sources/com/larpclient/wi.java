package com.larpclient;

import com.larpclient.module.CustomSkyModule;
import com.larpclient.module.Module;
import com.larpclient.module.SkyShaderModule;
import com.larpclient.module.StreamProofModule;
import com.mojang.blaze3d.buffers.GpuBufferSlice;
import com.sun.jna.Platform;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import net.fabricmc.fabric.api.client.rendering.v1.world.WorldRenderContext;
import net.minecraft.class_1011;
import net.minecraft.class_11246;
import net.minecraft.class_12185;
import net.minecraft.class_156;
import net.minecraft.class_276;
import net.minecraft.class_310;
import net.minecraft.class_332;
import net.minecraft.class_437;
import net.minecraft.class_12185.class_12186;
import org.joml.Matrix4f;

public final class wi {
   private static final qz w = new qz();
   private static final class_12185 W = new class_12185();
   private static i6 I;
   private static String M;
   private static boolean B;
   private static boolean r;
   private static boolean b;
   private static long D;
   private static long V;
   private static long Y = Long.MAX_VALUE;
   private static long H = -1L;
   private static boolean P;
   private static long q;
   private static final long y = 400L;
   private static class_332 F;
   private static List<class_12186> z = List.of();
   private static int U;
   private static boolean X;
   private static String R;

   private wi() {
   }

   public static void d() {
      D++;
      class_310 var0 = class_310.method_1551();
      StreamProofModule var1 = StreamProofModule.e();
      int var2 = var1 != null && var1.isOperational() ? 1 : 0;
      if (var2 == 0) {
         M = null;
      }

      int var3 = var2 != 0 && var0.field_1687 != null && M == null && var0.method_22683() != null ? 1 : 0;
      if (var3 == 0) {
         if (I != null) {
            W(var2 != 0 ? "left the world" : "module turned off");
         }

         r = (boolean)0;
         B = (boolean)0;
         F(false);
      } else {
         if (I == null) {
            S(var0);
         } else {
            y(var0);
         }

         if (I != null && (I.y() != null || w.N())) {
            H(I.y() != null ? "overlay presentation failed (" + I.y() + ")" : "layer rendering failed");
         }

         B = (boolean)(I != null ? 1 : 0);
         r = (boolean)(B && var1.T() ? 1 : 0);
         F(r && var1.K());
      }
   }

   private static void F(boolean var0) {
      if (var0 != qj.r()) {
         qj.u(var0);
         if (!var0) {
            q8.G("StreamProof", "Scene-altering visuals resume");
         } else {
            ArrayList var1 = new ArrayList();

            for (Module var3 : LarpClient.b().G().M()) {
               if (var3.isEnabled() && StreamProofModule.w.contains(var3.getClass())) {
                  var3.suspendForDisconnect();
                  var1.add(var3.getName());
               }
            }

            q8.G("StreamProof", "Hide Everything paused scene-altering visuals for the stream: {}", var1);
         }
      }
   }

   private static void S(class_310 var0) {
      if (!Platform.isWindows()) {
         M = "Stream Proof is only available on Windows";
         q8.L("StreamProof", M);
         p("Stream Proof unavailable", M + ".");
      } else {
         try {
            I = i6.K(var0.method_22683().method_4490());
            P = R(var0);
            q = 0L;
            Y = Long.MAX_VALUE;
            b = (boolean)0;
            U = Boolean.getBoolean("larp.streamProof.dump") ? 3 : 0;
            q8.G("StreamProof", "Enabled; backend=OpenGL, Windows build={}, display capture exclusion={}", i6.k(), I.z());
            if (!I.z()) {
               p("Stream Proof limited", "Game and window capture are hidden; this Windows build cannot hide display capture.");
            }
         } catch (RuntimeException | UnsatisfiedLinkError var3) {
            M = "overlay window could not be created";
            q8.u(
               "StreamProof",
               "Stream Proof could not create its overlay window; the client is drawn normally",
               var3 instanceof RuntimeException var2 ? var2 : new RuntimeException(var3)
            );
            p("Stream Proof unavailable", "The capture-hidden overlay could not start; the client is drawn normally.");
         }
      }
   }

   private static boolean R(class_310 var0) {
      return var0.method_22683().method_4498();
   }

   private static void y(class_310 var0) {
      boolean var1 = R(var0);
      long var2 = class_156.method_658();
      if (var1 != P) {
         P = var1;
         q = var2;
         q8.G("StreamProof", "Game window {} fullscreen; recreating the overlay once it settles", var1 ? "entered" : "left");
      } else if (q != 0L && var2 - q >= 400L) {
         q = 0L;
         I.X();
         I = null;
         S(var0);
         if (I != null) {
            q8.G("StreamProof", "Overlay recreated after the fullscreen change (fullscreen={})", var1);
         }
      }
   }

   private static void H(String var0) {
      M = var0;
      W(var0);
      p("Stream Proof stopped", "The client is drawn normally again. Reason: " + var0 + ".");
   }

   private static void W(String var0) {
      if (I != null) {
         I.X();
         I = null;
      }

      w.z();
      z = List.of();
      Y = Long.MAX_VALUE;
      q8.G("StreamProof", "Disabled ({})", var0);
   }

   public static void H() {
      qj.u(false);
      qj.B(false);
      if (I != null) {
         W("client shutdown");
      } else {
         w.z();
      }

      M = null;
   }

   public static void R() {
      w.z();
      z = List.of();
   }

   public static boolean V() {
      return I != null;
   }

   public static String r(StreamProofModule var0) {
      if (I == null) {
         return M != null ? "Unavailable: " + M : "";
      }

      String var1 = I.z() ? "game, window, display" : "game, window";
      return (var0.T() ? "Hiding all (" : "Keeping visuals (") + var1 + ")";
   }

   private static void p(String var0, String var1) {
      try {
         wt.T().z(var0, var1, wt.s.a, 6000L);
      } catch (RuntimeException var3) {
      }
   }

   public static class_332 p(class_332 var0, boolean var1, boolean var2) {
      boolean var3 = var1 ? r : B;
      return !var3 ? var0 : W();
   }

   public static class_332 E() {
      return W();
   }

   public static boolean x(Object var0) {
      return w.T(var0) && w.J();
   }

   public static class_276 V(Object var0) {
      return w.T(var0) ? w.q() : null;
   }

   public static void Y(Object var0, GpuBufferSlice var1) {
      if (!w.T(var0)) {
         w.d(var1);
      }
   }

   private static class_332 W() {
      if (H != D) {
         class_310 var0 = class_310.method_1551();
         int var1 = (int)Math.round(var0.field_1729.method_68879(var0.method_22683()));
         int var2 = (int)Math.round(var0.field_1729.method_68883(var0.method_22683()));
         F = new class_332(var0, w.W(), var1, var2);
         H = D;
      }

      return F;
   }

   public static class_11246 p(class_11246 var0) {
      return B ? w.W() : var0;
   }

   public static boolean g(class_276 var0) {
      class_276 var1 = w.q();
      if (B && var1 != null && var1.field_1482 == var0.field_1482 && var1.field_1481 == var0.field_1481) {
         t_.L(f2.f, var1, var0);
         return true;
      } else {
         return false;
      }
   }

   public static boolean W() {
      return B;
   }

   public static boolean Q(class_437 var0) {
      return B && var0 instanceof tp;
   }

   public static boolean t() {
      return B;
   }

   public static boolean E() {
      return r;
   }

   public static void o(WorldRenderContext var0, Runnable var1) {
      if (!r) {
         var1.run();
      } else {
         w.B(D, () -> {
            var1.run();
            if (!z.isEmpty()) {
               w.S(z, var0.worldState().field_63082, var0.matrices(), new Matrix4f(b9.T()));
            }
         });
      }
   }

   public static void O() {
      if (r) {
         w.d(D);
      }
   }

   public static boolean s() {
      return r;
   }

   public static class_276 A(class_276 var0) {
      return r ? w.v(D) : var0;
   }

   public static class_276 p(class_276 var0) {
      return r ? w.k(D) : var0;
   }

   public static void h() {
      if (r) {
         w.H(D, k());
      }
   }

   private static boolean k() {
      if (CustomSkyModule.h() && !CustomSkyModule.M().w()) {
         return true;
      }

      SkyShaderModule var0 = SkyShaderModule.y();
      return var0 != null && var0.O();
   }

   public static boolean p() {
      return r;
   }

   public static void i() {
      w.g(D);
      qj.B(true);
   }

   public static void X() {
      qj.B(false);
      w.P(D);
      w.b(true);
   }

   public static void k(Runnable var0) {
      w.m(D, var0);
   }

   public static void V() {
      w.b(false);
   }

   public static class_276 m(class_276 var0) {
      return w.I(var0);
   }

   public static void i(Runnable var0) {
      if (r) {
         w.h(D, var0);
      } else {
         var0.run();
      }
   }

   public static void I() {
      if (r) {
         w.M(D);
      }
   }

   public static void D(Runnable var0) {
      if (r) {
         w.Y(var0);
      } else {
         var0.run();
      }
   }

   public static List<class_12186> h(List<class_12186> var0) {
      if (!r) {
         return var0;
      }

      z = var0.isEmpty() ? List.of() : List.copyOf(var0);
      return List.of();
   }

   public static void E() {
      if (I == null) {
         w.O();
         z = List.of();
      } else {
         int var0 = r && w.m(D) ? 1 : 0;
         long var1 = V + 1L;
         int var3 = !X && (U <= 0 || var1 != 1L && var1 != 60L && var1 != 180L) ? 0 : 1;
         if (X) {
            X = (boolean)0;
            U = Math.max(U, 1);
         }

         int var4 = var3 != 0 && w.G() ? 1 : 0;
         i6 var5 = I;
         qz.j var6 = var3 == 0 ? var5::L : (var3x, var5x, var6x, var7x) -> {
            y(var3x, var5x, var6x, var7x, var4, var0);
            var5.L(var3x, var5x, var6x, var7x);
         };
         boolean var7 = w.o(D, var1, (boolean)var0, w::n, var6);
         z = List.of();
         if (var7) {
            V = var1;
            if (Y == Long.MAX_VALUE) {
               Y = var1;
            }

            class_276 var8 = class_310.method_1551().method_1522();
            I.V(true, Y, var8.field_1482, var8.field_1481);
            if (!b && var1 - Y > 240L && I.N() < Y) {
               b = (boolean)1;
               q8.L(
                  "StreamProof",
                  "Overlay has not presented a frame after {} submitted frames (presented={}, size {}x{}); read-back delivery or UpdateLayeredWindow is failing",
                  var1 - Y,
                  I.N(),
                  var8.field_1482,
                  var8.field_1481
               );
            }
         }
      }
   }

   public static void o() {
      l(null);
   }

   public static void l(String var0) {
      X = (boolean)1;
      R = var0;
   }

   private static void y(long var0, int var2, int var3, ByteBuffer var4, boolean var5, boolean var6) {
      if (U > 0) {
         U--;
         long var7 = 0L;
         long var9 = 0L;
         long var11 = 0L;
         long var13 = 0L;
         class_1011 var15 = new class_1011(var2, var3, false);

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
                  var15.method_4305(var18, var32, var23 << 24 | var26 << 16 | var25 << 8 | var24);
               }
            }

            Path var33 = class_310.method_1551().field_1697.toPath().resolve("screenshots");
            Files.createDirectories(var33);
            String var34 = R != null ? R : String.valueOf(var0);
            R = null;
            Path var35 = var33.resolve("streamproof-overlay-" + var34 + ".png");
            var15.method_4314(var35);
            long var36 = (long)var2 * var3;
            q8.G(
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
            q8.u("StreamProof", "Overlay frame dump failed", var30 instanceof RuntimeException var17 ? var17 : new RuntimeException(var30));
         } finally {
            var15.close();
         }
      }
   }
}
