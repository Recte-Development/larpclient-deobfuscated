package com.larpclient.module;

import com.larpclient.r4;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.util.Mth;

public final class SpinBotModule extends Module {
   private static SpinBotModule C;
   private final lc n = this.addSetting(new lc("Speed", "Model spin speed in degrees per second", 360.0, 15.0, 1440.0, 15.0));
   private final lp O = this.addSetting(new lp("Direction", "Model spin direction", "Clockwise", "Clockwise", "Counterclockwise"));
   private float X;
   private float p;

   public SpinBotModule() {
      super("Spin Bot", "Spin your player model on your screen only; real facing and movement are unchanged", Category.COMBAT, -1);
      C = this;
   }

   public static SpinBotModule B() {
      return C;
   }

   @Override
   public void onEnable() {
      getMc();
      this.X = this.p = mc.player == null ? 0.0F : mc.player.yBodyRot;
      r4.V(this.getName(), "Enabled: client-side model spin at {} degrees/s, {}", this.n.getValue(), this.O.getValue());
   }

   @Override
   public void onDisable() {
      r4.g(this.getName(), "Disabled: native model facing restored");
   }

   @Override
   public void onTick() {
      this.p = this.X;
      float var10001 = this.X;
      float var10002 = this.n.getValue().floatValue() / 20.0F;
      int var10003 = this.O.i("Clockwise");
      boolean var2 = false;
      byte var1 = (byte)var10003;
      var10003 = Integer.signum(var1);
      this.X = Mth.wrapDegrees(var10001 + var10002 * (((var10003 | -var10003) >>> 31 ^ 1) == 0 ? 1 : -1));
   }

   public float z(float var1, float var2) {
      if (this.isEnabled()) {
         return this.j() ? Mth.wrapDegrees(Mth.rotLerp(var2, this.p, this.X)) : var1;
      } else {
         return var1;
      }
   }

   public float s(float var1) {
      if (this.isEnabled()) {
         return this.j() ? 0.0F : var1;
      } else {
         return var1;
      }
   }

   private boolean j() {
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
