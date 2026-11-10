package com.larpclient.module;

import com.larpclient.bc;
import com.larpclient.c5;
import com.larpclient.p_;
import java.util.Set;
import net.minecraft.client.Minecraft;

public class FreelookModule extends Module {
   private final e h = this.addSetting(new e("Distance", "Camera distance", 4.0, 1.0, 10.0, 0.5));
   private final l Y = this.addSetting(new l("Activation", "Toggle the module key or keep it held to look around", "Toggle", "Toggle", "Hold"));
   private static FreelookModule q;
   private boolean F;

   @Override
   public Module.f getTickPhase() {
      return Module.f.N;
   }

   @Override
   public Set<m> getNetworkImpacts() {
      return Set.of(m.K);
   }

   public FreelookModule() {
      super("Freelook", "Look around without turning your player", Category.RENDER, -1);
      q = this;
      this.Y.o(var1 -> {
         if (this.J() || this.F) {
            this.setEnabled(false);
         }
      });
   }

   public boolean J() {
      return this.Y.O("Hold");
   }

   private boolean H() {
      int var1 = this.getKeyBind();
      return bc.a(var1) && bc.I(getMc().getWindow().handle(), var1);
   }

   public static boolean J(boolean var0, boolean var1, boolean var2, boolean var3) {
      return var0 && !var1 && var2 && var3;
   }

   public boolean V() {
      if (this.isEnabled()) {
         if (this.J() && this.H()) {
            this.F = (boolean)1;
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
      if (!this.J() || this.F) {
         super.enable();
      }
   }

   @Override
   public void disable() {
      this.F = (boolean)0;
      super.disable();
   }

   @Override
   public void suspendForDisconnect() {
      if (this.J()) {
         this.disable();
      } else {
         super.suspendForDisconnect();
      }
   }

   public static void S() {
      FreelookModule var0 = q;
      if (var0 != null && var0.J() && var0.isEnabled()) {
         Minecraft var1 = getMc();
         int var2 = var1 != null && var1.player != null && var1.level != null && var1.player.isAlive() ? 1 : 0;
         int var3 = var2 != 0 && var1.gui.screen() == null && var1.gui.overlay() == null && var1.isWindowActive() ? 0 : 1;
         if (!J((boolean)var2, (boolean)var3, true, var3 == 0 && var0.H())) {
            var0.setEnabled(false);
            com.larpclient.h.U("Freelook", "Hold ended by release, screen, focus or world change; camera returned");
         }
      }
   }

   @Override
   public void setKeyBind(int var1) {
      if (this.J() && var1 != this.getKeyBind()) {
         this.setEnabled(false);
      }

      super.setKeyBind(var1);
   }

   @Override
   public void onEnable() {
      this.q();
      com.larpclient.h.U("Freelook", "Camera passes through blocks at the selected orbit distance; activation={}", this.Y.getValue());
   }

   @Override
   public void onDisable() {
      this.F = (boolean)0;
      p_.Y(p_.o.t);
   }

   @Override
   public void onTick() {
      S();
      if (this.isRuntimeActive()) {
         this.q();
      }
   }

   private void q() {
      if (!c5.t("Freecam")) {
         p_.p(p_.o.t);
      }
   }

   public static boolean b() {
      return q != null && q.isEnabled() ? p_.A() == p_.o.t : false;
   }

   public static float e() {
      return p_.V();
   }

   public static float u() {
      return p_.s();
   }

   public static float E() {
      return q == null ? 4.0F : q.h.getValue().floatValue();
   }
}
