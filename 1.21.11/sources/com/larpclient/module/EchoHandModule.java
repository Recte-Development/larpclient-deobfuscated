package com.larpclient.module;

import com.larpclient.ez;
import com.larpclient.jf;
import com.larpclient.qo;
import java.awt.Color;
import java.util.List;
import java.util.Locale;

public final class EchoHandModule extends Module {
   private static EchoHandModule H;
   public final li e = this.addSetting(new li("Mode", "History material", "Echo Trail", "Echo Trail", "Flame"));
   public final l3 t = this.addSetting(new l3("Echo Trail", "Enable the echo trail material", true));
   public final lo S = this.U("Strength", 0.75, 0.05, 1.5, 0.05, false);
   public final lo Q = this.U("Radius", 11.0, 2.0, 28.0, 0.5, false);
   public final lo m = this.U("Trail", 0.84, 0.55, 0.96, 0.01, false);
   public final lo b = this.U("Lag", 1.15, 0.0, 3.0, 0.05, false);
   public final l0 h = this.addSetting(new l0("Color", "Echo color and opacity", -924394030));
   public final l3 A = this.addSetting(new l3("Outside Only", "Keep echoes outside the current hand silhouette", true));
   public final li E = this.addSetting(new li("Flame Color", "Flame color source", "Custom", "Custom", "Client", "Rainbow"));
   public final l0 L = this.addSetting(new l0("Flame Color Value", "Flame color and opacity", -419430401));
   public final lo D = this.U("Flame Strength", 0.85, 0.0, 2.0, 0.05, true);
   public final lo U = this.U("Rise Speed", 0.0, 0.0, 2.0, 0.05, true);
   public final lo n = this.U("Wobble", 0.65, 0.0, 2.0, 0.05, true);
   public final lo T = this.U("Length", 0.95, 0.1, 2.5, 0.05, true);
   public final lo G = this.U("Flame Brightness", 0.9, 0.0, 2.0, 0.05, true);
   public final lo K = this.U("Width", 1.0, 0.1, 3.0, 0.05, true);
   public final lo f = this.U("Fire", 1.0, 0.0, 3.0, 0.05, true);
   public final lo r = this.U("Turbulence", 1.0, 0.0, 3.0, 0.05, true);
   public final l3 a = this.addSetting(new l3("Only Items", "Require an item in either hand for the flame material", false));

   public EchoHandModule() {
      super("EchoHand", "Colored motion history around first-person hands and items", Category.RENDER, -1);
      H = this;
      this.t.O(() -> !this.v());

      for (lb var2 : List.of(this.S, this.Q, this.m, this.b, this.h, this.A)) {
         var2.O(() -> !this.v() && this.t.getValue());
      }

      this.E.O(this::v);
      this.L.O(() -> this.v() && this.E.getValue().equals("Custom"));
      this.a.O(this::v);
      this.e.a(var0 -> jf.o());
      this.t.a(var0 -> jf.o());
   }

   private lo U(String var1, double var2, double var4, double var6, final double var8, boolean var10) {
      var var11 = (<unrepresentable>)this.addSetting(new lo(var1, var1 + " of the history material", var2, var4, var6, var8) {
         @Override
         public String getValueAsString() {
            return String.format(Locale.ROOT, var8 >= 0.5 ? "%.1f" : "%.2f", this.getValue());
         }
      });
      var11.O(() -> this.v() == var10);
      return var11;
   }

   public boolean v() {
      return this.e.getValue().equals("Flame");
   }

   public List<lb<?>> w() {
      return List.of(this.e, this.t, this.a);
   }

   public List<lb<?>> U() {
      return List.of(this.S, this.h, this.A, this.E, this.L, this.D, this.G, this.T, this.K);
   }

   public List<lb<?>> F() {
      return List.of(this.Q, this.m, this.b, this.U, this.n, this.f, this.r);
   }

   public static EchoHandModule p() {
      return H;
   }

   public int c() {
      if (!this.v()) {
         return this.h.getValue();
      }

      return switch ((String)this.E.getValue()) {
         case "Client" -> -436207616 | ez.a & 16777215;
         case "Rainbow" -> -436207616 | Color.HSBtoRGB((float)(qo.x() / 4.0 % 1.0), 0.72F, 1.0F) & 16777215;
         default -> this.L.getValue();
      };
   }

   @Override
   public void onDisable() {
      jf.o();
   }

   @Override
   public void onTick() {
      jf.l();
   }
}
