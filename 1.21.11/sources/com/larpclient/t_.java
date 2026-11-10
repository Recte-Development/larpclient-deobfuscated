package com.larpclient;

import com.larpclient.module.ChamsModule;
import com.larpclient.module.KillFXModule;
import com.mojang.blaze3d.buffers.GpuBuffer;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.systems.RenderPass;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.textures.FilterMode;
import com.mojang.blaze3d.textures.GpuTextureView;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.OptionalInt;
import java.util.function.Supplier;
import net.minecraft.class_276;
import net.minecraft.class_310;
import org.lwjgl.system.MemoryStack;

public final class t_ {
   public static t_.t O = t_.t.Q;
   public static t_.t r = t_.t.Q;
   public static Supplier<t_.t> k = () -> null;
   private static t_.t h;
   public static class_276 X;
   private static final w7 J = new w7();
   private static final w7 I = new w7();
   private static final w7 B = new w7();
   private static final w7 H = new w7();
   private static boolean K;
   private static boolean G;
   private static long A;
   private static long c = -1L;
   public static int g;
   private static t_.t m;
   private static t_.t i;
   public static float V = 0.55F;
   public static float f = 1.0F;
   private static GpuBuffer S;
   private static boolean T;
   private static final List<int[]> z = new ArrayList<>();
   private static float n = 1.0F;
   private static int F;
   private static int b = 2;

   public static t_.t x() {
      t_.t var0 = h;
      return var0 != null && var0.ordinal() < O.ordinal() ? var0 : O;
   }

   private static class_276 i() {
      return X != null ? X : class_310.method_1551().method_1522();
   }

   public static int z(t_.t var0) {
      if (var0 == t_.t.A) {
         return 0;
      }

      int var1 = var0.t * 2 + 1;
      if (var0.ordinal() >= t_.t.Q.ordinal()) {
         var1 += t_.t.Q.t * 2 + 1;
      }

      if (var0.ordinal() >= t_.t.S.ordinal()) {
         var1 += var0.t * 2 + 1;
      }

      return var1;
   }

   static int D(t_.t var0) {
      return var0 == t_.t.A ? 0 : (var0.ordinal() >= t_.t.S.ordinal() ? 3 : (var0 == t_.t.Q ? 2 : 1));
   }

   static float a(float var0, int var1) {
      if (var1 >= 1 && Float.isFinite(var0) && !(var0 < 0.0F)) {
         return var0 / var1;
      } else {
         throw new IllegalArgumentException("Invalid blur footprint");
      }
   }

   public static void H() {
      T = (boolean)0;
      z.clear();
      O(2);
   }

   public static void O(int var0) {
      b = Math.clamp(var0, 0, 4);

      n = switch (b) {
         case 1 -> 0.6F;
         default -> 1.0F;
         case 3 -> 1.4F;
         case 4 -> 1.8F;
      };
      F = b >= 3 ? 1 : 0;
   }

   public static boolean P() {
      return r != t_.t.A && b > 0;
   }

   public static void i() {
      if (b > 0) {
         T = (boolean)1;
      }
   }

   public static void A(int var0, int var1, int var2, int var3) {
      if (b > 0 && z.size() < 32) {
         z.add(new int[]{var0, var1, var2, var3});
      }
   }

   public static void r() {
      ws.n();
      q2.d();
      if (!wi.t()) {
         c(class_310.method_1551().method_1522());
      } else {
         if (T || !z.isEmpty()) {
            O();
         }
      }
   }

   private static void c(class_276 var0) {
      if (T) {
         Y(var0);
      } else {
         for (int[] var2 : z) {
            k(var0, var2[0], var2[1], var2[2], var2[3]);
         }
      }
   }

   public static GpuTextureView I() {
      return O() ? I.d.method_71639() : null;
   }

   public static boolean i() {
      return T;
   }

   public static List<int[]> m() {
      return Collections.unmodifiableList(z);
   }

   public static void E() {
      A++;
      h = k.get();
      b0.C(h);
      t_.t var0 = x();
      if (m != var0) {
         J.close();
         B.close();
         H.close();
         q8.G("Bloom", "Quality changed: {}", var0);
         m = var0;
      }

      if (i != r) {
         if (r == t_.t.A) {
            I.close();
         }

         q8.v("Blur", "Quality: {}", r);
         i = r;
      }
   }

