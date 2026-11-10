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

final class is {
   private static final Vector4f m = new Vector4f(0.0F, 0.0F, 0.0F, 0.0F);
   private static final int B = 3;
   static final RenderPipeline A = y("streamproof_pack", "Interface", "Effects");
   static final RenderPipeline s = y("streamproof_pack_interface", "Interface");
   static final RenderPipeline j = y("streamproof_opaque_blur", "Source");
   static final RenderPipeline c = b("streamproof_hand_cover", M(), "Scene", "Depth");
   static final RenderPipeline H = b("streamproof_sky", M(), "Sky", "Depth");
   private final GuiRenderState y = new GuiRenderState();
   private GuiRenderer x;
   private TextureTarget i;
   private TextureTarget K;
   private TextureTarget I;
   private TextureTarget V;
   private TextureTarget k;
   private TextureTarget C;
   private boolean r;
   private long u = -1L;
   private final is.i[] W = new is.i[3];
   private boolean L;
   private boolean b;
   private boolean J;
   private boolean e;
   private long f = -1L;
   private long l = -1L;
   private int G;

   private static ColorTargetState M() {
      return new ColorTargetState(new BlendFunction(BlendFactor.ONE_MINUS_DST_ALPHA, BlendFactor.ONE, BlendFactor.ONE_MINUS_DST_ALPHA, BlendFactor.ONE));
   }

   GuiRenderState Z() {
      return this.y;
   }

   boolean l(Object var1) {
      return var1 != null && var1 == this.x;
   }

   RenderTarget N() {
      return this.i;
   }

   boolean y() {
      return this.b;
   }

   boolean v() {
      return this.G > 0;
   }

   boolean x(long var1) {
      return this.f == var1;
   }

   RenderTarget p() {
      return this.G > 0 ? this.K : null;
   }

   boolean l() {
      boolean[] var1 = new boolean[]{false};
      this.y.forEachElement(var1x -> var1[0] = (boolean)1, TraverseRange.ALL);
      if (!var1[0]) {
         this.y.forEachText(var1x -> var1[0] = (boolean)1);
      }

      if (!var1[0]) {
         this.y.forEachItem(var1x -> var1[0] = (boolean)1);
      }

      if (!var1[0]) {
         this.y.forEachPictureInPicture(var1x -> var1[0] = (boolean)1);
      }

      return var1[0];
   }

   void t(long var1) {
      if (this.f != var1) {
         RenderTarget var3 = Minecraft.getInstance().gameRenderer.mainRenderTarget();
         this.K = Q(this.K, "Larp Stream Proof effects", var3.width, var3.height, true);
         CommandEncoder var4 = RenderSystem.getDevice().createCommandEncoder();
         var4.clearColorTexture(this.K.getColorTexture(), m);
         this.f = var1;
         this.J = (boolean)0;
         this.e = (boolean)0;
      }
   }

   RenderTarget m(long var1) {
      this.t(var1);
      this.F();
      return this.K;
   }

   private void F() {
      if (!this.J) {
         this.K.copyDepthFrom(Minecraft.getInstance().gameRenderer.mainRenderTarget());
         this.J = (boolean)1;
      }
   }

   RenderTarget x(long var1) {
      this.t(var1);
      RenderTarget var3 = Minecraft.getInstance().gameRenderer.mainRenderTarget();
      this.I = Q(this.I, "Larp Stream Proof sky", var3.width, var3.height, true);
      if (!this.e) {
         RenderSystem.getDevice().createCommandEncoder().clearColorAndDepthTextures(this.I.getColorTexture(), m, this.I.getDepthTexture(), 0.0);
         this.e = (boolean)1;
      }

      return this.I;
   }

