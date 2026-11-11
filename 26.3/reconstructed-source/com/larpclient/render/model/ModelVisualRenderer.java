package com.larpclient.render.model;

import com.larpclient.gui.theme.Theme;
import com.larpclient.module.render.SelfVisualsModule;
import com.larpclient.render.misc.ClientPipelines;
import com.larpclient.render.misc.EffectBatch;
import com.larpclient.render.overlay.LiquidGlass;
import com.larpclient.render.overlay.PostProcessing;
import com.larpclient.render.sky.DistanceFogRenderer;
import com.larpclient.util.data.TickTimer;
import com.larpclient.util.misc.LarpLog;
import com.larpclient.util.misc.PerfProfiler;
import com.larpclient.util.render.ColorUtil;
import com.larpclient.util.render.ScreenProjection;
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

public final class ModelVisualRenderer implements InvocationHandler {
   private static final Map<ModelVisualRenderer.RenderTypeKey, RenderType> renderTypeCache = new LinkedHashMap<>();
   private static final Map<ModelVisualRenderer.PipelineKey, RenderPipeline> pipelineCache = new HashMap<>();
   private static final ModelVisualRenderer iNSTANCE = new ModelVisualRenderer();
   private final SubmitNodeCollector collectorProxy = (SubmitNodeCollector)Proxy.newProxyInstance(
      SubmitNodeCollector.class.getClassLoader(), new Class[]{SubmitNodeCollector.class}, this
   );
   private final PoseStack poseStack = new PoseStack();
   private final CameraRenderState cameraState = new CameraRenderState();
   private final QuadInstance quadInstance = new QuadInstance();
   private EffectBatch bufferSource;
   private ModelVisualRenderer.Material material;
   private int color;
   private double alpha;
   private double originY;
   private double originX;
   private boolean isSelf;
   private boolean isSilhouette;
   private boolean glintAllowed;
   private SelfVisualsModule selfVisuals;
   private int resourceGeneration = -1;
   private boolean probing;
   private boolean probeBody;
   private boolean chinaHatWarned;
   private boolean wingsWarned;
   private Pose probedPose;
   private boolean trackingBounds;
   private boolean fullScreen;
   private boolean projectionWarned;
   private int screenWidth;
   private int screenHeight;
   private float screenMinX;
   private float screenMinY;
   private float screenMaxX;
   private float screenMaxY;
   private float minX;
   private float minY;
   private float minZ;
   private float maxX;
   private float maxY;
   private float maxZ;
   private final Matrix4f projection = new Matrix4f();
   private static final Identifier wHITE_TEXTURE = Identifier.fromNamespaceAndPath("minecraft", "textures/block/white_concrete.png");
   private static final EnumSet<ModelVisualRenderer.Material> cHAMS_MATERIALS = EnumSet.range(
      ModelVisualRenderer.Material.CHAMS_STYLE_1, ModelVisualRenderer.Material.CHAMS_STYLE_7
   );

   public static void beginBoundsCapture(int var0, int var1) {
      ModelVisualRenderer var2 = iNSTANCE;
      var2.screenWidth = var0;
      var2.screenHeight = var1;
      var2.screenMinX = var2.screenMinY = Float.POSITIVE_INFINITY;
      var2.screenMaxX = var2.screenMaxY = Float.NEGATIVE_INFINITY;
      var2.fullScreen = (boolean)(!DistanceFogRenderer.getWorldProjection(var2.projection) ? 1 : 0);
      if (var2.fullScreen) {
         if (!var2.projectionWarned) {
            var2.projectionWarned = (boolean)1;
            LarpLog.warn("Render", "World projection not captured; model post passes cover the whole screen");
         }
      } else {
         var2.projection.mul(RenderSystem.getModelViewMatrixCopy());
         var2.trackingBounds = (boolean)1;
         var2.resetBounds();
      }
   }

   public static int[] endBoundsCapture() {
      ModelVisualRenderer var0 = iNSTANCE;
      var0.trackingBounds = (boolean)0;
      if (var0.fullScreen) {
         return null;
      }

      if (var0.screenMinX > var0.screenMaxX) {
         return new int[4];
      }

      int var1 = (int)Math.floor(var0.screenMinX);
      int var2 = (int)Math.floor(var0.screenMinY);
      return new int[]{var1, var2, (int)Math.ceil(var0.screenMaxX) - var1, (int)Math.ceil(var0.screenMaxY) - var2};
   }

