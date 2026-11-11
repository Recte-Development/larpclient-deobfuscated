package com.larpclient.module.render;

import com.larpclient.LarpClient;
import com.larpclient.gui.theme.Theme;
import com.larpclient.module.misc.BooleanSetting;
import com.larpclient.module.misc.Category;
import com.larpclient.module.misc.ColorSetting;
import com.larpclient.module.misc.MCFriendModule;
import com.larpclient.module.misc.ModeSetting;
import com.larpclient.module.misc.Module;
import com.larpclient.module.misc.NumberSetting;
import com.larpclient.module.misc.Setting;
import com.larpclient.render.misc.EffectBatch;
import com.larpclient.render.model.ModelVisualRenderer;
import com.larpclient.render.model.WingGeometry;
import com.larpclient.render.world.EffectsRenderer;
import com.larpclient.social.cosmetic.DragonWings;
import com.larpclient.util.data.TickTimer;
import com.larpclient.util.misc.LarpLog;
import com.larpclient.util.misc.PerfProfiler;
import com.larpclient.util.render.ColorUtil;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.PoseStack.Pose;
import java.util.ArrayList;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Set;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;

public final class WingsModule extends Module {
   public static final String sTYLE_LUMINOUS = "Luminous";
   public static final String sTYLE_ENDER_DRAGON = "Ender Dragon";
   private static WingsModule instance;
   private final BooleanSetting self = this.addSetting(new BooleanSetting("Self", "Show your wings in third person", true));
   private final BooleanSetting friends = this.addSetting(new BooleanSetting("Friends", "Show wings on friends", true));
   private final BooleanSetting others = this.addSetting(new BooleanSetting("Others", "Show wings on other players", false));
   private final NumberSetting size = this.addSetting(new NumberSetting("Size", "Wing size", 1.0, 0.75, 1.35, 0.05));
   private final BooleanSetting customColors = this.addSetting(new BooleanSetting("Custom Colors", "Cycle between your two colors", true));
   private final ColorSetting color1 = this.addSetting(new ColorSetting("Color 1", "First wing color", -52586));
   private final ColorSetting color2 = this.addSetting(new ColorSetting("Color 2", "Second wing color", -13461761));
   private final ModeSetting style = this.addSetting(new ModeSetting("Style", "Wing design", "Luminous", "Luminous", "Ender Dragon"));
   private final ColorSetting dragonColor = this.addSetting(
      new ColorSetting("Dragon Color", "Tints the Ender Dragon texture; white keeps it vanilla and alpha sets opacity", -1)
   );
   public static final int mAX_PREVIEW_WEARERS = 32;
   private List<WingsModule.WingPair> wingPairs = List.of();
   private List<WingsModule.DragonWingRender> dragonWings = List.of();
   private final IdentityHashMap<Object, WingGeometry.WingPreviewBuffer> previewBuffers = new IdentityHashMap<>();
   private ClientLevel renderedLevel;
   private ClientLevel bufferLevel;
   private DragonWings.WingModel dragonModel;
   private final PoseStack dragonPoseStack = new PoseStack();
   private final PoseStack bodyPoseStack = new PoseStack();

   public WingsModule() {
      super("Wings", "Wear luminous membrane wings or Ender Dragon wings", Category.VISUALS, -1);
      instance = this;
      this.customColors.visibleWhen(() -> this.style.isMode("Luminous"));
      this.color1.visibleWhen(() -> this.style.isMode("Luminous") && this.customColors.getValue());
      this.color2.visibleWhen(() -> this.style.isMode("Luminous") && this.customColors.getValue());
      this.dragonColor.visibleWhen(() -> this.style.isMode("Ender Dragon"));
      this.style.onChange(var0 -> LarpLog.info("Wings", "Style={}", var0));
   }

   public static WingsModule getInstance() {
      return instance;
   }

   public List<Setting<?>> getVisibilitySettings() {
      return List.of(this.self, this.friends, this.others);
   }

   public List<Setting<?>> getStyleSettings() {
      return List.of(this.style, this.size, this.customColors, this.color1, this.color2, this.dragonColor);
   }

   public boolean isDragonStyle() {
      return this.isEnabled() ? this.style.isMode("Ender Dragon") : false;
   }

   public WingsModule.WingStyle getWingStyle() {
      if (this.isEnabled()) {
         if (!this.isOperational() || !this.self.getValue()) {
            return null;
         } else if (this.isDragonStyle()) {
            return this.dragonColor.getValue() >>> 24 == 0
               ? null
               : new WingsModule.WingStyle(true, this.size.getValue().floatValue(), 0, 0, this.dragonColor.getValue());
         } else {
            return new WingsModule.WingStyle(
               false,
               this.size.getValue().floatValue(),
               this.customColors.getValue() ? this.color1.getValue() : Theme.accent,
               this.customColors.getValue() ? this.color2.getValue() : Theme.accentEnd,
               0
            );
         }
      } else {
         return null;
      }
   }

