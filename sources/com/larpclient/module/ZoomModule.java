package com.larpclient.module;

import com.larpclient.LarpClient;
import com.larpclient.pe;
import com.larpclient.pk;
import com.larpclient.r4;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import net.minecraft.client.Minecraft;

public final class ZoomModule extends Module {
   private static ZoomModule d;
   public final la I = this.addSetting(new la("Zoom Key", "Key used while this module is enabled", 67));
   public final lp r = this.addSetting(new lp("Activation", "Hold the zoom key or press it to toggle the zoom", "Hold", "Hold", "Toggle"));
   public final lc h = this.addSetting(new lc("Magnification", "Optical magnification before scroll adjustment", 4.0, 1.25, 16.0, 0.25));
   public final ln z = this.addSetting(new ln("Scroll Zoom", "Adjust magnification with the mouse wheel while zoomed", true));
   public final ln H = this.addSetting(new ln("Remember Magnification", "Keep the last wheel adjustment for the next zoom", false));
   public final ln O = this.addSetting(new ln("Reduce Sensitivity", "Scale mouse motion with optical magnification", true));
   public final ln x = this.addSetting(new ln("Smooth Transition", "Ease into and out of zoom without delaying input", true));
   public final lc v = this.addSetting(new lc("Transition Time", "Seconds to reach approximately 99 percent of a new zoom", 0.15, 0.0, 0.5, 0.01));
   private final pk p = new pk();
   private Object a;
   private Object D;
   private boolean j;

   public ZoomModule() {
      super("Zoom", "Hold a key to zoom smoothly, with wheel adjustment and precise mouse control", Category.RENDER, -1);
      d = this;
      this.v.x(this.x::getValue);
      this.H.x(this.z::getValue);
      this.I.W(var1 -> this.Y());
      this.r.W(var1 -> this.Y());
      this.h.W(var1 -> this.Y());
   }

   public static ZoomModule l() {
      return d;
   }

   public List<l6<?>> x() {
      return List.of(this.I, this.r, this.z, this.H);
   }

   public List<l6<?>> m() {
      return List.of(this.h, this.O, this.x, this.v);
   }

   @Override
   public Set<ld> getNetworkImpacts() {
      return Set.of(ld.k);
   }

   @Override
   public void onEnable() {
      this.Y();
      r4.g("Zoom", "Ready; activation={}, key={}", this.r.getValue(), this.I.getValue());
   }

   @Override
   public void onDisable() {
      this.Y();
   }

   @Override
   public void onTick() {
      this.n();
   }

   private void Y() {
      this.p.x(true);
      this.a = this.D = null;
      this.j = (boolean)0;
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

   private void n() {
      Minecraft var1 = getMc();
      if (!this.k()) {
         this.Y();
      } else {
         if (this.a != var1.player || this.D != var1.level) {
            this.p.x(true);
            this.a = var1.player;
            this.D = var1.level;
         }

         boolean var2 = pe.A(var1.getWindow().handle(), this.I.getValue());
         this.p.W(true, var2, this.r.i("Hold"), this.h.getValue(), this.H.getValue());
         if (this.j != this.p.F()) {
            this.j = this.p.F();
            r4.g("Zoom", "{}; magnification={}", this.j ? "Engaged" : "Released", this.p.y());
         }

         boolean var3 = this.x.getValue();
         LarpClient var4 = LarpClient.y();
         if (var4 != null && var4.v() != null) {
            var3 &= var4.v().E().o;
         }

         this.p.i(System.nanoTime(), this.v.getValue(), var3);
      }
   }

   public static float u(float var0, boolean var1) {
      if (d != null && d.isEnabled()) {
         ZoomModule var2 = d;
         if (var2 != null && !var1) {
            var2.n();
            return pk.e(var0, var2.p.D());
         } else {
            return var0;
         }
      } else {
         return var0;
      }
   }

   public static double d() {
      if (d != null && d.isEnabled()) {
         ZoomModule var0 = d;
         if (var0 == null) {
            return 1.0;
         }

         var0.n();
         return var0.O.getValue() ? var0.p.D() : 1.0;
      } else {
         return 1.0;
      }
   }

   public static boolean D(double var0) {
      if (d != null && d.isEnabled()) {
         ZoomModule var2 = d;
         if (var2 == null) {
            return false;
         }

         var2.n();
         return var2.z.getValue() && var2.p.c(var0);
      } else {
         return false;
      }
   }

   @Override
   public String getStatus() {
      if (!this.isRuntimeActive()) {
         return "Idle";
      } else {
         return this.p.F() ? String.format(Locale.ROOT, "%.2fx", this.p.y()) : "Waiting for zoom key";
      }
   }
}
