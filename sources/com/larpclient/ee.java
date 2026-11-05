package com.larpclient;

import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.pipeline.TextureTarget;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.renderpearl.api.GpuFormat;
import com.mojang.renderpearl.api.buffers.GpuBuffer;
import com.mojang.renderpearl.api.buffers.GpuBufferSlice.MappedView;
import com.mojang.renderpearl.api.commands.CommandEncoder;
import com.mojang.renderpearl.api.commands.RenderPass;
import com.mojang.renderpearl.api.pipeline.BindGroupLayout;
import com.mojang.renderpearl.api.pipeline.BlendFactor;
import com.mojang.renderpearl.api.pipeline.BlendFunction;
import com.mojang.renderpearl.api.pipeline.ColorTargetState;
import com.mojang.renderpearl.api.pipeline.PrimitiveTopology;
import com.mojang.renderpearl.api.pipeline.RenderPipeline;
import com.mojang.renderpearl.api.pipeline.BindGroupLayout.Builder;
import com.mojang.renderpearl.api.pipeline.RenderPipeline.Snippet;
import com.mojang.renderpearl.api.textures.FilterMode;
import com.mojang.renderpearl.api.textures.GpuSampler;
import com.mojang.renderpearl.api.textures.GpuTextureView;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.function.Consumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.render.GuiRenderer;
import net.minecraft.client.gui.render.pip.GuiBannerResultRenderer;
import net.minecraft.client.gui.render.pip.GuiBookModelRenderer;
import net.minecraft.client.gui.render.pip.GuiEntityRenderer;
import net.minecraft.client.gui.render.pip.GuiProfilerChartRenderer;
import net.minecraft.client.gui.render.pip.GuiSkinRenderer;
import net.minecraft.client.renderer.SubmitNodeStorage;
import net.minecraft.client.renderer.feature.FeatureRenderDispatcher;
import net.minecraft.client.renderer.feature.FeatureRenderDispatcher.PreparedFrame;
import net.minecraft.client.renderer.gizmos.DrawableGizmoPrimitives;
import net.minecraft.client.renderer.state.gui.GuiRenderState;
import net.minecraft.client.renderer.state.gui.GuiRenderState.TraverseRange;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.gizmos.SimpleGizmoCollector.GizmoInstance;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Util;
import org.joml.Vector4f;

final class ee {
   private static final Vector4f o = new Vector4f(0.0F, 0.0F, 0.0F, 0.0F);
   private static final int N = 3;
   static final RenderPipeline k = K("streamproof_pack", "Interface", "Effects");
   static final RenderPipeline w = K("streamproof_pack_interface", "Interface");
   static final RenderPipeline X = K("streamproof_opaque_blur", "Source");
   static final RenderPipeline u = g("streamproof_hand_cover", M(), "Scene", "Depth");
   static final RenderPipeline y = g("streamproof_sky", M(), "Sky", "Depth");
   private final GuiRenderState Q = new GuiRenderState();
   private GuiRenderer f;
   private TextureTarget l;
   private TextureTarget F;
   private TextureTarget O;
   private TextureTarget L;
   private TextureTarget i;
   private TextureTarget Z;
   private boolean m;
   private long G = -1L;
   private final ee.y[] K = new ee.y[3];
   private boolean H;
   private boolean R;
   private boolean j;
   private boolean n;
   private long q = -1L;
   private long d = -1L;
   private int g;

   private static ColorTargetState M() {
      return new ColorTargetState(new BlendFunction(BlendFactor.ONE_MINUS_DST_ALPHA, BlendFactor.ONE, BlendFactor.ONE_MINUS_DST_ALPHA, BlendFactor.ONE));
   }

   GuiRenderState N() {
      return this.Q;
   }

   boolean d(Object var1) {
      return var1 != null && var1 == this.f;
   }

   RenderTarget u() {
      return this.l;
   }

   boolean E() {
      return this.R;
   }

   boolean R() {
      return this.g > 0;
   }

   boolean P(long var1) {
      return this.q == var1;
   }

   RenderTarget k() {
      return this.g > 0 ? this.F : null;
   }

   boolean p() {
      boolean[] var1 = new boolean[]{false};
      this.Q.forEachElement(var1x -> var1[0] = (boolean)1, TraverseRange.ALL);
      if (!var1[0]) {
         this.Q.forEachText(var1x -> var1[0] = (boolean)1);
      }

      if (!var1[0]) {
         this.Q.forEachItem(var1x -> var1[0] = (boolean)1);
      }

      if (!var1[0]) {
         this.Q.forEachPictureInPicture(var1x -> var1[0] = (boolean)1);
      }

      return var1[0];
   }

