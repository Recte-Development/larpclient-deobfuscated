package com.larpclient.render.world;

import com.larpclient.render.entity.SwayAnimator;
import com.larpclient.render.misc.ClientPipelines;
import com.larpclient.render.misc.EffectBatch;
import com.larpclient.render.misc.RenderPipelineCompat;
import com.larpclient.render.misc.TintedVertexConsumer;
import com.larpclient.render.overlay.FontRenderer;
import com.larpclient.render.overlay.PostProcessing;
import com.larpclient.render.particle.ParticleSimulation;
import com.larpclient.setting.RangeSettings;
import com.larpclient.util.misc.LarpLog;
import com.larpclient.util.misc.PerfProfiler;
import com.larpclient.util.render.ColorUtil;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.renderpearl.api.pipeline.CompareOp;
import com.mojang.renderpearl.api.pipeline.DepthStencilState;
import com.mojang.renderpearl.api.pipeline.RenderPipeline;
import com.mojang.renderpearl.api.pipeline.RenderPipeline.Builder;
import com.mojang.renderpearl.api.pipeline.RenderPipeline.Snippet;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.Font.DisplayMode;
import net.minecraft.client.gui.Font.GlyphVisitor;
import net.minecraft.client.gui.Font.PreparedText;
import net.minecraft.client.gui.font.TextRenderable;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.rendertype.PreparedRenderType;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Quaternionfc;
import org.joml.Vector3f;

public final class WorldTextRenderer implements AutoCloseable, GlyphVisitor {
   private static final double revealStagger = 0.55;
   private static final int fullBright = 15728880;
   private static final int maxLabels = 256;
   private static final Comparator<WorldTextRenderer.LabelSlot> distanceOrder = Comparator.<WorldTextRenderer.LabelSlot>comparingDouble(var0 -> var0.distanceSq)
      .reversed();
   private final WorldTextRenderer.LabelSlot[] labelPool = new WorldTextRenderer.LabelSlot[256];
   private final WorldTextRenderer.LabelSlot[] sortedLabels = new WorldTextRenderer.LabelSlot[256];
   private int labelCount;
   private static final double textScale = 0.01890625;
   private final LinkedHashMap<String, WorldTextRenderer.PreparedLabel> preparedCache = new LinkedHashMap<>(64, 0.75F, true);
   private final Matrix4f pose = new Matrix4f();
   private final Matrix4f motionPose = new Matrix4f();
   private WorldTextRenderer.LabelMotion motion;
   private int currentColor;
   private int textWidth;
   private float lineScale;
   private boolean drawingShadow;
   private final Quaternionf tmpRotation = new Quaternionf();
   private final Vector3f cameraForward = new Vector3f();
   private final TintedVertexConsumer vertexConsumerAdapter = new TintedVertexConsumer();
   private final FloatingWord[] particles = new FloatingWord[96];
   private final double[] particleDistances = new double[96];
   private final RenderPipeline[][] pipelines = new RenderPipeline[3][2];
   private EffectBatch bufferSource;
   private Font font;
   private int fontVersion = -1;
   private int resourceGeneration = -1;
   private int depthMode;
   private boolean failed;
   private int particleCount;

   public void prepareParticles(ParticleSimulation var1, CameraRenderState var2) {
      this.particleCount = this.collectParticles(var1, var2);
   }

   public void submitLabels(List<WorldTextRenderer.WorldLabel> var1, CameraRenderState var2) {
      this.clearLabels();
      if (!this.failed || this.resourceGeneration != PostProcessing.reloadCount) {
         try {
            this.ensureResources();
            this.collectLabels(var1, var2);
         } catch (RuntimeException var4) {
            this.isolateFailure(var4);
         }
      }
   }

