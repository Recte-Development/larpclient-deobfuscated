package com.larpclient.module;

import com.larpclient.cc;
import com.larpclient.mt;
import com.larpclient.te;
import com.larpclient.tq;
import java.util.Set;
import net.minecraft.client.Minecraft;

public class FreelookModule extends Module {
   private final d9 i = this.addSetting(new d9("Distance", "Camera distance", 4.0, 1.0, 10.0, 0.5));
   private final d6 T = this.addSetting(new d6("Activation", "Toggle the module key or keep it held to look around", "Toggle", "Toggle", "Hold"));
   private static FreelookModule O;
   private boolean C;

   @Override
   public Module.f getTickPhase() {
      return Module.f.M;
   }

   @Override
   public Set<dm> getNetworkImpacts() {
      return Set.of(dm.z);
   }

   public FreelookModule() {
      super("Freelook", "Look around without turning your player", Category.RENDER, -1);
      O = this;
      this.T.h(var1 -> {
         if (this.g() || this.C) {
            this.setEnabled(false);
         }
      });
   }

   public boolean g() {
      return this.T.S("Hold");
   }

   private boolean Q() {
      int var1 = this.getKeyBind();
      return cc.N(var1) && cc.N(getMc().getWindow().handle(), var1);
   }

   public static boolean f(boolean var0, boolean var1, boolean var2, boolean var3) {
      return var0 && !var1 && var2 && var3;
   }

   public boolean s() {
      if (this.isEnabled()) {
         if (this.g() && this.Q()) {
            this.C = (boolean)1;
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
      if (!this.g() || this.C) {
         super.enable();
      }
   }

   @Override
   public void disable() {
      this.C = (boolean)0;
      super.disable();
   }

   @Override
   public void suspendForDisconnect() {
      if (this.g()) {
         this.disable();
      } else {
         super.suspendForDisconnect();
      }
   }

   public static void o() {
      FreelookModule var0 = O;
      if (var0 != null && var0.g() && var0.isEnabled()) {
         Minecraft var1 = getMc();
         int var2 = var1 != null && var1.player != null && var1.level != null && var1.player.isAlive() ? 1 : 0;
         int var3 = var2 != 0 && var1.gui.screen() == null && var1.gui.overlay() == null && var1.isWindowActive() ? 0 : 1;
         if (!f((boolean)var2, (boolean)var3, true, var3 == 0 && var0.Q())) {
            var0.setEnabled(false);
            mt.U("Freelook", "Hold ended by release, screen, focus or world change; camera returned");
         }
      }
   }

   @Override
   public void setKeyBind(int var1) {
      if (this.g() && var1 != this.getKeyBind()) {
         this.setEnabled(false);
      }

      super.setKeyBind(var1);
   }

   @Override
   public void onEnable() {
      this.W();
      mt.U("Freelook", "Camera passes through blocks at the selected orbit distance; activation={}", this.T.P());
   }

   @Override
   public void onDisable() {
      this.C = (boolean)0;
      tq.A(tq.y.O);
   }

   @Override
   public void onTick() {
      o();
      if (this.isRuntimeActive()) {
         this.W();
      }
   }

   private void W() {
      if (!te.i("Freecam")) {
         tq.i(tq.y.O);
      }
   }

   public static boolean D() {
      return O != null && O.isEnabled() ? tq.l() == tq.y.O : false;
   }

   public static float C() {
      return tq.J();
   }

   public static float X() {
      return tq.F();
   }

   public static float V() {
      return O == null ? 4.0F : O.i.P().floatValue();
   }
}