   void z(long var1) {
      if (this.q != var1) {
         RenderTarget var3 = Minecraft.getInstance().gameRenderer.mainRenderTarget();
         this.F = r(this.F, "Larp Stream Proof effects", var3.width, var3.height, true);
         CommandEncoder var4 = RenderSystem.getDevice().createCommandEncoder();
         var4.clearColorTexture(this.F.getColorTexture(), o);
         this.q = var1;
         this.j = (boolean)0;
         this.n = (boolean)0;
      }
   }

   RenderTarget P(long var1) {
      this.z(var1);
      this.z();
      return this.F;
   }

   private void z() {
      if (!this.j) {
         this.F.copyDepthFrom(Minecraft.getInstance().gameRenderer.mainRenderTarget());
         this.j = (boolean)1;
      }
   }

   RenderTarget n(long var1) {
      this.z(var1);
      RenderTarget var3 = Minecraft.getInstance().gameRenderer.mainRenderTarget();
      this.O = r(this.O, "Larp Stream Proof sky", var3.width, var3.height, true);
      if (!this.n) {
         RenderSystem.getDevice().createCommandEncoder().clearColorAndDepthTextures(this.O.getColorTexture(), o, this.O.getDepthTexture(), 0.0);
         this.n = (boolean)1;
      }

      return this.O;
   }

   void e(long var1, boolean var3) {
      if (this.n && this.q == var1 && !this.R) {
         RenderTarget var4 = Minecraft.getInstance().gameRenderer.mainRenderTarget();
         this.z();
         Object var5 = var3 ? this.F : var4;

         try {
            this.O();
            GpuSampler var6 = RenderSystem.getSamplerCache().getClampToEdge(FilterMode.NEAREST);
            RenderPass var7 = RenderSystem.getDevice()
               .createCommandEncoder()
               .createRenderPass(() -> "Larp Stream Proof sky", this.F.getColorTextureView(), Optional.empty());

            try {
               hj.P(var7, y);
               hj.u(var7, "Sky", this.O.getColorTextureView(), var6);
               hj.u(var7, "Depth", var5.getDepthTextureView(), var6);
               var7.draw(3, 1, 0, 0);
            } catch (Throwable var11) {
               if (var7 != null) {
                  try {
                     var7.close();
                  } catch (Throwable var10) {
                     var11.addSuppressed(var10);
                  }
               }

               throw var11;
            }

            if (var7 != null) {
               var7.close();
            }
         } catch (RuntimeException var12) {
            this.R("sky", var12);
         }
      }
   }

   void p(long var1) {
      if (this.q == var1 && !this.R) {
         RenderTarget var3 = Minecraft.getInstance().gameRenderer.mainRenderTarget();

         try {
            this.i = r(this.i, "Larp Stream Proof hand backdrop", var3.width, var3.height, false);
            m5.l(ea.m, var3, this.i);
         } catch (RuntimeException var5) {
            this.R("hand backdrop", var5);
         }
      }
   }

   void c(long var1) {
      if (this.q == var1 && !this.R && this.i != null) {
         RenderTarget var3 = Minecraft.getInstance().gameRenderer.mainRenderTarget();

         try {
            this.O();
            GpuSampler var4 = RenderSystem.getSamplerCache().getClampToEdge(FilterMode.NEAREST);
            CommandEncoder var5 = RenderSystem.getDevice().createCommandEncoder();
            RenderPass var6 = var5.createRenderPass(() -> "Larp Stream Proof hand cover", this.F.getColorTextureView(), Optional.empty());

            try {
               hj.P(var6, u);
               hj.u(var6, "Scene", this.i.getColorTextureView(), var4);
               hj.u(var6, "Depth", var3.getDepthTextureView(), var4);
               var6.draw(3, 1, 0, 0);
            } catch (Throwable var10) {
               if (var6 != null) {
                  try {
                     var6.close();
                  } catch (Throwable var9) {
                     var10.addSuppressed(var9);
                  }
               }

               throw var10;
            }

            if (var6 != null) {
               var6.close();
            }

            var5.clearDepthTexture(var3.getDepthTexture(), 0.0);
         } catch (RuntimeException var11) {
            this.R("hand cover", var11);
         }
      }
   }

   void e(long var1, Runnable var3) {
      if (this.q == var1 && !this.R) {
         RenderTarget var4 = Minecraft.getInstance().gameRenderer.mainRenderTarget();
         GpuTextureView var5 = xn.j;
         GpuTextureView var6 = xn.o;
         xn.j = this.F.getColorTextureView();
         xn.o = var4.getDepthTextureView();

         try {
            var3.run();
         } finally {
            xn.j = var5;
            xn.o = var6;
         }
      } else {
         var3.run();
      }
   }

