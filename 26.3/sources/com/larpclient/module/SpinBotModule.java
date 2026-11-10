package com.larpclient.module;

import com.larpclient._q;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.util.Mth;

public final class SpinBotModule extends Module {
   private static SpinBotModule O;
   private final b U = this.addSetting(new b("Speed", "Model spin speed in degrees per second", 360.0, 15.0, 1440.0, 15.0));
   private final o J = this.addSetting(new o("Direction", "Model spin direction", "Clockwise", "Clockwise", "Counterclockwise"));
   private float d;
   private float Z;

   public SpinBotModule() {
      super("Spin Bot", "Spin your player model on your screen only; real facing and movement are unchanged", Category.COMBAT, -1);
      O = this;
   }

   public static SpinBotModule X() {
      return O;
   }

   @Override
   public void onEnable() {
      getMc();
      this.d = this.Z = mc.player == null ? 0.0F : mc.player.yBodyRot;
      _q.z(this.getName(), "Enabled: client-side model spin at {} degrees/s, {}", this.U.getValue(), this.J.getValue());
   }

   @Override
   public void onDisable() {
      _q.c(this.getName(), "Disabled: native model facing restored");
   }

   @Override
   public void onTick() {
      this.Z = this.d;
      float var10001 = this.d;
      float var10002 = this.U.getValue().floatValue() / 20.0F;
      int var10003 = this.J.w("Clockwise");
      boolean var2 = false;
      byte var1 = (byte)var10003;
      var10003 = Integer.signum(var1);
      this.d = Mth.wrapDegrees(var10001 + var10002 * (((var10003 | -var10003) >>> 31 ^ 1) == 0 ? 1 : -1));
   }

   public float Y(float var1, float var2) {
      if (this.isEnabled()) {
         return this.B() ? Mth.wrapDegrees(Mth.rotLerp(var2, this.Z, this.d)) : var1;
      } else {
         return var1;
      }
   }

   public float L(float var1) {
      if (this.isEnabled()) {
         return this.B() ? 0.0F : var1;
      } else {
         return var1;
      }
   }

   private boolean B() {
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
