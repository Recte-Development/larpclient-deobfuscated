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

final class he {
   private static final Vector4f D = new Vector4f(0.0F, 0.0F, 0.0F, 0.0F);
   private static final int N = 3;
   static final RenderPipeline b = E("streamproof_pack", "Interface", "Effects");
   static final RenderPipeline M = E("streamproof_pack_interface", "Interface");
   static final RenderPipeline i = E("streamproof_opaque_blur", "Source");
   static final RenderPipeline H = O("streamproof_hand_cover", C(), "Scene", "Depth");
   static final RenderPipeline O = O("streamproof_sky", C(), "Sky", "Depth");
   private final GuiRenderState l = new GuiRenderState();
   private GuiRenderer V;
   private TextureTarget U;
   private TextureTarget g;
   private TextureTarget m;
   private TextureTarget n;
   private TextureTarget e;
   private TextureTarget J;
   private boolean u;
   private long o = -1L;
   private final he.s[] q = new he.s[3];
   private boolean f;
   private boolean S;
   private boolean F;
   private boolean W;
   private long I = -1L;
   private long E = -1L;
   private int v;

   private static ColorTargetState C() {
      return new ColorTargetState(new BlendFunction(BlendFactor.ONE_MINUS_DST_ALPHA, BlendFactor.ONE, BlendFactor.ONE_MINUS_DST_ALPHA, BlendFactor.ONE));
   }

   GuiRenderState D() {
      return this.l;
   }

   boolean y(Object var1) {
      return var1 != null && var1 == this.V;
   }

   RenderTarget j() {
      return this.U;
   }

   boolean g() {
      return this.S;
   }

   boolean M() {
      return this.v > 0;
   }

   boolean M(long var1) {
      return this.I == var1;
   }

   RenderTarget b() {
      return this.v > 0 ? this.g : null;
   }

   boolean b() {
      boolean[] var1 = new boolean[]{false};
      this.l.forEachElement(var1x -> var1[0] = (boolean)1, TraverseRange.ALL);
      if (!var1[0]) {
         this.l.forEachText(var1x -> var1[0] = (boolean)1);
      }

      if (!var1[0]) {
         this.l.forEachItem(var1x -> var1[0] = (boolean)1);
      }

      if (!var1[0]) {
         this.l.forEachPictureInPicture(var1x -> var1[0] = (boolean)1);
      }

      return var1[0];
   }

   void k(long var1) {
      if (this.I != var1) {
         RenderTarget var3 = Minecraft.getInstance().gameRenderer.mainRenderTarget();
         this.g = l(this.g, "Larp Stream Proof effects", var3.width, var3.height, true);
         CommandEncoder var4 = RenderSystem.getDevice().createCommandEncoder();
         var4.clearColorTexture(this.g.getColorTexture(), D);
         this.I = var1;
         this.F = (boolean)0;
         this.W = (boolean)0;
      }
   }

   RenderTarget M(long var1) {
      this.k(var1);
      this.h();
      return this.g;
   }

   private void h() {
      if (!this.F) {
         this.g.copyDepthFrom(Minecraft.getInstance().gameRenderer.mainRenderTarget());
         this.F = (boolean)1;
      }
   }

   RenderTarget n(long var1) {
      this.k(var1);
      RenderTarget var3 = Minecraft.getInstance().gameRenderer.mainRenderTarget();
      this.m = l(this.m, "Larp Stream Proof sky", var3.width, var3.height, true);
      if (!this.W) {
         RenderSystem.getDevice().createCommandEncoder().clearColorAndDepthTextures(this.m.getColorTexture(), D, this.m.getDepthTexture(), 0.0);
         this.W = (boolean)1;
      }

      return this.m;
   }

