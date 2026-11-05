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

public final class m6 implements AutoCloseable {
   private static final Vector4fc P = new Vector4f(0.0F);
   private static final RenderPipeline c = f("chams_edge", false);
   private static final RenderPipeline X = Y();
   private static final RenderPipeline C = f("chams_blur", false);
   private static final RenderPipeline I = f("chams_composite", true);
   private static final RenderPipeline p = RenderPipeline.builder(new Snippet[0])
      .withLocation(F("chams_hand_mask"))
      .withVertexShader(F("post/fullscreen"))
      .withFragmentShader(F("post/chams_hand_mask"))
      .withBindGroupLayout(BindGroupLayout.builder().withUniform("Before", hj.G).withUniform("After", hj.G).withUniform("HandDepth", hj.G).build())
      .withPrimitiveTopology(PrimitiveTopology.TRIANGLES)
      .withCull(false)
      .build();
   private final String O;
   private final e4 A = new e4();
   private TextureTarget Z;
   private GpuBuffer E;
   private xh R;
   private int Q = -1;
   private boolean x;
   private boolean s;
   private boolean J;
   private boolean D;
   private boolean k;
   private boolean z;

   public m6(String var1) {
      this.O = var1;
   }

   private static RenderPipeline Y() {
      return RenderPipeline.builder(new Snippet[0])
         .withLocation(F("chams_entity_mask"))
         .withVertexShader(F("post/fullscreen"))
         .withFragmentShader(F("post/chams_entity_mask"))
         .withBindGroupLayout(
            BindGroupLayout.builder()
               .withUniform("Source", hj.G)
               .withUniform("ModelDepth", hj.G)
               .withUniform("SceneDepth", hj.G)
               .withUniform("ChamsParameters", UniformType.UNIFORM_BUFFER)
               .build()
         )
         .withPrimitiveTopology(PrimitiveTopology.TRIANGLES)
         .withColorTargetState(ColorTargetState.DEFAULT)
         .withCull(false)
         .build();
   }

   private static RenderPipeline f(String var0, boolean var1) {
      Builder var2 = BindGroupLayout.builder().withUniform("Source", hj.G).withUniform("ChamsParameters", UniformType.UNIFORM_BUFFER);
      if (var1) {
         var2.withUniform("Glow", hj.G);
      }

      return RenderPipeline.builder(new Snippet[0])
         .withLocation(F(var0))
         .withVertexShader(F("post/fullscreen"))
         .withFragmentShader(F("post/" + var0))
         .withBindGroupLayout(var2.build())
         .withPrimitiveTopology(PrimitiveTopology.TRIANGLES)
         .withColorTargetState(var1 ? new ColorTargetState(BlendFunction.TRANSLUCENT) : ColorTargetState.DEFAULT)
         .withCull(false)
         .build();
   }

   private boolean x() {
      int var1 = xn.j == null && xn.o == null ? 1 : 0;
      if (var1 == 0 && !this.s) {
         this.s = (boolean)1;
         mt.T(this.O, "Gradient capture skipped while another renderer owns the output attachments");
      }

      return (boolean)var1;
   }

   private boolean E() {
      if (this.Q != m5.k) {
         this.close();
         this.Q = m5.k;
         this.x = (boolean)0;
      }

      if (!this.x && this.x()) {
         try {
            RenderTarget var1 = Minecraft.getInstance().gameRenderer.mainRenderTarget();
            if (this.Z != null && (this.Z.width != var1.width || this.Z.height != var1.height)) {
               this.close();
            }

            if (this.Z == null) {
               ea.z(c);
               ea.z(X);
               ea.z(C);
               ea.z(I);
               this.A.e(var1.width, var1.height, 1, e4.o.G);
               this.Z = hj.j("Larp " + this.O + " gradient", var1.width, var1.height, false, GpuFormat.RGBA8_UNORM);
               this.E = RenderSystem.getDevice().createBuffer(() -> "Larp " + this.O + " gradient parameters", 136, 80L);
               mt.K(this.O, "Gradient pass initialized {}x{}; four full-resolution targets", var1.width, var1.height);
            }

            return true;
         } catch (RuntimeException var2) {
            this.g("allocate/check", var2);
            return false;
         }
      } else {
         return false;
      }
   }

