package com.larpclient.module.render;

import com.larpclient.LarpClient;
import com.larpclient.module.misc.BooleanSetting;
import com.larpclient.module.misc.Category;
import com.larpclient.module.misc.KeybindSetting;
import com.larpclient.module.misc.ModeSetting;
import com.larpclient.module.misc.Module;
import com.larpclient.module.misc.NumberSetting;
import com.larpclient.module.misc.Setting;
import com.larpclient.module.world.ScanPhase;
import com.larpclient.util.data.BindCodes;
import com.larpclient.util.misc.LarpLog;
import com.larpclient.util.render.ZoomController;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import net.minecraft.client.Minecraft;

public final class ZoomModule extends Module {
   private static ZoomModule instance;
   public final KeybindSetting zoomKey = this.addSetting(new KeybindSetting("Zoom Key", "Key used while this module is enabled", 67));
   public final ModeSetting activation = this.addSetting(
      new ModeSetting("Activation", "Hold the zoom key or press it to toggle the zoom", "Hold", "Hold", "Toggle")
   );
   public final NumberSetting magnification = this.addSetting(
      new NumberSetting("Magnification", "Optical magnification before scroll adjustment", 4.0, 1.25, 16.0, 0.25)
   );
   public final BooleanSetting scrollZoom = this.addSetting(new BooleanSetting("Scroll Zoom", "Adjust magnification with the mouse wheel while zoomed", true));
   public final BooleanSetting rememberMagnification = this.addSetting(
      new BooleanSetting("Remember Magnification", "Keep the last wheel adjustment for the next zoom", false)
   );
   public final BooleanSetting reduceSensitivity = this.addSetting(
      new BooleanSetting("Reduce Sensitivity", "Scale mouse motion with optical magnification", true)
   );
   public final BooleanSetting smoothTransition = this.addSetting(
      new BooleanSetting("Smooth Transition", "Ease into and out of zoom without delaying input", true)
   );
   public final NumberSetting transitionTime = this.addSetting(
      new NumberSetting("Transition Time", "Seconds to reach approximately 99 percent of a new zoom", 0.15, 0.0, 0.5, 0.01)
   );
   private final ZoomController controller = new ZoomController();
   private Object lastPlayer;
   private Object lastLevel;
   private boolean wasActive;

   public ZoomModule() {
      super("Zoom", "Hold a key to zoom smoothly, with wheel adjustment and precise mouse control", Category.RENDER, -1);
      instance = this;
      this.transitionTime.visibleWhen(this.smoothTransition::getValue);
      this.rememberMagnification.visibleWhen(this.scrollZoom::getValue);
      this.zoomKey.onChange(var1 -> this.reset());
      this.activation.onChange(var1 -> this.reset());
      this.magnification.onChange(var1 -> this.reset());
   }

   public static ZoomModule getInstance() {
      return instance;
   }

   public List<Setting<?>> getKeySettings() {
      return List.of(this.zoomKey, this.activation, this.scrollZoom, this.rememberMagnification);
   }

   public List<Setting<?>> getZoomSettings() {
      return List.of(this.magnification, this.reduceSensitivity, this.smoothTransition, this.transitionTime);
   }

   @Override
   public Set<ScanPhase> getNetworkImpacts() {
      return Set.of(ScanPhase.iDLE);
   }

   @Override
   public void onEnable() {
      this.reset();
      LarpLog.debug("Zoom", "Ready; activation={}, key={}", this.activation.getValue(), this.zoomKey.getValue());
   }

   @Override
   public void onDisable() {
      this.reset();
   }

   @Override
   public void onTick() {
      this.updateZoom();
   }

   private void reset() {
      this.controller.reset(true);
      this.lastPlayer = this.lastLevel = null;
      this.wasActive = (boolean)0;
   }

   private boolean canZoom() {
      Minecraft var1 = getMc();
      return this.isRuntimeActive()
         && var1 != null
         && var1.player != null
         && var1.level != null
         && var1.player.isAlive()
         && !var1.player.isSleeping()
         && !var1.player.isScoping()
         && var1.gui.screen() == null
         && var1.gui.overlay() == null
         && var1.isWindowActive();
   }

   private void updateZoom() {
      Minecraft var1 = getMc();
      if (!this.canZoom()) {
         this.reset();
      } else {
         if (this.lastPlayer != var1.player || this.lastLevel != var1.level) {
            this.controller.reset(true);
            this.lastPlayer = var1.player;
            this.lastLevel = var1.level;
         }

         boolean var2 = BindCodes.isPressed(var1.getWindow().handle(), this.zoomKey.getValue());
         this.controller.update(true, var2, this.activation.isMode("Hold"), this.magnification.getValue(), this.rememberMagnification.getValue());
         if (this.wasActive != this.controller.isActive()) {
            this.wasActive = this.controller.isActive();
            LarpLog.debug("Zoom", "{}; magnification={}", this.wasActive ? "Engaged" : "Released", this.controller.getZoom());
         }

         boolean var3 = this.smoothTransition.getValue();
         LarpClient var4 = LarpClient.getInstance();
         if (var4 != null && var4.getConfigManager() != null) {
            var3 &= var4.getConfigManager().getPreferences().motion;
         }

         this.controller.advanceScale(System.nanoTime(), this.transitionTime.getValue(), var3);
      }
   }

   public static float applyFov(float var0, boolean var1) {
      if (instance != null && instance.isEnabled()) {
         ZoomModule var2 = instance;
         if (var2 != null && !var1) {
            var2.updateZoom();
            return ZoomController.scaleFov(var0, var2.controller.getScale());
         } else {
            return var0;
         }
      } else {
         return var0;
      }
   }

   public static double getSensitivityScale() {
      if (instance != null && instance.isEnabled()) {
         ZoomModule var0 = instance;
         if (var0 == null) {
            return 1.0;
         }

         var0.updateZoom();
         return var0.reduceSensitivity.getValue() ? var0.controller.getScale() : 1.0;
      } else {
         return 1.0;
      }
   }

   public static boolean onScroll(double var0) {
      if (instance != null && instance.isEnabled()) {
         ZoomModule var2 = instance;
         if (var2 == null) {
            return false;
         }

         var2.updateZoom();
         return var2.scrollZoom.getValue() && var2.controller.applyScroll(var0);
      } else {
         return false;
      }
   }

   @Override
   public String getStatus() {
      if (!this.isRuntimeActive()) {
         return "Idle";
      } else {
         return this.controller.isActive() ? String.format(Locale.ROOT, "%.2fx", this.controller.getZoom()) : "Waiting for zoom key";
      }
   }
}
