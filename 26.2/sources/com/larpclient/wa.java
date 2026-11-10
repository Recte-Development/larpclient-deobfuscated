package com.larpclient;

import com.larpclient.module.ChamsModule;
import com.larpclient.module.KillFXModule;
import com.mojang.blaze3d.buffers.GpuBuffer;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.systems.RenderPass;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.textures.FilterMode;
import com.mojang.blaze3d.textures.GpuTextureView;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;
import net.minecraft.client.Minecraft;
import org.joml.Vector4f;
import org.lwjgl.system.MemoryStack;

public final class wa {
   public static wa.a h = wa.a.Y;
   public static wa.a n = wa.a.Y;
   public static Supplier<wa.a> C = () -> null;
   private static wa.a W;
   public static RenderTarget B;
   private static final bt u = new bt();
   private static final bt a = new bt();
   private static final bt t = new bt();
   private static final bt j = new bt();
   private static boolean g;
   private static boolean L;
   private static long I;
   private static long N = -1L;
   public static int s;
   private static wa.a D;
   private static wa.a O;
   public static float w = 0.55F;
   public static float z = 1.0F;
   private static GpuBuffer M;
   private static boolean Y;
   private static final List<int[]> c = new ArrayList<>();
   private static float E = 1.0F;
   private static int x;
   private static int J = 2;

   public static wa.a Y() {
      wa.a var0 = W;
      return var0 != null && var0.ordinal() < h.ordinal() ? var0 : h;
   }

   private static RenderTarget K() {
      return B != null ? B : Minecraft.getInstance().gameRenderer.mainRenderTarget();
   }

   public static int T(wa.a var0) {
      if (var0 == wa.a.Z) {
         return 0;
      }

      int var1 = var0.T * 2 + 1;
      if (var0.ordinal() >= wa.a.Y.ordinal()) {
         var1 += wa.a.Y.T * 2 + 1;
      }

      if (var0.ordinal() >= wa.a.f.ordinal()) {
         var1 += var0.T * 2 + 1;
      }

      return var1;
   }

   static int c(wa.a var0) {
      return var0 == wa.a.Z ? 0 : (var0.ordinal() >= wa.a.f.ordinal() ? 3 : (var0 == wa.a.Y ? 2 : 1));
   }

   static float d(float var0, int var1) {
      if (var1 >= 1 && Float.isFinite(var0) && !(var0 < 0.0F)) {
         return var0 / var1;
      } else {
         throw new IllegalArgumentException("Invalid blur footprint");
      }
   }

   public static void t() {
      Y = (boolean)0;
      c.clear();
      b(2);
   }

   public static void b(int var0) {
      J = Math.clamp(var0, 0, 4);

      E = switch (J) {
         case 1 -> 0.6F;
         default -> 1.0F;
         case 3 -> 1.4F;
         case 4 -> 1.8F;
      };
      x = J >= 3 ? 1 : 0;
   }

   public static boolean b() {
      return n != wa.a.Z && J > 0;
   }

   public static void P() {
      if (J > 0) {
         Y = (boolean)1;
      }
   }

   public static void T(int var0, int var1, int var2, int var3) {
      if (J > 0 && c.size() < 32) {
         c.add(new int[]{var0, var1, var2, var3});
      }
   }

   public static void j() {
      tb.p();
      c2.Q();
      if (!p2.K()) {
         G(Minecraft.getInstance().gameRenderer.mainRenderTarget());
      } else {
         if (Y || !c.isEmpty()) {
            M();
         }
      }
   }

   private static void G(RenderTarget var0) {
      if (Y) {
         y(var0);
      } else {
         for (int[] var2 : c) {
            W(var0, var2[0], var2[1], var2[2], var2[3]);
         }
      }
   }

   public static GpuTextureView o() {
      return M() ? a.D.getColorTextureView() : null;
   }

   public static boolean p() {
      return Y;
   }

