package com.larpclient.module.misc;

import com.larpclient.core.ViewController;
import com.larpclient.gui.screen.EmoteWheelScreen;
import com.larpclient.module.world.ScanPhase;
import com.larpclient.render.overlay.AlphaScope;
import com.larpclient.social.emote.EmoteAnimation;
import com.larpclient.social.emote.EmoteRegistry;
import com.larpclient.social.emote.EmoteState;
import com.larpclient.util.data.BindCodes;
import com.larpclient.util.entity.SwingHelper;
import com.larpclient.util.misc.FadingMusicSound;
import com.larpclient.util.misc.LarpLog;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import net.minecraft.client.CameraType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.client.sounds.SoundEngine.PlayResult;
import net.minecraft.world.entity.Avatar;
import net.minecraft.world.entity.player.Input;
import net.minecraft.world.phys.Vec3;

public final class EmotesModule extends Module {
   private static final int bLEND_IN_TICKS = 3;
   private static final int bLEND_OUT_TICKS = 4;
   private static EmotesModule instance;
   public final KeybindSetting wheelKey = this.addSetting(new KeybindSetting("Wheel Key", "Hold to open the emote wheel, release over an emote to play it", 66));
   private final ModeSetting[] slotSettings = new ModeSetting[]{
      this.addSetting(new EmotesModule.EmoteModeSetting("Slot 1", "Top emote on the wheel", "Maybach Bounce")),
      this.addSetting(new EmotesModule.EmoteModeSetting("Slot 2", "Right emote on the wheel", "Money Spread")),
      this.addSetting(new EmotesModule.EmoteModeSetting("Slot 3", "Bottom emote on the wheel", "Crypto Mining")),
      this.addSetting(new EmotesModule.EmoteModeSetting("Slot 4", "Left emote on the wheel", "Floss"))
   };
   public final BooleanSetting thirdPerson = this.addSetting(
      new BooleanSetting("Third Person", "Switch to third person while an emote plays, then switch back", true)
   );
   public final NumberSetting musicVolume = this.addSetting(
      new NumberSetting(
         "Music Volume",
         "Loudness of your emote songs from the emote music resource pack; 0% is silent. Players and Master sliders also apply",
         0.3,
         0.0,
         1.0,
         0.05
      ) {
         @Override
         public String getEditValueAsString() {
            return Long.toString(Math.round(this.getValue() * 100.0));
         }

         @Override
         public String getValueAsString() {
            return this.getEditValueAsString() + "%";
         }

         @Override
         public void setFromEditString(String var1) {
            this.getDisplayValue(Double.parseDouble(var1.trim().replace("%", "")) / 100.0);
         }
      }
   );
   private EmoteAnimation animation;
   private EmoteRegistry.Emote emote;
   private String emoteLabel = "";
   private int tickCount;
   private int blendOutTicks;
   private boolean stopping;
   private boolean wheelKeyWasDown;
   private boolean cameraOverridden;
   private CameraType previousCamera = CameraType.FIRST_PERSON;
   private CameraType emoteCamera = CameraType.THIRD_PERSON_BACK;
   private final float[] sampleBuffer = new float[EmoteAnimation.totalChannels];
   private float bodyYaw;
   private int lastHurtTime;
   private Object player;
   private Object level;
   private FadingMusicSound musicSound;
   public static int counter;
   private static final String[] lOG_STRINGS;

   public EmotesModule() {
      super("Emotes", "Hold a key to pick an emote from a wheel, like Fortnite", Category.RENDER, -1);
      instance = this;
   }

   public static EmotesModule getInstance() {
      return instance;
   }

   @Override
   public Set<ScanPhase> getNetworkImpacts() {
      return Set.of(ScanPhase.iDLE);
   }

   public List<String> getSlotLabels() {
      return Arrays.stream(this.slotSettings).map(Setting::getValue).toList();
   }

   public int getWheelKey() {
      return this.wheelKey.getValue();
   }

   public boolean isPlaying() {
      return !this.isEnabled() ? false : this.animation != null && !this.stopping;
   }

   public String getStopReason() {
      return this.isPlaying() ? this.emoteLabel : "";
   }

   @Override
   public void onEnable() {
      this.wheelKeyWasDown = this.isWheelKeyDown();
      LarpLog.debug("Emotes", "Ready; wheel key={}, slots={}", this.wheelKey.getValue(), this.getSlotLabels());
   }