   public static int getVisibilitySettings(int var0, int var1, double var2) {
      return ColorUtil.lerpColor(var0, var1, (Math.sin(var2 * 2.0) + 1.0) / 2.0);
   }

   public static int blendColorOffset(int var0, int var1, double var2) {
      return ColorUtil.lerpColor(var0, var1, (Math.sin(var2 * 2.0 + 2.1) + 1.0) / 2.0);
   }

   @Override
   public void onEnable() {
      LarpLog.info(
         "Wings",
         this.isDragonStyle()
            ? "Ender Dragon wings enabled; vanilla dragon wing parts on the native torso, 48 quads per wearer, 32 nearby previews"
            : "Articulated membrane wings enabled on the native torso; shared bloom, 146 base quads per wearer, 32 nearby previews"
      );
   }

   @Override
   public void onDisable() {
      this.wingPairs = List.of();
      this.dragonWings = List.of();
      this.renderedLevel = null;
      this.previewBuffers.clear();
      this.bufferLevel = null;
   }

   public void updateWings(float var1) {
      if (this.isEnabled()) {
         this.wingPairs = List.of();
         this.dragonWings = List.of();
         this.renderedLevel = null;
         if (this.isOperational() && this.isInGame()) {
            if (this.bufferLevel != minecraft.level) {
               this.previewBuffers.clear();
               this.bufferLevel = minecraft.level;
            }

            MCFriendModule var2 = LarpClient.getInstance().getModuleManager().getModule(MCFriendModule.class);
            ArrayList var3 = new ArrayList();
            ArrayList var4 = new ArrayList();
            Set var5 = Collections.newSetFromMap(new IdentityHashMap());
            int var6 = 0;
            float var7 = this.size.getValue().floatValue();
            boolean var8 = this.isDragonStyle();
            int var9 = this.dragonColor.getValue();
            int var10 = this.customColors.getValue() ? this.color1.getValue() : Theme.accent;
            int var11 = this.customColors.getValue() ? this.color2.getValue() : Theme.accentEnd;
            int var12 = getVisibilitySettings(var10, var11, TickTimer.getTime());
            int var13 = blendColorOffset(var10, var11, TickTimer.getTime());

            for (AbstractClientPlayer var15 : minecraft.level.players()) {
               if (var15 == minecraft.player) {
                  if (!this.self.getValue() || minecraft.options.getCameraType().isFirstPerson()) {
                     continue;
                  }
               } else {
                  int var16 = var2 != null && var2.isFriend(var15.getName().getString()) ? 1 : 0;
                  if (!var16 != 0 ? this.friends.getValue() : this.others.getValue() || var6 >= 32) {
                     continue;
                  }
               }

               if (var15.isAlive()
                  && !var15.isInvisible()
                  && !var15.isSpectator()
                  && !var15.isSleeping()
                  && !var15.isInWater()
                  && !var15.isVisuallyCrawling()
                  && !var15.getItemBySlot(EquipmentSlot.CHEST).is(Items.ELYTRA)
                  && !(var15.distanceToSqr(minecraft.player) > 9216.0)) {
                  if (var15 != minecraft.player) {
                     var6++;
                  }

                  float var30 = Feature3DUIModule.getBodyYaw(var15, Mth.rotLerp(var1, var15.yBodyRotO, var15.yBodyRot), var1);
                  float var17 = var15.tickCount + var1;
                  float var18 = var15.walkAnimation.speed(var1);
                  Vec3 var19 = var15.getPosition(var1);
                  WingGeometry.WingPreviewBuffer var20 = this.previewBuffers.computeIfAbsent(var15, var0 -> new WingGeometry.WingPreviewBuffer());
                  var20.beginWith(var17, var15.isCrouching(), var18, var15.isFallFlying(), isAirborne(var15), var19);
                  var5.add(var15);
                  if (var8) {
                     float var31 = DragonWings.flapSpeed(var20.blend(), var15.isFallFlying(), var15.getFallFlyingTicks() + var1);
                     var20.end(DragonWings.flapSpeed(var20.ribbon(), var20.vertex(), var20.flush(), var31));
                     if (var9 >>> 24 != 0) {
                        DragonWings.WingAngles var32 = DragonWings.computePose(
                           var20.reset() + DragonWings.phaseOffset(var15.getId()), var20.ribbon(), var20.vertex(), var20.flush(), var31
                        );
                        var4.add(new WingsModule.DragonWingRender(ModelVisualRenderer.extractEntity(var15, var1), var32, var7, var9));
                     }
                  } else {
                     EntityRenderState var21 = ModelVisualRenderer.extractEntity(var15, var1);
                     Pose var22 = ModelVisualRenderer.bodyAttachment(var21, this.bodyPoseStack);
                     int var23 = var21 instanceof HumanoidRenderState var24 && var24.isCrouching ? 1 : 0;
                     float var33 = var22 == null
                        ? var20.blend()
                        : DragonWings.flapSpeed(var20.blend(), var15.isFallFlying(), var15.getFallFlyingTicks() + var1);
                     WingGeometry.WingPose var25 = WingGeometry.poseFromBlend(var20.vertex(), var33, var15.getFallFlyingTicks() + var1, var15.getViewXRot(var1));
                     WingGeometry.AnimatedWing var26 = WingGeometry.animatePoints(var17, var20.ribbon(), var20.vertex(), var33);
                     Vec3 var27 = var22 == null ? var19 : minecraft.gameRenderer.mainCamera().position();
                     Matrix4f var28 = var22 == null
                        ? WingGeometry.entityWingMatrix(var25, var30, var17, var20.ribbon(), var7, -1)
                        : new Matrix4f(var22.pose()).mul(WingGeometry.chestWingMatrix(var25, var17, var20.ribbon(), var7, -1, (boolean)var23));
                     Matrix4f var29 = var22 == null
                        ? WingGeometry.entityWingMatrix(var25, var30, var17, var20.ribbon(), var7, 1)
                        : new Matrix4f(var22.pose()).mul(WingGeometry.chestWingMatrix(var25, var17, var20.ribbon(), var7, 1, (boolean)var23));
                     var3.add(
                        new WingsModule.WingPair(
                           WingGeometry.buildWing(var27, var28, -1, var26, var12, var13, var7),
                           WingGeometry.buildWing(var27, var29, 1, var26, var12, var13, var7)
                        )
                     );
                  }
               }
            }

            this.previewBuffers.keySet().removeIf(var1x -> !var5.contains(var1x));
            this.renderedLevel = minecraft.level;
            this.wingPairs = List.copyOf(var3);
            this.dragonWings = List.copyOf(var4);
         } else {
            this.previewBuffers.clear();
            this.bufferLevel = null;
         }
      }
   }