   void J(long var1, boolean var3) {
      if (this.W && this.I == var1 && !this.S) {
         RenderTarget var4 = Minecraft.getInstance().gameRenderer.mainRenderTarget();
         this.h();
         Object var5 = var3 ? this.g : var4;

         try {
            this.Y();
            GpuSampler var6 = RenderSystem.getSamplerCache().getClampToEdge(FilterMode.NEAREST);
            RenderPass var7 = RenderSystem.getDevice()
               .createCommandEncoder()
               .createRenderPass(() -> "Larp Stream Proof sky", this.g.getColorTextureView(), Optional.empty());

            try {
               of.z(var7, O);
               of.h(var7, "Sky", this.m.getColorTextureView(), var6);
               of.h(var7, "Depth", var5.getDepthTextureView(), var6);
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
            this.C("sky", var12);
         }
      }
   }

   void t(long var1) {
      if (this.I == var1 && !this.S) {
         RenderTarget var3 = Minecraft.getInstance().gameRenderer.mainRenderTarget();

         try {
            this.e = l(this.e, "Larp Stream Proof hand backdrop", var3.width, var3.height, false);
            jk.y(e4.g, var3, this.e);
         } catch (RuntimeException var5) {
            this.C("hand backdrop", var5);
         }
      }
   }

   void Z(long var1) {
      if (this.I == var1 && !this.S && this.e != null) {
         RenderTarget var3 = Minecraft.getInstance().gameRenderer.mainRenderTarget();

         try {
            this.Y();
            GpuSampler var4 = RenderSystem.getSamplerCache().getClampToEdge(FilterMode.NEAREST);
            CommandEncoder var5 = RenderSystem.getDevice().createCommandEncoder();
            RenderPass var6 = var5.createRenderPass(() -> "Larp Stream Proof hand cover", this.g.getColorTextureView(), Optional.empty());

            try {
               of.z(var6, H);
               of.h(var6, "Scene", this.e.getColorTextureView(), var4);
               of.h(var6, "Depth", var3.getDepthTextureView(), var4);
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
            this.C("hand cover", var11);
         }
      }
   }

   void Z(long var1, Runnable var3) {
      if (this.I == var1 && !this.S) {
         RenderTarget var4 = Minecraft.getInstance().gameRenderer.mainRenderTarget();
         GpuTextureView var5 = _m.M;
         GpuTextureView var6 = _m.U;
         _m.M = this.g.getColorTextureView();
         _m.U = var4.getDepthTextureView();

         try {
            var3.run();
         } finally {
            _m.M = var5;
            _m.U = var6;
         }
      } else {
         var3.run();
      }
   }

   void Q(boolean var1) {
      this.u = var1;
   }

   RenderTarget V(RenderTarget var1) {
      return (RenderTarget)(this.u && this.g != null ? this.g : var1);
   }

   void m(long var1) {
      this.k(var1);
      RenderTarget var3 = Minecraft.getInstance().gameRenderer.mainRenderTarget();
      this.n = l(this.n, "Larp Stream Proof self depth", var3.width, var3.height, true);
      this.n.copyDepthFrom(var3);
      this.o = var1;
   }

   void z(long var1, Runnable var3) {
      if (this.o == var1 && this.n != null) {
         this.k(var1);
         GpuTextureView var4 = _m.M;
         GpuTextureView var5 = _m.U;
         RenderTarget var6 = jk.j;
         _m.M = this.g.getColorTextureView();
         _m.U = this.n.getDepthTextureView();
         jk.j = this.g;
         this.v++;

         try {
            var3.run();
         } finally {
            this.v--;
            _m.M = var4;
            _m.U = var5;
            jk.j = var6;
         }
      } else {
         this.P(var1, var3);
      }
   }

   void P(long var1, Runnable var3) {
      this.k(var1);
      this.h();
      GpuTextureView var4 = _m.M;
      GpuTextureView var5 = _m.U;
      RenderTarget var6 = jk.j;
      _m.M = this.g.getColorTextureView();
      _m.U = this.g.getDepthTextureView();
      jk.j = this.g;
      this.v++;

      try {
         var3.run();
      } finally {
         this.v--;
         _m.M = var4;
         _m.U = var5;
         jk.j = var6;
      }
   }

   void N(Runnable var1) {
      if (this.v == 0) {
         var1.run();
      } else {
         GpuTextureView var2 = _m.M;
         GpuTextureView var3 = _m.U;
         RenderTarget var4 = jk.j;
         int var5 = this.v;
         _m.M = null;
         _m.U = null;
         jk.j = null;
         this.v = 0;

         try {
            var1.run();
         } finally {
            this.v = var5;
            _m.M = var2;
            _m.U = var3;
            jk.j = var4;
         }
      }
   }

   boolean y(long var1, long var3, boolean var5, Consumer<RenderTarget> var6, he.l var7) {
      if (this.S) {
         this.l.reset();
         return false;
      }

      RenderTarget var8 = Minecraft.getInstance().gameRenderer.mainRenderTarget();
      String var9 = "resources";
      long var10 = h_.U();

      try {
         this.Y();
         int var12 = var8.width;
         int var13 = var8.height;
         this.U = l(this.U, "Larp Stream Proof interface", var12, var13, true);
         this.J = l(this.J, "Larp Stream Proof pack", var12, var13, false);
         CommandEncoder var14 = RenderSystem.getDevice().createCommandEncoder();
         var14.clearColorAndDepthTextures(this.U.getColorTexture(), D, this.U.getDepthTexture(), 0.0);
         var9 = "interface blur";
         var6.accept(this.U);
         var9 = "interface GUI";
         this.x().render();
         this.x().endFrame();
         var9 = "compose";
         int var15 = var5 && this.I == var1 && this.g != null && this.g.width == var12 && this.g.height == var13 ? 1 : 0;
         GpuSampler var16 = RenderSystem.getSamplerCache().getClampToEdge(FilterMode.NEAREST);
         RenderPass var17 = var14.createRenderPass(() -> "Larp Stream Proof compose", this.J.getColorTextureView(), Optional.empty());

         try {
            of.z(var17, var15 != 0 ? b : M);
            of.h(var17, "Interface", this.U.getColorTextureView(), var16);
            if (var15 != 0) {
               of.h(var17, "Effects", this.g.getColorTextureView(), var16);
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
         return this.d(var3, var12, var13, var7);
      } catch (RuntimeException var27) {
         this.l.reset();
         this.C(var9, var27);
         return false;
      } finally {
         h_.n("Stream Proof compose", var10);
      }
   }

   void l() {
      this.l.reset();
   }

   void R(RenderTarget var1) {
      if (jk.d()) {
         this.j(var1, null);
      } else {
         for (int[] var3 : jk.b()) {
            this.j(var1, var3);
         }
      }
   }

   boolean H() {
      if (this.U != null && !this.S) {
         this.j(this.U, null);
         return true;
      } else {
         return false;
      }
   }

   private void j(RenderTarget var1, int[] var2) {
      GpuTextureView var3 = jk.d();
      if (var3 != null) {
         this.Y();
         RenderPass var4 = RenderSystem.getDevice()
            .createCommandEncoder()
            .createRenderPass(() -> "Larp Stream Proof blur", var1.getColorTextureView(), Optional.empty());

         try {
            of.z(var4, i);
            of.h(var4, "Source", var3, RenderSystem.getSamplerCache().getClampToEdge(FilterMode.LINEAR));
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

   void D(List<GizmoInstance> var1, CameraRenderState var2, FeatureRenderDispatcher var3) {
      if (!this.S && !var1.isEmpty()) {
         long var4 = h_.U();

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
            this.C("gizmo overlay", var27);
         } finally {
            h_.n("Stream Proof gizmos", var4);
         }
      }
   }

   private boolean d(long var1, int var3, int var4, he.l var5) {
      he.s var6 = null;

      for (int var7 = 0; var7 < 3; var7++) {
         if (this.q[var7] == null) {
            this.q[var7] = new he.s();
         }

         he.s var8 = this.q[var7];
         if (!var8.l) {
            if (var8.K == null || var8.p != var3 || var8.t != var4) {
               if (var8.K != null) {
                  var8.K.close();
               }

               var8.K = RenderSystem.getDevice().createBuffer(() -> "Larp Stream Proof read-back", 9, (long)var3 * var4 * 4L);
               var8.p = var3;
               var8.t = var4;
            }

            var6 = var8;
            break;
         }
      }

      if (var6 == null) {
         h_.A("Stream Proof skipped read-backs", 1L);
         return false;
      } else {
         he.s var9 = var6;
         var9.l = (boolean)1;
         RenderSystem.getDevice().createCommandEncoder().copyTextureToBuffer(this.J.getColorTexture(), var9.K, 0L, () -> {
            try {
               if (!var9.U && var1 > this.E) {
                  MappedView var7x = var9.K.map(true, false);

                  try {
                     var5.p(var1, var3, var4, var7x.data());
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

                  this.E = var1;
               }
            } catch (RuntimeException var17) {
               r4.T("StreamProof", "Read-back delivery failed; the overlay keeps its previous frame", var17);
            } finally {
               var9.l = (boolean)0;
               if (var9.U && var9.K != null) {
                  var9.K.close();
                  var9.K = null;
               }
            }
         }, 0);
         return true;
      }
   }

   private GuiRenderer x() {
      if (this.V == null) {
         Minecraft var1 = Minecraft.getInstance();
         this.V = new GuiRenderer(
            this.l,
            var1.gameRenderer.featureRenderDispatcher(),
            List.of(
               new GuiEntityRenderer(var1.getEntityRenderDispatcher()),
               new GuiSkinRenderer(),
               new GuiBookModelRenderer(),
               new GuiBannerResultRenderer(var1.getAtlasManager()),
               new GuiProfilerChartRenderer()
            )
         );
         r4.g("StreamProof", "Interface GUI renderer created");
      }

      return this.V;
   }

   private void Y() {
      if (!this.f) {
         for (RenderPipeline var4 : new RenderPipeline[]{b, M, i, H, O}) {
            e4.U(var4);
         }

         this.f = (boolean)1;
         r4.V("StreamProof", "Layer pipelines compiled");
      }
   }

   private void C(String var1, RuntimeException var2) {
      this.S = (boolean)1;
      r4.r("StreamProof", "Layer stage=" + var1 + " failed; Stream Proof falls back to normal drawing until it is re-enabled", var2);
   }

   private static TextureTarget l(TextureTarget var0, String var1, int var2, int var3, boolean var4) {
      if (var0 != null && var0.width == var2 && var0.height == var3) {
         return var0;
      }

      if (var0 != null) {
         var0.destroyBuffers();
      }

      return of.T(var1, var2, var3, var4, GpuFormat.RGBA8_UNORM);
   }

   void u() {
      this.l.reset();
      if (this.V != null) {
         this.V.close();
         this.V = null;
      }

      for (TextureTarget var4 : new TextureTarget[]{this.U, this.g, this.m, this.n, this.e, this.J}) {
         if (var4 != null) {
            var4.destroyBuffers();
         }
      }

      this.U = this.g = this.m = this.n = this.e = this.J = null;
      this.u = (boolean)0;
      this.o = -1L;
      this.F = this.W = (boolean)0;

      for (int var5 = 0; var5 < 3; var5++) {
         he.s var6 = this.q[var5];
         if (var6 != null) {
            if (var6.l) {
               var6.U = (boolean)1;
            } else if (var6.K != null) {
               var6.K.close();
            }

            this.q[var5] = null;
         }
      }

      this.I = -1L;
      this.E = -1L;
      this.v = 0;
      this.S = (boolean)0;
      this.f = (boolean)0;
   }

   private static RenderPipeline E(String var0, String... var1) {
      return O(var0, ColorTargetState.DEFAULT, var1);
   }

   private static RenderPipeline O(String var0, ColorTargetState var1, String... var2) {
      Builder var3 = BindGroupLayout.builder();

      for (String var7 : var2) {
         var3.withUniform(var7, of.t);
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

   interface l {
      void p(long var1, int var3, int var4, ByteBuffer var5);
   }

   private static final class s {
      GpuBuffer K;
      int p;
      int t;
      boolean l;
      boolean U;
   }
}