   private void collectLabels(List<WorldTextRenderer.WorldLabel> var1, CameraRenderState var2) {
      for (WorldTextRenderer.WorldLabel var4 : var1) {
         if (this.labelCount == 256) {
            break;
         }

         if (var4 != null
            && var4.text() != null
            && !var4.text().isBlank()
            && Double.isFinite(var4.posX())
            && Double.isFinite(var4.posY())
            && Double.isFinite(var4.posZ())
            && Double.isFinite(var4.scale())
            && !(var4.scale() <= 0.0)
            && var4.color() >>> 24 != 0
            && (
               !var4.worldAligned()
                  || Float.isFinite(var4.rotX())
                     && Float.isFinite(var4.rotY())
                     && Float.isFinite(var4.rotZ())
                     && Float.isFinite(var4.rotW())
                     && !(var4.rotX() * var4.rotX() + var4.rotY() * var4.rotY() + var4.rotZ() * var4.rotZ() + var4.rotW() * var4.rotW() < 1.0E-4F)
            )) {
            WorldTextRenderer.PreparedLabel var5 = this.getPreparedText(var4.text());
            WorldTextRenderer.LabelSlot var6 = this.labelPool[this.labelCount];
            if (var6 == null) {
               this.labelPool[this.labelCount] = var6 = new WorldTextRenderer.LabelSlot();
            }

            var6.label = var4;
            var6.prepared = var5;
            double var7 = var4.posX() - var2.pos.x;
            double var9 = var4.posY() - var2.pos.y;
            double var11 = var4.posZ() - var2.pos.z;
            var6.distanceSq = var7 * var7 + var9 * var9 + var11 * var11;
            this.sortedLabels[this.labelCount++] = var6;
         }
      }

      Arrays.sort(this.sortedLabels, 0, this.labelCount, distanceOrder);
   }

   private void clearLabels() {
      for (int var1 = 0; var1 < this.labelCount; var1++) {
         this.sortedLabels[var1].label = null;
         this.sortedLabels[var1].prepared = null;
         this.sortedLabels[var1] = null;
      }

      this.labelCount = 0;
   }

   public void renderLabels(PoseStack var1, CameraRenderState var2, boolean var3) {
      this.renderLabelsFaded(var1, var2, var3, true, 1.0);
   }

   public void renderLabelsFaded(PoseStack var1, CameraRenderState var2, boolean var3, boolean var4, double var5) {
      if (this.labelCount != 0 && !this.failed && this.bufferSource != null) {
         try {
            this.depthMode = var3 ? 1 : 0;
            this.cameraForward.set(0.0F, 0.0F, -1.0F).rotate(var2.orientation);

            for (int var7 = 0; var7 < this.labelCount; var7++) {
               WorldTextRenderer.LabelSlot var8 = this.sortedLabels[var7];
               WorldTextRenderer.WorldLabel var9 = var8.label;
               WorldTextRenderer.PreparedLabel var10 = var8.prepared;
               double var11 = var9.posX() - var2.pos.x;
               double var13 = var9.posY() - var2.pos.y;
               double var15 = var9.posZ() - var2.pos.z;
               double var17 = Math.hypot(var10.width + 12, var10.height + 8) * 0.01890625 * var9.scale();
               if (!(var11 * this.cameraForward.x + var13 * this.cameraForward.y + var15 * this.cameraForward.z < -var17)) {
                  this.pose.set(var1.last().pose()).translate((float)var11, (float)var13, (float)var15);
                  this.applyOrientation(var9, var2.orientation);
                  float var19 = (float)(0.01890625 * Math.min(32.0, var9.scale()));
                  this.pose.scale(var19, -var19, var19).translate(0.0F, -var10.centerY, 0.0F);
                  this.motion = var9.motion();
                  this.textWidth = var10.width;
                  this.lineScale = var10.height / 9.0F;
                  if (var4) {
                     this.pose.translate(1.0F, 1.0F, -0.03F);
                     this.vertexConsumerAdapter.tint = this.currentColor = ColorUtil.withAlpha(-15724520, (var9.color() >>> 24) / 255.0 * 0.65 * var5);
                     this.drawingShadow = (boolean)1;
                     var10.text.visit(this);
                     this.pose.translate(-1.0F, -1.0F, 0.03F);
                  }

                  this.vertexConsumerAdapter.tint = this.currentColor = ColorUtil.withAlpha(var9.color(), (var9.color() >>> 24) / 255.0 * var5);
                  this.drawingShadow = (boolean)0;
                  var10.text.visit(this);
               }
            }

            this.bufferSource.flushWith(this::submitLabels);
         } catch (RuntimeException var23) {
            this.isolateFailure(var23);
         } finally {
            this.vertexConsumerAdapter.delegate = null;
            this.motion = null;
         }
      }
   }