   private static void m() {
      if (!G) {
         for (RenderPipeline var3 : new RenderPipeline[]{f2.y, f2.D, f2.f, f2.X}) {
            f2.a(var3);
         }

         G = (boolean)1;
         q8.G("Shader", "Post pipelines initialized");
      }
   }

   public static void L(RenderPipeline var0, class_276 var1, class_276 var2) {
      l(var0, var1, var2, 1.0F);
   }

   private static void l(RenderPipeline var0, class_276 var1, class_276 var2, float var3) {
      int var4 = var0 != f2.f ? 1 : 0;
      if (var4 != 0) {
         if (S == null) {
            S = RenderSystem.getDevice().createBuffer(() -> "Larp post parameters", 136, 16L);
         }

         MemoryStack var5 = MemoryStack.stackPush();

         try {
            ByteBuffer var6 = var5.malloc(16);
            int var7 = Math.max(1, D(x()));
            var6.putFloat(V / var7).putFloat(var3).putFloat(a(var3, var2.field_1482)).putFloat(a(var3, var2.field_1481)).flip();
            RenderSystem.getDevice().createCommandEncoder().writeToBuffer(S.slice(), var6);
         } catch (Throwable var10) {
            if (var5 != null) {
               try {
                  var5.close();
               } catch (Throwable var9) {
                  var10.addSuppressed(var9);
               }
            }

            throw var10;
         }

         if (var5 != null) {
            var5.close();
         }
      }

      RenderPass var12 = RenderSystem.getDevice()
         .createCommandEncoder()
         .createRenderPass(() -> "Larp post " + var0.getLocation(), var2.method_71639(), OptionalInt.empty());

      try {
         yf.l(var12, var0);
         if (var4 != 0) {
            var12.setUniform("PostParameters", S.slice());
         }

         yf.R(var12, "Source", var1.method_71639(), RenderSystem.getSamplerCache().method_75294(FilterMode.LINEAR));
         var12.draw(0, 3);
         c5.V("Post fullscreen draws", 1L);
      } catch (Throwable var11) {
         if (var12 != null) {
            try {
               var12.close();
            } catch (Throwable var8) {
               var11.addSuppressed(var8);
            }
         }

         throw var11;
      }

      if (var12 != null) {
         var12.close();
      }
   }

   public static void B(class_276 var0) {
      m();
      L(f2.f, class_310.method_1551().method_1522(), var0);
   }

   private static void c(class_276 var0, w7 var1, t_.t var2) {
      S(var0, var1, var2, 1.0F);
   }

   private static void S(class_276 var0, w7 var1, t_.t var2, float var3) {
      H(var0, var1, var2, var3, 0);
   }

   private static void H(class_276 var0, w7 var1, t_.t var2, float var3, int var4) {
      l(f2.y, var0, var1.F, var3);
      l(f2.D, var1.F, var1.d, var3);

      for (int var5 = 1; var5 < var2.t + var4; var5++) {
         l(f2.y, var1.d, var1.F, var3);
         l(f2.D, var1.F, var1.d, var3);
      }
   }

   public static class_276 m() {
      t_.t var0 = x();
      if (!K && var0 != t_.t.A) {
         try {
            m();
            class_276 var1 = class_310.method_1551().method_1522();
            J.V(var1.field_1482, var1.field_1481, var0.V, true);
            J.V.method_29329(var1);
            RenderSystem.getDevice().createCommandEncoder().clearColorTexture(J.V.method_30277(), 0);
            return J.V;
         } catch (RuntimeException var2) {
            f("Bloom", var2);
            return null;
         }
      } else {
         return null;
      }
   }

