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

public final class m5 {
   public static m5.n y = m5.n.h;
   public static m5.n u = m5.n.h;
   public static Supplier<m5.n> R = () -> null;
   private static m5.n H;
   public static RenderTarget A;
   private static final e4 x = new e4();
   private static final e4 r = new e4();
   private static final e4 i = new e4();
   private static final e4 w = new e4();
   private static boolean t;
   private static boolean c;
   private static long b;
   private static long m = -1L;
   public static int k;
   private static m5.n j;
   private static m5.n D;
   public static float v = 0.55F;
   public static float a = 1.0F;
   private static GpuBuffer L;
   private static boolean Z;
   private static final List<int[]> n = new ArrayList<>();
   private static float g = 1.0F;
   private static int l;
   private static int s = 2;

   public static m5.n M() {
      m5.n var0 = H;
      return var0 != null && var0.ordinal() < y.ordinal() ? var0 : y;
   }

   private static RenderTarget t() {
      return A != null ? A : Minecraft.getInstance().gameRenderer.mainRenderTarget();
   }

   public static int L(m5.n var0) {
      if (var0 == m5.n.N) {
         return 0;
      }

      int var1 = var0.Z * 2 + 1;
      if (var0.ordinal() >= m5.n.h.ordinal()) {
         var1 += m5.n.h.Z * 2 + 1;
      }

      if (var0.ordinal() >= m5.n.B.ordinal()) {
         var1 += var0.Z * 2 + 1;
      }

      return var1;
   }

   static int F(m5.n var0) {
      return var0 == m5.n.N ? 0 : (var0.ordinal() >= m5.n.B.ordinal() ? 3 : (var0 == m5.n.h ? 2 : 1));
   }

   static float d(float var0, int var1) {
      if (var1 >= 1 && Float.isFinite(var0) && !(var0 < 0.0F)) {
         return var0 / var1;
      } else {
         throw new IllegalArgumentException("Invalid blur footprint");
      }
   }

   public static void j() {
      Z = (boolean)0;
      n.clear();
      M(2);
   }

   public static void M(int var0) {
      s = Math.clamp(var0, 0, 4);

      g = switch (s) {
         case 1 -> 0.6F;
         default -> 1.0F;
         case 3 -> 1.4F;
         case 4 -> 1.8F;
      };
      l = s >= 3 ? 1 : 0;
   }

   public static boolean w() {
      return u != m5.n.N && s > 0;
   }

   public static void o() {
      if (s > 0) {
         Z = (boolean)1;
      }
   }

   public static void y(int var0, int var1, int var2, int var3) {
      if (s > 0 && n.size() < 32) {
         n.add(new int[]{var0, var1, var2, var3});
      }
   }

   public static void f() {
      tl.m();
      ew.W();
      if (!zo.Q()) {
         C(Minecraft.getInstance().gameRenderer.mainRenderTarget());
      } else {
         if (Z || !n.isEmpty()) {
            g();
         }
      }
   }

   private static void C(RenderTarget var0) {
      if (Z) {
         a(var0);
      } else {
         for (int[] var2 : n) {
            x(var0, var2[0], var2[1], var2[2], var2[3]);
         }
      }
   }

   public static GpuTextureView r() {
      return g() ? r.v.getColorTextureView() : null;
   }

   public static boolean R() {
      return Z;
   }

   public static List<int[]> v() {
      return Collections.unmodifiableList(n);
   }

   public static void x() {
      b++;
      H = R.get();
      _k.C(H);
      m5.n var0 = M();
      if (j != var0) {
         x.close();
         i.close();
         w.close();
         mt.K("Bloom", "Quality changed: {}", var0);
         j = var0;
      }

      if (D != u) {
         if (u == m5.n.N) {
            r.close();
         }

         mt.U("Blur", "Quality: {}", u);
         D = u;
      }
   }

   private static void R() {
      if (!c) {
         for (RenderPipeline var3 : new RenderPipeline[]{ea.X, ea.y, ea.m, ea.C}) {
            ea.z(var3);
         }

         c = (boolean)1;
         mt.K("Shader", "Post pipelines initialized");
      }
   }

   public static void l(RenderPipeline var0, RenderTarget var1, RenderTarget var2) {
      i(var0, var1, var2, 1.0F);
   }

