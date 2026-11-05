package com.larpclient;

import com.larpclient.module.SelfVisualsModule;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.QuadInstance;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.PoseStack.Pose;
import com.mojang.renderpearl.api.GpuFormat;
import com.mojang.renderpearl.api.pipeline.ColorTargetState;
import com.mojang.renderpearl.api.pipeline.CompareOp;
import com.mojang.renderpearl.api.pipeline.DepthStencilState;
import com.mojang.renderpearl.api.pipeline.RenderPipeline;
import com.mojang.renderpearl.api.pipeline.RenderPipeline.Builder;
import com.mojang.renderpearl.api.pipeline.RenderPipeline.Snippet;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Map.Entry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.player.PlayerCapeModel;
import net.minecraft.client.model.player.PlayerModel;
import net.minecraft.client.renderer.OrderedSubmitNodeCollector;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.SubmitNodeCollector.CustomGeometryRenderer;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.rendertype.RenderSetup;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderSetup.TextureBinding;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.UvMapping;
import net.minecraft.client.resources.model.geometry.BakedQuad;
import net.minecraft.client.resources.model.geometry.BakedQuad.MaterialInfo;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;

public final class tj implements InvocationHandler {
   private static final Map<tj.u, RenderType> k = new LinkedHashMap<>();
   private static final Map<tj.e, RenderPipeline> W = new HashMap<>();
   private static final tj Y = new tj();
   private final SubmitNodeCollector O = (SubmitNodeCollector)Proxy.newProxyInstance(
      SubmitNodeCollector.class.getClassLoader(), new Class[]{SubmitNodeCollector.class}, this
   );
   private final PoseStack v = new PoseStack();
   private final CameraRenderState E = new CameraRenderState();
   private final QuadInstance s = new QuadInstance();
   private xh r;
   private tj.b J;
   private int I;
   private double C;
   private double m;
   private double t;
   private boolean R;
   private boolean i;
   private boolean Q;
   private SelfVisualsModule S;
   private int f = -1;
   private boolean h;
   private boolean q;
   private boolean o;
   private boolean K;
   private Pose x;
   private static final Identifier U = Identifier.fromNamespaceAndPath("minecraft", "textures/block/white_concrete.png");
   private static final EnumSet<tj.b> b = EnumSet.range(tj.b.z, tj.b.s);

   public static EntityRenderState s(Entity var0, float var1) {
      return Minecraft.getInstance().getEntityRenderDispatcher().extractEntity(var0, var1);
   }

   public static boolean r() {
      return Y.r != null || Y.h;
   }

   public static Matrix4f Z(EntityRenderState var0, PoseStack var1) {
      Pose var2 = b(var0, var1, false);
      if (var2 == null && !Y.o) {
         Y.o = (boolean)1;
         mt.T(
            "ChinaHat",
            "Native head attachment unavailable for this renderer; cone uses upright fallback and cap is skipped, retry after world/resource cleanup"
         );
      }

      return var2 == null ? null : new Matrix4f(var2.pose());
   }

   public static Pose o(EntityRenderState var0, PoseStack var1) {
      Pose var2 = b(var0, var1, true);
      if (var2 == null && !Y.K) {
         Y.K = (boolean)1;
         mt.T(
            "Wings",
            "Native body attachment unavailable for this renderer; Luminous uses its upright fallback and Ender Dragon is skipped, retry after world/resource cleanup"
         );
      }

      return var2;
   }

   private static Pose b(EntityRenderState var0, PoseStack var1, boolean var2) {
      tj var3 = Y;
      Minecraft var4 = Minecraft.getInstance();
      Vec3 var5 = var4.gameRenderer.mainCamera().position();
      var3.E.pos = var5;
      var3.E.orientation.set(var4.gameRenderer.mainCamera().rotation());
      var3.v.last().set(var1.last());
      var3.h = (boolean)1;
      var3.q = var2;
      var3.x = null;

      try {
         var4.getEntityRenderDispatcher().submit(var0, var3.E, var0.x - var5.x, var0.y - var5.y, var0.z - var5.z, var3.v, var3.O);
         return var3.x;
      } finally {
         var3.h = (boolean)0;
         var3.x = null;

         while (!var3.v.isEmpty()) {
            var3.v.popPose();
         }
      }
   }

