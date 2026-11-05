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

public final class fy implements AutoCloseable {
   private static final Vector4fc i = new Vector4f(0.0F);
   private static final RenderPipeline t = g("chams_edge", false);
   private static final RenderPipeline m = l();
   private static final RenderPipeline S = g("chams_blur", false);
   private static final RenderPipeline M = g("chams_composite", true);
   private static final RenderPipeline V = RenderPipeline.builder(new Snippet[0])
      .withLocation(c("chams_hand_mask"))
      .withVertexShader(c("post/fullscreen"))
      .withFragmentShader(c("post/chams_hand_mask"))
      .withBindGroupLayout(BindGroupLayout.builder().withUniform("Before", of.t).withUniform("After", of.t).withUniform("HandDepth", of.t).build())
      .withPrimitiveTopology(PrimitiveTopology.TRIANGLES)
      .withCull(false)
      .build();
   private final String N;
   private final ld W = new ld();
   private TextureTarget x;
   private GpuBuffer o;
   private y2 T;
   private int Z = -1;
   private boolean a;
   private boolean j;
   private boolean e;
   private boolean q;
   private boolean y;
   private boolean C;

   public fy(String var1) {
      this.N = var1;
   }

   private static RenderPipeline l() {
      return RenderPipeline.builder(new Snippet[0])
         .withLocation(c("chams_entity_mask"))
         .withVertexShader(c("post/fullscreen"))
         .withFragmentShader(c("post/chams_entity_mask"))
         .withBindGroupLayout(
            BindGroupLayout.builder()
               .withUniform("Source", of.t)
               .withUniform("ModelDepth", of.t)
               .withUniform("SceneDepth", of.t)
               .withUniform("ChamsParameters", UniformType.UNIFORM_BUFFER)
               .build()
         )
         .withPrimitiveTopology(PrimitiveTopology.TRIANGLES)
         .withColorTargetState(ColorTargetState.DEFAULT)
         .withCull(false)
         .build();
   }

   private static RenderPipeline g(String var0, boolean var1) {
      Builder var2 = BindGroupLayout.builder().withUniform("Source", of.t).withUniform("ChamsParameters", UniformType.UNIFORM_BUFFER);
      if (var1) {
         var2.withUniform("Glow", of.t);
      }

      return RenderPipeline.builder(new Snippet[0])
         .withLocation(c(var0))
         .withVertexShader(c("post/fullscreen"))
         .withFragmentShader(c("post/" + var0))
         .withBindGroupLayout(var2.build())
         .withPrimitiveTopology(PrimitiveTopology.TRIANGLES)
         .withColorTargetState(var1 ? new ColorTargetState(BlendFunction.TRANSLUCENT) : ColorTargetState.DEFAULT)
         .withCull(false)
         .build();
   }

   private boolean W() {
      int var1 = _m.M == null && _m.U == null ? 1 : 0;
      if (var1 == 0 && !this.j) {
         this.j = (boolean)1;
         r4.m(this.N, "Gradient capture skipped while another renderer owns the output attachments");
      }

      return (boolean)var1;
   }

   private boolean o() {
      if (this.Z != jk.v) {
         this.close();
         this.Z = jk.v;
         this.a = (boolean)0;
      }

      if (!this.a && this.W()) {
         try {
            RenderTarget var1 = Minecraft.getInstance().gameRenderer.mainRenderTarget();
            if (this.x != null && (this.x.width != var1.width || this.x.height != var1.height)) {
               this.close();
            }

            if (this.x == null) {
               e4.U(t);
               e4.U(m);
               e4.U(S);
               e4.U(M);
               this.W.m(var1.width, var1.height, 1, ld.y.c);
               this.x = of.T("Larp " + this.N + " gradient", var1.width, var1.height, false, GpuFormat.RGBA8_UNORM);
               this.o = RenderSystem.getDevice().createBuffer(() -> "Larp " + this.N + " gradient parameters", 136, 80L);
               r4.V(this.N, "Gradient pass initialized {}x{}; four full-resolution targets", var1.width, var1.height);
            }

            return true;
         } catch (RuntimeException var2) {
            this.S("allocate/check", var2);
            return false;
         }
      } else {
         return false;
      }
   }

