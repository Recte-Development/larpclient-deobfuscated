package com.larpclient;

import com.mojang.blaze3d.buffers.GpuBuffer;
import com.mojang.blaze3d.pipeline.BlendFunction;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.pipeline.RenderPipeline.Builder;
import com.mojang.blaze3d.pipeline.RenderPipeline.Snippet;
import com.mojang.blaze3d.platform.DepthTestFunction;
import com.mojang.blaze3d.systems.CommandEncoder;
import com.mojang.blaze3d.systems.RenderPass;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.textures.FilterMode;
import com.mojang.blaze3d.textures.GpuTextureView;
import com.mojang.blaze3d.vertex.VertexFormat.class_5596;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.OptionalInt;
import net.minecraft.class_10017;
import net.minecraft.class_10789;
import net.minecraft.class_12137;
import net.minecraft.class_276;
import net.minecraft.class_290;
import net.minecraft.class_2960;
import net.minecraft.class_310;
import net.minecraft.class_4587;
import net.minecraft.class_6367;
import org.lwjgl.system.MemoryStack;

public final class wd implements AutoCloseable {
   private static final int a = 0;
   private static final double b = 1.0;
   private static final RenderPipeline I = r("chams_edge", false);
   private static final RenderPipeline X = p();
   private static final RenderPipeline f = r("chams_blur", false);
   private static final RenderPipeline V = r("chams_composite", true);
   private static final RenderPipeline Z = RenderPipeline.builder(new Snippet[0])
      .withLocation(O("chams_hand_mask"))
      .withVertexShader(O("post/fullscreen"))
      .withFragmentShader(O("post/chams_hand_mask"))
      .withSampler("Before")
      .withSampler("After")
      .withSampler("HandDepth")
      .withVertexFormat(class_290.field_60033, class_5596.field_27379)
      .withDepthTestFunction(DepthTestFunction.NO_DEPTH_TEST)
      .withDepthWrite(false)
      .withCull(false)
      .build();
   private final String k;
   private final w7 h = new w7();
   private class_6367 M;
   private GpuBuffer P;
   private cw U;
   private int A = -1;
   private boolean e;
   private boolean o;
   private boolean p;
   private boolean n;
   private boolean N;
   private boolean d;
   private static final long g = 5000000000L;
   private long R;

   public wd(String var1) {
      this.k = var1;
   }

   private static RenderPipeline p() {
      return RenderPipeline.builder(new Snippet[0])
         .withLocation(O("chams_entity_mask"))
         .withVertexShader(O("post/fullscreen"))
         .withFragmentShader(O("post/chams_entity_mask"))
         .withSampler("Source")
         .withSampler("ModelDepth")
         .withSampler("SceneDepth")
         .withUniform("ChamsParameters", class_10789.field_60031)
         .withVertexFormat(class_290.field_60033, class_5596.field_27379)
         .withDepthTestFunction(DepthTestFunction.NO_DEPTH_TEST)
         .withDepthWrite(false)
         .withoutBlend()
         .withCull(false)
         .build();
   }

   private static RenderPipeline r(String var0, boolean var1) {
      Builder var2 = RenderPipeline.builder(new Snippet[0]).withSampler("Source").withUniform("ChamsParameters", class_10789.field_60031);
      if (var1) {
         var2.withSampler("Glow");
      }

      if (var1) {
         var2.withBlend(BlendFunction.TRANSLUCENT);
      } else {
         var2.withoutBlend();
      }

      return var2.withLocation(O(var0))
         .withVertexShader(O("post/fullscreen"))
         .withFragmentShader(O("post/" + var0))
         .withVertexFormat(class_290.field_60033, class_5596.field_27379)
         .withDepthTestFunction(DepthTestFunction.NO_DEPTH_TEST)
         .withDepthWrite(false)
         .withCull(false)
         .build();
   }

   private boolean a() {
      int var1 = RenderSystem.outputColorTextureOverride == null && RenderSystem.outputDepthTextureOverride == null ? 1 : 0;
      if (var1 == 0 && !this.o) {
         this.o = (boolean)1;
         q8.L(this.k, "Gradient capture skipped while another renderer owns the output attachments");
      }

      return (boolean)var1;
   }

