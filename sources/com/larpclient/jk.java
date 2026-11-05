package com.larpclient;

import com.larpclient.module.ChamsModule;
import com.larpclient.module.KillFXModule;
import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.renderpearl.api.buffers.GpuBuffer;
import com.mojang.renderpearl.api.commands.RenderPass;
import com.mojang.renderpearl.api.pipeline.RenderPipeline;
import com.mojang.renderpearl.api.textures.FilterMode;
import com.mojang.renderpearl.api.textures.GpuTextureView;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;
import net.minecraft.client.Minecraft;
import org.joml.Vector4f;
import org.lwjgl.system.MemoryStack;

public final class jk {
   public static jk.x u = jk.x.V;
   public static jk.x E = jk.x.V;
   public static Supplier<jk.x> f = () -> null;
   private static jk.x x;
   public static RenderTarget j;
   private static final ld O = new ld();
   private static final ld i = new ld();
   private static final ld P = new ld();
   private static final ld T = new ld();
   private static boolean b;
   private static boolean o;
   private static long B;
   private static long S = -1L;
   public static int v;
   private static jk.x N;
   private static jk.x t;
   public static float L = 0.55F;
   public static float s = 1.0F;
   private static GpuBuffer G;
   private static boolean H;
   private static final List<int[]> g = new ArrayList<>();
   private static float w = 1.0F;
   private static int q;
   private static int U = 2;

   public static jk.x h() {
      jk.x var0 = x;
      return var0 != null && var0.ordinal() < u.ordinal() ? var0 : u;
   }

   private static RenderTarget G() {
      return j != null ? j : Minecraft.getInstance().gameRenderer.mainRenderTarget();
   }

   public static int r(jk.x var0) {
      if (var0 == jk.x.i) {
         return 0;
      }

      int var1 = var0.n * 2 + 1;
      if (var0.ordinal() >= jk.x.V.ordinal()) {
         var1 += jk.x.V.n * 2 + 1;
      }

      if (var0.ordinal() >= jk.x.C.ordinal()) {
         var1 += var0.n * 2 + 1;
      }

      return var1;
   }

   static int B(jk.x var0) {
      return var0 == jk.x.i ? 0 : (var0.ordinal() >= jk.x.C.ordinal() ? 3 : (var0 == jk.x.V ? 2 : 1));
   }

   static float a(float var0, int var1) {
      if (var1 >= 1 && Float.isFinite(var0) && !(var0 < 0.0F)) {
         return var0 / var1;
      } else {
         throw new IllegalArgumentException("Invalid blur footprint");
      }
   }

   public static void p() {
      H = (boolean)0;
      g.clear();
      W(2);
   }

   public static void W(int var0) {
      U = Math.clamp(var0, 0, 4);

      w = switch (U) {
         case 1 -> 0.6F;
         default -> 1.0F;
         case 3 -> 1.4F;
         case 4 -> 1.8F;
      };
      q = U >= 3 ? 1 : 0;
   }

   public static boolean Y() {
      return E != jk.x.i && U > 0;
   }

   public static void F() {
      if (U > 0) {
         H = (boolean)1;
      }
   }

   public static void Z(int var0, int var1, int var2, int var3) {
      if (U > 0 && g.size() < 32) {
         g.add(new int[]{var0, var1, var2, var3});
      }
   }

   public static void S() {
      ow.k();
      rz.U();
      if (!y8.e()) {
         P(Minecraft.getInstance().gameRenderer.mainRenderTarget());
      } else {
         if (H || !g.isEmpty()) {
            h();
         }
      }
   }

   private static void P(RenderTarget var0) {
      if (H) {
         E(var0);
      } else {
         for (int[] var2 : g) {
            u(var0, var2[0], var2[1], var2[2], var2[3]);
         }
      }
   }

   public static GpuTextureView d() {
      return h() ? i.H.getColorTextureView() : null;
   }

