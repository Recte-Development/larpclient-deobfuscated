package com.larpclient.module.render;

import com.larpclient.core.ViewController;
import com.larpclient.mixin.ClientPacketListenerMixin;
import com.larpclient.module.misc.BooleanSetting;
import com.larpclient.module.misc.Category;
import com.larpclient.module.misc.ModeSetting;
import com.larpclient.module.misc.Module;
import com.larpclient.module.misc.NumberSetting;
import com.larpclient.module.movement.FreecamInput;
import com.larpclient.module.player.ScaffoldModule;
import com.larpclient.module.world.ScanPhase;
import com.larpclient.util.data.BindCodes;
import com.larpclient.util.entity.MovementCorrection;
import com.larpclient.util.misc.LarpLog;
import java.util.Locale;
import java.util.Set;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.client.multiplayer.ClientChunkCache;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.player.Input;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.phys.Vec2;

public class FreecamModule extends Module {
   private final NumberSetting speed = this.addSetting(new NumberSetting("Speed", "Detached camera speed", 1.0, 0.1, 50.0, 0.1));
   private final NumberSetting verticalSpeed = this.addSetting(new NumberSetting("Vertical", "Vertical speed multiplier", 1.0, 0.2, 5.0, 0.05) {
      @Override
      public String getValueAsString() {
         return String.format(Locale.ROOT, "%.2f", this.getValue());
      }
   });
   private final BooleanSetting smooth = this.addSetting(new BooleanSetting("Smooth", "Use smooth half-speed camera travel", true));
   private final BooleanSetting showBody = this.addSetting(new BooleanSetting("Body", "Show your body from the detached camera", true));
   private final BooleanSetting showHands = this.addSetting(new BooleanSetting("Hands", "Show first-person hands while detached", true));
   private final BooleanSetting keysMoveBody = this.addSetting(new BooleanSetting("Keys Move Body", "Also steer your body with movement keys", false));
   private final BooleanSetting keepWalking = this.addSetting(new BooleanSetting("Keep Walking", "Carry movement held when Freecam starts", true));
   private final BooleanSetting scrollSpeed = this.addSetting(new BooleanSetting("Scroll Speed", "Adjust camera speed with the mouse wheel", true));
   private final BooleanSetting resetScrollSpeed = this.addSetting(
      new BooleanSetting("Reset Scroll Speed", "Keep wheel changes temporary until Freecam ends", true)
   );
   private final NumberSetting chunks = this.addSetting(
      new NumberSetting("Chunks", "Client chunk-view radius, within server-provided terrain", 12.0, 2.0, 32.0, 1.0)
   );
   private final ModeSetting activation = this.addSetting(
      new ModeSetting("Activation", "Toggle the module key or keep it held to use Freecam", "Toggle", "Toggle", "Hold")
   );
   private final FreecamInput freecamInput = new FreecamInput();
   private static FreecamModule instance;
   private LocalPlayer player;
   private ClientLevel level;
   private FreecamInput.ChunkWindow chunkWindow;
   private boolean flyingOverridden;
   private boolean wasFlying;
   private boolean holdActive;
   private double guidedSpeed;

   @Override
   public Module.TickPhase getTickPhase() {
      return Module.TickPhase.EARLY;
   }

   @Override
   public Set<ScanPhase> getNetworkImpacts() {
      return Set.of(ScanPhase.sCANNING);
   }

   public FreecamModule() {
      super("Freecam", "Explore with a detached camera", Category.PLAYER, -1);
      instance = this;
      this.resetScrollSpeed.visibleWhen(this.scrollSpeed::getValue);
      this.speed.onChange(var1 -> this.freecamInput.resetSpeed());
      this.keepWalking.onChange(var1 -> {
         if (!var1) {
            this.freecamInput.clearInput();
         }
      });
      this.activation.onChange(var1 -> {
         if (this.isHoldMode() || this.holdActive) {
            this.setEnabled(false);
         }
      });
   }

   public boolean isHoldMode() {
      return this.activation.isMode("Hold");
   }

   public void setGuidedSpeed(double var1) {
      this.guidedSpeed = Math.clamp(var1, 1.0, 40.0);
      LarpLog.info("Freecam", "Guided camera speed {} blocks/s; Speed setting stays {}", this.guidedSpeed, this.speed.getValue());
   }

   public void clearGuidedSpeed() {
      this.guidedSpeed = 0.0;
   }

   public double getGuidedSpeed() {
      return this.guidedSpeed;
   }

   private boolean setGuidedSpeed() {
      return BindCodes.isPressed(getMc().getWindow().handle(), this.getKeyBind());
   }

