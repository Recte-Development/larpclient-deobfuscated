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

public final class nl {
   public static nl.q x = nl.q.C;
   public static nl.q Q = nl.q.C;
   public static Supplier<nl.q> Y = () -> null;
   private static nl.q v;
   public static RenderTarget l;
   private static final bk f = new bk();
   private static final bk o = new bk();
   private static final bk m = new bk();
   private static final bk h = new bk();
   private static boolean s;
   private static boolean u;
   private static long E;
   private static long z = -1L;
   public static int q;
   private static nl.q K;
   private static nl.q N;
   public static float b = 0.55F;
   public static float C = 1.0F;
   private static GpuBuffer g;
   private static boolean a;
   private static final List<int[]> X = new ArrayList<>();
   private static float p = 1.0F;
   private static int Z;
   private static int L = 2;

   public static nl.q T() {
      nl.q var0 = v;
      return var0 != null && var0.ordinal() < x.ordinal() ? var0 : x;
   }

   private static RenderTarget p() {
      return l != null ? l : Minecraft.getInstance().gameRenderer.mainRenderTarget();
   }

   public static int v(nl.q var0) {
      if (var0 == nl.q.q) {
         return 0;
      }

      int var1 = var0.d * 2 + 1;
      if (var0.ordinal() >= nl.q.C.ordinal()) {
         var1 += nl.q.C.d * 2 + 1;
      }

      if (var0.ordinal() >= nl.q.n.ordinal()) {
         var1 += var0.d * 2 + 1;
      }

      return var1;
   }

   static int s(nl.q var0) {
      return var0 == nl.q.q ? 0 : (var0.ordinal() >= nl.q.n.ordinal() ? 3 : (var0 == nl.q.C ? 2 : 1));
   }

   static float b(float var0, int var1) {
      if (var1 >= 1 && Float.isFinite(var0) && !(var0 < 0.0F)) {
         return var0 / var1;
      } else {
         throw new IllegalArgumentException("Invalid blur footprint");
      }
   }

   public static void Z() {
      a = (boolean)0;
      X.clear();
      p(2);
   }

   public static void p(int var0) {
      L = Math.clamp(var0, 0, 4);

      p = switch (L) {
         case 1 -> 0.6F;
         default -> 1.0F;
         case 3 -> 1.4F;
         case 4 -> 1.8F;
      };
      Z = L >= 3 ? 1 : 0;
   }

   public static boolean z() {
      return Q != nl.q.q && L > 0;
   }

   public static void K() {
      if (L > 0) {
         a = (boolean)1;
      }
   }

   public static void N(int var0, int var1, int var2, int var3) {
      if (L > 0 && X.size() < 32) {
         X.add(new int[]{var0, var1, var2, var3});
      }
   }

   public static void x() {
      w1.U();
      n8.u();
      if (!qc.R()) {
         F(Minecraft.getInstance().gameRenderer.mainRenderTarget());
      } else {
         if (a || !X.isEmpty()) {
            s();
         }
      }
   }

   private static void F(RenderTarget var0) {
      if (a) {
         C(var0);
      } else {
         for (int[] var2 : X) {
            g(var0, var2[0], var2[1], var2[2], var2[3]);
         }
      }
   }

   public static GpuTextureView w() {
      return s() ? o.w.getColorTextureView() : null;
   }

   public static boolean Z() {
      return a;
   }

   public static List<int[]> E() {
      return Collections.unmodifiableList(X);
   }

   public static void u() {
      E++;
      v = Y.get();
      a4.M(v);
      nl.q var0 = T();
      if (K != var0) {
         f.close();
         m.close();
         h.close();
         _q.z("Bloom", "Quality changed: {}", var0);
         K = var0;
      }

      if (N != Q) {
         if (Q == nl.q.q) {
            o.close();
         }

         _q.c("Blur", "Quality: {}", Q);
         N = Q;
      }
   }

   private static void B() {
      if (!u) {
         for (RenderPipeline var3 : new RenderPipeline[]{se.i, se.p, se.j, se.g}) {
            se.j(var3);
         }

         u = (boolean)1;
         _q.z("Shader", "Post pipelines initialized");
      }
   }

   public static void G(RenderPipeline var0, RenderTarget var1, RenderTarget var2) {
      h(var0, var1, var2, 1.0F);
   }

