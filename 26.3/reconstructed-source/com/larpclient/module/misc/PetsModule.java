package com.larpclient.module.misc;

import com.larpclient.gui.clickgui.ClickGuiScreen;
import com.larpclient.gui.theme.Theme;
import com.larpclient.hud.NotificationManager;
import com.larpclient.render.entity.PetModelRenderer;
import com.larpclient.render.misc.VisualsRenderer;
import com.larpclient.render.overlay.FontRenderer;
import com.larpclient.render.world.EffectsRenderer;
import com.larpclient.social.pet.SahurPet;
import com.larpclient.util.data.TickTimer;
import com.larpclient.util.entity.SwingHelper;
import com.larpclient.util.misc.LarpLog;
import com.larpclient.util.render.ColorUtil;
import com.larpclient.util.render.DrawUtil;
import com.larpclient.util.render.ScreenProjection;
import com.mojang.blaze3d.systems.RenderSystem;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.ClipContext.Block;
import net.minecraft.world.level.ClipContext.Fluid;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.HitResult.Type;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public final class PetsModule extends Module {
   private static PetsModule instance;
   private final ModeSetting pet = this.addSetting(
      new ModeSetting("Pet", "Companion style", "Tung Tung Tung Sahur", "Dragon", "Jellyfish", "Rune Orb", "Ghost", "Tung Tung Tung Sahur")
   );
   private final BooleanSetting realism = this.addSetting(new BooleanSetting("Realism", "Detailed anatomy, natural finishes and reactive gaze", false));
   private final ModeSetting realisticPet = this.addSetting(
      new ModeSetting(
         "Realistic Pet", "Detailed companion style", "Tung Tung Tung Sahur", "Dragon", "Jellyfish", "Rune Orb", "Bee", "Ghost", "Tung Tung Tung Sahur"
      )
   );
   private final NumberSetting realisticSize = this.addSetting(new NumberSetting("Realistic Size", "Detailed companion size", 1.0, 0.5, 2.5, 0.05));
   private final NumberSetting size = this.addSetting(new NumberSetting("Size", "Pet size", 1.3, 0.5, 2.5, 0.05));
   private final ModeSetting position = this.addSetting(new ModeSetting("Position", "Where the companion follows", "Shoulder", "Shoulder", "Orbit", "Behind"));
   private final BooleanSetting sparkleTrail = this.addSetting(new BooleanSetting("Sparkle trail", "Sparks follow the companion", true));
   private final BooleanSetting inFirstPerson = this.addSetting(new BooleanSetting("In first person", "Show the companion in first person", true));
   private final BooleanSetting speechBubbles = this.addSetting(new BooleanSetting("Speech bubbles", "Show notifications above an on-screen companion", true));
   private final ModeSetting colorMode = this.addSetting(new ModeSetting("Color mode", "Realistic companion palette", "Natural", "Natural", "Theme", "Custom"));
   private final ColorSetting customColor = this.addSetting(new ColorSetting("Custom color", "Realistic companion glow", -1459895));
   private final Map<String, PetsModule.PetSettings> petSettings = new LinkedHashMap<>();
   private Object level;
   private Object player;
   private Vec3 petPos;
   private Vec3 velocity = Vec3.ZERO;
   private float facingYaw = Float.NaN;
   private float bodyYaw;
   private float bodyPitch;
   private double gazeYaw;
   private double gazePitch;
   private double hurtReaction;
   private double excitement;
   private double swingProgress;
   private double wingPhase;
   private double wingSpeed = 6.5;
   private double flightBlend;
   private double time;
   private double lastTime;
   private int lastHurtTime;
   private boolean wasSwinging;
   private String styleKey;
   private final ArrayDeque<PetsModule.Spark> sparks = new ArrayDeque<>();
   private final ArrayDeque<PetsModule.Notification> pendingNotifications = new ArrayDeque<>();
   private PetsModule.Notification currentNotification;
   private long notificationStart;
   private long lastLabelVisibleNanos;
   private PetsModule.Frame frame;
   private final Random random = new Random(1279349328L);
   private double sparkAccumulator;

   public PetsModule() {
      super("Pets", "A local companion with reactive motion and speech bubbles", Category.RENDER, -1);
      instance = this;
      this.pet.visibleWhen(() -> !this.realism.getValue());
      this.size.visibleWhen(() -> !this.realism.getValue());
      this.realisticPet.visibleWhen(() -> this.realism.getValue());
      this.realisticSize.visibleWhen(() -> this.realism.getValue());
      this.petSettings.put("Dragon", new PetsModule.PetSettings("Dragon", 50, "Scales", "Obsidian", "Gold"));
      this.petSettings.put("Jellyfish", new PetsModule.PetSettings("Jellyfish", 70, "Glass", "Energy", "Crystal"));
      this.petSettings.put("Rune Orb", new PetsModule.PetSettings("Rune Orb", 70, "Glass", "Crystal", "Obsidian"));
      this.petSettings.put("Ghost", new PetsModule.PetSettings("Ghost", 50, "Wisp", "Silk", "Glass"));
      this.speechBubbles.onChange(var1 -> {
         if (!var1) {
            this.flushNotifications();
            this.pendingNotifications.clear();
            this.currentNotification = null;
         }
      });
      this.colorMode.visibleWhen(() -> this.realism.getValue() && !isSahur(this.realisticPet.getValue()));
      this.customColor.visibleWhen(() -> this.colorMode.isVisible() && this.colorMode.isMode("Custom"));
   }

   public static PetsModule getInstance() {
      return instance;
   }

   public List<Setting<?>> getAppearanceSettings() {
      return List.of(this.realism, this.pet, this.realisticPet, this.size, this.realisticSize);
   }

   public List<Setting<?>> getBehaviorSettings() {
      return List.of(this.position, this.inFirstPerson, this.sparkleTrail, this.speechBubbles);
   }

   public List<Setting<?>> hasGlow() {
      ArrayList var1 = new ArrayList<>(List.of(this.colorMode, this.customColor));

      for (PetsModule.PetSettings var3 : this.petSettings.values()) {
         var1.addAll(List.of(var3.finish, var3.naturalColours, var3.color));
      }

      return List.copyOf(var1);
   }

   public List<Setting<?>> updateFrame() {
      ArrayList var1 = new ArrayList();

      for (PetsModule.PetSettings var3 : this.petSettings.values()) {
         var1.addAll(List.of(var3.glow, var3.glowColor));
      }

      return List.copyOf(var1);
   }

   @Override
   public void onEnable() {
      this.reset();
      LarpLog.debug("Pets", "Local companion activated: {}", this.pet.getValue());
   }

   @Override
   public void onDisable() {
      this.reset();
   }

   public void reset() {
      this.frame = null;
      this.lastLabelVisibleNanos = 0L;
      this.flushNotifications();
      this.level = this.player = null;
      this.petPos = null;
      this.velocity = Vec3.ZERO;
      this.facingYaw = Float.NaN;
      this.frame = null;
      this.sparks.clear();
      this.pendingNotifications.clear();
      this.currentNotification = null;
      this.lastLabelVisibleNanos = 0L;
      this.lastTime = TickTimer.getTime();
      this.styleKey = null;
      this.gazeYaw = this.gazePitch = this.hurtReaction = this.excitement = this.wingPhase = this.flightBlend = this.time = 0.0;
      this.swingProgress = 1.0;
      this.wingSpeed = 6.5;
      this.lastHurtTime = 0;
      this.wasSwinging = (boolean)0;
      this.sparkAccumulator = 0.0;
      this.random.setSeed(1279349328L);
   }

   private void flushNotifications() {
      PetsModule.Notification var1 = this.currentNotification;
      long var2 = this.notificationStart;
      List var4 = List.copyOf(this.pendingNotifications);
      this.currentNotification = null;
      this.pendingNotifications.clear();
      NotificationManager var5 = NotificationManager.getInstance();
      if (var1 != null) {
         long var6 = var1.duration - (System.nanoTime() - var2) / 1000000L;
         if (var6 > 0L) {
            var5.push(var1.title, var1.text, var1.type, var6);
         }
      }

      for (PetsModule.Notification var7 : var4) {
         var5.push(var7.title, var7.text, var7.type, var7.duration);
      }
   }

   public void excite() {
      if (this.isEnabled()) {
         if (this.isRuntimeActive()) {
            this.excitement = Math.max(this.excitement, 1.5);
            this.swingProgress = 0.0;
         }
      }
   }

   public void updateFrame(float var1, CameraRenderState var2) {
      if (this.isEnabled()) {
         this.frame = null;
         if (this.isRuntimeActive() && this.isInGame()) {
            Minecraft var3 = getMc();
            boolean var4 = this.realism.getValue();
            String var5 = var4 ? this.realisticPet.getValue() : this.pet.getValue();
            boolean var6 = isSahur(var5);
            if (this.level != var3.level || this.player != var3.player || !Objects.equals(this.styleKey, var5 + ":" + var4)) {
               this.reset();
               this.level = var3.level;
               this.player = var3.player;
               this.styleKey = var5 + ":" + var4;
               this.lastTime = TickTimer.getTime();
            }

            double var7 = Math.clamp(TickTimer.getTime() - this.lastTime, 0.0, 0.1);
            this.lastTime = TickTimer.getTime();
            this.time += var7;
            double var9 = petScale(var5, var4 ? this.realisticSize.getValue() : this.size.getValue());
            Vec3 var11 = var3.player.getEyePosition(var1);
            Vec3 var12 = var3.player.getDeltaMovement();
            float var13 = (float)Math.toRadians(var3.player.getViewYRot(var1));
            if (Float.isNaN(this.facingYaw)) {
               this.facingYaw = var13;
            }

            if (var12.horizontalDistance() * 20.0 > 1.0) {
               this.facingYaw = this.facingYaw + wrapAngle((float)Math.atan2(-var12.x, var12.z) - this.facingYaw) * (float)Math.min(1.0, var7 * 3.0);
            } else if ((var3.options.keyUse.isDown() || var3.options.keyAttack.isDown()) && this.petPos != null) {
               Vec3 var14 = this.petPos.subtract(var11);
               if (var14.length() < 6.0 && var14.length() > 0.001 && var14.normalize().dot(var3.player.getViewVector(var1)) > Math.cos(Math.toRadians(28.0))) {
                  this.facingYaw = this.facingYaw + wrapAngle(var13 - this.facingYaw) * (float)Math.min(1.0, var7 * 6.0);
               }
            }

            Vec3 var47 = new Vec3(-Math.sin(this.facingYaw), 0.0, Math.cos(this.facingYaw));
            Vec3 var15 = new Vec3(-var47.z, 0.0, var47.x);

            Vec3 var16 = switch ((String)this.position.getValue()) {
               case "Orbit" -> var11.add(
                  Math.cos(this.time * 0.9) * 1.3 * var9, -0.15 + Math.sin(this.time * 1.7) * 0.15, Math.sin(this.time * 0.9) * 1.3 * var9
               );
               case "Behind" -> var11.add(var47.scale(-1.4 * var9)).add(var15.scale(0.35)).add(0.0, -0.95, 0.0);
               default -> var11.add(var15.scale(0.72 * var9)).add(var47.scale(-0.35)).add(0.0, 0.28 * var9, 0.0);
            };
            BlockHitResult var48 = var3.level.clip(new ClipContext(var11, var16, Block.COLLIDER, Fluid.NONE, var3.player));
            if (var48.getType() == Type.BLOCK) {
               var16 = var48.getLocation().subtract(var16.subtract(var11).normalize().scale(0.3 * var9));
            }

            if (this.petPos == null || this.petPos.distanceToSqr(var16) > 400.0) {
               this.petPos = var16;
               this.velocity = Vec3.ZERO;
               this.sparks.clear();
            }

            int var49 = var3.player.hurtTime;
            if (!var6 && var49 > this.lastHurtTime && var49 >= 6) {
               this.hurtReaction = 1.0;
               this.velocity = this.velocity.add(this.petPos.subtract(var11).normalize().scale(2.5));
            }

            this.lastHurtTime = var49;
            this.hurtReaction = Math.max(0.0, this.hurtReaction - var7 * 1.6);
            this.excitement = Math.max(0.0, this.excitement - var7);
            if (SwingHelper.isSwinging(var3.player) && !this.wasSwinging) {
               this.excitement = Math.max(this.excitement, 1.1);
               this.swingProgress = 0.0;
            }

            this.wasSwinging = SwingHelper.isSwinging(var3.player);
            this.swingProgress = Math.min(1.0, this.swingProgress + var7 / 0.65);
            this.velocity = this.velocity.add(var16.subtract(this.petPos).scale(26.0 * var7)).scale(Math.pow(0.004, var7));
            this.petPos = this.petPos.add(this.velocity.scale(var7));
            double var19 = this.velocity.length();
            float var21 = var19 > 0.6 ? (float)Math.atan2(-this.velocity.x, this.velocity.z) : this.facingYaw;
            this.bodyYaw = this.bodyYaw + wrapAngle(var21 - this.bodyYaw) * (float)Math.min(1.0, var7 * 6.0);
            this.bodyPitch = this.bodyPitch + ((float)Math.clamp(-this.velocity.y * 0.35, -0.6, 0.6) - this.bodyPitch) * (float)Math.min(1.0, var7 * 5.0);
            Vec3 var22 = this.petPos
               .add(
                  0.0,
                  Math.sin(this.time * 2.1) * 0.05 * var9 + (!var6 && this.swingProgress < 1.0 ? Math.sin(this.swingProgress * Math.PI) * 0.12 * var9 : 0.0),
                  0.0
               );
            Vec3 var23 = (var3.hitResult != null && var3.hitResult.getType() != Type.MISS
                  ? var3.hitResult.getLocation()
                  : var11.add(var3.player.getViewVector(var1).scale(6.0)))
               .subtract(var22);
            double var24 = wrapAngle((float)(Math.atan2(-var23.x, var23.z) - this.bodyYaw));
            double var26 = -Math.atan2(var23.y, var23.horizontalDistance());
            int var28 = Math.abs(var24) < 1.6 ? 1 : 0;
            this.gazeYaw = this.gazeYaw
               + ((var28 != 0 ? Math.clamp(var24, -1.1, 1.1) : Math.sin(this.time * 0.6) * 0.35) - this.gazeYaw) * (1.0 - Math.exp(-4.0 * var7));
            this.gazePitch = this.gazePitch + ((var28 != 0 ? Math.clamp(var26, -0.6, 0.6) : 0.0) - this.gazePitch) * (1.0 - Math.exp(-4.0 * var7));
            this.flightBlend = this.flightBlend + (Math.clamp((var19 - 2.5) / 3.0, 0.0, 1.0) - this.flightBlend) * Math.min(1.0, var7 * 3.0);
            this.wingSpeed = this.wingSpeed + (6.5 + Math.min(var19, 2.5) * 0.6 - this.flightBlend * 3.0 - this.wingSpeed) * Math.min(1.0, var7 * 2.0);
            this.wingPhase = (this.wingPhase + var7 * this.wingSpeed) % (Math.PI * 2);
            double var29 = Math.sin(this.wingPhase) * (1.0 - this.flightBlend * 0.75) + this.flightBlend * 0.15;
            int var31 = var3.options.getCameraType().isFirstPerson() && !this.inFirstPerson.getValue() ? 0 : 1;
            if (var31 != 0) {
               if (var6) {
                  SahurPet.prepareMesh();
               }

               PetsModule.PetSettings var36 = this.petSettings.getOrDefault(var5, this.petSettings.get("Dragon"));
               int var32;
               int var33;
               double var34;
               if (var6) {
                  var32 = -1;
                  var33 = Theme.accent;
                  var34 = this.sparkleTrail.getValue() ? 0.5 : 0.0;
               } else if (var4) {
                  int var37 = switch (var5) {
                     case "Dragon" -> -10976403;
                     case "Ghost" -> -1840922;
                     case "Jellyfish" -> -7288099;
                     case "Rune Orb" -> -4025601;
                     default -> -2116013;
                  };
                  var32 = var37;

                  var33 = switch ((String)this.colorMode.getValue()) {
                     case "Theme" -> Theme.accent;
                     case "Custom" -> this.customColor.getValue();
                     default -> var37;
                  };
                  var34 = 0.6;
               } else {
                  var32 = var36.bodyColor();
                  var33 = var36.glowColorValue();
                  var34 = var36.glow.getValue() / 100.0;
               }

               this.sparks.removeIf(var1x -> this.time - var1x.birth > var1x.lifetime);
               if (this.sparkleTrail.getValue() && var7 > 0.0) {
                  this.sparkAccumulator = this.sparkAccumulator + var7 * (15.0 + Math.min(36.0, var19 * 18.0));

                  while (this.sparkAccumulator >= 1.0) {
                     this.sparkAccumulator--;
                     Vec3 var50 = var22.add(
                        this.random.nextGaussian() * 0.06 * var9, this.random.nextGaussian() * 0.06 * var9, this.random.nextGaussian() * 0.06 * var9
                     );
                     this.sparks.addLast(new PetsModule.Spark(var50, this.time, 0.8 + this.random.nextDouble() * 0.8, 0.8 + this.random.nextDouble() * 0.8));

                     while (this.sparks.size() > 96) {
                        this.sparks.removeFirst();
                     }
                  }
               }

               if (!this.sparkleTrail.getValue()) {
                  this.sparks.clear();
                  this.sparkAccumulator = 0.0;
               }

               BlockHitResult var51 = var3.level.clip(new ClipContext(var22, var22.add(0.0, -4.5, 0.0), Block.COLLIDER, Fluid.ANY, var3.player));
               Vec3 var53 = var51.getType() == Type.BLOCK ? var51.getLocation().add(0.0, 0.012, 0.0) : null;
               double var55 = var53 == null ? 0.0 : Math.pow(Math.max(0.0, 1.0 - (var22.y - var53.y) / 4.5), 2.0) * 0.4;
               Matrix4f var41 = new Matrix4f(var2.projectionMatrix).mul(var2.viewRotationMatrix);
               Vector3f var42 = new Quaternionf(var2.orientation).transform(new Vector3f(0.0F, 1.0F, 0.0F));
               ScreenProjection.ProjectedPoint var43 = ScreenProjection.projectPoint(
                  var22.add(0.0, 0.32 * var9, 0.0),
                  var2.pos,
                  var41,
                  var42,
                  var3.getWindow().getGuiScaledWidth(),
                  var3.getWindow().getGuiScaledHeight(),
                  RenderSystem.getDevice().getDeviceInfo().isZZeroToOne()
               );
               if (var43 != null) {
                  this.lastLabelVisibleNanos = System.nanoTime();
               }

               double var44 = this.swingProgress < 1.0 ? (1.0 - Math.pow(1.0 - this.swingProgress, 3.0)) * Math.PI * 2.0 : 0.0;
               Quaternionf var46 = var6
                  ? new Quaternionf().rotationY(-this.bodyYaw)
                  : new Quaternionf().rotationY((float)(-this.bodyYaw + var44)).rotateX(this.bodyPitch);
               this.frame = new PetsModule.Frame(
                  var5,
                  var36.finish.getValue(),
                  var4,
                  var22,
                  var2.pos.subtract(var22).normalize(),
                  var46,
                  var9,
                  this.time,
                  var19,
                  var29,
                  this.gazeYaw,
                  this.gazePitch,
                  Math.max(this.excitement, this.hurtReaction),
                  var32,
                  var33,
                  var34,
                  var53,
                  var55,
                  List.copyOf(this.sparks),
                  var43
               );
            }
         }
      }
   }

   private static float wrapAngle(float var0) {
      return (float)Math.atan2(Math.sin(var0), Math.cos(var0));
   }

   public static double petScale(String var0, double var1) {
      return var1 * (var0.equals("Tung Tung Tung Sahur") ? 1.2 : 1.0);
   }

   static boolean isSahur(String var0) {
      return var0.equals("Tung Tung Tung Sahur");
   }

   public boolean hasGlow() {
      return !this.isEnabled() ? false : this.frame != null && this.frame.glowStrength > 0.0;
   }

   public void render(EffectsRenderer var1, boolean var2) {
      if (this.isEnabled()) {
         PetsModule.Frame var3 = this.frame;
         if (var3 != null && (!var2 || !(var3.glowStrength <= 0.0))) {
            if (!var2 && var3.shadow != null) {
               var1.drawDisc(var3.shadow, 0.18 * var3.scale, 0.16 * var3.scale, ColorUtil.withAlpha(-16777216, var3.shadowAlpha));
            }

            PetModelRenderer.render(var1, var3, var2);

            for (PetsModule.Spark var5 : var3.sparks) {
               double var6 = Math.clamp(1.0 - (var3.time - var5.birth) / var5.lifetime, 0.0, 1.0);
               var1.drawSprite(
                  var5.point.x,
                  var5.point.y + 0.08 * (1.0 - var6),
                  var5.point.z,
                  0.035 * var3.scale * var6 * var5.scale,
                  var3.time,
                  ColorUtil.withAlpha(var3.glow, var6 * var6 * var3.glowStrength)
               );
            }
         }
      }
   }

   public boolean showNotification(String var1, String var2, NotificationManager.Type var3, long var4) {
      if (this.isEnabled()) {
         int var6 = switch (var3) {
            case WARNING -> -996986;
            case DANGER -> -1415819;
            default -> Theme.accent;
         };
         if (this.isRuntimeActive()
            && VisualsRenderer.isRenderingEnabled()
            && !getMc().gameRenderer.gameRenderState().guiRenderState.isHudHidden
            && !(getMc().gui.screen() instanceof ClickGuiScreen)
            && this.speechBubbles.getValue()
            && this.frame != null
            && this.frame.label != null
            && System.nanoTime() - this.lastLabelVisibleNanos <= 250000000L) {
            this.pendingNotifications.removeIf(var1x -> var1x.title.equals(var1));
            if (this.currentNotification != null && this.currentNotification.title.equals(var1)) {
               this.currentNotification = null;
            }

            this.pendingNotifications.addLast(new PetsModule.Notification(var1, var2 == null ? "" : var2, var6, Math.max(0L, var4), var3));

            while (this.pendingNotifications.size() > 4) {
               this.pendingNotifications.removeFirst();
            }

            this.excitement = Math.max(this.excitement, 0.6);
            return true;
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   public void removeNotification(String var1) {
      this.pendingNotifications.removeIf(var1x -> var1x.title.equals(var1));
      if (this.currentNotification != null && this.currentNotification.title.equals(var1)) {
         this.currentNotification = null;
      }
   }

   public void drawSpeechBubble(GuiGraphicsExtractor var1) {
      if (this.isEnabled()) {
         PetsModule.Frame var2 = this.frame;
         if (this.isRuntimeActive() && this.speechBubbles.getValue() && var2 != null && var2.label != null) {
            long var3 = System.nanoTime();
            if (this.currentNotification != null && (var3 - this.notificationStart) / 1000000L > this.currentNotification.duration + 220L) {
               this.currentNotification = null;
            }

            if (this.currentNotification == null && !this.pendingNotifications.isEmpty()) {
               this.currentNotification = this.pendingNotifications.removeFirst();
               this.notificationStart = var3;
            }

            if (this.currentNotification != null) {
               double var5 = (var3 - this.notificationStart) / 1000000.0;
               double var7 = Math.clamp(var5 / 120.0, 0.0, 1.0) * (1.0 - Math.clamp((var5 - this.currentNotification.duration) / 220.0, 0.0, 1.0));
               double var9 = Math.clamp(var5 / 220.0, 0.0, 1.0);
               double var11 = 1.0 + 2.70158 * Math.pow(var9 - 1.0, 3.0) + 1.70158 * Math.pow(var9 - 1.0, 2.0);
               String var13 = FontRenderer.truncateToWidth(this.currentNotification.title, 220, FontRenderer.Face.MEDIUM);
               String var14 = FontRenderer.truncateToWidth(this.currentNotification.text, 220, FontRenderer.Face.REGULAR);
               int var15 = Math.max(90, Math.max(FontRenderer.measureRawWidth(var13, FontRenderer.Face.MEDIUM), FontRenderer.getDefaultWidth(var14)) + 24);
               int var16 = Math.clamp(Math.round(var2.label.getX() - var15 / 2.0F), 4, Math.max(4, getMc().getWindow().getGuiScaledWidth() - var15 - 4));
               int var17 = Math.max(4, Math.round(var2.label.getY()) - 42);
               var1.pose().pushMatrix();

               try {
                  var1.pose().translate(var2.label.getX(), var2.label.getY());
                  var1.pose().scale((float)var11, (float)var11);
                  var1.pose().translate(-var2.label.getX(), -var2.label.getY());
                  DrawUtil.fillRoundedRect(var1, var16, var17, var15, 34.0F, 7.0F, ColorUtil.multiplyAlpha(-185207568, var7));
                  int var18 = Math.clamp(Math.round(var2.label.getX()), var16 + 10, var16 + var15 - 10);

                  for (int var19 = 0; var19 < 6; var19++) {
                     var1.fill(var18 - 5 + var19, var17 + 34 + var19, var18 + 5 - var19, var17 + 35 + var19, ColorUtil.multiplyAlpha(-185207568, var7));
                  }

                  DrawUtil.fillRoundedRect(var1, var16 + 8, var17 + 9, 3.0F, 3.0F, 1.0F, ColorUtil.multiplyAlpha(this.currentNotification.color, var7));
                  FontRenderer.drawTextRaw(var1, var13, var16 + 16, var17 + 5, ColorUtil.multiplyAlpha(-14539990, var7), FontRenderer.Face.MEDIUM);
                  FontRenderer.drawDefaultText(var1, var14, var16 + 8, var17 + 19, ColorUtil.multiplyAlpha(-11381667, var7));
               } finally {
                  var1.pose().popMatrix();
               }
            }
         }
      }
   }

   public record Frame(
      String style,
      String finish,
      boolean realism,
      Vec3 center,
      Vec3 view,
      Quaternionf rotation,
      double scale,
      double time,
      double speed,
      double wings,
      double gazeYaw,
      double gazePitch,
      double reaction,
      int body,
      int glow,
      double glowStrength,
      Vec3 shadow,
      double shadowAlpha,
      List<PetsModule.Spark> sparks,
      ScreenProjection.ProjectedPoint label
   ) {
   }

   private record Notification(String title, String text, int color, long duration, NotificationManager.Type type) {
   }

   private final class PetSettings {
      final ModeSetting finish;
      final BooleanSetting naturalColours;
      final ColorSetting color;
      final NumberSetting glow;
      final ColorSetting glowColor;

      PetSettings(String var2, int var3, String... var4) {
         this.finish = PetsModule.this.addSetting(new ModeSetting(var2 + " finish", "Companion surface finish", var4[0], var4));
         this.naturalColours = PetsModule.this.addSetting(new BooleanSetting(var2 + " natural colours", "Use the finish palette", false));
         this.color = PetsModule.this.addSetting(new ColorSetting(var2 + " colour", "Main companion color", -1459895));
         this.glow = PetsModule.this.addSetting(new NumberSetting(var2 + " glow", "Magic glow strength in percent", var3, 0.0, 100.0, 5.0));
         this.glowColor = PetsModule.this.addSetting(new ColorSetting(var2 + " glow colour", "Companion glow color", -1459895));
         this.finish.visibleWhen(() -> PetsModule.this.pet.isMode(var2) && !PetsModule.this.realism.getValue());
         this.naturalColours.visibleWhen(() -> this.finish.isVisible());
         this.color.visibleWhen(() -> this.finish.isVisible() && !this.naturalColours.getValue());
         this.glow.visibleWhen(() -> this.finish.isVisible());
         this.glowColor.visibleWhen(() -> this.finish.isVisible() && this.glow.getValue() > 0.0);
      }

      int bodyColor() {
         int var1;
         if (this.naturalColours.getValue()) {
            var1 = switch ((String)this.finish.getValue()) {
               case "Scales" -> -10252932;
               case "Obsidian" -> -13356471;
               case "Gold" -> -2247330;
               case "Energy" -> -5341185;
               case "Crystal" -> -6496791;
               case "Wisp" -> -2037525;
               case "Silk" -> -4412714;
               default -> -5054483;
            };
         } else {
            var1 = this.color.getValue().equals(this.color.getDefaultValue()) ? Theme.accent : this.color.getValue();
         }
         return switch ((String)this.finish.getValue()) {
            case "Gold" -> ColorUtil.lerpColor(var1, -12168, 0.4);
            case "Obsidian" -> ColorUtil.lerpColor(var1, -15724259, 0.65);
            case "Crystal" -> ColorUtil.lerpColor(var1, -1639937, 0.25);
            case "Silk" -> ColorUtil.lerpColor(var1, -1122056, 0.18);
            default -> var1;
         };
      }

      int glowColorValue() {
         return this.glowColor.getValue().equals(this.glowColor.getDefaultValue()) ? Theme.accent : this.glowColor.getValue();
      }
   }

   private record Spark(Vec3 point, double birth, double lifetime, double scale) {
   }
}
