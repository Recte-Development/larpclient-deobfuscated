package com.larpclient;

import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.pipeline.TextureTarget;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.renderpearl.api.GpuFormat;
import com.mojang.renderpearl.api.buffers.GpuBuffer;
import com.mojang.renderpearl.api.commands.CommandEncoder;
import com.mojang.renderpearl.api.commands.RenderPass;
import com.mojang.renderpearl.api.pipeline.BindGroupLayout;
import com.mojang.renderpearl.api.pipeline.BlendFunction;
import com.mojang.renderpearl.api.pipeline.ColorTargetState;
import com.mojang.renderpearl.api.pipeline.PrimitiveTopology;
import com.mojang.renderpearl.api.pipeline.RenderPipeline;
import com.mojang.renderpearl.api.pipeline.UniformType;
import com.mojang.renderpearl.api.pipeline.BindGroupLayout.Builder;
import com.mojang.renderpearl.api.pipeline.RenderPipeline.Snippet;
import com.mojang.renderpearl.api.textures.FilterMode;
import com.mojang.renderpearl.api.textures.GpuSampler;
import com.mojang.renderpearl.api.textures.GpuTextureView;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.Optional;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.resources.Identifier;
import org.joml.Vector4f;
import org.joml.Vector4fc;
import org.lwjgl.system.MemoryStack;

public final class i4 implements AutoCloseable {
   private static final Vector4fc L = new Vector4f(0.0F);
   private static final RenderPipeline S = F("chams_edge", false);
   private static final RenderPipeline A = w();
   private static final RenderPipeline f = F("chams_blur", false);
   private static final RenderPipeline W = F("chams_composite", true);
   private static final RenderPipeline d = RenderPipeline.builder(new Snippet[0])
      .withLocation(z("chams_hand_mask"))
      .withVertexShader(z("post/fullscreen"))
      .withFragmentShader(z("post/chams_hand_mask"))
      .withBindGroupLayout(BindGroupLayout.builder().withUniform("Before", p7.X).withUniform("After", p7.X).withUniform("HandDepth", p7.X).build())
      .withPrimitiveTopology(PrimitiveTopology.TRIANGLES)
      .withCull(false)
      .build();
   private final String R;
   private final bk I = new bk();
   private TextureTarget o;
   private GpuBuffer k;
   private wh h;
   private int N = -1;
   private boolean O;
   private boolean M;
   private boolean D;
   private boolean u;
   private boolean P;
   private boolean z;
   private static final long x = 5000000000L;
   private long E;

   public i4(String var1) {
      this.R = var1;
   }

   private static RenderPipeline w() {
      return RenderPipeline.builder(new Snippet[0])
         .withLocation(z("chams_entity_mask"))
         .withVertexShader(z("post/fullscreen"))
         .withFragmentShader(z("post/chams_entity_mask"))
         .withBindGroupLayout(
            BindGroupLayout.builder()
               .withUniform("Source", p7.X)
               .withUniform("ModelDepth", p7.X)
               .withUniform("SceneDepth", p7.X)
               .withUniform("ChamsParameters", UniformType.UNIFORM_BUFFER)
               .build()
         )
         .withPrimitiveTopology(PrimitiveTopology.TRIANGLES)
         .withColorTargetState(ColorTargetState.DEFAULT)
         .withCull(false)
         .build();
   }

   private static RenderPipeline F(String var0, boolean var1) {
      Builder var2 = BindGroupLayout.builder().withUniform("Source", p7.X).withUniform("ChamsParameters", UniformType.UNIFORM_BUFFER);
      if (var1) {
         var2.withUniform("Glow", p7.X);
      }

      return RenderPipeline.builder(new Snippet[0])
         .withLocation(z(var0))
         .withVertexShader(z("post/fullscreen"))
         .withFragmentShader(z("post/" + var0))
         .withBindGroupLayout(var2.build())
         .withPrimitiveTopology(PrimitiveTopology.TRIANGLES)
         .withColorTargetState(var1 ? new ColorTargetState(BlendFunction.TRANSLUCENT) : ColorTargetState.DEFAULT)
         .withCull(false)
         .build();
   }