   public void Q(List<fy.a> var1, PoseStack var2, rg.q var3, boolean var4) {
      if (var1.isEmpty()) {
         this.close();
      } else if (this.o()) {
         GpuTextureView var5 = _m.M;
         GpuTextureView var6 = _m.U;

         try {
            this.y = (boolean)1;
            this.C = var4;
            if (x1.z() == null) {
               throw new IllegalStateException("scene-depth capture unavailable");
            }

            if (this.T == null) {
               this.T = new y2();
            }

            CommandEncoder var7 = RenderSystem.getDevice().createCommandEncoder();
            var7.clearColorTexture(this.W.B.getColorTexture(), i);
            var7.clearDepthTexture(this.W.B.getDepthTexture(), 0.0);
            _m.M = this.W.B.getColorTextureView();
            _m.U = this.W.B.getDepthTextureView();

            try {
               for (fy.a var9 : var1) {
                  uv.V(var9.state, var2, this.T, uv.w.v, var9.color, 1.0, false, false);
               }

               this.T.v();
            } finally {
               _m.M = var5;
               _m.U = var6;
            }

            this.y(var3);
         } catch (RuntimeException var19) {
            this.S("entity mask/composite", var19);
         } finally {
            _m.M = var5;
            _m.U = var6;
            if (this.T != null) {
               this.T.R();
            }

            this.y = (boolean)0;
         }
      }
   }

   public boolean j() {
      this.e = (boolean)0;
      if (!this.o()) {
         return false;
      }

      try {
         if (!this.q) {
            e4.U(V);
            this.q = (boolean)1;
         }

         jk.y(e4.g, y8.s(Minecraft.getInstance().gameRenderer.mainRenderTarget()), this.x);
         this.e = (boolean)1;
         return true;
      } catch (RuntimeException var2) {
         this.S("hand scene capture", var2);
         return false;
      }
   }

   public void W(rg.q var1) {
      RenderTarget var2 = this.h();
      if (var2 != null) {
         try {
            this.y(var1);
         } catch (RuntimeException var4) {
            this.S("hand mask/composite", var4);
         }
      }
   }

   public void l(List<fy.j> var1) {
      RenderTarget var2 = this.h();
      if (var2 != null) {
         long var3 = h_.U();

         try {
            for (fy.j var6 : var1) {
               this.b(t, this.W.B, this.x, var6.frame(), 0.0F, 0.0F, var6);
               this.b(M, this.x, var2, var6.frame(), 0.0F, 0.0F, var6);
            }

            x1.N();
         } catch (RuntimeException var10) {
            this.S("hand region composite", var10);
         } finally {
            h_.n(this.N + " gradient post", var3);
         }
      }
   }