   private boolean applyMotion(TextRenderable var1) {
      WorldTextRenderer.LabelMotion var2 = this.motion;
      float var3 = (var1.left() + var1.right()) * 0.5F;
      float var4 = (var1.top() + var1.bottom()) * 0.5F;
      double var5 = this.textWidth <= 0 ? 0.0 : Math.clamp((var3 + this.textWidth * 0.5) / this.textWidth, 0.0, 1.0);
      double var7 = 1.0;
      double var9 = 0.0;
      double var11 = 0.0;
      double var13 = 0.0;
      double var15 = 0.0;
      double var17 = 1.0;
      double var19 = this.lineScale;
      if (var2.kind() != 0) {
         var7 = Math.clamp((var2.reveal() - var5 * 0.55) / 0.44999999999999996, 0.0, 1.0);
         var9 = Math.clamp((var2.exit() - var5 * 0.4) / 0.6, 0.0, 1.0);
         double var21 = 1.0 - Math.pow(1.0 - var7, 3.0);
         if (var2.kind() == 3) {
            double var23 = easeOutBounce(var7);
            double var25 = Math.sin(var5 * 91.7 + 1.3) * 0.55;
            var13 = var19 * 9.0 * (-12.0 * (1.0 - var23) + 14.0 * var9 * var9);
            var15 = var25 * (1.0 - var23) + var25 * 1.6 * var9;
         } else if (var2.kind() == 1) {
            double var28 = 1.0 + 2.2 * Math.pow(var7 - 1.0, 3.0) + 1.2 * Math.pow(var7 - 1.0, 2.0);
            var13 = var19 * (7.0 * (1.0 - var21) + 0.7 * Math.sin(var2.time() * 2.4 - var5 * 6.0) * var21 - 10.0 * var9 * var9);
            var11 = var19 * (var5 - 0.5) * 8.0 * var9 * var9;
            var15 = (var5 - 0.5) * 0.6 * var9;
            var17 = (0.4 + 0.6 * var28) * (1.0 - 0.3 * var9);
         } else {
            var11 = var19 * -6.0 * (1.0 - var21);
            var13 = var19 * (1.4 * Math.sin(var2.time() * 3.0 - var5 * 7.0) * var21 + 7.0 * var9 * var9);
         }
      }

      double var27 = var2.kind() == 3
         ? (var7 <= 0.0 ? 0.0 : SwayAnimator.smoothstep(var7 * 5.0)) * (1.0 - SwayAnimator.smoothstep(var9 * 1.15))
         : SwayAnimator.smoothstep(var7) * (1.0 - SwayAnimator.smoothstep(var9));
      if (var27 < 0.00392156862745098) {
         return false;
      }

      int var29 = this.currentColor;
      if (!this.drawingShadow && var2.accent() >>> 24 != 0) {
         double var24 = 0.5 - 0.5 * Math.cos(var5 * 2.4 - var2.time() * 1.2);
         var29 = ColorUtil.lerpColor(var29, var2.accent() & 16777215 | var29 & 0xFF000000, var24 * 0.85);
      }

      this.vertexConsumerAdapter.tint = ColorUtil.multiplyAlpha(var29, var27);
      this.motionPose
         .set(this.pose)
         .translate(var3 + (float)var11, var4 + (float)var13, 0.0F)
         .rotateZ((float)var15)
         .scale((float)var17)
         .translate(-var3, -var4, 0.0F);
      return true;
   }

   public static double easeOutBounce(double var0) {
      var0 = Math.clamp(var0, 0.0, 1.0);
      double var2 = 7.5625;
      double var4 = 2.75;
      if (var0 < 1.0 / var4) {
         return var2 * var0 * var0;
      } else if (var0 < 2.0 / var4) {
         var0 -= 1.5 / var4;
         return var2 * var0 * var0 + 0.75;
      } else if (var0 < 2.5 / var4) {
         var0 -= 2.25 / var4;
         return var2 * var0 * var0 + 0.9375;
      } else {
         var0 -= 2.625 / var4;
         return var2 * var0 * var0 + 0.984375;
      }
   }

   private void applyOrientation(WorldTextRenderer.WorldLabel var1, Quaternionfc var2) {
      this.pose.rotate((Quaternionfc)(var1.worldAligned() ? this.tmpRotation.set(var1.rotX(), var1.rotY(), var1.rotZ(), var1.rotW()).normalize() : var2));
   }

