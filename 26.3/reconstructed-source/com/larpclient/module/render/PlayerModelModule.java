package com.larpclient.module.render;

import com.larpclient.core.ViewController;
import com.larpclient.module.misc.BooleanSetting;
import com.larpclient.module.misc.Category;
import com.larpclient.module.misc.ModeSetting;
import com.larpclient.module.misc.Module;
import com.larpclient.module.misc.NumberSetting;
import com.larpclient.module.misc.Setting;
import com.larpclient.render.misc.EffectBatch;
import com.larpclient.render.model.BananaModel;
import com.larpclient.render.model.BananaModel2;
import com.larpclient.render.model.LegRig;
import com.larpclient.render.model.ModelTransforms;
import com.larpclient.render.model.ModelTransforms2;
import com.larpclient.render.overlay.PostProcessing;
import com.larpclient.social.emote.EmoteAnimation;
import com.larpclient.social.emote.EmoteState;
import com.larpclient.social.pet.SahurPet;
import com.larpclient.util.misc.LarpLog;
import com.mojang.blaze3d.vertex.PoseStack;
import java.util.List;
import java.util.Locale;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;

public final class PlayerModelModule extends Module {
   private static PlayerModelModule instance;
   private final ModeSetting character = this.addSetting(
      isBananaAvailable()
         ? new ModeSetting("Character", "Which character replaces your body", "Tung Tung Tung Sahur", "Tung Tung Tung Sahur", "Banana")
         : new ModeSetting("Character", "Which character replaces your body", "Tung Tung Tung Sahur", "Tung Tung Tung Sahur")
   );
   private final NumberSetting scale = this.addSetting(new NumberSetting("Scale", "Visual character size", 1.0, 0.5, 2.0, 0.05));
   private final BooleanSetting showTiming = this.addSetting(new BooleanSetting("Show Timing", "Show CPU model submission timing", false));
   private Object lastPlayer;
   private Object lastLevel;
   private long lastReportNanos;
   private long cpuNanos;
   private long frameCount;
   private boolean poseReady;
   private LivingEntityRenderState renderState;
   private int lastReloadCount = -1;
   private final Matrix4f sahurMatrix = new Matrix4f();
   private float currentScale;
   private boolean crouching;
   private LegRig legRig;
   private BananaModel bananaModel;
   private final Matrix4f bananaMatrix = new Matrix4f();
   private final float[] poseChannels = new float[EmoteAnimation.totalChannels];
   private boolean emoting;

   public static boolean isBananaAvailable() {
      return PlayerModelModule.class.getResource("/assets/larp-client/models/player/banana/mesh.bin") != null;
   }

   public PlayerModelModule() {
      super("Player Model", "Replace your rendered player with a character", Category.VISUALS, -1);
      instance = this;
      this.character.onChange(var1 -> {
         this.reset();
         LarpLog.debug("PlayerModel", "Character changed to {}", var1);
      });
   }

   public static PlayerModelModule getInstance() {
      return instance;
   }

   public List<Setting<?>> getCharacterSettings() {
      return List.of(this.character, this.scale);
   }

   public List<Setting<?>> onResourceReload() {
      return List.of(this.showTiming);
   }

   private boolean isBananaSelected() {
      return "Banana".equals(this.character.getValue());
   }

   public boolean isActive() {
      return !this.isEnabled()
         ? false
         : this.isRuntimeActive()
            && this.lastReloadCount != PostProcessing.reloadCount
            && (this.isBananaSelected() ? BananaModel2.isReady() && this.bananaModel != null : SahurPet.isReady());
   }

   public boolean onResourceReload(EntityRenderState var1) {
      return !this.isEnabled() ? false : this.isActive() && this.renderState == var1;
   }

   public void setRenderState(LivingEntityRenderState var1) {
      if (this.isEnabled()) {
         this.renderState = var1;
      }
   }

   @Override
   public void onEnable() {
      this.reset();

      try {
         this.setRenderState();
      } catch (Exception var2) {
         LarpLog.error("PlayerModel", "Mesh stage failed; retaining native body", var2);
         this.setEnabled(false);
      }
   }

