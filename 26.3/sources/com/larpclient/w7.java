package com.larpclient;

import com.larpclient.module.SelfVisualsModule;
import com.mojang.blaze3d.systems.RenderSystem;
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
import net.minecraft.client.resources.model.geometry.ItemQuads;
import net.minecraft.client.resources.model.geometry.BakedQuad.MaterialInfo;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;

public final class w7 implements InvocationHandler {
   private static final Map<w7.n, RenderType> Q = new LinkedHashMap<>();
   private static final Map<w7.c, RenderPipeline> V = new HashMap<>();
   private static final w7 l = new w7();
   private final SubmitNodeCollector v = (SubmitNodeCollector)Proxy.newProxyInstance(
      SubmitNodeCollector.class.getClassLoader(), new Class[]{SubmitNodeCollector.class}, this
   );
   private final PoseStack O = new PoseStack();
   private final CameraRenderState e = new CameraRenderState();
   private final QuadInstance N = new QuadInstance();
   private wh p;
   private w7.b y;
   private int d;
   private double M;
   private double Y;
   private double X;
   private boolean K;
   private boolean m;
   private boolean w;
   private SelfVisualsModule f;
   private int h = -1;
   private boolean C;
   private boolean L;
   private boolean c;
   private boolean k;
   private Pose A;
   private boolean R;
   private boolean j;
   private boolean H;
   private int U;
   private int D;
   private float F;
   private float q;
   private float Z;
   private float r;
   private float x;
   private float J;
   private float B;
   private float G;
   private float g;
   private float b;
   private final Matrix4f W = new Matrix4f();
   private static final Identifier s = Identifier.fromNamespaceAndPath("minecraft", "textures/block/white_concrete.png");
   private static final EnumSet<w7.b> T = EnumSet.range(w7.b.h, w7.b.V);

   public static void q(int var0, int var1) {
      w7 var2 = l;
      var2.U = var0;
      var2.D = var1;
      var2.F = var2.q = Float.POSITIVE_INFINITY;
      var2.Z = var2.r = Float.NEGATIVE_INFINITY;
      var2.j = (boolean)(!bj.f(var2.W) ? 1 : 0);
      if (var2.j) {
         if (!var2.H) {
            var2.H = (boolean)1;
            _q.R("Render", "World projection not captured; model post passes cover the whole screen");
         }
      } else {
         var2.W.mul(RenderSystem.getModelViewMatrixCopy());
         var2.R = (boolean)1;
         var2.e();
      }
   }

   public static int[] L() {
      w7 var0 = l;
      var0.R = (boolean)0;
      if (var0.j) {
         return null;
      }

      if (var0.F > var0.Z) {
         return new int[4];
      }

      int var1 = (int)Math.floor(var0.F);
      int var2 = (int)Math.floor(var0.q);
      return new int[]{var1, var2, (int)Math.ceil(var0.Z) - var1, (int)Math.ceil(var0.r) - var2};
   }

   private void e() {
      this.x = this.J = this.B = Float.POSITIVE_INFINITY;
      this.G = this.g = this.b = Float.NEGATIVE_INFINITY;
   }

   private void Z() {
      if (!(this.x > this.G) && !this.j) {
         if (this.x <= 0.0F && this.G >= 0.0F && this.J <= 0.0F && this.g >= 0.0F && this.B <= 0.0F && this.b >= 0.0F) {
            this.j = (boolean)1;
         } else {
            boolean var1 = RenderSystem.getDevice().getDeviceInfo().isZZeroToOne();
            iy.n var2 = iy.U(new AABB(this.x, this.J, this.B, this.G, this.g, this.b), Vec3.ZERO, this.W, this.U, this.D, var1);
            this.e();
            if (var2 != null) {
               this.F = Math.min(this.F, var2.left());
               this.Z = Math.max(this.Z, var2.right());
               this.q = Math.min(this.q, var2.top());
               this.r = Math.max(this.r, var2.bottom());
            }
         }
      }
   }

   public static EntityRenderState u(Entity var0, float var1) {
      return Minecraft.getInstance().getEntityRenderDispatcher().extractEntity(var0, var1);
   }

   public static boolean x() {
      return l.p != null || l.C;
   }