   public boolean getGuidedSpeed() {
      if (this.isEnabled()) {
         if (this.isHoldMode() && this.setGuidedSpeed()) {
            this.holdActive = (boolean)1;
            this.enable();
            return this.isEnabled();
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   @Override
   public void enable() {
      if (!this.isHoldMode() || this.holdActive) {
         super.enable();
      }
   }

   @Override
   public void disable() {
      this.holdActive = (boolean)0;
      super.disable();
   }

   @Override
   public void suspendForDisconnect() {
      if (this.isHoldMode()) {
         this.disable();
      } else {
         super.suspendForDisconnect();
      }
   }

   public static void onScroll() {
      FreecamModule var0 = instance;
      if (var0 != null && var0.isHoldMode() && var0.isEnabled()) {
         Minecraft var1 = getMc();
         if (var1 == null
            || var1.player == null
            || var1.level == null
            || !var1.player.isAlive()
            || var1.gui.screen() != null
            || var1.gui.overlay() != null
            || !var1.isWindowActive()
            || var0.player != null && (var0.player != var1.player || var0.level != var1.level)
            || !var0.setGuidedSpeed()) {
            var0.setEnabled(false);
         }
      }
   }

   @Override
   public void setKeyBind(int var1) {
      if (this.isHoldMode() && var1 != this.getKeyBind()) {
         this.setEnabled(false);
      }

      super.setKeyBind(var1);
   }

   @Override
   public void onEnable() {
      Minecraft var1 = getMc();
      this.player = var1.player;
      this.level = var1.level;
      boolean var2 = MovementCorrection.isStrictMovementActive();
      ViewController.acquire(ViewController.Owner.FREECAM);
      this.freecamInput
         .begin(
            var2 ? readKeyboardInput() : FreecamInput.mergeInputs(readKeyboardInput(), this.player.input.keyPresses),
            this.isSneaking(),
            this.keepWalking.getValue()
         );
      this.updateInput(true, var2);
      this.suppressFlying();
      this.updateChunkView();
      LarpLog.info(
         "Freecam",
         "Detached camera acquired; activation={}, speed={}, smooth={}, carry={}, body keys={}",
         this.activation.getValue(),
         this.speed.getValue(),
         this.smooth.getValue(),
         this.keepWalking.getValue(),
         this.keysMoveBody.getValue()
      );
      LarpLog.debug("Freecam", "Terrain visibility starts from loaded columns in view, including across unloaded camera chunks");
   }

   @Override
   public void onDisable() {
      Minecraft var1 = getMc();

      try {
         if (this.level != null && this.player != null) {
            ChunkPos var2 = this.player.chunkPosition();
            this.level.getChunkSource().updateViewCenter(var2.x(), var2.z());
            this.level.getChunkSource().updateViewRadius(this.getServerChunkRadius());
         }

         if (this.player == var1.player && this.level == var1.level && this.player != null && this.player.isAlive()) {
            if (this.flyingOverridden && this.player.getAbilities().mayfly && !this.player.getAbilities().flying) {
               this.player.getAbilities().flying = this.wasFlying;
            }

            this.player.input.keyPresses = Input.EMPTY;
         }
      } finally {
         this.holdActive = (boolean)0;
         this.guidedSpeed = 0.0;
         this.flyingOverridden = (boolean)0;
         this.player = null;
         this.level = null;
         this.chunkWindow = null;
         this.freecamInput.reset();
         ViewController.release(ViewController.Owner.FREECAM);
         if (var1.level != null) {
            var1.levelRenderer.sectionOcclusionGraph().invalidate();
         }

         LarpLog.info("Freecam", "Released camera, body input and chunk view");
      }
   }

   @Override
   public void onTick() {
      onScroll();
      if (this.isRuntimeActive()) {
         ViewController.acquire(ViewController.Owner.FREECAM);
         this.updateInput(false, false);
         this.suppressFlying();
         double var3;
         int var10000 = (var3 = this.guidedSpeed - 0.0) == 0.0 ? 0 : (var3 < 0.0 ? -1 : 1);
         boolean var2 = false;
         int var1 = var10000;
         if ((-Integer.signum(var1) >>> 31 ^ 1) == 0) {
            ViewController.isFreecam(this.guidedSpeed / 20.0, 1.0, false);
         } else {
            ViewController.isFreecam(
               this.freecamInput.scaledSpeed(this.speed.getValue(), this.resetScrollSpeed.getValue()), this.verticalSpeed.getValue(), this.smooth.getValue()
            );
         }

         this.updateChunkView();
      }
   }

   public static boolean isFreecamActive() {
      return instance != null && instance.isEnabled() ? ViewController.isFreecam() : false;
   }

   public static boolean shouldShowBody() {
      return instance != null && instance.isEnabled() ? instance != null && instance.showBody.getValue() : false;
   }

   public static boolean shouldShowHands() {
      return instance != null && instance.isEnabled() ? instance != null && instance.showHands.getValue() : false;
   }

   public static Input setGuidedSpeed(Input var0) {
      if (instance != null && instance.isEnabled()) {
         FreecamModule var1 = instance;
         Minecraft var2 = Minecraft.getInstance();
         return var1 != null && var2.gui.screen() == null && var2.isWindowActive()
            ? var1.freecamInput.consumeInput(var0, var1.keysMoveBody.getValue())
            : Input.EMPTY;
      } else {
         return var0;
      }
   }

   public static Input readKeyboardInput() {
      Options var0 = Minecraft.getInstance().options;
      return new Input(
         var0.keyUp.isDown(),
         var0.keyDown.isDown(),
         var0.keyLeft.isDown(),
         var0.keyRight.isDown(),
         var0.keyJump.isDown(),
         var0.keyShift.isDown(),
         var0.keySprint.isDown()
      );
   }

   public static Input getControlInput() {
      FreecamModule var0 = instance;
      Minecraft var1 = Minecraft.getInstance();
      return var0 != null && var1.gui.screen() == null && var1.isWindowActive()
         ? var0.freecamInput.applyInput(readKeyboardInput(), var0.keysMoveBody.getValue())
         : Input.EMPTY;
   }

   private boolean isSneaking() {
      return this.player.isShiftKeyDown() && !ScaffoldModule.isBridging(this.player);
   }

   private void updateInput(boolean var1, boolean var2) {
      if (this.player != null) {
         float var3 = ScaffoldModule.getWalkYaw();
         int var4 = !var2 && Float.isNaN(var3) ? 0 : 1;
         this.freecamInput
            .update(
               var4 != 0 ? readKeyboardInput() : FreecamInput.mergeInputs(readKeyboardInput(), this.player.input.keyPresses),
               this.keepWalking.getValue(),
               this.keysMoveBody.getValue(),
               this.isSneaking(),
               var4 != 0 ? Vec2.ZERO : new Vec2(this.player.xxa, this.player.zza),
               this.player.getDeltaMovement(),
               Float.isNaN(var3) ? this.player.getYRot() : var3,
               var1
            );
      }
   }

   private void suppressFlying() {
      if (this.player != null && this.player.getAbilities().mayfly) {
         if (!this.flyingOverridden) {
            this.wasFlying = this.player.getAbilities().flying;
            this.flyingOverridden = (boolean)1;
         }

         this.player.getAbilities().flying = (boolean)0;
      }
   }

   public static void setGuidedSpeed() {
      if (instance != null) {
         instance.flyingOverridden = (boolean)0;
      }
   }

   private void updateChunkView() {
      if (this.level != null && this.player != null) {
         ChunkPos var1 = this.player.chunkPosition();
         FreecamInput.ChunkWindow var2 = FreecamInput.applyInput(
            ViewController.getPosition(1.0F), var1.x(), var1.z(), this.chunks.getValue().intValue(), this.getServerChunkRadius()
         );
         ClientChunkCache var3 = this.level.getChunkSource();
         var3.updateViewCenter(var2.centerX(), var2.centerZ());
         var3.updateViewRadius(var2.radius());
         if (!var2.equals(this.chunkWindow)) {
            getMc().levelRenderer.sectionOcclusionGraph().invalidate();
            this.chunkWindow = var2;
         }
      }
   }

   private int getServerChunkRadius() {
      return this.player == null ? getMc().options.getEffectiveRenderDistance() : ((ClientPacketListenerMixin)this.player.connection).larpServerChunkRadius();
   }

   public static boolean onScroll(double var0) {
      if (instance != null && instance.isEnabled()) {
         FreecamModule var2 = instance;
         Minecraft var3 = Minecraft.getInstance();
         if (var2 == null
            || !ViewController.isFreecam()
            || !var2.scrollSpeed.getValue() && var2.guidedSpeed <= 0.0
            || var3.gui.screen() != null
            || var3.gui.overlay() != null
            || !var3.isWindowActive()) {
            return false;
         }

         if (var2.guidedSpeed > 0.0) {
            if (Double.isFinite(var0) && var0 != 0.0) {
               var2.guidedSpeed = FreecamInput.adjustSpeed(var2.guidedSpeed, var0);
               LarpLog.debug("Freecam", "Wheel guided camera speed={} blocks/s", var2.guidedSpeed);
               return true;
            } else {
               return false;
            }
         } else {
            if (!var2.freecamInput.scrollSpeed(var0, var2.speed.getValue(), var2.resetScrollSpeed.getValue())) {
               return false;
            }

            if (!var2.resetScrollSpeed.getValue()) {
               var2.speed.getDisplayValue(var2.speed.getValue() * (var0 > 0.0 ? 1.2 : 0.8333333333333334));
            }

            LarpLog.debug("Freecam", "Wheel camera speed={}", var2.freecamInput.scaledSpeed(var2.speed.getValue(), var2.resetScrollSpeed.getValue()));
            return true;
         }
      } else {
         return false;
      }
   }
}