   private void setRenderState() {
      if (this.isBananaSelected()) {
         BananaModel2.loadMesh();
         if (this.bananaModel == null || this.bananaModel.getMesh() != BananaModel2.getMesh()) {
            this.bananaModel = new BananaModel(BananaModel2.getMesh());
         }

         LarpLog.debug(
            "PlayerModel",
            "Banana ready: {} triangles, {} vertices, scale={}",
            this.bananaModel.getMesh().triangles(),
            this.bananaModel.getMesh().vertices(),
            this.scale.getValue()
         );
      } else {
         SahurPet.prepareMesh();
         if (this.legRig == null || this.legRig.getSourceModel() != SahurPet.getMesh()) {
            this.legRig = new LegRig(SahurPet.getMesh());
         }

         LarpLog.debug("PlayerModel", "Sahur ready: walk rig, {} triangles, scale={}", this.legRig.getMesh().triangles(), this.scale.getValue());
      }
   }

   @Override
   public void onDisable() {
      this.reset();
   }

   public void reset() {
      this.poseReady = (boolean)0;
      this.renderState = null;
      this.lastPlayer = this.lastLevel = null;
      this.frameCount = this.cpuNanos = 0L;
   }

   public void onResourceReload() {
      this.reset();
      this.lastReloadCount = PostProcessing.reloadCount;
   }

   public void updatePose(float var1, Vec3 var2) {
      if (this.isEnabled()) {
         this.poseReady = (boolean)0;
         if (this.isRuntimeActive() && this.isInGame() && this.lastReloadCount != PostProcessing.reloadCount) {
            if (this.isBananaSelected()
               ? !BananaModel2.isReady() || this.bananaModel == null || this.bananaModel.getMesh() != BananaModel2.getMesh()
               : !SahurPet.isReady() || this.legRig == null || this.legRig.getSourceModel() != SahurPet.getMesh()) {
               this.setRenderState();
            }

            if (this.isActive() && this.isInGame()) {
               LocalPlayer var3 = minecraft.player;
               if (this.lastPlayer != var3 || this.lastLevel != minecraft.level) {
                  LivingEntityRenderState var4 = this.renderState;
                  this.reset();
                  this.renderState = var4;
                  this.lastPlayer = var3;
                  this.lastLevel = minecraft.level;
               }

               if (!minecraft.options.getCameraType().isFirstPerson() || ViewController.isFreecam()) {
                  if (this.renderState != null) {
                     this.currentScale = this.scale.getValue().floatValue();
                     this.crouching = var3.isCrouching();
                     if (this.isBananaSelected()) {
                        this.buildBananaPose(var3, var1, var2);
                        this.poseReady = (boolean)1;
                     } else {
                        this.legRig.animate(this.renderState.walkAnimationPos, this.renderState.walkAnimationSpeed);
                        ModelTransforms.buildModelMatrix(
                           this.sahurMatrix,
                           SahurPet.getMesh(),
                           (float)(Mth.lerp(var1, var3.xOld, var3.getX()) - var2.x),
                           (float)(Mth.lerp(var1, var3.yOld, var3.getY()) - var2.y),
                           (float)(Mth.lerp(var1, var3.zOld, var3.getZ()) - var2.z),
                           (float)Math.toRadians(Feature3DUIModule.getBodyYaw(var3, Mth.rotLerp(var1, var3.yBodyRotO, var3.yBodyRot), var1)),
                           this.currentScale,
                           this.crouching
                        );
                        this.poseReady = (boolean)1;
                     }
                  }
               }
            }
         }
      }
   }