   void X(boolean var1) {
      this.m = var1;
   }

   RenderTarget K(RenderTarget var1) {
      return (RenderTarget)(this.m && this.F != null ? this.F : var1);
   }

   void g(long var1) {
      this.z(var1);
      RenderTarget var3 = Minecraft.getInstance().gameRenderer.mainRenderTarget();
      this.L = r(this.L, "Larp Stream Proof self depth", var3.width, var3.height, true);
      this.L.copyDepthFrom(var3);
      this.G = var1;
   }

   void O(long var1, Runnable var3) {
      if (this.G == var1 && this.L != null) {
         this.z(var1);
         GpuTextureView var4 = xn.j;
         GpuTextureView var5 = xn.o;
         RenderTarget var6 = m5.A;
         xn.j = this.F.getColorTextureView();
         xn.o = this.L.getDepthTextureView();
         m5.A = this.F;
         this.g++;

         try {
            var3.run();
         } finally {
            this.g--;
            xn.j = var4;
            xn.o = var5;
            m5.A = var6;
         }
      } else {
         this.W(var1, var3);
      }
   }

   void W(long var1, Runnable var3) {
      this.z(var1);
      this.z();
      GpuTextureView var4 = xn.j;
      GpuTextureView var5 = xn.o;
      RenderTarget var6 = m5.A;
      xn.j = this.F.getColorTextureView();
      xn.o = this.F.getDepthTextureView();
      m5.A = this.F;
      this.g++;

      try {
         var3.run();
      } finally {
         this.g--;
         xn.j = var4;
         xn.o = var5;
         m5.A = var6;
      }
   }

   void T(Runnable var1) {
      if (this.g == 0) {
         var1.run();
      } else {
         GpuTextureView var2 = xn.j;
         GpuTextureView var3 = xn.o;
         RenderTarget var4 = m5.A;
         int var5 = this.g;
         xn.j = null;
         xn.o = null;
         m5.A = null;
         this.g = 0;

         try {
            var1.run();
         } finally {
            this.g = var5;
            xn.j = var2;
            xn.o = var3;
            m5.A = var4;
         }
      }
   }

   boolean D(long var1, long var3, boolean var5, Consumer<RenderTarget> var6, ee.g var7) {
      if (this.R) {
         this.Q.reset();
         return false;
      }

      RenderTarget var8 = Minecraft.getInstance().gameRenderer.mainRenderTarget();
      String var9 = "resources";
      long var10 = kx.G();

      try {
         this.O();
         int var12 = var8.width;
         int var13 = var8.height;
         this.l = r(this.l, "Larp Stream Proof interface", var12, var13, true);
         this.Z = r(this.Z, "Larp Stream Proof pack", var12, var13, false);
         CommandEncoder var14 = RenderSystem.getDevice().createCommandEncoder();
         var14.clearColorAndDepthTextures(this.l.getColorTexture(), o, this.l.getDepthTexture(), 0.0);
         var9 = "interface blur";
         var6.accept(this.l);
         var9 = "interface GUI";
         this.R().render();
         this.R().endFrame();
         var9 = "compose";
         int var15 = var5 && this.q == var1 && this.F != null && this.F.width == var12 && this.F.height == var13 ? 1 : 0;
         GpuSampler var16 = RenderSystem.getSamplerCache().getClampToEdge(FilterMode.NEAREST);
         RenderPass var17 = var14.createRenderPass(() -> "Larp Stream Proof compose", this.Z.getColorTextureView(), Optional.empty());

         try {
            hj.P(var17, var15 != 0 ? k : w);
            hj.u(var17, "Interface", this.l.getColorTextureView(), var16);
            if (var15 != 0) {
               hj.u(var17, "Effects", this.F.getColorTextureView(), var16);
            }

            var17.draw(3, 1, 0, 0);
         } catch (Throwable var26) {
            if (var17 != null) {
               try {
                  var17.close();
               } catch (Throwable var25) {
                  var26.addSuppressed(var25);
               }
            }

            throw var26;
         }

         if (var17 != null) {
            var17.close();
         }

         var9 = "read-back";
         return this.l(var3, var12, var13, var7);
      } catch (RuntimeException var27) {
         this.Q.reset();
         this.R(var9, var27);
         return false;
      } finally {
         kx.u("Stream Proof compose", var10);
      }
   }

   void L() {
      this.Q.reset();
   }

