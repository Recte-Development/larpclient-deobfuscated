package com.larpclient.module.combat;

import com.larpclient.module.misc.Category;
import com.larpclient.module.misc.ModeSetting;
import com.larpclient.module.misc.Module;
import com.larpclient.module.misc.NumberSetting;
import com.larpclient.util.misc.LarpLog;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.util.Mth;

public final class SpinBotModule extends Module {
   private static SpinBotModule instance;
   private final NumberSetting speed = this.addSetting(new NumberSetting("Speed", "Model spin speed in degrees per second", 360.0, 15.0, 1440.0, 15.0));
   private final ModeSetting direction = this.addSetting(new ModeSetting("Direction", "Model spin direction", "Clockwise", "Clockwise", "Counterclockwise"));
   private float yaw;
   private float previousYaw;

   public SpinBotModule() {
      super("Spin Bot", "Spin your player model on your screen only; real facing and movement are unchanged", Category.COMBAT, -1);
      instance = this;
   }

   public static SpinBotModule getInstance() {
      return instance;
   }

   @Override
   public void onEnable() {
      getMc();
      this.yaw = this.previousYaw = minecraft.player == null ? 0.0F : minecraft.player.yBodyRot;
      LarpLog.info(this.getName(), "Enabled: client-side model spin at {} degrees/s, {}", this.speed.getValue(), this.direction.getValue());
   }

   @Override
   public void onDisable() {
      LarpLog.debug(this.getName(), "Disabled: native model facing restored");
   }

   @Override
   public void onTick() {
      this.previousYaw = this.yaw;
      float var10001 = this.yaw;
      float var10002 = this.speed.getValue().floatValue() / 20.0F;
      int var10003 = this.direction.isMode("Clockwise");
      boolean var2 = false;
      byte var1 = (byte)var10003;
      var10003 = Integer.signum(var1);
      this.yaw = Mth.wrapDegrees(var10001 + var10002 * (((var10003 | -var10003) >>> 31 ^ 1) == 0 ? 1 : -1));
   }

   public float getBodyYaw(float var1, float var2) {
      if (this.isEnabled()) {
         return this.isSpinning() ? Mth.wrapDegrees(Mth.rotLerp(var2, this.previousYaw, this.yaw)) : var1;
      } else {
         return var1;
      }
   }

   public float getHeadYawOffset(float var1) {
      if (this.isEnabled()) {
         return this.isSpinning() ? 0.0F : var1;
      } else {
         return var1;
      }
   }

   private boolean isSpinning() {
      if (!this.isRuntimeActive() || minecraft == null || minecraft.player == null) {
         return false;
      } else if (minecraft.options != null && !minecraft.options.getCameraType().isFirstPerson()) {
         Screen var1 = minecraft.gui.screen();
         return var1 == null || var1 instanceof ChatScreen;
      } else {
         return false;
      }
   }
}