   private boolean Z() {
      if (this.A != t_.g) {
         this.close();
         this.A = t_.g;
         this.e = (boolean)0;
      }

      if (!this.e && this.a()) {
         try {
            class_276 var1 = class_310.method_1551().method_1522();
            if (this.M != null && (this.M.field_1482 != var1.field_1482 || this.M.field_1481 != var1.field_1481)) {
               this.close();
            }

            if (this.M == null) {
               f2.a(I);
               f2.a(X);
               f2.a(f);
               f2.a(V);
               this.h.f(var1.field_1482, var1.field_1481, 1, w7.p.G);
               this.M = new class_6367("Larp " + this.k + " gradient", var1.field_1482, var1.field_1481, false);
               this.P = RenderSystem.getDevice().createBuffer(() -> "Larp " + this.k + " gradient parameters", 136, 80L);
               q8.G(this.k, "Gradient pass initialized {}x{}; four full-resolution targets", var1.field_1482, var1.field_1481);
            }

            return true;
         } catch (RuntimeException var2) {
            this.e("allocate/check", var2);
            return false;
         }
      } else {
         return false;
      }
   }

   public void f(List<wd.b> var1, class_4587 var2, ba.e var3, boolean var4) {
      if (var1.isEmpty()) {
         long var22 = System.nanoTime();
         if (this.R == 0L) {
            this.R = var22;
         } else if (var22 - this.R > 5000000000L) {
            this.close();
         }
      } else {
         this.R = 0L;
         if (this.Z()) {
            GpuTextureView var5 = RenderSystem.outputColorTextureOverride;
            GpuTextureView var6 = RenderSystem.outputDepthTextureOverride;

            try {
               this.N = (boolean)1;
               this.d = var4;
               if (h7.J() == null) {
                  throw new IllegalStateException("scene-depth capture unavailable");
               }

               if (this.U == null) {
                  this.U = new cw();
               }

               CommandEncoder var7 = RenderSystem.getDevice().createCommandEncoder();
               var7.clearColorTexture(this.h.V.method_30277(), 0);
               var7.clearDepthTexture(this.h.V.method_30278(), 1.0);
               RenderSystem.outputColorTextureOverride = this.h.V.method_71639();
               RenderSystem.outputDepthTextureOverride = this.h.V.method_71640();

               try {
                  gv.S(this.h.V.field_1482, this.h.V.field_1481);

                  for (wd.b var10 : var1) {
                     gv.o(var10.state, var2, this.U, gv.y.J, var10.color, 1.0, false, false);
                  }

                  this.U.P();
               } finally {
                  int[] var8 = gv.E();
                  RenderSystem.outputColorTextureOverride = var5;
                  RenderSystem.outputDepthTextureOverride = var6;
               }

               Object var23;
               if (var23 == null || ((Object[])var23)[2] > 0) {
                  this.y(var3, (int[])var23);
               }
            } catch (RuntimeException var20) {
               this.e("entity mask/composite", var20);
            } finally {
               RenderSystem.outputColorTextureOverride = var5;
               RenderSystem.outputDepthTextureOverride = var6;
               if (this.U != null) {
                  this.U.W();
               }

               this.N = (boolean)0;
            }
         }
      }
   }

   public boolean P() {
      this.p = (boolean)0;
      if (!this.Z()) {
         return false;
      }

      try {
         if (!this.n) {
            f2.a(Z);
            this.n = (boolean)1;
         }

         t_.L(f2.f, wi.m(class_310.method_1551().method_1522()), this.M);
         this.p = (boolean)1;
         return true;
      } catch (RuntimeException var2) {
         this.e("hand scene capture", var2);
         return false;
      }
   }

   public void H(ba.e var1) {
      class_276 var2 = this.E();
      if (var2 != null) {
         try {
            this.y(var1, null);
         } catch (RuntimeException var4) {
            this.e("hand mask/composite", var4);
         }
      }
   }

   public void b(List<wd.e> var1) {
      class_276 var2 = this.E();
      if (var2 != null) {
         long var3 = c5.X();

         try {
            for (wd.e var6 : var1) {
               this.Z(I, this.h.V, this.M, var6.frame(), 0.0F, 0.0F, var6);
               this.Z(V, this.M, var2, var6.frame(), 0.0F, 0.0F, var6);
            }

            h7.G();
         } catch (RuntimeException var10) {
            this.e("hand region composite", var10);
         } finally {
            c5.w(this.k + " gradient post", var3);
         }
      }
   }