   @Override
   public void onDisable() {
      if (getMc().gui.screen() instanceof EmoteWheelScreen var1) {
         var1.onClose();
      }

      this.endEmote("module disabled");
   }

   @Override
   public void onTick() {
      Minecraft var1 = getMc();
      byte var2 = this.isWheelKeyDown();
      boolean var4 = false;
      byte var3 = var2;
      int var10000 = Integer.signum(var3);
      if (((var10000 | -var10000) >>> 31 ^ 1) == 0) {
         boolean var6 = false;
         byte var5 = this.wheelKeyWasDown;
         var10000 = Integer.signum(var5);
         if ((var10000 | -var10000) >>> 31 == 0 && var1.gui.screen() == null && var1.gui.overlay() == null) {
            int var12 = var1.isWindowActive();
            boolean var8 = false;
            byte var7 = (byte)var12;
            var12 = Integer.signum(var7);
            if (((var12 | -var12) >>> 31 ^ 1) == 0 && var1.player != null) {
               int var14 = var1.player.isAlive();
               boolean var10 = false;
               byte var9 = (byte)var14;
               var14 = Integer.signum(var9);
               if (((var14 | -var14) >>> 31 ^ 1) == 0) {
                  var1.gui.setScreen(new EmoteWheelScreen(this));
                  LarpLog.debug("Emotes", "Wheel opened");
               }
            }
         }
      }

      this.wheelKeyWasDown = (boolean)var2;
      this.tickEmote();
   }

   private boolean isWheelKeyDown() {
      Minecraft var1 = getMc();
      return var1.getWindow() != null && BindCodes.isPressed(var1.getWindow().handle(), this.wheelKey.getValue());
   }

   public void playEmote(int var1) {
      if (this.isEnabled()) {
         if (var1 >= 0 && var1 < this.slotSettings.length && this.isRuntimeActive() && this.isInGame()) {
            EmoteRegistry.Emote var2 = EmoteRegistry.findByLabel(this.slotSettings[var1].getValue());
            if (var2 != null) {
               EmoteAnimation var3 = EmoteRegistry.load(var2);
               if (var3 != null) {
                  Minecraft var4 = getMc();
                  LocalPlayer var5 = var4.player;
                  this.animation = var3;
                  this.emote = var2;
                  this.emoteLabel = var2.label();
                  this.tickCount = this.blendOutTicks = 0;
                  this.stopping = (boolean)0;
                  this.player = var5;
                  this.level = var4.level;
                  this.lastHurtTime = var5.hurtTime;
                  this.bodyYaw = var5.getYRot();
                  this.startMusic(var2);
                  this.applyCamera(var2);
                  String[] var6 = lOG_STRINGS;
                  String var10000 = var6[1];
                  String var10001 = var6[0];
                  Object[] var10002 = new Object[]{this.emoteLabel, (var1 | 1) + (var1 & 1), null, null};
                  Object var10005;
                  if (this.cameraOverridden) {
                     var10005 = this.emoteCamera;
                  } else {
                     var6 = lOG_STRINGS;
                     var10005 = var6[3];
                  }

                  var10002[2] = var10005;
                  var10002[3] = var2.prop() == null ? lOG_STRINGS[2] : var2.prop();
                  LarpLog.info(var10000, var10001, var10002);
               }
            }
         }
      }
   }

   private void applyCamera(EmoteRegistry.Emote var1) {
      Options var2 = getMc().options;
      CameraType var3 = var2.getCameraType();
      CameraType var4 = var1.prop() != null ? CameraType.THIRD_PERSON_FRONT : CameraType.THIRD_PERSON_BACK;
      int var5 = this.cameraOverridden && var3 == this.emoteCamera ? 1 : 0;
      int var6 = var3.isFirstPerson() || var5 != 0 || var1.prop() != null && var3 == CameraType.THIRD_PERSON_BACK ? 1 : 0;
      if (this.thirdPerson.getValue() && ViewController.update() == ViewController.Owner.NONE && var6 != 0 && var3 != var4) {
         if (var5 == 0) {
            this.previousCamera = var3;
         }

         var2.setCameraType(var4);
         this.cameraOverridden = (boolean)1;
         this.emoteCamera = var4;
      }
   }

