package com.larpclient;

import com.mojang.blaze3d.GpuFormat;
import com.mojang.blaze3d.PrimitiveTopology;
import com.mojang.blaze3d.buffers.GpuBuffer;
import com.mojang.blaze3d.buffers.GpuBufferSlice.MappedView;
import com.mojang.blaze3d.pipeline.BindGroupLayout;
import com.mojang.blaze3d.pipeline.BlendFunction;
import com.mojang.blaze3d.pipeline.ColorTargetState;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.pipeline.TextureTarget;
import com.mojang.blaze3d.pipeline.BindGroupLayout.Builder;
import com.mojang.blaze3d.pipeline.RenderPipeline.Snippet;
import com.mojang.blaze3d.platform.BlendFactor;
import com.mojang.blaze3d.systems.CommandEncoder;
import com.mojang.blaze3d.systems.RenderPass;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.textures.FilterMode;
import com.mojang.blaze3d.textures.GpuSampler;
import com.mojang.blaze3d.textures.GpuTextureView;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.Optional;
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
import net.minecraft.client.renderer.gizmos.DrawableGizmoPrimitives;
import net.minecraft.client.renderer.state.gui.GuiRenderState;
import net.minecraft.client.renderer.state.gui.GuiRenderState.TraverseRange;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.gizmos.SimpleGizmoCollector.GizmoInstance;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Util;
import org.joml.Vector4f;

final class pp {
   private static final Vector4f l = new Vector4f(0.0F, 0.0F, 0.0F, 0.0F);
   private static final int d = 3;
   static final RenderPipeline r = f("streamproof_pack", "Interface", "Effects");
   static final RenderPipeline u = f("streamproof_pack_interface", "Interface");
   static final RenderPipeline s = f("streamproof_opaque_blur", "Source");
   static final RenderPipeline K = f("streamproof_hand_cover", o(), "Scene", "Depth");
   static final RenderPipeline U = f("streamproof_sky", o(), "Sky", "Depth");
   private final GuiRenderState p = new GuiRenderState();
   private GuiRenderer a;
   private TextureTarget J;
   private TextureTarget i;
   private TextureTarget I;
   private TextureTarget T;
   private TextureTarget X;
   private TextureTarget w;
   private boolean j;
   private long Q = -1L;
   private final pp.c[] F = new pp.c[3];
   private boolean e;
   private boolean P;
   private boolean R;
   private boolean G;
   private long x = -1L;
   private long k = -1L;
   private int v;

   private static ColorTargetState o() {
      return new ColorTargetState(new BlendFunction(BlendFactor.ONE_MINUS_DST_ALPHA, BlendFactor.ONE, BlendFactor.ONE_MINUS_DST_ALPHA, BlendFactor.ONE));
   }

   GuiRenderState v() {
      return this.p;
   }

   boolean W(Object var1) {
      return var1 != null && var1 == this.a;
   }

   RenderTarget E() {
      return this.J;
   }

   boolean r() {
      return this.P;
   }

   boolean z() {
      return this.v > 0;
   }

   boolean w(long var1) {
      return this.x == var1;
   }

   RenderTarget P() {
      return this.v > 0 ? this.i : null;
   }

   boolean m() {
      boolean[] var1 = new boolean[]{false};
      this.p.forEachElement(var1x -> var1[0] = (boolean)1, TraverseRange.ALL);
      if (!var1[0]) {
         this.p.forEachText(var1x -> var1[0] = (boolean)1);
      }

      if (!var1[0]) {
         this.p.forEachItem(var1x -> var1[0] = (boolean)1);
      }

      if (!var1[0]) {
         this.p.forEachPictureInPicture(var1x -> var1[0] = (boolean)1);
      }

      return var1[0];
   }

   void P(long var1) {
      if (this.x != var1) {
         RenderTarget var3 = Minecraft.getInstance().gameRenderer.mainRenderTarget();
         this.i = s(this.i, "Larp Stream Proof effects", var3.width, var3.height, true);
         CommandEncoder var4 = RenderSystem.getDevice().createCommandEncoder();
         var4.clearColorTexture(this.i.getColorTexture(), l);
         this.x = var1;
         this.R = (boolean)0;
         this.G = (boolean)0;
      }
   }

   RenderTarget R(long var1) {
      this.P(var1);
      this.U();
      return this.i;
   }

   private void U() {
      if (!this.R) {
         this.i.copyDepthFrom(Minecraft.getInstance().gameRenderer.mainRenderTarget());
         this.R = (boolean)1;
      }
   }