   private void resetBounds() {
      this.minX = this.minY = this.minZ = Float.POSITIVE_INFINITY;
      this.maxX = this.maxY = this.maxZ = Float.NEGATIVE_INFINITY;
   }

   private void flushBounds() {
      if (!(this.minX > this.maxX) && !this.fullScreen) {
         if (this.minX <= 0.0F && this.maxX >= 0.0F && this.minY <= 0.0F && this.maxY >= 0.0F && this.minZ <= 0.0F && this.maxZ >= 0.0F) {
            this.fullScreen = (boolean)1;
         } else {
            boolean var1 = RenderSystem.getDevice().getDeviceInfo().isZZeroToOne();
            ScreenProjection.FloatBounds var2 = ScreenProjection.projectBox(
               new AABB(this.minX, this.minY, this.minZ, this.maxX, this.maxY, this.maxZ),
               Vec3.ZERO,
               this.projection,
               this.screenWidth,
               this.screenHeight,
               var1
            );
            this.resetBounds();
            if (var2 != null) {
               this.screenMinX = Math.min(this.screenMinX, var2.left());
               this.screenMaxX = Math.max(this.screenMaxX, var2.right());
               this.screenMinY = Math.min(this.screenMinY, var2.top());
               this.screenMaxY = Math.max(this.screenMaxY, var2.bottom());
            }
         }
      }
   }

   public static EntityRenderState extractEntity(Entity var0, float var1) {
      return Minecraft.getInstance().getEntityRenderDispatcher().extractEntity(var0, var1);
   }

   public static boolean isProbing() {
      return iNSTANCE.bufferSource != null || iNSTANCE.probing;
   }

   public static Matrix4f resetBounds(EntityRenderState var0, PoseStack var1) {
      Pose var2 = probeAttachment(var0, var1, false);
      if (var2 == null && !iNSTANCE.chinaHatWarned) {
         iNSTANCE.chinaHatWarned = (boolean)1;
         LarpLog.warn(
            "ChinaHat",
            "Native head attachment unavailable for this renderer; cone uses upright fallback and cap is skipped, retry after world/resource cleanup"
         );
      }

      return var2 == null ? null : new Matrix4f(var2.pose());
   }

   public static Pose bodyAttachment(EntityRenderState var0, PoseStack var1) {
      Pose var2 = probeAttachment(var0, var1, true);
      if (var2 == null && !iNSTANCE.wingsWarned) {
         iNSTANCE.wingsWarned = (boolean)1;
         LarpLog.warn(
            "Wings",
            "Native body attachment unavailable for this renderer; Luminous uses its upright fallback and Ender Dragon is skipped, retry after world/resource cleanup"
         );
      }

      return var2;
   }

   private static Pose probeAttachment(EntityRenderState var0, PoseStack var1, boolean var2) {
      ModelVisualRenderer var3 = iNSTANCE;
      Minecraft var4 = Minecraft.getInstance();
      Vec3 var5 = var4.gameRenderer.mainCamera().position();
      var3.cameraState.pos = var5;
      var3.cameraState.orientation.set(var4.gameRenderer.mainCamera().rotation());
      var3.poseStack.last().set(var1.last());
      var3.probing = (boolean)1;
      var3.probeBody = var2;
      var3.probedPose = null;

      try {
         var4.getEntityRenderDispatcher()
            .submit(var0, var3.cameraState, var0.x - var5.x, var0.y - var5.y, var0.z - var5.z, var3.poseStack, var3.collectorProxy);
         return var3.probedPose;
      } finally {
         var3.probing = (boolean)0;
         var3.probedPose = null;

         while (!var3.poseStack.isEmpty()) {
            var3.poseStack.popPose();
         }
      }
   }

   public static void clearCaches() {
      iNSTANCE.chinaHatWarned = (boolean)0;
      iNSTANCE.wingsWarned = (boolean)0;
      renderTypeCache.clear();
      pipelineCache.clear();
   }

