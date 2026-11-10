package com.larpclient;

import com.larpclient.module.SelfVisualsModule;
import com.mojang.blaze3d.GpuFormat;
import com.mojang.blaze3d.pipeline.ColorTargetState;
import com.mojang.blaze3d.pipeline.DepthStencilState;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.pipeline.RenderPipeline.Builder;
import com.mojang.blaze3d.pipeline.RenderPipeline.Snippet;
import com.mojang.blaze3d.platform.CompareOp;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.QuadInstance;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.PoseStack.Pose;
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
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.geometry.BakedQuad;
import net.minecraft.client.resources.model.geometry.BakedQuad.MaterialInfo;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;

public final class j4 implements InvocationHandler {
   private static final Map<j4.e, RenderType> o = new LinkedHashMap<>();
   private static final Map<j4.f, RenderPipeline> H = new HashMap<>();
   private static final j4 Q = new j4();
   private final SubmitNodeCollector e = (SubmitNodeCollector)Proxy.newProxyInstance(
      SubmitNodeCollector.class.getClassLoader(), new Class[]{SubmitNodeCollector.class}, this
   );
   private final PoseStack W = new PoseStack();
   private final CameraRenderState h = new CameraRenderState();
   private final QuadInstance G = new QuadInstance();
   private hh M;
   private j4.j v;
   private int C;
   private double J;
   private double A;
   private double f;
   private boolean B;
   private boolean d;
   private boolean w;
   private SelfVisualsModule O;
   private int P = -1;
   private boolean p;
   private boolean u;
   private boolean a;
   private boolean F;
   private Pose y;
   private boolean g;
   private boolean s;
   private boolean i;
   private int K;
   private int b;
   private float m;
   private float N;
   private float t;
   private float q;
   private float S;
   private float Y;
   private float T;
   private float R;
   private float I;
   private float z;
   private final Matrix4f j = new Matrix4f();
   private static final Identifier x = Identifier.fromNamespaceAndPath("minecraft", "textures/block/white_concrete.png");
   private static final EnumSet<j4.j> n = EnumSet.range(j4.j.K, j4.j.F);

   public static void a(int var0, int var1) {
      j4 var2 = Q;
      var2.K = var0;
      var2.b = var1;
      var2.m = var2.N = Float.POSITIVE_INFINITY;
      var2.t = var2.q = Float.NEGATIVE_INFINITY;
      var2.s = (boolean)(!bu.H(var2.j) ? 1 : 0);
      if (var2.s) {
         if (!var2.i) {
            var2.i = (boolean)1;
            com.larpclient.h.g("Render", "World projection not captured; model post passes cover the whole screen");
         }
      } else {
         var2.j.mul(RenderSystem.getModelViewMatrixCopy());
         var2.g = (boolean)1;
         var2.w();
      }
   }

   public static int[] m() {
      j4 var0 = Q;
      var0.g = (boolean)0;
      if (var0.s) {
         return null;
      }

      if (var0.m > var0.t) {
         return new int[4];
      }

      int var1 = (int)Math.floor(var0.m);
      int var2 = (int)Math.floor(var0.N);
      return new int[]{var1, var2, (int)Math.ceil(var0.t) - var1, (int)Math.ceil(var0.q) - var2};
   }

   private void w() {
      this.S = this.Y = this.T = Float.POSITIVE_INFINITY;
      this.R = this.I = this.z = Float.NEGATIVE_INFINITY;
   }

   private void a() {
      if (!(this.S > this.R) && !this.s) {
         if (this.S <= 0.0F && this.R >= 0.0F && this.Y <= 0.0F && this.I >= 0.0F && this.T <= 0.0F && this.z >= 0.0F) {
            this.s = (boolean)1;
         } else {
            boolean var1 = RenderSystem.getDevice().getDeviceInfo().isZZeroToOne();
            k7.z var2 = k7.X(new AABB(this.S, this.Y, this.T, this.R, this.I, this.z), Vec3.ZERO, this.j, this.K, this.b, var1);
            this.w();
            if (var2 != null) {
               this.m = Math.min(this.m, var2.left());
               this.t = Math.max(this.t, var2.right());
               this.N = Math.min(this.N, var2.top());
               this.q = Math.max(this.q, var2.bottom());
            }
         }
      }
   }