   public static void Q() {
      Y.o = (boolean)0;
      Y.K = (boolean)0;
      k.clear();
      W.clear();
   }

   public static void t(ModelPart var0, PoseStack var1, xh var2, Identifier var3, int var4, double var5, double var7, boolean var9) {
      if (Y.f != m5.k) {
         Q();
         Y.f = m5.k;
      }

      tj.b var10 = var7 > 0.0 ? (var9 ? tj.b.N : tj.b.U) : tj.b.u;
      if (!var9 || !(var7 <= 0.0)) {
         tj.r var11 = new tj.r(var2.U(a(var3, var10, false)), var10, var4, var5, 0.0, 0.0, null);
         var11.X = (int)(Math.clamp(var7, 0.0, 1.0) * 4096.0);
         var0.render(var1, var11, 15728880, OverlayTexture.NO_OVERLAY);
      }
   }

   public static void u(EntityRenderState var0, PoseStack var1, xh var2, tj.b var3, int var4, double var5, boolean var7) {
      E(var0, var1, var2, var3, var4, var5, var7, true);
   }

   public static void E(EntityRenderState var0, PoseStack var1, xh var2, tj.b var3, int var4, double var5, boolean var7, boolean var8) {
      Y.a(var0, var1, var2, var3, var4, var5, var7, var8);
   }

   private void a(EntityRenderState var1, PoseStack var2, xh var3, tj.b var4, int var5, double var6, boolean var8, boolean var9) {
      if (this.f != m5.k) {
         Q();
         this.f = m5.k;
      }

      this.r = var3;
      this.J = var4;
      this.I = var5;
      this.C = var6;
      this.R = var8;
      this.Q = var9;
      this.i = (boolean)(var4 == tj.b.V ? 1 : 0);
      this.S = var8 ? SelfVisualsModule.V() : null;
      Minecraft var10 = Minecraft.getInstance();
      Vec3 var11 = var10.gameRenderer.mainCamera().position();
      this.m = var1.y - var11.y;
      this.t = var1.x - var11.x;
      this.E.pos = var11;
      this.E.orientation.set(var10.gameRenderer.mainCamera().rotation());
      this.v.last().set(var2.last());

      try {
         var10.getEntityRenderDispatcher().submit(var1, this.E, var1.x - var11.x, var1.y - var11.y, var1.z - var11.z, this.v, this.O);
      } catch (RuntimeException var16) {
         throw new IllegalStateException("ModelVisualRenderer stage=vanilla layer submission material=" + var4 + " entity=" + var1.entityType, var16);
      } finally {
         while (!this.v.isEmpty()) {
            this.v.popPose();
         }

         this.r = null;
         this.S = null;
      }
   }