   private boolean l() {
      int var1 = jf.b == null && jf.q == null ? 1 : 0;
      if (var1 == 0 && !this.M) {
         this.M = (boolean)1;
         _q.R(this.R, "Gradient capture skipped while another renderer owns the output attachments");
      }

      return (boolean)var1;
   }

   private boolean V() {
      if (this.N != nl.q) {
         this.close();
         this.N = nl.q;
         this.O = (boolean)0;
      }

      if (!this.O && this.l()) {
         try {
            RenderTarget var1 = Minecraft.getInstance().gameRenderer.mainRenderTarget();
            if (this.o != null && (this.o.width != var1.width || this.o.height != var1.height)) {
               this.close();
            }

            if (this.o == null) {
               se.j(S);
               se.j(A);
               se.j(f);
               se.j(W);
               this.I.l(var1.width, var1.height, 1, bk.p.v);
               this.o = p7.m("Larp " + this.R + " gradient", var1.width, var1.height, false, GpuFormat.RGBA8_UNORM);
               this.k = RenderSystem.getDevice().createBuffer(() -> "Larp " + this.R + " gradient parameters", 136, 80L);
               _q.z(this.R, "Gradient pass initialized {}x{}; four full-resolution targets", var1.width, var1.height);
            }

            return true;
         } catch (RuntimeException var2) {
            this.o("allocate/check", var2);
            return false;
         }
      } else {
         return false;
      }
   }

   public void k(List<i4.w> var1, PoseStack var2, q6.u var3, boolean var4) {
      if (var1.isEmpty()) {
         long var22 = System.nanoTime();
         if (this.E == 0L) {
            this.E = var22;
         } else if (var22 - this.E > 5000000000L) {
            this.close();
         }
      } else {
         this.E = 0L;
         if (this.V()) {
            GpuTextureView var5 = jf.b;
            GpuTextureView var6 = jf.q;

            try {
               this.P = (boolean)1;
               this.z = var4;
               if (_s.a() == null) {
                  throw new IllegalStateException("scene-depth capture unavailable");
               }

               if (this.h == null) {
                  this.h = new wh();
               }

               CommandEncoder var7 = RenderSystem.getDevice().createCommandEncoder();
               var7.clearColorTexture(this.I.Y.getColorTexture(), L);
               var7.clearDepthTexture(this.I.Y.getDepthTexture(), 0.0);
               jf.b = this.I.Y.getColorTextureView();
               jf.q = this.I.Y.getDepthTextureView();

               try {
                  w7.q(this.I.Y.width, this.I.Y.height);

                  for (i4.w var10 : var1) {
                     w7.r(var10.state, var2, this.h, w7.b.c, var10.color, 1.0, false, false);
                  }

                  this.h.S();
               } finally {
                  int[] var8 = w7.L();
                  jf.b = var5;
                  jf.q = var6;
               }

               Object var23;
               if (var23 == null || ((Object[])var23)[2] > 0) {
                  this.K(var3, (int[])var23);
               }
            } catch (RuntimeException var20) {
               this.o("entity mask/composite", var20);
            } finally {
               jf.b = var5;
               jf.q = var6;
               if (this.h != null) {
                  this.h.A();
               }

               this.P = (boolean)0;
            }
         }
      }
   }

   public boolean d() {
      this.D = (boolean)0;
      if (!this.V()) {
         return false;
      }

      try {
         if (!this.u) {
            se.j(d);
            this.u = (boolean)1;
         }

         nl.G(se.j, qc.A(Minecraft.getInstance().gameRenderer.mainRenderTarget()), this.o);
         this.D = (boolean)1;
         return true;
      } catch (RuntimeException var2) {
         this.o("hand scene capture", var2);
         return false;
      }
   }