   public static Matrix4f e(EntityRenderState var0, PoseStack var1) {
      Pose var2 = M(var0, var1, false);
      if (var2 == null && !l.c) {
         l.c = (boolean)1;
         _q.R(
            "ChinaHat",
            "Native head attachment unavailable for this renderer; cone uses upright fallback and cap is skipped, retry after world/resource cleanup"
         );
      }

      return var2 == null ? null : new Matrix4f(var2.pose());
   }

   public static Pose a(EntityRenderState var0, PoseStack var1) {
      Pose var2 = M(var0, var1, true);
      if (var2 == null && !l.k) {
         l.k = (boolean)1;
         _q.R(
            "Wings",
            "Native body attachment unavailable for this renderer; Luminous uses its upright fallback and Ender Dragon is skipped, retry after world/resource cleanup"
         );
      }

      return var2;
   }

   private static Pose M(EntityRenderState var0, PoseStack var1, boolean var2) {
      w7 var3 = l;
      Minecraft var4 = Minecraft.getInstance();
      Vec3 var5 = var4.gameRenderer.mainCamera().position();
      var3.e.pos = var5;
      var3.e.orientation.set(var4.gameRenderer.mainCamera().rotation());
      var3.O.last().set(var1.last());
      var3.C = (boolean)1;
      var3.L = var2;
      var3.A = null;

      try {
         var4.getEntityRenderDispatcher().submit(var0, var3.e, var0.x - var5.x, var0.y - var5.y, var0.z - var5.z, var3.O, var3.v);
         return var3.A;
      } finally {
         var3.C = (boolean)0;
         var3.A = null;

         while (!var3.O.isEmpty()) {
            var3.O.popPose();
         }
      }
   }

   public static void A() {
      l.c = (boolean)0;
      l.k = (boolean)0;
      Q.clear();
      V.clear();
   }

   public static void V(ModelPart var0, PoseStack var1, wh var2, Identifier var3, int var4, double var5, double var7, boolean var9) {
      if (l.h != nl.q) {
         A();
         l.h = nl.q;
      }

      w7.b var10 = var7 > 0.0 ? (var9 ? w7.b.b : w7.b.d) : w7.b.n;
      if (!var9 || !(var7 <= 0.0)) {
         w7.x var11 = new w7.x(var2.w(l(var3, var10, false)), var10, var4, var5, 0.0, 0.0, null);
         var11.N = (int)(Math.clamp(var7, 0.0, 1.0) * 4096.0);
         var0.render(var1, var11, 15728880, OverlayTexture.NO_OVERLAY);
      }
   }

   public static void Q(EntityRenderState var0, PoseStack var1, wh var2, w7.b var3, int var4, double var5, boolean var7) {
      r(var0, var1, var2, var3, var4, var5, var7, true);
   }

   public static void r(EntityRenderState var0, PoseStack var1, wh var2, w7.b var3, int var4, double var5, boolean var7, boolean var8) {
      l.d(var0, var1, var2, var3, var4, var5, var7, var8);
   }

   private void d(EntityRenderState var1, PoseStack var2, wh var3, w7.b var4, int var5, double var6, boolean var8, boolean var9) {
      if (this.h != nl.q) {
         A();
         this.h = nl.q;
      }

      this.p = var3;
      this.y = var4;
      this.d = var5;
      this.M = var6;
      this.K = var8;
      this.w = var9;
      this.m = (boolean)(var4 == w7.b.j ? 1 : 0);
      this.f = var8 ? SelfVisualsModule.v() : null;
      Minecraft var10 = Minecraft.getInstance();
      Vec3 var11 = var10.gameRenderer.mainCamera().position();
      this.Y = var1.y - var11.y;
      this.X = var1.x - var11.x;
      this.e.pos = var11;
      this.e.orientation.set(var10.gameRenderer.mainCamera().rotation());
      this.O.last().set(var2.last());

      try {
         var10.getEntityRenderDispatcher().submit(var1, this.e, var1.x - var11.x, var1.y - var11.y, var1.z - var11.z, this.O, this.v);
      } catch (RuntimeException var16) {
         throw new IllegalStateException("ModelVisualRenderer stage=vanilla layer submission material=" + var4 + " entity=" + var1.entityType, var16);
      } finally {
         while (!this.O.isEmpty()) {
            this.O.popPose();
         }

         this.p = null;
         this.f = null;
         if (this.R) {
            this.Z();
         }
      }
   }

