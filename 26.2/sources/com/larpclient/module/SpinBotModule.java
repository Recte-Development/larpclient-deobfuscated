package com.larpclient.module;

import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.util.Mth;

public final class SpinBotModule extends Module {
   private static SpinBotModule o;
   private final e F = this.addSetting(new e("Speed", "Model spin speed in degrees per second", 360.0, 15.0, 1440.0, 15.0));
   private final l T = this.addSetting(new l("Direction", "Model spin direction", "Clockwise", "Clockwise", "Counterclockwise"));
   private float U;
   private float k;

   public SpinBotModule() {
      super("Spin Bot", "Spin your player model on your screen only; real facing and movement are unchanged", Category.COMBAT, -1);
      o = this;
   }

   public static SpinBotModule T() {
      return o;
   }

   @Override
   public void onEnable() {
      getMc();
      this.U = this.k = mc.player == null ? 0.0F : mc.player.yBodyRot;
      com.larpclient.h.A(this.getName(), "Enabled: client-side model spin at {} degrees/s, {}", this.F.getValue(), this.T.getValue());
   }

   @Override
   public void onDisable() {
      com.larpclient.h.U(this.getName(), "Disabled: native model facing restored");
   }

   @Override
   public void onTick() {
      this.k = this.U;
      float var10001 = this.U;
      float var10002 = this.F.getValue().floatValue() / 20.0F;
      int var10003 = this.T.O("Clockwise");
      boolean var2 = false;
      byte var1 = (byte)var10003;
      var10003 = Integer.signum(var1);
      this.U = Mth.wrapDegrees(var10001 + var10002 * (((var10003 | -var10003) >>> 31 ^ 1) == 0 ? 1 : -1));
   }

   public float P(float var1, float var2) {
      if (this.isEnabled()) {
         return this.O() ? Mth.wrapDegrees(Mth.rotLerp(var2, this.k, this.U)) : var1;
      } else {
         return var1;
      }
   }

   public float q(float var1) {
      if (this.isEnabled()) {
         return this.O() ? 0.0F : var1;
      } else {
         return var1;
      }
   }

   private boolean O() {
      if (!this.isRuntimeActive() || mc == null || mc.player == null) {
         return false;
      } else if (mc.options != null && !mc.options.getCameraType().isFirstPerson()) {
         Screen var1 = mc.gui.screen();
         return var1 == null || var1 instanceof ChatScreen;
      } else {
         return false;
      }
   }
}
