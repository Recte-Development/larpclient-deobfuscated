package com.larpclient;

import com.mojang.blaze3d.GpuFormat;
import com.mojang.blaze3d.PrimitiveTopology;
import com.mojang.blaze3d.buffers.GpuBuffer;
import com.mojang.blaze3d.pipeline.BindGroupLayout;
import com.mojang.blaze3d.pipeline.BlendFunction;
import com.mojang.blaze3d.pipeline.ColorTargetState;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.pipeline.TextureTarget;
import com.mojang.blaze3d.pipeline.BindGroupLayout.Builder;
import com.mojang.blaze3d.pipeline.RenderPipeline.Snippet;
import com.mojang.blaze3d.shaders.UniformType;
import com.mojang.blaze3d.systems.CommandEncoder;
import com.mojang.blaze3d.systems.RenderPass;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.textures.FilterMode;
import com.mojang.blaze3d.textures.GpuSampler;
import com.mojang.blaze3d.textures.GpuTextureView;
import com.mojang.blaze3d.vertex.PoseStack;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.Optional;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.resources.Identifier;
import org.joml.Vector4f;
import org.joml.Vector4fc;
import org.lwjgl.system.MemoryStack;

public final class g1 implements AutoCloseable {
   private static final Vector4fc J = new Vector4f(0.0F);
   private static final RenderPipeline h = i("chams_edge", false);
   private static final RenderPipeline x = m();
   private static final RenderPipeline N = i("chams_blur", false);
   private static final RenderPipeline H = i("chams_composite", true);
   private static final RenderPipeline P = RenderPipeline.builder(new Snippet[0])
      .withLocation(x("chams_hand_mask"))
      .withVertexShader(x("post/fullscreen"))
      .withFragmentShader(x("post/chams_hand_mask"))
      .withBindGroupLayout(BindGroupLayout.builder().withSampler("Before").withSampler("After").withSampler("HandDepth").build())
      .withPrimitiveTopology(PrimitiveTopology.TRIANGLES)
      .withCull(false)
      .build();
   private final String v;
   private final bt m = new bt();
   private TextureTarget L;
   private GpuBuffer s;
   private hh I;
   private int X = -1;
   private boolean R;
   private boolean V;
   private boolean f;
   private boolean d;
   private boolean A;
   private boolean M;
   private static final long B = 5000000000L;
   private long b;

   public g1(String var1) {
      this.v = var1;
   }

   private static RenderPipeline m() {
      return RenderPipeline.builder(new Snippet[0])
         .withLocation(x("chams_entity_mask"))
         .withVertexShader(x("post/fullscreen"))
         .withFragmentShader(x("post/chams_entity_mask"))
         .withBindGroupLayout(
            BindGroupLayout.builder()
               .withSampler("Source")
               .withSampler("ModelDepth")
               .withSampler("SceneDepth")
               .withUniform("ChamsParameters", UniformType.UNIFORM_BUFFER)
               .build()
         )
         .withPrimitiveTopology(PrimitiveTopology.TRIANGLES)
         .withColorTargetState(ColorTargetState.DEFAULT)
         .withCull(false)
         .build();
   }

   private static RenderPipeline i(String var0, boolean var1) {
      Builder var2 = BindGroupLayout.builder().withSampler("Source").withUniform("ChamsParameters", UniformType.UNIFORM_BUFFER);
      if (var1) {
         var2.withSampler("Glow");
      }

      return RenderPipeline.builder(new Snippet[0])
         .withLocation(x(var0))
         .withVertexShader(x("post/fullscreen"))
         .withFragmentShader(x("post/" + var0))
         .withBindGroupLayout(var2.build())
         .withPrimitiveTopology(PrimitiveTopology.TRIANGLES)
         .withColorTargetState(var1 ? new ColorTargetState(BlendFunction.TRANSLUCENT) : ColorTargetState.DEFAULT)
         .withCull(false)
         .build();
   }