   public static boolean d() {
      return H;
   }

   public static List<int[]> b() {
      return Collections.unmodifiableList(g);
   }

   public static void L() {
      B++;
      x = f.get();
      lo.s(x);
      jk.x var0 = h();
      if (N != var0) {
         O.close();
         P.close();
         T.close();
         r4.V("Bloom", "Quality changed: {}", var0);
         N = var0;
      }

      if (t != E) {
         if (E == jk.x.i) {
            i.close();
         }

         r4.g("Blur", "Quality: {}", E);
         t = E;
      }
   }

   private static void i() {
      if (!o) {
         for (RenderPipeline var3 : new RenderPipeline[]{e4.P, e4.M, e4.g, e4.d}) {
            e4.U(var3);
         }

         o = (boolean)1;
         r4.V("Shader", "Post pipelines initialized");
      }
   }

   public static void y(RenderPipeline var0, RenderTarget var1, RenderTarget var2) {
      f(var0, var1, var2, 1.0F);
   }

   private static void f(RenderPipeline var0, RenderTarget var1, RenderTarget var2, float var3) {
      int var4 = var0 != e4.g ? 1 : 0;
      if (var4 != 0) {
         if (G == null) {
            G = RenderSystem.getDevice().createBuffer(() -> "Larp post parameters", 136, 16L);
         }

         MemoryStack var5 = MemoryStack.stackPush();

         try {
            ByteBuffer var6 = var5.malloc(16);
            int var7 = Math.max(1, B(h()));
            var6.putFloat(L / var7).putFloat(var3).putFloat(a(var3, var2.width)).putFloat(a(var3, var2.height)).flip();
            RenderSystem.getDevice().createCommandEncoder().writeToBuffer(G.slice(), var6);
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
         .createRenderPass(() -> "Larp post " + var0.getLocation(), var2.getColorTextureView(), Optional.empty());

      try {
         of.z(var12, var0);
         if (var4 != 0) {
            var12.setUniform("PostParameters", G.slice());
         }

         of.h(var12, "Source", var1.getColorTextureView(), RenderSystem.getSamplerCache().getClampToEdge(FilterMode.LINEAR));
         var12.draw(3, 1, 0, 0);
         h_.A("Post fullscreen draws", 1L);
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

   public static void S(RenderTarget var0) {
      i();
      y(e4.g, Minecraft.getInstance().gameRenderer.mainRenderTarget(), var0);
   }

   private static void I(RenderTarget var0, ld var1, jk.x var2) {
      t(var0, var1, var2, 1.0F);
   }

   private static void t(RenderTarget var0, ld var1, jk.x var2, float var3) {
      t(var0, var1, var2, var3, 0);
   }

   private static void t(RenderTarget var0, ld var1, jk.x var2, float var3, int var4) {
      f(e4.P, var0, var1.U, var3);
      f(e4.M, var1.U, var1.H, var3);

      for (int var5 = 1; var5 < var2.n + var4; var5++) {
         f(e4.P, var1.H, var1.U, var3);
         f(e4.M, var1.U, var1.H, var3);
      }
   }

   public static RenderTarget J() {
      jk.x var0 = h();
      if (!b && var0 != jk.x.i) {
         try {
            i();
            RenderTarget var1 = Minecraft.getInstance().gameRenderer.mainRenderTarget();
            O.t(var1.width, var1.height, var0.A, true);
            O.B.copyDepthFrom(var1);
            RenderSystem.getDevice().createCommandEncoder().clearColorTexture(O.B.getColorTexture(), new Vector4f());
            return O.B;
         } catch (RuntimeException var2) {
            G("Bloom", var2);
            return null;
         }
      } else {
         return null;
      }
   }

   public static void Y() {
      jk.x var0 = h();
      if (!b && var0 != jk.x.i && O.B != null) {
         long var1 = h_.U();

         try {
            t(O.B, O, var0, s);
            RenderTarget var3 = G();
            y(e4.d, O.H, var3);
            if (var0.ordinal() >= jk.x.V.ordinal()) {
               P.t(var3.width, var3.height, Math.max(4, var0.A * 2), false);
               t(O.H, P, jk.x.V, s);
               y(e4.d, P.H, var3);
            }

            if (var0.ordinal() >= jk.x.C.ordinal()) {
               T.t(var3.width, var3.height, Math.max(8, var0.A * 4), false);
               t(P.H, T, var0, s);
               y(e4.d, T.H, var3);
            }
         } catch (RuntimeException var7) {
            G("Bloom", var7);
         } finally {
            h_.n("Bloom", var1);
         }
      }
   }

   private static boolean h() {
      if (b || E == jk.x.i) {
         return false;
      }

      if (S == B) {
         return true;
      }

      long var0 = h_.U();

      try {
         i();
         RenderTarget var2 = Minecraft.getInstance().gameRenderer.mainRenderTarget();
         i.t(var2.width, var2.height, E.A, false);
         t(var2, i, E, w, q);
         S = B;
         return true;
      } catch (RuntimeException var7) {
         G("Blur", var7);
         return false;
      } finally {
         h_.n("Blur", var0);
      }
   }

   private static void E(RenderTarget var0) {
      if (h()) {
         try {
            y(e4.g, i.H, var0);
         } catch (RuntimeException var2) {
            G("Blur", var2);
         }
      }
   }

   private static void u(RenderTarget var0, int var1, int var2, int var3, int var4) {
      if (h()) {
         Minecraft var5 = Minecraft.getInstance();
         RenderTarget var6 = var0;
         int var7 = var5.getWindow().getGuiScale();

         try {
            RenderPass var8 = RenderSystem.getDevice()
               .createCommandEncoder()
               .createRenderPass(() -> "Larp HUD blur region", var6.getColorTextureView(), Optional.empty());

            try {
               of.z(var8, e4.g);
               of.h(var8, "Source", i.H.getColorTextureView(), RenderSystem.getSamplerCache().getClampToEdge(FilterMode.LINEAR));
               var8.enableScissor(
                  Math.max(0, var1 * var7),
                  Math.max(0, var6.height - (var2 + var4) * var7),
                  Math.max(0, Math.min(var3 * var7, var6.width - var1 * var7)),
                  Math.max(0, Math.min(var4 * var7, var6.height - var2 * var7))
               );
               var8.draw(3, 1, 0, 0);
               h_.A("Post fullscreen draws", 1L);
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
            G("Blur", var13);
         }
      }
   }

   private static void G(String var0, RuntimeException var1) {
      b = (boolean)1;
      r4.r(var0, "Post-processing disabled for this session; see shader/compiler diagnostics", var1);
      g();
   }

   public static void g() {
      O.close();
      P.close();
      T.close();
      i.close();
      mu.s();
      mc.J();
      la.J();
      dy.z();
      ChamsModule var0 = ChamsModule.A();
      if (var0 != null) {
         var0.K();
      }

      uv.S();
      if (G != null) {
         G.close();
         G = null;
      }

      o = (boolean)0;
      S = -1L;
   }

   public static void H() {
      v++;
      y8.T();
      com.larpclient.f.l();
      f8.M("Scene resource cleanup", x1::k);
      f8.M("Refraction cleanup", m6::D);
      ow.C();
      rz.C();
      jx.E();
      f8.O.v();
      KillFXModule var0 = KillFXModule.X();
      if (var0 != null) {
         var0.d();
      }

      g();
      b = (boolean)0;
      r4.V("Shader", "Resources reloaded; post pipelines will be checked on next use");
   }

   public enum x {
      i(0, 0),
      L(4, 1),
      V(2, 1),
      C(2, 2),
      a(1, 2);

      final int A;
      final int n;

      x(int var3, int var4) {
         this.A = var3;
         this.n = var4;
      }
   }
}