   public static void s() {
      t_.t var0 = x();
      if (!K && var0 != t_.t.A && J.V != null) {
         long var1 = c5.X();

         try {
            S(J.V, J, var0, f);
            class_276 var3 = i();
            L(f2.X, J.d, var3);
            if (var0.ordinal() >= t_.t.Q.ordinal()) {
               B.V(var3.field_1482, var3.field_1481, Math.max(4, var0.V * 2), false);
               S(J.d, B, t_.t.Q, f);
               L(f2.X, B.d, var3);
            }

            if (var0.ordinal() >= t_.t.S.ordinal()) {
               H.V(var3.field_1482, var3.field_1481, Math.max(8, var0.V * 4), false);
               S(B.d, H, var0, f);
               L(f2.X, H.d, var3);
            }
         } catch (RuntimeException var7) {
            f("Bloom", var7);
         } finally {
            c5.w("Bloom", var1);
         }
      }
   }

   private static boolean O() {
      if (K || r == t_.t.A) {
         return false;
      }

      if (c == A) {
         return true;
      }

      long var0 = c5.X();

      try {
         m();
         class_276 var2 = class_310.method_1551().method_1522();
         I.V(var2.field_1482, var2.field_1481, r.V, false);
         H(var2, I, r, n, F);
         c = A;
         return true;
      } catch (RuntimeException var7) {
         f("Blur", var7);
         return false;
      } finally {
         c5.w("Blur", var0);
      }
   }

   private static void Y(class_276 var0) {
      if (O()) {
         try {
            L(f2.f, I.d, var0);
         } catch (RuntimeException var2) {
            f("Blur", var2);
         }
      }
   }

   private static void k(class_276 var0, int var1, int var2, int var3, int var4) {
      if (O()) {
         class_310 var5 = class_310.method_1551();
         class_276 var6 = var0;
         int var7 = var5.method_22683().method_4495();

         try {
            RenderPass var8 = RenderSystem.getDevice()
               .createCommandEncoder()
               .createRenderPass(() -> "Larp HUD blur region", var6.method_71639(), OptionalInt.empty());

            try {
               yf.l(var8, f2.f);
               yf.R(var8, "Source", I.d.method_71639(), RenderSystem.getSamplerCache().method_75294(FilterMode.LINEAR));
               var8.enableScissor(
                  Math.max(0, var1 * var7),
                  Math.max(0, var6.field_1481 - (var2 + var4) * var7),
                  Math.max(0, Math.min(var3 * var7, var6.field_1482 - var1 * var7)),
                  Math.max(0, Math.min(var4 * var7, var6.field_1481 - var2 * var7))
               );
               var8.draw(0, 3);
               c5.V("Post fullscreen draws", 1L);
            } catch (Throwable var12) {
               if (var8 != null) {
                  try {
                     var8.close();
                  } catch (Throwable var11) {
                     var12.addSuppressed(var11);
                  }
               }

               throw var12;
            }

            if (var8 != null) {
               var8.close();
            }
         } catch (RuntimeException var13) {
            f("Blur", var13);
         }
      }
   }

   private static void f(String var0, RuntimeException var1) {
      K = (boolean)1;
      q8.u(var0, "Post-processing disabled for this session; see shader/compiler diagnostics", var1);
      b();
   }

   public static void b() {
      J.close();
      B.close();
      H.close();
      I.close();
      cf.P();
      bi.M();
      ww.E();
      hi.c();
      ChamsModule var0 = ChamsModule.K();
      if (var0 != null) {
         var0.n();
      }

      gv.A();
      if (S != null) {
         S.close();
         S = null;
      }

      G = (boolean)0;
      c = -1L;
   }

   public static void P() {
      g++;
      wi.R();
      f5.V();
      wc.y("Scene resource cleanup", h7::Q);
      wc.y("Refraction cleanup", gr::H);
      ws.h();
      q2.L();
      lj.m();
      wc.h.e();
      KillFXModule var0 = KillFXModule.y();
      if (var0 != null) {
         var0.w();
      }

      b();
      K = (boolean)0;
      q8.G("Shader", "Resources reloaded; post pipelines will be checked on next use");
   }

   public enum t {
      A(0, 0),
      Y(4, 1),
      Q(2, 1),
      S(2, 2),
      J(1, 2);

      final int V;
      final int t;

      t(int var3, int var4) {
         this.V = var3;
         this.t = var4;
      }
   }
}
