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

public final class uv implements InvocationHandler {
   private static final Map<uv.b, RenderType> a = new LinkedHashMap<>();
   private static final Map<uv.t, RenderPipeline> l = new HashMap<>();
   private static final uv T = new uv();
   private final SubmitNodeCollector G = (SubmitNodeCollector)Proxy.newProxyInstance(
      SubmitNodeCollector.class.getClassLoader(), new Class[]{SubmitNodeCollector.class}, this
   );
   private final PoseStack C = new PoseStack();
   private final CameraRenderState Z = new CameraRenderState();
   private final QuadInstance t = new QuadInstance();
   private y2 r;
   private uv.w B;
   private int L;
   private double h;
   private double Y;
   private double I;
   private boolean x;
   private boolean Q;
   private boolean c;
   private SelfVisualsModule S;
   private int J = -1;
   private boolean v;
   private boolean u;
   private boolean d;
   private boolean X;
   private Pose i;
   private static final Identifier K = Identifier.fromNamespaceAndPath("minecraft", "textures/block/white_concrete.png");
   private static final EnumSet<uv.w> M = EnumSet.range(uv.w.r, uv.w.A);

   public static EntityRenderState v(Entity var0, float var1) {
      return Minecraft.getInstance().getEntityRenderDispatcher().extractEntity(var0, var1);
   }

   public static boolean G() {
      return T.r != null || T.v;
   }

   public static Matrix4f z(EntityRenderState var0, PoseStack var1) {
      Pose var2 = g(var0, var1, false);
      if (var2 == null && !T.d) {
         T.d = (boolean)1;
         r4.m(
            "ChinaHat",
            "Native head attachment unavailable for this renderer; cone uses upright fallback and cap is skipped, retry after world/resource cleanup"
         );
      }

      return var2 == null ? null : new Matrix4f(var2.pose());
   }

   public static Pose o(EntityRenderState var0, PoseStack var1) {
      Pose var2 = g(var0, var1, true);
      if (var2 == null && !T.X) {
         T.X = (boolean)1;
         r4.m(
            "Wings",
            "Native body attachment unavailable for this renderer; Luminous uses its upright fallback and Ender Dragon is skipped, retry after world/resource cleanup"
         );
      }

      return var2;
   }

   private static Pose g(EntityRenderState var0, PoseStack var1, boolean var2) {
      uv var3 = T;
      Minecraft var4 = Minecraft.getInstance();
      Vec3 var5 = var4.gameRenderer.mainCamera().position();
      var3.Z.pos = var5;
      var3.Z.orientation.set(var4.gameRenderer.mainCamera().rotation());
      var3.C.last().set(var1.last());
      var3.v = (boolean)1;
      var3.u = var2;
      var3.i = null;

      try {
         var4.getEntityRenderDispatcher().submit(var0, var3.Z, var0.x - var5.x, var0.y - var5.y, var0.z - var5.z, var3.C, var3.G);
         return var3.i;
      } finally {
         var3.v = (boolean)0;
         var3.i = null;

         while (!var3.C.isEmpty()) {
            var3.C.popPose();
         }
      }
   }

   public static void S() {
      T.d = (boolean)0;
      T.X = (boolean)0;
      a.clear();
      l.clear();
   }

   public static void S(ModelPart var0, PoseStack var1, y2 var2, Identifier var3, int var4, double var5, double var7, boolean var9) {
      if (T.J != jk.v) {
         S();
         T.J = jk.v;
      }

      uv.w var10 = var7 > 0.0 ? (var9 ? uv.w.t : uv.w.P) : uv.w.a;
      if (!var9 || !(var7 <= 0.0)) {
         uv.s var11 = new uv.s(var2.a(I(var3, var10, false)), var10, var4, var5, 0.0, 0.0, null);
         var11.F = (int)(Math.clamp(var7, 0.0, 1.0) * 4096.0);
         var0.render(var1, var11, 15728880, OverlayTexture.NO_OVERLAY);
      }
   }

   public static void k(EntityRenderState var0, PoseStack var1, y2 var2, uv.w var3, int var4, double var5, boolean var7) {
      V(var0, var1, var2, var3, var4, var5, var7, true);
   }

   public static void V(EntityRenderState var0, PoseStack var1, y2 var2, uv.w var3, int var4, double var5, boolean var7, boolean var8) {
      T.e(var0, var1, var2, var3, var4, var5, var7, var8);
   }

