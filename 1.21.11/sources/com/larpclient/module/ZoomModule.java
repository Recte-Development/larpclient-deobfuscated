package com.larpclient.module;

import com.larpclient.LarpClient;
import com.larpclient.q8;
import com.larpclient.s5;
import com.larpclient.w2;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import net.minecraft.class_310;

public final class ZoomModule extends Module {
   private static ZoomModule l;
   public final lw N = this.addSetting(new lw("Zoom Key", "Key used while this module is enabled", 67));
   public final li n = this.addSetting(new li("Activation", "Hold the zoom key or press it to toggle the zoom", "Hold", "Hold", "Toggle"));
   public final lo t = this.addSetting(new lo("Magnification", "Optical magnification before scroll adjustment", 4.0, 1.25, 16.0, 0.25));
   public final l3 a = this.addSetting(new l3("Scroll Zoom", "Adjust magnification with the mouse wheel while zoomed", true));
   public final l3 H = this.addSetting(new l3("Remember Magnification", "Keep the last wheel adjustment for the next zoom", false));
   public final l3 E = this.addSetting(new l3("Reduce Sensitivity", "Scale mouse motion with optical magnification", true));
   public final l3 G = this.addSetting(new l3("Smooth Transition", "Ease into and out of zoom without delaying input", true));
   public final lo v = this.addSetting(new lo("Transition Time", "Seconds to reach approximately 99 percent of a new zoom", 0.15, 0.0, 0.5, 0.01));
   private final w2 o = new w2();
   private Object z;
   private Object Z;
   private boolean O;

   public ZoomModule() {
      super("Zoom", "Hold a key to zoom smoothly, with wheel adjustment and precise mouse control", Category.RENDER, -1);
      l = this;
      this.v.O(this.G::getValue);
      this.H.O(this.a::getValue);
      this.N.a(var1 -> this.h());
      this.n.a(var1 -> this.h());
      this.t.a(var1 -> this.h());
   }

   public static ZoomModule g() {
      return l;
   }

   public List<lb<?>> B() {
      return List.of(this.N, this.n, this.a, this.H);
   }

   public List<lb<?>> r() {
      return List.of(this.t, this.E, this.G, this.v);
   }

   @Override
   public Set<l5> getNetworkImpacts() {
      return Set.of(l5.E);
   }

   @Override
   public void onEnable() {
      this.h();
      q8.v("Zoom", "Ready; activation={}, key={}", this.n.getValue(), this.N.getValue());
   }

   @Override
   public void onDisable() {
      this.h();
   }

   @Override
   public void onTick() {
      this.W();
   }

   private void h() {
      this.o.V(true);
      this.z = this.Z = null;
      this.O = (boolean)0;
   }

   private boolean Q() {
      class_310 var1 = getMc();
      return this.isRuntimeActive()
         && var1 != null
         && var1.field_1724 != null
         && var1.field_1687 != null
         && var1.field_1724.method_5805()
         && !var1.field_1724.method_6113()
         && !var1.field_1724.method_31550()
         && var1.field_1755 == null
         && var1.method_18506() == null
         && var1.method_1569();
   }

   private void W() {
      class_310 var1 = getMc();
      if (!this.Q()) {
         this.h();
      } else {
         if (this.z != var1.field_1724 || this.Z != var1.field_1687) {
            this.o.V(true);
            this.z = var1.field_1724;
            this.Z = var1.field_1687;
         }

         boolean var2 = s5.m(var1.method_22683().method_4490(), this.N.getValue());
         this.o.R(true, var2, this.n.G("Hold"), this.t.getValue(), this.H.getValue());
         if (this.O != this.o.X()) {
            this.O = this.o.X();
            q8.v("Zoom", "{}; magnification={}", this.O ? "Engaged" : "Released", this.o.O());
         }

         boolean var3 = this.G.getValue();
         LarpClient var4 = LarpClient.b();
         if (var4 != null && var4.e() != null) {
            var3 &= var4.e().F().k;
         }

         this.o.Q(System.nanoTime(), this.v.getValue(), var3);
      }
   }

   public static float e(float var0, boolean var1) {
      if (l != null && l.isEnabled()) {
         ZoomModule var2 = l;
         if (var2 != null && !var1) {
            var2.W();
            return w2.q(var0, var2.o.F());
         } else {
            return var0;
         }
      } else {
         return var0;
      }
   }

   public static double n() {
      if (l != null && l.isEnabled()) {
         ZoomModule var0 = l;
         if (var0 == null) {
            return 1.0;
         }

         var0.W();
         return var0.E.getValue() ? var0.o.F() : 1.0;
      } else {
         return 1.0;
      }
   }

   public static boolean D(double var0) {
      if (l != null && l.isEnabled()) {
         ZoomModule var2 = l;
         if (var2 == null) {
            return false;
         }

         var2.W();
         return var2.a.getValue() && var2.o.H(var0);
      } else {
         return false;
      }
   }

   @Override
   public String getStatus() {
      if (!this.isRuntimeActive()) {
         return "Idle";
      } else {
         return this.o.X() ? String.format(Locale.ROOT, "%.2fx", this.o.O()) : "Waiting for zoom key";
      }
   }
}