   @Override
   public Object invoke(Object var1, Method var2, Object[] var3) throws Throwable {
      if (var2.isDefault()) {
         return InvocationHandler.invokeDefault(var1, var2, var3);
      }

      if (this.C && var2.getDeclaringClass() != Object.class && !var2.getName().equals("submitModel") && !var2.getName().equals("order")) {
         return null;
      }

      switch (var2.getName()) {
         case "order":
            return this.v;
         case "submitModel":
            Model var17 = (Model)var3[0];
            Object var19 = var3[1];
            PoseStack var20 = (PoseStack)var3[2];
            if (this.C) {
               if (this.A == null && var17 instanceof PlayerModel var22) {
                  var17.setupAnim(var19);
                  var20.pushPose();

                  try {
                     (this.L ? var22.body : var22.head).translateAndRotate(var20);
                     this.A = var20.last().copy();
                  } finally {
                     var20.popPose();
                  }
               }

               return null;
            } else {
               RenderType var21 = (RenderType)var3[3];
               boolean var23 = var17 instanceof PlayerModel;
               if (this.K && var17 instanceof PlayerCapeModel) {
                  if (n(this.y) || this.f == null || !this.f.E()) {
                     return null;
                  }
               } else if (this.K && !this.m && !var23 && (this.f == null || (n(this.y) ? !py.C() : !this.f.v.getValue()))) {
                  return null;
               }

               VertexConsumer var24 = this.N(var21);
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
            if (!this.K || this.m || this.f != null && !this.f.m() && this.f.S.getValue()) {
               PoseStack var16 = (PoseStack)var3[0];
               int[] var18 = (int[])var3[5];
               this.N.setLightCoords((Integer)var3[2]);
               this.N.setOverlayCoords((Integer)var3[3]);

               for (BakedQuad var9 : T(var3[6])) {
                  MaterialInfo var10 = var9.materialInfo();
                  TextureBinding var11 = (TextureBinding)var10.itemRenderType().state.textures.get("Sampler0");
                  if (var11 != null) {
                     int var12 = var10.tintIndex();
                     this.N.setColor(var12 >= 0 && var12 < var18.length ? var18[var12] : -1);
                     this.a(var11.location()).putBakedQuad(var16.last(), var9, this.N);
                  }
               }

               return null;
            }

            return null;
         case "submitCustomGeometry":
            if (!this.K || this.m || this.f != null && !this.f.m() && this.f.S.getValue()) {
               RenderType var6 = (RenderType)var3[1];
               VertexConsumer var7 = this.N(var6);
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

   public static List<BakedQuad> T(Object var0) {
      return ((ItemQuads)var0).all();
   }

   public static SubmitNodeCollector v(SubmitNodeCollector var0) {
      SelfVisualsModule var1 = SelfVisualsModule.v();
      if (var1 != null && var1.isOperational() && var1.l() && !var1.m() && var1.S.getValue()) {
         w7.b var2 = var1.E();
         return var2 == null ? var0 : (SubmitNodeCollector)z(var0, var1, var2, true);
      } else {
         return var0;
      }
   }

   private static Object z(OrderedSubmitNodeCollector var0, SelfVisualsModule var1, w7.b var2, boolean var3) {
      Class<SubmitNodeCollector> var4 = var3 ? SubmitNodeCollector.class : OrderedSubmitNodeCollector.class;
      return Proxy.newProxyInstance(var4.getClassLoader(), new Class[]{var4}, (var3x, var4x, var5) -> {
         if (var4x.isDefault()) {
            return InvocationHandler.invokeDefault(var3x, var4x, var5);
         }

         if (var4x.getName().equals("order")) {
            return z(((SubmitNodeCollector)var0).order((Integer)var5[0]), var1, var2, false);
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
            List var10 = List.copyOf(T(var5[6]));
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
            double var16 = var1.r.getValue();

            for (Entry var19 : var11.entrySet()) {
               List var20 = List.copyOf((Collection)var19.getValue());
               var0.submitCustomGeometry(var6, l((Identifier)var19.getKey(), var2, true), (var12, var13x) -> {
                  QuadInstance var14x = new QuadInstance();
                  var14x.setLightCoords(var7);
                  var14x.setOverlayCoords(var8);
                  w7.x var15 = new w7.x(var13x, var2, com.larpclient.c.y, var16, var22, var23, var1);

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

   public static boolean I(ModelPart var0, PoseStack var1, SubmitNodeCollector var2, RenderType var3, int var4, int var5, UvMapping var6) {
      SelfVisualsModule var7 = SelfVisualsModule.v();
      if (var7 != null && var7.isOperational() && var7.l() && var7.g.getValue() && (!var7.m() || py.Q())) {
         TextureBinding var8 = (TextureBinding)var3.state.textures.get("Sampler0");
         if (var8 == null) {
            return false;
         }

         w7.b var9 = var7.E() == null ? w7.b.E : var7.E();
         double var10 = var1.last().pose().m31();
         double var12 = var1.last().pose().m30();
         var2.submitCustomGeometry(var1, l(var8.location(), var9, true), (var10x, var11) -> {
            long var12x = he.s();
            PoseStack var14 = new PoseStack();
            var14.last().set(var10x);
            VertexConsumer var15 = new w7.x(var11, var9, com.larpclient.c.y, var7.v(), var12, var10, var7);
            if (var6 != null) {
               var15 = var6.wrap(var15);
            }

            try {
               var0.render(var14, var15, var4, var5);
            } catch (RuntimeException var20) {
               _q.g("SelfVisuals", "First-person arm stage=geometry shader=model/material failed; restoring vanilla on next frame", var20);
               if (var7.m()) {
                  py.m(var20);
               } else {
                  var7.disable();
               }
            } finally {
               he.T("SelfVisuals", var12x);
            }
         });
         return true;
      } else {
         return false;
      }
   }

   public static VertexConsumer Q(wh var0, w7.b var1) {
      if (!k(var1)) {
         throw new IllegalArgumentException("World surfaces use chams styles, not " + var1);
      }

      if (l.h != nl.q) {
         A();
         l.h = nl.q;
      }

      return var0.w(l(s, var1, false));
   }

   public static void S(
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
      var0.setColor(vv.O(var10, var12));
      var0.setUv(var5, var6);
      var0.setUv1(var11 >> 8 & 65535, var11 & 0xFF | (int)Math.round(Math.clamp(var14, 0.0, 1.0) * 255.0) << 8);
      var0.setUv2(Math.floorMod((long)(var18 * 4096.0), 4096), (int)Math.round(var16 * 256.0));
      var0.setNormal(var1, var7, var8, var9);
   }

   public static void v() {
      l(Identifier.fromNamespaceAndPath("minecraft", "textures/entity/player/wide/steve.png"), w7.b.I, true);
   }

   public static boolean n(w7.b var0) {
      return var0 == w7.b.I || var0 == w7.b.T || var0 == w7.b.L;
   }

   public static boolean k(w7.b var0) {
      return T.contains(var0);
   }

   private VertexConsumer a(Identifier var1) {
      return new w7.x(
         this.p.w(l(var1, this.y, this.y == w7.b.c || this.K && !this.m && this.y != w7.b.T && this.y != w7.b.L)),
         this.y,
         this.d,
         this.M,
         this.X,
         this.Y,
         this.f
      );
   }

   static boolean q(w7.b var0, boolean var1, boolean var2) {
      return var1 && var2 && (var0 == w7.b.J || var0 == w7.b.Q || var0 == w7.b.E || var0 == w7.b.n || var0 == w7.b.I || k(var0) && var0 != w7.b.V);
   }

   private VertexConsumer N(RenderType var1) {
      if (var1.pipeline() == RenderPipelines.GLINT) {
         return q(this.y, this.K, this.w) ? this.p.w(var1) : null;
      }

      TextureBinding var2 = (TextureBinding)var1.state.textures.get("Sampler0");
      return var2 == null ? null : this.a(var2.location());
   }

   private static RenderType l(Identifier var0, w7.b var1, boolean var2) {
      w7.n var3 = new w7.n(var0, var1, var2);
      RenderType var4 = Q.get(var3);
      if (var4 != null) {
         return var4;
      }

      if (Q.size() >= 256) {
         Q.clear();
      }

      w7.c var5 = new w7.c(var1, var2);
      RenderPipeline var6 = V.get(var5);
      if (var6 == null) {
         Builder var7 = RenderPipeline.builder(new Snippet[]{RenderPipelines.GUI_TEXTURED_SNIPPET})
            .withLocation(Identifier.fromNamespaceAndPath("larp-client", "model_" + var1.name().toLowerCase(Locale.ROOT) + (var2 ? "_solid" : "_transparent")))
            .withFragmentShader(Identifier.fromNamespaceAndPath("larp-client", "model/material"))
            .withVertexShader(Identifier.fromNamespaceAndPath("larp-client", "model/material"))
            .withVertexBinding(0, DefaultVertexFormat.ENTITY)
            .withDepthStencilState(new DepthStencilState(var1 == w7.b.y ? CompareOp.LESS_THAN : CompareOp.GREATER_THAN_OR_EQUAL, var2))
            .withCull(var1 != w7.b.j && var1 != w7.b.c);
         if (var1 == w7.b.c) {
            var7.withColorTargetState(ColorTargetState.DEFAULT);
            var7.withShaderDefine("CHAMS_SURFACE");
         }

         if (n(var1)) {
            var7.withShaderDefine("LIQUID_GLASS");
         }

         if (var1 == w7.b.T) {
            var7.withShaderDefine("GLASS_MASK");
         }

         if (var1 == w7.b.L) {
            var7.withShaderDefine("GLASS_GLOW");
         }

         if (var1 == w7.b.T) {
            var7.withColorTargetState(new ColorTargetState(Optional.empty(), GpuFormat.RGBA16_FLOAT, 15));
         }

         if (var1 == w7.b.J) {
            var7.withShaderDefine("RAINBOW");
         }

         if (var1 == w7.b.j || var1 == w7.b.q || var1 == w7.b.l || var1 == w7.b.y) {
            var7.withShaderDefine("SILHOUETTE");
         }

         if (var1 == w7.b.Q) {
            var7.withShaderDefine("HOLOGRAM");
         }

         if (var1 == w7.b.j) {
            var7.withShaderDefine("DEPTH_BIAS");
         }

         if (k(var1)) {
            var7.withShaderDefine("CHAMS_STYLE");
            var7.withShaderDefine("STYLE_" + var1.name().substring("CHAMS_".length()));
         }

         if (var1 == w7.b.l || var1 == w7.b.y) {
            var7.withShaderDefine("CHAMS");
         }

         if (var1 == w7.b.d || var1 == w7.b.b) {
            var7.withShaderDefine("DISSOLVE");
         }

         if (var1 == w7.b.b) {
            var7.withShaderDefine("DISSOLVE_GLOW");
         }

         var6 = var7.build();
         se.j(var6);
         V.put(var5, var6);
         _q.z("Shader", "Player material initialized: {} shader=model/material", var1);
      }

      RenderType var8 = RenderType.create("larp_model_" + var1, RenderSetup.builder(var6).withTexture("Sampler0", var0).sortOnUpload().createRenderSetup());
      Q.put(var3, var8);
      return var8;
   }

   public enum b {
      n,
      E,
      Q,
      q,
      J,
      j,
      l,
      y,
      d,
      b,
      I,
      T,
      L,
      c,
      h,
      a,
      s,
      U,
      w,
      R,
      V;
   }

   private record c(w7.b material, boolean depthWrite) {
   }

   private record n(Identifier texture, w7.b material, boolean depthWrite) {
   }

   static final class x implements VertexConsumer {
      private final VertexConsumer U;
      private final w7.b d;
      private final int B;
      private final double S;
      private final double t;
      private final double w;
      private final boolean i;
      private final boolean G;
      private final boolean r;
      private final boolean b;
      private final boolean T;
      private final int X;
      private final int W;
      private final int H;
      private final int g;
      private final int y;
      private final double I;
      private final double E;
      private final double k;
      private final int v;
      private float M;
      private float F;
      private int N;
      private int P = -1;

      x(VertexConsumer var1, w7.b var2, int var3, double var4, double var6, double var8, SelfVisualsModule var10) {
         this.U = var1;
         this.d = var2;
         this.B = var3;
         this.S = var4;
         this.t = var6;
         this.w = var8;
         this.b = (boolean)(w7.n(var2) && var10 != null ? 1 : 0);
         this.X = py.z();
         this.W = py.i();
         this.I = this.b ? py.O(var10) : 0.0;
         this.T = (boolean)(w7.k(var2) && var10 != null ? 1 : 0);
         int var11 = this.T ? var10.W() : 0;
         this.H = var11 >> 8 & 65535;
         this.g = this.T ? var11 & 0xFF | (int)Math.round(var10.z.getValue() * 255.0) << 8 : 0;
         this.y = this.T ? (int)Math.round(var10.u.getValue() * 256.0) : 0;
         this.i = (boolean)(var2 == w7.b.J && var10 != null ? 1 : 0);
         this.G = (boolean)(this.i && var10.I.w("Horizontal") ? 1 : 0);
         this.E = this.i ? var10.R.getValue() : 0.0;
         this.k = this.T
            ? hz.Q() * var10.i.getValue() % 1.0
            : (this.i && !var10.I.w("Static") ? hz.Q() * var10.H.getValue() * (var10.I.w("Upward") ? -1 : 1) % 1.0 : 0.0);
         this.r = (boolean)(!this.i && !this.b && !this.T && var2 != w7.b.j && var2 != w7.b.q && var2 != w7.b.l && var2 != w7.b.c && var2 != w7.b.y ? 0 : 1);
         int var12 = this.i
            ? 0xFF000000 | (int)(var10.l.getValue() * 255.0) << 16 | (int)(var10.L.getValue() * 255.0) << 8
            : (this.b ? py.R(var10, 1.0) : (this.T ? var10.v() : var3));
         this.v = vv.O(var12, var4);
      }

      public VertexConsumer addVertex(float var1, float var2, float var3) {
         this.M = var1;
         this.F = var2;
         w7 var4 = w7.l;
         if (var4.R) {
            var4.x = Math.min(var4.x, var1);
            var4.G = Math.max(var4.G, var1);
            var4.J = Math.min(var4.J, var2);
            var4.g = Math.max(var4.g, var2);
            var4.B = Math.min(var4.B, var3);
            var4.b = Math.max(var4.b, var3);
         }

         if (this.b) {
            this.P++;
         }

         this.U.addVertex(var1, var2, var3);
         return this;
      }

      public VertexConsumer setColor(int var1, int var2, int var3, int var4) {
         return this.setColor(var4 << 24 | var1 << 16 | var2 << 8 | var3);
      }

      public VertexConsumer setColor(int var1) {
         int var2 = this.N;
         if (this.b) {
            var2 = (int)(this.I * 4096.0);
         } else if (this.T) {
            var2 = (int)(this.k * 4096.0);
         } else if (this.i) {
            double var3 = this.G ? this.M - this.t : this.F - this.w;
            double var5 = var3 * this.E + this.k;
            var2 = Math.clamp(Math.round(var5 * 4096.0), -32768, 32767);
         }

         int var7 = this.P & 3;
         this.U.setUv2(var2, this.b ? (var7 == 2 ? 3 : (var7 == 3 ? 2 : var7)) : (this.T ? this.y : 0));
         this.U.setColor(this.r ? this.v : vv.O(this.d == w7.b.n ? var1 : vv.Q(var1, this.B, 0.85), this.S));
         return this;
      }

      public VertexConsumer setUv(float var1, float var2) {
         this.U.setUv(var1, var2);
         return this;
      }

      public VertexConsumer setUv1(int var1, int var2) {
         this.U.setUv1(this.b ? this.X : (this.T ? this.H : var1), this.b ? this.W : (this.T ? this.g : var2));
         return this;
      }

      public VertexConsumer setUv3(float var1, float var2) {
         return this;
      }

      public VertexConsumer setUv2(int var1, int var2) {
         return this;
      }

      public VertexConsumer setNormal(float var1, float var2, float var3) {
         this.U.setNormal(var1, var2, var3);
         return this;
      }

      public VertexConsumer setLineWidth(float var1) {
         return this;
      }
   }
}