   public static List<int[]> u() {
      return Collections.unmodifiableList(c);
   }

   public static void G() {
      I++;
      W = C.get();
      y.h(W);
      wa.a var0 = Y();
      if (D != var0) {
         u.close();
         t.close();
         j.close();
         com.larpclient.h.A("Bloom", "Quality changed: {}", var0);
         D = var0;
      }

      if (O != n) {
         if (n == wa.a.Z) {
            a.close();
         }

         com.larpclient.h.U("Blur", "Quality: {}", n);
         O = n;
      }
   }

   private static void N() {
      if (!L) {
         for (RenderPipeline var3 : new RenderPipeline[]{ai.j, ai.F, ai.C, ai.g}) {
            ai.U(var3);
         }

         L = (boolean)1;
         com.larpclient.h.A("Shader", "Post pipelines initialized");
      }
   }

   public static void Y(RenderPipeline var0, RenderTarget var1, RenderTarget var2) {
      g(var0, var1, var2, 1.0F);
   }

   private static void g(RenderPipeline var0, RenderTarget var1, RenderTarget var2, float var3) {
      int var4 = var0 != ai.C ? 1 : 0;
      if (var4 != 0) {
         if (M == null) {
            M = RenderSystem.getDevice().createBuffer(() -> "Larp post parameters", 136, 16L);
         }

         MemoryStack var5 = MemoryStack.stackPush();

         try {
            ByteBuffer var6 = var5.malloc(16);
            int var7 = Math.max(1, c(Y()));
            var6.putFloat(w / var7).putFloat(var3).putFloat(d(var3, var2.width)).putFloat(d(var3, var2.height)).flip();
            RenderSystem.getDevice().createCommandEncoder().writeToBuffer(M.slice(), var6);
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
         mx.i(var12, var0);
         if (var4 != 0) {
            var12.setUniform("PostParameters", M.slice());
         }

         mx.x(var12, "Source", var1.getColorTextureView(), RenderSystem.getSamplerCache().getClampToEdge(FilterMode.LINEAR));
         var12.draw(3, 1, 0, 0);
         zj.K("Post fullscreen draws", 1L);
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

   public static void K(RenderTarget var0) {
      N();
      Y(ai.C, Minecraft.getInstance().gameRenderer.mainRenderTarget(), var0);
   }

   private static void v(RenderTarget var0, bt var1, wa.a var2) {
      x(var0, var1, var2, 1.0F);
   }

   private static void x(RenderTarget var0, bt var1, wa.a var2, float var3) {
      L(var0, var1, var2, var3, 0);
   }

   private static void L(RenderTarget var0, bt var1, wa.a var2, float var3, int var4) {
      g(ai.j, var0, var1.M, var3);
      g(ai.F, var1.M, var1.D, var3);

      for (int var5 = 1; var5 < var2.T + var4; var5++) {
         g(ai.j, var1.D, var1.M, var3);
         g(ai.F, var1.M, var1.D, var3);
      }
   }

   public static RenderTarget x() {
      wa.a var0 = Y();
      if (!g && var0 != wa.a.Z) {
         try {
            N();
            RenderTarget var1 = Minecraft.getInstance().gameRenderer.mainRenderTarget();
            u.O(var1.width, var1.height, var0.V, true);
            u.I.copyDepthFrom(var1);
            RenderSystem.getDevice().createCommandEncoder().clearColorTexture(u.I.getColorTexture(), new Vector4f());
            return u.I;
         } catch (RuntimeException var2) {
            k("Bloom", var2);
            return null;
         }
      } else {
         return null;
      }
   }

   public static void E() {
      wa.a var0 = Y();
      if (!g && var0 != wa.a.Z && u.I != null) {
         long var1 = zj.m();

         try {
            x(u.I, u, var0, z);
            RenderTarget var3 = K();
            Y(ai.g, u.D, var3);
            if (var0.ordinal() >= wa.a.Y.ordinal()) {
               t.O(var3.width, var3.height, Math.max(4, var0.V * 2), false);
               x(u.D, t, wa.a.Y, z);
               Y(ai.g, t.D, var3);
            }

            if (var0.ordinal() >= wa.a.f.ordinal()) {
               j.O(var3.width, var3.height, Math.max(8, var0.V * 4), false);
               x(t.D, j, var0, z);
               Y(ai.g, j.D, var3);
            }
         } catch (RuntimeException var7) {
            k("Bloom", var7);
         } finally {
            zj.l("Bloom", var1);
         }
      }
   }

   private static boolean M() {
      if (g || n == wa.a.Z) {
         return false;
      }

      if (N == I) {
         return true;
      }

      long var0 = zj.m();

      try {
         N();
         RenderTarget var2 = Minecraft.getInstance().gameRenderer.mainRenderTarget();
         a.O(var2.width, var2.height, n.V, false);
         L(var2, a, n, E, x);
         N = I;
         return true;
      } catch (RuntimeException var7) {
         k("Blur", var7);
         return false;
      } finally {
         zj.l("Blur", var0);
      }
   }

   private static void y(RenderTarget var0) {
      if (M()) {
         try {
            Y(ai.C, a.D, var0);
         } catch (RuntimeException var2) {
            k("Blur", var2);
         }
      }
   }

   private static void W(RenderTarget var0, int var1, int var2, int var3, int var4) {
      if (M()) {
         Minecraft var5 = Minecraft.getInstance();
         RenderTarget var6 = var0;
         int var7 = var5.getWindow().getGuiScale();

         try {
            RenderPass var8 = RenderSystem.getDevice()
               .createCommandEncoder()
               .createRenderPass(() -> "Larp HUD blur region", var6.getColorTextureView(), Optional.empty());

            try {
               mx.i(var8, ai.C);
               mx.x(var8, "Source", a.D.getColorTextureView(), RenderSystem.getSamplerCache().getClampToEdge(FilterMode.LINEAR));
               var8.enableScissor(
                  Math.max(0, var1 * var7),
                  Math.max(0, var6.height - (var2 + var4) * var7),
                  Math.max(0, Math.min(var3 * var7, var6.width - var1 * var7)),
                  Math.max(0, Math.min(var4 * var7, var6.height - var2 * var7))
               );
               var8.draw(3, 1, 0, 0);
               zj.K("Post fullscreen draws", 1L);
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
            k("Blur", var13);
         }
      }
   }

   private static void k(String var0, RuntimeException var1) {
      g = (boolean)1;
      com.larpclient.h.m(var0, "Post-processing disabled for this session; see shader/compiler diagnostics", var1);
      b();
   }

   public static void b() {
      u.close();
      t.close();
      j.close();
      a.close();
      cd.U();
      sa.u();
      sm.q();
      be.o();
      ChamsModule var0 = ChamsModule.C();
      if (var0 != null) {
         var0.C();
      }

      j4.r();
      if (M != null) {
         M.close();
         M = null;
      }

      L = (boolean)0;
      N = -1L;
   }

   public static void x() {
      s++;
      p2.P();
      wv.h();
      kz.W("Scene resource cleanup", so::b);
      kz.W("Refraction cleanup", l3::l);
      tb.W();
      c2.K();
      he.t();
      kz.P.x();
      KillFXModule var0 = KillFXModule.t();
      if (var0 != null) {
         var0.r();
      }

      b();
      g = (boolean)0;
      com.larpclient.h.A("Shader", "Resources reloaded; post pipelines will be checked on next use");
   }

   public enum a {
      Z(0, 0),
      P(4, 1),
      Y(2, 1),
      f(2, 2),
      E(1, 2);

      final int V;
      final int T;

      a(int var3, int var4) {
         this.V = var3;
         this.T = var4;
      }
   }
}