   public void stopEmote(String var1) {
      if (this.animation != null && !this.stopping) {
         this.stopping = (boolean)1;
         this.blendOutTicks = 0;
         if (this.musicSound != null) {
            this.musicSound.startFadeOut();
         }

         this.restoreCamera();
         LarpLog.info("Emotes", "Stopped {} after {} ticks: {}", this.emoteLabel, this.tickCount, var1);
      }
   }

   private void endEmote(String var1) {
      if (this.animation != null && !this.stopping) {
         LarpLog.info("Emotes", "Stopped {} after {} ticks: {}", this.emoteLabel, this.tickCount, var1);
      }

      this.restoreCamera();
      this.stopMusic();
      this.animation = null;
      this.emote = null;
      this.emoteLabel = "";
      this.stopping = (boolean)0;
      this.player = this.level = null;
   }

   private void startMusic(EmoteRegistry.Emote var1) {
      this.stopMusic();
      if (!(this.musicVolume.getValue() <= 0.0)) {
         if (getMc().getSoundManager().getSoundEvent(var1.music()) == null) {
            LarpLog.debug("Emotes", "No music for {}: add {} with the emote music resource pack", var1.label(), var1.music());
         } else {
            FadingMusicSound var2 = new FadingMusicSound(var1, this.musicVolume::getValue, 4);

            try {
               PlayResult var3 = getMc().getSoundManager().play(var2);
               this.musicSound = var2;
               LarpLog.debug("Emotes", "Music {} started at {}% volume: {}", var1.music(), Math.round(this.musicVolume.getValue() * 100.0), var3);
            } catch (RuntimeException var4) {
               LarpLog.errorOnce("Emotes", "Emote music failed to start; the dance plays silently, reload resources to retry", var4);
            }
         }
      }
   }

   private void stopMusic() {
      if (this.musicSound != null) {
         FadingMusicSound var1 = this.musicSound;
         this.musicSound = null;

         try {
            getMc().getSoundManager().stop(var1);
         } catch (RuntimeException var3) {
            LarpLog.errorOnce("Emotes", "Emote music stop failed; it ends with the sound engine", var3);
         }
      }
   }

   private void restoreCamera() {
      Options var1 = getMc().options;
      if (this.cameraOverridden && var1.getCameraType() == this.emoteCamera) {
         var1.setCameraType(this.previousCamera);
      }

      this.cameraOverridden = (boolean)0;
   }

   private void tickEmote() {
      if (this.animation != null) {
         Minecraft var1 = getMc();
         LocalPlayer var2 = var1.player;
         if (var2 == null || var2 != this.player || var1.level != this.level || !var2.isAlive()) {
            this.endEmote("world or player changed");
         } else if (this.stopping) {
            if (++this.blendOutTicks >= 4) {
               this.endEmote("blend finished");
            }
         } else {
            this.tickCount++;
            String var3 = this.getStopReason(var2);
            if (var3 != null) {
               this.stopEmote(var3);
            } else if (this.animation.sample(this.tickCount)) {
               this.stopEmote("emote finished");
            }
         }
      }
   }

   private String getStopReason(LocalPlayer var1) {
      Input var2 = var1.input.keyPresses;
      if (var2.forward() || var2.backward() || var2.left() || var2.right()) {
         return "movement input";
      } else if (var2.jump()) {
         return "jump";
      } else if (var2.shift()) {
         return "sneak";
      } else {
         int var3 = var1.hurtTime;
         int var4 = var3 > this.lastHurtTime ? 1 : 0;
         this.lastHurtTime = var3;
         if (var4 != 0) {
            return "took damage";
         } else if (SwingHelper.isSwinging(var1) || var1.isUsingItem()) {
            return "attack or item use";
         } else {
            return !var1.isPassenger() && !var1.isFallFlying() && !var1.isSwimming() && !var1.isSleeping() && !var1.getAbilities().flying
               ? null
               : "pose changed";
         }
      }
   }

