package com.larpclient.module;

import com.larpclient.mt;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.util.Mth;

public final class SpinBotModule extends Module {
   private static SpinBotModule o;
   private final d9 U = this.addSetting(new d9("Speed", "Model spin speed in degrees per second", 360.0, 15.0, 1440.0, 15.0));
   private final d6 y = this.addSetting(new d6("Direction", "Model spin direction", "Clockwise", "Clockwise", "Counterclockwise"));
   private float M;
   private float b;

   public SpinBotModule() {
      super("Spin Bot", "Spin your player model on your screen only; real facing and movement are unchanged", Category.COMBAT, -1);
      o = this;
   }

   public static SpinBotModule L() {
      return o;
   }

   @Override
   public void onEnable() {
      getMc();
      this.M = this.b = mc.player == null ? 0.0F : mc.player.yBodyRot;
      mt.K(this.getName(), "Enabled: client-side model spin at {} degrees/s, {}", this.U.P(), this.y.P());
   }

   @Override
   public void onDisable() {
      mt.U(this.getName(), "Disabled: native model facing restored");
   }

   @Override
   public void onTick() {
      this.b = this.M;
      float var10001 = this.M;
      float var10002 = this.U.P().floatValue() / 20.0F;
      int var10003 = this.y.S("Clockwise");
      boolean var2 = false;
      byte var1 = (byte)var10003;
      var10003 = Integer.signum(var1);
      this.M = Mth.wrapDegrees(var10001 + var10002 * (((var10003 | -var10003) >>> 31 ^ 1) == 0 ? 1 : -1));
   }

   public float D(float var1, float var2) {
      if (this.isEnabled()) {
         return this.M() ? Mth.wrapDegrees(Mth.rotLerp(var2, this.b, this.M)) : var1;
      } else {
         return var1;
      }
   }

   public float I(float var1) {
      if (this.isEnabled()) {
         return this.M() ? 0.0F : var1;
      } else {
         return var1;
      }
   }

   private boolean M() {
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