   @Override
   public Object invoke(Object var1, Method var2, Object[] var3) throws Throwable {
      if (var2.isDefault()) {
         return InvocationHandler.invokeDefault(var1, var2, var3);
      }

      if (this.h && var2.getDeclaringClass() != Object.class && !var2.getName().equals("submitModel") && !var2.getName().equals("order")) {
         return null;
      }

      switch (var2.getName()) {
         case "order":
            return this.O;
         case "submitModel":
            Model var17 = (Model)var3[0];
            Object var19 = var3[1];
            PoseStack var20 = (PoseStack)var3[2];
            if (this.h) {
               if (this.x == null && var17 instanceof PlayerModel var22) {
                  var17.setupAnim(var19);
                  var20.pushPose();

                  try {
                     (this.q ? var22.body : var22.head).translateAndRotate(var20);
                     this.x = var20.last().copy();
                  } finally {
                     var20.popPose();
                  }
               }

               return null;
            } else {
               RenderType var21 = (RenderType)var3[3];
               boolean var23 = var17 instanceof PlayerModel;
               if (this.R && var17 instanceof PlayerCapeModel) {
                  if (A(this.J) || this.S == null || !this.S.W()) {
                     return null;
                  }
               } else if (this.R && !this.i && !var23 && (this.S == null || (A(this.J) ? !p7.o() : !this.S.D.P()))) {
                  return null;
               }

               VertexConsumer var24 = this.d(var21);
               if (var24 == null) {
                  return null;
               }

               if (var3[7] instanceof UvMapping var25) {
                  var24 = var25.wrap(var24);
               }

               var17.setupAnim(var19);
               var17.renderToBuffer(var20, var24, (Integer)var3[4], (Integer)var3[5], (Integer)var3[6]);
               return null;
            }
         case "submitItem":
            if (!this.R || this.i || this.S != null && !this.S.B() && this.S.c.P()) {
               PoseStack var16 = (PoseStack)var3[0];
               int[] var18 = (int[])var3[5];
               this.s.setLightCoords((Integer)var3[2]);
               this.s.setOverlayCoords((Integer)var3[3]);

               for (BakedQuad var9 : (List)var3[6]) {
                  MaterialInfo var10 = var9.materialInfo();
                  TextureBinding var11 = (TextureBinding)var10.itemRenderType().state.textures.get("Sampler0");
                  if (var11 != null) {
                     int var12 = var10.tintIndex();
                     this.s.setColor(var12 >= 0 && var12 < var18.length ? var18[var12] : -1);
                     this.i(var11.location()).putBakedQuad(var16.last(), var9, this.s);
                  }
               }

               return null;
            }

            return null;
         case "submitCustomGeometry":
            if (!this.R || this.i || this.S != null && !this.S.B() && this.S.c.P()) {
               RenderType var6 = (RenderType)var3[1];
               VertexConsumer var7 = this.d(var6);
               if (var7 != null) {
                  ((CustomGeometryRenderer)var3[2]).render(((PoseStack)var3[0]).last(), var7);
               }

               return null;
            }

            return null;
         case "toString":
            return "Larp visual model collector";
         case "hashCode":
            return System.identityHashCode(this);
         case "equals":
            return var1 == var3[0];
         default:
            return null;
      }
   }

   public static SubmitNodeCollector z(SubmitNodeCollector var0) {
      SelfVisualsModule var1 = SelfVisualsModule.V();
      if (var1 != null && var1.isOperational() && var1.v() && !var1.B() && var1.c.P()) {
         tj.b var2 = var1.S();
         return var2 == null ? var0 : (SubmitNodeCollector)f(var0, var1, var2, true);
      } else {
         return var0;
      }
   }

   private static Object f(OrderedSubmitNodeCollector var0, SelfVisualsModule var1, tj.b var2, boolean var3) {
      Class<SubmitNodeCollector> var4 = var3 ? SubmitNodeCollector.class : OrderedSubmitNodeCollector.class;
      return Proxy.newProxyInstance(var4.getClassLoader(), new Class[]{var4}, (var3x, var4x, var5) -> {
         if (var4x.isDefault()) {
            return InvocationHandler.invokeDefault(var3x, var4x, var5);
         }

         if (var4x.getName().equals("order")) {
            return f(((SubmitNodeCollector)var0).order((Integer)var5[0]), var1, var2, false);
         }

         if (!var4x.getName().equals("submitItem")) {
            try {
               return var4x.invoke(var0, var5);
            } catch (InvocationTargetException var21) {
               throw var21.getCause();
            }
         } else {
            PoseStack var6 = (PoseStack)var5[0];
            int var7 = (Integer)var5[2];
            int var8 = (Integer)var5[3];
            int[] var9 = (int[])((int[])var5[5]).clone();
            List var10 = List.copyOf((List)var5[6]);
            LinkedHashMap var11 = new LinkedHashMap();

            for (BakedQuad var13 : var10) {
               TextureBinding var14 = (TextureBinding)var13.materialInfo().itemRenderType().state.textures.get("Sampler0");
               if (var14 == null) {
                  return var4x.invoke(var0, var5);
               }

               var11.computeIfAbsent(var14.location(), var0xx -> new ArrayList<>()).add(var13);
            }

            double var22 = var6.last().pose().m30();
            double var23 = var6.last().pose().m31();
            double var16 = var1.W.P();

            for (Entry var19 : var11.entrySet()) {
               List var20 = List.copyOf((Collection)var19.getValue());
               var0.submitCustomGeometry(var6, a((Identifier)var19.getKey(), var2, true), (var12, var13x) -> {
                  QuadInstance var14x = new QuadInstance();
                  var14x.setLightCoords(var7);
                  var14x.setOverlayCoords(var8);
                  tj.r var15 = new tj.r(var13x, var2, com.larpclient.f.C, var16, var22, var23, var1);

                  for (BakedQuad var17 : var20) {
                     int var18 = var17.materialInfo().tintIndex();
                     var14x.setColor(var18 >= 0 && var18 < var9.length ? var9[var18] : -1);
                     var15.putBakedQuad(var12, var17, var14x);
                  }
               });
            }

            return null;
         }
      });
   }