   private void isolateFailure(RuntimeException var1) {
      this.failed = (boolean)1;
      this.clearLabels();
      this.preparedCache.clear();
      if (this.bufferSource != null) {
         this.bufferSource.close();
         this.bufferSource = null;
      }

      LarpLog.error("WorldText", "World label stage failed; isolated until resource reload or re-enable", var1);
   }

   private void ensureResources() {
      Font var1 = Minecraft.getInstance().font;
      if (this.font != var1 || this.fontVersion != FontRenderer.getVersion()) {
         this.preparedCache.clear();
         this.font = var1;
         this.fontVersion = FontRenderer.getVersion();
      }

      if (this.resourceGeneration != PostProcessing.reloadCount) {
         this.preparedCache.clear();
         this.failed = (boolean)0;
         this.resourceGeneration = PostProcessing.reloadCount;

         for (int var2 = 0; var2 < 3; var2++) {
            for (int var3 = 0; var3 < 2; var3++) {
               Builder var4 = RenderPipeline.builder(new Snippet[]{RenderPipelines.WORLD_TEXT_SNIPPET})
                  .withLocation(Identifier.fromNamespaceAndPath("larp-client", "world_text_" + var2 + "_" + var3))
                  .withVertexShader("core/text")
                  .withFragmentShader(Identifier.fromNamespaceAndPath("larp-client", "world_text"))
                  .withCull(false)
                  .withDepthStencilState(
                     var2 == 1
                        ? Optional.empty()
                        : Optional.of(new DepthStencilState(var2 == 2 ? CompareOp.LESS_THAN : CompareOp.GREATER_THAN_OR_EQUAL, false))
                  );
               if (var3 == 1) {
                  var4.withShaderDefine("IS_GRAYSCALE");
               }

               this.pipelines[var2][var3] = var4.build();
               ClientPipelines.validatePipeline(this.pipelines[var2][var3]);
            }
         }

         LarpLog.info("WorldText", "World glyph pipelines ready: normal, through walls, soft occlusion");
      }

      if (this.bufferSource == null) {
         this.bufferSource = new EffectBatch();
      }
   }

   private WorldTextRenderer.PreparedLabel getPreparedText(String var1) {
      WorldTextRenderer.PreparedLabel var2 = this.preparedCache.get(var1);
      if (var2 != null) {
         return var2;
      }

      Component var3 = FontRenderer.worldComponent(var1);
      int var4 = this.font.width(var3);
      PreparedText var5 = this.font.prepareText(var3.getVisualOrderText(), -var4 / 2.0F, 0.0F, -1, false, false, 0);
      ScreenRectangle var6 = var5.bounds();
      WorldTextRenderer.PreparedLabel var7 = new WorldTextRenderer.PreparedLabel(
         var5, var4, var6 == null ? 32 : var6.height(), var6 == null ? 16.0F : var6.top() + var6.height() / 2.0F
      );
      if (this.preparedCache.size() >= 256) {
         this.preparedCache.remove(this.preparedCache.keySet().iterator().next());
      }

      this.preparedCache.put(var1, var7);
      return var7;
   }

   public boolean isAvailable() {
      return !this.failed;
   }

   public int measureWidth(String var1) {
      if (var1.isEmpty()) {
         return 0;
      }

      this.ensureResources();
      return this.getPreparedText(var1).width;
   }

   public void renderParticles(PoseStack var1, CameraRenderState var2, ParticleSimulation var3, RangeSettings var4, boolean var5) {
      if (!this.failed || this.resourceGeneration != PostProcessing.reloadCount) {
         if (!var5 || var4.visibleOnly && !(var4.threshold <= 0.0)) {
            long var6 = PerfProfiler.startTimer();

            try {
               this.ensureResources();
               int var8 = this.particleCount;
               if (var4.sortMode == RangeSettings.DepthMode.NORMAL_WITH_GHOST) {
                  this.depthMode = 2;
                  this.clearLabels(var1, var2, var3, var4, var5, var8, 0.18);
               }

               this.depthMode = var4.sortMode == RangeSettings.DepthMode.SEE_THROUGH ? 1 : 0;
               this.clearLabels(var1, var2, var3, var4, var5, var8, 1.0);
            } catch (RuntimeException var12) {
               this.failed = (boolean)1;
               if (this.bufferSource != null) {
                  this.bufferSource.close();
                  this.bufferSource = null;
               }

               this.preparedCache.clear();
               LarpLog.error("WorldText", "World text render failed; isolated until resource reload or re-enable", var12);
            } finally {
               this.vertexConsumerAdapter.delegate = null;
               PerfProfiler.endTimer("WorldText", var6);
            }
         }
      }
   }

