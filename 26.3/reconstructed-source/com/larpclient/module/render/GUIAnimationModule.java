package com.larpclient.module.render;

import com.larpclient.core.ViewController;
import com.larpclient.module.misc.BooleanSetting;
import com.larpclient.module.misc.Category;
import com.larpclient.module.misc.ModeSetting;
import com.larpclient.module.misc.Module;
import com.larpclient.module.misc.NumberSetting;
import com.larpclient.module.misc.Setting;
import com.larpclient.util.misc.LarpLog;
import com.larpclient.util.render.Animation;
import java.math.BigDecimal;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.WeakHashMap;
import net.minecraft.client.CameraType;
import net.minecraft.world.inventory.Slot;

public final class GUIAnimationModule extends Module {
   private static GUIAnimationModule instance;
   private final BooleanSetting playerListToggle = this.addToggle("Player List", "Slide the player list in and out", true);
   private final BooleanSetting inventoryToggle = this.addToggle("Inventory", "Animate container entrances with aligned input", true);
   private final BooleanSetting cameraZoomToggle = this.addToggle("Camera Zoom", "Smooth changes in camera field of view", false);
   public final BooleanSetting nativeChunks = this.addSetting(
      new BooleanSetting("Chunk Update", "Keep vanilla chunk appearance; turn off to animate newly visible chunks", true) {
         @Override
         public String getDisplayName() {
            return "Native Chunks";
         }
      }
   );
   private final BooleanSetting perspectiveToggle = this.addToggle("Perspective Change", "Ease camera distance when switching perspective", false);
   private final BooleanSetting chatToggle = this.addToggle("Chat Messages", "Ease received chat, typed chat and client search text into view", true);
   private final BooleanSetting itemsToggle = this.addToggle("Items", "Animate hovered inventory items", true);
   private final ModeSetting chunkCurve = this.addSetting(
      new ModeSetting("Chunk Animation", "Curve for chunk appearance", "Quart", "Quart", "Circ", "Sine", "Cubic")
   );
   private final NumberSetting duration = this.addSetting(
      new NumberSetting("Speed", "Inventory and chunk entrance duration in tenths of a second", 6.0, 2.0, 10.0, 1.0) {
         @Override
         public String getDisplayName() {
            return "Duration";
         }

         @Override
         public String getValueAsString() {
            return String.format(Locale.ROOT, "%.1f s", this.getValue() / 10.0);
         }

         @Override
         public String getEditValueAsString() {
            return BigDecimal.valueOf(this.getValue() / 10.0).stripTrailingZeros().toPlainString();
         }

         @Override
         public void setFromEditString(String var1) {
            super.setFromEditString(Double.toString(Double.parseDouble(var1.strip().replace("s", "")) * 10.0));
         }
      }
   );
   private final Animation playerListAnimation = new Animation(0.0);
   private final Animation perspectiveAnimation = new Animation(0.0);
   private final Map<Slot, GUIAnimationModule.SlotScale> slotScales = new WeakHashMap<>();
   private Object lastCameraType;
   private float smoothedFov = Float.NaN;
   private long lastFovTime;

   public GUIAnimationModule() {
      super("GUI Animation", "Animate menus, chat, camera changes and chunks", Category.RENDER, -1);
      instance = this;
      this.chunkCurve.visibleWhen(() -> !this.nativeChunks.getValue());
      this.duration.visibleWhen(() -> !this.nativeChunks.getValue() || this.inventoryToggle.getValue());
   }

   private BooleanSetting addToggle(String var1, String var2, boolean var3) {
      return this.addSetting(new BooleanSetting(var1, var2, var3));
   }

   public static GUIAnimationModule getInstance() {
      return instance;
   }

   public List<Setting<?>> getInventorySettings() {
      return List.of(this.playerListToggle, this.inventoryToggle, this.chatToggle, this.itemsToggle);
   }

   public List<Setting<?>> addToggle() {
      return List.of(this.cameraZoomToggle, this.perspectiveToggle);
   }

   public List<Setting<?>> getChunkSettings() {
      return List.of(this.nativeChunks, this.chunkCurve, this.duration);
   }

   public boolean isInventoryAnimationActive() {
      return !this.isEnabled() ? false : this.isRuntimeActive() && this.inventoryToggle.getValue();
   }

   public boolean isItemAnimationActive() {
      return !this.isEnabled() ? false : this.isRuntimeActive() && this.itemsToggle.getValue();
   }

   public boolean isChatAnimationActive() {
      return !this.isEnabled() ? false : this.isRuntimeActive() && this.chatToggle.getValue();
   }

   public boolean getInstance() {
      return !this.isEnabled() ? false : this.isRuntimeActive() && !this.nativeChunks.getValue();
   }

   public boolean getPerspectiveProgress() {
      if (!this.isEnabled()) {
         return false;
      }

      SmoothCameraModule var1 = SmoothCameraModule.getInstance();
      return this.isRuntimeActive()
         && this.perspectiveToggle.getValue()
         && ViewController.update() == ViewController.Owner.NONE
         && (var1 == null || !var1.isRuntimeActive());
   }