   public static boolean H(ModelPart var0, PoseStack var1, SubmitNodeCollector var2, RenderType var3, int var4, int var5, UvMapping var6) {
      SelfVisualsModule var7 = SelfVisualsModule.V();
      if (var7 != null && var7.isOperational() && var7.v() && var7.s.P() && (!var7.B() || p7.B())) {
         TextureBinding var8 = (TextureBinding)var3.state.textures.get("Sampler0");
         if (var8 == null) {
            return false;
         }

         tj.b var9 = var7.S() == null ? tj.b.n : var7.S();
         double var10 = var1.last().pose().m31();
         double var12 = var1.last().pose().m30();
         var2.submitCustomGeometry(var1, a(var8.location(), var9, true), (var10x, var11) -> {
            long var12x = kx.G();
            PoseStack var14 = new PoseStack();
            var14.last().set(var10x);
            VertexConsumer var15 = new tj.r(var11, var9, com.larpclient.f.C, var7.A(), var12, var10, var7);
            if (var6 != null) {
               var15 = var6.wrap(var15);
            }

            try {
               var0.render(var14, var15, var4, var5);
            } catch (RuntimeException var20) {
               mt.B("SelfVisuals", "First-person arm stage=geometry shader=model/material failed; restoring vanilla on next frame", var20);
               if (var7.B()) {
                  p7.i(var20);
               } else {
                  var7.disable();
               }
            } finally {
               kx.u("SelfVisuals", var12x);
            }
         });
         return true;
      } else {
         return false;
      }
   }

   public static VertexConsumer W(xh var0, tj.b var1) {
      if (!I(var1)) {
         throw new IllegalArgumentException("World surfaces use chams styles, not " + var1);
      }

      if (Y.f != m5.k) {
         Q();
         Y.f = m5.k;
      }

      return var0.U(a(U, var1, false));
   }

   public static void n(
      VertexConsumer var0,
      Pose var1,
      float var2,
      float var3,
      float var4,
      float var5,
      float var6,
      float var7,
      float var8,
      float var9,
      int var10,
      int var11,
      double var12,
      double var14,
      double var16,
      double var18
   ) {
      var0.addVertex(var1, var2, var3, var4);
      var0.setColor(_3.U(var10, var12));
      var0.setUv(var5, var6);
      var0.setUv1(var11 >> 8 & 65535, var11 & 0xFF | (int)Math.round(Math.clamp(var14, 0.0, 1.0) * 255.0) << 8);
      var0.setUv2(Math.floorMod((long)(var18 * 4096.0), 4096), (int)Math.round(var16 * 256.0));
      var0.setNormal(var1, var7, var8, var9);
   }

   public static void O() {
      a(Identifier.fromNamespaceAndPath("minecraft", "textures/entity/player/wide/steve.png"), tj.b.X, true);
   }

   public static boolean A(tj.b var0) {
      return var0 == tj.b.X || var0 == tj.b.j || var0 == tj.b.T;
   }

   public static boolean I(tj.b var0) {
      return b.contains(var0);
   }

   private VertexConsumer i(Identifier var1) {
      return new tj.r(
         this.r.U(a(var1, this.J, this.J == tj.b.k || this.R && !this.i && this.J != tj.b.j && this.J != tj.b.T)),
         this.J,
         this.I,
         this.C,
         this.t,
         this.m,
         this.S
      );
   }

   static boolean P(tj.b var0, boolean var1, boolean var2) {
      return var1 && var2 && (var0 == tj.b.r || var0 == tj.b.g || var0 == tj.b.n || var0 == tj.b.u || var0 == tj.b.X || I(var0) && var0 != tj.b.s);
   }

