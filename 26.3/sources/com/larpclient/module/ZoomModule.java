package com.larpclient.module;

import com.larpclient.LarpClient;
import com.larpclient._q;
import com.larpclient.jk;
import com.larpclient.p4;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import net.minecraft.client.Minecraft;

public final class ZoomModule extends Module {
   private static ZoomModule a;
   public final x x = this.addSetting(new x("Zoom Key", "Key used while this module is enabled", 67));
   public final o P = this.addSetting(new o("Activation", "Hold the zoom key or press it to toggle the zoom", "Hold", "Hold", "Toggle"));
   public final b j = this.addSetting(new b("Magnification", "Optical magnification before scroll adjustment", 4.0, 1.25, 16.0, 0.25));
   public final m S = this.addSetting(new m("Scroll Zoom", "Adjust magnification with the mouse wheel while zoomed", true));
   public final m C = this.addSetting(new m("Remember Magnification", "Keep the last wheel adjustment for the next zoom", false));
   public final m n = this.addSetting(new m("Reduce Sensitivity", "Scale mouse motion with optical magnification", true));
   public final m V = this.addSetting(new m("Smooth Transition", "Ease into and out of zoom without delaying input", true));
   public final b f = this.addSetting(new b("Transition Time", "Seconds to reach approximately 99 percent of a new zoom", 0.15, 0.0, 0.5, 0.01));
   private final jk b = new jk();
   private Object I;
   private Object y;
   private boolean e;

   public ZoomModule() {
      super("Zoom", "Hold a key to zoom smoothly, with wheel adjustment and precise mouse control", Category.RENDER, -1);
      a = this;
      this.f.a(this.V::getValue);
      this.C.a(this.S::getValue);
      this.x.w(var1 -> this.C());
      this.P.w(var1 -> this.C());
      this.j.w(var1 -> this.C());
   }

   public static ZoomModule n() {
      return a;
   }

   public List<r<?>> v() {
      return List.of(this.x, this.P, this.S, this.C);
   }

   public List<r<?>> B() {
      return List.of(this.j, this.n, this.V, this.f);
   }

   @Override
   public Set<l> getNetworkImpacts() {
      return Set.of(l.E);
   }

   @Override
   public void onEnable() {
      this.C();
      _q.c("Zoom", "Ready; activation={}, key={}", this.P.getValue(), this.x.getValue());
   }

   @Override
   public void onDisable() {
      this.C();
   }

   @Override
   public void onTick() {
      this.J();
   }

   private void C() {
      this.b.h(true);
      this.I = this.y = null;
      this.e = (boolean)0;
   }

   private boolean k() {
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

   private void J() {
      Minecraft var1 = getMc();
      if (!this.k()) {
         this.C();
      } else {
         if (this.I != var1.player || this.y != var1.level) {
            this.b.h(true);
            this.I = var1.player;
            this.y = var1.level;
         }

         boolean var2 = p4.Y(var1.getWindow().handle(), this.x.getValue());
         this.b.z(true, var2, this.P.w("Hold"), this.j.getValue(), this.C.getValue());
         if (this.e != this.b.B()) {
            this.e = this.b.B();
            _q.c("Zoom", "{}; magnification={}", this.e ? "Engaged" : "Released", this.b.v());
         }

         boolean var3 = this.V.getValue();
         LarpClient var4 = LarpClient.N();
         if (var4 != null && var4.h() != null) {
            var3 &= var4.h().k().w;
         }

         this.b.X(System.nanoTime(), this.f.getValue(), var3);
      }
   }

   public static float P(float var0, boolean var1) {
      if (a != null && a.isEnabled()) {
         ZoomModule var2 = a;
         if (var2 != null && !var1) {
            var2.J();
            return jk.p(var0, var2.b.U());
         } else {
            return var0;
         }
      } else {
         return var0;
      }
   }

   public static double H() {
      if (a != null && a.isEnabled()) {
         ZoomModule var0 = a;
         if (var0 == null) {
            return 1.0;
         }

         var0.J();
         return var0.n.getValue() ? var0.b.U() : 1.0;
      } else {
         return 1.0;
      }
   }

   public static boolean f(double var0) {
      if (a != null && a.isEnabled()) {
         ZoomModule var2 = a;
         if (var2 == null) {
            return false;
         }

         var2.J();
         return var2.S.getValue() && var2.b.T(var0);
      } else {
         return false;
      }
   }

   @Override
   public String getStatus() {
      if (!this.isRuntimeActive()) {
         return "Idle";
      } else {
         return this.b.B() ? String.format(Locale.ROOT, "%.2fx", this.b.v()) : "Waiting for zoom key";
      }
   }
}
