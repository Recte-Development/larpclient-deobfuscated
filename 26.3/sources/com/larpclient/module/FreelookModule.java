package com.larpclient.module;

import com.larpclient._q;
import com.larpclient.ij;
import com.larpclient.nz;
import com.larpclient.p4;
import java.util.Set;
import net.minecraft.client.Minecraft;

public class FreelookModule extends Module {
   private final b G = this.addSetting(new b("Distance", "Camera distance", 4.0, 1.0, 10.0, 0.5));
   private final o q = this.addSetting(new o("Activation", "Toggle the module key or keep it held to look around", "Toggle", "Toggle", "Hold"));
   private static FreelookModule k;
   private boolean j;

   @Override
   public Module.a getTickPhase() {
      return Module.a.n;
   }

   @Override
   public Set<l> getNetworkImpacts() {
      return Set.of(l.F);
   }

   public FreelookModule() {
      super("Freelook", "Look around without turning your player", Category.RENDER, -1);
      k = this;
      this.q.w(var1 -> {
         if (this.J() || this.j) {
            this.setEnabled(false);
         }
      });
   }

   public boolean J() {
      return this.q.w("Hold");
   }

   private boolean E() {
      int var1 = this.getKeyBind();
      return p4.g(var1) && p4.Y(getMc().getWindow().handle(), var1);
   }

   public static boolean u(boolean var0, boolean var1, boolean var2, boolean var3) {
      return var0 && !var1 && var2 && var3;
   }

   public boolean k() {
      if (this.isEnabled()) {
         if (this.J() && this.E()) {
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
      if (!this.J() || this.j) {
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
      if (this.J()) {
         this.disable();
      } else {
         super.suspendForDisconnect();
      }
   }

   public static void d() {
      FreelookModule var0 = k;
      if (var0 != null && var0.J() && var0.isEnabled()) {
         Minecraft var1 = getMc();
         int var2 = var1 != null && var1.player != null && var1.level != null && var1.player.isAlive() ? 1 : 0;
         int var3 = var2 != 0 && var1.gui.screen() == null && var1.gui.overlay() == null && var1.isWindowActive() ? 0 : 1;
         if (!u((boolean)var2, (boolean)var3, true, var3 == 0 && var0.E())) {
            var0.setEnabled(false);
            _q.c("Freelook", "Hold ended by release, screen, focus or world change; camera returned");
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
      this.W();
      _q.c("Freelook", "Camera passes through blocks at the selected orbit distance; activation={}", this.q.getValue());
   }

   @Override
   public void onDisable() {
      this.j = (boolean)0;
      ij.W(ij.r.i);
   }

   @Override
   public void onTick() {
      d();
      if (this.isRuntimeActive()) {
         this.W();
      }
   }

   private void W() {
      if (!nz.X("Freecam")) {
         ij.V(ij.r.i);
      }
   }

   public static boolean y() {
      return k != null && k.isEnabled() ? ij.L() == ij.r.i : false;
   }

   public static float L() {
      return ij.D();
   }

   public static float U() {
      return ij.J();
   }

   public static float H() {
      return k == null ? 4.0F : k.G.getValue().floatValue();
   }
}