   public static void renderModelPart(ModelPart var0, PoseStack var1, EffectBatch var2, Identifier var3, int var4, double var5, double var7, boolean var9) {
      if (iNSTANCE.resourceGeneration != PostProcessing.reloadCount) {
         clearCaches();
         iNSTANCE.resourceGeneration = PostProcessing.reloadCount;
      }

      ModelVisualRenderer.Material var10 = var7 > 0.0
         ? (var9 ? ModelVisualRenderer.Material.DISSOLVE_GLOW : ModelVisualRenderer.Material.DISSOLVE)
         : ModelVisualRenderer.Material.NORMAL;
      if (!var9 || !(var7 <= 0.0)) {
         ModelVisualRenderer.MaterialVertexConsumer var11 = new ModelVisualRenderer.MaterialVertexConsumer(
            var2.getConsumer(getRenderType(var3, var10, false)), var10, var4, var5, 0.0, 0.0, null
         );
         var11.dissolveProgress = (int)(Math.clamp(var7, 0.0, 1.0) * 4096.0);
         var0.render(var1, var11, 15728880, OverlayTexture.NO_OVERLAY);
      }
   }

   public static void renderEntity(
      EntityRenderState var0, PoseStack var1, EffectBatch var2, ModelVisualRenderer.Material var3, int var4, double var5, boolean var7
   ) {
      renderEntityLayers(var0, var1, var2, var3, var4, var5, var7, true);
   }

   public static void renderEntityLayers(
      EntityRenderState var0, PoseStack var1, EffectBatch var2, ModelVisualRenderer.Material var3, int var4, double var5, boolean var7, boolean var8
   ) {
      iNSTANCE.submitEntity(var0, var1, var2, var3, var4, var5, var7, var8);
   }

   private void submitEntity(
      EntityRenderState var1, PoseStack var2, EffectBatch var3, ModelVisualRenderer.Material var4, int var5, double var6, boolean var8, boolean var9
   ) {
      if (this.resourceGeneration != PostProcessing.reloadCount) {
         clearCaches();
         this.resourceGeneration = PostProcessing.reloadCount;
      }

      this.bufferSource = var3;
      this.material = var4;
      this.color = var5;
      this.alpha = var6;
      this.isSelf = var8;
      this.glintAllowed = var9;
      this.isSilhouette = (boolean)(var4 == ModelVisualRenderer.Material.SILHOUETTE ? 1 : 0);
      this.selfVisuals = var8 ? SelfVisualsModule.getPrimaryColor() : null;
      Minecraft var10 = Minecraft.getInstance();
      Vec3 var11 = var10.gameRenderer.mainCamera().position();
      this.originY = var1.y - var11.y;
      this.originX = var1.x - var11.x;
      this.cameraState.pos = var11;
      this.cameraState.orientation.set(var10.gameRenderer.mainCamera().rotation());
      this.poseStack.last().set(var2.last());

      try {
         var10.getEntityRenderDispatcher()
            .submit(var1, this.cameraState, var1.x - var11.x, var1.y - var11.y, var1.z - var11.z, this.poseStack, this.collectorProxy);
      } catch (RuntimeException var16) {
         throw new IllegalStateException("ModelVisualRenderer stage=vanilla layer submission material=" + var4 + " entity=" + var1.entityType, var16);
      } finally {
         while (!this.poseStack.isEmpty()) {
            this.poseStack.popPose();
         }

         this.bufferSource = null;
         this.selfVisuals = null;
         if (this.trackingBounds) {
            this.flushBounds();
         }
      }
   }

