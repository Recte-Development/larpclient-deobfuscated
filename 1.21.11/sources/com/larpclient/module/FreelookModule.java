package com.larpclient.module;

import com.larpclient.j;
import com.larpclient.q8;
import com.larpclient.s5;
import com.larpclient.wp;
import java.util.Set;
import net.minecraft.class_310;

public class FreelookModule extends Module {
   private final lo z = this.addSetting(new lo("Distance", "Camera distance", 4.0, 1.0, 10.0, 0.5));
   private final li p = this.addSetting(new li("Activation", "Toggle the module key or keep it held to look around", "Toggle", "Toggle", "Hold"));
   private static FreelookModule s;
   private boolean c;

   @Override
   public Module.p getTickPhase() {
      return Module.p.g;
   }

   @Override
   public Set<l5> getNetworkImpacts() {
      return Set.of(l5.Y);
   }

   public FreelookModule() {
      super("Freelook", "Look around without turning your player", Category.RENDER, -1);
      s = this;
      this.p.a(var1 -> {
         if (this.E() || this.c) {
            this.setEnabled(false);
         }
      });
   }

   public boolean E() {
      return this.p.G("Hold");
   }

   private boolean X() {
      int var1 = this.getKeyBind();
      return s5.A(var1) && s5.m(getMc().method_22683().method_4490(), var1);
   }

   public static boolean r(boolean var0, boolean var1, boolean var2, boolean var3) {
      return var0 && !var1 && var2 && var3;
   }

   public boolean V() {
      if (this.isEnabled()) {
         if (this.E() && this.X()) {
            this.c = (boolean)1;
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
      if (!this.E() || this.c) {
         super.enable();
      }
   }

   @Override
   public void disable() {
      this.c = (boolean)0;
      super.disable();
   }

   @Override
   public void suspendForDisconnect() {
      if (this.E()) {
         this.disable();
      } else {
         super.suspendForDisconnect();
      }
   }

   public static void n() {
      FreelookModule var0 = s;
      if (var0 != null && var0.E() && var0.isEnabled()) {
         class_310 var1 = getMc();
         int var2 = var1 != null && var1.field_1724 != null && var1.field_1687 != null && var1.field_1724.method_5805() ? 1 : 0;
         int var3 = var2 != 0 && var1.field_1755 == null && var1.method_18506() == null && var1.method_1569() ? 0 : 1;
         if (!r((boolean)var2, (boolean)var3, true, var3 == 0 && var0.X())) {
            var0.setEnabled(false);
            q8.v("Freelook", "Hold ended by release, screen, focus or world change; camera returned");
         }
      }
   }

   @Override
   public void setKeyBind(int var1) {
      if (this.E() && var1 != this.getKeyBind()) {
         this.setEnabled(false);
      }

      super.setKeyBind(var1);
   }

   @Override
   public void onEnable() {
      this.Z();
      q8.v("Freelook", "Camera passes through blocks at the selected orbit distance; activation={}", this.p.getValue());
   }

   @Override
   public void onDisable() {
      this.c = (boolean)0;
      wp.k(wp.z.x);
   }

   @Override
   public void onTick() {
      n();
      if (this.isRuntimeActive()) {
         this.Z();
      }
   }

   private void Z() {
      if (!j.F("Freecam")) {
         wp.S(wp.z.x);
      }
   }

   public static boolean l() {
      return s != null && s.isEnabled() ? wp.o() == wp.z.x : false;
   }

   public static float O() {
      return wp.g();
   }

   public static float G() {
      return wp.J();
   }

   public static float f() {
      return s == null ? 4.0F : s.z.getValue().floatValue();
   }
}
