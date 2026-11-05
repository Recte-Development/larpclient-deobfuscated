package com.larpclient.module;

import com.larpclient.LarpClient;
import com.larpclient.cc;
import com.larpclient.mt;
import com.larpclient.xz;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import net.minecraft.client.Minecraft;

public final class ZoomModule extends Module {
   private static ZoomModule H;
   public final dv x = this.addSetting(new dv("Zoom Key", "Key used while this module is enabled", 67));
   public final d6 j = this.addSetting(new d6("Activation", "Hold the zoom key or press it to toggle the zoom", "Hold", "Hold", "Toggle"));
   public final d9 Q = this.addSetting(new d9("Magnification", "Optical magnification before scroll adjustment", 4.0, 1.25, 16.0, 0.25));
   public final dh P = this.addSetting(new dh("Scroll Zoom", "Adjust magnification with the mouse wheel while zoomed", true));
   public final dh i = this.addSetting(new dh("Remember Magnification", "Keep the last wheel adjustment for the next zoom", false));
   public final dh E = this.addSetting(new dh("Reduce Sensitivity", "Scale mouse motion with optical magnification", true));
   public final dh Z = this.addSetting(new dh("Smooth Transition", "Ease into and out of zoom without delaying input", true));
   public final d9 K = this.addSetting(new d9("Transition Time", "Seconds to reach approximately 99 percent of a new zoom", 0.15, 0.0, 0.5, 0.01));
   private final xz S = new xz();
   private Object F;
   private Object o;
   private boolean m;

   public ZoomModule() {
      super("Zoom", "Hold a key to zoom smoothly, with wheel adjustment and precise mouse control", Category.RENDER, -1);
      H = this;
      this.K.H(this.Z::P);
      this.i.H(this.P::P);
      this.x.h(var1 -> this.h());
      this.j.h(var1 -> this.h());
      this.Q.h(var1 -> this.h());
   }

   public static ZoomModule x() {
      return H;
   }

   public List<df<?>> U() {
      return List.of(this.x, this.j, this.P, this.i);
   }

   public List<df<?>> G() {
      return List.of(this.Q, this.E, this.Z, this.K);
   }

   @Override
   public Set<dm> getNetworkImpacts() {
      return Set.of(dm.m);
   }

   @Override
   public void onEnable() {
      this.h();
      mt.U("Zoom", "Ready; activation={}, key={}", this.j.P(), this.x.P());
   }

   @Override
   public void onDisable() {
      this.h();
   }

   @Override
   public void onTick() {
      this.t();
   }

   private void h() {
      this.S.I(true);
      this.F = this.o = null;
      this.m = (boolean)0;
   }

   private boolean I() {
      Minecraft var1 = getMc();
      return this.isRuntimeActive()
         && var1 != null
         && var1.player != null
         && var1.level != null
         && var1.player.isAlive()
         && !var1.player.isSleeping()
         && !var1.player.isScoping()
         && var1.gui.screen() == null
         && var1.gui.overlay() == null
         && var1.isWindowActive();
   }

   private void t() {
      Minecraft var1 = getMc();
      if (!this.I()) {
         this.h();
      } else {
         if (this.F != var1.player || this.o != var1.level) {
            this.S.I(true);
            this.F = var1.player;
            this.o = var1.level;
         }

         boolean var2 = cc.N(var1.getWindow().handle(), this.x.P());
         this.S.E(true, var2, this.j.S("Hold"), this.Q.P(), this.i.P());
         if (this.m != this.S.k()) {
            this.m = this.S.k();
            mt.U("Zoom", "{}; magnification={}", this.m ? "Engaged" : "Released", this.S.L());
         }

         boolean var3 = this.Z.P();
         LarpClient var4 = LarpClient.F();
         if (var4 != null && var4.f() != null) {
            var3 &= var4.f().t().V;
         }

         this.S.t(System.nanoTime(), this.K.P(), var3);
      }
   }

   public static float w(float var0, boolean var1) {
      if (H != null && H.isEnabled()) {
         ZoomModule var2 = H;
         if (var2 != null && !var1) {
            var2.t();
            return xz.K(var0, var2.S.H());
         } else {
            return var0;
         }
      } else {
         return var0;
      }
   }

   public static double q() {
      if (H != null && H.isEnabled()) {
         ZoomModule var0 = H;
         if (var0 == null) {
            return 1.0;
         }

         var0.t();
         return var0.E.P() ? var0.S.H() : 1.0;
      } else {
         return 1.0;
      }
   }

   public static boolean r(double var0) {
      if (H != null && H.isEnabled()) {
         ZoomModule var2 = H;
         if (var2 == null) {
            return false;
         }

         var2.t();
         return var2.P.P() && var2.S.z(var0);
      } else {
         return false;
      }
   }

   @Override
   public String getStatus() {
      if (!this.isRuntimeActive()) {
         return "Idle";
      } else {
         return this.S.k() ? String.format(Locale.ROOT, "%.2fx", this.S.L()) : "Waiting for zoom key";
      }
   }
}