   private static void h(RenderPipeline var0, RenderTarget var1, RenderTarget var2, float var3) {
      int var4 = var0 != se.j ? 1 : 0;
      if (var4 != 0) {
         if (g == null) {
            g = RenderSystem.getDevice().createBuffer(() -> "Larp post parameters", 136, 16L);
         }

         MemoryStack var5 = MemoryStack.stackPush();

         try {
            ByteBuffer var6 = var5.malloc(16);
            int var7 = Math.max(1, s(T()));
            var6.putFloat(b / var7).putFloat(var3).putFloat(b(var3, var2.width)).putFloat(b(var3, var2.height)).flip();
            RenderSystem.getDevice().createCommandEncoder().writeToBuffer(g.slice(), var6);
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
         p7.K(var12, var0);
         if (var4 != 0) {
            var12.setUniform("PostParameters", g.slice());
         }

         p7.t(var12, "Source", var1.getColorTextureView(), RenderSystem.getSamplerCache().getClampToEdge(FilterMode.LINEAR));
         var12.draw(3, 1, 0, 0);
         he.Z("Post fullscreen draws", 1L);
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

   public static void h(RenderTarget var0) {
      B();
      G(se.j, Minecraft.getInstance().gameRenderer.mainRenderTarget(), var0);
   }

   private static void r(RenderTarget var0, bk var1, nl.q var2) {
      w(var0, var1, var2, 1.0F);
   }

   private static void w(RenderTarget var0, bk var1, nl.q var2, float var3) {
      k(var0, var1, var2, var3, 0);
   }

   private static void k(RenderTarget var0, bk var1, nl.q var2, float var3, int var4) {
      h(se.i, var0, var1.B, var3);
      h(se.p, var1.B, var1.w, var3);

      for (int var5 = 1; var5 < var2.d + var4; var5++) {
         h(se.i, var1.w, var1.B, var3);
         h(se.p, var1.B, var1.w, var3);
      }
   }

   public static RenderTarget O() {
      nl.q var0 = T();
      if (!s && var0 != nl.q.q) {
         try {
            B();
            RenderTarget var1 = Minecraft.getInstance().gameRenderer.mainRenderTarget();
            f.l(var1.width, var1.height, var0.z, true);
            f.Y.copyDepthFrom(var1);
            RenderSystem.getDevice().createCommandEncoder().clearColorTexture(f.Y.getColorTexture(), new Vector4f());
            return f.Y;
         } catch (RuntimeException var2) {
            v("Bloom", var2);
            return null;
         }
      } else {
         return null;
      }
   }

   public static void X() {
      nl.q var0 = T();
      if (!s && var0 != nl.q.q && f.Y != null) {
         long var1 = he.s();

         try {
            w(f.Y, f, var0, C);
            RenderTarget var3 = p();
            G(se.g, f.w, var3);
            if (var0.ordinal() >= nl.q.C.ordinal()) {
               m.l(var3.width, var3.height, Math.max(4, var0.z * 2), false);
               w(f.w, m, nl.q.C, C);
               G(se.g, m.w, var3);
            }

            if (var0.ordinal() >= nl.q.n.ordinal()) {
               h.l(var3.width, var3.height, Math.max(8, var0.z * 4), false);
               w(m.w, h, var0, C);
               G(se.g, h.w, var3);
            }
         } catch (RuntimeException var7) {
            v("Bloom", var7);
         } finally {
            he.T("Bloom", var1);
         }
      }
   }

   private static boolean s() {
      if (s || Q == nl.q.q) {
         return false;
      }

      if (z == E) {
         return true;
      }

      long var0 = he.s();

      try {
         B();
         RenderTarget var2 = Minecraft.getInstance().gameRenderer.mainRenderTarget();
         o.l(var2.width, var2.height, Q.z, false);
         k(var2, o, Q, p, Z);
         z = E;
         return true;
      } catch (RuntimeException var7) {
         v("Blur", var7);
         return false;
      } finally {
         he.T("Blur", var0);
      }
   }

   private static void C(RenderTarget var0) {
      if (s()) {
         try {
            G(se.j, o.w, var0);
         } catch (RuntimeException var2) {
            v("Blur", var2);
         }
      }
   }

   private static void g(RenderTarget var0, int var1, int var2, int var3, int var4) {
      if (s()) {
         Minecraft var5 = Minecraft.getInstance();
         RenderTarget var6 = var0;
         int var7 = var5.getWindow().getGuiScale();

         try {
            RenderPass var8 = RenderSystem.getDevice()
               .createCommandEncoder()
               .createRenderPass(() -> "Larp HUD blur region", var6.getColorTextureView(), Optional.empty());

            try {
               p7.K(var8, se.j);
               p7.t(var8, "Source", o.w.getColorTextureView(), RenderSystem.getSamplerCache().getClampToEdge(FilterMode.LINEAR));
               var8.enableScissor(
                  Math.max(0, var1 * var7),
                  Math.max(0, var6.height - (var2 + var4) * var7),
                  Math.max(0, Math.min(var3 * var7, var6.width - var1 * var7)),
                  Math.max(0, Math.min(var4 * var7, var6.height - var2 * var7))
               );
               var8.draw(3, 1, 0, 0);
               he.Z("Post fullscreen draws", 1L);
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
            v("Blur", var13);
         }
      }
   }

   private static void v(String var0, RuntimeException var1) {
      s = (boolean)1;
      _q.o(var0, "Post-processing disabled for this session; see shader/compiler diagnostics", var1);
      y();
   }

   public static void y() {
      f.close();
      m.close();
      h.close();
      o.close();
      br.B();
      com.larpclient.u.e();
      h9.L();
      com.larpclient.v.H();
      ChamsModule var0 = ChamsModule.O();
      if (var0 != null) {
         var0.g();
      }

      w7.A();
      if (g != null) {
         g.close();
         g = null;
      }

      u = (boolean)0;
      z = -1L;
   }

   public static void I() {
      q++;
      qc.m();
      q3.z();
      wp.X("Scene resource cleanup", _s::m);
      wp.X("Refraction cleanup", bq::r);
      w1.o();
      n8.l();
      d3.X();
      wp.V.t();
      KillFXModule var0 = KillFXModule.J();
      if (var0 != null) {
         var0.D();
      }

      y();
      s = (boolean)0;
      _q.z("Shader", "Resources reloaded; post pipelines will be checked on next use");
   }

   public enum q {
      q(0, 0),
      l(4, 1),
      C(2, 1),
      n(2, 2),
      V(1, 2);

      final int z;
      final int d;

      q(int var3, int var4) {
         this.z = var3;
         this.d = var4;
      }
   }
}