   public float getPlayerListProgress() {
      if (this.isRuntimeActive() && this.playerListToggle.getValue()) {
         boolean var1 = getMc().options.keyPlayerList.isDown();
         return (float)Math.clamp(this.playerListAnimation.animateTo(var1 ? 1.0 : 0.0, var1 ? 240 : 190), 0.0, 1.0);
      } else {
         return getMc().options.keyPlayerList.isDown() ? 1.0F : 0.0F;
      }
   }

   public boolean isPlayerListAnimationActive() {
      return !this.isEnabled() ? false : this.isRuntimeActive() && this.playerListToggle.getValue();
   }

   public long getDurationMillis() {
      return Math.round(this.duration.getValue() * 100.0);
   }

   public float getInventoryScale(long var1) {
      if (this.isEnabled()) {
         if (!this.isInventoryAnimationActive()) {
            return 1.0F;
         }

         double var3 = Math.clamp((System.nanoTime() - var1) / (this.getDurationMillis() * 1000000.0), 0.0, 1.0);
         return (float)(0.86 + 0.14 * Animation.Easing.EASE_OUT_BACK.apply(var3));
      } else {
         return 1.0F;
      }
   }

   public double easeToward(double var1, double var3, float var5) {
      return var3 + (var1 - var3) / Math.max(0.01F, var5);
   }

   public float getSlotScale(Slot var1, boolean var2) {
      if (this.isEnabled()) {
         long var3 = System.nanoTime();
         GUIAnimationModule.SlotScale var5 = this.slotScales.get(var1);
         float var6 = var5 == null ? 1.0F : var5.scale;
         double var7 = var5 == null ? 0.0 : Math.clamp((var3 - var5.time) * 1.0E-9, 0.0, 0.1);
         float var9 = (float)(var6 + ((var2 ? 1.12 : 1.0) - var6) * (1.0 - Math.exp(-(var2 ? 6.5 : 5.0) * var7)));
         this.slotScales.put(var1, new GUIAnimationModule.SlotScale(var9, var3));
         return var9;
      } else {
         return 1.0F;
      }
   }

   public double applyChunkEasing(double var1) {
      var1 = Math.clamp(var1, 0.0, 1.0);

      return switch ((String)this.chunkCurve.getValue()) {
         case "Circ" -> Math.sqrt(1.0 - (var1 - 1.0) * (var1 - 1.0));
         case "Sine" -> Math.sin(var1 * Math.PI / 2.0);
         case "Cubic" -> 1.0 - Math.pow(1.0 - var1, 3.0);
         default -> 1.0 - Math.pow(1.0 - var1, 4.0);
      };
   }

   public float getPerspectiveProgress(boolean var1) {
      CameraType var2 = getMc().options.getCameraType();
      if (!this.getPerspectiveProgress()) {
         this.lastCameraType = var2;
         this.perspectiveAnimation.snapTo(var1 ? 1.0 : 0.0);
         return var1 ? 1.0F : 0.0F;
      }

      if (this.lastCameraType == null) {
         this.perspectiveAnimation.snapTo(var1 ? 1.0 : 0.0);
      } else if (this.lastCameraType != var2 && var1 && this.lastCameraType != CameraType.FIRST_PERSON) {
         this.perspectiveAnimation.snapTo(0.0);
      }

      this.lastCameraType = var2;
      return (float)this.perspectiveAnimation.animateTo(var1 ? 1.0 : 0.0, 320);
   }

   public float smoothFov(float var1) {
      if (this.isEnabled()) {
         if (this.isRuntimeActive() && this.cameraZoomToggle.getValue() && ViewController.update() == ViewController.Owner.NONE) {
            long var2 = System.nanoTime();
            if (Float.isNaN(this.smoothedFov)) {
               this.smoothedFov = var1;
            } else {
               this.smoothedFov = this.smoothedFov
                  + (var1 - this.smoothedFov) * (float)(1.0 - Math.exp(-12.0 * Math.clamp((var2 - this.lastFovTime) * 1.0E-9, 0.0, 0.1)));
            }

            this.lastFovTime = var2;
            return this.smoothedFov;
         } else {
            this.smoothedFov = Float.NaN;
            return var1;
         }
      } else {
         return var1;
      }
   }

   @Override
   public void onEnable() {
      LarpLog.debug("GUI", "GUI Animation active: chat and typed input={}, native editing remains immediate", this.chatToggle.getValue());
   }

   @Override
   public void onDisable() {
      LarpLog.debug("GUI", "GUI Animation inactive; text motion settles on the next extraction");
      this.playerListAnimation.snapTo(0.0);
      this.perspectiveAnimation.snapTo(0.0);
      this.slotScales.clear();
      this.lastCameraType = null;
      this.smoothedFov = Float.NaN;
      this.lastFovTime = 0L;
   }

   private record SlotScale(float scale, long time) {
   }
}