   public static EntityRenderState d(Entity var0, float var1) {
      return Minecraft.getInstance().getEntityRenderDispatcher().extractEntity(var0, var1);
   }

   public static boolean X() {
      return Q.M != null || Q.p;
   }

   public static Matrix4f i(EntityRenderState var0, PoseStack var1) {
      Pose var2 = B(var0, var1, false);
      if (var2 == null && !Q.a) {
         Q.a = (boolean)1;
         com.larpclient.h.g(
            "ChinaHat",
            "Native head attachment unavailable for this renderer; cone uses upright fallback and cap is skipped, retry after world/resource cleanup"
         );
      }

      return var2 == null ? null : new Matrix4f(var2.pose());
   }

   public static Pose L(EntityRenderState var0, PoseStack var1) {
      Pose var2 = B(var0, var1, true);
      if (var2 == null && !Q.F) {
         Q.F = (boolean)1;
         com.larpclient.h.g(
            "Wings",
            "Native body attachment unavailable for this renderer; Luminous uses its upright fallback and Ender Dragon is skipped, retry after world/resource cleanup"
         );
      }

      return var2;
   }

   private static Pose B(EntityRenderState var0, PoseStack var1, boolean var2) {
      j4 var3 = Q;
      Minecraft var4 = Minecraft.getInstance();
      Vec3 var5 = var4.gameRenderer.mainCamera().position();
      var3.h.pos = var5;
      var3.h.orientation.set(var4.gameRenderer.mainCamera().rotation());
      var3.W.last().set(var1.last());
      var3.p = (boolean)1;
      var3.u = var2;
      var3.y = null;

      try {
         var4.getEntityRenderDispatcher().submit(var0, var3.h, var0.x - var5.x, var0.y - var5.y, var0.z - var5.z, var3.W, var3.e);
         return var3.y;
      } finally {
         var3.p = (boolean)0;
         var3.y = null;

         while (!var3.W.isEmpty()) {
            var3.W.popPose();
         }
      }
   }

   public static void r() {
      Q.a = (boolean)0;
      Q.F = (boolean)0;
      o.clear();
      H.clear();
   }

   public static void a(ModelPart var0, PoseStack var1, hh var2, Identifier var3, int var4, double var5, double var7, boolean var9) {
      if (Q.P != wa.s) {
         r();
         Q.P = wa.s;
      }

      j4.j var10 = var7 > 0.0 ? (var9 ? j4.j.W : j4.j.n) : j4.j.A;
      if (!var9 || !(var7 <= 0.0)) {
         j4.m var11 = new j4.m(var2.g(e(var3, var10, false)), var10, var4, var5, 0.0, 0.0, null);
         var11.N = (int)(Math.clamp(var7, 0.0, 1.0) * 4096.0);
         var0.render(var1, var11, 15728880, OverlayTexture.NO_OVERLAY);
      }
   }

   public static void I(EntityRenderState var0, PoseStack var1, hh var2, j4.j var3, int var4, double var5, boolean var7) {
      b(var0, var1, var2, var3, var4, var5, var7, true);
   }

   public static void b(EntityRenderState var0, PoseStack var1, hh var2, j4.j var3, int var4, double var5, boolean var7, boolean var8) {
      Q.Q(var0, var1, var2, var3, var4, var5, var7, var8);
   }

   private void Q(EntityRenderState var1, PoseStack var2, hh var3, j4.j var4, int var5, double var6, boolean var8, boolean var9) {
      if (this.P != wa.s) {
         r();
         this.P = wa.s;
      }

      this.M = var3;
      this.v = var4;
      this.C = var5;
      this.J = var6;
      this.B = var8;
      this.w = var9;
      this.d = (boolean)(var4 == j4.j.j ? 1 : 0);
      this.O = var8 ? SelfVisualsModule.k() : null;
      Minecraft var10 = Minecraft.getInstance();
      Vec3 var11 = var10.gameRenderer.mainCamera().position();
      this.A = var1.y - var11.y;
      this.f = var1.x - var11.x;
      this.h.pos = var11;
      this.h.orientation.set(var10.gameRenderer.mainCamera().rotation());
      this.W.last().set(var2.last());

      try {
         var10.getEntityRenderDispatcher().submit(var1, this.h, var1.x - var11.x, var1.y - var11.y, var1.z - var11.z, this.W, this.e);
      } catch (RuntimeException var16) {
         throw new IllegalStateException("ModelVisualRenderer stage=vanilla layer submission material=" + var4 + " entity=" + var1.entityType, var16);
      } finally {
         while (!this.W.isEmpty()) {
            this.W.popPose();
         }

         this.M = null;
         this.O = null;
         if (this.g) {
            this.a();
         }
      }
   }