   public static void applyToRenderState(Avatar var0, AvatarRenderState var1, float var2) {
      if (instance != null && instance.isEnabled()) {
         EmoteState var3 = (EmoteState)var1;
         EmotesModule var4 = instance;
         if (var4 != null && var4.animation != null && var4.isRuntimeActive() && var0 == getMc().player) {
            float var5 = var4.getBlendWeight(var2);
            var3.larp$setEmoteWeight(var5);
            if (!(var5 <= 0.0F)) {
               int var6 = var4.tickCount;
               float var7 = var4.stopping ? 0.0F : var2;
               var4.animation.sample(var6, var7, var3.larp$emoteSample());
               var3.larp$setEmoteProp(var4.emote.prop(), var4.animation.loopProgress(var6, var7));
               Screen var8 = getMc().gui.screen();
               if (var8 == null || var8 instanceof ChatScreen || var8 instanceof EmoteWheelScreen) {
                  var1.bodyRot = var4.bodyYaw;
                  var1.yRot = 0.0F;
                  var1.xRot = 0.0F;
               }
            }
         } else {
            var3.larp$setEmoteWeight(0.0F);
            var3.larp$setEmoteProp(null, 0.0F);
         }
      }
   }

   private float getBlendWeight(float var1) {
      float var2 = Math.min(1.0F, (this.tickCount + (this.stopping ? 0.0F : var1)) / 3.0F);
      float var3 = this.stopping ? 1.0F - Math.min(1.0F, (this.blendOutTicks + var1) / 4.0F) : 1.0F;
      float var4 = Math.min(var2, var3);
      return var4 * var4 * (3.0F - 2.0F * var4);
   }

   public static EmotesModule.EmoteRenderState getPropState(float var0) {
      EmotesModule var1 = instance;
      if (var1 != null && var1.animation != null && var1.emote != null && var1.emote.prop() != null && !var1.emote.handProp() && var1.isRuntimeActive()) {
         LocalPlayer var2 = getMc().player;
         if (var2 != null && var2 == var1.player) {
            var1.animation.sample(var1.tickCount, var1.stopping ? 0.0F : var0, var1.sampleBuffer);
            int var3 = EmoteAnimation.Part.PROP.ordinal() * 10;
            float[] var4 = new float[7];

            for (int var5 = 0; var5 < 7; var5++) {
               float var6 = var1.sampleBuffer[var3 + (var5 == 6 ? 7 : var5)];
               var4[var5] = Float.isNaN(var6) ? (var5 == 6 ? 1 : 0) : var6;
            }

            float var7 = var1.stopping ? Math.min(1.0F, (var1.blendOutTicks + var0) / 4.0F) : 0.0F;
            float var8 = 1.0F - var7 * var7 * (3.0F - 2.0F * var7);
            return new EmotesModule.EmoteRenderState(
               var1.emote.prop(),
               var2.getPosition(var0),
               var1.bodyYaw,
               var2.getScale(),
               var4,
               var8,
               var1.tickCount + (var1.stopping ? 0.0F : var0),
               var1.stopping ? var1.blendOutTicks + var0 : 0.0F,
               var1.stopping
            );
         } else {
            return null;
         }
      } else {
         return null;
      }
   }

   public static boolean getWheelKey(AvatarRenderState var0) {
      return instance != null && instance.isEnabled() ? var0 instanceof EmoteState var1 && var1.larp$emoteWeight() > 0.0F : false;
   }

   @Override
   public String getStatus() {
      if (!this.isRuntimeActive()) {
         return "Idle";
      } else {
         int var1 = this.wheelKey.getValue();
         if (this.isPlaying()) {
            return this.emoteLabel;
         } else {
            return var1 < 0 ? "Wheel key unbound" : "Hold " + BindCodes.displayName(var1);
         }
      }
   }

   private static final class EmoteModeSetting extends ModeSetting {
      EmoteModeSetting(String var1, String var2, String var3) {
         super(var1, var2, var3, EmoteRegistry.getLabels());
      }

      @Override
      public int previewSize() {
         return 44;
      }

      @Override
      public void drawPreview(GuiGraphicsExtractor var1, String var2, int var3, int var4, int var5) {
         EmoteRegistry.Emote var6 = EmoteRegistry.findByLabel(var2);
         if (var6 != null) {
            short var7 = 192;
            var1.blit(RenderPipelines.GUI_TEXTURED, var6.icon(), var3, var4, 0.0F, 0.0F, var5, var5, var7, var7, var7, var7, AlphaScope.applyAlpha(-1));
         }
      }
   }

   public record EmoteRenderState(
      String prop, Vec3 feet, float facing, float avatarScale, float[] track, float fade, float age, float stopAge, boolean stopping
   ) {
   }
}