   void N(RenderTarget var1) {
      if (m5.R()) {
         this.I(var1, null);
      } else {
         for (int[] var3 : m5.v()) {
            this.I(var1, var3);
         }
      }
   }

   boolean q() {
      if (this.l != null && !this.R) {
         this.I(this.l, null);
         return true;
      } else {
         return false;
      }
   }

   private void I(RenderTarget var1, int[] var2) {
      GpuTextureView var3 = m5.r();
      if (var3 != null) {
         this.O();
         RenderPass var4 = RenderSystem.getDevice()
            .createCommandEncoder()
            .createRenderPass(() -> "Larp Stream Proof blur", var1.getColorTextureView(), Optional.empty());

         try {
            hj.P(var4, X);
            hj.u(var4, "Source", var3, RenderSystem.getSamplerCache().getClampToEdge(FilterMode.LINEAR));
            if (var2 != null) {
               int var5 = Minecraft.getInstance().getWindow().getGuiScale();
               int var6 = var2[0];
               int var7 = var2[1];
               int var8 = var2[2];
               int var9 = var2[3];
               var4.enableScissor(
                  Math.max(0, var6 * var5),
                  Math.max(0, var1.height - (var7 + var9) * var5),
                  Math.max(0, Math.min(var8 * var5, var1.width - var6 * var5)),
                  Math.max(0, Math.min(var9 * var5, var1.height - var7 * var5))
               );
            }

            var4.draw(3, 1, 0, 0);
         } catch (Throwable var11) {
            if (var4 != null) {
               try {
                  var4.close();
               } catch (Throwable var10) {
                  var11.addSuppressed(var10);
               }
            }

            throw var11;
         }

         if (var4 != null) {
            var4.close();
         }
      }
   }

   void U(List<GizmoInstance> var1, CameraRenderState var2, FeatureRenderDispatcher var3) {
      if (!this.R && !var1.isEmpty()) {
         long var4 = kx.G();

         try {
            long var6 = Util.getMillis();
            DrawableGizmoPrimitives var8 = new DrawableGizmoPrimitives();
            DrawableGizmoPrimitives var9 = new DrawableGizmoPrimitives();

            for (GizmoInstance var11 : var1) {
               var11.gizmo().emit(var11.isAlwaysOnTop() ? var9 : var8, var11.getAlphaMultiplier(var6));
            }

            SubmitNodeStorage var29 = new SubmitNodeStorage();
            var8.submit(var29, var2, false);
            var9.submit(var29, var2, true);
            RenderTarget var30 = Minecraft.getInstance().gameRenderer.mainRenderTarget();
            PreparedFrame var12 = var3.prepareFrame(var29);

            try {
               RenderPass var13 = RenderSystem.getDevice()
                  .createCommandEncoder()
                  .createRenderPass(
                     () -> "Larp stream proof gizmos", var30.getColorTextureView(), Optional.empty(), var30.getDepthTextureView(), OptionalDouble.empty()
                  );

               try {
                  RenderSystem.bindDefaultUniforms(var13);
                  FeatureRenderDispatcher.renderAllFeatures(var13, var12);
               } catch (Throwable var25) {
                  if (var13 != null) {
                     try {
                        var13.close();
                     } catch (Throwable var24) {
                        var25.addSuppressed(var24);
                     }
                  }

                  throw var25;
               }

               if (var13 != null) {
                  var13.close();
               }
            } catch (Throwable var26) {
               if (var12 != null) {
                  try {
                     var12.close();
                  } catch (Throwable var23) {
                     var26.addSuppressed(var23);
                  }
               }

               throw var26;
            }

            if (var12 != null) {
               var12.close();
            }
         } catch (RuntimeException var27) {
            this.R("gizmo overlay", var27);
         } finally {
            kx.u("Stream Proof gizmos", var4);
         }
      }
   }