   public void D(List<m6.j> var1, PoseStack var2, p3.p var3, boolean var4) {
      if (var1.isEmpty()) {
         this.close();
      } else if (this.E()) {
         GpuTextureView var5 = xn.j;
         GpuTextureView var6 = xn.o;

         try {
            this.k = (boolean)1;
            this.z = var4;
            if (ex.X() == null) {
               throw new IllegalStateException("scene-depth capture unavailable");
            }

            if (this.R == null) {
               this.R = new xh();
            }

            CommandEncoder var7 = RenderSystem.getDevice().createCommandEncoder();
            var7.clearColorTexture(this.A.M.getColorTexture(), P);
            var7.clearDepthTexture(this.A.M.getDepthTexture(), 0.0);
            xn.j = this.A.M.getColorTextureView();
            xn.o = this.A.M.getDepthTextureView();

            try {
               for (m6.j var9 : var1) {
                  tj.E(var9.state, var2, this.R, tj.b.k, var9.color, 1.0, false, false);
               }

               this.R.b();
            } finally {
               xn.j = var5;
               xn.o = var6;
            }

            this.P(var3);
         } catch (RuntimeException var19) {
            this.g("entity mask/composite", var19);
         } finally {
            xn.j = var5;
            xn.o = var6;
            if (this.R != null) {
               this.R.s();
            }

            this.k = (boolean)0;
         }
      }
   }

   public boolean Y() {
      this.J = (boolean)0;
      if (!this.E()) {
         return false;
      }

      try {
         if (!this.D) {
            ea.z(p);
            this.D = (boolean)1;
         }

         m5.l(ea.m, zo.E(Minecraft.getInstance().gameRenderer.mainRenderTarget()), this.Z);
         this.J = (boolean)1;
         return true;
      } catch (RuntimeException var2) {
         this.g("hand scene capture", var2);
         return false;
      }
   }

   public void V(p3.p var1) {
      RenderTarget var2 = this.G();
      if (var2 != null) {
         try {
            this.P(var1);
         } catch (RuntimeException var4) {
            this.g("hand mask/composite", var4);
         }
      }
   }

   public void b(List<m6.h> var1) {
      RenderTarget var2 = this.G();
      if (var2 != null) {
         long var3 = kx.G();

         try {
            for (m6.h var6 : var1) {
               this.x(c, this.A.M, this.Z, var6.frame(), 0.0F, 0.0F, var6);
               this.x(I, this.Z, var2, var6.frame(), 0.0F, 0.0F, var6);
            }

            ex.w();
         } catch (RuntimeException var10) {
            this.g("hand region composite", var10);
         } finally {
            kx.u(this.O + " gradient post", var3);
         }
      }
   }