   @Override
   public Object invoke(Object var1, Method var2, Object[] var3) throws Throwable {
      if (var2.isDefault()) {
         return InvocationHandler.invokeDefault(var1, var2, var3);
      }

      if (this.p && var2.getDeclaringClass() != Object.class && !var2.getName().equals("submitModel") && !var2.getName().equals("order")) {
         return null;
      }

      switch (var2.getName()) {
         case "order":
            return this.e;
         case "submitModel":
            Model var17 = (Model)var3[0];
            Object var19 = var3[1];
            PoseStack var20 = (PoseStack)var3[2];
            if (this.p) {
               if (this.y == null && var17 instanceof PlayerModel var22) {
                  var17.setupAnim(var19);
                  var20.pushPose();

                  try {
                     (this.u ? var22.body : var22.head).translateAndRotate(var20);
                     this.y = var20.last().copy();
                  } finally {
                     var20.popPose();
                  }
               }

               return null;
            } else {
               RenderType var21 = (RenderType)var3[3];
               boolean var23 = var17 instanceof PlayerModel;
               if (this.B && var17 instanceof PlayerCapeModel) {
                  if (R(this.v) || this.O == null || !this.O.s()) {
                     return null;
                  }
               } else if (this.B && !this.d && !var23 && (this.O == null || (R(this.v) ? !g8.o() : !this.O.D.getValue()))) {
                  return null;
               }

               VertexConsumer var24 = this.k(var21);
               if (var24 == null) {
                  return null;
               }

               if (var3[7] instanceof TextureAtlasSprite var25) {
                  var24 = var25.wrap(var24);
               }

               var17.setupAnim(var19);
               var17.renderToBuffer(var20, var24, (Integer)var3[4], (Integer)var3[5], (Integer)var3[6]);
               return null;
            }
         case "submitItem":
            if (!this.B || this.d || this.O != null && !this.O.P() && this.O.H.getValue()) {
               PoseStack var16 = (PoseStack)var3[0];
               int[] var18 = (int[])var3[5];
               this.G.setLightCoords((Integer)var3[2]);
               this.G.setOverlayCoords((Integer)var3[3]);

               for (BakedQuad var9 : o(var3[6])) {
                  MaterialInfo var10 = var9.materialInfo();
                  TextureBinding var11 = (TextureBinding)var10.itemRenderType().state.textures.get("Sampler0");
                  if (var11 != null) {
                     int var12 = var10.tintIndex();
                     this.G.setColor(var12 >= 0 && var12 < var18.length ? var18[var12] : -1);
                     this.n(var11.location()).putBakedQuad(var16.last(), var9, this.G);
                  }
               }

               return null;
            }

            return null;
         case "submitCustomGeometry":
            if (!this.B || this.d || this.O != null && !this.O.P() && this.O.H.getValue()) {
               RenderType var6 = (RenderType)var3[1];
               VertexConsumer var7 = this.k(var6);
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

   public static List<BakedQuad> o(Object var0) {
      return (List<BakedQuad>)var0;
   }

   public static SubmitNodeCollector B(SubmitNodeCollector var0) {
      SelfVisualsModule var1 = SelfVisualsModule.k();
      if (var1 != null && var1.isOperational() && var1.J() && !var1.P() && var1.H.getValue()) {
         j4.j var2 = var1.t();
         return var2 == null ? var0 : (SubmitNodeCollector)x(var0, var1, var2, true);
      } else {
         return var0;
      }
   }

   private static Object x(OrderedSubmitNodeCollector var0, SelfVisualsModule var1, j4.j var2, boolean var3) {
      Class<SubmitNodeCollector> var4 = var3 ? SubmitNodeCollector.class : OrderedSubmitNodeCollector.class;
      return Proxy.newProxyInstance(var4.getClassLoader(), new Class[]{var4}, (var3x, var4x, var5) -> {
         if (var4x.isDefault()) {
            return InvocationHandler.invokeDefault(var3x, var4x, var5);
         }

         if (var4x.getName().equals("order")) {
            return x(((SubmitNodeCollector)var0).order((Integer)var5[0]), var1, var2, false);
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
            List var10 = List.copyOf(o(var5[6]));
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
            double var16 = var1.I.getValue();

            for (Entry var19 : var11.entrySet()) {
               List var20 = List.copyOf((Collection)var19.getValue());
               var0.submitCustomGeometry(var6, e((Identifier)var19.getKey(), var2, true), (var12, var13x) -> {
                  QuadInstance var14x = new QuadInstance();
                  var14x.setLightCoords(var7);
                  var14x.setOverlayCoords(var8);
                  j4.m var15 = new j4.m(var13x, var2, ho.J, var16, var22, var23, var1);

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

   public static boolean t(ModelPart var0, PoseStack var1, SubmitNodeCollector var2, RenderType var3, int var4, int var5, TextureAtlasSprite var6) {
      SelfVisualsModule var7 = SelfVisualsModule.k();
      if (var7 != null && var7.isOperational() && var7.J() && var7.K.getValue() && (!var7.P() || g8.s())) {
         TextureBinding var8 = (TextureBinding)var3.state.textures.get("Sampler0");
         if (var8 == null) {
            return false;
         }

         j4.j var9 = var7.t() == null ? j4.j.o : var7.t();
         double var10 = var1.last().pose().m31();
         double var12 = var1.last().pose().m30();
         var2.submitCustomGeometry(var1, e(var8.location(), var9, true), (var10x, var11) -> {
            long var12x = zj.m();
            PoseStack var14 = new PoseStack();
            var14.last().set(var10x);
            VertexConsumer var15 = new j4.m(var11, var9, ho.J, var7.o(), var12, var10, var7);
            if (var6 != null) {
               var15 = var6.wrap(var15);
            }

            try {
               var0.render(var14, var15, var4, var5);
            } catch (RuntimeException var20) {
               com.larpclient.h.w("SelfVisuals", "First-person arm stage=geometry shader=model/material failed; restoring vanilla on next frame", var20);
               if (var7.P()) {
                  g8.Y(var20);
               } else {
                  var7.disable();
               }
            } finally {
               zj.l("SelfVisuals", var12x);
            }
         });
         return true;
      } else {
         return false;
      }
   }

   public static VertexConsumer b(hh var0, j4.j var1) {
      if (!x(var1)) {
         throw new IllegalArgumentException("World surfaces use chams styles, not " + var1);
      }

      if (Q.P != wa.s) {
         r();
         Q.P = wa.s;
      }

      return var0.g(e(x, var1, false));
   }

   public static void q(
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
      var0.setColor(uh.F(var10, var12));
      var0.setUv(var5, var6);
      var0.setUv1(var11 >> 8 & 65535, var11 & 0xFF | (int)Math.round(Math.clamp(var14, 0.0, 1.0) * 255.0) << 8);
      var0.setUv2(Math.floorMod((long)(var18 * 4096.0), 4096), (int)Math.round(var16 * 256.0));
      var0.setNormal(var1, var7, var8, var9);
   }

   public static void y() {
      e(Identifier.fromNamespaceAndPath("minecraft", "textures/entity/player/wide/steve.png"), j4.j.e, true);
   }

   public static boolean R(j4.j var0) {
      return var0 == j4.j.e || var0 == j4.j.M || var0 == j4.j.y;
   }

   public static boolean x(j4.j var0) {
      return n.contains(var0);
   }

   private VertexConsumer n(Identifier var1) {
      return new j4.m(
         this.M.g(e(var1, this.v, this.v == j4.j.h || this.B && !this.d && this.v != j4.j.M && this.v != j4.j.y)),
         this.v,
         this.C,
         this.J,
         this.f,
         this.A,
         this.O
      );
   }

   static boolean v(j4.j var0, boolean var1, boolean var2) {
      return var1 && var2 && (var0 == j4.j.J || var0 == j4.j.k || var0 == j4.j.o || var0 == j4.j.A || var0 == j4.j.e || x(var0) && var0 != j4.j.F);
   }

   private VertexConsumer k(RenderType var1) {
      if (var1.pipeline() == RenderPipelines.GLINT) {
         return v(this.v, this.B, this.w) ? this.M.g(var1) : null;
      }

      TextureBinding var2 = (TextureBinding)var1.state.textures.get("Sampler0");
      return var2 == null ? null : this.n(var2.location());
   }

   private static RenderType e(Identifier var0, j4.j var1, boolean var2) {
      j4.e var3 = new j4.e(var0, var1, var2);
      RenderType var4 = o.get(var3);
      if (var4 != null) {
         return var4;
      }

      if (o.size() >= 256) {
         o.clear();
      }

      j4.f var5 = new j4.f(var1, var2);
      RenderPipeline var6 = H.get(var5);
      if (var6 == null) {
         Builder var7 = RenderPipeline.builder(new Snippet[]{RenderPipelines.GUI_TEXTURED_SNIPPET})
            .withLocation(Identifier.fromNamespaceAndPath("larp-client", "model_" + var1.name().toLowerCase(Locale.ROOT) + (var2 ? "_solid" : "_transparent")))
            .withFragmentShader(Identifier.fromNamespaceAndPath("larp-client", "model/material"))
            .withVertexShader(Identifier.fromNamespaceAndPath("larp-client", "model/material"))
            .withVertexBinding(0, DefaultVertexFormat.ENTITY)
            .withDepthStencilState(new DepthStencilState(var1 == j4.j.l ? CompareOp.LESS_THAN : CompareOp.GREATER_THAN_OR_EQUAL, var2))
            .withCull(var1 != j4.j.j && var1 != j4.j.h);
         if (var1 == j4.j.h) {
            var7.withColorTargetState(ColorTargetState.DEFAULT);
            var7.withShaderDefine("CHAMS_SURFACE");
         }

         if (R(var1)) {
            var7.withShaderDefine("LIQUID_GLASS");
         }

         if (var1 == j4.j.M) {
            var7.withShaderDefine("GLASS_MASK");
         }

         if (var1 == j4.j.y) {
            var7.withShaderDefine("GLASS_GLOW");
         }

         if (var1 == j4.j.M) {
            var7.withColorTargetState(new ColorTargetState(Optional.empty(), GpuFormat.RGBA16_FLOAT, 15));
         }

         if (var1 == j4.j.J) {
            var7.withShaderDefine("RAINBOW");
         }

         if (var1 == j4.j.j || var1 == j4.j.H || var1 == j4.j.Z || var1 == j4.j.l) {
            var7.withShaderDefine("SILHOUETTE");
         }

         if (var1 == j4.j.k) {
            var7.withShaderDefine("HOLOGRAM");
         }

         if (var1 == j4.j.j) {
            var7.withShaderDefine("DEPTH_BIAS");
         }

         if (x(var1)) {
            var7.withShaderDefine("CHAMS_STYLE");
            var7.withShaderDefine("STYLE_" + var1.name().substring("CHAMS_".length()));
         }

         if (var1 == j4.j.Z || var1 == j4.j.l) {
            var7.withShaderDefine("CHAMS");
         }

         if (var1 == j4.j.n || var1 == j4.j.W) {
            var7.withShaderDefine("DISSOLVE");
         }

         if (var1 == j4.j.W) {
            var7.withShaderDefine("DISSOLVE_GLOW");
         }

         var6 = var7.build();
         ai.U(var6);
         H.put(var5, var6);
         com.larpclient.h.A("Shader", "Player material initialized: {} shader=model/material", var1);
      }

      RenderType var8 = RenderType.create("larp_model_" + var1, RenderSetup.builder(var6).withTexture("Sampler0", var0).sortOnUpload().createRenderSetup());
      o.put(var3, var8);
      return var8;
   }

   private record e(Identifier texture, j4.j material, boolean depthWrite) {
   }

   private record f(j4.j material, boolean depthWrite) {
   }

   public enum j {
      A,
      o,
      k,
      H,
      J,
      j,
      Z,
      l,
      n,
      W,
      e,
      M,
      y,
      h,
      K,
      u,
      T,
      N,
      r,
      z,
      F;
   }

   static final class m implements VertexConsumer {
      private final VertexConsumer W;
      private final j4.j g;
      private final int h;
      private final double z;
      private final double k;
      private final double s;
      private final boolean j;
      private final boolean J;
      private final boolean w;
      private final boolean F;
      private final boolean D;
      private final int I;
      private final int E;
      private final int u;
      private final int Y;
      private final int t;
      private final double L;
      private final double e;
      private final double q;
      private final int n;
      private float A;
      private float X;
      private int N;
      private int R = -1;

      m(VertexConsumer var1, j4.j var2, int var3, double var4, double var6, double var8, SelfVisualsModule var10) {
         this.W = var1;
         this.g = var2;
         this.h = var3;
         this.z = var4;
         this.k = var6;
         this.s = var8;
         this.F = (boolean)(j4.R(var2) && var10 != null ? 1 : 0);
         this.I = g8.c();
         this.E = g8.u();
         this.L = this.F ? g8.D(var10) : 0.0;
         this.D = (boolean)(j4.x(var2) && var10 != null ? 1 : 0);
         int var11 = this.D ? var10.E() : 0;
         this.u = var11 >> 8 & 65535;
         this.Y = this.D ? var11 & 0xFF | (int)Math.round(var10.v.getValue() * 255.0) << 8 : 0;
         this.t = this.D ? (int)Math.round(var10.r.getValue() * 256.0) : 0;
         this.j = (boolean)(var2 == j4.j.J && var10 != null ? 1 : 0);
         this.J = (boolean)(this.j && var10.p.O("Horizontal") ? 1 : 0);
         this.e = this.j ? var10.w.getValue() : 0.0;
         this.q = this.D
            ? cf.F() * var10.O.getValue() % 1.0
            : (this.j && !var10.p.O("Static") ? cf.F() * var10.t.getValue() * (var10.p.O("Upward") ? -1 : 1) % 1.0 : 0.0);
         this.w = (boolean)(!this.j && !this.F && !this.D && var2 != j4.j.j && var2 != j4.j.H && var2 != j4.j.Z && var2 != j4.j.h && var2 != j4.j.l ? 0 : 1);
         int var12 = this.j
            ? 0xFF000000 | (int)(var10.Q.getValue() * 255.0) << 16 | (int)(var10.E.getValue() * 255.0) << 8
            : (this.F ? g8.e(var10, 1.0) : (this.D ? var10.w() : var3));
         this.n = uh.F(var12, var4);
      }

      public VertexConsumer addVertex(float var1, float var2, float var3) {
         this.A = var1;
         this.X = var2;
         j4 var4 = j4.Q;
         if (var4.g) {
            var4.S = Math.min(var4.S, var1);
            var4.R = Math.max(var4.R, var1);
            var4.Y = Math.min(var4.Y, var2);
            var4.I = Math.max(var4.I, var2);
            var4.T = Math.min(var4.T, var3);
            var4.z = Math.max(var4.z, var3);
         }

         if (this.F) {
            this.R++;
         }

         this.W.addVertex(var1, var2, var3);
         return this;
      }

      public VertexConsumer setColor(int var1, int var2, int var3, int var4) {
         return this.setColor(var4 << 24 | var1 << 16 | var2 << 8 | var3);
      }

      public VertexConsumer setColor(int var1) {
         int var2 = this.N;
         if (this.F) {
            var2 = (int)(this.L * 4096.0);
         } else if (this.D) {
            var2 = (int)(this.q * 4096.0);
         } else if (this.j) {
            double var3 = this.J ? this.A - this.k : this.X - this.s;
            double var5 = var3 * this.e + this.q;
            var2 = Math.clamp(Math.round(var5 * 4096.0), -32768, 32767);
         }

         int var7 = this.R & 3;
         this.W.setUv2(var2, this.F ? (var7 == 2 ? 3 : (var7 == 3 ? 2 : var7)) : (this.D ? this.t : 0));
         this.W.setColor(this.w ? this.n : uh.F(this.g == j4.j.A ? var1 : uh.q(var1, this.h, 0.85), this.z));
         return this;
      }

      public VertexConsumer setUv(float var1, float var2) {
         this.W.setUv(var1, var2);
         return this;
      }

      public VertexConsumer setUv1(int var1, int var2) {
         this.W.setUv1(this.F ? this.I : (this.D ? this.u : var1), this.F ? this.E : (this.D ? this.Y : var2));
         return this;
      }

      public VertexConsumer setUv2(int var1, int var2) {
         return this;
      }

      public VertexConsumer setNormal(float var1, float var2, float var3) {
         this.W.setNormal(var1, var2, var3);
         return this;
      }

      public VertexConsumer setLineWidth(float var1) {
         return this;
      }
   }
}