   private int collectParticles(ParticleSimulation var1, CameraRenderState var2) {
      int var3 = 0;
      double var4 = Math.sin(var1.smoothedYaw);
      double var6 = Math.cos(var1.smoothedYaw);
      Vec3 var8 = var2.pos;

      for (FloatingWord var12 : var1.getParticles()) {
         if (var12.active && !(var12.alpha < 0.00392156862745098)) {
            double var13 = var12.x + var12.offsetY;
            double var15 = var12.z + var12.offsetZ;
            double var17 = var1.smoothedX + var13 * var6 + var15 * var4 - var8.x;
            double var19 = var1.smoothedY + var12.y + var12.offsetX - var8.y;
            double var21 = var1.smoothedZ + var15 * var6 - var13 * var4 - var8.z;
            double var23 = var17 * var17 + var19 * var19 + var21 * var21;

            int var25;
            for (var25 = var3++; var25 > 0 && this.particleDistances[var25 - 1] < var23; var25--) {
               this.particleDistances[var25] = this.particleDistances[var25 - 1];
               this.particles[var25] = this.particles[var25 - 1];
            }

            this.particleDistances[var25] = var23;
            this.particles[var25] = var12;
         }
      }

      return var3;
   }

   private void clearLabels(PoseStack var1, CameraRenderState var2, ParticleSimulation var3, RangeSettings var4, boolean var5, int var6, double var7) {
      double var9 = Math.sin(var3.smoothedYaw);
      double var11 = Math.cos(var3.smoothedYaw);
      Vec3 var13 = var2.pos;
      this.cameraForward.set(0.0F, 0.0F, -1.0F).rotate(var2.orientation);

      for (int var14 = 0; var14 < var6; var14++) {
         FloatingWord var15 = this.particles[var14];
         double var16 = Math.sqrt(this.particleDistances[var14]);
         if (!(var16 >= var4.maxRotation)) {
            double var18 = var15.alpha * var4.strength * var7;
            if (var4.enabled) {
               double var20 = Math.min(var4.fov, var4.maxRotation - 0.1);
               var18 *= 1.0 - SwayAnimator.smoothstep((var16 - var20) / (var4.maxRotation - var20));
            }

            var18 *= SwayAnimator.smoothstep(var16 / 0.8);
            if (var5) {
               var18 *= var4.threshold;
            }
            int var38 = switch (var4.behavior) {
               case SOLID -> var4.primaryColor;
               case PULSE -> ColorUtil.lerpColor(var4.primaryColor, var4.secondaryColor, (Math.sin(var15.phase) + 1.0) * 0.5);
               case RAINBOW -> ColorUtil.hsbToRgb((float)((var3.getElapsed() * 0.035 + var15.phase / (Math.PI * 2)) % 1.0), 0.45F, 1.0F);
            };
            var38 = ColorUtil.multiplyAlpha(var38, var18);
            if (var38 >>> 24 >= 1) {
               WorldTextRenderer.PreparedLabel var21 = this.getPreparedText(var15.text);
               double var22 = var15.x + var15.offsetY;
               double var24 = var15.z + var15.offsetZ;
               double var26 = var3.smoothedX + var22 * var11 + var24 * var9 - var13.x;
               double var28 = var3.smoothedY + var15.y + var15.offsetX - var13.y;
               double var30 = var3.smoothedZ + var24 * var11 - var22 * var9 - var13.z;
               double var32 = Math.hypot(var21.width + 12, var21.height + 8)
                  * 0.01890625
                  * var4.smoothing
                  * (1.0 + var15.jitter * var4.randomness)
                  * var15.scale
                  * 0.5;
               if (!(var26 * this.cameraForward.x + var28 * this.cameraForward.y + var30 * this.cameraForward.z < -var32)) {
                  this.pose.set(var1.last().pose()).translate((float)var26, (float)var28, (float)var30);
                  switch (var4.targetPriority) {
                     case CAMERA_FACING:
                        this.pose.rotate(var2.orientation);
                        break;
                     case YAW_ONLY:
                        this.pose.rotateY((float)Math.atan2(-var26, -var30));
                        break;
                     case DRIFT:
                        this.pose.rotateY((float)var15.driftPhase).rotateX((float)var15.driftX);
                  }

                  double var34 = var4.filter == RangeSettings.Movement.UPRIGHT ? 0.0 : var15.rotation;
                  this.pose.rotateZ((float)(var15.driftY + var34));
                  float var36 = (float)(0.01890625 * var4.smoothing * (1.0 + var15.jitter * var4.randomness) * var15.scale);
                  this.pose.scale(var36, -var36, var36).translate(0.0F, -var21.centerY, 0.0F);
                  if (var4.invert && !var5) {
                     this.pose.translate(2.0F, 2.0F, -0.03F);
                     this.vertexConsumerAdapter.tint = ColorUtil.withAlpha(-15724520, (var38 >>> 24) / 255.0 * 0.5);
                     var21.text.visit(this);
                     this.pose.translate(-2.0F, -2.0F, 0.03F);
                  }

                  this.vertexConsumerAdapter.tint = var38;
                  var21.text.visit(this);
               }
            }
         }
      }

      this.bufferSource.flushWith(this::submitLabels);
   }