   RenderTarget d(long var1) {
      this.P(var1);
      RenderTarget var3 = Minecraft.getInstance().gameRenderer.mainRenderTarget();
      this.I = s(this.I, "Larp Stream Proof sky", var3.width, var3.height, true);
      if (!this.G) {
         RenderSystem.getDevice().createCommandEncoder().clearColorAndDepthTextures(this.I.getColorTexture(), l, this.I.getDepthTexture(), 0.0);
         this.G = (boolean)1;
      }

      return this.I;
   }

   void A(long var1, boolean var3) {
      if (this.G && this.x == var1 && !this.P) {
         RenderTarget var4 = Minecraft.getInstance().gameRenderer.mainRenderTarget();
         this.U();
         Object var5 = var3 ? this.i : var4;

         try {
            this.M();
            GpuSampler var6 = RenderSystem.getSamplerCache().getClampToEdge(FilterMode.NEAREST);
            RenderPass var7 = RenderSystem.getDevice()
               .createCommandEncoder()
               .createRenderPass(() -> "Larp Stream Proof sky", this.i.getColorTextureView(), Optional.empty());

            try {
               mx.i(var7, U);
               mx.x(var7, "Sky", this.I.getColorTextureView(), var6);
               mx.x(var7, "Depth", var5.getDepthTextureView(), var6);
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
            this.I("sky", var12);
         }
      }
   }

   void X(long var1) {
      if (this.x == var1 && !this.P) {
         RenderTarget var3 = Minecraft.getInstance().gameRenderer.mainRenderTarget();

         try {
            this.X = s(this.X, "Larp Stream Proof hand backdrop", var3.width, var3.height, false);
            wa.Y(ai.C, var3, this.X);
         } catch (RuntimeException var5) {
            this.I("hand backdrop", var5);
         }
      }
   }

   void B(long var1) {
      if (this.x == var1 && !this.P && this.X != null) {
         RenderTarget var3 = Minecraft.getInstance().gameRenderer.mainRenderTarget();

         try {
            this.M();
            GpuSampler var4 = RenderSystem.getSamplerCache().getClampToEdge(FilterMode.NEAREST);
            CommandEncoder var5 = RenderSystem.getDevice().createCommandEncoder();
            RenderPass var6 = var5.createRenderPass(() -> "Larp Stream Proof hand cover", this.i.getColorTextureView(), Optional.empty());

            try {
               mx.i(var6, K);
               mx.x(var6, "Scene", this.X.getColorTextureView(), var4);
               mx.x(var6, "Depth", var3.getDepthTextureView(), var4);
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
            this.I("hand cover", var11);
         }
      }
   }

   void N(long var1, Runnable var3) {
      if (this.x == var1 && !this.P) {
         RenderTarget var4 = Minecraft.getInstance().gameRenderer.mainRenderTarget();
         GpuTextureView var5 = RenderSystem.outputColorTextureOverride;
         GpuTextureView var6 = RenderSystem.outputDepthTextureOverride;
         RenderSystem.outputColorTextureOverride = this.i.getColorTextureView();
         RenderSystem.outputDepthTextureOverride = var4.getDepthTextureView();

         try {
            var3.run();
         } finally {
            RenderSystem.outputColorTextureOverride = var5;
            RenderSystem.outputDepthTextureOverride = var6;
         }
      } else {
         var3.run();
      }
   }

   void i(boolean var1) {
      this.j = var1;
   }

   RenderTarget K(RenderTarget var1) {
      return (RenderTarget)(this.j && this.i != null ? this.i : var1);
   }

   void E(long var1) {
      this.P(var1);
      RenderTarget var3 = Minecraft.getInstance().gameRenderer.mainRenderTarget();
      this.T = s(this.T, "Larp Stream Proof self depth", var3.width, var3.height, true);
      this.T.copyDepthFrom(var3);
      this.Q = var1;
   }

   void n(long var1, Runnable var3) {
      if (this.Q == var1 && this.T != null) {
         this.P(var1);
         GpuTextureView var4 = RenderSystem.outputColorTextureOverride;
         GpuTextureView var5 = RenderSystem.outputDepthTextureOverride;
         RenderTarget var6 = wa.B;
         RenderSystem.outputColorTextureOverride = this.i.getColorTextureView();
         RenderSystem.outputDepthTextureOverride = this.T.getDepthTextureView();
         wa.B = this.i;
         this.v++;

         try {
            var3.run();
         } finally {
            this.v--;
            RenderSystem.outputColorTextureOverride = var4;
            RenderSystem.outputDepthTextureOverride = var5;
            wa.B = var6;
         }
      } else {
         this.u(var1, var3);
      }
   }

   void u(long var1, Runnable var3) {
      this.P(var1);
      this.U();
      GpuTextureView var4 = RenderSystem.outputColorTextureOverride;
      GpuTextureView var5 = RenderSystem.outputDepthTextureOverride;
      RenderTarget var6 = wa.B;
      RenderSystem.outputColorTextureOverride = this.i.getColorTextureView();
      RenderSystem.outputDepthTextureOverride = this.i.getDepthTextureView();
      wa.B = this.i;
      this.v++;

      try {
         var3.run();
      } finally {
         this.v--;
         RenderSystem.outputColorTextureOverride = var4;
         RenderSystem.outputDepthTextureOverride = var5;
         wa.B = var6;
      }
   }

   void E(Runnable var1) {
      if (this.v == 0) {
         var1.run();
      } else {
         GpuTextureView var2 = RenderSystem.outputColorTextureOverride;
         GpuTextureView var3 = RenderSystem.outputDepthTextureOverride;
         RenderTarget var4 = wa.B;
         int var5 = this.v;
         RenderSystem.outputColorTextureOverride = null;
         RenderSystem.outputDepthTextureOverride = null;
         wa.B = null;
         this.v = 0;

         try {
            var1.run();
         } finally {
            this.v = var5;
            RenderSystem.outputColorTextureOverride = var2;
            RenderSystem.outputDepthTextureOverride = var3;
            wa.B = var4;
         }
      }
   }

   boolean p(long var1, long var3, boolean var5, Consumer<RenderTarget> var6, pp.u var7) {
      if (this.P) {
         this.p.reset();
         return false;
      }

      RenderTarget var8 = Minecraft.getInstance().gameRenderer.mainRenderTarget();
      String var9 = "resources";
      long var10 = zj.m();

      try {
         this.M();
         int var12 = var8.width;
         int var13 = var8.height;
         this.J = s(this.J, "Larp Stream Proof interface", var12, var13, true);
         this.w = s(this.w, "Larp Stream Proof pack", var12, var13, false);
         CommandEncoder var14 = RenderSystem.getDevice().createCommandEncoder();
         var14.clearColorAndDepthTextures(this.J.getColorTexture(), l, this.J.getDepthTexture(), 0.0);
         var9 = "interface blur";
         var6.accept(this.J);
         var9 = "interface GUI";
         this.z().render();
         this.z().endFrame();
         var9 = "compose";
         int var15 = var5 && this.x == var1 && this.i != null && this.i.width == var12 && this.i.height == var13 ? 1 : 0;
         GpuSampler var16 = RenderSystem.getSamplerCache().getClampToEdge(FilterMode.NEAREST);
         RenderPass var17 = var14.createRenderPass(() -> "Larp Stream Proof compose", this.w.getColorTextureView(), Optional.empty());

         try {
            mx.i(var17, var15 != 0 ? r : u);
            mx.x(var17, "Interface", this.J.getColorTextureView(), var16);
            if (var15 != 0) {
               mx.x(var17, "Effects", this.i.getColorTextureView(), var16);
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
         return this.t(var3, var12, var13, var7);
      } catch (RuntimeException var27) {
         this.p.reset();
         this.I(var9, var27);
         return false;
      } finally {
         zj.l("Stream Proof compose", var10);
      }
   }

   void V() {
      this.p.reset();
   }

   void V(RenderTarget var1) {
      if (wa.p()) {
         this.w(var1, null);
      } else {
         for (int[] var3 : wa.u()) {
            this.w(var1, var3);
         }
      }
   }

   boolean i() {
      if (this.J != null && !this.P) {
         this.w(this.J, null);
         return true;
      } else {
         return false;
      }
   }

   private void w(RenderTarget var1, int[] var2) {
      GpuTextureView var3 = wa.o();
      if (var3 != null) {
         this.M();
         RenderPass var4 = RenderSystem.getDevice()
            .createCommandEncoder()
            .createRenderPass(() -> "Larp Stream Proof blur", var1.getColorTextureView(), Optional.empty());

         try {
            mx.i(var4, s);
            mx.x(var4, "Source", var3, RenderSystem.getSamplerCache().getClampToEdge(FilterMode.LINEAR));
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

   void l(List<GizmoInstance> var1, CameraRenderState var2, FeatureRenderDispatcher var3) {
      if (!this.P && !var1.isEmpty()) {
         long var4 = zj.m();

         try {
            long var6 = Util.getMillis();
            DrawableGizmoPrimitives var8 = new DrawableGizmoPrimitives();
            DrawableGizmoPrimitives var9 = new DrawableGizmoPrimitives();

            for (GizmoInstance var11 : var1) {
               var11.gizmo().emit(var11.isAlwaysOnTop() ? var9 : var8, var11.getAlphaMultiplier(var6));
            }

            SubmitNodeStorage var17 = new SubmitNodeStorage();
            var8.submit(var17, var2, false);
            var9.submit(var17, var2, true);
            var3.renderAllFeatures(var17);
         } catch (RuntimeException var15) {
            this.I("gizmo overlay", var15);
         } finally {
            zj.l("Stream Proof gizmos", var4);
         }
      }
   }

   private boolean t(long var1, int var3, int var4, pp.u var5) {
      pp.c var6 = null;

      for (int var7 = 0; var7 < 3; var7++) {
         if (this.F[var7] == null) {
            this.F[var7] = new pp.c();
         }

         pp.c var8 = this.F[var7];
         if (!var8.P) {
            if (var8.f == null || var8.u != var3 || var8.d != var4) {
               if (var8.f != null) {
                  var8.f.close();
               }

               var8.f = RenderSystem.getDevice().createBuffer(() -> "Larp Stream Proof read-back", 9, (long)var3 * var4 * 4L);
               var8.u = var3;
               var8.d = var4;
            }

            var6 = var8;
            break;
         }
      }

      if (var6 == null) {
         zj.K("Stream Proof skipped read-backs", 1L);
         return false;
      } else {
         pp.c var9 = var6;
         var9.P = (boolean)1;
         RenderSystem.getDevice().createCommandEncoder().copyTextureToBuffer(this.w.getColorTexture(), var9.f, 0L, () -> {
            try {
               if (!var9.C && var1 > this.k) {
                  MappedView var7x = var9.f.map(true, false);

                  try {
                     var5.J(var1, var3, var4, var7x.data());
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

                  this.k = var1;
               }
            } catch (RuntimeException var17) {
               h.w("StreamProof", "Read-back delivery failed; the overlay keeps its previous frame", var17);
            } finally {
               var9.P = (boolean)0;
               if (var9.C && var9.f != null) {
                  var9.f.close();
                  var9.f = null;
               }
            }
         }, 0);
         return true;
      }
   }

   private GuiRenderer z() {
      if (this.a == null) {
         Minecraft var1 = Minecraft.getInstance();
         this.a = new GuiRenderer(
            this.p,
            var1.gameRenderer.featureRenderDispatcher(),
            List.of(
               new GuiEntityRenderer(var1.getEntityRenderDispatcher()),
               new GuiSkinRenderer(),
               new GuiBookModelRenderer(),
               new GuiBannerResultRenderer(var1.getAtlasManager()),
               new GuiProfilerChartRenderer()
            )
         );
         h.U("StreamProof", "Interface GUI renderer created");
      }

      return this.a;
   }

   private void M() {
      if (!this.e) {
         for (RenderPipeline var4 : new RenderPipeline[]{r, u, s, K, U}) {
            ai.U(var4);
         }

         this.e = (boolean)1;
         h.A("StreamProof", "Layer pipelines compiled");
      }
   }

   private void I(String var1, RuntimeException var2) {
      this.P = (boolean)1;
      h.m("StreamProof", "Layer stage=" + var1 + " failed; Stream Proof falls back to normal drawing until it is re-enabled", var2);
   }

   private static TextureTarget s(TextureTarget var0, String var1, int var2, int var3, boolean var4) {
      if (var0 != null && var0.width == var2 && var0.height == var3) {
         return var0;
      }

      if (var0 != null) {
         var0.destroyBuffers();
      }

      return mx.n(var1, var2, var3, var4, GpuFormat.RGBA8_UNORM);
   }

   void u() {
      this.p.reset();
      if (this.a != null) {
         this.a.close();
         this.a = null;
      }

      for (TextureTarget var4 : new TextureTarget[]{this.J, this.i, this.I, this.T, this.X, this.w}) {
         if (var4 != null) {
            var4.destroyBuffers();
         }
      }

      this.J = this.i = this.I = this.T = this.X = this.w = null;
      this.j = (boolean)0;
      this.Q = -1L;
      this.R = this.G = (boolean)0;

      for (int var5 = 0; var5 < 3; var5++) {
         pp.c var6 = this.F[var5];
         if (var6 != null) {
            if (var6.P) {
               var6.C = (boolean)1;
            } else if (var6.f != null) {
               var6.f.close();
            }

            this.F[var5] = null;
         }
      }

      this.x = -1L;
      this.k = -1L;
      this.v = 0;
      this.P = (boolean)0;
      this.e = (boolean)0;
   }

   private static RenderPipeline f(String var0, String... var1) {
      return f(var0, ColorTargetState.DEFAULT, var1);
   }

   private static RenderPipeline f(String var0, ColorTargetState var1, String... var2) {
      Builder var3 = BindGroupLayout.builder();

      for (String var7 : var2) {
         var3.withSampler(var7);
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

   private static final class c {
      GpuBuffer f;
      int u;
      int d;
      boolean P;
      boolean C;
   }

   interface u {
      void J(long var1, int var3, int var4, ByteBuffer var5);
   }
}
