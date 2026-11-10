package com.larpclient.module;

import com.larpclient.b0;
import com.larpclient.b9;
import com.larpclient.ez;
import com.larpclient.f1;
import com.larpclient.i3;
import com.larpclient.js;
import com.larpclient.q8;
import java.util.List;
import net.minecraft.class_12075;
import net.minecraft.class_2338;
import net.minecraft.class_243;
import net.minecraft.class_310;
import net.minecraft.class_638;
import net.minecraft.class_2874.class_12326;
import net.minecraft.class_2902.class_2903;

public final class CelestialWeatherModule extends Module implements com.larpclient.ll {
   private static CelestialWeatherModule L;
   public final li O = this.addSetting(
      new li("Style", "Local celestial atmosphere", "Meteor Shower", "Meteor Shower", "Stardust", "Astral Rain", "Glowing Snow")
   );
   public final lo l = this.g("Density", "Emission strength within the weather budget", 1.0, 0.1, 4.0, 0.1);
   public final lo d = this.g("Speed", "Flight and floating motion", 1.0, 0.0, 3.0, 0.1);
   public final lo B = this.g("Size", "Meteor cores, dust, droplets and snowflakes", 1.0, 0.4, 2.5, 0.1);
   public final l3 o = this.addSetting(new l3("Theme Colors", "Use the current theme palette", true));
   public final l0 s = this.addSetting(new l0("Color", "Celestial cores and dust", -2561281));
   public final l0 G = this.addSetting(new l0("Secondary Color", "Celestial tails and contact glow", -6059009));
   public final l3 z = this.addSetting(new l3("Bloom", "Use shared world glow", true));
   public final lo C = this.g("Radius", "World-space emission region in blocks", 28.0, 8.0, 48.0, 1.0);
   public final lo h = this.g("Wind Strength", "Coherent horizontal drift", 0.8, 0.0, 6.0, 0.1);
   public final l3 j = this.addSetting(new l3("Surface Impacts", "Selected nearby flights contact actual block surfaces", true));
   public final lo N = this.g("Render Distance", "Fade celestial weather beyond this distance", 80.0, 24.0, 128.0, 4.0);
   public final li R = this.addSetting(new li("Dimension Filter", "Choose where local weather appears", "Overworld", "Overworld", "All"));
   public final li X = this.addSetting(new li("Indoors", "Fade weather when the camera is sheltered", "Fade", "Fade", "Allow"));
   private final js.q k = this::B;
   private final js e = new js();
   private Object n;
   private Object A;
   private class_243 W;
   private int u;
   private boolean b;
   private boolean t;

   public CelestialWeatherModule() {
      super("Celestial Weather", "Meteor showers, stardust, luminous rain and glowing snow", Category.VISUALS, -1);
      L = this;
      this.s.O(() -> !this.o.getValue());
      this.G.O(() -> !this.o.getValue());
      this.j.O(() -> !this.O.G("Stardust") && !this.O.G("Glowing Snow"));
      this.X.O(() -> !this.O.G("Glowing Snow"));
      this.O.a(var1 -> this.clear());
      this.R.a(var1 -> this.clear());
   }

   private lo g(String var1, String var2, double var3, double var5, double var7, double var9) {
      return this.addSetting(new lo(var1, var2, var3, var5, var7, var9));
   }

   public List<lb<?>> T() {
      return List.of(this.O, this.l, this.d, this.B);
   }

   public List<lb<?>> l() {
      return List.of(this.o, this.s, this.G, this.z);
   }

   public List<lb<?>> t() {
      return List.of(this.C, this.h, this.j, this.N, this.R, this.X);
   }

   public static CelestialWeatherModule D() {
      return L;
   }

   public js u() {
      return this.e;
   }

   public js.a i() {
      js.s var1 = this.O.G("Stardust") ? js.s.K : (this.O.G("Astral Rain") ? js.s.i : (this.O.G("Glowing Snow") ? js.s.F : js.s.q));
      return new js.a(
         var1,
         this.l.getValue(),
         this.C.getValue(),
         this.d.getValue(),
         this.B.getValue(),
         this.h.getValue(),
         this.N.getValue(),
         this.o.getValue() ? ez.a : this.s.getValue(),
         this.o.getValue() ? ez.f : this.G.getValue(),
         this.z.getValue(),
         this.j.getValue(),
         b0.Y(256)
      );
   }

   @Override
   public String effectName() {
      return "Celestial Weather";
   }

   @Override
   public void tick(class_310 var1) {
      if (this.isEnabled()) {
         if (this.isRuntimeActive() && var1.field_1687 != null && var1.field_1724 != null && var1.field_1724.method_5805()) {
            this.t = var1.method_1493();
            if (!this.t) {
               class_243 var2 = var1.field_1773.method_19418().method_71156();
               if (this.n != var1.field_1687 || this.A != var1.field_1724 || this.W != null && this.W.method_1025(var2) > 4096.0) {
                  this.clear();
                  this.n = var1.field_1687;
                  this.A = var1.field_1724;
                  q8.v(
                     this.effectName(),
                     "World field initialized; style={}; max flights={}; max sprites={}; max terrain queries/tick={}",
                     this.O.getValue(),
                     256,
                     1024,
                     4
                  );
               }

               this.W = var2;
               if (this.R.G("Overworld") && var1.field_1687.method_8597().comp_5220() != class_12326.field_64386) {
                  this.e.a();
               } else {
                  if (this.u++ % 5 == 0) {
                     class_2338 var3 = class_2338.method_49638(var2);
                     int var4 = this.X.G("Allow") || this.O.G("Glowing Snow") || var1.field_1687.method_22340(var3) && var1.field_1687.method_8311(var3)
                        ? 1
                        : 0;
                     if (var4 != this.b) {
                        q8.v(this.effectName(), "Exposure changed: {}; doorway fade active", var4 != 0 ? "outdoors" : "sheltered");
                     }

                     this.b = (boolean)var4;
                  }

                  this.e.v(var2, this.i(), this.b, (var1x, var2x) -> i3.c(var1.field_1687, var1x, var2x), this.O.G("Glowing Snow") ? this.k : null);
               }
            }
         } else {
            this.clear();
         }
      }
   }

   private double B(int var1, int var2) {
      class_638 var3 = class_310.method_1551().field_1687;
      return var3 != null && var3.method_8393(var1 >> 4, var2 >> 4) ? var3.method_8624(class_2903.field_13197, var1, var2) : Double.NaN;
   }

   @Override
   public void prepare(float var1, class_12075 var2) {
      if (this.isEnabled()) {
         if (!this.isRuntimeActive()) {
            this.e.a();
         } else {
            this.e.J(var2.field_63078, this.t ? 1.0F : var1, b9.k());
         }
      }
   }

   @Override
   public void render(f1 var1, boolean var2) {
      if (this.isEnabled()) {
         this.e.q(var1, var2);
      }
   }

   @Override
   public boolean glowing() {
      return this.isEnabled() ? this.e.y() : false;
   }

   @Override
   public boolean soft() {
      return this.e.f();
   }

   @Override
   public void clear() {
      this.e.a();
      this.n = this.A = null;
      this.W = null;
      this.u = 0;
      this.b = this.t = (boolean)0;
   }

   @Override
   public void onEnable() {
      this.clear();
      q8.G(this.effectName(), "Enabled {}; isolated weather pool; terrain queries capped at {}/tick", this.O.getValue(), 4);
   }

   @Override
   public void onDisable() {
      this.clear();
   }
}