   void I(long var1, boolean var3) {
      if (this.e && this.f == var1 && !this.b) {
         RenderTarget var4 = Minecraft.getInstance().gameRenderer.mainRenderTarget();
         this.F();
         Object var5 = var3 ? this.K : var4;

         try {
            this.b();
            GpuSampler var6 = RenderSystem.getSamplerCache().getClampToEdge(FilterMode.NEAREST);
            RenderPass var7 = RenderSystem.getDevice()
               .createCommandEncoder()
               .createRenderPass(() -> "Larp Stream Proof sky", this.K.getColorTextureView(), Optional.empty());

            try {
               p7.K(var7, H);
               p7.t(var7, "Sky", this.I.getColorTextureView(), var6);
               p7.t(var7, "Depth", var5.getDepthTextureView(), var6);
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
            this.T("sky", var12);
         }
      }
   }

   void Q(long var1) {
      if (this.f == var1 && !this.b) {
         RenderTarget var3 = Minecraft.getInstance().gameRenderer.mainRenderTarget();

         try {
            this.k = Q(this.k, "Larp Stream Proof hand backdrop", var3.width, var3.height, false);
            nl.G(se.j, var3, this.k);
         } catch (RuntimeException var5) {
            this.T("hand backdrop", var5);
         }
      }
   }

   void m(long var1) {
      if (this.f == var1 && !this.b && this.k != null) {
         RenderTarget var3 = Minecraft.getInstance().gameRenderer.mainRenderTarget();

         try {
            this.b();
            GpuSampler var4 = RenderSystem.getSamplerCache().getClampToEdge(FilterMode.NEAREST);
            CommandEncoder var5 = RenderSystem.getDevice().createCommandEncoder();
            RenderPass var6 = var5.createRenderPass(() -> "Larp Stream Proof hand cover", this.K.getColorTextureView(), Optional.empty());

            try {
               p7.K(var6, c);
               p7.t(var6, "Scene", this.k.getColorTextureView(), var4);
               p7.t(var6, "Depth", var3.getDepthTextureView(), var4);
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
            this.T("hand cover", var11);
         }
      }
   }

   void u(long var1, Runnable var3) {
      if (this.f == var1 && !this.b) {
         RenderTarget var4 = Minecraft.getInstance().gameRenderer.mainRenderTarget();
         GpuTextureView var5 = jf.b;
         GpuTextureView var6 = jf.q;
         jf.b = this.K.getColorTextureView();
         jf.q = var4.getDepthTextureView();

         try {
            var3.run();
         } finally {
            jf.b = var5;
            jf.q = var6;
         }
      } else {
         var3.run();
      }
   }

   void W(boolean var1) {
      this.r = var1;
   }

   RenderTarget q(RenderTarget var1) {
      return (RenderTarget)(this.r && this.K != null ? this.K : var1);
   }

   void r(long var1) {
      this.t(var1);
      RenderTarget var3 = Minecraft.getInstance().gameRenderer.mainRenderTarget();
      this.V = Q(this.V, "Larp Stream Proof self depth", var3.width, var3.height, true);
      this.V.copyDepthFrom(var3);
      this.u = var1;
   }

   void U(long var1, Runnable var3) {
      if (this.u == var1 && this.V != null) {
         this.t(var1);
         GpuTextureView var4 = jf.b;
         GpuTextureView var5 = jf.q;
         RenderTarget var6 = nl.l;
         jf.b = this.K.getColorTextureView();
         jf.q = this.V.getDepthTextureView();
         nl.l = this.K;
         this.G++;

         try {
            var3.run();
         } finally {
            this.G--;
            jf.b = var4;
            jf.q = var5;
            nl.l = var6;
         }
      } else {
         this.n(var1, var3);
      }
   }

   void n(long var1, Runnable var3) {
      this.t(var1);
      this.F();
      GpuTextureView var4 = jf.b;
      GpuTextureView var5 = jf.q;
      RenderTarget var6 = nl.l;
      jf.b = this.K.getColorTextureView();
      jf.q = this.K.getDepthTextureView();
      nl.l = this.K;
      this.G++;

      try {
         var3.run();
      } finally {
         this.G--;
         jf.b = var4;
         jf.q = var5;
         nl.l = var6;
      }
   }

   void p(Runnable var1) {
      if (this.G == 0) {
         var1.run();
      } else {
         GpuTextureView var2 = jf.b;
         GpuTextureView var3 = jf.q;
         RenderTarget var4 = nl.l;
         int var5 = this.G;
         jf.b = null;
         jf.q = null;
         nl.l = null;
         this.G = 0;

         try {
            var1.run();
         } finally {
            this.G = var5;
            jf.b = var2;
            jf.q = var3;
            nl.l = var4;
         }
      }
   }

   boolean i(long var1, long var3, boolean var5, Consumer<RenderTarget> var6, is.e var7) {
      if (this.b) {
         this.y.reset();
         return false;
      }

      RenderTarget var8 = Minecraft.getInstance().gameRenderer.mainRenderTarget();
      String var9 = "resources";
      long var10 = he.s();

      try {
         this.b();
         int var12 = var8.width;
         int var13 = var8.height;
         this.i = Q(this.i, "Larp Stream Proof interface", var12, var13, true);
         this.C = Q(this.C, "Larp Stream Proof pack", var12, var13, false);
         CommandEncoder var14 = RenderSystem.getDevice().createCommandEncoder();
         var14.clearColorAndDepthTextures(this.i.getColorTexture(), m, this.i.getDepthTexture(), 0.0);
         var9 = "interface blur";
         var6.accept(this.i);
         var9 = "interface GUI";
         this.o().render();
         this.o().endFrame();
         var9 = "compose";
         int var15 = var5 && this.f == var1 && this.K != null && this.K.width == var12 && this.K.height == var13 ? 1 : 0;
         GpuSampler var16 = RenderSystem.getSamplerCache().getClampToEdge(FilterMode.NEAREST);
         RenderPass var17 = var14.createRenderPass(() -> "Larp Stream Proof compose", this.C.getColorTextureView(), Optional.empty());

         try {
            p7.K(var17, var15 != 0 ? A : s);
            p7.t(var17, "Interface", this.i.getColorTextureView(), var16);
            if (var15 != 0) {
               p7.t(var17, "Effects", this.K.getColorTextureView(), var16);
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
         return this.f(var3, var12, var13, var7);
      } catch (RuntimeException var27) {
         this.y.reset();
         this.T(var9, var27);
         return false;
      } finally {
         he.T("Stream Proof compose", var10);
      }
   }

   void e() {
      this.y.reset();
   }

   void E(RenderTarget var1) {
      if (nl.Z()) {
         this.F(var1, null);
      } else {
         for (int[] var3 : nl.E()) {
            this.F(var1, var3);
         }
      }
   }

   boolean P() {
      if (this.i != null && !this.b) {
         this.F(this.i, null);
         return true;
      } else {
         return false;
      }
   }

   private void F(RenderTarget var1, int[] var2) {
      GpuTextureView var3 = nl.w();
      if (var3 != null) {
         this.b();
         RenderPass var4 = RenderSystem.getDevice()
            .createCommandEncoder()
            .createRenderPass(() -> "Larp Stream Proof blur", var1.getColorTextureView(), Optional.empty());

         try {
            p7.K(var4, j);
            p7.t(var4, "Source", var3, RenderSystem.getSamplerCache().getClampToEdge(FilterMode.LINEAR));
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

   void n(List<GizmoInstance> var1, CameraRenderState var2, FeatureRenderDispatcher var3) {
      if (!this.b && !var1.isEmpty()) {
         long var4 = he.s();

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
            this.T("gizmo overlay", var27);
         } finally {
            he.T("Stream Proof gizmos", var4);
         }
      }
   }

   private boolean f(long var1, int var3, int var4, is.e var5) {
      is.i var6 = null;

      for (int var7 = 0; var7 < 3; var7++) {
         if (this.W[var7] == null) {
            this.W[var7] = new is.i();
         }

         is.i var8 = this.W[var7];
         if (!var8.t) {
            if (var8.z == null || var8.I != var3 || var8.r != var4) {
               if (var8.z != null) {
                  var8.z.close();
               }

               var8.z = RenderSystem.getDevice().createBuffer(() -> "Larp Stream Proof read-back", 9, (long)var3 * var4 * 4L);
               var8.I = var3;
               var8.r = var4;
            }

            var6 = var8;
            break;
         }
      }

      if (var6 == null) {
         he.Z("Stream Proof skipped read-backs", 1L);
         return false;
      } else {
         is.i var9 = var6;
         var9.t = (boolean)1;
         RenderSystem.getDevice().createCommandEncoder().copyTextureToBuffer(this.C.getColorTexture(), var9.z, 0L, () -> {
            try {
               if (!var9.h && var1 > this.l) {
                  MappedView var7x = var9.z.map(true, false);

                  try {
                     var5.h(var1, var3, var4, var7x.data());
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

                  this.l = var1;
               }
            } catch (RuntimeException var17) {
               _q.g("StreamProof", "Read-back delivery failed; the overlay keeps its previous frame", var17);
            } finally {
               var9.t = (boolean)0;
               if (var9.h && var9.z != null) {
                  var9.z.close();
                  var9.z = null;
               }
            }
         }, 0);
         return true;
      }
   }

   private GuiRenderer o() {
      if (this.x == null) {
         Minecraft var1 = Minecraft.getInstance();
         this.x = new GuiRenderer(
            this.y,
            var1.gameRenderer.featureRenderDispatcher(),
            List.of(
               new GuiEntityRenderer(var1.getEntityRenderDispatcher()),
               new GuiSkinRenderer(),
               new GuiBookModelRenderer(),
               new GuiBannerResultRenderer(var1.getAtlasManager()),
               new GuiProfilerChartRenderer()
            )
         );
         _q.c("StreamProof", "Interface GUI renderer created");
      }

      return this.x;
   }

   private void b() {
      if (!this.L) {
         for (RenderPipeline var4 : new RenderPipeline[]{A, s, j, c, H}) {
            se.j(var4);
         }

         this.L = (boolean)1;
         _q.z("StreamProof", "Layer pipelines compiled");
      }
   }

   private void T(String var1, RuntimeException var2) {
      this.b = (boolean)1;
      _q.o("StreamProof", "Layer stage=" + var1 + " failed; Stream Proof falls back to normal drawing until it is re-enabled", var2);
   }

   private static TextureTarget Q(TextureTarget var0, String var1, int var2, int var3, boolean var4) {
      if (var0 != null && var0.width == var2 && var0.height == var3) {
         return var0;
      }

      if (var0 != null) {
         var0.destroyBuffers();
      }

      return p7.m(var1, var2, var3, var4, GpuFormat.RGBA8_UNORM);
   }

   void U() {
      this.y.reset();
      if (this.x != null) {
         this.x.close();
         this.x = null;
      }

      for (TextureTarget var4 : new TextureTarget[]{this.i, this.K, this.I, this.V, this.k, this.C}) {
         if (var4 != null) {
            var4.destroyBuffers();
         }
      }

      this.i = this.K = this.I = this.V = this.k = this.C = null;
      this.r = (boolean)0;
      this.u = -1L;
      this.J = this.e = (boolean)0;

      for (int var5 = 0; var5 < 3; var5++) {
         is.i var6 = this.W[var5];
         if (var6 != null) {
            if (var6.t) {
               var6.h = (boolean)1;
            } else if (var6.z != null) {
               var6.z.close();
            }

            this.W[var5] = null;
         }
      }

      this.f = -1L;
      this.l = -1L;
      this.G = 0;
      this.b = (boolean)0;
      this.L = (boolean)0;
   }

   private static RenderPipeline y(String var0, String... var1) {
      return b(var0, ColorTargetState.DEFAULT, var1);
   }

   private static RenderPipeline b(String var0, ColorTargetState var1, String... var2) {
      Builder var3 = BindGroupLayout.builder();

      for (String var7 : var2) {
         var3.withUniform(var7, p7.X);
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

   interface e {
      void h(long var1, int var3, int var4, ByteBuffer var5);
   }

   private static final class i {
      GpuBuffer z;
      int I;
      int r;
      boolean t;
      boolean h;
   }
}