   private static void i(RenderPipeline var0, RenderTarget var1, RenderTarget var2, float var3) {
      int var4 = var0 != ea.m ? 1 : 0;
      if (var4 != 0) {
         if (L == null) {
            L = RenderSystem.getDevice().createBuffer(() -> "Larp post parameters", 136, 16L);
         }

         MemoryStack var5 = MemoryStack.stackPush();

         try {
            ByteBuffer var6 = var5.malloc(16);
            int var7 = Math.max(1, F(M()));
            var6.putFloat(v / var7).putFloat(var3).putFloat(d(var3, var2.width)).putFloat(d(var3, var2.height)).flip();
            RenderSystem.getDevice().createCommandEncoder().writeToBuffer(L.slice(), var6);
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
         hj.P(var12, var0);
         if (var4 != 0) {
            var12.setUniform("PostParameters", L.slice());
         }

         hj.u(var12, "Source", var1.getColorTextureView(), RenderSystem.getSamplerCache().getClampToEdge(FilterMode.LINEAR));
         var12.draw(3, 1, 0, 0);
         kx.U("Post fullscreen draws", 1L);
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

   public static void W(RenderTarget var0) {
      R();
      l(ea.m, Minecraft.getInstance().gameRenderer.mainRenderTarget(), var0);
   }

   private static void g(RenderTarget var0, e4 var1, m5.n var2) {
      J(var0, var1, var2, 1.0F);
   }

   private static void J(RenderTarget var0, e4 var1, m5.n var2, float var3) {
      O(var0, var1, var2, var3, 0);
   }

   private static void O(RenderTarget var0, e4 var1, m5.n var2, float var3, int var4) {
      i(ea.X, var0, var1.b, var3);
      i(ea.y, var1.b, var1.v, var3);

      for (int var5 = 1; var5 < var2.Z + var4; var5++) {
         i(ea.X, var1.v, var1.b, var3);
         i(ea.y, var1.b, var1.v, var3);
      }
   }

   public static RenderTarget T() {
      m5.n var0 = M();
      if (!t && var0 != m5.n.N) {
         try {
            R();
            RenderTarget var1 = Minecraft.getInstance().gameRenderer.mainRenderTarget();
            x.r(var1.width, var1.height, var0.A, true);
            x.M.copyDepthFrom(var1);
            RenderSystem.getDevice().createCommandEncoder().clearColorTexture(x.M.getColorTexture(), new Vector4f());
            return x.M;
         } catch (RuntimeException var2) {
            L("Bloom", var2);
            return null;
         }
      } else {
         return null;
      }
   }

   public static void w() {
      m5.n var0 = M();
      if (!t && var0 != m5.n.N && x.M != null) {
         long var1 = kx.G();

         try {
            J(x.M, x, var0, a);
            RenderTarget var3 = t();
            l(ea.C, x.v, var3);
            if (var0.ordinal() >= m5.n.h.ordinal()) {
               i.r(var3.width, var3.height, Math.max(4, var0.A * 2), false);
               J(x.v, i, m5.n.h, a);
               l(ea.C, i.v, var3);
            }

            if (var0.ordinal() >= m5.n.B.ordinal()) {
               w.r(var3.width, var3.height, Math.max(8, var0.A * 4), false);
               J(i.v, w, var0, a);
               l(ea.C, w.v, var3);
            }
         } catch (RuntimeException var7) {
            L("Bloom", var7);
         } finally {
            kx.u("Bloom", var1);
         }
      }
   }

   private static boolean g() {
      if (t || u == m5.n.N) {
         return false;
      }

      if (m == b) {
         return true;
      }

      long var0 = kx.G();

      try {
         R();
         RenderTarget var2 = Minecraft.getInstance().gameRenderer.mainRenderTarget();
         r.r(var2.width, var2.height, u.A, false);
         O(var2, r, u, g, l);
         m = b;
         return true;
      } catch (RuntimeException var7) {
         L("Blur", var7);
         return false;
      } finally {
         kx.u("Blur", var0);
      }
   }

   private static void a(RenderTarget var0) {
      if (g()) {
         try {
            l(ea.m, r.v, var0);
         } catch (RuntimeException var2) {
            L("Blur", var2);
         }
      }
   }

   private static void x(RenderTarget var0, int var1, int var2, int var3, int var4) {
      if (g()) {
         Minecraft var5 = Minecraft.getInstance();
         RenderTarget var6 = var0;
         int var7 = var5.getWindow().getGuiScale();

         try {
            RenderPass var8 = RenderSystem.getDevice()
               .createCommandEncoder()
               .createRenderPass(() -> "Larp HUD blur region", var6.getColorTextureView(), Optional.empty());

            try {
               hj.P(var8, ea.m);
               hj.u(var8, "Source", r.v.getColorTextureView(), RenderSystem.getSamplerCache().getClampToEdge(FilterMode.LINEAR));
               var8.enableScissor(
                  Math.max(0, var1 * var7),
                  Math.max(0, var6.height - (var2 + var4) * var7),
                  Math.max(0, Math.min(var3 * var7, var6.width - var1 * var7)),
                  Math.max(0, Math.min(var4 * var7, var6.height - var2 * var7))
               );
               var8.draw(3, 1, 0, 0);
               kx.U("Post fullscreen draws", 1L);
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
            L("Blur", var13);
         }
      }
   }

   private static void L(String var0, RuntimeException var1) {
      t = (boolean)1;
      mt.P(var0, "Post-processing disabled for this session; see shader/compiler diagnostics", var1);
      A();
   }

   public static void A() {
      x.close();
      i.close();
      w.close();
      r.close();
      eb.f();
      x6.o();
      zj.g();
      xw.F();
      ChamsModule var0 = ChamsModule.u();
      if (var0 != null) {
         var0.w();
      }

      tj.Q();
      if (L != null) {
         L.close();
         L = null;
      }

      c = (boolean)0;
      m = -1L;
   }

   public static void s() {
      k++;
      zo.w();
      mb.e();
      h.K("Scene resource cleanup", ex::x);
      h.K("Refraction cleanup", _c::o);
      tl.k();
      ew.A();
      _v.w();
      h.H.C();
      KillFXModule var0 = KillFXModule.l();
      if (var0 != null) {
         var0.c();
      }

      A();
      t = (boolean)0;
      mt.K("Shader", "Resources reloaded; post pipelines will be checked on next use");
   }

   public enum n {
      N(0, 0),
      T(4, 1),
      h(2, 1),
      B(2, 2),
      O(1, 2);

      final int A;
      final int Z;

      n(int var3, int var4) {
         this.A = var3;
         this.Z = var4;
      }
   }
}