   private static boolean isAirborne(Player var0) {
      return !var0.onGround() && !var0.isPassenger() && !var0.onClimbable() && !var0.isFallFlying();
   }

   public boolean renderWings(PoseStack var1, EffectBatch var2) {
      if (!this.isEnabled()) {
         return false;
      }

      if (this.isOperational() && this.renderedLevel == minecraft.level && !this.dragonWings.isEmpty()) {
         if (this.dragonModel == null) {
            this.dragonModel = DragonWings.WingModel.createLayer();
            LarpLog.info("Wings", "Ender Dragon wing parts ready from the vanilla layer definition; texture={}", DragonWings.dRAGON_TEXTURE);
         }

         long var3 = PerfProfiler.startTimer();
         byte var5 = 0;

         for (WingsModule.DragonWingRender var7 : this.dragonWings) {
            Pose var8 = ModelVisualRenderer.bodyAttachment(var7.state, var1);
            if (var8 != null) {
               this.dragonPoseStack.last().set(var8);
               this.dragonModel.render(var7.angles);
               int var9 = var7.state instanceof HumanoidRenderState var10 && var10.isCrouching ? 1 : 0;
               this.dragonModel
                  .getRoot(
                     this.dragonPoseStack, var2.getConsumer(DragonWings.renderType(var7.color)), var7.size, (boolean)var9, var7.state.lightCoords, var7.color
                  );
               var5 = 1;
            }
         }

         PerfProfiler.endTimer("Wings Ender Dragon attachment and submission", var3);
         return (boolean)var5;
      } else {
         return false;
      }
   }

   public void renderWings(EffectsRenderer var1, EffectBatch var2) {
      if (this.isEnabled()) {
         if (this.isOperational() && this.renderedLevel == minecraft.level) {
            for (WingGeometry.RenderMode var6 : WingGeometry.RenderMode.values()) {
               for (WingsModule.WingPair var8 : this.wingPairs) {
                  WingGeometry.renderWing(var1, var8.left, var6);
                  WingGeometry.renderWing(var1, var8.right, var6);
               }

               var2.flush();
            }
         }
      }
   }

   public boolean hasMembraneOutline() {
      return !this.isEnabled()
         ? false
         : this.isOperational() && this.renderedLevel == minecraft.level && this.wingPairs.stream().anyMatch(var0 -> WingGeometry.membraneColor(var0.left));
   }

   public void renderOutlines(EffectsRenderer var1, EffectBatch var2) {
      if (this.isEnabled()) {
         if (this.hasMembraneOutline()) {
            for (WingsModule.WingPair var4 : this.wingPairs) {
               WingGeometry.renderOutline(var1, var4.left);
               WingGeometry.renderOutline(var1, var4.right);
            }

            var2.flush();
         }
      }
   }

   private record DragonWingRender(EntityRenderState state, DragonWings.WingAngles angles, float size, int color) {
   }

   private record WingPair(WingGeometry.WingInstance left, WingGeometry.WingInstance right) {
   }

   public record WingStyle(boolean dragon, float size, int first, int second, int dragonColor) {
   }
}
