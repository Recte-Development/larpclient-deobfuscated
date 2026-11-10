package com.larpclient.module;

import com.larpclient.cf;
import com.larpclient.ho;
import com.larpclient.ly;
import java.awt.Color;
import java.util.List;
import java.util.Locale;

public final class EchoHandModule extends Module {
   private static EchoHandModule E;
   public final l l = this.addSetting(new l("Mode", "History material", "Echo Trail", "Echo Trail", "Flame"));
   public final s q = this.addSetting(new s("Echo Trail", "Enable the echo trail material", true));
   public final e X = this.y("Strength", 0.75, 0.05, 1.5, 0.05, false);
   public final e Q = this.y("Radius", 11.0, 2.0, 28.0, 0.5, false);
   public final e a = this.y("Trail", 0.84, 0.55, 0.96, 0.01, false);
   public final e o = this.y("Lag", 1.15, 0.0, 3.0, 0.05, false);
   public final i e = this.addSetting(new i("Color", "Echo color and opacity", -924394030));
   public final s C = this.addSetting(new s("Outside Only", "Keep echoes outside the current hand silhouette", true));
   public final l D = this.addSetting(new l("Flame Color", "Flame color source", "Custom", "Custom", "Client", "Rainbow"));
   public final i S = this.addSetting(new i("Flame Color Value", "Flame color and opacity", -419430401));
   public final e W = this.y("Flame Strength", 0.85, 0.0, 2.0, 0.05, true);
   public final e r = this.y("Rise Speed", 0.0, 0.0, 2.0, 0.05, true);
   public final e t = this.y("Wobble", 0.65, 0.0, 2.0, 0.05, true);
   public final e j = this.y("Length", 0.95, 0.1, 2.5, 0.05, true);
   public final e F = this.y("Flame Brightness", 0.9, 0.0, 2.0, 0.05, true);
   public final e d = this.y("Width", 1.0, 0.1, 3.0, 0.05, true);
   public final e B = this.y("Fire", 1.0, 0.0, 3.0, 0.05, true);
   public final e g = this.y("Turbulence", 1.0, 0.0, 3.0, 0.05, true);
   public final s T = this.addSetting(new s("Only Items", "Require an item in either hand for the flame material", false));

   public EchoHandModule() {
      super("EchoHand", "Colored motion history around first-person hands and items", Category.RENDER, -1);
      E = this;
      this.q.P(() -> !this.s());

      for (q var2 : List.of(this.X, this.Q, this.a, this.o, this.e, this.C)) {
         var2.P(() -> !this.s() && this.q.getValue());
      }

      this.D.P(this::s);
      this.S.P(() -> this.s() && this.D.getValue().equals("Custom"));
      this.T.P(this::s);
      this.l.o(var0 -> ly.a());
      this.q.o(var0 -> ly.a());
   }

   private e y(String var1, double var2, double var4, double var6, final double var8, boolean var10) {
      var var11 = (<unrepresentable>)this.addSetting(new e(var1, var1 + " of the history material", var2, var4, var6, var8) {
         @Override
         public String getValueAsString() {
            return String.format(Locale.ROOT, var8 >= 0.5 ? "%.1f" : "%.2f", this.getValue());
         }
      });
      var11.P(() -> this.s() == var10);
      return var11;
   }

   public boolean s() {
      return this.l.getValue().equals("Flame");
   }

   public List<q<?>> m() {
      return List.of(this.l, this.q, this.T);
   }

   public List<q<?>> y() {
      return List.of(this.X, this.e, this.C, this.D, this.S, this.W, this.F, this.j, this.d);
   }

   public List<q<?>> j() {
      return List.of(this.Q, this.a, this.o, this.r, this.t, this.B, this.g);
   }

   public static EchoHandModule e() {
      return E;
   }

   public int c() {
      if (!this.s()) {
         return this.e.getValue();
      }

      return switch ((String)this.D.getValue()) {
         case "Client" -> -436207616 | ho.J & 16777215;
         case "Rainbow" -> -436207616 | Color.HSBtoRGB((float)(cf.F() / 4.0 % 1.0), 0.72F, 1.0F) & 16777215;
         default -> this.S.getValue();
      };
   }

   @Override
   public void onDisable() {
      ly.a();
   }

   @Override
   public void onTick() {
      ly.H();
   }
}