   @Override
   public Object invoke(Object var1, Method var2, Object[] var3) throws Throwable {
      if (var2.isDefault()) {
         return InvocationHandler.invokeDefault(var1, var2, var3);
      }

      if (this.probing && var2.getDeclaringClass() != Object.class && !var2.getName().equals("submitModel") && !var2.getName().equals("order")) {
         return null;
      }

      switch (var2.getName()) {
         case "order":
            return this.collectorProxy;
         case "submitModel":
            Model var17 = (Model)var3[0];
            Object var19 = var3[1];
            PoseStack var20 = (PoseStack)var3[2];
            if (this.probing) {
               if (this.probedPose == null && var17 instanceof PlayerModel var22) {
                  var17.setupAnim(var19);
                  var20.pushPose();

                  try {
                     (this.probeBody ? var22.body : var22.head).translateAndRotate(var20);
                     this.probedPose = var20.last().copy();
                  } finally {
                     var20.popPose();
                  }
               }

               return null;
            } else {
               RenderType var21 = (RenderType)var3[3];
               boolean var23 = var17 instanceof PlayerModel;
               if (this.isSelf && var17 instanceof PlayerCapeModel) {
                  if (isLiquidGlass(this.material) || this.selfVisuals == null || !this.selfVisuals.getMaterial()) {
                     return null;
                  }
               } else if (this.isSelf
                  && !this.isSilhouette
                  && !var23
                  && (this.selfVisuals == null || (isLiquidGlass(this.material) ? !LiquidGlass.isArmorEnabled() : !this.selfVisuals.affectArmor.getValue()))) {
                  return null;
               }

               VertexConsumer var24 = this.consumerFor(var21);
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
            if (!this.isSelf
               || this.isSilhouette
               || this.selfVisuals != null && !this.selfVisuals.isLiquidGlass() && this.selfVisuals.affectHeldItems.getValue()) {
               PoseStack var16 = (PoseStack)var3[0];
               int[] var18 = (int[])var3[5];
               this.quadInstance.setLightCoords((Integer)var3[2]);
               this.quadInstance.setOverlayCoords((Integer)var3[3]);

               for (BakedQuad var9 : itemQuads(var3[6])) {
                  MaterialInfo var10 = var9.materialInfo();
                  TextureBinding var11 = (TextureBinding)var10.itemRenderType().state.textures.get("Sampler0");
                  if (var11 != null) {
                     int var12 = var10.tintIndex();
                     this.quadInstance.setColor(var12 >= 0 && var12 < var18.length ? var18[var12] : -1);
                     this.bodyAttachment(var11.location()).putBakedQuad(var16.last(), var9, this.quadInstance);
                  }
               }

               return null;
            }

            return null;
         case "submitCustomGeometry":
            if (!this.isSelf
               || this.isSilhouette
               || this.selfVisuals != null && !this.selfVisuals.isLiquidGlass() && this.selfVisuals.affectHeldItems.getValue()) {
               RenderType var6 = (RenderType)var3[1];
               VertexConsumer var7 = this.consumerFor(var6);
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

   public static List<BakedQuad> itemQuads(Object var0) {
      return ((ItemQuads)var0).all();
   }

   public static SubmitNodeCollector wrapCollector(SubmitNodeCollector var0) {
      SelfVisualsModule var1 = SelfVisualsModule.getPrimaryColor();
      if (var1 != null && var1.isOperational() && var1.rendersModel() && !var1.isLiquidGlass() && var1.affectHeldItems.getValue()) {
         ModelVisualRenderer.Material var2 = var1.getMaterial();
         return var2 == null ? var0 : (SubmitNodeCollector)createWrappedCollector(var0, var1, var2, true);
      } else {
         return var0;
      }
   }

   private static Object createWrappedCollector(OrderedSubmitNodeCollector var0, SelfVisualsModule var1, ModelVisualRenderer.Material var2, boolean var3) {
      Class<SubmitNodeCollector> var4 = var3 ? SubmitNodeCollector.class : OrderedSubmitNodeCollector.class;
      return Proxy.newProxyInstance(
         var4.getClassLoader(),
         new Class[]{var4},
         (var3x, var4x, var5) -> {
            if (var4x.isDefault()) {
               return InvocationHandler.invokeDefault(var3x, var4x, var5);
            }

            if (var4x.getName().equals("order")) {
               return createWrappedCollector(((SubmitNodeCollector)var0).order((Integer)var5[0]), var1, var2, false);
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
               List var10 = List.copyOf(itemQuads(var5[6]));
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
               double var16 = var1.modelOpacity.getValue();

               for (Entry var19 : var11.entrySet()) {
                  List var20 = List.copyOf((Collection)var19.getValue());
                  var0.submitCustomGeometry(
                     var6,
                     getRenderType((Identifier)var19.getKey(), var2, true),
                     (var12, var13x) -> {
                        QuadInstance var14x = new QuadInstance();
                        var14x.setLightCoords(var7);
                        var14x.setOverlayCoords(var8);
                        ModelVisualRenderer.MaterialVertexConsumer var15 = new ModelVisualRenderer.MaterialVertexConsumer(
                           var13x, var2, Theme.accent, var16, var22, var23, var1
                        );

                        for (BakedQuad var17 : var20) {
                           int var18 = var17.materialInfo().tintIndex();
                           var14x.setColor(var18 >= 0 && var18 < var9.length ? var9[var18] : -1);
                           var15.putBakedQuad(var12, var17, var14x);
                        }
                     }
                  );
               }

               return null;
            }
         }
      );
   }

   public static boolean renderFirstPersonArm(ModelPart var0, PoseStack var1, SubmitNodeCollector var2, RenderType var3, int var4, int var5, UvMapping var6) {
      SelfVisualsModule var7 = SelfVisualsModule.getPrimaryColor();
      if (var7 != null
         && var7.isOperational()
         && var7.rendersModel()
         && var7.firstPersonArms.getValue()
         && (!var7.isLiquidGlass() || LiquidGlass.isMaterialAvailable())) {
         TextureBinding var8 = (TextureBinding)var3.state.textures.get("Sampler0");
         if (var8 == null) {
            return false;
         }

         ModelVisualRenderer.Material var9 = var7.getMaterial() == null ? ModelVisualRenderer.Material.TINT : var7.getMaterial();
         double var10 = var1.last().pose().m31();
         double var12 = var1.last().pose().m30();
         var2.submitCustomGeometry(var1, getRenderType(var8.location(), var9, true), (var10x, var11) -> {
            long var12x = PerfProfiler.startTimer();
            PoseStack var14 = new PoseStack();
            var14.last().set(var10x);
            VertexConsumer var15 = new ModelVisualRenderer.MaterialVertexConsumer(var11, var9, Theme.accent, var7.getPrimaryColor(), var12, var10, var7);
            if (var6 != null) {
               var15 = var6.wrap(var15);
            }

            try {
               var0.render(var14, var15, var4, var5);
            } catch (RuntimeException var20) {
               LarpLog.errorOnce("SelfVisuals", "First-person arm stage=geometry shader=model/material failed; restoring vanilla on next frame", var20);
               if (var7.isLiquidGlass()) {
                  LiquidGlass.onSubmitFailed(var20);
               } else {
                  var7.disable();
               }
            } finally {
               PerfProfiler.endTimer("SelfVisuals", var12x);
            }
         });
         return true;
      } else {
         return false;
      }
   }

   public static VertexConsumer renderEntity(EffectBatch var0, ModelVisualRenderer.Material var1) {
      if (!isChamsStyle(var1)) {
         throw new IllegalArgumentException("World surfaces use chams styles, not " + var1);
      }

      if (iNSTANCE.resourceGeneration != PostProcessing.reloadCount) {
         clearCaches();
         iNSTANCE.resourceGeneration = PostProcessing.reloadCount;
      }

      return var0.getConsumer(getRenderType(wHITE_TEXTURE, var1, false));
   }

   public static void putVertex(
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
      var0.setColor(ColorUtil.multiplyAlpha(var10, var12));
      var0.setUv(var5, var6);
      var0.setUv1(var11 >> 8 & 65535, var11 & 0xFF | (int)Math.round(Math.clamp(var14, 0.0, 1.0) * 255.0) << 8);
      var0.setUv2(Math.floorMod((long)(var18 * 4096.0), 4096), (int)Math.round(var16 * 256.0));
      var0.setNormal(var1, var7, var8, var9);
   }

   public static void wrapCollector() {
      getRenderType(Identifier.fromNamespaceAndPath("minecraft", "textures/entity/player/wide/steve.png"), ModelVisualRenderer.Material.LIQUID_GLASS, true);
   }

   public static boolean isLiquidGlass(ModelVisualRenderer.Material var0) {
      return var0 == ModelVisualRenderer.Material.LIQUID_GLASS
         || var0 == ModelVisualRenderer.Material.GLASS_MASK
         || var0 == ModelVisualRenderer.Material.GLASS_GLOW;
   }

   public static boolean isChamsStyle(ModelVisualRenderer.Material var0) {
      return cHAMS_MATERIALS.contains(var0);
   }

   private VertexConsumer bodyAttachment(Identifier var1) {
      return new ModelVisualRenderer.MaterialVertexConsumer(
         this.bufferSource
            .getConsumer(
               getRenderType(
                  var1,
                  this.material,
                  this.material == ModelVisualRenderer.Material.CHAMS_SURFACE
                     || this.isSelf
                        && !this.isSilhouette
                        && this.material != ModelVisualRenderer.Material.GLASS_MASK
                        && this.material != ModelVisualRenderer.Material.GLASS_GLOW
               )
            ),
         this.material,
         this.color,
         this.alpha,
         this.originX,
         this.originY,
         this.selfVisuals
      );
   }

   static boolean beginBoundsCapture(ModelVisualRenderer.Material var0, boolean var1, boolean var2) {
      return var1
         && var2
         && (
            var0 == ModelVisualRenderer.Material.RAINBOW
               || var0 == ModelVisualRenderer.Material.HOLOGRAM
               || var0 == ModelVisualRenderer.Material.TINT
               || var0 == ModelVisualRenderer.Material.NORMAL
               || var0 == ModelVisualRenderer.Material.LIQUID_GLASS
               || isChamsStyle(var0) && var0 != ModelVisualRenderer.Material.CHAMS_STYLE_7
         );
   }

   private VertexConsumer consumerFor(RenderType var1) {
      if (var1.pipeline() == RenderPipelines.GLINT) {
         return beginBoundsCapture(this.material, this.isSelf, this.glintAllowed) ? this.bufferSource.getConsumer(var1) : null;
      }

      TextureBinding var2 = (TextureBinding)var1.state.textures.get("Sampler0");
      return var2 == null ? null : this.bodyAttachment(var2.location());
   }

   private static RenderType getRenderType(Identifier var0, ModelVisualRenderer.Material var1, boolean var2) {
      ModelVisualRenderer.RenderTypeKey var3 = new ModelVisualRenderer.RenderTypeKey(var0, var1, var2);
      RenderType var4 = renderTypeCache.get(var3);
      if (var4 != null) {
         return var4;
      }

      if (renderTypeCache.size() >= 256) {
         renderTypeCache.clear();
      }

      ModelVisualRenderer.PipelineKey var5 = new ModelVisualRenderer.PipelineKey(var1, var2);
      RenderPipeline var6 = pipelineCache.get(var5);
      if (var6 == null) {
         Builder var7 = RenderPipeline.builder(new Snippet[]{RenderPipelines.GUI_TEXTURED_SNIPPET})
            .withLocation(Identifier.fromNamespaceAndPath("larp-client", "model_" + var1.name().toLowerCase(Locale.ROOT) + (var2 ? "_solid" : "_transparent")))
            .withFragmentShader(Identifier.fromNamespaceAndPath("larp-client", "model/material"))
            .withVertexShader(Identifier.fromNamespaceAndPath("larp-client", "model/material"))
            .withVertexBinding(0, DefaultVertexFormat.ENTITY)
            .withDepthStencilState(
               new DepthStencilState(var1 == ModelVisualRenderer.Material.CHAMS_VISIBLE ? CompareOp.LESS_THAN : CompareOp.GREATER_THAN_OR_EQUAL, var2)
            )
            .withCull(var1 != ModelVisualRenderer.Material.SILHOUETTE && var1 != ModelVisualRenderer.Material.CHAMS_SURFACE);
         if (var1 == ModelVisualRenderer.Material.CHAMS_SURFACE) {
            var7.withColorTargetState(ColorTargetState.DEFAULT);
            var7.withShaderDefine("CHAMS_SURFACE");
         }

         if (isLiquidGlass(var1)) {
            var7.withShaderDefine("LIQUID_GLASS");
         }

         if (var1 == ModelVisualRenderer.Material.GLASS_MASK) {
            var7.withShaderDefine("GLASS_MASK");
         }

         if (var1 == ModelVisualRenderer.Material.GLASS_GLOW) {
            var7.withShaderDefine("GLASS_GLOW");
         }

         if (var1 == ModelVisualRenderer.Material.GLASS_MASK) {
            var7.withColorTargetState(new ColorTargetState(Optional.empty(), GpuFormat.RGBA16_FLOAT, 15));
         }

         if (var1 == ModelVisualRenderer.Material.RAINBOW) {
            var7.withShaderDefine("RAINBOW");
         }

         if (var1 == ModelVisualRenderer.Material.SILHOUETTE
            || var1 == ModelVisualRenderer.Material.OUTLINE
            || var1 == ModelVisualRenderer.Material.CHAMS_THROUGH_WALLS
            || var1 == ModelVisualRenderer.Material.CHAMS_VISIBLE) {
            var7.withShaderDefine("SILHOUETTE");
         }

         if (var1 == ModelVisualRenderer.Material.HOLOGRAM) {
            var7.withShaderDefine("HOLOGRAM");
         }

         if (var1 == ModelVisualRenderer.Material.SILHOUETTE) {
            var7.withShaderDefine("DEPTH_BIAS");
         }

         if (isChamsStyle(var1)) {
            var7.withShaderDefine("CHAMS_STYLE");
            var7.withShaderDefine("STYLE_" + var1.name().substring("CHAMS_".length()));
         }

         if (var1 == ModelVisualRenderer.Material.CHAMS_THROUGH_WALLS || var1 == ModelVisualRenderer.Material.CHAMS_VISIBLE) {
            var7.withShaderDefine("CHAMS");
         }

         if (var1 == ModelVisualRenderer.Material.DISSOLVE || var1 == ModelVisualRenderer.Material.DISSOLVE_GLOW) {
            var7.withShaderDefine("DISSOLVE");
         }

         if (var1 == ModelVisualRenderer.Material.DISSOLVE_GLOW) {
            var7.withShaderDefine("DISSOLVE_GLOW");
         }

         var6 = var7.build();
         ClientPipelines.validatePipeline(var6);
         pipelineCache.put(var5, var6);
         LarpLog.info("Shader", "Player material initialized: {} shader=model/material", var1);
      }

      RenderType var8 = RenderType.create("larp_model_" + var1, RenderSetup.builder(var6).withTexture("Sampler0", var0).sortOnUpload().createRenderSetup());
      renderTypeCache.put(var3, var8);
      return var8;
   }

   public enum Material {
      NORMAL,
      TINT,
      HOLOGRAM,
      OUTLINE,
      RAINBOW,
      SILHOUETTE,
      CHAMS_THROUGH_WALLS,
      CHAMS_VISIBLE,
      DISSOLVE,
      DISSOLVE_GLOW,
      LIQUID_GLASS,
      GLASS_MASK,
      GLASS_GLOW,
      CHAMS_SURFACE,
      CHAMS_STYLE_1,
      CHAMS_STYLE_2,
      CHAMS_STYLE_3,
      CHAMS_STYLE_4,
      CHAMS_STYLE_5,
      CHAMS_STYLE_6,
      CHAMS_STYLE_7;
   }

   static final class MaterialVertexConsumer implements VertexConsumer {
      private final VertexConsumer delegate;
      private final ModelVisualRenderer.Material material;
      private final int color;
      private final double alpha;
      private final double originX;
      private final double originY;
      private final boolean isRainbow;
      private final boolean isHorizontal;
      private final boolean usesEffectColor;
      private final boolean isLiquidGlass;
      private final boolean isChams;
      private final int fogRedGreen;
      private final int fogBlue;
      private final int chamsParamsHigh;
      private final int chamsParamsLow;
      private final int chamsStrength;
      private final double flowPhase;
      private final double rainbowScale;
      private final double animationPhase;
      private final int effectColor;
      private float lastX;
      private float lastY;
      private int dissolveProgress;
      private int vertexIndex = -1;

      MaterialVertexConsumer(VertexConsumer var1, ModelVisualRenderer.Material var2, int var3, double var4, double var6, double var8, SelfVisualsModule var10) {
         this.delegate = var1;
         this.material = var2;
         this.color = var3;
         this.alpha = var4;
         this.originX = var6;
         this.originY = var8;
         this.isLiquidGlass = (boolean)(ModelVisualRenderer.isLiquidGlass(var2) && var10 != null ? 1 : 0);
         this.fogRedGreen = LiquidGlass.fogRedGreen();
         this.fogBlue = LiquidGlass.fogBlue();
         this.flowPhase = this.isLiquidGlass ? LiquidGlass.flowPhase(var10) : 0.0;
         this.isChams = (boolean)(ModelVisualRenderer.isChamsStyle(var2) && var10 != null ? 1 : 0);
         int var11 = this.isChams ? var10.getSecondaryColor() : 0;
         this.chamsParamsHigh = var11 >> 8 & 65535;
         this.chamsParamsLow = this.isChams ? var11 & 0xFF | (int)Math.round(var10.shading.getValue() * 255.0) << 8 : 0;
         this.chamsStrength = this.isChams ? (int)Math.round(var10.patternScale.getValue() * 256.0) : 0;
         this.isRainbow = (boolean)(var2 == ModelVisualRenderer.Material.RAINBOW && var10 != null ? 1 : 0);
         this.isHorizontal = (boolean)(this.isRainbow && var10.direction.isMode("Horizontal") ? 1 : 0);
         this.rainbowScale = this.isRainbow ? var10.gradientScale.getValue() : 0.0;
         this.animationPhase = this.isChams
            ? TickTimer.getTime() * var10.patternSpeed.getValue() % 1.0
            : (
               this.isRainbow && !var10.direction.isMode("Static")
                  ? TickTimer.getTime() * var10.rainbowSpeed.getValue() * (var10.direction.isMode("Upward") ? -1 : 1) % 1.0
                  : 0.0
            );
         this.usesEffectColor = (boolean)(!this.isRainbow
               && !this.isLiquidGlass
               && !this.isChams
               && var2 != ModelVisualRenderer.Material.SILHOUETTE
               && var2 != ModelVisualRenderer.Material.OUTLINE
               && var2 != ModelVisualRenderer.Material.CHAMS_THROUGH_WALLS
               && var2 != ModelVisualRenderer.Material.CHAMS_SURFACE
               && var2 != ModelVisualRenderer.Material.CHAMS_VISIBLE
            ? 0
            : 1);
         int var12 = this.isRainbow
            ? 0xFF000000 | (int)(var10.saturation.getValue() * 255.0) << 16 | (int)(var10.brightness.getValue() * 255.0) << 8
            : (this.isLiquidGlass ? LiquidGlass.packParams(var10, 1.0) : (this.isChams ? var10.getPrimaryColor() : var3));
         this.effectColor = ColorUtil.multiplyAlpha(var12, var4);
      }

      public VertexConsumer addVertex(float var1, float var2, float var3) {
         this.lastX = var1;
         this.lastY = var2;
         ModelVisualRenderer var4 = ModelVisualRenderer.iNSTANCE;
         if (var4.trackingBounds) {
            var4.minX = Math.min(var4.minX, var1);
            var4.maxX = Math.max(var4.maxX, var1);
            var4.minY = Math.min(var4.minY, var2);
            var4.maxY = Math.max(var4.maxY, var2);
            var4.minZ = Math.min(var4.minZ, var3);
            var4.maxZ = Math.max(var4.maxZ, var3);
         }

         if (this.isLiquidGlass) {
            this.vertexIndex++;
         }

         this.delegate.addVertex(var1, var2, var3);
         return this;
      }

      public VertexConsumer setColor(int var1, int var2, int var3, int var4) {
         return this.setColor(var4 << 24 | var1 << 16 | var2 << 8 | var3);
      }

      public VertexConsumer setColor(int var1) {
         int var2 = this.dissolveProgress;
         if (this.isLiquidGlass) {
            var2 = (int)(this.flowPhase * 4096.0);
         } else if (this.isChams) {
            var2 = (int)(this.animationPhase * 4096.0);
         } else if (this.isRainbow) {
            double var3 = this.isHorizontal ? this.lastX - this.originX : this.lastY - this.originY;
            double var5 = var3 * this.rainbowScale + this.animationPhase;
            var2 = Math.clamp(Math.round(var5 * 4096.0), -32768, 32767);
         }

         int var7 = this.vertexIndex & 3;
         this.delegate.setUv2(var2, this.isLiquidGlass ? (var7 == 2 ? 3 : (var7 == 3 ? 2 : var7)) : (this.isChams ? this.chamsStrength : 0));
         this.delegate
            .setColor(
               this.usesEffectColor
                  ? this.effectColor
                  : ColorUtil.multiplyAlpha(
                     this.material == ModelVisualRenderer.Material.NORMAL ? var1 : ColorUtil.lerpColor(var1, this.color, 0.85), this.alpha
                  )
            );
         return this;
      }

      public VertexConsumer setUv(float var1, float var2) {
         this.delegate.setUv(var1, var2);
         return this;
      }

      public VertexConsumer setUv1(int var1, int var2) {
         this.delegate
            .setUv1(
               this.isLiquidGlass ? this.fogRedGreen : (this.isChams ? this.chamsParamsHigh : var1),
               this.isLiquidGlass ? this.fogBlue : (this.isChams ? this.chamsParamsLow : var2)
            );
         return this;
      }

      public VertexConsumer setUv3(float var1, float var2) {
         return this;
      }

      public VertexConsumer setUv2(int var1, int var2) {
         return this;
      }

      public VertexConsumer setNormal(float var1, float var2, float var3) {
         this.delegate.setNormal(var1, var2, var3);
         return this;
      }

      public VertexConsumer setLineWidth(float var1) {
         return this;
      }
   }

   private record PipelineKey(ModelVisualRenderer.Material material, boolean depthWrite) {
   }

   private record RenderTypeKey(Identifier texture, ModelVisualRenderer.Material material, boolean depthWrite) {
   }
}