   private RenderTarget G() {
      if (!this.J) {
         return null;
      }

      this.J = (boolean)0;
      if (!this.x()) {
         this.close();
         return null;
      }

      try {
         RenderTarget var1 = Minecraft.getInstance().gameRenderer.mainRenderTarget();
         if (var1.width == this.Z.width && var1.height == this.Z.height) {
            CommandEncoder var2 = RenderSystem.getDevice().createCommandEncoder();
            RenderPass var3 = var2.createRenderPass(() -> "Larp " + this.O + " silhouette", this.A.M.getColorTextureView(), Optional.empty());

            try {
               hj.P(var3, p);
               GpuSampler var4 = RenderSystem.getSamplerCache().getClampToEdge(FilterMode.LINEAR);
               hj.u(var3, "Before", this.Z.getColorTextureView(), var4);
               hj.u(var3, "After", zo.E(var1).getColorTextureView(), var4);
               hj.u(var3, "HandDepth", var1.getDepthTextureView(), RenderSystem.getSamplerCache().getClampToEdge(FilterMode.NEAREST));
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
         this.g("hand mask", var8);
         return null;
      }
   }

   private void P(p3.p var1) {
      long var2 = kx.G();

      try {
         if (this.k) {
            this.b(X, this.A.M, this.A.b, var1, 0.0F, 0.0F);
         }

         this.b(c, this.k ? this.A.b : this.A.M, this.Z, var1, 0.0F, 0.0F);
         if (var1.glowing()) {
            this.b(C, this.Z, this.A.b, var1, 1.0F, 0.0F);
            this.b(C, this.A.b, this.A.v, var1, 0.0F, 1.0F);
         }

         this.b(I, this.Z, zo.t(Minecraft.getInstance().gameRenderer.mainRenderTarget()), var1, 0.0F, 0.0F);
         ex.w();
      } finally {
         kx.u(this.O + " gradient post", var2);
      }
   }

   private void b(RenderPipeline var1, RenderTarget var2, RenderTarget var3, p3.p var4, float var5, float var6) {
      this.x(var1, var2, var3, var4, var5, var6, null);
   }

   private void x(RenderPipeline var1, RenderTarget var2, RenderTarget var3, p3.p var4, float var5, float var6, m6.h var7) {
      CommandEncoder var8 = RenderSystem.getDevice().createCommandEncoder();
      MemoryStack var9 = MemoryStack.stackPush();

      try {
         ByteBuffer var10 = var9.malloc(80);
         var10.putFloat(var4.lineWidth()).putFloat(var4.scale()).putFloat(var4.fill()).putFloat(var4.outline());
         var10.putFloat(var4.entityColor() ? 1.0F : 0.0F).putFloat(var4.phase()).putFloat(var4.radius()).putFloat(var4.glowing() ? var4.intensity() : 0.0F);
         A(var10, var4.color1());
         A(var10, var4.color2());
         var10.putFloat(var5).putFloat(var6).putFloat(this.z ? 1.0F : 0.0F).putFloat(0.0F).flip();
         var8.writeToBuffer(this.E.slice(), var10);
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

      RenderPass var18 = var8.createRenderPass(() -> "Larp " + this.O + " " + var1.getLocation(), var3.getColorTextureView(), Optional.empty());

      try {
         hj.P(var18, var1);
         if (var7 != null) {
            int var19 = Math.clamp(var7.x(), 0, var3.width);
            int var11 = Math.clamp(var7.y(), 0, var3.height);
            int var12 = Math.clamp(var7.x() + var7.width(), var19, var3.width);
            int var13 = Math.clamp(var7.y() + var7.height(), var11, var3.height);
            var18.enableScissor(var19, var3.height - var13, var12 - var19, var13 - var11);
         }

         var18.setUniform("ChamsParameters", this.E.slice());
         GpuSampler var20 = RenderSystem.getSamplerCache().getClampToEdge(FilterMode.LINEAR);
         hj.u(var18, "Source", var2.getColorTextureView(), var20);
         if (var1 == X) {
            GpuSampler var21 = RenderSystem.getSamplerCache().getClampToEdge(FilterMode.NEAREST);
            hj.u(var18, "Source", var2.getColorTextureView(), var21);
            hj.u(var18, "ModelDepth", this.A.M.getDepthTextureView(), var21);
            hj.u(var18, "SceneDepth", ex.X(), var21);
         }

         if (var1 == I) {
            hj.u(var18, "Glow", (var4.glowing() ? this.A.v : this.Z).getColorTextureView(), var20);
         }

         var18.draw(3, 1, 0, 0);
         kx.U(this.O + " fullscreen draws", 1L);
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

   private static void A(ByteBuffer var0, int var1) {
      var0.putFloat((var1 >> 16 & 0xFF) / 255.0F).putFloat((var1 >> 8 & 0xFF) / 255.0F).putFloat((var1 & 0xFF) / 255.0F).putFloat((var1 >>> 24) / 255.0F);
   }

   private void g(String var1, RuntimeException var2) {
      this.x = (boolean)1;
      this.close();
      mt.P(this.O, "stage=" + var1 + "; gradient pass disabled until resource reload; ordinary models retained", var2);
   }

   @Override
   public void close() {
      this.J = (boolean)0;
      this.D = (boolean)0;
      TextureTarget var1 = this.Z;
      this.Z = null;
      GpuBuffer var2 = this.E;
      this.E = null;
      xh var3 = this.R;
      this.R = null;
      RuntimeException var4 = null;

      for (Runnable var8 : new Runnable[]{this.A::close, () -> {
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
         mt.P(this.O, "stage=cleanup; all gradient resource releases attempted", var4);
      }
   }

   private static Identifier F(String var0) {
      return Identifier.fromNamespaceAndPath("larp-client", var0);
   }

   public record h(int x, int y, int width, int height, p3.p frame) {
   }

   public record j(EntityRenderState state, int color) {
   }
}