   private class_276 E() {
      if (!this.p) {
         return null;
      }

      this.p = (boolean)0;
      if (!this.a()) {
         this.close();
         return null;
      }

      try {
         class_276 var1 = class_310.method_1551().method_1522();
         if (var1.field_1482 == this.M.field_1482 && var1.field_1481 == this.M.field_1481) {
            CommandEncoder var2 = RenderSystem.getDevice().createCommandEncoder();
            RenderPass var3 = var2.createRenderPass(() -> "Larp " + this.k + " silhouette", this.h.V.method_71639(), OptionalInt.empty());

            try {
               yf.l(var3, Z);
               class_12137 var4 = RenderSystem.getSamplerCache().method_75294(FilterMode.LINEAR);
               yf.R(var3, "Before", this.M.method_71639(), var4);
               yf.R(var3, "After", wi.m(var1).method_71639(), var4);
               yf.R(var3, "HandDepth", var1.method_71640(), RenderSystem.getSamplerCache().method_75294(FilterMode.NEAREST));
               var3.draw(0, 3);
            } catch (Throwable var7) {
               if (var3 != null) {
                  try {
                     var3.close();
                  } catch (Throwable var6) {
                     var7.addSuppressed(var6);
                  }
               }

               throw var7;
            }

            if (var3 != null) {
               var3.close();
            }

            return var1;
         } else {
            this.close();
            return null;
         }
      } catch (RuntimeException var8) {
         this.e("hand mask", var8);
         return null;
      }
   }

   public static int[][] p(int[] var0, float var1, float var2) {
      int var3 = (int)Math.ceil(Math.max(var1, 1.0F));
      int var4 = var2 > 0.0F ? (int)Math.ceil(Math.clamp(var2, 1.0F, 32.0F)) : 0;
      int[] var5 = g(var0, var3 + var4 + 2);
      int[] var6 = g(var5, var4 + 1);
      int[] var7 = g(var6, var4 + 1);
      return new int[][]{g(var7, var3 + 1), var7, var6, var5};
   }

   private static int[] g(int[] var0, int var1) {
      return new int[]{var0[0] - var1, var0[1] - var1, var0[2] + 2 * var1, var0[3] + 2 * var1};
   }

   private static wd.e l(int[][] var0, int var1, ba.e var2) {
      if (var0 == null) {
         return null;
      }

      int[] var3 = var0[var1];
      return new wd.e(var3[0], var3[1], var3[2], var3[3], var2);
   }

   private void y(ba.e var1, int[] var2) {
      long var3 = c5.X();

      try {
         int[][] var5 = var2 == null ? null : p(var2, var1.lineWidth(), var1.glowing() ? var1.radius() : 0.0F);
         if (this.N) {
            this.Z(X, this.h.V, this.h.F, var1, 0.0F, 0.0F, l(var5, 0, var1));
         }

         this.Z(I, this.N ? this.h.F : this.h.V, this.M, var1, 0.0F, 0.0F, l(var5, 1, var1));
         if (var1.glowing()) {
            this.Z(f, this.M, this.h.F, var1, 1.0F, 0.0F, l(var5, 2, var1));
            this.Z(f, this.h.F, this.h.d, var1, 0.0F, 1.0F, l(var5, 3, var1));
         }

         this.Z(V, this.M, wi.p(class_310.method_1551().method_1522()), var1, 0.0F, 0.0F, l(var5, 3, var1));
         h7.G();
      } finally {
         c5.w(this.k + " gradient post", var3);
      }
   }