   private void e(EntityRenderState var1, PoseStack var2, y2 var3, uv.w var4, int var5, double var6, boolean var8, boolean var9) {
      if (this.J != jk.v) {
         S();
         this.J = jk.v;
      }

      this.r = var3;
      this.B = var4;
      this.L = var5;
      this.h = var6;
      this.x = var8;
      this.c = var9;
      this.Q = (boolean)(var4 == uv.w.c ? 1 : 0);
      this.S = var8 ? SelfVisualsModule.f() : null;
      Minecraft var10 = Minecraft.getInstance();
      Vec3 var11 = var10.gameRenderer.mainCamera().position();
      this.Y = var1.y - var11.y;
      this.I = var1.x - var11.x;
      this.Z.pos = var11;
      this.Z.orientation.set(var10.gameRenderer.mainCamera().rotation());
      this.C.last().set(var2.last());

      try {
         var10.getEntityRenderDispatcher().submit(var1, this.Z, var1.x - var11.x, var1.y - var11.y, var1.z - var11.z, this.C, this.G);
      } catch (RuntimeException var16) {
         throw new IllegalStateException("ModelVisualRenderer stage=vanilla layer submission material=" + var4 + " entity=" + var1.entityType, var16);
      } finally {
         while (!this.C.isEmpty()) {
            this.C.popPose();
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

      if (this.v && var2.getDeclaringClass() != Object.class && !var2.getName().equals("submitModel") && !var2.getName().equals("order")) {
         return null;
      }

      switch (var2.getName()) {
         case "order":
            return this.G;
         case "submitModel":
            Model var17 = (Model)var3[0];
            Object var19 = var3[1];
            PoseStack var20 = (PoseStack)var3[2];
            if (this.v) {
               if (this.i == null && var17 instanceof PlayerModel var22) {
                  var17.setupAnim(var19);
                  var20.pushPose();

                  try {
                     (this.u ? var22.body : var22.head).translateAndRotate(var20);
                     this.i = var20.last().copy();
                  } finally {
                     var20.popPose();
                  }
               }

               return null;
            } else {
               RenderType var21 = (RenderType)var3[3];
               boolean var23 = var17 instanceof PlayerModel;
               if (this.x && var17 instanceof PlayerCapeModel) {
                  if (b(this.B) || this.S == null || !this.S.I()) {
                     return null;
                  }
               } else if (this.x && !this.Q && !var23 && (this.S == null || (b(this.B) ? !my.T() : !this.S.T.getValue()))) {
                  return null;
               }

               VertexConsumer var24 = this.r(var21);
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
            if (!this.x || this.Q || this.S != null && !this.S.U() && this.S.k.getValue()) {
               PoseStack var16 = (PoseStack)var3[0];
               int[] var18 = (int[])var3[5];
               this.t.setLightCoords((Integer)var3[2]);
               this.t.setOverlayCoords((Integer)var3[3]);

               for (BakedQuad var9 : (List)var3[6]) {
                  MaterialInfo var10 = var9.materialInfo();
                  TextureBinding var11 = (TextureBinding)var10.itemRenderType().state.textures.get("Sampler0");
                  if (var11 != null) {
                     int var12 = var10.tintIndex();
                     this.t.setColor(var12 >= 0 && var12 < var18.length ? var18[var12] : -1);
                     this.h(var11.location()).putBakedQuad(var16.last(), var9, this.t);
                  }
               }

               return null;
            }

            return null;
         case "submitCustomGeometry":
            if (!this.x || this.Q || this.S != null && !this.S.U() && this.S.k.getValue()) {
               RenderType var6 = (RenderType)var3[1];
               VertexConsumer var7 = this.r(var6);
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

   public static SubmitNodeCollector m(SubmitNodeCollector var0) {
      SelfVisualsModule var1 = SelfVisualsModule.f();
      if (var1 != null && var1.isOperational() && var1.w() && !var1.U() && var1.k.getValue()) {
         uv.w var2 = var1.c();
         return var2 == null ? var0 : (SubmitNodeCollector)X(var0, var1, var2, true);
      } else {
         return var0;
      }
   }

   private static Object X(OrderedSubmitNodeCollector var0, SelfVisualsModule var1, uv.w var2, boolean var3) {
      Class<SubmitNodeCollector> var4 = var3 ? SubmitNodeCollector.class : OrderedSubmitNodeCollector.class;
      return Proxy.newProxyInstance(var4.getClassLoader(), new Class[]{var4}, (var3x, var4x, var5) -> {
         if (var4x.isDefault()) {
            return InvocationHandler.invokeDefault(var3x, var4x, var5);
         }

         if (var4x.getName().equals("order")) {
            return X(((SubmitNodeCollector)var0).order((Integer)var5[0]), var1, var2, false);
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
            double var16 = var1.p.getValue();

            for (Entry var19 : var11.entrySet()) {
               List var20 = List.copyOf((Collection)var19.getValue());
               var0.submitCustomGeometry(var6, I((Identifier)var19.getKey(), var2, true), (var12, var13x) -> {
                  QuadInstance var14x = new QuadInstance();
                  var14x.setLightCoords(var7);
                  var14x.setOverlayCoords(var8);
                  uv.s var15 = new uv.s(var13x, var2, rv.e, var16, var22, var23, var1);

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

   public static boolean Q(ModelPart var0, PoseStack var1, SubmitNodeCollector var2, RenderType var3, int var4, int var5, UvMapping var6) {
      SelfVisualsModule var7 = SelfVisualsModule.f();
      if (var7 != null && var7.isOperational() && var7.w() && var7.s.getValue() && (!var7.U() || my.f())) {
         TextureBinding var8 = (TextureBinding)var3.state.textures.get("Sampler0");
         if (var8 == null) {
            return false;
         }

         uv.w var9 = var7.c() == null ? uv.w.V : var7.c();
         double var10 = var1.last().pose().m31();
         double var12 = var1.last().pose().m30();
         var2.submitCustomGeometry(var1, I(var8.location(), var9, true), (var10x, var11) -> {
            long var12x = h_.U();
            PoseStack var14 = new PoseStack();
            var14.last().set(var10x);
            VertexConsumer var15 = new uv.s(var11, var9, rv.e, var7.K(), var12, var10, var7);
            if (var6 != null) {
               var15 = var6.wrap(var15);
            }

            try {
               var0.render(var14, var15, var4, var5);
            } catch (RuntimeException var20) {
               r4.T("SelfVisuals", "First-person arm stage=geometry shader=model/material failed; restoring vanilla on next frame", var20);
               if (var7.U()) {
                  my.O(var20);
               } else {
                  var7.disable();
               }
            } finally {
               h_.n("SelfVisuals", var12x);
            }
         });
         return true;
      } else {
         return false;
      }
   }

   public static VertexConsumer i(y2 var0, uv.w var1) {
      if (!I(var1)) {
         throw new IllegalArgumentException("World surfaces use chams styles, not " + var1);
      }

      if (T.J != jk.v) {
         S();
         T.J = jk.v;
      }

      return var0.a(I(K, var1, false));
   }

   public static void j(
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
      var0.setColor(oh.S(var10, var12));
      var0.setUv(var5, var6);
      var0.setUv1(var11 >> 8 & 65535, var11 & 0xFF | (int)Math.round(Math.clamp(var14, 0.0, 1.0) * 255.0) << 8);
      var0.setUv2(Math.floorMod((long)(var18 * 4096.0), 4096), (int)Math.round(var16 * 256.0));
      var0.setNormal(var1, var7, var8, var9);
   }

   public static void n() {
      I(Identifier.fromNamespaceAndPath("minecraft", "textures/entity/player/wide/steve.png"), uv.w.w, true);
   }

   public static boolean b(uv.w var0) {
      return var0 == uv.w.w || var0 == uv.w.F || var0 == uv.w.q;
   }

   public static boolean I(uv.w var0) {
      return M.contains(var0);
   }

   private VertexConsumer h(Identifier var1) {
      return new uv.s(
         this.r.a(I(var1, this.B, this.B == uv.w.v || this.x && !this.Q && this.B != uv.w.F && this.B != uv.w.q)),
         this.B,
         this.L,
         this.h,
         this.I,
         this.Y,
         this.S
      );
   }

   static boolean p(uv.w var0, boolean var1, boolean var2) {
      return var1 && var2 && (var0 == uv.w.k || var0 == uv.w.I || var0 == uv.w.V || var0 == uv.w.a || var0 == uv.w.w || I(var0) && var0 != uv.w.A);
   }

   private VertexConsumer r(RenderType var1) {
      if (var1.pipeline() == RenderPipelines.GLINT) {
         return p(this.B, this.x, this.c) ? this.r.a(var1) : null;
      }

      TextureBinding var2 = (TextureBinding)var1.state.textures.get("Sampler0");
      return var2 == null ? null : this.h(var2.location());
   }

   private static RenderType I(Identifier var0, uv.w var1, boolean var2) {
      uv.b var3 = new uv.b(var0, var1, var2);
      RenderType var4 = a.get(var3);
      if (var4 != null) {
         return var4;
      }

      if (a.size() >= 256) {
         a.clear();
      }

      uv.t var5 = new uv.t(var1, var2);
      RenderPipeline var6 = l.get(var5);
      if (var6 == null) {
         Builder var7 = RenderPipeline.builder(new Snippet[]{RenderPipelines.GUI_TEXTURED_SNIPPET})
            .withLocation(Identifier.fromNamespaceAndPath("larp-client", "model_" + var1.name().toLowerCase(Locale.ROOT) + (var2 ? "_solid" : "_transparent")))
            .withFragmentShader(Identifier.fromNamespaceAndPath("larp-client", "model/material"))
            .withVertexShader(Identifier.fromNamespaceAndPath("larp-client", "model/material"))
            .withVertexBinding(0, DefaultVertexFormat.ENTITY)
            .withDepthStencilState(new DepthStencilState(var1 == uv.w.S ? CompareOp.LESS_THAN : CompareOp.GREATER_THAN_OR_EQUAL, var2))
            .withCull(var1 != uv.w.c && var1 != uv.w.v);
         if (var1 == uv.w.v) {
            var7.withColorTargetState(ColorTargetState.DEFAULT);
            var7.withShaderDefine("CHAMS_SURFACE");
         }

         if (b(var1)) {
            var7.withShaderDefine("LIQUID_GLASS");
         }

         if (var1 == uv.w.F) {
            var7.withShaderDefine("GLASS_MASK");
         }

         if (var1 == uv.w.q) {
            var7.withShaderDefine("GLASS_GLOW");
         }

         if (var1 == uv.w.F) {
            var7.withColorTargetState(new ColorTargetState(Optional.empty(), GpuFormat.RGBA16_FLOAT, 15));
         }

         if (var1 == uv.w.k) {
            var7.withShaderDefine("RAINBOW");
         }

         if (var1 == uv.w.c || var1 == uv.w.Y || var1 == uv.w.C || var1 == uv.w.S) {
            var7.withShaderDefine("SILHOUETTE");
         }

         if (var1 == uv.w.I) {
            var7.withShaderDefine("HOLOGRAM");
         }

         if (var1 == uv.w.c) {
            var7.withShaderDefine("DEPTH_BIAS");
         }

         if (I(var1)) {
            var7.withShaderDefine("CHAMS_STYLE");
            var7.withShaderDefine("STYLE_" + var1.name().substring("CHAMS_".length()));
         }

         if (var1 == uv.w.C || var1 == uv.w.S) {
            var7.withShaderDefine("CHAMS");
         }

         if (var1 == uv.w.P || var1 == uv.w.t) {
            var7.withShaderDefine("DISSOLVE");
         }

         if (var1 == uv.w.t) {
            var7.withShaderDefine("DISSOLVE_GLOW");
         }

         var6 = var7.build();
         e4.U(var6);
         l.put(var5, var6);
         r4.V("Shader", "Player material initialized: {} shader=model/material", var1);
      }

      RenderType var8 = RenderType.create("larp_model_" + var1, RenderSetup.builder(var6).withTexture("Sampler0", var0).sortOnUpload().createRenderSetup());
      a.put(var3, var8);
      return var8;
   }

   private record b(Identifier texture, uv.w material, boolean depthWrite) {
   }

   static final class s implements VertexConsumer {
      private final VertexConsumer O;
      private final uv.w W;
      private final int I;
      private final double b;
      private final double P;
      private final double h;
      private final boolean q;
      private final boolean K;
      private final boolean d;
      private final boolean s;
      private final boolean n;
      private final int r;
      private final int k;
      private final int A;
      private final int L;
      private final int l;
      private final double f;
      private final double p;
      private final double D;
      private final int Z;
      private float z;
      private float U;
      private int F;
      private int j = -1;

      s(VertexConsumer var1, uv.w var2, int var3, double var4, double var6, double var8, SelfVisualsModule var10) {
         this.O = var1;
         this.W = var2;
         this.I = var3;
         this.b = var4;
         this.P = var6;
         this.h = var8;
         this.s = (boolean)(uv.b(var2) && var10 != null ? 1 : 0);
         this.r = my.d();
         this.k = my.H();
         this.f = this.s ? my.w(var10) : 0.0;
         this.n = (boolean)(uv.I(var2) && var10 != null ? 1 : 0);
         int var11 = this.n ? var10.P() : 0;
         this.A = var11 >> 8 & 65535;
         this.L = this.n ? var11 & 0xFF | (int)Math.round(var10.b.getValue() * 255.0) << 8 : 0;
         this.l = this.n ? (int)Math.round(var10.n.getValue() * 256.0) : 0;
         this.q = (boolean)(var2 == uv.w.k && var10 != null ? 1 : 0);
         this.K = (boolean)(this.q && var10.Z.i("Horizontal") ? 1 : 0);
         this.p = this.q ? var10.l.getValue() : 0.0;
         this.D = this.n
            ? ea.y() * var10.t.getValue() % 1.0
            : (this.q && !var10.Z.i("Static") ? ea.y() * var10.O.getValue() * (var10.Z.i("Upward") ? -1 : 1) % 1.0 : 0.0);
         this.d = (boolean)(!this.q && !this.s && !this.n && var2 != uv.w.c && var2 != uv.w.Y && var2 != uv.w.C && var2 != uv.w.v && var2 != uv.w.S ? 0 : 1);
         int var12 = this.q
            ? 0xFF000000 | (int)(var10.f.getValue() * 255.0) << 16 | (int)(var10.X.getValue() * 255.0) << 8
            : (this.s ? my.J(var10, 1.0) : (this.n ? var10.Z() : var3));
         this.Z = oh.S(var12, var4);
      }

      public VertexConsumer addVertex(float var1, float var2, float var3) {
         this.z = var1;
         this.U = var2;
         if (this.s) {
            this.j++;
         }

         this.O.addVertex(var1, var2, var3);
         return this;
      }

      public VertexConsumer setColor(int var1, int var2, int var3, int var4) {
         return this.setColor(var4 << 24 | var1 << 16 | var2 << 8 | var3);
      }

      public VertexConsumer setColor(int var1) {
         int var2 = this.F;
         if (this.s) {
            var2 = (int)(this.f * 4096.0);
         } else if (this.n) {
            var2 = (int)(this.D * 4096.0);
         } else if (this.q) {
            double var3 = this.K ? this.z - this.P : this.U - this.h;
            double var5 = var3 * this.p + this.D;
            var2 = Math.clamp(Math.round(var5 * 4096.0), -32768, 32767);
         }

         int var7 = this.j & 3;
         this.O.setUv2(var2, this.s ? (var7 == 2 ? 3 : (var7 == 3 ? 2 : var7)) : (this.n ? this.l : 0));
         this.O.setColor(this.d ? this.Z : oh.S(this.W == uv.w.a ? var1 : oh.j(var1, this.I, 0.85), this.b));
         return this;
      }

      public VertexConsumer setUv(float var1, float var2) {
         this.O.setUv(var1, var2);
         return this;
      }

      public VertexConsumer setUv1(int var1, int var2) {
         this.O.setUv1(this.s ? this.r : (this.n ? this.A : var1), this.s ? this.k : (this.n ? this.L : var2));
         return this;
      }

      public VertexConsumer setUv3(float var1, float var2) {
         return this;
      }

      public VertexConsumer setUv2(int var1, int var2) {
         return this;
      }

      public VertexConsumer setNormal(float var1, float var2, float var3) {
         this.O.setNormal(var1, var2, var3);
         return this;
      }

      public VertexConsumer setLineWidth(float var1) {
         return this;
      }
   }

   private record t(uv.w material, boolean depthWrite) {
   }

   public enum w {
      a,
      V,
      I,
      Y,
      k,
      c,
      C,
      S,
      P,
      t,
      w,
      F,
      q,
      v,
      r,
      K,
      x,
      p,
      n,
      g,
      A;
   }
}