   private boolean l(long var1, int var3, int var4, ee.g var5) {
      ee.y var6 = null;

      for (int var7 = 0; var7 < 3; var7++) {
         if (this.K[var7] == null) {
            this.K[var7] = new ee.y();
         }

         ee.y var8 = this.K[var7];
         if (!var8.R) {
            if (var8.N == null || var8.W != var3 || var8.z != var4) {
               if (var8.N != null) {
                  var8.N.close();
               }

               var8.N = RenderSystem.getDevice().createBuffer(() -> "Larp Stream Proof read-back", 9, (long)var3 * var4 * 4L);
               var8.W = var3;
               var8.z = var4;
            }

            var6 = var8;
            break;
         }
      }

      if (var6 == null) {
         kx.U("Stream Proof skipped read-backs", 1L);
         return false;
      } else {
         ee.y var9 = var6;
         var9.R = (boolean)1;
         RenderSystem.getDevice().createCommandEncoder().copyTextureToBuffer(this.Z.getColorTexture(), var9.N, 0L, () -> {
            try {
               if (!var9.B && var1 > this.d) {
                  MappedView var7x = var9.N.map(true, false);

                  try {
                     var5.H(var1, var3, var4, var7x.data());
                  } catch (Throwable var16) {
                     if (var7x != null) {
                        try {
                           var7x.close();
                        } catch (Throwable var15) {
                           var16.addSuppressed(var15);
                        }
                     }

                     throw var16;
                  }

                  if (var7x != null) {
                     var7x.close();
                  }

                  this.d = var1;
               }
            } catch (RuntimeException var17) {
               mt.B("StreamProof", "Read-back delivery failed; the overlay keeps its previous frame", var17);
            } finally {
               var9.R = (boolean)0;
               if (var9.B && var9.N != null) {
                  var9.N.close();
                  var9.N = null;
               }
            }
         }, 0);
         return true;
      }
   }

   private GuiRenderer R() {
      if (this.f == null) {
         Minecraft var1 = Minecraft.getInstance();
         this.f = new GuiRenderer(
            this.Q,
            var1.gameRenderer.featureRenderDispatcher(),
            List.of(
               new GuiEntityRenderer(var1.getEntityRenderDispatcher()),
               new GuiSkinRenderer(),
               new GuiBookModelRenderer(),
               new GuiBannerResultRenderer(var1.getAtlasManager()),
               new GuiProfilerChartRenderer()
            )
         );
         mt.U("StreamProof", "Interface GUI renderer created");
      }

      return this.f;
   }

   private void O() {
      if (!this.H) {
         for (RenderPipeline var4 : new RenderPipeline[]{k, w, X, u, y}) {
            ea.z(var4);
         }

         this.H = (boolean)1;
         mt.K("StreamProof", "Layer pipelines compiled");
      }
   }

   private void R(String var1, RuntimeException var2) {
      this.R = (boolean)1;
      mt.P("StreamProof", "Layer stage=" + var1 + " failed; Stream Proof falls back to normal drawing until it is re-enabled", var2);
   }

   private static TextureTarget r(TextureTarget var0, String var1, int var2, int var3, boolean var4) {
      if (var0 != null && var0.width == var2 && var0.height == var3) {
         return var0;
      }

      if (var0 != null) {
         var0.destroyBuffers();
      }

      return hj.j(var1, var2, var3, var4, GpuFormat.RGBA8_UNORM);
   }

   void K() {
      this.Q.reset();
      if (this.f != null) {
         this.f.close();
         this.f = null;
      }

      for (TextureTarget var4 : new TextureTarget[]{this.l, this.F, this.O, this.L, this.i, this.Z}) {
         if (var4 != null) {
            var4.destroyBuffers();
         }
      }

      this.l = this.F = this.O = this.L = this.i = this.Z = null;
      this.m = (boolean)0;
      this.G = -1L;
      this.j = this.n = (boolean)0;

      for (int var5 = 0; var5 < 3; var5++) {
         ee.y var6 = this.K[var5];
         if (var6 != null) {
            if (var6.R) {
               var6.B = (boolean)1;
            } else if (var6.N != null) {
               var6.N.close();
            }

            this.K[var5] = null;
         }
      }

      this.q = -1L;
      this.d = -1L;
      this.g = 0;
      this.R = (boolean)0;
      this.H = (boolean)0;
   }

   private static RenderPipeline K(String var0, String... var1) {
      return g(var0, ColorTargetState.DEFAULT, var1);
   }

   private static RenderPipeline g(String var0, ColorTargetState var1, String... var2) {
      Builder var3 = BindGroupLayout.builder();

      for (String var7 : var2) {
         var3.withUniform(var7, hj.G);
      }

      return RenderPipeline.builder(new Snippet[0])
         .withLocation(Identifier.fromNamespaceAndPath("larp-client", var0))
         .withVertexShader(Identifier.fromNamespaceAndPath("larp-client", "post/fullscreen"))
         .withFragmentShader(Identifier.fromNamespaceAndPath("larp-client", "post/" + var0))
         .withBindGroupLayout(var3.build())
         .withPrimitiveTopology(PrimitiveTopology.TRIANGLES)
         .withColorTargetState(var1)
         .withCull(false)
         .build();
   }

   interface g {
      void H(long var1, int var3, int var4, ByteBuffer var5);
   }

   private static final class y {
      GpuBuffer N;
      int W;
      int z;
      boolean R;
      boolean B;
   }
}