   private RenderTarget h() {
      if (!this.e) {
         return null;
      }

      this.e = (boolean)0;
      if (!this.W()) {
         this.close();
         return null;
      }

      try {
         RenderTarget var1 = Minecraft.getInstance().gameRenderer.mainRenderTarget();
         if (var1.width == this.x.width && var1.height == this.x.height) {
            CommandEncoder var2 = RenderSystem.getDevice().createCommandEncoder();
            RenderPass var3 = var2.createRenderPass(() -> "Larp " + this.N + " silhouette", this.W.B.getColorTextureView(), Optional.empty());

            try {
               of.z(var3, V);
               GpuSampler var4 = RenderSystem.getSamplerCache().getClampToEdge(FilterMode.LINEAR);
               of.h(var3, "Before", this.x.getColorTextureView(), var4);
               of.h(var3, "After", y8.s(var1).getColorTextureView(), var4);
               of.h(var3, "HandDepth", var1.getDepthTextureView(), RenderSystem.getSamplerCache().getClampToEdge(FilterMode.NEAREST));
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
         this.S("hand mask", var8);
         return null;
      }
   }

   private void y(rg.q var1) {
      long var2 = h_.U();

      try {
         if (this.y) {
            this.u(m, this.W.B, this.W.U, var1, 0.0F, 0.0F);
         }

         this.u(t, this.y ? this.W.U : this.W.B, this.x, var1, 0.0F, 0.0F);
         if (var1.glowing()) {
            this.u(S, this.x, this.W.U, var1, 1.0F, 0.0F);
            this.u(S, this.W.U, this.W.H, var1, 0.0F, 1.0F);
         }

         this.u(M, this.x, y8.m(Minecraft.getInstance().gameRenderer.mainRenderTarget()), var1, 0.0F, 0.0F);
         x1.N();
      } finally {
         h_.n(this.N + " gradient post", var2);
      }
   }

   private void u(RenderPipeline var1, RenderTarget var2, RenderTarget var3, rg.q var4, float var5, float var6) {
      this.b(var1, var2, var3, var4, var5, var6, null);
   }

   private void b(RenderPipeline var1, RenderTarget var2, RenderTarget var3, rg.q var4, float var5, float var6, fy.j var7) {
      CommandEncoder var8 = RenderSystem.getDevice().createCommandEncoder();
      MemoryStack var9 = MemoryStack.stackPush();

      try {
         ByteBuffer var10 = var9.malloc(80);
         var10.putFloat(var4.lineWidth()).putFloat(var4.scale()).putFloat(var4.fill()).putFloat(var4.outline());
         var10.putFloat(var4.entityColor() ? 1.0F : 0.0F).putFloat(var4.phase()).putFloat(var4.radius()).putFloat(var4.glowing() ? var4.intensity() : 0.0F);
         W(var10, var4.color1());
         W(var10, var4.color2());
         var10.putFloat(var5).putFloat(var6).putFloat(this.C ? 1.0F : 0.0F).putFloat(0.0F).flip();
         var8.writeToBuffer(this.o.slice(), var10);
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

      RenderPass var18 = var8.createRenderPass(() -> "Larp " + this.N + " " + var1.getLocation(), var3.getColorTextureView(), Optional.empty());

      try {
         of.z(var18, var1);
         if (var7 != null) {
            int var19 = Math.clamp(var7.x(), 0, var3.width);
            int var11 = Math.clamp(var7.y(), 0, var3.height);
            int var12 = Math.clamp(var7.x() + var7.width(), var19, var3.width);
            int var13 = Math.clamp(var7.y() + var7.height(), var11, var3.height);
            var18.enableScissor(var19, var3.height - var13, var12 - var19, var13 - var11);
         }

         var18.setUniform("ChamsParameters", this.o.slice());
         GpuSampler var20 = RenderSystem.getSamplerCache().getClampToEdge(FilterMode.LINEAR);
         of.h(var18, "Source", var2.getColorTextureView(), var20);
         if (var1 == m) {
            GpuSampler var21 = RenderSystem.getSamplerCache().getClampToEdge(FilterMode.NEAREST);
            of.h(var18, "Source", var2.getColorTextureView(), var21);
            of.h(var18, "ModelDepth", this.W.B.getDepthTextureView(), var21);
            of.h(var18, "SceneDepth", x1.z(), var21);
         }

         if (var1 == M) {
            of.h(var18, "Glow", (var4.glowing() ? this.W.H : this.x).getColorTextureView(), var20);
         }

         var18.draw(3, 1, 0, 0);
         h_.A(this.N + " fullscreen draws", 1L);
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

   private static void W(ByteBuffer var0, int var1) {
      var0.putFloat((var1 >> 16 & 0xFF) / 255.0F).putFloat((var1 >> 8 & 0xFF) / 255.0F).putFloat((var1 & 0xFF) / 255.0F).putFloat((var1 >>> 24) / 255.0F);
   }

   private void S(String var1, RuntimeException var2) {
      this.a = (boolean)1;
      this.close();
      r4.r(this.N, "stage=" + var1 + "; gradient pass disabled until resource reload; ordinary models retained", var2);
   }

   @Override
   public void close() {
      this.e = (boolean)0;
      this.q = (boolean)0;
      TextureTarget var1 = this.x;
      this.x = null;
      GpuBuffer var2 = this.o;
      this.o = null;
      y2 var3 = this.T;
      this.T = null;
      RuntimeException var4 = null;

      for (Runnable var8 : new Runnable[]{this.W::close, () -> {
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
         r4.r(this.N, "stage=cleanup; all gradient resource releases attempted", var4);
      }
   }

   private static Identifier c(String var0) {
      return Identifier.fromNamespaceAndPath("larp-client", var0);
   }

   public record a(EntityRenderState state, int color) {
   }

   public record j(int x, int y, int width, int height, rg.q frame) {
   }
}
