package com.larpclient.module;

import com.larpclient.q8;
import net.minecraft.class_3532;
import net.minecraft.class_408;
import net.minecraft.class_437;

public final class SpinBotModule extends Module {
   private static SpinBotModule B;
   private final lo V = this.addSetting(new lo("Speed", "Model spin speed in degrees per second", 360.0, 15.0, 1440.0, 15.0));
   private final li g = this.addSetting(new li("Direction", "Model spin direction", "Clockwise", "Clockwise", "Counterclockwise"));
   private float P;
   private float k;

   public SpinBotModule() {
      super("Spin Bot", "Spin your player model on your screen only; real facing and movement are unchanged", Category.COMBAT, -1);
      B = this;
   }

   public static SpinBotModule A() {
      return B;
   }

   @Override
   public void onEnable() {
      getMc();
      this.P = this.k = mc.field_1724 == null ? 0.0F : mc.field_1724.field_6283;
      q8.G(this.getName(), "Enabled: client-side model spin at {} degrees/s, {}", this.V.getValue(), this.g.getValue());
   }

   @Override
   public void onDisable() {
      q8.v(this.getName(), "Disabled: native model facing restored");
   }

   @Override
   public void onTick() {
      this.k = this.P;
      float var10001 = this.P;
      float var10002 = this.V.getValue().floatValue() / 20.0F;
      int var10003 = this.g.G("Clockwise");
      boolean var2 = false;
      byte var1 = (byte)var10003;
      var10003 = Integer.signum(var1);
      this.P = class_3532.method_15393(var10001 + var10002 * (((var10003 | -var10003) >>> 31 ^ 1) == 0 ? 1 : -1));
   }

   public float u(float var1, float var2) {
      if (this.isEnabled()) {
         return this.K() ? class_3532.method_15393(class_3532.method_17821(var2, this.k, this.P)) : var1;
      } else {
         return var1;
      }
   }

   public float o(float var1) {
      if (this.isEnabled()) {
         return this.K() ? 0.0F : var1;
      } else {
         return var1;
      }
   }

   private boolean K() {
      if (!this.isRuntimeActive() || mc == null || mc.field_1724 == null) {
         return false;
      } else if (mc.field_1690 != null && !mc.field_1690.method_31044().method_31034()) {
         class_437 var1 = mc.field_1755;
         return var1 == null || var1 instanceof class_408;
      } else {
         return false;
      }
   }
}
