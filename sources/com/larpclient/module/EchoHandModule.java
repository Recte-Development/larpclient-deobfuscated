package com.larpclient.module;

import com.larpclient.ea;
import com.larpclient.rv;
import java.awt.Color;
import java.util.List;
import java.util.Locale;

public final class EchoHandModule extends Module {
   private static EchoHandModule M;
   public final lp j = this.addSetting(new lp("Mode", "History material", "Echo Trail", "Echo Trail", "Flame"));
   public final ln e = this.addSetting(new ln("Echo Trail", "Enable the echo trail material", true));
   public final lc S = this.k("Strength", 0.75, 0.05, 1.5, 0.05, false);
   public final lc d = this.k("Radius", 11.0, 2.0, 28.0, 0.5, false);
   public final lc D = this.k("Trail", 0.84, 0.55, 0.96, 0.01, false);
   public final lc z = this.k("Lag", 1.15, 0.0, 3.0, 0.05, false);
   public final lv w = this.addSetting(new lv("Color", "Echo color and opacity", -924394030));
   public final ln J = this.addSetting(new ln("Outside Only", "Keep echoes outside the current hand silhouette", true));
   public final lp W = this.addSetting(new lp("Flame Color", "Flame color source", "Custom", "Custom", "Client", "Rainbow"));
   public final lv R = this.addSetting(new lv("Flame Color Value", "Flame color and opacity", -419430401));
   public final lc O = this.k("Flame Strength", 0.85, 0.0, 2.0, 0.05, true);
   public final lc P = this.k("Rise Speed", 0.0, 0.0, 2.0, 0.05, true);
   public final lc f = this.k("Wobble", 0.65, 0.0, 2.0, 0.05, true);
   public final lc F = this.k("Length", 0.95, 0.1, 2.5, 0.05, true);
   public final lc A = this.k("Flame Brightness", 0.9, 0.0, 2.0, 0.05, true);
   public final lc i = this.k("Width", 1.0, 0.1, 3.0, 0.05, true);
   public final lc Y = this.k("Fire", 1.0, 0.0, 3.0, 0.05, true);
   public final lc H = this.k("Turbulence", 1.0, 0.0, 3.0, 0.05, true);
   public final ln E = this.addSetting(new ln("Only Items", "Require an item in either hand for the flame material", false));

   public EchoHandModule() {
      super("EchoHand", "Colored motion history around first-person hands and items", Category.RENDER, -1);
      M = this;
      this.e.x(() -> !this.U());

      for (l6 var2 : List.of(this.S, this.d, this.D, this.z, this.w, this.J)) {
         var2.x(() -> !this.U() && this.e.getValue());
      }

      this.W.x(this::U);
      this.R.x(() -> this.U() && this.W.getValue().equals("Custom"));
      this.E.x(this::U);
      this.j.W(var0 -> com.larpclient.ly.d());
      this.e.W(var0 -> com.larpclient.ly.d());
   }

   private lc k(String var1, double var2, double var4, double var6, final double var8, boolean var10) {
      var var11 = (<unrepresentable>)this.addSetting(new lc(var1, var1 + " of the history material", var2, var4, var6, var8) {
         @Override
         public String getValueAsString() {
            return String.format(Locale.ROOT, var8 >= 0.5 ? "%.1f" : "%.2f", this.getValue());
         }
      });
      var11.x(() -> this.U() == var10);
      return var11;
   }

   public boolean U() {
      return this.j.getValue().equals("Flame");
   }

   public List<l6<?>> S() {
      return List.of(this.j, this.e, this.E);
   }

   public List<l6<?>> c() {
      return List.of(this.S, this.w, this.J, this.W, this.R, this.O, this.A, this.F, this.i);
   }

   public List<l6<?>> G() {
      return List.of(this.d, this.D, this.z, this.P, this.f, this.Y, this.H);
   }

   public static EchoHandModule p() {
      return M;
   }

   public int f() {
      if (!this.U()) {
         return this.w.getValue();
      }

      return switch ((String)this.W.getValue()) {
         case "Client" -> -436207616 | rv.e & 16777215;
         case "Rainbow" -> -436207616 | Color.HSBtoRGB((float)(ea.y() / 4.0 % 1.0), 0.72F, 1.0F) & 16777215;
         default -> this.R.getValue();
      };
   }

   @Override
   public void onDisable() {
      com.larpclient.ly.d();
   }

   @Override
   public void onTick() {
      com.larpclient.ly.W();
   }
}
