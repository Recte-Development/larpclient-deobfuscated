package com.larpclient.module;

import com.larpclient.m1;
import com.larpclient.ty;
import java.awt.Color;
import java.util.List;
import java.util.Locale;

public final class EchoHandModule extends Module {
   private static EchoHandModule V;
   public final d6 N = this.addSetting(new d6("Mode", "History material", "Echo Trail", "Echo Trail", "Flame"));
   public final dh v = this.addSetting(new dh("Echo Trail", "Enable the echo trail material", true));
   public final d9 R = this.c("Strength", 0.75, 0.05, 1.5, 0.05, false);
   public final d9 J = this.c("Radius", 11.0, 2.0, 28.0, 0.5, false);
   public final d9 g = this.c("Trail", 0.84, 0.55, 0.96, 0.01, false);
   public final d9 p = this.c("Lag", 1.15, 0.0, 3.0, 0.05, false);
   public final dk h = this.addSetting(new dk("Color", "Echo color and opacity", -924394030));
   public final dh z = this.addSetting(new dh("Outside Only", "Keep echoes outside the current hand silhouette", true));
   public final d6 M = this.addSetting(new d6("Flame Color", "Flame color source", "Custom", "Custom", "Client", "Rainbow"));
   public final dk Z = this.addSetting(new dk("Flame Color Value", "Flame color and opacity", -419430401));
   public final d9 s = this.c("Flame Strength", 0.85, 0.0, 2.0, 0.05, true);
   public final d9 b = this.c("Rise Speed", 0.0, 0.0, 2.0, 0.05, true);
   public final d9 n = this.c("Wobble", 0.65, 0.0, 2.0, 0.05, true);
   public final d9 y = this.c("Length", 0.95, 0.1, 2.5, 0.05, true);
   public final d9 t = this.c("Flame Brightness", 0.9, 0.0, 2.0, 0.05, true);
   public final d9 Y = this.c("Width", 1.0, 0.1, 3.0, 0.05, true);
   public final d9 A = this.c("Fire", 1.0, 0.0, 3.0, 0.05, true);
   public final d9 W = this.c("Turbulence", 1.0, 0.0, 3.0, 0.05, true);
   public final dh T = this.addSetting(new dh("Only Items", "Require an item in either hand for the flame material", false));

   public EchoHandModule() {
      super("EchoHand", "Colored motion history around first-person hands and items", Category.RENDER, -1);
      V = this;
      this.v.H(() -> !this.d());

      for (df var2 : List.of(this.R, this.J, this.g, this.p, this.h, this.z)) {
         var2.H(() -> !this.d() && this.v.P());
      }

      this.M.H(this::d);
      this.Z.H(() -> this.d() && this.M.P().equals("Custom"));
      this.T.H(this::d);
      this.N.h(var0 -> m1.f());
      this.v.h(var0 -> m1.f());
   }

   private d9 c(String var1, double var2, double var4, double var6, final double var8, boolean var10) {
      var var11 = (<unrepresentable>)this.addSetting(new d9(var1, var1 + " of the history material", var2, var4, var6, var8) {
         @Override
         public String getValueAsString() {
            return String.format(Locale.ROOT, var8 >= 0.5 ? "%.1f" : "%.2f", this.P());
         }
      });
      var11.H(() -> this.d() == var10);
      return var11;
   }

   public boolean d() {
      return this.N.P().equals("Flame");
   }

   public List<df<?>> p() {
      return List.of(this.N, this.v, this.T);
   }

   public List<df<?>> P() {
      return List.of(this.R, this.h, this.z, this.M, this.Z, this.s, this.t, this.y, this.Y);
   }

   public List<df<?>> W() {
      return List.of(this.J, this.g, this.p, this.b, this.n, this.A, this.W);
   }

   public static EchoHandModule v() {
      return V;
   }

   public int U() {
      if (!this.d()) {
         return this.h.P();
      }

      return switch ((String)this.M.P()) {
         case "Client" -> -436207616 | com.larpclient.f.C & 16777215;
         case "Rainbow" -> -436207616 | Color.HSBtoRGB((float)(ty.K() / 4.0 % 1.0), 0.72F, 1.0F) & 16777215;
         default -> this.Z.P();
      };
   }

   @Override
   public void onDisable() {
      m1.f();
   }

   @Override
   public void onTick() {
      m1.M();
   }
}