   private VertexConsumer d(RenderType var1) {
      if (var1.pipeline() == RenderPipelines.GLINT) {
         return P(this.J, this.R, this.Q) ? this.r.U(var1) : null;
      }

      TextureBinding var2 = (TextureBinding)var1.state.textures.get("Sampler0");
      return var2 == null ? null : this.i(var2.location());
   }

   private static RenderType a(Identifier var0, tj.b var1, boolean var2) {
      tj.u var3 = new tj.u(var0, var1, var2);
      RenderType var4 = k.get(var3);
      if (var4 != null) {
         return var4;
      }

      if (k.size() >= 256) {
         k.clear();
      }

      tj.e var5 = new tj.e(var1, var2);
      RenderPipeline var6 = W.get(var5);
      if (var6 == null) {
         Builder var7 = RenderPipeline.builder(new Snippet[]{RenderPipelines.GUI_TEXTURED_SNIPPET})
            .withLocation(Identifier.fromNamespaceAndPath("larp-client", "model_" + var1.name().toLowerCase(Locale.ROOT) + (var2 ? "_solid" : "_transparent")))
            .withFragmentShader(Identifier.fromNamespaceAndPath("larp-client", "model/material"))
            .withVertexShader(Identifier.fromNamespaceAndPath("larp-client", "model/material"))
            .withVertexBinding(0, DefaultVertexFormat.ENTITY)
            .withDepthStencilState(new DepthStencilState(var1 == tj.b.L ? CompareOp.LESS_THAN : CompareOp.GREATER_THAN_OR_EQUAL, var2))
            .withCull(var1 != tj.b.V && var1 != tj.b.k);
         if (var1 == tj.b.k) {
            var7.withColorTargetState(ColorTargetState.DEFAULT);
            var7.withShaderDefine("CHAMS_SURFACE");
         }

         if (A(var1)) {
            var7.withShaderDefine("LIQUID_GLASS");
         }

         if (var1 == tj.b.j) {
            var7.withShaderDefine("GLASS_MASK");
         }

         if (var1 == tj.b.T) {
            var7.withShaderDefine("GLASS_GLOW");
         }

         if (var1 == tj.b.j) {
            var7.withColorTargetState(new ColorTargetState(Optional.empty(), GpuFormat.RGBA16_FLOAT, 15));
         }

         if (var1 == tj.b.r) {
            var7.withShaderDefine("RAINBOW");
         }

         if (var1 == tj.b.V || var1 == tj.b.i || var1 == tj.b.p || var1 == tj.b.L) {
            var7.withShaderDefine("SILHOUETTE");
         }

         if (var1 == tj.b.g) {
            var7.withShaderDefine("HOLOGRAM");
         }

         if (var1 == tj.b.V) {
            var7.withShaderDefine("DEPTH_BIAS");
         }

         if (I(var1)) {
            var7.withShaderDefine("CHAMS_STYLE");
            var7.withShaderDefine("STYLE_" + var1.name().substring("CHAMS_".length()));
         }

         if (var1 == tj.b.p || var1 == tj.b.L) {
            var7.withShaderDefine("CHAMS");
         }

         if (var1 == tj.b.U || var1 == tj.b.N) {
            var7.withShaderDefine("DISSOLVE");
         }

         if (var1 == tj.b.N) {
            var7.withShaderDefine("DISSOLVE_GLOW");
         }

         var6 = var7.build();
         ea.z(var6);
         W.put(var5, var6);
         mt.K("Shader", "Player material initialized: {} shader=model/material", var1);
      }

      RenderType var8 = RenderType.create("larp_model_" + var1, RenderSetup.builder(var6).withTexture("Sampler0", var0).sortOnUpload().createRenderSetup());
      k.put(var3, var8);
      return var8;
   }

   public enum b {
      u,
      n,
      g,
      i,
      r,
      V,
      p,
      L,
      U,
      N,
      X,
      j,
      T,
      k,
      z,
      b,
      I,
      d,
      Z,
      B,
      s;
   }

   private record e(tj.b material, boolean depthWrite) {
   }

   static final class r implements VertexConsumer {
      private final VertexConsumer v;
      private final tj.b i;
      private final int Q;
      private final double D;
      private final double e;
      private final double n;
      private final boolean w;
      private final boolean s;
      private final boolean H;
      private final boolean I;
      private final boolean R;
      private final int m;
      private final int J;
      private final int p;
      private final int Z;
      private final int S;
      private final double r;
      private final double W;
      private final double K;
      private final int B;
      private float C;
      private float k;
      private int X;
      private int O = -1;