   public void r(q6.u var1) {
      RenderTarget var2 = this.c();
      if (var2 != null) {
         try {
            this.K(var1, null);
         } catch (RuntimeException var4) {
            this.o("hand mask/composite", var4);
         }
      }
   }

   public void F(List<i4.q> var1) {
      RenderTarget var2 = this.c();
      if (var2 != null) {
         long var3 = he.s();

         try {
            for (i4.q var6 : var1) {
               this.z(S, this.I.Y, this.o, var6.frame(), 0.0F, 0.0F, var6);
               this.z(W, this.o, var2, var6.frame(), 0.0F, 0.0F, var6);
            }

            _s.g();
         } catch (RuntimeException var10) {
            this.o("hand region composite", var10);
         } finally {
            he.T(this.R + " gradient post", var3);
         }
      }
   }

   private RenderTarget c() {
      if (!this.D) {
         return null;
      }

      this.D = (boolean)0;
      if (!this.l()) {
         this.close();
         return null;
      }

      try {
         RenderTarget var1 = Minecraft.getInstance().gameRenderer.mainRenderTarget();
         if (var1.width == this.o.width && var1.height == this.o.height) {
            CommandEncoder var2 = RenderSystem.getDevice().createCommandEncoder();
            RenderPass var3 = var2.createRenderPass(() -> "Larp " + this.R + " silhouette", this.I.Y.getColorTextureView(), Optional.empty());

            try {
               p7.K(var3, d);
               GpuSampler var4 = RenderSystem.getSamplerCache().getClampToEdge(FilterMode.LINEAR);
               p7.t(var3, "Before", this.o.getColorTextureView(), var4);
               p7.t(var3, "After", qc.A(var1).getColorTextureView(), var4);
               p7.t(var3, "HandDepth", var1.getDepthTextureView(), RenderSystem.getSamplerCache().getClampToEdge(FilterMode.NEAREST));
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
         this.o("hand mask", var8);
         return null;
      }
   }

   public static int[][] U(int[] var0, float var1, float var2) {
      int var3 = (int)Math.ceil(Math.max(var1, 1.0F));
      int var4 = var2 > 0.0F ? (int)Math.ceil(Math.clamp(var2, 1.0F, 32.0F)) : 0;
      int[] var5 = C(var0, var3 + var4 + 2);
      int[] var6 = C(var5, var4 + 1);
      int[] var7 = C(var6, var4 + 1);
      return new int[][]{C(var7, var3 + 1), var7, var6, var5};
   }

   private static int[] C(int[] var0, int var1) {
      return new int[]{var0[0] - var1, var0[1] - var1, var0[2] + 2 * var1, var0[3] + 2 * var1};
   }

   private static i4.q E(int[][] var0, int var1, q6.u var2) {
      if (var0 == null) {
         return null;
      }

      int[] var3 = var0[var1];
      return new i4.q(var3[0], var3[1], var3[2], var3[3], var2);
   }

   private void K(q6.u var1, int[] var2) {
      long var3 = he.s();

      try {
         int[][] var5 = var2 == null ? null : U(var2, var1.lineWidth(), var1.glowing() ? var1.radius() : 0.0F);
         if (this.P) {
            this.z(A, this.I.Y, this.I.B, var1, 0.0F, 0.0F, E(var5, 0, var1));
         }

         this.z(S, this.P ? this.I.B : this.I.Y, this.o, var1, 0.0F, 0.0F, E(var5, 1, var1));
         if (var1.glowing()) {
            this.z(f, this.o, this.I.B, var1, 1.0F, 0.0F, E(var5, 2, var1));
            this.z(f, this.I.B, this.I.w, var1, 0.0F, 1.0F, E(var5, 3, var1));
         }

         this.z(W, this.o, qc.Y(Minecraft.getInstance().gameRenderer.mainRenderTarget()), var1, 0.0F, 0.0F, E(var5, 3, var1));
         _s.g();
      } finally {
         he.T(this.R + " gradient post", var3);
      }
   }

   private void z(RenderPipeline var1, RenderTarget var2, RenderTarget var3, q6.u var4, float var5, float var6, i4.q var7) {
      CommandEncoder var8 = RenderSystem.getDevice().createCommandEncoder();
      MemoryStack var9 = MemoryStack.stackPush();

      try {
         ByteBuffer var10 = var9.malloc(80);
         var10.putFloat(var4.lineWidth()).putFloat(var4.scale()).putFloat(var4.fill()).putFloat(var4.outline());
         var10.putFloat(var4.entityColor() ? 1.0F : 0.0F).putFloat(var4.phase()).putFloat(var4.radius()).putFloat(var4.glowing() ? var4.intensity() : 0.0F);
         s(var10, var4.color1());
         s(var10, var4.color2());
         var10.putFloat(var5).putFloat(var6).putFloat(this.z ? 1.0F : 0.0F).putFloat(0.0F).flip();
         var8.writeToBuffer(this.k.slice(), var10);
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

      RenderPass var18 = var8.createRenderPass(() -> "Larp " + this.R + " " + var1.getLocation(), var3.getColorTextureView(), Optional.empty());

      try {
         p7.K(var18, var1);
         if (var7 != null) {
            int var19 = Math.clamp(var7.x(), 0, var3.width);
            int var11 = Math.clamp(var7.y(), 0, var3.height);
            int var12 = Math.clamp(var7.x() + var7.width(), var19, var3.width);
            int var13 = Math.clamp(var7.y() + var7.height(), var11, var3.height);
            var18.enableScissor(var19, var3.height - var13, var12 - var19, var13 - var11);
         }

         var18.setUniform("ChamsParameters", this.k.slice());
         GpuSampler var20 = RenderSystem.getSamplerCache().getClampToEdge(FilterMode.LINEAR);
         p7.t(var18, "Source", var2.getColorTextureView(), var20);
         if (var1 == A) {
            GpuSampler var21 = RenderSystem.getSamplerCache().getClampToEdge(FilterMode.NEAREST);
            p7.t(var18, "Source", var2.getColorTextureView(), var21);
            p7.t(var18, "ModelDepth", this.I.Y.getDepthTextureView(), var21);
            p7.t(var18, "SceneDepth", _s.a(), var21);
         }

         if (var1 == W) {
            p7.t(var18, "Glow", (var4.glowing() ? this.I.w : this.o).getColorTextureView(), var20);
         }

         var18.draw(3, 1, 0, 0);
         he.Z(this.R + " fullscreen draws", 1L);
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

   private static void s(ByteBuffer var0, int var1) {
      var0.putFloat((var1 >> 16 & 0xFF) / 255.0F).putFloat((var1 >> 8 & 0xFF) / 255.0F).putFloat((var1 & 0xFF) / 255.0F).putFloat((var1 >>> 24) / 255.0F);
   }

   private void o(String var1, RuntimeException var2) {
      this.O = (boolean)1;
      this.close();
      _q.o(this.R, "stage=" + var1 + "; gradient pass disabled until resource reload; ordinary models retained", var2);
   }

   @Override
   public void close() {
      this.D = (boolean)0;
      this.u = (boolean)0;
      this.E = 0L;
      TextureTarget var1 = this.o;
      this.o = null;
      GpuBuffer var2 = this.k;
      this.k = null;
      wh var3 = this.h;
      this.h = null;
      RuntimeException var4 = null;

      for (Runnable var8 : new Runnable[]{this.I::close, () -> {
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
         _q.o(this.R, "stage=cleanup; all gradient resource releases attempted", var4);
      }
   }

   private static Identifier z(String var0) {
      return Identifier.fromNamespaceAndPath("larp-client", var0);
   }

   public record q(int x, int y, int width, int height, q6.u frame) {
   }

   public record w(EntityRenderState state, int color) {
   }
}
