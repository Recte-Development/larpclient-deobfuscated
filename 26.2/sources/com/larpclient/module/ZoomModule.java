package com.larpclient.module;

import com.larpclient.LarpClient;
import com.larpclient.bc;
import com.larpclient.tn;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import net.minecraft.client.Minecraft;

public final class ZoomModule extends Module {
   private static ZoomModule A;
   public final v c = this.addSetting(new v("Zoom Key", "Key used while this module is enabled", 67));
   public final l I = this.addSetting(new l("Activation", "Hold the zoom key or press it to toggle the zoom", "Hold", "Hold", "Toggle"));
   public final e h = this.addSetting(new e("Magnification", "Optical magnification before scroll adjustment", 4.0, 1.25, 16.0, 0.25));
   public final s P = this.addSetting(new s("Scroll Zoom", "Adjust magnification with the mouse wheel while zoomed", true));
   public final s s = this.addSetting(new s("Remember Magnification", "Keep the last wheel adjustment for the next zoom", false));
   public final s e = this.addSetting(new s("Reduce Sensitivity", "Scale mouse motion with optical magnification", true));
   public final s G = this.addSetting(new s("Smooth Transition", "Ease into and out of zoom without delaying input", true));
   public final e L = this.addSetting(new e("Transition Time", "Seconds to reach approximately 99 percent of a new zoom", 0.15, 0.0, 0.5, 0.01));
   private final tn M = new tn();
   private Object z;
   private Object J;
   private boolean C;

   public ZoomModule() {
      super("Zoom", "Hold a key to zoom smoothly, with wheel adjustment and precise mouse control", Category.RENDER, -1);
      A = this;
      this.L.P(this.G::getValue);
      this.s.P(this.P::getValue);
      this.c.o(var1 -> this.z());
      this.I.o(var1 -> this.z());
      this.h.o(var1 -> this.z());
   }

   public static ZoomModule S() {
      return A;
   }

   public List<q<?>> Q() {
      return List.of(this.c, this.I, this.P, this.s);
   }

   public List<q<?>> r() {
      return List.of(this.h, this.e, this.G, this.L);
   }

   @Override
   public Set<m> getNetworkImpacts() {
      return Set.of(m.k);
   }

   @Override
   public void onEnable() {
      this.z();
      com.larpclient.h.U("Zoom", "Ready; activation={}, key={}", this.I.getValue(), this.c.getValue());
   }

   @Override
   public void onDisable() {
      this.z();
   }

   @Override
   public void onTick() {
      this.G();
   }

   private void z() {
      this.M.s(true);
      this.z = this.J = null;
      this.C = (boolean)0;
   }

   private boolean p() {
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

   private void G() {
      Minecraft var1 = getMc();
      if (!this.p()) {
         this.z();
      } else {
         if (this.z != var1.player || this.J != var1.level) {
            this.M.s(true);
            this.z = var1.player;
            this.J = var1.level;
         }

         boolean var2 = bc.I(var1.getWindow().handle(), this.c.getValue());
         this.M.M(true, var2, this.I.O("Hold"), this.h.getValue(), this.s.getValue());
         if (this.C != this.M.H()) {
            this.C = this.M.H();
            com.larpclient.h.U("Zoom", "{}; magnification={}", this.C ? "Engaged" : "Released", this.M.n());
         }

         boolean var3 = this.G.getValue();
         LarpClient var4 = LarpClient.R();
         if (var4 != null && var4.o() != null) {
            var3 &= var4.o().g().L;
         }

         this.M.w(System.nanoTime(), this.L.getValue(), var3);
      }
   }

   public static float s(float var0, boolean var1) {
      if (A != null && A.isEnabled()) {
         ZoomModule var2 = A;
         if (var2 != null && !var1) {
            var2.G();
            return tn.A(var0, var2.M.I());
         } else {
            return var0;
         }
      } else {
         return var0;
      }
   }

   public static double u() {
      if (A != null && A.isEnabled()) {
         ZoomModule var0 = A;
         if (var0 == null) {
            return 1.0;
         }

         var0.G();
         return var0.e.getValue() ? var0.M.I() : 1.0;
      } else {
         return 1.0;
      }
   }

   public static boolean W(double var0) {
      if (A != null && A.isEnabled()) {
         ZoomModule var2 = A;
         if (var2 == null) {
            return false;
         }

         var2.G();
         return var2.P.getValue() && var2.M.E(var0);
      } else {
         return false;
      }
   }

   @Override
   public String getStatus() {
      if (!this.isRuntimeActive()) {
         return "Idle";
      } else {
         return this.M.H() ? String.format(Locale.ROOT, "%.2fx", this.M.n()) : "Waiting for zoom key";
      }
   }
}