   private PreparedRenderType submitLabels(PreparedRenderType var1) {
      int var2 = var1.pipeline() == RenderPipelines.TEXT_GRAYSCALE ? 1 : 0;
      return RenderPipelineCompat.createTextureTarget(var1, this.pipelines[this.depthMode][var2]);
   }

   public void acceptRenderable(TextRenderable var1) {
      this.vertexConsumerAdapter.delegate = this.bufferSource.getConsumer(var1.renderType(DisplayMode.NORMAL));
      if (this.motion == null) {
         var1.render(this.pose, this.vertexConsumerAdapter, 15728880, false);
      } else if (this.applyMotion(var1)) {
         var1.render(this.motionPose, this.vertexConsumerAdapter, 15728880, false);
      }
   }

   public void endBatch() {
      if (this.bufferSource != null) {
         this.bufferSource.endFrame();
      }
   }

   @Override
   public void close() {
      this.preparedCache.clear();
      this.clearLabels();
      Arrays.fill(this.particles, null);
      if (this.bufferSource != null) {
         this.bufferSource.close();
         this.bufferSource = null;
      }

      this.failed = (boolean)0;
      this.resourceGeneration = -1;
   }

   public record LabelMotion(int kind, double reveal, double exit, double time, int accent) {
      public static final int FADE = 0;
      public static final int POP = 1;
      public static final int WAVE = 2;
      public static final int FALL = 3;
      public static final double FALL_HEIGHT = 12.0;
   }

   private static final class LabelSlot {
      WorldTextRenderer.WorldLabel label;
      WorldTextRenderer.PreparedLabel prepared;
      double distanceSq;
   }

   private record PreparedLabel(PreparedText text, int width, int height, float centerY) {
   }

   public record WorldLabel(
      String text,
      double x,
      double y,
      double z,
      double scale,
      int color,
      float qx,
      float qy,
      float qz,
      float qw,
      boolean worldAligned,
      WorldTextRenderer.LabelMotion motion
   ) {
      private final double posX;
      private final double posY;
      private final double posZ;
      private final float rotX;
      private final float rotY;
      private final float rotZ;
      private final float rotW;

      public WorldLabel(String var1, double var2, double var4, double var6, double var8, int var10) {
         this(var1, var2, var4, var6, var8, var10, 0.0F, 0.0F, 0.0F, 1.0F, false);
      }

      public WorldLabel(
         String var1, double var2, double var4, double var6, double var8, int var10, float var11, float var12, float var13, float var14, boolean var15
      ) {
         this(var1, var2, var4, var6, var8, var10, var11, var12, var13, var14, var15, null);
      }

      public WorldLabel(
         String text,
         double x,
         double y,
         double z,
         double scale,
         int color,
         float qx,
         float qy,
         float qz,
         float qw,
         boolean worldAligned,
         WorldTextRenderer.LabelMotion motion
      ) {
         this.text = text;
         this.posX = x;
         this.posY = y;
         this.posZ = z;
         this.scale = scale;
         this.color = color;
         this.rotX = qx;
         this.rotY = qy;
         this.rotZ = qz;
         this.rotW = qw;
         this.worldAligned = worldAligned;
         this.motion = motion;
      }
   }
}