   private boolean C() {
      int var1 = RenderSystem.outputColorTextureOverride == null && RenderSystem.outputDepthTextureOverride == null ? 1 : 0;
      if (var1 == 0 && !this.V) {
         this.V = (boolean)1;
         com.larpclient.h.g(this.v, "Gradient capture skipped while another renderer owns the output attachments");
      }

      return (boolean)var1;
   }

   private boolean L() {
      if (this.X != wa.s) {
         this.close();
         this.X = wa.s;
         this.R = (boolean)0;
      }

      if (!this.R && this.C()) {
         try {
            RenderTarget var1 = Minecraft.getInstance().gameRenderer.mainRenderTarget();
            if (this.L != null && (this.L.width != var1.width || this.L.height != var1.height)) {
               this.close();
            }

            if (this.L == null) {
               ai.U(h);
               ai.U(x);
               ai.U(N);
               ai.U(H);
               this.m.V(var1.width, var1.height, 1, bt.p.F);
               this.L = mx.n("Larp " + this.v + " gradient", var1.width, var1.height, false, GpuFormat.RGBA8_UNORM);
               this.s = RenderSystem.getDevice().createBuffer(() -> "Larp " + this.v + " gradient parameters", 136, 80L);
               com.larpclient.h.A(this.v, "Gradient pass initialized {}x{}; four full-resolution targets", var1.width, var1.height);
            }

            return true;
         } catch (RuntimeException var2) {
            this.R("allocate/check", var2);
            return false;
         }
      } else {
         return false;
      }
   }

   public void n(List<g1.u> var1, PoseStack var2, bl.d var3, boolean var4) {
      if (var1.isEmpty()) {
         long var22 = System.nanoTime();
         if (this.b == 0L) {
            this.b = var22;
         } else if (var22 - this.b > 5000000000L) {
            this.close();
         }
      } else {
         this.b = 0L;
         if (this.L()) {
            GpuTextureView var5 = RenderSystem.outputColorTextureOverride;
            GpuTextureView var6 = RenderSystem.outputDepthTextureOverride;

            try {
               this.A = (boolean)1;
               this.M = var4;
               if (so.v() == null) {
                  throw new IllegalStateException("scene-depth capture unavailable");
               }

               if (this.I == null) {
                  this.I = new hh();
               }

               CommandEncoder var7 = RenderSystem.getDevice().createCommandEncoder();
               var7.clearColorTexture(this.m.I.getColorTexture(), J);
               var7.clearDepthTexture(this.m.I.getDepthTexture(), 0.0);
               RenderSystem.outputColorTextureOverride = this.m.I.getColorTextureView();
               RenderSystem.outputDepthTextureOverride = this.m.I.getDepthTextureView();

               try {
                  j4.a(this.m.I.width, this.m.I.height);

                  for (g1.u var10 : var1) {
                     j4.b(var10.state, var2, this.I, j4.j.h, var10.color, 1.0, false, false);
                  }

                  this.I.K();
               } finally {
                  int[] var8 = j4.m();
                  RenderSystem.outputColorTextureOverride = var5;
                  RenderSystem.outputDepthTextureOverride = var6;
               }

               Object var23;
               if (var23 == null || ((Object[])var23)[2] > 0) {
                  this.M(var3, (int[])var23);
               }
            } catch (RuntimeException var20) {
               this.R("entity mask/composite", var20);
            } finally {
               RenderSystem.outputColorTextureOverride = var5;
               RenderSystem.outputDepthTextureOverride = var6;
               if (this.I != null) {
                  this.I.g();
               }

               this.A = (boolean)0;
            }
         }
      }
   }

   public boolean D() {
      this.f = (boolean)0;
      if (!this.L()) {
         return false;
      }

      try {
         if (!this.d) {
            ai.U(P);
            this.d = (boolean)1;
         }

         wa.Y(ai.C, p2.d(Minecraft.getInstance().gameRenderer.mainRenderTarget()), this.L);
         this.f = (boolean)1;
         return true;
      } catch (RuntimeException var2) {
         this.R("hand scene capture", var2);
         return false;
      }
   }

