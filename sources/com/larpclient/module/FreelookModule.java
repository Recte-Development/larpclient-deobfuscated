package com.larpclient.module;

import com.larpclient.pe;
import com.larpclient.r4;
import com.larpclient.rr;
import com.larpclient.yu;
import java.util.Set;
import net.minecraft.client.Minecraft;

public class FreelookModule extends Module {
   private final lc s = this.addSetting(new lc("Distance", "Camera distance", 4.0, 1.0, 10.0, 0.5));
   private final lp z = this.addSetting(new lp("Activation", "Toggle the module key or keep it held to look around", "Toggle", "Toggle", "Hold"));
   private static FreelookModule o;
   private boolean j;

   @Override
   public Module.b getTickPhase() {
      return Module.b.m;
   }

   @Override
   public Set<ld> getNetworkImpacts() {
      return Set.of(ld.a);
   }

   public FreelookModule() {
      super("Freelook", "Look around without turning your player", Category.RENDER, -1);
      o = this;
      this.z.W(var1 -> {
         if (this.o() || this.j) {
            this.setEnabled(false);
         }
      });
   }

   public boolean o() {
      return this.z.i("Hold");
   }

   private boolean N() {
      int var1 = this.getKeyBind();
      return pe.F(var1) && pe.A(getMc().getWindow().handle(), var1);
   }

   public static boolean Q(boolean var0, boolean var1, boolean var2, boolean var3) {
      return var0 && !var1 && var2 && var3;
   }

   public boolean P() {
      if (this.isEnabled()) {
         if (this.o() && this.N()) {
            this.j = (boolean)1;
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
      if (!this.o() || this.j) {
         super.enable();
      }
   }

   @Override
   public void disable() {
      this.j = (boolean)0;
      super.disable();
   }

   @Override
   public void suspendForDisconnect() {
      if (this.o()) {
         this.disable();
      } else {
         super.suspendForDisconnect();
      }
   }

   public static void k() {
      FreelookModule var0 = o;
      if (var0 != null && var0.o() && var0.isEnabled()) {
         Minecraft var1 = getMc();
         int var2 = var1 != null && var1.player != null && var1.level != null && var1.player.isAlive() ? 1 : 0;
         int var3 = var2 != 0 && var1.gui.screen() == null && var1.gui.overlay() == null && var1.isWindowActive() ? 0 : 1;
         if (!Q((boolean)var2, (boolean)var3, true, var3 == 0 && var0.N())) {
            var0.setEnabled(false);
            r4.g("Freelook", "Hold ended by release, screen, focus or world change; camera returned");
         }
      }
   }

   @Override
   public void setKeyBind(int var1) {
      if (this.o() && var1 != this.getKeyBind()) {
         this.setEnabled(false);
      }

      super.setKeyBind(var1);
   }

   @Override
   public void onEnable() {
      this.Y();
      r4.g("Freelook", "Camera passes through blocks at the selected orbit distance; activation={}", this.z.getValue());
   }

   @Override
   public void onDisable() {
      this.j = (boolean)0;
      rr.N(rr.f.R);
   }

   @Override
   public void onTick() {
      k();
      if (this.isRuntimeActive()) {
         this.Y();
      }
   }

   private void Y() {
      if (!yu.G("Freecam")) {
         rr.W(rr.f.R);
      }
   }

   public static boolean w() {
      return o != null && o.isEnabled() ? rr.u() == rr.f.R : false;
   }

   public static float U() {
      return rr.j();
   }

   public static float O() {
      return rr.T();
   }

   public static float Q() {
      return o == null ? 4.0F : o.s.getValue().floatValue();
   }
}