   private void Z(RenderPipeline var1, class_276 var2, class_276 var3, ba.e var4, float var5, float var6, wd.e var7) {
      CommandEncoder var8 = RenderSystem.getDevice().createCommandEncoder();
      MemoryStack var9 = MemoryStack.stackPush();

      try {
         ByteBuffer var10 = var9.malloc(80);
         var10.putFloat(var4.lineWidth()).putFloat(var4.scale()).putFloat(var4.fill()).putFloat(var4.outline());
         var10.putFloat(var4.entityColor() ? 1.0F : 0.0F).putFloat(var4.phase()).putFloat(var4.radius()).putFloat(var4.glowing() ? var4.intensity() : 0.0F);
         C(var10, var4.color1());
         C(var10, var4.color2());
         var10.putFloat(var5).putFloat(var6).putFloat(this.d ? 1.0F : 0.0F).putFloat(0.0F).flip();
         var8.writeToBuffer(this.P.slice(), var10);
      } catch (Throwable var16) {
         if (var9 != null) {
            try {
               var9.close();
            } catch (Throwable var15) {
               var16.addSuppressed(var15);
            }
         }

         throw var16;
      }

      if (var9 != null) {
         var9.close();
      }

      RenderPass var18 = var8.createRenderPass(() -> "Larp " + this.k + " " + var1.getLocation(), var3.method_71639(), OptionalInt.empty());

      try {
         yf.l(var18, var1);
         if (var7 != null) {
            int var19 = Math.clamp(var7.x(), 0, var3.field_1482);
            int var11 = Math.clamp(var7.y(), 0, var3.field_1481);
            int var12 = Math.clamp(var7.x() + var7.width(), var19, var3.field_1482);
            int var13 = Math.clamp(var7.y() + var7.height(), var11, var3.field_1481);
            var18.enableScissor(var19, var3.field_1481 - var13, var12 - var19, var13 - var11);
         }

         var18.setUniform("ChamsParameters", this.P.slice());
         class_12137 var20 = RenderSystem.getSamplerCache().method_75294(FilterMode.LINEAR);
         yf.R(var18, "Source", var2.method_71639(), var20);
         if (var1 == X) {
            class_12137 var21 = RenderSystem.getSamplerCache().method_75294(FilterMode.NEAREST);
            yf.R(var18, "Source", var2.method_71639(), var21);
            yf.R(var18, "ModelDepth", this.h.V.method_71640(), var21);
            yf.R(var18, "SceneDepth", h7.J(), var21);
         }

         if (var1 == V) {
            yf.R(var18, "Glow", (var4.glowing() ? this.h.d : this.M).method_71639(), var20);
         }

         var18.draw(0, 3);
         c5.V(this.k + " fullscreen draws", 1L);
      } catch (Throwable var17) {
         if (var18 != null) {
            try {
               var18.close();
            } catch (Throwable var14) {
               var17.addSuppressed(var14);
            }
         }

         throw var17;
      }

      if (var18 != null) {
         var18.close();
      }
   }

   private static void C(ByteBuffer var0, int var1) {
      var0.putFloat((var1 >> 16 & 0xFF) / 255.0F).putFloat((var1 >> 8 & 0xFF) / 255.0F).putFloat((var1 & 0xFF) / 255.0F).putFloat((var1 >>> 24) / 255.0F);
   }

   private void e(String var1, RuntimeException var2) {
      this.e = (boolean)1;
      this.close();
      q8.u(this.k, "stage=" + var1 + "; gradient pass disabled until resource reload; ordinary models retained", var2);
   }

   @Override
   public void close() {
      this.p = (boolean)0;
      this.n = (boolean)0;
      this.R = 0L;
      class_6367 var1 = this.M;
      this.M = null;
      GpuBuffer var2 = this.P;
      this.P = null;
      cw var3 = this.U;
      this.U = null;
      RuntimeException var4 = null;

      for (Runnable var8 : new Runnable[]{this.h::close, () -> {
         if (var1 != null) {
            var1.method_1238();
         }
      }, () -> {
         if (var2 != null) {
            var2.close();
         }
      }, () -> {
         if (var3 != null) {
            var3.close();
         }
      }}) {
         try {
            var8.run();
         } catch (RuntimeException var10) {
            if (var4 == null) {
               var4 = var10;
            } else {
               var4.addSuppressed(var10);
            }
         }
      }

      if (var4 != null) {
         q8.u(this.k, "stage=cleanup; all gradient resource releases attempted", var4);
      }
   }

   private static class_2960 O(String var0) {
      return class_2960.method_60655("larp-client", var0);
   }

   public record b(class_10017 state, int color) {
   }

   public record e(int x, int y, int width, int height, ba.e frame) {
   }
}