   public void a(bl.d var1) {
      RenderTarget var2 = this.N();
      if (var2 != null) {
         try {
            this.M(var1, null);
         } catch (RuntimeException var4) {
            this.R("hand mask/composite", var4);
         }
      }
   }

   public void Z(List<g1.b> var1) {
      RenderTarget var2 = this.N();
      if (var2 != null) {
         long var3 = zj.m();

         try {
            for (g1.b var6 : var1) {
               this.t(h, this.m.I, this.L, var6.frame(), 0.0F, 0.0F, var6);
               this.t(H, this.L, var2, var6.frame(), 0.0F, 0.0F, var6);
            }

            so.K();
         } catch (RuntimeException var10) {
            this.R("hand region composite", var10);
         } finally {
            zj.l(this.v + " gradient post", var3);
         }
      }
   }

   private RenderTarget N() {
      if (!this.f) {
         return null;
      }

      this.f = (boolean)0;
      if (!this.C()) {
         this.close();
         return null;
      }

      try {
         RenderTarget var1 = Minecraft.getInstance().gameRenderer.mainRenderTarget();
         if (var1.width == this.L.width && var1.height == this.L.height) {
            CommandEncoder var2 = RenderSystem.getDevice().createCommandEncoder();
            RenderPass var3 = var2.createRenderPass(() -> "Larp " + this.v + " silhouette", this.m.I.getColorTextureView(), Optional.empty());

            try {
               mx.i(var3, P);
               GpuSampler var4 = RenderSystem.getSamplerCache().getClampToEdge(FilterMode.LINEAR);
               mx.x(var3, "Before", this.L.getColorTextureView(), var4);
               mx.x(var3, "After", p2.d(var1).getColorTextureView(), var4);
               mx.x(var3, "HandDepth", var1.getDepthTextureView(), RenderSystem.getSamplerCache().getClampToEdge(FilterMode.NEAREST));
               var3.draw(3, 1, 0, 0);
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
         this.R("hand mask", var8);
         return null;
      }
   }

   public static int[][] f(int[] var0, float var1, float var2) {
      int var3 = (int)Math.ceil(Math.max(var1, 1.0F));
      int var4 = var2 > 0.0F ? (int)Math.ceil(Math.clamp(var2, 1.0F, 32.0F)) : 0;
      int[] var5 = N(var0, var3 + var4 + 2);
      int[] var6 = N(var5, var4 + 1);
      int[] var7 = N(var6, var4 + 1);
      return new int[][]{N(var7, var3 + 1), var7, var6, var5};
   }

   private static int[] N(int[] var0, int var1) {
      return new int[]{var0[0] - var1, var0[1] - var1, var0[2] + 2 * var1, var0[3] + 2 * var1};
   }

   private static g1.b P(int[][] var0, int var1, bl.d var2) {
      if (var0 == null) {
         return null;
      }

      int[] var3 = var0[var1];
      return new g1.b(var3[0], var3[1], var3[2], var3[3], var2);
   }

   private void M(bl.d var1, int[] var2) {
      long var3 = zj.m();

      try {
         int[][] var5 = var2 == null ? null : f(var2, var1.lineWidth(), var1.glowing() ? var1.radius() : 0.0F);
         if (this.A) {
            this.t(x, this.m.I, this.m.M, var1, 0.0F, 0.0F, P(var5, 0, var1));
         }

         this.t(h, this.A ? this.m.M : this.m.I, this.L, var1, 0.0F, 0.0F, P(var5, 1, var1));
         if (var1.glowing()) {
            this.t(N, this.L, this.m.M, var1, 1.0F, 0.0F, P(var5, 2, var1));
            this.t(N, this.m.M, this.m.D, var1, 0.0F, 1.0F, P(var5, 3, var1));
         }

         this.t(H, this.L, p2.Q(Minecraft.getInstance().gameRenderer.mainRenderTarget()), var1, 0.0F, 0.0F, P(var5, 3, var1));
         so.K();
      } finally {
         zj.l(this.v + " gradient post", var3);
      }
   }

   private void t(RenderPipeline var1, RenderTarget var2, RenderTarget var3, bl.d var4, float var5, float var6, g1.b var7) {
      CommandEncoder var8 = RenderSystem.getDevice().createCommandEncoder();
      MemoryStack var9 = MemoryStack.stackPush();

      try {
         ByteBuffer var10 = var9.malloc(80);
         var10.putFloat(var4.lineWidth()).putFloat(var4.scale()).putFloat(var4.fill()).putFloat(var4.outline());
         var10.putFloat(var4.entityColor() ? 1.0F : 0.0F).putFloat(var4.phase()).putFloat(var4.radius()).putFloat(var4.glowing() ? var4.intensity() : 0.0F);
         R(var10, var4.color1());
         R(var10, var4.color2());
         var10.putFloat(var5).putFloat(var6).putFloat(this.M ? 1.0F : 0.0F).putFloat(0.0F).flip();
         var8.writeToBuffer(this.s.slice(), var10);
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

      RenderPass var18 = var8.createRenderPass(() -> "Larp " + this.v + " " + var1.getLocation(), var3.getColorTextureView(), Optional.empty());

      try {
         mx.i(var18, var1);
         if (var7 != null) {
            int var19 = Math.clamp(var7.x(), 0, var3.width);
            int var11 = Math.clamp(var7.y(), 0, var3.height);
            int var12 = Math.clamp(var7.x() + var7.width(), var19, var3.width);
            int var13 = Math.clamp(var7.y() + var7.height(), var11, var3.height);
            var18.enableScissor(var19, var3.height - var13, var12 - var19, var13 - var11);
         }

         var18.setUniform("ChamsParameters", this.s.slice());
         GpuSampler var20 = RenderSystem.getSamplerCache().getClampToEdge(FilterMode.LINEAR);
         mx.x(var18, "Source", var2.getColorTextureView(), var20);
         if (var1 == x) {
            GpuSampler var21 = RenderSystem.getSamplerCache().getClampToEdge(FilterMode.NEAREST);
            mx.x(var18, "Source", var2.getColorTextureView(), var21);
            mx.x(var18, "ModelDepth", this.m.I.getDepthTextureView(), var21);
            mx.x(var18, "SceneDepth", so.v(), var21);
         }

         if (var1 == H) {
            mx.x(var18, "Glow", (var4.glowing() ? this.m.D : this.L).getColorTextureView(), var20);
         }

         var18.draw(3, 1, 0, 0);
         zj.K(this.v + " fullscreen draws", 1L);
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

   private static void R(ByteBuffer var0, int var1) {
      var0.putFloat((var1 >> 16 & 0xFF) / 255.0F).putFloat((var1 >> 8 & 0xFF) / 255.0F).putFloat((var1 & 0xFF) / 255.0F).putFloat((var1 >>> 24) / 255.0F);
   }

   private void R(String var1, RuntimeException var2) {
      this.R = (boolean)1;
      this.close();
      com.larpclient.h.m(this.v, "stage=" + var1 + "; gradient pass disabled until resource reload; ordinary models retained", var2);
   }

   @Override
   public void close() {
      this.f = (boolean)0;
      this.d = (boolean)0;
      this.b = 0L;
      TextureTarget var1 = this.L;
      this.L = null;
      GpuBuffer var2 = this.s;
      this.s = null;
      hh var3 = this.I;
      this.I = null;
      RuntimeException var4 = null;

      for (Runnable var8 : new Runnable[]{this.m::close, () -> {
         if (var1 != null) {
            var1.destroyBuffers();
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
         com.larpclient.h.m(this.v, "stage=cleanup; all gradient resource releases attempted", var4);
      }
   }

   private static Identifier x(String var0) {
      return Identifier.fromNamespaceAndPath("larp-client", var0);
   }

   public record b(int x, int y, int width, int height, bl.d frame) {
   }

   public record u(EntityRenderState state, int color) {
   }
}
