package com.larpclient.module;

import com.larpclient.hz;
import com.larpclient.pm;
import java.awt.Color;
import java.util.List;
import java.util.Locale;

public final class EchoHandModule extends Module {
   private static EchoHandModule u;
   public final o r = this.addSetting(new o("Mode", "History material", "Echo Trail", "Echo Trail", "Flame"));
   public final m w = this.addSetting(new m("Echo Trail", "Enable the echo trail material", true));
   public final b M = this.O("Strength", 0.75, 0.05, 1.5, 0.05, false);
   public final b S = this.O("Radius", 11.0, 2.0, 28.0, 0.5, false);
   public final b c = this.O("Trail", 0.84, 0.55, 0.96, 0.01, false);
   public final b T = this.O("Lag", 1.15, 0.0, 3.0, 0.05, false);
   public final h V = this.addSetting(new h("Color", "Echo color and opacity", -924394030));
   public final m R = this.addSetting(new m("Outside Only", "Keep echoes outside the current hand silhouette", true));
   public final o P = this.addSetting(new o("Flame Color", "Flame color source", "Custom", "Custom", "Client", "Rainbow"));
   public final h g = this.addSetting(new h("Flame Color Value", "Flame color and opacity", -419430401));
   public final b K = this.O("Flame Strength", 0.85, 0.0, 2.0, 0.05, true);
   public final b O = this.O("Rise Speed", 0.0, 0.0, 2.0, 0.05, true);
   public final b a = this.O("Wobble", 0.65, 0.0, 2.0, 0.05, true);
   public final b F = this.O("Length", 0.95, 0.1, 2.5, 0.05, true);
   public final b G = this.O("Flame Brightness", 0.9, 0.0, 2.0, 0.05, true);
   public final b s = this.O("Width", 1.0, 0.1, 3.0, 0.05, true);
   public final b j = this.O("Fire", 1.0, 0.0, 3.0, 0.05, true);
   public final b Z = this.O("Turbulence", 1.0, 0.0, 3.0, 0.05, true);
   public final m N = this.addSetting(new m("Only Items", "Require an item in either hand for the flame material", false));

   public EchoHandModule() {
      super("EchoHand", "Colored motion history around first-person hands and items", Category.RENDER, -1);
      u = this;
      this.w.a(() -> !this.S());

      for (r var2 : List.of(this.M, this.S, this.c, this.T, this.V, this.R)) {
         var2.a(() -> !this.S() && this.w.getValue());
      }

      this.P.a(this::S);
      this.g.a(() -> this.S() && this.P.getValue().equals("Custom"));
      this.N.a(this::S);
      this.r.w(var0 -> pm.C());
      this.w.w(var0 -> pm.C());
   }

   private b O(String var1, double var2, double var4, double var6, final double var8, boolean var10) {
      var var11 = (<unrepresentable>)this.addSetting(new b(var1, var1 + " of the history material", var2, var4, var6, var8) {
         @Override
         public String getValueAsString() {
            return String.format(Locale.ROOT, var8 >= 0.5 ? "%.1f" : "%.2f", this.getValue());
         }
      });
      var11.a(() -> this.S() == var10);
      return var11;
   }

   public boolean S() {
      return this.r.getValue().equals("Flame");
   }

   public List<r<?>> L() {
      return List.of(this.r, this.w, this.N);
   }

   public List<r<?>> y() {
      return List.of(this.M, this.V, this.R, this.P, this.g, this.K, this.G, this.F, this.s);
   }

   public List<r<?>> O() {
      return List.of(this.S, this.c, this.T, this.O, this.a, this.j, this.Z);
   }

   public static EchoHandModule e() {
      return u;
   }

   public int B() {
      if (!this.S()) {
         return this.V.getValue();
      }

      return switch ((String)this.P.getValue()) {
         case "Client" -> -436207616 | com.larpclient.c.y & 16777215;
         case "Rainbow" -> -436207616 | Color.HSBtoRGB((float)(hz.Q() / 4.0 % 1.0), 0.72F, 1.0F) & 16777215;
         default -> this.g.getValue();
      };
   }

   @Override
   public void onDisable() {
      pm.C();
   }

   @Override
   public void onTick() {
      pm.j();
   }
}
