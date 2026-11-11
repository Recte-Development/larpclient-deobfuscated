package com.larpclient.module.render;

import com.larpclient.core.ViewController;
import com.larpclient.module.misc.Category;
import com.larpclient.module.misc.ModeSetting;
import com.larpclient.module.misc.Module;
import com.larpclient.module.misc.NumberSetting;
import com.larpclient.module.world.ScanPhase;
import com.larpclient.util.data.BindCodes;
import com.larpclient.util.entity.PlayerInputUtil;
import com.larpclient.util.misc.LarpLog;
import java.util.Set;
import net.minecraft.client.Minecraft;

public class FreelookModule extends Module {
   private final NumberSetting distance = this.addSetting(new NumberSetting("Distance", "Camera distance", 4.0, 1.0, 10.0, 0.5));
   private final ModeSetting activation = this.addSetting(
      new ModeSetting("Activation", "Toggle the module key or keep it held to look around", "Toggle", "Toggle", "Hold")
   );
   private static FreelookModule instance;
   private boolean holdActive;

   @Override
   public Module.TickPhase getTickPhase() {
      return Module.TickPhase.EARLY;
   }

   @Override
   public Set<ScanPhase> getNetworkImpacts() {
      return Set.of(ScanPhase.sCANNING);
   }

   public FreelookModule() {
      super("Freelook", "Look around without turning your player", Category.RENDER, -1);
      instance = this;
      this.activation.onChange(var1 -> {
         if (this.isHoldMode() || this.holdActive) {
            this.setEnabled(false);
         }
      });
   }

   public boolean isHoldMode() {
      return this.activation.isMode("Hold");
   }

   private boolean isBindPressed() {
      int var1 = this.getKeyBind();
      return BindCodes.isValidBind(var1) && BindCodes.isPressed(getMc().getWindow().handle(), var1);
   }

   public static boolean shouldKeepHold(boolean var0, boolean var1, boolean var2, boolean var3) {
      return var0 && !var1 && var2 && var3;
   }

   public boolean tryStartHold() {
      if (this.isEnabled()) {
         if (this.isHoldMode() && this.isBindPressed()) {
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

   public static void checkHold() {
      FreelookModule var0 = instance;
      if (var0 != null && var0.isHoldMode() && var0.isEnabled()) {
         Minecraft var1 = getMc();
         int var2 = var1 != null && var1.player != null && var1.level != null && var1.player.isAlive() ? 1 : 0;
         int var3 = var2 != 0 && var1.gui.screen() == null && var1.gui.overlay() == null && var1.isWindowActive() ? 0 : 1;
         if (!shouldKeepHold((boolean)var2, (boolean)var3, true, var3 == 0 && var0.isBindPressed())) {
            var0.setEnabled(false);
            LarpLog.debug("Freelook", "Hold ended by release, screen, focus or world change; camera returned");
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
      this.acquireView();
      LarpLog.debug("Freelook", "Camera passes through blocks at the selected orbit distance; activation={}", this.activation.getValue());
   }

   @Override
   public void onDisable() {
      this.holdActive = (boolean)0;
      ViewController.release(ViewController.Owner.FREELOOK);
   }

   @Override
   public void onTick() {
      checkHold();
      if (this.isRuntimeActive()) {
         this.acquireView();
      }
   }

   private void acquireView() {
      if (!PlayerInputUtil.isModuleActive("Freecam")) {
         ViewController.acquire(ViewController.Owner.FREELOOK);
      }
   }

   public static boolean isFreelookActive() {
      return instance != null && instance.isEnabled() ? ViewController.update() == ViewController.Owner.FREELOOK : false;
   }

   public static float getYaw() {
      return ViewController.getRawYaw();
   }

   public static float getPitch() {
      return ViewController.getPosition();
   }

   public static float getDistance() {
      return instance == null ? 4.0F : instance.distance.getValue().floatValue();
   }
}