      r(VertexConsumer var1, tj.b var2, int var3, double var4, double var6, double var8, SelfVisualsModule var10) {
         this.v = var1;
         this.i = var2;
         this.Q = var3;
         this.D = var4;
         this.e = var6;
         this.n = var8;
         this.I = (boolean)(tj.A(var2) && var10 != null ? 1 : 0);
         this.m = p7.L();
         this.J = p7.I();
         this.r = this.I ? p7.D(var10) : 0.0;
         this.R = (boolean)(tj.I(var2) && var10 != null ? 1 : 0);
         int var11 = this.R ? var10.k() : 0;
         this.p = var11 >> 8 & 65535;
         this.Z = this.R ? var11 & 0xFF | (int)Math.round(var10.N.P() * 255.0) << 8 : 0;
         this.S = this.R ? (int)Math.round(var10.l.P() * 256.0) : 0;
         this.w = (boolean)(var2 == tj.b.r && var10 != null ? 1 : 0);
         this.s = (boolean)(this.w && var10.A.S("Horizontal") ? 1 : 0);
         this.W = this.w ? var10.g.P() : 0.0;
         this.K = this.R ? ty.K() * var10.J.P() % 1.0 : (this.w && !var10.A.S("Static") ? ty.K() * var10.I.P() * (var10.A.S("Upward") ? -1 : 1) % 1.0 : 0.0);
         this.H = (boolean)(!this.w && !this.I && !this.R && var2 != tj.b.V && var2 != tj.b.i && var2 != tj.b.p && var2 != tj.b.k && var2 != tj.b.L ? 0 : 1);
         int var12 = this.w
            ? 0xFF000000 | (int)(var10.q.P() * 255.0) << 16 | (int)(var10.S.P() * 255.0) << 8
            : (this.I ? p7.e(var10, 1.0) : (this.R ? var10.n() : var3));
         this.B = _3.U(var12, var4);
      }

      public VertexConsumer addVertex(float var1, float var2, float var3) {
         this.C = var1;
         this.k = var2;
         if (this.I) {
            this.O++;
         }

         this.v.addVertex(var1, var2, var3);
         return this;
      }

      public VertexConsumer setColor(int var1, int var2, int var3, int var4) {
         return this.setColor(var4 << 24 | var1 << 16 | var2 << 8 | var3);
      }

      public VertexConsumer setColor(int var1) {
         int var2 = this.X;
         if (this.I) {
            var2 = (int)(this.r * 4096.0);
         } else if (this.R) {
            var2 = (int)(this.K * 4096.0);
         } else if (this.w) {
            double var3 = this.s ? this.C - this.e : this.k - this.n;
            double var5 = var3 * this.W + this.K;
            var2 = Math.clamp(Math.round(var5 * 4096.0), -32768, 32767);
         }

         int var7 = this.O & 3;
         this.v.setUv2(var2, this.I ? (var7 == 2 ? 3 : (var7 == 3 ? 2 : var7)) : (this.R ? this.S : 0));
         this.v.setColor(this.H ? this.B : _3.U(this.i == tj.b.u ? var1 : _3.D(var1, this.Q, 0.85), this.D));
         return this;
      }

      public VertexConsumer setUv(float var1, float var2) {
         this.v.setUv(var1, var2);
         return this;
      }

      public VertexConsumer setUv1(int var1, int var2) {
         this.v.setUv1(this.I ? this.m : (this.R ? this.p : var1), this.I ? this.J : (this.R ? this.Z : var2));
         return this;
      }

      public VertexConsumer setUv3(float var1, float var2) {
         return this;
      }

      public VertexConsumer setUv2(int var1, int var2) {
         return this;
      }

      public VertexConsumer setNormal(float var1, float var2, float var3) {
         this.v.setNormal(var1, var2, var3);
         return this;
      }

      public VertexConsumer setLineWidth(float var1) {
         return this;
      }
   }

   private record u(Identifier texture, tj.b material, boolean depthWrite) {
   }
}