   private void buildBananaPose(LocalPlayer var1, float var2, Vec3 var3) {
      long var4 = System.nanoTime();
      float[] var6 = null;
      float var7 = 0.0F;
      if (this.renderState instanceof EmoteState var8 && var8.larp$emoteWeight() > 0.0F) {
         var6 = var8.larp$emoteSample();
         var7 = var8.larp$emoteWeight();
      }

      BananaModel.buildPose(
         this.poseChannels,
         this.renderState.walkAnimationPos,
         this.renderState.walkAnimationSpeed,
         this.renderState.ageInTicks,
         this.renderState.yRot,
         this.renderState.xRot,
         this.crouching
      );
      if (var6 != null) {
         BananaModel.blendPose(this.poseChannels, var6, var7);
      }

      this.bananaModel.updatePose(this.poseChannels, 1.0F - var7);
      if (var6 != null && !this.emoting) {
         LarpLog.debug("PlayerModel", "Banana emote started");
      }

      this.emoting = (boolean)(var6 != null ? 1 : 0);
      float var10 = var6 != null ? this.renderState.bodyRot : Feature3DUIModule.getBodyYaw(var1, Mth.rotLerp(var2, var1.yBodyRotO, var1.yBodyRot), var2);
      ModelTransforms2.buildModelMatrix(
         this.bananaMatrix,
         this.bananaModel.getMesh(),
         (float)(Mth.lerp(var2, var1.xOld, var1.getX()) - var3.x),
         (float)(Mth.lerp(var2, var1.yOld, var1.getY()) - var3.y),
         (float)(Mth.lerp(var2, var1.zOld, var1.getZ()) - var3.z),
         (float)Math.toRadians(var10),
         this.currentScale,
         var6,
         var7
      );
      if (this.showTiming.getValue()) {
         this.cpuNanos = this.cpuNanos + (System.nanoTime() - var4);
      }
   }

   public void renderModel(PoseStack var1, EffectBatch var2) {
      if (this.isEnabled()) {
         if (this.poseReady && this.isActive() && this.lastPlayer == minecraft.player && this.lastLevel == minecraft.level) {
            long var3 = System.nanoTime();

            try {
               if (this.isBananaSelected()) {
                  BananaModel2.render(var1, var2, this.bananaMatrix, this.bananaModel);
               } else {
                  SahurPet.renderCustomMesh(var1, var2, this.sahurMatrix, this.legRig.getVertices(), this.legRig.getMesh().colors());
               }
            } catch (RuntimeException var6) {
               LarpLog.error("PlayerModel", "Submission failed; restoring native body", var6);
               this.setEnabled(false);
            }

            if (this.showTiming.getValue()) {
               this.cpuNanos = this.cpuNanos + (System.nanoTime() - var3);
               this.frameCount++;
               if (System.nanoTime() - this.lastReportNanos > 1000000000L) {
                  minecraft.player
                     .sendOverlayMessage(
                        Component.literal(
                           String.format(
                              Locale.ROOT, "%s: %.3f ms CPU/frame", this.character.getValue(), this.cpuNanos / 1000000.0 / Math.max(1L, this.frameCount)
                           )
                        )
                     );
                  this.lastReportNanos = System.nanoTime();
                  this.cpuNanos = this.frameCount = 0L;
               }
            }
         }
      }
   }

   public boolean translateToHand(EntityRenderState var1, HumanoidArm var2, PoseStack var3) {
      if (this.isEnabled()) {
         if (this.onResourceReload(var1) && this.poseReady) {
            int var4 = var2 == HumanoidArm.RIGHT ? 1 : 0;
            if (this.isBananaSelected()) {
               float var5 = this.renderState instanceof EmoteState var6 ? var6.larp$emoteWeight() : 0.0F;
               ModelTransforms2.translateToHand(
                  var3,
                  this.bananaModel.getMesh(),
                  this.bananaModel.blendPose((boolean)var4),
                  (boolean)var4,
                  this.currentScale,
                  this.crouching,
                  this.renderState.scale,
                  var5 > 0.0F ? ((EmoteState)this.renderState).larp$emoteSample() : null,
                  var5
               );
               return true;
            } else {
               ModelTransforms.applyOffsetTransform(
                  var3,
                  SahurPet.getMesh(),
                  (boolean)var4,
                  this.legRig.transformPoint((boolean)var4, ModelTransforms.handOffset((boolean)var4)),
                  this.currentScale,
                  this.crouching,
                  this.renderState.scale
               );
               return true;
            }
         } else {
            return false;
         }
      } else {
         return false;
      }
   }
}
